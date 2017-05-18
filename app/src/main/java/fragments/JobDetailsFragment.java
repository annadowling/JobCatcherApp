package fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import app.com.jobcatcherapp.R;
import models.Employer;
import models.Job;

/**
 * Created by annadowling on 11/05/2017.
 * Create the JobDetailsFragment view and attach all event handling to that fragment
 */
public class JobDetailsFragment extends Fragment implements View.OnClickListener {


    private OnFragmentInteractionListener mListener;
    TextView email, phone, employerName, jobTitle, jobDescription;
    Button makeCall, sendEmail;
    public static String companyNameText;
    public static String emailText;
    public static String phoneText;
    public static String jobTitletext;
    public static String jobDescriptiontext;

    public JobDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * newInstance
     * @param job
     * @param employer
     * @return
     */
    public static JobDetailsFragment newInstance(Job job, Employer employer) {
        JobDetailsFragment fragment = new JobDetailsFragment();
        emailText = employer.employerEmail;
        companyNameText = employer.employerName;
        phoneText = employer.contactNumber;
        jobTitletext = job.jobName;
        jobDescriptiontext = job.jobDescription;

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

        View view = inflater.inflate(R.layout.fragment_job_details, container, false);
        email = (TextView) view.findViewById(R.id.employerEmail2);
        phone = (TextView) view.findViewById(R.id.phone2);
        employerName = (TextView) view.findViewById(R.id.employerName);
        jobTitle = (TextView) view.findViewById(R.id.jobTitle2);
        jobDescription = (TextView) view.findViewById(R.id.jobDescription2);
        sendEmail = (Button) view.findViewById(R.id.sendEmail2);
        makeCall = (Button) view.findViewById(R.id.dialNumber2);

        email.setText(emailText);
        phone.setText(phoneText);
        employerName.setText(companyNameText);
        jobTitle.setText(jobTitletext);
        jobDescription.setText(jobDescriptiontext);

        sendEmail.setOnClickListener(this);
        makeCall.setOnClickListener(this);
        // Inflate the layout for this fragment
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
     * Create the email intent and populate
     */
    protected void sendEmail() {
        Log.i("Send email", "");
        String[] CC = {""};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] { email.getText().toString() });
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, jobTitle.getText());
        emailIntent.putExtra(Intent.EXTRA_TEXT, "I wish to enquire about this job . . . .");

        try {
            startActivity(emailIntent);
            Log.i("Finished sending email", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getActivity().getApplicationContext(), "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * Create the call intent and populate
     */
    protected void makeCall() {
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.putExtra(Intent.EXTRA_PHONE_NUMBER, phone.getText());
        try {
            startActivity(callIntent);
            Log.i("Making call", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getActivity().getApplicationContext(), "There is call client installed.", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Handles onClick events
     * @param v
     */
    @Override
    public void onClick(View v) {
        if (v == sendEmail) {
            sendEmail();
        } else if (v == makeCall) {
            makeCall();
        }
    }
}
