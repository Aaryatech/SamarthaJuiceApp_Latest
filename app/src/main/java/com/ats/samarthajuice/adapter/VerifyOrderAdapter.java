package com.ats.samarthajuice.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ats.samarthajuice.R;
import com.ats.samarthajuice.model.Item;

import java.util.ArrayList;

public class VerifyOrderAdapter extends RecyclerView.Adapter<VerifyOrderAdapter.MyViewHolder> {


    private ArrayList<Item> itemList;
    private Context context;

    public VerifyOrderAdapter(ArrayList<Item> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvItem, tvRate, tvQty;
        public ImageView imageView;
        public LinearLayout linearLayout;

        public MyViewHolder(View view) {
            super(view);
            tvItem = view.findViewById(R.id.tvItem);
            tvRate = view.findViewById(R.id.tvRate);
            tvQty = view.findViewById(R.id.tvQty);
            imageView = view.findViewById(R.id.imageView);
            linearLayout = view.findViewById(R.id.linearLayout);
        }
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_display_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Item item = itemList.get(position);
        Log.e("Adapter : ", " model : " + item);

        holder.tvItem.setText(item.getItemName());
        holder.tvRate.setText("" + item.getMrpRegular());
        holder.tvQty.setText("" + item.getQty());

        if (item.isCancelStatus()) {
            holder.imageView.setVisibility(View.VISIBLE);
        } else {
            holder.imageView.setVisibility(View.INVISIBLE);
        }

        if (position%2==0){
            holder.linearLayout.setBackgroundColor(context.getResources().getColor(R.color.colorGray));
        }else{
            holder.linearLayout.setBackgroundColor(context.getResources().getColor(R.color.colorWhite));
        }


        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.setQty(0);
                item.setCancelStatus(false);
                itemList.remove(item);
                notifyDataSetChanged();

                Intent pushNotificationIntent = new Intent();
                pushNotificationIntent.setAction("REFRESH_DATA");
                LocalBroadcastManager.getInstance(context).sendBroadcast(pushNotificationIntent);

            }
        });


        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_quantity);

                final ListView listView = dialog.findViewById(R.id.listView);

                final ArrayList<Integer> intArray = new ArrayList<>();
                for (int i = 1; i <= 100; i++) {
                    intArray.add(i);
                }

                ArrayAdapter<Integer> arrayIntAdapter = new ArrayAdapter<Integer>(context, android.R.layout.simple_expandable_list_item_1, intArray) {

                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {

                        LayoutInflater inflater1 = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View view = inflater1.inflate(R.layout.adapter_quantity, parent, false);
                        TextView tvQty = view.findViewById(R.id.tvQtyItem);
                        tvQty.setText("" + intArray.get(position));
                        return view;
                    }
                };
                listView.setAdapter(arrayIntAdapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        //holder.tvQty.setText("" + (i + 1));
                        item.setQty((i + 1));
                        item.setCancelStatus(true);
                        dialog.dismiss();
                        notifyDataSetChanged();

                        Intent pushNotificationIntent = new Intent();
                        pushNotificationIntent.setAction("REFRESH_DATA");
                        LocalBroadcastManager.getInstance(context).sendBroadcast(pushNotificationIntent);


                    }
                });

                dialog.show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

}
