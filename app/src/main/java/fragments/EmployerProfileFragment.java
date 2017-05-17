package fragments;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import app.com.jobcatcherapp.R;
import app.com.jobcatcherapp.activities.MainActivity;
import main.JobCatcherApp;
import models.Job;
import requests.VolleyRequest;

import static android.content.Context.MODE_PRIVATE;
import static app.com.jobcatcherapp.R.id.employerProfileFrame;
import static app.com.jobcatcherapp.R.id.homeFrameEmployer;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EmployerProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EmployerProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
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
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment EmployerProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

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

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void launchAddJob() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();

        JobFragment jobFragment = JobFragment.newInstance();
        ft.replace(employerProfileFrame, jobFragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    public void launchEditEmployerProfile() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();

        EditEmployerFragment editFragment = EditEmployerFragment.newInstance();
        ft.replace(employerProfileFrame, editFragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    public void getEmployerJobDetails() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        EmployerJobListFragment jobFragment = EmployerJobListFragment.newInstance();
        ft.replace(employerProfileFrame, jobFragment);
        ft.addToBackStack(null);
        ft.commit();
    }

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
