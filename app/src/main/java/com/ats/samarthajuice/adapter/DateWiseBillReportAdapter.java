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
import com.ats.samarthajuice.model.DateWiseReportModel;

import java.util.ArrayList;

public class DateWiseBillReportAdapter extends RecyclerView.Adapter<DateWiseBillReportAdapter.MyViewHolder> {

    private ArrayList<DateWiseReportModel> reportList;
    private Context context;

    public DateWiseBillReportAdapter(ArrayList<DateWiseReportModel> reportList, Context context) {
        this.reportList = reportList;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvDate, tvAmount, tvTotal,tvType;
        public LinearLayout linearLayout;

        public MyViewHolder(View view) {
            super(view);
            tvDate = view.findViewById(R.id.tvDate);
            tvTotal = view.findViewById(R.id.tvTotal);
            tvAmount = view.findViewById(R.id.tvAmount);
            tvType = view.findViewById(R.id.tvType);
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

        if(model.getType()==1) {
            holder.tvType.setText("Card");
        }else if(model.getType()==2)
        {
            holder.tvType.setText("googlePay");
        }else if(model.getType()==3)
        {
            holder.tvType.setText("payTM");
        }else if(model.getType()==4)
        {
            holder.tvType.setText("Cash");
        }else if(model.getType()==5)
        {
            holder.tvType.setText("foodPanda");
        }else if(model.getType()==6)
        {
            holder.tvType.setText("Swiggy");
        }else if(model.getType()==7)
        {
            holder.tvType.setText("uber_eats");
        }else if(model.getType()==8)
        {
            holder.tvType.setText("Zomato");
        }

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
