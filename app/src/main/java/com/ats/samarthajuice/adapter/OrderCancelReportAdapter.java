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
import com.ats.samarthajuice.model.OrderCancelReportModel;

import java.util.ArrayList;

public class OrderCancelReportAdapter extends RecyclerView.Adapter<OrderCancelReportAdapter.MyViewHolder> {

    private ArrayList<OrderCancelReportModel> reportList;
    private Context context;

    public OrderCancelReportAdapter(ArrayList<OrderCancelReportModel> reportList, Context context) {
        this.reportList = reportList;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvOrder, tvItem, tvQty, tvStatus, tvRemark;
        public LinearLayout linearLayout;

        public MyViewHolder(View view) {
            super(view);
            tvOrder = view.findViewById(R.id.tvOrder);
            tvItem = view.findViewById(R.id.tvItem);
            tvQty = view.findViewById(R.id.tvQty);
            tvStatus = view.findViewById(R.id.tvStatus);
            tvRemark = view.findViewById(R.id.tvRemark);
            linearLayout = view.findViewById(R.id.linearLayout);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_order_cancel_report, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final OrderCancelReportModel model = reportList.get(position);
        //Log.e("Adapter : ", " model : " + model);

        holder.tvOrder.setText("" + model.getOrderId());
        holder.tvItem.setText("" + model.getItemName());
        holder.tvQty.setText("" + model.getQuantity());
        holder.tvRemark.setText("" + model.getRemark());

        String status = "";
        if (model.getStatus() == 1) {
            status = "Chargeable";
        } else if (model.getStatus() == 2) {
            status = "Cancel";
        } else if (model.getStatus() == 3) {
            status = "NC 1";
        } else if (model.getStatus() == 4) {
            status = "NC 2";
        } else if (model.getStatus() == 5) {
            status = "NC 3";
        }
        holder.tvStatus.setText("" + status);


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
