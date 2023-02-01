package com.technolgiya.myapplication;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Semaphore;


/**
 * A simple {@link Fragment} subclass.
 */
public class CartFragment extends Fragment {

    private RecyclerView cart_recycler;
    private Toolbar toolbar;
    private DatabaseReference userRef,reqRef,productRef;
    private DatabaseReference cartRef,cart;
    private int add=0;
    private int amount=0;
    private int iteam_amount = 0;

    private int mrpamount =0;
    private int iteam_mrp_amount=0;

    private Button proceedToPay_btn;
    private TextView txt_location_cart;
    private int availabilityflag=0;
    private ProgressDialog progressDialog,pro;


    private int total = 0;
    private int count = 0;
    private int totaldiscount = 0;


    private TextView txt_cart_totalamount_iteam_count;
    private TextView txt_cart_totalsavings;


    private  String outofstck="";



    public CartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        progressDialog = new ProgressDialog(getContext());
        pro = new ProgressDialog(getContext());

        proceedToPay_btn =view.findViewById(R.id.btn_buy_cart);

        proceedToPay_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                proceedToPay(progressDialog);
                v.setEnabled(false);
            }
        });
        txt_location_cart =view.findViewById(R.id.txt_location_cart);

        cart_recycler=view.findViewById(R.id.recyl_cart);
        FirebaseRecyclerOptions<cart_model> options_cart_detail = null;
        cart_recycler.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

        txt_cart_totalamount_iteam_count = view.findViewById(R.id.txt_cart_totalamount_iteam_count);
        txt_cart_totalsavings = view.findViewById(R.id.txt_cart_totalsavings);

        toolbar = getActivity().findViewById(R.id.toolbar_layout_client);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setTitle("My Cart");

        reqRef = FirebaseDatabase.getInstance().getReference().child("Location").child("North 24 Parganas").child("request")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        productRef = FirebaseDatabase.getInstance().getReference().child("Location").child("North 24 Parganas")
                .child("product");

        itemtotalcounting();


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        userRef = FirebaseDatabase.getInstance().getReference().child("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());



        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    final String subAdmin = dataSnapshot.child("subAdmin").getValue().toString();

                    String address= dataSnapshot.child("Address").getValue().toString();

                    txt_location_cart.setText(address);

                    cartRef = FirebaseDatabase.getInstance().getReference().child("Location").child(subAdmin).child("cart")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

                    cartRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(!dataSnapshot.exists())
                            {
                                proceedToPay_btn.setVisibility(View.GONE);
                                AlertDialog alertDialog1 = new AlertDialog.Builder(
                                        getContext()).create();
                                alertDialog1.setTitle("Cart message");
                                alertDialog1.setMessage("Cart is empty");
                                alertDialog1.setIcon(R.drawable.playstore);
                                alertDialog1.setButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                       Intent intent = new Intent(getActivity(),MainActivity.class);
                                       startActivity(intent);
                                       getActivity().finish();
                                    }
                                });

                                // Showing Alert Message
                                alertDialog1.show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    FirebaseRecyclerOptions<cart_model>options_cart_detail=null;

                    options_cart_detail =
                            new FirebaseRecyclerOptions.Builder<cart_model>()
                                    .setQuery(cartRef, cart_model.class)
                                    .build();
                    FirebaseRecyclerAdapter<cart_model,CartFragment.cartList>FirebaseRecyclearAdaptor=
                            new FirebaseRecyclerAdapter<cart_model, cartList>(options_cart_detail) {
                                @Override
                                protected void onBindViewHolder(@NonNull final CartFragment.cartList holder, final int position, @NonNull final cart_model model) {


                                    holder.cart_name.setText(model.getName());
                                    Picasso.get().load(model.getImage()).placeholder(R.drawable.ad).into(holder.cart_pic);
                                    holder.cart_qty.setText(model.getQty());
                                    holder.cart_price.setText(model.getRate());
                                    holder.cart_subhead_txt.setText(model.getQuantity());

                                    holder.text_view_original_cash_amount.setText(model.getMrprate());


                                    holder.txt_discount_percentage.setText(String.valueOf(model.getDiscountpercentage()+"% off"));


                                    cart = FirebaseDatabase.getInstance().getReference().child("Location").child(subAdmin).child("cart")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                    cart.child(holder.cart_name.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if(dataSnapshot.exists())
                                            {
                                                String qty = dataSnapshot.child("qty").getValue().toString();
                                                holder.cart_order_qty.setText(qty);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });

                                    ////qty add

                                    holder.cart_order_plus.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                           /* cart = FirebaseDatabase.getInstance().getReference().child("Location").child(subAdmin).child("cart")
                                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

                                            if(holder.cart_order_qty.getText().toString().equals("ADD"))
                                                add=0;
                                            else
                                                add = Integer.valueOf(holder.cart_order_qty.getText().toString());
                                            holder.cart_order_qty.setText(String.valueOf(++add));
                                            amount = amount+ Integer.valueOf(holder.cart_price.getText().toString());

                                            iteam_amount=0;

                                            mrpamount = mrpamount + Integer.valueOf(holder.text_view_original_cash_amount.getText().toString());

                                            iteam_mrp_amount = 0;

                                            cart.child(holder.cart_name.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    if(dataSnapshot.exists())
                                                    {
                                                        iteam_amount = Integer.valueOf(dataSnapshot.child("amount").getValue().toString());
                                                        iteam_amount =iteam_amount+Integer.valueOf(holder.cart_price.getText().toString());
                                                        Toast.makeText(getActivity(), ""+iteam_amount, Toast.LENGTH_SHORT).show();

                                                        HashMap<String,Object>cartMap=new HashMap<>();
                                                        cartMap.put("name",holder.cart_name.getText().toString());
                                                        cartMap.put("qty",holder.cart_order_qty.getText().toString());
                                                        cartMap.put("rate",holder.cart_price.getText().toString());
                                                        cartMap.put("image",model.getImage());
                                                        cartMap.put("amount",String.valueOf(iteam_amount));
                                                       cartMap.put("quantity", holder.cart_subhead_txt.getText().toString());

                                                        cart.child(holder.cart_name.getText().toString()).updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if(task.isSuccessful())
                                                                {
                                                                    Toast.makeText(getActivity(), "data updated to cart", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });
                                                    }
                                                    else
                                                    {
                                                        HashMap<String,Object>cartMap=new HashMap<>();
                                                        cartMap.put("name",holder.cart_name.getText().toString());
                                                        cartMap.put("qty",holder.cart_order_qty.getText().toString());
                                                        cartMap.put("rate",holder.cart_price.getText().toString());
                                                        cartMap.put("image",model.getImage());
                                                        cartMap.put("amount",holder.cart_price.getText().toString());
                                                        cartMap.put("quantity", holder.cart_subhead_txt.getText().toString());

                                                        cart.child(holder.cart_name.getText().toString()).updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if(task.isSuccessful())
                                                                {
                                                                    Toast.makeText(getActivity(), "data updated to cart", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });*/

                                           pro.setTitle("adding");
                                           pro.setMessage("adding item");
                                           pro.show();
                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {

                                            cart = FirebaseDatabase.getInstance().getReference().child("Location").child(subAdmin).child("cart")
                                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

                                            if(holder.cart_order_qty.getText().toString().equals("ADD"))
                                                add=0;
                                            else
                                                add = Integer.valueOf(holder.cart_order_qty.getText().toString());

                                            holder.cart_order_qty.setText(String.valueOf(++add));
                                            amount = amount+ Integer.valueOf(holder.cart_price.getText().toString());
                                            iteam_amount = 0;


                                            mrpamount = mrpamount+Integer.valueOf(holder.text_view_original_cash_amount.getText().toString());
                                            iteam_mrp_amount = 0;


                                            cart.child(holder.cart_name.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    if(dataSnapshot.exists())
                                                    {
                                                        iteam_amount = Integer.valueOf(dataSnapshot.child("amount").getValue().toString());
                                                        iteam_amount = iteam_amount+Integer.valueOf(holder.cart_price.getText().toString());

                                                        iteam_mrp_amount = Integer.valueOf(dataSnapshot.child("mrpamount").getValue().toString());
                                                        iteam_mrp_amount = iteam_mrp_amount+Integer.valueOf(holder.text_view_original_cash_amount
                                                                .getText().toString());

                                                        Toast.makeText(getActivity(), ""+iteam_amount, Toast.LENGTH_SHORT).show();

                                                        HashMap<String,Object> cartMap=new HashMap<>();
                                                        cartMap.put("name",holder.cart_name.getText().toString());
                                                        cartMap.put("qty",holder.cart_order_qty.getText().toString());
                                                        cartMap.put("rate",holder.cart_price.getText().toString());
                                                        cartMap.put("mrprate",holder.text_view_original_cash_amount.getText().toString());
                                                        cartMap.put("mrpamount",String.valueOf(iteam_mrp_amount));
                                                        cartMap.put("totaldiscount",String.valueOf(iteam_mrp_amount-iteam_amount));
                                                        cartMap.put("image",model.getImage());
                                                        cartMap.put("amount",String.valueOf(iteam_amount));
                                                        cartMap.put("quantity",holder.cart_subhead_txt.getText().toString());
                                                        cartMap.put("discountpercentage",model.getDiscountpercentage());

                                                        cart.child(holder.cart_name.getText().toString()).updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if(task.isSuccessful())
                                                                {
                                                                    Toast.makeText(getActivity(), "data updated to cart", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });
                                                    }
                                                    else
                                                    {
                                                        HashMap<String,Object>cartMap=new HashMap<>();
                                                        cartMap.put("name",holder.cart_name.getText().toString());
                                                        cartMap.put("qty",holder.cart_order_qty.getText().toString());
                                                        cartMap.put("rate",holder.cart_price.getText().toString());
                                                        cartMap.put("image",model.getImage());
                                                        cartMap.put("amount",holder.cart_price.getText().toString());
                                                        cartMap.put("quantity",holder.cart_subhead_txt.getText().toString());
                                                        cartMap.put("mrprate",holder.text_view_original_cash_amount.getText().toString());
                                                        cartMap.put("mrpamount",holder.text_view_original_cash_amount.getText().toString());
                                                        cartMap.put("totaldiscount",String.valueOf(Integer.valueOf(holder.text_view_original_cash_amount.getText().toString()
                                                        )-Integer.valueOf(holder.cart_price.getText().toString())));
                                                        cartMap.put("discountpercentage",model.getDiscountpercentage());

                                                        cart.child(holder.cart_name.getText().toString()).updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if(task.isSuccessful())
                                                                {
                                                                    Toast.makeText(getActivity(), "data updated to cart", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });
                                                    }
                                                    itemtotalcounting();
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }


                                            });
                                            pro.dismiss();
                                                }
                                            }, 2000);
                                        }

                                    });
                                    ///qty minus


                                    holder.cart_order_minus.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                           /* cart = FirebaseDatabase.getInstance().getReference().child("Location").child(subAdmin).child("cart")
                                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

                                            if(holder.cart_order_qty.getText().toString().equals("ADD"))
                                            {
                                                holder.cart_order_qty.setText("0");
                                            }
                                            cart = FirebaseDatabase.getInstance().getReference().child("Location").child(subAdmin).child("cart")
                                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

                                            if (Integer.valueOf(holder.cart_order_qty.getText().toString())==1) {
                                                cart.child(holder.cart_name.getText().toString()).removeValue()
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    Toast.makeText(getActivity(), "Remove from cart", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });
                                            }
                                            if (holder.cart_order_qty.getText().toString().equals("ADD"))
                                                add = 0;
                                            else
                                                add = Integer.valueOf(holder.cart_order_qty.getText().toString());
                                            if (add > 0)
                                                holder.cart_order_qty.setText(String.valueOf(--add));


                                                if (Integer.valueOf(holder.cart_order_qty.getText().toString()) > 0) {
                                                    iteam_amount = 0;
                                                    cart.child(holder.cart_name.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            if (dataSnapshot.exists()) {
                                                                iteam_amount = Integer.valueOf(dataSnapshot.child("amount").getValue().toString());
                                                                iteam_amount = iteam_amount - Integer.valueOf(holder.cart_price.getText().toString());
                                                                Toast.makeText(getActivity(), "" + iteam_amount, Toast.LENGTH_SHORT).show();

                                                                HashMap<String, Object> cartMap = new HashMap<>();
                                                                cartMap.put("name", holder.cart_name.getText().toString());
                                                                cartMap.put("qty", holder.cart_order_qty.getText().toString());
                                                                cartMap.put("rate", holder.cart_price.getText().toString());
                                                                cartMap.put("image", model.getImage());
                                                                cartMap.put("amount", String.valueOf(iteam_amount));
                                                                cartMap.put("quantity", holder.cart_subhead_txt.getText().toString());

                                                                cart.child(holder.cart_name.getText().toString()).updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if (task.isSuccessful()) {
                                                                            Toast.makeText(getActivity(), "data updated to cart", Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    }
                                                                });
                                                            } else {
                                                                HashMap<String, Object> cartMap = new HashMap<>();
                                                                cartMap.put("name", holder.cart_name.getText().toString());
                                                                cartMap.put("qty", holder.cart_order_qty.getText().toString());
                                                                cartMap.put("rate", holder.cart_price.getText().toString());
                                                                cartMap.put("image", model.getImage());
                                                                cartMap.put("amount", holder.cart_price.getText().toString());
                                                                cartMap.put("quantity", holder.cart_subhead_txt.getText().toString());

                                                                cart.child(holder.cart_name.getText().toString()).updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if (task.isSuccessful()) {
                                                                            Toast.makeText(getActivity(), "data updated to cart", Toast.LENGTH_SHORT).show();
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
                                                    */
                                            pro.setTitle("deducting");
                                            pro.setMessage("deducting item");
                                            pro.show();
                                                // snackbar.dismiss();
                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {

                                            if(holder.cart_order_qty.getText().toString().equals("ADD"))
                                            {
                                                holder.cart_order_qty.setText("0");
                                            }
                                            cart = FirebaseDatabase.getInstance().getReference().child("Location").child(subAdmin).child("cart")
                                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

                                            if (Integer.valueOf(holder.cart_order_qty.getText().toString())==1) {
                                                cart.child(holder.cart_name.getText().toString()).removeValue()
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    Toast.makeText(getActivity(), "Remove from cart", Toast.LENGTH_SHORT).show();
                                                                    itemtotalcounting();
                                                                }
                                                            }
                                                        });
                                            }
                                            if (holder.cart_order_qty.getText().toString().equals("ADD"))
                                                add = 0;
                                            else
                                                add = Integer.valueOf(holder.cart_order_qty.getText().toString());
                                            if (add > 0) {
                                                holder.cart_order_qty.setText(String.valueOf(--add));

                                          //  if ( amount>0 ) {
                                                if (Integer.valueOf(holder.cart_order_qty.getText().toString()) > 0) {
                                                    iteam_amount = 0;

                                                    iteam_mrp_amount = 0;

                                                    cart.child(holder.cart_name.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            if (dataSnapshot.exists()) {

                                                                Log.d("cart", "check");
                                                                iteam_amount = Integer.valueOf(dataSnapshot.child("amount").getValue().toString());
                                                                iteam_amount = iteam_amount - Integer.valueOf(holder.cart_price.getText().toString());

                                                                iteam_mrp_amount = Integer.valueOf(dataSnapshot.child("mrpamount").getValue().toString());
                                                                iteam_mrp_amount = iteam_mrp_amount - Integer.valueOf(holder.text_view_original_cash_amount
                                                                        .getText().toString());


                                                                Toast.makeText(getActivity(), "" + iteam_amount, Toast.LENGTH_SHORT).show();

                                                                HashMap<String, Object> cartMap = new HashMap<>();
                                                                cartMap.put("name", holder.cart_name.getText().toString());
                                                                cartMap.put("qty", holder.cart_order_qty.getText().toString());
                                                                cartMap.put("rate", holder.cart_price.getText().toString());
                                                                cartMap.put("image", model.getImage());
                                                                cartMap.put("amount", String.valueOf(iteam_amount));
                                                                cartMap.put("quantity", holder.cart_subhead_txt.getText().toString());
                                                                cartMap.put("mrprate", holder.text_view_original_cash_amount.getText().toString());
                                                                cartMap.put("mrpamount", String.valueOf(iteam_mrp_amount));
                                                                cartMap.put("totaldiscount", String.valueOf(iteam_mrp_amount - iteam_amount));
                                                                cartMap.put("discountpercentage", model.getDiscountpercentage());

                                                                cart.child(holder.cart_name.getText().toString()).updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if (task.isSuccessful()) {
                                                                            Toast.makeText(getActivity(), "data updated to cart", Toast.LENGTH_SHORT).show();
                                                                            itemtotalcounting();
                                                                            pro.dismiss();
                                                                        }
                                                                    }
                                                                });
                                                                itemtotalcounting();
                                                            } else {
                                                                HashMap<String, Object> cartMap = new HashMap<>();
                                                                cartMap.put("name", holder.cart_name.getText().toString());
                                                                cartMap.put("qty", holder.cart_order_qty.getText().toString());
                                                                cartMap.put("rate", holder.cart_price.getText().toString());
                                                                cartMap.put("image", model.getImage());
                                                                cartMap.put("amount", holder.cart_price.getText().toString());
                                                                cartMap.put("quantity", holder.cart_subhead_txt.getText().toString());
                                                                cartMap.put("mrprate", holder.text_view_original_cash_amount.getText().toString());
                                                                cartMap.put("mrpamount", holder.text_view_original_cash_amount.getText().toString());
                                                                cartMap.put("totaldiscount", String.valueOf(Integer.valueOf(holder.text_view_original_cash_amount.getText().toString()) - Integer.valueOf(holder.cart_price
                                                                        .getText().toString())));
                                                                cartMap.put("discountpercentage", model.getDiscountpercentage());

                                                                cart.child(holder.cart_name.getText().toString()).updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if (task.isSuccessful()) {
                                                                            Toast.makeText(getActivity(), "data updated to cart", Toast.LENGTH_SHORT).show();
                                                                            itemtotalcounting();
                                                                            pro.dismiss();
                                                                        }
                                                                    }
                                                                });
                                                            }

                                                            itemtotalcounting();
                                                        }

                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {

                                                        }
                                                    });


                                                }

                                                amount = amount - Integer.valueOf(holder.cart_price.getText().toString());

                                                // snackbar.dismiss();
                                            //}
                                            }

                                                }
                                            }, 2000);

                                        }


                                    });

                                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            holder.getAdapterPosition();
                                            String visit_user_id = getRef(position).getKey();
                                            Toast.makeText(getActivity(), visit_user_id, Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    ///check box

                                    holder.checkbox.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            if(!holder.checkbox.isChecked())
                                            {
                                                cart.child(holder.cart_name.getText().toString()).removeValue()
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    itemtotalcounting();
                                                                    Toast.makeText(getActivity(), "Remove from cart", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });

                                                Toast.makeText(getActivity(), holder.cart_name.getText().toString()+" "+"is unchecked", Toast.LENGTH_SHORT).show();

                                            }
                                        }
                                    });

                                }

                                @NonNull
                                @Override
                                public cartList onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_retrive_design, parent, false);
                                    CartFragment.cartList viewHolder = new CartFragment.cartList(view);
                                    return viewHolder;

                                }
                            };
                    FirebaseRecyclearAdaptor.startListening();
                    cart_recycler.setAdapter(FirebaseRecyclearAdaptor);

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
        TextView cart_qty;
        TextView cart_price;
        TextView cart_order_qty;
        Button cart_order_minus;
        Button cart_order_plus;
        CardView cart_parent_card;
        CheckBox checkbox;
        TextView cart_subhead_txt;
        TextView text_view_original_cash_amount;
        TextView txt_discount_percentage;


        public cartList(@NonNull View itemView) {
            super(itemView);

            cart_parent_lin = itemView.findViewById(R.id.cart_parent_lin);
            cart_pic = itemView.findViewById(R.id.cart_pic_img);
            cart_name = itemView.findViewById(R.id.cart_name_txt);
            cart_qty = itemView.findViewById(R.id.cart_qty_txt);
            cart_price = itemView.findViewById(R.id.cart_price_txt);
            cart_order_qty = itemView.findViewById(R.id.cart_order_qty_txt);
            cart_order_minus = itemView.findViewById(R.id.cart_order_minus_btn);
            cart_order_plus = itemView.findViewById(R.id.cart_order_plus_btn);
            cart_parent_card = itemView.findViewById(R.id.cart_parent_card);
            checkbox = itemView.findViewById(R.id.cart_checkbox);
            cart_subhead_txt=itemView.findViewById(R.id.cart_subhead_txt);
            text_view_original_cash_amount = itemView.findViewById(R.id.text_view_original_cash_amount);
            txt_discount_percentage = itemView.findViewById(R.id.txt_discount_percentage);
        }
    }

     void proceedToPay(ProgressDialog p) {
        p.setTitle("Gathering stock info");
        p.setMessage("Please wait...");
        p.show();
        cart = FirebaseDatabase.getInstance().getReference().child("Location").child("North 24 Parganas").child("cart")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());


        cart.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                    for (final DataSnapshot cartchild : dataSnapshot.getChildren()) {

                        final String qty = cartchild.child("qty").getValue().toString();

                        productRef.child(cartchild.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                if(dataSnapshot.hasChild("left")) {


                                    String left = dataSnapshot.child("left").getValue().toString();

                                    Log.d("availability", "availabe(1) left: " + left);

                                    if (Integer.valueOf(left) < Integer.valueOf(qty)) {
                                        Log.d("availability", "Quantity inside if(2) " + qty);
                                        availabilityflag = 1;
                                        Log.d("availability", "inside (3)" + availabilityflag);
                                        // Toast.makeText(getContext(), ""+availabilityflag, Toast.LENGTH_SHORT).show();
                                        outofstck = outofstck + cartchild.getKey();


                                    }
                                }
                                else {
                                    Log.d("errordebug", ""+dataSnapshot.getKey());
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
                proceedtobilling();

            } else {
                final AlertDialog alertDialog3 = new AlertDialog.Builder(
                        getContext()).create();
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


    void proceedtobilling()
    {
        Intent intent = new Intent(getContext(),BillingActivity.class);
        startActivity(intent);

        Log.d("availability", "proceedtobilling: ");
    }


    void itemtotalcounting()
    {
        DatabaseReference cartcounter = FirebaseDatabase.getInstance().getReference().child("Location").child("North 24 Parganas").child("cart")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        total = 0;
        totaldiscount = 0;
        count = 0;

        cartcounter.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    for (DataSnapshot product : dataSnapshot.getChildren()) {

                        total = total + Integer.valueOf(product.child("amount").getValue().toString());
                        totaldiscount = totaldiscount + Integer.valueOf(product.child("totaldiscount").getValue().toString());
                        count = count + 1;
                        Log.d("cartTotal", "onDataChange: " + product + total);
                    }

                    txt_cart_totalamount_iteam_count.setText("\u20B9" + total + "(" + count + " items" + ")");
                    txt_cart_totalsavings.setText("\u20B9" + totaldiscount + " Savings");
                }
                else{
                    txt_cart_totalamount_iteam_count.setVisibility(View.GONE);
                    txt_cart_totalsavings.setVisibility(View.GONE);
                    Intent intent = new Intent(getContext(),MainActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
