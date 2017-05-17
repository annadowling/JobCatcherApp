package fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import adapters.EmployerJobAdapter;
import app.com.jobcatcherapp.R;
import main.JobCatcherApp;
import models.Job;
import requests.VolleyListener;
import requests.VolleyRequest;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EmployerJobListFragment} interface
 * to handle interaction events.
 * Use the {@link EmployerJobListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */


public class EmployerJobListFragment extends Fragment implements AdapterView.OnItemClickListener,
        View.OnClickListener, VolleyListener {
    protected static EmployerJobAdapter listAdapter;
    protected ListView listView;
    protected SwipeRefreshLayout mSwipeRefreshLayout;
    VolleyRequest request;
    SharedPreferences pref;

    public JobCatcherApp app = JobCatcherApp.getInstance();

    public EmployerJobListFragment() {
        // Required empty public constructor
    }

    public static EmployerJobListFragment newInstance() {
        EmployerJobListFragment fragment = new EmployerJobListFragment();
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
        View v = null;

        v = inflater.inflate(R.layout.fragment_home, container, false);
        pref = getActivity().getSharedPreferences("AppPref", MODE_PRIVATE);

        listView = (ListView) v.findViewById(R.id.jobList);
        mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.job_swipe_refresh_layout);
        setSwipeRefreshLayout();


        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        VolleyRequest.attachListener(this);
        refreshJobDetails();

    }

    @Override
    public void onStop() {
        super.onStop();
        VolleyRequest.detachListener();
    }

    @Override
    public void onPause() {
        super.onPause();
        VolleyRequest.detachListener();
    }

    protected void setSwipeRefreshLayout() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshJobDetails();
            }
        });
    }

    @Override
    public void setJob(Job j) {
    }

    @Override
    public void setList(List<Job> list) {
        app.employerJobList = list;
        updateUI();
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Bundle activityInfo = new Bundle();
        activityInfo.putString("jobID", (String) view.getTag());
    }

    public void refreshJobDetails() {
        String url = "http://10.0.2.2:8080/getEmployerJobsList";
        String token = pref.getString("token", "default");

        request = new VolleyRequest();
        request.makeVolleyGetRequestForEmployerJobDetails(app, getActivity(), getActivity().getApplicationContext(), url, token, mSwipeRefreshLayout);
    }

    public void deleteJob(String jobToken) {
        String url = "http://10.0.2.2:8080/deleteJob";
        String employerToken = pref.getString("token", "default");

        request = new VolleyRequest();
        request.makeVolleyDeleteRequest(getActivity().getApplicationContext(), jobToken, employerToken, url);
    }

    @Override
    public void onClick(View view) {
        if (view.getTag() instanceof Job) {
            onJobDelete((Job) view.getTag());
        }
    }

    public void onJobDelete(final Job job) {
        String stringName = job.jobName;
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Are you sure you want to Delete the \'Job\' " + stringName + "?");
        builder.setCancelable(false);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deleteJob(job.jobToken);
                listAdapter.employerJobList.remove(job); // update adapters data
                listAdapter.notifyDataSetChanged(); // refresh adapter
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void updateUI() {

        listAdapter = new EmployerJobAdapter(getActivity(), this, app.employerJobList);
        listView.setAdapter(listAdapter);

        listView.setOnItemClickListener(this);
        listView.setEmptyView(getActivity().findViewById(R.id.empty_list_view));
        listAdapter.notifyDataSetChanged(); // Update the adapter
    }
}

