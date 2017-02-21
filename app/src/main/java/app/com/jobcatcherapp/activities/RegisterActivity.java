package app.com.jobcatcherapp.activities;

/**
 * Created by annadowling on 06/02/2017.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import app.com.jobcatcherapp.R;
import fragments.LoginFragment;


public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_FIRSTNAME = "firstName";
    public static final String KEY_LASTNAME = "lastName";
    public static final String KEY_EMAIL = "email";
    EditText email, firstName, lastName, password;
    Button login, register;
    String emailtxt, passwordtxt, firstNametxt, lastNametxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email = (EditText) findViewById(R.id.email);
        firstName = (EditText) findViewById(R.id.firstName);
        lastName = (EditText) findViewById(R.id.lastName);
        password = (EditText) findViewById(R.id.password);
        register = (Button) findViewById(R.id.registerbtn);
        login = (Button) findViewById(R.id.login);

        register.setOnClickListener(this);
        login.setOnClickListener(this);

    }

    private void registerUser() {
        emailtxt = email.getText().toString();
        firstNametxt = firstName.getText().toString();
        lastNametxt = lastName.getText().toString();
        passwordtxt = password.getText().toString();
        String url = "http://10.0.2.2:8080/register";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(RegisterActivity.this, response, Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(RegisterActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(KEY_PASSWORD, passwordtxt);
                params.put(KEY_FIRSTNAME, firstNametxt);
                params.put(KEY_LASTNAME, lastNametxt);
                params.put(KEY_EMAIL, emailtxt);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void loginToApp() {
        Intent regactivity = new Intent(RegisterActivity.this, LoginFragment.class);
        startActivity(regactivity);
        finish();
    }

    @Override
    public void onClick(View v) {
        if (v == register) {
            registerUser();
        } else if (v == login) {
            loginToApp();
        }
    }

}