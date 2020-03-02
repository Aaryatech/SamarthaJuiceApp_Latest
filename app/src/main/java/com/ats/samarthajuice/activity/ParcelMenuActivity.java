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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
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

import com.ats.samarthajuice.adapter.MenuItemAdapter;
import com.ats.samarthajuice.adapter.VerifyOrderAdapter;
import com.ats.samarthajuice.model.Admin;
import com.ats.samarthajuice.model.CategoryItemModel;
import com.ats.samarthajuice.model.ErrorMessage;
import com.ats.samarthajuice.model.Item;
import com.ats.samarthajuice.model.ParcelOrderDetails;
import com.ats.samarthajuice.model.ParcelOrderHeaderModel;
import com.ats.samarthajuice.util.CommonDialog;
import com.ats.samarthajuice.util.CustomSharedPreference;
import com.ats.samarthajuice.R;
import com.ats.samarthajuice.constant.Constants;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ParcelMenuActivity extends AppCompatActivity implements View.OnClickListener {

    private GridView gridView;
    private TextView tvTable, tvAmount, tvDate;
    private ListView listView;
    private LinearLayout llVerify;
    private EditText edSearch;
    private ImageView imageView;

    private ArrayList<CategoryItemModel> categoryWiseItemList = new ArrayList<>();

    MenuCategoryAdapter categoryAdapter;
    MenuItemAdapter itemAdapter;

    int userId, venueId;

    private BroadcastReceiver mBroadcastReceiver;
    Dialog openDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parcel_menu);

        setTitle("Menu");

        gridView = findViewById(R.id.gridView);
        listView = findViewById(R.id.listView);
        tvTable = findViewById(R.id.tvTable);
        tvDate = findViewById(R.id.tvDate);
        llVerify = findViewById(R.id.llVerify);
        edSearch = findViewById(R.id.edSearch);
        imageView = findViewById(R.id.imageView);

        llVerify.setOnClickListener(this);
        imageView.setOnClickListener(this);

        Gson gson = new Gson();
        String jsonAdmin = CustomSharedPreference.getString(this, CustomSharedPreference.KEY_ADMIN);
        final Admin admin = gson.fromJson(jsonAdmin, Admin.class);

        Log.e("Admin : ", "---------------------------" + admin);

        if (admin != null) {
            userId = admin.getAdminId();
            venueId = admin.getVenueId();
        }

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        tvDate.setText("Date : " + sdf.format(calendar.getTimeInMillis()));

        tvTable.setText("Parcel Order");

        getAllCategoryWiseItems();

        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals("REFRESH_DATA")) {
                    handlePushNotification(intent);
                }
            }
        };

    }

    @Override
    public void onPause() {
        Log.e("SUGGESTION", "  ON PAUSE");

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

        Log.e("handlePushNotification", "------------------------------------**********");
        openDialog.dismiss();
        itemAdapter.notifyDataSetChanged();

        ArrayList<Item> itemList = new ArrayList<>();

        for (int i = 0; i < categoryWiseItemList.size(); i++) {
            for (int j = 0; j < categoryWiseItemList.get(i).getItemList().size(); j++) {
                if (categoryWiseItemList.get(i).getItemList().get(j).getQty() > 0) {
                    Item item = categoryWiseItemList.get(i).getItemList().get(j);
                    itemList.add(item);
                }
            }
        }

        if (itemList.size() > 0) {
            showVerifyDialog(itemList);
        }

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.imageView) {

            ArrayList<Item> itemList = new ArrayList<>();

            for (int i = 0; i < categoryWiseItemList.size(); i++) {
                for (int j = 0; j < categoryWiseItemList.get(i).getItemList().size(); j++) {
                    if (categoryWiseItemList.get(i).getItemList().get(j).getQty() > 0) {
                        Item item = categoryWiseItemList.get(i).getItemList().get(j);
                        itemList.add(item);
                    }
                }
            }

            if (itemList.size() > 0) {
                showVerifyDialog(itemList);
            } else {
                Toast.makeText(this, "Please select item!", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public class MenuCategoryAdapter extends BaseAdapter {

        private ArrayList<CategoryItemModel> categoryModelList;
        private Context context;
        private LayoutInflater inflater = null;

        public MenuCategoryAdapter(ArrayList<CategoryItemModel> categoryModelList, Context context) {
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
            final MenuCategoryAdapter.Holder holder;
            View rowView = convertView;

            if (rowView == null) {
                holder = new MenuCategoryAdapter.Holder();
                LayoutInflater inflater = LayoutInflater.from(context);
                rowView = inflater.inflate(R.layout.adapter_menu_category, null);

                holder.tvName = rowView.findViewById(R.id.tvName);
                holder.linearLayout = rowView.findViewById(R.id.linearLayout);
                holder.imageView = rowView.findViewById(R.id.imageView);

                rowView.setTag(holder);

            } else {
                holder = (MenuCategoryAdapter.Holder) rowView.getTag();
            }

            holder.tvName.setText(categoryModelList.get(position).getCatName());

            try {
                Picasso.get().load(Constants.categoryImagePath + "" + categoryModelList.get(position).getCatImage())
                        .placeholder(R.drawable.logo)
                        .error(R.drawable.logo)
                        .into(holder.imageView);
            } catch (Exception e) {
            }

            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getItemPosition(categoryModelList.get(position).getCatName());
                    smoothScrollToPositionFromTop(listView, position);
                }
            });

            gridViewSetting(gridView);

            return rowView;
        }
    }

    public int getItemPosition(String name) {
        for (int i = 0; i < categoryWiseItemList.size(); i++)
            if (categoryWiseItemList.get(i).getCatName().equalsIgnoreCase(name))
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

    private void gridViewSetting(GridView gridview) {

        int size = categoryWiseItemList.size();
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

    public void getAllCategoryWiseItems() {

        if (Constants.isOnline(this)) {
            final CommonDialog commonDialog = new CommonDialog(this, "Loading", "Please Wait...");
            commonDialog.show();

            Call<ArrayList<CategoryItemModel>> listCall = Constants.myInterface.getAllCategoryWiseItems();
            listCall.enqueue(new Callback<ArrayList<CategoryItemModel>>() {
                @Override
                public void onResponse(Call<ArrayList<CategoryItemModel>> call, Response<ArrayList<CategoryItemModel>> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("Data : ", "------------" + response.body());

                            ArrayList<CategoryItemModel> data = response.body();
                            if (data == null) {
                                commonDialog.dismiss();
                            } else {

                                categoryWiseItemList.clear();
                                categoryWiseItemList = data;

                                for (int i = 0; i < categoryWiseItemList.size(); i++) {
                                    for (int j = 0; j < categoryWiseItemList.get(i).getItemList().size(); j++) {
                                        categoryWiseItemList.get(i).getItemList().get(j).setQty(0);
                                        categoryWiseItemList.get(i).getItemList().get(j).setCancelStatus(false);
                                    }
                                }

                                gridView.setNumColumns(categoryWiseItemList.size());

                                categoryAdapter = new MenuCategoryAdapter(categoryWiseItemList, ParcelMenuActivity.this);
                                gridView.setAdapter(categoryAdapter);

                                itemAdapter = new MenuItemAdapter(categoryWiseItemList, ParcelMenuActivity.this);
                                listView.setAdapter(itemAdapter);

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
                public void onFailure(Call<ArrayList<CategoryItemModel>> call, Throwable t) {
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
    public void onBackPressed() {
        super.onBackPressed();

        startActivity(new Intent(ParcelMenuActivity.this, HomeActivity.class));
        finish();

    }

    public void showVerifyDialog(final ArrayList<Item> itemArrayList) {
        openDialog = new Dialog(ParcelMenuActivity.this);
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

        VerifyOrderAdapter verifyAdapter = new VerifyOrderAdapter(itemArrayList, ParcelMenuActivity.this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(ParcelMenuActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(verifyAdapter);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemAdapter.notifyDataSetChanged();
                openDialog.dismiss();
            }
        });

        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<ParcelOrderDetails> orderDetailArray = new ArrayList<>();

                for (int i = 0; i < categoryWiseItemList.size(); i++) {
                    for (int j = 0; j < categoryWiseItemList.get(i).getItemList().size(); j++) {
                        if (categoryWiseItemList.get(i).getItemList().get(j).getQty() > 0) {
                            Item item = categoryWiseItemList.get(i).getItemList().get(j);

                            ParcelOrderDetails orderDetails = new ParcelOrderDetails(0, 0, item.getItemId(), item.getQty(), item.getMrpRegular(), 1, 0, "na");
                            orderDetailArray.add(orderDetails);
                        }
                    }
                }

                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

                if (orderDetailArray.size() == 0) {
                    Toast.makeText(ParcelMenuActivity.this, "Please select item!", Toast.LENGTH_SHORT).show();
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

                    Log.e("BEAN : ", "--------------------" + headerModel);
                    saveParcelOrder(headerModel, discount);
                    openDialog.dismiss();

                }
            }
        });

        openDialog.show();
    }


    public void saveParcelOrder(ParcelOrderHeaderModel order, final float discount) {

        if (Constants.isOnline(this)) {
            final CommonDialog commonDialog = new CommonDialog(this, "Loading", "Please Wait...");
            commonDialog.show();

            Call<ParcelOrderHeaderModel> listCall = Constants.myInterface.saveParcelOrder(order);
            listCall.enqueue(new Callback<ParcelOrderHeaderModel>() {
                @Override
                public void onResponse(Call<ParcelOrderHeaderModel> call, Response<ParcelOrderHeaderModel> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("Order Data : ", "------------" + response.body());

                            ParcelOrderHeaderModel data = response.body();
                            if (data == null) {
                                commonDialog.dismiss();
                            } else {

                                Toast.makeText(ParcelMenuActivity.this, "Order Placed", Toast.LENGTH_SHORT).show();
                                generateBillForParcel(data.getParcelOrderId(), userId, discount, venueId);

                                for (int i = 0; i < categoryWiseItemList.size(); i++) {
                                    for (int j = 0; j < categoryWiseItemList.get(i).getItemList().size(); j++) {
                                        categoryWiseItemList.get(i).getItemList().get(j).setQty(0);
                                        categoryWiseItemList.get(i).getItemList().get(j).setCancelStatus(false);
                                    }
                                }
                                itemAdapter.notifyDataSetChanged();

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
                public void onFailure(Call<ParcelOrderHeaderModel> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(this, "No Internet Connection!", Toast.LENGTH_SHORT).show();
        }
    }

    public void generateBillForParcel(int parcelOrderId, int userId, float discount, int venuId) {

        if (Constants.isOnline(this)) {
            final CommonDialog commonDialog = new CommonDialog(this, "Loading", "Please Wait...");
            commonDialog.show();

            Call<ErrorMessage> listCall = Constants.myInterface.generateBillForParcel(userId, discount, parcelOrderId, venuId);

            listCall.enqueue(new Callback<ErrorMessage>() {
                @Override
                public void onResponse(Call<ErrorMessage> call, Response<ErrorMessage> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("parcel generate bill : ", "------------" + response.body());

                            ErrorMessage data = response.body();
                            if (data == null) {
                                Toast.makeText(ParcelMenuActivity.this, "Unable to process!", Toast.LENGTH_SHORT).show();
                                commonDialog.dismiss();
                            } else {

                                if (data.isError()) {
                                    Toast.makeText(ParcelMenuActivity.this, "Unable to process!", Toast.LENGTH_SHORT).show();
                                }
//                                else {
//                                    startActivity(new Intent(ParcelMenuActivity.this, HomeActivity.class));
//                                    finish();
//                                }

                                commonDialog.dismiss();
                            }
                        } else {
                            Toast.makeText(ParcelMenuActivity.this, "Unable to process!", Toast.LENGTH_SHORT).show();
                            commonDialog.dismiss();
                            Log.e("Data Null : ", "-----------");
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Toast.makeText(ParcelMenuActivity.this, "Unable to process!", Toast.LENGTH_SHORT).show();
                        Log.e("Exception : ", "-----------" + e.getMessage());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ErrorMessage> call, Throwable t) {
                    Toast.makeText(ParcelMenuActivity.this, "Unable to process!", Toast.LENGTH_SHORT).show();
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


}
