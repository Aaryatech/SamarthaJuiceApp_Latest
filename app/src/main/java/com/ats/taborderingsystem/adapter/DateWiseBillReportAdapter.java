package com.ats.taborderingsystem.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ats.taborderingsystem.R;
import com.ats.taborderingsystem.model.DateWiseReportModel;

import java.util.ArrayList;

public class DateWiseBillReportAdapter extends RecyclerView.Adapter<DateWiseBillReportAdapter.MyViewHolder> {

    private ArrayList<DateWiseReportModel> reportList;
    private Context context;

    public DateWiseBillReportAdapter(ArrayList<DateWiseReportModel> reportList, Context context) {
        this.reportList = reportList;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvDate, tvAmount, tvTotal;
        public LinearLayout linearLayout;

        public MyViewHolder(View view) {
            super(view);
            tvDate = view.findViewById(R.id.tvDate);
            tvTotal = view.findViewById(R.id.tvTotal);
            tvAmount = view.findViewById(R.id.tvAmount);
            linearLayout = view.findViewById(R.id.linearLayout);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_date_wise_bill_report, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final DateWiseReportModel model = reportList.get(position);
        Log.e("Adapter : ", " model : " + model);

        holder.tvDate.setText(model.getBillDate());
        holder.tvTotal.setText("" + model.getTotal());
        holder.tvAmount.setText("" + model.getPayableAmount());

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
