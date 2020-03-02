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
import com.ats.samarthajuice.model.ItemReportModel;

import java.util.ArrayList;

public class ItemWiseReportAdapter extends RecyclerView.Adapter<ItemWiseReportAdapter.MyViewHolder> {

    private ArrayList<ItemReportModel> reportList;
    private Context context;

    public ItemWiseReportAdapter(ArrayList<ItemReportModel> reportList, Context context) {
        this.reportList = reportList;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName, tvQty, tvAmount, tvRate, tvTotal;
        public LinearLayout linearLayout;

        public MyViewHolder(View view) {
            super(view);
            tvName = view.findViewById(R.id.tvName);
            tvQty = view.findViewById(R.id.tvQty);
            tvRate = view.findViewById(R.id.tvRate);
            tvTotal = view.findViewById(R.id.tvTotal);
            tvAmount = view.findViewById(R.id.tvAmount);
            linearLayout = view.findViewById(R.id.linearLayout);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_item_wise_report, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final ItemReportModel model = reportList.get(position);
        Log.e("Adapter : ", " model : " + model);

        holder.tvName.setText(model.getItemName());
        holder.tvQty.setText("" + model.getQuantity());
        holder.tvAmount.setText("" + model.getPayableAmt());
        holder.tvRate.setText("" + model.getRate());
        holder.tvTotal.setText("" + model.getTotal());

        if (position % 2 == 0) {
            holder.linearLayout.setBackgroundColor(context.getResources().getColor(R.color.colorGray));
        } else {
            holder.linearLayout.setBackgroundColor(context.getResources().getColor(R.color.colorWhite));
        }

    }

    @Override
    public int getItemCount() {
        return reportList.size();
    }


}
