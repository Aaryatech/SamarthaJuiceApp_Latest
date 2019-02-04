package com.ats.taborderingsystem.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ats.taborderingsystem.R;
import com.ats.taborderingsystem.activity.HomeActivity;
import com.ats.taborderingsystem.activity.LoginActivity;
import com.ats.taborderingsystem.adapter.TableCategoryAdapter;
import com.ats.taborderingsystem.constant.Constants;
import com.ats.taborderingsystem.model.Admin;
import com.ats.taborderingsystem.model.CategoryModel;
import com.ats.taborderingsystem.model.TableCategoryModel;
import com.ats.taborderingsystem.model.TableModel;
import com.ats.taborderingsystem.util.CommonDialog;
import com.ats.taborderingsystem.util.CustomSharedPreference;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddTableFragment extends Fragment implements View.OnClickListener {

    private EditText edNo, edName;
    private Button btnSubmit;
    private Spinner spinner;

    int tableNo, userId , tableId = 0, tableCatId = 0, venueId ;
    String strTableName;

    private ArrayList<String> tableCatNameArray = new ArrayList<>();
    private ArrayList<Integer> tableCatIdArray = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_table, container, false);

        getActivity().setTitle("Table Details");

        edNo = view.findViewById(R.id.edNo);
        edName = view.findViewById(R.id.edName);
        spinner = view.findViewById(R.id.spinner);
        btnSubmit = view.findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(this);

        Gson gson = new Gson();
        String jsonAdmin = CustomSharedPreference.getString(getContext(), CustomSharedPreference.KEY_ADMIN);
        final Admin admin = gson.fromJson(jsonAdmin, Admin.class);

        Log.e("Admin : ", "---------------------------" + admin);
        if (admin != null) {
            userId=admin.getAdminId();
            venueId = admin.getVenueId();
        }

        String str = "";
        try {
            str = getArguments().getString("model");
        } catch (Exception e) {
        }

        Gson gson1 = new Gson();
        TableModel model = gson1.fromJson(str, TableModel.class);

        if (model != null) {
            Log.e("MODEL : ", "--------------" + model);

            tableId = model.getTableId();
            edName.setText("" + model.getTableName());
            edNo.setText("" + model.getTableNo());

            tableCatId = model.getIsActive();
        }

        getAllTableCategory();


        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnSubmit) {

            tableNo = Integer.parseInt(edNo.getText().toString());
            strTableName = edName.getText().toString();

            if (edNo.getText().toString().isEmpty()) {
                edNo.setError("required");
            } else if (edName.getText().toString().isEmpty()) {
                edNo.setError(null);
                edName.setError("required");
            } else if (spinner.getSelectedItemPosition() == 0) {
                edName.setError(null);
                TextView view = (TextView) spinner.getSelectedView();
                view.setError("required");
            } else {
                TextView view = (TextView) spinner.getSelectedView();
                view.setError(null);

                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                int pos = spinner.getSelectedItemPosition();
                tableCatId = tableCatIdArray.get(pos);

                TableModel tableModel = new TableModel(tableId, tableNo, strTableName, 1, tableCatId, userId, sdf.format(calendar.getTimeInMillis()), venueId);
                saveTable(tableModel);
            }
        }
    }

    public void saveTable(TableModel model) {
        if (Constants.isOnline(getContext())) {
            final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<TableModel> listCall = Constants.myInterface.saveTable(model);
            listCall.enqueue(new Callback<TableModel>() {
                @Override
                public void onResponse(Call<TableModel> call, Response<TableModel> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("Data : ", "------------" + response.body());

                            TableModel data = response.body();
                            if (data == null) {
                                commonDialog.dismiss();
                                Toast.makeText(getContext(), "Unable to process!", Toast.LENGTH_SHORT).show();
                            } else {

                                Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
                                commonDialog.dismiss();

                                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                                ft.replace(R.id.content_frame, new TableMasterFragment(), "HomeFragment");
                                ft.commit();

                            }
                        } else {
                            commonDialog.dismiss();
                            Toast.makeText(getContext(), "Unable to process!", Toast.LENGTH_SHORT).show();
                            Log.e("Data Null : ", "-----------");
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Toast.makeText(getContext(), "Unable to process!", Toast.LENGTH_SHORT).show();
                        Log.e("Exception : ", "-----------" + e.getMessage());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<TableModel> call, Throwable t) {
                    commonDialog.dismiss();
                    Toast.makeText(getContext(), "Unable to process!", Toast.LENGTH_SHORT).show();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(getContext(), "No Internet Connection !", Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(getContext(), "No category found", Toast.LENGTH_SHORT).show();
                            } else {

                                tableCatIdArray.clear();
                                tableCatNameArray.clear();
                                tableCatIdArray.add(0);
                                tableCatNameArray.add("Select Table Category");

                                for (int i = 0; i < data.size(); i++) {
                                    tableCatNameArray.add(data.get(i).getTableCatName());
                                    tableCatIdArray.add(data.get(i).getTableCatId());
                                }

                                ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, tableCatNameArray);
                                spinner.setAdapter(adapter);

                                ArrayAdapter<Integer> catIdAdapter = new ArrayAdapter<Integer>(getContext(), android.R.layout.simple_spinner_dropdown_item, tableCatIdArray);
                                if (tableCatId > 0) {
                                    int spinnerPosition = catIdAdapter.getPosition(tableCatId);
                                    spinner.setSelection(spinnerPosition);
                                }


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
                public void onFailure(Call<ArrayList<TableCategoryModel>> call, Throwable t) {
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

}
