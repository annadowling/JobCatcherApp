package app.com.jobcatcherapp.activities;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import app.com.jobcatcherapp.R;
import fragments.ContactFragment;
import fragments.EmployerFragment;
import fragments.EmployerPortalFragment;
import fragments.MapsFragment;
import fragments.ProfileFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    SharedPreferences pref;

    public static TextView emailText, userNameText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        pref = this.getSharedPreferences("AppPref", MODE_PRIVATE);
        String email = pref.getString("email", "default");
        String firstName = pref.getString("firstName", "default");
        String lastName = pref.getString("lastName", "default");


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();

                ContactFragment contactFragment = ContactFragment.newInstance();
                ft.replace(R.id.homeFrame, contactFragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View view = findViewById(R.id.navHeaderView);

        emailText = (TextView) view.findViewById(R.id.navUserEmail);
        emailText.setText(email);
        userNameText = (TextView) view.findViewById(R.id.navUserName);
        userNameText.setText(firstName + " " + lastName);


        FragmentTransaction ft = getFragmentManager().beginTransaction();

        ProfileFragment profilFragment = ProfileFragment.newInstance();
        ft.replace(R.id.homeFrame, profilFragment);
        ft.addToBackStack(null);
        ft.commit();
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
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Base/Up button, so long
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

        if (id == R.id.nav_map) {
            Intent mapactivity = new Intent(MainActivity.this, MapsFragment.class);
            startActivity(mapactivity);
        } else if (id == R.id.contact_options) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();

            ContactFragment contactFragment = ContactFragment.newInstance();
            ft.replace(R.id.homeFrame, contactFragment);
            ft.addToBackStack(null);
            ft.commit();
        } else if (id == R.id.register_employer) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();

            EmployerFragment employerFragment = EmployerFragment.newInstance();
            ft.replace(R.id.homeFrame, employerFragment);
            ft.addToBackStack(null);
            ft.commit();
        } else if (id == R.id.employer_portal) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();

            EmployerPortalFragment portalFragment = EmployerPortalFragment.newInstance();
            ft.replace(R.id.homeFrame, portalFragment);
            ft.addToBackStack(null);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
