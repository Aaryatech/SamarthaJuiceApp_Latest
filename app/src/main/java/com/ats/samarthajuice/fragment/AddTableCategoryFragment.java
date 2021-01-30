package com.ats.samarthajuice.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ats.samarthajuice.R;
import com.ats.samarthajuice.constant.Constants;
import com.ats.samarthajuice.model.TableCategoryModel;
import com.ats.samarthajuice.util.CommonDialog;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddTableCategoryFragment extends Fragment implements View.OnClickListener {

    private EditText edName, edDesc;
    private Button btnSubmit;

    int tableCatId=0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_table_category, container, false);

        getActivity().setTitle("Table Category Details");

        edName = view.findViewById(R.id.edName);
        edDesc = view.findViewById(R.id.edDesc);
        btnSubmit = view.findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(this);

        String str = "";
        try {
            str = getArguments().getString("model");
        } catch (Exception e) {
        }

        Gson gson = new Gson();
        TableCategoryModel model = gson.fromJson(str, TableCategoryModel.class);

        //Log.e("GSON", "--------------" + model);

        if (model != null) {
            //Log.e("MODEL : ", "--------------" + model);

            tableCatId=model.getTableCatId();
            edName.setText(""+model.getTableCatName());
            edDesc.setText(""+model.getTableCatDesc());
        }

        return view;
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnSubmit) {

            if (edName.getText().toString().isEmpty()){
                edName.setError("required");

            }else{

                String name=edName.getText().toString();
                String desc=edDesc.getText().toString();

                TableCategoryModel tableCategoryModel=new TableCategoryModel(tableCatId,name,desc,1);
                saveTableCategory(tableCategoryModel);
            }

        }
    }


    public void saveTableCategory(TableCategoryModel model) {
        if (Constants.isOnline(getContext())) {
            final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<TableCategoryModel> listCall = Constants.myInterface.saveTableCategory(model);
            listCall.enqueue(new Callback<TableCategoryModel>() {
                @Override
                public void onResponse(Call<TableCategoryModel> call, Response<TableCategoryModel> response) {
                    try {
                        if (response.body() != null) {

                            //Log.e("Data : ", "------------" + response.body());

                            TableCategoryModel data = response.body();
                            if (data == null) {
                                commonDialog.dismiss();
                                Toast.makeText(getContext(), "Unable to process!", Toast.LENGTH_SHORT).show();
                            } else {

                                Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
                                commonDialog.dismiss();

                                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                                ft.replace(R.id.content_frame, new TableCategoryMasterFragment(), "HomeFragment");
                                ft.commit();

                            }
                        } else {
                            commonDialog.dismiss();
                            Toast.makeText(getContext(), "Unable to process!", Toast.LENGTH_SHORT).show();
                            //Log.e("Data Null : ", "-----------");
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Toast.makeText(getContext(), "Unable to process!", Toast.LENGTH_SHORT).show();
                        //Log.e("Exception : ", "-----------" + e.getMessage());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<TableCategoryModel> call, Throwable t) {
                    commonDialog.dismiss();
                    Toast.makeText(getContext(), "Unable to process!", Toast.LENGTH_SHORT).show();
                    //Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(getContext(), "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }
    }

}
