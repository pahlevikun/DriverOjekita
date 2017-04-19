package com.wensoft.driverojekita.main;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.wensoft.driverojekita.R;
import com.wensoft.driverojekita.config.APIConfig;
import com.wensoft.driverojekita.config.DirectionsJSONParser;
import com.wensoft.driverojekita.config.GPSTracker;
import com.wensoft.driverojekita.database.DatabaseHandler;
import com.wensoft.driverojekita.pojo.Profil;
import com.wensoft.driverojekita.singleton.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JobActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private GPSTracker gps;
    private Geocoder geocoder;
    private GoogleMap mGoogleMap;
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private Marker mCurrLocationMarker, markerJemput, markerTujuan;

    private ProgressDialog loading;
    private ArrayList<Profil> valuesProfil;
    private DatabaseHandler dataSource;
    private String token, id,type,foodnote,start_latitude,start_longitude,alamat_jemput,alamat_tujuan,total_biaya,jarak,telepon_pemesan,nama,snote,enote;
    private String slat, slng, elat, elng,food_price;
    private LatLng sLatLng, eLatLng;
    private Button btPick,btTurunkan,btTelpon,btSms,btFood;

    private boolean pickorder = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job);

        dataSource = new DatabaseHandler(this);
        valuesProfil = (ArrayList<Profil>) dataSource.getAllProfils();

        Intent ambil = getIntent();
        id = ambil.getStringExtra("idOrder");
        type = ambil.getStringExtra("type");
        alamat_jemput = ambil.getStringExtra("alamat_jemput");
        alamat_tujuan = ambil.getStringExtra("alamat_tujuan");
        total_biaya = ambil.getStringExtra("total_biaya");
        jarak = ambil.getStringExtra("jarak");
        telepon_pemesan = ambil.getStringExtra("telepon_pemesan");
        nama = ambil.getStringExtra("nama");
        slat = ambil.getStringExtra("start_latitude");
        slng = ambil.getStringExtra("start_longitude");
        elat = ambil.getStringExtra("end_latitude");
        elng = ambil.getStringExtra("end_longitude");
        snote = ambil.getStringExtra("snote");
        enote = ambil.getStringExtra("enote");
        foodnote = ambil.getStringExtra("foodnote");
        food_price = ambil.getStringExtra("food_price");

        double Slat = Double.valueOf(slat);
        double Slng = Double.valueOf(slng);
        double Elat = Double.valueOf(elat);
        double Elng = Double.valueOf(elng);
        sLatLng = new LatLng(Slat,Slng);
        eLatLng = new LatLng(Elat,Elng);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map1);
        mapFragment.getMapAsync(JobActivity.this);

        Log.i("dobel lat String",slat);
        Log.i("dobel lng String",slng);
        Log.i("dobel elat String",elat);
        Log.i("dobel elng String",elng);
        Log.i("dobel lat dobel",""+Slat);
        Log.i("dobel lng dobel",""+Slng);
        Log.i("dobel elat dobel",""+Slat);
        Log.i("dobel elng dobel",""+Slng);

        String url = getDirectionsUrl(sLatLng,eLatLng);
        JobActivity.DownloadTask downloadTask = new JobActivity.DownloadTask();
        downloadTask.execute(url);

        btPick = (Button) findViewById(R.id.buttonPesan);
        btTurunkan = (Button) findViewById(R.id.buttonSelesai);
        btTelpon = (Button) findViewById(R.id.btTelpon);
        btSms = (Button) findViewById(R.id.btSms);
        btFood = (Button) findViewById(R.id.btDetailFood);

        btTelpon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", telepon_pemesan, null));
                startActivity(intent);
            }
        });

        btSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", telepon_pemesan, null));
                startActivity(intent);
            }
        });

        ImageView imageView = (ImageView) findViewById(R.id.ivorder);
        TextView tvBiaya = (TextView) findViewById(R.id.tvBiaya);
        TextView tvJarak = (TextView) findViewById(R.id.tvJarak);
        TextView tvAlamatJemput = (TextView) findViewById(R.id.tvJemput);
        TextView tvAlamatTujuan = (TextView) findViewById(R.id.tvTujuan);
        TextView tvName = (TextView) findViewById(R.id.tvNama);
        if (type.equals("1")){
            imageView.setImageResource(R.drawable.ic_motor);
            btFood.setVisibility(View.GONE);
            tvBiaya.setText("Rp. "+total_biaya+",-");
        }else if(type.equals("2")){
            imageView.setImageResource(R.drawable.ic_mobil);
            btFood.setVisibility(View.GONE);
            tvBiaya.setText("Rp. "+total_biaya+",- (Rp. "+food_price+",-)");
        }else if(type.equals("3")){
            imageView.setImageResource(R.drawable.ic_food);
            btFood.setVisibility(View.VISIBLE);
            tvBiaya.setText("Rp. "+total_biaya+",-");
            btFood.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(JobActivity.this, JobFoodActivity.class);
                    intent.putExtra("order_id",id);
                    intent.putExtra("foodnote",foodnote);
                    startActivity(intent);
                }
            });
        }
        tvJarak.setText(jarak+" KM");
        tvAlamatJemput.setText(alamat_jemput+"\n"+snote);
        tvAlamatTujuan.setText(alamat_tujuan+"\n"+enote);
        tvName.setText(nama);
        btTurunkan.setText("Selesaikan Pesanan");

        btTurunkan.setVisibility(View.GONE);
        btPick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickOrder(id);
            }
        });
        btTurunkan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder alert = new AlertDialog.Builder(JobActivity.this);
                alert.setTitle("Peringatan");
                alert.setMessage("Selesaikan Pesanan?");
                alert.setPositiveButton("Ya",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                endOrder(id);
                            }
                        });
                alert.setNegativeButton("Tidak",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                            }
                        });
                alert.show();
            }
        });


    }

    public void onStart(){
        super.onStart();
    }

    private void pickOrder(final String idOrder) {

        loading = ProgressDialog.show(this,"Mohon Tunggu","Sedang mengambil order...",false,false);

        for (Profil profil : valuesProfil){
            token = profil.getToken();
        }

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, APIConfig.API_PICK_ORDER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                hideDialog();
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        Toast.makeText(JobActivity.this, "Berhasil mengambil order!", Toast.LENGTH_SHORT).show();
                        btPick.setVisibility(View.GONE);
                        btTurunkan.setVisibility(View.VISIBLE);
                        pickorder = true;
                    }else{
                        if (jObj.has("msg")){
                            String msg = jObj.getString("msg");
                            final AlertDialog.Builder alert = new AlertDialog.Builder(JobActivity.this);
                            alert.setTitle("Peringatan");
                            alert.setMessage(""+msg);
                            alert.setPositiveButton("Ya",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            // TODO Auto-generated method stub
                                        }
                                    });
                            alert.show();
                            //Toast.makeText(JobActivity.this, ""+msg, Toast.LENGTH_LONG).show();
                        }else if(jObj.has("message")){
                            String msg = jObj.getString("message");
                            final AlertDialog.Builder alert = new AlertDialog.Builder(JobActivity.this);
                            alert.setTitle("Peringatan");
                            alert.setMessage(""+msg);
                            alert.setPositiveButton("Ya",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            // TODO Auto-generated method stub
                                        }
                                    });
                            alert.show();
                            //Toast.makeText(JobActivity.this, ""+msg, Toast.LENGTH_LONG).show();
                        }
                        finish();

                    }
                } catch (JSONException e) {
                    Toast.makeText(JobActivity.this, ""+e, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                hideDialog();
                Toast.makeText(JobActivity.this, ""+error, Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("order_id", idOrder);
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

    private void endOrder(final String idOrder) {

        loading = ProgressDialog.show(this,"Mohon Tunggu","Sedang mengakhiri order...",false,false);

        for (Profil profil : valuesProfil){
            token = profil.getToken();
        }

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, APIConfig.API_END_ORDER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                hideDialog();
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        Toast.makeText(JobActivity.this, "Berhasil mengakhiri order!", Toast.LENGTH_SHORT).show();
                        finish();
                    }else{
                        /*if (jObj.has("msg")){
                            message = jObj.getString("msg");
                        }else if(jObj.has("message")){
                            message = jObj.getString("message");
                        }*/
                        finish();
                        final AlertDialog.Builder alert = new AlertDialog.Builder(JobActivity.this);
                        alert.setTitle("Peringatan");
                        alert.setMessage("Gagal menurunkan customer atau pesanan sudah dicancel cutomer.");
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
                    Toast.makeText(JobActivity.this, ""+e, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                hideDialog();
                Toast.makeText(JobActivity.this, ""+error, Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("order_id", idOrder);
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
    public void onMapReady(GoogleMap googleMap) {

        mGoogleMap = googleMap;

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mGoogleMap.setMyLocationEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            mGoogleMap.setMyLocationEnabled(true);
        }

        mGoogleMap.addMarker(new MarkerOptions().position(sLatLng).title("Lokasi Jemput").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_dest)));
        mGoogleMap.addMarker(new MarkerOptions().position(eLatLng).title("Lokasi Tujuan").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_del)));


        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
        mGoogleMap.getUiSettings().setAllGesturesEnabled(true);
        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
        mGoogleMap.getUiSettings().setCompassEnabled(true);
        mGoogleMap.getUiSettings().setMapToolbarEnabled(false);

        //makeJsonObjectRequest();
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }


    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {}

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mGoogleMap.moveCamera(CameraUpdateFactory.zoomTo(14));

        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }


    private String getDirectionsUrl(LatLng origin,LatLng dest){

        // Origin of route
        String str_origin = "origin="+origin.latitude+","+origin.longitude;
        // Destination of route
        String str_dest = "destination="+dest.latitude+","+dest.longitude;
        // Sensor enabled
        String sensor = "sensor=false";
        // Building the parameters to the web service
        String parameters = str_origin+"&"+str_dest+"&"+sensor;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;

        return url;
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while( ( line = br.readLine()) != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            //Log.d("Exception while downloading url", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try{
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            JobActivity.ParserTask parserTask = new JobActivity.ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    /** A class to parse the Google Places in JSON format */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try{
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            }catch(Exception e){
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();

            // Traversing through all the routes
            for(int i=0;i<result.size();i++){
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for(int j=0;j<path.size();j++){
                    HashMap<String,String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(4);
                lineOptions.color(getResources().getColor(R.color.colorAccent));
            }

            // Drawing polyline in the Google Map for the i-th route
            mGoogleMap.addPolyline(lineOptions);
        }
    }

    @Override
    public void onBackPressed() {
        if (pickorder){
            Toast.makeText(this, "Selesaikan pesanan terlebih dahulu!", Toast.LENGTH_SHORT).show();
        }else{
            finish();
        }
    }

    private void hideDialog() {
        if (loading.isShowing())
            loading.dismiss();
    }
}
