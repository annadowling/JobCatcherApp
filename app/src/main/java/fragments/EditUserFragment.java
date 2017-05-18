package fragments;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.HashMap;
import java.util.Map;

import app.com.jobcatcherapp.R;
import requests.VolleyRequest;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by annadowling on 11/05/2017.
 * Create the EditUserFragment view and attach all event handling to that fragment
 */
public class EditUserFragment extends Fragment implements View.OnClickListener {

    private OnFragmentInteractionListener mListener;

    EditText firstName, lastName, password, age, bio, profession;
    Button saveEdits;
    ImageView backToProfile;

    VolleyRequest request;
    SharedPreferences pref;

    public EditUserFragment() {
        // Required empty public constructor
    }

    /**
     * @return A new instance of fragment EditUserFragment.
     */
    public static EditUserFragment newInstance() {
        EditUserFragment fragment = new EditUserFragment();
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
        View view = inflater.inflate(R.layout.fragment_edit_user, container, false);

        pref = getActivity().getSharedPreferences("AppPref", MODE_PRIVATE);

        firstName = (EditText) view.findViewById(R.id.editFirstName);
        lastName = (EditText) view.findViewById(R.id.editLastName);
        password = (EditText) view.findViewById(R.id.editPassword);
        age = (EditText) view.findViewById(R.id.editAge);
        bio = (EditText) view.findViewById(R.id.editBio);
        profession = (EditText) view.findViewById(R.id.editProfession);

        saveEdits = (Button) view.findViewById(R.id.editProfileBtn);
        saveEdits.setOnClickListener(this);

        backToProfile = (ImageView) view.findViewById(R.id.backToUserProfile);
        backToProfile.setOnClickListener(this);
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
     * Save edits to the user model using Volley POST request.
     */
    public void saveEditsToUserProfile() {
        String url = "http://10.0.2.2:8080/updateUser";
        String token = pref.getString("token", "default");

        Map<String, String> requestParameters = new HashMap<String, String>();
        requestParameters.put("firstName", firstName.getText().toString());
        requestParameters.put("lastName", lastName.getText().toString());
        requestParameters.put("password", password.getText().toString());
        requestParameters.put("age", age.getText().toString());
        requestParameters.put("bio", bio.getText().toString());
        requestParameters.put("profession", profession.getText().toString());

        String postUrl = url + "/token=" + token;

        request = new VolleyRequest();
        request.makeVolleyPostRequest(getActivity().getApplicationContext(), requestParameters, postUrl);
    }

    /**
     * Refresh user profile data using Volley GET request
     */
    public void backToUserProfile(){
        String url = "http://10.0.2.2:8080/getUserDetails";
        String token = pref.getString("token", "default");

        request = new VolleyRequest();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        request.makeVolleyGetRequestForUserDetails(getActivity().getApplicationContext(), url, token, ft, R.id.editUserFrame);
    }

    /**
     * Handles on click events
     */
    @Override
    public void onClick(View v) {
        if (v == saveEdits) {
            saveEditsToUserProfile();
        }else if(v == backToProfile){
            backToUserProfile();
        }
    }
}
