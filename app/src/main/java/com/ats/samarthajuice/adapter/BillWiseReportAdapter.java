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
import com.ats.samarthajuice.model.BillWiseReportModel;

import java.util.ArrayList;

public class BillWiseReportAdapter extends RecyclerView.Adapter<BillWiseReportAdapter.MyViewHolder> {

    private ArrayList<BillWiseReportModel> reportList;
    private Context context;

    public BillWiseReportAdapter(ArrayList<BillWiseReportModel> reportList, Context context) {
        this.reportList = reportList;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvDate, tvBill, tvAmount, tvDiscount, tvTotalTax, tvGrandTotal, tvTaxableAmt;
        public LinearLayout linearLayout;

        public MyViewHolder(View view) {
            super(view);
            tvDate = view.findViewById(R.id.tvDate);
            tvBill = view.findViewById(R.id.tvBill);
            tvTotalTax = view.findViewById(R.id.tvTotalTax);
            tvGrandTotal = view.findViewById(R.id.tvGrandTotal);
            tvTaxableAmt = view.findViewById(R.id.tvTaxableAmt);
            tvDiscount = view.findViewById(R.id.tvDiscount);
            tvAmount = view.findViewById(R.id.tvAmount);
            linearLayout = view.findViewById(R.id.linearLayout);
        }
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_bill_wise_report, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final BillWiseReportModel model = reportList.get(position);
        //Log.e("Adapter : ", " model : " + model);

        holder.tvDate.setText(model.getBillDate());
        holder.tvBill.setText("" + model.getBillNo());
        holder.tvAmount.setText("" + model.getPayableAmount());
        holder.tvDiscount.setText("" + model.getDiscount());
        holder.tvTotalTax.setText("" + (model.getCgst() + model.getSgst()));
        holder.tvGrandTotal.setText("" + model.getGrandTotal());
        holder.tvTaxableAmt.setText("" + model.getTaxableAmount());

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
