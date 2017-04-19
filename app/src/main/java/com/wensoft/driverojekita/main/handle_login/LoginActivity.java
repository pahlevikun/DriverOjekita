package com.wensoft.driverojekita.main.handle_login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.iid.FirebaseInstanceId;
import com.wensoft.driverojekita.config.APIConfig;
import com.wensoft.driverojekita.database.DatabaseHandler;
import com.wensoft.driverojekita.main.MainActivity;
import com.wensoft.driverojekita.R;
import com.wensoft.driverojekita.pojo.FCM;
import com.wensoft.driverojekita.pojo.Profil;
import com.wensoft.driverojekita.singleton.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private TextView tvForgot;
    private Button btLogin;
    private EditText etEmail, etPassword;
    private ProgressDialog loading;
    private String token, username, mail,created_at, editemail, editpassword, token_fcm, phone, is_driver, driver_type;
    private int user_id;
    private DatabaseHandler dataSource;
    private ArrayList<FCM> valuesFCM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = (EditText) findViewById(R.id.editTextEmail);
        etPassword = (EditText) findViewById(R.id.editTextPassword);
        tvForgot = (TextView) findViewById(R.id.tvLandingForgot);
        btLogin = (Button) findViewById(R.id.buttonMasuk);

        dataSource = new DatabaseHandler(this);

        valuesFCM = (ArrayList<FCM>) dataSource.getAllFCMs();
        for(FCM fcm : valuesFCM){
            token_fcm = fcm.getTokenFCM();
        }
        Log.d("TOKEN FCM", ""+token_fcm);
        Log.d("TOKEN FCM",""+FirebaseInstanceId.getInstance().getToken());
        token_fcm = FirebaseInstanceId.getInstance().getToken();

        tvForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, ForgotActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editemail = etEmail.getText().toString().trim();
                editpassword = etPassword.getText().toString().trim();

                if (editemail.isEmpty() || editpassword.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Email / Password masih kosong!", Toast.LENGTH_LONG).show();
                } else {
                    checkLogin(editemail,editpassword,token_fcm);
                }
            }
        });

    }

    private void checkLogin(final String email, final String password, final String tokens) {
        String tag_string_req = "req_login";
        loading = ProgressDialog.show(this,"Mohon Tunggu","Sedang login...",false,false);
        //Toast.makeText(this, ""+email+" "+password, Toast.LENGTH_SHORT).show();
        StringRequest strReq = new StringRequest(Request.Method.POST, APIConfig.API_LOGIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        token = jObj.getString("token");
                        JSONObject data = jObj.getJSONObject("data");
                        user_id = data.getInt("id");
                        username = data.getString("name");
                        mail = data.getString("email");
                        phone = data.getString("phone");
                        created_at = data.getString("created_at");
                        is_driver = data.getString("is_driver");
                        driver_type = data.getString("driver_type");
                        if(is_driver.equals("1")) {
                            dataSource.hapusDbaseProfil();
                            dataSource.tambahProfil(new Profil(user_id, username, mail, token, phone, created_at,driver_type));
                            SharedPreferences sharedPreferences = getSharedPreferences("DATA", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean("session", true);
                            editor.commit();

                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            LoginActivity.this.startActivity(intent);
                            LoginActivity.this.finish();
                        }else{
                            Toast.makeText(LoginActivity.this, "Bukan akun driver!", Toast.LENGTH_SHORT).show();
                        }
                    } else  {
                        String pesan = jObj.getString("message");
                        Toast.makeText(getApplicationContext(), pesan, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json Error : " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Gagal Terhubung ke Server", Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("password", password);
                params.put("fcm_token", tokens);

                return params;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                //Map<String, String> headersSys = super.getHeaders();
                Map<String, String> headers = new HashMap<String, String>();
                //headersSys.remove("Authorization");
                //headers.put("Content-Type", "multipart/form-data");
                //headers.putAll(headersSys);
                return headers;
            }
        };


        int socketTimeout = 40000; // 40 seconds. You can change it
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        strReq.setRetryPolicy(policy);
        AppController.getmInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void hideDialog() {
        if (loading.isShowing())
            loading.dismiss();
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}
