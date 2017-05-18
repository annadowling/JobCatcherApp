package fragments;

import android.app.FragmentTransaction;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.HashMap;
import java.util.Map;

import app.com.jobcatcherapp.R;
import requests.VolleyRequest;

/**
 * Created by annadowling on 11/05/2017.
 * Create the EmployerFragment view and attach all event handling to that fragment
 */
public class EmployerFragment extends Fragment implements View.OnClickListener{
    private OnFragmentInteractionListener mListener;

    public static final String KEY_PASSWORD = "password";
    public static final String KEY_COMPANYNAME = "companyName";
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_LATITUDE = "latitude";
    public static final String KEY_LONGITUDE = "longitude";
    EditText email, companyName, password, address, latitude, longitude ;
    Button login, register;
    String emailtxt, passwordtxt, addresstxt, companyNametxt, latitudetxt, longitudetxt;

    public EmployerFragment() {
        // Required empty public constructor
    }

    /**
     *
     * @return newInstance
     */
    public static EmployerFragment newInstance() {
        EmployerFragment fragment = new EmployerFragment();
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
        View view = inflater.inflate(R.layout.fragment_employer, container, false);
        email = (EditText) view.findViewById(R.id.employer_email);
        companyName = (EditText) view.findViewById(R.id.company_name);
        address = (EditText) view.findViewById(R.id.address);
        latitude = (EditText) view.findViewById(R.id.latitude);
        longitude = (EditText) view.findViewById(R.id.longitude);
        password = (EditText) view.findViewById(R.id.employerPassword);
        register = (Button) view.findViewById(R.id.register_employer);
        login = (Button) view.findViewById(R.id.employer_login);

        register.setOnClickListener(this);
        login.setOnClickListener(this);
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
     * register an new employer using Volley POST request
     */
    private void registerEmployer() {
        //email, companyName, address, latitude, longitude, password
        emailtxt = email.getText().toString();
        companyNametxt = companyName.getText().toString();
        addresstxt = address.getText().toString();
        latitudetxt = latitude.getText().toString();
        longitudetxt = longitude.getText().toString();
        passwordtxt = password.getText().toString();
        String url = "http://10.0.2.2:8080/registerEmployer";

        Map<String, String> params = new HashMap<String, String>();
        params.put(KEY_PASSWORD, passwordtxt);
        params.put(KEY_COMPANYNAME, companyNametxt);
        params.put(KEY_ADDRESS, addresstxt);
        params.put(KEY_LATITUDE, latitudetxt);
        params.put(KEY_LONGITUDE, longitudetxt);
        params.put(KEY_EMAIL, emailtxt);

        VolleyRequest request = new VolleyRequest();
        request.makeVolleyPostRequest(getActivity().getApplicationContext(), params, url);
    }

    /**
     * Launch the EmployerPortalFragment
     */
    private void loginToEmployerPortal() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();

        EmployerPortalFragment loginFragment = EmployerPortalFragment.newInstance();
        ft.replace(R.id.employerRegisterFrame, loginFragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    /**
     * Handles on click events
     * @param v
     */
    @Override
    public void onClick(View v) {
        if (v == register) {
            registerEmployer();
        }else if(v == login){
            loginToEmployerPortal();
        }
    }
}
