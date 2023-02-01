package com.technolgiya.myapplication;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;
import com.razorpay.Payment;
import com.razorpay.Refund;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class cancelorder extends Fragment {


    public cancelorder() {
        // Required empty public constructor
    }
    private RecyclerView recyclear_cart;
    private DatabaseReference userRef,cartRef,bill,order,productRef;
    private TextView nameTxt,phoneTxt,payAmount;
    //   private Button pay;
    private int amount = 0;
    private Toolbar toolbar;
    private View parent_frame_layout_client;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cancelorder, container, false);


        nameTxt=view.findViewById(R.id.client_name_bill_txt);
        phoneTxt=view.findViewById(R.id.client_phone_bill_txt);
        // payAmount = findViewById(R.id.client_net_pay_txt);
        // pay = findViewById(R.id.client_pay_btn);

        parent_frame_layout_client = getActivity().findViewById(R.id.frame_layout_client);
        toolbar = getActivity().findViewById(R.id.toolbar_layout_client);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setTitle("My Order");


        productRef = FirebaseDatabase.getInstance().getReference().child("Location").child("North 24 Parganas")
                .child("product");
        recyclear_cart = view.findViewById(R.id.recyclear_cart);
        FirebaseRecyclerOptions<order_model> options_cart_detail = null;
        // recyclear_cart.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        recyclear_cart.setLayoutManager(mLayoutManager);


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

                    String phone = dataSnapshot.child("phoneNumber").getValue().toString();
                    final String name = dataSnapshot.child("name").getValue().toString();
                    nameTxt.setText(name);
                    phoneTxt.setText(phone);


                    FirebaseRecyclerOptions<order_model> options_cart_detail=null;

                    bill = FirebaseDatabase.getInstance().getReference().child("Location").child(subAdmin).child("bill");
                    Query query = bill.orderByKey();
                    options_cart_detail =
                            new FirebaseRecyclerOptions.Builder<order_model>()
                                    .setQuery(query, order_model.class)
                                    .build();

                    final FirebaseRecyclerAdapter<order_model, cancelorder.cartList> FirebaseRecyclearAdaptor=
                            new FirebaseRecyclerAdapter<order_model, cancelorder.cartList>(options_cart_detail) {
                                @Override
                                protected void onBindViewHolder(@NonNull final cancelorder.cartList holder, final int position, @NonNull final order_model model) {

                                    bill.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(final DataSnapshot dataSnapshot) {
                                            String uid = model.getUid();

                                            if(!uid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))
                                            {
                                                holder.cart_parent_card.setVisibility(View.GONE);
                                                holder.cart_parent_lin.setVisibility(View.GONE);
                                                holder.parent_lin_one.setVisibility(View.GONE);
                                            }


                                           else
                                            {

                                                    holder.cart_name.setText(model.getName());
                                                    holder.cart_phone.setText(model.getPhoneNumber());
                                                    holder.cart_price.setText(model.getValue());
                                                    holder.cart_address.setText(model.getAddress());
                                                    holder.total.setText(model.getTotal());
                                                    holder.pay_status.setText(model.getStatus());
                                                    holder.btn_delivered.setText("cancel order");
                                                    holder.txt_time_bill.setText(model.getTime());

                                                    holder.btn_delivered.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {

                                                            DatabaseReference cancelorderadjust=FirebaseDatabase.getInstance().getReference()
                                                                    .child("Location").child("North 24 Parganas").child("bill")
                                                                    .child(model.getTime()).child("Product");
                                                            cancelorderadjust.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                                    for(final DataSnapshot productqty:dataSnapshot.getChildren())
                                                                    {
                                                                        final String qty = productqty.getValue().toString();

                                                                        productRef.child(productqty.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                            @Override
                                                                            public void onDataChange(DataSnapshot dataSnapshot) {


                                                                                String left = dataSnapshot.child("left").getValue().toString();

                                                                                int remaining = Integer.valueOf(left) + Integer.valueOf(qty);
                                                                                Log.d("availability", "availabe(1) left: " + left);
                                                                                HashMap<String,Object> leftmap = new HashMap<>();
                                                                                leftmap.put("left",String.valueOf(remaining));
                                                                                productRef.child(productqty.getKey()).updateChildren(leftmap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                    @Override
                                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                                        if(task.isSuccessful())
                                                                                        {
                                                                                            Toast.makeText(getActivity().getApplicationContext(), "remaining changed", Toast.LENGTH_SHORT).show();
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

                                                            if(model.getStatus().equals("paid"))
                                                            {
                                                                Thread thread = new Thread(new Runnable() {

                                                                    @Override
                                                                    public void run() {
                                                                        try  {
                                                                            //Your code goes here
                                                                            String paymentid =dataSnapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Payment ID").getValue().toString();

                                                                            try {
                                                                                RazorpayClient razorpayClient = new RazorpayClient("rzp_test_O5zEt3cSMpoXaW", "a9URRJXookmOxqswyvHuqkba");
                                                                                JSONObject refundRequest = new JSONObject();
                                                                                try {
                                                                                    refundRequest.put("payment_id", paymentid);
                                                                                    Refund refund = razorpayClient.Payments.refund(refundRequest);
                                                                                } catch (JSONException e) {
                                                                                    e.printStackTrace();
                                                                                }



                                                                            } catch (RazorpayException e) {
                                                                                e.printStackTrace();
                                                                                // For full refunds

                                                                            }
                                                                        } catch (Exception e) {
                                                                            e.printStackTrace();
                                                                        }
                                                                    }
                                                                });

                                                                thread.start();
                                                                bill.child(getRef(position).getKey()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if (task.isSuccessful()) {
                                                                            Toast.makeText(getContext(), "order cancelled", Toast.LENGTH_SHORT).show();
                                                                            Intent intent = new Intent(getContext(), MainActivity.class);
                                                                            startActivity(intent);
                                                                            getActivity().finish();
                                                                        }
                                                                    }
                                                                });

                                                            }

                                                            else if(model.getStatus().equals("paid by wallet"))
                                                            {
                                                                DatabaseReference wallet = FirebaseDatabase.getInstance().getReference()
                                                                        .child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("wallet");

                                                                final DatabaseReference walletupdate = FirebaseDatabase.getInstance().getReference()
                                                                        .child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

                                                                wallet.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                                        String currentbal = dataSnapshot.getValue().toString();
                                                                        String update = String.valueOf(Integer.valueOf(currentbal)+Integer.valueOf(model.getTotal()));
                                                                        HashMap<String,Object> walletupdatemap = new HashMap<>();
                                                                        walletupdatemap.put("wallet",update);
                                                                        walletupdate.updateChildren(walletupdatemap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                if(task.isSuccessful())
                                                                                {
                                                                                    Toast.makeText(getActivity().getApplicationContext(), "wallet bal updated", Toast.LENGTH_SHORT).show();
                                                                                    bill.child(getRef(position).getKey()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                        @Override
                                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                                            if (task.isSuccessful()) {
                                                                                                Toast.makeText(getContext(), "order cancelled", Toast.LENGTH_SHORT).show();
                                                                                                Intent intent = new Intent(getContext(), MainActivity.class);
                                                                                                startActivity(intent);
                                                                                                getActivity().finish();
                                                                                            }
                                                                                        }
                                                                                    });


                                                                                }
                                                                            }
                                                                        });
                                                                    }

                                                                    @Override
                                                                    public void onCancelled(DatabaseError databaseError) {

                                                                    }
                                                                });


                                                            }

                                                            else {
                                                                bill.child(getRef(position).getKey()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if (task.isSuccessful()) {
                                                                            Toast.makeText(getContext(), "order cancelled", Toast.LENGTH_SHORT).show();
                                                                            Intent intent = new Intent(getContext(), MainActivity.class);
                                                                            startActivity(intent);
                                                                            getActivity().finish();
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

                                @NonNull
                                @Override
                                public cancelorder.cartList onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.billing_design_prev, parent, false);
                                    cancelorder.cartList viewHolder = new cancelorder.cartList(view);
                                    return viewHolder;

                                }
                            };
                    FirebaseRecyclearAdaptor.startListening();
                    recyclear_cart.setAdapter(FirebaseRecyclearAdaptor);
                    recyclear_cart.getRecycledViewPool().setMaxRecycledViews(0, 0);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public static class cartList extends RecyclerView.ViewHolder{

        LinearLayout cart_parent_lin;
        TextView cart_name;
        TextView cart_price;
        TextView cart_phone;
        CardView cart_parent_card;
        TextView total;
        Button btn_delivered;
        TextView cart_address;
        TextView pay_status;
        TextView txt_time_bill;
        LinearLayout parent_lin_one;
        public cartList(@NonNull View itemView) {
            super(itemView);

            cart_parent_lin = itemView.findViewById(R.id.bill_parent_lin);
            cart_name = itemView.findViewById(R.id.order_name);
            cart_price = itemView.findViewById(R.id.bill_price_txt);
            cart_parent_card = itemView.findViewById(R.id.bill_parent_card);
            cart_phone = itemView.findViewById(R.id.order_phone);
            btn_delivered = itemView.findViewById(R.id.client_delivered_btn);
            total = itemView.findViewById(R.id.bill_qty_txt);
            cart_address=itemView.findViewById(R.id.order_address);
            pay_status = itemView.findViewById(R.id.pay_status);
            txt_time_bill = itemView.findViewById(R.id.txt_time_bill);
            parent_lin_one = itemView.findViewById(R.id.parent_lin_one);
        }
    }
}