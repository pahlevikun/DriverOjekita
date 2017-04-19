package com.wensoft.driverojekita.pojo;

/**
 * Created by farhan on 5/31/16.
 */
public class FCM {
    //private variables
    int _id;
    String _token;

    // Empty constructor
    public FCM() {
    }

    // constructor
    public FCM(int id, String token) {
        this._id = id;
        this._token = token;
    }

    // constructor
    public FCM(String token) {
        this._token = token;
    }

    // getting ID
    public int getID() {
        return this._id;
    }

    // setting id
    public void setID(int id) {
        this._id = id;
    }

    // getting ID
    public String getTokenFCM() {
        return this._token;
    }

    // setting id
    public void setTokenFCM(String token) {
        this._token = token;
    }


}