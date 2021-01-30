package com.ats.samarthajuice.activity;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ats.samarthajuice.R;
import com.ats.samarthajuice.constant.Constants;
import com.ats.samarthajuice.model.LoginModel;
import com.ats.samarthajuice.util.CommonDialog;
import com.ats.samarthajuice.util.CustomSharedPreference;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edUsername, edPassword;
    private Button btnSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edPassword = findViewById(R.id.edPassword);
        edUsername = findViewById(R.id.edUsername);
        btnSignIn = findViewById(R.id.btnSignIn);
        btnSignIn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnSignIn) {

            if (edUsername.getText().toString().isEmpty()){
                edUsername.setError("required");
            }else if (edPassword.getText().toString().isEmpty()){
                edUsername.setError(null);
                edPassword.setError("required");
            }else{
                edPassword.setError(null);

                loginUser(edUsername.getText().toString(),edPassword.getText().toString());
            }

        }
    }

    public void loginUser(String username, String pass) {

        if (Constants.isOnline(this)) {
            final CommonDialog commonDialog = new CommonDialog(this, "Loading", "Please Wait...");
            commonDialog.show();

            Call<LoginModel> listCall = Constants.myInterface.loginUser(username, pass);
            listCall.enqueue(new Callback<LoginModel>() {
                @Override
                public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                    try {
                        if (response.body() != null) {

                            //Log.e("Login Data : ", "------------" + response.body());

                            LoginModel data = response.body();
                            if (data == null) {
                                Toast.makeText(LoginActivity.this, "Unable to login!", Toast.LENGTH_SHORT).show();
                                commonDialog.dismiss();
                            } else {

                                if (!data.isError()) {

                                    Gson gson = new Gson();
                                    String jsonAdmin = gson.toJson(data.getAdmin());
                                    CustomSharedPreference.putString(LoginActivity.this, CustomSharedPreference.KEY_ADMIN, jsonAdmin);

                                    startActivity(new Intent(LoginActivity.this,HomeActivity.class));
                                    finish();

                                }else{
                                    Toast.makeText(LoginActivity.this, ""+data.getMessage(), Toast.LENGTH_SHORT).show();
                                }

                                commonDialog.dismiss();
                            }
                        } else {
                            Toast.makeText(LoginActivity.this, "Unable to login!", Toast.LENGTH_SHORT).show();

                            commonDialog.dismiss();
                            //Log.e("Data Null : ", "-----------");
                        }
                    } catch (Exception e) {
                        Toast.makeText(LoginActivity.this, "Unable to login!", Toast.LENGTH_SHORT).show();
                        commonDialog.dismiss();
                        //Log.e("Exception : ", "-----------" + e.getMessage());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<LoginModel> call, Throwable t) {
                    commonDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Unable to login!", Toast.LENGTH_SHORT).show();
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


}
