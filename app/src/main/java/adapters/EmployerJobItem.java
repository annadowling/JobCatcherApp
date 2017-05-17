package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import app.com.jobcatcherapp.R;
import models.Job;

/**
 * Created by annadowling on 17/05/2017.
 */

public class EmployerJobItem {
    View view;

    public EmployerJobItem(Context context, ViewGroup parent, View.OnClickListener deleteListener, Job job)
    {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.fragment_employer_job_list, parent, false);
        view.setId(job.jobId);

        updateControls(job);

        ImageView imgDelete = (ImageView) view.findViewById(R.id.imgDelete);
        imgDelete.setTag(job);
        imgDelete.setOnClickListener(deleteListener);
    }

    private void updateControls(Job job) {
        TextView jobName = (TextView) view.findViewById(R.id.rowJobName);
        TextView jobDescription = (TextView) view.findViewById(R.id.rowJobDescription);
        TextView contactNumber = (TextView) view.findViewById(R.id.rowContactNumber);
        TextView hiddenValue = (TextView) view.findViewById(R.id.hidden_value);

        jobName.setText(job.jobName);
        jobDescription.setText(job.jobDescription);
        contactNumber.setText(job.contactNumber);
        hiddenValue.setText(job.jobToken);
    }
}
