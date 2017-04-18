package fragments;

import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
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
import app.com.jobcatcherapp.activities.MainActivity;
import requests.VolleyRequest;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EmployerPortalFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EmployerPortalFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EmployerPortalFragment extends Fragment implements View.OnClickListener {


    private OnFragmentInteractionListener mListener;

    EditText email, password;
    Button login, register;
    String emailtxt, passwordtxt;
    VolleyRequest request;
    SharedPreferences pref;
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_EMAIL = "email";

    public EmployerPortalFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static EmployerPortalFragment newInstance() {
        EmployerPortalFragment fragment = new EmployerPortalFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_employer_portal, container, false);
        email = (EditText) view.findViewById(R.id.employer_email);
        password = (EditText) view.findViewById(R.id.employer_password);
        login = (Button) view.findViewById(R.id.employer_loginbtn);
        register =  (Button) view.findViewById(R.id.registerEmployerBtn);

        pref = getActivity().getSharedPreferences("AppPref", MODE_PRIVATE);

        login.setOnClickListener(this);
        register.setOnClickListener(this);

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

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
                                edit.commit();

                                configureEmployerProfile();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(getActivity().getApplicationContext(), "You are logged in!", Toast.LENGTH_LONG).show();
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

    public void launchRegisterPage(){
        FragmentTransaction ft = getFragmentManager().beginTransaction();

        EmployerFragment employerFragment = EmployerFragment.newInstance();
        ft.replace(R.id.employerPortalFrame, employerFragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    public void configureEmployerProfile() {

        String url = "http://10.0.2.2:8080/getEmployerDetails";
        String token = pref.getString("token", "default");

        request = new VolleyRequest();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        request.makeVolleyGetRequestForEmployerDetails(getActivity().getApplicationContext(), url, token, ft);
    }

    @Override
    public void onClick(View v) {
        if (v == login) {
            loginToPortal();
        }else if(v == register){
            launchRegisterPage();
        }
    }
}
