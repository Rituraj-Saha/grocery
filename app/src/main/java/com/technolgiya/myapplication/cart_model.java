package com.technolgiya.myapplication;

public class cart_model {

    String name;
    String qty;
    String rate;
    String image;

    String quantity;


    String mrprate;
    String mrpamount;
    String totaldiscount;


    String discountpercentage;

    String amount;

    public String getTimeOfdel() {
        return timeOfdel;
    }

    public void setTimeOfdel(String timeOfdel) {
        this.timeOfdel = timeOfdel;
    }

    String timeOfdel;

    public cart_model() {
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getMrprate() {
        return mrprate;
    }

    public void setMrprate(String mrprate) {
        this.mrprate = mrprate;
    }

    public String getMrpamount() {
        return mrpamount;
    }

    public void setMrpamount(String mrpamount) {
        this.mrpamount = mrpamount;
    }

    public String getTotaldiscount() {
        return totaldiscount;
    }

    public void setTotaldiscount(String totaldiscount) {
        this.totaldiscount = totaldiscount;
    }

    public String getDiscountpercentage() {
        return discountpercentage;
    }

    public void setDiscountpercentage(String discountpercentage) {
        this.discountpercentage = discountpercentage;
    }


    public cart_model(String name, String qty, String rate, String image,String amount,String quantity,
                      String mrpamount,String mrprate,String totaldiscount,String discountpercentage,
                      String timeOfdel) {
        this.name = name;
        this.qty = qty;
        this.rate = rate;
        this.image = image;
        this.amount = amount;
        this.quantity= quantity;
        this.mrpamount = mrpamount;
        this.mrprate = mrprate;
        this.totaldiscount = totaldiscount;
        this.discountpercentage = discountpercentage;
        this.timeOfdel = timeOfdel;
    }
}
