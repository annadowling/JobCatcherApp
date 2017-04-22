package fragments;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import app.com.jobcatcherapp.R;
import models.Job;
import requests.VolleyRequest;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EmployerJobListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EmployerJobListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EmployerJobListFragment extends Fragment implements View.OnClickListener {


    private OnFragmentInteractionListener mListener;

    ImageView delete;
    TextView jobName, jobDescription, contactNumber, hiddenValue;
    public static List<Job> employerJobsList;
    VolleyRequest request;
    SharedPreferences pref;

    public EmployerJobListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment EmployerJobListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EmployerJobListFragment newInstance(List<Job> jobsList) {
        EmployerJobListFragment fragment = new EmployerJobListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        employerJobsList = new ArrayList<Job>();
        employerJobsList.addAll(jobsList);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_employer_job_list_main, container, false);
        pref = getActivity().getSharedPreferences("AppPref", MODE_PRIVATE);
        initView(view);
        return view;
    }

    public void initView(View rootView) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        LinearLayout parentPanel = (LinearLayout) rootView.findViewById(R.id.fragmentEmployerJobListMain);
        for (Job job : employerJobsList) {
            View listView = inflater.inflate(R.layout.fragment_employer_job_list, null);
            jobName = (TextView) listView.findViewById(R.id.rowJobName);
            jobDescription = (TextView) listView.findViewById(R.id.rowJobDescription);
            contactNumber = (TextView) listView.findViewById(R.id.rowContactNumber);
            hiddenValue = (TextView) listView.findViewById(R.id.hidden_value);

            jobName.setText(job.jobName);
            jobDescription.setText(job.jobDescription);
            contactNumber.setText(job.contactNumber);
            hiddenValue.setText(job.jobToken);
            delete = (ImageView) listView.findViewById(R.id.imgDelete);
            delete.setOnClickListener(this);
            parentPanel.addView(listView);
        }

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

    public void deleteJob(){
        String url = "http://10.0.2.2:8080/deleteJob";
        String token = hiddenValue.getText().toString();
        String employerToken = pref.getString("token", "default");

        request = new VolleyRequest();
        request.makeVolleyDeleteRequest(getActivity().getApplicationContext(), token, employerToken, url);
        //refreshJobDetails();
    }

    public void refreshJobDetails() {
        String url = "http://10.0.2.2:8080/getEmployerJobsList";
        String token = pref.getString("token", "default");

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

        request = new VolleyRequest();
        request.makeVolleyGetRequestForEmployerJobDetails(getActivity().getApplicationContext(), url, token, fragmentTransaction, R.id.fragmentEmployerJobListMain);
    }

    @Override
    public void onClick(View v) {
        if (v == delete) {
            deleteJob();
        }
    }
}
