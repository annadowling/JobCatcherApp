package requests;

import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import app.com.jobcatcherapp.R;
import fragments.UserProfileFragment;

/**
 * Created by annadowling on 23/02/2017.
 */

public class VolleyRequest {

    TextView userName, userEmail;


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

    public void makeVolleyGetRequestForUserDetails(Context context, String url, String token, FragmentTransaction ft) {
        final Context applicationContext = context;
        final FragmentTransaction fragmentTransaction = ft;

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


                            UserProfileFragment profileFragment = UserProfileFragment.newInstance(responseEntries);
                            fragmentTransaction.replace(R.id.profileFrame, profileFragment);
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

    public void uploadFileVolleyRequest(Context context, String url, String token) {
//        final Context applicationContext = context;
//
//        ImageRequest imgRequest = new ImageRequest(new String(url.toString()),
//                new Response.Listener<Bitmap>() {
//                    @Override
//                    public void onResponse(Bitmap response) {
//                        app.googlePhoto = response;
//                        //googlePhoto.setImageBitmap(app.googlePhoto);
//                    }
//                }, 0, 0, ImageView.ScaleType.FIT_XY, Bitmap.Config.ARGB_8888, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                System.out.println("Something went wrong!");
//                error.printStackTrace();
//            }
//        });
//
//// Add the request to the queue
//        RequestQueue requestQueue = Volley.newRequestQueue(applicationContext);
//        requestQueue.add(imgRequest);

    }


}

