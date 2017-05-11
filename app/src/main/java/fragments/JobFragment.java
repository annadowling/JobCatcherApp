package fragments;

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
import app.com.jobcatcherapp.activities.Base;
import requests.VolleyRequest;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link JobFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link JobFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class JobFragment extends Fragment implements View.OnClickListener {

    private OnFragmentInteractionListener mListener;
    EditText email, phone, jobTitle, jobDescription;
    Button addJob;

    public JobFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment JobFragment.
     */
    public static JobFragment newInstance() {
        JobFragment fragment = new JobFragment();
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
        View view = inflater.inflate(R.layout.fragment_job, container, false);
        email = (EditText) view.findViewById(R.id.employerEmail);
        phone = (EditText) view.findViewById(R.id.phone);
        jobTitle = (EditText) view.findViewById(R.id.jobTitle);
        jobDescription = (EditText) view.findViewById(R.id.jobDescription);
        addJob = (Button) view.findViewById(R.id.addjob);

        addJob.setOnClickListener(this);

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

    private void addJob() {
        final String emailTxt = email.getText().toString();
        final String phoneTxt = phone.getText().toString();
        final String jobTitleTxt = jobTitle.getText().toString();
        final String jobDescriptionTxt = jobDescription.getText().toString();

        String url = "http://10.0.2.2:8080/addJob";

        Map<String, String> params = new HashMap<String, String>();
        params.put("email", emailTxt);
        params.put("contactNumber", phoneTxt);
        params.put("jobTitle", jobTitleTxt);
        params.put("jobDescription", jobDescriptionTxt);

        VolleyRequest request = new VolleyRequest();
        request.makeVolleyPostRequest(getActivity().getApplicationContext(), params, url);
    }


    @Override
    public void onClick(View v) {
        if (v == addJob) {
            addJob();
        }
    }
}
