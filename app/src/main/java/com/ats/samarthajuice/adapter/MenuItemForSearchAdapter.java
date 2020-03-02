package com.ats.samarthajuice.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ats.samarthajuice.R;
import com.ats.samarthajuice.model.ItemModel;

import java.util.ArrayList;

import static com.ats.samarthajuice.activity.MenuWithSearchActivity.spRemark;
import static com.ats.samarthajuice.activity.MenuWithSearchActivity.staticItemList;

public class MenuItemForSearchAdapter extends BaseAdapter implements Filterable {

    private ArrayList<ItemModel> itemList;
    private ArrayList<ItemModel> originalList;
    private Context context;
    private static LayoutInflater inflater = null;

    public MenuItemForSearchAdapter(ArrayList<ItemModel> itemList, Context context) {
        this.itemList = itemList;
        this.originalList = itemList;
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
        public ImageView imageView;
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
        holder.imageView.setVisibility(View.INVISIBLE);
       /* if (itemList.get(position).getQty() == 0) {
            holder.tvQty.setText("");
        } else {
            holder.tvQty.setText("" + itemList.get(position).getQty());
        }*/

       /* if (itemList.get(position).isCancelStatus()) {
            holder.imageView.setVisibility(View.VISIBLE);
        } else {
            holder.imageView.setVisibility(View.INVISIBLE);
        }*/

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemList.get(position).setQty(0);
                itemList.get(position).setCancelStatus(false);
                for (int j = 0; j < originalList.size(); j++) {
                    if (originalList.get(j).getItemId() == itemList.get(position).getItemId()) {
                        originalList.get(position).setQty(0);
                        originalList.get(position).setCancelStatus(false);
                    }
                }

                notifyDataSetChanged();

                Intent pushNotificationIntent = new Intent();
                pushNotificationIntent.setAction("REFRESH");

                pushNotificationIntent.putExtra("itemId", itemList.get(position).getItemId());

                LocalBroadcastManager.getInstance(context).sendBroadcast(pushNotificationIntent);

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

                        Log.e("Item Pos","--------------------------------------"+intArray.get(position));

                        tvQty.setText("" + intArray.get(position));
                        return view;
                    }
                };
                listView.setAdapter(arrayIntAdapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        //holder.tvQty.setText("" + (i + 1));
                       /* itemList.get(position).setQty((i + 1));
                        itemList.get(position).setCancelStatus(true);
                        for (int j = 0; j < originalList.size(); j++) {
                            if (originalList.get(j).getItemId() == itemList.get(position).getItemId()) {
                                originalList.get(j).setQty((i + 1));
                                originalList.get(j).setCancelStatus(true);
                                if (spRemark.getSelectedItemPosition()==0){
                                    originalList.get(j).setRemark("");
                                }else{
                                    originalList.get(j).setRemark(spRemark.getSelectedItem().toString());
                                }

                            }
                        }
                        dialog.dismiss();
                        notifyDataSetChanged();

                        Intent pushNotificationIntent = new Intent();
                        pushNotificationIntent.setAction("REFRESH_TEMP");
                        LocalBroadcastManager.getInstance(context).sendBroadcast(pushNotificationIntent);
*/

                        ItemModel item=itemList.get(position);

                        if (spRemark.getSelectedItemPosition()==0){
                            item.setRemark("");
                        }else{
                            item.setRemark(spRemark.getSelectedItem().toString());
                        }

                        ItemModel newItem=new ItemModel(item.getItemId(),item.getItemName(),item.getMrpRegular(),(i + 1),item.getRemark());
                        staticItemList.add(newItem);

                        Intent pushNotificationIntent = new Intent();
                        pushNotificationIntent.setAction("REFRESH_TEMP");
                        LocalBroadcastManager.getInstance(context).sendBroadcast(pushNotificationIntent);

                        dialog.dismiss();

                    }
                });

                dialog.show();
            }
        });


        return rowView;
    }


    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults results = new FilterResults();
                ArrayList<ItemModel> filteredArrayList = new ArrayList<ItemModel>();

                if (originalList == null) {
                    originalList = new ArrayList<ItemModel>(itemList);
                }

                if (charSequence == null || charSequence.length() == 0) {
                    results.count = originalList.size();
                    results.values = originalList;
                } else {
                    charSequence = charSequence.toString().toLowerCase();
                    for (int i = 0; i < originalList.size(); i++) {
                        String name = originalList.get(i).getItemName();
                        if (name.toLowerCase().contains(charSequence.toString())) {
                            filteredArrayList.add(new ItemModel(originalList.get(i).getItemId(), originalList.get(i).getItemName(), originalList.get(i).getItemDesc(), originalList.get(i).getItemImage(), originalList.get(i).getMrpGame(), originalList.get(i).getMrpRegular(), originalList.get(i).getMrpSpecial(), originalList.get(i).getOpeningRate(), originalList.get(i).getMaxRate(), originalList.get(i).getMinRate(), originalList.get(i).getCurrentStock(), originalList.get(i).getCatId(), originalList.get(i).getSgst(), originalList.get(i).getCgst(), originalList.get(i).getIsMixerApplicable(), originalList.get(i).getUserId(), originalList.get(i).getUpdatedDate(), originalList.get(i).getDelStatus(), originalList.get(i).getMinStock(), originalList.get(i).getHsnCode(), originalList.get(i).getQty(), originalList.get(i).isCancelStatus()));
                        }
                    }
                    results.count = filteredArrayList.size();
                    results.values = filteredArrayList;
                }

                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                itemList = (ArrayList<ItemModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };

        return filter;
    }

}
