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
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EditUserFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EditUserFragment#newInstance} factory method to
 * create an instance of this fragment.
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
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment EditUserFragment.
     */
    public static EditUserFragment newInstance() {
        EditUserFragment fragment = new EditUserFragment();
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

    public void backToUserProfile(){
        String url = "http://10.0.2.2:8080/getUserDetails";
        String token = pref.getString("token", "default");

        request = new VolleyRequest();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        request.makeVolleyGetRequestForUserDetails(getActivity().getApplicationContext(), url, token, ft, R.id.editUserFrame);
    }

    @Override
    public void onClick(View v) {
        if (v == saveEdits) {
            saveEditsToUserProfile();
        }else if(v == backToProfile){
            backToUserProfile();
        }
    }
}
