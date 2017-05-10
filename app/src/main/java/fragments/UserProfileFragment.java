package fragments;

import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
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
public class UserProfileFragment extends Fragment implements View.OnClickListener {


    private OnFragmentInteractionListener mListener;
    TextView userName, email, age, profession, bio;
    Button uploadFile;
    Button selectFile;
    public static String userNameText = "";
    public static String emailText = "";
    public static String ageText = "";
    public static String professionText = "";
    public static String bioText = "";
    ImageView editUser;
    TextView file;
    SharedPreferences pref;
    VolleyRequest request;
    int PICKFILE_REQUEST_CODE = 1;
    Uri selectedFileURI;
    String encodedString;
    byte[] fileByteArray;
    private static final boolean IS_CHUNKED = true;

    public UserProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment UserProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

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

        selectFile = (Button) view.findViewById(R.id.selectfile);
        selectFile.setOnClickListener(this);

        file = (TextView) view.findViewById(R.id.file);

        uploadFile = (Button) view.findViewById(R.id.uploadcv);
        uploadFile.setOnClickListener(this);

        editUser = (ImageView) view.findViewById(R.id.editUser);
        editUser.setOnClickListener(this);

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

    public void editUser() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();

        EditUserFragment editUserFragment = EditUserFragment.newInstance();
        ft.replace(R.id.userProfileFrame, editUserFragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    public void uploadFile() {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Uploading, please wait...");
        progressDialog.show();


        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        FileInputStream fis;
        try {
            String convertedFilePath = getFilePathFromContentUri(selectedFileURI, getActivity().getContentResolver());
            fis = new FileInputStream(new File(convertedFilePath));
            byte[] buf = new byte[1024];
            int n;
            while (-1 != (n = fis.read(buf)))
                baos.write(buf, 0, n);
        } catch (Exception e) {
            e.printStackTrace();
        }
        byte[] bbytes = baos.toByteArray();
        encodedString = Base64.encodeToString(bbytes, Base64.DEFAULT);

        final String encodedBase64 = encodedString;
        String URL = "http://10.0.2.2:8080/upload";

        //sending image to server
        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                progressDialog.dismiss();
                if (s.equals("true")) {
                    Toast.makeText(getActivity().getApplicationContext(), "Upload Successful", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Some error occurred!", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (volleyError != null && volleyError.getMessage() != null) {
                    Toast.makeText(getActivity().getApplicationContext(), "Some error occurred -> " + volleyError, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Some error occurred -> ", Toast.LENGTH_LONG).show();
                }
                ;
            }
        }) {
            //adding parameters to send
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("file", encodedBase64);
                return parameters;
            }
        };

        RequestQueue rQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        rQueue.add(request);
    }

    public void chooseFile() {
        Intent intent = new Intent();
        intent.setType("*/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"), PICKFILE_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            selectedFileURI = data.getData();
            file.setText(getFileName(selectedFileURI));
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    private String getFilePathFromContentUri(Uri selectedUri, ContentResolver contentResolver) {
        String filePath = null;
        if (selectedUri != null && "content".equals(selectedUri.getScheme())) {
            Cursor cursor = contentResolver.query(selectedUri, new String[] { android.provider.MediaStore.Images.ImageColumns.DATA }, null, null, null);
            cursor.moveToFirst();
            filePath = cursor.getString(0);
            cursor.close();
        } else {
            filePath = selectedUri.getPath();
        }
        Log.d("","Chosen path = "+ filePath);
        return filePath;
    }

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    @Override
    public void onClick(View v) {
        if (v == uploadFile) {
            uploadFile();
        } else if (v == editUser) {
            editUser();
        } else if (v == selectFile) {
            chooseFile();
        }
    }
}
