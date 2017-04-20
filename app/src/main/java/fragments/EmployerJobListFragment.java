package fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Map;
import java.util.TreeMap;

import app.com.jobcatcherapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EmployerJobListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EmployerJobListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EmployerJobListFragment extends Fragment implements View.OnClickListener {


    private OnFragmentInteractionListener mListener;

    ImageView delete;
    TextView jobName, jobDescription, contactNumber;
    public static TreeMap<String, String> jobParameters;

    public EmployerJobListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment EmployerJobListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EmployerJobListFragment newInstance(TreeMap<String, String> mapParameters) {
        EmployerJobListFragment fragment = new EmployerJobListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        jobParameters.putAll(mapParameters);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_employer_job_list, container, false);

        for (Map.Entry<String, String> entry : jobParameters.entrySet()) {
            if (entry.getKey().contains("jobName")) {
                jobName = (TextView) view.findViewById(R.id.rowJobName);
                jobName.setText(entry.getValue());
            } else if (entry.getKey().contains("jobDescription")) {
                jobDescription = (TextView) view.findViewById(R.id.rowJobDescription);
                jobDescription.setText(entry.getValue());
            } else if (entry.getKey().contains("contactNumber")) {
                contactNumber = (TextView) view.findViewById(R.id.rowContactNumber);
                contactNumber.setText(entry.getValue());
            }
            delete = (ImageView) view.findViewById(R.id.imgDelete);
            delete.setOnClickListener(this);
        }

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

    @Override
    public void onClick(View v) {
        if(v == delete){
            //TODO
        }
    }
}
