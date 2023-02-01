package com.technolgiya.myapplication;

public class SliderItem {

    //here you can use string variable to store url to show image from internet

    public String getImagedb() {
        return imagedb;
    }

    public void setImagedb(String imagedb) {
        this.imagedb = imagedb;
    }

    String imagedb;

    public SliderItem(){

    };

    public SliderItem(String imagedb)
    {
        this.imagedb = imagedb;
    }

    private int image;

    SliderItem(int image)
    {
        this.image = image;
    }

    public int getImage() {
        return image;
    }
}
