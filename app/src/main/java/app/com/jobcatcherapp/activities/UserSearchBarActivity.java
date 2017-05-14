package app.com.jobcatcherapp.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import app.com.jobcatcherapp.R;
import fragments.JobListFragment;
import fragments.SearchFragment;
import fragments.SearchUserFragment;
import fragments.UserListFragment;
import main.JobCatcherApp;

public class UserSearchBarActivity extends AppCompatActivity {

    protected UserListFragment userFragment;
    public JobCatcherApp app = (JobCatcherApp) getApplication();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_search_bar);
    }

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

