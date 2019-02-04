package com.ats.taborderingsystem.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ats.taborderingsystem.R;
import com.ats.taborderingsystem.activity.GenerateBillActivity;
import com.ats.taborderingsystem.activity.HomeActivity;
import com.ats.taborderingsystem.activity.MenuActivity;
import com.ats.taborderingsystem.activity.MenuWithSearchActivity;
import com.ats.taborderingsystem.model.TableBusyModel;

import java.util.ArrayList;

public class BusyTableAdapter extends BaseAdapter {

    private ArrayList<TableBusyModel> tableList;
    private ArrayList<TableBusyModel> originalList;
    private Context context;
    private static LayoutInflater inflater = null;

    public BusyTableAdapter(ArrayList<TableBusyModel> tableList, Context context) {
        this.tableList = tableList;
        this.originalList = tableList;
        this.context = context;
        this.inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return tableList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public static class Holder {
        TextView tvTable, tvAmount, tvKot;
        LinearLayout linearLayout;
        Button btnBill;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        final Holder holder;
        View rowView = convertView;

        if (rowView == null) {
            holder = new Holder();
            LayoutInflater inflater = LayoutInflater.from(context);
            rowView = inflater.inflate(R.layout.adapter_busy_table, null);

            holder.tvTable = rowView.findViewById(R.id.tvTable);
            holder.tvAmount = rowView.findViewById(R.id.tvAmount);
            holder.tvKot = rowView.findViewById(R.id.tvKot);
            holder.linearLayout = rowView.findViewById(R.id.linearLayout);
            holder.btnBill=rowView.findViewById(R.id.btnBill);

            rowView.setTag(holder);

        } else {
            holder = (Holder) rowView.getTag();
        }

//        holder.tvTable.setText("Table No. " + tableList.get(position).getTableName());
//        holder.tvKot.setText("Last Kot : " + tableList.get(position).getOrderId());
//        holder.tvAmount.setText("Amount : " + tableList.get(position).getTotalAmt());

        holder.tvTable.setText("" + tableList.get(position).getTableName());
        holder.tvKot.setText("" + tableList.get(position).getOrderId());
        holder.tvAmount.setText("" + tableList.get(position).getTotalAmt());

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MenuWithSearchActivity.class);
                intent.putExtra("table", tableList.get(position).getTableNo());
                intent.putExtra("tableName", tableList.get(position).getTableName());
                context.startActivity(intent);
                HomeActivity activity=(HomeActivity)context;
                activity.finish();
            }
        });

        holder.btnBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, GenerateBillActivity.class);
                intent.putExtra("table", tableList.get(position).getTableNo());
                intent.putExtra("tableName", tableList.get(position).getTableName());
                context.startActivity(intent);

            }
        });

        return rowView;
    }


}
