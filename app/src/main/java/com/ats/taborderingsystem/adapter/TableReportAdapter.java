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
import com.ats.taborderingsystem.model.TableWiseReportModel;

import java.util.ArrayList;

public class TableReportAdapter extends RecyclerView.Adapter<TableReportAdapter.MyViewHolder> {

    private ArrayList<TableWiseReportModel> reportList;
    private Context context;

    public TableReportAdapter(ArrayList<TableWiseReportModel> reportList, Context context) {
        this.reportList = reportList;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTableNo, tvAmount, tvName,tvTotal;
        public LinearLayout linearLayout;

        public MyViewHolder(View view) {
            super(view);
            tvName = view.findViewById(R.id.tvName);
            tvTableNo = view.findViewById(R.id.tvTableNo);
            tvTotal = view.findViewById(R.id.tvTotal);
            tvAmount = view.findViewById(R.id.tvAmount);
            linearLayout = view.findViewById(R.id.linearLayout);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_table_report, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final TableWiseReportModel model = reportList.get(position);
        Log.e("Adapter : ", " model : " + model);

        holder.tvName.setText(model.getTableName());
        holder.tvTotal.setText("" + model.getTotal());
        holder.tvAmount.setText("" + model.getPayableAmount());
        holder.tvTableNo.setText("" + model.getTableNo());

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
