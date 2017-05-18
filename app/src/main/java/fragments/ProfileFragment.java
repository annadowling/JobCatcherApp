package fragments;

import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import app.com.jobcatcherapp.R;
import requests.VolleyRequest;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by annadowling on 11/05/2017.
 * Create the ProfileFragment view and attach all event handling to that fragment
 */
public class ProfileFragment extends Fragment implements View.OnClickListener {

    SharedPreferences pref;
    VolleyRequest request;
    String token, grav, oldpasstxt, newpasstxt;
    Button viewProfile, chgpass, chgpassfr, cancel, logout, linkedin;
    Dialog dlg;
    EditText oldpass, newpass;
    public static final String KEY_OLDPASSWORD = "oldpass";
    public static final String KEY_NEWPASSWORD = "newpass";
    public static final String KEY_ID = "id";

    private OnFragmentInteractionListener mListener;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     *
     * @return newInstance
     */
    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }

    /**
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    /**
     * Create and populate view data associated with the fragment.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        viewProfile = (Button) view.findViewById(R.id.viewDetails);
        chgpass = (Button) view.findViewById(R.id.chgbtn);
        logout = (Button) view.findViewById(R.id.logout);

        chgpass.setOnClickListener(this);

        viewProfile.setOnClickListener(this);
        linkedin = (Button) view.findViewById(R.id.linked_in);
        linkedin.setOnClickListener(this);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor edit = pref.edit();
                //Storing Data using SharedPreferences
                edit.putString("token", "");
                edit.commit();
                FragmentTransaction ft = getFragmentManager().beginTransaction();

                LoginFragment loginFragment = LoginFragment.newInstance();
                ft.replace(R.id.profileFrame, loginFragment);
                ft.commit();
            }
        });

        pref = getActivity().getSharedPreferences("AppPref", MODE_PRIVATE);
        token = pref.getString("token", "");
        grav = pref.getString("grav", "");


        // Inflate the layout for this fragment
        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    /**
     * Change password or cancel password change button listeners.
     */
    private void changePassword() {
        dlg = new Dialog(getActivity());
        dlg.setContentView(R.layout.chgpassword_fragment);
        dlg.setTitle("Change Password");
        chgpassfr = (Button) dlg.findViewById(R.id.chgbtn);

        chgpassfr.setOnClickListener(this);

        cancel = (Button) dlg.findViewById(R.id.cancelbtn);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dlg.dismiss();
            }
        });
        dlg.show();
    }

    /**
     * Handle password change thorugh Volley POST request
     */
    private void handleChangePasswordFragment() {
        oldpass = (EditText) dlg.findViewById(R.id.oldpass);
        newpass = (EditText) dlg.findViewById(R.id.newpass);
        oldpasstxt = oldpass.getText().toString();
        newpasstxt = newpass.getText().toString();

        String url = "http://10.0.2.2:8080/api/chgpass";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject josnOBJ = new JSONObject(response);
                            if (josnOBJ.getBoolean("res")) {
                                dlg.dismiss();
                                Toast.makeText(getActivity().getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getActivity().getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity().getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(KEY_OLDPASSWORD, oldpasstxt);
                params.put(KEY_NEWPASSWORD, newpasstxt);
                params.put(KEY_ID, token);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        requestQueue.add(stringRequest);
    }

    /**
     * Volley GET request for get user profile data
     */
    public void configureUserProfile() {

        String url = "http://10.0.2.2:8080/getUserDetails";
        String token = pref.getString("token", "default");

        request = new VolleyRequest();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        request.makeVolleyGetRequestForUserDetails(getActivity().getApplicationContext(), url, token, ft, R.id.profileFrame);
    }

    /**
     * Launch intent to share to linked in
     */
    public void shareTolinkedIn() {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/*");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "");
        startActivity(Intent.createChooser(sharingIntent, "Share to:"));
    }

    /**
     * Handles on click events
     * @param v
     */
    @Override
    public void onClick(View v) {
        if (v == chgpass) {
            changePassword();
        } else if (v == chgpassfr) {
            handleChangePasswordFragment();
        } else if (v == viewProfile) {
            configureUserProfile();
        } else if (v == linkedin) {
            shareTolinkedIn();
        }
    }
}
