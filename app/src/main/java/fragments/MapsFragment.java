package fragments;

import android.app.FragmentTransaction;
import android.location.Geocoder;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import android.location.Address;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;


import app.com.jobcatcherapp.R;
import main.JobCatcherApp;
import models.Job;
import requests.VolleyRequest;

public class MapsFragment extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {


    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;

    LatLng latLng;
    GoogleMap mMap;
    SupportMapFragment mFragment;
    Marker currLocationMarker;
    JobCatcherApp app;
    VolleyRequest request;
    Marker jobMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_maps);
        app = (JobCatcherApp) getApplication();
        getAllJobDetails();

        mFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mFragment.getMapAsync(this);
    }

    public void getAllJobDetails() {
        String url = "http://10.0.2.2:8080/getAllJobsList";
        request = new VolleyRequest();
        request.makeVolleyGetRequestForAllJobDetails(app, this, this.getApplicationContext(), url, false);
    }

    @Override
    public void onMapReady(GoogleMap gMap) {
        mMap = gMap;
        try {
            mMap.setMyLocationEnabled(true);
            app = (JobCatcherApp) getApplication();
            getAllJobDetails();
            addJobs(app.jobsList);
        } catch (SecurityException exception) {

        }

        buildGoogleApiClient();

        mGoogleApiClient.connect();

    }


    public void addJobs(List<Job> list) {
        for (Job j : list) {
            jobMarker = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(Double.parseDouble(j.latitude), Double.parseDouble(j.longitude)))
                    .title(j.jobName)
                    .snippet(j.jobDescription + " " + j.contactNumber)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.findajob)));

            jobMarker.setTag(j);
        }
    }

    private final int[] MAP_TYPES = {
            GoogleMap.MAP_TYPE_SATELLITE,
            GoogleMap.MAP_TYPE_NORMAL,
            GoogleMap.MAP_TYPE_HYBRID,
            GoogleMap.MAP_TYPE_TERRAIN,
            GoogleMap.MAP_TYPE_NONE
    };

    private int curMapTypeIndex = 1;

    protected synchronized void buildGoogleApiClient() {
        Toast.makeText(this, "buildGoogleApiClient", Toast.LENGTH_SHORT).show();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onConnected(Bundle bundle) {
        try {
            Toast.makeText(this, "onConnected", Toast.LENGTH_SHORT).show();
            addJobs(app.jobsList);
            Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (mLastLocation != null) {
                addJobs(app.jobsList);
            }
            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

                @Override
                public boolean onMarkerClick(Marker marker) {

                    Log.d("Message", "got here into marker click" + marker.getTag());
                    if(marker.getTag() != null ) {
                        String result = marker.getTag().toString();
                        if (result != null) {
                            String jobToken = result.substring(result.indexOf("jobToken ="), result.indexOf(", jobDescription"));
                            String token = jobToken.split("=")[1];

                            FragmentTransaction ft = getFragmentManager().beginTransaction();
                            String url = "http://10.0.2.2:8080/findEmployerAndJob";


                            request = new VolleyRequest();
                            request.makeVolleyGetRequestForEmployerAndJob(getApplicationContext(), url, token, ft, R.id.map);
                        }
                    }
                    return true;
                }

            });
            mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(5000); //5 seconds
            mLocationRequest.setFastestInterval(3000); //3 seconds
            mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

        } catch (SecurityException exception) {

        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this, "onConnectionSuspended", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(this, "onConnectionFailed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationChanged(Location location) {

        //place marker at current position
        mMap.clear();
        if (currLocationMarker != null) {
            currLocationMarker.remove();
        }
        latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        currLocationMarker = mMap.addMarker(markerOptions);
        addJobs(app.jobsList);

        Toast.makeText(this, "Location Changed", Toast.LENGTH_SHORT).show();

        //zoom to current position:
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11));

    }

    public void onMapSearch(View view) {
        EditText locationSearch = (EditText) findViewById(R.id.editText);
        String location = locationSearch.getText().toString();
        List<android.location.Address> addressList = null;

        if (location != null && !location.equals("")) {
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(location, 1);

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Invalid Input", Toast.LENGTH_SHORT).show();
            }
            Address address = addressList.get(0);
            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
            mMap.addMarker(new MarkerOptions().position(latLng).title("Marker"));
            addJobs(app.jobsList);
            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        }
    }


}


