package fragments;

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

import java.util.HashMap;
import java.util.Map;

import app.com.jobcatcherapp.R;
import requests.VolleyRequest;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EditEmployerFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EditEmployerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditEmployerFragment extends Fragment implements View.OnClickListener{

    private OnFragmentInteractionListener mListener;

    EditText companyName, address, latitude, longitude, password;
    Button saveEdits;

    VolleyRequest request;
    SharedPreferences pref;

    public EditEmployerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment EditEmployerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditEmployerFragment newInstance() {
        EditEmployerFragment fragment = new EditEmployerFragment();
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

        View view = inflater.inflate(R.layout.fragment_edit_employer, container, false);

        pref = getActivity().getSharedPreferences("AppPref", MODE_PRIVATE);
        // Inflate the layout for this fragment

        companyName = (EditText) view.findViewById(R.id.editCompanyName);
        password = (EditText) view.findViewById(R.id.editEmployerPassword);
        latitude =  (EditText) view.findViewById(R.id.editLatitude);
        longitude = (EditText) view.findViewById(R.id.editLongitude);
        address = (EditText) view.findViewById(R.id.editAddress);

        saveEdits = (Button) view.findViewById(R.id.editEmployerBtn);
        saveEdits.setOnClickListener(this);
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

    public void saveEditsToEmployerProfile() {
        String url = "http://10.0.2.2:8080/updateEmployer";
        String token = pref.getString("token", "default");

        Map<String, String> requestParameters = new HashMap<String, String>();
        requestParameters.put("companyName", companyName.getText().toString());
        requestParameters.put("address", address.getText().toString());
        requestParameters.put("password", password.getText().toString());
        requestParameters.put("latitude", latitude.getText().toString());
        requestParameters.put("longitude", longitude.getText().toString());

        String postUrl = url + "/token=" + token;

        request = new VolleyRequest();
        request.makeVolleyPostRequest(getActivity().getApplicationContext(), requestParameters, postUrl);
    }

    @Override
    public void onClick(View v) {
        if (v == saveEdits) {
            saveEditsToEmployerProfile();
        }
    }
}
