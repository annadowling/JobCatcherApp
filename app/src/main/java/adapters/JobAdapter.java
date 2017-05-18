package adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

import app.com.jobcatcherapp.R;
import models.Job;

/**
 * Created by annadowling on 11/05/2017.
 *  List Adapter for job list
 */

public class JobAdapter extends ArrayAdapter<Job> {
    private Context context;
    public List<Job> jobList;

    /**
     * Overloaded JobAdapter constructor
     * @param context
     * @param jobList
     */
    public JobAdapter(Context context, List<Job> jobList) {
        super(context, R.layout.fragment_job_list_main, jobList);

        this.context = context;
        this.jobList = jobList;
    }

    /**
     *  get the job row view for each job entry in the list
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        JobItem item = new JobItem(context, parent,
                jobList.get(position));
        return item.view;
    }

    /**
     *
     * @return int
     */
    @Override
    public int getCount() {
        return jobList.size();
    }

    /**
     *
     * @return jobList
     */
    public List<Job> getJobList() {
        return this.jobList;
    }

    /**
     *  get view item int posiiton in list
     * @param position
     * @return
     */
    @Override
    public Job getItem(int position) {
        return jobList.get(position);
    }

    /**
     * get view item id
     * @param position
     * @return
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * get current job position in list using index
     * @param j
     * @return
     */
    @Override
    public int getPosition(Job j) {
        return jobList.indexOf(j);
    }
}

