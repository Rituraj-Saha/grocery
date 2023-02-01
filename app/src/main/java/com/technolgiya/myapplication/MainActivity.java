package com.technolgiya.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private CoordinatorLayout coordinatorLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private ViewPager2 viewPager2;
    private DatabaseReference imageDb;
    private List<SliderItem> sliderItems = new ArrayList<>();
    private RecyclerView fresh_recycler,milk_recycler,cooking_delights_recycler,dairy_goodness_recycler,
            breakfast_recyclear,bevarage_recyclear,snacks_recycler,package_recycler,personal_recycler,cleaning_recycler,
            home_essential_recycler,baby_care_recycler,wellness_recycler;
    private FrameLayout parent_frame_layout_client;
    private EditText et_search;
    private LinearLayout cart_icon;
    int badgecount=0;

    private ImageView img_nextday_del;

 //   private LinearLayout lin_fresh,lin_milk;
  //  private ImageView img_fresh,img_milk;

    private LinearLayout lin_cooking_delightone,lin_cooking_delightstwo,lin_cooking_delightsthree,lin_cooking_delightsfour,lin_cooking_delightsfive,lin_cooking_delightssix;
    private ImageView img_cooking_delightone,img_cooking_delightstwo,img_cooking_delightsthree,img_cooking_delightsfour,img_cooking_delightsfive,img_cooking_delightssix;
    private TextView txt_cooking_delightone,txt_cooking_delightstwo,txt_cooking_delightsthree,txt_cooking_delightsfour,txt_cooking_delightsfive,txt_cooking_delightssix;
    private DatabaseReference cooking_delightsRef;


    private LinearLayout lin_fresh_subcat1,lin_fresh_subcat2,lin_fresh_subcat3;
    private ImageView img_fresh_subcat1,img_fresh_subcat2,img_fresh_subcat3;
    private TextView txt_fresh_subcat1,txt_fresh_subcat2,txt_fresh_subcat3;
    private DatabaseReference fresh_catelogueRef;


    private LinearLayout lin_dairy_goodnessone,lin_dairy_goodnesstwo,lin_dairy_goodnessthree,lin_dairy_goodnessfour,lin_dairy_goodnessfive,
            lin_dairy_goodnesssix;

    private ImageView img_dairy_goodnessone,img_dairy_goodnesstwo,img_dairy_goodnessthree,img_dairy_goodnessfour,img_dairy_goodnessfive,img_dairy_goodnesssix;

    private TextView txt_dairy_goodnessone,txt_dairy_goodnesstwo,txt_dairy_goodnessthree,txt_dairy_goodnessfour,txt_dairy_goodnessfive,txt_dairy_goodnesssix;

    private DatabaseReference dairygoodness_catelogueRef;



    private LinearLayout lin_fruits_and_nutsone,lin_fruits_and_nutstwo,lin_fruits_and_nutsthree;
    private TextView txt_fruits_and_nutsone,txt_fruits_and_nutstwo,txt_fruits_and_nutsthree;
    private ImageView img_fruits_and_nutsone,img_fruits_and_nutstwo,img_fruits_and_nutsthree;
    private DatabaseReference fruitsandnuts_catelogueRef;

    private LinearLayout lin_breakfastone,lin_breakfasttwo,lin_breakfastthree,lin_breakfastfour;
    private ImageView img_breakfastone,img_breakfasttwo,img_breakfastthree,img_breakfastfour;
    private TextView txt_breakfastone,txt_breakfasttwo,txt_breakfastthree,txt_breakfastfour;
    private DatabaseReference breakfastRef;

    private LinearLayout lin_homeessential,lin_cleaningessential,lin_personalcare,lin_packedfood,lin_babycare
        ,lin_wellness;
    private DatabaseReference essentialsref;

    private ImageView img_homeessential,img_cleaningessential,img_personalcare,img_packedfood,img_babycare,
            img_wellness;

    private TextView txt_homeessential,txt_cleaningessential,txt_personalcare,txt_packedfood,txt_babycare,txt_wellness;

    private LinearLayout lin_bevarage_subcatone,lin_bevarage_subcattwo,lin_bevarage_subcatthree;
    //lin_bevarage_subcatfour;
    private ImageView img_bevarage_subcatone,img_bevarage_subcattwo,img_bevarage_subcatthree;
    //img_bevarage_subcatfour;
    private TextView txt_bevarage_subcatone,txt_bevarage_subcattwo,txt_bevarage_subcatthree;
    //txt_bevarage_subcatfour;
    private DatabaseReference bevarageRef;




    private LinearLayout lin_snacks_subcatone,lin_snacks_subcattwo,lin_snacks_subcatthree,lin_snacks_subcatfour;
    private ImageView img_snacks_subcatone,img_snacks_subcattwo,img_snacks_subcatthree,img_snacks_subcatfour;
    private TextView txt_snacks_subcatone,txt_snacks_subcattwo,txt_snacks_subcatthree,txt_snacks_subcatfour;
    private DatabaseReference snacksRef;





    private DatabaseReference userlocationRef;
    private DatabaseReference image_fresh,image_milk;
    private DatabaseReference fresh,userRef,milk,cooking_delights,dairy_gooness,breakfast,
            bevarage,snacks,PackageFood,personalcare,cleaning,HomeEssential,babycare,wellness;

    private String nameDbheader="";
    private TextView mTextView;

    private Handler sliderHandler = new Handler();

    ImageView promo1;
    ImageView promo2;
    ImageView promo3;
    ImageView promo4;
    private DatabaseReference promoimg1Db,promoimg2Db,promoimg3Db,promoimg4Db,promoimg5Db;
    private DatabaseReference promoimg1directdb,promoimg2directdb,promoimg3directdb,promoimg4directdb;

    private LinearLayout badge_cart;
    private TextView txt_badge_count;
    private DatabaseReference cartRef;

    int promoAlertcount = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout_clientmain);
        coordinatorLayout = findViewById(R.id.coordinator_layout_client);
        toolbar = findViewById(R.id.toolbar_layout_client);
        navigationView = findViewById(R.id.navigation_layout_client);
        cart_icon = findViewById(R.id.cart_icon);
       /* lin_fresh=findViewById(R.id.lin_fresh);
        img_fresh=findViewById(R.id.img_fresh);
        lin_milk = findViewById(R.id.lin_milk);
        img_milk = findViewById(R.id.img_milk);*/

        setupToolBar();

        lin_fresh_subcat1=findViewById(R.id.lin_fresh_subcat1);
        lin_fresh_subcat2=findViewById(R.id.lin_fresh_subcat2);
        lin_fresh_subcat3=findViewById(R.id.lin_fresh_subcat3);

        img_fresh_subcat1=findViewById(R.id.img_fresh_subact_1);
        img_fresh_subcat2=findViewById(R.id.img_fresh_subact_2);
        img_fresh_subcat3=findViewById(R.id.img_fresh_subact_3);

        txt_fresh_subcat1=findViewById(R.id.txt_fresh_subact_1);
        txt_fresh_subcat2=findViewById(R.id.txt_fresh_subact_2);
        txt_fresh_subcat3=findViewById(R.id.txt_fresh_subact_3);
        fresh_catelogueRef = FirebaseDatabase.getInstance().getReference().child("fres_catelogue");

// dairy goodness

        lin_dairy_goodnessone=findViewById(R.id.lin_dairy_goodnessone);
        lin_dairy_goodnesstwo=findViewById(R.id.lin_dairy_goodnesstwo);
        lin_dairy_goodnessthree=findViewById(R.id.lin_dairy_goodnessthree);
        lin_dairy_goodnessfour=findViewById(R.id.lin_dairy_goodnessfour);
        lin_dairy_goodnessfive=findViewById(R.id.lin_dairy_goodnessfive);
        lin_dairy_goodnesssix=findViewById(R.id.lin_dairy_goodnesssix);

        img_dairy_goodnessone= findViewById(R.id.img_dairy_goodnessone);
        img_dairy_goodnesstwo=findViewById(R.id.img_dairy_goodnesstwo);
        img_dairy_goodnessthree=findViewById(R.id.img_dairy_goodnessthree);
        img_dairy_goodnessfour=findViewById(R.id.img_dairy_goodnessfour);
        img_dairy_goodnessfive=findViewById(R.id.img_dairy_goodnessfive);
        img_dairy_goodnesssix = findViewById(R.id.img_dairy_goodnesssix);

        txt_dairy_goodnessone=findViewById(R.id.txt_dairy_goodnessone);
        txt_dairy_goodnesstwo=findViewById(R.id.txt_dairy_goodnesstwo);
        txt_dairy_goodnessthree=findViewById(R.id.txt_dairy_goodnessthree);
        txt_dairy_goodnessfour=findViewById(R.id.txt_dairy_goodnessfour);
        txt_dairy_goodnessfive=findViewById(R.id.txt_dairy_goodnessfive);
        txt_dairy_goodnesssix = findViewById(R.id.txt_dairy_goodnesssix);

        dairygoodness_catelogueRef =FirebaseDatabase.getInstance().getReference("dairygoodness_catelogue");

//ens of dairy goodness

        lin_cooking_delightone = findViewById(R.id.lin_cooking_delightone);
        img_cooking_delightone =  findViewById(R.id.img_cooking_delightone);
        txt_cooking_delightone = findViewById(R.id.txt_cooking_delightone);
        cooking_delightsRef =FirebaseDatabase.getInstance().getReference().child("cookingdelights_catelogue");

        lin_cooking_delightstwo =findViewById(R.id.lin_cooking_delighttwo);
        img_cooking_delightstwo = findViewById(R.id.img_cooking_delighttwo);
        txt_cooking_delightstwo = findViewById(R.id.txt_cooking_delighttwo);

        lin_cooking_delightsthree =findViewById(R.id.lin_cooking_delightthree);
        img_cooking_delightsthree = findViewById(R.id.img_cooking_delightthree);
        txt_cooking_delightsthree = findViewById(R.id.txt_cooking_delightthree);

        lin_cooking_delightsfour =findViewById(R.id.lin_cooking_delightfour);
        img_cooking_delightsfour = findViewById(R.id.img_cooking_delightfour);
        txt_cooking_delightsfour = findViewById(R.id.txt_cooking_delightfour);

        lin_cooking_delightsfive =findViewById(R.id.lin_cooking_delightfive);
        img_cooking_delightsfive = findViewById(R.id.img_cooking_delightfive);
        txt_cooking_delightsfive = findViewById(R.id.txt_cooking_delightfive);

        lin_cooking_delightssix =findViewById(R.id.lin_cooking_delightsix);
        img_cooking_delightssix = findViewById(R.id.img_cooking_delightsix);
        txt_cooking_delightssix = findViewById(R.id.txt_cooking_delightsix);


        //fruits and nuts

        lin_fruits_and_nutsone=findViewById(R.id.lin_fruits_and_nutsone);
        lin_fruits_and_nutstwo=findViewById(R.id.lin_fruits_and_nutstwo);
        lin_fruits_and_nutsthree=findViewById(R.id.lin_fruits_and_nutsthree);
        fruitsandnuts_catelogueRef = FirebaseDatabase.getInstance().getReference().child("fruitsandnuts_catelogue");

        txt_fruits_and_nutsone=findViewById(R.id.txt_fruits_and_nutsone);
        txt_fruits_and_nutstwo=findViewById(R.id.txt_fruits_and_nutstwo);
        txt_fruits_and_nutsthree=findViewById(R.id.txt_fruits_and_nutsthree);

        img_fruits_and_nutsone=findViewById(R.id.img_fruits_and_nutsone);
        img_fruits_and_nutstwo=findViewById(R.id.img_fruits_and_nutstwo);
        img_fruits_and_nutsthree=findViewById(R.id.img_fruits_and_nutsthree);


//end fruits and nuts


        breakfastRef = FirebaseDatabase.getInstance().getReference().child("breakfast_catelogue");

        lin_breakfastone = findViewById(R.id.lin_breakfastone);
        img_breakfastone =  findViewById(R.id.img_breakfastone);
        txt_breakfastone = findViewById(R.id.txt_breakfastone);

        lin_breakfasttwo =findViewById(R.id.lin_breakfasttwo);
        img_breakfasttwo = findViewById(R.id.img_breakfasttwo);
        txt_breakfasttwo = findViewById(R.id.txt_breakfasttwo);

        lin_breakfastthree =findViewById(R.id.lin_breakfastthree);
        img_breakfastthree = findViewById(R.id.img_breakfastthree);
        txt_breakfastthree = findViewById(R.id.txt_breakfastthree);

      /*  lin_breakfastfour =findViewById(R.id.lin_breakfastfour);
        img_breakfastfour = findViewById(R.id.img_breakfastfour);
        txt_breakfastfour = findViewById(R.id.txt_breakfastfour);*/

        badge_cart = findViewById(R.id.badge_cart);
        txt_badge_count= findViewById(R.id.txt_badge_count);
        cartRef = FirebaseDatabase.getInstance().getReference().child("Location")
                .child("North 24 Parganas").child("cart").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        cartRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                txt_badge_count.setText(String.valueOf(dataSnapshot.getChildrenCount()));

                if(dataSnapshot.exists())
                {
                    badge_cart.setVisibility(View.VISIBLE);
                    badgecount = 0;
                    for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren())
                    {
                        badgecount= badgecount+1;
                        txt_badge_count.setText(String.valueOf(badgecount));
                    }
                }
                else
                {
                    badge_cart.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        /*
        *
        *
        * fire base msg subscribe to topic importent
        cmt.
        *
        *
          FirebaseMessaging.getInstance().subscribeToTopic(FirebaseAuth.getInstance().getCurrentUser().getUid());
         */


        img_nextday_del= findViewById(R.id.img_nextday_del);
        img_nextday_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelorder cancelobj = new cancelorder();
                FragmentTransaction fragmentTransaction_cancel = MainActivity.this.getSupportFragmentManager().beginTransaction();
                fragmentTransaction_cancel.replace(parent_frame_layout_client.getId(),cancelobj);
                fragmentTransaction_cancel.addToBackStack(null);
                fragmentTransaction_cancel.commit();

            }
        });


        promo1 = findViewById(R.id.promo1);
        promo2=findViewById(R.id.promo2);
        promo3 = findViewById(R.id.promo3);
        promo4=findViewById(R.id.promo4);




        promoimg1Db= FirebaseDatabase.getInstance().getReference().child("Promo").child("image1");
        promoimg2Db= FirebaseDatabase.getInstance().getReference().child("Promo").child("image2");
        promoimg3Db= FirebaseDatabase.getInstance().getReference().child("Promo").child("image3");
        promoimg4Db= FirebaseDatabase.getInstance().getReference().child("Promo").child("image4");
        promoimg5Db= FirebaseDatabase.getInstance().getReference().child("Promo").child("inapp");



        promoimg1Db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String promo= dataSnapshot.getValue().toString();
                Picasso.get().load(promo).placeholder(R.drawable.logo).into(promo1);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        promoimg2Db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String promo= dataSnapshot.getValue().toString();
                Picasso.get().load(promo).placeholder(R.drawable.logo).into(promo2);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        promoimg3Db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String promo= dataSnapshot.getValue().toString();
                Picasso.get().load(promo).placeholder(R.drawable.logo).into(promo3);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        promoimg4Db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String promo= dataSnapshot.getValue().toString();
                Picasso.get().load(promo).placeholder(R.drawable.logo).into(promo4);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        promoimg5Db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String flag = dataSnapshot.child("flag").getValue().toString();
                if(flag.equals("1"))
                {
                    LayoutInflater layout = LayoutInflater.from(getApplicationContext());
                    final View dialogView = layout.inflate(R.layout.promo_alert_inapp, null);
                    final AlertDialog dialog = new AlertDialog.Builder(MainActivity.this).create();
                    dialog.setView(dialogView);
                    if(promoAlertcount==1) {
                        dialog.show();
                    }
                    promoAlertcount++;
                    ImageView close = dialogView.findViewById(R.id.close_promo);
                    close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    ImageView promo5 = dialogView.findViewById(R.id.promo5);

                    String promo= dataSnapshot.child("image5").getValue().toString();
                    Picasso.get().load(promo).placeholder(R.drawable.logo).into(promo5);

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        promo1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                promoimg1directdb=FirebaseDatabase.getInstance().getReference().child("Promo").child("image1cat");
                promoimg1directdb.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists())
                        {
                            String cat = dataSnapshot.getValue().toString();
                            Bundle bundle = new Bundle();
                            String myCategory = cat;
                            bundle.putString("message", myCategory );
                            bundle.putString("select","");
                            Cooking_delightsFragment fragobjcookingdelight = new Cooking_delightsFragment();
                            fragobjcookingdelight.setArguments(bundle);
                            FragmentTransaction fragmentTransaction = MainActivity.this.getSupportFragmentManager().beginTransaction();
                            fragmentTransaction.replace(parent_frame_layout_client.getId(),fragobjcookingdelight);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });
        promo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promoimg2directdb=FirebaseDatabase.getInstance().getReference().child("Promo").child("image2cat");
                promoimg2directdb.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists())
                        {
                            String cat = dataSnapshot.getValue().toString();
                            Bundle bundle = new Bundle();
                            String myCategory = cat;
                            bundle.putString("message", myCategory );
                            bundle.putString("select","");
                            Cooking_delightsFragment fragobjcookingdelight = new Cooking_delightsFragment();
                            fragobjcookingdelight.setArguments(bundle);
                            FragmentTransaction fragmentTransaction = MainActivity.this.getSupportFragmentManager().beginTransaction();
                            fragmentTransaction.replace(parent_frame_layout_client.getId(),fragobjcookingdelight);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });
        promo3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promoimg3directdb=FirebaseDatabase.getInstance().getReference().child("Promo").child("image3cat");
                promoimg3directdb.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists())
                        {
                            String cat = dataSnapshot.getValue().toString();
                            Bundle bundle = new Bundle();
                            String myCategory = cat;
                            bundle.putString("message", myCategory );
                            bundle.putString("select","");
                            Cooking_delightsFragment fragobjcookingdelight = new Cooking_delightsFragment();
                            fragobjcookingdelight.setArguments(bundle);
                            FragmentTransaction fragmentTransaction = MainActivity.this.getSupportFragmentManager().beginTransaction();
                            fragmentTransaction.replace(parent_frame_layout_client.getId(),fragobjcookingdelight);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });
        promo4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promoimg4directdb=FirebaseDatabase.getInstance().getReference().child("Promo").child("image4cat");
                promoimg4directdb.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists())
                        {
                            String cat = dataSnapshot.getValue().toString();
                            Bundle bundle = new Bundle();
                            String myCategory = cat;
                            bundle.putString("message", myCategory );
                            bundle.putString("select","");
                            Cooking_delightsFragment fragobjcookingdelight = new Cooking_delightsFragment();
                            fragobjcookingdelight.setArguments(bundle);
                            FragmentTransaction fragmentTransaction = MainActivity.this.getSupportFragmentManager().beginTransaction();
                            fragmentTransaction.replace(parent_frame_layout_client.getId(),fragobjcookingdelight);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });

        lin_fresh_subcat1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                String myCategory = "Fresh";
                bundle.putString("message", myCategory );
                bundle.putString("select",txt_fresh_subcat1.getText().toString());
                Cooking_delightsFragment fragobjcookingdelight = new Cooking_delightsFragment();
                fragobjcookingdelight.setArguments(bundle);
                FragmentTransaction fragmentTransaction = MainActivity.this.getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(parent_frame_layout_client.getId(),fragobjcookingdelight);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });
        lin_fresh_subcat2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                String myCategory = "Fresh";
                bundle.putString("message", myCategory );
                bundle.putString("select",txt_fresh_subcat2.getText().toString());
                Cooking_delightsFragment fragobjcookingdelight = new Cooking_delightsFragment();
                fragobjcookingdelight.setArguments(bundle);
                FragmentTransaction fragmentTransaction = MainActivity.this.getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(parent_frame_layout_client.getId(),fragobjcookingdelight);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });
        lin_fresh_subcat3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                String myCategory = "Fresh";
                bundle.putString("message", myCategory );
                bundle.putString("select","Exoticvegetable");
                Cooking_delightsFragment fragobjcookingdelight = new Cooking_delightsFragment();
                fragobjcookingdelight.setArguments(bundle);
                FragmentTransaction fragmentTransaction = MainActivity.this.getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(parent_frame_layout_client.getId(),fragobjcookingdelight);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });


        lin_dairy_goodnessone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                String myCategory = "DairyGoodness";
                bundle.putString("message", myCategory );
                bundle.putString("select",txt_dairy_goodnessone.getText().toString());
                Cooking_delightsFragment fragobjcookingdelight = new Cooking_delightsFragment();
                fragobjcookingdelight.setArguments(bundle);
                FragmentTransaction fragmentTransaction = MainActivity.this.getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(parent_frame_layout_client.getId(),fragobjcookingdelight);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });
        lin_dairy_goodnesstwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                String myCategory = "DairyGoodness";
                bundle.putString("message", myCategory );
                bundle.putString("select",txt_dairy_goodnesstwo.getText().toString());
                Cooking_delightsFragment fragobjcookingdelight = new Cooking_delightsFragment();
                fragobjcookingdelight.setArguments(bundle);
                FragmentTransaction fragmentTransaction = MainActivity.this.getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(parent_frame_layout_client.getId(),fragobjcookingdelight);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });
        lin_dairy_goodnessthree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                String myCategory = "DairyGoodness";
                bundle.putString("message", myCategory );
                bundle.putString("select",txt_dairy_goodnessthree.getText().toString());
                Cooking_delightsFragment fragobjcookingdelight = new Cooking_delightsFragment();
                fragobjcookingdelight.setArguments(bundle);
                FragmentTransaction fragmentTransaction = MainActivity.this.getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(parent_frame_layout_client.getId(),fragobjcookingdelight);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        lin_dairy_goodnessfour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                String myCategory = "DairyGoodness";
                bundle.putString("message", myCategory );
                bundle.putString("select",txt_dairy_goodnessfour.getText().toString());
                Cooking_delightsFragment fragobjcookingdelight = new Cooking_delightsFragment();
                fragobjcookingdelight.setArguments(bundle);
                FragmentTransaction fragmentTransaction = MainActivity.this.getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(parent_frame_layout_client.getId(),fragobjcookingdelight);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        lin_dairy_goodnessfive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                String myCategory = "DairyGoodness";
                bundle.putString("message", myCategory );
                bundle.putString("select",txt_dairy_goodnessfive.getText().toString());
                Cooking_delightsFragment fragobjcookingdelight = new Cooking_delightsFragment();
                fragobjcookingdelight.setArguments(bundle);
                FragmentTransaction fragmentTransaction = MainActivity.this.getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(parent_frame_layout_client.getId(),fragobjcookingdelight);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        lin_dairy_goodnesssix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                String myCategory = "DairyGoodness";
                bundle.putString("message", myCategory );
                bundle.putString("select",txt_dairy_goodnesssix.getText().toString());
                Cooking_delightsFragment fragobjcookingdelight = new Cooking_delightsFragment();
                fragobjcookingdelight.setArguments(bundle);
                FragmentTransaction fragmentTransaction = MainActivity.this.getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(parent_frame_layout_client.getId(),fragobjcookingdelight);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });








        lin_cooking_delightone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                String myCategory = "CookingDelights";
                bundle.putString("message", myCategory );
                bundle.putString("select",txt_cooking_delightone.getText().toString());
                Cooking_delightsFragment fragobjcookingdelight = new Cooking_delightsFragment();
                fragobjcookingdelight.setArguments(bundle);
                FragmentTransaction fragmentTransaction = MainActivity.this.getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(parent_frame_layout_client.getId(),fragobjcookingdelight);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        lin_cooking_delightstwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                String myCategory = "CookingDelights";
                bundle.putString("message", myCategory );
                bundle.putString("select",txt_cooking_delightstwo.getText().toString());
                Cooking_delightsFragment fragobjcookingdelight = new Cooking_delightsFragment();
                fragobjcookingdelight.setArguments(bundle);
                FragmentTransaction fragmentTransaction = MainActivity.this.getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(parent_frame_layout_client.getId(),fragobjcookingdelight);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });


        lin_cooking_delightsthree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                String myCategory = "CookingDelights";
                bundle.putString("message", myCategory );
                bundle.putString("select",txt_cooking_delightsthree.getText().toString());
                Cooking_delightsFragment fragobjcookingdelight = new Cooking_delightsFragment();
                fragobjcookingdelight.setArguments(bundle);
                FragmentTransaction fragmentTransaction = MainActivity.this.getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(parent_frame_layout_client.getId(),fragobjcookingdelight);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        lin_cooking_delightsfour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                String myCategory = "CookingDelights";
                bundle.putString("message", myCategory );
                bundle.putString("select",txt_cooking_delightsfour.getText().toString());
                Cooking_delightsFragment fragobjcookingdelight = new Cooking_delightsFragment();
                fragobjcookingdelight.setArguments(bundle);
                FragmentTransaction fragmentTransaction = MainActivity.this.getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(parent_frame_layout_client.getId(),fragobjcookingdelight);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        lin_cooking_delightsfive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                String myCategory = "CookingDelights";
                bundle.putString("message", myCategory );
                bundle.putString("select",txt_cooking_delightsfive.getText().toString());
                Cooking_delightsFragment fragobjcookingdelight = new Cooking_delightsFragment();
                fragobjcookingdelight.setArguments(bundle);
                FragmentTransaction fragmentTransaction = MainActivity.this.getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(parent_frame_layout_client.getId(),fragobjcookingdelight);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        lin_cooking_delightsfive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                String myCategory = "CookingDelights";
                bundle.putString("message", myCategory );
                bundle.putString("select",txt_cooking_delightsfive.getText().toString());
                Cooking_delightsFragment fragobjcookingdelight = new Cooking_delightsFragment();
                fragobjcookingdelight.setArguments(bundle);
                FragmentTransaction fragmentTransaction = MainActivity.this.getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(parent_frame_layout_client.getId(),fragobjcookingdelight);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        lin_cooking_delightssix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                String myCategory = "CookingDelights";
                bundle.putString("message", myCategory );
                bundle.putString("select",txt_cooking_delightssix.getText().toString());
                Cooking_delightsFragment fragobjcookingdelight = new Cooking_delightsFragment();
                fragobjcookingdelight.setArguments(bundle);
                FragmentTransaction fragmentTransaction = MainActivity.this.getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(parent_frame_layout_client.getId(),fragobjcookingdelight);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });



        //fruits & nuts
        lin_fruits_and_nutsone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                String myCategory = "FruitsandNuts";
                bundle.putString("message", myCategory );
                bundle.putString("select",txt_fruits_and_nutsone.getText().toString());
                Cooking_delightsFragment fragobjcookingdelight = new Cooking_delightsFragment();
                fragobjcookingdelight.setArguments(bundle);
                FragmentTransaction fragmentTransaction = MainActivity.this.getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(parent_frame_layout_client.getId(),fragobjcookingdelight);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        lin_fruits_and_nutstwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                String myCategory = "FruitsandNuts";
                bundle.putString("message", myCategory );
                bundle.putString("select",txt_fruits_and_nutstwo.getText().toString());
                Cooking_delightsFragment fragobjcookingdelight = new Cooking_delightsFragment();
                fragobjcookingdelight.setArguments(bundle);
                FragmentTransaction fragmentTransaction = MainActivity.this.getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(parent_frame_layout_client.getId(),fragobjcookingdelight);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        lin_fruits_and_nutsthree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                String myCategory = "FruitsandNuts";
                bundle.putString("message", myCategory );
                bundle.putString("select",txt_fruits_and_nutsthree.getText().toString());
                Cooking_delightsFragment fragobjcookingdelight = new Cooking_delightsFragment();
                fragobjcookingdelight.setArguments(bundle);
                FragmentTransaction fragmentTransaction = MainActivity.this.getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(parent_frame_layout_client.getId(),fragobjcookingdelight);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });



        lin_breakfastone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                String myCategory = "Breakfast";
                bundle.putString("message", myCategory );
                bundle.putString("select",txt_breakfastone.getText().toString());
                Cooking_delightsFragment fragobjcookingdelight = new Cooking_delightsFragment();
                fragobjcookingdelight.setArguments(bundle);
                FragmentTransaction fragmentTransaction = MainActivity.this.getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(parent_frame_layout_client.getId(),fragobjcookingdelight);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                closeDrawer();
            }
        });

        lin_breakfasttwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                String myCategory = "Breakfast";
                bundle.putString("message", myCategory );
                bundle.putString("select",txt_breakfasttwo.getText().toString());
                Cooking_delightsFragment fragobjcookingdelight = new Cooking_delightsFragment();
                fragobjcookingdelight.setArguments(bundle);
                FragmentTransaction fragmentTransaction = MainActivity.this.getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(parent_frame_layout_client.getId(),fragobjcookingdelight);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                closeDrawer();
            }
        });


        lin_breakfastthree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                String myCategory = "Breakfast";
                bundle.putString("message", myCategory );
                bundle.putString("select",txt_breakfastthree.getText().toString());
                Cooking_delightsFragment fragobjcookingdelight = new Cooking_delightsFragment();
                fragobjcookingdelight.setArguments(bundle);
                FragmentTransaction fragmentTransaction = MainActivity.this.getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(parent_frame_layout_client.getId(),fragobjcookingdelight);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                closeDrawer();
            }
        });

/*        lin_breakfastfour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                String myCategory = "Breakfast";
                bundle.putString("message", myCategory );
                Cooking_delightsFragment fragobjcookingdelight = new Cooking_delightsFragment();
                bundle.putString("select",txt_breakfastfour.getText().toString());
                fragobjcookingdelight.setArguments(bundle);
                FragmentTransaction fragmentTransaction = MainActivity.this.getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(parent_frame_layout_client.getId(),fragobjcookingdelight);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                closeDrawer();
            }
        });*/

        lin_homeessential = findViewById(R.id.lin_homeessential);
        lin_cleaningessential=findViewById(R.id.lin_cleaningessential);
        lin_personalcare=findViewById(R.id.lin_personalcare);
        lin_packedfood=findViewById(R.id.lin_packedfood);
        lin_babycare=findViewById(R.id.lin_babycare);
        lin_wellness = findViewById(R.id.lin_wellness);

        img_homeessential=findViewById(R.id.img_homeessential);
        img_cleaningessential=findViewById(R.id.img_cleaningessential);
        img_personalcare=findViewById(R.id.img_personalcare);
        img_packedfood=findViewById(R.id.img_packedfood);
        img_babycare=findViewById(R.id.img_babycare);
        img_wellness = findViewById(R.id.img_wellness);

        txt_homeessential=findViewById(R.id.txt_homeessential);
        txt_cleaningessential=findViewById(R.id.txt_cleaningessential);
        txt_personalcare=findViewById(R.id.txt_personalcare);
        txt_packedfood=findViewById(R.id.txt_packedfood);
        txt_babycare=findViewById(R.id.txt_babycare);
        txt_wellness = findViewById(R.id.txt_wellness);

        essentialsref =FirebaseDatabase.getInstance().getReference().child("essential_catelogue");

        lin_homeessential.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                String myCategory = "HomeEssential";
                bundle.putString("message", myCategory );
                bundle.putString("select","Storage&container");
                Cooking_delightsFragment fragobjcookingdelight = new Cooking_delightsFragment();
                fragobjcookingdelight.setArguments(bundle);
                FragmentTransaction fragmentTransaction = MainActivity.this.getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(parent_frame_layout_client.getId(),fragobjcookingdelight);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        lin_cleaningessential.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                String myCategory = "CleaningEssential";
                bundle.putString("message", myCategory );
                bundle.putString("select"," Detergent");
                Cooking_delightsFragment fragobjcookingdelight = new Cooking_delightsFragment();
                fragobjcookingdelight.setArguments(bundle);
                FragmentTransaction fragmentTransaction = MainActivity.this.getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(parent_frame_layout_client.getId(),fragobjcookingdelight);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                closeDrawer();
            }
        });

        lin_personalcare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                String myCategory = "PersonalCare";
                bundle.putString("message", myCategory );
                bundle.putString("select","shampoo");
                Cooking_delightsFragment fragobjcookingdelight = new Cooking_delightsFragment();
                fragobjcookingdelight.setArguments(bundle);
                FragmentTransaction fragmentTransaction = MainActivity.this.getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(parent_frame_layout_client.getId(),fragobjcookingdelight);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                closeDrawer();

            }
        });

        lin_packedfood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                String myCategory = "PackageFood";
                bundle.putString("message", myCategory );
                bundle.putString("select","Jam&Sauce");
                Cooking_delightsFragment fragobjcookingdelight = new Cooking_delightsFragment();
                fragobjcookingdelight.setArguments(bundle);
                FragmentTransaction fragmentTransaction = MainActivity.this.getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(parent_frame_layout_client.getId(),fragobjcookingdelight);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                closeDrawer();

            }
        });
        lin_babycare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                String myCategory = "BabyCare";
                bundle.putString("message", myCategory );
                bundle.putString("select","diper");
                Cooking_delightsFragment fragobjcookingdelight = new Cooking_delightsFragment();
                fragobjcookingdelight.setArguments(bundle);
                FragmentTransaction fragmentTransaction = MainActivity.this.getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(parent_frame_layout_client.getId(),fragobjcookingdelight);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                closeDrawer();
            }
        });

        lin_wellness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                String myCategory = "Wellness";
                bundle.putString("message", myCategory );
                bundle.putString("select","Detol");
                Cooking_delightsFragment fragobjcookingdelight = new Cooking_delightsFragment();
                fragobjcookingdelight.setArguments(bundle);
                FragmentTransaction fragmentTransaction = MainActivity.this.getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(parent_frame_layout_client.getId(),fragobjcookingdelight);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                closeDrawer();
            }
        });



                parent_frame_layout_client =findViewById(R.id.frame_layout_client);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.open_drawer,
                R.string.close_drawer);

        if (drawerLayout != null) {
            drawerLayout.addDrawerListener(toggle);
        }

        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id =menuItem.getItemId();

                switch (id)
                {
                    case (R.id.cancel_order):
                        cancelorder cancelobj = new cancelorder();
                        FragmentTransaction fragmentTransaction_cancel = MainActivity.this.getSupportFragmentManager().beginTransaction();
                        fragmentTransaction_cancel.replace(parent_frame_layout_client.getId(),cancelobj);
                        fragmentTransaction_cancel.addToBackStack(null);
                        fragmentTransaction_cancel.commit();
                        closeDrawer();
                        break;
                    case (R.id.my_cart):
                        CartFragment cartobj = new CartFragment();
                        FragmentTransaction fragmentTransaction_cart = MainActivity.this.getSupportFragmentManager().beginTransaction();
                        fragmentTransaction_cart.replace(parent_frame_layout_client.getId(),cartobj);
                        fragmentTransaction_cart.addToBackStack(null);
                        fragmentTransaction_cart.commit();
                        closeDrawer();
                        break;
                    case (R.id.my_orders):
                        OrderhistoryFragment orederhis = new OrderhistoryFragment();
                        FragmentTransaction fragmentTransactionorederhis = MainActivity.this.getSupportFragmentManager().beginTransaction();
                        fragmentTransactionorederhis.replace(parent_frame_layout_client.getId(),orederhis);
                        fragmentTransactionorederhis.addToBackStack(null);
                        fragmentTransactionorederhis.commit();
                        closeDrawer();
                        break;
                    case (R.id.dashboard):
                        Intent intent = new Intent(MainActivity.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    case (R.id.client_find_electrician):
                        Bundle bundle_fresh = new Bundle();
                        String myCategory_fresh = "Fresh";
                        bundle_fresh.putString("message", myCategory_fresh );
                        bundle_fresh.putString("select","Fruits");
                        Cooking_delightsFragment fragobjcookingdelight_fresh = new Cooking_delightsFragment();
                        fragobjcookingdelight_fresh.setArguments(bundle_fresh);
                        FragmentTransaction fragmentTransaction = MainActivity.this.getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(parent_frame_layout_client.getId(),fragobjcookingdelight_fresh);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                        closeDrawer();

                        break;

                    case (R.id.client_find_appliance):
                       // Toast.makeText(MainActivity.this, "milk selected", Toast.LENGTH_SHORT).show();

                        Bundle bundle_cook = new Bundle();
                        String myCategory_cook = "CookingDelights";
                        bundle_cook.putString("message", myCategory_cook );
                        bundle_cook.putString("select","Atta");
                        Cooking_delightsFragment fragobjcookingdelight_cook = new Cooking_delightsFragment();
                        fragobjcookingdelight_cook.setArguments(bundle_cook);
                        FragmentTransaction fragmentTransaction_cook = MainActivity.this.getSupportFragmentManager().beginTransaction();
                        fragmentTransaction_cook.replace(parent_frame_layout_client.getId(),fragobjcookingdelight_cook);
                        fragmentTransaction_cook.addToBackStack(null);
                        fragmentTransaction_cook.commit();
                        closeDrawer();

                        break;
                    case(R.id.client_find_plumber):
                        Bundle bundle = new Bundle();
                        String myCategory = "DairyGoodness";
                        bundle.putString("message", myCategory );
                        bundle.putString("select","Milk");
                        Cooking_delightsFragment fragobjcookingdelight = new Cooking_delightsFragment();
                        fragobjcookingdelight.setArguments(bundle);
                        FragmentTransaction fragmentTransactiond = MainActivity.this.getSupportFragmentManager().beginTransaction();
                        fragmentTransactiond.replace(parent_frame_layout_client.getId(),fragobjcookingdelight);
                        fragmentTransactiond.addToBackStack(null);
                        fragmentTransactiond.commit();
                        closeDrawer();


                        break;

                    case(R.id.client_find_carpenter):
                        Bundle bundlew = new Bundle();
                        String myCategoryw = "Wellness";
                        bundlew.putString("message", myCategoryw );
                        bundlew.putString("select","");
                        Cooking_delightsFragment fragobjcookingdelightw = new Cooking_delightsFragment();
                        fragobjcookingdelightw.setArguments(bundlew);
                        FragmentTransaction fragmentTransactionw = MainActivity.this.getSupportFragmentManager().beginTransaction();
                        fragmentTransactionw.replace(parent_frame_layout_client.getId(),fragobjcookingdelightw);
                        fragmentTransactionw.addToBackStack(null);
                        fragmentTransactionw.commit();
                        closeDrawer();

                        break;

                    case (R.id.client_view_profile):

                     /*   userlocationRef = FirebaseDatabase.getInstance().getReference("Users");
                        userlocationRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot)
                                    {
                                        if(dataSnapshot.exists())
                                        {
                                            String phoneNumber = dataSnapshot.child("phoneNumber").getValue().toString();

                                            Intent profile = new Intent(MainActivity.this,ProfileActivity.class);
                                            profile.putExtra("phoneNumber",phoneNumber);
                                            startActivity(profile);

                                        }


                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });*/

                        ProfileFragment fragobjp = new ProfileFragment();
                        FragmentTransaction fragmentTransactionp = MainActivity.this.getSupportFragmentManager().beginTransaction();
                        fragmentTransactionp.replace(parent_frame_layout_client.getId(),fragobjp);
                        fragmentTransactionp.addToBackStack(null);
                        fragmentTransactionp.commit();
                        closeDrawer();


                        break;

                    case(R.id.client_log_out):
                        FirebaseAuth.getInstance().signOut();
                        Intent logoutIntent = new Intent(MainActivity.this,RegistrationActivity.class);
                        startActivity(logoutIntent);
                        finish();
                        break;

                    case(R.id.my_subscription):
                        SubscriptionFragment fragobjsub = new SubscriptionFragment();
                        FragmentTransaction fragmentTransactionsub = MainActivity.this.getSupportFragmentManager().beginTransaction();
                        fragmentTransactionsub.replace(parent_frame_layout_client.getId(),fragobjsub);
                        fragmentTransactionsub.addToBackStack(null);
                        fragmentTransactionsub.commit();
                        closeDrawer();
                        break;

                  /*  case(R.id.my_week_subscription):
                        SubscriptionweekFragment fragobjsubW = new SubscriptionweekFragment();
                        FragmentTransaction fragmentTransactionsubW = MainActivity.this.getSupportFragmentManager().beginTransaction();
                        fragmentTransactionsubW.replace(parent_frame_layout_client.getId(),fragobjsubW);
                        fragmentTransactionsubW.addToBackStack(null);
                        fragmentTransactionsubW.commit();
                        closeDrawer();
                        break;

                    case(R.id.my_daily_subscription):
                        Dailysubscriptionfragment fragobjsubD = new Dailysubscriptionfragment();
                        FragmentTransaction fragmentTransactionsubD = MainActivity.this.getSupportFragmentManager().beginTransaction();
                        fragmentTransactionsubD.replace(parent_frame_layout_client.getId(),fragobjsubD);
                        fragmentTransactionsubD.addToBackStack(null);
                        fragmentTransactionsubD.commit();
                        closeDrawer();
                        break;*/



                }


                return true;
            }
        });

        cart_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CartFragment cartobj = new CartFragment();
                FragmentTransaction fragmentTransaction_cart = MainActivity.this.getSupportFragmentManager().beginTransaction();
                fragmentTransaction_cart.replace(parent_frame_layout_client.getId(),cartobj);
                fragmentTransaction_cart.addToBackStack(null);
                fragmentTransaction_cart.commit();
            }
        });

        et_search = findViewById(R.id.et_search);
        et_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setFocusable(true);
                v.setFocusableInTouchMode(true);
                SearchFragment searchobj = new SearchFragment();
                FragmentTransaction fragmentTransaction_cart = MainActivity.this.getSupportFragmentManager().beginTransaction();
                fragmentTransaction_cart.replace(parent_frame_layout_client.getId(),searchobj);
                fragmentTransaction_cart.addToBackStack(null);
                fragmentTransaction_cart.commit();
                closeDrawer();

            }
        });

        viewPager2 = findViewById(R.id.viewPage);
        imageDb = FirebaseDatabase.getInstance().getReference().child("Image");

        imageDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String child_data = dataSnapshot.child("image1").getValue().toString();
                Log.v("normal",""+child_data);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        imageDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {


                    final String Key = childDataSnapshot.getKey();
                    imageDb.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot)
                        {
                            String newValue = dataSnapshot.child(Key).getValue().toString();
                            Log.v("newchild",""+ newValue);
                            Log.v("Key",""+ Key); //displays the key for the node

                            sliderItems.add(new SliderItem(newValue));
                            viewPager2.setAdapter(new SliderAdapter(sliderItems,viewPager2));
                            viewPager2.setClipToPadding(false);
                            viewPager2.setClipChildren(false);
                            viewPager2.setOffscreenPageLimit(3);
                            viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        viewPager2.setAdapter(new SliderAdapter(sliderItems,viewPager2));
        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        viewPager2.setOffscreenPageLimit(3);
        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r= 1 - Math.abs(position);
                page.setScaleY(0.85f+r*0.15f);
            }
        });
        viewPager2.setPageTransformer(compositePageTransformer);

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                sliderHandler.removeCallbacks(sliderRunnable);
                sliderHandler.postDelayed(sliderRunnable,3000);

            }
        });



        userRef = FirebaseDatabase.getInstance().getReference().child("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());



        userRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {
                        if(dataSnapshot.exists())
                        {

                            String nameDb = dataSnapshot.child("name").getValue().toString();
                            String phone = dataSnapshot.child("phoneNumber").getValue().toString();
                            //  Toast.makeText(ServiceproviderActivity.this, nameDb, Toast.LENGTH_SHORT).show();

                            nameDbheader = nameDb;

                           // Toast.makeText(MainActivity.this, nameDbheader, Toast.LENGTH_SHORT).show();
                            mTextView = navigationView.getHeaderView(0).findViewById(R.id.drawer_header_txt);
                           TextView phoneText = navigationView.getHeaderView(0).findViewById(R.id.drawer_header_phone_txt);
                            mTextView.setText(nameDbheader);
                            phoneText.setText(phone);

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        //starting fresh

        //dairy_goodness
       // FirebaseRecyclerOptions<fresh_model> option_dairy_goodness= null;
       // dairy_goodness_recycler=findViewById(R.id.dairy_goodness_recycler);
       // RecyclerView.LayoutManager manager_dairy_goodness = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false);
       // dairy_goodness_recycler.setLayoutManager(manager_dairy_goodness);




        //bevarage

        lin_bevarage_subcatone = findViewById(R.id.lin_bevarage_subcatone);
        lin_bevarage_subcattwo=findViewById(R.id.lin_bevarage_subcattwo);
        lin_bevarage_subcatthree=findViewById(R.id.lin_bevarage_subcatthree);
      // lin_bevarage_subcatfour = findViewById(R.id.lin_bevarage_subcatfour);


        img_bevarage_subcatone= findViewById(R.id.img_bevarage_subcatone);
        img_bevarage_subcattwo=findViewById(R.id.img_bevarage_subcattwo);
        img_bevarage_subcatthree=findViewById(R.id.img_bevarage_subcatthree);
        //img_bevarage_subcatfour=findViewById(R.id.img_bevarage_subcatfour);

        txt_bevarage_subcatone=findViewById(R.id.txt_bevarage_subcatone);
        txt_bevarage_subcattwo=findViewById(R.id.txt_bevarage_subcattwo);
        txt_bevarage_subcatthree=findViewById(R.id.txt_bevarage_subcatthree);
        //txt_bevarage_subcatfour=findViewById(R.id.txt_bevarage_subcatfour);

        bevarageRef = FirebaseDatabase.getInstance().getReference().child("bevarage_catelogue");

        lin_bevarage_subcatone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                String myCategory = "Bevarage";
                bundle.putString("message", myCategory );
                bundle.putString("select",txt_bevarage_subcatone.getText().toString());
                Cooking_delightsFragment fragobjcookingdelight = new Cooking_delightsFragment();
                fragobjcookingdelight.setArguments(bundle);
                FragmentTransaction fragmentTransaction = MainActivity.this.getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(parent_frame_layout_client.getId(),fragobjcookingdelight);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                closeDrawer();
            }
        });

        lin_bevarage_subcattwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                String myCategory = "Bevarage";
                bundle.putString("message", myCategory );
                bundle.putString("select",txt_bevarage_subcattwo.getText().toString());
                Cooking_delightsFragment fragobjcookingdelight = new Cooking_delightsFragment();
                fragobjcookingdelight.setArguments(bundle);
                FragmentTransaction fragmentTransaction = MainActivity.this.getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(parent_frame_layout_client.getId(),fragobjcookingdelight);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                closeDrawer();
            }
        });

        lin_bevarage_subcatthree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                String myCategory = "Bevarage";
                bundle.putString("message", myCategory );
                bundle.putString("select",txt_bevarage_subcatthree.getText().toString());
                Cooking_delightsFragment fragobjcookingdelight = new Cooking_delightsFragment();
                fragobjcookingdelight.setArguments(bundle);
                FragmentTransaction fragmentTransaction = MainActivity.this.getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(parent_frame_layout_client.getId(),fragobjcookingdelight);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                closeDrawer();
            }
        });

        /*lin_bevarage_subcatfour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                String myCategory = "Bevarage";
                bundle.putString("message", myCategory );
                bundle.putString("select",txt_bevarage_subcatfour.getText().toString());
                Cooking_delightsFragment fragobjcookingdelight = new Cooking_delightsFragment();
                fragobjcookingdelight.setArguments(bundle);
                FragmentTransaction fragmentTransaction = MainActivity.this.getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(parent_frame_layout_client.getId(),fragobjcookingdelight);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                closeDrawer();
            }
        });*/




        //snacks

        lin_snacks_subcatone=findViewById(R.id.lin_snacks_subcatone);
        lin_snacks_subcattwo=findViewById(R.id.lin_snacks_subcattwo);
        lin_snacks_subcatthree=findViewById(R.id.lin_snacks_subcatthree);
       // lin_snacks_subcatfour=findViewById(R.id.lin_snacks_subcatfour);

        img_snacks_subcatone=findViewById(R.id.img_snacks_subcatone);
        img_snacks_subcattwo=findViewById(R.id.img_snacks_subcattwo);
        img_snacks_subcatthree=findViewById(R.id.img_snacks_subcatthree);
       // img_snacks_subcatfour=findViewById(R.id.img_snacks_subcatfour);

        txt_snacks_subcatone=findViewById(R.id.txt_snacks_subcatone);
        txt_snacks_subcattwo=findViewById(R.id.txt_snacks_subcattwo);
        txt_snacks_subcatthree=findViewById(R.id.txt_snacks_subcatthree);
      //  txt_snacks_subcatfour=findViewById(R.id.txt_snacks_subcatfour);

        snacksRef=FirebaseDatabase.getInstance().getReference().child("snacks_catelogue");

        lin_snacks_subcatone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                String myCategory = "Snacks";
                bundle.putString("message", myCategory );
                bundle.putString("select",txt_snacks_subcatone.getText().toString());
                Cooking_delightsFragment fragobjcookingdelight = new Cooking_delightsFragment();
                fragobjcookingdelight.setArguments(bundle);
                FragmentTransaction fragmentTransaction = MainActivity.this.getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(parent_frame_layout_client.getId(),fragobjcookingdelight);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                closeDrawer();
            }
        });
        lin_snacks_subcattwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                String myCategory = "Snacks";
                bundle.putString("message", myCategory );
                bundle.putString("select",txt_snacks_subcattwo.getText().toString());
                Cooking_delightsFragment fragobjcookingdelight = new Cooking_delightsFragment();
                fragobjcookingdelight.setArguments(bundle);
                FragmentTransaction fragmentTransaction = MainActivity.this.getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(parent_frame_layout_client.getId(),fragobjcookingdelight);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                closeDrawer();
            }
        });

        lin_snacks_subcatthree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                String myCategory = "Snacks";
                bundle.putString("message", myCategory );
                bundle.putString("select",txt_snacks_subcatthree.getText().toString());
                Cooking_delightsFragment fragobjcookingdelight = new Cooking_delightsFragment();
                fragobjcookingdelight.setArguments(bundle);
                FragmentTransaction fragmentTransaction = MainActivity.this.getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(parent_frame_layout_client.getId(),fragobjcookingdelight);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                closeDrawer();
            }
        });

       /* lin_snacks_subcatfour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                String myCategory = "Snacks";
                bundle.putString("message", myCategory );
                bundle.putString("select",txt_snacks_subcatfour.getText().toString());
                Cooking_delightsFragment fragobjcookingdelight = new Cooking_delightsFragment();
                fragobjcookingdelight.setArguments(bundle);
                FragmentTransaction fragmentTransaction = MainActivity.this.getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(parent_frame_layout_client.getId(),fragobjcookingdelight);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                closeDrawer();
            }
        });*/



    }

    @Override
    protected void onStart() {
        super.onStart();

        userRef = FirebaseDatabase.getInstance().getReference().child("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());


        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    String subAdmin = dataSnapshot.child("subAdmin").getValue().toString();
                    fresh = FirebaseDatabase.getInstance().getReference().child("Location").child(subAdmin).child("Category")
                            .child("Fresh");

                    milk = FirebaseDatabase.getInstance().getReference().child("Location").child(subAdmin).child("Category")
                            .child("Milk");

                    cooking_delights =FirebaseDatabase.getInstance().getReference().child("Location").child(subAdmin).child("Category")
                            .child("CookingDelights");


                    FirebaseRecyclerOptions<fresh_model> option_milk = null;
                    FirebaseRecyclerOptions<fresh_model> option_cooking_delights= null;

                    image_fresh = FirebaseDatabase.getInstance().getReference().child("Image_fresh_catelogue").child("image1");
                    image_fresh.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String strimage_fresh=dataSnapshot.getValue().toString();
                         //   Picasso.get().load(strimage_fresh).placeholder(R.drawable.exoticveg).into(img_fresh);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                   /* lin_fresh.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Bundle bundle = new Bundle();
                            String myCategory = "Fresh";
                            bundle.putString("message", myCategory );
                            bundle.putString("select","Exoticvegetable");
                            Cooking_delightsFragment fragobjcookingdelight = new Cooking_delightsFragment();
                            fragobjcookingdelight.setArguments(bundle);
                            FragmentTransaction fragmentTransaction = MainActivity.this.getSupportFragmentManager().beginTransaction();
                            fragmentTransaction.replace(parent_frame_layout_client.getId(),fragobjcookingdelight);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();

                        }
                    });*/

                    image_milk = FirebaseDatabase.getInstance().getReference().child("Image_milk_catelogue").child("image1");
                    image_milk.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String strimage_milk=dataSnapshot.getValue().toString();
                           // Picasso.get().load(strimage_milk).placeholder(R.drawable.milk_home).into(img_milk);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                   /* lin_milk.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Bundle bundle = new Bundle();
                            String myCategory = "Milk";
                            bundle.putString("message", myCategory );
                            bundle.putString("select","FreshMilk");
                            Cooking_delightsFragment fragobjcookingdelight = new Cooking_delightsFragment();
                            fragobjcookingdelight.setArguments(bundle);
                            FragmentTransaction fragmentTransaction = MainActivity.this.getSupportFragmentManager().beginTransaction();
                            fragmentTransaction.replace(parent_frame_layout_client.getId(),fragobjcookingdelight);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();

                        }
                    });*/

                    fresh_catelogueRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists())
                            {
                                String image = dataSnapshot.child("subcat_one").child("image").getValue().toString();
                                Picasso.get().load(image).placeholder(R.drawable.fruit).into(img_fresh_subcat1);
                                String txt = dataSnapshot.child("subcat_one").child("name").getValue().toString();
                                txt_fresh_subcat1.setText(txt);

                                String image2 = dataSnapshot.child("subcat_two").child("image").getValue().toString();
                                Picasso.get().load(image2).placeholder(R.drawable.veg).into(img_fresh_subcat2);
                                String txt2 = dataSnapshot.child("subcat_two").child("name").getValue().toString();
                                txt_fresh_subcat2.setText(txt2);

                                String image3 = dataSnapshot.child("subcat_three").child("image").getValue().toString();
                                Picasso.get().load(image3).placeholder(R.drawable.ex_veg).into(img_fresh_subcat3);
                                //String txt3 = dataSnapshot.child("subcat_three").child("name").getValue().toString();
                                //txt_cooking_delightsthree.setText(txt3);



                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                    dairygoodness_catelogueRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String image = dataSnapshot.child("subcat_one").child("image").getValue().toString();
                            Picasso.get().load(image).placeholder(R.drawable.milk).into(img_dairy_goodnessone);
                            String txt = dataSnapshot.child("subcat_one").child("name").getValue().toString();
                            txt_dairy_goodnessone.setText(txt);

                            String image2 = dataSnapshot.child("subcat_two").child("image").getValue().toString();
                            Picasso.get().load(image2).placeholder(R.drawable.butter).into(img_dairy_goodnesstwo);
                            String txt2 = dataSnapshot.child("subcat_two").child("name").getValue().toString();
                            txt_dairy_goodnesstwo.setText(txt2);

                            String image3 = dataSnapshot.child("subcat_three").child("image").getValue().toString();
                            Picasso.get().load(image3).placeholder(R.drawable.curd).into(img_dairy_goodnessthree);
                            String txt3 = dataSnapshot.child("subcat_three").child("name").getValue().toString();
                            txt_dairy_goodnessthree.setText(txt3);

                            String image4 = dataSnapshot.child("subcat_four").child("image").getValue().toString();
                            Picasso.get().load(image4).placeholder(R.drawable.ghee).into(img_dairy_goodnessfour);
                            String txt4 = dataSnapshot.child("subcat_four").child("name").getValue().toString();
                            txt_dairy_goodnessfour.setText(txt4);

                            String image5 = dataSnapshot.child("subcat_five").child("image").getValue().toString();
                            Picasso.get().load(image5).placeholder(R.drawable.paneer).into(img_dairy_goodnessfive);
                            String txt5 = dataSnapshot.child("subcat_five").child("name").getValue().toString();
                            txt_dairy_goodnessfive.setText(txt5);


                            String image6 = dataSnapshot.child("subcat_six").child("image").getValue().toString();
                            Picasso.get().load(image6).placeholder(R.drawable.cheese).into(img_dairy_goodnesssix);
                            String txt6 = dataSnapshot.child("subcat_six").child("name").getValue().toString();
                            txt_dairy_goodnesssix.setText(txt6);


                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                    cooking_delightsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            if(dataSnapshot.exists()) {
                                String image = dataSnapshot.child("subcat_one").child("image").getValue().toString();
                                Picasso.get().load(image).placeholder(R.drawable.atta).into(img_cooking_delightone);
                                String txt = dataSnapshot.child("subcat_one").child("name").getValue().toString();
                                txt_cooking_delightone.setText(txt);

                                String image2 = dataSnapshot.child("subcat_two").child("image").getValue().toString();
                                Picasso.get().load(image2).placeholder(R.drawable.rice).into(img_cooking_delightstwo);
                                String txt2 = dataSnapshot.child("subcat_two").child("name").getValue().toString();
                                txt_cooking_delightstwo.setText(txt2);

                                String image3 = dataSnapshot.child("subcat_three").child("image").getValue().toString();
                                Picasso.get().load(image3).placeholder(R.drawable.pulse).into(img_cooking_delightsthree);
                                String txt3 = dataSnapshot.child("subcat_three").child("name").getValue().toString();
                                txt_cooking_delightsthree.setText(txt3);

                                String image4 = dataSnapshot.child("subcat_four").child("image").getValue().toString();
                                Picasso.get().load(image4).placeholder(R.drawable.salt).into(img_cooking_delightsfour);
                                String txt4 = dataSnapshot.child("subcat_four").child("name").getValue().toString();
                                txt_cooking_delightsfour.setText(txt4);

                                String image5 = dataSnapshot.child("subcat_five").child("image").getValue().toString();
                                Picasso.get().load(image5).placeholder(R.drawable.oil).into(img_cooking_delightsfive);
                                String txt5 = dataSnapshot.child("subcat_five").child("name").getValue().toString();
                                txt_cooking_delightsfive.setText(txt5);

                                String image6 = dataSnapshot.child("subcat_six").child("image").getValue().toString();
                                Picasso.get().load(image6).placeholder(R.drawable.sugar).into(img_cooking_delightssix);
                                String txt6 = dataSnapshot.child("subcat_six").child("name").getValue().toString();
                                txt_cooking_delightssix.setText(txt6);


                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                    fruitsandnuts_catelogueRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String image = dataSnapshot.child("subcat_one").child("image").getValue().toString();
                            Picasso.get().load(image).placeholder(R.drawable.dry_fruits).into(img_fruits_and_nutsone);
                            String txt = dataSnapshot.child("subcat_one").child("name").getValue().toString();
                            txt_fruits_and_nutsone.setText(txt);

                            String image2 = dataSnapshot.child("subcat_two").child("image").getValue().toString();
                            Picasso.get().load(image2).placeholder(R.drawable.berries).into(img_fruits_and_nutstwo);
                            String txt2 = dataSnapshot.child("subcat_two").child("name").getValue().toString();
                            txt_fruits_and_nutstwo.setText(txt2);

                            String image3 = dataSnapshot.child("subcat_three").child("image").getValue().toString();
                            Picasso.get().load(image3).placeholder(R.drawable.cherry).into(img_fruits_and_nutsthree);
                            String txt3 = dataSnapshot.child("subcat_three").child("name").getValue().toString();
                            txt_fruits_and_nutsthree.setText(txt3);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    //dairy Goodness recycler
                 /*   FirebaseRecyclerOptions<fresh_model> option_dairy_goodness= null;

                    dairy_gooness =FirebaseDatabase.getInstance().getReference().child("Location").child(subAdmin).child("Category")
                            .child("DairyGoodness");
                    option_dairy_goodness =
                            new FirebaseRecyclerOptions.Builder<fresh_model>()
                                    .setQuery(dairy_gooness, fresh_model.class)
                                    .build();


                    final FirebaseRecyclerAdapter<fresh_model,MainActivity.subcategory_list> firebaseRecyclerAdapter_dairy_goodness=
                            new FirebaseRecyclerAdapter<fresh_model, subcategory_list>(option_dairy_goodness) {


                                @Override
                                protected void onBindViewHolder(@NonNull final subcategory_list holder,
                                                                final int position, @NonNull final fresh_model model)
                                {
                                    holder.subcat_txt.setText(model.getName());
                                    Picasso.get().load(model.getImage()).placeholder(R.drawable.ad).into(holder.subcat_img);

                                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            holder.getAdapterPosition();
                                            String visit_user_id = getRef(position).getKey();
                                            Bundle bundle = new Bundle();
                                            String myCategory = "DairyGoodness";
                                            bundle.putString("message", myCategory );
                                            bundle.putString("select",model.getName());
                                            Cooking_delightsFragment fragobjcookingdelight = new Cooking_delightsFragment();
                                            fragobjcookingdelight.setArguments(bundle);
                                            FragmentTransaction fragmentTransaction = MainActivity.this.getSupportFragmentManager().beginTransaction();
                                            fragmentTransaction.replace(parent_frame_layout_client.getId(),fragobjcookingdelight);
                                            fragmentTransaction.addToBackStack(null);
                                            fragmentTransaction.commit();
                                            closeDrawer();

                                        }
                                    });

                                }

                                @NonNull
                                @Override
                                public MainActivity.subcategory_list onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subcategory_design,parent,false);
                                    MainActivity.subcategory_list viewHolder = new MainActivity.subcategory_list(view);
                                    return viewHolder;
                                }
                            };
                    firebaseRecyclerAdapter_dairy_goodness.startListening();
                    dairy_goodness_recycler.setAdapter(firebaseRecyclerAdapter_dairy_goodness);*/


                    //breakfast
                    breakfastRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists())
                            {
                                String image = dataSnapshot.child("subcat_one").child("image").getValue().toString();
                                Picasso.get().load(image).placeholder(R.drawable.bread).into(img_breakfastone);
                                String txt = dataSnapshot.child("subcat_one").child("name").getValue().toString();
                                txt_breakfastone.setText(txt);

                                String image2 = dataSnapshot.child("subcat_two").child("image").getValue().toString();
                                Picasso.get().load(image2).placeholder(R.drawable.eggs).into(img_breakfasttwo);
                                String txt2 = dataSnapshot.child("subcat_two").child("name").getValue().toString();
                                txt_breakfasttwo.setText(txt2);

                                String image3 = dataSnapshot.child("subcat_three").child("image").getValue().toString();
                                Picasso.get().load(image3).placeholder(R.drawable.flex).into(img_breakfastthree);
                                String txt3 = dataSnapshot.child("subcat_three").child("name").getValue().toString();
                                txt_breakfastthree.setText(txt3);

                              /*  String image4 = dataSnapshot.child("subcat_four").child("image").getValue().toString();
                                Picasso.get().load(image4).placeholder(R.drawable.ad).into(img_breakfastfour);
                                String txt4 = dataSnapshot.child("subcat_four").child("name").getValue().toString();
                                txt_breakfastfour.setText(txt4);*/

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    essentialsref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists())
                            {
                                String image = dataSnapshot.child("baby_care").child("image").getValue().toString();
                                Picasso.get().load(image).placeholder(R.drawable.baby_care).into(img_babycare);
                                String txt = dataSnapshot.child("baby_care").child("name").getValue().toString();
                                txt_babycare.setText(txt);


                                String image2 = dataSnapshot.child("cleaning_essentials").child("image").getValue().toString();
                                Picasso.get().load(image2).placeholder(R.drawable.cleaning).into(img_cleaningessential);
                                String txt2 = dataSnapshot.child("cleaning_essentials").child("name").getValue().toString();
                                txt_cleaningessential.setText(txt2);

                                String image3 = dataSnapshot.child("home_essentials").child("image").getValue().toString();
                                Picasso.get().load(image3).placeholder(R.drawable.home).into(img_homeessential);
                                String txt3 = dataSnapshot.child("home_essentials").child("name").getValue().toString();
                                txt_homeessential.setText(txt3);


                                String image4 = dataSnapshot.child("packed_food").child("image").getValue().toString();
                                Picasso.get().load(image4).placeholder(R.drawable.packed_food).into(img_packedfood);
                                String txt4 = dataSnapshot.child("packed_food").child("name").getValue().toString();
                                txt_packedfood.setText(txt4);

                                String image5 = dataSnapshot.child("personal_care").child("image").getValue().toString();
                                Picasso.get().load(image5).placeholder(R.drawable.personal_care).into(img_personalcare);
                                String txt5 = dataSnapshot.child("personal_care").child("name").getValue().toString();
                                txt_personalcare.setText(txt5);

                                String image6 = dataSnapshot.child("wellness").child("image").getValue().toString();
                                Picasso.get().load(image6).placeholder(R.drawable.wellness).into(img_wellness);
                                String txt6 = dataSnapshot.child("wellness").child("name").getValue().toString();
                                txt_wellness.setText(txt6);



                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    //bevarage

                    bevarageRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists())
                            {
                                String image = dataSnapshot.child("subcat_one").child("image").getValue().toString();
                                Picasso.get().load(image).placeholder(R.drawable.tea).into(img_bevarage_subcatone);
                                String txt = dataSnapshot.child("subcat_one").child("name").getValue().toString();
                                txt_bevarage_subcatone.setText(txt);

                                String image2 = dataSnapshot.child("subcat_two").child("image").getValue().toString();
                                Picasso.get().load(image2).placeholder(R.drawable.coffee).into(img_bevarage_subcattwo);
                                String txt2 = dataSnapshot.child("subcat_two").child("name").getValue().toString();
                                txt_bevarage_subcattwo.setText(txt2);

                                String image3 = dataSnapshot.child("subcat_three").child("image").getValue().toString();
                                Picasso.get().load(image3).placeholder(R.drawable.health_drink).into(img_bevarage_subcatthree);
                                String txt3 = dataSnapshot.child("subcat_three").child("name").getValue().toString();
                                txt_bevarage_subcatthree.setText(txt3);

                              /*  String image4 = dataSnapshot.child("subcat_four").child("image").getValue().toString();
                                Picasso.get().load(image4).placeholder(R.drawable.ad).into(img_bevarage_subcatfour);
                                String txt4 = dataSnapshot.child("subcat_four").child("name").getValue().toString();
                                txt_bevarage_subcatfour.setText(txt4);*/

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });





                    //snacks


                    snacksRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists())
                            {
                                String image = dataSnapshot.child("subcat_one").child("image").getValue().toString();
                                Picasso.get().load(image).placeholder(R.drawable.chocolate).into(img_snacks_subcatone);
                                String txt = dataSnapshot.child("subcat_one").child("name").getValue().toString();
                                txt_snacks_subcatone.setText(txt);

                                String image2 = dataSnapshot.child("subcat_two").child("image").getValue().toString();
                                Picasso.get().load(image2).placeholder(R.drawable.nuggets).into(img_snacks_subcattwo);
                                String txt2 = dataSnapshot.child("subcat_two").child("name").getValue().toString();
                                txt_snacks_subcattwo.setText(txt2);

                                String image3 = dataSnapshot.child("subcat_three").child("image").getValue().toString();
                                Picasso.get().load(image3).placeholder(R.drawable.popcorn).into(img_snacks_subcatthree);
                                String txt3 = dataSnapshot.child("subcat_three").child("name").getValue().toString();
                                txt_snacks_subcatthree.setText(txt3);

                              /*  String image4 = dataSnapshot.child("subcat_four").child("image").getValue().toString();
                                Picasso.get().load(image4).placeholder(R.drawable.ad).into(img_snacks_subcatfour);
                                String txt4 = dataSnapshot.child("subcat_four").child("name").getValue().toString();
                                txt_snacks_subcatfour.setText(txt4);*/

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                }







            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Window window = MainActivity.this.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(MainActivity.this,R.color.colorPrimaryDark));


        cartRef = FirebaseDatabase.getInstance().getReference().child("Location").child("North 24 Parganas").child("cart").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        cartRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                txt_badge_count.setText(String.valueOf(dataSnapshot.getChildrenCount()));

                if(dataSnapshot.exists())
                {
                    badge_cart.setVisibility(View.VISIBLE);
                    badgecount = 0;
                    for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren())
                    {
                        badgecount= badgecount+1;
                        txt_badge_count.setText(String.valueOf(badgecount));
                    }
                }
                else
                {
                    badge_cart.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    public static class subcategory_list extends RecyclerView.ViewHolder
    {
        TextView subcat_txt;
        ImageView subcat_img;

        public subcategory_list(@NonNull View itemView) {
            super(itemView);

            subcat_txt = itemView.findViewById(R.id.subcategory_txt);
            subcat_img = itemView.findViewById(R.id.subcategory_img);

        }
    }

    public void  setupToolBar()
    {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("My Everyday Essentials");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {


        int id = item.getItemId();

        if(id == android.R.id.home)
        {
            drawerLayout.openDrawer(GravityCompat.START);
        }

        return super.onOptionsItemSelected(item);
    }

    private Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            viewPager2.setCurrentItem(viewPager2.getCurrentItem()+1);

        }
    };


    @Override
    protected void onPause() {
        super.onPause();
        sliderHandler.removeCallbacks(sliderRunnable);

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser!=null) {
            cartRef = FirebaseDatabase.getInstance().getReference().child("Location").child("North 24 Parganas").child("cart").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
            cartRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    txt_badge_count.setText(String.valueOf(dataSnapshot.getChildrenCount()));

                    if (dataSnapshot.exists()) {
                        badge_cart.setVisibility(View.VISIBLE);
                        badgecount = 0;
                        for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                            badgecount = badgecount + 1;
                            txt_badge_count.setText(String.valueOf(badgecount));
                        }
                    } else {
                        badge_cart.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        sliderHandler.postDelayed(sliderRunnable,3000);

        cartRef = FirebaseDatabase.getInstance().getReference().child("Location").child("North 24 Parganas").child("cart").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        cartRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                txt_badge_count.setText(String.valueOf(dataSnapshot.getChildrenCount()));

                if(dataSnapshot.exists())
                {
                    badge_cart.setVisibility(View.VISIBLE);
                    badgecount = 0;
                    for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren())
                    {
                        badgecount= badgecount+1;
                        txt_badge_count.setText(String.valueOf(badgecount));
                    }
                }
                else
                {
                    badge_cart.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    public void closeDrawer() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    public void onBackPressed() {

        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();
            //additional code
        } else {
            getSupportFragmentManager().popBackStack();
        }
        setupToolBar();

        cartRef = FirebaseDatabase.getInstance().getReference().child("Location").child("North 24 Parganas").child("cart").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        cartRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                txt_badge_count.setText(String.valueOf(dataSnapshot.getChildrenCount()));

                if(dataSnapshot.exists())
                {
                    badgecount = 0;
                    for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren())
                    {
                        badge_cart.setVisibility(View.VISIBLE);
                        badgecount= badgecount+1;
                        txt_badge_count.setText(String.valueOf(badgecount));
                    }
                }
                else
                {
                    badge_cart.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    public void badge()
    {
        cartRef = FirebaseDatabase.getInstance().getReference().child("Location").child("North 24 Parganas").child("cart").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        cartRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                txt_badge_count.setText(String.valueOf(dataSnapshot.getChildrenCount()));

                if(dataSnapshot.exists())
                {
                    badge_cart.setVisibility(View.VISIBLE);
                    badgecount = 0;
                    for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren())
                    {
                        badgecount= badgecount+1;
                        txt_badge_count.setText(String.valueOf(badgecount));
                    }
                }
                else
                {
                    badge_cart.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    @Override
    protected void onRestart() {
        super.onRestart();
        badge();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}
