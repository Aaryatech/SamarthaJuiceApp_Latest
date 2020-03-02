package com.ats.samarthajuice.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ats.samarthajuice.R;
import com.ats.samarthajuice.printer.PrintHelper;
import com.ats.samarthajuice.printer.PrintReceiptType;
import com.ats.samarthajuice.util.CustomSharedPreference;

public class PrinterSettingsFragment extends Fragment implements View.OnClickListener {


    private EditText edBillIP, edKOTIP;
    private Button btnSave, btnTestBill, btnTestKOT;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_printer_settings, container, false);

        getActivity().setTitle("Printer Settings");

        edBillIP = view.findViewById(R.id.edBillIP);
        edKOTIP = view.findViewById(R.id.edKOTIP);

        btnSave = view.findViewById(R.id.btnSave);
        btnTestBill = view.findViewById(R.id.btnTestBill);
        btnTestKOT = view.findViewById(R.id.btnTestKOT);

        String billIp = CustomSharedPreference.getStringPrinter(getActivity(), CustomSharedPreference.KEY_BILL_IP);
        String kotIp = CustomSharedPreference.getStringPrinter(getActivity(), CustomSharedPreference.KEY_KOT_IP);

        if (billIp != null) {
            edBillIP.setText(billIp);
        }

        if (kotIp != null) {
            edKOTIP.setText(kotIp);
        }

        btnSave.setOnClickListener(this);
        btnTestBill.setOnClickListener(this);
        btnTestKOT.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnSave) {

            String billIp = edBillIP.getText().toString();
            String kotIp = edKOTIP.getText().toString();

            if (billIp.isEmpty()) {
                Toast.makeText(getActivity(), "Please enter Bill IP Address", Toast.LENGTH_SHORT).show();
                edBillIP.setError("required");
            } else if (kotIp.isEmpty()) {
                edBillIP.setError(null);
                Toast.makeText(getActivity(), "Please enter KOT IP Address", Toast.LENGTH_SHORT).show();
                edKOTIP.setError("required");
            } else {
                edKOTIP.setError(null);

                String billIPTCP = "TCP:" + billIp;
                String kotIPTCP = "TCP:" + kotIp;


                CustomSharedPreference.putStringPrinter(getActivity(), CustomSharedPreference.KEY_BILL_IP, billIPTCP);
                CustomSharedPreference.putStringPrinter(getActivity(), CustomSharedPreference.KEY_KOT_IP, kotIPTCP);
                Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
            }


        } else if (v.getId() == R.id.btnTestBill) {
            try {
                String ip = CustomSharedPreference.getStringPrinter(getActivity(), CustomSharedPreference.KEY_BILL_IP);
                PrintHelper printHelper = new PrintHelper(getActivity(), ip, PrintReceiptType.TEST);
                printHelper.runPrintReceiptSequence();
            } catch (Exception e) {
            }
        } else if (v.getId() == R.id.btnTestKOT) {
            try {
                String ip = CustomSharedPreference.getStringPrinter(getActivity(), CustomSharedPreference.KEY_KOT_IP);
                PrintHelper printHelper = new PrintHelper(getActivity(), ip, PrintReceiptType.TEST);
                printHelper.runPrintReceiptSequence();
            } catch (Exception e) {
            }
        }
    }
}
