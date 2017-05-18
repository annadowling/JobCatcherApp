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
 * List Adapter for Employer's job list
 */

public class EmployerJobAdapter extends ArrayAdapter<Job> {
    private Context context;
    public List<Job> employerJobList;
    private View.OnClickListener deleteListener;

    /**
     * Overloaded EmployerJobAdapter constructor
     * @param context
     * @param deleteListener
     * @param employerJobList
     */
    public EmployerJobAdapter(Context context, View.OnClickListener deleteListener, List<Job> employerJobList) {
        super(context, R.layout.fragment_employer_job_list, employerJobList);

        this.context = context;
        this.deleteListener = deleteListener;
        this.employerJobList = employerJobList;
    }

    /**
     * get the job row view for each job entry in the list
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        EmployerJobItem item = new EmployerJobItem(context, parent, deleteListener, employerJobList.get(position));
        return item.view;
    }

    /**
     * @return int
     */

    @Override
    public int getCount(){
        return employerJobList != null ? employerJobList.size() : 0;
    }

    /**
     * @return employerJobList
     */
    public List<Job> getEmployerJobList() {
        return this.employerJobList;
    }

    /**
     * get view item int posiiton in list
     * @param position
     * @return
     */
    @Override
    public Job getItem(int position) {
        return employerJobList.get(position);
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
        return employerJobList.indexOf(j);
    }
}
