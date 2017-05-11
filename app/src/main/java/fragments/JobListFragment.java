package fragments;

import android.app.ListFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import adapters.JobAdapter;
import adapters.JobFilter;
import app.com.jobcatcherapp.R;
import models.Job;
import requests.VolleyRequest;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link JobListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link JobListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class JobListFragment extends ListFragment {


    private OnFragmentInteractionListener mListener;
    ImageView delete;
    TextView jobName, jobDescription, contactNumber, hiddenValue;
    EditText filterText;
    public static List<Job> fullJobsList;
    VolleyRequest request;
    SharedPreferences pref;
    Button searchButton;

    protected JobFilter jobFilter;
    protected static JobAdapter jobAdapter;

    public JobListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment JobListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static JobListFragment newInstance(List<Job> jobsList) {
        JobListFragment fragment = new JobListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        fullJobsList = new ArrayList<Job>();
        fullJobsList.addAll(jobsList);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_job_list_main, container, false);
        pref = getActivity().getSharedPreferences("AppPref", MODE_PRIVATE);
        filterText = (EditText) view.findViewById(R.id.filterText);
        searchButton = (Button) view.findViewById(R.id.search_button);

        if (fullJobsList != null) {
            initView(view);
        }
        return view;
    }

    public void initView(View rootView) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        LinearLayout parentPanel = (LinearLayout) rootView.findViewById(R.id.fragmentJobListMain);
        for (Job job : fullJobsList) {
            View listView = inflater.inflate(R.layout.fragment_job_list, null);
            jobName = (TextView) listView.findViewById(R.id.rowJobName2);
            jobDescription = (TextView) listView.findViewById(R.id.rowJobDescription2);
            contactNumber = (TextView) listView.findViewById(R.id.rowContactNumber2);
            hiddenValue = (TextView) listView.findViewById(R.id.hidden_value2);

            jobName.setText(job.jobName);
            jobDescription.setText(job.jobDescription);
            contactNumber.setText(job.contactNumber);
            hiddenValue.setText(job.jobToken);
            delete = (ImageView) listView.findViewById(R.id.imgDelete2);
            parentPanel.addView(listView);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        jobAdapter = new JobAdapter(getActivity().getApplicationContext(), fullJobsList);
        jobFilter = new JobFilter(fullJobsList, "all", jobAdapter);

        setListAdapter(jobAdapter);
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
}
