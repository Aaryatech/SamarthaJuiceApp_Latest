package com.ats.samarthajuice.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ats.samarthajuice.model.BillDetail;
import com.ats.samarthajuice.R;

import java.util.ArrayList;


public class BillItemAdapter extends RecyclerView.Adapter<BillItemAdapter.MyViewHolder> {

    private ArrayList<BillDetail> orderItemList;
    private Context context;

    public BillItemAdapter(ArrayList<BillDetail> orderItemList, Context context) {
        this.orderItemList = orderItemList;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvItem, tvQty,tvRate,tvTotal;

        public MyViewHolder(View view) {
            super(view);
            tvItem = view.findViewById(R.id.tvItem);
            tvQty = view.findViewById(R.id.tvQty);
            tvRate = view.findViewById(R.id.tvRate);
            tvTotal = view.findViewById(R.id.tvTotal);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_bill_item_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final BillDetail model = orderItemList.get(position);
        Log.e("Adapter : ", " model : " + model);
        holder.tvItem.setText("" + model.getItemName());
        holder.tvQty.setText("" + model.getQuantity());
        holder.tvRate.setText("" + model.getRate());
        holder.tvTotal.setText("" + (model.getRate()*model.getQuantity()));


    }

    @Override
    public int getItemCount() {
        return orderItemList.size();
    }


}
