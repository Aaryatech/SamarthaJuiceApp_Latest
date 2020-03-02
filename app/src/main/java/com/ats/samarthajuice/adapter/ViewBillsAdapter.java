package com.ats.samarthajuice.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ats.samarthajuice.activity.GenerateBillActivity;
import com.ats.samarthajuice.activity.HomeActivity;
import com.ats.samarthajuice.constant.Constants;
import com.ats.samarthajuice.fragment.EditBillFragment;
import com.ats.samarthajuice.model.Admin;
import com.ats.samarthajuice.model.BillDetail;
import com.ats.samarthajuice.model.BillHeaderModel;
import com.ats.samarthajuice.model.TaxableDataForBillPrint;
import com.ats.samarthajuice.printer.PrintHelper;
import com.ats.samarthajuice.printer.PrintReceiptType;
import com.ats.samarthajuice.util.CommonDialog;
import com.ats.samarthajuice.util.CustomSharedPreference;
import com.ats.samarthajuice.R;
import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

            try{

                Gson gson = new Gson();
                String jsonAdmin = CustomSharedPreference.getString(context, CustomSharedPreference.KEY_ADMIN);
                final Admin admin = gson.fromJson(jsonAdmin, Admin.class);

                if (admin!=null){
                    if (admin.getType().equalsIgnoreCase("Admin")){
                        holder.ivEdit.setVisibility(View.VISIBLE);
                    }else{
                        holder.ivEdit.setVisibility(View.GONE);
                    }
                }

            }catch (Exception e){
                e.printStackTrace();
            }


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
               /* try {
                    HomeActivity activity = (HomeActivity) context;
                    String ip = CustomSharedPreference.getStringPrinter(activity, CustomSharedPreference.KEY_BILL_IP);
                    PrintHelper printHelper = new PrintHelper(activity, ip, model, PrintReceiptType.RE_GENERATE_BILL);
                    printHelper.runPrintReceiptSequence();
                } catch (Exception e) {
                }*/

                getTaxDataForBill(model.getBillId(),model);

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



    public void getTaxDataForBill(int billId, final BillHeaderModel model) {

        ArrayList<Integer> billIds=new ArrayList<>();
        billIds.add(billId);

        Log.e("Taxable Data : ", "------PARAM------" + billId);

        if (Constants.isOnline(context)) {
            final CommonDialog commonDialog = new CommonDialog(context, "Loading", "Please Wait...");
            commonDialog.show();

            Call<ArrayList<TaxableDataForBillPrint>> listCall = Constants.myInterface.getTaxDataFroBillPrintByBillId(billIds);
            listCall.enqueue(new Callback<ArrayList<TaxableDataForBillPrint>>() {
                @Override
                public void onResponse(Call<ArrayList<TaxableDataForBillPrint>> call, Response<ArrayList<TaxableDataForBillPrint>> response) {
                    try {
                        Log.e("Taxable Data : ", "------------" + response.body());
                        if (response.body() != null) {


                            ArrayList<TaxableDataForBillPrint> data = response.body();

                            try {
                                HomeActivity activity = (HomeActivity) context;
                                String ip = CustomSharedPreference.getStringPrinter(activity, CustomSharedPreference.KEY_BILL_IP);
                                PrintHelper printHelper = new PrintHelper(activity, ip, model,data, PrintReceiptType.RE_GENERATE_BILL);
                                printHelper.runPrintReceiptSequence();
                            } catch (Exception e) {
                            }


                            commonDialog.dismiss();
                        } else {
                            commonDialog.dismiss();
                            Log.e("Data Null : ", "-----------");
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Log.e("Exception : ", "-----------" + e.getMessage());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<TaxableDataForBillPrint>> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();

                }
            });
        } else {
            Toast.makeText(context, "No Internet Connection!", Toast.LENGTH_SHORT).show();
        }
    }


}
