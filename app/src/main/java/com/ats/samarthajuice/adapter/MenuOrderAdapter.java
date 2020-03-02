package com.ats.samarthajuice.adapter;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.ats.samarthajuice.R;
import com.ats.samarthajuice.model.OrderDetailsList;
import com.ats.samarthajuice.model.OrderHeaderModel;

import java.util.ArrayList;

public class MenuOrderAdapter extends RecyclerView.Adapter<MenuOrderAdapter.MyViewHolder> {

    private ArrayList<OrderHeaderModel> orderList;
    private Context context;

    public MenuOrderAdapter(ArrayList<OrderHeaderModel> orderList, Context context) {
        this.orderList = orderList;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvOrder, tvCount;
        public RecyclerView recyclerView;
        public CheckBox checkBox;

        public MyViewHolder(View view) {
            super(view);
            tvOrder = view.findViewById(R.id.tvOrder);
            tvCount = view.findViewById(R.id.tvCount);
            recyclerView = view.findViewById(R.id.recyclerView);
            checkBox = view.findViewById(R.id.checkBox);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_menu_order, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final OrderHeaderModel model = orderList.get(position);
        Log.e("Adapter : ", " model : " + model);

        holder.tvOrder.setText("KOT No. : " + model.getOrderId());
        holder.checkBox.setVisibility(View.GONE);

        final ArrayList<OrderDetailsList> orderDetailsLists = new ArrayList<>();
        if (model.getOrderDetailsList().size() > 0) {
            for (int i = 0; i < model.getOrderDetailsList().size(); i++) {
                if (model.getOrderDetailsList().get(i).getStatus() == 1) {
                    orderDetailsLists.add(model.getOrderDetailsList().get(i));
                }
            }
        }

        holder.tvCount.setText("TOTAL ITEMS : " + orderDetailsLists.size());

        final OrderDetailsAdapter itemAdapter = new OrderDetailsAdapter(orderDetailsLists, context);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        holder.recyclerView.setLayoutManager(mLayoutManager);
        holder.recyclerView.setItemAnimator(new DefaultItemAnimator());
        holder.recyclerView.setAdapter(itemAdapter);

       /* holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    for (int i = 0; i < model.getOrderDetailsList().size(); i++) {
                        model.getOrderDetailsList().get(i).setChecked(true);
                        orderDetailIdStaticList.add(model.getOrderDetailsList().get(i).getOrderDetailsId());
                    }
                    Log.e("CHECKED : ", "-----------" + model.getOrderDetailsList());

                    HashSet<Integer> hashSet = new HashSet<>();
                    hashSet.addAll(orderDetailIdStaticList);

                    orderDetailIdStaticList.clear();
                    orderDetailIdStaticList.addAll(hashSet);

                    Log.e("ALL CHECKED : ", "-----STATIC------" + orderDetailIdStaticList);

                    notifyDataSetChanged();
                }
            }
        });*/

    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

}
