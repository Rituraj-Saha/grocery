package com.technolgiya.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class PayActivity extends AppCompatActivity implements PaymentResultListener {

    TextView txtamount, name, upivirtualid;
    EditText note;
    private RadioGroup pay_radiogroup;
    private Button btn_continue;

    RadioButton  btpay;
    RadioButton cod;
    final int UPI_PAYMENT = 0;
    private String sAmount = "";
    private DatabaseReference bill,reqRef,productRef,walletRef;
    private ImageView back;
    private TextView delivary_charge_et;
    private TextView sub_total_et;
    private TextView discount_et;
    private TextView total_et;
    private String time="";
    private int availabilityflag=0;
    private String outofstck = "";
    private ProgressDialog progressDialog;

    private String slected_method ="";

    private TextView txt_total_amount;

    RadioButton credit,wallet,netbanck;

    int temp = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        btpay =  findViewById(R.id.send);
        cod=findViewById(R.id.cod);
        back = findViewById(R.id.back);

        walletRef=FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("wallet");
       credit = findViewById(R.id.credit);
       netbanck =findViewById(R.id.netbank);

       wallet = findViewById(R.id.wallet);
        progressDialog = new ProgressDialog(PayActivity.this);
        delivary_charge_et = findViewById(R.id.delivary_charge_et);
        sub_total_et = findViewById(R.id.sub_total_et);
        discount_et = findViewById(R.id.discount_et);
        total_et = findViewById(R.id.total_et);

        txtamount = (TextView) findViewById(R.id.amount_et);

        note = (EditText) findViewById(R.id.note);
        name = (TextView) findViewById(R.id.name);
        upivirtualid =(TextView) findViewById(R.id.upi_id);

        txt_total_amount = findViewById(R.id.txt_total_amount);


        pay_radiogroup = findViewById(R.id.pay_radiogroup);

        btn_continue = findViewById(R.id.btn_continue);

        sAmount = getIntent().getStringExtra("payamount");
        time=getIntent().getStringExtra("time");

        reqRef = FirebaseDatabase.getInstance().getReference().child("Location").child("North 24 Parganas")
                .child("Request").child(time).child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        txtamount.setText(sAmount);
        sAmount=sAmount.trim();
        final int amount = Math.round(Float.parseFloat(sAmount)*100);

        temp = Integer.valueOf(txtamount.getText().toString())+Integer.valueOf(delivary_charge_et.getText().toString());
        sub_total_et.setText(String.valueOf(sAmount));
        temp = temp - Integer.valueOf(discount_et.getText().toString());

        total_et.setText(String.valueOf(temp));

        txt_total_amount.setText(String.valueOf("\u20B9"+temp));

        productRef = FirebaseDatabase.getInstance().getReference().child("Location").child("North 24 Parganas")
                .child("product");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bill = FirebaseDatabase.getInstance().getReference().child("Location").child("North 24 Parganas").child("bill")
                        .child(time);
                reqRef = FirebaseDatabase.getInstance().getReference().child("Location").child("North 24 Parganas")
                        .child("Request").child(time).child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                bill.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){

                            reqRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        Intent intent = new Intent(PayActivity.this,BillingActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });

                        }
                    }
                });

            }
        });


                pay_radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        RadioButton choosen;
                        choosen = findViewById(checkedId);

                        if(checkedId == -1){
                            Toast.makeText(PayActivity.this,"Nothing selected", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(PayActivity.this,choosen.getText(), Toast.LENGTH_SHORT).show();

                            if(choosen.getText().equals("UPI"))
                            {
                                slected_method = "UPI";
                            }
                            else if(choosen.getText().equals("Credit / Debit Card"))
                            {
                                slected_method = "Credit / Debit Card";
                            }
                            else if(choosen.getText().equals("Wallets"))
                            {
                                slected_method = "Wallets";
                            }
                            else if(choosen.getText().equals("Net Banking"))
                            {
                                slected_method = "Net Banking";
                            }
                            else if(choosen.getText().equals("Cash on Delivery"))
                            {
                                slected_method = "Cash on Delivery";
                            }
                        }
                    }
                });

                btn_continue.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(PayActivity.this, slected_method, Toast.LENGTH_SHORT).show();

                        if(slected_method.equals("UPI"))
                        {
                            //slected_method = "UPI";

                                    progressDialog.setTitle("gathering stock info");
                                    progressDialog.setMessage("Please wait...");
                                    progressDialog.show();
                                    reqRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            for(final DataSnapshot reqchild:dataSnapshot.getChildren())
                                            {
                                                final String qty = reqchild.getValue().toString();

                                                productRef.child(reqchild.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {


                                                        String left = dataSnapshot.child("left").getValue().toString();

                                                        Log.d("availability", "availabe(1) left: " + left);

                                                        if (Integer.valueOf(left) < Integer.valueOf(qty)) {
                                                            Log.d("availability", "Quantity inside if(2) " + qty);
                                                            availabilityflag = 1;
                                                            Log.d("availability", "inside (3)" + availabilityflag);
                                                            // Toast.makeText(getContext(), ""+availabilityflag, Toast.LENGTH_SHORT).show();
                                                            outofstck = outofstck + reqchild.getKey();
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

                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            {

                                                progressDialog.dismiss();
                                                if (availabilityflag == 0) {
                                                    proceedtobilling(amount);

                                                } else {
                                                    final android.app.AlertDialog alertDialog3 = new android.app.AlertDialog.Builder(
                                                            PayActivity.this).create();
                                                    alertDialog3.setTitle("Cart message");
                                                    alertDialog3.setMessage(outofstck + " is out of stock");
                                                    alertDialog3.setIcon(R.drawable.playstore);
                                                    alertDialog3.setButton("OK", new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            alertDialog3.dismiss();
                                                        }
                                                    });

                                                    // Showing Alert Message
                                                    alertDialog3.show();
                                                    outofstck ="";
                                                    availabilityflag = 0;
                                                }
                                            }
                                        }
                                    }, 3000);





                        }
                        else if(slected_method.equals("Credit / Debit Card"))
                        {
                            //slected_method = "Credit / Debit Card";

                                    progressDialog.setTitle("gathering stock info");
                                    progressDialog.setMessage("Please wait...");
                                    progressDialog.show();
                                    reqRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            for(final DataSnapshot reqchild:dataSnapshot.getChildren())
                                            {
                                                final String qty = reqchild.getValue().toString();

                                                productRef.child(reqchild.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {


                                                        String left = dataSnapshot.child("left").getValue().toString();

                                                        Log.d("availability", "availabe(1) left: " + left);

                                                        if (Integer.valueOf(left) < Integer.valueOf(qty)) {
                                                            Log.d("availability", "Quantity inside if(2) " + qty);
                                                            availabilityflag = 1;
                                                            Log.d("availability", "inside (3)" + availabilityflag);
                                                            // Toast.makeText(getContext(), ""+availabilityflag, Toast.LENGTH_SHORT).show();
                                                            outofstck = outofstck + reqchild.getKey();
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

                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            {

                                                progressDialog.dismiss();
                                                if (availabilityflag == 0) {
                                                    proceedtobilling(amount);

                                                } else {
                                                    final android.app.AlertDialog alertDialog3 = new android.app.AlertDialog.Builder(
                                                            PayActivity.this).create();
                                                    alertDialog3.setTitle("Cart message");
                                                    alertDialog3.setMessage(outofstck + " is out of stock");
                                                    alertDialog3.setIcon(R.drawable.playstore);
                                                    alertDialog3.setButton("OK", new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            alertDialog3.dismiss();
                                                        }
                                                    });

                                                    // Showing Alert Message
                                                    alertDialog3.show();
                                                    outofstck ="";
                                                    availabilityflag = 0;
                                                }
                                            }
                                        }
                                    }, 3000);





                        }
                        else if(slected_method.equals("Wallets"))
                        {
                         //   slected_method = "Wallets";

                                    progressDialog.setTitle("gathering stock info");
                                    progressDialog.setMessage("Please wait...");
                                    progressDialog.show();
                                    reqRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            for(final DataSnapshot reqchild:dataSnapshot.getChildren())
                                            {
                                                final String qty = reqchild.getValue().toString();

                                                productRef.child(reqchild.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {


                                                        String left = dataSnapshot.child("left").getValue().toString();

                                                        Log.d("availability", "availabe(1) left: " + left);

                                                        if (Integer.valueOf(left) < Integer.valueOf(qty))
                                                        {
                                                            Log.d("availability", "Quantity inside if(2) " + qty);
                                                            availabilityflag = 1;
                                                            Log.d("availability", "inside (3)" + availabilityflag);
                                                            // Toast.makeText(getContext(), ""+availabilityflag, Toast.LENGTH_SHORT).show();
                                                            outofstck = outofstck + reqchild.getKey();

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

                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            {

                                                progressDialog.dismiss();
                                                if (availabilityflag == 0) {

                                                    reqRef = FirebaseDatabase.getInstance().getReference().child("Location").child("North 24 Parganas")
                                                            .child("Request").child(time).child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                                    reqRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            for(final DataSnapshot reqchild:dataSnapshot.getChildren())
                                                            {
                                                                final String qty = reqchild.getValue().toString();

                                                                productRef.child(reqchild.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(DataSnapshot dataSnapshot) {


                                                                        String left = dataSnapshot.child("left").getValue().toString();

                                                                        int remaining = Integer.valueOf(left) - Integer.valueOf(qty);
                                                                        Log.d("availability", "availabe(1) left: " + left);
                                                                        HashMap<String,Object> leftmap = new HashMap<>();
                                                                        leftmap.put("left",String.valueOf(remaining));
                                                                        productRef.child(reqchild.getKey()).updateChildren(leftmap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                if(task.isSuccessful())
                                                                                {
                                                                                    Toast.makeText(PayActivity.this, "remaining changed", Toast.LENGTH_SHORT).show();
                                                                                }
                                                                            }
                                                                        });

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



                                                    HashMap<String,Object>billmap = new HashMap<>();
                                                    billmap.put("status","paid by wallet");
                                                    bill = FirebaseDatabase.getInstance().getReference().child("Location").child("North 24 Parganas").child("bill")
                                                            .child(time);
                                                    bill.updateChildren(billmap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            reqRef = FirebaseDatabase.getInstance().getReference().child("Location").child("North 24 Parganas")
                                                                    .child("Request").child(time).child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                                            reqRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if(task.isSuccessful())
                                                                    {
                                                                        walletRef=FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                                                .child("wallet");
                                                                        walletRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                            @Override
                                                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                String walletbal = dataSnapshot.getValue().toString();
                                                                                if(Integer.valueOf(sAmount)<=Integer.valueOf(walletbal)) {
                                                                                    String updatebal = String.valueOf(Integer.valueOf(walletbal) - Integer.valueOf(sAmount));
                                                                                    HashMap<String,Object> updatebalmap = new HashMap<>();
                                                                                    updatebalmap.put("wallet",updatebal);
                                                                                    DatabaseReference walletupdate = FirebaseDatabase.getInstance().getReference()
                                                                                            .child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                                                                    walletupdate.updateChildren(updatebalmap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                        @Override
                                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                                            Intent intent = new Intent(PayActivity.this,MainActivity.class);
                                                                                            startActivity(intent);
                                                                                            finish();
                                                                                        }
                                                                                    });

                                                                                }
                                                                                else{
                                                                                    Toast.makeText(PayActivity.this, "Dont have sufficiant bal", Toast.LENGTH_SHORT).show();
                                                                                }
                                                                            }

                                                                            @Override
                                                                            public void onCancelled(DatabaseError databaseError) {

                                                                            }
                                                                        });


                                                                    }
                                                                }
                                                            });
                                                        }
                                                    });


                                                } else {
                                                    final android.app.AlertDialog alertDialog3 = new android.app.AlertDialog.Builder(
                                                            PayActivity.this).create();
                                                    alertDialog3.setTitle("Cart message");
                                                    alertDialog3.setMessage(outofstck + " is out of stock");
                                                    alertDialog3.setIcon(R.drawable.playstore);
                                                    alertDialog3.setButton("OK", new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            alertDialog3.dismiss();
                                                        }
                                                    });

                                                    // Showing Alert Message
                                                    alertDialog3.show();
                                                    outofstck ="";
                                                    availabilityflag = 0;
                                                }
                                            }
                                        }
                                    }, 3000);





                        }
                        else if(slected_method.equals("Net Banking"))
                        {
                            //slected_method = "Net Banking";

                                    progressDialog.setTitle("gathering stock info");
                                    progressDialog.setMessage("Please wait...");
                                    progressDialog.show();
                                    reqRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            for(final DataSnapshot reqchild:dataSnapshot.getChildren())
                                            {
                                                final String qty = reqchild.getValue().toString();

                                                productRef.child(reqchild.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {


                                                        String left = dataSnapshot.child("left").getValue().toString();

                                                        Log.d("availability", "availabe(1) left: " + left);

                                                        if (Integer.valueOf(left) < Integer.valueOf(qty)) {
                                                            Log.d("availability", "Quantity inside if(2) " + qty);
                                                            availabilityflag = 1;
                                                            Log.d("availability", "inside (3)" + availabilityflag);
                                                            // Toast.makeText(getContext(), ""+availabilityflag, Toast.LENGTH_SHORT).show();
                                                            outofstck = outofstck + reqchild.getKey();
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

                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            {

                                                progressDialog.dismiss();
                                                if (availabilityflag == 0) {
                                                    proceedtobilling(amount);

                                                } else {
                                                    final android.app.AlertDialog alertDialog3 = new android.app.AlertDialog.Builder(
                                                            PayActivity.this).create();
                                                    alertDialog3.setTitle("Cart message");
                                                    alertDialog3.setMessage(outofstck + " is out of stock");
                                                    alertDialog3.setIcon(R.drawable.playstore);
                                                    alertDialog3.setButton("OK", new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            alertDialog3.dismiss();
                                                        }
                                                    });

                                                    // Showing Alert Message
                                                    alertDialog3.show();
                                                    outofstck ="";
                                                    availabilityflag = 0;
                                                }
                                            }
                                        }
                                    }, 3000);







                        }
                        else if(slected_method.equals("Cash on Delivery"))
                        {
                                    progressDialog.setTitle("gathering stock info");
                                    progressDialog.setMessage("Please wait...");
                                    progressDialog.show();
                                    reqRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            for(final DataSnapshot reqchild:dataSnapshot.getChildren())
                                            {
                                                final String qty = reqchild.getValue().toString();

                                                productRef.child(reqchild.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {


                                                        String left = dataSnapshot.child("left").getValue().toString();

                                                        Log.d("availability", "availabe(1) left: " + left);

                                                        if (Integer.valueOf(left) < Integer.valueOf(qty)) {
                                                            Log.d("availability", "Quantity inside if(2) " + qty);
                                                            availabilityflag = 1;
                                                            Log.d("availability", "inside (3)" + availabilityflag);
                                                            // Toast.makeText(getContext(), ""+availabilityflag, Toast.LENGTH_SHORT).show();
                                                            outofstck = outofstck + reqchild.getKey();
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

                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            {

                                                progressDialog.dismiss();
                                                if (availabilityflag == 0) {

                                                    reqRef = FirebaseDatabase.getInstance().getReference().child("Location").child("North 24 Parganas")
                                                            .child("Request").child(time).child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                                    reqRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            for(final DataSnapshot reqchild:dataSnapshot.getChildren())
                                                            {
                                                                final String qty = reqchild.getValue().toString();

                                                                productRef.child(reqchild.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(DataSnapshot dataSnapshot) {


                                                                        String left = dataSnapshot.child("left").getValue().toString();

                                                                        int remaining = Integer.valueOf(left) - Integer.valueOf(qty);
                                                                        Log.d("availability", "availabe(1) left: " + left);
                                                                        HashMap<String,Object> leftmap = new HashMap<>();
                                                                        leftmap.put("left",String.valueOf(remaining));
                                                                        productRef.child(reqchild.getKey()).updateChildren(leftmap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                if(task.isSuccessful())
                                                                                {
                                                                                    Toast.makeText(PayActivity.this, "remaining changed", Toast.LENGTH_SHORT).show();
                                                                                }
                                                                            }
                                                                        });

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



                                                    HashMap<String,Object>billmap = new HashMap<>();
                                                    billmap.put("status","pay due by cash");
                                                    bill = FirebaseDatabase.getInstance().getReference().child("Location").child("North 24 Parganas").child("bill")
                                                            .child(time);
                                                    bill.updateChildren(billmap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            reqRef = FirebaseDatabase.getInstance().getReference().child("Location").child("North 24 Parganas")
                                                                    .child("Request").child(time).child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                                            reqRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if(task.isSuccessful())
                                                                    {
                                                                        Intent intent = new Intent(PayActivity.this,MainActivity.class);
                                                                        startActivity(intent);
                                                                        finish();
                                                                    }
                                                                }
                                                            });
                                                        }
                                                    });


                                                } else {
                                                    final android.app.AlertDialog alertDialog3 = new android.app.AlertDialog.Builder(
                                                            PayActivity.this).create();
                                                    alertDialog3.setTitle("Cart message");
                                                    alertDialog3.setMessage(outofstck + " is out of stock");
                                                    alertDialog3.setIcon(R.drawable.playstore);
                                                    alertDialog3.setButton("OK", new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            alertDialog3.dismiss();
                                                        }
                                                    });

                                                    // Showing Alert Message
                                                    alertDialog3.show();
                                                    outofstck ="";
                                                    availabilityflag = 0;
                                                }
                                            }
                                        }
                                    }, 3000);




                        }
                    }
                });



      /*  btpay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog.setTitle("gathering stock info");
                progressDialog.setMessage("Please wait...");
                progressDialog.show();
                reqRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(final DataSnapshot reqchild:dataSnapshot.getChildren())
                        {
                            final String qty = reqchild.getValue().toString();

                            productRef.child(reqchild.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {


                                    String left = dataSnapshot.child("left").getValue().toString();

                                    Log.d("availability", "availabe(1) left: " + left);

                                    if (Integer.valueOf(left) < Integer.valueOf(qty)) {
                                        Log.d("availability", "Quantity inside if(2) " + qty);
                                        availabilityflag = 1;
                                        Log.d("availability", "inside (3)" + availabilityflag);
                                        // Toast.makeText(getContext(), ""+availabilityflag, Toast.LENGTH_SHORT).show();
                                        outofstck = outofstck + reqchild.getKey();
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

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        {

                            progressDialog.dismiss();
                            if (availabilityflag == 0) {
                                proceedtobilling(amount);

                            } else {
                                final android.app.AlertDialog alertDialog3 = new android.app.AlertDialog.Builder(
                                        PayActivity.this).create();
                                alertDialog3.setTitle("Cart message");
                                alertDialog3.setMessage(outofstck + " is out of stock");
                                alertDialog3.setIcon(R.drawable.playstore);
                                alertDialog3.setButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        alertDialog3.dismiss();
                                    }
                                });

                                // Showing Alert Message
                                alertDialog3.show();
                                outofstck ="";
                                availabilityflag = 0;
                            }
                        }
                    }
                }, 3000);

            }
        });

        cod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog.setTitle("gathering stock info");
                progressDialog.setMessage("Please wait...");
                progressDialog.show();
                reqRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(final DataSnapshot reqchild:dataSnapshot.getChildren())
                        {
                            final String qty = reqchild.getValue().toString();

                            productRef.child(reqchild.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {


                                    String left = dataSnapshot.child("left").getValue().toString();

                                    Log.d("availability", "availabe(1) left: " + left);

                                    if (Integer.valueOf(left) < Integer.valueOf(qty)) {
                                        Log.d("availability", "Quantity inside if(2) " + qty);
                                        availabilityflag = 1;
                                        Log.d("availability", "inside (3)" + availabilityflag);
                                        // Toast.makeText(getContext(), ""+availabilityflag, Toast.LENGTH_SHORT).show();
                                        outofstck = outofstck + reqchild.getKey();
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

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        {

                            progressDialog.dismiss();
                            if (availabilityflag == 0) {

                                reqRef = FirebaseDatabase.getInstance().getReference().child("Location").child("North 24 Parganas")
                                        .child("Request").child(time).child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                reqRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for(final DataSnapshot reqchild:dataSnapshot.getChildren())
                                        {
                                            final String qty = reqchild.getValue().toString();

                                            productRef.child(reqchild.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {


                                                    String left = dataSnapshot.child("left").getValue().toString();

                                                    int remaining = Integer.valueOf(left) - Integer.valueOf(qty);
                                                    Log.d("availability", "availabe(1) left: " + left);
                                                    HashMap<String,Object> leftmap = new HashMap<>();
                                                    leftmap.put("left",String.valueOf(remaining));
                                                    productRef.child(reqchild.getKey()).updateChildren(leftmap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if(task.isSuccessful())
                                                            {
                                                                Toast.makeText(PayActivity.this, "remaining changed", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });

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



                                HashMap<String,Object>billmap = new HashMap<>();
                                billmap.put("status","pay due by cash");
                                bill = FirebaseDatabase.getInstance().getReference().child("Location").child("North 24 Parganas").child("bill")
                                        .child(time);
                                bill.updateChildren(billmap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        reqRef = FirebaseDatabase.getInstance().getReference().child("Location").child("North 24 Parganas")
                                                .child("Request").child(time).child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                        reqRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful())
                                                {
                                                    Intent intent = new Intent(PayActivity.this,MainActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            }
                                        });
                                    }
                                });


                            } else {
                                final android.app.AlertDialog alertDialog3 = new android.app.AlertDialog.Builder(
                                        PayActivity.this).create();
                                alertDialog3.setTitle("Cart message");
                                alertDialog3.setMessage(outofstck + " is out of stock");
                                alertDialog3.setIcon(R.drawable.playstore);
                                alertDialog3.setButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        alertDialog3.dismiss();
                                    }
                                });

                                // Showing Alert Message
                                alertDialog3.show();
                                outofstck ="";
                                availabilityflag = 0;
                            }
                        }
                    }
                }, 3000);


            }
        });

        credit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setTitle("gathering stock info");
                progressDialog.setMessage("Please wait...");
                progressDialog.show();
                reqRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(final DataSnapshot reqchild:dataSnapshot.getChildren())
                        {
                            final String qty = reqchild.getValue().toString();

                            productRef.child(reqchild.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {


                                    String left = dataSnapshot.child("left").getValue().toString();

                                    Log.d("availability", "availabe(1) left: " + left);

                                    if (Integer.valueOf(left) < Integer.valueOf(qty)) {
                                        Log.d("availability", "Quantity inside if(2) " + qty);
                                        availabilityflag = 1;
                                        Log.d("availability", "inside (3)" + availabilityflag);
                                        // Toast.makeText(getContext(), ""+availabilityflag, Toast.LENGTH_SHORT).show();
                                        outofstck = outofstck + reqchild.getKey();
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

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        {

                            progressDialog.dismiss();
                            if (availabilityflag == 0) {
                                proceedtobilling(amount);

                            } else {
                                final android.app.AlertDialog alertDialog3 = new android.app.AlertDialog.Builder(
                                        PayActivity.this).create();
                                alertDialog3.setTitle("Cart message");
                                alertDialog3.setMessage(outofstck + " is out of stock");
                                alertDialog3.setIcon(R.drawable.playstore);
                                alertDialog3.setButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        alertDialog3.dismiss();
                                    }
                                });

                                // Showing Alert Message
                                alertDialog3.show();
                                outofstck ="";
                                availabilityflag = 0;
                            }
                        }
                    }
                }, 3000);


            }
        });

        netbanck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setTitle("gathering stock info");
                progressDialog.setMessage("Please wait...");
                progressDialog.show();
                reqRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(final DataSnapshot reqchild:dataSnapshot.getChildren())
                        {
                            final String qty = reqchild.getValue().toString();

                            productRef.child(reqchild.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {


                                    String left = dataSnapshot.child("left").getValue().toString();

                                    Log.d("availability", "availabe(1) left: " + left);

                                    if (Integer.valueOf(left) < Integer.valueOf(qty)) {
                                        Log.d("availability", "Quantity inside if(2) " + qty);
                                        availabilityflag = 1;
                                        Log.d("availability", "inside (3)" + availabilityflag);
                                        // Toast.makeText(getContext(), ""+availabilityflag, Toast.LENGTH_SHORT).show();
                                        outofstck = outofstck + reqchild.getKey();
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

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        {

                            progressDialog.dismiss();
                            if (availabilityflag == 0) {
                                proceedtobilling(amount);

                            } else {
                                final android.app.AlertDialog alertDialog3 = new android.app.AlertDialog.Builder(
                                        PayActivity.this).create();
                                alertDialog3.setTitle("Cart message");
                                alertDialog3.setMessage(outofstck + " is out of stock");
                                alertDialog3.setIcon(R.drawable.playstore);
                                alertDialog3.setButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        alertDialog3.dismiss();
                                    }
                                });

                                // Showing Alert Message
                                alertDialog3.show();
                                outofstck ="";
                                availabilityflag = 0;
                            }
                        }
                    }
                }, 3000);



            }
        });

        wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setTitle("gathering stock info");
                progressDialog.setMessage("Please wait...");
                progressDialog.show();
                reqRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(final DataSnapshot reqchild:dataSnapshot.getChildren())
                        {
                            final String qty = reqchild.getValue().toString();

                            productRef.child(reqchild.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {


                                    String left = dataSnapshot.child("left").getValue().toString();

                                    Log.d("availability", "availabe(1) left: " + left);

                                    if (Integer.valueOf(left) < Integer.valueOf(qty))
                                    {
                                        Log.d("availability", "Quantity inside if(2) " + qty);
                                        availabilityflag = 1;
                                        Log.d("availability", "inside (3)" + availabilityflag);
                                        // Toast.makeText(getContext(), ""+availabilityflag, Toast.LENGTH_SHORT).show();
                                        outofstck = outofstck + reqchild.getKey();

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

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        {

                            progressDialog.dismiss();
                            if (availabilityflag == 0) {

                                reqRef = FirebaseDatabase.getInstance().getReference().child("Location").child("North 24 Parganas")
                                        .child("Request").child(time).child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                reqRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for(final DataSnapshot reqchild:dataSnapshot.getChildren())
                                        {
                                            final String qty = reqchild.getValue().toString();

                                            productRef.child(reqchild.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {


                                                    String left = dataSnapshot.child("left").getValue().toString();

                                                    int remaining = Integer.valueOf(left) - Integer.valueOf(qty);
                                                    Log.d("availability", "availabe(1) left: " + left);
                                                    HashMap<String,Object> leftmap = new HashMap<>();
                                                    leftmap.put("left",String.valueOf(remaining));
                                                    productRef.child(reqchild.getKey()).updateChildren(leftmap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if(task.isSuccessful())
                                                            {
                                                                Toast.makeText(PayActivity.this, "remaining changed", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });

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



                                HashMap<String,Object>billmap = new HashMap<>();
                                billmap.put("status","paid by wallet");
                                bill = FirebaseDatabase.getInstance().getReference().child("Location").child("North 24 Parganas").child("bill")
                                        .child(time);
                                bill.updateChildren(billmap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        reqRef = FirebaseDatabase.getInstance().getReference().child("Location").child("North 24 Parganas")
                                                .child("Request").child(time).child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                        reqRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful())
                                                {
                                                    walletRef=FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                            .child("wallet");
                                                    walletRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            String walletbal = dataSnapshot.getValue().toString();
                                                            if(Integer.valueOf(sAmount)<=Integer.valueOf(walletbal)) {
                                                                String updatebal = String.valueOf(Integer.valueOf(walletbal) - Integer.valueOf(sAmount));
                                                                HashMap<String,Object> updatebalmap = new HashMap<>();
                                                                updatebalmap.put("wallet",updatebal);
                                                                DatabaseReference walletupdate = FirebaseDatabase.getInstance().getReference()
                                                                        .child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                                                walletupdate.updateChildren(updatebalmap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        Intent intent = new Intent(PayActivity.this,MainActivity.class);
                                                                        startActivity(intent);
                                                                        finish();
                                                                    }
                                                                });

                                                            }
                                                            else{
                                                                Toast.makeText(PayActivity.this, "Dont have sufficiant bal", Toast.LENGTH_SHORT).show();
                                                            }
                                                            }

                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {

                                                        }
                                                    });


                                                }
                                            }
                                        });
                                    }
                                });


                            } else {
                                final android.app.AlertDialog alertDialog3 = new android.app.AlertDialog.Builder(
                                        PayActivity.this).create();
                                alertDialog3.setTitle("Cart message");
                                alertDialog3.setMessage(outofstck + " is out of stock");
                                alertDialog3.setIcon(R.drawable.playstore);
                                alertDialog3.setButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        alertDialog3.dismiss();
                                    }
                                });

                                // Showing Alert Message
                                alertDialog3.show();
                                outofstck ="";
                                availabilityflag = 0;
                            }
                        }
                    }
                }, 3000);


            }
        });*/

    }


    @Override
    public void onPaymentSuccess(String s) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("PaymentID");
        builder.setMessage(s);
        builder.show();

        HashMap<String,Object>billmap = new HashMap<>();
        billmap.put("status","paid");
        billmap.put("Payment ID",s);
        bill = FirebaseDatabase.getInstance().getReference().child("Location").child("North 24 Parganas").child("bill")
                .child(time);
        bill.updateChildren(billmap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    reqRef = FirebaseDatabase.getInstance().getReference().child("Location").child("North 24 Parganas")
                            .child("Request").child(time).child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    reqRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {

                                Intent intent = new Intent(PayActivity.this,MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });

                }
            }
        });

    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
        reqRef = FirebaseDatabase.getInstance().getReference().child("Location").child("North 24 Parganas")
                .child("Request").child(time).child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        reqRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(final DataSnapshot reqchild:dataSnapshot.getChildren())
                {
                    final String qty = reqchild.getValue().toString();

                    productRef.child(reqchild.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {


                            String left = dataSnapshot.child("left").getValue().toString();

                            int remaining = Integer.valueOf(left) + Integer.valueOf(qty);
                            Log.d("availability", "availabe(1) left: " + left);
                            HashMap<String,Object> leftmap = new HashMap<>();
                            leftmap.put("left",String.valueOf(remaining));
                            productRef.child(reqchild.getKey()).updateChildren(leftmap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        Toast.makeText(PayActivity.this, "remaining changed", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

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




    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        bill = FirebaseDatabase.getInstance().getReference().child("Location").child("North 24 Parganas").child("bill")
                .child(time);
        reqRef = FirebaseDatabase.getInstance().getReference().child("Location").child("North 24 Parganas")
                .child("Request").child(time).child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        bill.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){

                    reqRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                Intent intent = new Intent(PayActivity.this,BillingActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });

                }
            }
        });

    }

    void proceedtobilling(int amount)
    {
        reqRef = FirebaseDatabase.getInstance().getReference().child("Location").child("North 24 Parganas")
                .child("Request").child(time).child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        reqRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(final DataSnapshot reqchild:dataSnapshot.getChildren())
                {
                    final String qty = reqchild.getValue().toString();

                    productRef.child(reqchild.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {


                            String left = dataSnapshot.child("left").getValue().toString();

                            int remaining = Integer.valueOf(left) - Integer.valueOf(qty);
                            Log.d("availability", "availabe(1) left: " + left);
                            HashMap<String,Object> leftmap = new HashMap<>();
                            leftmap.put("left",String.valueOf(remaining));
                            productRef.child(reqchild.getKey()).updateChildren(leftmap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                   if(task.isSuccessful())
                                   {
                                       Toast.makeText(PayActivity.this, "remaining changed", Toast.LENGTH_SHORT).show();
                                   }
                                }
                            });

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


        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_O5zEt3cSMpoXaW");
        checkout.setImage(R.drawable.rzp_logo);

        JSONObject object = new JSONObject();
        try{
            object.put("name","My Everyday Essentials");
            object.put("description","Payment");
            object.put("theme.color","#0093DD");
            object.put("currency","INR");
            object.put("amount",amount);
            object.put("prefill.contact","7462097579");
            object.put("prefill.email","shazzshayan04@gmail.com");
            checkout.open(PayActivity.this,object);

        }
        catch (JSONException e)
        {
            Log.d("rzperror", "json_rzp_exception: ");
        }


    }
}
