package com.ats.samarthajuice.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.ats.samarthajuice.fragment.AddCategoryFragment;
import com.ats.samarthajuice.R;
import com.ats.samarthajuice.activity.HomeActivity;
import com.ats.samarthajuice.constant.Constants;
import com.ats.samarthajuice.fragment.CategoryMasterFragment;
import com.ats.samarthajuice.model.CategoryModel;
import com.ats.samarthajuice.model.ErrorMessage;
import com.ats.samarthajuice.util.CommonDialog;
import com.ats.samarthajuice.util.ShowPopupMenuIcon;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryAdapter extends BaseAdapter implements Filterable {

    private ArrayList<CategoryModel> categoryList;
    private ArrayList<CategoryModel> originalList;
    private Context context;
    private static LayoutInflater inflater = null;

    public CategoryAdapter(ArrayList<CategoryModel> categoryList, Context context) {
        this.categoryList = categoryList;
        this.originalList = categoryList;
        this.context = context;
        this.inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return categoryList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults results = new FilterResults();
                ArrayList<CategoryModel> filteredArrayList = new ArrayList<CategoryModel>();

                if (originalList == null) {
                    originalList = new ArrayList<CategoryModel>(categoryList);
                }

                if (charSequence == null || charSequence.length() == 0) {
                    results.count = originalList.size();
                    results.values = originalList;
                } else {
                    charSequence = charSequence.toString().toLowerCase();
                    for (int i = 0; i < originalList.size(); i++) {
                        String name = originalList.get(i).getCatName();
                        if (name.toLowerCase().contains(charSequence.toString())) {
                            filteredArrayList.add(new CategoryModel(originalList.get(i).getCatId(), originalList.get(i).getCatName(), originalList.get(i).getCatDesc(), originalList.get(i).getCatImage(), originalList.get(i).getDelStatus(), originalList.get(i).getUserId(), originalList.get(i).getUpdatedDate()));
                        }
                    }
                    results.count = filteredArrayList.size();
                    results.values = filteredArrayList;
                }

                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                categoryList = (ArrayList<CategoryModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };

        return filter;
    }

    public static class Holder {
        TextView tvName, tvDesc;
        ImageView imageView, ivMenu;
    }


    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        final Holder holder;
        View rowView = convertView;

        if (rowView == null) {
            holder = new Holder();
            LayoutInflater inflater = LayoutInflater.from(context);
            rowView = inflater.inflate(R.layout.adapter_category, null);

            holder.tvName = rowView.findViewById(R.id.tvName);
            holder.tvDesc = rowView.findViewById(R.id.tvDesc);
            holder.imageView = rowView.findViewById(R.id.imageView);
            holder.ivMenu = rowView.findViewById(R.id.ivMenu);

            rowView.setTag(holder);

        } else {
            holder = (Holder) rowView.getTag();
        }

        holder.tvName.setText(categoryList.get(position).getCatName());
        holder.tvDesc.setText(categoryList.get(position).getCatDesc());

        try {
            Picasso.get().load(Constants.categoryImagePath + "" + categoryList.get(position).getCatImage())
                    .placeholder(R.drawable.samartha_logo)
                    .error(R.drawable.samartha_logo)
                    .into(holder.imageView);
        } catch (Exception e) {
        }

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
                            String str = gson.toJson(categoryList.get(position));

                            Fragment adf = new AddCategoryFragment();
                            Bundle args = new Bundle();
                            args.putString("model", str);
                            adf.setArguments(args);
                            activity.getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, adf, "CategoryMasterFragment").commit();

                        } else if (menuItem.getItemId() == R.id.item_delete) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setTitle("Confirm Action");
                            builder.setMessage("Do you want to delete?");
                            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    deleteCategory(categoryList.get(position).getCatId());
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

        return rowView;
    }


    public void deleteCategory(int catId) {
        if (Constants.isOnline(context)) {
            final CommonDialog commonDialog = new CommonDialog(context, "Loading", "Please Wait...");
            commonDialog.show();

            Call<ErrorMessage> listCall = Constants.myInterface.deleteCategory(catId);
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

                                HomeActivity activity = (HomeActivity) context;

                                FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
                                ft.replace(R.id.content_frame, new CategoryMasterFragment(), "HomeFragment");
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
