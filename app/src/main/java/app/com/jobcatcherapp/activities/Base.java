package app.com.jobcatcherapp.activities;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import app.com.jobcatcherapp.R;
import fragments.LoginFragment;

/**
 * Created by annadowling on 11/05/2017.
 * Sets the splash activity and launches the login fragment as the landing page
 */
public class Base extends AppCompatActivity {


    /**
     * Create the login fragment view
     * @param savedInstanceState
     */
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
