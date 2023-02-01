package com.technolgiya.myapplication;

public class order_model {

    public order_model(String name, String value, String total, String address, String status, String phoneNumber, String uid,String time
    ,String pin,String deliverystatus) {
        this.name = name;
        Value = value;
        this.total = total;
        this.address = address;
        this.status = status;
        this.phoneNumber = phoneNumber;
        Uid = uid;
        this.time = time;
        this.pin = pin;
        this.deliverystatus = deliverystatus;

    }

    String name;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        Value = value;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    String Value;
    String total;
    String address;
    String status;
    String phoneNumber;
    String Uid;
    String deliverystatus;

    public String getDeliverystatus() {
        return deliverystatus;
    }

    public void setDeliverystatus(String deliverystatus) {
        this.deliverystatus = deliverystatus;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    String pin;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    String time;

    public order_model() {
    }

}
