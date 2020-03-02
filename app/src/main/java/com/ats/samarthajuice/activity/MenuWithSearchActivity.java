package com.ats.samarthajuice.activity;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ats.samarthajuice.adapter.MenuOrderAdapter;
import com.ats.samarthajuice.R;
import com.ats.samarthajuice.adapter.MenuItemForSearchAdapter;
import com.ats.samarthajuice.adapter.TempSelectedItemAdapter;
import com.ats.samarthajuice.adapter.VerifyOrderItemModelAdapter;
import com.ats.samarthajuice.constant.Constants;
import com.ats.samarthajuice.model.Admin;
import com.ats.samarthajuice.model.CancelMessageModel;
import com.ats.samarthajuice.model.CategoryItemModel;
import com.ats.samarthajuice.model.CategoryMenuModel;
import com.ats.samarthajuice.model.ErrorMessage;
import com.ats.samarthajuice.model.ItemModel;
import com.ats.samarthajuice.model.OrderDetails;
import com.ats.samarthajuice.model.OrderDetailsList;
import com.ats.samarthajuice.model.OrderHeaderModel;
import com.ats.samarthajuice.model.OrderModel;
import com.ats.samarthajuice.printer.PrintHelper;
import com.ats.samarthajuice.printer.PrintReceiptType;
import com.ats.samarthajuice.util.CommonDialog;
import com.ats.samarthajuice.util.CustomSharedPreference;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuWithSearchActivity extends AppCompatActivity implements View.OnClickListener {

    private GridView gridView;
    private RecyclerView rvOrder, rvTempOrder,recyclerView;
    private TextView tvTable, tvAmount, tvDate;
    private ListView listView;
    private LinearLayout llVerify, llButton;
    private EditText edSearch, edCode, edQty;
    private Button btnCancel, btnBill, btnCancelOrder, btnPlaceOrder, btnPlaceOrderTemp;
    private ImageView imageView, ivSend,ivSend1,ivBack;
    public static Spinner spRemark;

    private ArrayList<OrderHeaderModel> orderList = new ArrayList<>();
    MenuOrderAdapter orderAdapter;

    private ArrayList<CategoryItemModel> categoryWiseItemList = new ArrayList<>();
    private ArrayList<String> cancelMessageList = new ArrayList<>();

    public static ArrayList<Integer> orderDetailIdStaticList = new ArrayList<>();
    public static ArrayList<OrderDetailsList> orderDetailStaticList = new ArrayList<>();

    int tableNo, userId;
    String tableName;

    private ArrayList<ItemModel> itemList = new ArrayList<>();
    MenuItemForSearchAdapter itemForSearchAdapter;

    private ArrayList<CategoryMenuModel> categoryList = new ArrayList<>();

    private BroadcastReceiver broadcastReceiver,broadcastReceiverTemp;

    public static ArrayList<ItemModel> staticItemList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        staticItemList.clear();

        if (Constants.isTablet(this)) {
            if (getResources().getConfiguration().orientation == 1) {
                setContentView(R.layout.activity_menu_with_search);
            } else {
                setContentView(R.layout.activity_menu_with_search);
            }
        } else {
            if (getResources().getConfiguration().orientation == 1) {
                setContentView(R.layout.activity_menu_with_search_phone_port);
            } else {
                setContentView(R.layout.activity_menu_with_search);
            }
        }
        setTitle("Menu");

        gridView = findViewById(R.id.gridView);
        listView = findViewById(R.id.listView);
        rvOrder = findViewById(R.id.rvOrder);
        tvTable = findViewById(R.id.tvTable);
        tvAmount = findViewById(R.id.tvAmount);
        tvDate = findViewById(R.id.tvDate);
        llVerify = findViewById(R.id.llVerify);
        edSearch = findViewById(R.id.edSearch);
        btnCancel = findViewById(R.id.btnCancel);
        llButton = findViewById(R.id.llButton);
        btnBill = findViewById(R.id.btnBill);
        imageView = findViewById(R.id.imageView);
        btnCancelOrder = findViewById(R.id.btnCancelOrder);
        btnPlaceOrder = findViewById(R.id.btnPlaceOrder);
        btnPlaceOrderTemp = findViewById(R.id.btnPlaceOrderTemp);
        ivBack = findViewById(R.id.ivBack);
        spRemark = findViewById(R.id.spRemark);

        rvTempOrder = findViewById(R.id.rvTempOrder);

        edCode = findViewById(R.id.edCode);
        edQty = findViewById(R.id.edQty);
        ivSend = findViewById(R.id.ivSend);
        ivSend1 = findViewById(R.id.ivSend1);
        edCode.requestFocus();

        llVerify.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        llButton.setOnClickListener(this);
        btnBill.setOnClickListener(this);
        imageView.setOnClickListener(this);
        btnCancelOrder.setOnClickListener(this);
        btnPlaceOrder.setOnClickListener(this);
        ivBack.setOnClickListener(this);

        btnPlaceOrderTemp.setOnClickListener(this);
        ivSend.setOnClickListener(this);
        ivSend1.setOnClickListener(this);

        llButton.setVisibility(View.GONE);

        ArrayAdapter<String> spAdapter=new ArrayAdapter<String>(this,R.layout.custom_spinner_layout,getResources().getStringArray(R.array.remarkarray));
        spRemark.setAdapter(spAdapter);

        Gson gson = new Gson();
        String jsonAdmin = CustomSharedPreference.getString(this, CustomSharedPreference.KEY_ADMIN);
        final Admin admin = gson.fromJson(jsonAdmin, Admin.class);

        Log.e("Admin : ", "---------------------------" + admin);

        if (admin != null) {
            userId = admin.getAdminId();
            if (admin.getType().equalsIgnoreCase("Captain")) {
                btnBill.setVisibility(View.GONE);
            }
        }

        tableNo = getIntent().getIntExtra("table", 0);
        tableName = getIntent().getStringExtra("tableName");

        tvTable.setText("Table No. " + tableName);
        tvAmount.setText("Amount : ");

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        tvDate.setText("Date : " + sdf.format(calendar.getTimeInMillis()));

        getOrdersByTable(tableNo);
        getAllCancelMessages();
        getAllItems();
        getCategoryMenu();

        edSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (itemForSearchAdapter != null) {
                    itemForSearchAdapter.getFilter().filter(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        edQty.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    ivSend.performClick();
                    return true;
                }
                return false;
            }
        });

        edQty.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction()==KeyEvent.KEYCODE_TAB|| event.getAction()==KeyEvent.KEYCODE_ENTER){
                    ivSend.performClick();
                    return true;
                }

                return false;
            }
        });


        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals("REFRESH")) {
                    handlePushNotification(intent);
                }
            }
        };


        broadcastReceiverTemp = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals("REFRESH_TEMP")) {
                    handlePushNotificationTemp(intent);
                }
            }
        };



    }


    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiverTemp);

    }

    @Override
    protected void onResume() {
        super.onResume();


        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,
                new IntentFilter("REFRESH"));
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiverTemp,
                new IntentFilter("REFRESH_TEMP"));


    }


    private void handlePushNotification(Intent intent) {

        int itemId = intent.getIntExtra("itemId",0);

        for(int i=0;i<itemList.size();i++){
            if (itemId==itemList.get(i).getItemId()){
                itemList.get(i).setQty(0);
                itemList.get(i).setCancelStatus(false);
            }
        }

        itemForSearchAdapter.notifyDataSetChanged();

        tempSelectedItemList();

    }

    private void handlePushNotificationTemp(Intent intent) {

        edQty.setText("");
        edCode.setText("");
        spRemark.setSelection(0);
        edCode.requestFocus();


        TempSelectedItemAdapter adapter = new TempSelectedItemAdapter(staticItemList, MenuWithSearchActivity.this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(MenuWithSearchActivity.this);
        rvTempOrder.setLayoutManager(mLayoutManager);
        rvTempOrder.setItemAnimator(new DefaultItemAnimator());
        rvTempOrder.setAdapter(adapter);

       /* tempSelectedItemList();*/

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
                                llButton.setVisibility(View.GONE);
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
                                    llButton.setVisibility(View.GONE);
                                } else {
                                    llButton.setVisibility(View.GONE);
                                }
                                //orderList = data;

                                for (int i = 0; i < orderList.size(); i++) {
                                    for (int j = 0; j < orderList.get(i).getOrderDetailsList().size(); j++) {
                                        orderList.get(i).getOrderDetailsList().get(j).setChecked(false);
                                    }
                                }

                                orderAdapter = new MenuOrderAdapter(orderList, getApplicationContext());
                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(MenuWithSearchActivity.this);
                                rvOrder.setLayoutManager(mLayoutManager);
                                rvOrder.setItemAnimator(new DefaultItemAnimator());
                                rvOrder.setAdapter(orderAdapter);

                                float total = 0;
                                for (int i = 0; i < orderList.size(); i++) {
                                    total = total + orderList.get(i).getOrderTotal();
                                }
                                tvAmount.setText("Amount : " + total);

                                commonDialog.dismiss();
                            }
                        } else {
                            commonDialog.dismiss();
                            llButton.setVisibility(View.GONE);
                            Log.e("Data Null : ", "-----------");
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        llButton.setVisibility(View.GONE);
                        Log.e("Exception : ", "-----------" + e.getMessage());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<OrderHeaderModel>> call, Throwable t) {
                    commonDialog.dismiss();
                    llButton.setVisibility(View.GONE);
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(this, "No Internet Connection!", Toast.LENGTH_SHORT).show();
        }
    }

    public void showVerifyDialog(final ArrayList<ItemModel> itemArrayList) {
        final Dialog openDialog = new Dialog(MenuWithSearchActivity.this);
        openDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        openDialog.setContentView(R.layout.dialog_verify_order);
        openDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        Window window = openDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        // wlp.gravity = Gravity.TOP | Gravity.RIGHT;
        wlp.x = 100;
        wlp.y = 100;
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);

         recyclerView = openDialog.findViewById(R.id.recyclerView);
        Button btnCancel = openDialog.findViewById(R.id.btnCancel);
        Button btnOrder = openDialog.findViewById(R.id.btnOrder);

        VerifyOrderItemModelAdapter verifyAdapter = new VerifyOrderItemModelAdapter(itemArrayList, MenuWithSearchActivity.this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(MenuWithSearchActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(verifyAdapter);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemForSearchAdapter.notifyDataSetChanged();
                openDialog.dismiss();
            }
        });

        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<OrderDetails> orderDetailArray = new ArrayList<>();

                for (int i = 0; i < itemArrayList.size(); i++) {
                    if (itemArrayList.get(i).getQty() > 0) {
                        ItemModel item = itemArrayList.get(i);
                        OrderDetails orderDetail = new OrderDetails(0, 0, item.getItemId(), item.getQty(), item.getMrpRegular(), 1, 0, "", item.getItemName());
                        orderDetailArray.add(orderDetail);
                    }
                }

                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

                OrderModel orderModel = new OrderModel(0, userId, tableNo, 1, sdf.format(calendar.getTimeInMillis()), sdf1.format(calendar.getTimeInMillis()), 1, orderDetailArray);

                if (orderDetailArray.size() > 0) {
                    Log.e("BEAN : ", "--------------------" + orderModel);
                    saveOrder(orderModel);
                    openDialog.dismiss();

                } else {
                    Toast.makeText(MenuWithSearchActivity.this, "Please select item!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        openDialog.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        startActivity(new Intent(MenuWithSearchActivity.this, HomeActivity.class));
        finish();

    }

    public void showCancelDialog(final ArrayList<Integer> orderDetailIdList) {
        Log.e("CANCEL", "----------------" + orderDetailIdStaticList);

        final Dialog openDialog = new Dialog(MenuWithSearchActivity.this);
        openDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        openDialog.setContentView(R.layout.dialog_cancel_order);
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

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(MenuWithSearchActivity.this, android.R.layout.simple_spinner_dropdown_item, cancelMessageList);
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
        if (Constants.isOnline(this)) {
            final CommonDialog commonDialog = new CommonDialog(this, "Loading", "Please Wait...");
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
                                Toast.makeText(MenuWithSearchActivity.this, "Unable to process!", Toast.LENGTH_SHORT).show();
                                commonDialog.dismiss();
                            } else {

                                try {
                                    String ip = CustomSharedPreference.getStringPrinter(MenuWithSearchActivity.this, CustomSharedPreference.KEY_KOT_IP);
                                    PrintHelper printHelper = new PrintHelper(MenuWithSearchActivity.this, ip, orderDetailStaticList, tableName, PrintReceiptType.VOID_KOT);
                                    printHelper.runPrintReceiptSequence();
                                } catch (Exception e) {
                                }


                                Toast.makeText(MenuWithSearchActivity.this, "Order Cancelled", Toast.LENGTH_SHORT).show();
                                getOrdersByTable(tableNo);

                                commonDialog.dismiss();
                            }
                        } else {
                            Toast.makeText(MenuWithSearchActivity.this, "Unable to process!", Toast.LENGTH_SHORT).show();

                            commonDialog.dismiss();
                            Log.e("Data Null : ", "-----------");
                        }
                    } catch (Exception e) {
                        Toast.makeText(MenuWithSearchActivity.this, "Unable to process!", Toast.LENGTH_SHORT).show();
                        commonDialog.dismiss();
                        Log.e("Exception : ", "-----------" + e.getMessage());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ErrorMessage> call, Throwable t) {
                    Toast.makeText(MenuWithSearchActivity.this, "Unable to process!", Toast.LENGTH_SHORT).show();
                    commonDialog.dismiss();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(this, "No Internet Connection!", Toast.LENGTH_SHORT).show();
        }
    }

    public void getAllCancelMessages() {

        if (Constants.isOnline(this)) {
            final CommonDialog commonDialog = new CommonDialog(this, "Loading", "Please Wait...");
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
            Toast.makeText(this, "No Internet Connection!", Toast.LENGTH_SHORT).show();
        }
    }

    public void saveOrder(final OrderModel order) {

        if (Constants.isOnline(this)) {
            final CommonDialog commonDialog = new CommonDialog(this, "Loading", "Please Wait...");
            commonDialog.show();

            Call<OrderHeaderModel> listCall = Constants.myInterface.saveOrder(order);
            listCall.enqueue(new Callback<OrderHeaderModel>() {
                @Override
                public void onResponse(Call<OrderHeaderModel> call, Response<OrderHeaderModel> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("Order Data : ", "------------" + response.body());

                            OrderHeaderModel data = response.body();
                            if (data == null) {
                                commonDialog.dismiss();
                            } else {

                                Toast.makeText(MenuWithSearchActivity.this, "Order Placed", Toast.LENGTH_SHORT).show();
                                getOrdersByTable(tableNo);

                                for (int i = 0; i < itemList.size(); i++) {
                                    itemList.get(i).setQty(0);
                                    itemList.get(i).setCancelStatus(false);
                                }

                                itemForSearchAdapter.notifyDataSetChanged();

                                ArrayList<OrderDetails> orderDetailsArray = (ArrayList<OrderDetails>) order.getOrderDetailList();

                                tempSelectedItemList();

                                try {
                                    String ip = CustomSharedPreference.getStringPrinter(MenuWithSearchActivity.this, CustomSharedPreference.KEY_KOT_IP);
                                    PrintHelper printHelper = new PrintHelper(MenuWithSearchActivity.this, ip, data, orderDetailsArray, tableName, PrintReceiptType.KOT);
                                    printHelper.runPrintReceiptSequence();
                                } catch (Exception e) {
                                }

                                onBackPressed();

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
                public void onFailure(Call<OrderHeaderModel> call, Throwable t) {
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


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.llVerify) {

            //showVerifyDialog(staticItemList);

            ArrayList<ItemModel> verifyItemList = new ArrayList<>();

            for (int i = 0; i < staticItemList.size(); i++) {
                if (staticItemList.get(i).getQty() > 0) {
                    verifyItemList.add(staticItemList.get(i));
                }
            }
            Log.e("Item Phone","---------------------------------------------------------"+staticItemList);
            Log.e("Item Phone","--------------------------Verify-------------------------------"+verifyItemList);

            if (verifyItemList.size() > 0) {
                showVerifyDialog(verifyItemList);
            } else {
                Toast.makeText(this, "Please select item!", Toast.LENGTH_SHORT).show();
            }

          /*  for (int i = 0; i < itemList.size(); i++) {
                itemList.get(i).setQty(qtyInt);
                itemList.get(i).setCancelStatus(true);
                itemList.get(i).setRemark(remark);
            }

            // tempSelectedItemList();

            edCode.setText("");
            edQty.setText("");
            edCode.requestFocus();
            spRemark.setSelection(0);

            ItemModel item=itemList.get(i);

            ItemModel newItem=new ItemModel(item.getItemId(),item.getItemName(),item.getMrpRegular(),item.getQty(),item.getRemark());
            staticItemList.add(newItem);

            TempSelectedItemAdapter adapter = new TempSelectedItemAdapter(staticItemList, MenuWithSearchActivity.this);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(MenuWithSearchActivity.this);
            rvTempOrder.setLayoutManager(mLayoutManager);
            rvTempOrder.setItemAnimator(new DefaultItemAnimator());
            rvTempOrder.setAdapter(adapter);*/



        } else if (v.getId() == R.id.btnCancel) {
            if (orderDetailIdStaticList.size() > 0) {
                showCancelDialog(orderDetailIdStaticList);
            } else {
                Toast.makeText(this, "Please select item to cancel!", Toast.LENGTH_SHORT).show();
            }

        } else if (v.getId() == R.id.btnBill) {
            Intent intent = new Intent(MenuWithSearchActivity.this, GenerateBillActivity.class);
            intent.putExtra("table", tableNo);
            intent.putExtra("tableName", tableName);
            startActivity(intent);
        } else if (v.getId() == R.id.btnCancelOrder) {

            Intent intent = new Intent(MenuWithSearchActivity.this, CancelOrderActivity.class);
            intent.putExtra("table", tableNo);
            intent.putExtra("tableName", tableName);
            startActivity(intent);
        }else if(v.getId()==R.id.ivSend1)
        {
            String code = edCode.getText().toString().trim();
            String qty = edQty.getText().toString().trim();

            String remark="";
            if (spRemark.getSelectedItemPosition()==0){
                remark="";
            }else{
                remark=spRemark.getSelectedItem().toString();
            }

            if (code.isEmpty()) {
                edCode.setError("required");
                edCode.requestFocus();
            } else if (qty.isEmpty()) {
                edCode.setError(null);
                edQty.setError("required");
                edQty.requestFocus();
            } else {
                edCode.setError(null);
                edQty.setError(null);

                int qtyInt = Integer.parseInt(qty);

                if (itemList != null) {

                    int isFound = 0;

                    for (int i = 0; i < itemList.size(); i++) {

                        if (code.equalsIgnoreCase(itemList.get(i).getItemDesc())) {
                            isFound = 1;

                            itemList.get(i).setQty(qtyInt);
                            itemList.get(i).setCancelStatus(true);
                            itemList.get(i).setRemark(remark);

                            // tempSelectedItemList();

                            edCode.setText("");
                            edQty.setText("");
                            edCode.requestFocus();
                            spRemark.setSelection(0);

                            ItemModel item = itemList.get(i);

                            ItemModel newItem = new ItemModel(item.getItemId(), item.getItemName(), item.getMrpRegular(), item.getQty(), item.getRemark());
                            staticItemList.add(newItem);

                            showVerifyDialog(staticItemList);

//                            VerifyOrderItemModelAdapter verifyAdapter = new VerifyOrderItemModelAdapter(staticItemList, MenuWithSearchActivity.this);
//                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(MenuWithSearchActivity.this);
//                            recyclerView.setLayoutManager(mLayoutManager);
//                            recyclerView.setItemAnimator(new DefaultItemAnimator());
//                            recyclerView.setAdapter(verifyAdapter);
                            break;
                        }

                    }

                    if (isFound == 0) {
                        Toast.makeText(this, "Item not found!", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        }
        else if (v.getId() == R.id.ivSend) {

            String code = edCode.getText().toString().trim();
            String qty = edQty.getText().toString().trim();

            String remark="";
            if (spRemark.getSelectedItemPosition()==0){
                remark="";
            }else{
                remark=spRemark.getSelectedItem().toString();
            }


            if (code.isEmpty()) {
                edCode.setError("required");
                edCode.requestFocus();
            } else if (qty.isEmpty()) {
                edCode.setError(null);
                edQty.setError("required");
                edQty.requestFocus();
            } else {
                edCode.setError(null);
                edQty.setError(null);

                int qtyInt = Integer.parseInt(qty);

                if (itemList != null) {

                    int isFound = 0;

                    for (int i = 0; i < itemList.size(); i++) {

                        if (code.equalsIgnoreCase(itemList.get(i).getItemDesc())) {
                            isFound = 1;

                            itemList.get(i).setQty(qtyInt);
                            itemList.get(i).setCancelStatus(true);
                            itemList.get(i).setRemark(remark);

                           // tempSelectedItemList();

                            edCode.setText("");
                            edQty.setText("");
                            edCode.requestFocus();
                            spRemark.setSelection(0);

                            ItemModel item=itemList.get(i);

                            ItemModel newItem=new ItemModel(item.getItemId(),item.getItemName(),item.getMrpRegular(),item.getQty(),item.getRemark());
                            staticItemList.add(newItem);

                            TempSelectedItemAdapter adapter = new TempSelectedItemAdapter(staticItemList, MenuWithSearchActivity.this);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(MenuWithSearchActivity.this);
                            rvTempOrder.setLayoutManager(mLayoutManager);
                            rvTempOrder.setItemAnimator(new DefaultItemAnimator());
                            rvTempOrder.setAdapter(adapter);
                            break;
                        }

                    }

                    if (isFound == 0) {
                        Toast.makeText(this, "Item not found!", Toast.LENGTH_SHORT).show();
                    }

                }

            }


        } else if (v.getId() == R.id.btnPlaceOrderTemp) {

            ArrayList<OrderDetails> orderDetailArray = new ArrayList<>();

            if (staticItemList!=null){

                if (!staticItemList.isEmpty()){

                    for (int i = 0; i < staticItemList.size(); i++) {
                        if (staticItemList.get(i).getQty() > 0) {
                            ItemModel item = staticItemList.get(i);
                            OrderDetails orderDetail = new OrderDetails(0, 0, item.getItemId(), item.getQty(), item.getMrpRegular(), 1, 0, item.getRemark(), item.getItemName());
                            orderDetailArray.add(orderDetail);
                        }
                    }

                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

                    OrderModel orderModel = new OrderModel(0, userId, tableNo, 1, sdf.format(calendar.getTimeInMillis()), sdf1.format(calendar.getTimeInMillis()), 1, orderDetailArray);

                    if (orderDetailArray.size() > 0) {
                        Log.e("BEAN : ", "--------------------" + orderModel);
                        saveOrder(orderModel);
                    } else {
                        Toast.makeText(MenuWithSearchActivity.this, "Please select item!", Toast.LENGTH_SHORT).show();
                    }


                }else{
                    Toast.makeText(MenuWithSearchActivity.this, "Please select item!", Toast.LENGTH_SHORT).show();
                }
            }



        }else if (v.getId()==R.id.ivBack){
            onBackPressed();
        }
    }


    public void tempSelectedItemList() {
        ArrayList<ItemModel> verifyItemList = new ArrayList<>();

        for (int i = 0; i < itemList.size(); i++) {
            if (itemList.get(i).getQty() > 0) {
                verifyItemList.add(itemList.get(i));
            }
        }

        edCode.requestFocus();

        Log.e("ITEMS ", " -------------- " + verifyItemList);

        TempSelectedItemAdapter adapter = new TempSelectedItemAdapter(verifyItemList, MenuWithSearchActivity.this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(MenuWithSearchActivity.this);
        rvTempOrder.setLayoutManager(mLayoutManager);
        rvTempOrder.setItemAnimator(new DefaultItemAnimator());
        rvTempOrder.setAdapter(adapter);

        itemForSearchAdapter = new MenuItemForSearchAdapter(itemList, MenuWithSearchActivity.this);
        listView.setAdapter(itemForSearchAdapter);


        spRemark.setSelection(0); edCode.requestFocus();

    }


    public void getAllItems() {

        if (Constants.isOnline(MenuWithSearchActivity.this)) {
            final CommonDialog commonDialog = new CommonDialog(MenuWithSearchActivity.this, "Loading", "Please Wait...");
            commonDialog.show();

            Call<ArrayList<ItemModel>> listCall = Constants.myInterface.getAllItemsByDelStatus();
            listCall.enqueue(new Callback<ArrayList<ItemModel>>() {
                @Override
                public void onResponse(Call<ArrayList<ItemModel>> call, Response<ArrayList<ItemModel>> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("Category Data : ", "------------" + response.body());

                            ArrayList<ItemModel> data = response.body();
                            if (data == null) {
                                commonDialog.dismiss();
                                Toast.makeText(MenuWithSearchActivity.this, "No items found", Toast.LENGTH_SHORT).show();
                            } else {

                                itemList.clear();
                                itemList = data;

                                itemForSearchAdapter = new MenuItemForSearchAdapter(itemList, MenuWithSearchActivity.this);
                                listView.setAdapter(itemForSearchAdapter);

                                commonDialog.dismiss();
                            }
                        } else {
                            commonDialog.dismiss();
                            Toast.makeText(MenuWithSearchActivity.this, "No items found", Toast.LENGTH_SHORT).show();
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
                public void onFailure(Call<ArrayList<ItemModel>> call, Throwable t) {
                    commonDialog.dismiss();
                    Toast.makeText(MenuWithSearchActivity.this, "No items found", Toast.LENGTH_SHORT).show();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(MenuWithSearchActivity.this, "No Internet Connection!", Toast.LENGTH_SHORT).show();
        }
    }

    public void getCategoryMenu() {

        if (Constants.isOnline(MenuWithSearchActivity.this)) {
            final CommonDialog commonDialog = new CommonDialog(MenuWithSearchActivity.this, "Loading", "Please Wait...");
            commonDialog.show();

            Call<ArrayList<CategoryMenuModel>> listCall = Constants.myInterface.getCategoryMenu();
            listCall.enqueue(new Callback<ArrayList<CategoryMenuModel>>() {
                @Override
                public void onResponse(Call<ArrayList<CategoryMenuModel>> call, Response<ArrayList<CategoryMenuModel>> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("Category Data : ", "------------" + response.body());

                            ArrayList<CategoryMenuModel> data = response.body();
                            if (data == null) {
                                commonDialog.dismiss();
                            } else {

                                categoryList.clear();
                                categoryList = data;

                                MenuCategoryForSearchAdapter categoryAdapter = new MenuCategoryForSearchAdapter(categoryList, MenuWithSearchActivity.this);
                                gridView.setAdapter(categoryAdapter);

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
                public void onFailure(Call<ArrayList<CategoryMenuModel>> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(MenuWithSearchActivity.this, "No Internet Connection!", Toast.LENGTH_SHORT).show();
        }
    }

    public class MenuCategoryForSearchAdapter extends BaseAdapter {

        private ArrayList<CategoryMenuModel> categoryModelList;
        private Context context;
        private LayoutInflater inflater = null;

        public MenuCategoryForSearchAdapter(ArrayList<CategoryMenuModel> categoryModelList, Context context) {
            this.categoryModelList = categoryModelList;
            this.context = context;
            this.inflater = (LayoutInflater) context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }


        @Override
        public int getCount() {
            return categoryModelList.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public class Holder {
            TextView tvName;
            ImageView imageView;
            LinearLayout linearLayout;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final Holder holder;
            View rowView = convertView;

            if (rowView == null) {
                holder = new Holder();
                LayoutInflater inflater = LayoutInflater.from(context);
                rowView = inflater.inflate(R.layout.adapter_menu_category, null);

                holder.tvName = rowView.findViewById(R.id.tvName);
                holder.linearLayout = rowView.findViewById(R.id.linearLayout);
                holder.imageView = rowView.findViewById(R.id.imageView);

                rowView.setTag(holder);

            } else {
                holder = (Holder) rowView.getTag();
            }

            holder.tvName.setText(categoryModelList.get(position).getCatName());

            try {
                Picasso.get().load(Constants.categoryImagePath + "" + categoryModelList.get(position).getCatImage())
                        .placeholder(R.drawable.samartha_logo)
                        .error(R.drawable.samartha_logo)
                        .into(holder.imageView);
            } catch (Exception e) {
            }

            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getItemPosition(categoryModelList.get(position).getCatName());

                    int posit = 0;

                    if (position == 0) {
                        posit = 0;
                    } else {
                        for (int i = position; i >= 0; i--) {
                            posit = posit + categoryModelList.get(i).getCount();
                        }
                    }
                    Log.e("POSITION : ", "-------------------" + posit);

                    smoothScrollToPositionFromTop(listView, posit);
                }
            });

            gridViewSetting(gridView);

            return rowView;
        }
    }

    private void gridViewSetting(GridView gridview) {

        int size = categoryList.size();
        // Log.e("Size : ", "----------" + size);
        // Calculated single Item Layout Width for each grid element ....
        int width = 130;//400

        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        display.getMetrics(dm);
        // getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        float density = dm.density;

        int totalWidth = (int) (width * size * density);
        //  Log.e("Total Width : ", "----------" + totalWidth);
        int singleItemWidth = (int) (width * density);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                totalWidth, LinearLayout.LayoutParams.MATCH_PARENT);

        gridview.setLayoutParams(params);
        gridview.setColumnWidth(130);
        gridview.setHorizontalSpacing(10);
        gridview.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
        gridview.setNumColumns(size);
    }

    public int getItemPosition(String name) {
        for (int i = 0; i < categoryList.size(); i++)
            if (categoryList.get(i).getCatName().equalsIgnoreCase(name))
                return i;
        return 0;
    }

    public static void smoothScrollToPositionFromTop(final AbsListView view, final int position) {
        View child = getChildAtPosition(view, position);
        // There's no need to scroll if child is already at top or view is already scrolled to its end
        if ((child != null) && ((child.getTop() == 0) || ((child.getTop() > 0) && !view.canScrollVertically(1)))) {
            return;
        }

        view.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(final AbsListView view, final int scrollState) {
                if (scrollState == SCROLL_STATE_IDLE) {
                    view.setOnScrollListener(null);

                    // Fix for scrolling bug
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            view.setSelection(position);
                        }
                    });
                }
            }

            @Override
            public void onScroll(final AbsListView view, final int firstVisibleItem, final int visibleItemCount,
                                 final int totalItemCount) {
            }
        });

        // Perform scrolling to position
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                view.smoothScrollToPositionFromTop(position, 0);
            }
        });
    }

    public static View getChildAtPosition(final AdapterView view, final int position) {
        final int index = position - view.getFirstVisiblePosition();
        if ((index >= 0) && (index < view.getChildCount())) {
            return view.getChildAt(index);
        } else {
            return null;
        }
    }


}
