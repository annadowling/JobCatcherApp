package models;

import java.io.Serializable;

/**
 * Created by annadowling on 21/04/2017.
 */

public class Job implements Serializable {

    public static int autoid = 1;
    public int jobId;
    public String jobToken;
    public String jobName;
    public String jobDescription;
    public String contactNumber;


    public Job() {
    }

    public Job(String jobToken, String jobName, String jobDescription, String contactNumber) {
        this.jobId = autoid++;
        this.jobToken = jobToken;
        this.jobName = jobName;
        this.jobDescription = jobDescription;
        this.contactNumber = contactNumber;
    }

    @Override
    public String toString() {
        return "Job [jobName=" + jobName
                + ", jobToken =" + jobToken + ", jobDescription=" + jobDescription + ", contactNumber=" + contactNumber;
    }
}
