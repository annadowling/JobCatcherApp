package adapters;

import android.widget.Filter;

import java.util.ArrayList;
import java.util.List;

import models.Job;

/**
 * Created by annadowling on 11/05/2017.
 * Filters search input results and updates the list adapter
 */

public class JobFilter extends Filter {
    private List<Job> originalJobList;
    private String filterText;
    private JobAdapter adapter;

    /**
     * Overloaded contructor
     * @param originalJobList
     * @param filterText
     * @param adapter
     */
    public JobFilter(List<Job> originalJobList, String filterText,
                     JobAdapter adapter) {
        super();
        this.originalJobList = originalJobList;
        this.filterText = filterText;
        this.adapter = adapter;
    }

    /**
     * Filtertext string assignment
     * @param filterText
     */
    public void setFilter(String filterText) {
        this.filterText = filterText;
    }

    /**
     * Performs the filtering based on the CharSequence passed in.
     * @param prefix
     * @return
     */
    @Override
    protected FilterResults performFiltering(CharSequence prefix) {
        FilterResults results = new FilterResults();
        String prefixString = prefix.toString().toLowerCase();
        List<Job> newJobs = new ArrayList<Job>();

        if (originalJobList == null) {
            originalJobList = new ArrayList<Job>();
        }
        if (prefix == null || prefix.length() == 0) {
            if (filterText.equals("all")) {
                results.values = originalJobList;
                results.count = originalJobList.size();
            } else {
                if (filterText.equals("by description")) {
                    for (Job j : originalJobList)
                        if (j.jobDescription.toLowerCase().contains(prefixString))
                            newJobs.add(j);
                }
                results.values = newJobs;
                results.count = newJobs.size();
            }
        } else {

            if (prefix.length() > 1) {
                for (Job j : originalJobList) {
                    final String itemName = j.jobName.toLowerCase();
                    if (filterText.equals("all")) {
                        if (itemName.contains(prefixString)) {
                            newJobs.add(j);
                        }
                    } else if (filterText.equals("by description")) {
                        if (j.jobDescription.toLowerCase().contains(prefixString)) {
                            newJobs.add(j);
                        }
                    }
                }
            }
            results.values = newJobs;
            results.count = newJobs.size();
        }
        return results;
    }

    /**
     * Publishes the resulting filtered data to the adapter.
     * @param prefix
     * @param results
     */
    @SuppressWarnings("unchecked")
    @Override
    protected void publishResults(CharSequence prefix, FilterResults results) {

        adapter.jobList = (ArrayList<Job>) results.values;

        if (results.count >= 0)
            adapter.notifyDataSetChanged();
        else {
            adapter.notifyDataSetInvalidated();
            adapter.jobList = originalJobList;
        }
    }
}

