package com.ats.taborderingsystem.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ats.taborderingsystem.R;
import com.ats.taborderingsystem.activity.GenerateBillActivity;
import com.ats.taborderingsystem.activity.HomeActivity;
import com.ats.taborderingsystem.fragment.AddCategoryFragment;
import com.ats.taborderingsystem.fragment.EditBillFragment;
import com.ats.taborderingsystem.model.BillDetail;
import com.ats.taborderingsystem.model.BillHeaderModel;
import com.ats.taborderingsystem.printer.PrintHelper;
import com.ats.taborderingsystem.printer.PrintReceiptType;
import com.ats.taborderingsystem.util.CustomSharedPreference;
import com.google.gson.Gson;

import java.util.ArrayList;

public class ViewBillsAdapter extends RecyclerView.Adapter<ViewBillsAdapter.MyViewHolder> {

    private ArrayList<BillHeaderModel> billHeaderList;
    private Context context;

    public ViewBillsAdapter(ArrayList<BillHeaderModel> billHeaderList, Context context) {
        this.billHeaderList = billHeaderList;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvDate, tvBillNo, tvType, tvCustomer, tvMobile, tvTable, tvDiscount, tvTotal;
        public LinearLayout llParcel;
        public RecyclerView recyclerView;
        public ImageView ivPrint, ivEdit;

        public MyViewHolder(View view) {
            super(view);
            tvDate = view.findViewById(R.id.tvDate);
            tvBillNo = view.findViewById(R.id.tvBillNo);
            tvType = view.findViewById(R.id.tvType);
            tvCustomer = view.findViewById(R.id.tvCustomer);
            tvMobile = view.findViewById(R.id.tvMobile);
            tvTable = view.findViewById(R.id.tvTable);
            tvDiscount = view.findViewById(R.id.tvDiscount);
            tvTotal = view.findViewById(R.id.tvTotal);
            llParcel = view.findViewById(R.id.llParcel);
            recyclerView = view.findViewById(R.id.recyclerView);
            ivPrint = view.findViewById(R.id.ivPrint);
            ivEdit = view.findViewById(R.id.ivEdit);
        }
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_view_bills, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final BillHeaderModel model = billHeaderList.get(position);
        Log.e("Adapter : ", " model : " + model);

        holder.tvDate.setText(model.getBillDate());
        holder.tvCustomer.setText("NAME : " + model.getName());
        holder.tvMobile.setText("MOBILE : " + model.getMobileNo());
        holder.tvBillNo.setText("BILL  : " + model.getBillNo());
        holder.tvDiscount.setText("DISCOUNT : " + model.getDiscount());
        holder.tvTotal.setText("TOTAL : " + model.getPayableAmt());
        holder.tvTable.setText("TABLE : " + model.getTableNo());

        if (model.getEnterBy() == 1) {
            holder.llParcel.setVisibility(View.GONE);
            holder.tvType.setText("");
        } else {
            holder.llParcel.setVisibility(View.VISIBLE);
            holder.tvType.setText("Parcel");
            holder.tvTable.setVisibility(View.GONE);
        }

        if (model.getBillDetails().size() > 0) {

            BillItemAdapter adapter = new BillItemAdapter((ArrayList<BillDetail>) model.getBillDetails(), context);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
            holder.recyclerView.setLayoutManager(mLayoutManager);
            holder.recyclerView.setItemAnimator(new DefaultItemAnimator());
            holder.recyclerView.setAdapter(adapter);
        }

        holder.ivPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    HomeActivity activity = (HomeActivity) context;
                    String ip = CustomSharedPreference.getString(activity, CustomSharedPreference.KEY_BILL_IP);
                    PrintHelper printHelper = new PrintHelper(activity, ip, model, PrintReceiptType.RE_GENERATE_BILL);
                    printHelper.runPrintReceiptSequence();
                } catch (Exception e) {
                }

            }
        });

        holder.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeActivity activity = (HomeActivity) context;

                Gson gson = new Gson();
                String str = gson.toJson(model);

                Fragment adf = new EditBillFragment();
                Bundle args = new Bundle();
                args.putString("model", str);
                adf.setArguments(args);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, adf, "ViewBillsFragment").commit();


            }
        });


    }

    @Override
    public int getItemCount() {
        return billHeaderList.size();
    }

}
