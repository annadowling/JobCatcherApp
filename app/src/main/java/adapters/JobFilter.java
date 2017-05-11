package adapters;

import android.widget.Filter;

import java.util.ArrayList;
import java.util.List;

import models.Job;

/**
 * Created by annadowling on 11/05/2017.
 */

public class JobFilter extends Filter {
    private List<Job> originalJobList;
    private String filterText;
    private JobAdapter adapter;

    public JobFilter(List<Job> originalJobList, String filterText,
                     JobAdapter adapter) {
        super();
        this.originalJobList = originalJobList;
        this.filterText = filterText;
        this.adapter = adapter;
    }

    public void setFilter(String filterText) {
        this.filterText = filterText;
    }

    @Override
    protected FilterResults performFiltering(CharSequence prefix) {
        FilterResults results = new FilterResults();

        if (originalJobList == null) {
            originalJobList = new ArrayList<Job>();
        }
        if (prefix == null || prefix.length() == 0) {
            List<Job> newJobs = new ArrayList<Job>();
            if (filterText.equals("all")) {
                results.values = originalJobList;
                results.count = originalJobList.size();
            } else {
//                if (filterText.equals("favourites")) {
//                    for (Job j : originalCoffeeList)
//                        if (c.favourite)
//                            newCoffees.add(c);
//                }
                results.values = newJobs;
                results.count = newJobs.size();
            }
        } else {
            String prefixString = prefix.toString().toLowerCase();
            final ArrayList<Job> newJobs = new ArrayList<Job>();

            for (Job j : originalJobList) {
                final String itemName = j.jobName.toLowerCase();
                if (itemName.contains(prefixString)) {
                    if (filterText.equals("all")) {
                        newJobs.add(j);
                    }
//                    else if (c.favourite) {
//                        newCoffees.add(j);
//                    }
                }
            }
            results.values = newJobs;
            results.count = newJobs.size();
        }
        return results;
    }

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

