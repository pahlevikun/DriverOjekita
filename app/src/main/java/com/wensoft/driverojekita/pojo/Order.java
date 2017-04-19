package com.wensoft.driverojekita.pojo;

/**
 * Created by farhan on 3/13/17.
 */

public class Order {

    String idList;
    String idOrder;
    String idUser;
    String orderType;
    String slatitude;
    String slongitude;
    String elatitude;
    String elongitude;
    String alamatJemput;
    String alamatTujuan;
    String jarak;
    String biaya;
    String telepon;
    String nama;
    String snote;
    String enote;
    String foodnote;
    String food_price;

    public Order() {
    }

    public String getFoodnote() {
        return foodnote;
    }

    public void setFoodnote(String foodnote) {
        this.foodnote = foodnote;
    }

    public String getFood_price() {
        return food_price;
    }

    public void setFood_price(String food_price) {
        this.food_price = food_price;
    }

    public Order(String idList, String idOrder, String idUser, String orderType, String slatitude, String slongitude, String elatitude, String elongitude, String alamatJemput, String alamatTujuan, String jarak, String biaya, String telepon, String nama, String snote, String enote, String foodnote, String food_price) {
        this.idList = idList;
        this.idOrder = idOrder;
        this.idUser = idUser;
        this.orderType = orderType;
        this.slatitude = slatitude;
        this.slongitude = slongitude;
        this.elatitude = elatitude;
        this.elongitude = elongitude;
        this.alamatJemput = alamatJemput;
        this.alamatTujuan = alamatTujuan;
        this.jarak = jarak;
        this.biaya = biaya;
        this.telepon = telepon;
        this.nama = nama;
        this.snote = snote;
        this.enote = enote;
        this.foodnote = foodnote;
        this.food_price = food_price;
    }

    public String getIdList() {
        return idList;
    }

    public void setIdList(String idList) {
        this.idList = idList;
    }

    public String getIdOrder() {
        return idOrder;
    }

    public void setIdOrder(String idOrder) {
        this.idOrder = idOrder;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getSLongitude() {
        return slongitude;
    }

    public void setSLongitude(String slongitude) {
        this.slongitude = slongitude;
    }

    public String getSLatitude() {
        return slatitude;
    }

    public void setSLatitude(String slatitude) {
        this.slatitude = slatitude;
    }

    public String getELongitude() {
        return elongitude;
    }

    public void setELongitude(String elongitude) {
        this.elongitude = elongitude;
    }

    public String getELatitude() {
        return elatitude;
    }

    public void setELatitude(String elatitude) {
        this.elatitude = elatitude;
    }


    public String getAlamatJemput() {
        return alamatJemput;
    }

    public void setAlamatJemput(String alamatJemput) {
        this.alamatJemput = alamatJemput;
    }

    public String getAlamatTujuan() {
        return alamatTujuan;
    }

    public void setAlamatTujuan(String alamatTujuan) {
        this.alamatTujuan = alamatTujuan;
    }

    public String getJarak() {
        return jarak;
    }

    public void setJarak(String jarak) {
        this.jarak = jarak;
    }

    public String getBiaya() {
        return biaya;
    }

    public void setBiaya(String biaya) {
        this.biaya = biaya;
    }

    public String getTelepon() {
        return telepon;
    }

    public void setTelepon(String telepon) {
        this.telepon = telepon;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getEnote() {
        return enote;
    }

    public void setEnote(String enote) {
        this.enote = enote;
    }

    public String getSnote() {
        return snote;
    }

    public void setSnote(String snote) {
        this.snote = snote;
    }
}
