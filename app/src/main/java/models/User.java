package models;

import java.io.Serializable;

/**
 * Created by annadowling on 14/05/2017.
 */

public class User implements Serializable {

    public static int autoid = 1;
    public int userId;
    public String email;
    public String firstName;
    public String lastName;
    public String bio;
    public String profession;


    public User() {
    }

    public User(String firstName, String lastName, String email, String bio, String profession) {
        this.userId = autoid++;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.bio = bio;
        this.profession = profession;
    }

    @Override
    public String toString() {
        return "User [firstName=" + firstName + " lastName=" + lastName + ", email=" + email;
    }
}
