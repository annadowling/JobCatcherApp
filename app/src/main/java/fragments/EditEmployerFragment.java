package fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.HashMap;
import java.util.Map;

import app.com.jobcatcherapp.R;
import requests.VolleyRequest;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by annadowling on 11/05/2017.
 * Create the EditEmployerFragment view and attach all event handling to that fragment
 */
public class EditEmployerFragment extends Fragment implements View.OnClickListener {

    private OnFragmentInteractionListener mListener;

    EditText companyName, address, latitude, longitude, password;
    Button saveEdits;
    ImageView backToProfile;

    VolleyRequest request;
    SharedPreferences pref;

    public EditEmployerFragment() {
        // Required empty public constructor
    }

    /**
     * newInstance of EditEmployerFragment
     * @return
     */
    public static EditEmployerFragment newInstance() {
        EditEmployerFragment fragment = new EditEmployerFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Create and populate view data associated with the fragment.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_edit_employer, container, false);

        pref = getActivity().getSharedPreferences("AppPref", MODE_PRIVATE);
        // Inflate the layout for this fragment

        companyName = (EditText) view.findViewById(R.id.editCompanyName);
        password = (EditText) view.findViewById(R.id.editEmployerPassword);
        latitude = (EditText) view.findViewById(R.id.editLatitude);
        longitude = (EditText) view.findViewById(R.id.editLongitude);
        address = (EditText) view.findViewById(R.id.editAddress);

        saveEdits = (Button) view.findViewById(R.id.editEmployerBtn);
        saveEdits.setOnClickListener(this);

        backToProfile = (ImageView) view.findViewById(R.id.backToEmployerProfile);
        backToProfile.setOnClickListener(this);
        return view;
    }

    /**
     *
     * @param uri
     */
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    /**
     *
      * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    /**
     * Sve edits to the employer profile using Volley POST request
     */
    public void saveEditsToEmployerProfile() {
        String url = "http://10.0.2.2:8080/updateEmployer";
        String token = pref.getString("token", "default");

        Map<String, String> requestParameters = new HashMap<String, String>();
        requestParameters.put("companyName", companyName.getText().toString());
        requestParameters.put("address", address.getText().toString());
        requestParameters.put("password", password.getText().toString());
        requestParameters.put("latitude", latitude.getText().toString());
        requestParameters.put("longitude", longitude.getText().toString());

        String postUrl = url + "/token=" + token;

        request = new VolleyRequest();
        request.makeVolleyPostRequest(getActivity().getApplicationContext(), requestParameters, postUrl);
    }

    /**
     * Repopulates the employer profile data after profile edits using Volley GET request
     */
    public void backToEmployerProfile() {
        String url = "http://10.0.2.2:8080/getEmployerDetails";
        String token = pref.getString("token", "default");

        request = new VolleyRequest();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        request.makeVolleyGetRequestForEmployerDetails(getActivity().getApplicationContext(), url, token, ft, R.id.employerEditProfileFrame);
    }

    /**
     * Handles on click events
     * @param v
     */
    @Override
    public void onClick(View v) {
        if (v == saveEdits) {
            saveEditsToEmployerProfile();
        } else if (v == backToProfile) {
            backToEmployerProfile();
        }
    }
}
