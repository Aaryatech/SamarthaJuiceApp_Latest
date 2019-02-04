package com.ats.taborderingsystem.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.ats.taborderingsystem.R;
import com.ats.taborderingsystem.model.OrderDetailsList;

import java.util.ArrayList;

import static com.ats.taborderingsystem.activity.MenuWithSearchActivity.orderDetailIdStaticList;
import static com.ats.taborderingsystem.activity.MenuWithSearchActivity.orderDetailStaticList;

public class OrderDetailsAdapter extends RecyclerView.Adapter<OrderDetailsAdapter.MyViewHolder> {

    private ArrayList<OrderDetailsList> orderItemList;
    private Context context;

    public OrderDetailsAdapter(ArrayList<OrderDetailsList> orderItemList, Context context) {
        this.orderItemList = orderItemList;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvItem, tvQty;
        public CheckBox checkBox;

        public MyViewHolder(View view) {
            super(view);
            tvItem = view.findViewById(R.id.tvItem);
            tvQty = view.findViewById(R.id.tvQty);
            checkBox = view.findViewById(R.id.checkBox);

        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_order_details, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final OrderDetailsList model = orderItemList.get(position);
        Log.e("Adapter : ", " model : " + model);

        holder.tvItem.setText("" + model.getItemName());
        holder.tvQty.setText("" + model.getQuantity());

        if (model.isChecked()) {
            holder.checkBox.setChecked(true);
        } else {
            holder.checkBox.setChecked(false);
        }

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Log.e("CHECK", "-----------------" + model.getOrderDetailsId());
                    orderDetailIdStaticList.add(model.getOrderDetailsId());
                    orderDetailStaticList.add(model);
                } else {
                    Log.e("UN CHECK", "-----------------" + model.getOrderDetailsId());

                    ArrayList<Integer> tempArray = new ArrayList<>();
                    tempArray.add(model.getOrderDetailsId());

                    orderDetailIdStaticList.removeAll(tempArray);
                    orderDetailStaticList.remove(model);
                }
                Log.e("STATIC ARRAY : ", "-------------------" + orderDetailIdStaticList);
                Log.e("STATIC ARRAY MODEL : ", "-------------------" + orderDetailStaticList);
            }
        });


    }

    @Override
    public int getItemCount() {
        return orderItemList.size();
    }

}
