package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import java.util.List;

import app.com.jobcatcherapp.R;
import models.Job;

/**
 * Created by annadowling on 11/05/2017.
 */

public class JobAdapter extends ArrayAdapter<Job> {
    private Context context;
    public List<Job> jobList;

    public JobAdapter(Context context, List<Job> jobList) {
        super(context, R.layout.fragment_job_list_main, jobList);

        this.context = context;
        this.jobList = jobList;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        JobItem item = new JobItem(context, parent,
                jobList.get(position));
        return item.view;
    }


    @Override
    public int getCount() {
        return jobList.size();
    }

    public List<Job> getJobList() {
        return this.jobList;
    }

    @Override
    public Job getItem(int position) {
        return jobList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getPosition(Job j) {
        return jobList.indexOf(j);
    }
}

