package com.wensoft.driverojekita.pojo;

/**
 * Created by farhan on 5/31/16.
 */
public class Profil {
    //private variables
    int _id;
    int _userid;
    String _username;
    String _email;
    String _token;
    String _telepon;
    String _create;
    String _driver_type;

    // Empty constructor
    public Profil() {
    }

    // constructor
    public Profil(int id, int userid, String username, String email, String token, String telepon, String create, String driver_type) {
        this._id = id;
        this._userid = userid;
        this._username = username;
        this._email = email;
        this._token = token;
        this._telepon = telepon;
        this._create = create;
        this._driver_type = driver_type;
    }

    // constructor
    public Profil(int userid, String username, String email, String token, String telepon, String create, String driver_type) {
        this._userid = userid;
        this._username = username;
        this._email = email;
        this._token = token;
        this._telepon = telepon;
        this._create = create;
        this._driver_type = driver_type;
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
    public int getUserID() {
        return this._userid;
    }

    // setting id
    public void setUserID(int userid) {
        this._userid = userid;
    }

    // getting judul
    public String getUsername() {
        return this._username;
    }

    // setting judul
    public void setUsername(String username) {
        this._username = username;
    }

    // getting judul
    public String getEmail() {
        return this._email;
    }

    // setting judul
    public void setEmail(String email) {
        this._email = email;
    }

    // getting ID
    public String getToken() {
        return this._token;
    }

    // setting id
    public void setToken(String token) {
        this._token = token;
    }

    // getting ID
    public String getTelepon() {
        return this._telepon;
    }

    // setting id
    public void setTelepon(String telepon) {
        this._telepon = telepon;
    }

    // getting ID
    public String getCreate() {
        return this._create;
    }

    // setting id
    public void setCreate(String create) {
        this._create = create;
    }

    // getting ID
    public String getDriver_type() {
        return this._driver_type;
    }

    // setting id
    public void setDriver_type(String driver_type) {
        this._driver_type = driver_type;
    }

}