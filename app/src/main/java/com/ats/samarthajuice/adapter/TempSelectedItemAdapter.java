package com.ats.samarthajuice.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ats.samarthajuice.R;
import com.ats.samarthajuice.model.ItemModel;

import java.util.ArrayList;

public class TempSelectedItemAdapter extends RecyclerView.Adapter<TempSelectedItemAdapter.MyViewHolder> {

    private ArrayList<ItemModel> itemList;
    private Context context;

    public TempSelectedItemAdapter(ArrayList<ItemModel> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName, tvQty;
        private ImageView ivCancel;

        public MyViewHolder(View view) {
            super(view);
            tvName = view.findViewById(R.id.tvName);
            tvQty = view.findViewById(R.id.tvQty);
            ivCancel = view.findViewById(R.id.ivCancel);
        }
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_temp_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final ItemModel item = itemList.get(position);
        //Log.e("Adapter : ", " model : " + item);

        if (item.getRemark() != null) {
            if (item.getRemark().isEmpty()) {
                holder.tvName.setText(item.getItemName());

            } else {
                holder.tvName.setText(item.getItemName() + "- " + item.getRemark());

            }
        }else{
            holder.tvName.setText(item.getItemName());
        }

        holder.tvQty.setText("" + item.getQty());

        holder.ivCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              /*  Intent pushNotificationIntent = new Intent();
                pushNotificationIntent.setAction("REFRESH");

                pushNotificationIntent.putExtra("itemId", itemList.get(position).getItemId());

                LocalBroadcastManager.getInstance(context).sendBroadcast(pushNotificationIntent);
*/
                itemList.remove(position);
                notifyDataSetChanged();


            }
        });

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }


}
