package com.wensoft.driverojekita.main.handle_login;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.wensoft.driverojekita.R;
import com.wensoft.driverojekita.config.APIConfig;
import com.wensoft.driverojekita.singleton.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ResetActivity extends AppCompatActivity {


    private EditText etPassword, etPasswordConfirm;
    private Button btLupa;
    private String lupaPassword, lupaPasswordConfirm, token, email;
    private ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset);

        etPassword = (EditText) findViewById(R.id.editTextPasswordReset);
        etPasswordConfirm = (EditText) findViewById(R.id.editTextKonfirmasiReset);
        btLupa = (Button) findViewById(R.id.buttonReset);

        Intent ambil = getIntent();
        token =ambil.getStringExtra("tokenForgot");
        email =ambil.getStringExtra("emailForgot");

        btLupa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                lupaPassword = etPassword.getText().toString().trim();
                lupaPasswordConfirm = etPasswordConfirm.getText().toString().trim();
                if(!lupaPassword.equals(lupaPasswordConfirm)||(lupaPassword.length()<6&&lupaPasswordConfirm.length()<6)){
                    Toast.makeText(ResetActivity.this, "Password berbeda atau kurang dari 6 digit!", Toast.LENGTH_LONG).show();
                    etPassword.setText("");
                    etPasswordConfirm.setText("");
                }else{
                    checkLogin(lupaPassword, token, email);
                }
            }
        });
    }

    private void checkLogin(final String password, final String token, final String eMail) {
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        String tag_string_req = "req_login";
        loading = ProgressDialog.show(this,"Mohon Tunggu","Sedang memperbaharui...",false,false);

        //Buat memulai Request, AppConfig.API_LOGIN itu url untuk api login
        StringRequest strReq = new StringRequest(Request.Method.POST, APIConfig.API_FORGOT_CHANGE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                hideDialog();

                //Mulai parsing json dari response API
                try {
                    JSONObject jObj = new JSONObject(response);
                    String message = jObj.getString("message");
                    if (message.equals("Your password has been reset!")) {
                        Intent intent = new Intent(ResetActivity.this, LandingActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                        Toast.makeText(ResetActivity.this, "Password berhasil direset!", Toast.LENGTH_SHORT).show();
                    }else{
                        alert.setTitle("Pemberitahuan");
                        alert.setMessage("Silahkan cek kembali!");
                        alert.setPositiveButton("Ya",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // TODO Auto-generated method stub

                                    }
                                });
                        alert.show();


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
            //Untuk post data menggunakan volley
            //Data yang dikirim adalah email dan password
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("password", password);
                params.put("password_confirmation", password);
                params.put("token", token);
                params.put("email", eMail);

                return params;
            }
            private Map<String, String> checkParams(Map<String, String> map){
                Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<String, String> pairs = (Map.Entry<String, String>)it.next();
                    if(pairs.getValue()==null){
                        map.put(pairs.getKey(), "");
                    }
                }
                return map;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headersSys = super.getHeaders();
                Map<String, String> headers = new HashMap<String, String>();
                headersSys.remove("accept");
                headers.put("accept", "application/json");
                headers.putAll(headersSys);
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
