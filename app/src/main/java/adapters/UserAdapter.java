package adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

import app.com.jobcatcherapp.R;
import models.User;


/**
 * Created by annadowling on 11/05/2017.
 */

public class UserAdapter extends ArrayAdapter<User> {
    private Context context;
    public List<User> userList;

    public UserAdapter(Context context, List<User> userList) {
        super(context, R.layout.fragment_user_list_main, userList);

        this.context = context;
        this.userList = userList;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        UserItem item = new UserItem(context, parent, userList.get(position));
        return item.view;
    }


    @Override
    public int getCount() {
        return userList.size();
    }

    public List<User> getUserList() {
        return this.userList;
    }

    @Override
    public User getItem(int position) {
        return userList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getPosition(User u) {
        return userList.indexOf(u);
    }
}

