package fragments;

import android.app.ListFragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import adapters.UserAdapter;
import adapters.UserFilter;
import main.JobCatcherApp;
import requests.VolleyRequest;

/**
 * Created by annadowling on 11/05/2017.
 * Create the UserListFragment view and attach all event handling to that fragment
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
     *
     * @return
     */
    public static UserListFragment newInstance() {
        UserListFragment fragment = new UserListFragment();
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
     * Set the filter and adapter
     */
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

    /**
     *
     * @param uri
     */
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


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
