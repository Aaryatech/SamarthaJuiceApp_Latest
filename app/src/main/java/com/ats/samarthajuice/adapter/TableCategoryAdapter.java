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
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.ats.samarthajuice.fragment.AddTableCategoryFragment;
import com.ats.samarthajuice.fragment.TableCategoryMasterFragment;
import com.ats.samarthajuice.R;
import com.ats.samarthajuice.activity.HomeActivity;
import com.ats.samarthajuice.constant.Constants;
import com.ats.samarthajuice.model.ErrorMessage;
import com.ats.samarthajuice.model.TableCategoryModel;
import com.ats.samarthajuice.util.CommonDialog;
import com.ats.samarthajuice.util.ShowPopupMenuIcon;
import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TableCategoryAdapter extends BaseAdapter implements Filterable {

    private ArrayList<TableCategoryModel> tableCategoryList;
    private ArrayList<TableCategoryModel> originalList;
    private Context context;
    private static LayoutInflater inflater = null;

    public TableCategoryAdapter(ArrayList<TableCategoryModel> tableCategoryList, Context context) {
        this.tableCategoryList = tableCategoryList;
        this.originalList = tableCategoryList;
        this.context = context;
        this.inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return tableCategoryList.size();
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
        TextView tvName, tvDesc;
        LinearLayout linearLayout;
    }


    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        final Holder holder;
        View rowView = convertView;

        if (rowView == null) {
            holder = new Holder();
            LayoutInflater inflater = LayoutInflater.from(context);
            rowView = inflater.inflate(R.layout.adapter_table_category, null);

            holder.tvName = rowView.findViewById(R.id.tvName);
            holder.tvDesc = rowView.findViewById(R.id.tvDesc);
            holder.linearLayout=rowView.findViewById(R.id.linearLayout);

            rowView.setTag(holder);

        } else {
            holder = (Holder) rowView.getTag();
        }

        holder.tvName.setText(tableCategoryList.get(position).getTableCatName());
        holder.tvDesc.setText(tableCategoryList.get(position).getTableCatDesc());

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
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
                            String str = gson.toJson(tableCategoryList.get(position));

                            Fragment adf = new AddTableCategoryFragment();
                            Bundle args = new Bundle();
                            args.putString("model", str);
                            adf.setArguments(args);
                            activity.getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, adf, "TableCategoryMasterFragment").commit();

                        } else if (menuItem.getItemId() == R.id.item_delete) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setTitle("Confirm Action");
                            builder.setMessage("Do you want to delete?");
                            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                     deleteTableCategory(tableCategoryList.get(position).getTableCatId());
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

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults results = new FilterResults();
                ArrayList<TableCategoryModel> filteredArrayList = new ArrayList<TableCategoryModel>();

                if (originalList == null) {
                    originalList = new ArrayList<TableCategoryModel>(tableCategoryList);
                }

                if (charSequence == null || charSequence.length() == 0) {
                    results.count = originalList.size();
                    results.values = originalList;
                } else {
                    charSequence = charSequence.toString().toLowerCase();
                    for (int i = 0; i < originalList.size(); i++) {
                        String name = originalList.get(i).getTableCatName();
                        if (name.toLowerCase().contains(charSequence.toString())) {
                            filteredArrayList.add(new TableCategoryModel(originalList.get(i).getTableCatId(), originalList.get(i).getTableCatName(), originalList.get(i).getTableCatDesc(), originalList.get(i).getIsActive()));
                        }
                    }
                    results.count = filteredArrayList.size();
                    results.values = filteredArrayList;
                }

                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                tableCategoryList = (ArrayList<TableCategoryModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };

        return filter;
    }


    public void deleteTableCategory(int tableCatId) {
        if (Constants.isOnline(context)) {
            final CommonDialog commonDialog = new CommonDialog(context, "Loading", "Please Wait...");
            commonDialog.show();

            Call<ErrorMessage> listCall = Constants.myInterface.deleteTableCategory(tableCatId);
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
                                ft.replace(R.id.content_frame, new TableCategoryMasterFragment(), "HomeFragment");
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
