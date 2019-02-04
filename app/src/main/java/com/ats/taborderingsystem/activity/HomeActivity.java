package com.ats.taborderingsystem.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.ats.taborderingsystem.R;
import com.ats.taborderingsystem.constant.Constants;
import com.ats.taborderingsystem.fragment.AddCategoryFragment;
import com.ats.taborderingsystem.fragment.AddItemFragment;
import com.ats.taborderingsystem.fragment.AddTableCategoryFragment;
import com.ats.taborderingsystem.fragment.AddTableFragment;
import com.ats.taborderingsystem.fragment.AddUserFragment;
import com.ats.taborderingsystem.fragment.CategoryMasterFragment;
import com.ats.taborderingsystem.fragment.EditBillFragment;
import com.ats.taborderingsystem.fragment.HomeFragment;
import com.ats.taborderingsystem.fragment.ItemMasterFragment;
import com.ats.taborderingsystem.fragment.ItemReportFragment;
import com.ats.taborderingsystem.fragment.PrinterSettingsFragment;
import com.ats.taborderingsystem.fragment.ReportsFragment;
import com.ats.taborderingsystem.fragment.TableCategoryMasterFragment;
import com.ats.taborderingsystem.fragment.TableMasterFragment;
import com.ats.taborderingsystem.fragment.UserMasterFragment;
import com.ats.taborderingsystem.fragment.ViewBillsFragment;
import com.ats.taborderingsystem.model.Admin;
import com.ats.taborderingsystem.util.CustomSharedPreference;
import com.google.gson.Gson;

import java.io.File;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    NavigationView navigationView;

    String userType = "";

    File folder = new File(Environment.getExternalStorageDirectory() + File.separator, "HappyFeastCo/data" +
            "");
    File f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Gson gson = new Gson();
        String jsonAdmin = CustomSharedPreference.getString(this, CustomSharedPreference.KEY_ADMIN);
        final Admin admin = gson.fromJson(jsonAdmin, Admin.class);

        Log.e("Admin : ", "---------------------------" + admin);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        TextView tvName = header.findViewById(R.id.tvName);
        if (admin != null) {
            tvName.setText("" + admin.getUsername());
        }

        if (admin == null) {
            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
            finish();
        } else {
            userType = admin.getType();
            Log.e("USER TYPE : ", "------------------" + userType);
            hideItem();

        }


        if (savedInstanceState == null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, new HomeFragment(), "Home");
            ft.commit();
        }


        createFolder();
    }

    private void hideItem() {

        Menu nav_Menu = navigationView.getMenu();
        if (userType.equalsIgnoreCase("Captain")) {
            nav_Menu.findItem(R.id.nav_user).setVisible(false);
            nav_Menu.findItem(R.id.nav_category).setVisible(false);
            nav_Menu.findItem(R.id.nav_item).setVisible(false);
            nav_Menu.findItem(R.id.nav_table_category).setVisible(false);
            nav_Menu.findItem(R.id.nav_table).setVisible(false);
            nav_Menu.findItem(R.id.nav_report).setVisible(false);

        }

    }

    public void createFolder() {
        if (!folder.exists()) {
            folder.mkdir();
        }
    }

    @Override
    public void onBackPressed() {

        Fragment home = getSupportFragmentManager().findFragmentByTag("Home");
        Fragment homeFragment = getSupportFragmentManager().findFragmentByTag("HomeFragment");
        Fragment userMasterFragment = getSupportFragmentManager().findFragmentByTag("UserMasterFragment");
        Fragment categoryMasterFragment = getSupportFragmentManager().findFragmentByTag("CategoryMasterFragment");
        Fragment itemMasterFragment = getSupportFragmentManager().findFragmentByTag("ItemMasterFragment");
        Fragment tableCategoryMasterFragment = getSupportFragmentManager().findFragmentByTag("TableCategoryMasterFragment");
        Fragment tableMasterFragment = getSupportFragmentManager().findFragmentByTag("TableMasterFragment");
        Fragment reportsFragment = getSupportFragmentManager().findFragmentByTag("ReportsFragment");
        Fragment viewBillsFragment = getSupportFragmentManager().findFragmentByTag("ViewBillsFragment");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (home instanceof HomeFragment && home.isVisible()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
            builder.setTitle("Confirm Action");
            builder.setMessage("Do you want to exit?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
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


        } else if (homeFragment instanceof UserMasterFragment && homeFragment.isVisible() ||
                homeFragment instanceof CategoryMasterFragment && homeFragment.isVisible() ||
                homeFragment instanceof ItemMasterFragment && homeFragment.isVisible() ||
                homeFragment instanceof TableCategoryMasterFragment && homeFragment.isVisible() ||
                homeFragment instanceof TableMasterFragment && homeFragment.isVisible() ||
                homeFragment instanceof ReportsFragment && homeFragment.isVisible() ||
                homeFragment instanceof PrinterSettingsFragment && homeFragment.isVisible() ||
                homeFragment instanceof ViewBillsFragment && homeFragment.isVisible()) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, new HomeFragment(), "Home");
            ft.commit();

        } else if (userMasterFragment instanceof AddUserFragment && userMasterFragment.isVisible()) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, new UserMasterFragment(), "HomeFragment");
            ft.commit();

        } else if (categoryMasterFragment instanceof AddCategoryFragment && categoryMasterFragment.isVisible()) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, new CategoryMasterFragment(), "HomeFragment");
            ft.commit();

        } else if (itemMasterFragment instanceof AddItemFragment && itemMasterFragment.isVisible()) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, new ItemMasterFragment(), "HomeFragment");
            ft.commit();

        } else if (tableCategoryMasterFragment instanceof AddTableCategoryFragment && tableCategoryMasterFragment.isVisible()) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, new TableCategoryMasterFragment(), "HomeFragment");
            ft.commit();

        } else if (tableMasterFragment instanceof AddTableFragment && tableMasterFragment.isVisible()) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, new TableMasterFragment(), "HomeFragment");
            ft.commit();

        } else if (reportsFragment instanceof ItemReportFragment && reportsFragment.isVisible()) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, new ReportsFragment(), "HomeFragment");
            ft.commit();

        } else if (viewBillsFragment instanceof EditBillFragment && viewBillsFragment.isVisible()) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, new ViewBillsFragment(), "HomeFragment");
            ft.commit();

        }else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, new HomeFragment(), "Home");
            ft.commit();

        } else if (id == R.id.nav_parcel) {
            startActivity(new Intent(HomeActivity.this, ParcelMenuWithSearchActivity.class));
            finish();

        } else if (id == R.id.nav_edit_bill) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, new ViewBillsFragment(), "HomeFragment");
            ft.commit();

        } else if (id == R.id.nav_user) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, new UserMasterFragment(), "HomeFragment");
            ft.commit();

        } else if (id == R.id.nav_category) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, new CategoryMasterFragment(), "HomeFragment");
            ft.commit();

        } else if (id == R.id.nav_item) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, new ItemMasterFragment(), "HomeFragment");
            ft.commit();

        } else if (id == R.id.nav_table_category) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, new TableCategoryMasterFragment(), "HomeFragment");
            ft.commit();

        } else if (id == R.id.nav_table) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, new TableMasterFragment(), "HomeFragment");
            ft.commit();

        } else if (id == R.id.nav_report) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, new ReportsFragment(), "HomeFragment");
            ft.commit();

        } else if (id == R.id.nav_printer) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, new PrinterSettingsFragment(), "HomeFragment");
            ft.commit();

        } else if (id == R.id.nav_logout) {

            AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this, R.style.AlertDialogTheme);
            builder.setTitle("Logout");
            builder.setMessage("Are you sure you want to logout?");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    // updateUserToken(userId, "");
                    CustomSharedPreference.deletePreference(HomeActivity.this);

                    Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
            });
            builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return super.onRetainCustomNonConfigurationInstance();
    }

    @Nullable
    @Override
    public Object getLastNonConfigurationInstance() {
        return super.getLastNonConfigurationInstance();
    }

}
