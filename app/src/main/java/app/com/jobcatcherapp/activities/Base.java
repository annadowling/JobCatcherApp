package app.com.jobcatcherapp.activities;

import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import app.com.jobcatcherapp.R;
import fragments.JobFragment;
import fragments.JobListFragment;
import fragments.LoginFragment;
import main.JobCatcherApp;

public class Base extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        FragmentTransaction ft = getFragmentManager().beginTransaction();

        LoginFragment loginFragment = LoginFragment.newInstance();
        ft.replace(R.id.baseFrame, loginFragment);
        ft.addToBackStack(null);
        ft.commit();
    }
}
