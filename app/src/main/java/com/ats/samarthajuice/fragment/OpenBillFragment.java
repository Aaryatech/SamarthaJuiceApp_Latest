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
import com.ats.samarthajuice.adapter.OpenBillAdapter;
import com.ats.samarthajuice.constant.Constants;
import com.ats.samarthajuice.model.BillHeaderModel;
import com.ats.samarthajuice.util.CommonDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class OpenBillFragment extends Fragment implements View.OnClickListener {
    private RecyclerView recyclerView;
    private ArrayList<BillHeaderModel> billHeaderList = new ArrayList<>();
    OpenBillAdapter billAdapter;
    private Spinner  spinnerType;
    private ImageView ivSearch;
    private TextView tvDate;
    private EditText edDate;


    int yyyy, mm, dd;
    long fromDateMillis, toDateMillis;

    String fromDate, toDate;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_open_bill, container, false);
        getActivity().setTitle("Open Bills");

        recyclerView = view.findViewById(R.id.recyclerView);
        spinnerType = view.findViewById(R.id.spinnerType);
        tvDate = view.findViewById(R.id.tvDate);
        edDate = view.findViewById(R.id.edDate);
        spinnerType = view.findViewById(R.id.spinnerType);
        ivSearch = view.findViewById(R.id.ivSearch);

        ivSearch.setOnClickListener(this);
        edDate.setOnClickListener(this);

        ArrayList<String> typeArray = new ArrayList<>();
        typeArray.add("General");
        typeArray.add("Parcel");

        ArrayAdapter<String> adapterType = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, typeArray);
        spinnerType.setAdapter(adapterType);

        Date todayDate = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        String currentDate = formatter.format(todayDate);
        Log.e("Mytag","todayString"+currentDate);

        SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MM-yyyy");

        tvDate.setText("" +currentDate);
        edDate.setText("" + currentDate);

        Date ToDate = null;
        try {
            ToDate = formatter1.parse(currentDate);//catch exception
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
       String DateTo = formatter2.format(ToDate);

        getAllBills(DateTo, 1,0);

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
    private void getAllBills(String dateTo, int type, int billClose) {




        Log.e("PARAMETERS : ", "        DATE : " + dateTo + "      TYPE : " + type+ "      BILL : " + billClose);

        if (Constants.isOnline(getContext())) {
            final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<ArrayList<BillHeaderModel>> listCall = Constants.myInterface.getAllBills(dateTo, type,billClose);
            listCall.enqueue(new Callback<ArrayList<BillHeaderModel>>() {
                @Override
                public void onResponse(Call<ArrayList<BillHeaderModel>> call, Response<ArrayList<BillHeaderModel>> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("bill Data Open : ", "------------" + response.body());

                            ArrayList<BillHeaderModel> data = response.body();
                            if (data == null) {
                                commonDialog.dismiss();
                                Toast.makeText(getContext(), "No report found!", Toast.LENGTH_SHORT).show();
                            } else {
                                billHeaderList.clear();
                                billHeaderList = data;

                                billAdapter = new OpenBillAdapter(billHeaderList, getContext());
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

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
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


            SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MM-yyyy");


            Date ToDate = null;
            try {
                ToDate = formatter1.parse(tvDate.getText().toString());//catch exception
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            String DateTo = formatter2.format(ToDate);

            getAllBills(DateTo, type,0);
        }

    }
}
