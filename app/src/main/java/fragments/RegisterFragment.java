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
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RegisterFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment implements View.OnClickListener {
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_FIRSTNAME = "firstName";
    public static final String KEY_LASTNAME = "lastName";
    public static final String KEY_EMAIL = "email";
    EditText email, firstName, lastName, password;
    Button login, register;
    String emailtxt, passwordtxt, firstNametxt, lastNametxt;


    private OnFragmentInteractionListener mListener;

    public RegisterFragment() {
        // Required empty public constructor
    }

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

        register.setOnClickListener(this);
        login.setOnClickListener(this);
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

    private void registerUser() {
        emailtxt = email.getText().toString();
        firstNametxt = firstName.getText().toString();
        lastNametxt = lastName.getText().toString();
        passwordtxt = password.getText().toString();
        String url = "http://10.0.2.2:8080/register";

        Map<String, String> params = new HashMap<String, String>();
        params.put(KEY_PASSWORD, passwordtxt);
        params.put(KEY_FIRSTNAME, firstNametxt);
        params.put(KEY_LASTNAME, lastNametxt);
        params.put(KEY_EMAIL, emailtxt);

        VolleyRequest request = new VolleyRequest();
        request.makeVolleyRequest(getActivity().getApplicationContext(), params, url);
    }

    private void loginToApp() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();

        LoginFragment loginFragment = LoginFragment.newInstance();
        ft.replace(R.id.registerFrame, loginFragment);
        ft.addToBackStack(null);
        ft.commit();
    }


    @Override
    public void onClick(View v) {
        if (v == register) {
            registerUser();
        } else if (v == login) {
            loginToApp();
        }
    }


}
