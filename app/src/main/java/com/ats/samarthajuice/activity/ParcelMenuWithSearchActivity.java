package com.ats.samarthajuice.activity;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ats.samarthajuice.R;
import com.ats.samarthajuice.adapter.MenuItemForSearchAdapter;
import com.ats.samarthajuice.adapter.VerifyOrderItemModelAdapter;
import com.ats.samarthajuice.constant.Constants;
import com.ats.samarthajuice.model.Admin;
import com.ats.samarthajuice.model.CategoryItemModel;
import com.ats.samarthajuice.model.CategoryMenuModel;
import com.ats.samarthajuice.model.ErrorMessage;
import com.ats.samarthajuice.model.ItemModel;
import com.ats.samarthajuice.model.ParcelOrderDetails;
import com.ats.samarthajuice.model.ParcelOrderHeaderModel;
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

public class ParcelMenuWithSearchActivity extends AppCompatActivity implements View.OnClickListener {

    private GridView gridView;
    private TextView tvTable, tvAmount, tvDate, tvOrderCount;
    private ListView listView;
    private LinearLayout llVerify;
    private EditText edSearch, edCode, edQty;
    private ImageView imageView, ivSend,ivBack;

    private ArrayList<CategoryItemModel> categoryWiseItemList = new ArrayList<>();

    int userId, venueId;

    private BroadcastReceiver mBroadcastReceiver;
    Dialog openDialog;

    private ArrayList<ItemModel> itemList = new ArrayList<>();
    MenuItemForSearchAdapter itemForSearchAdapter;

    private ArrayList<CategoryMenuModel> categoryList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parcel_menu_with_search);

        setTitle("Menu");

        gridView = findViewById(R.id.gridView);
        listView = findViewById(R.id.listView);
        tvTable = findViewById(R.id.tvTable);
        tvDate = findViewById(R.id.tvDate);
        llVerify = findViewById(R.id.llVerify);
        edSearch = findViewById(R.id.edSearch);
        imageView = findViewById(R.id.imageView);
        ivBack = findViewById(R.id.ivBack);

        edCode = findViewById(R.id.edCode);
        edQty = findViewById(R.id.edQty);
        ivSend = findViewById(R.id.ivSend);
        tvOrderCount = findViewById(R.id.tvOrderCount);

        llVerify.setOnClickListener(this);
        imageView.setOnClickListener(this);
        ivSend.setOnClickListener(this);
        ivBack.setOnClickListener(this);

        Gson gson = new Gson();
        String jsonAdmin = CustomSharedPreference.getString(this, CustomSharedPreference.KEY_ADMIN);
        final Admin admin = gson.fromJson(jsonAdmin, Admin.class);

        //Log.e("Admin : ", "---------------------------" + admin);

        if (admin != null) {
            userId = admin.getAdminId();
            venueId = admin.getVenueId();
        }

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        tvDate.setText("Date : " + sdf.format(calendar.getTimeInMillis()));

        tvTable.setText("Parcel Order");

        getAllItems();
        getCategoryMenu();

        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals("REFRESH_DATA")) {
                    handlePushNotification(intent);
                }
            }
        };

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


    }

    @Override
    public void onPause() {

        LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastReceiver);
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();

        LocalBroadcastManager.getInstance(this).registerReceiver(mBroadcastReceiver,
                new IntentFilter("REFRESH_DATA"));

    }

    private void handlePushNotification(Intent intent) {

        //Log.e("handlePushNotification", "------------------------------------**********");
        openDialog.dismiss();
        itemForSearchAdapter.notifyDataSetChanged();

        ArrayList<ItemModel> itemModelList = new ArrayList<>();

        for (int i = 0; i < itemList.size(); i++) {
            if (itemList.get(i).getQty() > 0) {
                ItemModel item = itemList.get(i);
                itemModelList.add(item);
            }
        }

        if (itemList.size() > 0) {
            showVerifyDialog(itemModelList);
        }

    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.llVerify) {

            ArrayList<ItemModel> itemModelList = new ArrayList<>();

            for (int i = 0; i < itemList.size(); i++) {
                if (itemList.get(i).getQty() > 0) {
                    ItemModel item = itemList.get(i);
                    itemModelList.add(item);
                }
            }

            if (itemModelList.size() > 0) {
                showVerifyDialog(itemModelList);
            } else {
                Toast.makeText(this, "Please select item!", Toast.LENGTH_SHORT).show();
            }
        } else if (v.getId() == R.id.ivSend) {


            String code = edCode.getText().toString().trim();
            String qty = edQty.getText().toString().trim();

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

                            tempSelectedItemList();


                            edCode.setText("");
                            edQty.setText("");
                            edCode.requestFocus();

                        }

                    }

                    if (isFound == 0) {
                        Toast.makeText(this, "Item not found!", Toast.LENGTH_SHORT).show();
                    }

                }

            }
        }else if (v.getId()==R.id.ivBack){
            onBackPressed();
        }
    }


    public void tempSelectedItemList() {
        ArrayList<ItemModel> verifyItemList = new ArrayList<>();

        int count = 0;
        for (int i = 0; i < itemList.size(); i++) {
            if (itemList.get(i).getQty() > 0) {
                count++;
            }
        }

        //Log.e("COUNT ", " -------------- " + count);
        if (count != 0) {
            tvOrderCount.setText("Ordered Items - " + count);
        }

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        startActivity(new Intent(ParcelMenuWithSearchActivity.this, HomeActivity.class));
        finish();

    }

    public void showVerifyDialog(final ArrayList<ItemModel> itemArrayList) {
        openDialog = new Dialog(ParcelMenuWithSearchActivity.this);
        openDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        openDialog.setContentView(R.layout.dialog_verify_parcel_order);
        openDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        Window window = openDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        // wlp.gravity = Gravity.TOP | Gravity.RIGHT;
        wlp.x = 100;
        wlp.y = 100;
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);

        RecyclerView recyclerView = openDialog.findViewById(R.id.recyclerView);
        Button btnCancel = openDialog.findViewById(R.id.btnCancel);
        Button btnOrder = openDialog.findViewById(R.id.btnOrder);
        TextView tvTotal = openDialog.findViewById(R.id.tvTotal);

        final EditText edName = openDialog.findViewById(R.id.edName);
        final EditText edMobile = openDialog.findViewById(R.id.edMobile);
        final EditText edDiscount = openDialog.findViewById(R.id.edDiscount);
        final EditText edTotal = openDialog.findViewById(R.id.edTotal);
        final EditText edRemark = openDialog.findViewById(R.id.edRemark);

        float total = 0;
        for (int i = 0; i < itemArrayList.size(); i++) {
            total = total + (itemArrayList.get(i).getQty() * itemArrayList.get(i).getMrpRegular());
        }
        tvTotal.setText("TOTAL = " + total);
        edTotal.setText("" + total);

        final float finalTotal = total;
        edDiscount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                try {
                    float disc = ((finalTotal * Float.parseFloat(s.toString())) / 100);
                    float temp = finalTotal - disc;
                    edTotal.setText("" + temp);

                } catch (Exception e) {
                    edTotal.setText("" + finalTotal);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        VerifyOrderItemModelAdapter verifyAdapter = new VerifyOrderItemModelAdapter(itemArrayList, ParcelMenuWithSearchActivity.this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(ParcelMenuWithSearchActivity.this);
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
                ArrayList<ParcelOrderDetails> orderDetailArray = new ArrayList<>();
                String Remark = edRemark.getText().toString();
                for (int i = 0; i < itemList.size(); i++) {
                    if (itemList.get(i).getQty() > 0) {
                        ItemModel item = itemList.get(i);

                        ParcelOrderDetails orderDetails = new ParcelOrderDetails(0, 0, item.getItemId(), item.getQty(), item.getMrpRegular(), 1, 0, Remark, item.getItemName());
                        orderDetailArray.add(orderDetails);
                    }
                }

                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

                if (orderDetailArray.size() == 0) {
                    Toast.makeText(ParcelMenuWithSearchActivity.this, "Please select item!", Toast.LENGTH_SHORT).show();
                } else if (edName.getText().toString().isEmpty()) {
                    edName.setError("required");
                }
                /*else if (edMobile.getText().toString().isEmpty()) {
                    edName.setError(null);
                    edMobile.setError("required");
                } else if (edMobile.getText().toString().length() != 10) {
                    edName.setError(null);
                    edMobile.setError("invalid mobile number");
                }*/
                else if (edDiscount.getText().toString().isEmpty()) {
                    edName.setError(null);
                    edMobile.setError(null);
                    edDiscount.setError("required");
                } else {

                    float discount = Float.parseFloat(edDiscount.getText().toString());

                    ParcelOrderHeaderModel headerModel = new ParcelOrderHeaderModel(0, userId, edName.getText().toString(), edMobile.getText().toString(), 2, sdf.format(calendar.getTimeInMillis()), sdf1.format(calendar.getTimeInMillis()), 1, orderDetailArray);

                    //Log.e("BEAN : ", "--------------------" + headerModel);
                    saveParcelOrder(headerModel, discount);
                    openDialog.dismiss();

                }
            }
        });

        openDialog.show();
    }


    public void saveParcelOrder(final ParcelOrderHeaderModel order, final float discount) {

        if (Constants.isOnline(this)) {
            final CommonDialog commonDialog = new CommonDialog(this, "Loading", "Please Wait...");
            commonDialog.show();

            Call<ParcelOrderHeaderModel> listCall = Constants.myInterface.saveParcelOrder(order);
            listCall.enqueue(new Callback<ParcelOrderHeaderModel>() {
                @Override
                public void onResponse(Call<ParcelOrderHeaderModel> call, Response<ParcelOrderHeaderModel> response) {
                    try {
                        if (response.body() != null) {

                            //Log.e("Order Data : ", "------------" + response.body());

                            ParcelOrderHeaderModel data = response.body();
                            if (data == null) {
                                commonDialog.dismiss();
                            } else {

                                Toast.makeText(ParcelMenuWithSearchActivity.this, "Order Placed", Toast.LENGTH_SHORT).show();

                                ArrayList<ParcelOrderDetails> orderDetails = (ArrayList<ParcelOrderDetails>) order.getParcelOrderDetailsList();



                                try {
                                    String ip = CustomSharedPreference.getStringPrinter(ParcelMenuWithSearchActivity.this, CustomSharedPreference.KEY_KOT_IP);
                                    PrintHelper printHelper = new PrintHelper(ParcelMenuWithSearchActivity.this, ip, data, orderDetails, PrintReceiptType.KOT_PARCEL);
                                    printHelper.runPrintReceiptSequence();
                                } catch (Exception e) {
                                }


                                generateBillForParcel(data.getParcelOrderId(), userId, discount, venueId, data, orderDetails);

                                for (int i = 0; i < itemList.size(); i++) {
                                    itemList.get(i).setQty(0);
                                    itemList.get(i).setCancelStatus(false);
                                }
                                itemForSearchAdapter.notifyDataSetChanged();

                                tempSelectedItemList();

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
                public void onFailure(Call<ParcelOrderHeaderModel> call, Throwable t) {
                    commonDialog.dismiss();
                    //Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(this, "No Internet Connection!", Toast.LENGTH_SHORT).show();
        }
    }

    public void generateBillForParcel(int parcelOrderId, int userId, final float discount, int venuId, final ParcelOrderHeaderModel header, final ArrayList<ParcelOrderDetails> orderDetails) {

        if (Constants.isOnline(this)) {
            final CommonDialog commonDialog = new CommonDialog(this, "Loading", "Please Wait...");
            commonDialog.show();

            Call<ErrorMessage> listCall = Constants.myInterface.generateBillForParcel(userId, discount, parcelOrderId, venuId);

            listCall.enqueue(new Callback<ErrorMessage>() {
                @Override
                public void onResponse(Call<ErrorMessage> call, Response<ErrorMessage> response) {
                    try {
                        if (response.body() != null) {

                            //Log.e("parcel generate bill : ", "------------" + response.body());

                            ErrorMessage data = response.body();
                            if (data == null) {
                                Toast.makeText(ParcelMenuWithSearchActivity.this, "Unable to process!", Toast.LENGTH_SHORT).show();
                                commonDialog.dismiss();
                            } else {

                                if (data.isError()) {
                                    Toast.makeText(ParcelMenuWithSearchActivity.this, "Unable to process!", Toast.LENGTH_SHORT).show();
                                }
//                                else {
//                                    startActivity(new Intent(ParcelMenuActivity.this, HomeActivity.class));
//                                    finish();
//                                }

                                try {
                                    String ip = CustomSharedPreference.getStringPrinter(ParcelMenuWithSearchActivity.this, CustomSharedPreference.KEY_BILL_IP);
                                    PrintHelper printHelper = new PrintHelper(ParcelMenuWithSearchActivity.this, ip, header, orderDetails, discount, data.getMessage(), PrintReceiptType.BILL_PARCEL);
                                    printHelper.runPrintReceiptSequence();
                                } catch (Exception e) {
                                }

                                commonDialog.dismiss();
                            }
                        } else {
                            Toast.makeText(ParcelMenuWithSearchActivity.this, "Unable to process!", Toast.LENGTH_SHORT).show();
                            commonDialog.dismiss();
                            //Log.e("Data Null : ", "-----------");
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Toast.makeText(ParcelMenuWithSearchActivity.this, "Unable to process!", Toast.LENGTH_SHORT).show();
                        //Log.e("Exception : ", "-----------" + e.getMessage());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ErrorMessage> call, Throwable t) {
                    Toast.makeText(ParcelMenuWithSearchActivity.this, "Unable to process!", Toast.LENGTH_SHORT).show();
                    commonDialog.dismiss();
                    //Log.e("onFailure : ", "-----------" + t.getMessage());
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


    public void getAllItems() {

        if (Constants.isOnline(ParcelMenuWithSearchActivity.this)) {
            final CommonDialog commonDialog = new CommonDialog(ParcelMenuWithSearchActivity.this, "Loading", "Please Wait...");
            commonDialog.show();

            Call<ArrayList<ItemModel>> listCall = Constants.myInterface.getAllItemsByDelStatus();
            listCall.enqueue(new Callback<ArrayList<ItemModel>>() {
                @Override
                public void onResponse(Call<ArrayList<ItemModel>> call, Response<ArrayList<ItemModel>> response) {
                    try {
                        if (response.body() != null) {

                            //Log.e("Category Data : ", "------------" + response.body());

                            ArrayList<ItemModel> data = response.body();
                            if (data == null) {
                                commonDialog.dismiss();
                                Toast.makeText(ParcelMenuWithSearchActivity.this, "No items found", Toast.LENGTH_SHORT).show();
                            } else {

                                itemList.clear();
                                itemList = data;

                                itemForSearchAdapter = new MenuItemForSearchAdapter(itemList, ParcelMenuWithSearchActivity.this);
                                listView.setAdapter(itemForSearchAdapter);

                                commonDialog.dismiss();
                            }
                        } else {
                            commonDialog.dismiss();
                            Toast.makeText(ParcelMenuWithSearchActivity.this, "No items found", Toast.LENGTH_SHORT).show();
                            //Log.e("Data Null : ", "-----------");
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        //  Toast.makeText(getContext(), "No categories found", Toast.LENGTH_SHORT).show();
                        //Log.e("Exception : ", "-----------" + e.getMessage());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<ItemModel>> call, Throwable t) {
                    commonDialog.dismiss();
                    Toast.makeText(ParcelMenuWithSearchActivity.this, "No items found", Toast.LENGTH_SHORT).show();
                    //Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(ParcelMenuWithSearchActivity.this, "No Internet Connection!", Toast.LENGTH_SHORT).show();
        }
    }


    public void getCategoryMenu() {

        if (Constants.isOnline(ParcelMenuWithSearchActivity.this)) {
            final CommonDialog commonDialog = new CommonDialog(ParcelMenuWithSearchActivity.this, "Loading", "Please Wait...");
            commonDialog.show();

            Call<ArrayList<CategoryMenuModel>> listCall = Constants.myInterface.getCategoryMenu();
            listCall.enqueue(new Callback<ArrayList<CategoryMenuModel>>() {
                @Override
                public void onResponse(Call<ArrayList<CategoryMenuModel>> call, Response<ArrayList<CategoryMenuModel>> response) {
                    try {
                        if (response.body() != null) {

                            //Log.e("Category Data : ", "------------" + response.body());

                            ArrayList<CategoryMenuModel> data = response.body();
                            if (data == null) {
                                commonDialog.dismiss();
                            } else {

                                categoryList.clear();
                                categoryList = data;

                                MenuCategoryForSearchAdapter categoryAdapter = new MenuCategoryForSearchAdapter(categoryList, ParcelMenuWithSearchActivity.this);
                                gridView.setAdapter(categoryAdapter);

                                commonDialog.dismiss();
                            }
                        } else {
                            commonDialog.dismiss();
                            //Log.e("Data Null : ", "-----------");
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        //  Toast.makeText(getContext(), "No categories found", Toast.LENGTH_SHORT).show();
                        //Log.e("Exception : ", "-----------" + e.getMessage());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<CategoryMenuModel>> call, Throwable t) {
                    commonDialog.dismiss();
                    //Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(ParcelMenuWithSearchActivity.this, "No Internet Connection!", Toast.LENGTH_SHORT).show();
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
                    //Log.e("POSITION : ", "-------------------" + posit);

                    smoothScrollToPositionFromTop(listView, posit);
                }
            });

            gridViewSetting(gridView);

            return rowView;
        }
    }

    private void gridViewSetting(GridView gridview) {

        int size = categoryList.size();
        // //Log.e("Size : ", "----------" + size);
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
