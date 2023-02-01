package com.technolgiya.myapplication;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

class ViewAdaptor extends PagerAdapter {
     Context context;
    private LayoutInflater layoutInflater;
    String str1,str2,str3;
    String image[] = new String[3];


    ViewAdaptor(String str1,String str2,String str3,Context context)
    {
        this.str1=str1;
        this.str2=str2;
        this.str3=str3;
        this.context=context;
        Log.d("str", "str: "+str1+"\n"+str2+"\n"+str3);
        image[0]=str1;
        image[1]=str2;
        image[2]=str3;

    }


    @Override
    public int getCount() {
        return image.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);

        View view =layoutInflater.inflate(R.layout.welcome_container,null);
        ImageView imageview;
        imageview = view.findViewById(R.id.imgwelcome);
        Log.d("str1", "str1: "+str1);

        Log.d("imagepos", "instantiateItem: "+image[position]);
        Picasso.get().load(image[position]).placeholder(R.drawable.ad).into(imageview);
        ViewPager viewPager = (ViewPager) container;
        viewPager.addView(view,0);
        return view;

    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        super.destroyItem(container, position, object);
        ViewPager2 viewPager2 = (ViewPager2) container;
        View view=(View) object;
        viewPager2.removeView(view);
    }
}
