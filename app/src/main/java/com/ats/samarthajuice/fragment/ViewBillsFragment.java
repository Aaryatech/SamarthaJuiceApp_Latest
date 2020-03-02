package com.ats.samarthajuice.fragment;


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ats.samarthajuice.R;
import com.ats.samarthajuice.adapter.ViewBillsAdapter;
import com.ats.samarthajuice.constant.Constants;
import com.ats.samarthajuice.model.BillHeaderModel;
import com.ats.samarthajuice.model.TableModel;
import com.ats.samarthajuice.util.CommonDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewBillsFragment extends Fragment implements View.OnClickListener {

    private EditText edDate;
    private Spinner spinnerTable, spinnerType;
    private TextView tvDate;
    private ImageView ivSearch;
    private RecyclerView recyclerView;

    private ArrayList<TableModel> tableList = new ArrayList<>();
    private ArrayList<String> tableNameList = new ArrayList<>();
    private ArrayList<Integer> tableIdList = new ArrayList<>();

    int yyyy, mm, dd;
    long fromDateMillis, toDateMillis;

    String fromDate, toDate;

    private ArrayList<BillHeaderModel> billHeaderList = new ArrayList<>();
    ViewBillsAdapter billAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_bills, container, false);

        getActivity().setTitle("View Bills");

        edDate = view.findViewById(R.id.edDate);
        tvDate = view.findViewById(R.id.tvDate);
        spinnerTable = view.findViewById(R.id.spinnerTable);
        spinnerType = view.findViewById(R.id.spinnerType);
        ivSearch = view.findViewById(R.id.ivSearch);
        recyclerView = view.findViewById(R.id.recyclerView);

        ivSearch.setOnClickListener(this);
        edDate.setOnClickListener(this);


        getAllTables();

        ArrayList<String> typeArray = new ArrayList<>();
        typeArray.add("General");
        typeArray.add("Parcel");

        ArrayAdapter<String> adapterType = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, typeArray);
        spinnerType.setAdapter(adapterType);

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();

        edDate.setText("" + sdf.format(calendar.getTimeInMillis()));
        tvDate.setText("" + sdf1.format(calendar.getTimeInMillis()));

        String date = tvDate.getText().toString();

        getAllBills(date, 1,1);

        return view;
    }

    DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            yyyy = year;
            mm = month + 1;
            dd = dayOfMonth;
            edDate.setText(dd + "-" + mm + "-" + yyyy);
            tvDate.setText(yyyy + "-" + mm + "-" + dd);
            fromDate = dd + "-" + mm + "-" + yyyy;

            Calendar calendar = Calendar.getInstance();
            calendar.set(yyyy, mm - 1, dd);
            calendar.set(Calendar.MILLISECOND, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.HOUR, 0);
            fromDateMillis = calendar.getTimeInMillis();
        }
    };

    public void getAllTables() {

        if (Constants.isOnline(getContext())) {
            final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<ArrayList<TableModel>> listCall = Constants.myInterface.getAllTables();
            listCall.enqueue(new Callback<ArrayList<TableModel>>() {
                @Override
                public void onResponse(Call<ArrayList<TableModel>> call, Response<ArrayList<TableModel>> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("Category Data : ", "------------" + response.body());

                            ArrayList<TableModel> data = response.body();
                            if (data == null) {
                                commonDialog.dismiss();
                                Toast.makeText(getContext(), "No category found", Toast.LENGTH_SHORT).show();
                            } else {

                                tableList.clear();
                                tableNameList.clear();
                                tableIdList.clear();

                                tableIdList.add(0);
                                tableNameList.add("All");

                                tableList = data;

                                for (int i = 0; i < tableList.size(); i++) {
                                    tableNameList.add(tableList.get(i).getTableName());
                                    tableIdList.add(tableList.get(i).getTableId());
                                }

                                ArrayAdapter<String> adapterTable = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, tableNameList);
                                spinnerTable.setAdapter(adapterTable);

                                commonDialog.dismiss();
                            }
                        } else {
                            commonDialog.dismiss();
                            Toast.makeText(getContext(), "No category found", Toast.LENGTH_SHORT).show();
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
                public void onFailure(Call<ArrayList<TableModel>> call, Throwable t) {
                    commonDialog.dismiss();
                    Toast.makeText(getContext(), "No category found", Toast.LENGTH_SHORT).show();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(getContext(), "No Internet Connection!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.edDate) {
            int yr, mn, dy;
            if (fromDateMillis > 0) {
                Calendar purchaseCal = Calendar.getInstance();
                purchaseCal.setTimeInMillis(fromDateMillis);
                yr = purchaseCal.get(Calendar.YEAR);
                mn = purchaseCal.get(Calendar.MONTH);
                dy = purchaseCal.get(Calendar.DAY_OF_MONTH);
            } else {
                Calendar purchaseCal = Calendar.getInstance();
                yr = purchaseCal.get(Calendar.YEAR);
                mn = purchaseCal.get(Calendar.MONTH);
                dy = purchaseCal.get(Calendar.DAY_OF_MONTH);
            }
            DatePickerDialog dialog = new DatePickerDialog(getActivity(), dateListener, yr, mn, dy);
            dialog.show();
        } else if (v.getId() == R.id.ivSearch) {

            int type = spinnerType.getSelectedItemPosition() + 1;
            String date = tvDate.getText().toString();

            getAllBills(date, type,1);
        }
    }


    public void getAllBills(String date, int type,int billClose) {

        if (Constants.isOnline(getContext())) {
            final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<ArrayList<BillHeaderModel>> listCall = Constants.myInterface.getAllBills(date, type,billClose);
            listCall.enqueue(new Callback<ArrayList<BillHeaderModel>>() {
                @Override
                public void onResponse(Call<ArrayList<BillHeaderModel>> call, Response<ArrayList<BillHeaderModel>> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("bill Data View : ", "------------" + response.body());

                            ArrayList<BillHeaderModel> data = response.body();
                            if (data == null) {
                                commonDialog.dismiss();
                                Toast.makeText(getContext(), "No report found!", Toast.LENGTH_SHORT).show();
                            } else {
                                billHeaderList.clear();
                                billHeaderList = data;

                                billAdapter = new ViewBillsAdapter(billHeaderList, getContext());
                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                                recyclerView.setLayoutManager(mLayoutManager);
                                recyclerView.setItemAnimator(new DefaultItemAnimator());
                                recyclerView.setAdapter(billAdapter);

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
                public void onFailure(Call<ArrayList<BillHeaderModel>> call, Throwable t) {
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
