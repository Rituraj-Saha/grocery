package com.technolgiya.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;
import com.mukesh.OtpView;

import java.util.concurrent.TimeUnit;

public class RegistrationActivity extends AppCompatActivity {

    private CountryCodePicker ccp;
    private EditText phoneText;
    private OtpView codeText;
    private Button continueAndNextBtn;
    private String checker = "",phoneNumber = "";
    private RelativeLayout relativeLayout;
    private String flag ="login";

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private FirebaseAuth mAuth;
    private String mVarificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private ProgressDialog loadingBar;

    private DatabaseReference userRef;

    TabLayout tabLayout;
    TabItem login,signup;

    private TextView txt_phone_number_given;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);


        userRef = FirebaseDatabase.getInstance().getReference().child("Users");

        tabLayout = findViewById(R.id.tabLayout);
        login =findViewById(R.id.login);
        signup=findViewById(R.id.signup);

        Log.d("flag", "onCreate: flag"+flag);


        mAuth = FirebaseAuth.getInstance();
        loadingBar = new ProgressDialog(this);

        phoneText = findViewById(R.id.phoneText);
        codeText = findViewById(R.id.codeText);
        continueAndNextBtn = findViewById(R.id.continueNextButton);
        relativeLayout = findViewById(R.id.phoneAuth);
        txt_phone_number_given =findViewById(R.id.txt_phone_number_given);
        ccp = findViewById(R.id.ccp);

        ccp.registerCarrierNumberEditText(phoneText);

        phoneText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(phoneText.getText().toString().equals(""))
                {
                    continueAndNextBtn.setEnabled(false);
                }
                else
                {
                    continueAndNextBtn.setBackgroundColor(Color.parseColor("#ea5f62"));
                    continueAndNextBtn.setEnabled(true);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        continueAndNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (continueAndNextBtn.getText().equals("Submit") || checker.equals("Code Sent")) {
                    String verificationCode = codeText.getText().toString();
                    if (verificationCode.equals("")) {
                        Toast.makeText(RegistrationActivity.this, "Please write Verification Code first", Toast.LENGTH_SHORT).show();
                    } else {
                        loadingBar.setTitle("Code Verification");
                        loadingBar.setMessage("Verification of code is in progress..");
                        loadingBar.setCanceledOnTouchOutside(false);
                        loadingBar.show();

                        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVarificationId, verificationCode);
                        signInWithPhoneAuthCredential(credential);

                    }
                } else {
                    phoneNumber = ccp.getFullNumberWithPlus();
                    if (!phoneNumber.equals("")) {
                        loadingBar.setTitle("Phone Number Verification");
                        loadingBar.setMessage("Verification of phone number is in progress..");
                        loadingBar.setCanceledOnTouchOutside(false);
                        loadingBar.show();

                        PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNumber,
                                60, TimeUnit.SECONDS,
                                RegistrationActivity.this,
                                mCallbacks);
                    } else {
                        Toast.makeText(RegistrationActivity.this, "Please enter the valid phone number", Toast.LENGTH_SHORT).show();
                    }
                }


            }
        });

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

                signInWithPhoneAuthCredential(phoneAuthCredential);


            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Toast.makeText(RegistrationActivity.this, "Invalid phone number..", Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
                relativeLayout.setVisibility(View.VISIBLE);
                continueAndNextBtn.setText("Continue");
                codeText.setVisibility(View.GONE);
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);

                mVarificationId = s;
                mResendToken = forceResendingToken;

                relativeLayout.setVisibility(View.GONE);
                checker = "Code Sent";
                continueAndNextBtn.setText("Submit");
                codeText.setVisibility(View.VISIBLE);
                txt_phone_number_given.setVisibility(View.VISIBLE);
                txt_phone_number_given.setText("please check the OTP sent to your mobile number"+" "+phoneNumber);
                loadingBar.dismiss();

                Toast.makeText(RegistrationActivity.this, "Code has been sent, Please check..", Toast.LENGTH_SHORT).show();

            }
        };

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        flag = "login";
                        Log.d("flag", "onCll: flag"+flag);
                        continueAndNextBtn.setText("Login Using Otp");
                        break;
                    case 1:
                        flag = "signup";
                        Log.d("flag", "onCls: flag"+flag);
                        continueAndNextBtn.setText("Sign Up Using Otp");
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            loadingBar.dismiss();
                            Toast.makeText(RegistrationActivity.this, "Welcome to Everyday Essential", Toast.LENGTH_SHORT).show();

                            sendUserToProfileActivity();
                            // Sign in success, update UI with the signed-in user's information
                           /* Log.d(TAG, "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();*/
                            // ...
                        } else {
                            // Sign in failed, display a message and update the UI
                           loadingBar.dismiss();
                           String e = task.getException().toString();
                            Toast.makeText(RegistrationActivity.this, "Error: "+e, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void sendUserToProfileActivity()
    {
        Intent intent = new Intent(RegistrationActivity.this,ProfileActivity.class);
        intent.putExtra("phoneNumber",phoneNumber);
        intent.putExtra("flag","signup");
        startActivity(intent);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if(flag.equals("login")){
        if(firebaseUser != null )
        {
            loadingBar.setTitle("Fetching account if any..");
            loadingBar.setMessage("Fetching account");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();


            userRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot)
                        {
                            if(dataSnapshot.exists())
                            {

                                String sevicetypeDb = dataSnapshot.child("serviceType").getValue().toString();
                                String locationDb = dataSnapshot.child("locationbutton").getValue().toString();


                               if(sevicetypeDb.equals("Client"))
                                {
                                    loadingBar.dismiss();
                                    Intent clientIntent =new Intent(RegistrationActivity.this,MainActivity.class);
                                    startActivity(clientIntent);
                                    finish();
                                }

                            }
                            else
                            {
                                Toast.makeText(RegistrationActivity.this, "Your account is not Found", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


        }

        else{
            Toast.makeText(this, "Please SignUp first", Toast.LENGTH_SHORT).show();
        }
        }


    }
}
