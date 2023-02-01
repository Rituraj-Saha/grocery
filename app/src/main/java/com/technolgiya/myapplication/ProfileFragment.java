package com.technolgiya.myapplication;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
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

import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {


    public ProfileFragment() {
        // Required empty public constructor
    }

    private TextView txt_profile_name;
    private TextView txt_profile_email;
    private TextView txt_profile_phone;
    private ImageView img_edit_profile;
    private TextView txt_address_profile;
    private TextView txt_order_history;
    private TextView txt_my_order;
    private TextView txt_my_rating;
    private FrameLayout parent_frame_layout_client;
    private EditText et_search;
    private TextView txt_p;
    private TextView txt_my_wallet;
    private TextView wallet_amount_txt;

    private TextView txt_faq_us;

    private TextView txt_my_coin;
   // private TextView txt_my_coin_amount;

    private DatabaseReference userref ;

    private FrameLayout profile_fragment;
    private TextView txt_my_subscription;
    private Toolbar toolbar;

    private int oredrcount = 0;
    private int billcount = 0;
    private int subscriptioncount = 0;
   // private View

    private TextView orderhistory_count_txt;
    private TextView order_count_txt;
    private TextView sub_count_txt;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view =inflater.inflate(R.layout.fragment_profile, container, false);
        txt_profile_name = view.findViewById(R.id.txt_profile_name);
        txt_profile_email = view.findViewById(R.id.txt_profile_email);
        txt_profile_phone=view.findViewById(R.id.txt_profile_phone);
        txt_address_profile =view.findViewById(R.id.txt_address_profile);
        txt_order_history = view.findViewById(R.id.txt_order_history);
        txt_my_order = view.findViewById(R.id.txt_my_order);
        txt_my_rating = view.findViewById(R.id.txt_my_rating);
        img_edit_profile = view.findViewById(R.id.img_edit_profile);
        parent_frame_layout_client = getActivity().findViewById(R.id.frame_layout_client);
        txt_my_coin = view.findViewById(R.id.txt_my_coin);

        txt_faq_us = view.findViewById(R.id.txt_faq_us);

        orderhistory_count_txt = view.findViewById(R.id.orderhistory_count_txt);
        order_count_txt = view.findViewById(R.id.order_count_txt);
        sub_count_txt = view.findViewById(R.id.sub_count_txt);

        txt_my_subscription = view.findViewById(R.id.txt_my_subscription);

        profile_fragment = view.findViewById(R.id.profile_fragment);
        profile_fragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        wallet_amount_txt = view.findViewById(R.id.wallet_amount_txt);
       // txt_my_coin_amount = view.findViewById(R.id.txt_my_coin_amount);



       // TextView txt_p=view.findViewById(R.id.txt_p);

       /* txt_p.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/

        parent_frame_layout_client = getActivity().findViewById(R.id.frame_layout_client);
        toolbar = getActivity().findViewById(R.id.toolbar_layout_client);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setTitle("My Profile");


        txt_my_wallet=view.findViewById(R.id.txt_my_wallet);

        txt_my_wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(),AddMoneytoWallet.class);
                startActivity(intent);
            }
        });

        txt_my_subscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SubscriptionFragment fragobjsub = new SubscriptionFragment();
                FragmentTransaction fragmentTransactionsub = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransactionsub.replace(parent_frame_layout_client.getId(),fragobjsub);
                fragmentTransactionsub.addToBackStack(null);
                fragmentTransactionsub.commit();

            }
        });

        txt_faq_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FaqFragment fragfaq = new FaqFragment();
                FragmentTransaction fragfaqtrans = getActivity().getSupportFragmentManager().beginTransaction();
                fragfaqtrans.replace(parent_frame_layout_client.getId(),fragfaq);
                fragfaqtrans.addToBackStack(null);
                fragfaqtrans.commit();

            }
        });

        userref = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        userref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    String name = dataSnapshot.child("name").getValue().toString();
                    String phone = dataSnapshot.child("phoneNumber").getValue().toString();
                    String address = dataSnapshot.child("Address").getValue().toString();
                    String email = dataSnapshot.child("email").getValue().toString();

                    if(dataSnapshot.child("coin").exists())
                    {
                       // txt_my_coin_amount.setText(dataSnapshot.child("coin").getValue().toString());
                    }
                    if(dataSnapshot.child("wallet").exists())
                    {
                        wallet_amount_txt.setText("\u20B9 "+dataSnapshot.child("wallet").getValue().toString());
                    }

                    txt_profile_name.setText(name);
                    txt_profile_phone.setText(phone);
                    txt_address_profile.setText(address);
                    txt_profile_email.setText(email);

                    txt_order_history.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            OrderhistoryFragment orederhis = new OrderhistoryFragment();
                            FragmentTransaction fragmentTransactionorederhis = getActivity().getSupportFragmentManager().beginTransaction();
                            fragmentTransactionorederhis.replace(parent_frame_layout_client.getId(),orederhis);
                            fragmentTransactionorederhis.addToBackStack(null);
                            fragmentTransactionorederhis.commit();
                        }
                    });

                    txt_my_order.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            cancelorder cancelobj = new cancelorder();
                            FragmentTransaction fragmentTransaction_cancel = getActivity().getSupportFragmentManager().beginTransaction();
                            fragmentTransaction_cancel.replace(parent_frame_layout_client.getId(),cancelobj);
                            fragmentTransaction_cancel.addToBackStack(null);
                            fragmentTransaction_cancel.commit();

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        img_edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists())
                        {

                           /* String phone = dataSnapshot.child("phoneNumber").getValue().toString();

                            Intent profile = new Intent(getActivity(),ProfileActivity.class);
                            profile.putExtra("phoneNumber",phone);
                            profile.putExtra("flag","edit");
                            startActivity(profile);
                            getActivity().finish();*/

                            LayoutInflater layout = LayoutInflater.from(getContext());
                            View dialogView = layout.inflate(R.layout.profileeditalert, null);
                            final AlertDialog dialog = new AlertDialog.Builder(getContext()).create();
                            dialog.setView(dialogView);
                            dialog.show();

                            final EditText et_profile_name = dialogView.findViewById(R.id.et_profile_name);
                            final EditText et_profile_email = dialogView.findViewById(R.id.et_profile_email);
                            final EditText txt_address_profile = dialogView.findViewById(R.id.txt_address_profile);
                            final EditText et_pin = dialogView.findViewById(R.id.et_pin);
                            Button btn_cancel_changes = dialogView.findViewById(R.id.btn_cancel_changes);
                            Button btn_confirm_changes = dialogView.findViewById(R.id.btn_confirm_changes);


                            String name =dataSnapshot.child("name").getValue().toString();
                            String email = dataSnapshot.child("email").getValue().toString();
                            String Address = dataSnapshot.child("Address").getValue().toString();
                            String pincode = dataSnapshot.child("locationbutton").getValue().toString();

                            et_profile_name.setText(name);
                            et_profile_email.setText(email);
                            et_pin.setText(pincode);
                            txt_address_profile.setText(Address);

                            btn_confirm_changes.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                                    alertDialogBuilder.setMessage("Are you sure?");

                                    alertDialogBuilder.setPositiveButton("yes",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface arg0, int arg1) {

                                                    final HashMap<String, Object> profile_update_map = new HashMap<>();
                                                    profile_update_map.put("name", et_profile_name.getText().toString());
                                                    profile_update_map.put("email",et_profile_email.getText().toString());
                                                    profile_update_map.put("Address",txt_address_profile.getText().toString());
                                                    profile_update_map.put("locationbutton",et_pin.getText().toString());
                                                    userref.updateChildren(profile_update_map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            DatabaseReference locationUser = FirebaseDatabase.getInstance().getReference().child("Location")
                                                                    .child("North 24 Parganas").child("Client")
                                                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                                            locationUser.updateChildren(profile_update_map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if(task.isSuccessful())
                                                                    {
                                                                        Toast.makeText(getActivity().getApplicationContext(), "Your profile is successfully updated", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }
                                                            });

                                                        }
                                                    });



                                                }
                                            });

                                    alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });

                                    AlertDialog alertDialog = alertDialogBuilder.create();
                                    alertDialog.show();


                                }
                            });

                            btn_cancel_changes.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });



                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });

        txt_my_coin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userref = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                userref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(final DataSnapshot dataSnapshot) {
                        if(dataSnapshot.child("coin").exists())
                        {
                            LayoutInflater layout = LayoutInflater.from(getActivity().getApplicationContext());
                            final View dialogView = layout.inflate(R.layout.coin_redeem_alert, null);
                            final AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();
                            dialog.setView(dialogView);
                            final TextView collected_coin = dialogView.findViewById(R.id.collected_coin);
                            Button btn_reddem = dialogView.findViewById(R.id.btn_reddem);
                           // Button btn_cancel = dialogView.findViewById(R.id.btn_cancel);
                          /*  btn_cancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });*/

                            dialog.show();
                            String existingcoin = dataSnapshot.child("coin").getValue().toString();
                            collected_coin.setText(existingcoin + " coins");

                            btn_reddem.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    final String existingcoin = dataSnapshot.child("coin").getValue().toString();

                                    HashMap<String,Object> reddemmap = new HashMap<>();
                                    reddemmap.put("wallet",String.valueOf(Integer.valueOf(existingcoin)+
                                            Integer.valueOf(dataSnapshot.child("wallet").getValue().toString())));
                                    userref.updateChildren(reddemmap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if(task.isSuccessful())
                                            {
                                                HashMap<String ,Object> coinreducemap = new HashMap<>();
                                                coinreducemap.put("coin","0");
                                                userref.updateChildren(coinreducemap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful())
                                                        {
                                                            userref.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                                    collected_coin.setText(dataSnapshot.child("coin").getValue().toString());
                                                                }

                                                                @Override
                                                                public void onCancelled(DatabaseError databaseError) {

                                                                }
                                                            });

                                                            // txt_my_coin_amount.setText(dataSnapshot.child("coin").getValue().toString());
                                                            Toast.makeText(getActivity().getApplicationContext(), "Reddemed Succesfully", Toast.LENGTH_SHORT).show();
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

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });

     /*   new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                orderhisCount();
        orderhistory_count_txt.setText(""+oredrcount);
            }
        }, 2000);*/
        orderhisCount();
        myordercount();
        subscriptioncounter();

        return view;
    }
    void orderhisCount(){
        oredrcount = 0;
        DatabaseReference oreder = FirebaseDatabase.getInstance().getReference().child("Location").child("North 24 Parganas")
                .child("order");

        oreder.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot order: dataSnapshot.getChildren())
                {
                    if(order.child("Uid").getValue().toString().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        Log.d("test", "onDataChange: "+order.child("Uid").getValue().toString()+oredrcount);
                        oredrcount++;
                    }
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                       // orderhisCount();
                        orderhistory_count_txt.setText(""+oredrcount);
                    }
                }, 2000);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    void myordercount()
    {
        billcount = 0;
        DatabaseReference oreder = FirebaseDatabase.getInstance().getReference().child("Location").child("North 24 Parganas")
                .child("bill");

        oreder.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot order: dataSnapshot.getChildren())
                {
                    if(order.child("Uid").getValue().toString().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        Log.d("test", "onDataChange: "+order.child("Uid").getValue().toString()+billcount);
                        billcount++;
                    }
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        // orderhisCount();
                        order_count_txt.setText(""+billcount);
                    }
                }, 1000);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    void subscriptioncounter()
    {
        subscriptioncount =0;
        final DatabaseReference subM = FirebaseDatabase.getInstance().getReference().child("Location").child("North 24 Parganas")
                .child("subscribeM").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("product");
        subM.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    long sub =dataSnapshot.getChildrenCount();
                    subscriptioncount = Integer.valueOf((int) sub);
                    sub_count_txt.setText(""+subscriptioncount);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        final DatabaseReference subW = FirebaseDatabase.getInstance().getReference().child("Location").child("North 24 Parganas")
                .child("subscribeW").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("product");
        subW.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    long sub =dataSnapshot.getChildrenCount();
                    subscriptioncount = subscriptioncount+Integer.valueOf((int) sub);
                    sub_count_txt.setText(""+subscriptioncount);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        final DatabaseReference subD = FirebaseDatabase.getInstance().getReference().child("Location").child("North 24 Parganas")
                .child("subscribeD").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("product");
        subD.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    long sub =dataSnapshot.getChildrenCount();
                    subscriptioncount = subscriptioncount+Integer.valueOf((int) sub);
                    sub_count_txt.setText(""+subscriptioncount);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}