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
 * Create the RegisterFragment view and attach all event handling to that fragment
 */
public class RegisterFragment extends Fragment implements View.OnClickListener {
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_FIRSTNAME = "firstName";
    public static final String KEY_LASTNAME = "lastName";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_BIO = "bio";
    public static final String KEY_PROFESSION = "profession";

    EditText email, firstName, lastName, password, bio, profession;
    Button login, register;
    String emailtxt, passwordtxt, firstNametxt, lastNametxt, biotxt, professiontxt;


    private OnFragmentInteractionListener mListener;

    public RegisterFragment() {
        // Required empty public constructor
    }

    /**
     *
     * @return newInstance
     */
    public static RegisterFragment newInstance() {
        RegisterFragment fragment = new RegisterFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

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
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        email = (EditText) view.findViewById(R.id.email);
        firstName = (EditText) view.findViewById(R.id.firstName);
        lastName = (EditText) view.findViewById(R.id.lastName);
        password = (EditText) view.findViewById(R.id.password);
        register = (Button) view.findViewById(R.id.registerbtn);
        login = (Button) view.findViewById(R.id.login);
        bio = (EditText) view.findViewById(R.id.userBiography);
        profession = (EditText) view.findViewById(R.id.userProfession);

        register.setOnClickListener(this);
        login.setOnClickListener(this);
        return view;
    }

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
     * register user using Volley POST request
     */
    private void registerUser() {
        emailtxt = email.getText().toString();
        firstNametxt = firstName.getText().toString();
        lastNametxt = lastName.getText().toString();
        passwordtxt = password.getText().toString();
        biotxt = bio.getText().toString();
        professiontxt = profession.getText().toString();

        String url = "http://10.0.2.2:8080/register";

        Map<String, String> params = new HashMap<String, String>();
        params.put(KEY_PASSWORD, passwordtxt);
        params.put(KEY_FIRSTNAME, firstNametxt);
        params.put(KEY_LASTNAME, lastNametxt);
        params.put(KEY_EMAIL, emailtxt);
        params.put(KEY_BIO, biotxt);
        params.put(KEY_PROFESSION, professiontxt);

        VolleyRequest request = new VolleyRequest();
        request.makeVolleyPostRequest(getActivity().getApplicationContext(), params, url);
    }

    /**
     * Launch LoginFragment
     */
    private void loginToApp() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();

        LoginFragment loginFragment = LoginFragment.newInstance();
        ft.replace(R.id.registerFrame, loginFragment);
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
            registerUser();
        } else if (v == login) {
            loginToApp();
        }
    }


}
