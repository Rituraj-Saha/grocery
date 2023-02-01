package com.technolgiya.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Splash extends AppCompatActivity {


    Animation topAnim,bottomAnim;

    ImageView imageView;
    LinearLayout textView;
    private static int SPLASH_SCREEN= 5000;
    private Handler mWaitHandler = new Handler();
    DatabaseReference userRef;
    int flag =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        topAnim = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);


        imageView = findViewById(R.id.imageView);
        textView = findViewById(R.id.textView);

        imageView.setAnimation(topAnim);
        textView.setAnimation(bottomAnim);
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if(firebaseUser != null )
        {

            userRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists())
                    {

                        String sevicetypeDb = dataSnapshot.child("serviceType").getValue().toString();
                        String locationDb = dataSnapshot.child("locationbutton").getValue().toString();


                        if(sevicetypeDb.equals("Client"))
                        {
                                flag =1;
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else
                            flag =0;



                    }


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

    }
        mWaitHandler.postDelayed(new Runnable() {

            @Override
            public void run() {

                //The following code will execute after the 5 seconds.

                try {

                    //Go to next page i.e, start the next activity.
                    if(flag == 0)
                    {
                        Intent intent = new Intent(getApplicationContext(), welcome.class);
                        startActivity(intent);
                        finish();

                    }

                    //Let's Finish Splash Activity since we don't want to show this when user press back button.

                } catch (Exception ignored) {
                    ignored.printStackTrace();
                }
            }
        }, SPLASH_SCREEN);

    }
}
