package fragments;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.HashMap;
import java.util.Map;

import app.com.jobcatcherapp.R;
import requests.VolleyRequest;


/**
 * Created by annadowling on 11/05/2017.
 * Create the JobFragment view and attach all event handling to that fragment
 */
public class JobFragment extends Fragment implements View.OnClickListener {

    private OnFragmentInteractionListener mListener;
    EditText email, phone, jobTitle, jobDescription, jobLatitude, jobLongitude;
    Button addJob;

    public JobFragment() {
        // Required empty public constructor
    }

    /**
     * @return A new instance of fragment JobFragment.
     */
    public static JobFragment newInstance() {
        JobFragment fragment = new JobFragment();
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
        View view = inflater.inflate(R.layout.fragment_job, container, false);
        email = (EditText) view.findViewById(R.id.employerEmail);
        phone = (EditText) view.findViewById(R.id.phone);
        jobTitle = (EditText) view.findViewById(R.id.jobTitle);
        jobDescription = (EditText) view.findViewById(R.id.jobDescription);
        jobLatitude =  (EditText) view.findViewById(R.id.jobLatitude);
        jobLongitude =  (EditText) view.findViewById(R.id.jobLongitude);
        addJob = (Button) view.findViewById(R.id.addjob);

        addJob.setOnClickListener(this);

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
     * Add a job entry using Volley GET request
     */
    private void addJob() {
        final String emailTxt = email.getText().toString();
        final String phoneTxt = phone.getText().toString();
        final String jobTitleTxt = jobTitle.getText().toString();
        final String jobDescriptionTxt = jobDescription.getText().toString();
        final String jobLatitudeTxt = jobLatitude.getText().toString();
        final String jobLongitudeTxt = jobLongitude.getText().toString();

        String url = "http://10.0.2.2:8080/addJob";

        Map<String, String> params = new HashMap<String, String>();
        params.put("email", emailTxt);
        params.put("contactNumber", phoneTxt);
        params.put("jobTitle", jobTitleTxt);
        params.put("jobDescription", jobDescriptionTxt);
        params.put("latitude", jobLatitudeTxt);
        params.put("longitude", jobLongitudeTxt);

        VolleyRequest request = new VolleyRequest();
        request.makeVolleyPostRequest(getActivity().getApplicationContext(), params, url);
    }

    /**
     * Handles on click events
     * @param v
     */
    @Override
    public void onClick(View v) {
        if (v == addJob) {
            addJob();
        }
    }
}
