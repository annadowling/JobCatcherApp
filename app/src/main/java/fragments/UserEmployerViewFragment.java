package fragments;

import android.annotation.SuppressLint;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.net.Uri;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import android.util.Base64;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import app.com.jobcatcherapp.R;
import requests.VolleyRequest;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UserProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UserProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserEmployerViewFragment extends Fragment implements View.OnClickListener {


    private OnFragmentInteractionListener mListener;
    TextView userName, email, age, profession, bio;

    public static String userNameText = "";
    public static String emailText = "";
    public static String professionText = "";
    public static String bioText = "";
    ImageView contactUser;
    VolleyRequest request;


    public UserEmployerViewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment UserProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserEmployerViewFragment newInstance(Map<String, String> userDetails) {
        UserEmployerViewFragment fragment = new UserEmployerViewFragment();
        userNameText = userDetails.get("firstName") + " " + userDetails.get("lastName");
        emailText = userDetails.get("email");
        professionText = userDetails.get("profession");
        bioText = userDetails.get("bio");
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

        View view = inflater.inflate(R.layout.fragment_user_employer_view, container, false);

        userName = (TextView) view.findViewById(R.id.user_profile_name);
        userName.setText(userNameText);
        email = (TextView) view.findViewById(R.id.user_profile_email);
        email.setText(emailText);

        profession = (TextView) view.findViewById(R.id.userProfession);
        profession.setText(professionText);

        bio = (TextView) view.findViewById(R.id.userBio);
        bio.setText(bioText);

        contactUser = (ImageView) view.findViewById(R.id.contactUser);
        contactUser.setOnClickListener(this);

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
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void contactUser() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();

        ContactFragment contactFragment = ContactFragment.newInstance();
        ft.replace(R.id.userProfileEmployerFrame, contactFragment);
        ft.addToBackStack(null);
        ft.commit();
    }


    @Override
    public void onClick(View v) {

        if (v == contactUser) {
            contactUser();
        }
    }
}
