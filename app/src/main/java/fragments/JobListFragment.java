package fragments;

import android.app.ListFragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;

import adapters.JobAdapter;
import adapters.JobFilter;
import main.JobCatcherApp;
import requests.VolleyRequest;

/**
 * Created by annadowling on 11/05/2017.
 * Create the JobListFragment view and attach all event handling to that fragment
 */
public class JobListFragment extends ListFragment {


    private OnFragmentInteractionListener mListener;
    VolleyRequest request;

    protected JobFilter jobFilter;
    protected static JobAdapter jobAdapter;

    public JobListFragment() {
        // Required empty public constructor
    }

    /**
     * @return A new instance of fragment JobListFragment.
     */
    public static JobListFragment newInstance() {
        JobListFragment fragment = new JobListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        JobCatcherApp app = (JobCatcherApp) getActivity().getApplication();
        jobAdapter = new JobAdapter(getActivity().getApplicationContext(), app.jobsList);
        jobFilter = new JobFilter(app.jobsList, "all", jobAdapter);

        setListAdapter(jobAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    /**
     * @param uri
     */
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
