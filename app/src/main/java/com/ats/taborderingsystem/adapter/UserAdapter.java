package com.ats.taborderingsystem.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.ats.taborderingsystem.R;
import com.ats.taborderingsystem.activity.HomeActivity;
import com.ats.taborderingsystem.constant.Constants;
import com.ats.taborderingsystem.fragment.AddUserFragment;
import com.ats.taborderingsystem.fragment.UserMasterFragment;
import com.ats.taborderingsystem.model.AdminModel;
import com.ats.taborderingsystem.model.ErrorMessage;
import com.ats.taborderingsystem.util.CommonDialog;
import com.ats.taborderingsystem.util.ShowPopupMenuIcon;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder> {

    private ArrayList<AdminModel> userList;
    private Context context;

    public UserAdapter(ArrayList<AdminModel> userList, Context context) {
        this.userList = userList;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName, tvPass, tvType;
        public ImageView ivMenu;

        public MyViewHolder(View view) {
            super(view);
            tvName = view.findViewById(R.id.tvName);
            tvPass = view.findViewById(R.id.tvPass);
            tvType = view.findViewById(R.id.tvType);
            ivMenu = view.findViewById(R.id.ivMenu);
        }
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_user, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final AdminModel model = userList.get(position);
        Log.e("Adapter : ", " model : " + model);

        holder.tvName.setText(model.getUsername());
        holder.tvPass.setText(model.getPassword());
        holder.tvType.setText(model.getType());

        holder.ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PopupMenu popupMenu = new PopupMenu(context, v);
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if (menuItem.getItemId() == R.id.item_edit) {

                            HomeActivity activity = (HomeActivity) context;

                            Gson gson = new Gson();
                            String str = gson.toJson(model);

                            Fragment adf = new AddUserFragment();
                            Bundle args = new Bundle();
                            args.putString("model", str);
                            adf.setArguments(args);
                            activity.getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, adf, "UserMasterFragment").commit();

                        } else if (menuItem.getItemId() == R.id.item_delete) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setTitle("Confirm Action");
                            builder.setMessage("Do you want to delete?");
                            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    deleteUser(model.getAdminId());
                                }
                            });
                            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });

                            AlertDialog dialog = builder.create();
                            dialog.show();

                        }
                        return true;
                    }

                });
                ShowPopupMenuIcon.setForceShowIcon(popupMenu);
                popupMenu.show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public void updateList(ArrayList<AdminModel> list){
        userList = list;
        notifyDataSetChanged();
    }




    public void deleteUser(int userId) {
        if (Constants.isOnline(context)) {
            final CommonDialog commonDialog = new CommonDialog(context, "Loading", "Please Wait...");
            commonDialog.show();

            Call<ErrorMessage> listCall = Constants.myInterface.deleteAdminUser(userId);
            listCall.enqueue(new Callback<ErrorMessage>() {
                @Override
                public void onResponse(Call<ErrorMessage> call, Response<ErrorMessage> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("Data : ", "------------" + response.body());

                            ErrorMessage data = response.body();
                            if (data == null) {
                                commonDialog.dismiss();
                                Toast.makeText(context, "Unable to process!", Toast.LENGTH_SHORT).show();
                            } else {

                                commonDialog.dismiss();

                                HomeActivity activity=(HomeActivity)context;

                                FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
                                ft.replace(R.id.content_frame, new UserMasterFragment(), "HomeFragment");
                                ft.commit();

                            }
                        } else {
                            commonDialog.dismiss();
                            Toast.makeText(context, "Unable to process!", Toast.LENGTH_SHORT).show();
                            Log.e("Data Null : ", "-----------");
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Toast.makeText(context, "Unable to process!", Toast.LENGTH_SHORT).show();
                        Log.e("Exception : ", "-----------" + e.getMessage());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ErrorMessage> call, Throwable t) {
                    commonDialog.dismiss();
                    Toast.makeText(context, "Unable to process!", Toast.LENGTH_SHORT).show();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(context, "No Internet Connection!", Toast.LENGTH_SHORT).show();
        }

    }

}
