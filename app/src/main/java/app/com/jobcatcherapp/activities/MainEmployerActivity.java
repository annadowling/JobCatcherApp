package app.com.jobcatcherapp.activities;

import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import app.com.jobcatcherapp.R;
import fragments.ContactFragment;
import fragments.EditEmployerFragment;
import fragments.EmployerPortalFragment;
import fragments.JobFragment;
import main.JobCatcherApp;
import requests.VolleyRequest;

import static app.com.jobcatcherapp.R.id.employerProfileFrame;
import static app.com.jobcatcherapp.R.id.homeFrameEmployer;

public class MainEmployerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    SharedPreferences pref;
    VolleyRequest request;
    public static TextView emailText, userNameText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pref = this.getSharedPreferences("AppPref", MODE_PRIVATE);
        setContentView(R.layout.activity_main_employer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String email = pref.getString("companyEmail", "default");
        String companyName = pref.getString("companyName", "default");
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();

                ContactFragment contactFragment = ContactFragment.newInstance();
                ft.replace(R.id.homeFrameEmployer, contactFragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_employer);
        navigationView.setNavigationItemSelectedListener(this);
        View header=navigationView.getHeaderView(0);

        emailText = (TextView) header.findViewById(R.id.navUserEmailEmployer);
        emailText.setText(email);
        userNameText = (TextView) header.findViewById(R.id.navUserNameEmployer);
        userNameText.setText(companyName);

        String url = "http://10.0.2.2:8080/getEmployerDetails";
        String token = pref.getString("token", "default");

        request = new VolleyRequest();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        request.makeVolleyGetRequestForEmployerDetails(getApplicationContext(), url, token, ft, homeFrameEmployer);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_employer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentTransaction ft = getFragmentManager().beginTransaction();

        if (id == R.id.contact_options) {

            ContactFragment contactFragment = ContactFragment.newInstance();
            ft.replace(homeFrameEmployer, contactFragment);
            ft.addToBackStack(null);
            ft.commit();
        } else if (id == R.id.employer_profile) {
            String url = "http://10.0.2.2:8080/getEmployerDetails";
            String token = pref.getString("token", "default");

            request = new VolleyRequest();
            request.makeVolleyGetRequestForEmployerDetails(getApplicationContext(), url, token, ft, homeFrameEmployer);

        } else if (id == R.id.edit_profile) {
            EditEmployerFragment editFragment = EditEmployerFragment.newInstance();
            ft.replace(homeFrameEmployer, editFragment);
            ft.addToBackStack(null);
            ft.commit();

        } else if (id == R.id.employer_jobs_list) {
            String url = "http://10.0.2.2:8080/getEmployerJobsList";
            String token = pref.getString("token", "default");


            request = new VolleyRequest();
            request.makeVolleyGetRequestForEmployerJobDetails(this, getApplicationContext(), url, token, ft, R.id.homeFrameEmployer);

        } else if (id == R.id.add_job) {
            JobFragment jobFragment = JobFragment.newInstance();
            ft.replace(homeFrameEmployer, jobFragment);
            ft.addToBackStack(null);
            ft.commit();

        } else if (id == R.id.logout_employer) {
            SharedPreferences.Editor edit = pref.edit();
            //Storing Data using SharedPreferences
            edit.putString("token", "");
            edit.commit();

            EmployerPortalFragment loginFragment = EmployerPortalFragment.newInstance();
            ft.replace(R.id.homeFrameEmployer, loginFragment);
            ft.commit();
        }else if(id == R.id.search_job_seekers){
            getAllUserDetails();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void getAllUserDetails() {
        String url = "http://10.0.2.2:8080/getAllUsersList";
        request = new VolleyRequest();
        JobCatcherApp app = (JobCatcherApp) getApplication();
        request.makeVolleyGetRequestForAllUserDetails(app, MainEmployerActivity.this, this.getApplicationContext(), url, true);
    }
}
