package com.ats.taborderingsystem.adapter;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ats.taborderingsystem.R;
import com.ats.taborderingsystem.model.CategoryItemModel;
import com.ats.taborderingsystem.model.Item;
import com.ats.taborderingsystem.model.ItemModel;

import java.util.ArrayList;

public class MenuItemAdapter extends BaseAdapter implements Filterable {

    private ArrayList<CategoryItemModel> itemList;
    private ArrayList<CategoryItemModel> originalList;
    private Context context;
    private static LayoutInflater inflater = null;

    public MenuItemAdapter(ArrayList<CategoryItemModel> itemList, Context context) {
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
        public TextView tvTitle, tvCount;
        public ListView listView;
    }


    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        final Holder holder;
        View rowView = convertView;

        if (rowView == null) {
            holder = new Holder();
            LayoutInflater inflater = LayoutInflater.from(context);
            rowView = inflater.inflate(R.layout.adapter_menu_item, null);

            holder.tvTitle = rowView.findViewById(R.id.tvTitle);
            holder.tvCount = rowView.findViewById(R.id.tvCount);
            holder.listView = rowView.findViewById(R.id.listView);
            rowView.setTag(holder);

        } else {
            holder = (Holder) rowView.getTag();
        }

        holder.tvTitle.setText(itemList.get(position).getCatName());
        holder.tvCount.setText("" + itemList.get(position).getItemList().size() + " items");

        DisplayItemAdapter adapter = new DisplayItemAdapter((ArrayList<Item>) itemList.get(position).getItemList(), context);
        holder.listView.setAdapter(adapter);

        setListViewHeightBasedOnChildren(holder.listView);

        return rowView;
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, LinearLayoutCompat.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults results = new FilterResults();
                ArrayList<CategoryItemModel> filteredArrayList = new ArrayList<CategoryItemModel>();

                if (originalList == null) {
                    originalList = new ArrayList<CategoryItemModel>(itemList);
                }

                if (charSequence == null || charSequence.length() == 0) {
                    results.count = originalList.size();
                    results.values = originalList;
                } else {
                    charSequence = charSequence.toString().toLowerCase();
                    for (int i = 0; i < originalList.size(); i++) {
                        String name = originalList.get(i).getCatName();
                        if (name.toLowerCase().contains(charSequence.toString())) {
                            filteredArrayList.add(new CategoryItemModel(originalList.get(i).getCatId(), originalList.get(i).getCatName(), originalList.get(i).getCatDesc(), originalList.get(i).getCatImage(), originalList.get(i).getDelStatus(), originalList.get(i).getUserId(), originalList.get(i).getUpdatedDate(), originalList.get(i).getItemList()));
                        }
                    }
                    results.count = filteredArrayList.size();
                    results.values = filteredArrayList;
                }

                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                itemList = (ArrayList<CategoryItemModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };

        return filter;
    }
}
