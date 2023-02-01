package com.technolgiya.myapplication;


import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class FreshFragment extends Fragment {


    public FreshFragment() {
        // Required empty public constructor
    }




    private Toolbar toolbar;
    private RecyclerView horizontalsearch;
    private RecyclerView itemsRecycler;
    private DatabaseReference userRef,fresh,product,cart;
    private String selector ="Exoticvegetable";
    private String subcategory = "";
    private String category="";
    private int temp=0;
    private int add =0;
    private int amount = 0;
    private  int iteam_amount=0;
    private EditText et_search;

    private View parent_frame_layout_client;
    private LinearLayout badge_cart;
    private TextView txt_badge_count;
    private DatabaseReference cartRef;
    int badgecount =0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_fresh, container, false);


        parent_frame_layout_client = getActivity().findViewById(R.id.frame_layout_client);
        toolbar = getActivity().findViewById(R.id.toolbar_layout_client);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setTitle("Fresh");

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

        badge_cart = getActivity().findViewById(R.id.badge_cart);
        txt_badge_count= getActivity().findViewById(R.id.txt_badge_count);
        cartRef = FirebaseDatabase.getInstance().getReference().child("Location").child("North 24 Parganas")
                .child("cart").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        cartRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                txt_badge_count.setText(String.valueOf(dataSnapshot.getChildrenCount()));

                if(dataSnapshot.exists())
                {
                    badgecount = 0;
                    for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren())
                    {
                        badgecount= badgecount+1;
                        txt_badge_count.setText(String.valueOf(badgecount));
                    }
                }
                else
                {
                    badge_cart.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

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

        badge();
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists()) {
                    final String subAdmin = dataSnapshot.child("subAdmin").getValue().toString();


                    fresh = FirebaseDatabase.getInstance().getReference().child("Location").child(subAdmin).child("Category")
                            .child("Fresh");

                    product = FirebaseDatabase.getInstance().getReference().child("Location").child(subAdmin).child("product");

                    FirebaseRecyclerOptions<fresh_model> options = null;

                    options =
                            new FirebaseRecyclerOptions.Builder<fresh_model>()
                                    .setQuery(fresh, fresh_model.class)
                                    .build();
                   // Toast.makeText(getActivity(), selector, Toast.LENGTH_SHORT).show();

                    final FirebaseRecyclerAdapter<fresh_model, FreshFragment.subcategory_list> firebaseRecyclerAdapter =
                            new FirebaseRecyclerAdapter<fresh_model, FreshFragment.subcategory_list>(options) {


                                @Override
                                protected void onBindViewHolder(@NonNull final FreshFragment.subcategory_list holder,
                                                                final int position, @NonNull final fresh_model model) {
                                    holder.txt_search.setText(model.getName());


                                    holder.txt_search.setBackgroundColor(temp == position ? Color.parseColor("#689f39") : Color.TRANSPARENT);
                                    holder.txt_search.setTextColor(temp == position ? Color.parseColor("#ffffff"):Color.parseColor("#000000"));

                                    Log.d("selector", "onClick: "+selector);
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
                                            //Toast.makeText(getActivity(), selector, Toast.LENGTH_SHORT).show();
                                            Log.d("selector", "onClick: "+selector);


                                            FirebaseRecyclerOptions<iteam_model> options_item_detail = null;


                                            options_item_detail =
                                                    new FirebaseRecyclerOptions.Builder<iteam_model>()
                                                            .setQuery(product, iteam_model.class)
                                                            .build();

                                            final FirebaseRecyclerAdapter<iteam_model, FreshFragment.iteam_list> firebaseRecyclerAdapter_iteam =
                                                    new FirebaseRecyclerAdapter<iteam_model, FreshFragment.iteam_list>(options_item_detail) {

                                                        @Override
                                                        protected void onBindViewHolder(@NonNull final FreshFragment.iteam_list holder,
                                                                                        final int position, @NonNull final iteam_model modelA) {

                                                            subcategory = modelA.getSubcategory();
                                                            category = modelA.getCategory();
                                                            if(!category.equals("Fresh"))
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

                                                                            cart = FirebaseDatabase.getInstance().getReference().child("Location").child(subAdmin).child("cart")
                                                                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

                                                                            if(holder.iteam_order_qty.getText().toString().equals("ADD"))
                                                                                add=0;
                                                                            else
                                                                                add = Integer.valueOf(holder.iteam_order_qty.getText().toString());
                                                                            holder.iteam_order_qty.setText(String.valueOf(++add));
                                                                            amount = amount+ Integer.valueOf(holder.iteam_price.getText().toString());

                                                                            iteam_amount=0;
                                                                            cart.child(holder.iteam_name.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                @Override
                                                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                    if(dataSnapshot.exists())
                                                                                    {
                                                                                        iteam_amount = Integer.valueOf(dataSnapshot.child("amount").getValue().toString());
                                                                                        iteam_amount =iteam_amount+Integer.valueOf(holder.iteam_price.getText().toString());
                                                                                        Toast.makeText(getActivity(), ""+iteam_amount, Toast.LENGTH_SHORT).show();

                                                                                        HashMap<String,Object>cartMap=new HashMap<>();
                                                                                        cartMap.put("name",holder.iteam_name.getText().toString());
                                                                                        cartMap.put("qty",holder.iteam_order_qty.getText().toString());
                                                                                        cartMap.put("rate",holder.iteam_price.getText().toString());
                                                                                        cartMap.put("image",modelA.getImage());
                                                                                        cartMap.put("amount",String.valueOf(iteam_amount));
                                                                                        cartMap.put("quantity",holder.iteam_qty.getText().toString());
                                                                                        cart.child(holder.iteam_name.getText().toString()).updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                            @Override
                                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                                if(task.isSuccessful())
                                                                                                {
                                                                                                    Toast.makeText(getActivity(), "data updated to cart", Toast.LENGTH_SHORT).show();
                                                                                                }
                                                                                            }
                                                                                        });
                                                                                        badge();
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
                                                                                        cart.child(holder.iteam_name.getText().toString()).updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                            @Override
                                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                                if(task.isSuccessful())
                                                                                                {
                                                                                                    Toast.makeText(getActivity(), "data updated to cart", Toast.LENGTH_SHORT).show();
                                                                                                }
                                                                                            }
                                                                                        });
                                                                                        badge();
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

                                                                            cart = FirebaseDatabase.getInstance().getReference().child("Location").child(subAdmin).child("cart")
                                                                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

                                                                            if(holder.iteam_order_qty.getText().toString().equals("ADD"))
                                                                                add=0;
                                                                            else
                                                                                add = Integer.valueOf(holder.iteam_order_qty.getText().toString());
                                                                            holder.iteam_order_qty.setText(String.valueOf(++add));
                                                                            amount = amount+ Integer.valueOf(holder.iteam_price.getText().toString());

                                                                            iteam_amount=0;
                                                                            cart.child(holder.iteam_name.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                @Override
                                                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                    if(dataSnapshot.exists())
                                                                                    {
                                                                                        iteam_amount = Integer.valueOf(dataSnapshot.child("amount").getValue().toString());
                                                                                        iteam_amount =iteam_amount+Integer.valueOf(holder.iteam_price.getText().toString());
                                                                                        Toast.makeText(getActivity(), ""+iteam_amount, Toast.LENGTH_SHORT).show();

                                                                                        HashMap<String,Object>cartMap=new HashMap<>();
                                                                                        cartMap.put("name",holder.iteam_name.getText().toString());
                                                                                        cartMap.put("qty",holder.iteam_order_qty.getText().toString());
                                                                                        cartMap.put("rate",holder.iteam_price.getText().toString());
                                                                                        cartMap.put("image",modelA.getImage());
                                                                                        cartMap.put("amount",String.valueOf(iteam_amount));
                                                                                        cartMap.put("quantity",holder.iteam_qty.getText().toString());
                                                                                        Log.d("Img", "onDataChange: "+model.getImage());

                                                                                        cart.child(holder.iteam_name.getText().toString()).updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                            @Override
                                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                                if(task.isSuccessful())
                                                                                                {
                                                                                                    Toast.makeText(getActivity(), "data updated to cart", Toast.LENGTH_SHORT).show();
                                                                                                }
                                                                                            }
                                                                                        });
                                                                                        badge();
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
                                                                                        Log.d("Img", "onDataChange: "+model.getImage());

                                                                                        cart.child(holder.iteam_name.getText().toString()).updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                            @Override
                                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                                if(task.isSuccessful())
                                                                                                {
                                                                                                    Toast.makeText(getActivity(), "data updated to cart", Toast.LENGTH_SHORT).show();
                                                                                                }
                                                                                            }
                                                                                        });
                                                                                        badge();
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
                                                                                    cart.child(holder.iteam_name.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                        @Override
                                                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                            if (dataSnapshot.exists()) {
                                                                                                iteam_amount = Integer.valueOf(dataSnapshot.child("amount").getValue().toString());
                                                                                                iteam_amount = iteam_amount - Integer.valueOf(holder.iteam_price.getText().toString());
                                                                                                Toast.makeText(getActivity(), "" + iteam_amount, Toast.LENGTH_SHORT).show();

                                                                                                HashMap<String, Object> cartMap = new HashMap<>();
                                                                                                cartMap.put("name", holder.iteam_name.getText().toString());
                                                                                                cartMap.put("qty", holder.iteam_order_qty.getText().toString());
                                                                                                cartMap.put("rate", holder.iteam_price.getText().toString());
                                                                                                cartMap.put("image", modelA.getImage());
                                                                                                cartMap.put("amount",String.valueOf(iteam_amount));
                                                                                                cartMap.put("quantity",holder.iteam_qty.getText().toString());

                                                                                                cart.child(holder.iteam_name.getText().toString()).updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                    @Override
                                                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                                                        if (task.isSuccessful()) {
                                                                                                            Toast.makeText(getActivity(), "data updated to cart", Toast.LENGTH_SHORT).show();
                                                                                                        }
                                                                                                    }
                                                                                                });
                                                                                                badge();
                                                                                            } else {
                                                                                                HashMap<String, Object> cartMap = new HashMap<>();
                                                                                                cartMap.put("name", holder.iteam_name.getText().toString());
                                                                                                cartMap.put("qty", holder.iteam_order_qty.getText().toString());
                                                                                                cartMap.put("rate", holder.iteam_price.getText().toString());
                                                                                                cartMap.put("image", modelA.getImage());
                                                                                                cartMap.put("amount", holder.iteam_price.getText().toString());
                                                                                                cartMap.put("quantity",holder.iteam_qty.getText().toString());
                                                                                                cart.child(holder.iteam_name.getText().toString()).updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                    @Override
                                                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                                                        if (task.isSuccessful()) {
                                                                                                            Toast.makeText(getActivity(), "data updated to cart", Toast.LENGTH_SHORT).show();
                                                                                                        }
                                                                                                    }
                                                                                                });
                                                                                                badge();
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

                                                                }
                                                            }
                                                        }

                                                        @NonNull
                                                        @Override
                                                        public FreshFragment.iteam_list onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                                                            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.iteamview_design, parent, false);
                                                            FreshFragment.iteam_list viewHolder = new FreshFragment.iteam_list(view);
                                                            return viewHolder;
                                                        }
                                                    };
                                            firebaseRecyclerAdapter_iteam.startListening();
                                            itemsRecycler.setAdapter(firebaseRecyclerAdapter_iteam);

                                        }
                                    });
                                }

                                @NonNull
                                @Override
                                public FreshFragment.subcategory_list onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_search_design, parent, false);
                                    FreshFragment.subcategory_list viewHolder = new FreshFragment.subcategory_list(view);
                                    return viewHolder;
                                }
                            };
                    firebaseRecyclerAdapter.startListening();
                    horizontalsearch.setAdapter(firebaseRecyclerAdapter);




                    FirebaseRecyclerOptions<iteam_model> options_item_detail = null;


                    options_item_detail =
                            new FirebaseRecyclerOptions.Builder<iteam_model>()
                                    .setQuery(product, iteam_model.class)
                                    .build();


                    final FirebaseRecyclerAdapter<iteam_model, FreshFragment.iteam_list> firebaseRecyclerAdapter_iteam =
                            new FirebaseRecyclerAdapter<iteam_model, FreshFragment.iteam_list>(options_item_detail) {

                                @Override
                                protected void onBindViewHolder(@NonNull final FreshFragment.iteam_list holder,
                                                                final int position, @NonNull final iteam_model modelA) {

                                    category = modelA.getCategory();
                                    if(!category.equals("Fresh"))
                                    {
                                        holder.parent_card.setVisibility(View.GONE);
                                        holder.parent_lin.setVisibility(View.GONE);
                                        holder.parent_lin.setLayoutParams(new LinearLayout.LayoutParams(0,0));

                                        holder.parent_card.setVisibility(View.GONE);
                                        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams)holder.parent_card.getLayoutParams();
                                        layoutParams.setMargins(0, 0, 0, 0);
                                        holder.parent_card.setLayoutParams(layoutParams);

                                    }

                                  //  Toast.makeText(getContext(), selector, Toast.LENGTH_SHORT).show();
                                    if(selector.equals("Exoticvegetable")) {

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
                                                cart = FirebaseDatabase.getInstance().getReference().child("Location").child(subAdmin).child("cart")
                                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

                                                if(holder.iteam_order_qty.getText().toString().equals("ADD"))
                                                    add=0;
                                                else
                                                    add = Integer.valueOf(holder.iteam_order_qty.getText().toString());
                                                holder.iteam_order_qty.setText(String.valueOf(++add));
                                                amount = amount+ Integer.valueOf(holder.iteam_price.getText().toString());

                                                iteam_amount=0;
                                                cart.child(holder.iteam_name.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        if(dataSnapshot.exists())
                                                        {
                                                            iteam_amount = Integer.valueOf(dataSnapshot.child("amount").getValue().toString());
                                                            iteam_amount =iteam_amount+Integer.valueOf(holder.iteam_price.getText().toString());
                                                            Toast.makeText(getActivity(), ""+iteam_amount, Toast.LENGTH_SHORT).show();

                                                            HashMap<String,Object>cartMap=new HashMap<>();
                                                            cartMap.put("name",holder.iteam_name.getText().toString());
                                                            cartMap.put("qty",holder.iteam_order_qty.getText().toString());
                                                            cartMap.put("rate",holder.iteam_price.getText().toString());
                                                            cartMap.put("image",modelA.getImage());
                                                            cartMap.put("amount",String.valueOf(iteam_amount));
                                                            cartMap.put("quantity",holder.iteam_qty.getText().toString());

                                                            cart.child(holder.iteam_name.getText().toString()).updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if(task.isSuccessful())
                                                                    {
                                                                        Toast.makeText(getActivity(), "data updated to cart", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }
                                                            });
                                                            badge();
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

                                                            cart.child(holder.iteam_name.getText().toString()).updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if(task.isSuccessful())
                                                                    {
                                                                        Toast.makeText(getActivity(), "data updated to cart", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }
                                                            });
                                                            badge();
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
                                                cart = FirebaseDatabase.getInstance().getReference().child("Location").child(subAdmin).child("cart")
                                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

                                                if(holder.iteam_order_qty.getText().toString().equals("ADD"))
                                                    add=0;
                                                else
                                                    add = Integer.valueOf(holder.iteam_order_qty.getText().toString());
                                                holder.iteam_order_qty.setText(String.valueOf(++add));
                                                amount = amount+ Integer.valueOf(holder.iteam_price.getText().toString());

                                                iteam_amount=0;
                                                cart.child(holder.iteam_name.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        if(dataSnapshot.exists())
                                                        {
                                                            iteam_amount = Integer.valueOf(dataSnapshot.child("amount").getValue().toString());
                                                            iteam_amount =iteam_amount+Integer.valueOf(holder.iteam_price.getText().toString());
                                                            Toast.makeText(getActivity(), ""+iteam_amount, Toast.LENGTH_SHORT).show();

                                                            HashMap<String,Object>cartMap=new HashMap<>();
                                                            cartMap.put("name",holder.iteam_name.getText().toString());
                                                            cartMap.put("qty",holder.iteam_order_qty.getText().toString());
                                                            cartMap.put("rate",holder.iteam_price.getText().toString());
                                                            cartMap.put("image",modelA.getImage());
                                                            cartMap.put("amount",String.valueOf(iteam_amount));
                                                            cartMap.put("quantity",holder.iteam_qty.getText().toString());

                                                            cart.child(holder.iteam_name.getText().toString()).updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if(task.isSuccessful())
                                                                    {
                                                                        Toast.makeText(getActivity(), "data updated to cart", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }
                                                            });
                                                            badge();
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

                                                            cart.child(holder.iteam_name.getText().toString()).updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if(task.isSuccessful())
                                                                    {
                                                                        Toast.makeText(getActivity(), "data updated to cart", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }
                                                            });
                                                            badge();
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
                                                        cart.child(holder.iteam_name.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                                if (dataSnapshot.exists()) {
                                                                    iteam_amount = Integer.valueOf(dataSnapshot.child("amount").getValue().toString());
                                                                    iteam_amount = iteam_amount - Integer.valueOf(holder.iteam_price.getText().toString());
                                                                    Toast.makeText(getActivity(), "" + iteam_amount, Toast.LENGTH_SHORT).show();

                                                                    HashMap<String, Object> cartMap = new HashMap<>();
                                                                    cartMap.put("name", holder.iteam_name.getText().toString());
                                                                    cartMap.put("qty", holder.iteam_order_qty.getText().toString());
                                                                    cartMap.put("rate", holder.iteam_price.getText().toString());
                                                                    cartMap.put("image", modelA.getImage());
                                                                    cartMap.put("amount", String.valueOf(iteam_amount));
                                                                    cartMap.put("quantity",holder.iteam_qty.getText().toString());

                                                                    cart.child(holder.iteam_name.getText().toString()).updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                            if (task.isSuccessful()) {
                                                                                Toast.makeText(getActivity(), "data updated to cart", Toast.LENGTH_SHORT).show();
                                                                            }
                                                                        }
                                                                    });
                                                                    badge();
                                                                } else {
                                                                    HashMap<String, Object> cartMap = new HashMap<>();
                                                                    cartMap.put("name", holder.iteam_name.getText().toString());
                                                                    cartMap.put("qty", holder.iteam_order_qty.getText().toString());
                                                                    cartMap.put("rate", holder.iteam_price.getText().toString());
                                                                    cartMap.put("image", modelA.getImage());
                                                                    cartMap.put("amount", holder.iteam_price.getText().toString());
                                                                    cartMap.put("quantity",holder.iteam_qty.getText().toString());

                                                                    cart.child(holder.iteam_name.getText().toString()).updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                            if (task.isSuccessful()) {
                                                                                Toast.makeText(getActivity(), "data updated to cart", Toast.LENGTH_SHORT).show();
                                                                            }
                                                                        }
                                                                    });
                                                                    badge();
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
                                    }





                                }

                                @NonNull
                                @Override
                                public FreshFragment.iteam_list onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.iteamview_design, parent, false);
                                    FreshFragment.iteam_list viewHolder = new FreshFragment.iteam_list(view);
                                    return viewHolder;
                                }
                            };
                    firebaseRecyclerAdapter_iteam.startListening();
                    itemsRecycler.setAdapter(firebaseRecyclerAdapter_iteam);

                }





            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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
        TextView txt_search;
        CardView crd_search_menu;
       // LinearLayout lin_search_menu;


        public subcategory_list(@NonNull View itemView) {
            super(itemView);

            txt_search = itemView.findViewById(R.id.txt_search_menu);
            crd_search_menu = itemView.findViewById(R.id.crd_search_menu);
           // lin_search_menu = itemView.findViewById(R.id.lin_search_menu);

        }
    }

    public static class iteam_list extends RecyclerView.ViewHolder
    {
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
            btn_add_btn.setClipToOutline(true);
        }
    }
    public void badge()
    {
       // Toast.makeText(getActivity().getApplicationContext(), "badge called", Toast.LENGTH_SHORT).show();
        cartRef = FirebaseDatabase.getInstance().getReference().child("Location").child("North 24 Parganas")
                .child("cart").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
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



}
