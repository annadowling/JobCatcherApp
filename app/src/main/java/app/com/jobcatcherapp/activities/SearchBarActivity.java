package app.com.jobcatcherapp.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import app.com.jobcatcherapp.R;
import fragments.JobListFragment;
import fragments.SearchFragment;
import main.JobCatcherApp;

/**
 * Created by annadowling on 11/05/2017.
 * Sets the SearchBar view for the User Job list search page
 */
public class SearchBarActivity extends AppCompatActivity {

    protected JobListFragment jobFragment;
    public JobCatcherApp app = (JobCatcherApp) getApplication();

    /**
     *
     * @param savedInstanceState
     */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_bar);
    }

    /**
     * create and populate the search fragment
     */
    @Override
    protected void onResume() {
        super.onResume();

        jobFragment = SearchFragment.newInstance(); //get a new Fragment instance
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_layout, jobFragment)
                .commit(); // add/replace in the current activity
    }
}
