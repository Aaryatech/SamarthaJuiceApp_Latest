package com.ats.samarthajuice.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ats.samarthajuice.R;
import com.ats.samarthajuice.constant.Constants;
import com.ats.samarthajuice.model.CategoryItemModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MenuCategoryAdapter extends BaseAdapter {

    private ArrayList<CategoryItemModel> categoryModelList;
    private Context context;
    private static LayoutInflater inflater = null;

    public MenuCategoryAdapter(ArrayList<CategoryItemModel> categoryModelList, Context context) {
        this.categoryModelList = categoryModelList;
        this.context = context;
        this.inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return categoryModelList.size();
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
        TextView tvName;
        ImageView imageView;
        LinearLayout linearLayout;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Holder holder;
        View rowView = convertView;

        if (rowView == null) {
            holder = new Holder();
            LayoutInflater inflater = LayoutInflater.from(context);
            rowView = inflater.inflate(R.layout.adapter_menu_category, null);

            holder.tvName = rowView.findViewById(R.id.tvName);
            holder.imageView = rowView.findViewById(R.id.imageView);

            rowView.setTag(holder);

        } else {
            holder = (Holder) rowView.getTag();
        }

        holder.tvName.setText(categoryModelList.get(position).getCatName());

        try {
            Picasso.get().load(Constants.categoryImagePath + "" + categoryModelList.get(position).getCatImage())
                    .placeholder(R.drawable.logo)
                    .error(R.drawable.logo)
                    .into(holder.imageView);
        } catch (Exception e) {
        }


        return rowView;
    }


}
