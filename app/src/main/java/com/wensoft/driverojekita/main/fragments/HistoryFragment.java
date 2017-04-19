package com.wensoft.driverojekita.main.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.wensoft.driverojekita.R;
import com.wensoft.driverojekita.adapter.HistoryAdapter;
import com.wensoft.driverojekita.adapter.OrderAdapter;
import com.wensoft.driverojekita.config.APIConfig;
import com.wensoft.driverojekita.database.DatabaseHandler;
import com.wensoft.driverojekita.pojo.History;
import com.wensoft.driverojekita.pojo.Profil;
import com.wensoft.driverojekita.singleton.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by farhan on 2/19/17.
 */

public class HistoryFragment extends Fragment {

    private Button btRefresh;
    private ListView listView;
    private List<History> dataList = new ArrayList<History>();
    private ArrayList<Profil> valuesProfil;
    private DatabaseHandler dataSource;
    private String token;
    private HistoryAdapter adapter;
    private LinearLayout linLayHistory;

    public HistoryFragment() {
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
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        dataSource = new DatabaseHandler(getActivity());
        valuesProfil = (ArrayList<Profil>) dataSource.getAllProfils();

        boolean onOff = getActivity().getSharedPreferences("DATA",MODE_PRIVATE).getBoolean("onoff",true);



        listView = (ListView) view.findViewById(R.id.listViewHistory);
        btRefresh = (Button) view.findViewById(R.id.buttonRefresh);
        linLayHistory = (LinearLayout) view.findViewById(R.id.linLayStatus);


        adapter = new HistoryAdapter(getActivity(), dataList);
        dataList.clear();
        listView.setAdapter(adapter);
        if(onOff){
            btRefresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dataList.clear();
                    makeJsonObjectRequest();
                }
            });
            makeJsonObjectRequest();
            listView.setVisibility(View.VISIBLE);
            linLayHistory.setVisibility(View.GONE);
        }else{
            listView.setVisibility(View.GONE);
            linLayHistory.setVisibility(View.VISIBLE);
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        return view;
    }

    private void makeJsonObjectRequest() {

        for (Profil profil : valuesProfil) {
            token = profil.getToken();
        }
        StringRequest jsonObjReq = new StringRequest(Request.Method.GET, APIConfig.API_GET_HISTORY, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        JSONArray dataArray = jObj.getJSONArray("data");
                        try {
                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject isi = dataArray.getJSONObject(i);
                                String id = isi.getString("order_id");
                                String user_id = isi.getString("invoice_number");
                                String order_type = isi.getString("type");
                                String price = isi.getString("total_price");
                                double Slatitude = isi.getDouble("start_longitude");
                                double Slongitude = isi.getDouble("start_longitude");
                                double Elatitude = isi.getDouble("end_latitude");
                                double Elongitude = isi.getDouble("end_longitude");
                                String address = isi.getString("address");
                                String created_at = isi.getString("created_at");
                                String status = isi.getString("status");
                                dataList.add(new History(String.valueOf(i), id, user_id, order_type, price, Slatitude, Slongitude, Elatitude, Elongitude,address,created_at,status));
                            }
                        } catch (JSONException e) {
                            Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                } catch (JSONException e) {
                    Toast.makeText(getActivity(), "" + e, Toast.LENGTH_SHORT).show();
                }
                adapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
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
        // Adding request to request queue
        AppController.getmInstance().addToRequestQueue(jsonObjReq);
    }

}