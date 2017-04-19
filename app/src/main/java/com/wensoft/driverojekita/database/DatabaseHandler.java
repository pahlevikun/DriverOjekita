package com.wensoft.driverojekita.database;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.wensoft.driverojekita.pojo.FCM;
import com.wensoft.driverojekita.pojo.Profil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by farhan on 10/17/16.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    // Versi Database
    public static final int DATABASE_VERSION = 5;

    // Nama Database
    public static final String DATABASE_NAME = "ojekita";

    // Nama Tabel
    public static final String TABLE_PROFIL = "profil";
    public static final String TABLE_FCM = "fcm";

    //Tabel Profil
    public static final String KEY_ID_PROFIL = "_id";
    public static final String KEY_USERID_PROFIL = "userid";
    public static final String KEY_USERNAME_PROFIL = "username";
    public static final String KEY_EMAIL_PROFIL = "email";
    public static final String KEY_TOKEN_PROFIL = "token";
    public static final String KEY_TELEPON_PROFIL = "telepon";
    public static final String KEY_CREATED_AT = "created_at";
    public static final String KEY_DRIVER_TYPE = "driver_type";

    //Tabel Riwayat
    public static final String KEY_ID_FCM = "_id";
    public static final String KEY_TOKENS_FCM = "token";


    public Resources res;

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        res = context.getResources();
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_PROFIL = "CREATE TABLE " + TABLE_PROFIL + "("
                + KEY_ID_PROFIL + " INTEGER PRIMARY KEY," + KEY_USERID_PROFIL + " INTEGER,"
                + KEY_USERNAME_PROFIL + " TEXT," + KEY_EMAIL_PROFIL + " TEXT,"
                + KEY_TOKEN_PROFIL + " TEXT," + KEY_TELEPON_PROFIL + " TEXT," + KEY_CREATED_AT + " TEXT,"
                + KEY_DRIVER_TYPE + " TEXT" + ")";

        String CREATE_TABLE_FCM = "CREATE TABLE " + TABLE_FCM + "("
                + KEY_ID_FCM + " INTEGER PRIMARY KEY," + KEY_TOKENS_FCM + " TEXT" + ")";

        db.execSQL(CREATE_TABLE_PROFIL);
        db.execSQL(CREATE_TABLE_FCM);

        ContentValues values = new ContentValues();
        values.put(KEY_ID_FCM, 1);
        db.insert(TABLE_FCM, null, values);

    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROFIL);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FCM);
        // Create tables again
        onCreate(db);
    }

    public void hapusDbaseProfil() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_PROFIL);
    }

    public void tambahProfil(Profil profil) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_USERID_PROFIL, profil.getUserID());
        values.put(KEY_USERNAME_PROFIL, profil.getUsername());
        values.put(KEY_EMAIL_PROFIL, profil.getEmail());
        values.put(KEY_TOKEN_PROFIL, profil.getToken());
        values.put(KEY_TELEPON_PROFIL, profil.getTelepon());
        values.put(KEY_CREATED_AT, profil.getCreate());
        values.put(KEY_DRIVER_TYPE, profil.getDriver_type());

        // Inserting Row
        db.insert(TABLE_PROFIL, null, values);
        db.close();
    }



    public List<Profil> getAllProfils() {
        List<Profil> profilList = new ArrayList<Profil>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_PROFIL;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Profil profil = new Profil();
                profil.setID(Integer.parseInt(cursor.getString(0)));
                profil.setUserID(Integer.parseInt(cursor.getString(1)));
                profil.setUsername(cursor.getString(2));
                profil.setEmail(cursor.getString(3));
                profil.setToken(cursor.getString(4));
                profil.setTelepon(cursor.getString(5));
                profil.setCreate(cursor.getString(6));
                profil.setDriver_type(cursor.getString(7));
                // Adding contact to list
                profilList.add(profil);
            } while (cursor.moveToNext());
        }

        // return contact list
        return profilList;
    }

    public List<FCM> getAllFCMs() {
        List<FCM> profilList = new ArrayList<FCM>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_FCM;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                FCM profil = new FCM();
                profil.setID(Integer.parseInt(cursor.getString(0)));
                profil.setTokenFCM(cursor.getString(1));
                // Adding contact to list
                profilList.add(profil);
            } while (cursor.moveToNext());
        }

        // return contact list
        return profilList;
    }
}
