package app.com.jobcatcherapp.activities;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import app.com.jobcatcherapp.R;
import fragments.ContactFragment;
import fragments.EditUserFragment;
import fragments.EmployerPortalFragment;
import fragments.LoginFragment;
import fragments.MapsFragment;
import fragments.ProfileFragment;
import main.JobCatcherApp;
import requests.VolleyRequest;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    SharedPreferences pref;
    VolleyRequest request;

    public static TextView emailText, userNameText;
    ImageView profileimage;
    private static final int REQUEST_CODE_PICTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        pref = this.getSharedPreferences("AppPref", MODE_PRIVATE);
        String email = pref.getString("email", "default");
        String firstName = pref.getString("firstName", "default");
        String lastName = pref.getString("lastName", "default");
        String imageUrl = pref.getString("imagepath", "default");


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();

                ContactFragment contactFragment = ContactFragment.newInstance();
                ft.replace(R.id.homeFrame, contactFragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);

        emailText = (TextView) header.findViewById(R.id.navUserEmail);
        emailText.setText(email);
        userNameText = (TextView) header.findViewById(R.id.navUserName);
        userNameText.setText(firstName + " " + lastName);

        profileimage = (ImageView) header.findViewById(R.id.imageViewUser);
        if (imageUrl != null && imageUrl != "") {
            try {
                URL url = new URL(imageUrl);
                new DownloadImageTask(profileimage).execute(imageUrl);
            } catch (IOException e) {
            }
        }

        profileimage.setOnClickListener(this);


        FragmentTransaction ft = getFragmentManager().beginTransaction();

        ProfileFragment profileFragment = ProfileFragment.newInstance();
        ft.replace(R.id.homeFrame, profileFragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Base/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentTransaction ft = getFragmentManager().beginTransaction();

        if (id == R.id.nav_map) {
            Intent mapactivity = new Intent(MainActivity.this, MapsFragment.class);
            startActivity(mapactivity);
        } else if (id == R.id.contact_options) {

            ContactFragment contactFragment = ContactFragment.newInstance();
            ft.replace(R.id.homeFrame, contactFragment);
            ft.addToBackStack(null);
            ft.commit();
        } else if (id == R.id.employer_portal) {

            EmployerPortalFragment portalFragment = EmployerPortalFragment.newInstance();
            ft.replace(R.id.homeFrame, portalFragment);
            ft.addToBackStack(null);
            ft.commit();
        } else if (id == R.id.edit_user_profile) {
            EditUserFragment editUserFragment = EditUserFragment.newInstance();
            ft.replace(R.id.homeFrame, editUserFragment);
            ft.addToBackStack(null);
            ft.commit();
        } else if (id == R.id.search_jobs) {
            getAllJobDetails();
        } else if (id == R.id.logout_menu) {
            LoginFragment loginFragment = LoginFragment.newInstance();
            ft.replace(R.id.homeFrame, loginFragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void getAllJobDetails() {
        String url = "http://10.0.2.2:8080/getAllJobsList";
        request = new VolleyRequest();
        JobCatcherApp app = (JobCatcherApp) getApplication();
        request.makeVolleyGetRequestForAllJobDetails(app, MainActivity.this, this.getApplicationContext(), url, true);
    }

    /**
     * Click on View to change photo. Sets into View of your layout, android:onClick="clickOnPhoto"
     *
     * @param view View
     */
    public void clickOnPhoto(View view) {
        Intent pickIntent = new Intent();
        pickIntent.setType("image/*");
        pickIntent.setAction(Intent.ACTION_GET_CONTENT);
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String pickTitle = "Take or select a photo";
        Intent chooserIntent = Intent.createChooser(pickIntent, pickTitle);
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{takePhotoIntent});
        startActivityForResult(chooserIntent, REQUEST_CODE_PICTURE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICTURE && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                return;
            }
            try {
                InputStream inputStream = getContentResolver().openInputStream(data.getData());
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                profileimage.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v == profileimage) {
            clickOnPhoto(v);
        }
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

        private ProgressDialog mDialog;
        private ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected void onPreExecute() {

            mDialog = ProgressDialog.show(MainActivity.this,"Please wait...", "Retrieving data ...", true);
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", "image download error");
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            //set image of your imageview
            bmImage.setImageBitmap(result);
            //close
            mDialog.dismiss();
        }
    }
}


