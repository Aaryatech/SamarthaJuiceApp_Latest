package com.ats.taborderingsystem.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ats.taborderingsystem.R;
import com.ats.taborderingsystem.adapter.MenuOrderAdapter;
import com.ats.taborderingsystem.constant.Constants;
import com.ats.taborderingsystem.model.Admin;
import com.ats.taborderingsystem.model.BillItemsAdapter;
import com.ats.taborderingsystem.model.ErrorMessage;
import com.ats.taborderingsystem.model.OrderDetailsList;
import com.ats.taborderingsystem.model.OrderHeaderModel;
import com.ats.taborderingsystem.printer.PrintHelper;
import com.ats.taborderingsystem.printer.PrintReceiptType;
import com.ats.taborderingsystem.util.CommonDialog;
import com.ats.taborderingsystem.util.CustomSharedPreference;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GenerateBillActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvTable, tvAmount, tvDate;
    private RecyclerView recyclerView;
    private EditText edDiscount;
    private Button btnBill;

    private ArrayList<OrderHeaderModel> orderList = new ArrayList<>();
    private ArrayList<OrderDetailsList> orderItemList = new ArrayList<>();

    BillItemsAdapter adapter;

    int tableNo, userId, venueId;
    String tableName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_bill);

        setTitle("Bill");

        tvTable = findViewById(R.id.tvTable);
        tvAmount = findViewById(R.id.tvAmount);
        tvDate = findViewById(R.id.tvDate);
        recyclerView = findViewById(R.id.recyclerView);
        edDiscount = findViewById(R.id.edDiscount);
        btnBill = findViewById(R.id.btnBill);
        btnBill.setOnClickListener(this);

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        tvDate.setText("Date : " + sdf.format(calendar.getTimeInMillis()));

        tableNo = getIntent().getIntExtra("table", 0);
        tableName = getIntent().getStringExtra("tableName");

        tvTable.setText("Table No. " + tableName);
        tvAmount.setText("Amount : ");

        Gson gson = new Gson();
        String jsonAdmin = CustomSharedPreference.getString(this, CustomSharedPreference.KEY_ADMIN);
        final Admin admin = gson.fromJson(jsonAdmin, Admin.class);

        Log.e("Admin : ", "---------------------------" + admin);

        if (admin != null) {
            userId = admin.getAdminId();
            venueId = admin.getVenueId();
        }


        getOrdersByTable(tableNo);

    }

    public void getOrdersByTable(int tableNo) {

        if (Constants.isOnline(this)) {
            final CommonDialog commonDialog = new CommonDialog(this, "Loading", "Please Wait...");
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
                            } else {

                                orderList.clear();
                                orderList = data;

                                for (int i = 0; i < orderList.size(); i++) {
                                    for (int j = 0; j < orderList.get(i).getOrderDetailsList().size(); j++) {
                                        orderItemList.add(orderList.get(i).getOrderDetailsList().get(j));
                                    }
                                }

                                adapter = new BillItemsAdapter(orderItemList, getApplicationContext());
                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(GenerateBillActivity.this);
                                recyclerView.setLayoutManager(mLayoutManager);
                                recyclerView.setItemAnimator(new DefaultItemAnimator());
                                recyclerView.setAdapter(adapter);

                                float total = 0;
                                for (int i = 0; i < orderList.size(); i++) {
                                    total = total + orderList.get(i).getOrderTotal();
                                }
                                tvAmount.setText("Amount : " + total);

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
                public void onFailure(Call<ArrayList<OrderHeaderModel>> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(this, "No Internet Connection!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnBill) {

            if (edDiscount.getText().toString().isEmpty()) {
                edDiscount.setError("required");
            } else {
                edDiscount.setError(null);

                float discount = Float.parseFloat(edDiscount.getText().toString());

                generateBill(tableNo, userId, discount, venueId);
            }

        }
    }

    public void generateBill(int tableNo, int userId, final float discount, int venuId) {

        if (Constants.isOnline(this)) {
            final CommonDialog commonDialog = new CommonDialog(this, "Loading", "Please Wait...");
            commonDialog.show();

            Call<ErrorMessage> listCall = Constants.myInterface.generateBill(userId, discount, tableNo, venuId);

            listCall.enqueue(new Callback<ErrorMessage>() {
                @Override
                public void onResponse(Call<ErrorMessage> call, Response<ErrorMessage> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("generate bill : ", "------------" + response.body());

                            ErrorMessage data = response.body();
                            if (data == null) {
                                Toast.makeText(GenerateBillActivity.this, "Unable to process!", Toast.LENGTH_SHORT).show();
                                commonDialog.dismiss();
                            } else {

                                if (data.isError()) {
                                    Toast.makeText(GenerateBillActivity.this, "Unable to process!", Toast.LENGTH_SHORT).show();
                                } else {

                                    try {
                                        String ip = CustomSharedPreference.getString(GenerateBillActivity.this, CustomSharedPreference.KEY_BILL_IP);
                                        PrintHelper printHelper = new PrintHelper(GenerateBillActivity.this, ip, orderList, tableName, discount,data.getMessage(), PrintReceiptType.BILL);
                                        printHelper.runPrintReceiptSequence();
                                    } catch (Exception e) {
                                    }

                                    AlertDialog.Builder builder = new AlertDialog.Builder(GenerateBillActivity.this, R.style.AlertDialogTheme);
                                    builder.setTitle("Success");
                                    builder.setMessage("Bill Generated");
                                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            startActivity(new Intent(GenerateBillActivity.this, HomeActivity.class));
                                             finish();
                                        }
                                    });
                                    AlertDialog dialog = builder.create();
                                    dialog.show();
                                }

                                commonDialog.dismiss();
                            }
                        } else {
                            commonDialog.dismiss();
                            Toast.makeText(GenerateBillActivity.this, "Unable to process!", Toast.LENGTH_SHORT).show();
                            Log.e("Data Null : ", "-----------");
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Toast.makeText(GenerateBillActivity.this, "Unable to process!", Toast.LENGTH_SHORT).show();
                        Log.e("Exception : ", "-----------" + e.getMessage());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ErrorMessage> call, Throwable t) {
                    commonDialog.dismiss();
                    Toast.makeText(GenerateBillActivity.this, "Unable to process!", Toast.LENGTH_SHORT).show();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(this, "No Internet Connection!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return super.onRetainCustomNonConfigurationInstance();
    }

    @Nullable
    @Override
    public Object getLastNonConfigurationInstance() {
        return super.getLastNonConfigurationInstance();
    }


}
