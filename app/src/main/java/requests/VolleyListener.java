package requests;

import java.util.List;

import models.Job;

/**
 * Created by annadowling on 17/05/2017.
 */

public interface VolleyListener {
    void setList(List<Job> list);
    void setJob(Job j);
}