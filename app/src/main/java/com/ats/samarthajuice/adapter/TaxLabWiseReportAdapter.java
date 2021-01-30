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
import com.ats.samarthajuice.model.TaxLabWiseReportModel;

import java.util.ArrayList;

public class TaxLabWiseReportAdapter extends RecyclerView.Adapter<TaxLabWiseReportAdapter.MyViewHolder> {

    private ArrayList<TaxLabWiseReportModel> reportList;
    private Context context;

    public TaxLabWiseReportAdapter(ArrayList<TaxLabWiseReportModel> reportList, Context context) {
        this.reportList = reportList;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTax, tvAmount, tvTotalTax;
        public LinearLayout linearLayout;

        public MyViewHolder(View view) {
            super(view);
            tvTax = view.findViewById(R.id.tvTax);
            tvTotalTax = view.findViewById(R.id.tvTotalTax);
            tvAmount = view.findViewById(R.id.tvAmount);
            linearLayout = view.findViewById(R.id.linearLayout);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_tax_lab_report, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final TaxLabWiseReportModel model = reportList.get(position);
        //Log.e("Adapter : ", " model : " + model);

        holder.tvTax.setText("" + model.getTax());
        holder.tvTotalTax.setText("" + model.getTotalTax());
        holder.tvAmount.setText("" + model.getTaxableAmount());

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
