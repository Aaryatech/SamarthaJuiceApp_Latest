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
import com.ats.samarthajuice.model.MonthWiseBillReportModel;

import java.util.ArrayList;

public class MonthWiseBillReportAdapter extends RecyclerView.Adapter<MonthWiseBillReportAdapter.MyViewHolder>{

    private ArrayList<MonthWiseBillReportModel> reportList;
    private Context context;

    public MonthWiseBillReportAdapter(ArrayList<MonthWiseBillReportModel> reportList, Context context) {
        this.reportList = reportList;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvMonth, tvAmount, tvTaxAmt,tvTaxableAmt,tvGrandTotal;
        public LinearLayout linearLayout;

        public MyViewHolder(View view) {
            super(view);
            tvMonth = view.findViewById(R.id.tvMonth);
            tvTaxAmt = view.findViewById(R.id.tvTaxAmt);
            tvTaxableAmt = view.findViewById(R.id.tvTaxableAmt);
            tvGrandTotal = view.findViewById(R.id.tvGrandTotal);
            tvAmount = view.findViewById(R.id.tvAmount);
            linearLayout = view.findViewById(R.id.linearLayout);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_month_wise_bill_report, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final MonthWiseBillReportModel model = reportList.get(position);
        //Log.e("Adapter : ", " model : " + model);

        holder.tvMonth.setText(model.getMonth());
        holder.tvAmount.setText("" + model.getPayableAmount());
        holder.tvGrandTotal.setText("" + model.getGrandTotal());
        holder.tvTaxableAmt.setText("" + model.getTaxableAmount());
        holder.tvTaxAmt.setText("" + model.getTaxAmt());


        if (position % 2 == 0) {
            holder.linearLayout.setBackgroundColor(context.getResources().getColor(R.color.colorGray));
        }else{
            holder.linearLayout.setBackgroundColor(context.getResources().getColor(R.color.colorWhite));
        }

    }

    @Override
    public int getItemCount() {
        return reportList.size();
    }

}
