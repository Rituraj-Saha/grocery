package com.technolgiya.myapplication;


import android.content.DialogInterface;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import java.util.HashMap;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;


/**
 * A simple {@link Fragment} subclass.
 */
public class OrderhistoryFragment extends Fragment {


    public OrderhistoryFragment()
    {
        // Required empty public constructor
    }

    private RecyclerView recyclear_cart;
    private DatabaseReference userRef,cartRef,bill,order;
    private TextView nameTxt,phoneTxt,payAmount;
    //   private Button pay;
    private Toolbar toolbar;
    private int amount = 0;

    private View parent_frame_layout_client;

     FirebaseRecyclerAdapter firebaseRecyclearAdaptor;

    private int position_of_deleted_Iteam;

    private String returncause ="";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view= inflater.inflate(R.layout.fragment_orderhistory, container, false);
        nameTxt=view.findViewById(R.id.client_name_bill_txt);
        phoneTxt=view.findViewById(R.id.client_phone_bill_txt);

        //  payAmount = findViewById(R.id.client_net_pay_txt);
        //pay = findViewById(R.id.client_pay_btn);


        recyclear_cart = view.findViewById(R.id.recyclear_cart);
        FirebaseRecyclerOptions<order_model> options_cart_detail = null;

        //recyclear_cart.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

        parent_frame_layout_client = getActivity().findViewById(R.id.frame_layout_client);
        toolbar = getActivity().findViewById(R.id.toolbar_layout_client);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setTitle("Order History");


        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        recyclear_cart.setLayoutManager(mLayoutManager);



        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclear_cart);

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

                    bill = FirebaseDatabase.getInstance().getReference().child("Location").child(subAdmin).child("order");
                    Query query = bill.orderByKey();
                    options_cart_detail =
                            new FirebaseRecyclerOptions.Builder<order_model>()
                                    .setQuery(query, order_model.class)
                                    .build();

                    firebaseRecyclearAdaptor = new FirebaseRecyclerAdapter<order_model, cartList>(options_cart_detail) {
                                @Override
                                protected void onBindViewHolder(@NonNull final OrderhistoryFragment.cartList holder, final int position, @NonNull final order_model model) {


                                    String Uid = model.getUid();

                                    if(!Uid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))
                                    {
                                        holder.cart_parent_card.removeAllViews();
                                        holder.cart_parent_card.setVisibility(View.GONE);
                                        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams)holder.cart_parent_card.getLayoutParams();
                                        layoutParams.setMargins(0, 0, 0, 0);
                                        holder.cart_parent_card.setLayoutParams(layoutParams);


                                    }
                                    //if(model.getUid().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))
                                    else
                                    {
                                        if(model.getDeliverystatus().equals("delivered"))
                                        {
                                            holder.btn_delivered.setVisibility(View.VISIBLE);
                                        }
                                        else
                                        {
                                            holder.btn_delivered.setVisibility(View.GONE);
                                        }
                                        holder.cart_parent_card.setVisibility(View.VISIBLE);
                                        String time = getRef(position).getKey();
                                        holder.cart_name.setText(model.getName());
                                        holder.cart_phone.setText(model.getPhoneNumber());
                                        holder.cart_price.setText(model.getValue());
                                        holder.cart_address.setText(model.getAddress());
                                        holder.total.setText(model.getTotal());
                                        holder.pay_status.setText(model.getStatus());
                                        holder.btn_delivered.setText("Claim for Refund");
                                        holder.txt_time_bill.setText(time);

                                        DatabaseReference returnref = FirebaseDatabase.getInstance().getReference()
                                                .child("Location").child("North 24 Parganas").child("returnItemReq").child(getRef(position).getKey());

                                        returnref.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                if(dataSnapshot.exists())
                                                {
                                                    holder.btn_delivered.setVisibility(View.GONE);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });

                                        holder.btn_delivered.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                LayoutInflater layout = LayoutInflater.from(getActivity().getApplicationContext());
                                                final View dialogView = layout.inflate(R.layout.refund_alert, null);
                                                final AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();
                                                dialog.setView(dialogView);
                                                dialog.show();
                                                RadioGroup radio_grp_cause;
                                                RadioButton rb_damaged;
                                                RadioButton rb_quality;
                                                final TextView txt_bill_no;
                                                Button btn_proceed;
                                                Button btn_cancel;
                                                radio_grp_cause = dialogView.findViewById(R.id.radio_grp_cause);
                                                rb_damaged = dialogView.findViewById(R.id.rb_damaged);
                                                rb_quality = dialogView.findViewById(R.id.rb_quality);
                                                txt_bill_no = dialogView.findViewById(R.id.txt_bill_no);
                                                btn_proceed = dialogView.findViewById(R.id.btn_proceed);
                                                btn_cancel = dialogView.findViewById(R.id.btn_cancel);
                                                txt_bill_no.setText(getRef(position).getKey());

                                                radio_grp_cause.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                                    @Override
                                                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                                                        RadioButton choosen;
                                                        choosen = (RadioButton)dialogView.findViewById(checkedId);

                                                        if(checkedId == -1)
                                                        {
                                                            Toast.makeText(getActivity().getApplicationContext(), "Noting selected", Toast.LENGTH_SHORT).show();
                                                        }
                                                        else {
                                                            if(choosen.getText().toString().equals("Damaged"))
                                                            {
                                                                returncause = "Damaged";
                                                                Toast.makeText(getActivity().getApplicationContext(), ""+returncause, Toast.LENGTH_SHORT).show();
                                                            }
                                                            if(choosen.getText().toString().equals("Poor quality"))
                                                            {
                                                                returncause = "Poor quality";
                                                                Toast.makeText(getActivity().getApplicationContext(), ""+returncause, Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    }
                                                });
                                                btn_proceed.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        HashMap<String,Object> returnmap = new HashMap<>();
                                                        returnmap.put("Uid",model.getUid());
                                                        returnmap.put("returnstatus","applied");
                                                        returnmap.put("billno",txt_bill_no.getText().toString());
                                                        returnmap.put("value",model.getValue());
                                                        returnmap.put("name",model.getName());
                                                        returnmap.put("total",model.getTotal());
                                                        returnmap.put("phoneNumber",model.getPhoneNumber());
                                                        returnmap.put("address",model.getAddress());
                                                        returnmap.put("pin",model.getPin());
                                                        if(!returncause.equals(""))
                                                        {
                                                            returnmap.put("reason",returncause);

                                                        DatabaseReference returnref = FirebaseDatabase.getInstance().getReference()
                                                                .child("Location").child("North 24 Parganas").child("returnItemReq").child(getRef(position).getKey());
                                                        returnref.updateChildren(returnmap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {

                                                                if(task.isSuccessful())
                                                                {
                                                                    Toast.makeText(getActivity().getApplicationContext(), "Your request is registered.", Toast.LENGTH_SHORT).show();
                                                                    dialog.dismiss();
                                                                    OrderhistoryFragment orederhis = new OrderhistoryFragment();
                                                                    FragmentTransaction fragmentTransactionorederhis = getActivity().getSupportFragmentManager().beginTransaction();
                                                                    fragmentTransactionorederhis.replace(parent_frame_layout_client.getId(),orederhis);
                                                                    fragmentTransactionorederhis.addToBackStack(null);
                                                                    fragmentTransactionorederhis.commit();
                                                                }

                                                            }
                                                        });
                                                        }
                                                        else {
                                                            Toast.makeText(getActivity().getApplicationContext(), "reason can not be empty", Toast.LENGTH_SHORT).show();
                                                        }

                                                    }
                                                });


                                            }
                                        });


                                    }
                                   /* else {
                                       // holder.cart_parent_card.setVisibility(View.GONE);
                                      //  holder.parent_lint_hor.setVisibility(View.GONE);
                                        holder.cart_parent_card.removeAllViews();
                                     //   holder.cart_parent_lin.setVisibility(View.GONE);

                                      //  holder.cart_parent_card.setVisibility(View.GONE);
                                      //  RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams)holder.cart_parent_card.getLayoutParams();
                                      //  layoutParams.setMargins(0, 0, 0, 0);
                                      //  holder.cart_parent_card.setLayoutParams(layoutParams);


                                    }*/

                                }

                                @NonNull
                                @Override
                                public OrderhistoryFragment.cartList onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.billing_design_prev, parent, false);
                                    OrderhistoryFragment.cartList viewHolder = new OrderhistoryFragment.cartList(view);
                                    return viewHolder;

                                }


                              /*  public void deleteItem(int position)
                                {
                                    String time = getRef(position).getKey();
                                    Toast.makeText(getActivity().getApplicationContext(), ""+time, Toast.LENGTH_SHORT).show();
                                }*/


                            };

                    firebaseRecyclearAdaptor.startListening();
                    recyclear_cart.setAdapter(firebaseRecyclearAdaptor);
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
        LinearLayout parent_lint_hor;
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
            parent_lint_hor = itemView.findViewById(R.id.parent_lint_hor);
        }
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {


            if(direction == ItemTouchHelper.LEFT)
            {

                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                alertDialogBuilder.setMessage("Are you sure,You wanted to make decision");
                        alertDialogBuilder.setPositiveButton("yes",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(final DialogInterface arg0, int arg1) {
                                        int  position = viewHolder.getAdapterPosition();
                                        String rtime = firebaseRecyclearAdaptor.getRef(position).getKey();

                                        DatabaseReference orderhis = FirebaseDatabase.getInstance().getReference().child("Location").child("North 24 Parganas")
                                                .child("order").child(rtime);
                                        orderhis.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful())
                                                {
                                                    OrderhistoryFragment orederhis = new OrderhistoryFragment();
                                                    FragmentTransaction fragmentTransactionorederhis = getActivity().getSupportFragmentManager().beginTransaction();
                                                    fragmentTransactionorederhis.replace(parent_frame_layout_client.getId(),orederhis);
                                                    fragmentTransactionorederhis.addToBackStack(null);
                                                    fragmentTransactionorederhis.commit();
                                                    //Toast.makeText(getActivity().getApplicationContext(), "clicked No", Toast.LENGTH_SHORT).show();
                                                    arg0.dismiss();
                                                }
                                            }
                                        });

                                        Toast.makeText(getActivity().getApplicationContext(),"You clicked yes button",Toast.LENGTH_LONG).show();
                                    }
                                });

                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        OrderhistoryFragment orederhis = new OrderhistoryFragment();
                        FragmentTransaction fragmentTransactionorederhis = getActivity().getSupportFragmentManager().beginTransaction();
                        fragmentTransactionorederhis.replace(parent_frame_layout_client.getId(),orederhis);
                        fragmentTransactionorederhis.addToBackStack(null);
                        fragmentTransactionorederhis.commit();
                       // Toast.makeText(getActivity().getApplicationContext(), "clicked No", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

               // Toast.makeText(getActivity().getApplicationContext(), ""+firebaseRecyclearAdaptor.getRef(position).getKey(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(getContext(),R.color.colorRed))
                    .addSwipeLeftActionIcon(R.drawable.ic_delete_red_24dp)

                    .create()
                    .decorate();

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

}
