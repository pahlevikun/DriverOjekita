package com.wensoft.driverojekita.main.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
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
import com.wensoft.driverojekita.adapter.OrderAdapter;
import com.wensoft.driverojekita.config.APIConfig;
import com.wensoft.driverojekita.database.DatabaseHandler;
import com.wensoft.driverojekita.main.JobActivity;
import com.wensoft.driverojekita.pojo.Order;
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

public class OrderFragment extends Fragment {

    private Button btRefresh;
    private ListView listView;
    private List<Order> dataList = new ArrayList<Order>();
    private ArrayList<Profil> valuesProfil;
    private DatabaseHandler dataSource;
    private String token,foodnote,driver_type;
    private OrderAdapter adapter;
    private LinearLayout linLayNyala;

    public OrderFragment() {
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
        View view = inflater.inflate(R.layout.fragment_order, container, false);

        dataSource = new DatabaseHandler(getActivity());
        valuesProfil = (ArrayList<Profil>) dataSource.getAllProfils();
        boolean onOff = getActivity().getSharedPreferences("DATA",MODE_PRIVATE).getBoolean("onoff",true);

        listView = (ListView) view.findViewById(R.id.listViewOrder);
        btRefresh = (Button) view.findViewById(R.id.buttonRefresh);
        linLayNyala = (LinearLayout) view.findViewById(R.id.linLayStatus);


        adapter = new OrderAdapter(getActivity(), dataList);
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
            linLayNyala.setVisibility(View.GONE);
        }else {
            listView.setVisibility(View.GONE);
            linLayNyala.setVisibility(View.VISIBLE);
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), JobActivity.class);
                intent.putExtra("idOrder",dataList.get(position).getIdOrder());
                intent.putExtra("type",dataList.get(position).getOrderType());
                intent.putExtra("start_latitude",dataList.get(position).getSLatitude());
                intent.putExtra("start_longitude",dataList.get(position).getSLongitude());
                intent.putExtra("end_latitude",dataList.get(position).getELatitude());
                intent.putExtra("end_longitude",dataList.get(position).getELongitude());
                intent.putExtra("alamat_jemput",dataList.get(position).getAlamatJemput());
                intent.putExtra("alamat_tujuan",dataList.get(position).getAlamatTujuan());
                intent.putExtra("total_biaya",dataList.get(position).getBiaya());
                intent.putExtra("jarak",dataList.get(position).getJarak());
                intent.putExtra("telepon_pemesan",dataList.get(position).getTelepon());
                intent.putExtra("nama",dataList.get(position).getNama());
                intent.putExtra("snote",dataList.get(position).getSnote());
                intent.putExtra("enote",dataList.get(position).getEnote());
                intent.putExtra("foodnote",dataList.get(position).getFoodnote());
                intent.putExtra("food_price",dataList.get(position).getFood_price());
                startActivity(intent);
            }
        });

        return view;
    }

    private void makeJsonObjectRequest() {

        for (Profil profil : valuesProfil) {
            token = profil.getToken();
            driver_type = profil.getDriver_type();
        }
        StringRequest jsonObjReq = new StringRequest(Request.Method.GET, APIConfig.API_GET_ORDER, new Response.Listener<String>() {

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
                                String user_id = isi.getString("user_id");
                                String order_type = isi.getString("type");
                                String slatitude = isi.getString("start_latitude");
                                String slongitude = isi.getString("start_longitude");
                                String elatitude = isi.getString("end_latitude");
                                String elongitude = isi.getString("end_longitude");
                                String alamatJemput = isi.getString("alamat_jemput");
                                String alamatTujuan = isi.getString("alamat_tujuan");
                                String biaya = isi.getString("total_biaya");
                                String food_price = isi.getString("food_price");
                                String jarak = isi.getString("jarak");
                                String telepon = isi.getString("telepon_pemesan");
                                String nama = isi.getString("nama_pemesan");
                                String snote = isi.getString("notes");
                                String enote = isi.getString("endnotes");
                                if(order_type.equals("3")) {
                                    foodnote = isi.getString("foodnotes");
                                }else{
                                    foodnote = "Bukan order food";
                                }

                                if(order_type.equals("2")&&driver_type.equals("2")){
                                    dataList.add(new Order(String.valueOf(i), id, user_id, order_type, slatitude, slongitude, elatitude, elongitude, alamatJemput, alamatTujuan, jarak, biaya, telepon, nama, snote, enote, foodnote,food_price));
                                }else if((order_type.equals("1")||order_type.equals("3"))&&driver_type.equals("1")){
                                    dataList.add(new Order(String.valueOf(i), id, user_id, order_type, slatitude, slongitude, elatitude, elongitude, alamatJemput, alamatTujuan, jarak, biaya, telepon, nama, snote, enote, foodnote,food_price));
                                }
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