package app.com.jobcatcherapp.activities;

import android.support.v7.app.AppCompatActivity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import app.com.jobcatcherapp.R;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    EditText email, password, res_email, code, newpass;
    Button login, cont, cont_code, cancel, cancel1, register, forpass;
    String emailtxt, passwordtxt, email_res_txt, code_txt, npass_txt;
    SharedPreferences pref;
    Dialog reset;
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_EMAIL = "email";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.loginbtn);
        register = (Button) findViewById(R.id.register);
        forpass = (Button) findViewById(R.id.forgotpass);

        pref = getSharedPreferences("AppPref", MODE_PRIVATE);

        register.setOnClickListener(this);

        login.setOnClickListener(this);

        forpass.setOnClickListener(this);
    }


    public void register() {
        Intent regactivity = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(regactivity);
        finish();
    }

    public void login() {
        emailtxt = email.getText().toString();
        passwordtxt = password.getText().toString();

        String url = "http://10.0.2.2:8080/login";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject josnOBJ = new JSONObject(response);
                            if (josnOBJ.getBoolean("res")) {
                                String token = josnOBJ.getString("token");
                                String grav = josnOBJ.getString("grav");
                                SharedPreferences.Editor edit = pref.edit();
                                //Storing Data using SharedPreferences
                                edit.putString("token", token);
                                edit.putString("grav", grav);
                                edit.commit();
                                Intent profactivity = new Intent(LoginActivity.this, ProfileActivity.class);

                                startActivity(profactivity);
                                finish();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(getApplication(), response, Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(KEY_PASSWORD, passwordtxt);
                params.put(KEY_EMAIL, emailtxt);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void forgotPassword() {
        reset = new Dialog(LoginActivity.this);
        reset.setTitle("Reset Password");
        reset.setContentView(R.layout.reset_password);
        cont = (Button) reset.findViewById(R.id.resbtn);
        cancel = (Button) reset.findViewById(R.id.cancelbtn);
        res_email = (EditText) reset.findViewById(R.id.email);

        cancel.setOnClickListener(this);
        cont.setOnClickListener(this);
        reset.show();
    }

    public void cancel() {
        reset.dismiss();
    }

    public void setListenersForButton(){
        cancel1.setOnClickListener(this);
        cont_code.setOnClickListener(this);
    }

    public void changePassword() {
        email_res_txt = res_email.getText().toString();

        String url = "http://10.0.2.2:8080/api/resetpass";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject josnOBJ = new JSONObject(response);
                            if (josnOBJ.getBoolean("res")) {
                                Log.e("JSON", josnOBJ.toString());
                                Toast.makeText(getApplication(), response, Toast.LENGTH_LONG).show();
                                reset.setContentView(R.layout.reset_passcode);
                                cont_code = (Button) reset.findViewById(R.id.conbtn);
                                code = (EditText) reset.findViewById(R.id.code);
                                newpass = (EditText) reset.findViewById(R.id.npass);
                                cancel1 = (Button) reset.findViewById(R.id.cancel);

                                setListenersForButton();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(getApplication(), response, Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email_res_txt);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }


    public void setCode() {
        code_txt = code.getText().toString();
        npass_txt = newpass.getText().toString();
        Log.e("Code", code_txt);
        Log.e("New pass", npass_txt);

        String url = "http://10.0.2.2:8080/api/resetpass/chg";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject josnOBJ = new JSONObject(response);
                            if (josnOBJ.getBoolean("res")) {
                                reset.dismiss();
                                Toast.makeText(getApplication(), response, Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getApplication(), response, Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LoginActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email_res_txt);
                params.put("code", code_txt);
                params.put("newpass", npass_txt);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }


    @Override
    public void onClick(View v) {
        if (v == register) {
            register();
        } else if (v == login) {
            login();

        } else if (v == forpass) {
            forgotPassword();
        } else if (v == cancel) {
            cancel();
        } else if (v == cont) {
            changePassword();
        } else if (v == cancel1) {
            cancel();
        } else if (v == cont_code) {
            setCode();
        }
    }
}

