package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import app.com.jobcatcherapp.R;
import models.Job;

/**
 * Created by annadowling on 11/05/2017.
 */

public class JobAdapter extends ArrayAdapter<Job> {
    private Context context;
    public List<Job> jobList;

    public JobAdapter(Context context,
                      List<Job> coffeeList) {
        super(context, R.layout.fragment_job_list, coffeeList);

        this.context = context;
        this.jobList = coffeeList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Job job = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.fragment_job_list, parent, false);
        }

        TextView jobName = (TextView) convertView.findViewById(R.id.rowJobName2);
        TextView jobDescription = (TextView) convertView.findViewById(R.id.rowJobDescription2);
        TextView contactNumber = (TextView) convertView.findViewById(R.id.rowContactNumber2);
        TextView hiddenValue = (TextView) convertView.findViewById(R.id.hidden_value2);
        ImageView delete = (ImageView) convertView.findViewById(R.id.imgDelete2);

        jobName.setText(job.jobName);
        jobDescription.setText(job.jobDescription);
        contactNumber.setText(job.contactNumber);
        hiddenValue.setText(job.jobToken);
        return convertView;
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

