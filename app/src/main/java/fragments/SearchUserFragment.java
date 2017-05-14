package fragments;

/**
 * Created by annadowling on 11/05/2017.
 */

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import app.com.jobcatcherapp.R;
import app.com.jobcatcherapp.activities.MainEmployerActivity;


public class SearchUserFragment extends UserListFragment implements AdapterView.OnItemSelectedListener, TextWatcher, View.OnClickListener {

    ImageView backToProfile;

    public SearchUserFragment() {
        // Required empty public constructor
    }

    public static SearchUserFragment newInstance() {
        SearchUserFragment fragment = new SearchUserFragment();
        return fragment;
    }
    @Override
    public void onAttach(Context c) {
        super.onAttach(c);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter
                .createFromResource(getActivity(), R.array.userTypes,
                        android.R.layout.simple_spinner_item);

        spinnerAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinner = ((Spinner) getActivity().findViewById(R.id.searchUserTypeSpinner));
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(this);

        EditText nameText = (EditText) getActivity().findViewById(R.id.filterTextSearchUser);
        nameText.addTextChangedListener(this);

        backToProfile = (ImageView) getActivity().findViewById(R.id.backToProfileUser);
        backToProfile.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selected = parent.getItemAtPosition(position).toString();

        if (selected != null) {
            if (selected.equals("All Types")) {
                userFilter.setFilter("all");
            }else if(selected.equals("By Description")){
                userFilter.setFilter("by description");
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        userFilter.filter(s);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onClick(View v) {
        if (v == backToProfile) {
            Intent intent = new Intent(getActivity(), MainEmployerActivity.class);
            getActivity().startActivity(intent);
        }
    }
}
