package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import app.com.jobcatcherapp.R;
import models.User;

/**
 * Created by annadowling on 14/05/2017.
 *  UserItem class provides a row entry view layout for each employer job
 */

public class UserItem {
    View view;
    Context viewContext;

    /**
     * Overloaded UserItem constructor
     * @param context
     * @param parent
     * @param user
     */
    public UserItem(Context context, ViewGroup parent, User user) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        viewContext = context;
        view = inflater.inflate(R.layout.fragment_user_list, parent, false);
        view.setId(user.userId);

        updateControls(user);
    }

    /**
     * Sets the row data values
     * @param user
     */
    private void updateControls(User user) {
        TextView userName = (TextView) view.findViewById(R.id.rowUserName);
        TextView bio = (TextView) view.findViewById(R.id.rowBio);
        TextView profession = (TextView) view.findViewById(R.id.rowProfession);
        TextView email = (TextView) view.findViewById(R.id.rowEmail);

        userName.setText(user.firstName + " " + user.lastName);
        bio.setText(user.bio);
        profession.setText(user.profession);
        email.setText(user.email);
    }
}
