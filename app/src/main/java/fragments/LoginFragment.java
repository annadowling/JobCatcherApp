package fragments;

import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import app.com.jobcatcherapp.R;
import app.com.jobcatcherapp.activities.MainActivity;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by annadowling on 11/05/2017.
 * Create the LoginFragment view and attach all event handling to that fragment
 */
public class LoginFragment extends android.app.Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {
    EditText email, password, res_email, code, newpass;
    Button login, cont, cont_code, cancel, cancel1, register, employerPortal;
    String emailtxt, passwordtxt, email_res_txt, code_txt, npass_txt;
    SharedPreferences pref;
    Dialog reset;
    SignInButton googleSignInButton;
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_EMAIL = "email";
    private static final String TAG = "SignInActivity";
    Intent intentData;

    /* Request code used to invoke sign in user interactions. */
    public static final int RC_SIGN_IN = 0;

    /* Client used to interact with Google APIs. */
    private GoogleApiClient mGoogleApiClient;

    /* A flag indicating that a PendingIntent is in progress and prevents
     * us from starting further intents.
     */
    private boolean mIntentInProgress;

    /**
     * True if the sign-in button was clicked.  When true, we know to resolve all
     * issues preventing sign-in without waiting.
     */
    private boolean mSignInClicked;

    private OnFragmentInteractionListener mListener;


    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * @return newInstance
     */
    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Create and populate view data associated with the fragment.
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
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


        googleSignInButton = (SignInButton) view.findViewById(R.id.sign_in_button);
        googleSignInButton.setOnClickListener(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity().getApplicationContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);

        return view;
    }

    /**
     * @param uri
     */
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    /**
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

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect(GoogleApiClient.SIGN_IN_MODE_OPTIONAL);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("Login");
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    /**
     * @param bundle
     */
    @Override
    public void onConnected(Bundle bundle) {
        mSignInClicked = false;
        Toast.makeText(getActivity(), "User is connected!", Toast.LENGTH_LONG).show();
    }

    /**
     * @param i
     */
    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect(GoogleApiClient.SIGN_IN_MODE_OPTIONAL);
    }

    /**
     * @param connectionResult
     */
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (!mIntentInProgress) {
            if (mSignInClicked && connectionResult.hasResolution()) {
                // The user has already clicked 'sign-in' so we attempt to resolve all
                // errors until the user is signed in, or they cancel.
                try {
                    connectionResult.startResolutionForResult(((MainActivity) getActivity()), RC_SIGN_IN);
                    mIntentInProgress = true;
                } catch (IntentSender.SendIntentException e) {
                    // The intent was canceled before it was sent.  Return to the default
                    // state and attempt to connect to get an updated ConnectionResult.
                    mIntentInProgress = false;
                    mGoogleApiClient.connect(GoogleApiClient.SIGN_IN_MODE_OPTIONAL);
                }
            }
        }
    }

    /**
     * Set the goolesign in connection data
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_SIGN_IN) {
            if (resultCode != MainActivity.RESULT_OK) {
                mSignInClicked = false;
            }

            intentData = data;
            mIntentInProgress = false;

            if (!mGoogleApiClient.isConnected()) {
                mGoogleApiClient.reconnect();
            }
        }
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    /**
     * Launch the RegisterFragment
     */
    public void register() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();

        RegisterFragment registerFragment = RegisterFragment.newInstance();
        ft.replace(R.id.loginFrame, registerFragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    /**
     * Authenticate using Volley POST request
     */
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
                                edit.remove("imagepath");
                                edit.commit();
                                Toast.makeText(getActivity().getApplicationContext(), "You are logged in!", Toast.LENGTH_LONG).show();

                                Intent intent = new Intent(getActivity(), MainActivity.class);
                                getActivity().startActivity(intent);
                            } else {
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

    public void cancel() {
        reset.dismiss();
    }

    public void setListenersForButton() {
        cancel1.setOnClickListener(this);
        cont_code.setOnClickListener(this);
    }

    /**
     * Change password using Volley POST request
     */
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

    /**
     * Set new password using Volley POST request
     */
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

    /**
     * Launch EmployerPortalFragment
     */
    public void launchEmployerPortal() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();

        EmployerPortalFragment portalFragment = EmployerPortalFragment.newInstance();
        ft.replace(R.id.loginFrame, portalFragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    /**
     * Handle on click events
     *
     * @param v
     */
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
        } else if (v == googleSignInButton && !mGoogleApiClient.isConnecting()) {
            mSignInClicked = true;
            mGoogleApiClient.connect(GoogleApiClient.SIGN_IN_MODE_OPTIONAL);
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(intentData);
            handleSignInResult(result);
        }
    }

    /**
     * Handles googlesignin results
     *
     * @param result
     */
    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            Log.d("accountTag", "account is " + acct.getDisplayName());
            SharedPreferences.Editor edit = pref.edit();
            edit.clear();
            edit.putString("firstName", acct.getDisplayName().split(" ")[0]);
            edit.putString("lastName", acct.getDisplayName().split(" ")[1]);
            edit.putString("email", acct.getEmail());
            if (acct.getPhotoUrl() != null) {
                edit.putString("imagepath", acct.getPhotoUrl().toString());
            }
            edit.commit();

            updateUI(true);
        } else {
            // Signed out, show unauthenticated UI.
            updateUI(false);
        }
    }

    /**
     * Determines whether to launch MainActivity using sign in boolean
     *
     * @param signedIn
     */
    public void updateUI(boolean signedIn) {
        if (signedIn) {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
        }
    }


}
