package com.wensoft.driverojekita.main;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
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
import com.wensoft.driverojekita.database.DatabaseHandler;
import com.wensoft.driverojekita.pojo.Profil;
import com.wensoft.driverojekita.singleton.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class JobFoodActivity extends AppCompatActivity {


    private ProgressDialog loading;
    private ArrayList<Profil> valuesProfil;
    private DatabaseHandler dataSource;
    private String token;
    private StringBuffer sMenu,sHarga;
    private int total_bayar=0;
    private TextView tvMenu, tvHarga, tvTotal, tvNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_food);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar(). setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        setTitle("Daftar Menu");

        tvMenu = (TextView) findViewById(R.id.textViewMenu);
        tvHarga = (TextView) findViewById(R.id.textViewHarga);
        tvTotal = (TextView) findViewById(R.id.textViewTotalBayar);
        tvNote = (TextView) findViewById(R.id.textViewNote);

        dataSource = new DatabaseHandler(this);
        valuesProfil = (ArrayList<Profil>) dataSource.getAllProfils();
        Intent intent = getIntent();
        String id_order = intent.getStringExtra("order_id");
        String note = intent.getStringExtra("foodnote");

        tvNote.setText("Note : \n"+note);

        sMenu = new StringBuffer();
        sHarga = new StringBuffer();
        makeRequest(id_order);
    }

    private void makeRequest(final String order_id) {

        loading = ProgressDialog.show(this,"Mohon Tunggu","Sedang memuat...",false,false);

        for (Profil profil : valuesProfil){
            token = profil.getToken();
        }

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, APIConfig.API_FOOD_DETAIL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                hideDialog();
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        JSONArray array = jObj.getJSONArray("data");
                        for (int i = 0; i < array.length(); i++){
                            JSONObject data = array.getJSONObject(i);
                            String id = data.getString("id");
                            String nama_menu = data.getString("nama_menu");
                            String harga = data.getString("harga");
                            String jumlah = data.getString("jumlah");
                            String harga_total = data.getString("harga_total");
                            sMenu.append(nama_menu+" x "+jumlah+"\n");
                            sHarga.append("Rp. "+harga_total+",-\n");
                            total_bayar = total_bayar + Integer.parseInt(harga_total);
                        }
                        tvHarga.setText(sHarga);
                        tvMenu.setText(sMenu);
                        tvTotal.setText("Rp. "+total_bayar+",-");
                    }else{
                        Toast.makeText(JobFoodActivity.this, "Gagal memuat!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } catch (JSONException e) {
                    Toast.makeText(JobFoodActivity.this, ""+e, Toast.LENGTH_SHORT).show();
                    Log.d("ERROR",""+e);
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                hideDialog();
                Toast.makeText(JobFoodActivity.this, ""+error, Toast.LENGTH_SHORT).show();
                Log.d("ERROR",""+error);
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", order_id);
                return params;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String bearer = "Bearer " + token;
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", bearer);
                return headers;
            }
        };

        int socketTimeout = 40000; // 40 seconds. You can change it
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjReq.setRetryPolicy(policy);
        AppController.getmInstance().addToRequestQueue(jsonObjReq);
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

    private void hideDialog() {
        if (loading.isShowing())
            loading.dismiss();
    }
}
