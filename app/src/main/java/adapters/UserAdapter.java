package adapters;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.com.jobcatcherapp.R;
import fragments.UserEmployerViewFragment;
import models.User;


/**
 * Created by annadowling on 11/05/2017.
 * List Adapter for User list
 */

public class UserAdapter extends ArrayAdapter<User>  {
    private Context context;
    public List<User> userList;

    /**
     * Overloaded UserAdapter constructor
     * @param context
     * @param userList
     */
    public UserAdapter(Context context, List<User> userList) {
        super(context, R.layout.fragment_user_list_main, userList);

        this.context = context;
        this.userList = userList;
    }

    /**
     * get the user row view for each job entry in the list
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, final View convertView, ViewGroup parent) {
        UserItem item = new UserItem(context, parent, userList.get(position));
        final User user = userList.get(position);
        final Context parentContext = parent.getContext();

        TextView userName = (TextView) item.view.findViewById(R.id.rowUserName);
        userName.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final FragmentTransaction ft =  ((Activity) parentContext).getFragmentManager().beginTransaction();
                final Map<String, String> responseEntries = new HashMap<String, String>();
                responseEntries.put("email", user.email);
                responseEntries.put("firstName", user.firstName);
                responseEntries.put("lastName", user.lastName);
                responseEntries.put("age", "");
                responseEntries.put("bio", user.bio);
                responseEntries.put("profession", user.profession);
                UserEmployerViewFragment profileFragment = UserEmployerViewFragment.newInstance(responseEntries);
                userList.clear();
                notifyDataSetChanged();

                ft.replace(R.id.fragmentUserListSearchBar, profileFragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        return item.view;
    }

    /**
     *
     * @return int
     */
    @Override
    public int getCount() {
        return userList.size();
    }

    /**
     *
     * @return userList
     */
    public List<User> getUserList() {
        return this.userList;
    }

    /**
     * get view item int posiiton in list
     * @param position
     * @return
     */
    @Override
    public User getItem(int position) {
        return userList.get(position);
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
     * @param u
     * @return
     */
    @Override
    public int getPosition(User u) {
        return userList.indexOf(u);
    }

}

