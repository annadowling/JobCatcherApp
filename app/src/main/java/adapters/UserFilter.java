package adapters;

import android.widget.Filter;

import java.util.ArrayList;
import java.util.List;

import models.Job;
import models.User;

/**
 * Created by annadowling on 14/05/2017.
 */

public class UserFilter extends Filter {

    private List<User> originalUserList;
    private String filterText;
    private UserAdapter adapter;

    public UserFilter(List<User> originalUserList, String filterText,
                     UserAdapter adapter) {
        super();
        this.originalUserList = originalUserList;
        this.filterText = filterText;
        this.adapter = adapter;
    }

    public void setFilter(String filterText) {
        this.filterText = filterText;
    }

    @Override
    protected FilterResults performFiltering(CharSequence prefix) {
        FilterResults results = new FilterResults();
        String prefixString = prefix.toString().toLowerCase();
        List<User> newUsers = new ArrayList<User>();

        if (originalUserList == null) {
            originalUserList = new ArrayList<User>();
        }
        if (prefix == null || prefix.length() == 0) {
            if (filterText.equals("profession")) {
                results.values = originalUserList;
                results.count = originalUserList.size();
            } else {
                if (filterText.equals("bio")) {
                    for (User u : originalUserList)
                        if (u.bio.toLowerCase().contains(prefixString))
                            newUsers.add(u);
                }
                results.values = newUsers;
                results.count = newUsers.size();
            }
        } else {

            if (prefix.length() > 1) {
                for (User u : originalUserList) {
                    final String itemName = u.profession.toLowerCase();
                    if (filterText.equals("profession")) {
                        if (itemName.contains(prefixString)) {
                            newUsers.add(u);
                        }
                    } else if (filterText.equals("bio")) {
                        if (u.bio.toLowerCase().contains(prefixString)) {
                            newUsers.add(u);
                        }
                    }
                }
            }
            results.values = newUsers;
            results.count = newUsers.size();
        }
        return results;
    }


    @SuppressWarnings("unchecked")
    @Override
    protected void publishResults(CharSequence prefix, FilterResults results) {

        adapter.userList = (ArrayList<User>) results.values;

        if (results.count >= 0)
            adapter.notifyDataSetChanged();
        else {
            adapter.notifyDataSetInvalidated();
            adapter.userList = originalUserList;
        }
    }
}
