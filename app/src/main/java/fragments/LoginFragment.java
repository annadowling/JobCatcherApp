package fragments;

import android.app.Activity;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.SupportMapFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import app.com.jobcatcherapp.R;
import app.com.jobcatcherapp.activities.GoogleSignInActivity;
import app.com.jobcatcherapp.activities.MainActivity;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LoginFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends android.app.Fragment implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {
    EditText email, password, res_email, code, newpass;
    Button login, cont, cont_code, cancel, cancel1, register, employerPortal;
    String emailtxt, passwordtxt, email_res_txt, code_txt, npass_txt;
    SharedPreferences pref;
    Dialog reset;
    SignInButton googleSignInButton;
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_EMAIL = "email";
    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;

    private GoogleApiClient mGoogleApiClient;


    private OnFragmentInteractionListener mListener;


    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
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
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        email = (EditText) view.findViewById(R.id.email);
        password = (EditText) view.findViewById(R.id.password);
        login = (Button) view.findViewById(R.id.loginbtn);
        register = (Button) view.findViewById(R.id.register);
        employerPortal = (Button) view.findViewById(R.id.employerPortalButton);
        googleSignInButton = (SignInButton) view.findViewById(R.id.sign_in_button);

        pref = getActivity().getSharedPreferences("AppPref", MODE_PRIVATE);

        register.setOnClickListener(this);

        login.setOnClickListener(this);

        employerPortal.setOnClickListener(this);

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity().getApplicationContext())
                //.enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


        googleSignInButton = (SignInButton) view.findViewById(R.id.sign_in_button);
        googleSignInButton.setOnClickListener(this);
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

    private void googleSignIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void register() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();

        RegisterFragment registerFragment = RegisterFragment.newInstance();
        ft.replace(R.id.loginFrame, registerFragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    public void login() {
        emailtxt = email.getText().toString();
        passwordtxt = password.getText().toString();

        String url = "http://10.0.2.2:8080/login";

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
                                edit.putString("firstName", josnOBJ.getString("firstName"));
                                edit.putString("lastName", josnOBJ.getString("lastName"));
                                edit.putString("email", josnOBJ.getString("email"));
                                edit.commit();

                                Intent intent = new Intent(getActivity(), MainActivity.class);
                                getActivity().startActivity(intent);
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

//    public void forgotPassword() {
//        reset = new Dialog(getActivity());
//        reset.setTitle("Reset Password");
//        reset.setContentView(R.layout.reset_password);
//        cont = (Button) reset.findViewById(R.id.resbtn);
//        cancel = (Button) reset.findViewById(R.id.cancelbtn);
//        res_email = (EditText) reset.findViewById(R.id.email);
//
//        cancel.setOnClickListener(this);
//        cont.setOnClickListener(this);
//        reset.show();
//    }

    public void cancel() {
        reset.dismiss();
    }

    public void setListenersForButton() {
        cancel1.setOnClickListener(this);
        cont_code.setOnClickListener(this);
    }

    public void changePassword() {
        email_res_txt = res_email.getText().toString();

        String url = "http://10.0.2.2:8080/api/resetpass";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject josnOBJ = new JSONObject(response);
                            if (josnOBJ.getBoolean("res")) {
                                Log.e("JSON", josnOBJ.toString());
                                Toast.makeText(getActivity().getApplicationContext(), response, Toast.LENGTH_LONG).show();
                                reset.setContentView(R.layout.reset_passcode);
                                cont_code = (Button) reset.findViewById(R.id.conbtn);
                                code = (EditText) reset.findViewById(R.id.code);
                                newpass = (EditText) reset.findViewById(R.id.npass);
                                cancel1 = (Button) reset.findViewById(R.id.cancel);

                                setListenersForButton();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(getActivity().getApplicationContext(), response, Toast.LENGTH_LONG).show();
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
                params.put("email", email_res_txt);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        requestQueue.add(stringRequest);

    }

    public void setCode() {
        code_txt = code.getText().toString();
        npass_txt = newpass.getText().toString();
        Log.e("Code", code_txt);
        Log.e("New pass", npass_txt);

        String url = "http://10.0.2.2:8080/api/resetpass/chg";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject josnOBJ = new JSONObject(response);
                            if (josnOBJ.getBoolean("res")) {
                                reset.dismiss();
                                Toast.makeText(getActivity().getApplicationContext(), response, Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getActivity().getApplicationContext(), response, Toast.LENGTH_LONG).show();
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
                params.put("email", email_res_txt);
                params.put("code", code_txt);
                params.put("newpass", npass_txt);
                return params;
            }

        };
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(getActivity().getApplicationContext(), "onConnectionFailed", Toast.LENGTH_SHORT).show();
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            //GoogleSignInAccount acct = result.getSignInAccount();
            //mStatusTextView.setText(getString("0x7f1000ec", acct.getDisplayName()));
            updateUI(true);
        } else {
            // Signed out, show unauthenticated UI.
            updateUI(true);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    public void updateUI(Boolean updateUI) {
        if (updateUI) {
            Intent intent = new Intent(getActivity().getApplicationContext(), MainActivity.class);
            LoginFragment.this.startActivity(intent);
        }
    }

    public void launchEmployerPortal() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();

        EmployerPortalFragment portalFragment = EmployerPortalFragment.newInstance();
        ft.replace(R.id.loginFrame, portalFragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    public void onClick(View v) {
        if (v == register) {
            register();
        } else if (v == login) {
            login();
        } else if (v == employerPortal) {
            launchEmployerPortal();
        } else if (v == cancel) {
            cancel();
        } else if (v == cont) {
            changePassword();
        } else if (v == cancel1) {
            cancel();
        } else if (v == cont_code) {
            setCode();
        } else if (v == googleSignInButton) {
            googleSignIn();
        }
    }
}
