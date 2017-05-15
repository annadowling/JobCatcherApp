package fragments;

import android.app.ListFragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import adapters.JobAdapter;
import adapters.JobFilter;
import adapters.UserAdapter;
import adapters.UserFilter;
import app.com.jobcatcherapp.R;
import main.JobCatcherApp;
import requests.VolleyRequest;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UserListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UserListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserListFragment extends ListFragment {
    private UserListFragment.OnFragmentInteractionListener mListener;
    VolleyRequest request;

    protected UserFilter userFilter;
    protected static UserAdapter userAdapter;

    public UserListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment JobListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserListFragment newInstance() {
        UserListFragment fragment = new UserListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        JobCatcherApp app = (JobCatcherApp) getActivity().getApplication();
        userAdapter = new UserAdapter(getActivity().getApplicationContext(), app.userList);
        userFilter = new UserFilter(app.userList, "profession", userAdapter);

        setListAdapter(userAdapter);
    }

    @Override
    public void onStart()
    {
        super.onStart();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
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
}
