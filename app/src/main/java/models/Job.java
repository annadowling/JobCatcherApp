package models;

import java.io.Serializable;

/**
 * Created by annadowling on 13/05/2017.
 * Job model for mapping to Volley data sent from node server.
 */

public class Job implements Serializable {

    public static int autoid = 1;
    public int jobId;
    public String jobToken;
    public String jobName;
    public String jobDescription;
    public String contactNumber;
    public String latitude;
    public String longitude;


    public Job() {
    }

    public Job(String jobToken, String jobName, String jobDescription, String contactNumber, String latitude, String longitude) {
        this.jobId = autoid++;
        this.jobToken = jobToken;
        this.jobName = jobName;
        this.jobDescription = jobDescription;
        this.contactNumber = contactNumber;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "Job [jobName=" + jobName
                + ", jobToken =" + jobToken + ", jobDescription=" + jobDescription + ", contactNumber=" + contactNumber + ", latitude=" + longitude + ", longitude=";
    }
}
