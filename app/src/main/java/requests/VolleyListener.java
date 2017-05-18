package requests;

import java.util.List;

import models.Job;

/**
 * Created by annadowling on 17/05/2017.
 * Sets the job and user list for use in VolleyRequest
 */

public interface VolleyListener {
    void setList(List<Job> list);
    void setJob(Job j);
}