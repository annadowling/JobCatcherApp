package adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

import app.com.jobcatcherapp.R;
import models.Job;

/**
 * Created by annadowling on 17/05/2017.
 */

public class EmployerJobAdapter extends ArrayAdapter<Job> {
    private Context context;
    public List<Job> employerJobList;
    private View.OnClickListener deleteListener;

    public EmployerJobAdapter(Context context, View.OnClickListener deleteListener, List<Job> employerJobList) {
        super(context, R.layout.fragment_employer_job_list, employerJobList);

        this.context = context;
        this.deleteListener = deleteListener;
        this.employerJobList = employerJobList;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        EmployerJobItem item = new EmployerJobItem(context, parent, deleteListener, employerJobList.get(position));
        return item.view;
    }


    @Override
    public int getCount() {
        return employerJobList.size();
    }

    public List<Job> getEmployerJobList() {
        return this.employerJobList;
    }

    @Override
    public Job getItem(int position) {
        return employerJobList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getPosition(Job j) {
        return employerJobList.indexOf(j);
    }
}
