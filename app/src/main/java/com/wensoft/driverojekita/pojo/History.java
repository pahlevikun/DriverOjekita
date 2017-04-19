package com.wensoft.driverojekita.pojo;

/**
 * Created by farhan on 3/13/17.
 */

public class History {

    String idList;
    String idOrder;
    String idUser;
    String orderType;
    String price;
    double Slatitude;
    double Slongitude;
    double Elatitude;
    double Elongitude;
    String address;
    String created_at;
    String status;

    public History() {
    }

    public History(String idList, String idOrder, String idUser, String orderType, String price, double Slatitude, double Slongitude,
                   double Elatitude, double Elongitude, String address, String created_at, String status) {
        this.idList = idList;
        this.idOrder = idOrder;
        this.idUser = idUser;
        this.orderType = orderType;
        this.price = price;
        this.Slatitude = Slatitude;
        this.Slongitude = Slongitude;
        this.Elatitude = Elatitude;
        this.Elongitude = Elongitude;
        this.address = address;
        this.created_at = created_at;
        this.status = status;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public double getSlatitude() {
        return Slatitude;
    }

    public void setSlatitude(double Slatitude) {
        this.Slatitude = Slatitude;
    }

    public double getSlongitude() {
        return Slongitude;
    }

    public void setSlongitude(double Slongitude) {
        this.Slongitude = Slongitude;
    }

    public double getElatitude() {
        return Elatitude;
    }

    public void setElatitude(double Elatitude) {
        this.Elatitude = Elatitude;
    }

    public double getElongitude() {
        return Elongitude;
    }

    public void setElongitude(double Elongitude) {
        this.Elongitude = Elongitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
