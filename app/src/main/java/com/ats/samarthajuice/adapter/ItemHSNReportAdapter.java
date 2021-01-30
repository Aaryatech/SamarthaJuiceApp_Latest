package com.ats.samarthajuice.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ats.samarthajuice.R;
import com.ats.samarthajuice.model.ItemHSNCodeReportModel;

import java.util.ArrayList;

public class ItemHSNReportAdapter extends RecyclerView.Adapter<ItemHSNReportAdapter.MyViewHolder>{

    private ArrayList<ItemHSNCodeReportModel> itemList;
    private Context context;

    public ItemHSNReportAdapter(ArrayList<ItemHSNCodeReportModel> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName, tvAmount, tvTax,tvCgst,tvSgst;
        public LinearLayout linearLayout;

        public MyViewHolder(View view) {
            super(view);
            tvName = view.findViewById(R.id.tvName);
            tvCgst = view.findViewById(R.id.tvCgst);
            tvSgst = view.findViewById(R.id.tvSgst);
            tvTax = view.findViewById(R.id.tvTax);
            tvAmount = view.findViewById(R.id.tvAmount);
            linearLayout = view.findViewById(R.id.linearLayout);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_item_hsn_report, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final ItemHSNCodeReportModel model = itemList.get(position);
        //Log.e("Adapter : ", " model : " + model);

        holder.tvName.setText(model.getHsnCode());
        holder.tvAmount.setText("" + model.getTaxableAmount());
        holder.tvTax.setText("" + model.getTotalTax());
        holder.tvCgst.setText("" + model.getCgst());
        holder.tvSgst.setText("" + model.getSgst());

        if (position % 2 == 0) {
            holder.linearLayout.setBackgroundColor(context.getResources().getColor(R.color.colorGray));
        }else{
            holder.linearLayout.setBackgroundColor(context.getResources().getColor(R.color.colorWhite));
        }

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }


}
