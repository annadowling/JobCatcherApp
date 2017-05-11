package app.com.jobcatcherapp.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import app.com.jobcatcherapp.R;
import fragments.JobListFragment;
import fragments.SearchFragment;
import main.JobCatcherApp;

public class SearchActivity extends AppCompatActivity {
    protected JobListFragment jobFragment;
    public JobCatcherApp app = (JobCatcherApp) getApplication();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_search);
    }

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
