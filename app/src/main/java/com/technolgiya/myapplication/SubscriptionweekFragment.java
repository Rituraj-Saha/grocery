package com.technolgiya.myapplication;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
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

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 */
public class SubscriptionweekFragment extends Fragment {


    public SubscriptionweekFragment() {
        // Required empty public constructor
    }
    private RecyclerView subrecycl;
    private DatabaseReference subproref;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_subscriptionweek, container, false);
        subproref = FirebaseDatabase.getInstance().getReference().child("Location").child("North 24 Parganas").child("subscribeW")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("product");

        subrecycl = view.findViewById(R.id.sub_recyclear);
        FirebaseRecyclerOptions<cart_model> options_sub_detail = null;
        subrecycl.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<cart_model>options_sub_detail=null;

        options_sub_detail =
                new FirebaseRecyclerOptions.Builder<cart_model>()
                        .setQuery(subproref, cart_model.class)
                        .build();


        FirebaseRecyclerAdapter<cart_model,SubscriptionweekFragment.subList> FirebaseRecyclearAdaptor =
                new FirebaseRecyclerAdapter<cart_model, SubscriptionweekFragment.subList>(options_sub_detail) {
                    @Override
                    protected void onBindViewHolder(@NonNull SubscriptionweekFragment.subList holder, int position, @NonNull cart_model model) {
                        holder.cart_name.setText(model.getName());
                        Picasso.get().load(model.getImage()).placeholder(R.drawable.ad).into(holder.cart_pic);
                        holder.cart_qty.setText(model.getQty());
                        holder.cart_price.setText(model.getRate());
                        holder.cart_subhead_txt.setText(model.getQuantity());


                    }

                    @NonNull
                    @Override
                    public SubscriptionweekFragment.subList onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_retrive_design, parent, false);
                        SubscriptionweekFragment.subList viewHolder = new SubscriptionweekFragment.subList(view);
                        return viewHolder;
                    }
                };
        FirebaseRecyclearAdaptor.startListening();
        subrecycl.setAdapter(FirebaseRecyclearAdaptor);

    }

    public static class subList extends RecyclerView.ViewHolder{

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


        public subList(@NonNull View itemView) {
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
        }
    }

}
