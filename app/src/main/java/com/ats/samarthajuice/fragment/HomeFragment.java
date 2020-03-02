package com.ats.samarthajuice.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ats.samarthajuice.R;
import com.ats.samarthajuice.adapter.BusyTableAdapter;
import com.ats.samarthajuice.adapter.FreeTableAdapter;
import com.ats.samarthajuice.constant.Constants;
import com.ats.samarthajuice.model.TableBusyModel;
import com.ats.samarthajuice.model.TableCategoryModel;
import com.ats.samarthajuice.model.TableFreeModel;
import com.ats.samarthajuice.util.CommonDialog;
import com.ats.samarthajuice.util.CustomSharedPreference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private GridView gridView;
    private RecyclerView recyclerView;
    private Spinner spinner;
    private TextView tvDate;

    private ArrayList<TableBusyModel> busyTableList = new ArrayList<>();
    BusyTableAdapter busyTableAdapter;

    private ArrayList<TableFreeModel> freeTableList = new ArrayList<>();
    FreeTableAdapter freeTableAdapter;

    private ArrayList<String> tableCatNameArray = new ArrayList<>();
    private ArrayList<Integer> tableCatIdArray = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view;
        if (Constants.isTablet(getContext())) {
            if (getResources().getConfiguration().orientation == 1) {
                view = inflater.inflate(R.layout.fragment_home_tab_port, container, false);
            } else {
                view = inflater.inflate(R.layout.fragment_home_tab_land, container, false);
            }
        } else {
            if (getResources().getConfiguration().orientation == 1) {
                view = inflater.inflate(R.layout.fragment_home_phone_port, container, false);
            } else {
                view = inflater.inflate(R.layout.fragment_home_phone_land, container, false);
            }
        }

        getActivity().setTitle("Dashboard");

        gridView = view.findViewById(R.id.gridView);
        recyclerView = view.findViewById(R.id.recyclerView);
        spinner = view.findViewById(R.id.spinner);
        tvDate = view.findViewById(R.id.tvDate);

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

        tvDate.setText("Date : " + sdf.format(calendar.getTimeInMillis()));

        getAllTableCategory();


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int catId = tableCatIdArray.get(position);

                CustomSharedPreference.putInt(getActivity(), CustomSharedPreference.KEY_TABLE_CAT_ID, catId);

                getAllBusyTables(catId);
                getAllFreeTables(catId);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return view;
    }

    public void getAllBusyTables(int catId) {

        if (Constants.isOnline(getContext())) {
            final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<ArrayList<TableBusyModel>> listCall;
            if (catId == 0) {
                listCall = Constants.myInterface.getAllBusyTables();
            } else {
                listCall = Constants.myInterface.getAllBusyTables(catId);
            }

            listCall.enqueue(new Callback<ArrayList<TableBusyModel>>() {
                @Override
                public void onResponse(Call<ArrayList<TableBusyModel>> call, Response<ArrayList<TableBusyModel>> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("busy Data : ", "------------" + response.body());

                            ArrayList<TableBusyModel> data = response.body();
                            if (data == null) {
                                commonDialog.dismiss();
                            } else {

                                busyTableList.clear();
                                busyTableList = data;

                                busyTableAdapter = new BusyTableAdapter(busyTableList, getContext(),freeTableList);
                                gridView.setAdapter(busyTableAdapter);

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
                public void onFailure(Call<ArrayList<TableBusyModel>> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(getContext(), "No Internet Connection!", Toast.LENGTH_SHORT).show();
        }
    }

    public void getAllFreeTables(int catId) {

        if (Constants.isOnline(getContext())) {
            final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<ArrayList<TableFreeModel>> listCall;
            if (catId == 0) {
                listCall = Constants.myInterface.getAllFreeTables();
            } else {
                listCall = Constants.myInterface.getAllFreeTables(catId);
            }

            listCall.enqueue(new Callback<ArrayList<TableFreeModel>>() {
                @Override
                public void onResponse(Call<ArrayList<TableFreeModel>> call, Response<ArrayList<TableFreeModel>> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("free Data : ", "------------" + response.body());

                            ArrayList<TableFreeModel> data = response.body();
                            if (data == null) {
                                commonDialog.dismiss();
                            } else {

                                freeTableList.clear();
                                freeTableList = data;

                                freeTableAdapter = new FreeTableAdapter(freeTableList, getContext());
                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                                recyclerView.setLayoutManager(mLayoutManager);
                                recyclerView.setItemAnimator(new DefaultItemAnimator());
                                recyclerView.setAdapter(freeTableAdapter);

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
                public void onFailure(Call<ArrayList<TableFreeModel>> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(getContext(), "No Internet Connection!", Toast.LENGTH_SHORT).show();
        }
    }

    public void getAllTableCategory() {

        if (Constants.isOnline(getContext())) {
            final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<ArrayList<TableCategoryModel>> listCall = Constants.myInterface.getAllTableCategory();
            listCall.enqueue(new Callback<ArrayList<TableCategoryModel>>() {
                @Override
                public void onResponse(Call<ArrayList<TableCategoryModel>> call, Response<ArrayList<TableCategoryModel>> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("Category Data : ", "------------" + response.body());

                            ArrayList<TableCategoryModel> data = response.body();
                            if (data == null) {
                                commonDialog.dismiss();
                            } else {

                                tableCatIdArray.clear();
                                tableCatNameArray.clear();
                                tableCatIdArray.add(0);
                                tableCatNameArray.add("All Tables");

                                for (int i = 0; i < data.size(); i++) {
                                    tableCatIdArray.add(data.get(i).getTableCatId());
                                    tableCatNameArray.add(data.get(i).getTableCatName());
                                }

                                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getContext(), R.layout.spinner_layout, tableCatNameArray);
                                spinner.setAdapter(spinnerAdapter);

                                int catId = CustomSharedPreference.getInt(getActivity(), CustomSharedPreference.KEY_TABLE_CAT_ID);
                                int pos = 0;
                                for (int i = 0; i < tableCatIdArray.size(); i++) {
                                    if (catId == tableCatIdArray.get(i)) {
                                        pos = i;
                                    }
                                }

                                spinner.setSelection(pos);

                                commonDialog.dismiss();
                            }
                        } else {
                            commonDialog.dismiss();
                            Log.e("Data Null : ", "-----------");
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        //  Toast.makeText(getContext(), "No categories found", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<TableCategoryModel>> call, Throwable t) {
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
