package com.ats.samarthajuice.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ats.samarthajuice.activity.MenuWithSearchActivity;
import com.ats.samarthajuice.R;
import com.ats.samarthajuice.activity.GenerateBillActivity;
import com.ats.samarthajuice.activity.HomeActivity;
import com.ats.samarthajuice.model.Admin;
import com.ats.samarthajuice.model.TableBusyModel;
import com.ats.samarthajuice.util.CustomSharedPreference;
import com.google.gson.Gson;

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

//------------------------

        TextView tvTableNo,tvTotal;
        LinearLayout llBill,llOrder;

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

            //-------------------------------------------

            holder.tvTableNo=rowView.findViewById(R.id.tvTableNo);
            holder.tvTotal=rowView.findViewById(R.id.tvTotal);
            holder.llBill=rowView.findViewById(R.id.llBill);
            holder.llOrder=rowView.findViewById(R.id.llOrder);

            rowView.setTag(holder);

        } else {
            holder = (Holder) rowView.getTag();
        }

//        holder.tvTable.setText("Table No. " + tableList.get(position).getTableName());
//        holder.tvKot.setText("Last Kot : " + tableList.get(position).getOrderId());
//        holder.tvAmount.setText("Amount : " + tableList.get(position).getTotalAmt());


        try{

            Gson gson = new Gson();
            String jsonAdmin = CustomSharedPreference.getString(context, CustomSharedPreference.KEY_ADMIN);
            final Admin admin = gson.fromJson(jsonAdmin, Admin.class);

            if (admin!=null){
                if (admin.getType().equalsIgnoreCase("Captain")){
                    holder.btnBill.setVisibility(View.GONE);
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }


        holder.tvTable.setText("" + tableList.get(position).getTableName());
        holder.tvKot.setText("" + tableList.get(position).getOrderId());
        holder.tvAmount.setText("" + tableList.get(position).getTotalAmt());

        holder.tvTableNo.setText(""+tableList.get(position).getTableName());
        holder.tvTotal.setText(" - "+tableList.get(position).getTotalAmt());

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

        //----------------------------------------------------------------------

        holder.llOrder.setOnClickListener(new View.OnClickListener() {
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

        holder.llBill.setOnClickListener(new View.OnClickListener() {
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
