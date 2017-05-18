package requests;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.com.jobcatcherapp.activities.SearchBarActivity;
import app.com.jobcatcherapp.activities.UserSearchBarActivity;
import fragments.EmployerProfileFragment;
import fragments.JobDetailsFragment;
import fragments.UserProfileFragment;
import main.JobCatcherApp;
import models.Employer;
import models.Job;
import models.User;


/**
 * Created by annadowling on 23/02/2017.
 * VolleyRequest class controls all HTPP reuqests for the applciation including GET, POST and DELETE
 */

public class VolleyRequest {
    public JobCatcherApp app;
    private static VolleyListener vListener;

    public static void attachListener(VolleyListener fragment) {
        vListener = fragment;
    }

    public static void detachListener() {
        vListener = null;
    }

    /**
     * Genral post request method
     *
     * @param context
     * @param requestParameters
     * @param url
     */
    public void makeVolleyPostRequest(Context context, Map<String, String> requestParameters, String url) {
        final Context applicationContext = context;
        final Map<String, String> mapParameters = requestParameters;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(applicationContext, response, Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(applicationContext, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.putAll(mapParameters);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(applicationContext);
        requestQueue.add(stringRequest);
    }

    /**
     * GET request for user details
     *
     * @param context
     * @param url
     * @param token
     * @param ft
     * @param i
     */
    public void makeVolleyGetRequestForUserDetails(Context context, String url, String token, FragmentTransaction ft, int i) {
        final Context applicationContext = context;
        final FragmentTransaction fragmentTransaction = ft;
        final int frameId = i;

        final Map<String, String> responseEntries = new HashMap<String, String>();

        String getUrl = url + "/token=" + token;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, getUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response", response.toString());
                        try {
                            responseEntries.put("email", response.getString("email"));
                            responseEntries.put("firstName", response.getString("firstName"));
                            responseEntries.put("lastName", response.getString("lastName"));
                            responseEntries.put("age", response.getString("age"));
                            responseEntries.put("bio", response.getString("bio"));
                            responseEntries.put("profession", response.getString("profession"));


                            UserProfileFragment profileFragment = UserProfileFragment.newInstance(responseEntries);
                            fragmentTransaction.replace(frameId, profileFragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error: ", error.getMessage());
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(applicationContext);
        requestQueue.add(request);
    }

    /**
     * GET request for employer details
     *
     * @param context
     * @param url
     * @param token
     * @param ft
     * @param i
     */
    public void makeVolleyGetRequestForEmployerDetails(Context context, String url, String token, FragmentTransaction ft, int i) {
        final Context applicationContext = context;
        final FragmentTransaction fragmentTransaction = ft;
        final int frameId = i;

        final Map<String, String> responseEntries = new HashMap<String, String>();

        String getUrl = url + "/token=" + token;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, getUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response", response.toString());
                        try {
                            responseEntries.put("email", response.getString("email"));
                            responseEntries.put("companyName", response.getString("companyName"));
                            responseEntries.put("address", response.getString("address"));
                            responseEntries.put("latitude", response.getString("latitude"));
                            responseEntries.put("longitude", response.getString("longitude"));


                            EmployerProfileFragment profileFragment = EmployerProfileFragment.newInstance(responseEntries);
                            fragmentTransaction.replace(frameId, profileFragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error: ", error.getMessage());
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(applicationContext);
        requestQueue.add(request);
    }

    /**
     * get request for employer job details
     *
     * @param app
     * @param activityPointer
     * @param context
     * @param url
     * @param token
     * @param mSwipeRefreshLayout
     */
    public void makeVolleyGetRequestForEmployerJobDetails(final JobCatcherApp app, Activity activityPointer, Context context, String url, String token, final SwipeRefreshLayout mSwipeRefreshLayout) {
        final Context applicationContext = context;

        String getUrl = url + "/token=" + token;

        final ProgressDialog progressDialog = new ProgressDialog(activityPointer);
        progressDialog.setMessage("Loading page ....");
        progressDialog.show();

        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, getUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("JsonArray", response.toString());
                        JSONArray jsonArray = null;
                        try {
                            jsonArray = response.getJSONArray("response");

                            List<Job> jobsList = new ArrayList<Job>();

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                                String latitude = null;
                                String longitude = null;
                                if (jsonObject.getString("latitude") != null) {
                                    latitude = jsonObject.getString("latitude");
                                }
                                if (jsonObject.getString("longitude") != null) {
                                    longitude = jsonObject.getString("longitude");
                                }
                                Job job = new Job(jsonObject.getString("_id"), jsonObject.getString("jobTitle"), jsonObject.getString("jobDescription"), jsonObject.getString("contactNumber"), latitude, longitude);
                                jobsList.add(job);
                            }

                            vListener.setList(jobsList);
                            if (mSwipeRefreshLayout != null)
                                mSwipeRefreshLayout.setRefreshing(false);
                            progressDialog.dismiss();

                        } catch (JSONException e) {
                            if (mSwipeRefreshLayout != null)
                                mSwipeRefreshLayout.setRefreshing(false);
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String err = (error.getMessage() == null) ? "No Jobs found!" : error.getMessage();
                Log.d("Error: ", err);
                Toast.makeText(applicationContext, "No Jobs found!", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(applicationContext);
        requestQueue.add(request);
    }

    /**
     * GET reguest for employer and spefcific job data
     *
     * @param context
     * @param url
     * @param token
     * @param ft
     * @param i
     */
    public void makeVolleyGetRequestForEmployerAndJob(Context context, String url, String token, FragmentTransaction ft, int i) {
        final Context applicationContext = context;
        final FragmentTransaction fragmentTransaction = ft;
        final int frameId = i;

        String getUrl = url + "/token=" + token;

        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, getUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Job returnedJob = new Job(response.getString("id"), response.getString("jobTitle"), response.getString("jobDescription"), response.getString("contactNumber"), response.getString("latitude"), response.getString("longitude"));

                            Employer returnedEmployer = new Employer(response.getString("companyName"), response.getString("email"), response.getString("contactNumber"));

                            JobDetailsFragment jobDetailsFragment = JobDetailsFragment.newInstance(returnedJob, returnedEmployer);
                            fragmentTransaction.replace(frameId, jobDetailsFragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String err = (error.getMessage() == null) ? "No Jobs found!" : error.getMessage();
                Log.d("Error: ", err);
                Toast.makeText(applicationContext, "No Jobs found!", Toast.LENGTH_LONG).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(applicationContext);
        requestQueue.add(request);
    }

    /**
     * GET request for all job details
     *
     * @param app
     * @param activityPointer
     * @param context
     * @param url
     * @param launchSearchActivity
     */
    public void makeVolleyGetRequestForAllJobDetails(final JobCatcherApp app, Activity activityPointer, Context context, String url, final Boolean launchSearchActivity) {
        final Context applicationContext = context;
        final Activity activity = activityPointer;
        final JobCatcherApp application = app;


        final ProgressDialog progressDialog = new ProgressDialog(activityPointer);
        progressDialog.setMessage("Loading page ....");
        progressDialog.show();

        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("JsonArray", response.toString());
                        JSONArray jsonArray = null;
                        try {
                            jsonArray = response.getJSONArray("response");

                            List<Job> jobsList = new ArrayList<Job>();

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                                String latitude = null;
                                String longitude = null;
                                if (jsonObject.getString("latitude") != null) {
                                    latitude = jsonObject.getString("latitude");
                                }
                                if (jsonObject.getString("longitude") != null) {
                                    longitude = jsonObject.getString("longitude");
                                }
                                Job job = new Job(jsonObject.getString("_id"), jsonObject.getString("jobTitle"), jsonObject.getString("jobDescription"), jsonObject.getString("contactNumber"), latitude, longitude);
                                jobsList.add(job);
                            }


                            application.jobsList.clear();
                            progressDialog.dismiss();
                            application.jobsList.addAll(jobsList);

                            if (launchSearchActivity) {
                                Intent intent = new Intent(activity, SearchBarActivity.class);
                                activity.startActivity(intent);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String err = (error.getMessage() == null) ? "No Jobs found!" : error.getMessage();
                Log.d("Error: ", err);
                Toast.makeText(applicationContext, "No Jobs found!", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(applicationContext);
        requestQueue.add(request);
    }

    /**
     * GET request for all user details
     *
     * @param app
     * @param activityPointer
     * @param context
     * @param url
     * @param launchSearchActivity
     */
    public void makeVolleyGetRequestForAllUserDetails(final JobCatcherApp app, Activity activityPointer, Context context, String url, final Boolean launchSearchActivity) {
        final Context applicationContext = context;
        final Activity activity = activityPointer;
        final JobCatcherApp application = app;


        final ProgressDialog progressDialog = new ProgressDialog(activityPointer);
        progressDialog.setMessage("Loading page ....");
        progressDialog.show();

        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("JsonArray", response.toString());
                        JSONArray jsonArray = null;
                        try {
                            jsonArray = response.getJSONArray("response");

                            List<User> userList = new ArrayList<User>();

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                                String biography = null;
                                if (jsonObject.getString("bio") != null) {
                                    biography = jsonObject.getString("bio");
                                }
                                User user = new User(jsonObject.getString("firstName"), jsonObject.getString("lastName"), jsonObject.getString("email"), biography, jsonObject.getString("profession"));
                                userList.add(user);
                            }


                            application.userList.clear();
                            progressDialog.dismiss();
                            application.userList.addAll(userList);

                            if (launchSearchActivity) {
                                Intent intent = new Intent(activity, UserSearchBarActivity.class);
                                activity.startActivity(intent);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String err = (error.getMessage() == null) ? "No Jobs found!" : error.getMessage();
                Log.d("Error: ", err);
                Toast.makeText(applicationContext, "No Jobs found!", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(applicationContext);
        requestQueue.add(request);
    }

    /**
     * DELETE request for deleting jobs from employer
     *
     * @param context
     * @param token
     * @param employerToken
     * @param url
     */
    public void makeVolleyDeleteRequest(Context context, String token, String employerToken, String url) {
        final Context applicationContext = context;
        final String requestToken = token;
        final String requestEmployerToken = employerToken;

        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(applicationContext, response, Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(applicationContext, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("token", requestToken);
                headers.put("employerToken", requestEmployerToken);
                return headers;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(applicationContext);
        requestQueue.add(stringRequest);
    }


}

