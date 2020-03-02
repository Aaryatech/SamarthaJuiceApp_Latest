package com.ats.samarthajuice.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ats.samarthajuice.R;
import com.ats.samarthajuice.adapter.BillItemEditAdapter;
import com.ats.samarthajuice.constant.Constants;
import com.ats.samarthajuice.model.BillDetail;
import com.ats.samarthajuice.model.BillHeaderModel;
import com.ats.samarthajuice.util.CommonDialog;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditBillFragment extends Fragment implements View.OnClickListener {

    private TextView tvBillNo, tvDate, tvDiscount, tvTotal, tvCustomer, tvMobile, tvTable;
    private RecyclerView recyclerView;
    private Button btnSubmit;
    private LinearLayout llParcel;

    BillItemEditAdapter adapter;
    ArrayList<BillDetail> billItemList = new ArrayList<>();
    ArrayList<BillDetail> billItemListToSave = new ArrayList<>();
    BillHeaderModel model;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_bill, container, false);

        getActivity().setTitle("Edit Bill");

        tvBillNo = view.findViewById(R.id.tvBillNo);
        tvDate = view.findViewById(R.id.tvDate);
        tvDiscount = view.findViewById(R.id.tvDiscount);
        tvTotal = view.findViewById(R.id.tvTotal);
        tvCustomer = view.findViewById(R.id.tvCustomer);
        tvMobile = view.findViewById(R.id.tvMobile);
        recyclerView = view.findViewById(R.id.recyclerView);
        btnSubmit = view.findViewById(R.id.btnSubmit);
        llParcel = view.findViewById(R.id.llParcel);
        tvTable = view.findViewById(R.id.tvTable);

        btnSubmit.setOnClickListener(this);

        String str = "";
        try {
            str = getArguments().getString("model");
        } catch (Exception e) {
        }

        Gson gson1 = new Gson();
        model = gson1.fromJson(str, BillHeaderModel.class);

        if (model != null) {

            tvBillNo.setText("BILL : " + model.getBillNo());
            tvDate.setText("DATE : " + model.getBillDate());
            tvDiscount.setText("DISCOUNT : " + model.getDiscount());
            tvTotal.setText("TOTAL : " + model.getPayableAmt());
            tvCustomer.setText("CUSTOMER : " + model.getName());
            tvMobile.setText("MOBILE : " + model.getMobileNo());
            tvTable.setText("TABLE : " + model.getTableNo());

            billItemList.clear();
            billItemList = (ArrayList<BillDetail>) model.getBillDetails();

            billItemListToSave.clear();
            for (int i = 0; i < model.getBillDetails().size(); i++) {
                billItemListToSave.add(model.getBillDetails().get(i));
            }


            if (model.getEnterBy() == 2) {
                llParcel.setVisibility(View.VISIBLE);
            } else {
                llParcel.setVisibility(View.GONE);
            }

            adapter = new BillItemEditAdapter(billItemList, billItemListToSave, getContext());
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(adapter);


        }


        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnSubmit) {

            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date date = sdf.parse(model.getBillDate());

                model.setBillDate(sdf1.format(date));

            } catch (ParseException e) {
                e.printStackTrace();
            }

            Log.e("BILL DETAIL : ", "----------------" + billItemListToSave);

            model.setBillDetails(billItemListToSave);
            editModel(model);

        }
    }

    public void editModel(BillHeaderModel billHeaderModel) {

        if (Constants.isOnline(getContext())) {
            final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<BillHeaderModel> listCall = Constants.myInterface.editBill(billHeaderModel);
            listCall.enqueue(new Callback<BillHeaderModel>() {
                @Override
                public void onResponse(Call<BillHeaderModel> call, Response<BillHeaderModel> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("edit bill : ", "------------" + response.body());

                            BillHeaderModel data = response.body();
                            if (data == null) {
                                commonDialog.dismiss();
                            } else {

                                Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();

                                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                                ft.replace(R.id.content_frame, new ViewBillsFragment(), "HomeFragment");
                                ft.commit();

                                commonDialog.dismiss();
                            }
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
                public void onFailure(Call<BillHeaderModel> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(getContext(), "No Internet Connection!", Toast.LENGTH_SHORT).show();
        }
    }

}
