package com.ats.samarthajuice.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ats.samarthajuice.R;
import com.ats.samarthajuice.activity.HomeActivity;
import com.ats.samarthajuice.constant.Constants;
import com.ats.samarthajuice.model.ErrorMessage;
import com.ats.samarthajuice.model.TableBusyModel;
import com.ats.samarthajuice.model.TableFreeModel;
import com.ats.samarthajuice.util.CommonDialog;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangeTableAdapter extends RecyclerView.Adapter<ChangeTableAdapter.MyViewHolder> {

    private ArrayList<TableFreeModel> tableList;
    private Context context;
    TableBusyModel tableBusyModel;

    public ChangeTableAdapter(ArrayList<TableFreeModel> tableList, Context context,  TableBusyModel tableBusyModel) {
        this.tableList = tableList;
        this.context = context;
        this.tableBusyModel = tableBusyModel;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTable;
        public LinearLayout linearLayout;

        public MyViewHolder(View view) {
            super(view);
            tvTable = view.findViewById(R.id.tvTable);
            linearLayout = view.findViewById(R.id.linearLayout);
        }
    }


    @Override
    public ChangeTableAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_free_table, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ChangeTableAdapter.MyViewHolder holder, int position) {
        final TableFreeModel model = tableList.get(position);
        Log.e("Adapter : ", " model : " + model);

        // holder.tvTable.setText("Table No. " + model.getTableName());
        holder.tvTable.setText("" + model.getTableName());

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              getUpdateTable(model.getTableNo(),tableBusyModel.getOrderId());
            }
        });

        if (position%2==0){
            holder.linearLayout.setBackgroundColor(context.getResources().getColor(R.color.colorGray));
        }else{
            holder.linearLayout.setBackgroundColor(context.getResources().getColor(R.color.colorWhite));
        }

    }

    @Override
    public int getItemCount() {
        return tableList.size();
    }

    public void getUpdateTable(int tableNo,int orderId) {
        Log.e("PARAMETER","      TABLE NO   :   "+tableNo+"      ORDER NO :    "+orderId);
        if (Constants.isOnline(context)) {
            final CommonDialog commonDialog = new CommonDialog(context, "Loading", "Please Wait...");
            commonDialog.show();

            Call<ErrorMessage> listCall = Constants.myInterface.changeTableNoInOrder(tableNo,orderId);
            listCall.enqueue(new Callback<ErrorMessage>() {
                @Override
                public void onResponse(Call<ErrorMessage> call, Response<ErrorMessage> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("Update table : ", "------------" + response.body());

//                            HomeActivity activity = (HomeActivity) context;
//                            FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
//                            ft.replace(R.id.content_frame, new HomeFragment(), "Home");
//                            ft.commit();

                            Intent intent = new Intent(context, HomeActivity.class);
                            context.startActivity(intent);

                                commonDialog.dismiss();
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
                public void onFailure(Call<ErrorMessage> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(context, "No Internet Connection!", Toast.LENGTH_SHORT).show();
        }
    }

}
