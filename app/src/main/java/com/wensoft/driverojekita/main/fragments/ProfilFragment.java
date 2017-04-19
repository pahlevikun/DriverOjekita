package com.wensoft.driverojekita.main.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.wensoft.driverojekita.main.JobActivity;
import com.wensoft.driverojekita.main.fragments.handle_profil.ChangeEmailActivity;
import com.wensoft.driverojekita.main.fragments.handle_profil.ChangeProfilActivity;
import com.wensoft.driverojekita.main.handle_login.LandingActivity;
import com.wensoft.driverojekita.pojo.FCM;
import com.wensoft.driverojekita.pojo.Profil;
import com.wensoft.driverojekita.service.BackgroundService;
import com.wensoft.driverojekita.singleton.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by farhan on 2/19/17.
 */

public class ProfilFragment extends Fragment {

    private TextView tvLogout,tvPassword, tvNama, tvEmail, tvPhone, tvSaldo;
    private Button btProfil, btOnoff, btSaldo;
    private DatabaseHandler dataSource;
    private ArrayList<Profil> valuesProfil;
    private String nama, email, telepon, token;
    private ProgressDialog loading;

    public ProfilFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_profile, container, false);

        dataSource = new DatabaseHandler(getActivity());
        valuesProfil = (ArrayList<Profil>) dataSource.getAllProfils();
        for(Profil profil : valuesProfil){
            nama = profil.getUsername();
            email = profil.getEmail();
            telepon = profil.getTelepon();
        }


        tvNama = (TextView) view.findViewById(R.id.textNamaUser);
        tvEmail = (TextView) view.findViewById(R.id.textEmailUser);
        tvPhone = (TextView) view.findViewById(R.id.textPhoneUser);
        tvLogout = (TextView) view.findViewById(R.id.textLogOut);
        tvPassword = (TextView) view.findViewById(R.id.textSandi);
        tvSaldo = (TextView) view.findViewById(R.id.tvSaldo);
        btSaldo = (Button) view.findViewById(R.id.buttonUpdateSaldo);
        btProfil = (Button) view.findViewById(R.id.buttonUbahProfil);
        btOnoff = (Button) view.findViewById(R.id.buttonOnOff);

        tvNama.setText(nama);
        tvEmail.setText(email);
        tvPhone.setText(telepon);
        saldo();
        btSaldo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saldo();
            }
        });

        btProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ChangeProfilActivity.class);
                startActivity(intent);
            }
        });

        final boolean onOff = getActivity().getSharedPreferences("DATA",MODE_PRIVATE).getBoolean("onoff",true);
        if(onOff){
            btOnoff.setText("Ubah Status menjadi non-Aktif");
            Intent i=new Intent(getActivity(),BackgroundService.class);
            getActivity().startService(i);
        }else if(!onOff){
            btOnoff.setText("Ubah Status menjadi Aktif");
            Intent i=new Intent(getActivity(),BackgroundService.class);
            getActivity().stopService(i);
        }

        btOnoff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onOff){
                    SharedPreferences sharedPreferences =  getActivity().getSharedPreferences("DATA",MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("onoff",false);
                    editor.commit();
                }else if(!onOff){
                    SharedPreferences sharedPreferences =  getActivity().getSharedPreferences("DATA",MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("onoff",true);
                    editor.commit();
                }
            }
        });

        tvPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ChangeEmailActivity.class);
                startActivity(intent);
            }
        });

        tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataSource.hapusDbaseProfil();
                dataSource.close();

                SharedPreferences sharedPreferences =  getActivity().getSharedPreferences("DATA",MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("session",false);
                editor.commit();

                dataSource.hapusDbaseProfil();

                Intent intent = new Intent(getActivity(), LandingActivity.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                getActivity().finish();
            }
        });

        return view;
    }

    private void saldo() {

        //loading = ProgressDialog.show(getActivity(),"Mohon Tunggu","Sedang mengakhiri order...",false,false);

        for (Profil profil : valuesProfil){
            token = profil.getToken();
        }

        StringRequest jsonObjReq = new StringRequest(Request.Method.GET, APIConfig.API_SALDO, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                //hideDialog();
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        String deposit = jObj.getString("deposit");
                        tvSaldo.setText("Deposit : Rp. "+deposit+",-");
                    }else{
                        Toast.makeText(getActivity(), "Gagal update Saldo!", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(getActivity(), ""+e, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //hideDialog();
                Toast.makeText(getActivity(), ""+error, Toast.LENGTH_SHORT).show();
            }
        }){
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