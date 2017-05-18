package fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
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

import java.util.HashMap;
import java.util.Map;

import app.com.jobcatcherapp.R;
import app.com.jobcatcherapp.activities.MainEmployerActivity;
import requests.VolleyRequest;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by annadowling on 11/05/2017.
 * Create the EmployerPortalFragment view and attach all event handling to that fragment
 */
public class EmployerPortalFragment extends Fragment implements View.OnClickListener {


    private OnFragmentInteractionListener mListener;

    EditText email, password;
    Button login, register, jobseekerPortalButton;
    String emailtxt, passwordtxt;
    VolleyRequest request;
    SharedPreferences pref;
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_EMAIL = "email";

    public EmployerPortalFragment() {
        // Required empty public constructor
    }

    /**
     *
     * @return newInstance
     */
    public static EmployerPortalFragment newInstance() {
        EmployerPortalFragment fragment = new EmployerPortalFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_employer_portal, container, false);
        email = (EditText) view.findViewById(R.id.employer_email);
        password = (EditText) view.findViewById(R.id.employer_password);
        login = (Button) view.findViewById(R.id.employer_loginbtn);
        register = (Button) view.findViewById(R.id.registerEmployerBtn);
        jobseekerPortalButton = (Button) view.findViewById(R.id.jobseekerPortalButton);

        pref = getActivity().getSharedPreferences("AppPref", MODE_PRIVATE);

        login.setOnClickListener(this);
        register.setOnClickListener(this);
        jobseekerPortalButton.setOnClickListener(this);

        return view;
    }

    /**
     *
     * @param uri
     */
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    /**
     *
     * @param context
     */
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
     * Volley POST request for employer login to employer portal
     */
    public void loginToPortal() {
        emailtxt = email.getText().toString();
        passwordtxt = password.getText().toString();

        String url = "http://10.0.2.2:8080/employerLogin";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject josnOBJ = new JSONObject(response);
                            if (josnOBJ.getBoolean("res")) {
                                String token = josnOBJ.getString("token");
                                String grav = josnOBJ.getString("grav");
                                SharedPreferences.Editor edit = pref.edit();
                                //Storing Data using SharedPreferences
                                edit.putString("token", token);
                                edit.putString("grav", grav);
                                edit.putString("companyName", josnOBJ.getString("companyName"));
                                edit.putString("companyEmail", josnOBJ.getString("email"));
                                edit.remove("imagepath");
                                edit.commit();

                                configureEmployerProfile();
                                Toast.makeText(getActivity().getApplicationContext(), "You are logged in!", Toast.LENGTH_LONG).show();
                            }else{
                                Toast.makeText(getActivity().getApplicationContext(), "Password is incorrect!", Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(KEY_PASSWORD, passwordtxt);
                params.put(KEY_EMAIL, emailtxt);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        requestQueue.add(stringRequest);
    }

    /**
     * Launches the EmployerFragment
     */
    public void launchRegisterPage() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();

        EmployerFragment employerFragment = EmployerFragment.newInstance();
        ft.replace(R.id.employerPortalFrame, employerFragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    /**
     * Launches the MainEmployerActivity
     */
    public void configureEmployerProfile() {
        Intent intent = new Intent(getActivity(), MainEmployerActivity.class);
        getActivity().startActivity(intent);
    }

    /**
     * Launches the LoginFragment
     */
    public void launchUserLogin(){
        FragmentTransaction ft = getFragmentManager().beginTransaction();

        LoginFragment loginFragment = LoginFragment.newInstance();
        ft.replace(R.id.employerPortalFrame, loginFragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    /**
     *  Handles on click events
     * @param v
     */
    @Override
    public void onClick(View v) {
        if (v == login) {
            loginToPortal();
        } else if (v == register) {
            launchRegisterPage();
        }else if(v == jobseekerPortalButton){
            launchUserLogin();
        }
    }
}
