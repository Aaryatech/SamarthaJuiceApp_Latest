package com.ats.taborderingsystem.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ats.taborderingsystem.R;
import com.ats.taborderingsystem.model.BillDetail;

import java.util.ArrayList;

public class BillItemEditAdapter extends RecyclerView.Adapter<BillItemEditAdapter.MyViewHolder> {

    private ArrayList<BillDetail> billDetail;
    private ArrayList<BillDetail> billDetailtoSave;
    private Context context;

    public BillItemEditAdapter(ArrayList<BillDetail> billDetail, ArrayList<BillDetail> billDetailtoSave, Context context) {
        this.billDetail = billDetail;
        this.billDetailtoSave = billDetailtoSave;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvItem, tvRate, tvTotal;
        public EditText edQty;
        public ImageView ivDelete;

        public MyViewHolder(View view) {
            super(view);
            tvItem = view.findViewById(R.id.tvItem);
            edQty = view.findViewById(R.id.edQty);
            tvRate = view.findViewById(R.id.tvRate);
            tvTotal = view.findViewById(R.id.tvTotal);
            ivDelete = view.findViewById(R.id.ivDelete);

        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_bill_item_edit, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final BillDetail model = billDetail.get(position);
        final BillDetail modeltoSave = billDetailtoSave.get(position);
        Log.e("Adapter : ", " model : " + model);


        holder.tvItem.setText("" + model.getItemName());
        holder.tvRate.setText("" + model.getRate());
        holder.edQty.setText("" + model.getQuantity());
        holder.tvTotal.setText("" + model.getTotal());

        holder.edQty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    int qty = Integer.parseInt(s.toString());
                    model.setQuantity(qty);
                    modeltoSave.setQuantity(qty);

                    holder.tvTotal.setText("" + (model.getRate() * qty));

                } catch (Exception e) {
                    model.setQuantity(model.getQuantity());
                    modeltoSave.setQuantity(model.getQuantity());

                }
            }
        });

        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("DELETE ITEM", "--------------------------------");
                model.setDelStatus(0);
                modeltoSave.setDelStatus(0);

                removeAt(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return billDetail.size();
    }

    public void removeAt(int position) {
        billDetail.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, billDetail.size());
    }


}
