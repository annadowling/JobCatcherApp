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
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Map;

import app.com.jobcatcherapp.R;
import main.JobCatcherApp;
import requests.VolleyRequest;

import static android.content.Context.MODE_PRIVATE;
import static app.com.jobcatcherapp.R.id.employerProfileFrame;

/**
 * Created by annadowling on 11/05/2017.
 * Create the EmployerProfileFragment view and attach all event handling to that fragment
 */
public class EmployerProfileFragment extends Fragment implements View.OnClickListener {


    private OnFragmentInteractionListener mListener;

    ImageView addJobIcon, editEmployerIcon;
    TextView latitude, longitude, address, email, companyname;
    Button manageJobs;
    public static String companyNameText;
    public static String emailText;
    public static String latitudeNameText;
    public static String longitudeText;
    public static String addressText;

    VolleyRequest request;
    SharedPreferences pref;
    public JobCatcherApp app = JobCatcherApp.getInstance();

    public EmployerProfileFragment() {
        // Required empty public constructor
    }

    /**
     *
     * @param mapEntries
     * @return
     */
    public static EmployerProfileFragment newInstance(Map<String, String> mapEntries) {
        EmployerProfileFragment fragment = new EmployerProfileFragment();
        companyNameText = mapEntries.get("companyName");
        emailText = mapEntries.get("email");
        latitudeNameText = mapEntries.get("latitude");
        longitudeText = mapEntries.get("longitude");
        addressText = mapEntries.get("address");

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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_employer_profile, container, false);

        pref = getActivity().getSharedPreferences("AppPref", MODE_PRIVATE);

        companyname = (TextView) view.findViewById(R.id.employer_profile_name);
        companyname.setText(companyNameText);

        email = (TextView) view.findViewById(R.id.employer_profile_email);
        email.setText(emailText);

        address = (TextView) view.findViewById(R.id.employer_address);
        address.setText(addressText);

        latitude = (TextView) view.findViewById(R.id.employer_lat);
        latitude.setText(latitudeNameText);

        longitude = (TextView) view.findViewById(R.id.employer_long);
        longitude.setText(longitudeText);

        addJobIcon = (ImageView) view.findViewById(R.id.add_job);
        addJobIcon.setOnClickListener(this);

        editEmployerIcon = (ImageView) view.findViewById(R.id.editEmployer);
        editEmployerIcon.setOnClickListener(this);

        manageJobs = (Button) view.findViewById(R.id.manageJobBtn);
        manageJobs.setOnClickListener(this);

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
     * Launch the JobFragment
     */
    public void launchAddJob() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();

        JobFragment jobFragment = JobFragment.newInstance();
        ft.replace(employerProfileFrame, jobFragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    /**
     * Launch the EditEmployerFragment
     */
    public void launchEditEmployerProfile() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();

        EditEmployerFragment editFragment = EditEmployerFragment.newInstance();
        ft.replace(employerProfileFrame, editFragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    /**
     * Launch the EmployerJobListFragment
     */
    public void getEmployerJobDetails() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        EmployerJobListFragment jobFragment = EmployerJobListFragment.newInstance();
        ft.replace(employerProfileFrame, jobFragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    /**
     *  Handles on click events
     * @param v
     */
    @Override
    public void onClick(View v) {
        if (v == addJobIcon) {
            launchAddJob();
        } else if (v == editEmployerIcon) {
            launchEditEmployerProfile();
        } else if (v == manageJobs) {
            getEmployerJobDetails();
        }
    }
}
