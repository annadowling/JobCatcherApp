package app.com.jobcatcherapp.activities;

/**
 * Created by annadowling on 06/02/2017.
 */

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
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


public class ProfileActivity extends AppCompatActivity implements View.OnClickListener{


    SharedPreferences pref;
    String token, grav, oldpasstxt, newpasstxt;
    WebView web;
    Button chgpass, chgpassfr, cancel, logout;
    Dialog dlg;
    EditText oldpass, newpass;
    public static final String KEY_OLDPASSWORD = "oldpass";
    public static final String KEY_NEWPASSWORD = "newpass";
    public static final String KEY_ID = "id";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        web = (WebView) findViewById(R.id.webView);
        chgpass = (Button) findViewById(R.id.chgbtn);
        logout = (Button) findViewById(R.id.logout);

        chgpass.setOnClickListener(this);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor edit = pref.edit();
                //Storing Data using SharedPreferences
                edit.putString("token", "");
                edit.commit();
                Intent loginactivity = new Intent(ProfileActivity.this, LoginActivity.class);

                startActivity(loginactivity);
                finish();
            }
        });

        pref = getSharedPreferences("AppPref", MODE_PRIVATE);
        token = pref.getString("token", "");
        grav = pref.getString("grav", "");

        web.getSettings().setUseWideViewPort(true);
        web.getSettings().setLoadWithOverviewMode(true);
        web.loadUrl(grav);
    }

    private void changePassword(){
                dlg = new Dialog(ProfileActivity.this);
                dlg.setContentView(R.layout.chgpassword_fragment);
                dlg.setTitle("Change Password");
                chgpassfr = (Button) dlg.findViewById(R.id.chgbtn);

                chgpassfr.setOnClickListener(this);

                cancel = (Button) dlg.findViewById(R.id.cancelbtn);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dlg.dismiss();
                    }
                });
                dlg.show();
            }

    private void handleChangePasswordFragment(){
                oldpass = (EditText) dlg.findViewById(R.id.oldpass);
                newpass = (EditText) dlg.findViewById(R.id.newpass);
                oldpasstxt = oldpass.getText().toString();
                newpasstxt = newpass.getText().toString();

                String url = "http://10.0.2.2:8080/api/chgpass";

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if (response.contains("\"res\":true")) {
                                    dlg.dismiss();
                                    Toast.makeText(getApplication(), response, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplication(), response, Toast.LENGTH_SHORT).show();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(ProfileActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put(KEY_OLDPASSWORD, oldpasstxt);
                        params.put(KEY_NEWPASSWORD, newpasstxt);
                        params.put(KEY_ID, token);
                        return params;
                    }

                };

                RequestQueue requestQueue = Volley.newRequestQueue(this);
                requestQueue.add(stringRequest);
    }



    @Override
    public void onClick(View v) {
        if(v == chgpass){
            changePassword();
        }else if(v == chgpassfr){
            handleChangePasswordFragment();
        }
    }
}
