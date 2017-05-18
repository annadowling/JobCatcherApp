package models;
import java.io.Serializable;

/**
 * Created by annadowling on 13/05/2017.
 * Employer model for mapping to Volley data sent from node server.
 */

public class Employer implements Serializable {

    public static int autoid = 1;
    public int employerId;
    public String employerName;
    public String employerEmail;
    public String contactNumber;


    public Employer() {
    }

    public Employer(String employerName, String employerEmail, String contactNumber) {
        this.employerId = autoid++;
        this.employerName = employerName;
        this.employerEmail = employerEmail;
        this.contactNumber = contactNumber;
    }

    @Override
    public String toString() {
        return "Employer [employerName=" + employerName + " contactNumber=" + contactNumber + ", employerEmail=" + employerEmail;
    }
}

