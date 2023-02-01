package com.technolgiya.myapplication;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class ProfileActivity extends AppCompatActivity implements View.OnFocusChangeListener {

    private String item="";
   // private ImageView profileImage;
    private static int GalleryPick = 1;
    //private EditText userProfileName;
    private String name="";

    private String fullname="";
   // private Spinner getLocation;
   // private Spinner getServiceType;
    private Button submit;
    private String checker="";
    private String locationChecker="";
    private String fullAddress="";
    private StorageReference userProfileImgRef;
    private ProgressDialog progressDialog;
    private  String downloadUrl;
    private TextView locationText;
    private TextView serviceText;
   // private String subAdmin="";
    private String phoneNumber="";

    private TextView txt_first_name,txt_last_name;
    private EditText et_first_name,et_last_name;

    private EditText userprofileemail_et;


    private LinearLayout getAdd;
    private TextView textView4,textView6;
    private EditText textView5;

    private String flag = "signup";

    FusedLocationProviderClient fusedLocationProviderClient;


    private Uri ImageUri;

    private DatabaseReference userRef;
    private DatabaseReference userlocationRef;

    private TextView greeting;

    private int evalue=0;

    private CardView crd_5;

    private TextView wallet_exist;
    private TextView coin_exist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        getAdd = findViewById(R.id.btn_add_get);

        textView4= findViewById(R.id.txt_add_4);
        textView5= findViewById(R.id.txt_add_5);
        textView6=findViewById(R.id.txt_add_6);
        crd_5=findViewById(R.id.crd_5);

        txt_first_name =findViewById(R.id.txt_first_name);
        txt_last_name =findViewById(R.id.txt_last_name);
        et_first_name= findViewById(R.id.et_first_name);
        et_last_name=findViewById(R.id.et_last_name);

        et_first_name.setOnFocusChangeListener(this);
        et_last_name.setOnFocusChangeListener(this);

        wallet_exist = findViewById(R.id.wallet_exist);
        coin_exist = findViewById(R.id.coin_exist);

        fullname = et_first_name.getText().toString()+" "+et_last_name.getText().toString();

        userprofileemail_et = findViewById(R.id.userprofileemail_et);

        greeting = findViewById(R.id.greeting);

        phoneNumber = getIntent().getExtras().get("phoneNumber").toString();
        flag =getIntent().getExtras().get("flag").toString();


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        getAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(ProfileActivity.this
                        , Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){

                    getLoc();
                }
                else
                {
                    ActivityCompat.requestPermissions(ProfileActivity.this
                            , new String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);

                }
            }
        });

        checker ="Client";


        locationText = findViewById(R.id.location_txt);
        serviceText=findViewById(R.id.service_txt);

        progressDialog = new ProgressDialog(this);

        userProfileImgRef = FirebaseStorage.getInstance().getReference().child("Profile Images");



        userlocationRef = FirebaseDatabase.getInstance().getReference().child("Location");
        userlocationRef.child(locationChecker);

        userRef = FirebaseDatabase.getInstance().getReference().child("Users");


        submit = findViewById(R.id.submit_btn);


        //profileImage = findViewById(R.id.profile_image);

       // profileImage.setOnClickListener(new View.OnClickListener() {
          /*  @Override
            public void onClick(View v) {

                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GalleryPick);
            }
        });*/



       // name = userProfileName.getText().toString();

        //getLocation = findViewById(R.id.spinner_location);
       // getLocation.setOnItemSelectedListener(this);


       /* getServiceType = findViewById(R.id.spinner_service);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.Spinner_item_sevice,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        getServiceType.setAdapter(adapter);
        getServiceType.setOnItemSelectedListener(this);*/



        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // saveUserData();
                saveInfoOnlyWithoutImage();

            }
        });

        retrieveUserInfo();



    }

    private void getLoc()
    {
        progressDialog.setTitle("fetching locationbutton");
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task)
            {

                Location location = task.getResult();
                if(location != null)
                {
                    progressDialog.dismiss();
                    submit.setBackgroundColor(Color.parseColor("#e46667"));
                    try {

                        Geocoder geocoder = new Geocoder(ProfileActivity.this,
                                Locale.getDefault());

                        List<Address> addresses = geocoder.getFromLocation(
                                location.getLatitude(),location.getLongitude(),1
                        );


                        textView4.setVisibility(View.GONE);
                        textView4.setText(Html.fromHtml("<font color='#6200EE'><b>Locality :</b><br></font>"
                                +addresses.get(0).getLocality()));

                        //locationChecker = textView4.toString();
                        crd_5.setVisibility(View.VISIBLE);
                        textView5.setVisibility(View.VISIBLE);

                        textView5.setText(Html.fromHtml(addresses.get(0).getAddressLine(0)));

                        textView6.setVisibility(View.GONE);
                        textView6.setText(Html.fromHtml("<font color='#6200EE'><b>SubAdmin :</b><br></font>"
                                +addresses.get(0).getSubAdminArea()));

                       /* subAdmin = addresses.get(0).getSubAdminArea();*/
                        // locationChecker=addresses.get(0).getLocality();
                        locationChecker=addresses.get(0).getPostalCode();
                         fullAddress = addresses.get(0).getAddressLine(0);

                       //  String pin = addresses.get(0).getPostalCode();

                       // Toast.makeText(ProfileActivity.this, pin, Toast.LENGTH_SHORT).show();


                        Log.d("msglocheckerrrrr", "saveLocationData: "+checker+" "+locationChecker);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        });



    }


    private void saveInfoOnlyWithoutImage() {


        fullAddress = textView5.getText().toString();
        final String getUserLocation = locationChecker;
        final String getUserService = checker;
        final String getUserFullAddress = fullAddress;
        final String getUserSubAdmin = "North 24 Parganas";
        final String getPhoneNumber=phoneNumber;
        final String fullname = et_first_name.getText().toString()+" "+et_last_name.getText().toString();
        final String email = userprofileemail_et.getText().toString();

        if(fullname.equals(""))
        {
            Toast.makeText(this, "user name can not be empty", Toast.LENGTH_LONG).show();
        }
        else if(getUserLocation.equals(""))
        {
            Toast.makeText(this, "Location can not be empty", Toast.LENGTH_LONG).show();
        }
        else if(getUserService.equals(""))
        {
            Toast.makeText(this, "please select your service type", Toast.LENGTH_LONG).show();
        }
        else if(email.equals(""))
        {
            Toast.makeText(this, "please enter your mailId", Toast.LENGTH_LONG).show();
        }
        else
        {
            progressDialog.setTitle("Account Settings");
            progressDialog.setMessage("Please wait...");
            progressDialog.show();

            HashMap<String, Object> profileMap = new HashMap<>();
            profileMap.put("uid", FirebaseAuth.getInstance().getCurrentUser().getUid());
            profileMap.put("name", fullname);
            profileMap.put("locationbutton", getUserLocation);
            profileMap.put("Address",getUserFullAddress);
            profileMap.put("subAdmin",getUserSubAdmin);
            profileMap.put("serviceType", getUserService);
            profileMap.put("phoneNumber",getPhoneNumber);
            profileMap.put("email",email);
            profileMap.put("wallet",wallet_exist.getText().toString());
            profileMap.put("coin",coin_exist.getText().toString());


            userRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).
                    updateChildren(profileMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task)
                {
                    if(task.isSuccessful())
                    {
                        if(checker.equals("Client"))
                        {
                            HashMap<String, Object> profileMap = new HashMap<>();
                            profileMap.put("uid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                            profileMap.put("name", fullname);
                            profileMap.put("locationbutton", getUserLocation);
                            profileMap.put("Address",getUserFullAddress);
                            profileMap.put("subAdmin",getUserSubAdmin);
                            profileMap.put("serviceType", getUserService);
                            profileMap.put("phoneNumber",getPhoneNumber);
                            profileMap.put("email",email);
                            //profileMap.put("wallet","0");

                            userlocationRef.child("North 24 Parganas").child(checker).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).
                                    updateChildren(profileMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {

                                        Intent serviceProvider = new Intent(ProfileActivity.this, MainActivity.class);
                                        startActivity(serviceProvider);
                                        finish();
                                        progressDialog.dismiss();

                                    }
                                }
                            });

                        }
                        Toast.makeText(ProfileActivity.this,
                                "Profile settings has been updated.",
                                Toast.LENGTH_SHORT).show();


                    }
                }
            });
        }
    }

    private void retrieveUserInfo()
    {
        userRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {
                        if(dataSnapshot.exists())
                        {
                            //String imageDb = dataSnapshot.child("image").getValue().toString();
                            String nameDb = dataSnapshot.child("name").getValue().toString();
                            String locationDb = dataSnapshot.child("locationbutton").getValue().toString();
                            String sevicetypeDb = dataSnapshot.child("serviceType").getValue().toString();
                            String addressDb=dataSnapshot.child("Address").getValue().toString();

                           // userProfileName.setText(nameDb);
                            locationText.setVisibility(View.GONE);
                          //  getLocation.setVisibility(View.GONE);
                            locationText.setText(locationDb);
                            textView5.setVisibility(View.GONE);
                            textView5.setText(addressDb);
                            serviceText.setVisibility(View.GONE);
                           // getServiceType.setVisibility(View.GONE);
                            serviceText.setText(sevicetypeDb);

                           // Picasso.get().load(imageDb).placeholder(R.drawable.profile_image).into(profileImage);
                            if(dataSnapshot.child("wallet").exists()){
                            String wallet = dataSnapshot.child("wallet").getValue().toString();
                                wallet_exist.setText(wallet);
                            }

                            if(dataSnapshot.child("coin").exists())
                            {
                                String coin = dataSnapshot.child("coin").getValue().toString();
                                coin_exist.setText(coin);
                            }


                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if(!flag.equals("edit")) {


            if (firebaseUser != null) {

                userRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {

                                    String sevicetypeDb = dataSnapshot.child("serviceType").getValue().toString();
                                    String locationDb = dataSnapshot.child("locationbutton").getValue().toString();


                                    if (sevicetypeDb.equals("Client")) {

                                        Intent clientIntent = new Intent(ProfileActivity.this, MainActivity.class);
                                        startActivity(clientIntent);
                                        finish();
                                    }

                                } else {
                                    Toast.makeText(ProfileActivity.this, "Your account is not Found", Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


            }
        }
        else
        {
            greeting.setVisibility(View.GONE);
        }

    }

    @Override
    public void onFocusChange(View view, boolean hasfocus) {
        switch(view.getId()){
            case R.id.et_first_name:
                //Do something
                txt_first_name.setVisibility(View.VISIBLE);
                et_first_name.setHint("");
                Log.d("et_one", "onFocusChange: "+"etfirst");
                break;
            case R.id.et_last_name:

                txt_last_name.setVisibility(View.VISIBLE);
                et_last_name.setHint("");

                break;

        }
    }

}
