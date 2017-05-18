package adapters;

/**
 * Created by annadowling on 11/05/2017.
 * JobItem class provides a row entry view layout for each job
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import app.com.jobcatcherapp.R;
import models.Job;

public class JobItem {
    View view;

    /**
     * Overloaded JobItem constructor
     * @param context
     * @param parent
     * @param job
     */
    public JobItem(Context context, ViewGroup parent, Job job)
    {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.fragment_job_list, parent, false);
        view.setId(job.jobId);

        updateControls(job);
    }

    /**
     * Sets the row data values
     * @param job
     */
    private void updateControls(Job job) {
        TextView jobName = (TextView) view.findViewById(R.id.rowJobName2);
        TextView jobDescription = (TextView) view.findViewById(R.id.rowJobDescription2);
        TextView contactNumber = (TextView) view.findViewById(R.id.rowContactNumber2);
        TextView hiddenValue = (TextView) view.findViewById(R.id.hidden_value2);

        jobName.setText(job.jobName);
        jobDescription.setText(job.jobDescription);
        contactNumber.setText(job.contactNumber);
        hiddenValue.setText(job.jobToken);
    }
}
