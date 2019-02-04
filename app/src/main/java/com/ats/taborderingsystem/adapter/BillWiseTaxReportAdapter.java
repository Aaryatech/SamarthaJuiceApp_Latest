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
import com.ats.taborderingsystem.model.BillWiseTaxReportModel;
import com.ats.taborderingsystem.model.DateWiseReportModel;

import java.util.ArrayList;

public class BillWiseTaxReportAdapter extends RecyclerView.Adapter<BillWiseTaxReportAdapter.MyViewHolder> {

    private ArrayList<BillWiseTaxReportModel> reportList;
    private Context context;

    public BillWiseTaxReportAdapter(ArrayList<BillWiseTaxReportModel> reportList, Context context) {
        this.reportList = reportList;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvDate, tvAmount, tvBill, tvTax, tvTotalTax;
        public LinearLayout linearLayout;

        public MyViewHolder(View view) {
            super(view);
            tvDate = view.findViewById(R.id.tvDate);
            tvAmount = view.findViewById(R.id.tvAmount);
            tvBill = view.findViewById(R.id.tvBill);
            tvTax = view.findViewById(R.id.tvTax);
            tvTotalTax = view.findViewById(R.id.tvTotalTax);
            linearLayout = view.findViewById(R.id.linearLayout);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_bill_wise_tax_report, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final BillWiseTaxReportModel model = reportList.get(position);
        Log.e("Adapter : ", " model : " + model);

        holder.tvDate.setText(model.getBillDate());
        holder.tvAmount.setText("" + model.getTaxableAmount());
        holder.tvBill.setText("" + model.getBillNo());
        holder.tvTax.setText("" + model.getTax());
        holder.tvTotalTax.setText("" + model.getTotalTax());


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
