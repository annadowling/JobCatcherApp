package app.com.jobcatcherapp.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import app.com.jobcatcherapp.R;
import fragments.SearchUserFragment;
import fragments.UserListFragment;
import main.JobCatcherApp;


/**
 * Created by annadowling on 11/05/2017.
 * Cretease the search bar for user with the employer user search fragment.
 */
public class UserSearchBarActivity extends AppCompatActivity {

    protected UserListFragment userFragment;
    public JobCatcherApp app = (JobCatcherApp) getApplication();

    /**
     *
      * @param savedInstanceState
     */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_search_bar);
    }

    /**
     * create and populate the search fragment
     */
    @Override
    protected void onResume() {
        super.onResume();

        userFragment = SearchUserFragment.newInstance(); //get a new Fragment instance
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_user_layout, userFragment)
                .commit(); // add/replace in the current activity
    }
}

