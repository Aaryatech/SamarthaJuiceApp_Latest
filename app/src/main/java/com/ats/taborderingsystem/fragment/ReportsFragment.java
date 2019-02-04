package com.ats.taborderingsystem.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ats.taborderingsystem.R;

public class ReportsFragment extends Fragment implements View.OnClickListener {

    private TextView tvItemWise, tvDateWiseItem, tvCategoryWise, tvItemWiseHSN, tvBillWise, tvDateWise, tvMonthWise, tvDateWiseTax, tvBillWiseTax, tvTaxLabWise, tvTableWise, tvOrderCancelWise, tvItemCancelWise;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reports, container, false);

        getActivity().setTitle("Reports");

        tvItemWise = view.findViewById(R.id.tvItemWise);
        tvDateWiseItem = view.findViewById(R.id.tvDateWiseItem);
        tvCategoryWise = view.findViewById(R.id.tvCategoryWise);
        tvItemWiseHSN = view.findViewById(R.id.tvItemWiseHSN);
        tvBillWise = view.findViewById(R.id.tvBillWise);
        tvDateWise = view.findViewById(R.id.tvDateWise);
        tvMonthWise = view.findViewById(R.id.tvMonthWise);
        tvDateWiseTax = view.findViewById(R.id.tvDateWiseTax);
        tvBillWiseTax = view.findViewById(R.id.tvBillWiseTax);
        tvTaxLabWise = view.findViewById(R.id.tvTaxLabWise);
        tvTableWise = view.findViewById(R.id.tvTableWise);
        tvOrderCancelWise = view.findViewById(R.id.tvOrderCancelWise);
        tvItemCancelWise = view.findViewById(R.id.tvItemCancelWise);

        tvItemWise.setOnClickListener(this);
        tvDateWiseItem.setOnClickListener(this);
        tvCategoryWise.setOnClickListener(this);
        tvItemWiseHSN.setOnClickListener(this);
        tvBillWise.setOnClickListener(this);
        tvDateWise.setOnClickListener(this);
        tvMonthWise.setOnClickListener(this);
        tvDateWiseTax.setOnClickListener(this);
        tvBillWiseTax.setOnClickListener(this);
        tvTaxLabWise.setOnClickListener(this);
        tvTableWise.setOnClickListener(this);
        tvOrderCancelWise.setOnClickListener(this);
        tvItemCancelWise.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tvItemWise) {
            Fragment adf = new ItemReportFragment();
            Bundle args = new Bundle();
            args.putString("reportType", "ItemWise");
            adf.setArguments(args);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, adf, "ReportsFragment").commit();

        } else if (v.getId() == R.id.tvDateWiseItem) {
            Fragment adf = new ItemReportFragment();
            Bundle args = new Bundle();
            args.putString("reportType", "DateWiseItem");
            adf.setArguments(args);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, adf, "ReportsFragment").commit();

        } else if (v.getId() == R.id.tvCategoryWise) {
            Fragment adf = new ItemReportFragment();
            Bundle args = new Bundle();
            args.putString("reportType", "CategoryWise");
            adf.setArguments(args);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, adf, "ReportsFragment").commit();

        } else if (v.getId() == R.id.tvItemWiseHSN) {
            Fragment adf = new ItemReportFragment();
            Bundle args = new Bundle();
            args.putString("reportType", "ItemHSN");
            adf.setArguments(args);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, adf, "ReportsFragment").commit();

        } else if (v.getId() == R.id.tvBillWise) {
            Fragment adf = new ItemReportFragment();
            Bundle args = new Bundle();
            args.putString("reportType", "BillWise");
            adf.setArguments(args);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, adf, "ReportsFragment").commit();

        } else if (v.getId() == R.id.tvDateWise) {
            Fragment adf = new ItemReportFragment();
            Bundle args = new Bundle();
            args.putString("reportType", "DateWiseBill");
            adf.setArguments(args);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, adf, "ReportsFragment").commit();

        } else if (v.getId() == R.id.tvMonthWise) {
            Fragment adf = new ItemReportFragment();
            Bundle args = new Bundle();
            args.putString("reportType", "MonthWiseBill");
            adf.setArguments(args);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, adf, "ReportsFragment").commit();

        } else if (v.getId() == R.id.tvBillWiseTax) {
            Fragment adf = new ItemReportFragment();
            Bundle args = new Bundle();
            args.putString("reportType", "BillWiseBillTax");
            adf.setArguments(args);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, adf, "ReportsFragment").commit();

        } else if (v.getId() == R.id.tvDateWiseTax) {
            Fragment adf = new ItemReportFragment();
            Bundle args = new Bundle();
            args.putString("reportType", "DateWiseBillTax");
            adf.setArguments(args);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, adf, "ReportsFragment").commit();

        } else if (v.getId() == R.id.tvTaxLabWise) {
            Fragment adf = new ItemReportFragment();
            Bundle args = new Bundle();
            args.putString("reportType", "TaxLab");
            adf.setArguments(args);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, adf, "ReportsFragment").commit();

        } else if (v.getId() == R.id.tvTableWise) {
            Fragment adf = new ItemReportFragment();
            Bundle args = new Bundle();
            args.putString("reportType", "TableWise");
            adf.setArguments(args);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, adf, "ReportsFragment").commit();

        } else if (v.getId() == R.id.tvOrderCancelWise) {
            Fragment adf = new ItemReportFragment();
            Bundle args = new Bundle();
            args.putString("reportType", "OrderCancel");
            adf.setArguments(args);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, adf, "ReportsFragment").commit();

        } else if (v.getId() == R.id.tvItemCancelWise) {
            Fragment adf = new ItemReportFragment();
            Bundle args = new Bundle();
            args.putString("reportType", "ItemCancel");
            adf.setArguments(args);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, adf, "ReportsFragment").commit();

        }
    }





}
