package com.ats.samarthajuice.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import android.widget.ImageView;
import android.widget.Toast;

import com.ats.samarthajuice.R;
import com.ats.samarthajuice.adapter.UserAdapter;
import com.ats.samarthajuice.constant.Constants;
import com.ats.samarthajuice.model.AdminModel;
import com.ats.samarthajuice.util.CommonDialog;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserMasterFragment extends Fragment {


    private RecyclerView recyclerView;
    private FloatingActionButton fab;

    private ArrayList<AdminModel> userList = new ArrayList<>();

    UserAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_master, container, false);

        getActivity().setTitle("User");

        recyclerView = view.findViewById(R.id.recyclerView);
        fab = view.findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, new AddUserFragment(), "UserMasterFragment");
                ft.commit();
            }
        });

        getAdminUserList();

        return view;
    }

    public void getAdminUserList() {

        if (Constants.isOnline(getContext())) {
            final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<ArrayList<AdminModel>> listCall = Constants.myInterface.getAllAdminUser();
            listCall.enqueue(new Callback<ArrayList<AdminModel>>() {
                @Override
                public void onResponse(Call<ArrayList<AdminModel>> call, Response<ArrayList<AdminModel>> response) {
                    try {
                        if (response.body() != null) {

                            //Log.e("Admin Data : ", "------------" + response.body());

                            ArrayList<AdminModel> data = response.body();
                            if (data == null) {
                                commonDialog.dismiss();
                                Toast.makeText(getContext(), "No users found", Toast.LENGTH_SHORT).show();
                            } else {

                                userList.clear();
                                userList = data;

                                adapter = new UserAdapter(userList, getContext());
                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                                recyclerView.setLayoutManager(mLayoutManager);
                                recyclerView.setItemAnimator(new DefaultItemAnimator());
                                recyclerView.setAdapter(adapter);

                                commonDialog.dismiss();
                            }
                        } else {
                            commonDialog.dismiss();
                            Toast.makeText(getContext(), "No users found", Toast.LENGTH_SHORT).show();
                            //Log.e("Data Null : ", "-----------");
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Toast.makeText(getContext(), "No users found", Toast.LENGTH_SHORT).show();
                        //Log.e("Exception : ", "-----------" + e.getMessage());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<AdminModel>> call, Throwable t) {
                    commonDialog.dismiss();
                    Toast.makeText(getContext(), "No users found", Toast.LENGTH_SHORT).show();
                    //Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(getContext(), "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }
    }

    void filter(String text) {
        ArrayList<AdminModel> temp = new ArrayList();
        for (AdminModel d : userList) {
            if (d.getType().toLowerCase().contains(text) || d.getUsername().toLowerCase().contains(text)) {
                temp.add(d);
            }
        }
        adapter.updateList(temp);
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
                    filter(charSequence.toString());
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


}
