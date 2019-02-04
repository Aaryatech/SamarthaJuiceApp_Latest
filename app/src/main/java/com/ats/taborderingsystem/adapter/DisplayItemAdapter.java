package com.ats.taborderingsystem.adapter;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ats.taborderingsystem.R;
import com.ats.taborderingsystem.model.Item;

import java.util.ArrayList;

public class DisplayItemAdapter extends BaseAdapter {

    private ArrayList<Item> itemList;
    private Context context;
    private static LayoutInflater inflater = null;

    public DisplayItemAdapter(ArrayList<Item> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
        this.inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return itemList.size();
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
        public TextView tvItem, tvQty, tvRate;
        public LinearLayout linearLayout;
        private ImageView imageView;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Holder holder;
        View rowView = convertView;

        if (rowView == null) {
            holder = new Holder();
            LayoutInflater inflater = LayoutInflater.from(context);
            rowView = inflater.inflate(R.layout.adapter_display_item, null);

            holder.tvItem = rowView.findViewById(R.id.tvItem);
            holder.tvQty = rowView.findViewById(R.id.tvQty);
            holder.tvRate = rowView.findViewById(R.id.tvRate);
            holder.linearLayout = rowView.findViewById(R.id.linearLayout);
            holder.imageView = rowView.findViewById(R.id.imageView);

            rowView.setTag(holder);

        } else {
            holder = (Holder) rowView.getTag();
        }

        holder.tvItem.setText(itemList.get(position).getItemName());
        holder.tvRate.setText("" + itemList.get(position).getMrpRegular());

        if (itemList.get(position).getQty() == 0) {
            holder.tvQty.setText("");
        } else {
            holder.tvQty.setText("" + itemList.get(position).getQty());
        }

        if (itemList.get(position).isCancelStatus()) {
            holder.imageView.setVisibility(View.VISIBLE);
        } else {
            holder.imageView.setVisibility(View.INVISIBLE);
        }

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemList.get(position).setQty(0);
                itemList.get(position).setCancelStatus(false);
                notifyDataSetChanged();
            }
        });

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_quantity);

                final ListView listView = dialog.findViewById(R.id.listView);

                final ArrayList<Integer> intArray = new ArrayList<>();
                for (int i = 1; i <= 100; i++) {
                    intArray.add(i);
                }

                ArrayAdapter<Integer> arrayIntAdapter = new ArrayAdapter<Integer>(context, android.R.layout.simple_expandable_list_item_1, intArray) {

                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {

                        LayoutInflater inflater1 = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View view = inflater1.inflate(R.layout.adapter_quantity, parent, false);
                        TextView tvQty = view.findViewById(R.id.tvQtyItem);
                        tvQty.setText("" + intArray.get(position));
                        return view;
                    }
                };
                listView.setAdapter(arrayIntAdapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        //holder.tvQty.setText("" + (i + 1));
                        itemList.get(position).setQty((i + 1));
                        itemList.get(position).setCancelStatus(true);
                        dialog.dismiss();
                        notifyDataSetChanged();

                    }
                });

                dialog.show();
            }
        });


        return rowView;
    }

}
