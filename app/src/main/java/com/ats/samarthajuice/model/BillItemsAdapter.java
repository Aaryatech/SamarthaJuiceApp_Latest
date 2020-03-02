package com.ats.samarthajuice.model;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ats.samarthajuice.R;

import java.util.ArrayList;

public class BillItemsAdapter extends RecyclerView.Adapter<BillItemsAdapter.MyViewHolder> {

    private ArrayList<OrderDetailsList> itemList;
    private Context context;

    public BillItemsAdapter(ArrayList<OrderDetailsList> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvItem, tvQty, tvPrice, tvTotal, tvKOT, tvStatus;
        public LinearLayout  linearLayout;
        public View viewLine;

        public MyViewHolder(View view) {
            super(view);
            tvItem = view.findViewById(R.id.tvItem);
            tvQty = view.findViewById(R.id.tvQty);
            tvPrice = view.findViewById(R.id.tvPrice);
            tvTotal = view.findViewById(R.id.tvTotal);
            tvKOT = view.findViewById(R.id.tvKOT);
            tvStatus = view.findViewById(R.id.tvStatus);
            linearLayout= view.findViewById(R.id.linearLayout);
            viewLine= view.findViewById(R.id.viewLine);
        }
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_bill_items, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final OrderDetailsList model = itemList.get(position);
        Log.e("Adapter : ", " model : " + model);

        holder.viewLine.setVisibility(View.GONE);

        holder.tvItem.setText("" + model.getItemName());
        holder.tvQty.setText("" + model.getQuantity());
        holder.tvPrice.setText("" + model.getRate());
        holder.tvTotal.setText("" + (model.getRate() * model.getQuantity()));
        holder.tvKOT.setText("" + model.getOrderId());

        if (position%2==0){
            holder.linearLayout.setBackgroundColor(context.getResources().getColor(R.color.colorGray));
        }else{
            holder.linearLayout.setBackgroundColor(context.getResources().getColor(R.color.colorWhite));
        }

        String status = "";
        if (model.getStatus() == 1) {
            status = "CH";
        } else if (model.getStatus() == 2) {
            status = "C";
        } else if (model.getStatus() == 3) {
            status = "NC 1";
        } else if (model.getStatus() == 4) {
            status = "NC 2";
        } else if (model.getStatus() == 5) {
            status = "NC 3";
        }

        holder.tvStatus.setText("" + status);


    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }


}
