package com.ats.samarthajuice.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.ats.samarthajuice.fragment.OpenBillFragment;
import com.ats.samarthajuice.R;
import com.ats.samarthajuice.activity.HomeActivity;
import com.ats.samarthajuice.constant.Constants;
import com.ats.samarthajuice.model.BillDetail;
import com.ats.samarthajuice.model.BillHeaderModel;
import com.ats.samarthajuice.util.CommonDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OpenBillAdapter extends RecyclerView.Adapter<OpenBillAdapter.MyViewHolder> {

    private ArrayList<BillHeaderModel> billHeaderList;
    private Context context;

    public OpenBillAdapter(ArrayList<BillHeaderModel> billHeaderList, Context context) {
        this.billHeaderList = billHeaderList;
        this.context = context;
    }

    @NonNull
    @Override
    public OpenBillAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_open_bills, parent, false);

        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull OpenBillAdapter.MyViewHolder holder, int position) {
        final BillHeaderModel model = billHeaderList.get(position);

            //Log.e("Adapter : ", " model : " + model);

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

        String billdate=model.getBillDate();
        SimpleDateFormat formatter3 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MM-yyyy");

        Date BillDate = null;
        try {
            BillDate = formatter1.parse(billdate);//catch exception
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        final String dateBill = formatter3.format(BillDate);

        final ArrayList<BillDetail> detailList1 = new ArrayList<>();
        for(int i=0;i<model.getBillDetails().size();i++) {
            BillDetail detail = new BillDetail(model.getBillDetails().get(i).getBillDetailsId(),model.getBillDetails().get(i).getBillId(),model.getBillDetails().get(i).getOrderId(),model.getBillDetails().get(i).getDelStatus(),model.getBillDetails().get(i).getItemId(),model.getBillDetails().get(i).getItemName(),model.getBillDetails().get(i).getQuantity(),model.getBillDetails().get(i).getRate(),model.getBillDetails().get(i).getSgst(),model.getBillDetails().get(i).getCgst(),model.getBillDetails().get(i).getTotal(),model.getBillDetails().get(i).getTaxableAmt(),model.getBillDetails().get(i).getTotalTax());
            detailList1.add(detail);

        }

        // PdfData(model.getQuotHeadId());

        holder.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context, v);
                popupMenu.getMenuInflater().inflate(R.menu.menu_open_bill, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if(menuItem.getItemId() == R.id.action_card){

                            // PdfData(model.getQuotHeadId());
                            BillHeaderModel billHeaderModel=new BillHeaderModel(model.getBillId(),dateBill,model.getDelStatus(),model.getUserId(),model.getEnterBy(),1,model.getDiscount(),model.getGrandTotal(),model.getPayableAmt(),model.getTableNo(),model.getBillNo(),1,model.getCgst(),model.getSgst(),model.getTaxableAmount(),model.getName(),model.getMobileNo(),detailList1);
                            updateStatus(billHeaderModel);

//                            HomeActivity activity = (HomeActivity) context;
//                            FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
//                            ft.replace(R.id.content_frame, new OpenBillFragment(), "HomeFragment");
//                            ft.commit();


                            //Log.e("BillHeaderModelCard","------------------------------------BillHeaderModel-----------------"+billHeaderModel);

                        }else if(menuItem.getItemId() == R.id.action_googlePay){

                            // PdfData(model.getQuotHeadId());
                            BillHeaderModel billHeaderModel=new BillHeaderModel(model.getBillId(),dateBill,model.getDelStatus(),model.getUserId(),model.getEnterBy(),1,model.getDiscount(),model.getGrandTotal(),model.getPayableAmt(),model.getTableNo(),model.getBillNo(),2,model.getCgst(),model.getSgst(),model.getTaxableAmount(),model.getName(),model.getMobileNo(),detailList1);
                            updateStatus(billHeaderModel);

                            //Log.e("BillHeaderModelGoogle","------------------------------------BillHeaderModel-----------------"+billHeaderModel);
                           // refreshEvents(billHeaderList);
//                            HomeActivity activity = (HomeActivity) context;
//                            FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
//                            ft.replace(R.id.content_frame, new OpenBillFragment(), "HomeFragment");
//                            ft.commit();

                        }else if(menuItem.getItemId() == R.id.action_payTM){

                            // PdfData(model.getQuotHeadId());
                            BillHeaderModel billHeaderModel=new BillHeaderModel(model.getBillId(),dateBill,model.getDelStatus(),model.getUserId(),model.getEnterBy(),1,model.getDiscount(),model.getGrandTotal(),model.getPayableAmt(),model.getTableNo(),model.getBillNo(),3,model.getCgst(),model.getSgst(),model.getTaxableAmount(),model.getName(),model.getMobileNo(),detailList1);
                            updateStatus(billHeaderModel);
                            //Log.e("BillHeaderModelPayTm","------------------------------------BillHeaderModel-----------------"+billHeaderModel);
//                            HomeActivity activity = (HomeActivity) context;
//                            FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
//                            ft.replace(R.id.content_frame, new OpenBillFragment(), "HomeFragment");
//                            ft.commit();

                        }else if(menuItem.getItemId() == R.id.action_cash){

                            BillHeaderModel billHeaderModel=new BillHeaderModel(model.getBillId(),dateBill,model.getDelStatus(),model.getUserId(),model.getEnterBy(),1,model.getDiscount(),model.getGrandTotal(),model.getPayableAmt(),model.getTableNo(),model.getBillNo(),4,model.getCgst(),model.getSgst(),model.getTaxableAmount(),model.getName(),model.getMobileNo(),detailList1);
                            updateStatus(billHeaderModel);
                            //Log.e("BillHeaderModelcash","------------------------------------BillHeaderModel-----------------"+billHeaderModel);
//                            HomeActivity activity = (HomeActivity) context;
//                            FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
//                            ft.replace(R.id.content_frame, new OpenBillFragment(), "HomeFragment");
//                            ft.commit();

                        }else if(menuItem.getItemId() == R.id.action_Foodpanda){

                            // PdfData(model.getQuotHeadId());
                            BillHeaderModel billHeaderModel=new BillHeaderModel(model.getBillId(),dateBill,model.getDelStatus(),model.getUserId(),model.getEnterBy(),1,model.getDiscount(),model.getGrandTotal(),model.getPayableAmt(),model.getTableNo(),model.getBillNo(),5,model.getCgst(),model.getSgst(),model.getTaxableAmount(),model.getName(),model.getMobileNo(),detailList1);
                            updateStatus(billHeaderModel);
                            //Log.e("BillHeaderModelFood","------------------------------------BillHeaderModel-----------------"+billHeaderModel);
//                            HomeActivity activity = (HomeActivity) context;
//                            FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
//                            ft.replace(R.id.content_frame, new OpenBillFragment(), "HomeFragment");
//                            ft.commit();

                        }else if(menuItem.getItemId() == R.id.action_Swiggy){

                            // PdfData(model.getQuotHeadId());
                            BillHeaderModel billHeaderModel=new BillHeaderModel(model.getBillId(),dateBill,model.getDelStatus(),model.getUserId(),model.getEnterBy(),1,model.getDiscount(),model.getGrandTotal(),model.getPayableAmt(),model.getTableNo(),model.getBillNo(),6,model.getCgst(),model.getSgst(),model.getTaxableAmount(),model.getName(),model.getMobileNo(),detailList1);
                            updateStatus(billHeaderModel);
                            //Log.e("BillHeaderModelawiggy","------------------------------------BillHeaderModel-----------------"+billHeaderModel);
//                            HomeActivity activity = (HomeActivity) context;
//                            FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
//                            ft.replace(R.id.content_frame, new OpenBillFragment(), "HomeFragment");
//                            ft.commit();

                        }else if(menuItem.getItemId() == R.id.action_Uber_eats){

                            // PdfData(model.getQuotHeadId());
                            BillHeaderModel billHeaderModel=new BillHeaderModel(model.getBillId(),dateBill,model.getDelStatus(),model.getUserId(),model.getEnterBy(),1,model.getDiscount(),model.getGrandTotal(),model.getPayableAmt(),model.getTableNo(),model.getBillNo(),7,model.getCgst(),model.getSgst(),model.getTaxableAmount(),model.getName(),model.getMobileNo(),detailList1);
                            updateStatus(billHeaderModel);
                            //Log.e("BillHeaderModeluber","------------------------------------BillHeaderModel-----------------"+billHeaderModel);
//                            HomeActivity activity = (HomeActivity) context;
//                            FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
//                            ft.replace(R.id.content_frame, new OpenBillFragment(), "HomeFragment");
//                            ft.commit();

                        }else if(menuItem.getItemId() == R.id.action_Zomato){

                            // PdfData(model.getQuotHeadId());
                            BillHeaderModel billHeaderModel=new BillHeaderModel(model.getBillId(),dateBill,model.getDelStatus(),model.getUserId(),model.getEnterBy(),1,model.getDiscount(),model.getGrandTotal(),model.getPayableAmt(),model.getTableNo(),model.getBillNo(),8,model.getCgst(),model.getSgst(),model.getTaxableAmount(),model.getName(),model.getMobileNo(),detailList1);
                            updateStatus(billHeaderModel);
                            //Log.e("BillHeaderModelzomato","------------------------------------BillHeaderModel-----------------"+billHeaderModel);
//                            HomeActivity activity = (HomeActivity) context;
//                            FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
//                            ft.replace(R.id.content_frame, new OpenBillFragment(), "HomeFragment");
//                            ft.commit();

                        }
                        return true;
                    }
                });
                popupMenu.show();

            }
        });
    }


    public void refreshEvents(List<BillHeaderModel> billHeaderList)
    {
        this.billHeaderList.clear();
        this.billHeaderList.addAll(billHeaderList);
        notifyDataSetChanged();
    }

    private void updateStatus(BillHeaderModel billHeaderModel) {
        //Log.e("Para BillHeaderModel","------------------------------------BillHeaderModel-----------------"+billHeaderModel);
        if (Constants.isOnline(context)) {
            final CommonDialog commonDialog = new CommonDialog(context, "Loading", "Please Wait...");
            commonDialog.show();

            Call<BillHeaderModel> listCall = Constants.myInterface.editBill(billHeaderModel);
            listCall.enqueue(new Callback<BillHeaderModel>() {
                @Override
                public void onResponse(Call<BillHeaderModel> call, Response<BillHeaderModel> response) {
                    try {
                        if (response.body() != null) {

                            //Log.e("Update satus bill : ", "------------" + response.body());

                            BillHeaderModel data = response.body();
                            if (data == null) {
                                commonDialog.dismiss();
                            } else {

                                HomeActivity activity = (HomeActivity) context;
                                Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
                                FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
                                ft.replace(R.id.content_frame, new OpenBillFragment(), "HomeFragment");
                                ft.commit();

                                commonDialog.dismiss();
                            }
                        } else {

                            commonDialog.dismiss();
                            //Log.e("Data Null : ", "-----------");
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        //Log.e("Exception : ", "-----------" + e.getMessage());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<BillHeaderModel> call, Throwable t) {
                    commonDialog.dismiss();
                    //Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(context, "No Internet Connection!", Toast.LENGTH_SHORT).show();
        }



    }



    @Override
    public int getItemCount() {
        return billHeaderList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvDate, tvBillNo, tvType, tvCustomer, tvMobile, tvTable, tvDiscount, tvTotal;
        public LinearLayout llParcel;
        public RecyclerView recyclerView;
        public ImageView ivPrint, ivEdit;
        public CardView cardView;
        public MyViewHolder(View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvBillNo = itemView.findViewById(R.id.tvBillNo);
            tvType = itemView.findViewById(R.id.tvType);
            tvCustomer = itemView.findViewById(R.id.tvCustomer);
            tvMobile = itemView.findViewById(R.id.tvMobile);
            tvTable = itemView.findViewById(R.id.tvTable);
            tvDiscount = itemView.findViewById(R.id.tvDiscount);
            tvTotal = itemView.findViewById(R.id.tvTotal);
            llParcel = itemView.findViewById(R.id.llParcel);
            recyclerView = itemView.findViewById(R.id.recyclerView);
            ivPrint = itemView.findViewById(R.id.ivPrint);
            ivEdit = itemView.findViewById(R.id.ivEdit);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}
