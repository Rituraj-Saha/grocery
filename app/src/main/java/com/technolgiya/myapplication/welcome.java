package com.technolgiya.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;
import com.tbuonomo.viewpagerdotsindicator.SpringDotsIndicator;

import java.util.ArrayList;
import java.util.List;

public class welcome extends AppCompatActivity {

    private ViewPager viewPager2;
    private DatabaseReference imageDb;
    private List<SliderItem> sliderItems = new ArrayList<>();
    private Handler sliderHandler = new Handler();
    private SpringDotsIndicator dots_indicator;
    private ViewAdaptor viewAdaptor;
    private TextView text1,text2,text3;
    private Button btn_sgnup;
    private TextView btn_login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);


        dots_indicator = findViewById(R.id.dots_indicator);

        viewPager2 = findViewById(R.id.viewPage);
        imageDb = FirebaseDatabase.getInstance().getReference().child("imagewelcome");
        text1 = findViewById(R.id.txt1);
        text2=findViewById(R.id.txt2);
        text3 = findViewById(R.id.txt3);
        btn_login=findViewById(R.id.btn_login);
        btn_sgnup =findViewById(R.id.btn_sgnup);


        imageDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                    String image1= dataSnapshot.child("image1").getValue().toString();
                    String image2= dataSnapshot.child("image2").getValue().toString();
                    String image3= dataSnapshot.child("image3").getValue().toString();
                    text1.setText(image1);
                    text2.setText(image2);
                    text3.setText(image3);

                            sliderItems.add(new SliderItem(image1));
                            sliderItems.add(new SliderItem(image2));
                            sliderItems.add(new SliderItem(image3));

                            viewAdaptor = new ViewAdaptor(text1.getText().toString(),
                                    text2.getText().toString(),
                                    text3.getText().toString(),welcome.this);
                            viewPager2.setAdapter(viewAdaptor);
                            dots_indicator.setViewPager(viewPager2);





            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r= 1 - Math.abs(position);
                page.setScaleY(0.85f+r*0.15f);
            }
        });


        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent reg = new Intent(welcome.this,RegistrationActivity.class);
                startActivity(reg);
                finish();
            }
        });

        btn_sgnup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent reg = new Intent(welcome.this,RegistrationActivity.class);
                startActivity(reg);
                finish();
            }
        });


    }



}
