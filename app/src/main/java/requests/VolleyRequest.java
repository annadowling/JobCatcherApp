package requests;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by annadowling on 23/02/2017.
 */

public class VolleyRequest {

    private ArrayList<String> responseEntries;

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

    public ArrayList<String> makeVolleyGetRequest(Context context, String url) {
        final Context applicationContext = context;
        JsonArrayRequest request = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            try {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                responseEntries.add(jsonObject.toString());
                            } catch (JSONException e) {
                                responseEntries.add("Error: " + e.getLocalizedMessage());
                            }
                        }

                        //allDone();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(applicationContext, error.toString(), Toast.LENGTH_LONG).show();
                    }
                });

        //mEntries = new ArrayList<>();
        RequestQueue requestQueue = Volley.newRequestQueue(applicationContext);
        requestQueue.add(request);
        return responseEntries;
    }


}

