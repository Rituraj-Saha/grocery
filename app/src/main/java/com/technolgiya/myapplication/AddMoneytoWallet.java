package com.technolgiya.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

import java.util.HashMap;

public class AddMoneytoWallet extends AppCompatActivity implements PaymentResultListener {
    private TextView wallet_amt_txt;
    private TextView wallet_added_amt_txt;
    private Button wallet_added_rs_250_btn;
    private Button wallet_added_rs_500_btn;
    private Button wallet_added_rs_1000_btn;
    private Button wallet_pay_online_btn;
    private DatabaseReference wallet,walltadd;
    private String selectedAmountToadd="0";
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_moneyto_wallet);

        wallet_amt_txt = findViewById(R.id.wallet_amt_txt);
        wallet_added_amt_txt = findViewById(R.id.wallet_added_amt_txt);
        wallet_added_rs_250_btn=findViewById(R.id.wallet_added_rs_250_btn);
        wallet_added_rs_500_btn= findViewById(R.id.wallet_added_rs_500_btn);
        wallet_added_rs_1000_btn=findViewById(R.id.wallet_added_rs_1000_btn);
        wallet_pay_online_btn=findViewById(R.id.wallet_pay_online_btn);
        back = findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        wallet = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("wallet");
        wallet.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String bal = dataSnapshot.getValue().toString();

                String balToShow = bal+".00";
                wallet_amt_txt.setText(balToShow);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        wallet_added_rs_250_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                wallet_added_rs_250_btn.setBackgroundColor(Color.parseColor("#3892cc"));
                wallet_added_rs_250_btn.setTextColor(Color.parseColor("#ffffff"));

                wallet_added_rs_500_btn.setTextColor(Color.parseColor("#000000"));
                wallet_added_rs_500_btn.setBackgroundColor(Color.parseColor("#F0F0F0"));
                wallet_added_rs_1000_btn.setTextColor(Color.parseColor("#000000"));
                wallet_added_rs_1000_btn.setBackgroundColor(Color.parseColor("#F0F0F0"));

                selectedAmountToadd ="250";
                wallet_added_amt_txt.setText("\u20B9"+selectedAmountToadd);
            }
        });
        wallet_added_rs_500_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                wallet_added_rs_500_btn.setBackgroundColor(Color.parseColor("#3892cc"));
                wallet_added_rs_500_btn.setTextColor(Color.parseColor("#ffffff"));

                wallet_added_rs_250_btn.setTextColor(Color.parseColor("#000000"));
                wallet_added_rs_250_btn.setBackgroundColor(Color.parseColor("#F0F0F0"));
                wallet_added_rs_1000_btn.setTextColor(Color.parseColor("#000000"));
                wallet_added_rs_1000_btn.setBackgroundColor(Color.parseColor("#F0F0F0"));
                selectedAmountToadd ="500";
                wallet_added_amt_txt.setText("\u20B9"+selectedAmountToadd);
            }
        });
        wallet_added_rs_1000_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                wallet_added_rs_1000_btn.setBackgroundColor(Color.parseColor("#3892cc"));
                wallet_added_rs_1000_btn.setTextColor(Color.parseColor("#ffffff"));

                wallet_added_rs_500_btn.setTextColor(Color.parseColor("#000000"));
                wallet_added_rs_500_btn.setBackgroundColor(Color.parseColor("#F0F0F0"));
                wallet_added_rs_250_btn.setTextColor(Color.parseColor("#000000"));
                wallet_added_rs_250_btn.setBackgroundColor(Color.parseColor("#F0F0F0"));

                selectedAmountToadd="1000";
                wallet_added_amt_txt.setText("\u20B9"+selectedAmountToadd);
            }
        });

        wallet_pay_online_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


               int amount= Math.round(Float.parseFloat(selectedAmountToadd)*100);

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
                    checkout.open(AddMoneytoWallet.this,object);

                }
                catch (JSONException e)
                {
                    Log.d("rzperror", "json_rzp_exception: ");
                }

            }
        });
    }

    @Override
    public void onPaymentSuccess(String s) {

        wallet = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("wallet");
        walltadd =FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        wallet.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String avlBal=dataSnapshot.getValue().toString();
                String newBal = String.valueOf(Integer.valueOf(avlBal)+Integer.valueOf(selectedAmountToadd));
                HashMap<String,Object> walletmap = new HashMap<>();
                walletmap.put("wallet",newBal);
                walltadd.updateChildren(walletmap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful())
                        {
                            Toast.makeText(AddMoneytoWallet.this, "Wallet bal Added", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(this, "Sorry transaction failed", Toast.LENGTH_SHORT).show();
    }
}
