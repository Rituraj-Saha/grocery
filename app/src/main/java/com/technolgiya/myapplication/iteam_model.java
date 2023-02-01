package com.technolgiya.myapplication;

public class iteam_model {



    String category;
    String image;
    String name;
    String quantity;
    String rate;
    String subcategory;
    String mrp;
    String discount;
    String discountpercentage;


    public iteam_model() {
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }


    public String getMrp() {
        return mrp;
    }

    public void setMrp(String mrp) {
        this.mrp = mrp;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getDiscountpercentage() {
        return discountpercentage;
    }

    public void setDiscountpercentage(String discountpercentage) {
        this.discountpercentage = discountpercentage;
    }



    public iteam_model(String category, String image, String name, String quantity, String rate, String subcategory, String mrp, String discount,
                       String discountpercentage) {
        this.category = category;
        this.image = image;
        this.name = name;
        this.quantity = quantity;
        this.rate = rate;
        this.subcategory = subcategory;
        this.mrp = mrp;
        this.discount = discount;
        this.discountpercentage = discountpercentage;
    }
}
