package main;

import android.app.Application;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import models.Job;

/**
 * Created by annadowling on 14/02/2017.
 */

public class JobCatcherApp extends Application {

    public List <Job>  jobsList = new ArrayList<Job>();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.v("jobCatcherApp", "JobCatcher App Started");
    }
}
