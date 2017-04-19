package com.wensoft.driverojekita.main.fragments.handle_profil;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
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
import com.wensoft.driverojekita.main.handle_login.ResetActivity;
import com.wensoft.driverojekita.singleton.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by farhan on 3/4/17.
 */

public class ChangeEmailActivity extends AppCompatActivity {

    private EditText etLupaEmail, etLupaPhone;
    private Button btLupa;
    private String lupaEmail, lupaPhone;
    private ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);
        etLupaEmail = (EditText) findViewById(R.id.editTextEmailForgot);
        etLupaPhone = (EditText) findViewById(R.id.editTextPhoneForgot);
        btLupa = (Button) findViewById(R.id.buttonForgot);

        btLupa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lupaEmail = etLupaEmail.getText().toString().trim();
                lupaPhone = etLupaPhone.getText().toString().trim();
                checkLogin(lupaEmail,lupaPhone);
            }
        });
    }

    private void checkLogin(final String email, final String phone) {
        String tag_string_req = "req_login";
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);

        loading = ProgressDialog.show(this,"Mohon Tunggu","Sedang mengecek...",false,false);

        StringRequest strReq = new StringRequest(Request.Method.POST, APIConfig.API_FORGOT_CEK, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    if (jObj.has("message")) {
                        alert.setTitle("Pemberitahuan");
                        alert.setMessage("Email dan Telepon tidak sama atau tidak terdaftar!");
                        alert.setPositiveButton("Ya",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // TODO Auto-generated method stub

                                    }
                                });
                        alert.show();
                    }else{
                        String token = jObj.getString("token");
                        Intent intent = new Intent(ChangeEmailActivity.this, ResetActivity.class);
                        intent.putExtra("tokenForgot",token);
                        intent.putExtra("emailForgot",email);
                        startActivity(intent);
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
                params.put("phone", phone);

                return params;
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
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id==android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        finish();
    }
}