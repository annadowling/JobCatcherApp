package fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Map;

import app.com.jobcatcherapp.R;
import requests.VolleyRequest;


/**
 * Created by annadowling on 11/05/2017.
 * Create the UserProfileFragment view and attach all event handling to that fragment
 */
public class UserProfileFragment extends Fragment implements View.OnClickListener {


    private OnFragmentInteractionListener mListener;
    TextView userName, email, age, profession, bio;
    public static String userNameText = "";
    public static String emailText = "";
    public static String ageText = "";
    public static String professionText = "";
    public static String bioText = "";
    ImageView editUser;
    VolleyRequest request;

    public UserProfileFragment() {
        // Required empty public constructor
    }

    /**
     *
     * @param userDetails
     * @return
     */
    public static UserProfileFragment newInstance(Map<String, String> userDetails) {
        UserProfileFragment fragment = new UserProfileFragment();
        userNameText = userDetails.get("firstName") + " " + userDetails.get("lastName");
        emailText = userDetails.get("email");
        ageText = userDetails.get("age");
        professionText = userDetails.get("profession");
        bioText = userDetails.get("bio");
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

        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);

        userName = (TextView) view.findViewById(R.id.user_profile_name);
        userName.setText(userNameText);
        email = (TextView) view.findViewById(R.id.user_profile_email);
        email.setText(emailText);

        age = (TextView) view.findViewById(R.id.userAge);
        age.setText(ageText);

        profession = (TextView) view.findViewById(R.id.userProfession);
        profession.setText(professionText);

        bio = (TextView) view.findViewById(R.id.userBio);
        bio.setText(bioText);

        editUser = (ImageView) view.findViewById(R.id.editUser);
        editUser.setOnClickListener(this);

        // Inflate the layout for this fragment
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
     * Launch EditUserFragment
     */
    public void editUser() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();

        EditUserFragment editUserFragment = EditUserFragment.newInstance();
        ft.replace(R.id.userProfileFrame, editUserFragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    /**
     * Handles click events
     * @param v
     */
    @Override
    public void onClick(View v) {

      if (v == editUser) {
            editUser();
        }
    }
}
