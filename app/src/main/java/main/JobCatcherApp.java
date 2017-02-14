package main;

import android.app.Application;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by annadowling on 14/02/2017.
 */

public class JobCatcherApp extends Application {
    //TODO initialise components such as lists etc that will be used throughout apps lifetime

    @Override
    public void onCreate() {
        super.onCreate();
        Log.v("jobCatcherApp", "JobCatcher App Started");
    }
}
