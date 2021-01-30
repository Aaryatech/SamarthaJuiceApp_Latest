package com.ats.samarthajuice.fragment;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.print.PrintManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ats.samarthajuice.activity.GenerateBillActivity;
import com.ats.samarthajuice.activity.HomeActivity;
import com.ats.samarthajuice.adapter.DateWiseBillReportAdapter;
import com.ats.samarthajuice.BuildConfig;
import com.ats.samarthajuice.R;
import com.ats.samarthajuice.adapter.BillWiseReportAdapter;
import com.ats.samarthajuice.adapter.BillWiseTaxReportAdapter;
import com.ats.samarthajuice.adapter.CategoryWiseReportAdapter;
import com.ats.samarthajuice.adapter.DateWiseTaxReportAdapter;
import com.ats.samarthajuice.adapter.ItemCancelReportAdapter;
import com.ats.samarthajuice.adapter.ItemHSNReportAdapter;
import com.ats.samarthajuice.adapter.ItemWiseReportAdapter;
import com.ats.samarthajuice.adapter.MonthWiseBillReportAdapter;
import com.ats.samarthajuice.adapter.OrderCancelReportAdapter;
import com.ats.samarthajuice.adapter.TableReportAdapter;
import com.ats.samarthajuice.adapter.TaxLabWiseReportAdapter;
import com.ats.samarthajuice.constant.Constants;
import com.ats.samarthajuice.model.BillWiseReportModel;
import com.ats.samarthajuice.model.BillWiseTaxReportModel;
import com.ats.samarthajuice.model.CategoryWiseReportModel;
import com.ats.samarthajuice.model.DateWiseReportModel;
import com.ats.samarthajuice.model.ItemCancelReportModel;
import com.ats.samarthajuice.model.ItemHSNCodeReportModel;
import com.ats.samarthajuice.model.ItemReportModel;
import com.ats.samarthajuice.model.MonthWiseBillReportModel;
import com.ats.samarthajuice.model.OrderCancelReportModel;
import com.ats.samarthajuice.model.TableWiseReportModel;
import com.ats.samarthajuice.model.TaxLabWiseReportModel;
import com.ats.samarthajuice.model.TaxableDataForBillPrint;
import com.ats.samarthajuice.printer.PrintHelper;
import com.ats.samarthajuice.printer.PrintReceiptType;
import com.ats.samarthajuice.util.CommonDialog;
import com.ats.samarthajuice.util.CustomSharedPreference;
import com.ats.samarthajuice.util.ViewPrintAdapter;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.html.WebColors;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.PRINT_SERVICE;

public class ItemReportFragment extends Fragment {

    private RelativeLayout relativeLayout;
    private RecyclerView recyclerView;
    private LinearLayout llItemWise, llBillWise, llItemHSN, llDateWiseBill, llTableWise, llDateWiseBillTax, llTaxLab, llOrderCancel, llItemCancel, llMonthWise, llCatWise, llBillWiseTax;

    private ArrayList<ItemReportModel> itemReportList = new ArrayList<>();
    ItemWiseReportAdapter itemWiseAdapter;

    private ArrayList<CategoryWiseReportModel> catReportList = new ArrayList<>();
    CategoryWiseReportAdapter catWiseAdapter;

    private ArrayList<BillWiseReportModel> billReportList = new ArrayList<>();
    BillWiseReportAdapter billWiseAdapter;

    private ArrayList<ItemHSNCodeReportModel> itemHSNList = new ArrayList<>();
    ItemHSNReportAdapter hsnReportAdapter;

    private ArrayList<DateWiseReportModel> reportList = new ArrayList<>();
    DateWiseBillReportAdapter dateWiseBillAdapter;

    private ArrayList<MonthWiseBillReportModel> monthlyReportList = new ArrayList<>();
    MonthWiseBillReportAdapter monthlyReportAdapter;

    private ArrayList<TableWiseReportModel> tableReportList = new ArrayList<>();
    TableReportAdapter tableReportAdapter;

    private ArrayList<BillWiseTaxReportModel> billWiseTaxList = new ArrayList<>();
    BillWiseTaxReportAdapter billWiseTaxReportAdapter;
    DateWiseTaxReportAdapter dateWiseTaxReportAdapter;

    private ArrayList<TaxLabWiseReportModel> taxLabReportList = new ArrayList<>();
    TaxLabWiseReportAdapter taxLabWiseReportAdapter;

    private ArrayList<OrderCancelReportModel> orderCancelList = new ArrayList<>();
    OrderCancelReportAdapter orderCancelReportAdapter;

    private ArrayList<ItemCancelReportModel> itemCancelList = new ArrayList<>();
    ItemCancelReportAdapter itemCancelReportAdapter;

    int yyyy, mm, dd;
    long fromDateMillis, toDateMillis;

    String reportType = "";
    View v;

    String fromDate, toDate;


    //------PDF------
    private PdfPCell cell;
    private String path;
    private File dir;
    private File file;
    private TextInputLayout inputLayoutBillTo, inputLayoutEmailTo;
    double totalAmount = 0;
    int day, month, year;
    long dateInMillis;
    long amtLong;
    private Image bgImage;
    BaseColor myColor = WebColors.getRGBColor("#ffffff");
    BaseColor myColor1 = WebColors.getRGBColor("#cbccce");


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_report_filter, container, false);
        v = view;

        relativeLayout = view.findViewById(R.id.relativeLayout);
        recyclerView = view.findViewById(R.id.recyclerView);
        llBillWise = view.findViewById(R.id.llBillWise);
        llItemWise = view.findViewById(R.id.llItemWise);
        llItemHSN = view.findViewById(R.id.llItemHSN);
        llDateWiseBill = view.findViewById(R.id.llDateWiseBill);
        llTableWise = view.findViewById(R.id.llTableWise);
        llDateWiseBillTax = view.findViewById(R.id.llDateWiseBillTax);
        llTaxLab = view.findViewById(R.id.llTaxLab);
        llOrderCancel = view.findViewById(R.id.llOrderCancel);
        llItemCancel = view.findViewById(R.id.llItemCancel);
        llMonthWise = view.findViewById(R.id.llMonthWise);
        llCatWise = view.findViewById(R.id.llCategoryWise);
        llBillWiseTax = view.findViewById(R.id.llBillWiseTax);

        SimpleDateFormat sdf_From = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

        Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DATE, -1);
        String fDate = sdf_From.format(yesterday.getTimeInMillis());
        String tDate = sdf_From.format(Calendar.getInstance().getTimeInMillis());

        fromDate = sdf.format(yesterday.getTimeInMillis());
        toDate = sdf.format(Calendar.getInstance().getTimeInMillis());

        reportType = getArguments().getString("reportType");

        if (reportType.equalsIgnoreCase("ItemWise")) {
            getActivity().setTitle("Item Report");
            llItemWise.setVisibility(View.VISIBLE);
            getItemwiseReport(fDate, tDate);
        } else if (reportType.equalsIgnoreCase("CategoryWise")) {
            getActivity().setTitle("Category Report");
            llCatWise.setVisibility(View.VISIBLE);
            getCategorywiseReport(fDate, tDate);
        } else if (reportType.equalsIgnoreCase("BillWise")) {
            getActivity().setTitle("Bill Report");
            llBillWise.setVisibility(View.VISIBLE);
            getBillwiseReport(fDate, tDate);
        } else if (reportType.equalsIgnoreCase("ItemHSN")) {
            getActivity().setTitle("Item HSN Report");
            llItemHSN.setVisibility(View.VISIBLE);
            getItemHSNReport(fDate, tDate);
        } else if (reportType.equalsIgnoreCase("DateWiseBill")) {
            getActivity().setTitle("Date Wise Bill Report");
            llDateWiseBill.setVisibility(View.VISIBLE);
            getDateWiseBillReport(fDate, tDate);
        } else if (reportType.equalsIgnoreCase("MonthWiseBill")) {
            getActivity().setTitle("Month Wise Bill Report");
            llMonthWise.setVisibility(View.VISIBLE);
            getMonthWiseBillReport(fDate, tDate);
        } else if (reportType.equalsIgnoreCase("TableWise")) {
            getActivity().setTitle("Table Wise Report");
            llTableWise.setVisibility(View.VISIBLE);
            getTableWiseReport(fDate, tDate);
        } else if (reportType.equalsIgnoreCase("DateWiseBillTax")) {
            getActivity().setTitle("Date Wise Tax Report");
            llDateWiseBillTax.setVisibility(View.VISIBLE);
            getDateWiseBillTaxReport(fDate, tDate);
        } else if (reportType.equalsIgnoreCase("BillWiseBillTax")) {
            getActivity().setTitle("Bill Wise Tax Report");
            llBillWiseTax.setVisibility(View.VISIBLE);
            getBillWiseBillTaxReport(fDate, tDate);
        } else if (reportType.equalsIgnoreCase("TaxLab")) {
            getActivity().setTitle("Tax Lab Report");
            llTaxLab.setVisibility(View.VISIBLE);
            getTaxLabWiseReport(fDate, tDate);
        } else if (reportType.equalsIgnoreCase("OrderCancel")) {
            getActivity().setTitle("Order Cancellation Report");
            llOrderCancel.setVisibility(View.VISIBLE);
            getOrderCancelReport(fDate, tDate);
        } else if (reportType.equalsIgnoreCase("ItemCancel")) {
            getActivity().setTitle("Item Cancellation Report");
            llItemCancel.setVisibility(View.VISIBLE);
            getItemCancelReport(fDate, tDate);
        }

        path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/HotelFeastCo/Reports";
        dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        return view;
    }


    public void getItemwiseReport(String fromDate, String toDate) {

        if (Constants.isOnline(getContext())) {
            final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<ArrayList<ItemReportModel>> listCall = Constants.myInterface.getItemwiseReport(fromDate, toDate);
            listCall.enqueue(new Callback<ArrayList<ItemReportModel>>() {
                @Override
                public void onResponse(Call<ArrayList<ItemReportModel>> call, Response<ArrayList<ItemReportModel>> response) {
                    try {
                        if (response.body() != null) {

                            //Log.e("item report Data : ", "------------" + response.body());

                            ArrayList<ItemReportModel> data = response.body();
                            if (data == null) {
                                commonDialog.dismiss();
                                Toast.makeText(getContext(), "No report found!", Toast.LENGTH_SHORT).show();
                            } else {
                                itemReportList.clear();
                                itemReportList = data;

                                itemWiseAdapter = new ItemWiseReportAdapter(itemReportList, getContext());
                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                                recyclerView.setLayoutManager(mLayoutManager);
                                recyclerView.setItemAnimator(new DefaultItemAnimator());
                                recyclerView.setAdapter(itemWiseAdapter);

                                commonDialog.dismiss();
                            }
                        } else {
                            commonDialog.dismiss();
                            //Log.e("Data Null : ", "-----------");
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        //Log.e("Exception : ", "-----------" + e.getMessage());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<ItemReportModel>> call, Throwable t) {
                    commonDialog.dismiss();
                    //Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(getContext(), "No Internet Connection!", Toast.LENGTH_SHORT).show();
        }
    }

    public void getCategorywiseReport(String fromDate, String toDate) {

        if (Constants.isOnline(getContext())) {
            final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<ArrayList<CategoryWiseReportModel>> listCall = Constants.myInterface.getCategorywiseReport(fromDate, toDate);
            listCall.enqueue(new Callback<ArrayList<CategoryWiseReportModel>>() {
                @Override
                public void onResponse(Call<ArrayList<CategoryWiseReportModel>> call, Response<ArrayList<CategoryWiseReportModel>> response) {
                    try {
                        if (response.body() != null) {

                            //Log.e("item report Data : ", "------------" + response.body());

                            ArrayList<CategoryWiseReportModel> data = response.body();
                            if (data == null) {
                                commonDialog.dismiss();
                                Toast.makeText(getContext(), "No report found!", Toast.LENGTH_SHORT).show();
                            } else {
                                catReportList.clear();
                                catReportList = data;

                                catWiseAdapter = new CategoryWiseReportAdapter(catReportList, getContext());
                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                                recyclerView.setLayoutManager(mLayoutManager);
                                recyclerView.setItemAnimator(new DefaultItemAnimator());
                                recyclerView.setAdapter(catWiseAdapter);

                                commonDialog.dismiss();
                            }
                        } else {
                            commonDialog.dismiss();
                            //Log.e("Data Null : ", "-----------");
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        //Log.e("Exception : ", "-----------" + e.getMessage());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<CategoryWiseReportModel>> call, Throwable t) {
                    commonDialog.dismiss();
                    //Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(getContext(), "No Internet Connection!", Toast.LENGTH_SHORT).show();
        }
    }

    public void getBillwiseReport(String fromDate, String toDate) {

        if (Constants.isOnline(getContext())) {
            final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<ArrayList<BillWiseReportModel>> listCall = Constants.myInterface.getBillwiseReport(fromDate, toDate);
            listCall.enqueue(new Callback<ArrayList<BillWiseReportModel>>() {
                @Override
                public void onResponse(Call<ArrayList<BillWiseReportModel>> call, Response<ArrayList<BillWiseReportModel>> response) {
                    try {
                        if (response.body() != null) {

                            //Log.e("item report Data : ", "------------" + response.body());

                            ArrayList<BillWiseReportModel> data = response.body();
                            if (data == null) {
                                commonDialog.dismiss();
                                Toast.makeText(getContext(), "No report found!", Toast.LENGTH_SHORT).show();
                            } else {
                                billReportList.clear();
                                billReportList = data;

                                billWiseAdapter = new BillWiseReportAdapter(billReportList, getContext());
                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                                recyclerView.setLayoutManager(mLayoutManager);
                                recyclerView.setItemAnimator(new DefaultItemAnimator());
                                recyclerView.setAdapter(billWiseAdapter);

                                commonDialog.dismiss();
                            }
                        } else {
                            commonDialog.dismiss();
                            //Log.e("Data Null : ", "-----------");
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        //Log.e("Exception : ", "-----------" + e.getMessage());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<BillWiseReportModel>> call, Throwable t) {
                    commonDialog.dismiss();
                    //Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(getContext(), "No Internet Connection!", Toast.LENGTH_SHORT).show();
        }
    }

    public void getItemHSNReport(String fromDate, String toDate) {

        if (Constants.isOnline(getContext())) {
            final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<ArrayList<ItemHSNCodeReportModel>> listCall = Constants.myInterface.getItemHSNReport(fromDate, toDate);
            listCall.enqueue(new Callback<ArrayList<ItemHSNCodeReportModel>>() {
                @Override
                public void onResponse(Call<ArrayList<ItemHSNCodeReportModel>> call, Response<ArrayList<ItemHSNCodeReportModel>> response) {
                    try {
                        if (response.body() != null) {

                            //Log.e("item HSN Data : ", "------------" + response.body());

                            ArrayList<ItemHSNCodeReportModel> data = response.body();
                            if (data == null) {
                                commonDialog.dismiss();
                                Toast.makeText(getContext(), "No report found!", Toast.LENGTH_SHORT).show();
                            } else {
                                itemHSNList.clear();
                                itemHSNList = data;

                                hsnReportAdapter = new ItemHSNReportAdapter(itemHSNList, getContext());
                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                                recyclerView.setLayoutManager(mLayoutManager);
                                recyclerView.setItemAnimator(new DefaultItemAnimator());
                                recyclerView.setAdapter(hsnReportAdapter);

                                commonDialog.dismiss();
                            }
                        } else {
                            commonDialog.dismiss();
                            //Log.e("Data Null : ", "-----------");
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        //Log.e("Exception : ", "-----------" + e.getMessage());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<ItemHSNCodeReportModel>> call, Throwable t) {
                    commonDialog.dismiss();
                    //Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(getContext(), "No Internet Connection!", Toast.LENGTH_SHORT).show();
        }
    }

    public void getDateWiseBillReport(String fromDate, String toDate) {

        if (Constants.isOnline(getContext())) {
            final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<ArrayList<DateWiseReportModel>> listCall = Constants.myInterface.getDatewiseReport(fromDate, toDate);
            listCall.enqueue(new Callback<ArrayList<DateWiseReportModel>>() {
                @Override
                public void onResponse(Call<ArrayList<DateWiseReportModel>> call, Response<ArrayList<DateWiseReportModel>> response) {
                    try {
                        if (response.body() != null) {

                            //Log.e("bill Data : ", "------------" + response.body());

                            ArrayList<DateWiseReportModel> data = response.body();
                            if (data == null) {
                                commonDialog.dismiss();
                                Toast.makeText(getContext(), "No report found!", Toast.LENGTH_SHORT).show();
                            } else {
                                reportList.clear();
                                reportList = data;

                                dateWiseBillAdapter = new DateWiseBillReportAdapter(reportList, getContext());
                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                                recyclerView.setLayoutManager(mLayoutManager);
                                recyclerView.setItemAnimator(new DefaultItemAnimator());
                                recyclerView.setAdapter(dateWiseBillAdapter);

                                commonDialog.dismiss();
                            }
                        } else {
                            commonDialog.dismiss();
                            //Log.e("Data Null : ", "-----------");
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        //Log.e("Exception : ", "-----------" + e.getMessage());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<DateWiseReportModel>> call, Throwable t) {
                    commonDialog.dismiss();
                    //Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(getContext(), "No Internet Connection!", Toast.LENGTH_SHORT).show();
        }
    }

    public void getMonthWiseBillReport(String fromDate, String toDate) {

        if (Constants.isOnline(getContext())) {
            final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<ArrayList<MonthWiseBillReportModel>> listCall = Constants.myInterface.getMonthWiseBillReport(fromDate, toDate);
            listCall.enqueue(new Callback<ArrayList<MonthWiseBillReportModel>>() {
                @Override
                public void onResponse(Call<ArrayList<MonthWiseBillReportModel>> call, Response<ArrayList<MonthWiseBillReportModel>> response) {
                    try {
                        if (response.body() != null) {

                            //Log.e("bill Data : ", "------------" + response.body());

                            ArrayList<MonthWiseBillReportModel> data = response.body();
                            if (data == null) {
                                commonDialog.dismiss();
                                Toast.makeText(getContext(), "No report found!", Toast.LENGTH_SHORT).show();
                            } else {
                                monthlyReportList.clear();
                                monthlyReportList = data;

                                monthlyReportAdapter = new MonthWiseBillReportAdapter(monthlyReportList, getContext());
                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                                recyclerView.setLayoutManager(mLayoutManager);
                                recyclerView.setItemAnimator(new DefaultItemAnimator());
                                recyclerView.setAdapter(monthlyReportAdapter);

                                commonDialog.dismiss();
                            }
                        } else {
                            commonDialog.dismiss();
                            //Log.e("Data Null : ", "-----------");
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        //Log.e("Exception : ", "-----------" + e.getMessage());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<MonthWiseBillReportModel>> call, Throwable t) {
                    commonDialog.dismiss();
                    //Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(getContext(), "No Internet Connection!", Toast.LENGTH_SHORT).show();
        }
    }

    public void getTableWiseReport(String fromDate, String toDate) {

        if (Constants.isOnline(getContext())) {
            final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<ArrayList<TableWiseReportModel>> listCall = Constants.myInterface.getTableWiseReport(fromDate, toDate);
            listCall.enqueue(new Callback<ArrayList<TableWiseReportModel>>() {
                @Override
                public void onResponse(Call<ArrayList<TableWiseReportModel>> call, Response<ArrayList<TableWiseReportModel>> response) {
                    try {
                        if (response.body() != null) {

                            //Log.e("bill Data : ", "------------" + response.body());

                            ArrayList<TableWiseReportModel> data = response.body();
                            if (data == null) {
                                commonDialog.dismiss();
                                Toast.makeText(getContext(), "No report found!", Toast.LENGTH_SHORT).show();
                            } else {
                                tableReportList.clear();
                                tableReportList = data;

                                tableReportAdapter = new TableReportAdapter(tableReportList, getContext());
                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                                recyclerView.setLayoutManager(mLayoutManager);
                                recyclerView.setItemAnimator(new DefaultItemAnimator());
                                recyclerView.setAdapter(tableReportAdapter);

                                commonDialog.dismiss();
                            }
                        } else {
                            commonDialog.dismiss();
                            //Log.e("Data Null : ", "-----------");
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        //Log.e("Exception : ", "-----------" + e.getMessage());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<TableWiseReportModel>> call, Throwable t) {
                    commonDialog.dismiss();
                    //Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(getContext(), "No Internet Connection!", Toast.LENGTH_SHORT).show();
        }
    }

    public void getDateWiseBillTaxReport(String fromDate, String toDate) {

        if (Constants.isOnline(getContext())) {
            final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<ArrayList<BillWiseTaxReportModel>> listCall = Constants.myInterface.getDateWiseBillTaxReport(fromDate, toDate);
            listCall.enqueue(new Callback<ArrayList<BillWiseTaxReportModel>>() {
                @Override
                public void onResponse(Call<ArrayList<BillWiseTaxReportModel>> call, Response<ArrayList<BillWiseTaxReportModel>> response) {
                    try {
                        if (response.body() != null) {

                            //Log.e("bill Data : ", "------------" + response.body());

                            ArrayList<BillWiseTaxReportModel> data = response.body();
                            if (data == null) {
                                commonDialog.dismiss();
                                Toast.makeText(getContext(), "No report found!", Toast.LENGTH_SHORT).show();
                            } else {
                                billWiseTaxList.clear();
                                billWiseTaxList = data;

                                dateWiseTaxReportAdapter = new DateWiseTaxReportAdapter(billWiseTaxList, getContext());
                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                                recyclerView.setLayoutManager(mLayoutManager);
                                recyclerView.setItemAnimator(new DefaultItemAnimator());
                                recyclerView.setAdapter(dateWiseTaxReportAdapter);

                                commonDialog.dismiss();
                            }
                        } else {
                            commonDialog.dismiss();
                            //Log.e("Data Null : ", "-----------");
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        //Log.e("Exception : ", "-----------" + e.getMessage());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<BillWiseTaxReportModel>> call, Throwable t) {
                    commonDialog.dismiss();
                    //Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(getContext(), "No Internet Connection!", Toast.LENGTH_SHORT).show();
        }
    }

    public void getBillWiseBillTaxReport(String fromDate, String toDate) {

        if (Constants.isOnline(getContext())) {
            final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<ArrayList<BillWiseTaxReportModel>> listCall = Constants.myInterface.getDateWiseBillTaxReport(fromDate, toDate);
            listCall.enqueue(new Callback<ArrayList<BillWiseTaxReportModel>>() {
                @Override
                public void onResponse(Call<ArrayList<BillWiseTaxReportModel>> call, Response<ArrayList<BillWiseTaxReportModel>> response) {
                    try {
                        if (response.body() != null) {

                            //Log.e("bill Data : ", "------------" + response.body());

                            ArrayList<BillWiseTaxReportModel> data = response.body();
                            if (data == null) {
                                commonDialog.dismiss();
                                Toast.makeText(getContext(), "No report found!", Toast.LENGTH_SHORT).show();
                            } else {
                                billWiseTaxList.clear();
                                billWiseTaxList = data;

                                billWiseTaxReportAdapter = new BillWiseTaxReportAdapter(billWiseTaxList, getContext());
                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                                recyclerView.setLayoutManager(mLayoutManager);
                                recyclerView.setItemAnimator(new DefaultItemAnimator());
                                recyclerView.setAdapter(billWiseTaxReportAdapter);

                                commonDialog.dismiss();
                            }
                        } else {
                            commonDialog.dismiss();
                            //Log.e("Data Null : ", "-----------");
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        //Log.e("Exception : ", "-----------" + e.getMessage());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<BillWiseTaxReportModel>> call, Throwable t) {
                    commonDialog.dismiss();
                    //Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(getContext(), "No Internet Connection!", Toast.LENGTH_SHORT).show();
        }
    }

    public void getTaxLabWiseReport(String fromDate, String toDate) {

        if (Constants.isOnline(getContext())) {
            final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<ArrayList<TaxLabWiseReportModel>> listCall = Constants.myInterface.getTaxLabWiseReport(fromDate, toDate);
            listCall.enqueue(new Callback<ArrayList<TaxLabWiseReportModel>>() {
                @Override
                public void onResponse(Call<ArrayList<TaxLabWiseReportModel>> call, Response<ArrayList<TaxLabWiseReportModel>> response) {
                    try {
                        if (response.body() != null) {

                            //Log.e("bill Data : ", "------------" + response.body());

                            ArrayList<TaxLabWiseReportModel> data = response.body();
                            if (data == null) {
                                commonDialog.dismiss();
                                Toast.makeText(getContext(), "No report found!", Toast.LENGTH_SHORT).show();
                            } else {
                                taxLabReportList.clear();
                                taxLabReportList = data;

                                taxLabWiseReportAdapter = new TaxLabWiseReportAdapter(taxLabReportList, getContext());
                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                                recyclerView.setLayoutManager(mLayoutManager);
                                recyclerView.setItemAnimator(new DefaultItemAnimator());
                                recyclerView.setAdapter(taxLabWiseReportAdapter);

                                commonDialog.dismiss();
                            }
                        } else {
                            commonDialog.dismiss();
                            //Log.e("Data Null : ", "-----------");
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        //Log.e("Exception : ", "-----------" + e.getMessage());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<TaxLabWiseReportModel>> call, Throwable t) {
                    commonDialog.dismiss();
                    //Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(getContext(), "No Internet Connection!", Toast.LENGTH_SHORT).show();
        }
    }

    public void getOrderCancelReport(String fromDate, String toDate) {

        if (Constants.isOnline(getContext())) {
            final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<ArrayList<OrderCancelReportModel>> listCall = Constants.myInterface.getOrderCancelReport(fromDate, toDate);
            listCall.enqueue(new Callback<ArrayList<OrderCancelReportModel>>() {
                @Override
                public void onResponse(Call<ArrayList<OrderCancelReportModel>> call, Response<ArrayList<OrderCancelReportModel>> response) {
                    try {
                        if (response.body() != null) {

                            //Log.e("bill Data : ", "------------" + response.body());

                            ArrayList<OrderCancelReportModel> data = response.body();
                            if (data == null) {
                                commonDialog.dismiss();
                                Toast.makeText(getContext(), "No report found!", Toast.LENGTH_SHORT).show();
                            } else {
                                orderCancelList.clear();
                                orderCancelList = data;

                                orderCancelReportAdapter = new OrderCancelReportAdapter(orderCancelList, getContext());
                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                                recyclerView.setLayoutManager(mLayoutManager);
                                recyclerView.setItemAnimator(new DefaultItemAnimator());
                                recyclerView.setAdapter(orderCancelReportAdapter);

                                commonDialog.dismiss();
                            }
                        } else {
                            commonDialog.dismiss();
                            //Log.e("Data Null : ", "-----------");
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        //Log.e("Exception : ", "-----------" + e.getMessage());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<OrderCancelReportModel>> call, Throwable t) {
                    commonDialog.dismiss();
                    //Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(getContext(), "No Internet Connection!", Toast.LENGTH_SHORT).show();
        }
    }

    public void getItemCancelReport(String fromDate, String toDate) {

        if (Constants.isOnline(getContext())) {
            final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<ArrayList<ItemCancelReportModel>> listCall = Constants.myInterface.getItemCancelReport(fromDate, toDate);
            listCall.enqueue(new Callback<ArrayList<ItemCancelReportModel>>() {
                @Override
                public void onResponse(Call<ArrayList<ItemCancelReportModel>> call, Response<ArrayList<ItemCancelReportModel>> response) {
                    try {
                        if (response.body() != null) {

                            //Log.e("bill Data : ", "------------" + response.body());

                            ArrayList<ItemCancelReportModel> data = response.body();
                            if (data == null) {
                                commonDialog.dismiss();
                                Toast.makeText(getContext(), "No report found!", Toast.LENGTH_SHORT).show();
                            } else {
                                itemCancelList.clear();
                                itemCancelList = data;

                                itemCancelReportAdapter = new ItemCancelReportAdapter(itemCancelList, getContext());
                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                                recyclerView.setLayoutManager(mLayoutManager);
                                recyclerView.setItemAnimator(new DefaultItemAnimator());
                                recyclerView.setAdapter(itemCancelReportAdapter);

                                commonDialog.dismiss();
                            }
                        } else {
                            commonDialog.dismiss();
                            //Log.e("Data Null : ", "-----------");
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        //Log.e("Exception : ", "-----------" + e.getMessage());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<ItemCancelReportModel>> call, Throwable t) {
                    commonDialog.dismiss();
                    //Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(getContext(), "No Internet Connection!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem item = menu.findItem(R.id.action_filter);
        MenuItem itemPrint = menu.findItem(R.id.action_print);
        MenuItem itemDownload = menu.findItem(R.id.action_download);
        item.setVisible(true);
        itemDownload.setVisible(true);

        if (reportType.equalsIgnoreCase("DateWiseBillTax")){
            itemPrint.setVisible(true);
        }else{
            itemPrint.setVisible(false);
        }

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        View menuItemView = getView().findViewById(R.id.action_download);
        switch (item.getItemId()) {
            case R.id.action_filter:
                new showDateDialog(getContext()).show();
                return true;

            case R.id.action_print:

                try{

                    SimpleDateFormat sdf=new SimpleDateFormat("dd-MM-yyyy");
                    SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-MM-dd");

                    Date fromD=sdf.parse(fromDate);
                    Date toD=sdf.parse(toDate);

                    String frDate=sdf1.format(fromD.getTime());
                    String tDate=sdf1.format(toD.getTime());

                    getTaxDataForPrint(frDate,tDate);

                }catch (Exception e){e.printStackTrace();}



                return true;

            case R.id.action_download:
                downloadDialog();

                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }


    public class showDateDialog extends Dialog {

        EditText edFromDate, edToDate;
        TextView tvFromDate, tvToDate;

        public showDateDialog(@NonNull Context context) {
            super(context);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            //requestWindowFeature(Window.FEATURE_NO_TITLE);
            setTitle("Filter");
            setContentView(R.layout.dialog_item_report_filter);

            edFromDate = findViewById(R.id.edDatePicker_FromDate);
            edToDate = findViewById(R.id.edDatePicker_ToDate);
            TextView tvDialogSearch = findViewById(R.id.tvDatePicker_Search);
            TextView tvDialogCancel = findViewById(R.id.tvDatePicker_Cancel);
            tvFromDate = findViewById(R.id.tvDatePicker_FromDate);
            tvToDate = findViewById(R.id.tvDatePicker_ToDate);

            edFromDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int yr, mn, dy;
                    if (fromDateMillis > 0) {
                        Calendar purchaseCal = Calendar.getInstance();
                        purchaseCal.setTimeInMillis(fromDateMillis);
                        yr = purchaseCal.get(Calendar.YEAR);
                        mn = purchaseCal.get(Calendar.MONTH);
                        dy = purchaseCal.get(Calendar.DAY_OF_MONTH);
                    } else {
                        Calendar purchaseCal = Calendar.getInstance();
                        yr = purchaseCal.get(Calendar.YEAR);
                        mn = purchaseCal.get(Calendar.MONTH);
                        dy = purchaseCal.get(Calendar.DAY_OF_MONTH);
                    }
                    DatePickerDialog dialog = new DatePickerDialog(getActivity(), fromDateListener, yr, mn, dy);
                    dialog.show();
                }
            });

            edToDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int yr, mn, dy;
                    if (toDateMillis > 0) {
                        Calendar purchaseCal = Calendar.getInstance();
                        purchaseCal.setTimeInMillis(toDateMillis);
                        yr = purchaseCal.get(Calendar.YEAR);
                        mn = purchaseCal.get(Calendar.MONTH);
                        dy = purchaseCal.get(Calendar.DAY_OF_MONTH);
                    } else {
                        Calendar purchaseCal = Calendar.getInstance();
                        yr = purchaseCal.get(Calendar.YEAR);
                        mn = purchaseCal.get(Calendar.MONTH);
                        dy = purchaseCal.get(Calendar.DAY_OF_MONTH);
                    }
                    DatePickerDialog dialog = new DatePickerDialog(getActivity(), toDateListener, yr, mn, dy);
                    dialog.show();
                }
            });


            tvDialogSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (edFromDate.getText().toString().isEmpty()) {
                        edFromDate.setError("Select From Date");
                        edFromDate.requestFocus();
                    } else if (edToDate.getText().toString().isEmpty()) {
                        edToDate.setError("Select To Date");
                        edToDate.requestFocus();
                    } else {
                        dismiss();

                        String fromDate = tvFromDate.getText().toString();
                        String toDate = tvToDate.getText().toString();

                        //getItemwiseReport(fromDate, toDate);
                        if (reportType.equalsIgnoreCase("ItemWise")) {
                            llItemWise.setVisibility(View.VISIBLE);
                            getItemwiseReport(fromDate, toDate);
                        } else if (reportType.equalsIgnoreCase("CategoryWise")) {
                            llCatWise.setVisibility(View.VISIBLE);
                            getCategorywiseReport(fromDate, toDate);
                        } else if (reportType.equalsIgnoreCase("BillWise")) {
                            llBillWise.setVisibility(View.VISIBLE);
                            getBillwiseReport(fromDate, toDate);
                        } else if (reportType.equalsIgnoreCase("ItemHSN")) {
                            llItemHSN.setVisibility(View.VISIBLE);
                            getItemHSNReport(fromDate, toDate);
                        } else if (reportType.equalsIgnoreCase("DateWiseBill")) {
                            llDateWiseBill.setVisibility(View.VISIBLE);
                            getDateWiseBillReport(fromDate, toDate);
                        } else if (reportType.equalsIgnoreCase("MonthWiseBill")) {
                            llMonthWise.setVisibility(View.VISIBLE);
                            getMonthWiseBillReport(fromDate, toDate);
                        } else if (reportType.equalsIgnoreCase("TableWise")) {
                            llTableWise.setVisibility(View.VISIBLE);
                            getTableWiseReport(fromDate, toDate);
                        } else if (reportType.equalsIgnoreCase("DateWiseBillTax")) {
                            llDateWiseBillTax.setVisibility(View.VISIBLE);
                            getDateWiseBillTaxReport(fromDate, toDate);
                        } else if (reportType.equalsIgnoreCase("BillWiseBillTax")) {
                            llBillWiseTax.setVisibility(View.VISIBLE);
                            getBillWiseBillTaxReport(fromDate, toDate);
                        } else if (reportType.equalsIgnoreCase("TaxLab")) {
                            llTaxLab.setVisibility(View.VISIBLE);
                            getTaxLabWiseReport(fromDate, toDate);
                        } else if (reportType.equalsIgnoreCase("OrderCancel")) {
                            llOrderCancel.setVisibility(View.VISIBLE);
                            getOrderCancelReport(fromDate, toDate);
                        } else if (reportType.equalsIgnoreCase("ItemCancel")) {
                            llItemCancel.setVisibility(View.VISIBLE);
                            getItemCancelReport(fromDate, toDate);
                        }


                    }
                }
            });

            tvDialogCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });

        }

        DatePickerDialog.OnDateSetListener fromDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                yyyy = year;
                mm = month + 1;
                dd = dayOfMonth;
                edFromDate.setText(dd + "-" + mm + "-" + yyyy);
                tvFromDate.setText(yyyy + "-" + mm + "-" + dd);
                fromDate = dd + "-" + mm + "-" + yyyy;

                Calendar calendar = Calendar.getInstance();
                calendar.set(yyyy, mm - 1, dd);
                calendar.set(Calendar.MILLISECOND, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.HOUR, 0);
                fromDateMillis = calendar.getTimeInMillis();
            }
        };

        DatePickerDialog.OnDateSetListener toDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                yyyy = year;
                mm = month + 1;
                dd = dayOfMonth;
                edToDate.setText(dd + "-" + mm + "-" + yyyy);
                tvToDate.setText(yyyy + "-" + mm + "-" + dd);
                toDate = dd + "-" + mm + "-" + yyyy;

                Calendar calendar = Calendar.getInstance();
                calendar.set(yyyy, mm - 1, dd);
                calendar.set(Calendar.MILLISECOND, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.HOUR, 0);
                toDateMillis = calendar.getTimeInMillis();
            }
        };
    }

    public void printPDF(View view) {
        PrintManager printManager = (PrintManager) getActivity().getSystemService(PRINT_SERVICE);
        printManager.print("print_any_view_job_name", new ViewPrintAdapter(getContext(),
                view), null);
    }

    public void downloadDialog() {

        final Dialog openDialog = new Dialog(getActivity());
        openDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        openDialog.setContentView(R.layout.dialog_download);
        openDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        Window window = openDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.TOP | Gravity.RIGHT;
        wlp.x = 50;
        wlp.y = 100;
        // wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        wlp.width = 350;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);

        TextView tvPdf = openDialog.findViewById(R.id.tvPdf);
        TextView tvExcel = openDialog.findViewById(R.id.tvExcel);

        tvPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (reportType.equalsIgnoreCase("ItemWise")) {
                    createItemWiseReportPDF(fromDate, toDate, itemReportList);
                } else if (reportType.equalsIgnoreCase("CategoryWise")) {
                    createCategoryWiseReportPDF(fromDate, toDate, catReportList);
                } else if (reportType.equalsIgnoreCase("BillWise")) {
                    createBillWiseReportPDF(fromDate, toDate, billReportList);
                } else if (reportType.equalsIgnoreCase("ItemHSN")) {
                    createItemWiseHSNCodeReportPDF(fromDate, toDate, itemHSNList);
                } else if (reportType.equalsIgnoreCase("DateWiseBill")) {
                    createDateWiseBillReportPDF(fromDate, toDate, reportList);
                } else if (reportType.equalsIgnoreCase("MonthWiseBill")) {
                    createMonthWiseBillReportPDF("", monthlyReportList);
                } else if (reportType.equalsIgnoreCase("TableWise")) {
                    createTableWiseReportPDF(fromDate, toDate, tableReportList);
                } else if (reportType.equalsIgnoreCase("DateWiseBillTax")) {
                    createDateWiseTaxReportPDF(fromDate, toDate, billWiseTaxList);
                } else if (reportType.equalsIgnoreCase("BillWiseBillTax")) {
                    createBillWiseTaxReportPDF(fromDate, toDate, billWiseTaxList);
                } else if (reportType.equalsIgnoreCase("TaxLab")) {
                    createTaxLabReportPDF(fromDate, toDate, taxLabReportList);
                } else if (reportType.equalsIgnoreCase("OrderCancel")) {
                    createOrderCancelReportPDF(fromDate, toDate, orderCancelList);
                } else if (reportType.equalsIgnoreCase("ItemCancel")) {
                    createItemCancelReportPDF(fromDate, toDate, itemCancelList);
                }

                openDialog.dismiss();
            }
        });

        tvExcel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (reportType.equalsIgnoreCase("ItemWise")) {
                    createItemWiseReportExcel(fromDate, toDate, itemReportList);
                } else if (reportType.equalsIgnoreCase("CategoryWise")) {
                    createCategoryWiseReportExcel(fromDate, toDate, catReportList);
                } else if (reportType.equalsIgnoreCase("BillWise")) {
                    createBillWiseReportExcel(fromDate, toDate, billReportList);
                } else if (reportType.equalsIgnoreCase("ItemHSN")) {
                    createItemHSNReportExcel(fromDate, toDate, itemHSNList);
                } else if (reportType.equalsIgnoreCase("DateWiseBill")) {
                    createDateWiseBillReportExcel(fromDate, toDate, reportList);
                } else if (reportType.equalsIgnoreCase("MonthWiseBill")) {
                    createMonthWiseBillReportExcel(fromDate, monthlyReportList);
                } else if (reportType.equalsIgnoreCase("TableWise")) {
                    createTableReportExcel(fromDate, toDate, tableReportList);
                } else if (reportType.equalsIgnoreCase("DateWiseBillTax")) {
                    createDateWiseTaxReportExcel(fromDate, toDate, billWiseTaxList);
                } else if (reportType.equalsIgnoreCase("BillWiseBillTax")) {
                    createBillWiseTaxReportExcel(fromDate, toDate, billWiseTaxList);
                } else if (reportType.equalsIgnoreCase("TaxLab")) {
                    createTaxLabReportExcel(fromDate, toDate, taxLabReportList);
                } else if (reportType.equalsIgnoreCase("OrderCancel")) {
                    createOrderCancelReportExcel(fromDate, toDate, orderCancelList);
                } else if (reportType.equalsIgnoreCase("ItemCancel")) {
                    createItemCancelReportExcel(fromDate, toDate, itemCancelList);
                }

                openDialog.dismiss();
            }
        });


        openDialog.show();

    }

    public void createItemWiseReportPDF(String fromDate, String toDate, ArrayList<ItemReportModel> itemReportList) {

        final CommonDialog commonDialog = new CommonDialog(getActivity(), "Loading", "Please Wait...");
        commonDialog.show();

        String resultTo = fromDate;

        Document doc = new Document();

        doc.setMargins(-16, -17, 40, 40);
        Font boldFont = new Font(Font.FontFamily.TIMES_ROMAN, 13, Font.BOLD);
        Font boldTotalFont = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD);
        Font boldTextFont = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD);
        Font textFont = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL);
        try {

            Calendar calendar = Calendar.getInstance();
            day = calendar.get(Calendar.DAY_OF_MONTH);
            month = calendar.get(Calendar.MONTH) + 1;
            year = calendar.get(Calendar.YEAR);
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minutes = calendar.get(Calendar.MINUTE);
            dateInMillis = calendar.getTimeInMillis();

            String fileName = "Item_Wise_Report_" + fromDate + "_to_" + toDate + ".pdf";
            file = new File(dir, fileName);
            // file = new File(dir, resultTo + "_Bill_" + day + "-" + month + "-" + year + "_" + hour + ":" + minutes + ".pdf");
            // file = new File(dir, "Bill.pdf");
            FileOutputStream fOut = new FileOutputStream(file);
            PdfWriter writer = PdfWriter.getInstance(doc, fOut);

            Log.d("File Name-------------", "" + file.getName());
            //open the document
            doc.open();

            PdfPTable ptHead = new PdfPTable(1);
            ptHead.setWidthPercentage(100);
            cell = new PdfPCell(new Paragraph("Bill", boldFont));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(1);

            //create table
            PdfPTable pt = new PdfPTable(1);
            pt.setWidthPercentage(100);

            cell = new PdfPCell();
            cell.setBorder(Rectangle.NO_BORDER);

            //set drawable in cell
            Drawable myImage = getActivity().getResources().getDrawable(R.drawable.ic_camera);
            Bitmap bitmap = ((BitmapDrawable) myImage).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);

            try {
                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                pt.addCell(cell);

                cell = new PdfPCell(new Paragraph("Happy Feast Co.", boldFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(1);
                pt.addCell(cell);

                cell = new PdfPCell(new Paragraph("Report : Item Wise Report", boldFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(1);
                pt.addCell(cell);

                PdfPTable dateTable = new PdfPTable(2);
                dateTable.setWidthPercentage(100);
                cell = new PdfPCell(new Paragraph("From Date : " + fromDate, boldFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                dateTable.addCell(cell);

                cell = new PdfPCell(new Paragraph("To Date : " + toDate, boldFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(2);
                dateTable.addCell(cell);


                PdfPTable pTable = new PdfPTable(1);
                pTable.setWidthPercentage(100);

                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(1);
                cell.addElement(pt);
                pTable.addCell(cell);

                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(1);
                cell.addElement(dateTable);
                pTable.addCell(cell);


                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(1);
                cell.addElement(ptHead);
                pTable.addCell(cell);

                PdfPTable table = new PdfPTable(6);
                float[] columnWidth = new float[]{10, 50, 30, 30, 30, 30};
                table.setWidths(columnWidth);
                table.setTotalWidth(columnWidth);

                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setBackgroundColor(myColor);
                cell.setColspan(6);
                cell.addElement(pTable);

                table.addCell(cell);//image cell&address

                cell = new PdfPCell(new Phrase("NO.", boldTextFont));
                cell.setHorizontalAlignment(1);
                cell.setBackgroundColor(myColor1);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("ITEM", boldTextFont));
                cell.setBackgroundColor(myColor1);
                cell.setHorizontalAlignment(1);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("QUANTITY", boldTextFont));
                cell.setHorizontalAlignment(1);
                cell.setBackgroundColor(myColor1);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("UNIT PRICE", boldTextFont));
                cell.setHorizontalAlignment(1);
                cell.setBackgroundColor(myColor1);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("TOTAL", boldTextFont));
                cell.setHorizontalAlignment(1);
                cell.setBackgroundColor(myColor1);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("PAYABLE AMOUNT", boldTextFont));
                cell.setBackgroundColor(myColor1);
                cell.setHorizontalAlignment(1);
                table.addCell(cell);

                float total = 0;
                for (int i = 0; i < itemReportList.size(); i++) {

                    table.addCell("" + (i + 1));

                    table.addCell("" + itemReportList.get(i).getItemName());

                    int qty = (int) itemReportList.get(i).getQuantity();

                    cell = new PdfPCell(new Phrase("" + qty));
                    cell.setHorizontalAlignment(1);
                    cell.setBackgroundColor(myColor);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase("" + itemReportList.get(i).getRate()));
                    cell.setHorizontalAlignment(2);
                    cell.setBackgroundColor(myColor);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase("" + (itemReportList.get(i).getTotal())));
                    cell.setHorizontalAlignment(2);
                    cell.setBackgroundColor(myColor);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase("" + (itemReportList.get(i).getPayableAmt())));
                    cell.setHorizontalAlignment(2);
                    cell.setBackgroundColor(myColor);
                    table.addCell(cell);

                    total = total + (itemReportList.get(i).getPayableAmt());
                }

                //----BLANK ROW
                cell = new PdfPCell(new Phrase(""));
                cell.setBackgroundColor(myColor);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(""));
                cell.setBackgroundColor(myColor);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(""));
                cell.setBackgroundColor(myColor);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(" "));
                cell.setBackgroundColor(myColor);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(" "));
                cell.setBackgroundColor(myColor);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(" "));
                cell.setBackgroundColor(myColor);
                table.addCell(cell);

                //-------------NEW TABLE--------------------------

                PdfPTable table2 = new PdfPTable(3);
                float[] columnWidth2 = new float[]{60, 60, 60};
                table2.setWidths(columnWidth2);

                cell = new PdfPCell(new Phrase(""));
                cell.setBackgroundColor(myColor);
                table2.addCell(cell);


                cell = new PdfPCell(new Phrase("  TOTAL", boldTotalFont));
                cell.setBorder(Rectangle.LEFT | Rectangle.BOTTOM | Rectangle.TOP);
                cell.setBackgroundColor(myColor);
                table2.addCell(cell);

                cell = new PdfPCell(new Phrase("" + total, boldTotalFont));
                cell.setHorizontalAlignment(2);
                cell.setBorder(Rectangle.RIGHT | Rectangle.BOTTOM | Rectangle.TOP);
                cell.setBackgroundColor(myColor);
                table2.addCell(cell);

                doc.add(table);
                doc.add(table2);

            } catch (DocumentException de) {
                commonDialog.dismiss();
                ////Log.e("PDFCreator", "DocumentException:" + de);
                Toast.makeText(getActivity(), "Unable To Generate Bill", Toast.LENGTH_SHORT).show();
            } finally {
                doc.close();
                commonDialog.dismiss();

                File file1 = new File(dir, fileName);

                if (file1.exists()) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);

                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                        intent.setDataAndType(Uri.fromFile(file1), "application/pdf");
                    } else {
                        if (file1.exists()) {
                            String authorities = BuildConfig.APPLICATION_ID + ".provider";
                            Uri uri = FileProvider.getUriForFile(getActivity(), authorities, file1);
                            intent.setDataAndType(uri, "application/pdf");
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        }
                    }
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    getActivity().startActivity(intent);

                } else {
                    commonDialog.dismiss();
                    Toast.makeText(getActivity(), "Unable To Generate Bill", Toast.LENGTH_SHORT).show();
                }

            }
        } catch (Exception e) {
            commonDialog.dismiss();
            e.printStackTrace();
            Toast.makeText(getActivity(), "Unable To Generate Bill", Toast.LENGTH_SHORT).show();
        }
    }

    public void createCategoryWiseReportPDF(String fromDate, String toDate, ArrayList<CategoryWiseReportModel> reportList) {

        final CommonDialog commonDialog = new CommonDialog(getActivity(), "Loading", "Please Wait...");
        commonDialog.show();

        String resultTo = fromDate;

        Document doc = new Document();
        doc.setMargins(-16, -17, 40, 40);
        Font boldFont = new Font(Font.FontFamily.TIMES_ROMAN, 13, Font.BOLD);
        Font boldTotalFont = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD);
        Font boldTextFont = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD);
        Font textFont = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL);
        try {

            Calendar calendar = Calendar.getInstance();
            day = calendar.get(Calendar.DAY_OF_MONTH);
            month = calendar.get(Calendar.MONTH) + 1;
            year = calendar.get(Calendar.YEAR);
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minutes = calendar.get(Calendar.MINUTE);
            dateInMillis = calendar.getTimeInMillis();

            String fileName = "Category_Wise_Report_" + fromDate + "_to_" + toDate + ".pdf";
            file = new File(dir, fileName);
            // file = new File(dir, resultTo + "_Bill_" + day + "-" + month + "-" + year + "_" + hour + ":" + minutes + ".pdf");
            // file = new File(dir, "Bill.pdf");
            FileOutputStream fOut = new FileOutputStream(file);
            PdfWriter writer = PdfWriter.getInstance(doc, fOut);

            Log.d("File Name-------------", "" + file.getName());
            //open the document
            doc.open();

            PdfPTable ptHead = new PdfPTable(1);
            ptHead.setWidthPercentage(100);
            cell = new PdfPCell(new Paragraph("Bill", boldFont));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(1);

            PdfPTable ptHeadBlank1 = new PdfPTable(1);
            ptHeadBlank1.setWidthPercentage(100);
            cell = new PdfPCell(new Paragraph(" "));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(1);
            ptHeadBlank1.addCell(cell);

            PdfPTable ptHeadBlank2 = new PdfPTable(1);
            ptHeadBlank2.setWidthPercentage(100);
            cell = new PdfPCell(new Paragraph(" "));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(1);
            ptHeadBlank2.addCell(cell);

            //create table
            PdfPTable pt = new PdfPTable(1);
            pt.setWidthPercentage(100);

            cell = new PdfPCell();
            cell.setBorder(Rectangle.NO_BORDER);

            //set drawable in cell
            Drawable myImage = getActivity().getResources().getDrawable(R.drawable.ic_camera);
            Bitmap bitmap = ((BitmapDrawable) myImage).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);

            try {
                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                pt.addCell(cell);

                cell = new PdfPCell(new Paragraph("Happy Feast Co.", boldFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(1);
                pt.addCell(cell);

                cell = new PdfPCell(new Paragraph("Report : Category Wise Report", boldFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(1);
                pt.addCell(cell);

                PdfPTable dateTable = new PdfPTable(2);
                dateTable.setWidthPercentage(100);
                cell = new PdfPCell(new Paragraph("From Date : " + fromDate, boldFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                dateTable.addCell(cell);

                cell = new PdfPCell(new Paragraph("To Date : " + toDate, boldFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(2);
                dateTable.addCell(cell);

                PdfPTable pTable = new PdfPTable(1);
                pTable.setWidthPercentage(100);

                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(1);
                cell.addElement(pt);
                pTable.addCell(cell);

                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(1);
                cell.addElement(dateTable);
                pTable.addCell(cell);

                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(1);
                cell.addElement(ptHead);
                pTable.addCell(cell);

                PdfPTable table = new PdfPTable(5);
                float[] columnWidth = new float[]{10, 60, 30, 40, 40};
                table.setWidths(columnWidth);
                table.setTotalWidth(columnWidth);

                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setBackgroundColor(myColor);
                cell.setColspan(5);
                cell.addElement(pTable);

                table.addCell(cell);//image cell&address

                cell = new PdfPCell(new Phrase("NO.", boldTextFont));
                cell.setHorizontalAlignment(1);
                cell.setBackgroundColor(myColor1);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("CATEGORY NAME", boldTextFont));
                cell.setBackgroundColor(myColor1);
                cell.setHorizontalAlignment(1);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("QUANTITY", boldTextFont));
                cell.setHorizontalAlignment(1);
                cell.setBackgroundColor(myColor1);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("TOTAL", boldTextFont));
                cell.setHorizontalAlignment(1);
                cell.setBackgroundColor(myColor1);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("PAYABLE AMOUNT", boldTextFont));
                cell.setBackgroundColor(myColor1);
                cell.setHorizontalAlignment(1);
                table.addCell(cell);

                float total = 0;
                for (int i = 0; i < reportList.size(); i++) {

                    table.addCell("" + (i + 1));

                    table.addCell("" + reportList.get(i).getCatName());

                    int qty = (int) reportList.get(i).getQuantity();

                    cell = new PdfPCell(new Phrase("" + qty));
                    cell.setHorizontalAlignment(1);
                    cell.setBackgroundColor(myColor);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase("" + reportList.get(i).getTotal()));
                    cell.setHorizontalAlignment(2);
                    cell.setBackgroundColor(myColor);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase("" + reportList.get(i).getPayableAmt()));
                    cell.setHorizontalAlignment(2);
                    cell.setBackgroundColor(myColor);
                    table.addCell(cell);

                    total = total + (reportList.get(i).getPayableAmt());
                }

                //----BLANK ROW
                cell = new PdfPCell(new Phrase(""));
                cell.setBackgroundColor(myColor);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(""));
                cell.setBackgroundColor(myColor);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(""));
                cell.setBackgroundColor(myColor);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(" "));
                cell.setBackgroundColor(myColor);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(" "));
                cell.setBackgroundColor(myColor);
                table.addCell(cell);

                //-------------NEW TABLE--------------------------

                PdfPTable table2 = new PdfPTable(3);
                float[] columnWidth2 = new float[]{70, 50, 60};
                table2.setWidths(columnWidth2);

                cell = new PdfPCell(new Phrase(""));
                cell.setBackgroundColor(myColor);
                table2.addCell(cell);

                cell = new PdfPCell(new Phrase("  TOTAL", boldTotalFont));
                cell.setBorder(Rectangle.LEFT | Rectangle.BOTTOM | Rectangle.TOP);
                cell.setBackgroundColor(myColor);
                table2.addCell(cell);

                cell = new PdfPCell(new Phrase("" + total, boldTotalFont));
                cell.setHorizontalAlignment(2);
                cell.setBorder(Rectangle.RIGHT | Rectangle.BOTTOM | Rectangle.TOP);
                cell.setBackgroundColor(myColor);
                table2.addCell(cell);

                doc.add(table);
                doc.add(table2);

            } catch (DocumentException de) {
                commonDialog.dismiss();
                ////Log.e("PDFCreator", "DocumentException:" + de);
                Toast.makeText(getActivity(), "Unable To Generate Bill", Toast.LENGTH_SHORT).show();
            } finally {
                doc.close();
                commonDialog.dismiss();

                File file1 = new File(dir, fileName);

                if (file1.exists()) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);

                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                        intent.setDataAndType(Uri.fromFile(file1), "application/pdf");
                    } else {
                        if (file1.exists()) {
                            String authorities = BuildConfig.APPLICATION_ID + ".provider";
                            Uri uri = FileProvider.getUriForFile(getActivity(), authorities, file1);
                            intent.setDataAndType(uri, "application/pdf");
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        }
                    }
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    getActivity().startActivity(intent);

                } else {
                    commonDialog.dismiss();
                    Toast.makeText(getActivity(), "Unable To Generate Bill", Toast.LENGTH_SHORT).show();
                }

            }
        } catch (Exception e) {
            commonDialog.dismiss();
            e.printStackTrace();
            Toast.makeText(getActivity(), "Unable To Generate Bill", Toast.LENGTH_SHORT).show();
        }
    }

    public void createItemWiseHSNCodeReportPDF(String fromDate, String toDate, ArrayList<ItemHSNCodeReportModel> reportList) {

        final CommonDialog commonDialog = new CommonDialog(getActivity(), "Loading", "Please Wait...");
        commonDialog.show();

        String resultTo = fromDate;

        Document doc = new Document();
        doc.setMargins(-16, -17, 40, 40);
        Font boldFont = new Font(Font.FontFamily.TIMES_ROMAN, 13, Font.BOLD);
        Font boldTotalFont = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD);
        Font boldTextFont = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD);
        Font textFont = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL);
        try {

            Calendar calendar = Calendar.getInstance();
            day = calendar.get(Calendar.DAY_OF_MONTH);
            month = calendar.get(Calendar.MONTH) + 1;
            year = calendar.get(Calendar.YEAR);
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minutes = calendar.get(Calendar.MINUTE);
            dateInMillis = calendar.getTimeInMillis();

            String fileName = "Item_HSN_Report_" + fromDate + "_to_" + toDate + ".pdf";
            file = new File(dir, fileName);
            // file = new File(dir, resultTo + "_Bill_" + day + "-" + month + "-" + year + "_" + hour + ":" + minutes + ".pdf");
            // file = new File(dir, "Bill.pdf");
            FileOutputStream fOut = new FileOutputStream(file);
            PdfWriter writer = PdfWriter.getInstance(doc, fOut);

            Log.d("File Name-------------", "" + file.getName());
            //open the document
            doc.open();

            PdfPTable ptHead = new PdfPTable(1);
            ptHead.setWidthPercentage(100);
            cell = new PdfPCell(new Paragraph("Bill", boldFont));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(1);

            PdfPTable ptHeadBlank1 = new PdfPTable(1);
            ptHeadBlank1.setWidthPercentage(100);
            cell = new PdfPCell(new Paragraph(" "));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(1);
            ptHeadBlank1.addCell(cell);

            PdfPTable ptHeadBlank2 = new PdfPTable(1);
            ptHeadBlank2.setWidthPercentage(100);
            cell = new PdfPCell(new Paragraph(" "));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(1);
            ptHeadBlank2.addCell(cell);

            //create table
            PdfPTable pt = new PdfPTable(1);
            pt.setWidthPercentage(100);

            cell = new PdfPCell();
            cell.setBorder(Rectangle.NO_BORDER);

            //set drawable in cell
            Drawable myImage = getActivity().getResources().getDrawable(R.drawable.ic_camera);
            Bitmap bitmap = ((BitmapDrawable) myImage).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);

            try {
                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                pt.addCell(cell);

                cell = new PdfPCell(new Paragraph("Happy Feast Co.", boldFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(1);
                pt.addCell(cell);

                cell = new PdfPCell(new Paragraph("Report : Item Wise HSN Report", boldFont));
                cell.setHorizontalAlignment(1);
                cell.setBorder(Rectangle.NO_BORDER);
                pt.addCell(cell);

                PdfPTable dateTable = new PdfPTable(2);
                dateTable.setWidthPercentage(100);
                cell = new PdfPCell(new Paragraph("From Date : " + fromDate, boldFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                dateTable.addCell(cell);

                cell = new PdfPCell(new Paragraph("To Date : " + toDate, boldFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(2);
                dateTable.addCell(cell);

                PdfPTable pTable = new PdfPTable(1);
                pTable.setWidthPercentage(100);

                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(1);
                cell.addElement(pt);
                pTable.addCell(cell);

                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(1);
                cell.addElement(dateTable);
                pTable.addCell(cell);

                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(1);
                cell.addElement(ptHead);
                pTable.addCell(cell);

                PdfPTable table = new PdfPTable(6);
                float[] columnWidth = new float[]{10, 30, 30, 30, 40, 40};
                table.setWidths(columnWidth);
                table.setTotalWidth(columnWidth);

                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setBackgroundColor(myColor);
                cell.setColspan(6);
                cell.addElement(pTable);

                table.addCell(cell);//image cell&address

                cell = new PdfPCell(new Phrase("NO.", boldTextFont));
                cell.setHorizontalAlignment(1);
                cell.setBackgroundColor(myColor1);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("HSN Code", boldTextFont));
                cell.setBackgroundColor(myColor1);
                cell.setHorizontalAlignment(1);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("CGST", boldTextFont));
                cell.setHorizontalAlignment(1);
                cell.setBackgroundColor(myColor1);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("SGST", boldTextFont));
                cell.setHorizontalAlignment(1);
                cell.setBackgroundColor(myColor1);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("TOTAL TAX", boldTextFont));
                cell.setHorizontalAlignment(1);
                cell.setBackgroundColor(myColor1);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("TAXABLE AMOUNT", boldTextFont));
                cell.setBackgroundColor(myColor1);
                cell.setHorizontalAlignment(1);
                table.addCell(cell);

                float total = 0;
                for (int i = 0; i < reportList.size(); i++) {

                    table.addCell("" + (i + 1));

                    table.addCell("" + reportList.get(i).getHsnCode());

                    cell = new PdfPCell(new Phrase("" + reportList.get(i).getCgst()));
                    cell.setHorizontalAlignment(2);
                    cell.setBackgroundColor(myColor);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase("" + reportList.get(i).getSgst()));
                    cell.setHorizontalAlignment(2);
                    cell.setBackgroundColor(myColor);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase("" + reportList.get(i).getTotalTax()));
                    cell.setHorizontalAlignment(1);
                    cell.setBackgroundColor(myColor);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase("" + reportList.get(i).getTaxableAmount()));
                    cell.setHorizontalAlignment(2);
                    cell.setBackgroundColor(myColor);
                    table.addCell(cell);

                    total = total + (reportList.get(i).getTaxableAmount());
                }

                //----BLANK ROW
                cell = new PdfPCell(new Phrase(""));
                cell.setBackgroundColor(myColor);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(""));
                cell.setBackgroundColor(myColor);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(""));
                cell.setBackgroundColor(myColor);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(" "));
                cell.setBackgroundColor(myColor);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(""));
                cell.setBackgroundColor(myColor);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(" "));
                cell.setBackgroundColor(myColor);
                table.addCell(cell);

                //-------------NEW TABLE--------------------------

                PdfPTable table2 = new PdfPTable(3);
                float[] columnWidth2 = new float[]{70, 30, 40};
                table2.setWidths(columnWidth2);

                cell = new PdfPCell(new Phrase(""));
                cell.setBackgroundColor(myColor);
                table2.addCell(cell);


                cell = new PdfPCell(new Phrase("  TOTAL", boldTotalFont));
                cell.setBorder(Rectangle.LEFT | Rectangle.BOTTOM | Rectangle.TOP);
                cell.setBackgroundColor(myColor);
                table2.addCell(cell);

                cell = new PdfPCell(new Phrase("" + total, boldTotalFont));
                cell.setHorizontalAlignment(2);
                cell.setBorder(Rectangle.RIGHT | Rectangle.BOTTOM | Rectangle.TOP);
                cell.setBackgroundColor(myColor);
                table2.addCell(cell);

                doc.add(table);
                doc.add(table2);

            } catch (DocumentException de) {
                commonDialog.dismiss();
                ////Log.e("PDFCreator", "DocumentException:" + de);
                Toast.makeText(getActivity(), "Unable To Generate Bill", Toast.LENGTH_SHORT).show();
            } finally {
                doc.close();
                commonDialog.dismiss();

                File file1 = new File(dir, fileName);

                if (file1.exists()) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);

                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                        intent.setDataAndType(Uri.fromFile(file1), "application/pdf");
                    } else {
                        if (file1.exists()) {
                            String authorities = BuildConfig.APPLICATION_ID + ".provider";
                            Uri uri = FileProvider.getUriForFile(getActivity(), authorities, file1);
                            intent.setDataAndType(uri, "application/pdf");
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        }
                    }
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    getActivity().startActivity(intent);

                } else {
                    commonDialog.dismiss();
                    Toast.makeText(getActivity(), "Unable To Generate Bill", Toast.LENGTH_SHORT).show();
                }

            }
        } catch (Exception e) {
            commonDialog.dismiss();
            e.printStackTrace();
            Toast.makeText(getActivity(), "Unable To Generate Bill", Toast.LENGTH_SHORT).show();
        }
    }

    public void createBillWiseReportPDF(String fromDate, String toDate, ArrayList<BillWiseReportModel> reportList) {

        final CommonDialog commonDialog = new CommonDialog(getActivity(), "Loading", "Please Wait...");
        commonDialog.show();

        Document doc = new Document();
        doc.setMargins(-60, -60, 40, 40);
        Font boldFont = new Font(Font.FontFamily.TIMES_ROMAN, 13, Font.BOLD);
        Font boldTotalFont = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD);
        Font boldTextFont = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD);
        Font textFont = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL);
        try {

            Calendar calendar = Calendar.getInstance();
            day = calendar.get(Calendar.DAY_OF_MONTH);
            month = calendar.get(Calendar.MONTH) + 1;
            year = calendar.get(Calendar.YEAR);
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minutes = calendar.get(Calendar.MINUTE);
            dateInMillis = calendar.getTimeInMillis();

            String fileName = "Bill_Wise_Report_" + fromDate + "_to_" + toDate + ".pdf";
            file = new File(dir, fileName);
            // file = new File(dir, resultTo + "_Bill_" + day + "-" + month + "-" + year + "_" + hour + ":" + minutes + ".pdf");
            // file = new File(dir, "Bill.pdf");
            FileOutputStream fOut = new FileOutputStream(file);
            PdfWriter writer = PdfWriter.getInstance(doc, fOut);

            Log.d("File Name-------------", "" + file.getName());
            //open the document
            doc.open();

            PdfPTable ptHead = new PdfPTable(1);
            ptHead.setWidthPercentage(100);
            cell = new PdfPCell(new Paragraph("", boldFont));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(1);

            //create table
            PdfPTable pt = new PdfPTable(1);
            pt.setWidthPercentage(100);

            cell = new PdfPCell();
            cell.setBorder(Rectangle.NO_BORDER);

            //set drawable in cell
            Drawable myImage = getActivity().getResources().getDrawable(R.drawable.ic_camera);
            Bitmap bitmap = ((BitmapDrawable) myImage).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);

            try {
                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                pt.addCell(cell);

                cell = new PdfPCell(new Paragraph("Happy Feast Co.", boldFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(1);
                pt.addCell(cell);

                cell = new PdfPCell(new Paragraph("Report : Bill Wise Report", boldFont));
                cell.setHorizontalAlignment(1);
                cell.setBorder(Rectangle.NO_BORDER);
                pt.addCell(cell);

                PdfPTable dateTable = new PdfPTable(2);
                dateTable.setWidthPercentage(100);
                cell = new PdfPCell(new Paragraph("From Date : " + fromDate, boldFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                dateTable.addCell(cell);

                cell = new PdfPCell(new Paragraph("To Date : " + toDate, boldFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(2);
                dateTable.addCell(cell);

                PdfPTable pTable = new PdfPTable(1);
                pTable.setWidthPercentage(100);

                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(1);
                cell.addElement(pt);
                pTable.addCell(cell);

                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(1);
                cell.addElement(dateTable);
                pTable.addCell(cell);

                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(1);
                cell.addElement(ptHead);
                pTable.addCell(cell);

                PdfPTable table = new PdfPTable(8);
                float[] columnWidth = new float[]{10, 30, 30, 30, 30, 30, 30, 30};
                table.setWidths(columnWidth);
                table.setTotalWidth(columnWidth);

                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setBackgroundColor(myColor);
                cell.setColspan(8);
                cell.addElement(pTable);

                table.addCell(cell);//image cell&address

                cell = new PdfPCell(new Phrase("NO.", boldTextFont));
                cell.setHorizontalAlignment(1);
                cell.setBackgroundColor(myColor1);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("BILL NO.", boldTextFont));
                cell.setHorizontalAlignment(1);
                cell.setBackgroundColor(myColor1);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("DATE", boldTextFont));
                cell.setBackgroundColor(myColor1);
                cell.setHorizontalAlignment(1);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("TOTAL TAX", boldTextFont));
                cell.setBackgroundColor(myColor1);
                cell.setHorizontalAlignment(1);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("TAXABLE AMOUNT", boldTextFont));
                cell.setBackgroundColor(myColor1);
                cell.setHorizontalAlignment(1);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("DISCOUNT", boldTextFont));
                cell.setBackgroundColor(myColor1);
                cell.setHorizontalAlignment(1);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("GRAND TOTAL", boldTextFont));
                cell.setBackgroundColor(myColor1);
                cell.setHorizontalAlignment(1);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("AMOUNT", boldTextFont));
                cell.setBackgroundColor(myColor1);
                cell.setHorizontalAlignment(1);
                table.addCell(cell);

                float total = 0;
                for (int i = 0; i < reportList.size(); i++) {

                    table.addCell("" + (i + 1));

                    table.addCell("" + reportList.get(i).getBillNo());

                    // int qty = (int) reportList.get(i).getBillNo();

                    cell = new PdfPCell(new Phrase("" + reportList.get(i).getBillDate()));
                    cell.setBackgroundColor(myColor);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase("" + (reportList.get(i).getCgst() + reportList.get(i).getSgst())));
                    cell.setHorizontalAlignment(2);
                    cell.setBackgroundColor(myColor);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase("" + reportList.get(i).getTaxableAmount()));
                    cell.setHorizontalAlignment(2);
                    cell.setBackgroundColor(myColor);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase("" + reportList.get(i).getDiscount()));
                    cell.setHorizontalAlignment(2);
                    cell.setBackgroundColor(myColor);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase("" + reportList.get(i).getGrandTotal()));
                    cell.setHorizontalAlignment(2);
                    cell.setBackgroundColor(myColor);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase("" + reportList.get(i).getPayableAmount()));
                    cell.setHorizontalAlignment(2);
                    cell.setBackgroundColor(myColor);
                    table.addCell(cell);

                    total = total + (reportList.get(i).getPayableAmount());
                }

                //----BLANK ROW
                cell = new PdfPCell(new Phrase(""));
                cell.setBackgroundColor(myColor);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(""));
                cell.setBackgroundColor(myColor);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(""));
                cell.setBackgroundColor(myColor);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(""));
                cell.setBackgroundColor(myColor);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(" "));
                cell.setBackgroundColor(myColor);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(""));
                cell.setBackgroundColor(myColor);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(" "));
                cell.setBackgroundColor(myColor);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(""));
                cell.setBackgroundColor(myColor);
                table.addCell(cell);


                //-------------NEW TABLE--------------------------

                PdfPTable table2 = new PdfPTable(3);
                float[] columnWidth2 = new float[]{80, 80, 60};
                table2.setWidths(columnWidth2);

                cell = new PdfPCell(new Phrase(""));
                cell.setBackgroundColor(myColor);
                table2.addCell(cell);


                cell = new PdfPCell(new Phrase("  TOTAL", boldTotalFont));
                cell.setBorder(Rectangle.LEFT | Rectangle.BOTTOM | Rectangle.TOP);
                cell.setBackgroundColor(myColor);
                table2.addCell(cell);

                cell = new PdfPCell(new Phrase("" + total, boldTotalFont));
                cell.setHorizontalAlignment(2);
                cell.setBorder(Rectangle.RIGHT | Rectangle.BOTTOM | Rectangle.TOP);
                cell.setBackgroundColor(myColor);
                table2.addCell(cell);

                doc.add(table);
                doc.add(table2);

            } catch (DocumentException de) {
                commonDialog.dismiss();
                ////Log.e("PDFCreator", "DocumentException:" + de);
                Toast.makeText(getActivity(), "Unable To Generate Bill", Toast.LENGTH_SHORT).show();
            } finally {
                doc.close();
                commonDialog.dismiss();

                File file1 = new File(dir, fileName);

                if (file1.exists()) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);

                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                        intent.setDataAndType(Uri.fromFile(file1), "application/pdf");
                    } else {
                        if (file1.exists()) {
                            String authorities = BuildConfig.APPLICATION_ID + ".provider";
                            Uri uri = FileProvider.getUriForFile(getActivity(), authorities, file1);
                            intent.setDataAndType(uri, "application/pdf");
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        }
                    }
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    getActivity().startActivity(intent);

                } else {
                    commonDialog.dismiss();
                    Toast.makeText(getActivity(), "Unable To Generate Bill", Toast.LENGTH_SHORT).show();
                }

            }
        } catch (Exception e) {
            commonDialog.dismiss();
            e.printStackTrace();
            Toast.makeText(getActivity(), "Unable To Generate Bill", Toast.LENGTH_SHORT).show();
        }
    }

    public void createDateWiseBillReportPDF(String fromDate, String toDate, ArrayList<DateWiseReportModel> reportList) {

        final CommonDialog commonDialog = new CommonDialog(getActivity(), "Loading", "Please Wait...");
        commonDialog.show();

        String resultTo = fromDate;

        Document doc = new Document();
        doc.setMargins(-16, -17, 40, 40);
        Font boldFont = new Font(Font.FontFamily.TIMES_ROMAN, 13, Font.BOLD);
        Font boldTotalFont = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD);
        Font boldTextFont = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD);
        Font textFont = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL);
        try {

            Calendar calendar = Calendar.getInstance();
            day = calendar.get(Calendar.DAY_OF_MONTH);
            month = calendar.get(Calendar.MONTH) + 1;
            year = calendar.get(Calendar.YEAR);
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minutes = calendar.get(Calendar.MINUTE);
            dateInMillis = calendar.getTimeInMillis();

            String fileName = "Date_Wise_Bill_Report_" + fromDate + "_to_" + toDate + ".pdf";
            file = new File(dir, fileName);
            // file = new File(dir, resultTo + "_Bill_" + day + "-" + month + "-" + year + "_" + hour + ":" + minutes + ".pdf");
            // file = new File(dir, "Bill.pdf");
            FileOutputStream fOut = new FileOutputStream(file);
            PdfWriter writer = PdfWriter.getInstance(doc, fOut);

            Log.d("File Name-------------", "" + file.getName());
            //open the document
            doc.open();

            PdfPTable ptHead = new PdfPTable(1);
            ptHead.setWidthPercentage(100);
            cell = new PdfPCell(new Paragraph("", boldFont));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(1);

            //create table
            PdfPTable pt = new PdfPTable(1);
            pt.setWidthPercentage(100);

            cell = new PdfPCell();
            cell.setBorder(Rectangle.NO_BORDER);

            //set drawable in cell
            Drawable myImage = getActivity().getResources().getDrawable(R.drawable.ic_camera);
            Bitmap bitmap = ((BitmapDrawable) myImage).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);

            try {
                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                pt.addCell(cell);

                cell = new PdfPCell(new Paragraph("Happy Feast Co.", boldFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(1);
                pt.addCell(cell);

                cell = new PdfPCell(new Paragraph("Report : Date Wise Bill Report", boldFont));
                cell.setHorizontalAlignment(1);
                cell.setBorder(Rectangle.NO_BORDER);
                pt.addCell(cell);

                PdfPTable dateTable = new PdfPTable(2);
                dateTable.setWidthPercentage(100);
                cell = new PdfPCell(new Paragraph("From Date : " + fromDate, boldFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                dateTable.addCell(cell);

                cell = new PdfPCell(new Paragraph("To Date : " + toDate, boldFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(2);
                dateTable.addCell(cell);

                PdfPTable pTable = new PdfPTable(1);
                pTable.setWidthPercentage(100);

                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(1);
                cell.addElement(pt);
                pTable.addCell(cell);

                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(1);
                cell.addElement(dateTable);
                pTable.addCell(cell);

                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(1);
                cell.addElement(ptHead);
                pTable.addCell(cell);

                PdfPTable table = new PdfPTable(4);
                float[] columnWidth = new float[]{20, 50, 40, 50};
                table.setWidths(columnWidth);
                table.setTotalWidth(columnWidth);

                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setBackgroundColor(myColor);
                cell.setColspan(3);
                cell.addElement(pTable);

                table.addCell(cell);//image cell&address

                cell = new PdfPCell(new Phrase("NO.", boldTextFont));
                cell.setHorizontalAlignment(1);
                cell.setBackgroundColor(myColor1);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("DATE", boldTextFont));
                cell.setBackgroundColor(myColor1);
                cell.setHorizontalAlignment(1);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("TOTAL", boldTextFont));
                cell.setBackgroundColor(myColor1);
                cell.setHorizontalAlignment(1);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("PAYABLE AMOUNT", boldTextFont));
                cell.setBackgroundColor(myColor1);
                cell.setHorizontalAlignment(1);
                table.addCell(cell);

                float total = 0;
                for (int i = 0; i < reportList.size(); i++) {

                    table.addCell("" + (i + 1));

                    table.addCell("" + reportList.get(i).getBillDate());

                    cell = new PdfPCell(new Phrase("" + reportList.get(i).getTotal()));
                    cell.setHorizontalAlignment(2);
                    cell.setBackgroundColor(myColor);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase("" + reportList.get(i).getPayableAmount()));
                    cell.setHorizontalAlignment(2);
                    cell.setBackgroundColor(myColor);
                    table.addCell(cell);

                    total = total + (reportList.get(i).getPayableAmount());
                }

                //----BLANK ROW
                cell = new PdfPCell(new Phrase(""));
                cell.setBackgroundColor(myColor);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(""));
                cell.setBackgroundColor(myColor);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(""));
                cell.setBackgroundColor(myColor);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(""));
                cell.setBackgroundColor(myColor);
                table.addCell(cell);

                //-------------NEW TABLE--------------------------

                PdfPTable table2 = new PdfPTable(3);
                float[] columnWidth2 = new float[]{60, 60, 60};
                table2.setWidths(columnWidth2);

                cell = new PdfPCell(new Phrase(""));
                cell.setBackgroundColor(myColor);
                table2.addCell(cell);


                cell = new PdfPCell(new Phrase("  TOTAL", boldTotalFont));
                cell.setBorder(Rectangle.LEFT | Rectangle.BOTTOM | Rectangle.TOP);
                cell.setBackgroundColor(myColor);
                table2.addCell(cell);

                cell = new PdfPCell(new Phrase("" + total, boldTotalFont));
                cell.setHorizontalAlignment(2);
                cell.setBorder(Rectangle.RIGHT | Rectangle.BOTTOM | Rectangle.TOP);
                cell.setBackgroundColor(myColor);
                table2.addCell(cell);

                doc.add(table);
                doc.add(table2);

            } catch (DocumentException de) {
                commonDialog.dismiss();
                ////Log.e("PDFCreator", "DocumentException:" + de);
                Toast.makeText(getActivity(), "Unable To Generate Bill", Toast.LENGTH_SHORT).show();
            } finally {
                doc.close();
                commonDialog.dismiss();

                File file1 = new File(dir, fileName);

                if (file1.exists()) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);

                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                        intent.setDataAndType(Uri.fromFile(file1), "application/pdf");
                    } else {
                        if (file1.exists()) {
                            String authorities = BuildConfig.APPLICATION_ID + ".provider";
                            Uri uri = FileProvider.getUriForFile(getActivity(), authorities, file1);
                            intent.setDataAndType(uri, "application/pdf");
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        }
                    }
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    getActivity().startActivity(intent);

                } else {
                    commonDialog.dismiss();
                    Toast.makeText(getActivity(), "Unable To Generate Bill", Toast.LENGTH_SHORT).show();
                }

            }
        } catch (Exception e) {
            commonDialog.dismiss();
            e.printStackTrace();
            Toast.makeText(getActivity(), "Unable To Generate Bill", Toast.LENGTH_SHORT).show();
        }
    }

    public void createMonthWiseBillReportPDF(String monthYear, ArrayList<MonthWiseBillReportModel> reportList) {

        final CommonDialog commonDialog = new CommonDialog(getActivity(), "Loading", "Please Wait...");
        commonDialog.show();

        Document doc = new Document();
        doc.setMargins(-16, -17, 40, 40);
        Font boldFont = new Font(Font.FontFamily.TIMES_ROMAN, 13, Font.BOLD);
        Font boldTotalFont = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD);
        Font boldTextFont = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD);
        Font textFont = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL);
        try {

            Calendar calendar = Calendar.getInstance();
            day = calendar.get(Calendar.DAY_OF_MONTH);
            month = calendar.get(Calendar.MONTH) + 1;
            year = calendar.get(Calendar.YEAR);
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minutes = calendar.get(Calendar.MINUTE);
            dateInMillis = calendar.getTimeInMillis();

            String fileName = "Month_Wise_Bill_Report_" + reportList.get(0).getMonth() + "_" + monthYear + ".pdf";
            file = new File(dir, fileName);
            // file = new File(dir, resultTo + "_Bill_" + day + "-" + month + "-" + year + "_" + hour + ":" + minutes + ".pdf");
            // file = new File(dir, "Bill.pdf");
            FileOutputStream fOut = new FileOutputStream(file);
            PdfWriter writer = PdfWriter.getInstance(doc, fOut);

            Log.d("File Name-------------", "" + file.getName());
            //open the document
            doc.open();

            PdfPTable ptHead = new PdfPTable(1);
            ptHead.setWidthPercentage(100);
            cell = new PdfPCell(new Paragraph("", boldFont));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(1);

            //create table
            PdfPTable pt = new PdfPTable(1);
            pt.setWidthPercentage(100);

            cell = new PdfPCell();
            cell.setBorder(Rectangle.NO_BORDER);

            //set drawable in cell
            Drawable myImage = getActivity().getResources().getDrawable(R.drawable.ic_camera);
            Bitmap bitmap = ((BitmapDrawable) myImage).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);

            try {
                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                pt.addCell(cell);

                cell = new PdfPCell(new Paragraph("Happy Feast Co.", boldFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(1);
                pt.addCell(cell);

                cell = new PdfPCell(new Paragraph("Report : Month Wise Bill Report", boldFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(1);
                pt.addCell(cell);

                PdfPTable dateTable = new PdfPTable(2);
                dateTable.setWidthPercentage(100);
                cell = new PdfPCell(new Paragraph("Month : " + reportList.get(0).getMonth(), boldFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                dateTable.addCell(cell);

                cell = new PdfPCell(new Paragraph("Year : " + monthYear, boldFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(2);
                dateTable.addCell(cell);

                PdfPTable pTable = new PdfPTable(1);
                pTable.setWidthPercentage(100);

                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(1);
                cell.addElement(pt);
                pTable.addCell(cell);

                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(1);
                cell.addElement(dateTable);
                pTable.addCell(cell);

                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(1);
                cell.addElement(ptHead);
                pTable.addCell(cell);

                PdfPTable table = new PdfPTable(6);
                float[] columnWidth = new float[]{10, 30, 30, 30, 30, 30};
                table.setWidths(columnWidth);
                table.setTotalWidth(columnWidth);

                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setBackgroundColor(myColor);
                cell.setColspan(6);
                cell.addElement(pTable);

                table.addCell(cell);//image cell&address

                cell = new PdfPCell(new Phrase("NO.", boldTextFont));
                cell.setHorizontalAlignment(1);
                cell.setBackgroundColor(myColor1);
                table.addCell(cell);


                cell = new PdfPCell(new Phrase("MONTH", boldTextFont));
                cell.setBackgroundColor(myColor1);
                cell.setHorizontalAlignment(1);
                table.addCell(cell);


                cell = new PdfPCell(new Phrase("GRAND TOTAL", boldTextFont));
                cell.setBackgroundColor(myColor1);
                cell.setHorizontalAlignment(1);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("TAXABLE AMOUNT", boldTextFont));
                cell.setBackgroundColor(myColor1);
                cell.setHorizontalAlignment(1);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("TAX AMOUNT", boldTextFont));
                cell.setBackgroundColor(myColor1);
                cell.setHorizontalAlignment(1);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("AMOUNT", boldTextFont));
                cell.setBackgroundColor(myColor1);
                cell.setHorizontalAlignment(1);
                table.addCell(cell);

                float total = 0;
                for (int i = 0; i < reportList.size(); i++) {

                    table.addCell("" + (i + 1));

                    table.addCell("" + reportList.get(i).getMonth());


                    cell = new PdfPCell(new Phrase("" + reportList.get(i).getGrandTotal()));
                    cell.setHorizontalAlignment(2);
                    cell.setBackgroundColor(myColor);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase("" + reportList.get(i).getTaxableAmount()));
                    cell.setHorizontalAlignment(2);
                    cell.setBackgroundColor(myColor);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase("" + reportList.get(i).getTaxAmt()));
                    cell.setHorizontalAlignment(2);
                    cell.setBackgroundColor(myColor);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase("" + reportList.get(i).getPayableAmount()));
                    cell.setHorizontalAlignment(2);
                    cell.setBackgroundColor(myColor);
                    table.addCell(cell);


                    total = total + (reportList.get(i).getPayableAmount());
                }

                //----BLANK ROW
                cell = new PdfPCell(new Phrase(""));
                cell.setBackgroundColor(myColor);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(""));
                cell.setBackgroundColor(myColor);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(""));
                cell.setBackgroundColor(myColor);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(""));
                cell.setBackgroundColor(myColor);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(""));
                cell.setBackgroundColor(myColor);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(" "));
                cell.setBackgroundColor(myColor);
                table.addCell(cell);

                //-------------NEW TABLE--------------------------

                PdfPTable table2 = new PdfPTable(3);
                float[] columnWidth2 = new float[]{60, 50, 50};
                table2.setWidths(columnWidth2);

                cell = new PdfPCell(new Phrase(""));
                cell.setBackgroundColor(myColor);
                table2.addCell(cell);


                cell = new PdfPCell(new Phrase("  TOTAL", boldTotalFont));
                cell.setBorder(Rectangle.LEFT | Rectangle.BOTTOM | Rectangle.TOP);
                cell.setBackgroundColor(myColor);
                table2.addCell(cell);

                cell = new PdfPCell(new Phrase("" + total, boldTotalFont));
                cell.setHorizontalAlignment(2);
                cell.setBorder(Rectangle.RIGHT | Rectangle.BOTTOM | Rectangle.TOP);
                cell.setBackgroundColor(myColor);
                table2.addCell(cell);

                doc.add(table);
                doc.add(table2);

            } catch (DocumentException de) {
                commonDialog.dismiss();
                ////Log.e("PDFCreator", "DocumentException:" + de);
                Toast.makeText(getActivity(), "Unable To Generate Bill", Toast.LENGTH_SHORT).show();
            } finally {
                doc.close();
                commonDialog.dismiss();

                File file1 = new File(dir, fileName);

                if (file1.exists()) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);

                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                        intent.setDataAndType(Uri.fromFile(file1), "application/pdf");
                    } else {
                        if (file1.exists()) {
                            String authorities = BuildConfig.APPLICATION_ID + ".provider";
                            Uri uri = FileProvider.getUriForFile(getActivity(), authorities, file1);
                            intent.setDataAndType(uri, "application/pdf");
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        }
                    }
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    getActivity().startActivity(intent);

                } else {
                    commonDialog.dismiss();
                    Toast.makeText(getActivity(), "Unable To Generate Bill", Toast.LENGTH_SHORT).show();
                }

            }
        } catch (Exception e) {
            commonDialog.dismiss();
            e.printStackTrace();
            Toast.makeText(getActivity(), "Unable To Generate Bill", Toast.LENGTH_SHORT).show();
        }
    }

    public void createDateWiseTaxReportPDF(String fromDate, String toDate, ArrayList<BillWiseTaxReportModel> reportList) {

        final CommonDialog commonDialog = new CommonDialog(getActivity(), "Loading", "Please Wait...");
        commonDialog.show();

        Document doc = new Document();
        doc.setMargins(-16, -17, 40, 40);
        Font boldFont = new Font(Font.FontFamily.TIMES_ROMAN, 13, Font.BOLD);
        Font boldTotalFont = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD);
        Font boldTextFont = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD);
        Font textFont = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL);
        try {

            Calendar calendar = Calendar.getInstance();
            day = calendar.get(Calendar.DAY_OF_MONTH);
            month = calendar.get(Calendar.MONTH) + 1;
            year = calendar.get(Calendar.YEAR);
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minutes = calendar.get(Calendar.MINUTE);
            dateInMillis = calendar.getTimeInMillis();

            String fileName = "Date_Wise_Tax_Report_" + fromDate + "_to_" + toDate + ".pdf";
            file = new File(dir, fileName);
            // file = new File(dir, resultTo + "_Bill_" + day + "-" + month + "-" + year + "_" + hour + ":" + minutes + ".pdf");
            // file = new File(dir, "Bill.pdf");
            FileOutputStream fOut = new FileOutputStream(file);
            PdfWriter writer = PdfWriter.getInstance(doc, fOut);

            Log.d("File Name-------------", "" + file.getName());
            //open the document
            doc.open();

            PdfPTable ptHead = new PdfPTable(1);
            ptHead.setWidthPercentage(100);
            cell = new PdfPCell(new Paragraph("", boldFont));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(1);

            //create table
            PdfPTable pt = new PdfPTable(1);
            pt.setWidthPercentage(100);

            cell = new PdfPCell();
            cell.setBorder(Rectangle.NO_BORDER);

            //set drawable in cell
            Drawable myImage = getActivity().getResources().getDrawable(R.drawable.ic_camera);
            Bitmap bitmap = ((BitmapDrawable) myImage).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);

            try {
                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                pt.addCell(cell);

                cell = new PdfPCell(new Paragraph("Happy Feast Co.", boldFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(1);
                pt.addCell(cell);

                cell = new PdfPCell(new Paragraph("Report : Date Wise Tax Report", boldFont));
                cell.setHorizontalAlignment(1);
                cell.setBorder(Rectangle.NO_BORDER);
                pt.addCell(cell);

                PdfPTable dateTable = new PdfPTable(2);
                dateTable.setWidthPercentage(100);
                cell = new PdfPCell(new Paragraph("From Date : " + fromDate, boldFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                dateTable.addCell(cell);

                cell = new PdfPCell(new Paragraph("To Date : " + toDate, boldFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(2);
                dateTable.addCell(cell);

                PdfPTable pTable = new PdfPTable(1);
                pTable.setWidthPercentage(100);

                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(1);
                cell.addElement(pt);
                pTable.addCell(cell);

                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(1);
                cell.addElement(dateTable);
                pTable.addCell(cell);

                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(1);
                cell.addElement(ptHead);
                pTable.addCell(cell);

                PdfPTable table = new PdfPTable(5);
                float[] columnWidth = new float[]{20, 50, 30, 40, 40};
                table.setWidths(columnWidth);
                table.setTotalWidth(columnWidth);

                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setBackgroundColor(myColor);
                cell.setColspan(5);
                cell.addElement(pTable);

                table.addCell(cell);//image cell&address

                cell = new PdfPCell(new Phrase("NO.", boldTextFont));
                cell.setHorizontalAlignment(1);
                cell.setBackgroundColor(myColor1);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("DATE", boldTextFont));
                cell.setBackgroundColor(myColor1);
                cell.setHorizontalAlignment(1);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("TAX", boldTextFont));
                cell.setBackgroundColor(myColor1);
                cell.setHorizontalAlignment(1);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("TOTAL TAX", boldTextFont));
                cell.setBackgroundColor(myColor1);
                cell.setHorizontalAlignment(1);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("TAXABLE AMOUNT", boldTextFont));
                cell.setBackgroundColor(myColor1);
                cell.setHorizontalAlignment(1);
                table.addCell(cell);

                float total = 0;
                for (int i = 0; i < reportList.size(); i++) {

                    table.addCell("" + (i + 1));

                    table.addCell("" + reportList.get(i).getBillDate());

                    cell = new PdfPCell(new Phrase("" + reportList.get(i).getTax()));
                    cell.setHorizontalAlignment(2);
                    cell.setBackgroundColor(myColor);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase("" + reportList.get(i).getTotalTax()));
                    cell.setHorizontalAlignment(2);
                    cell.setBackgroundColor(myColor);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase("" + reportList.get(i).getTaxableAmount()));
                    cell.setHorizontalAlignment(2);
                    cell.setBackgroundColor(myColor);
                    table.addCell(cell);


                    total = total + (reportList.get(i).getTaxableAmount());
                }

                //----BLANK ROW
                cell = new PdfPCell(new Phrase(""));
                cell.setBackgroundColor(myColor);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(""));
                cell.setBackgroundColor(myColor);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(""));
                cell.setBackgroundColor(myColor);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(""));
                cell.setBackgroundColor(myColor);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(""));
                cell.setBackgroundColor(myColor);
                table.addCell(cell);

                //-------------NEW TABLE--------------------------

                PdfPTable table2 = new PdfPTable(3);
                float[] columnWidth2 = new float[]{60, 60, 60};
                table2.setWidths(columnWidth2);

                cell = new PdfPCell(new Phrase(""));
                cell.setBackgroundColor(myColor);
                table2.addCell(cell);


                cell = new PdfPCell(new Phrase("  TOTAL", boldTotalFont));
                cell.setBorder(Rectangle.LEFT | Rectangle.BOTTOM | Rectangle.TOP);
                cell.setBackgroundColor(myColor);
                table2.addCell(cell);

                cell = new PdfPCell(new Phrase("" + total, boldTotalFont));
                cell.setHorizontalAlignment(2);
                cell.setBorder(Rectangle.RIGHT | Rectangle.BOTTOM | Rectangle.TOP);
                cell.setBackgroundColor(myColor);
                table2.addCell(cell);

                doc.add(table);
                doc.add(table2);

            } catch (DocumentException de) {
                commonDialog.dismiss();
                ////Log.e("PDFCreator", "DocumentException:" + de);
                Toast.makeText(getActivity(), "Unable To Generate Bill", Toast.LENGTH_SHORT).show();
            } finally {
                doc.close();
                commonDialog.dismiss();

                File file1 = new File(dir, fileName);

                if (file1.exists()) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);

                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                        intent.setDataAndType(Uri.fromFile(file1), "application/pdf");
                    } else {
                        if (file1.exists()) {
                            String authorities = BuildConfig.APPLICATION_ID + ".provider";
                            Uri uri = FileProvider.getUriForFile(getActivity(), authorities, file1);
                            intent.setDataAndType(uri, "application/pdf");
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        }
                    }
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    getActivity().startActivity(intent);

                } else {
                    commonDialog.dismiss();
                    Toast.makeText(getActivity(), "Unable To Generate Bill", Toast.LENGTH_SHORT).show();
                }

            }
        } catch (Exception e) {
            commonDialog.dismiss();
            e.printStackTrace();
            Toast.makeText(getActivity(), "Unable To Generate Bill", Toast.LENGTH_SHORT).show();
        }
    }

    public void createBillWiseTaxReportPDF(String fromDate, String toDate, ArrayList<BillWiseTaxReportModel> reportList) {

        final CommonDialog commonDialog = new CommonDialog(getActivity(), "Loading", "Please Wait...");
        commonDialog.show();

        Document doc = new Document();
        doc.setMargins(-16, -17, 40, 40);
        Font boldFont = new Font(Font.FontFamily.TIMES_ROMAN, 13, Font.BOLD);
        Font boldTotalFont = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD);
        Font boldTextFont = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD);
        Font textFont = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL);
        try {

            Calendar calendar = Calendar.getInstance();
            day = calendar.get(Calendar.DAY_OF_MONTH);
            month = calendar.get(Calendar.MONTH) + 1;
            year = calendar.get(Calendar.YEAR);
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minutes = calendar.get(Calendar.MINUTE);
            dateInMillis = calendar.getTimeInMillis();

            String fileName = "Bill_Wise_Tax_Report_" + fromDate + "_to_" + toDate + ".pdf";
            file = new File(dir, fileName);
            // file = new File(dir, resultTo + "_Bill_" + day + "-" + month + "-" + year + "_" + hour + ":" + minutes + ".pdf");
            // file = new File(dir, "Bill.pdf");
            FileOutputStream fOut = new FileOutputStream(file);
            PdfWriter writer = PdfWriter.getInstance(doc, fOut);

            Log.d("File Name-------------", "" + file.getName());
            //open the document
            doc.open();

            PdfPTable ptHead = new PdfPTable(1);
            ptHead.setWidthPercentage(100);
            cell = new PdfPCell(new Paragraph("", boldFont));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(1);

            //create table
            PdfPTable pt = new PdfPTable(1);
            pt.setWidthPercentage(100);

            cell = new PdfPCell();
            cell.setBorder(Rectangle.NO_BORDER);

            //set drawable in cell
            Drawable myImage = getActivity().getResources().getDrawable(R.drawable.ic_camera);
            Bitmap bitmap = ((BitmapDrawable) myImage).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);

            try {
                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                pt.addCell(cell);

                cell = new PdfPCell(new Paragraph("Happy Feast Co.", boldFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(1);
                pt.addCell(cell);

                cell = new PdfPCell(new Paragraph("Report : Bill Wise Tax Report", boldFont));
                cell.setHorizontalAlignment(1);
                cell.setBorder(Rectangle.NO_BORDER);
                pt.addCell(cell);

                PdfPTable dateTable = new PdfPTable(2);
                dateTable.setWidthPercentage(100);
                cell = new PdfPCell(new Paragraph("From Date : " + fromDate, boldFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                dateTable.addCell(cell);

                cell = new PdfPCell(new Paragraph("To Date : " + toDate, boldFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(2);
                dateTable.addCell(cell);

                PdfPTable pTable = new PdfPTable(1);
                pTable.setWidthPercentage(100);

                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(1);
                cell.addElement(pt);
                pTable.addCell(cell);

                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(1);
                cell.addElement(dateTable);
                pTable.addCell(cell);

                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(1);
                cell.addElement(ptHead);
                pTable.addCell(cell);

                PdfPTable table = new PdfPTable(6);
                float[] columnWidth = new float[]{20, 30, 30, 30, 30, 40};
                table.setWidths(columnWidth);
                table.setTotalWidth(columnWidth);

                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setBackgroundColor(myColor);
                cell.setColspan(6);
                cell.addElement(pTable);

                table.addCell(cell);//image cell&address

                cell = new PdfPCell(new Phrase("NO.", boldTextFont));
                cell.setHorizontalAlignment(1);
                cell.setBackgroundColor(myColor1);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("BILL NO.", boldTextFont));
                cell.setBackgroundColor(myColor1);
                cell.setHorizontalAlignment(1);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("DATE", boldTextFont));
                cell.setBackgroundColor(myColor1);
                cell.setHorizontalAlignment(1);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("TAX", boldTextFont));
                cell.setBackgroundColor(myColor1);
                cell.setHorizontalAlignment(1);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("TOTAL TAX", boldTextFont));
                cell.setBackgroundColor(myColor1);
                cell.setHorizontalAlignment(1);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("TAXABLE AMOUNT", boldTextFont));
                cell.setBackgroundColor(myColor1);
                cell.setHorizontalAlignment(1);
                table.addCell(cell);

                float total = 0;
                for (int i = 0; i < reportList.size(); i++) {

                    table.addCell("" + (i + 1));

                    table.addCell("" + reportList.get(i).getBillNo());

                    table.addCell("" + reportList.get(i).getBillDate());

                    cell = new PdfPCell(new Phrase("" + reportList.get(i).getTax()));
                    cell.setHorizontalAlignment(2);
                    cell.setBackgroundColor(myColor);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase("" + reportList.get(i).getTotalTax()));
                    cell.setHorizontalAlignment(2);
                    cell.setBackgroundColor(myColor);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase("" + reportList.get(i).getTaxableAmount()));
                    cell.setHorizontalAlignment(2);
                    cell.setBackgroundColor(myColor);
                    table.addCell(cell);

                    total = total + (reportList.get(i).getTaxableAmount());
                }

                //----BLANK ROW
                cell = new PdfPCell(new Phrase(""));
                cell.setBackgroundColor(myColor);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(""));
                cell.setBackgroundColor(myColor);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(""));
                cell.setBackgroundColor(myColor);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(""));
                cell.setBackgroundColor(myColor);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(""));
                cell.setBackgroundColor(myColor);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(""));
                cell.setBackgroundColor(myColor);
                table.addCell(cell);

                //-------------NEW TABLE--------------------------

                PdfPTable table2 = new PdfPTable(3);
                float[] columnWidth2 = new float[]{60, 60, 60};
                table2.setWidths(columnWidth2);

                cell = new PdfPCell(new Phrase(""));
                cell.setBackgroundColor(myColor);
                table2.addCell(cell);


                cell = new PdfPCell(new Phrase("  TOTAL", boldTotalFont));
                cell.setBorder(Rectangle.LEFT | Rectangle.BOTTOM | Rectangle.TOP);
                cell.setBackgroundColor(myColor);
                table2.addCell(cell);

                cell = new PdfPCell(new Phrase("" + total, boldTotalFont));
                cell.setHorizontalAlignment(2);
                cell.setBorder(Rectangle.RIGHT | Rectangle.BOTTOM | Rectangle.TOP);
                cell.setBackgroundColor(myColor);
                table2.addCell(cell);

                doc.add(table);
                doc.add(table2);

            } catch (DocumentException de) {
                commonDialog.dismiss();
                ////Log.e("PDFCreator", "DocumentException:" + de);
                Toast.makeText(getActivity(), "Unable To Generate Bill", Toast.LENGTH_SHORT).show();
            } finally {
                doc.close();
                commonDialog.dismiss();

                File file1 = new File(dir, fileName);

                if (file1.exists()) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);

                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                        intent.setDataAndType(Uri.fromFile(file1), "application/pdf");
                    } else {
                        if (file1.exists()) {
                            String authorities = BuildConfig.APPLICATION_ID + ".provider";
                            Uri uri = FileProvider.getUriForFile(getActivity(), authorities, file1);
                            intent.setDataAndType(uri, "application/pdf");
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        }
                    }
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    getActivity().startActivity(intent);

                } else {
                    commonDialog.dismiss();
                    Toast.makeText(getActivity(), "Unable To Generate Bill", Toast.LENGTH_SHORT).show();
                }

            }
        } catch (Exception e) {
            commonDialog.dismiss();
            e.printStackTrace();
            Toast.makeText(getActivity(), "Unable To Generate Bill", Toast.LENGTH_SHORT).show();
        }
    }

    public void createTaxLabReportPDF(String fromDate, String toDate, ArrayList<TaxLabWiseReportModel> reportList) {

        final CommonDialog commonDialog = new CommonDialog(getActivity(), "Loading", "Please Wait...");
        commonDialog.show();

        Document doc = new Document();
        doc.setMargins(-16, -17, 40, 40);
        Font boldFont = new Font(Font.FontFamily.TIMES_ROMAN, 13, Font.BOLD);
        Font boldTotalFont = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD);
        Font boldTextFont = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD);
        Font textFont = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL);
        try {

            Calendar calendar = Calendar.getInstance();
            day = calendar.get(Calendar.DAY_OF_MONTH);
            month = calendar.get(Calendar.MONTH) + 1;
            year = calendar.get(Calendar.YEAR);
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minutes = calendar.get(Calendar.MINUTE);
            dateInMillis = calendar.getTimeInMillis();

            String fileName = "Tax_Lab_Wise_Bill_Report_" + fromDate + "_to_" + toDate + ".pdf";
            file = new File(dir, fileName);
            // file = new File(dir, resultTo + "_Bill_" + day + "-" + month + "-" + year + "_" + hour + ":" + minutes + ".pdf");
            // file = new File(dir, "Bill.pdf");
            FileOutputStream fOut = new FileOutputStream(file);
            PdfWriter writer = PdfWriter.getInstance(doc, fOut);

            Log.d("File Name-------------", "" + file.getName());
            //open the document
            doc.open();

            PdfPTable ptHead = new PdfPTable(1);
            ptHead.setWidthPercentage(100);
            cell = new PdfPCell(new Paragraph("", boldFont));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(1);

            //create table
            PdfPTable pt = new PdfPTable(1);
            pt.setWidthPercentage(100);

            cell = new PdfPCell();
            cell.setBorder(Rectangle.NO_BORDER);

            //set drawable in cell
            Drawable myImage = getActivity().getResources().getDrawable(R.drawable.ic_camera);
            Bitmap bitmap = ((BitmapDrawable) myImage).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);

            try {
                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                pt.addCell(cell);

                cell = new PdfPCell(new Paragraph("Happy Feast Co.", boldFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(1);
                pt.addCell(cell);

                cell = new PdfPCell(new Paragraph("Report : Tax Lab Wise Bill Report", boldFont));
                cell.setHorizontalAlignment(1);
                cell.setBorder(Rectangle.NO_BORDER);
                pt.addCell(cell);

                PdfPTable dateTable = new PdfPTable(2);
                dateTable.setWidthPercentage(100);
                cell = new PdfPCell(new Paragraph("From Date : " + fromDate, boldFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                dateTable.addCell(cell);

                cell = new PdfPCell(new Paragraph("To Date : " + toDate, boldFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(2);
                dateTable.addCell(cell);

                PdfPTable pTable = new PdfPTable(1);
                pTable.setWidthPercentage(100);

                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(1);
                cell.addElement(pt);
                pTable.addCell(cell);

                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(1);
                cell.addElement(dateTable);
                pTable.addCell(cell);

                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(1);
                cell.addElement(ptHead);
                pTable.addCell(cell);

                PdfPTable table = new PdfPTable(4);
                float[] columnWidth = new float[]{30, 50, 50, 50};
                table.setWidths(columnWidth);
                table.setTotalWidth(columnWidth);

                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setBackgroundColor(myColor);
                cell.setColspan(4);
                cell.addElement(pTable);

                table.addCell(cell);//image cell&address

                cell = new PdfPCell(new Phrase("NO.", boldTextFont));
                cell.setHorizontalAlignment(1);
                cell.setBackgroundColor(myColor1);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("TAX", boldTextFont));
                cell.setBackgroundColor(myColor1);
                cell.setHorizontalAlignment(1);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("TOTAL TAX", boldTextFont));
                cell.setBackgroundColor(myColor1);
                cell.setHorizontalAlignment(1);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("TAXABLE AMOUNT", boldTextFont));
                cell.setBackgroundColor(myColor1);
                cell.setHorizontalAlignment(1);
                table.addCell(cell);

                float total = 0;
                for (int i = 0; i < reportList.size(); i++) {

                    table.addCell("" + (i + 1));

                    cell = new PdfPCell(new Phrase("" + reportList.get(i).getTax()));
                    cell.setHorizontalAlignment(2);
                    cell.setBackgroundColor(myColor);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase("" + reportList.get(i).getTotalTax()));
                    cell.setHorizontalAlignment(2);
                    cell.setBackgroundColor(myColor);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase("" + reportList.get(i).getTaxableAmount()));
                    cell.setHorizontalAlignment(2);
                    cell.setBackgroundColor(myColor);
                    table.addCell(cell);

                    total = total + (reportList.get(i).getTaxableAmount());
                }

                //----BLANK ROW
                cell = new PdfPCell(new Phrase(""));
                cell.setBackgroundColor(myColor);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(""));
                cell.setBackgroundColor(myColor);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(""));
                cell.setBackgroundColor(myColor);
                table.addCell(cell);


                //-------------NEW TABLE--------------------------

                PdfPTable table2 = new PdfPTable(3);
                float[] columnWidth2 = new float[]{80, 50, 50};
                table2.setWidths(columnWidth2);

                cell = new PdfPCell(new Phrase(""));
                cell.setBackgroundColor(myColor);
                table2.addCell(cell);

                cell = new PdfPCell(new Phrase("  TOTAL", boldTotalFont));
                cell.setBorder(Rectangle.LEFT | Rectangle.BOTTOM | Rectangle.TOP);
                cell.setBackgroundColor(myColor);
                table2.addCell(cell);

                cell = new PdfPCell(new Phrase("" + total, boldTotalFont));
                cell.setHorizontalAlignment(2);
                cell.setBorder(Rectangle.RIGHT | Rectangle.BOTTOM | Rectangle.TOP);
                cell.setBackgroundColor(myColor);
                table2.addCell(cell);

                doc.add(table);
                doc.add(table2);

            } catch (DocumentException de) {
                commonDialog.dismiss();
                ////Log.e("PDFCreator", "DocumentException:" + de);
                Toast.makeText(getActivity(), "Unable To Generate Bill", Toast.LENGTH_SHORT).show();
            } finally {
                doc.close();
                commonDialog.dismiss();

                File file1 = new File(dir, fileName);

                if (file1.exists()) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);

                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                        intent.setDataAndType(Uri.fromFile(file1), "application/pdf");
                    } else {
                        if (file1.exists()) {
                            String authorities = BuildConfig.APPLICATION_ID + ".provider";
                            Uri uri = FileProvider.getUriForFile(getActivity(), authorities, file1);
                            intent.setDataAndType(uri, "application/pdf");
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        }
                    }
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    getActivity().startActivity(intent);

                } else {
                    commonDialog.dismiss();
                    Toast.makeText(getActivity(), "Unable To Generate Bill", Toast.LENGTH_SHORT).show();
                }

            }
        } catch (Exception e) {
            commonDialog.dismiss();
            e.printStackTrace();
            Toast.makeText(getActivity(), "Unable To Generate Bill", Toast.LENGTH_SHORT).show();
        }
    }

    public void createOrderCancelReportPDF(String fromDate, String toDate, ArrayList<OrderCancelReportModel> reportList) {

        final CommonDialog commonDialog = new CommonDialog(getActivity(), "Loading", "Please Wait...");
        commonDialog.show();

        Document doc = new Document();
        doc.setMargins(-16, -17, 40, 40);
        Font boldFont = new Font(Font.FontFamily.TIMES_ROMAN, 13, Font.BOLD);
        Font boldTotalFont = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD);
        Font boldTextFont = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD);
        Font textFont = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL);

        try {

            Calendar calendar = Calendar.getInstance();
            day = calendar.get(Calendar.DAY_OF_MONTH);
            month = calendar.get(Calendar.MONTH) + 1;
            year = calendar.get(Calendar.YEAR);
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minutes = calendar.get(Calendar.MINUTE);
            dateInMillis = calendar.getTimeInMillis();

            String fileName = "Order_Cancel_Report_" + fromDate + "_to_" + toDate + ".pdf";
            file = new File(dir, fileName);
            // file = new File(dir, resultTo + "_Bill_" + day + "-" + month + "-" + year + "_" + hour + ":" + minutes + ".pdf");
            // file = new File(dir, "Bill.pdf");
            FileOutputStream fOut = new FileOutputStream(file);
            PdfWriter writer = PdfWriter.getInstance(doc, fOut);

            Log.d("File Name-------------", "" + file.getName());
            //open the document
            doc.open();

            PdfPTable ptHead = new PdfPTable(1);
            ptHead.setWidthPercentage(100);
            cell = new PdfPCell(new Paragraph("", boldFont));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(1);

            //create table
            PdfPTable pt = new PdfPTable(1);
            pt.setWidthPercentage(100);

            cell = new PdfPCell();
            cell.setBorder(Rectangle.NO_BORDER);

            //set drawable in cell
            Drawable myImage = getActivity().getResources().getDrawable(R.drawable.ic_camera);
            Bitmap bitmap = ((BitmapDrawable) myImage).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);

            try {
                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                pt.addCell(cell);

                cell = new PdfPCell(new Paragraph("Happy Feast Co.", boldFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(1);
                pt.addCell(cell);

                cell = new PdfPCell(new Paragraph("Report : Order Cancel Report", boldFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(1);
                pt.addCell(cell);

                PdfPTable dateTable = new PdfPTable(2);
                dateTable.setWidthPercentage(100);
                cell = new PdfPCell(new Paragraph("From Date : " + fromDate, boldFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                dateTable.addCell(cell);

                cell = new PdfPCell(new Paragraph("To Date : " + toDate, boldFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(2);
                dateTable.addCell(cell);


                PdfPTable pTable = new PdfPTable(1);
                pTable.setWidthPercentage(100);

                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(1);
                cell.addElement(pt);
                pTable.addCell(cell);

                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(1);
                cell.addElement(dateTable);
                pTable.addCell(cell);

                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(1);
                cell.addElement(ptHead);
                pTable.addCell(cell);

                PdfPTable table = new PdfPTable(6);
                float[] columnWidth = new float[]{10, 30, 50, 30, 50, 50};
                table.setWidths(columnWidth);
                table.setTotalWidth(columnWidth);

                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setBackgroundColor(myColor);
                cell.setColspan(6);
                cell.addElement(pTable);

                table.addCell(cell);//image cell&address

                cell = new PdfPCell(new Phrase("NO.", boldTextFont));
                cell.setHorizontalAlignment(1);
                cell.setBackgroundColor(myColor1);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("ORDER NO.", boldTextFont));
                cell.setBackgroundColor(myColor1);
                cell.setHorizontalAlignment(1);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("ITEM NAME", boldTextFont));
                cell.setBackgroundColor(myColor1);
                cell.setHorizontalAlignment(1);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("QUANTITY", boldTextFont));
                cell.setBackgroundColor(myColor1);
                cell.setHorizontalAlignment(1);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("STATUS", boldTextFont));
                cell.setBackgroundColor(myColor1);
                cell.setHorizontalAlignment(1);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("REMARK", boldTextFont));
                cell.setBackgroundColor(myColor1);
                cell.setHorizontalAlignment(1);
                table.addCell(cell);

                float total = 0;
                for (int i = 0; i < reportList.size(); i++) {

                    cell = new PdfPCell(new Phrase("" + (i + 1)));
                    cell.setHorizontalAlignment(1);
                    cell.setBackgroundColor(myColor);
                    table.addCell(cell);

                    table.addCell("" + reportList.get(i).getOrderId());

                    cell = new PdfPCell(new Phrase("" + reportList.get(i).getItemName()));
                    cell.setBackgroundColor(myColor);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase("" + reportList.get(i).getQuantity()));
                    cell.setHorizontalAlignment(1);
                    cell.setBackgroundColor(myColor);
                    table.addCell(cell);

                    String status = "";
                    if (reportList.get(i).getStatus() == 1) {
                        status = "Chargeable";
                    } else if (reportList.get(i).getStatus() == 2) {
                        status = "Cancel";
                    } else if (reportList.get(i).getStatus() == 3) {
                        status = "NC 1";
                    } else if (reportList.get(i).getStatus() == 4) {
                        status = "NC 2";
                    } else if (reportList.get(i).getStatus() == 5) {
                        status = "NC 3";
                    }

                    table.addCell("" + status);

                    table.addCell("" + reportList.get(i).getRemark());

                }

                doc.add(table);

            } catch (DocumentException de) {
                commonDialog.dismiss();
                ////Log.e("PDFCreator", "DocumentException:" + de);
                Toast.makeText(getActivity(), "Unable To Generate Bill", Toast.LENGTH_SHORT).show();
            } finally {
                doc.close();
                commonDialog.dismiss();

                File file1 = new File(dir, fileName);

                if (file1.exists()) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);

                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                        intent.setDataAndType(Uri.fromFile(file1), "application/pdf");
                    } else {
                        if (file1.exists()) {
                            String authorities = BuildConfig.APPLICATION_ID + ".provider";
                            Uri uri = FileProvider.getUriForFile(getActivity(), authorities, file1);
                            intent.setDataAndType(uri, "application/pdf");
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        }
                    }
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    getActivity().startActivity(intent);

                } else {
                    commonDialog.dismiss();
                    Toast.makeText(getActivity(), "Unable To Generate Bill", Toast.LENGTH_SHORT).show();
                }

            }
        } catch (Exception e) {
            commonDialog.dismiss();
            e.printStackTrace();
            Toast.makeText(getActivity(), "Unable To Generate Bill", Toast.LENGTH_SHORT).show();
        }
    }

    public void createItemCancelReportPDF(String fromDate, String toDate, ArrayList<ItemCancelReportModel> reportList) {

        final CommonDialog commonDialog = new CommonDialog(getActivity(), "Loading", "Please Wait...");
        commonDialog.show();

        Document doc = new Document();
        doc.setMargins(-16, -17, 40, 40);
        Font boldFont = new Font(Font.FontFamily.TIMES_ROMAN, 13, Font.BOLD);
        Font boldTotalFont = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD);
        Font boldTextFont = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD);
        Font textFont = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL);

        try {

            Calendar calendar = Calendar.getInstance();
            day = calendar.get(Calendar.DAY_OF_MONTH);
            month = calendar.get(Calendar.MONTH) + 1;
            year = calendar.get(Calendar.YEAR);
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minutes = calendar.get(Calendar.MINUTE);
            dateInMillis = calendar.getTimeInMillis();

            String fileName = "Item_Cancel_Report_" + fromDate + "_to_" + toDate + ".pdf";
            file = new File(dir, fileName);
            // file = new File(dir, resultTo + "_Bill_" + day + "-" + month + "-" + year + "_" + hour + ":" + minutes + ".pdf");
            // file = new File(dir, "Bill.pdf");
            FileOutputStream fOut = new FileOutputStream(file);
            PdfWriter writer = PdfWriter.getInstance(doc, fOut);

            Log.d("File Name-------------", "" + file.getName());
            //open the document
            doc.open();

            PdfPTable ptHead = new PdfPTable(1);
            ptHead.setWidthPercentage(100);
            cell = new PdfPCell(new Paragraph("", boldFont));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(1);

            //create table
            PdfPTable pt = new PdfPTable(1);
            pt.setWidthPercentage(100);

            cell = new PdfPCell();
            cell.setBorder(Rectangle.NO_BORDER);

            //set drawable in cell
            Drawable myImage = getActivity().getResources().getDrawable(R.drawable.ic_camera);
            Bitmap bitmap = ((BitmapDrawable) myImage).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);

            try {
                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                pt.addCell(cell);

                cell = new PdfPCell(new Paragraph("Happy Feast Co.", boldFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(1);
                pt.addCell(cell);

                cell = new PdfPCell(new Paragraph("Report : Item Cancel Report", boldFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(1);
                pt.addCell(cell);

                PdfPTable dateTable = new PdfPTable(2);
                dateTable.setWidthPercentage(100);
                cell = new PdfPCell(new Paragraph("From Date : " + fromDate, boldFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                dateTable.addCell(cell);

                cell = new PdfPCell(new Paragraph("To Date : " + toDate, boldFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(2);
                dateTable.addCell(cell);

                PdfPTable pTable = new PdfPTable(1);
                pTable.setWidthPercentage(100);

                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(1);
                cell.addElement(pt);
                pTable.addCell(cell);

                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(1);
                cell.addElement(dateTable);
                pTable.addCell(cell);

                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(1);
                cell.addElement(ptHead);
                pTable.addCell(cell);

                PdfPTable table = new PdfPTable(6);
                float[] columnWidth = new float[]{20, 50, 20, 20, 20, 20};
                table.setWidths(columnWidth);
                table.setTotalWidth(columnWidth);

                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setBackgroundColor(myColor);
                cell.setColspan(6);
                cell.addElement(pTable);

                table.addCell(cell);//image cell&address

                cell = new PdfPCell(new Phrase("NO.", boldTextFont));
                cell.setHorizontalAlignment(1);
                cell.setBackgroundColor(myColor1);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("ITEM NAME", boldTextFont));
                cell.setBackgroundColor(myColor1);
                cell.setHorizontalAlignment(1);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("CHARGEABLE", boldTextFont));
                cell.setBackgroundColor(myColor1);
                cell.setHorizontalAlignment(1);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("NC 1", boldTextFont));
                cell.setBackgroundColor(myColor1);
                cell.setHorizontalAlignment(1);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("NC 2", boldTextFont));
                cell.setBackgroundColor(myColor1);
                cell.setHorizontalAlignment(1);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("NC 3", boldTextFont));
                cell.setBackgroundColor(myColor1);
                cell.setHorizontalAlignment(1);
                table.addCell(cell);

                float total = 0;
                for (int i = 0; i < reportList.size(); i++) {

                    cell = new PdfPCell(new Phrase("" + (i + 1)));
                    cell.setHorizontalAlignment(1);
                    cell.setBackgroundColor(myColor);
                    table.addCell(cell);

                    table.addCell("" + reportList.get(i).getItemName());

                    table.addCell("" + reportList.get(i).getChargable());
                    table.addCell("" + reportList.get(i).getNc1());
                    table.addCell("" + reportList.get(i).getNc2());
                    table.addCell("" + reportList.get(i).getNc3());

                }

                doc.add(table);

            } catch (DocumentException de) {
                commonDialog.dismiss();
                ////Log.e("PDFCreator", "DocumentException:" + de);
                Toast.makeText(getActivity(), "Unable To Generate Bill", Toast.LENGTH_SHORT).show();
            } finally {
                doc.close();
                commonDialog.dismiss();

                File file1 = new File(dir, fileName);

                if (file1.exists()) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);

                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                        intent.setDataAndType(Uri.fromFile(file1), "application/pdf");
                    } else {
                        if (file1.exists()) {
                            String authorities = BuildConfig.APPLICATION_ID + ".provider";
                            Uri uri = FileProvider.getUriForFile(getActivity(), authorities, file1);
                            intent.setDataAndType(uri, "application/pdf");
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        }
                    }
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    getActivity().startActivity(intent);

                } else {
                    commonDialog.dismiss();
                    Toast.makeText(getActivity(), "Unable To Generate Bill", Toast.LENGTH_SHORT).show();
                }

            }
        } catch (Exception e) {
            commonDialog.dismiss();
            e.printStackTrace();
            Toast.makeText(getActivity(), "Unable To Generate Bill", Toast.LENGTH_SHORT).show();
        }
    }

    public void createTableWiseReportPDF(String fromDate, String toDate, ArrayList<TableWiseReportModel> reportList) {

        final CommonDialog commonDialog = new CommonDialog(getActivity(), "Loading", "Please Wait...");
        commonDialog.show();

        String resultTo = fromDate;

        Document doc = new Document();
        doc.setMargins(-16, -17, 40, 40);
        Font boldFont = new Font(Font.FontFamily.TIMES_ROMAN, 13, Font.BOLD);
        Font boldTotalFont = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD);
        Font boldTextFont = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD);
        Font textFont = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL);
        try {

            Calendar calendar = Calendar.getInstance();
            day = calendar.get(Calendar.DAY_OF_MONTH);
            month = calendar.get(Calendar.MONTH) + 1;
            year = calendar.get(Calendar.YEAR);
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minutes = calendar.get(Calendar.MINUTE);
            dateInMillis = calendar.getTimeInMillis();

            String fileName = "Table_Wise_Report_" + fromDate + "_to_" + toDate + ".pdf";
            file = new File(dir, fileName);
            // file = new File(dir, resultTo + "_Bill_" + day + "-" + month + "-" + year + "_" + hour + ":" + minutes + ".pdf");
            // file = new File(dir, "Bill.pdf");
            FileOutputStream fOut = new FileOutputStream(file);
            PdfWriter writer = PdfWriter.getInstance(doc, fOut);

            Log.d("File Name-------------", "" + file.getName());
            //open the document
            doc.open();

            PdfPTable ptHead = new PdfPTable(1);
            ptHead.setWidthPercentage(100);
            cell = new PdfPCell(new Paragraph("", boldFont));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(1);

            //create table
            PdfPTable pt = new PdfPTable(1);
            pt.setWidthPercentage(100);

            cell = new PdfPCell();
            cell.setBorder(Rectangle.NO_BORDER);

            //set drawable in cell
            Drawable myImage = getActivity().getResources().getDrawable(R.drawable.ic_camera);
            Bitmap bitmap = ((BitmapDrawable) myImage).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);

            try {
                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                pt.addCell(cell);

                cell = new PdfPCell(new Paragraph("Happy Feast Co.", boldFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(1);
                pt.addCell(cell);

                cell = new PdfPCell(new Paragraph("Report : Table Wise Report", boldFont));
                cell.setHorizontalAlignment(1);
                cell.setBorder(Rectangle.NO_BORDER);
                pt.addCell(cell);

                PdfPTable dateTable = new PdfPTable(2);
                dateTable.setWidthPercentage(100);
                cell = new PdfPCell(new Paragraph("From Date : " + fromDate, boldFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                dateTable.addCell(cell);

                cell = new PdfPCell(new Paragraph("To Date : " + toDate, boldFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(2);
                dateTable.addCell(cell);

                PdfPTable pTable = new PdfPTable(1);
                pTable.setWidthPercentage(100);

                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(1);
                cell.addElement(pt);
                pTable.addCell(cell);

                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(1);
                cell.addElement(dateTable);
                pTable.addCell(cell);

                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(1);
                cell.addElement(ptHead);
                pTable.addCell(cell);

                PdfPTable table = new PdfPTable(5);
                float[] columnWidth = new float[]{20, 30, 50, 40, 40};
                table.setWidths(columnWidth);
                table.setTotalWidth(columnWidth);

                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setBackgroundColor(myColor);
                cell.setColspan(5);
                cell.addElement(pTable);

                table.addCell(cell);//image cell&address

                cell = new PdfPCell(new Phrase("NO.", boldTextFont));
                cell.setHorizontalAlignment(1);
                cell.setBackgroundColor(myColor1);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("TABLE NO.", boldTextFont));
                cell.setBackgroundColor(myColor1);
                cell.setHorizontalAlignment(1);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("TABLE NAME", boldTextFont));
                cell.setBackgroundColor(myColor1);
                cell.setHorizontalAlignment(1);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("TOTAL", boldTextFont));
                cell.setBackgroundColor(myColor1);
                cell.setHorizontalAlignment(1);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("AMOUNT", boldTextFont));
                cell.setBackgroundColor(myColor1);
                cell.setHorizontalAlignment(1);
                table.addCell(cell);

                float total = 0;
                for (int i = 0; i < reportList.size(); i++) {

                    table.addCell("" + (i + 1));

                    table.addCell("" + reportList.get(i).getTableNo());

                    table.addCell("" + reportList.get(i).getTableName());

                    cell = new PdfPCell(new Phrase("" + reportList.get(i).getTotal()));
                    cell.setHorizontalAlignment(2);
                    cell.setBackgroundColor(myColor);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase("" + reportList.get(i).getPayableAmount()));
                    cell.setHorizontalAlignment(2);
                    cell.setBackgroundColor(myColor);
                    table.addCell(cell);

                    total = total + (reportList.get(i).getPayableAmount());
                }

                //----BLANK ROW
                cell = new PdfPCell(new Phrase(""));
                cell.setBackgroundColor(myColor);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(""));
                cell.setBackgroundColor(myColor);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(""));
                cell.setBackgroundColor(myColor);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(""));
                cell.setBackgroundColor(myColor);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(""));
                cell.setBackgroundColor(myColor);
                table.addCell(cell);


                //-------------NEW TABLE--------------------------

                PdfPTable table2 = new PdfPTable(3);
                float[] columnWidth2 = new float[]{50, 70, 60};
                table2.setWidths(columnWidth2);

                cell = new PdfPCell(new Phrase(""));
                cell.setBackgroundColor(myColor);
                table2.addCell(cell);


                cell = new PdfPCell(new Phrase("  TOTAL", boldTotalFont));
                cell.setBorder(Rectangle.LEFT | Rectangle.BOTTOM | Rectangle.TOP);
                cell.setBackgroundColor(myColor);
                table2.addCell(cell);

                cell = new PdfPCell(new Phrase("" + total, boldTotalFont));
                cell.setHorizontalAlignment(2);
                cell.setBorder(Rectangle.RIGHT | Rectangle.BOTTOM | Rectangle.TOP);
                cell.setBackgroundColor(myColor);
                table2.addCell(cell);

                doc.add(table);
                doc.add(table2);

            } catch (DocumentException de) {
                commonDialog.dismiss();
                ////Log.e("PDFCreator", "DocumentException:" + de);
                Toast.makeText(getActivity(), "Unable To Generate Bill", Toast.LENGTH_SHORT).show();
            } finally {
                doc.close();
                commonDialog.dismiss();

                File file1 = new File(dir, fileName);

                if (file1.exists()) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);

                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                        intent.setDataAndType(Uri.fromFile(file1), "application/pdf");
                    } else {
                        if (file1.exists()) {
                            String authorities = BuildConfig.APPLICATION_ID + ".provider";
                            Uri uri = FileProvider.getUriForFile(getActivity(), authorities, file1);
                            intent.setDataAndType(uri, "application/pdf");
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        }
                    }
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    getActivity().startActivity(intent);

                } else {
                    commonDialog.dismiss();
                    Toast.makeText(getActivity(), "Unable To Generate Bill", Toast.LENGTH_SHORT).show();
                }

            }
        } catch (Exception e) {
            commonDialog.dismiss();
            e.printStackTrace();
            Toast.makeText(getActivity(), "Unable To Generate Bill", Toast.LENGTH_SHORT).show();
        }
    }


    //---------------------EXCEL-----------------------------

    public void createItemWiseReportExcel(String fromDate, String toDate, ArrayList<ItemReportModel> reportList) {
        Workbook wb = null;
        String fileName = "";

        final CommonDialog commonDialog = new CommonDialog(getActivity(), "Loading", "Please Wait...");
        commonDialog.show();

        try {
            fileName = "Item_Wise_Report_" + fromDate + "_" + toDate + ".xls";

            wb = new HSSFWorkbook();
            Cell c = null;

            //------------------New Sheet-----------------
            Sheet sheet1 = null;
            sheet1 = wb.createSheet("Report");

            //---------------------Header Row--------------------
            Row companyTitleRow = sheet1.createRow(0);
            c = companyTitleRow.createCell(1);
            c.setCellValue("Happy Feast Co.");

            Row reportTitleRow = sheet1.createRow(1);
            c = reportTitleRow.createCell(1);
            c.setCellValue("REPORT : ");
            c = reportTitleRow.createCell(2);
            c.setCellValue("Item Wise Report");

            //-------------------Empty Row------------------
            Row emptyRow = sheet1.createRow(2);

            //----------------Date Row--------------
            Row dateRow = sheet1.createRow(3);

            c = dateRow.createCell(1);
            c.setCellValue("FROM DATE : ");

            c = dateRow.createCell(2);
            c.setCellValue("" + fromDate);

            c = dateRow.createCell(3);
            c.setCellValue("TO DATE : ");

            c = dateRow.createCell(4);
            c.setCellValue("" + toDate);

            //-------------------Empty Row----------------
            Row emptyRow3 = sheet1.createRow(4);

            //--------------Table Header Row--------------
            Row headerRow = sheet1.createRow(5);

            c = headerRow.createCell(0);
            c.setCellValue("NO.");

            c = headerRow.createCell(1);
            c.setCellValue("ITEM");

            c = headerRow.createCell(2);
            c.setCellValue("QUANTITY");

            c = headerRow.createCell(3);
            c.setCellValue("UNIT PRICE");

            c = headerRow.createCell(4);
            c.setCellValue("TOTAL");

            c = headerRow.createCell(5);
            c.setCellValue("PAYABLE AMOUNT");


            float total = 0;

            //-----------------Table Data Row---------------------
            for (int i = 0; i < reportList.size(); i++) {

                total = total + reportList.get(i).getPayableAmt();

                Row newRow = sheet1.createRow((i + 6));

                c = newRow.createCell(0);
                c.setCellValue("" + (i + 1));

                c = newRow.createCell(1);
                c.setCellValue("" + reportList.get(i).getItemName());

                c = newRow.createCell(2);
                c.setCellValue("" + reportList.get(i).getQuantity());

                c = newRow.createCell(3);
                c.setCellValue("" + reportList.get(i).getRate());

                c = newRow.createCell(4);
                c.setCellValue("" + reportList.get(i).getTotal());

                c = newRow.createCell(5);
                c.setCellValue("" + reportList.get(i).getPayableAmt());

            }

            //--------------------Total Row-----------------------
            Row totalRow = sheet1.createRow(reportList.size() + 7);

            c = totalRow.createCell(0);
            c.setCellValue("");

            c = totalRow.createCell(1);
            c.setCellValue("");

            c = totalRow.createCell(2);
            c.setCellValue("");

            c = totalRow.createCell(3);
            c.setCellValue("TOTAL");

            c = totalRow.createCell(4);
            c.setCellValue("" + total);

            sheet1.setColumnWidth(0, (3 * 500));
            sheet1.setColumnWidth(1, (16 * 500));
            sheet1.setColumnWidth(2, (6 * 500));
            sheet1.setColumnWidth(3, (9 * 500));
            sheet1.setColumnWidth(4, (9 * 500));

            File file = new File(dir, fileName);
            FileOutputStream os = null;

            try {
                os = new FileOutputStream(file);
                wb.write(os);
                //Log.e("FileUtils", "Writing file" + file);
            } catch (IOException e) {
                commonDialog.dismiss();
                //Log.e("FileUtils", "Error writing " + file, e);
            } catch (Exception e) {
                commonDialog.dismiss();
                //Log.e("FileUtils", "Failed to save file", e);
            } finally {
                try {
                    if (null != os)
                        os.close();

                    if (file.exists()) {
                        commonDialog.dismiss();
                        //Log.e("EXCEL", "----------------------------------------------");
                        Intent intent = new Intent(Intent.ACTION_VIEW);

                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                            intent.setDataAndType(Uri.fromFile(file), "application/vnd.ms-excel");
                        } else {
                            if (file.exists()) {
                                String authorities = BuildConfig.APPLICATION_ID + ".provider";
                                Uri uri = FileProvider.getUriForFile(getContext(), authorities, file);
                                intent.setDataAndType(uri, "application/vnd.ms-excel");
                                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            }
                        }
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        startActivity(intent);

                    } else {
                        commonDialog.dismiss();
                        Toast.makeText(getContext(), "Unable To Generate Excel", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception ex) {
                    commonDialog.dismiss();
                }
            }
        } catch (Exception e) {
            commonDialog.dismiss();
        }

    }

    public void createCategoryWiseReportExcel(String fromDate, String toDate, ArrayList<CategoryWiseReportModel> reportList) {
        Workbook wb = null;
        String fileName = "";

        final CommonDialog commonDialog = new CommonDialog(getActivity(), "Loading", "Please Wait...");
        commonDialog.show();

        try {
            fileName = "Category_Wise_Report_" + fromDate + "_" + toDate + ".xls";

            wb = new HSSFWorkbook();
            Cell c = null;

            //------------------New Sheet-----------------
            Sheet sheet1 = null;
            sheet1 = wb.createSheet("Report");

            //---------------------Header Row--------------------
            Row companyTitleRow = sheet1.createRow(0);
            c = companyTitleRow.createCell(1);
            c.setCellValue("Happy Feast Co.");

            Row reportTitleRow = sheet1.createRow(1);
            c = reportTitleRow.createCell(1);
            c.setCellValue("REPORT : ");
            c = reportTitleRow.createCell(2);
            c.setCellValue("Category Wise Report");

            //-------------------Empty Row------------------
            Row emptyRow = sheet1.createRow(2);

            //----------------Date Row--------------
            Row dateRow = sheet1.createRow(3);

            c = dateRow.createCell(1);
            c.setCellValue("FROM DATE : ");

            c = dateRow.createCell(2);
            c.setCellValue("" + fromDate);

            c = dateRow.createCell(3);
            c.setCellValue("TO DATE : ");

            c = dateRow.createCell(4);
            c.setCellValue("" + toDate);

            //-------------------Empty Row----------------
            Row emptyRow3 = sheet1.createRow(4);

            //--------------Table Header Row--------------
            Row headerRow = sheet1.createRow(5);

            c = headerRow.createCell(0);
            c.setCellValue("NO.");

            c = headerRow.createCell(1);
            c.setCellValue("CATEGORY NAME");

            c = headerRow.createCell(2);
            c.setCellValue("QUANTITY");

            c = headerRow.createCell(2);
            c.setCellValue("TOTAL");

            c = headerRow.createCell(3);
            c.setCellValue("AMOUNT");


            float total = 0;

            //-----------------Table Data Row---------------------
            for (int i = 0; i < reportList.size(); i++) {

                total = total + reportList.get(i).getPayableAmt();

                Row newRow = sheet1.createRow((i + 6));

                c = newRow.createCell(0);
                c.setCellValue("" + (i + 1));

                c = newRow.createCell(1);
                c.setCellValue("" + reportList.get(i).getCatName());

                c = newRow.createCell(2);
                c.setCellValue("" + reportList.get(i).getQuantity());

                c = newRow.createCell(3);
                c.setCellValue("" + reportList.get(i).getTotal());

                c = newRow.createCell(4);
                c.setCellValue("" + reportList.get(i).getPayableAmt());


            }

            //--------------------Total Row-----------------------
            Row totalRow = sheet1.createRow(reportList.size() + 7);

            c = totalRow.createCell(0);
            c.setCellValue("");

            c = totalRow.createCell(1);
            c.setCellValue("");

            c = totalRow.createCell(2);
            c.setCellValue("TOTAL");

            c = totalRow.createCell(3);
            c.setCellValue("" + total);

            sheet1.setColumnWidth(0, (3 * 500));
            sheet1.setColumnWidth(1, (16 * 500));
            sheet1.setColumnWidth(2, (6 * 500));
            sheet1.setColumnWidth(3, (9 * 500));

            File file = new File(dir, fileName);
            FileOutputStream os = null;

            try {
                os = new FileOutputStream(file);
                wb.write(os);
                //Log.e("FileUtils", "Writing file" + file);
            } catch (IOException e) {
                commonDialog.dismiss();
                //Log.e("FileUtils", "Error writing " + file, e);
            } catch (Exception e) {
                commonDialog.dismiss();
                //Log.e("FileUtils", "Failed to save file", e);
            } finally {
                try {
                    if (null != os)
                        os.close();

                    if (file.exists()) {
                        commonDialog.dismiss();
                        //Log.e("EXCEL", "----------------------------------------------");
                        Intent intent = new Intent(Intent.ACTION_VIEW);

                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                            intent.setDataAndType(Uri.fromFile(file), "application/vnd.ms-excel");
                        } else {
                            if (file.exists()) {
                                String authorities = BuildConfig.APPLICATION_ID + ".provider";
                                Uri uri = FileProvider.getUriForFile(getContext(), authorities, file);
                                intent.setDataAndType(uri, "application/vnd.ms-excel");
                                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            }
                        }
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        startActivity(intent);

                    } else {
                        commonDialog.dismiss();
                        Toast.makeText(getContext(), "Unable To Generate Excel", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception ex) {
                    commonDialog.dismiss();
                }
            }
        } catch (Exception e) {
            commonDialog.dismiss();
        }

    }

    public void createItemHSNReportExcel(String fromDate, String toDate, ArrayList<ItemHSNCodeReportModel> reportList) {
        Workbook wb = null;
        String fileName = "";

        final CommonDialog commonDialog = new CommonDialog(getActivity(), "Loading", "Please Wait...");
        commonDialog.show();

        try {
            fileName = "Item_HSN_Report_" + fromDate + "_" + toDate + ".xls";

            wb = new HSSFWorkbook();
            Cell c = null;

            //------------------New Sheet-----------------
            Sheet sheet1 = null;
            sheet1 = wb.createSheet("Report");

            //---------------------Header Row--------------------
            Row companyTitleRow = sheet1.createRow(0);
            c = companyTitleRow.createCell(1);
            c.setCellValue("Happy Feast Co.");

            Row reportTitleRow = sheet1.createRow(1);
            c = reportTitleRow.createCell(1);
            c.setCellValue("REPORT : ");
            c = reportTitleRow.createCell(2);
            c.setCellValue("Item HSN Report");

            //-------------------Empty Row------------------
            Row emptyRow = sheet1.createRow(2);

            //----------------Date Row--------------
            Row dateRow = sheet1.createRow(3);

            c = dateRow.createCell(1);
            c.setCellValue("FROM DATE : ");

            c = dateRow.createCell(2);
            c.setCellValue("" + fromDate);

            c = dateRow.createCell(3);
            c.setCellValue("TO DATE : ");

            c = dateRow.createCell(4);
            c.setCellValue("" + toDate);

            //-------------------Empty Row----------------
            Row emptyRow3 = sheet1.createRow(4);

            //--------------Table Header Row--------------
            Row headerRow = sheet1.createRow(5);

            c = headerRow.createCell(0);
            c.setCellValue("NO.");

            c = headerRow.createCell(1);
            c.setCellValue("HSN CODE");

            c = headerRow.createCell(2);
            c.setCellValue("CGST");

            c = headerRow.createCell(3);
            c.setCellValue("SGST");

            c = headerRow.createCell(4);
            c.setCellValue("TOTAL TAX");

            c = headerRow.createCell(5);
            c.setCellValue("TAXABLE AMOUNT");

            float total = 0;

            //-----------------Table Data Row---------------------
            for (int i = 0; i < reportList.size(); i++) {

                total = total + reportList.get(i).getTaxableAmount();

                Row newRow = sheet1.createRow((i + 6));

                c = newRow.createCell(0);
                c.setCellValue("" + (i + 1));

                c = newRow.createCell(1);
                c.setCellValue("" + reportList.get(i).getHsnCode());

                c = newRow.createCell(2);
                c.setCellValue("" + reportList.get(i).getCgst());

                c = newRow.createCell(3);
                c.setCellValue("" + reportList.get(i).getSgst());

                c = newRow.createCell(4);
                c.setCellValue("" + reportList.get(i).getTotalTax());

                c = newRow.createCell(5);
                c.setCellValue("" + reportList.get(i).getTaxableAmount());

            }

            //--------------------Total Row-----------------------
            Row totalRow = sheet1.createRow(reportList.size() + 7);

            c = totalRow.createCell(0);
            c.setCellValue("");

            c = totalRow.createCell(1);
            c.setCellValue("");

            c = totalRow.createCell(2);
            c.setCellValue("");

            c = totalRow.createCell(3);
            c.setCellValue("");

            c = totalRow.createCell(4);
            c.setCellValue("TOTAL");

            c = totalRow.createCell(5);
            c.setCellValue("" + total);

            sheet1.setColumnWidth(0, (3 * 500));
            sheet1.setColumnWidth(1, (16 * 500));
            sheet1.setColumnWidth(2, (6 * 500));
            sheet1.setColumnWidth(3, (9 * 500));
            sheet1.setColumnWidth(4, (7 * 500));
            sheet1.setColumnWidth(5, (7 * 500));

            File file = new File(dir, fileName);
            FileOutputStream os = null;

            try {
                os = new FileOutputStream(file);
                wb.write(os);
                //Log.e("FileUtils", "Writing file" + file);
            } catch (IOException e) {
                commonDialog.dismiss();
                //Log.e("FileUtils", "Error writing " + file, e);
            } catch (Exception e) {
                commonDialog.dismiss();
                //Log.e("FileUtils", "Failed to save file", e);
            } finally {
                try {
                    if (null != os)
                        os.close();

                    if (file.exists()) {
                        commonDialog.dismiss();
                        //Log.e("EXCEL", "----------------------------------------------");
                        Intent intent = new Intent(Intent.ACTION_VIEW);

                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                            intent.setDataAndType(Uri.fromFile(file), "application/vnd.ms-excel");
                        } else {
                            if (file.exists()) {
                                String authorities = BuildConfig.APPLICATION_ID + ".provider";
                                Uri uri = FileProvider.getUriForFile(getContext(), authorities, file);
                                intent.setDataAndType(uri, "application/vnd.ms-excel");
                                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            }
                        }
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        startActivity(intent);

                    } else {
                        commonDialog.dismiss();
                        Toast.makeText(getContext(), "Unable To Generate Excel", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception ex) {
                    commonDialog.dismiss();
                }
            }
        } catch (Exception e) {
            commonDialog.dismiss();
        }

    }

    public void createBillWiseReportExcel(String fromDate, String toDate, ArrayList<BillWiseReportModel> reportList) {
        Workbook wb = null;
        String fileName = "";

        final CommonDialog commonDialog = new CommonDialog(getActivity(), "Loading", "Please Wait...");
        commonDialog.show();

        try {
            fileName = "Bill_Report_" + fromDate + "_" + toDate + ".xls";

            wb = new HSSFWorkbook();
            Cell c = null;

            //------------------New Sheet-----------------
            Sheet sheet1 = null;
            sheet1 = wb.createSheet("Report");

            //---------------------Header Row--------------------
            Row companyTitleRow = sheet1.createRow(0);
            c = companyTitleRow.createCell(1);
            c.setCellValue("Happy Feast Co.");

            Row reportTitleRow = sheet1.createRow(1);
            c = reportTitleRow.createCell(1);
            c.setCellValue("REPORT : ");
            c = reportTitleRow.createCell(2);
            c.setCellValue("Bill Report");

            //-------------------Empty Row------------------
            Row emptyRow = sheet1.createRow(2);

            //----------------Date Row--------------
            Row dateRow = sheet1.createRow(3);

            c = dateRow.createCell(1);
            c.setCellValue("FROM DATE : ");

            c = dateRow.createCell(2);
            c.setCellValue("" + fromDate);

            c = dateRow.createCell(3);
            c.setCellValue("TO DATE : ");

            c = dateRow.createCell(4);
            c.setCellValue("" + toDate);

            //-------------------Empty Row----------------
            Row emptyRow3 = sheet1.createRow(4);

            //--------------Table Header Row--------------
            Row headerRow = sheet1.createRow(5);

            c = headerRow.createCell(0);
            c.setCellValue("NO.");

            c = headerRow.createCell(1);
            c.setCellValue("BILL NO.");

            c = headerRow.createCell(2);
            c.setCellValue("DATE");

            c = headerRow.createCell(3);
            c.setCellValue("TOTAL TAX");

            c = headerRow.createCell(4);
            c.setCellValue("TAXABLE AMOUNT");

            c = headerRow.createCell(5);
            c.setCellValue("DISCOUNT");

            c = headerRow.createCell(6);
            c.setCellValue("GRAND TOTAL");

            c = headerRow.createCell(7);
            c.setCellValue("PAYABLE AMOUNT");

            float total = 0;

            //-----------------Table Data Row---------------------
            for (int i = 0; i < reportList.size(); i++) {

                total = total + reportList.get(i).getTaxableAmount();

                Row newRow = sheet1.createRow((i + 6));

                c = newRow.createCell(0);
                c.setCellValue("" + (i + 1));

                c = newRow.createCell(1);
                c.setCellValue("" + reportList.get(i).getBillNo());

                c = newRow.createCell(2);
                c.setCellValue("" + reportList.get(i).getBillDate());

                c = newRow.createCell(3);
                c.setCellValue("" + (reportList.get(i).getCgst() + reportList.get(i).getSgst()));

                c = newRow.createCell(4);
                c.setCellValue("" + reportList.get(i).getTaxableAmount());

                c = newRow.createCell(5);
                c.setCellValue("" + reportList.get(i).getDiscount());

                c = newRow.createCell(6);
                c.setCellValue("" + reportList.get(i).getGrandTotal());

                c = newRow.createCell(7);
                c.setCellValue("" + reportList.get(i).getPayableAmount());

            }

            //--------------------Total Row-----------------------
            Row totalRow = sheet1.createRow(reportList.size() + 7);

            c = totalRow.createCell(0);
            c.setCellValue("");

            c = totalRow.createCell(1);
            c.setCellValue("");

            c = totalRow.createCell(2);
            c.setCellValue("");

            c = totalRow.createCell(3);
            c.setCellValue("");

            c = totalRow.createCell(4);
            c.setCellValue("");

            c = totalRow.createCell(5);
            c.setCellValue("");

            c = totalRow.createCell(6);
            c.setCellValue("TOTAL");

            c = totalRow.createCell(7);
            c.setCellValue("" + total);

            sheet1.setColumnWidth(0, (3 * 500));
            sheet1.setColumnWidth(1, (16 * 500));
            sheet1.setColumnWidth(2, (6 * 500));
            sheet1.setColumnWidth(3, (9 * 500));
            sheet1.setColumnWidth(4, (9 * 500));
            sheet1.setColumnWidth(5, (9 * 500));
            sheet1.setColumnWidth(6, (9 * 500));
            sheet1.setColumnWidth(7, (9 * 500));

            File file = new File(dir, fileName);
            FileOutputStream os = null;

            try {
                os = new FileOutputStream(file);
                wb.write(os);
                //Log.e("FileUtils", "Writing file" + file);
            } catch (IOException e) {
                commonDialog.dismiss();
                //Log.e("FileUtils", "Error writing " + file, e);
            } catch (Exception e) {
                commonDialog.dismiss();
                //Log.e("FileUtils", "Failed to save file", e);
            } finally {
                try {
                    if (null != os)
                        os.close();

                    if (file.exists()) {
                        commonDialog.dismiss();
                        //Log.e("EXCEL", "----------------------------------------------");
                        Intent intent = new Intent(Intent.ACTION_VIEW);

                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                            intent.setDataAndType(Uri.fromFile(file), "application/vnd.ms-excel");
                        } else {
                            if (file.exists()) {
                                String authorities = BuildConfig.APPLICATION_ID + ".provider";
                                Uri uri = FileProvider.getUriForFile(getContext(), authorities, file);
                                intent.setDataAndType(uri, "application/vnd.ms-excel");
                                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            }
                        }
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        startActivity(intent);

                    } else {
                        commonDialog.dismiss();
                        Toast.makeText(getContext(), "Unable To Generate Excel", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception ex) {
                    commonDialog.dismiss();
                }
            }
        } catch (Exception e) {
            commonDialog.dismiss();
        }

    }

    public void createDateWiseBillReportExcel(String fromDate, String toDate, ArrayList<DateWiseReportModel> reportList) {
        Workbook wb = null;
        String fileName = "";

        final CommonDialog commonDialog = new CommonDialog(getActivity(), "Loading", "Please Wait...");
        commonDialog.show();

        try {
            fileName = "Date_Wise_Bill_Report_" + fromDate + "_" + toDate + ".xls";

            wb = new HSSFWorkbook();
            Cell c = null;

            //------------------New Sheet-----------------
            Sheet sheet1 = null;
            sheet1 = wb.createSheet("Report");

            //---------------------Header Row--------------------
            Row companyTitleRow = sheet1.createRow(0);
            c = companyTitleRow.createCell(1);
            c.setCellValue("Happy Feast Co.");

            Row reportTitleRow = sheet1.createRow(1);
            c = reportTitleRow.createCell(1);
            c.setCellValue("REPORT : ");
            c = reportTitleRow.createCell(2);
            c.setCellValue("Date Wise Bill Report");

            //-------------------Empty Row------------------
            Row emptyRow = sheet1.createRow(2);

            //----------------Date Row--------------
            Row dateRow = sheet1.createRow(3);

            c = dateRow.createCell(1);
            c.setCellValue("FROM DATE : ");

            c = dateRow.createCell(2);
            c.setCellValue("" + fromDate);

            c = dateRow.createCell(3);
            c.setCellValue("TO DATE : ");

            c = dateRow.createCell(4);
            c.setCellValue("" + toDate);

            //-------------------Empty Row----------------
            Row emptyRow3 = sheet1.createRow(4);

            //--------------Table Header Row--------------
            Row headerRow = sheet1.createRow(5);

            c = headerRow.createCell(0);
            c.setCellValue("NO.");

            c = headerRow.createCell(1);
            c.setCellValue("BILL DATE");

            c = headerRow.createCell(2);
            c.setCellValue("TOTAL");

            c = headerRow.createCell(3);
            c.setCellValue("PAYABLE AMOUNT");

            float total = 0;

            //-----------------Table Data Row---------------------
            for (int i = 0; i < reportList.size(); i++) {

                total = total + reportList.get(i).getPayableAmount();

                Row newRow = sheet1.createRow((i + 6));

                c = newRow.createCell(0);
                c.setCellValue("" + (i + 1));

                c = newRow.createCell(1);
                c.setCellValue("" + reportList.get(i).getBillDate());

                c = newRow.createCell(2);
                c.setCellValue("" + reportList.get(i).getTotal());

                c = newRow.createCell(3);
                c.setCellValue("" + reportList.get(i).getPayableAmount());


            }

            //--------------------Total Row-----------------------
            Row totalRow = sheet1.createRow(reportList.size() + 7);

            c = totalRow.createCell(0);
            c.setCellValue("");

            c = totalRow.createCell(1);
            c.setCellValue("");

            c = totalRow.createCell(2);
            c.setCellValue("TOTAL");

            c = totalRow.createCell(3);
            c.setCellValue("" + total);

            sheet1.setColumnWidth(0, (3 * 500));
            sheet1.setColumnWidth(1, (16 * 500));
            sheet1.setColumnWidth(2, (9 * 500));
            sheet1.setColumnWidth(3, (9 * 500));


            File file = new File(dir, fileName);
            FileOutputStream os = null;

            try {
                os = new FileOutputStream(file);
                wb.write(os);
                //Log.e("FileUtils", "Writing file" + file);
            } catch (IOException e) {
                commonDialog.dismiss();
                //Log.e("FileUtils", "Error writing " + file, e);
            } catch (Exception e) {
                commonDialog.dismiss();
                //Log.e("FileUtils", "Failed to save file", e);
            } finally {
                try {
                    if (null != os)
                        os.close();

                    if (file.exists()) {
                        commonDialog.dismiss();
                        //Log.e("EXCEL", "----------------------------------------------");
                        Intent intent = new Intent(Intent.ACTION_VIEW);

                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                            intent.setDataAndType(Uri.fromFile(file), "application/vnd.ms-excel");
                        } else {
                            if (file.exists()) {
                                String authorities = BuildConfig.APPLICATION_ID + ".provider";
                                Uri uri = FileProvider.getUriForFile(getContext(), authorities, file);
                                intent.setDataAndType(uri, "application/vnd.ms-excel");
                                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            }
                        }
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        startActivity(intent);

                    } else {
                        commonDialog.dismiss();
                        Toast.makeText(getContext(), "Unable To Generate Excel", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception ex) {
                    commonDialog.dismiss();
                }
            }
        } catch (Exception e) {
            commonDialog.dismiss();
        }

    }

    public void createMonthWiseBillReportExcel(String monthYear, ArrayList<MonthWiseBillReportModel> reportList) {
        Workbook wb = null;
        String fileName = "";

        final CommonDialog commonDialog = new CommonDialog(getActivity(), "Loading", "Please Wait...");
        commonDialog.show();

        try {
            fileName = "Month_Wise_Bill_Report_" + fromDate + "_" + toDate + ".xls";

            wb = new HSSFWorkbook();
            Cell c = null;

            //------------------New Sheet-----------------
            Sheet sheet1 = null;
            sheet1 = wb.createSheet("Report");

            //---------------------Header Row--------------------
            Row companyTitleRow = sheet1.createRow(0);
            c = companyTitleRow.createCell(1);
            c.setCellValue("Happy Feast Co.");

            Row reportTitleRow = sheet1.createRow(1);
            c = reportTitleRow.createCell(1);
            c.setCellValue("REPORT : ");
            c = reportTitleRow.createCell(2);
            c.setCellValue("Month Wise Bill Report");

            //-------------------Empty Row------------------
            Row emptyRow = sheet1.createRow(2);

            //----------------Date Row--------------
            Row dateRow = sheet1.createRow(3);

            c = dateRow.createCell(1);
            c.setCellValue("MONTH : ");

            c = dateRow.createCell(2);
            c.setCellValue("" + reportList.get(0).getMonth());

            c = dateRow.createCell(3);
            c.setCellValue("YEAR : ");

            c = dateRow.createCell(4);
            c.setCellValue("" + monthYear);

            //-------------------Empty Row----------------
            Row emptyRow3 = sheet1.createRow(4);

            //--------------Table Header Row--------------
            Row headerRow = sheet1.createRow(5);

            c = headerRow.createCell(0);
            c.setCellValue("NO.");

            c = headerRow.createCell(1);
            c.setCellValue("MONTH");

            c = headerRow.createCell(2);
            c.setCellValue("GRAND TOTAL");

            c = headerRow.createCell(3);
            c.setCellValue("TAXABLE AMOUNT");

            c = headerRow.createCell(4);
            c.setCellValue("TAX AMOUNT");

            c = headerRow.createCell(5);
            c.setCellValue("PAYABLE AMOUNT");

            float total = 0;

            //-----------------Table Data Row---------------------
            for (int i = 0; i < reportList.size(); i++) {

                total = total + reportList.get(i).getPayableAmount();

                Row newRow = sheet1.createRow((i + 6));

                c = newRow.createCell(0);
                c.setCellValue("" + (i + 1));

                c = newRow.createCell(1);
                c.setCellValue("" + reportList.get(i).getMonth());

                c = newRow.createCell(2);
                c.setCellValue("" + reportList.get(i).getGrandTotal());

                c = newRow.createCell(3);
                c.setCellValue("" + reportList.get(i).getTaxableAmount());

                c = newRow.createCell(4);
                c.setCellValue("" + reportList.get(i).getTaxAmt());

                c = newRow.createCell(5);
                c.setCellValue("" + reportList.get(i).getPayableAmount());


            }

            //--------------------Total Row-----------------------
            Row totalRow = sheet1.createRow(reportList.size() + 7);

            c = totalRow.createCell(0);
            c.setCellValue("");

            c = totalRow.createCell(1);
            c.setCellValue("");

            c = totalRow.createCell(2);
            c.setCellValue("");

            c = totalRow.createCell(3);
            c.setCellValue("");

            c = totalRow.createCell(4);
            c.setCellValue("TOTAL");

            c = totalRow.createCell(5);
            c.setCellValue("" + total);

            sheet1.setColumnWidth(0, (3 * 500));
            sheet1.setColumnWidth(1, (16 * 500));
            sheet1.setColumnWidth(2, (9 * 500));
            sheet1.setColumnWidth(3, (9 * 500));
            sheet1.setColumnWidth(4, (9 * 500));
            sheet1.setColumnWidth(5, (9 * 500));


            File file = new File(dir, fileName);
            FileOutputStream os = null;

            try {
                os = new FileOutputStream(file);
                wb.write(os);
                //Log.e("FileUtils", "Writing file" + file);
            } catch (IOException e) {
                commonDialog.dismiss();
                //Log.e("FileUtils", "Error writing " + file, e);
            } catch (Exception e) {
                commonDialog.dismiss();
                //Log.e("FileUtils", "Failed to save file", e);
            } finally {
                try {
                    if (null != os)
                        os.close();

                    if (file.exists()) {
                        commonDialog.dismiss();
                        //Log.e("EXCEL", "----------------------------------------------");
                        Intent intent = new Intent(Intent.ACTION_VIEW);

                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                            intent.setDataAndType(Uri.fromFile(file), "application/vnd.ms-excel");
                        } else {
                            if (file.exists()) {
                                String authorities = BuildConfig.APPLICATION_ID + ".provider";
                                Uri uri = FileProvider.getUriForFile(getContext(), authorities, file);
                                intent.setDataAndType(uri, "application/vnd.ms-excel");
                                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            }
                        }
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        startActivity(intent);

                    } else {
                        commonDialog.dismiss();
                        Toast.makeText(getContext(), "Unable To Generate Excel", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception ex) {
                    commonDialog.dismiss();
                }
            }
        } catch (Exception e) {
            commonDialog.dismiss();
        }

    }

    public void createDateWiseTaxReportExcel(String fromDate, String toDate, ArrayList<BillWiseTaxReportModel> reportList) {
        Workbook wb = null;
        String fileName = "";

        final CommonDialog commonDialog = new CommonDialog(getActivity(), "Loading", "Please Wait...");
        commonDialog.show();

        try {
            fileName = "Date_Wise_Tax_Report_" + fromDate + "_" + toDate + ".xls";

            wb = new HSSFWorkbook();
            Cell c = null;

            //------------------New Sheet-----------------
            Sheet sheet1 = null;
            sheet1 = wb.createSheet("Report");

            //---------------------Header Row--------------------
            Row companyTitleRow = sheet1.createRow(0);
            c = companyTitleRow.createCell(1);
            c.setCellValue("Happy Feast Co.");

            Row reportTitleRow = sheet1.createRow(1);
            c = reportTitleRow.createCell(1);
            c.setCellValue("REPORT : ");
            c = reportTitleRow.createCell(2);
            c.setCellValue("Date Wise Tax Report");

            //-------------------Empty Row------------------
            Row emptyRow = sheet1.createRow(2);

            //----------------Date Row--------------
            Row dateRow = sheet1.createRow(3);

            c = dateRow.createCell(1);
            c.setCellValue("FROM DATE : ");

            c = dateRow.createCell(2);
            c.setCellValue("" + fromDate);

            c = dateRow.createCell(3);
            c.setCellValue("TO DATE : ");

            c = dateRow.createCell(4);
            c.setCellValue("" + toDate);

            //-------------------Empty Row----------------
            Row emptyRow3 = sheet1.createRow(4);

            //--------------Table Header Row--------------
            Row headerRow = sheet1.createRow(5);

            c = headerRow.createCell(0);
            c.setCellValue("NO.");

            c = headerRow.createCell(1);
            c.setCellValue("DATE");

            c = headerRow.createCell(2);
            c.setCellValue("TAX");

            c = headerRow.createCell(3);
            c.setCellValue("TOTAL TAX");

            c = headerRow.createCell(4);
            c.setCellValue("TAXABLE AMOUNT");

            float total = 0;

            //-----------------Table Data Row---------------------
            for (int i = 0; i < reportList.size(); i++) {

                total = total + reportList.get(i).getTaxableAmount();

                Row newRow = sheet1.createRow((i + 6));

                c = newRow.createCell(0);
                c.setCellValue("" + (i + 1));

                c = newRow.createCell(1);
                c.setCellValue("" + reportList.get(i).getBillDate());

                c = newRow.createCell(2);
                c.setCellValue("" + reportList.get(i).getTax());

                c = newRow.createCell(3);
                c.setCellValue("" + reportList.get(i).getTotalTax());

                c = newRow.createCell(4);
                c.setCellValue("" + reportList.get(i).getTaxableAmount());

            }

            //--------------------Total Row-----------------------
            Row totalRow = sheet1.createRow(reportList.size() + 7);

            c = totalRow.createCell(0);
            c.setCellValue("");

            c = totalRow.createCell(1);
            c.setCellValue("");

            c = totalRow.createCell(2);
            c.setCellValue("");

            c = totalRow.createCell(3);
            c.setCellValue("TOTAL");

            c = totalRow.createCell(4);
            c.setCellValue("" + total);

            sheet1.setColumnWidth(0, (3 * 500));
            sheet1.setColumnWidth(1, (16 * 500));
            sheet1.setColumnWidth(2, (9 * 500));
            sheet1.setColumnWidth(3, (9 * 500));
            sheet1.setColumnWidth(4, (9 * 500));

            File file = new File(dir, fileName);
            FileOutputStream os = null;

            try {
                os = new FileOutputStream(file);
                wb.write(os);
                //Log.e("FileUtils", "Writing file" + file);
            } catch (IOException e) {
                commonDialog.dismiss();
                //Log.e("FileUtils", "Error writing " + file, e);
            } catch (Exception e) {
                commonDialog.dismiss();
                //Log.e("FileUtils", "Failed to save file", e);
            } finally {
                try {
                    if (null != os)
                        os.close();

                    if (file.exists()) {
                        commonDialog.dismiss();
                        //Log.e("EXCEL", "----------------------------------------------");
                        Intent intent = new Intent(Intent.ACTION_VIEW);

                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                            intent.setDataAndType(Uri.fromFile(file), "application/vnd.ms-excel");
                        } else {
                            if (file.exists()) {
                                String authorities = BuildConfig.APPLICATION_ID + ".provider";
                                Uri uri = FileProvider.getUriForFile(getContext(), authorities, file);
                                intent.setDataAndType(uri, "application/vnd.ms-excel");
                                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            }
                        }
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        startActivity(intent);

                    } else {
                        commonDialog.dismiss();
                        Toast.makeText(getContext(), "Unable To Generate Excel", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception ex) {
                    commonDialog.dismiss();
                }
            }
        } catch (Exception e) {
            commonDialog.dismiss();
        }

    }

    public void createBillWiseTaxReportExcel(String fromDate, String toDate, ArrayList<BillWiseTaxReportModel> reportList) {
        Workbook wb = null;
        String fileName = "";

        final CommonDialog commonDialog = new CommonDialog(getActivity(), "Loading", "Please Wait...");
        commonDialog.show();

        try {
            fileName = "Bill_Wise_Tax_Report_" + fromDate + "_" + toDate + ".xls";

            wb = new HSSFWorkbook();
            Cell c = null;

            //------------------New Sheet-----------------
            Sheet sheet1 = null;
            sheet1 = wb.createSheet("Report");

            //---------------------Header Row--------------------
            Row companyTitleRow = sheet1.createRow(0);
            c = companyTitleRow.createCell(1);
            c.setCellValue("Happy Feast Co.");

            Row reportTitleRow = sheet1.createRow(1);
            c = reportTitleRow.createCell(1);
            c.setCellValue("REPORT : ");
            c = reportTitleRow.createCell(2);
            c.setCellValue("Bill Wise Tax Report");

            //-------------------Empty Row------------------
            Row emptyRow = sheet1.createRow(2);

            //----------------Date Row--------------
            Row dateRow = sheet1.createRow(3);

            c = dateRow.createCell(1);
            c.setCellValue("FROM DATE : ");

            c = dateRow.createCell(2);
            c.setCellValue("" + fromDate);

            c = dateRow.createCell(3);
            c.setCellValue("TO DATE : ");

            c = dateRow.createCell(4);
            c.setCellValue("" + toDate);

            //-------------------Empty Row----------------
            Row emptyRow3 = sheet1.createRow(4);

            //--------------Table Header Row--------------
            Row headerRow = sheet1.createRow(5);

            c = headerRow.createCell(0);
            c.setCellValue("NO.");

            c = headerRow.createCell(1);
            c.setCellValue("BILL NO");

            c = headerRow.createCell(2);
            c.setCellValue("DATE");

            c = headerRow.createCell(3);
            c.setCellValue("TAX");

            c = headerRow.createCell(4);
            c.setCellValue("TOTAL TAX");

            c = headerRow.createCell(5);
            c.setCellValue("TAXABLE AMOUNT");

            float total = 0;

            //-----------------Table Data Row---------------------
            for (int i = 0; i < reportList.size(); i++) {

                total = total + reportList.get(i).getTaxableAmount();

                Row newRow = sheet1.createRow((i + 6));

                c = newRow.createCell(0);
                c.setCellValue("" + (i + 1));

                c = newRow.createCell(1);
                c.setCellValue("" + reportList.get(i).getBillNo());

                c = newRow.createCell(2);
                c.setCellValue("" + reportList.get(i).getBillDate());

                c = newRow.createCell(3);
                c.setCellValue("" + reportList.get(i).getTax());

                c = newRow.createCell(4);
                c.setCellValue("" + reportList.get(i).getTotalTax());

                c = newRow.createCell(5);
                c.setCellValue("" + reportList.get(i).getTaxableAmount());

            }

            //--------------------Total Row-----------------------
            Row totalRow = sheet1.createRow(reportList.size() + 7);

            c = totalRow.createCell(0);
            c.setCellValue("");

            c = totalRow.createCell(1);
            c.setCellValue("");

            c = totalRow.createCell(2);
            c.setCellValue("");

            c = totalRow.createCell(3);
            c.setCellValue("");

            c = totalRow.createCell(4);
            c.setCellValue("TOTAL");

            c = totalRow.createCell(5);
            c.setCellValue("" + total);

            sheet1.setColumnWidth(0, (3 * 500));
            sheet1.setColumnWidth(1, (16 * 500));
            sheet1.setColumnWidth(2, (9 * 500));
            sheet1.setColumnWidth(3, (9 * 500));
            sheet1.setColumnWidth(4, (9 * 500));
            sheet1.setColumnWidth(5, (9 * 500));

            File file = new File(dir, fileName);
            FileOutputStream os = null;

            try {
                os = new FileOutputStream(file);
                wb.write(os);
                //Log.e("FileUtils", "Writing file" + file);
            } catch (IOException e) {
                commonDialog.dismiss();
                //Log.e("FileUtils", "Error writing " + file, e);
            } catch (Exception e) {
                commonDialog.dismiss();
                //Log.e("FileUtils", "Failed to save file", e);
            } finally {
                try {
                    if (null != os)
                        os.close();

                    if (file.exists()) {
                        commonDialog.dismiss();
                        //Log.e("EXCEL", "----------------------------------------------");
                        Intent intent = new Intent(Intent.ACTION_VIEW);

                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                            intent.setDataAndType(Uri.fromFile(file), "application/vnd.ms-excel");
                        } else {
                            if (file.exists()) {
                                String authorities = BuildConfig.APPLICATION_ID + ".provider";
                                Uri uri = FileProvider.getUriForFile(getContext(), authorities, file);
                                intent.setDataAndType(uri, "application/vnd.ms-excel");
                                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            }
                        }
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        startActivity(intent);

                    } else {
                        commonDialog.dismiss();
                        Toast.makeText(getContext(), "Unable To Generate Excel", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception ex) {
                    commonDialog.dismiss();
                }
            }
        } catch (Exception e) {
            commonDialog.dismiss();
        }

    }

    public void createTaxLabReportExcel(String fromDate, String toDate, ArrayList<TaxLabWiseReportModel> reportList) {
        Workbook wb = null;
        String fileName = "";

        final CommonDialog commonDialog = new CommonDialog(getActivity(), "Loading", "Please Wait...");
        commonDialog.show();

        try {
            fileName = "Tax_Lab_Report_" + fromDate + "_" + toDate + ".xls";

            wb = new HSSFWorkbook();
            Cell c = null;

            //------------------New Sheet-----------------
            Sheet sheet1 = null;
            sheet1 = wb.createSheet("Report");

            //---------------------Header Row--------------------
            Row companyTitleRow = sheet1.createRow(0);
            c = companyTitleRow.createCell(1);
            c.setCellValue("Happy Feast Co.");

            Row reportTitleRow = sheet1.createRow(1);
            c = reportTitleRow.createCell(1);
            c.setCellValue("REPORT : ");
            c = reportTitleRow.createCell(2);
            c.setCellValue("Tax Lab Report");

            //-------------------Empty Row------------------
            Row emptyRow = sheet1.createRow(2);

            //----------------Date Row--------------
            Row dateRow = sheet1.createRow(3);

            c = dateRow.createCell(1);
            c.setCellValue("FROM DATE : ");

            c = dateRow.createCell(2);
            c.setCellValue("" + fromDate);

            c = dateRow.createCell(3);
            c.setCellValue("TO DATE : ");

            c = dateRow.createCell(4);
            c.setCellValue("" + toDate);

            //-------------------Empty Row----------------
            Row emptyRow3 = sheet1.createRow(4);

            //--------------Table Header Row--------------
            Row headerRow = sheet1.createRow(5);

            c = headerRow.createCell(0);
            c.setCellValue("NO.");

            c = headerRow.createCell(1);
            c.setCellValue("TAX");

            c = headerRow.createCell(2);
            c.setCellValue("TOTAL TAX");

            c = headerRow.createCell(3);
            c.setCellValue("TAXABLE AMOUNT");

            float total = 0;

            //-----------------Table Data Row---------------------
            for (int i = 0; i < reportList.size(); i++) {

                total = total + reportList.get(i).getTaxableAmount();

                Row newRow = sheet1.createRow((i + 6));

                c = newRow.createCell(0);
                c.setCellValue("" + (i + 1));

                c = newRow.createCell(1);
                c.setCellValue("" + reportList.get(i).getTax());

                c = newRow.createCell(2);
                c.setCellValue("" + reportList.get(i).getTotalTax());

                c = newRow.createCell(3);
                c.setCellValue("" + reportList.get(i).getTaxableAmount());

            }

            //--------------------Total Row-----------------------
            Row totalRow = sheet1.createRow(reportList.size() + 7);

            c = totalRow.createCell(0);
            c.setCellValue("");

            c = totalRow.createCell(1);
            c.setCellValue("");

            c = totalRow.createCell(2);
            c.setCellValue("TOTAL");

            c = totalRow.createCell(3);
            c.setCellValue("" + total);

            sheet1.setColumnWidth(0, (3 * 500));
            sheet1.setColumnWidth(1, (9 * 500));
            sheet1.setColumnWidth(2, (9 * 500));
            sheet1.setColumnWidth(3, (9 * 500));


            File file = new File(dir, fileName);
            FileOutputStream os = null;

            try {
                os = new FileOutputStream(file);
                wb.write(os);
                //Log.e("FileUtils", "Writing file" + file);
            } catch (IOException e) {
                commonDialog.dismiss();
                //Log.e("FileUtils", "Error writing " + file, e);
            } catch (Exception e) {
                commonDialog.dismiss();
                //Log.e("FileUtils", "Failed to save file", e);
            } finally {
                try {
                    if (null != os)
                        os.close();

                    if (file.exists()) {
                        commonDialog.dismiss();
                        //Log.e("EXCEL", "----------------------------------------------");
                        Intent intent = new Intent(Intent.ACTION_VIEW);

                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                            intent.setDataAndType(Uri.fromFile(file), "application/vnd.ms-excel");
                        } else {
                            if (file.exists()) {
                                String authorities = BuildConfig.APPLICATION_ID + ".provider";
                                Uri uri = FileProvider.getUriForFile(getContext(), authorities, file);
                                intent.setDataAndType(uri, "application/vnd.ms-excel");
                                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            }
                        }
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        startActivity(intent);

                    } else {
                        commonDialog.dismiss();
                        Toast.makeText(getContext(), "Unable To Generate Excel", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception ex) {
                    commonDialog.dismiss();
                }
            }
        } catch (Exception e) {
            commonDialog.dismiss();
        }

    }

    public void createTableReportExcel(String fromDate, String toDate, ArrayList<TableWiseReportModel> reportList) {
        Workbook wb = null;
        String fileName = "";

        final CommonDialog commonDialog = new CommonDialog(getActivity(), "Loading", "Please Wait...");
        commonDialog.show();

        try {
            fileName = "Table_Report_" + fromDate + "_" + toDate + ".xls";

            wb = new HSSFWorkbook();
            Cell c = null;

            //------------------New Sheet-----------------
            Sheet sheet1 = null;
            sheet1 = wb.createSheet("Report");

            //---------------------Header Row--------------------
            Row companyTitleRow = sheet1.createRow(0);
            c = companyTitleRow.createCell(1);
            c.setCellValue("Happy Feast Co.");

            Row reportTitleRow = sheet1.createRow(1);
            c = reportTitleRow.createCell(1);
            c.setCellValue("REPORT : ");
            c = reportTitleRow.createCell(2);
            c.setCellValue("Table Report");

            //-------------------Empty Row------------------
            Row emptyRow = sheet1.createRow(2);

            //----------------Date Row--------------
            Row dateRow = sheet1.createRow(3);

            c = dateRow.createCell(1);
            c.setCellValue("FROM DATE : ");

            c = dateRow.createCell(2);
            c.setCellValue("" + fromDate);

            c = dateRow.createCell(3);
            c.setCellValue("TO DATE : ");

            c = dateRow.createCell(4);
            c.setCellValue("" + toDate);

            //-------------------Empty Row----------------
            Row emptyRow3 = sheet1.createRow(4);

            //--------------Table Header Row--------------
            Row headerRow = sheet1.createRow(5);

            c = headerRow.createCell(0);
            c.setCellValue("NO.");

            c = headerRow.createCell(1);
            c.setCellValue("TABLE NO");

            c = headerRow.createCell(2);
            c.setCellValue("TABLE NAME");

            c = headerRow.createCell(3);
            c.setCellValue("TOTAL");

            c = headerRow.createCell(4);
            c.setCellValue("PAYABLE AMOUNT");

            float total = 0;

            //-----------------Table Data Row---------------------
            for (int i = 0; i < reportList.size(); i++) {

                total = total + reportList.get(i).getPayableAmount();

                Row newRow = sheet1.createRow((i + 6));

                c = newRow.createCell(0);
                c.setCellValue("" + (i + 1));

                c = newRow.createCell(1);
                c.setCellValue("" + reportList.get(i).getTableNo());

                c = newRow.createCell(2);
                c.setCellValue("" + reportList.get(i).getTableName());

                c = newRow.createCell(3);
                c.setCellValue("" + reportList.get(i).getTotal());

                c = newRow.createCell(4);
                c.setCellValue("" + reportList.get(i).getPayableAmount());

            }

            //--------------------Total Row-----------------------
            Row totalRow = sheet1.createRow(reportList.size() + 7);

            c = totalRow.createCell(0);
            c.setCellValue("");

            c = totalRow.createCell(1);
            c.setCellValue("");

            c = totalRow.createCell(2);
            c.setCellValue("");

            c = totalRow.createCell(3);
            c.setCellValue("TOTAL");

            c = totalRow.createCell(4);
            c.setCellValue("" + total);

            sheet1.setColumnWidth(0, (3 * 500));
            sheet1.setColumnWidth(1, (9 * 500));
            sheet1.setColumnWidth(2, (9 * 500));
            sheet1.setColumnWidth(3, (9 * 500));
            sheet1.setColumnWidth(4, (10 * 500));


            File file = new File(dir, fileName);
            FileOutputStream os = null;

            try {
                os = new FileOutputStream(file);
                wb.write(os);
                //Log.e("FileUtils", "Writing file" + file);
            } catch (IOException e) {
                commonDialog.dismiss();
                //Log.e("FileUtils", "Error writing " + file, e);
            } catch (Exception e) {
                commonDialog.dismiss();
                //Log.e("FileUtils", "Failed to save file", e);
            } finally {
                try {
                    if (null != os)
                        os.close();

                    if (file.exists()) {
                        commonDialog.dismiss();
                        //Log.e("EXCEL", "----------------------------------------------");
                        Intent intent = new Intent(Intent.ACTION_VIEW);

                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                            intent.setDataAndType(Uri.fromFile(file), "application/vnd.ms-excel");
                        } else {
                            if (file.exists()) {
                                String authorities = BuildConfig.APPLICATION_ID + ".provider";
                                Uri uri = FileProvider.getUriForFile(getContext(), authorities, file);
                                intent.setDataAndType(uri, "application/vnd.ms-excel");
                                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            }
                        }
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        startActivity(intent);

                    } else {
                        commonDialog.dismiss();
                        Toast.makeText(getContext(), "Unable To Generate Excel", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception ex) {
                    commonDialog.dismiss();
                }
            }
        } catch (Exception e) {
            commonDialog.dismiss();
        }

    }

    public void createOrderCancelReportExcel(String fromDate, String toDate, ArrayList<OrderCancelReportModel> reportList) {
        Workbook wb = null;
        String fileName = "";

        final CommonDialog commonDialog = new CommonDialog(getActivity(), "Loading", "Please Wait...");
        commonDialog.show();

        try {
            fileName = "Order_Cancel_Report_" + fromDate + "_" + toDate + ".xls";

            wb = new HSSFWorkbook();
            Cell c = null;

            //------------------New Sheet-----------------
            Sheet sheet1 = null;
            sheet1 = wb.createSheet("Report");

            //---------------------Header Row--------------------
            Row companyTitleRow = sheet1.createRow(0);
            c = companyTitleRow.createCell(1);
            c.setCellValue("Happy Feast Co.");

            Row reportTitleRow = sheet1.createRow(1);
            c = reportTitleRow.createCell(1);
            c.setCellValue("REPORT : ");
            c = reportTitleRow.createCell(2);
            c.setCellValue("Order Cancellation Report");

            //-------------------Empty Row------------------
            Row emptyRow = sheet1.createRow(2);

            //----------------Date Row--------------
            Row dateRow = sheet1.createRow(3);

            c = dateRow.createCell(1);
            c.setCellValue("FROM DATE : ");

            c = dateRow.createCell(2);
            c.setCellValue("" + fromDate);

            c = dateRow.createCell(3);
            c.setCellValue("TO DATE : ");

            c = dateRow.createCell(4);
            c.setCellValue("" + toDate);

            //-------------------Empty Row----------------
            Row emptyRow3 = sheet1.createRow(4);

            //--------------Table Header Row--------------
            Row headerRow = sheet1.createRow(5);

            c = headerRow.createCell(0);
            c.setCellValue("NO.");

            c = headerRow.createCell(1);
            c.setCellValue("ORDER ID");

            c = headerRow.createCell(2);
            c.setCellValue("ITEM NAME");

            c = headerRow.createCell(3);
            c.setCellValue("QUANTITY");

            c = headerRow.createCell(4);
            c.setCellValue("STATUS");

            c = headerRow.createCell(5);
            c.setCellValue("REMARK");

            //-----------------Table Data Row---------------------
            for (int i = 0; i < reportList.size(); i++) {

                Row newRow = sheet1.createRow((i + 6));

                c = newRow.createCell(0);
                c.setCellValue("" + (i + 1));

                c = newRow.createCell(1);
                c.setCellValue("" + reportList.get(i).getOrderId());

                c = newRow.createCell(2);
                c.setCellValue("" + reportList.get(i).getItemName());

                c = newRow.createCell(3);
                c.setCellValue("" + reportList.get(i).getQuantity());

                c = newRow.createCell(4);
                c.setCellValue("" + reportList.get(i).getStatus());

                c = newRow.createCell(5);
                c.setCellValue("" + reportList.get(i).getRemark());

            }

            sheet1.setColumnWidth(0, (3 * 500));
            sheet1.setColumnWidth(1, (6 * 500));
            sheet1.setColumnWidth(2, (12 * 500));
            sheet1.setColumnWidth(3, (6 * 500));
            sheet1.setColumnWidth(4, (10 * 500));
            sheet1.setColumnWidth(5, (12 * 500));


            File file = new File(dir, fileName);
            FileOutputStream os = null;

            try {
                os = new FileOutputStream(file);
                wb.write(os);
                //Log.e("FileUtils", "Writing file" + file);
            } catch (IOException e) {
                commonDialog.dismiss();
                //Log.e("FileUtils", "Error writing " + file, e);
            } catch (Exception e) {
                commonDialog.dismiss();
                //Log.e("FileUtils", "Failed to save file", e);
            } finally {
                try {
                    if (null != os)
                        os.close();

                    if (file.exists()) {
                        commonDialog.dismiss();
                        //Log.e("EXCEL", "----------------------------------------------");
                        Intent intent = new Intent(Intent.ACTION_VIEW);

                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                            intent.setDataAndType(Uri.fromFile(file), "application/vnd.ms-excel");
                        } else {
                            if (file.exists()) {
                                String authorities = BuildConfig.APPLICATION_ID + ".provider";
                                Uri uri = FileProvider.getUriForFile(getContext(), authorities, file);
                                intent.setDataAndType(uri, "application/vnd.ms-excel");
                                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            }
                        }
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        startActivity(intent);

                    } else {
                        commonDialog.dismiss();
                        Toast.makeText(getContext(), "Unable To Generate Excel", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception ex) {
                    commonDialog.dismiss();
                }
            }
        } catch (Exception e) {
            commonDialog.dismiss();
        }

    }

    public void createItemCancelReportExcel(String fromDate, String toDate, ArrayList<ItemCancelReportModel> reportList) {
        Workbook wb = null;
        String fileName = "";

        final CommonDialog commonDialog = new CommonDialog(getActivity(), "Loading", "Please Wait...");
        commonDialog.show();

        try {
            fileName = "Item_Cancel_Report_" + fromDate + "_" + toDate + ".xls";

            wb = new HSSFWorkbook();
            Cell c = null;

            //------------------New Sheet-----------------
            Sheet sheet1 = null;
            sheet1 = wb.createSheet("Report");

            //---------------------Header Row--------------------
            Row companyTitleRow = sheet1.createRow(0);
            c = companyTitleRow.createCell(1);
            c.setCellValue("Happy Feast Co.");

            Row reportTitleRow = sheet1.createRow(1);
            c = reportTitleRow.createCell(1);
            c.setCellValue("REPORT : ");
            c = reportTitleRow.createCell(2);
            c.setCellValue("Item Cancellation Report");

            //-------------------Empty Row------------------
            Row emptyRow = sheet1.createRow(2);

            //----------------Date Row--------------
            Row dateRow = sheet1.createRow(3);

            c = dateRow.createCell(1);
            c.setCellValue("FROM DATE : ");

            c = dateRow.createCell(2);
            c.setCellValue("" + fromDate);

            c = dateRow.createCell(3);
            c.setCellValue("TO DATE : ");

            c = dateRow.createCell(4);
            c.setCellValue("" + toDate);

            //-------------------Empty Row----------------
            Row emptyRow3 = sheet1.createRow(4);

            //--------------Table Header Row--------------
            Row headerRow = sheet1.createRow(5);

            c = headerRow.createCell(0);
            c.setCellValue("NO.");

            c = headerRow.createCell(1);
            c.setCellValue("ITEM NAME");

            c = headerRow.createCell(2);
            c.setCellValue("CHARGEABLE");

            c = headerRow.createCell(3);
            c.setCellValue("NC 1");

            c = headerRow.createCell(4);
            c.setCellValue("NC 2");

            c = headerRow.createCell(5);
            c.setCellValue("NC 3");

            //-----------------Table Data Row---------------------
            for (int i = 0; i < reportList.size(); i++) {

                Row newRow = sheet1.createRow((i + 6));

                c = newRow.createCell(0);
                c.setCellValue("" + (i + 1));

                c = newRow.createCell(1);
                c.setCellValue("" + reportList.get(i).getItemName());

                c = newRow.createCell(2);
                c.setCellValue("" + reportList.get(i).getChargable());

                c = newRow.createCell(3);
                c.setCellValue("" + reportList.get(i).getNc1());

                c = newRow.createCell(4);
                c.setCellValue("" + reportList.get(i).getNc2());

                c = newRow.createCell(5);
                c.setCellValue("" + reportList.get(i).getNc3());

            }

            sheet1.setColumnWidth(0, (3 * 500));
            sheet1.setColumnWidth(1, (12 * 500));
            sheet1.setColumnWidth(2, (9 * 500));
            sheet1.setColumnWidth(3, (9 * 500));
            sheet1.setColumnWidth(4, (9 * 500));
            sheet1.setColumnWidth(5, (9 * 500));

            File file = new File(dir, fileName);
            FileOutputStream os = null;

            try {
                os = new FileOutputStream(file);
                wb.write(os);
                //Log.e("FileUtils", "Writing file" + file);
            } catch (IOException e) {
                commonDialog.dismiss();
                //Log.e("FileUtils", "Error writing " + file, e);
            } catch (Exception e) {
                commonDialog.dismiss();
                //Log.e("FileUtils", "Failed to save file", e);
            } finally {
                try {
                    if (null != os)
                        os.close();

                    if (file.exists()) {
                        commonDialog.dismiss();
                        //Log.e("EXCEL", "----------------------------------------------");
                        Intent intent = new Intent(Intent.ACTION_VIEW);

                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                            intent.setDataAndType(Uri.fromFile(file), "application/vnd.ms-excel");
                        } else {
                            if (file.exists()) {
                                String authorities = BuildConfig.APPLICATION_ID + ".provider";
                                Uri uri = FileProvider.getUriForFile(getContext(), authorities, file);
                                intent.setDataAndType(uri, "application/vnd.ms-excel");
                                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            }
                        }
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        startActivity(intent);

                    } else {
                        commonDialog.dismiss();
                        Toast.makeText(getContext(), "Unable To Generate Excel", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception ex) {
                    commonDialog.dismiss();
                }
            }
        } catch (Exception e) {
            commonDialog.dismiss();
        }

    }



    public void getTaxDataForPrint(String fromDate,String toDate) {

        //Log.e("PARAM "," ------------------ "+fromDate+"    to    "+toDate);

        if (Constants.isOnline(getContext())) {
            final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<ArrayList<TaxableDataForBillPrint>> listCall = Constants.myInterface.getTaxDataFroBillPrintByDate(fromDate,toDate);
            listCall.enqueue(new Callback<ArrayList<TaxableDataForBillPrint>>() {
                @Override
                public void onResponse(Call<ArrayList<TaxableDataForBillPrint>> call, Response<ArrayList<TaxableDataForBillPrint>> response) {
                    try {
                        //Log.e("Taxable Data : ", "------------" + response.body());
                        if (response.body() != null) {


                            ArrayList<TaxableDataForBillPrint> data = response.body();

                            try {
                                String ip = CustomSharedPreference.getStringPrinter(getContext(), CustomSharedPreference.KEY_BILL_IP);
                                PrintHelper printHelper = new PrintHelper(getActivity(), ip, data, PrintReceiptType.TAX_PRINT);
                                printHelper.runPrintReceiptSequence();
                            } catch (Exception e) {
                            }



                            commonDialog.dismiss();
                        } else {
                            commonDialog.dismiss();
                            //Log.e("Data Null : ", "-----------");

                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        //Log.e("Exception : ", "-----------" + e.getMessage());
                        e.printStackTrace();


                    }
                }

                @Override
                public void onFailure(Call<ArrayList<TaxableDataForBillPrint>> call, Throwable t) {
                    commonDialog.dismiss();
                    //Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();

                }
            });
        } else {
            Toast.makeText(getContext(), "No Internet Connection!", Toast.LENGTH_SHORT).show();
        }
    }


}
