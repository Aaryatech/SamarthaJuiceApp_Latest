package com.ats.samarthajuice.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.ats.samarthajuice.R;
import com.ats.samarthajuice.adapter.TableCategoryAdapter;
import com.ats.samarthajuice.constant.Constants;
import com.ats.samarthajuice.model.TableCategoryModel;
import com.ats.samarthajuice.util.CommonDialog;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TableCategoryMasterFragment extends Fragment {

    private GridView gridView;
    private FloatingActionButton fab;

    private ArrayList<TableCategoryModel> tableCategoryList = new ArrayList<>();
    TableCategoryAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_table_category_master, container, false);

        getActivity().setTitle("Table Category");

        gridView = view.findViewById(R.id.gridView);
        fab = view.findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, new AddTableCategoryFragment(), "TableCategoryMasterFragment");
                ft.commit();
            }
        });

        getAllTableCategory();

        return view;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        MenuItem item = menu.findItem(R.id.action_search);
        item.setVisible(true);

        SearchView searchView = (SearchView) item.getActionView();
        EditText searchEditText = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(getResources().getColor(R.color.colorWhite));
        searchEditText.setHintTextColor(getResources().getColor(R.color.colorWhite));
        ImageView v = (ImageView) searchView.findViewById(android.support.v7.appcompat.R.id.search_button);
        v.setImageResource(R.drawable.ic_search); //Changing the image

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (adapter != null)
                    adapter.getFilter().filter(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        searchView.setQueryHint("search");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public void getAllTableCategory() {

        if (Constants.isOnline(getContext())) {
            final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<ArrayList<TableCategoryModel>> listCall = Constants.myInterface.getAllTableCategory();
            listCall.enqueue(new Callback<ArrayList<TableCategoryModel>>() {
                @Override
                public void onResponse(Call<ArrayList<TableCategoryModel>> call, Response<ArrayList<TableCategoryModel>> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("Category Data : ", "------------" + response.body());

                            ArrayList<TableCategoryModel> data = response.body();
                            if (data == null) {
                                commonDialog.dismiss();
                                Toast.makeText(getContext(), "No category found", Toast.LENGTH_SHORT).show();
                            } else {

                                tableCategoryList.clear();
                                tableCategoryList = data;

                                adapter = new TableCategoryAdapter(tableCategoryList, getContext());
                                gridView.setAdapter(adapter);

                                commonDialog.dismiss();
                            }
                        } else {
                            commonDialog.dismiss();
                            Toast.makeText(getContext(), "No category found", Toast.LENGTH_SHORT).show();
                            Log.e("Data Null : ", "-----------");
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        //  Toast.makeText(getContext(), "No categories found", Toast.LENGTH_SHORT).show();
                        Log.e("Exception : ", "-----------" + e.getMessage());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<TableCategoryModel>> call, Throwable t) {
                    commonDialog.dismiss();
                    Toast.makeText(getContext(), "No category found", Toast.LENGTH_SHORT).show();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(getContext(), "No Internet Connection!", Toast.LENGTH_SHORT).show();
        }
    }

}
