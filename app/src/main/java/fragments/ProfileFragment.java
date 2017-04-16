package fragments;

import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.Context;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import app.com.jobcatcherapp.R;
import requests.VolleyRequest;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment implements View.OnClickListener {

    SharedPreferences pref;
    VolleyRequest request;
    String token, grav, oldpasstxt, newpasstxt;
    Button viewProfile, chgpass, chgpassfr, cancel, logout;
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
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        viewProfile = (Button) view.findViewById(R.id.viewDetails);
        chgpass = (Button) view.findViewById(R.id.chgbtn);
        logout = (Button) view.findViewById(R.id.logout);

        chgpass.setOnClickListener(this);

        viewProfile.setOnClickListener(this);

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
     * "http://developer.android.com/training/basics/fragments/communianacating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

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

    public void configureUserProfile() {
        String url = "http://10.0.2.2:8080/getUserDetails";
        String token = pref.getString("token", "default");

        request = new VolleyRequest();
        Map<String, String> userDetails = request.makeVolleyGetRequest(getActivity().getApplicationContext(), url, token);

        FragmentTransaction ft = getFragmentManager().beginTransaction();

        UserProfileFragment profileFragment = UserProfileFragment.newInstance(userDetails);
        ft.replace(R.id.profileFrame, profileFragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    public void onClick(View v) {
        if (v == chgpass) {
            changePassword();
        } else if (v == chgpassfr) {
            handleChangePasswordFragment();
        } else if (v == viewProfile) {
            configureUserProfile();
        }
    }
}
