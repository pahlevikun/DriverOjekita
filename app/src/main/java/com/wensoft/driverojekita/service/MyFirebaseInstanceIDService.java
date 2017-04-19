package com.wensoft.driverojekita.service;

/**
 * Created by farhan on 1/22/17.
 */

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.wensoft.driverojekita.database.DatabaseHandler;


public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = "MyFirebaseIIDService";
    public String refreshedToken;

    private SQLiteDatabase dataSource;
    private DatabaseHandler db;


    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        refreshedToken =  FirebaseInstanceId.getInstance().getToken();

        db = new DatabaseHandler(getApplicationContext());

        dataSource = db.getWritableDatabase();
        dataSource.execSQL("UPDATE fcm SET token = '" + refreshedToken + "' WHERE _id = '1';");
        dataSource.close();

        Log.d(TAG, "Refreshed token: " + refreshedToken);
        sendRegistrationToServer(refreshedToken);
    }

    // fungsi untuk kirim token ke server kita ( opsional )
    private void sendRegistrationToServer(String token) {
        // Add custom implementation, as needed.
    }
}