package com.ats.samarthajuice.fragment;


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

import com.ats.samarthajuice.model.AdminModel;
import com.ats.samarthajuice.util.CommonDialog;
import com.ats.samarthajuice.R;
import com.ats.samarthajuice.constant.Constants;
import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddUserFragment extends Fragment implements View.OnClickListener {

    private EditText edUsername, edPassword, edConfirmPassword;
    private Button btnSubmit;
    private Spinner spinner;

    String strUsername, strPass, strConfirmPass, strType;
    int adminId=0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_user, container, false);

        getActivity().setTitle("User Details");

        edUsername = view.findViewById(R.id.edUsername);
        edPassword = view.findViewById(R.id.edPassword);
        edConfirmPassword = view.findViewById(R.id.edConfirmPassword);
        btnSubmit = view.findViewById(R.id.btnSubmit);
        spinner = view.findViewById(R.id.spinner);

        btnSubmit.setOnClickListener(this);

        ArrayList<String> userTypeArray = new ArrayList<>();
        userTypeArray.add("Select User Type");
        userTypeArray.add("Manager");
        userTypeArray.add("Captain");

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, userTypeArray);
        spinner.setAdapter(spinnerAdapter);

        String str = "";
        try {
            str = getArguments().getString("model");
        } catch (Exception e) {
        }

        Gson gson = new Gson();
        AdminModel model = gson.fromJson(str, AdminModel.class);

        //Log.e("GSON", "--------------" + model);

        if (model != null) {
            //Log.e("MODEL : ", "--------------" + model);

            adminId=model.getAdminId();

            edUsername.setText(model.getUsername());
            edPassword.setText(model.getPassword());
            edConfirmPassword.setText(model.getPassword());

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, userTypeArray);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            if (!model.getType().equals(null)) {
                int spinnerPosition = adapter.getPosition(model.getType());
                spinner.setSelection(spinnerPosition);
            }

        }

        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnSubmit) {

            strUsername = edUsername.getText().toString();
            strPass = edPassword.getText().toString();
            strConfirmPass = edConfirmPassword.getText().toString();
            strType = spinner.getSelectedItem().toString();

            validate();
        }
    }

    public void validate() {

        boolean isType = false, isUsername = false, isPass = false, isConfirmPass = false;

        if (spinner.getSelectedItemPosition() == 0) {
            TextView view = (TextView) spinner.getSelectedView();
            view.setError("required");
            isType = false;
        } else {
            TextView view = (TextView) spinner.getSelectedView();
            view.setError(null);
            isType = true;
        }

        if (strUsername.isEmpty()) {
            edUsername.setError("required");
            isUsername = false;
        } else {
            edUsername.setError(null);
            isUsername = true;
        }

        if (strPass.isEmpty()) {
            edPassword.setError("required");
            isPass = false;
        } else {
            edPassword.setError(null);
            isPass = true;
        }

        if (strConfirmPass.isEmpty()) {
            edConfirmPassword.setError("required");
            isConfirmPass = false;
        } else if (!strConfirmPass.equals(strPass)) {
            edConfirmPassword.setError("password not matched");
            isConfirmPass = false;
        } else {
            edConfirmPassword.setError(null);
            isConfirmPass = true;
        }


        if (isType && isUsername && isPass && isConfirmPass) {
            AdminModel model = new AdminModel(adminId,strUsername, strPass, strType, 1, "token");
            saveAdminUser(model);

        }
    }

    public void saveAdminUser(AdminModel adminModel) {
        if (Constants.isOnline(getContext())) {
            final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<AdminModel> listCall = Constants.myInterface.saveAdminUser(adminModel);
            listCall.enqueue(new Callback<AdminModel>() {
                @Override
                public void onResponse(Call<AdminModel> call, Response<AdminModel> response) {
                    try {
                        if (response.body() != null) {

                            //Log.e("Admin Data : ", "------------" + response.body());

                            AdminModel data = response.body();
                            if (data == null) {
                                commonDialog.dismiss();
                                Toast.makeText(getContext(), "Unable to process!", Toast.LENGTH_SHORT).show();
                            } else {

                                Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
                                commonDialog.dismiss();

                                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                                ft.replace(R.id.content_frame, new UserMasterFragment(), "HomeFragment");
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
                public void onFailure(Call<AdminModel> call, Throwable t) {
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
