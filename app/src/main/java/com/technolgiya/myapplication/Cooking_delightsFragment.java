package com.technolgiya.myapplication;


import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;



/**
 * A simple {@link Fragment} subclass.
 */
public class Cooking_delightsFragment extends Fragment {


    public Cooking_delightsFragment() {
        // Required empty public constructor
    }

    private Toolbar toolbar;
    private RecyclerView horizontalsearch;
    private RecyclerView itemsRecycler;
    private DatabaseReference userRef,fresh,product,cart;
    private String selector ="";
    private String passselec="";
    private String subcategory = "";
    private String category="";
    private int temp=100;
    private int add =0;
    private int amount = 0;
    private  int iteam_amount=0;

    private int mrpamount = 0;
    private int iteam_mrp_amount = 0;

    private EditText et_search;
    private String categoryName="CookingDelights";
    private ProgressDialog progressDialog;

    private String subscriptiondeliverytime ="";

    private View parent_frame_layout_client;
    FusedLocationProviderClient fusedLocationProviderClient;

    private String subprValue ="";
    private String subscribe = "";

    private int calopen_close = 0;

    private int alert_qty = 0;

    private  TextView alert_sub_item_order_qty_txt;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fresh, container, false);


        categoryName = this.getArguments().getString("message");

         passselec= this.getArguments().getString("select");
         selector = passselec.trim().replace("\n","");

        parent_frame_layout_client = getActivity().findViewById(R.id.frame_layout_client);
        toolbar = getActivity().findViewById(R.id.toolbar_layout_client);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setTitle(categoryName);

        et_search = view.findViewById(R.id.et_search);
        et_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setFocusable(true);
                v.setFocusableInTouchMode(true);
                SearchFragment searchobj = new SearchFragment();
                FragmentTransaction fragmentTransaction_cart = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction_cart.replace(parent_frame_layout_client.getId(),searchobj);
                fragmentTransaction_cart.addToBackStack(null);
                fragmentTransaction_cart.commit();

            }
        });

        horizontalsearch =view.findViewById(R.id.frsh_menu_recycler);
        itemsRecycler = view.findViewById(R.id.item_fresh_recycler);

        FirebaseRecyclerOptions<fresh_model> options = null;
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL, false);
        horizontalsearch.setLayoutManager(manager);

        FirebaseRecyclerOptions<iteam_model> options_item_detail = null;
        itemsRecycler.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));


        return view;
    }
    @Override
    public void onStart() {
        super.onStart();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());


        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists()) {
                    final String subAdmin = dataSnapshot.child("subAdmin").getValue().toString();


                    fresh = FirebaseDatabase.getInstance().getReference().child("Location").child(subAdmin).child("Category")
                            .child(categoryName);

                    product = FirebaseDatabase.getInstance().getReference().child("Location").child(subAdmin).child("product");

                    FirebaseRecyclerOptions<fresh_model> options = null;

                    options =
                            new FirebaseRecyclerOptions.Builder<fresh_model>()
                                    .setQuery(fresh, fresh_model.class)
                                    .build();





                    final FirebaseRecyclerAdapter<fresh_model, Cooking_delightsFragment.subcategory_list> firebaseRecyclerAdapter =
                            new FirebaseRecyclerAdapter<fresh_model, Cooking_delightsFragment.subcategory_list>(options) {


                                @Override
                                protected void onBindViewHolder(@NonNull final Cooking_delightsFragment.subcategory_list holder,
                                                                final int position, @NonNull final fresh_model model) {
                                    holder.txt_search.setText(model.getName());

                                    if(model.getName().trim().equals(selector))
                                    {
                                      //  Toast.makeText(getContext(), String.valueOf(position), Toast.LENGTH_SHORT).show();
                                       // holder.txt_search.setBackgroundColor( Color.parseColor("#689f39") );
                                       // holder.txt_search.setTextColor( Color.parseColor("#ffffff"));
                                       // holder.txt_search.setBackgroundColor(1 == position ? Color.parseColor("#689f39") : Color.TRANSPARENT);
                                        //holder.txt_search.setTextColor(1 == position ? Color.parseColor("#ffffff"):Color.parseColor("#000000"));

                                        temp = position;
                                        Log.d("value of temp", "onBindViewHolder: "+temp);
                                       // Toast.makeText(getActivity().getApplicationContext(), ""+temp, Toast.LENGTH_SHORT).show();


                                    }

                                    holder.txt_search.setBackgroundColor(temp == position ? Color.parseColor("#3892cc") : Color.TRANSPARENT);
                                    holder.txt_search.setTextColor(temp == position ? Color.parseColor("#ffffff"):Color.parseColor("#000000"));


                                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            holder.getAdapterPosition();

                                            String visit_user_id = getRef(position).getKey();

                                            if (holder.getAdapterPosition() == RecyclerView.NO_POSITION) return;

                                            // Updating old as well as new positions
                                            notifyItemChanged(temp);
                                            temp= holder.getAdapterPosition();
                                            notifyItemChanged(temp);

                                            // Do your another stuff for your onClick
                                            selector = visit_user_id;


                                            FirebaseRecyclerOptions<iteam_model> options_item_detail = null;


                                            options_item_detail =
                                                    new FirebaseRecyclerOptions.Builder<iteam_model>()
                                                            .setQuery(product, iteam_model.class)
                                                            .build();

                                            final FirebaseRecyclerAdapter<iteam_model, Cooking_delightsFragment.iteam_list> firebaseRecyclerAdapter_iteam =
                                                    new FirebaseRecyclerAdapter<iteam_model, Cooking_delightsFragment.iteam_list>(options_item_detail) {

                                                        @Override
                                                        protected void onBindViewHolder(@NonNull final Cooking_delightsFragment.iteam_list holder,
                                                                                        final int position, @NonNull final iteam_model modelA) {

                                                            subcategory = modelA.getSubcategory();
                                                            category = modelA.getCategory();
                                                            if(!category.equals(categoryName))
                                                            {
                                                                holder.parent_card.setVisibility(View.GONE);
                                                                holder.parent_lin.setVisibility(View.GONE);
                                                                holder.parent_lin.setLayoutParams(new LinearLayout.LayoutParams(0,0));

                                                                holder.parent_card.setVisibility(View.GONE);
                                                                RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams)holder.parent_card.getLayoutParams();
                                                                layoutParams.setMargins(0, 0, 0, 0);
                                                                holder.parent_card.setLayoutParams(layoutParams);

                                                            }

                                                            if(!selector.equals("")) {
                                                                if(!selector.equals(subcategory))
                                                                {

                                                                    holder.parent_card.setVisibility(View.GONE);
                                                                    holder.parent_lin.setVisibility(View.GONE);
                                                                    holder.parent_lin.setLayoutParams(new LinearLayout.LayoutParams(0,0));

                                                                    holder.parent_card.setVisibility(View.GONE);
                                                                    RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams)holder.parent_card.getLayoutParams();
                                                                    layoutParams.setMargins(0, 0, 0, 0);
                                                                    holder.parent_card.setLayoutParams(layoutParams);

                                                                }
                                                                else {
                                                                    holder.iteam_name.setText(modelA.getName());
                                                                    Picasso.get().load(modelA.getImage()).placeholder(R.drawable.ad).into(holder.iteam_pic);
                                                                    holder.iteam_qty.setText(modelA.getQuantity());
                                                                    holder.iteam_price.setText(modelA.getRate());
                                                                    holder.text_view_original_cash_amount.setText(modelA.getMrp());

                                                                    holder.txt_discount_percentage.setText(modelA.getDiscountpercentage()+"% off");

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
                                                                            iteam_amount = 0;


                                                                            mrpamount = mrpamount+Integer.valueOf(holder.text_view_original_cash_amount.getText().toString());
                                                                            iteam_mrp_amount = 0;


                                                                            cart.child(holder.iteam_name.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                @Override
                                                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                    if(dataSnapshot.exists())
                                                                                    {
                                                                                        iteam_amount = Integer.valueOf(dataSnapshot.child("amount").getValue().toString());
                                                                                        iteam_amount = iteam_amount+Integer.valueOf(holder.iteam_price.getText().toString());

                                                                                        iteam_mrp_amount = Integer.valueOf(dataSnapshot.child("mrpamount").getValue().toString());
                                                                                        iteam_mrp_amount = iteam_mrp_amount+Integer.valueOf(holder.text_view_original_cash_amount
                                                                                                .getText().toString());

                                                                                        Toast.makeText(getActivity(), ""+iteam_amount, Toast.LENGTH_SHORT).show();

                                                                                        HashMap<String,Object> cartMap=new HashMap<>();
                                                                                        cartMap.put("name",holder.iteam_name.getText().toString());
                                                                                        cartMap.put("qty",holder.iteam_order_qty.getText().toString());
                                                                                        cartMap.put("rate",holder.iteam_price.getText().toString());
                                                                                        cartMap.put("mrprate",holder.text_view_original_cash_amount.getText().toString());
                                                                                        cartMap.put("mrpamount",String.valueOf(iteam_mrp_amount));
                                                                                        cartMap.put("totaldiscount",String.valueOf(iteam_mrp_amount-iteam_amount));
                                                                                        cartMap.put("image",modelA.getImage());
                                                                                        cartMap.put("amount",String.valueOf(iteam_amount));
                                                                                        cartMap.put("quantity",holder.iteam_qty.getText().toString());
                                                                                        cartMap.put("discountpercentage",modelA.getDiscountpercentage());

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
                                                                                        cartMap.put("image",modelA.getImage());
                                                                                        cartMap.put("amount",holder.iteam_price.getText().toString());
                                                                                        cartMap.put("quantity",holder.iteam_qty.getText().toString());
                                                                                        cartMap.put("mrprate",holder.text_view_original_cash_amount.getText().toString());
                                                                                        cartMap.put("mrpamount",holder.text_view_original_cash_amount.getText().toString());
                                                                                        cartMap.put("totaldiscount",String.valueOf(Integer.valueOf(holder.text_view_original_cash_amount.getText().toString()
                                                                                        )-Integer.valueOf(holder.iteam_price.getText().toString())));
                                                                                        cartMap.put("discountpercentage",modelA.getDiscountpercentage());

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
                                                                                        cartMap.put("image",modelA.getImage());
                                                                                        cartMap.put("amount",String.valueOf(iteam_amount));
                                                                                        cartMap.put("quantity",holder.iteam_qty.getText().toString());
                                                                                        cartMap.put("mrprate",holder.text_view_original_cash_amount.getText().toString());
                                                                                        cartMap.put("mrpamount",String.valueOf(iteam_mrp_amount));
                                                                                        cartMap.put("totaldiscount",String.valueOf(iteam_mrp_amount-iteam_amount));
                                                                                        cartMap.put("discountpercentage",modelA.getDiscountpercentage());

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
                                                                                        cartMap.put("image",modelA.getImage());
                                                                                        cartMap.put("amount",holder.iteam_price.getText().toString());
                                                                                        cartMap.put("quantity",holder.iteam_qty.getText().toString());
                                                                                        cartMap.put("mrprate",holder.text_view_original_cash_amount.getText().toString());
                                                                                        cartMap.put("mrpamount",holder.text_view_original_cash_amount.getText().toString());
                                                                                        cartMap.put("totaldiscount",String.valueOf(Integer.valueOf(holder.text_view_original_cash_amount.getText().toString())-Integer.valueOf(holder.iteam_price.getText().toString())));
                                                                                        cartMap.put("discountpercentage",modelA.getDiscountpercentage());

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

                                                                                    iteam_mrp_amount = 0;

                                                                                    cart.child(holder.iteam_name.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                        @Override
                                                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                            if (dataSnapshot.exists()) {
                                                                                                iteam_amount = Integer.valueOf(dataSnapshot.child("amount").getValue().toString());
                                                                                                iteam_amount = iteam_amount - Integer.valueOf(holder.iteam_price.getText().toString());

                                                                                                iteam_mrp_amount = Integer.valueOf(dataSnapshot.child("mrpamount").getValue().toString());
                                                                                                iteam_mrp_amount = iteam_mrp_amount-Integer.valueOf(holder.text_view_original_cash_amount
                                                                                                        .getText().toString());


                                                                                                Toast.makeText(getActivity(), "" + iteam_amount, Toast.LENGTH_SHORT).show();

                                                                                                HashMap<String, Object> cartMap = new HashMap<>();
                                                                                                cartMap.put("name", holder.iteam_name.getText().toString());
                                                                                                cartMap.put("qty", holder.iteam_order_qty.getText().toString());
                                                                                                cartMap.put("rate", holder.iteam_price.getText().toString());
                                                                                                cartMap.put("image", modelA.getImage());
                                                                                                cartMap.put("amount", String.valueOf(iteam_amount));
                                                                                                cartMap.put("quantity",holder.iteam_qty.getText().toString());
                                                                                                cartMap.put("mrprate",holder.text_view_original_cash_amount.getText().toString());
                                                                                                cartMap.put("mrpamount",String.valueOf(iteam_mrp_amount));
                                                                                                cartMap.put("totaldiscount",String.valueOf(iteam_mrp_amount-iteam_amount));
                                                                                                cartMap.put("discountpercentage",modelA.getDiscountpercentage());

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
                                                                                                cartMap.put("image", modelA.getImage());
                                                                                                cartMap.put("amount", holder.iteam_price.getText().toString());
                                                                                                cartMap.put("quantity",holder.iteam_qty.getText().toString());
                                                                                                cartMap.put("mrprate",holder.text_view_original_cash_amount.getText().toString());
                                                                                                cartMap.put("mrpamount",holder.text_view_original_cash_amount.getText().toString());
                                                                                                cartMap.put("totaldiscount",String.valueOf(Integer.valueOf(holder.text_view_original_cash_amount.getText().toString())-Integer.valueOf(holder.iteam_price
                                                                                                .getText().toString())));
                                                                                                cartMap.put("discountpercentage",modelA.getDiscountpercentage());

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

                                                                            holder.btn_subscribe.setOnClickListener(new View.OnClickListener() {
                                                                                @Override
                                                                                public void onClick(View v) {
                                                                                    progressDialog = new ProgressDialog(getActivity().getApplicationContext());
                                                                                    LayoutInflater layout = LayoutInflater.from(getActivity().getApplicationContext());
                                                                                    final View dialogView = layout.inflate(R.layout.subscribealert, null);
                                                                                    final AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();
                                                                                    dialog.setView(dialogView);
                                                                                    dialog.show();
                                                                                    RadioGroup radioGroup;
                                                                                    radioGroup=(RadioGroup)dialogView.findViewById(R.id.radioGroup);
                                                                                    final Spinner month_spin;
                                                                                    final Spinner week_spin;

                                                                                    final LinearLayout lin_sub_rep = dialogView.findViewById(R.id.lin_sub_rep);
                                                                                    final TextView txt_sub_rep = dialogView.findViewById(R.id.txt_sub_rep);

                                                                                    ImageView alert_sub_item_pic_img = dialogView.findViewById(R.id.alert_sub_item_pic_img);

                                                                                    Picasso.get().load(modelA.getImage()).placeholder(R.drawable.ad).into(alert_sub_item_pic_img);

                                                                                    TextView alert_sub_item_name_txt = dialogView.findViewById(R.id.alert_sub_item_name_txt);

                                                                                    alert_sub_item_name_txt.setText(modelA.getName());

                                                                                    TextView alert_sub_item_qty_txt = dialogView.findViewById(R.id.alert_sub_item_qty_txt);
                                                                                    alert_sub_item_qty_txt.setText(modelA.getQuantity());

                                                                                    TextView alert_sub_item_price_txt = dialogView.findViewById(R.id.alert_sub_item_price_txt);
                                                                                    alert_sub_item_price_txt.setText(modelA.getRate());

                                                                                    TextView alert_sub_text_view_original_cash_amount = dialogView.findViewById(R.id.alert_sub_text_view_original_cash_amount);
                                                                                    alert_sub_text_view_original_cash_amount.setText(modelA.getMrp());

                                                                                    TextView alert_sub_txt_discount_percentage =dialogView.findViewById(R.id.alert_sub_txt_discount_percentage);
                                                                                    alert_sub_txt_discount_percentage.setText(modelA.getDiscountpercentage()+"% off");


                                                                                     TextView alret_dropdown_txt = dialogView.findViewById(R.id.alret_dropdown_txt);


                                                                                    Button alert_sub_item_order_plus_btn = dialogView.findViewById(R.id.alert_sub_item_order_plus_btn);
                                                                                   alert_sub_item_order_qty_txt = dialogView.findViewById(R.id.alert_sub_item_order_qty_txt);
                                                                                    Button alert_sub_item_order_minus_btn = dialogView.findViewById(R.id.alert_sub_item_order_minus_btn);
                                                                                    alert_qty = 0;

                                                                                    alert_sub_item_order_plus_btn.setOnClickListener(new View.OnClickListener() {
                                                                                        @Override
                                                                                        public void onClick(View v) {
                                                                                            alert_qty = alert_qty+1;
                                                                                            alert_sub_item_order_qty_txt.setText(""+alert_qty);
                                                                                        }
                                                                                    });

                                                                                    alert_sub_item_order_minus_btn.setOnClickListener(new View.OnClickListener() {
                                                                                        @Override
                                                                                        public void onClick(View v) {
                                                                                            if(alert_qty>1){
                                                                                            alert_qty = alert_qty - 1;
                                                                                            alert_sub_item_order_qty_txt.setText(""+alert_qty);}

                                                                                        }
                                                                                    });


                                                                                    calopen_close = 0;

                                                                                    alret_dropdown_txt.setOnClickListener(new View.OnClickListener() {
                                                                                        @Override
                                                                                        public void onClick(View v) {

                                                                                            if(calopen_close%2==0) {
                                                                                                FrameLayout alert_frame = dialogView.findViewById(R.id.alert_frame);
                                                                                                alert_frame.setVisibility(View.VISIBLE);
                                                                                                CalendarView cal_alert_pick = dialogView.findViewById(R.id.cal_alert_pick);
                                                                                                cal_alert_pick.setVisibility(View.VISIBLE);
                                                                                            }
                                                                                            else{
                                                                                                FrameLayout alert_frame = dialogView.findViewById(R.id.alert_frame);
                                                                                                alert_frame.setVisibility(View.GONE);
                                                                                                CalendarView cal_alert_pick = dialogView.findViewById(R.id.cal_alert_pick);
                                                                                                cal_alert_pick.setVisibility(View.GONE);
                                                                                            }
                                                                                            calopen_close = calopen_close+1;
                                                                                        }
                                                                                    });

                                                                                    final TextView txt_alert_start_date = dialogView.findViewById(R.id.txt_alert_start_date);

                                                                                     CalendarView cal_alert_pick = dialogView.findViewById(R.id.cal_alert_pick);

                                                                                    cal_alert_pick.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                                                                                        @Override
                                                                                        public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

                                                                                            final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                                                                                            Calendar calendar = Calendar.getInstance();
                                                                                            calendar.set(year, month, dayOfMonth);
                                                                                            String sDate = sdf.format(calendar.getTime());
                                                                                           // Log.d(TAG, "sDate formatted: " + sDate);
                                                                                            txt_alert_start_date.setText(sDate);


                                                                                        }
                                                                                    });




                                                                                    week_spin=dialogView.findViewById(R.id.week_spin);
                                                                                    month_spin = dialogView.findViewById(R.id.month_spin);

                                                                                    final EditText qtyofsub = dialogView.findViewById(R.id.qtyofsub);

                                                                                    final Button btn_subscribe = dialogView.findViewById(R.id.btn_subscribe);

                                                                                    final EditText address=dialogView.findViewById(R.id.et_address_sub);
                                                                                    final EditText pin=dialogView.findViewById(R.id.et_pin_sub);
                                                                                   // qtyofsub.setVisibility(View.VISIBLE);


                                                                                    radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                                                                        @Override
                                                                                        public void onCheckedChanged(RadioGroup group, int checkedId) {

                                                                                            RadioButton radioMonthely;
                                                                                            RadioButton radioWeekly;
                                                                                            RadioButton radioDaily;
                                                                                            RadioButton choosen;
                                                                                            radioMonthely = dialogView.findViewById(R.id.radioMonthely);
                                                                                            radioWeekly = dialogView.findViewById(R.id.radioWeekly);
                                                                                            radioDaily =  dialogView.findViewById(R.id.radioDaily);

                                                                                            Log.d("checkedid", "onCheckedChanged: "+checkedId);

                                                                                            int monthid = radioMonthely.getId();
                                                                                            int weekid = radioWeekly.getId();

                                                                                            choosen = (RadioButton)dialogView.findViewById(checkedId);

                                                                                            if(checkedId == -1){
                                                                                                Toast.makeText(getActivity().getApplicationContext(),"Nothing selected", Toast.LENGTH_SHORT).show();
                                                                                            }
                                                                                            else{
                                                                                                Toast.makeText(getActivity().getApplicationContext(),choosen.getText(), Toast.LENGTH_SHORT).show();
                                                                                                if(choosen.getText().equals(" Monthly"))
                                                                                                {
                                                                                                    month_spin.setVisibility(View.VISIBLE);
                                                                                                    week_spin.setVisibility(View.GONE);
                                                                                                    subscribe = "subscribeM";

                                                                                                    txt_sub_rep.setVisibility(View.VISIBLE);
                                                                                                    lin_sub_rep.setVisibility(View.VISIBLE);


                                                                                                    month_spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                                                                        @Override
                                                                                                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                                                                                                        {

                                                                                                            subscriptiondeliverytime =parent.getItemAtPosition(position).toString();
                                                                                                            Toast.makeText(getActivity().getApplicationContext(), "sub"+subscriptiondeliverytime, Toast.LENGTH_SHORT).show();


                                                                                                        }

                                                                                                        @Override
                                                                                                        public void onNothingSelected(AdapterView<?> parent)
                                                                                                        {

                                                                                                        }
                                                                                                    });

                                                                                                }
                                                                                                else if(choosen.getText().equals(" Weekly"))
                                                                                                {
                                                                                                    week_spin.setVisibility(View.VISIBLE);
                                                                                                    month_spin.setVisibility(View.GONE);

                                                                                                    subscribe = "subscribeW";


                                                                                                    txt_sub_rep.setVisibility(View.VISIBLE);
                                                                                                    lin_sub_rep.setVisibility(View.VISIBLE);


                                                                                                    week_spin.setAdapter(new ArrayAdapter<String>(getActivity().getApplicationContext(),
                                                                                                            android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.day)));
                                                                                                    week_spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                                                                        @Override
                                                                                                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                                                                                                        {
                                                                                                            subscriptiondeliverytime =parent.getItemAtPosition(position).toString();
                                                                                                            Toast.makeText(getActivity().getApplicationContext()," sub"+ subscriptiondeliverytime, Toast.LENGTH_SHORT).show();
                                                                                                        }

                                                                                                        @Override
                                                                                                        public void onNothingSelected(AdapterView<?> parent)
                                                                                                        {


                                                                                                        }
                                                                                                    });

                                                                                                }


                                                                                                else if(choosen.getText().equals(" Daily"))
                                                                                                {


                                                                                                    subscribe = "subscribeD";

                                                                                                    txt_sub_rep.setVisibility(View.GONE);
                                                                                                    lin_sub_rep.setVisibility(View.GONE);
                                                                                                    subscriptiondeliverytime = "Everyday";
                                                                                                   /* week_spin.setAdapter(new ArrayAdapter<String>(getActivity().getApplicationContext(),
                                                                                                            android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.day)));
                                                                                                    week_spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                                                                        @Override
                                                                                                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                                                                                                        {
                                                                                                            subscriptiondeliverytime =parent.getItemAtPosition(position).toString();
                                                                                                            Toast.makeText(getActivity().getApplicationContext()," sub"+ subscriptiondeliverytime, Toast.LENGTH_SHORT).show();
                                                                                                        }

                                                                                                        @Override
                                                                                                        public void onNothingSelected(AdapterView<?> parent)
                                                                                                        {


                                                                                                        }
                                                                                                    });*/

                                                                                                }



                                                                                            }

                                                                                              /*  switch(checkedId){
                                                                                                    case monthid:
                                                                                                        Toast.makeText(getActivity().getApplicationContext(), "Monthely", Toast.LENGTH_SHORT).show();
                                                                                                        break;
                                                                                                    case weekid:
                                                                                                        Toast.makeText(getActivity().getApplicationContext(), "weekly", Toast.LENGTH_SHORT).show();
                                                                                                        break;
                                                                                                    default:
                                                                                                        throw new IllegalStateException("Unexpected value: " + checkedId);
                                                                                                }*/
                                                                                                                                }
                                                                                    });

                                                                                    btn_subscribe.setOnClickListener(new View.OnClickListener() {
                                                                                        @Override
                                                                                        public void onClick(View v) {
                                                                                            final DatabaseReference subscribeproductRef =FirebaseDatabase.getInstance().getReference().child("Location").child(subAdmin).child(subscribe)
                                                                                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("product").child(holder.iteam_name.getText().toString());

                                                                                            if(alert_sub_item_order_qty_txt.getText().toString().equals("ADD"))
                                                                                            {
                                                                                                Toast.makeText(getActivity().getApplicationContext(), "qty can not be empty", Toast.LENGTH_SHORT).show();
                                                                                            }

                                                                                            else if(txt_alert_start_date.getText().toString().equals("dd/mm/yyyy"))
                                                                                            {
                                                                                                Toast.makeText(getActivity().getApplicationContext(), "Start date can not be empty", Toast.LENGTH_SHORT).show();
                                                                                            }

                                                                                          /*  if(qtyofsub.getText().toString().equals(""))
                                                                                            {
                                                                                                Toast.makeText(getActivity().getApplicationContext(), "qty can not be empty", Toast.LENGTH_SHORT).show();
                                                                                            }*/

                                                                                            else {
                                                                                                HashMap<String, Object> submap = new HashMap<>();
                                                                                                submap.put("name", holder.iteam_name.getText().toString());
                                                                                                submap.put("qty", alert_sub_item_order_qty_txt.getText().toString());
                                                                                                submap.put("rate", holder.iteam_price.getText().toString());
                                                                                                submap.put("image", modelA.getImage());
                                                                                                submap.put("amount", String.valueOf(Integer.valueOf(holder.iteam_price.getText().toString())*Integer.valueOf( alert_sub_item_order_qty_txt.getText().toString())));
                                                                                                submap.put("quantity", holder.iteam_qty.getText().toString());
                                                                                                submap.put("startdate",txt_alert_start_date.getText().toString());
                                                                                                //Log.d("startdate", "onClick: "+txt_alert_start_date);
                                                                                                submap.put("timeOfdel",subscriptiondeliverytime);
                                                                                                subscribeproductRef.updateChildren(submap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                    @Override
                                                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                                                        if(task.isSuccessful())
                                                                                                        {
                                                                                                            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                                @Override
                                                                                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                                                    final String name = dataSnapshot.child("name").getValue().toString();
                                                                                                                    final String phoneNumber = dataSnapshot.child("phoneNumber").getValue().toString();
                                                                                                                    String timeOfdel =subscriptiondeliverytime;
                                                                                                                    final String Address = address.getText().toString();
                                                                                                                    final String Pin= pin.getText().toString();
                                                                                                                    if(Pin.equals(""))
                                                                                                                    {
                                                                                                                        Toast.makeText(getActivity().getApplicationContext(), "Pin can not be empty", Toast.LENGTH_SHORT).show();
                                                                                                                        subscribeproductRef.removeValue();
                                                                                                                    }
                                                                                                                    else{
                                                                                                                        DatabaseReference adresspinRef=FirebaseDatabase.getInstance().getReference().child("serviceAreapin");
                                                                                                                        adresspinRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                                            @Override
                                                                                                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                                                                int flag =0;
                                                                                                                                for (DataSnapshot pin:dataSnapshot.getChildren())
                                                                                                                                {
                                                                                                                                    Log.d("pin", "onDataChange: "+pin.getValue().toString());
                                                                                                                                    if(Pin.equals(pin.getValue().toString()))
                                                                                                                                    {
                                                                                                                                        //  Toast.makeText(BillingActivity.this, pin.getValue().toString(), Toast.LENGTH_SHORT).show();
                                                                                                                                        flag=1;
                                                                                                                                    }
                                                                                                                                }
                                                                                                                                if(flag==1)
                                                                                                                                {
                                                                                                                                    if(Address.equals(""))
                                                                                                                                    {
                                                                                                                                        Toast.makeText(getActivity().getApplicationContext(), "Address can not be empty", Toast.LENGTH_SHORT).show();
                                                                                                                                    }
                                                                                                                                    else {

                                                                                                                                        final DatabaseReference subscribeRef = FirebaseDatabase.getInstance().getReference().child("Location").child(subAdmin).child(subscribe)
                                                                                                                                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                                                                                                                        DatabaseReference subproduct = FirebaseDatabase.getInstance().getReference().child("Location").child(subAdmin).child(subscribe)
                                                                                                                                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("product");
                                                                                                                                        subproduct.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                                                            @Override
                                                                                                                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                                                                                subprValue ="";
                                                                                                                                                for (DataSnapshot product : dataSnapshot.getChildren()) {
                                                                                                                                                    Log.d("snap", "onDataChange: " + product);
                                                                                                                                                    String itmname = product.getKey();
                                                                                                                                                    String itmqty = product.child("qty").getValue().toString();
                                                                                                                                                    String itmrate = product.child("rate").getValue().toString();
                                                                                                                                                    String itmamt = product.child("amount").getValue().toString();
                                                                                                                                                    String timeOfdel = product.child("timeOfdel").getValue().toString();
                                                                                                                                                    if(product.hasChild("value"))
                                                                                                                                                    {
                                                                                                                                                        subprValue = product.child("value").getValue().toString();
                                                                                                                                                    }
                                                                                                                                                    subprValue = subprValue+"\n "+ itmname + "\nQty: " + itmqty + "\nrate: " + itmrate + "\nTotal: " + itmamt + "\n Time Of del: " + timeOfdel;

                                                                                                                                                    HashMap<String,Object> submap= new HashMap<>();
                                                                                                                                                    submap.put("name",name);
                                                                                                                                                    submap.put("phoneNumber",phoneNumber);
                                                                                                                                                    submap.put("timeOfdel",timeOfdel);
                                                                                                                                                    submap.put("value",subprValue);
                                                                                                                                                    submap.put("Address",Address);
                                                                                                                                                    submap.put("pin",Pin);
                                                                                                                                                    subscribeRef.updateChildren(submap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                                                                        @Override
                                                                                                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                                                                                                            if(task.isSuccessful())
                                                                                                                                                            {
                                                                                                                                                                Toast.makeText(getActivity().getApplicationContext(), "subscriptiona added", Toast.LENGTH_SHORT).show();
                                                                                                                                                                dialog.dismiss();
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
                                                                                                                                else{
                                                                                                                                    Toast.makeText(getActivity().getApplicationContext(), "Sorry we do not serve at this location", Toast.LENGTH_SHORT).show();
                                                                                                                                    subscribeproductRef.removeValue();
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
                                                                                                    }
                                                                                                });

                                                                                            }

                                                                                        }
                                                                                    });


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

                                                                }
                                                            }
                                                        }

                                                        @NonNull
                                                        @Override
                                                        public Cooking_delightsFragment.iteam_list onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                                                            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.iteamview_design, parent, false);
                                                            Cooking_delightsFragment.iteam_list viewHolder = new Cooking_delightsFragment.iteam_list(view);
                                                            return viewHolder;
                                                        }
                                                    };
                                            firebaseRecyclerAdapter_iteam.startListening();
                                            itemsRecycler.setAdapter(firebaseRecyclerAdapter_iteam);
                                            itemsRecycler.getRecycledViewPool().setMaxRecycledViews(0, 0);



                                        }
                                    });
                                }

                                @NonNull
                                @Override
                                public Cooking_delightsFragment.subcategory_list onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_search_design, parent, false);
                                    Cooking_delightsFragment.subcategory_list viewHolder = new Cooking_delightsFragment.subcategory_list(view);
                                    return viewHolder;
                                }
                            };
                    firebaseRecyclerAdapter.startListening();
                    horizontalsearch.setAdapter(firebaseRecyclerAdapter);
                    new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                    horizontalsearch.smoothScrollToPosition(temp);
                    Log.d("temp", "onDataChange: "+temp);
                        }
                    }, 100);




                    FirebaseRecyclerOptions<iteam_model> options_item_detail = null;


                    options_item_detail =
                            new FirebaseRecyclerOptions.Builder<iteam_model>()
                                    .setQuery(product, iteam_model.class)
                                    .build();


                    final FirebaseRecyclerAdapter<iteam_model, Cooking_delightsFragment.iteam_list> firebaseRecyclerAdapter_iteam =
                            new FirebaseRecyclerAdapter<iteam_model, Cooking_delightsFragment.iteam_list>(options_item_detail) {

                                @Override
                                protected void onBindViewHolder(@NonNull final Cooking_delightsFragment.iteam_list holder,
                                                                final int position, @NonNull final iteam_model modelA) {

                                    category = modelA.getCategory();
                                    if(!category.equals(categoryName))
                                    {
                                        holder.parent_card.setVisibility(View.GONE);
                                        holder.parent_lin.setVisibility(View.GONE);
                                        holder.parent_lin.setLayoutParams(new LinearLayout.LayoutParams(0,0));

                                        holder.parent_card.setVisibility(View.GONE);
                                        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams)holder.parent_card.getLayoutParams();
                                        layoutParams.setMargins(0, 0, 0, 0);
                                        holder.parent_card.setLayoutParams(layoutParams);

                                    }


                                    if(selector.equals(selector)) {
                                        subcategory = modelA.getSubcategory();
                                        if(!selector.equals(subcategory))
                                        {

                                            holder.parent_card.setVisibility(View.GONE);
                                            holder.parent_lin.setVisibility(View.GONE);
                                            holder.parent_lin.setLayoutParams(new LinearLayout.LayoutParams(0,0));

                                            holder.parent_card.setVisibility(View.GONE);
                                            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams)holder.parent_card.getLayoutParams();
                                            layoutParams.setMargins(0, 0, 0, 0);
                                            holder.parent_card.setLayoutParams(layoutParams);

                                        }

                                        holder.iteam_name.setText(modelA.getName());
                                        Picasso.get().load(modelA.getImage()).placeholder(R.drawable.ad).into(holder.iteam_pic);
                                        holder.iteam_qty.setText(modelA.getQuantity());
                                        holder.iteam_price.setText(modelA.getRate());
                                        holder.text_view_original_cash_amount.setText(modelA.getMrp());


                                        holder.txt_discount_percentage.setText(modelA.getDiscountpercentage()+"% off");


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
                                                            cartMap.put("image",modelA.getImage());
                                                            cartMap.put("amount",String.valueOf(iteam_amount));
                                                            cartMap.put("quantity",holder.iteam_qty.getText().toString());
                                                            cartMap.put("mrprate",holder.text_view_original_cash_amount.getText().toString());
                                                            cartMap.put("mrpamount",String.valueOf(iteam_mrp_amount));
                                                            cartMap.put("totaldiscount",String.valueOf(iteam_mrp_amount-iteam_amount));
                                                            cartMap.put("discountpercentage",modelA.getDiscountpercentage());

                                                            cart.child(holder.iteam_name.getText().toString()).updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if(task.isSuccessful())
                                                                    {
                                                                        Toast.makeText(getActivity().getApplicationContext(), "data updated to cart", Toast.LENGTH_SHORT).show();
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
                                                            cartMap.put("image",modelA.getImage());
                                                            cartMap.put("amount",holder.iteam_price.getText().toString());
                                                            cartMap.put("quantity",holder.iteam_qty.getText().toString());
                                                            cartMap.put("mrprate",holder.text_view_original_cash_amount.getText().toString());
                                                            cartMap.put("mrpamount",holder.text_view_original_cash_amount.getText().toString());
                                                            cartMap.put("totaldiscount",String.valueOf(Integer.valueOf(holder.text_view_original_cash_amount.getText().toString())-Integer.valueOf(holder.iteam_price.getText().toString())));
                                                            cartMap.put("discountpercentage",modelA.getDiscountpercentage());

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
                                                            cartMap.put("image",modelA.getImage());
                                                            cartMap.put("amount",String.valueOf(iteam_amount));
                                                            cartMap.put("quantity",holder.iteam_qty.getText().toString());
                                                            cartMap.put("mrprate",holder.text_view_original_cash_amount.getText().toString());
                                                            cartMap.put("mrpamount",String.valueOf(iteam_mrp_amount));
                                                            cartMap.put("totaldiscount",String.valueOf(iteam_mrp_amount-iteam_amount));
                                                            cartMap.put("discountpercentage",modelA.getDiscountpercentage());


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
                                                            cartMap.put("image",modelA.getImage());
                                                            cartMap.put("amount",holder.iteam_price.getText().toString());
                                                            cartMap.put("quantity",holder.iteam_qty.getText().toString());
                                                            cartMap.put("mrprate",holder.text_view_original_cash_amount.getText().toString());
                                                            cartMap.put("mrpamount",holder.text_view_original_cash_amount.getText().toString());
                                                            cartMap.put("totaldiscount",String.valueOf(Integer.valueOf(holder.text_view_original_cash_amount.getText().toString())-Integer.valueOf(holder.iteam_price.getText().toString())));
                                                            cartMap.put("discountpercentage",modelA.getDiscountpercentage());


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
                                                                    cartMap.put("image", modelA.getImage());
                                                                    cartMap.put("amount", String.valueOf(iteam_amount));
                                                                    cartMap.put("quantity",holder.iteam_qty.getText().toString());
                                                                    cartMap.put("mrprate",holder.text_view_original_cash_amount.getText().toString());
                                                                    cartMap.put("mrpamount",String.valueOf(iteam_mrp_amount));
                                                                    cartMap.put("totaldiscount",String.valueOf(iteam_mrp_amount-iteam_amount));
                                                                    cartMap.put("discountpercentage",modelA.getDiscountpercentage());

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
                                                                    cartMap.put("image", modelA.getImage());
                                                                    cartMap.put("amount", holder.iteam_price.getText().toString());
                                                                    cartMap.put("quantity",holder.iteam_qty.getText().toString());
                                                                    cartMap.put("mrprate",holder.text_view_original_cash_amount.getText().toString());
                                                                    cartMap.put("mrpamount",holder.text_view_original_cash_amount.getText().toString());
                                                                    cartMap.put("totaldiscount",String.valueOf(Integer.valueOf(holder.text_view_original_cash_amount.getText().toString())-
                                                                            Integer.valueOf(holder.iteam_price.getText().toString())));

                                                                    cartMap.put("discountpercentage",modelA.getDiscountpercentage());



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


                                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                holder.getAdapterPosition();
                                                String visit_user_id = getRef(position).getKey();
                                                Toast.makeText(getActivity(), visit_user_id, Toast.LENGTH_SHORT).show();
                                            }
                                        });


                                        holder.btn_subscribe.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                progressDialog = new ProgressDialog(getActivity().getApplicationContext());
                                                LayoutInflater layout = LayoutInflater.from(getActivity().getApplicationContext());
                                                final View dialogView = layout.inflate(R.layout.subscribealert, null);
                                                final AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();
                                                dialog.setView(dialogView);
                                                dialog.show();
                                                RadioGroup radioGroup;
                                                radioGroup=(RadioGroup)dialogView.findViewById(R.id.radioGroup);
                                                final Spinner month_spin;
                                                final Spinner week_spin;

                                                final LinearLayout lin_sub_rep = dialogView.findViewById(R.id.lin_sub_rep);
                                                final TextView txt_sub_rep = dialogView.findViewById(R.id.txt_sub_rep);


                                                ImageView alert_sub_item_pic_img = dialogView.findViewById(R.id.alert_sub_item_pic_img);

                                                Picasso.get().load(modelA.getImage()).placeholder(R.drawable.ad).into(alert_sub_item_pic_img);

                                                TextView alert_sub_item_name_txt = dialogView.findViewById(R.id.alert_sub_item_name_txt);

                                                alert_sub_item_name_txt.setText(modelA.getName());

                                                TextView alert_sub_item_qty_txt = dialogView.findViewById(R.id.alert_sub_item_qty_txt);
                                                alert_sub_item_qty_txt.setText(modelA.getQuantity());

                                                TextView alert_sub_item_price_txt = dialogView.findViewById(R.id.alert_sub_item_price_txt);
                                                alert_sub_item_price_txt.setText(modelA.getRate());

                                                TextView alert_sub_text_view_original_cash_amount = dialogView.findViewById(R.id.alert_sub_text_view_original_cash_amount);
                                                alert_sub_text_view_original_cash_amount.setText(modelA.getMrp());

                                                TextView alert_sub_txt_discount_percentage =dialogView.findViewById(R.id.alert_sub_txt_discount_percentage);
                                                alert_sub_txt_discount_percentage.setText(modelA.getDiscountpercentage()+"% off");

                                                TextView alret_dropdown_txt = dialogView.findViewById(R.id.alret_dropdown_txt);

                                                Button alert_sub_item_order_plus_btn = dialogView.findViewById(R.id.alert_sub_item_order_plus_btn);
                                                alert_sub_item_order_qty_txt = dialogView.findViewById(R.id.alert_sub_item_order_qty_txt);
                                                Button alert_sub_item_order_minus_btn = dialogView.findViewById(R.id.alert_sub_item_order_minus_btn);
                                                alert_qty = 0;

                                                alert_sub_item_order_plus_btn.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        alert_qty = alert_qty+1;
                                                        alert_sub_item_order_qty_txt.setText(""+alert_qty);
                                                    }
                                                });

                                                alert_sub_item_order_minus_btn.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        if(alert_qty>1){
                                                            alert_qty = alert_qty - 1;
                                                            alert_sub_item_order_qty_txt.setText(""+alert_qty);}

                                                    }
                                                });







                                                calopen_close = 0;

                                                alret_dropdown_txt.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {

                                                        if(calopen_close%2==0) {
                                                            FrameLayout alert_frame = dialogView.findViewById(R.id.alert_frame);
                                                            alert_frame.setVisibility(View.VISIBLE);
                                                            CalendarView cal_alert_pick = dialogView.findViewById(R.id.cal_alert_pick);
                                                            cal_alert_pick.setVisibility(View.VISIBLE);
                                                        }
                                                        else{
                                                            FrameLayout alert_frame = dialogView.findViewById(R.id.alert_frame);
                                                            alert_frame.setVisibility(View.GONE);
                                                            CalendarView cal_alert_pick = dialogView.findViewById(R.id.cal_alert_pick);
                                                            cal_alert_pick.setVisibility(View.GONE);
                                                        }
                                                        calopen_close = calopen_close+1;
                                                    }
                                                });

                                                final TextView txt_alert_start_date = dialogView.findViewById(R.id.txt_alert_start_date);

                                                 CalendarView cal_alert_pick = dialogView.findViewById(R.id.cal_alert_pick);
                                                 cal_alert_pick.setVisibility(dialogView.VISIBLE);

                                                cal_alert_pick.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                                                    @Override
                                                    public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

                                                        final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                                                        Calendar calendar = Calendar.getInstance();
                                                        calendar.set(year, month, dayOfMonth);
                                                        String sDate = sdf.format(calendar.getTime());
                                                        // Log.d(TAG, "sDate formatted: " + sDate);
                                                        txt_alert_start_date.setText(sDate);


                                                    }
                                                });




                                                week_spin=dialogView.findViewById(R.id.week_spin);
                                                month_spin = dialogView.findViewById(R.id.month_spin);

                                                final EditText qtyofsub = dialogView.findViewById(R.id.qtyofsub);

                                                final Button btn_subscribe = dialogView.findViewById(R.id.btn_subscribe);

                                                final EditText address=dialogView.findViewById(R.id.et_address_sub);
                                                final EditText pin=dialogView.findViewById(R.id.et_pin_sub);



                                                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                                    @Override
                                                    public void onCheckedChanged(RadioGroup group, int checkedId) {

                                                        RadioButton radioMonthely;
                                                        RadioButton radioWeekly;
                                                        RadioButton choosen;
                                                        radioMonthely = dialogView.findViewById(R.id.radioMonthely);
                                                        radioWeekly = dialogView.findViewById(R.id.radioWeekly);

                                                        Log.d("checkedid", "onCheckedChanged: "+checkedId);

                                                        int monthid = radioMonthely.getId();
                                                         int weekid = radioWeekly.getId();

                                                        choosen = (RadioButton)dialogView.findViewById(checkedId);

                                                        if(checkedId == -1){
                                                            Toast.makeText(getActivity().getApplicationContext(),"Nothing selected", Toast.LENGTH_SHORT).show();
                                                        }
                                                        else{
                                                            Toast.makeText(getActivity().getApplicationContext(),choosen.getText(), Toast.LENGTH_SHORT).show();
                                                            if(choosen.getText().equals(" Monthly"))
                                                            {
                                                                lin_sub_rep.setVisibility(View.VISIBLE);
                                                                txt_sub_rep.setVisibility(View.VISIBLE);

                                                                month_spin.setVisibility(View.VISIBLE);
                                                                week_spin.setVisibility(View.GONE);
                                                                subscribe = "subscribeM";

                                                                month_spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                                    @Override
                                                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                                                                    {

                                                                        subscriptiondeliverytime =parent.getItemAtPosition(position).toString();
                                                                        Toast.makeText(getActivity().getApplicationContext(), "sub"+subscriptiondeliverytime, Toast.LENGTH_SHORT).show();


                                                                    }

                                                                    @Override
                                                                    public void onNothingSelected(AdapterView<?> parent)
                                                                    {

                                                                    }
                                                                });

                                                            }
                                                            else if(choosen.getText().equals(" Weekly"))
                                                            {
                                                                week_spin.setVisibility(View.VISIBLE);
                                                                month_spin.setVisibility(View.GONE);
                                                                subscribe = "subscribeW";

                                                                lin_sub_rep.setVisibility(View.VISIBLE);
                                                                txt_sub_rep.setVisibility(View.VISIBLE);

                                                                week_spin.setAdapter(new ArrayAdapter<String>(getActivity().getApplicationContext(),
                                                                        android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.day)));
                                                                week_spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                                    @Override
                                                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                                                                    {
                                                                        subscriptiondeliverytime =parent.getItemAtPosition(position).toString();
                                                                        Toast.makeText(getActivity().getApplicationContext()," sub"+ subscriptiondeliverytime, Toast.LENGTH_SHORT).show();
                                                                    }

                                                                    @Override
                                                                    public void onNothingSelected(AdapterView<?> parent)
                                                                    {


                                                                    }
                                                                });

                                                            }
                                                            else if(choosen.getText().equals(" Daily"))
                                                            {


                                                                subscribe = "subscribeD";

                                                                txt_sub_rep.setVisibility(View.GONE);
                                                                lin_sub_rep.setVisibility(View.GONE);
                                                                subscriptiondeliverytime = "Everyday";
                                                                                                   /* week_spin.setAdapter(new ArrayAdapter<String>(getActivity().getApplicationContext(),
                                                                                                            android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.day)));
                                                                                                    week_spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                                                                        @Override
                                                                                                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                                                                                                        {
                                                                                                            subscriptiondeliverytime =parent.getItemAtPosition(position).toString();
                                                                                                            Toast.makeText(getActivity().getApplicationContext()," sub"+ subscriptiondeliverytime, Toast.LENGTH_SHORT).show();
                                                                                                        }

                                                                                                        @Override
                                                                                                        public void onNothingSelected(AdapterView<?> parent)
                                                                                                        {


                                                                                                        }
                                                                                                    });*/

                                                            }




                                                        }

                                                      /*  switch(checkedId){
                                                            case monthid:
                                                                Toast.makeText(getActivity().getApplicationContext(), "Monthely", Toast.LENGTH_SHORT).show();
                                                                break;
                                                            case weekid:
                                                                Toast.makeText(getActivity().getApplicationContext(), "weekly", Toast.LENGTH_SHORT).show();
                                                                break;
                                                            default:
                                                                throw new IllegalStateException("Unexpected value: " + checkedId);
                                                        }*/
                                                    }
                                                });

                                                btn_subscribe.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        final DatabaseReference subscribeproductRef =FirebaseDatabase.getInstance().getReference().child("Location").child(subAdmin).child(subscribe)
                                                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("product").child(holder.iteam_name.getText().toString());


                                                        if(alert_sub_item_order_qty_txt.getText().toString().equals("ADD"))
                                                        {
                                                            Toast.makeText(getActivity().getApplicationContext(), "qty can not be empty", Toast.LENGTH_SHORT).show();
                                                        }
                                                        else if(txt_alert_start_date.getText().toString().equals("dd/mm/yyyy"))
                                                        {
                                                            Toast.makeText(getActivity().getApplicationContext(), "Start date can not be empty", Toast.LENGTH_SHORT).show();
                                                        }
                                                        else {

                                                           // String testamount =  holder.iteam_price.getText().toString()*Integer.valueOf(qtyofsub.getText().toString()));
                                                          //  Log.d("testamountcheck", "testamount "+ holder.iteam_price.getText().toString()+""qtyofsub.getText().toString());

                                                            HashMap<String, Object> submap = new HashMap<>();
                                                            submap.put("name", holder.iteam_name.getText().toString());
                                                            submap.put("qty", alert_sub_item_order_qty_txt.getText().toString());
                                                            submap.put("rate", holder.iteam_price.getText().toString());
                                                            submap.put("image", modelA.getImage());
                                                            submap.put("amount", String.valueOf(Integer.valueOf(holder.iteam_price.getText().toString())*Integer.valueOf(alert_sub_item_order_qty_txt.getText().toString())));
                                                            submap.put("quantity", holder.iteam_qty.getText().toString());
                                                            submap.put("timeOfdel",subscriptiondeliverytime);
                                                            submap.put("startdate",txt_alert_start_date.getText().toString());
                                                            subscribeproductRef.updateChildren(submap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if(task.isSuccessful())
                                                                    {
                                                                        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                            @Override
                                                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                final String name = dataSnapshot.child("name").getValue().toString();
                                                                                final String phoneNumber = dataSnapshot.child("phoneNumber").getValue().toString();
                                                                                String timeOfdel =subscriptiondeliverytime;
                                                                                final String Address = address.getText().toString();
                                                                                final String Pin= pin.getText().toString();
                                                                                if(Pin.equals(""))
                                                                                {
                                                                                    Toast.makeText(getActivity().getApplicationContext(), "Pin can not be empty", Toast.LENGTH_SHORT).show();
                                                                                    subscribeproductRef.removeValue();
                                                                                }
                                                                                else{
                                                                                    DatabaseReference adresspinRef=FirebaseDatabase.getInstance().getReference().child("serviceAreapin");
                                                                                    adresspinRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                        @Override
                                                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                            int flag =0;
                                                                                            for (DataSnapshot pin:dataSnapshot.getChildren())
                                                                                            {
                                                                                                Log.d("pin", "onDataChange: "+pin.getValue().toString());
                                                                                                if(Pin.equals(pin.getValue().toString()))
                                                                                                {
                                                                                                    //  Toast.makeText(BillingActivity.this, pin.getValue().toString(), Toast.LENGTH_SHORT).show();
                                                                                                    flag=1;
                                                                                                }
                                                                                            }
                                                                                            if(flag==1)
                                                                                            {
                                                                                                if(Address.equals(""))
                                                                                                {
                                                                                                    Toast.makeText(getActivity().getApplicationContext(), "Address can not be empty", Toast.LENGTH_SHORT).show();
                                                                                                }
                                                                                                else {

                                                                                                    final DatabaseReference subscribeRef = FirebaseDatabase.getInstance().getReference().child("Location").child(subAdmin).child(subscribe)
                                                                                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                                                                                    DatabaseReference subproduct = FirebaseDatabase.getInstance().getReference().child("Location").child(subAdmin).child(subscribe)
                                                                                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("product");
                                                                                                    subproduct.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                        @Override
                                                                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                                            subprValue ="";
                                                                                                            for (DataSnapshot product : dataSnapshot.getChildren()) {
                                                                                                                Log.d("snap", "onDataChange: " + product);
                                                                                                                String itmname = product.getKey();
                                                                                                                String itmqty = product.child("qty").getValue().toString();
                                                                                                                String itmrate = product.child("rate").getValue().toString();
                                                                                                                String itmamt = product.child("amount").getValue().toString();
                                                                                                                String timeOfdel = product.child("timeOfdel").getValue().toString();
                                                                                                                if(product.hasChild("value"))
                                                                                                                {
                                                                                                                    subprValue = product.child("value").getValue().toString();
                                                                                                                }
                                                                                                                subprValue = subprValue+"\n "+ itmname + "\nQty: " + itmqty + "\nrate: " + itmrate + "\nTotal: " + itmamt + "\n Time Of del: " + timeOfdel;

                                                                                                                HashMap<String,Object> submap= new HashMap<>();
                                                                                                                submap.put("name",name);
                                                                                                                submap.put("phoneNumber",phoneNumber);
                                                                                                                submap.put("timeOfdel",timeOfdel);
                                                                                                                submap.put("value",subprValue);
                                                                                                                submap.put("Address",Address);
                                                                                                                submap.put("pin",Pin);
                                                                                                                subscribeRef.updateChildren(submap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                                    @Override
                                                                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                                                                        if(task.isSuccessful())
                                                                                                                        {
                                                                                                                            Toast.makeText(getActivity().getApplicationContext(), "subscriptiona added", Toast.LENGTH_SHORT).show();
                                                                                                                            dialog.dismiss();
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
                                                                                            else{
                                                                                                Toast.makeText(getActivity().getApplicationContext(), "Sorry we do not serve at this location", Toast.LENGTH_SHORT).show();
                                                                                                subscribeproductRef.removeValue();
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
                                                                }
                                                            });

                                                        }

                                                    }
                                                });

                                            }

                                        });

                                    }

                                }

                                @NonNull
                                @Override
                                public Cooking_delightsFragment.iteam_list onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.iteamview_design, parent, false);
                                    Cooking_delightsFragment.iteam_list viewHolder = new Cooking_delightsFragment.iteam_list(view);
                                    return viewHolder;
                                }
                            };
                    firebaseRecyclerAdapter_iteam.startListening();
                    itemsRecycler.setAdapter(firebaseRecyclerAdapter_iteam);
                    itemsRecycler.getRecycledViewPool().setMaxRecycledViews(0, 0);


                }





            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    public static class subcategory_list extends RecyclerView.ViewHolder
    {
        TextView txt_search;
        CardView card_menu;


        public subcategory_list(@NonNull View itemView) {
            super(itemView);

            txt_search = itemView.findViewById(R.id.txt_search_menu);
            card_menu = itemView.findViewById(R.id.crd_search_menu);

        }
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