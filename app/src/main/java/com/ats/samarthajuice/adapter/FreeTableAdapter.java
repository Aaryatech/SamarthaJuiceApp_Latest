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

import com.ats.samarthajuice.activity.MenuWithSearchActivity;
import com.ats.samarthajuice.R;
import com.ats.samarthajuice.activity.HomeActivity;
import com.ats.samarthajuice.model.TableFreeModel;

import java.util.ArrayList;

public class FreeTableAdapter extends RecyclerView.Adapter<FreeTableAdapter.MyViewHolder> {

    private ArrayList<TableFreeModel> tableList;
    private Context context;

    public FreeTableAdapter(ArrayList<TableFreeModel> tableList, Context context) {
        this.tableList = tableList;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView  tvTable;
        public LinearLayout linearLayout;

        public MyViewHolder(View view) {
            super(view);
            tvTable = view.findViewById(R.id.tvTable);
            linearLayout = view.findViewById(R.id.linearLayout);
        }
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_free_table, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final TableFreeModel model = tableList.get(position);
        //Log.e("Adapter : ", " model : " + model);

       // holder.tvTable.setText("Table No. " + model.getTableName());
        holder.tvTable.setText("" + model.getTableName());

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MenuWithSearchActivity.class);
                intent.putExtra("table", model.getTableNo());
                intent.putExtra("tableName", model.getTableName());
                context.startActivity(intent);
                HomeActivity activity=(HomeActivity)context;
                activity.finish();
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


}
