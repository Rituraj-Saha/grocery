package com.technolgiya.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class BillingActivity extends AppCompatActivity {

    private RecyclerView recyclear_cart;
    private DatabaseReference userRef,cartRef,bill,billnoref,adresspinRef,reqRef;
    private TextView nameTxt,phoneTxt;
    private TextView addresstxt;
    private int amount = 0;
    private int mrpamount = 0;
    private int discountamount = 0;

    private String value = "";
    private LinearLayout add_adress;

    private ProgressDialog progressDialog;
    FusedLocationProviderClient fusedLocationProviderClient;
    TextView client_bill_pin;
    private String billno="";
    String pin="";

    private TextView txt_mrp_price_total;

    private TextView txt_total_discount;

    private TextView txt_item_count;

    private TextView txt_total_amount_bill;

    private TextView txt_saving_bill;

    private int itemcount = 0;

    private TextView bill_total_txt;
    private Button bill_btn_checkout;

    private Toolbar billtoolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing);

        nameTxt=findViewById(R.id.client_name_bill_txt);
        phoneTxt=findViewById(R.id.client_phone_bill_txt);
        addresstxt = findViewById(R.id.client_address_bill_txt);
        client_bill_pin=findViewById(R.id.client_bill_pin);

        txt_mrp_price_total =findViewById(R.id.txt_mrp_price_total);

        bill_total_txt = findViewById(R.id.bill_total_txt);

        bill_btn_checkout = findViewById(R.id.bill_btn_checkout);

        txt_item_count = findViewById(R.id.txt_item_count);

        txt_total_discount = findViewById(R.id.txt_total_discount);

        txt_total_amount_bill = findViewById(R.id.txt_total_amount_bill);

        txt_saving_bill = findViewById(R.id.txt_saving_bill);

        billtoolbar = findViewById(R.id.bill_toolbar);

        setSupportActionBar(billtoolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        reqRef = FirebaseDatabase.getInstance().getReference().child("Location").child("North 24 Parganas")
                .child("Request");
        add_adress=findViewById(R.id.add_adress);

        add_adress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(BillingActivity.this);
                ViewGroup viewGroup = findViewById(android.R.id.content);
                final View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.custom_dialog_adress, viewGroup, false);

                final EditText house =dialogView.findViewById(R.id.et_housename);
                final EditText street = dialogView.findViewById(R.id.et_strtname);
                final EditText landmark = dialogView.findViewById(R.id.et_lndmrk);
                final EditText pin = dialogView.findViewById(R.id.et_pin);

                builder.setView(dialogView);
                final AlertDialog alertDialog = builder.create();
                alertDialog.show();

              final Button add = dialogView.findViewById(R.id.btn_add_addresss);
              add.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {


                      if(street.getText().toString().equals(""))
                      {
                          Toast.makeText(BillingActivity.this, "streat can't be empty", Toast.LENGTH_SHORT).show();
                      }
                      else if(pin.getText().toString().equals(""))
                      {
                          Toast.makeText(BillingActivity.this, "pin can't be empty", Toast.LENGTH_SHORT).show();
                      }

                      else {
                          String address = house.getText().toString() + ", " + street.getText().toString() + "\n"
                                  + landmark.getText().toString() + "\n" + pin.getText().toString();

                          addresstxt.setText(address);
                          final String housestr = house.getText().toString();
                          //Toast.makeText(BillingActivity.this, housestr, Toast.LENGTH_SHORT).show();
                          client_bill_pin.setText(pin.getText().toString());
                          alertDialog.dismiss();
                      }

                  }
              });

              Button cancel = dialogView.findViewById(R.id.btn_cancel);
              cancel.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                      alertDialog.dismiss();
                  }
              });
                progressDialog = new ProgressDialog(BillingActivity.this);

               Button lin_add_current_get =dialogView.findViewById(R.id.lin_add_current_get_bill);
               lin_add_current_get.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {



                       fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(BillingActivity.this);
                               if (ActivityCompat.checkSelfPermission(BillingActivity.this
                                       , Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){

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
                                               try {

                                                   Geocoder geocoder = new Geocoder(BillingActivity.this,
                                                           Locale.getDefault());

                                                   List<Address> addresses = geocoder.getFromLocation(
                                                           location.getLatitude(),location.getLongitude(),1
                                                   );

                                                   //locationChecker = textView4.toString();
                                                   CardView crd_5 = dialogView.findViewById(R.id.crd_5);
                                                   final TextView textView5 = dialogView.findViewById(R.id.txt_add_5);
                                                   crd_5.setVisibility(View.VISIBLE);
                                                   textView5.setVisibility(View.VISIBLE);
                                                   textView5.setText(Html.fromHtml(addresses.get(0).getAddressLine(0)));

                                                   final TextView txt_pin_add = dialogView.findViewById(R.id.txt_pin_add);
                                                   CardView crd_pin = dialogView.findViewById(R.id.crd_pin);
                                                  txt_pin_add.setVisibility(View.VISIBLE);
                                                  crd_pin.setVisibility(View.VISIBLE);
                                                  txt_pin_add.setText(Html.fromHtml(addresses.get(0).getPostalCode()));


                                                   add.setOnClickListener(new View.OnClickListener() {
                                                       @Override
                                                       public void onClick(View v) {
                                                           addresstxt.setText(textView5.getText().toString());
                                                           client_bill_pin.setText(txt_pin_add.getText().toString());
                                                           alertDialog.dismiss();
                                                       }
                                                   });


                                               } catch (IOException e) {
                                                   e.printStackTrace();
                                               }
                                           }

                                       }
                                   });


                               }
                               else
                               {
                                   ActivityCompat.requestPermissions(BillingActivity.this
                                           , new String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);

                               }

                   }
               });



            }
        });


        recyclear_cart = findViewById(R.id.recyclear_cart);
        FirebaseRecyclerOptions<cart_model> options_cart_detail = null;
        recyclear_cart.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        userRef = FirebaseDatabase.getInstance().getReference().child("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    final String subAdmin = dataSnapshot.child("subAdmin").getValue().toString();

                    cartRef = FirebaseDatabase.getInstance().getReference().child("Location").child(subAdmin).child("cart")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

                    cartRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists())
                            {
                                for(DataSnapshot childdatasnapshot: dataSnapshot.getChildren())
                                {
                                    itemcount++;
                                    final String Key = childdatasnapshot.getKey();
                                    cartRef.child(Key).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {

                                            if(dataSnapshot.exists()) {
                                                String amt = dataSnapshot.child("amount").getValue().toString();

                                                String mrpTotal = dataSnapshot.child("mrpamount").getValue().toString();

                                                String discountTotal = dataSnapshot.child("totaldiscount").getValue().toString();
                                              //  Log.v("childList", "" + newValue);
                                                //displays the key for the node
                                                amount = amount+Integer.valueOf(amt);

                                                mrpamount = mrpamount +Integer.valueOf(mrpTotal);

                                                discountamount = discountamount+Integer.valueOf(discountTotal);

                                                Log.v("amount", "" + amount);
                                               /* View parentLayout = findViewById(android.R.id.content);
                                                 Snackbar snackbar = Snackbar.make(parentLayout, "", Snackbar.LENGTH_INDEFINITE);
                                                snackbar.setText("Amount Rs: " + amount);
                                                View sbView = snackbar.getView();
                                                sbView.setBackgroundColor(Color.parseColor("#36474f"));
                                                Button button= (Button) snackbar.getView().findViewById(com.google.android.material.R.id.snackbar_action);

                                                button.setPadding(20,40,20,40);
                                                button.setBackgroundColor(Color.parseColor("#e46667"));
                                                snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                                                snackbar.setAction("Procced", new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        //biling...

                                                    }
                                                });
                                                snackbar.show();*/

                                                txt_mrp_price_total.setText("\u20B9"+mrpamount);

                                                bill_total_txt.setText("\u20B9"+amount);

                                                txt_total_discount.setText("- \u20B9"+discountamount);

                                                txt_total_amount_bill.setText("\u20B9"+amount);

                                                txt_saving_bill.setText("you will save "+"\u20B9"+discountamount+" on this order");



                                                bill_btn_checkout.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        adresspinRef=FirebaseDatabase.getInstance().getReference().child("serviceAreapin");
                                                        adresspinRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                                int flag =0;
                                                                for (DataSnapshot pin:dataSnapshot.getChildren())
                                                                {
                                                                    Log.d("pin", "onDataChange: "+pin.getValue().toString());
                                                                    if(client_bill_pin.getText().toString().equals(pin.getValue().toString()))
                                                                    {
                                                                        //  Toast.makeText(BillingActivity.this, pin.getValue().toString(), Toast.LENGTH_SHORT).show();
                                                                        flag=1;
                                                                    }
                                                                }
                                                                if(flag==1)
                                                                {
                                                                    payment();
                                                                }
                                                                else{
                                                                    Toast.makeText(BillingActivity.this, "Sorry we do not serve at this location", Toast.LENGTH_SHORT).show();
                                                                    Intent intent = new Intent(BillingActivity.this,BillingActivity.class);
                                                                    startActivity(intent);
                                                                    finish();
                                                                }
                                                            }

                                                            @Override
                                                            public void onCancelled(DatabaseError databaseError) {

                                                            }
                                                        });

                                                        v.setEnabled(false);
                                                    }
                                                });


                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });

                                }

                                txt_item_count.setText("Price("+String.valueOf(itemcount)+" items)");


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


    }

    @Override
    protected void onStart() {
        super.onStart();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    final String subAdmin = dataSnapshot.child("subAdmin").getValue().toString();

                    String phone = dataSnapshot.child("phoneNumber").getValue().toString();
                    String name = dataSnapshot.child("name").getValue().toString();
                    String address = dataSnapshot.child("Address").getValue().toString();
                    String pin=dataSnapshot.child("locationbutton").getValue().toString();

                    nameTxt.setText(name);
                    phoneTxt.setText(phone);
                    addresstxt.setText(address);
                    client_bill_pin.setText(pin);


                    cartRef = FirebaseDatabase.getInstance().getReference().child("Location").child(subAdmin).child("cart")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

                    FirebaseRecyclerOptions<cart_model>options_cart_detail=null;

                    options_cart_detail =
                            new FirebaseRecyclerOptions.Builder<cart_model>()
                                    .setQuery(cartRef, cart_model.class)
                                    .build();
                    FirebaseRecyclerAdapter<cart_model,BillingActivity.cartList> FirebaseRecyclearAdaptor=
                            new FirebaseRecyclerAdapter<cart_model, BillingActivity.cartList>(options_cart_detail) {
                                @Override
                                protected void onBindViewHolder(@NonNull final BillingActivity.cartList holder, final int position, @NonNull final cart_model model) {


                                    holder.cart_name.setText(model.getName());
                                    Picasso.get().load(model.getImage()).placeholder(R.drawable.ad).into(holder.cart_pic);
                                    holder.cart_price.setText(model.getAmount());
                                    holder.bill_subhead.setText(model.getQuantity());
                                    holder.bill_qty_txt.setText(model.getQty());

                                }

                                @NonNull
                                @Override
                                public BillingActivity.cartList onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.billing_design, parent, false);
                                    BillingActivity.cartList viewHolder = new BillingActivity.cartList(view);
                                    return viewHolder;

                                }
                            };
                    FirebaseRecyclearAdaptor.startListening();
                    recyclear_cart.setAdapter(FirebaseRecyclearAdaptor);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public static class cartList extends RecyclerView.ViewHolder{

        LinearLayout cart_parent_lin;
        ImageView cart_pic;
        TextView cart_name;
        TextView cart_price;
        CardView cart_parent_card;
        TextView bill_subhead;
        TextView bill_qty_txt;

        public cartList(@NonNull View itemView) {
            super(itemView);

            cart_parent_lin = itemView.findViewById(R.id.bill_parent_lin);
            cart_pic = itemView.findViewById(R.id.bill_pic_img);
            cart_name = itemView.findViewById(R.id.bill_name_txt);
            cart_price = itemView.findViewById(R.id.bill_price_txt);
            cart_parent_card = itemView.findViewById(R.id.bill_parent_card);
            bill_subhead=itemView.findViewById(R.id.bill_subhead);
            bill_qty_txt=itemView.findViewById(R.id.bill_qty_txt);

        }
    }

    void payment()
    {
        amount =0;
        userRef = FirebaseDatabase.getInstance().getReference().child("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    final String subAdmin = dataSnapshot.child("subAdmin").getValue().toString();
                    final String phoneNumber=dataSnapshot.child("phoneNumber").getValue().toString();
                    final String name = dataSnapshot.child("name").getValue().toString();
                    final String address = addresstxt.getText().toString();
                    final String pin = client_bill_pin.getText().toString();

                    cartRef = FirebaseDatabase.getInstance().getReference().child("Location").child(subAdmin).child("cart")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

                    cartRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists())
                            {
                                for(DataSnapshot childdatasnapshot: dataSnapshot.getChildren())
                                {
                                    final String Key = childdatasnapshot.getKey();
                                    cartRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {

                                            if(dataSnapshot.exists()) {
                                                String amt = dataSnapshot.child(Key).child("amount").getValue().toString();
                                                final String qty = dataSnapshot.child(Key).child("qty").getValue().toString();
                                                //  Log.v("childList", "" + newValue);
                                                //displays the key for the node

                                                value = value+" "+Key.toString()+" "+"qyantity"+" "+qty+" "+"Rs."+" "+amt+" "+",";
                                                Date time =  Calendar.getInstance().getTime();
                                                final String timeStr = time.toString();
                                                amount = amount+Integer.valueOf(amt);
                                                final HashMap<String,Object>billmap = new HashMap<>();
                                                billmap.put(Key,amt);
                                                billmap.put("phoneNumber",phoneNumber);
                                                billmap.put("name",name);
                                                billmap.put("total",String.valueOf(amount));
                                                billmap.put("Value",value);
                                                billmap.put ("address",address);
                                                billmap.put("status","not paid");
                                                billmap.put("billcreatedfrom","");
                                                billmap.put("time",timeStr);
                                                billmap.put("Uid",FirebaseAuth.getInstance().getCurrentUser().getUid());
                                                billmap.put("pin",pin);
                                                billmap.put(Key+"qty",qty);
                                                bill =FirebaseDatabase.getInstance().getReference().child("Location").child(subAdmin).child("bill")
                                                        .child(timeStr);
                                                DatabaseReference billProduct =FirebaseDatabase.getInstance().getReference().child("Location").child(subAdmin).child("bill")
                                                        .child(timeStr).child("Product");

                                                HashMap<String,Object>billproductmap = new HashMap<>();
                                                billproductmap.put(Key,qty);
                                                billProduct.updateChildren(billproductmap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful())
                                                        {
                                                            reqRef = FirebaseDatabase.getInstance().getReference().child("Location").child("North 24 Parganas")
                                                                    .child("Request").child(timeStr).child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                                            HashMap<String,Object> reqMap = new HashMap<>();
                                                            reqMap.put(Key,qty);

                                                            reqRef.updateChildren(reqMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if(task.isSuccessful())
                                                                    {
                                                                        bill.updateChildren(billmap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                cartRef = FirebaseDatabase.getInstance().getReference().child("Location").child("North 24 Parganas").child("cart")
                                                                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                                                                cartRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                    @Override
                                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                                        Intent intent = new Intent(BillingActivity.this,PayActivity.class);
                                                                                        intent.putExtra("payamount",String.valueOf(amount));
                                                                                        intent.putExtra("time",timeStr);

                                                                                        startActivity(intent);
                                                                                        finish();
                                                                                    }
                                                                                });

                                                                            }
                                                                        });
                                                                    }
                                                                }
                                                            });


                                                        }
                                                    }
                                                });




                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });

                                }
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



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
               // onBackPressed();

                Intent intent = new Intent(BillingActivity.this,MainActivity.class);
                startActivity(intent);

               /* CartFragment cartobj = new CartFragment();
                FragmentTransaction fragmentTransaction_cart = MainActivity.class.getSupportFragmentManager().beginTransaction();
                fragmentTransaction_cart.replace(parent_frame_layout_client.getId(),cartobj);
                fragmentTransaction_cart.addToBackStack(null);
                fragmentTransaction_cart.commit();*/
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
