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
import com.ats.samarthajuice.model.ItemCancelReportModel;

import java.util.ArrayList;

public class ItemCancelReportAdapter extends RecyclerView.Adapter<ItemCancelReportAdapter.MyViewHolder> {

    private ArrayList<ItemCancelReportModel> itemList;
    private Context context;

    public ItemCancelReportAdapter(ArrayList<ItemCancelReportModel> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvItem, tvChargeable, tvNC1, tvNC2, tvNC3;
        public LinearLayout linearLayout;

        public MyViewHolder(View view) {
            super(view);
            tvItem = view.findViewById(R.id.tvItem);
            tvChargeable = view.findViewById(R.id.tvChargeable);
            tvNC1 = view.findViewById(R.id.tvNC1);
            tvNC2 = view.findViewById(R.id.tvNC2);
            tvNC3 = view.findViewById(R.id.tvNC3);
            linearLayout = view.findViewById(R.id.linearLayout);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_item_cancel_report, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final ItemCancelReportModel model = itemList.get(position);
        Log.e("Adapter : ", " model : " + model);

        holder.tvItem.setText(model.getItemName());
        holder.tvChargeable.setText("" + model.getChargable());
        holder.tvNC1.setText("" + model.getNc1());
        holder.tvNC2.setText("" + model.getNc2());
        holder.tvNC3.setText("" + model.getNc3());

        if (position % 2 == 0) {
            holder.linearLayout.setBackgroundColor(context.getResources().getColor(R.color.colorGray));
        } else {
            holder.linearLayout.setBackgroundColor(context.getResources().getColor(R.color.colorWhite));
        }

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }


}
