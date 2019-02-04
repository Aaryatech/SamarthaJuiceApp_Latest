package com.ats.taborderingsystem.fragment;


import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ats.taborderingsystem.R;
import com.ats.taborderingsystem.activity.MenuActivity;
import com.ats.taborderingsystem.adapter.MenuOrderAdapter;
import com.ats.taborderingsystem.constant.Constants;
import com.ats.taborderingsystem.model.CancelMessageModel;
import com.ats.taborderingsystem.model.CategoryItemModel;
import com.ats.taborderingsystem.model.ErrorMessage;
import com.ats.taborderingsystem.model.OrderHeaderModel;
import com.ats.taborderingsystem.util.CommonDialog;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ats.taborderingsystem.activity.MenuActivity.orderDetailIdStaticList;

public class CancelOrderFragment extends Fragment {

    private RecyclerView recyclerView;
    private Button btnCancelOrder;
    private TextView tvTable,tvAmount;

    int tableNo;
    String tableName;

    private ArrayList<OrderHeaderModel> orderList = new ArrayList<>();
    MenuOrderAdapter orderAdapter;

    private ArrayList<CategoryItemModel> categoryWiseItemList = new ArrayList<>();
    private ArrayList<String> cancelMessageList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cancel_order, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        btnCancelOrder = view.findViewById(R.id.btnCancelOrder);
        tvAmount = view.findViewById(R.id.tvAmount);
        tvTable = view.findViewById(R.id.tvTable);

        tableNo = getArguments().getInt("table", 0);
        tableName = getArguments().getString("tableName");

        tvTable.setText(""+tableName);

        getOrdersByTable(tableNo);
        getAllCancelMessages();

        btnCancelOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (orderDetailIdStaticList.size() > 0) {
                    showCancelDialog(orderDetailIdStaticList);
                } else {
                    Toast.makeText(getActivity(), "Please select item to cancel!", Toast.LENGTH_SHORT).show();
                }
            }
        });


        return view;
    }

    public void getOrdersByTable(int tableNo) {

        if (Constants.isOnline(getContext())) {
            final CommonDialog commonDialog = new CommonDialog(getActivity(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<ArrayList<OrderHeaderModel>> listCall = Constants.myInterface.getOrdersByTable(tableNo);
            listCall.enqueue(new Callback<ArrayList<OrderHeaderModel>>() {
                @Override
                public void onResponse(Call<ArrayList<OrderHeaderModel>> call, Response<ArrayList<OrderHeaderModel>> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("order Data : ", "------------" + response.body());

                            ArrayList<OrderHeaderModel> data = response.body();
                            if (data == null) {
                                commonDialog.dismiss();
                                btnCancelOrder.setVisibility(View.INVISIBLE);
                            } else {

                                orderList.clear();

                                for (int i = 0; i < data.size(); i++) {
                                    int count = 0;
                                    for (int j = 0; j < data.get(i).getOrderDetailsList().size(); j++) {
                                        if (data.get(i).getOrderDetailsList().get(j).getStatus() == 1) {
                                            count++;
                                        }
                                    }
                                    if (count > 0) {
                                        orderList.add(data.get(i));
                                    }
                                }

                                if (orderList.size() > 0) {
                                    btnCancelOrder.setVisibility(View.VISIBLE);
                                } else {
                                    btnCancelOrder.setVisibility(View.INVISIBLE);
                                }
                                //orderList = data;

                                for (int i = 0; i < orderList.size(); i++) {
                                    for (int j = 0; j < orderList.get(i).getOrderDetailsList().size(); j++) {
                                        orderList.get(i).getOrderDetailsList().get(j).setChecked(false);
                                    }
                                }

                                orderAdapter = new MenuOrderAdapter(orderList, getContext());
                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                                recyclerView.setLayoutManager(mLayoutManager);
                                recyclerView.setItemAnimator(new DefaultItemAnimator());
                                recyclerView.setAdapter(orderAdapter);

                                float total = 0;
                                for (int i = 0; i < orderList.size(); i++) {
                                    total = total + orderList.get(i).getOrderTotal();
                                }
                                tvAmount.setText("Amount : " + total);

                                commonDialog.dismiss();
                            }
                        } else {
                            commonDialog.dismiss();
                            btnCancelOrder.setVisibility(View.INVISIBLE);
                            Log.e("Data Null : ", "-----------");
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        btnCancelOrder.setVisibility(View.INVISIBLE);
                        Log.e("Exception : ", "-----------" + e.getMessage());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<OrderHeaderModel>> call, Throwable t) {
                    commonDialog.dismiss();
                    btnCancelOrder.setVisibility(View.INVISIBLE);
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(getActivity(), "No Internet Connection!", Toast.LENGTH_SHORT).show();
        }
    }

    public void showCancelDialog(final ArrayList<Integer> orderDetailIdList) {
        Log.e("CANCEL", "----------------" + orderDetailIdStaticList);

        final Dialog openDialog = new Dialog(getContext());
        openDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        openDialog.setContentView(R.layout.dialog_cancel_order);
        openDialog.setCancelable(false);
        openDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        final RadioButton rbCancel = openDialog.findViewById(R.id.rbCancel);
        final RadioButton rbNC1 = openDialog.findViewById(R.id.rbNC1);
        final RadioButton rbNC2 = openDialog.findViewById(R.id.rbNC2);
        final RadioButton rbNC3 = openDialog.findViewById(R.id.rbNC3);

        final EditText edRemark = openDialog.findViewById(R.id.edRemark);
        final Spinner spinner = openDialog.findViewById(R.id.spinner);

        Button btnCancel = openDialog.findViewById(R.id.btnCancel);
        Button btnSubmit = openDialog.findViewById(R.id.btnSubmit);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog.dismiss();
            }
        });

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, cancelMessageList);
        spinner.setAdapter(spinnerAdapter);


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int status = 2;
                String remark = "";
                if (rbCancel.isChecked()) {
                    status = 2;
                } else if (rbNC1.isChecked()) {
                    status = 3;
                } else if (rbNC2.isChecked()) {
                    status = 4;
                } else if (rbNC3.isChecked()) {
                    status = 5;
                }

                if (spinner.getSelectedItemPosition() == 0) {
                    TextView view = (TextView) spinner.getSelectedView();
                    view.setError("required");
                } else {
                    TextView view = (TextView) spinner.getSelectedView();
                    view.setError(null);

                    remark = spinner.getSelectedItem().toString();

                    cancelOrder(orderDetailIdList, status, remark);
                    openDialog.dismiss();
                }


            }
        });

        openDialog.show();
    }

    public void cancelOrder(ArrayList<Integer> orderDetailIds, int status, String remark) {
        Log.e("Parameters : ", "Order Detail Id List : ------------------" + orderDetailIds);
        if (Constants.isOnline(getContext())) {
            final CommonDialog commonDialog = new CommonDialog(getActivity(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<ErrorMessage> listCall = Constants.myInterface.cancelOrder(orderDetailIds, status, remark);
            listCall.enqueue(new Callback<ErrorMessage>() {
                @Override
                public void onResponse(Call<ErrorMessage> call, Response<ErrorMessage> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("cancel order: ", "------------" + response.body());

                            ErrorMessage data = response.body();
                            if (data == null) {
                                Toast.makeText(getActivity(), "Unable to process!", Toast.LENGTH_SHORT).show();
                                commonDialog.dismiss();
                            } else {

                                Toast.makeText(getActivity(), "Order Cancelled", Toast.LENGTH_SHORT).show();
                                getOrdersByTable(tableNo);

                                commonDialog.dismiss();
                            }
                        } else {
                            Toast.makeText(getActivity(), "Unable to process!", Toast.LENGTH_SHORT).show();

                            commonDialog.dismiss();
                            Log.e("Data Null : ", "-----------");
                        }
                    } catch (Exception e) {
                        Toast.makeText(getActivity(), "Unable to process!", Toast.LENGTH_SHORT).show();
                        commonDialog.dismiss();
                        Log.e("Exception : ", "-----------" + e.getMessage());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ErrorMessage> call, Throwable t) {
                    Toast.makeText(getActivity(), "Unable to process!", Toast.LENGTH_SHORT).show();
                    commonDialog.dismiss();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(getActivity(), "No Internet Connection!", Toast.LENGTH_SHORT).show();
        }
    }

    public void getAllCancelMessages() {

        if (Constants.isOnline(getContext())) {
            final CommonDialog commonDialog = new CommonDialog(getActivity(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<ArrayList<CancelMessageModel>> listCall = Constants.myInterface.getAllMessages();

            listCall.enqueue(new Callback<ArrayList<CancelMessageModel>>() {
                @Override
                public void onResponse(Call<ArrayList<CancelMessageModel>> call, Response<ArrayList<CancelMessageModel>> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("free Data : ", "------------" + response.body());

                            ArrayList<CancelMessageModel> data = response.body();
                            if (data == null) {
                                commonDialog.dismiss();
                            } else {

                                cancelMessageList.clear();
                                cancelMessageList.add("Select Remark");
                                for (int i = 0; i < data.size(); i++) {
                                    cancelMessageList.add(data.get(i).getMsgTitle());
                                }

                                commonDialog.dismiss();
                            }
                        } else {
                            commonDialog.dismiss();
                            Log.e("Data Null : ", "-----------");
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        //  Toast.makeText(getContext(), "No categories found", Toast.LENGTH_SHORT).show();
                        Log.e("Exception : ", "-----------" + e.getMessage());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<CancelMessageModel>> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(getActivity(), "No Internet Connection!", Toast.LENGTH_SHORT).show();
        }
    }

}
