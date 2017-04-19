package com.wensoft.driverojekita.service;

import android.app.ProgressDialog;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.wensoft.driverojekita.config.APIConfig;
import com.wensoft.driverojekita.config.GPSTracker;
import com.wensoft.driverojekita.database.DatabaseHandler;
import com.wensoft.driverojekita.pojo.Profil;
import com.wensoft.driverojekita.singleton.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by farhan on 3/15/17.
 */

public class BackgroundService extends Service {


    private Handler handler;
    private  int delay = 30000;
    private ArrayList<Profil> valuesProfil;
    private DatabaseHandler dataSource;
    private String token;
    private double latitude, longitude;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.
        handler = new Handler();
        handler.postDelayed(new Runnable(){
            public void run(){
                //do something
                updateLocation();
                handler.postDelayed(this, delay);
            }
        }, delay);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Status non-Aktif!", Toast.LENGTH_LONG).show();
    }


    private void updateLocation() {

        GPSTracker tracker = new GPSTracker(this);
        //if (!tracker.canGetLocation()) {
         //   tracker.showSettingsAlert();
        //} else {
            latitude = tracker.getLatitude();
            longitude = tracker.getLongitude();
        //}

        Log.d("LOKASI",""+latitude+","+longitude);


        dataSource = new DatabaseHandler(this);
        valuesProfil = (ArrayList<Profil>) dataSource.getAllProfils();
        for (Profil profil : valuesProfil){
            token = profil.getToken();
        }

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, APIConfig.API_UPDATE_LOCATION, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {

                    }
                } catch (JSONException e) {
                    //Toast.makeText(BackgroundService.this, ""+e, Toast.LENGTH_SHORT).show();
                    Log.d("ERROR JSONEXception",""+e);
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(BackgroundService.this, ""+error, Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("latitude",String.valueOf(latitude));
                params.put("longitude",String.valueOf(longitude));
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
}