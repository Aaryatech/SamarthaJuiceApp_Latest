package com.ats.samarthajuice.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ats.samarthajuice.R;
import com.ats.samarthajuice.constant.Constants;
import com.ats.samarthajuice.model.Admin;
import com.ats.samarthajuice.model.BillItemsAdapter;
import com.ats.samarthajuice.model.ErrorMessage;
import com.ats.samarthajuice.model.OrderDetailsList;
import com.ats.samarthajuice.model.OrderHeaderModel;
import com.ats.samarthajuice.model.TaxableDataForBillPrint;
import com.ats.samarthajuice.printer.PrintHelper;
import com.ats.samarthajuice.printer.PrintReceiptType;
import com.ats.samarthajuice.util.CommonDialog;
import com.ats.samarthajuice.util.CustomSharedPreference;
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
    private Button btnBill, btnBillPrint;
    private ImageView ivBack;

    private ArrayList<OrderHeaderModel> orderList = new ArrayList<>();
    private ArrayList<OrderDetailsList> orderItemList = new ArrayList<>();

    BillItemsAdapter adapter;

    int tableNo, userId, venueId;
    String tableName = "";
    float total;
    int discount;
    float finalTotal;
    float temp;

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
        btnBillPrint = findViewById(R.id.btnBillPrint);
        btnBillPrint.setOnClickListener(this);
        ivBack = findViewById(R.id.ivBack);
        ivBack.setOnClickListener(this);

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

                                //float total = 0;
                                for (int i = 0; i < orderList.size(); i++) {
                                    total = total + orderList.get(i).getOrderTotal();
                                }
                                tvAmount.setText("Amount : " + total);

                                finalTotal = total;
                                edDiscount.addTextChangedListener(new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                    }

                                    @Override
                                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                                        try {
                                            float disc = ((finalTotal * Float.parseFloat(s.toString())) / 100);
                                            total = finalTotal - disc;
                                            tvAmount.setText("" + total);

                                        } catch (Exception e) {
                                            tvAmount.setText("" + finalTotal);
                                        }

                                    }

                                    @Override
                                    public void afterTextChanged(Editable s) {

                                    }
                                });


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

            generateBill(tableNo, userId, 0, venueId,0);

        } else if (v.getId() == R.id.ivBack) {
            finish();
        } else if (v.getId() == R.id.btnBillPrint) {
            generateBill(tableNo, userId, 0, venueId,1);

        }
    }

    private void DisplayDialog() {
        final Dialog openDialog = new Dialog(GenerateBillActivity.this);
        openDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        openDialog.setContentView(R.layout.dialog_discount);
        openDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        TextView tvAmount = openDialog.findViewById(R.id.tv_amount);
        TextView tvDiscount = openDialog.findViewById(R.id.tv_discount);
        TextView tvbillAmount = openDialog.findViewById(R.id.tv_billAmount);
        Button btnCancel = openDialog.findViewById(R.id.btnCancel);
        Button btnBill = openDialog.findViewById(R.id.btnBill);

        discount = Integer.parseInt(edDiscount.getText().toString());

        tvAmount.setText("Amount : " + total);
        tvbillAmount.setText("Bill Amount : " + finalTotal);
        tvDiscount.setText("Discount : " + discount + "%");


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openDialog.dismiss();
            }
        });

        btnBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateBill(tableNo, userId, discount, venueId,0);
                openDialog.dismiss();
            }
        });

        openDialog.show();


    }

    public void generateBill(int tableNo, int userId, final float discount, int venuId, final int print) {

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


                                    Toast.makeText(GenerateBillActivity.this, "Bill Generated Successfully", Toast.LENGTH_SHORT).show();

                                    if (print == 1) {

                                        try{

                                            String billData=data.getMessage();
                                            String billNo=billData.substring(0,billData.indexOf('#'));
                                            String billId=billData.substring(billData.indexOf('#')+1);
                                            int bId=Integer.parseInt(billId);

                                            Log.e("DATA"," ------------------ "+data.getMessage());

                                            Log.e("BILL NO"," ------------------ "+billNo);
                                            Log.e("BILL ID"," ------------------ "+bId);

                                            getTaxDataForBill(bId,billNo);

                                        }catch (Exception e){
                                            e.printStackTrace();
                                        }




                                       /* try {
                                            String ip = CustomSharedPreference.getStringPrinter(GenerateBillActivity.this, CustomSharedPreference.KEY_BILL_IP);
                                            PrintHelper printHelper = new PrintHelper(GenerateBillActivity.this, ip, orderList, tableName, discount, data.getMessage(), PrintReceiptType.BILL);
                                            printHelper.runPrintReceiptSequence();
                                        } catch (Exception e) {
                                        }*/
                                    }else{

                                        startActivity(new Intent(GenerateBillActivity.this, HomeActivity.class));
                                        finish();
                                    }

                                    /*AlertDialog.Builder builder = new AlertDialog.Builder(GenerateBillActivity.this, R.style.AlertDialogTheme);
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
                                    dialog.show();*/


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


    public void getTaxDataForBill(int billId, final String billNo) {

        ArrayList<Integer> billIds=new ArrayList<>();
        billIds.add(billId);

        Log.e("Taxable Data : ", "------PARAM------" + billId);

        if (Constants.isOnline(this)) {
            final CommonDialog commonDialog = new CommonDialog(this, "Loading", "Please Wait...");
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
                                String ip = CustomSharedPreference.getStringPrinter(GenerateBillActivity.this, CustomSharedPreference.KEY_BILL_IP);
                                PrintHelper printHelper = new PrintHelper(GenerateBillActivity.this, ip, orderList, tableName, discount, billNo,data, PrintReceiptType.BILL);
                                printHelper.runPrintReceiptSequence();

                            } catch (Exception e) {
                            }

                            startActivity(new Intent(GenerateBillActivity.this, HomeActivity.class));
                            finish();

                            commonDialog.dismiss();
                        } else {
                            commonDialog.dismiss();
                            Log.e("Data Null : ", "-----------");
                            startActivity(new Intent(GenerateBillActivity.this, HomeActivity.class));
                            finish();
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Log.e("Exception : ", "-----------" + e.getMessage());
                        e.printStackTrace();

                        startActivity(new Intent(GenerateBillActivity.this, HomeActivity.class));
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<TaxableDataForBillPrint>> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();

                    startActivity(new Intent(GenerateBillActivity.this, HomeActivity.class));
                    finish();
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
