package main;

import android.app.Application;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import models.Job;
import models.User;

/**
 * Created by annadowling on 14/02/2017.
 */

public class JobCatcherApp extends Application {

    public List <Job>  jobsList = new ArrayList<Job>();
    public List <User>  userList = new ArrayList<User>();
    public List <Job> employerJobList = new ArrayList<Job>();
    private static JobCatcherApp mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        Log.v("jobCatcherApp", "JobCatcher App Started");
    }

    public static synchronized JobCatcherApp getInstance() {
        return mInstance;
    }
}
