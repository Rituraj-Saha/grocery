package com.technolgiya.myapplication;


import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
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
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 */
public class SubscriptionFragment extends Fragment {


    public SubscriptionFragment() {
        // Required empty public constructor
    }
    private RecyclerView subrecycl;
    private DatabaseReference subproref;
    private RecyclerView weekly_sub_recycl;
    private RecyclerView daily_sub_recycl;
    private DatabaseReference subprorew;
    private DatabaseReference subprored;
    private Toolbar toolbar;
    private View parent_frame_layout_client;
    private LinearLayout lin_sub_parent;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_subscription, container, false);
        subproref = FirebaseDatabase.getInstance().getReference().child("Location").child("North 24 Parganas").child("subscribeM")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("product");
        subprorew = FirebaseDatabase.getInstance().getReference().child("Location").child("North 24 Parganas").child("subscribeW")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("product");
        subprored = FirebaseDatabase.getInstance().getReference().child("Location").child("North 24 Parganas").child("subscribeD")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("product");

        lin_sub_parent = view.findViewById(R.id.lin_sub_parent);
        lin_sub_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        parent_frame_layout_client = getActivity().findViewById(R.id.frame_layout_client);
        toolbar = getActivity().findViewById(R.id.toolbar_layout_client);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setTitle("My Subscription");

        subrecycl = view.findViewById(R.id.sub_recyclear);
        FirebaseRecyclerOptions<cart_model> options_sub_detail = null;
        FirebaseRecyclerOptions<cart_model> options_sub_detail_week = null;
        FirebaseRecyclerOptions<cart_model> options_sub_detail_daily = null;


        subrecycl.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

        weekly_sub_recycl = view.findViewById(R.id.weekly_sub_recycl);
        weekly_sub_recycl.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

        daily_sub_recycl = view.findViewById(R.id.daily_sub_recycl);
        daily_sub_recycl.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));


        return view;

    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<cart_model>options_sub_detail=null;
        FirebaseRecyclerOptions<cart_model> options_sub_detail_week = null;
        FirebaseRecyclerOptions<cart_model> options_sub_detail_daily = null;

        options_sub_detail =
                new FirebaseRecyclerOptions.Builder<cart_model>()
                        .setQuery(subproref, cart_model.class)
                        .build();

        options_sub_detail_week =
                new FirebaseRecyclerOptions.Builder<cart_model>()
                        .setQuery(subprorew, cart_model.class)
                        .build();

        options_sub_detail_daily = new FirebaseRecyclerOptions.Builder<cart_model>()
                .setQuery(subprored, cart_model.class)
                .build();


        FirebaseRecyclerAdapter<cart_model,SubscriptionFragment.subList> FirebaseRecyclearAdaptor =
                new FirebaseRecyclerAdapter<cart_model, subList>(options_sub_detail) {
            @Override
            protected void onBindViewHolder(@NonNull subList holder, int position, @NonNull final cart_model model) {
                holder.sub_history_item_name_txt.setText(model.getName());
                Picasso.get().load(model.getImage()).placeholder(R.drawable.ad).into(holder.sub_history_item_pic_img);
                holder.sub_history_qty_txt.setText(model.getQty());
                holder.sub_history_amount_txt.setText("\u20B9"+model.getRate());
                holder.sub_history_item_subhead_txt.setText(model.getQuantity());
                holder.sub_history_repetation_txt.setText("Monthly: "+model.getTimeOfdel());
                holder.sub_history_btn_unsubscribe.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                        alertDialogBuilder.setMessage("Are you sure you want to Unsubscribe the item?");
                        alertDialogBuilder.setPositiveButton("yes",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(final DialogInterface arg0, int arg1) {

                                        subproref = FirebaseDatabase.getInstance().getReference().child("Location").child("North 24 Parganas").child("subscribeM")
                                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("product");

                                        subproref.child(model.getName()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                if(task.isSuccessful())
                                                {
                                                    subproref = FirebaseDatabase.getInstance().getReference().child("Location").child("North 24 Parganas").child("subscribeM")
                                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("product");

                                                    subproref.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            if(!dataSnapshot.exists())
                                                            {
                                                                DatabaseReference subref =FirebaseDatabase.getInstance().getReference().child("Location").child("North 24 Parganas").child("subscribeM")
                                                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                                                subref.removeValue();

                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {

                                                        }
                                                    });
                                                    Toast.makeText(getActivity().getApplicationContext(), "Subcription monthly cancelled", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }
                                });

                        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                                // Toast.makeText(getActivity().getApplicationContext(), "clicked No", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        });

                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();

                    }
                });


            }

            @NonNull
            @Override
            public subList onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subscription_layout_design, parent, false);
                SubscriptionFragment.subList viewHolder = new SubscriptionFragment.subList(view);
                return viewHolder;
            }
        };
        FirebaseRecyclearAdaptor.startListening();
        subrecycl.setAdapter(FirebaseRecyclearAdaptor);


        FirebaseRecyclerAdapter<cart_model,SubscriptionFragment.subList> FirebaseRecyclearAdaptorweek =
                new FirebaseRecyclerAdapter<cart_model, subList>(options_sub_detail_week) {
                    @Override
                    protected void onBindViewHolder(@NonNull subList holder, int position, @NonNull final cart_model model) {
                        holder.sub_history_item_name_txt.setText(model.getName());
                        Picasso.get().load(model.getImage()).placeholder(R.drawable.ad).into(holder.sub_history_item_pic_img);
                        holder.sub_history_qty_txt.setText(model.getQty());
                        holder.sub_history_amount_txt.setText("\u20B9 "+model.getRate());
                        holder.sub_history_item_subhead_txt.setText(model.getQuantity());
                        holder.sub_history_repetation_txt.setText("Weekly: "+model.getTimeOfdel());
                        holder.sub_history_btn_unsubscribe.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {



                                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                                alertDialogBuilder.setMessage("Are you sure you want to Unsubscribe the item?");
                                alertDialogBuilder.setPositiveButton("yes",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(final DialogInterface arg0, int arg1) {

                                                subprorew = FirebaseDatabase.getInstance().getReference().child("Location").child("North 24 Parganas").child("subscribeW")
                                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("product");

                                                subprorew.child(model.getName()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {

                                                        if(task.isSuccessful())
                                                        {
                                                            subprorew = FirebaseDatabase.getInstance().getReference().child("Location").child("North 24 Parganas").child("subscribeW")
                                                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("product");

                                                            subprorew.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                                    if(!dataSnapshot.exists())
                                                                    {
                                                                        DatabaseReference subref =FirebaseDatabase.getInstance().getReference().child("Location").child("North 24 Parganas").child("subscribeW")
                                                                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                                                        subref.removeValue();
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

                                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {


                                        // Toast.makeText(getActivity().getApplicationContext(), "clicked No", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    }
                                });

                                AlertDialog alertDialog = alertDialogBuilder.create();
                                alertDialog.show();





                            }
                        });


                    }

                    @NonNull
                    @Override
                    public subList onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subscription_layout_design, parent, false);
                        SubscriptionFragment.subList viewHolder = new SubscriptionFragment.subList(view);
                        return viewHolder;
                    }
                };
        FirebaseRecyclearAdaptorweek.startListening();
        weekly_sub_recycl.setAdapter(FirebaseRecyclearAdaptorweek);


        FirebaseRecyclerAdapter<cart_model,SubscriptionFragment.subList> FirebaseRecyclearAdaptordaily =
                new FirebaseRecyclerAdapter<cart_model, subList>(options_sub_detail_daily) {
                    @Override
                    protected void onBindViewHolder(@NonNull subList holder, int position, @NonNull final cart_model model) {
                        holder.sub_history_item_name_txt.setText(model.getName());
                        Picasso.get().load(model.getImage()).placeholder(R.drawable.ad).into(holder.sub_history_item_pic_img);
                        holder.sub_history_qty_txt.setText(model.getQty());
                        holder.sub_history_amount_txt.setText("\u20B9 "+model.getRate());
                      holder.sub_history_item_subhead_txt.setText(model.getQuantity());
                      holder.sub_history_repetation_txt.setText("Daily: Everyday");

                      holder.sub_history_btn_unsubscribe.setOnClickListener(new View.OnClickListener() {
                          @Override
                          public void onClick(View v) {



                              final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                              alertDialogBuilder.setMessage("Are you sure you want to Unsubscribe the item?");
                              alertDialogBuilder.setPositiveButton("yes",
                                      new DialogInterface.OnClickListener() {
                                          @Override
                                          public void onClick(final DialogInterface arg0, int arg1) {

                                              subprored = FirebaseDatabase.getInstance().getReference().child("Location").child("North 24 Parganas").child("subscribeD")
                                                      .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("product");

                                              subprored.child(model.getName()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                  @Override
                                                  public void onComplete(@NonNull Task<Void> task) {

                                                      if(task.isSuccessful())
                                                      {
                                                          subprored = FirebaseDatabase.getInstance().getReference().child("Location").child("North 24 Parganas").child("subscribeD")
                                                                  .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("product");

                                                          subprorew.addListenerForSingleValueEvent(new ValueEventListener() {
                                                              @Override
                                                              public void onDataChange(DataSnapshot dataSnapshot) {
                                                                  if(!dataSnapshot.exists())
                                                                  {
                                                                      DatabaseReference subref =FirebaseDatabase.getInstance().getReference().child("Location").child("North 24 Parganas").child("subscribeD")
                                                                              .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                                                      subref.removeValue();
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

                              alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                  @Override
                                  public void onClick(DialogInterface dialog, int which) {


                                      // Toast.makeText(getActivity().getApplicationContext(), "clicked No", Toast.LENGTH_SHORT).show();
                                      dialog.dismiss();
                                  }
                              });

                              AlertDialog alertDialog = alertDialogBuilder.create();
                              alertDialog.show();




                          }
                      });


                    }

                    @NonNull
                    @Override
                    public subList onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subscription_layout_design, parent, false);
                        SubscriptionFragment.subList viewHolder = new SubscriptionFragment.subList(view);
                        return viewHolder;
                    }
                };
        FirebaseRecyclearAdaptordaily.startListening();
        daily_sub_recycl.setAdapter(FirebaseRecyclearAdaptordaily);






    }

    public static class subList extends RecyclerView.ViewHolder{

        LinearLayout parent_lin_sub;
        ImageView sub_history_item_pic_img;
        TextView sub_history_item_name_txt;
        TextView sub_history_qty_txt;
        TextView sub_history_amount_txt;
        TextView sub_history_item_subhead_txt;
        Button sub_history_btn_unsubscribe;
        TextView sub_history_repetation_txt;

//        TextView cart_order_qty;
  //      Button cart_order_minus;
    //    Button cart_order_plus;
      //  CardView cart_parent_card;
       // CheckBox checkbox;
        //TextView cart_subhead_txt;


        public subList(@NonNull View itemView) {
            super(itemView);

            parent_lin_sub = itemView.findViewById(R.id.parent_lin_sub);
            sub_history_item_pic_img = itemView.findViewById(R.id.sub_history_item_pic_img);
            sub_history_item_name_txt = itemView.findViewById(R.id.sub_history_item_name_txt);
            sub_history_qty_txt = itemView.findViewById(R.id.sub_history_qty_txt);
            sub_history_amount_txt = itemView.findViewById(R.id.sub_history_amount_txt);
            sub_history_item_subhead_txt=itemView.findViewById(R.id.sub_history_item_subhead_txt);
            sub_history_btn_unsubscribe = itemView.findViewById(R.id.sub_history_btn_unsubscribe);
            sub_history_repetation_txt = itemView.findViewById(R.id.sub_history_repetation_txt);

          //  cart_order_qty = itemView.findViewById(R.id.cart_order_qty_txt);
            //cart_order_minus = itemView.findViewById(R.id.cart_order_minus_btn);
            //cart_order_plus = itemView.findViewById(R.id.cart_order_plus_btn);
            //cart_parent_card = itemView.findViewById(R.id.cart_parent_card);
            //checkbox = itemView.findViewById(R.id.cart_checkbox);
            //cart_subhead_txt=itemView.findViewById(R.id.cart_subhead_txt);
        }
    }
}
