package com.technolgiya.myapplication;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
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

import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {


    public SearchFragment() {
        // Required empty public constructor
    }


    private EditText searchET;
    private String str = "";
    private RecyclerView recyclear_search;
    private DatabaseReference product,userRef,cart;
    private int add =0;
    private int amount =0;
    private int iteam_amount =0;
    private FrameLayout parent_frame_layout_client;


    private int mrpamount = 0;
    private int iteam_mrp_amount = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_search, container, false);

        parent_frame_layout_client = getActivity().findViewById(R.id.frame_layout_client);
        userRef=FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        FirebaseRecyclerOptions<fresh_model> options = null;

        searchET = getActivity().findViewById(R.id.et_search);
        recyclear_search = view.findViewById(R.id.recyclear_search);
        recyclear_search.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));




        searchET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charseq, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charseq, int start, int before, int count) {
                if (searchET.getText().toString().equals("")) {
                  //  Toast.makeText(getActivity().getApplicationContext(), "please write name to search", Toast.LENGTH_SHORT).show();

                } else {
                    str = charseq.toString();
                    onStart();
                }


            }


            @Override
            public void afterTextChanged(Editable charseq) {

            }
        });


        return view;
    }

    @Override
    public void onStart() {
        userRef = FirebaseDatabase.getInstance().getReference().child("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                final String subAdmin = dataSnapshot.child("subAdmin").getValue().toString();
                product = FirebaseDatabase.getInstance().getReference().child("Location").child(subAdmin).child("product");


                FirebaseRecyclerOptions<iteam_model> options = null;
                if (str.equals("")) {
                    options =
                            new FirebaseRecyclerOptions.Builder<iteam_model>()
                                    .setQuery(product, iteam_model.class)
                                    .build();
                }
                else
                {
                    options =
                            new FirebaseRecyclerOptions.Builder<iteam_model>()
                                    .setQuery(product.orderByChild("name")
                                                    .startAt(str)
                                                    .endAt(str + "\uf8ff")
                                            , iteam_model.class)
                                    .build();
                }



                final FirebaseRecyclerAdapter<iteam_model, SearchFragment.iteam_list> firebaseRecyclerAdapter =
                        new FirebaseRecyclerAdapter<iteam_model, SearchFragment.iteam_list>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final  SearchFragment.iteam_list holder, final int position, @NonNull final iteam_model model)
                    {


                        holder.iteam_name.setText(model.getName());
                        Picasso.get().load(model.getImage()).placeholder(R.drawable.ad).into(holder.iteam_pic);
                        holder.iteam_qty.setText(model.getQuantity());
                        holder.iteam_price.setText(model.getRate());
                        holder.text_view_original_cash_amount.setText(model.getMrp());


                        holder.txt_discount_percentage.setText(model.getDiscountpercentage()+"% off");


                        cart = FirebaseDatabase.getInstance().getReference().child("Location").child(subAdmin).child("cart")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                        cart.child(holder.iteam_name.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists())
                                {
                                    String qty = dataSnapshot.child("qty").getValue().toString();
                                    holder.iteam_order_qty.setText(qty);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });



                        holder.iteam_order_plus.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                holder.crd_itm_add_btn_crd.setVisibility(View.GONE);
                                holder.crd_itm_add_txt.setVisibility(View.VISIBLE);
                                holder.crd_itm_minus_crd.setVisibility(View.VISIBLE);
                                holder.iteam_order_plus.setBackgroundColor(Color.parseColor("#3892cc"));
                                holder.iteam_order_plus.setTextColor(Color.parseColor("#ffffff"));

                                cart = FirebaseDatabase.getInstance().getReference().child("Location").child(subAdmin).child("cart")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

                                if(holder.iteam_order_qty.getText().toString().equals("ADD"))
                                    add=0;
                                else
                                    add = Integer.valueOf(holder.iteam_order_qty.getText().toString());
                                holder.iteam_order_qty.setText(String.valueOf(++add));

                                amount = amount+ Integer.valueOf(holder.iteam_price.getText().toString());
                                mrpamount = mrpamount+Integer.valueOf(holder.text_view_original_cash_amount.getText().toString());

                                iteam_amount=0;
                                iteam_mrp_amount = 0;

                                cart.child(holder.iteam_name.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.exists())
                                        {
                                            iteam_amount = Integer.valueOf(dataSnapshot.child("amount").getValue().toString());
                                            iteam_amount =iteam_amount+Integer.valueOf(holder.iteam_price.getText().toString());

                                            iteam_mrp_amount = Integer.valueOf(dataSnapshot.child("mrpamount").getValue().toString());
                                            iteam_mrp_amount = iteam_mrp_amount+Integer.valueOf(holder.text_view_original_cash_amount
                                                    .getText().toString());


                                            Toast.makeText(getActivity(), ""+iteam_amount, Toast.LENGTH_SHORT).show();

                                            HashMap<String,Object>cartMap=new HashMap<>();
                                            cartMap.put("name",holder.iteam_name.getText().toString());
                                            cartMap.put("qty",holder.iteam_order_qty.getText().toString());
                                            cartMap.put("rate",holder.iteam_price.getText().toString());
                                            cartMap.put("image",model.getImage());
                                            cartMap.put("amount",String.valueOf(iteam_amount));
                                            cartMap.put("quantity",holder.iteam_qty.getText().toString());
                                            cartMap.put("mrprate",holder.text_view_original_cash_amount.getText().toString());
                                            cartMap.put("mrpamount",String.valueOf(iteam_mrp_amount));
                                            cartMap.put("totaldiscount",String.valueOf(iteam_mrp_amount-iteam_amount));
                                            cartMap.put("discountpercentage",model.getDiscountpercentage());

                                            cart.child(holder.iteam_name.getText().toString()).updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
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
                                            cartMap.put("name",holder.iteam_name.getText().toString());
                                            cartMap.put("qty",holder.iteam_order_qty.getText().toString());
                                            cartMap.put("rate",holder.iteam_price.getText().toString());
                                            cartMap.put("image",model.getImage());
                                            cartMap.put("amount",holder.iteam_price.getText().toString());
                                            cartMap.put("quantity",holder.iteam_qty.getText().toString());
                                            cartMap.put("mrprate",holder.text_view_original_cash_amount.getText().toString());
                                            cartMap.put("mrpamount",holder.text_view_original_cash_amount.getText().toString());
                                            cartMap.put("totaldiscount",String.valueOf(Integer.valueOf(holder.text_view_original_cash_amount.getText().toString())-Integer.valueOf(holder.iteam_price.getText().toString())));
                                            cartMap.put("discountpercentage",model.getDiscountpercentage());

                                            cart.child(holder.iteam_name.getText().toString()).updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
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
                                });



                                Snackbar snackbar = Snackbar.make(v, "", Snackbar.LENGTH_LONG);
                                snackbar.setText("Amount Rs:: " + amount);
                                snackbar.setAction("Procced", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        //biling....
                                        CartFragment cartobj = new CartFragment();
                                        FragmentTransaction fragmentTransaction_cart = getActivity().getSupportFragmentManager().beginTransaction();
                                        fragmentTransaction_cart.replace(parent_frame_layout_client.getId(),cartobj);
                                        fragmentTransaction_cart.addToBackStack(null);
                                        fragmentTransaction_cart.commit();
                                    }
                                });
                                snackbar.show();





                            }
                        });

                        holder.btn_add_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                holder.crd_itm_add_btn_crd.setVisibility(View.GONE);
                                holder.crd_itm_add_txt.setVisibility(View.VISIBLE);
                                holder.crd_itm_minus_crd.setVisibility(View.VISIBLE);
                                holder.iteam_order_plus.setBackgroundColor(Color.parseColor("#3892cc"));
                                holder.iteam_order_plus.setTextColor(Color.parseColor("#ffffff"));

                                cart = FirebaseDatabase.getInstance().getReference().child("Location").child(subAdmin).child("cart")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

                                if(holder.iteam_order_qty.getText().toString().equals("ADD"))
                                    add=0;
                                else
                                    add = Integer.valueOf(holder.iteam_order_qty.getText().toString());
                                holder.iteam_order_qty.setText(String.valueOf(++add));
                                amount = amount+ Integer.valueOf(holder.iteam_price.getText().toString());
                                mrpamount = mrpamount + Integer.valueOf(holder.text_view_original_cash_amount.getText().toString());

                                iteam_amount=0;
                                iteam_mrp_amount = 0;

                                cart.child(holder.iteam_name.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.exists())
                                        {
                                            iteam_amount = Integer.valueOf(dataSnapshot.child("amount").getValue().toString());
                                            iteam_amount =iteam_amount+Integer.valueOf(holder.iteam_price.getText().toString());

                                            iteam_mrp_amount = Integer.valueOf(dataSnapshot.child("mrpamount").getValue().toString());
                                            iteam_mrp_amount = iteam_mrp_amount+Integer.valueOf(holder.text_view_original_cash_amount
                                                    .getText().toString());


                                            Toast.makeText(getActivity(), ""+iteam_amount, Toast.LENGTH_SHORT).show();

                                            HashMap<String,Object>cartMap=new HashMap<>();
                                            cartMap.put("name",holder.iteam_name.getText().toString());
                                            cartMap.put("qty",holder.iteam_order_qty.getText().toString());
                                            cartMap.put("rate",holder.iteam_price.getText().toString());
                                            cartMap.put("image",model.getImage());
                                            cartMap.put("amount",String.valueOf(iteam_amount));
                                            cartMap.put("quantity",holder.iteam_qty.getText().toString());
                                            cartMap.put("mrprate",holder.text_view_original_cash_amount.getText().toString());
                                            cartMap.put("mrpamount",String.valueOf(iteam_mrp_amount));
                                            cartMap.put("totaldiscount",String.valueOf(iteam_mrp_amount-iteam_amount));
                                            cartMap.put("discountpercentage",model.getDiscountpercentage());


                                            cart.child(holder.iteam_name.getText().toString()).updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
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
                                            cartMap.put("name",holder.iteam_name.getText().toString());
                                            cartMap.put("qty",holder.iteam_order_qty.getText().toString());
                                            cartMap.put("rate",holder.iteam_price.getText().toString());
                                            cartMap.put("image",model.getImage());
                                            cartMap.put("amount",holder.iteam_price.getText().toString());
                                            cartMap.put("quantity",holder.iteam_qty.getText().toString());
                                            cartMap.put("mrprate",holder.text_view_original_cash_amount.getText().toString());
                                            cartMap.put("mrpamount",holder.text_view_original_cash_amount.getText().toString());
                                            cartMap.put("totaldiscount",String.valueOf(Integer.valueOf(holder.text_view_original_cash_amount.getText().toString())-Integer.valueOf(holder.iteam_price.getText().toString())));
                                            cartMap.put("discountpercentage",model.getDiscountpercentage());


                                            cart.child(holder.iteam_name.getText().toString()).updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
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
                                });



                                Snackbar snackbar = Snackbar.make(v, "", Snackbar.LENGTH_LONG);
                                snackbar.setText("Amount Rs:: " + amount);
                                snackbar.setAction("Procced", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        //biling....
                                        CartFragment cartobj = new CartFragment();
                                        FragmentTransaction fragmentTransaction_cart = getActivity().getSupportFragmentManager().beginTransaction();
                                        fragmentTransaction_cart.replace(parent_frame_layout_client.getId(),cartobj);
                                        fragmentTransaction_cart.addToBackStack(null);
                                        fragmentTransaction_cart.commit();
                                    }
                                });
                                snackbar.show();





                            }
                        });





                        holder.iteam_order_minus.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(holder.iteam_order_qty.getText().toString().equals("ADD"))
                                {
                                    holder.iteam_order_qty.setText("0");
                                }
                                cart = FirebaseDatabase.getInstance().getReference().child("Location").child(subAdmin).child("cart")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

                                if (Integer.valueOf(holder.iteam_order_qty.getText().toString())==1) {
                                    cart.child(holder.iteam_name.getText().toString()).removeValue()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(getActivity(), "Remove from cart", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                }
                                if (holder.iteam_order_qty.getText().toString().equals("ADD"))
                                    add = 0;
                                else
                                    add = Integer.valueOf(holder.iteam_order_qty.getText().toString());
                                if (add > 0)
                                    holder.iteam_order_qty.setText(String.valueOf(--add));


                                if ( amount>0 ) {
                                    if (Integer.valueOf(holder.iteam_order_qty.getText().toString()) > 0) {
                                        iteam_amount = 0;

                                        iteam_mrp_amount=0;
                                        cart.child(holder.iteam_name.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                if (dataSnapshot.exists()) {
                                                    iteam_amount = Integer.valueOf(dataSnapshot.child("amount").getValue().toString());
                                                    iteam_amount = iteam_amount - Integer.valueOf(holder.iteam_price.getText().toString());
                                                    Toast.makeText(getActivity(), "" + iteam_amount, Toast.LENGTH_SHORT).show();

                                                    iteam_mrp_amount = Integer.valueOf(dataSnapshot.child("mrpamount").getValue().toString());
                                                    iteam_mrp_amount = iteam_mrp_amount-Integer.valueOf(holder.text_view_original_cash_amount
                                                            .getText().toString());



                                                    HashMap<String, Object> cartMap = new HashMap<>();
                                                    cartMap.put("name", holder.iteam_name.getText().toString());
                                                    cartMap.put("qty", holder.iteam_order_qty.getText().toString());
                                                    cartMap.put("rate", holder.iteam_price.getText().toString());
                                                    cartMap.put("image", model.getImage());
                                                    cartMap.put("amount", String.valueOf(iteam_amount));
                                                    cartMap.put("quantity",holder.iteam_qty.getText().toString());
                                                    cartMap.put("mrprate",holder.text_view_original_cash_amount.getText().toString());
                                                    cartMap.put("mrpamount",String.valueOf(iteam_mrp_amount));
                                                    cartMap.put("totaldiscount",String.valueOf(iteam_mrp_amount-iteam_amount));
                                                    cartMap.put("discountpercentage",model.getDiscountpercentage());

                                                    cart.child(holder.iteam_name.getText().toString()).updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                Toast.makeText(getActivity(), "data updated to cart", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });
                                                } else {
                                                    HashMap<String, Object> cartMap = new HashMap<>();
                                                    cartMap.put("name", holder.iteam_name.getText().toString());
                                                    cartMap.put("qty", holder.iteam_order_qty.getText().toString());
                                                    cartMap.put("rate", holder.iteam_price.getText().toString());
                                                    cartMap.put("image", model.getImage());
                                                    cartMap.put("amount", holder.iteam_price.getText().toString());
                                                    cartMap.put("quantity",holder.iteam_qty.getText().toString());
                                                    cartMap.put("mrprate",holder.text_view_original_cash_amount.getText().toString());
                                                    cartMap.put("mrpamount",holder.text_view_original_cash_amount.getText().toString());
                                                    cartMap.put("totaldiscount",String.valueOf(Integer.valueOf(holder.text_view_original_cash_amount.getText().toString())-
                                                            Integer.valueOf(holder.iteam_price.getText().toString())));

                                                    cartMap.put("discountpercentage",model.getDiscountpercentage());



                                                    cart.child(holder.iteam_name.getText().toString()).updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
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

                                    amount = amount - Integer.valueOf(holder.iteam_price.getText().toString());
                                    Snackbar snackbar = Snackbar.make(v, "", Snackbar.LENGTH_LONG);
                                    snackbar.setText("Amount Rs:: " + amount);
                                    snackbar.setAction("Procced", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            //biling....

                                            CartFragment cartobj = new CartFragment();
                                            FragmentTransaction fragmentTransaction_cart = getActivity().getSupportFragmentManager().beginTransaction();
                                            fragmentTransaction_cart.replace(parent_frame_layout_client.getId(),cartobj);
                                            fragmentTransaction_cart.addToBackStack(null);
                                            fragmentTransaction_cart.commit();
                                        }
                                    });

                                    snackbar.show();
                                    // snackbar.dismiss();
                                }
                            }
                        });


                        //String visit_user_id = getRef(position).getKey();
                        //Toast.makeText(FindPeopleActivity.this, "view on:" + visit_user_id + getRef(position), Toast.LENGTH_LONG).show();


                    }

                    @NonNull
                    @Override
                    public SearchFragment.iteam_list onCreateViewHolder(@NonNull ViewGroup p, int viewType) {
                        View view = LayoutInflater.from(p.getContext()).inflate(R.layout.iteamview_design, p, false);
                        SearchFragment.iteam_list viewHolder = new  SearchFragment.iteam_list(view);
                        return viewHolder;
                    }

                };
                firebaseRecyclerAdapter.startListening();
                recyclear_search.setAdapter(firebaseRecyclerAdapter);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        super.onStart();
    }

    public static class iteam_list extends RecyclerView.ViewHolder {
        LinearLayout parent_lin;
        ImageView iteam_pic;
        TextView iteam_name;
        TextView iteam_qty;
        TextView iteam_price;
        TextView iteam_order_qty;
        Button iteam_order_minus;
        Button iteam_order_plus;
        CardView parent_card;
        Button btn_add_btn;
        Button btn_subscribe;

        CardView crd_itm_minus_crd;
        CardView crd_itm_add_txt;
        CardView crd_itm_add_btn_crd;

        TextView text_view_original_cash_amount;

        TextView txt_discount_percentage;


        public iteam_list(@NonNull View itemView) {
            super(itemView);

            parent_lin = itemView.findViewById(R.id.item_parent_lin);
            iteam_pic = itemView.findViewById(R.id.item_pic_img);
            iteam_name = itemView.findViewById(R.id.item_name_txt);
            iteam_qty = itemView.findViewById(R.id.item_qty_txt);
            iteam_price = itemView.findViewById(R.id.item_price_txt);
            iteam_order_qty = itemView.findViewById(R.id.item_order_qty_txt);
            iteam_order_minus = itemView.findViewById(R.id.item_order_minus_btn);
            iteam_order_plus = itemView.findViewById(R.id.item_order_plus_btn);
            parent_card = itemView.findViewById(R.id.item_parent_card);
            btn_add_btn = itemView.findViewById(R.id.btn_add_btn);
            btn_subscribe = itemView.findViewById(R.id.btn_subscribe);
            crd_itm_minus_crd = itemView.findViewById(R.id.crd_itm_minus_crd);
            crd_itm_add_txt = itemView.findViewById(R.id.crd_itm_add_txt);
            crd_itm_add_btn_crd = itemView.findViewById(R.id.crd_itm_add_btn_crd);

            text_view_original_cash_amount = itemView.findViewById(R.id.text_view_original_cash_amount);

            txt_discount_percentage = itemView.findViewById(R.id.txt_discount_percentage);

            btn_add_btn.setClipToOutline(true);
        }

    }



}
