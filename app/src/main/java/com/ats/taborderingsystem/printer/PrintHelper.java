package com.ats.taborderingsystem.printer;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.ats.taborderingsystem.R;
import com.ats.taborderingsystem.model.BillDetail;
import com.ats.taborderingsystem.model.BillHeaderModel;
import com.ats.taborderingsystem.model.OrderDetails;
import com.ats.taborderingsystem.model.OrderDetailsList;
import com.ats.taborderingsystem.model.OrderHeaderModel;
import com.ats.taborderingsystem.model.ParcelOrderDetails;
import com.ats.taborderingsystem.model.ParcelOrderHeaderModel;
import com.epson.epos2.Epos2Exception;
import com.epson.epos2.printer.Printer;
import com.epson.epos2.printer.PrinterStatusInfo;
import com.epson.epos2.printer.ReceiveListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class PrintHelper implements ReceiveListener {

    Activity activity;
    String printerAddress;
    int modelConstant;
    int printReceiptType;
    private Printer mPrinter = null;
    public static String tableType = null;

    private OrderHeaderModel orderDisplays;
    private ArrayList<OrderDetails> orderDetails;
    String tableName;
    private ArrayList<OrderHeaderModel> orderHeaderArray;
    private float discount;
    private ParcelOrderHeaderModel parcelOrderHeaderModel;
    private ArrayList<ParcelOrderDetails> parcelOrderDetails;
    String customer, mobile, billNo;
    private ArrayList<OrderDetailsList> cancelOrderList;
    private BillHeaderModel billHeader;

    static {
        try {
            System.loadLibrary("libepos2.so");
        } catch (UnsatisfiedLinkError e) {
            Log.e("UnsatisfiedLinkError", "-----------------------" + e.getMessage());
        } catch (Exception e) {
            Log.e("Exception", "------------------------" + e.getMessage());
        }
    }

    public PrintHelper(Activity activity, String printerAddress, int printReceiptType) {
        this.activity = activity;
        this.printerAddress = printerAddress;
        this.modelConstant = Printer.TM_T82; //ModelConstant;
        this.printReceiptType = printReceiptType;
    }

    //------KOT-----------------
    public PrintHelper(Activity activity, String printerAddress, OrderHeaderModel orderDisplay, ArrayList<OrderDetails> orderDetails, String tableName, int printReceiptType) {
        this.activity = activity;
        this.printerAddress = printerAddress;
        this.modelConstant = Printer.TM_T82;
        this.orderDisplays = orderDisplay;
        this.orderDetails = orderDetails;
        this.tableName = tableName;
        this.printReceiptType = printReceiptType;
    }

    //------BILL-----------------
    public PrintHelper(Activity activity, String printerAddress, ArrayList<OrderHeaderModel> orderHeaderArray, String tableName, float discount, String billNo, int printReceiptType) {
        this.activity = activity;
        this.printerAddress = printerAddress;
        this.modelConstant = Printer.TM_T82; //ModelConstant;
        this.orderHeaderArray = orderHeaderArray;
        this.tableName = tableName;
        this.discount = discount;
        this.billNo = billNo;
        this.printReceiptType = printReceiptType;
    }

    //------KOT PARCEL-----------------
    public PrintHelper(Activity activity, String printerAddress, ParcelOrderHeaderModel parcelOrderHeaderModel, ArrayList<ParcelOrderDetails> parcelOrderDetails, int printReceiptType) {
        this.activity = activity;
        this.printerAddress = printerAddress;
        this.modelConstant = Printer.TM_T82;
        this.parcelOrderHeaderModel = parcelOrderHeaderModel;
        this.parcelOrderDetails = parcelOrderDetails;
        this.printReceiptType = printReceiptType;
    }

    //------BILL PARCEL-----------------
    public PrintHelper(Activity activity, String printerAddress, ParcelOrderHeaderModel parcelOrderHeaderModel, ArrayList<ParcelOrderDetails> parcelOrderDetails, float discount, String billNo, int printReceiptType) {
        this.activity = activity;
        this.printerAddress = printerAddress;
        this.modelConstant = Printer.TM_T82; //ModelConstant;
        this.parcelOrderHeaderModel = parcelOrderHeaderModel;
        this.parcelOrderDetails = parcelOrderDetails;
        this.discount = discount;
        this.billNo = billNo;
        this.printReceiptType = printReceiptType;
    }

    //------KOT CANCEL ORDER-----------------
    public PrintHelper(Activity activity, String printerAddress, ArrayList<OrderDetailsList> cancelOrderList, String tableName, int printReceiptType) {
        this.activity = activity;
        this.printerAddress = printerAddress;
        this.modelConstant = Printer.TM_T82;
        this.cancelOrderList = cancelOrderList;
        this.tableName = tableName;
        this.printReceiptType = printReceiptType;
    }

    //------BILL-----------------
    public PrintHelper(Activity activity, String printerAddress, BillHeaderModel billHeader, int printReceiptType) {
        this.activity = activity;
        this.printerAddress = printerAddress;
        this.modelConstant = Printer.TM_T82; //ModelConstant;
        this.billHeader = billHeader;
        this.printReceiptType = printReceiptType;
    }


    public boolean createReceiptData() {
        if (mPrinter == null) {
            return false;
        }

        if (printReceiptType == PrintReceiptType.BILL) {
            //create bill invoice
            return createBillReceipt(orderHeaderArray, tableName, discount, billNo);
        } else if (printReceiptType == PrintReceiptType.KOT) {
            //create KOT invoice
            return createKOTReceipt(orderDisplays, orderDetails, tableName);
        } else if (printReceiptType == PrintReceiptType.BILL_PARCEL) {
            //create bill invoice
            return createParcelBillReceipt(parcelOrderHeaderModel, parcelOrderDetails, discount, billNo);
        } else if (printReceiptType == PrintReceiptType.KOT_PARCEL) {
            //create KOT invoice
            return createParcelKOTReceipt(parcelOrderHeaderModel, parcelOrderDetails);
        } else if (printReceiptType == PrintReceiptType.VOID_KOT) {
            //create VOID KOT invoice
            return createVoidKOTReceipt(cancelOrderList, tableName);
        } else if (printReceiptType == PrintReceiptType.RE_GENERATE_BILL) {
            //create VOID KOT invoice
            return createReGeneraterBillReceipt(billHeader);
        } else {
            return createTestReceipt();
        }
    }

    private boolean createTestReceipt() {
        String method = "";
        StringBuilder textData = new StringBuilder();

        try {
            method = "addTextAlign";
            mPrinter.addTextAlign(Printer.ALIGN_CENTER);
            method = "addFeedLine";
            mPrinter.addFeedLine(1);
            textData.append("This is a TEST receipt\n");
            mPrinter.addText(textData.toString());
            mPrinter.addCut(Printer.CUT_FEED);
            Log.e("PRINT : ", "" + textData);
        } catch (Exception e) {
            ShowMsg.showException(e, method, activity, false);
            return false;
        }
        return true;
    }

    private boolean createKOTReceipt(OrderHeaderModel orderDisplays, ArrayList<OrderDetails> orderItemsArray, String tableName) {
        String method = "";
        StringBuilder textData = new StringBuilder();

        try {
            ArrayList<OrderDetails> orderItems = orderItemsArray;

            String tableNo = String.valueOf(orderDisplays.getTableNo());

            method = "addTextAlign";
            mPrinter.addTextAlign(Printer.ALIGN_LEFT);
            method = "addFeedLine";
            mPrinter.addFeedLine(1);
            String date = orderDisplays.getOrderDate();
            textData.append("\t\t\tKOT\n");

            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
            Calendar calendar = Calendar.getInstance();

            // textData.append("Captain Name :- "+captainName + "\n");
            textData.append("Date :- " + date + "  " + sdf.format(calendar.getTimeInMillis()) + "\n");

            textData.append("Order No :- " + orderDisplays.getOrderId());
            textData.append("\t\t\t Table No :- " + tableName + "\n\n");

            Log.e("OrderItems : ", "-----------" + orderItems.toString());

            mPrinter.addTextStyle(Printer.FALSE, Printer.FALSE, Printer.TRUE, Printer.COLOR_1);

            textData.append("Item");
            for (int s = 0; s < 24; s++) {
                textData.append(" ");
            }
            textData.append("    Qty\n");
            textData.append("-------------------------------------\n");

            for (int i = 0; i < orderItems.size(); i++) {
                try {

                    String strName = orderItems.get(i).getItemName();
                    if (strName.length() >= 28) {
                        String itemName = orderItems.get(i).getItemName().substring(0, 28);
                        textData.append("" + itemName);
                    } else if (strName.length() < 28) {
                        textData.append("" + strName);
                        int difference = 28 - strName.length();

                        for (int d = 0; d < difference; d++) {
                            textData.append(" ");
                        }
                    }

                    String qty = String.valueOf(orderItems.get(i).getQuantity());

                    textData.append("    " + qty + "\n");


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            textData.append("\n");


            mPrinter.addText(textData.toString());
            Log.e("Print ", "\n\n" + textData.toString());

            mPrinter.addCut(Printer.CUT_FEED);


        } catch (Exception e) {
            e.printStackTrace();
            ShowMsg.showException(e, method, activity, false);
            return false;
        }
        return true;
    }

    private boolean createBillReceipt(ArrayList<OrderHeaderModel> orderHeaderModels, String tableName, float discount, String bill) {
        String method = "";
        StringBuilder textData = new StringBuilder();

        try {
            ArrayList<OrderDetailsList> orderItems = new ArrayList<>();
            for (int i = 0; i < orderHeaderModels.size(); i++) {
                orderItems.addAll(orderHeaderModels.get(i).getOrderDetailsList());
            }

            method = "addTextAlign";
            mPrinter.addTextAlign(Printer.ALIGN_CENTER);
            method = "addFeedLine";
            mPrinter.addFeedLine(1);
            String date = orderHeaderModels.get(0).getOrderDate();
            textData.append("\t\t\bThe Happy Feast Co.\n");
            mPrinter.addTextAlign(Printer.ALIGN_LEFT);

            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
            Calendar calendar = Calendar.getInstance();

            textData.append("Date :- " + date + "  " + sdf.format(calendar.getTimeInMillis()) + "\n");
            textData.append("Invoice No :- " + bill + "\n");
            textData.append("Table No :- " + tableName + "\n\n");

            Log.e("OrderItems : ", "-----------" + orderItems.toString());

            textData.append("Item");
            for (int i = 0; i < 16; i++) {
                textData.append(" ");
            }
            textData.append("   Qty");
            textData.append("     Rate");
            textData.append("    Amount\n");

            for (int i = 0; i < 45; i++) {
                textData.append("-");
            }
            textData.append("\n");

            mPrinter.addTextStyle(Printer.FALSE, Printer.FALSE, Printer.TRUE, Printer.COLOR_1);

            double billTotal = 0;

            for (int i = 0; i < orderItems.size(); i++) {
                if (orderItems.get(i).getStatus() == 1) {

                    String strName = orderItems.get(i).getItemName();
                    if (strName.length() >= 20) {
                        String itemName = orderItems.get(i).getItemName().substring(0, 20);
                        textData.append(itemName);

                    } else if (strName.length() < 20) {
                        textData.append(strName);
                        int difference = 20 - strName.length();

                        for (int d = 0; d < difference; d++) {
                            textData.append(" ");
                        }
                    }

                    String qty = String.valueOf(orderItems.get(i).getQuantity());
                    double totalDouble = orderItems.get(i).getRate() * orderItems.get(i).getQuantity();
                    //String rate = String.valueOf(rateDouble);
                    String total = String.format("%.1f", totalDouble);
                    String rate = String.valueOf(orderItems.get(i).getRate());

                    billTotal = billTotal + totalDouble;


                    try {

                        textData.append("   " + qty);
                        int difference = 3 - qty.length();
                        for (int d = 0; d < difference; d++) {
                            textData.append(" ");
                        }

                        textData.append("   ");

                        difference = 6 - rate.length();
                        for (int d = 0; d < difference; d++) {
                            textData.append(" ");
                        }
                        textData.append("" + rate);

                        textData.append("   ");

                        difference = 7 - total.length();
                        for (int d = 0; d < difference; d++) {
                            textData.append(" ");
                        }
                        textData.append("" + total + "\n");

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }

            for (int i = 0; i < 45; i++) {
                textData.append("-");
            }
            textData.append("\n");

            String bTot = "Total : " + String.format("%.2f", billTotal);
            int difference = 45 - bTot.length();
            for (int d = 0; d < difference; d++) {
                textData.append(" ");
            }
            textData.append(bTot + "\n");

            String disc = "Discount : " + discount + " %";
            difference = 45 - disc.length();
            for (int d = 0; d < difference; d++) {
                textData.append(" ");
            }
            textData.append(disc + "\n");

            for (int i = 0; i < 45; i++) {
                textData.append("-");
            }
            textData.append("\n");

            double discAmt = ((billTotal * discount) / 100);
            double temp = billTotal - discAmt;

            String grandTotal = "GRAND TOTAL : " + String.format("%.2f", temp);
            difference = 45 - grandTotal.length();
            for (int d = 0; d < difference; d++) {
                textData.append(" ");
            }
            textData.append(grandTotal + "\n");

            for (int i = 0; i < 45; i++) {
                textData.append("-");
            }
            textData.append("\n");

            // textData.append("GSTNo : 27ABGPJ9389N1ZP\n\n\n");


            mPrinter.addText(textData.toString());
            Log.e("Print --", "\n\n" + textData.toString());
            mPrinter.addCut(Printer.CUT_FEED);

//            mPrinter.addText(textData.toString());
//            Log.e("p\n", "\n\n" + textData.toString());
//            mPrinter.addCut(Printer.CUT_FEED);


        } catch (Exception e) {
            e.printStackTrace();
            ShowMsg.showException(e, method, activity, false);
            return false;
        }
        return true;
    }

    private boolean createParcelKOTReceipt(ParcelOrderHeaderModel orderDisplays, ArrayList<ParcelOrderDetails> parcelOrderDetails) {
        String method = "";
        StringBuilder textData = new StringBuilder();

        try {
            ArrayList<ParcelOrderDetails> orderItems = parcelOrderDetails;

            method = "addTextAlign";
            mPrinter.addTextAlign(Printer.ALIGN_LEFT);
            method = "addFeedLine";
            mPrinter.addFeedLine(1);
            String date = orderDisplays.getOrderDate();
            textData.append("\t\t\tPARCEL KOT\n");

            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
            Calendar calendar = Calendar.getInstance();

            textData.append(date + "  " + sdf.format(calendar.getTimeInMillis()) + "\n");

            textData.append("Order No :- " + orderDisplays.getParcelOrderId()+ "\n\n");

            Log.e("OrderItems : ", "-----------" + orderItems.toString());

            mPrinter.addTextStyle(Printer.FALSE, Printer.FALSE, Printer.TRUE, Printer.COLOR_1);

            textData.append("Item");
            for (int s = 0; s < 24; s++) {
                textData.append(" ");
            }
            textData.append("    Qty\n");
            textData.append("-------------------------------------\n");

            for (int i = 0; i < orderItems.size(); i++) {
                try {

                    String strName = orderItems.get(i).getItemName();
                    if (strName.length() >= 28) {
                        String itemName = orderItems.get(i).getItemName().substring(0, 28);
                        textData.append("" + itemName);
                    } else if (strName.length() < 28) {
                        textData.append("" + strName);
                        int difference = 28 - strName.length();

                        for (int d = 0; d < difference; d++) {
                            textData.append(" ");
                        }
                    }

                    String qty = String.valueOf(orderItems.get(i).getQuantity());

                    textData.append("    " + qty + "\n");


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            textData.append("\n\n");


            mPrinter.addText(textData.toString());
            Log.e("Print ", "\n\n" + textData.toString());

            mPrinter.addCut(Printer.CUT_FEED);


        } catch (Exception e) {
            e.printStackTrace();
            ShowMsg.showException(e, method, activity, false);
            return false;
        }
        return true;
    }

    private boolean createParcelBillReceipt(ParcelOrderHeaderModel orderDisplays, ArrayList<ParcelOrderDetails> parcelOrderDetails, float discount, String bill) {
        String method = "";
        StringBuilder textData = new StringBuilder();

        try {
            ArrayList<ParcelOrderDetails> orderItems = parcelOrderDetails;

            method = "addTextAlign";
            mPrinter.addTextAlign(Printer.ALIGN_CENTER);
            method = "addFeedLine";
            mPrinter.addFeedLine(1);
            String date = orderDisplays.getOrderDate();
            textData.append("\t\t\bThe Happy Feast Co.\n\n");
            mPrinter.addTextAlign(Printer.ALIGN_LEFT);
            textData.append("\bParcel\n");

            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
            Calendar calendar = Calendar.getInstance();

            textData.append("Date :- " + date + "  " + sdf.format(calendar.getTimeInMillis()) + "\n");
            textData.append("Invoice No :- " + bill + "\n");
            textData.append("Customer :- " + orderDisplays.getName() + "\n");
            textData.append("Mobile :- " + orderDisplays.getMobileNo() + "\n\n");

            Log.e("OrderItems : ", "-----------" + orderItems.toString());

            textData.append("Item");
            for (int i = 0; i < 16; i++) {
                textData.append(" ");
            }
            textData.append("   Qty");
            textData.append("     Rate");
            textData.append("    Amount\n");

            for (int i = 0; i < 45; i++) {
                textData.append("-");
            }
            textData.append("\n");

            mPrinter.addTextStyle(Printer.FALSE, Printer.FALSE, Printer.TRUE, Printer.COLOR_1);

            double billTotal = 0;

            for (int i = 0; i < orderItems.size(); i++) {

                String strName = orderItems.get(i).getItemName();
                if (strName.length() >= 20) {
                    String itemName = orderItems.get(i).getItemName().substring(0, 20);
                    textData.append(itemName);

                } else if (strName.length() < 20) {
                    textData.append(strName);
                    int difference = 20 - strName.length();

                    for (int d = 0; d < difference; d++) {
                        textData.append(" ");
                    }
                }

                String qty = String.valueOf(orderItems.get(i).getQuantity());
                double totalDouble = orderItems.get(i).getRate() * orderItems.get(i).getQuantity();
                //String rate = String.valueOf(rateDouble);
                String total = String.format("%.1f", totalDouble);
                String rate = String.valueOf(orderItems.get(i).getRate());

                billTotal = billTotal + totalDouble;


                try {

                    textData.append("   " + qty);
                    int difference = 3 - qty.length();
                    for (int d = 0; d < difference; d++) {
                        textData.append(" ");
                    }

                    textData.append("   ");

                    difference = 6 - rate.length();
                    for (int d = 0; d < difference; d++) {
                        textData.append(" ");
                    }
                    textData.append("" + rate);

                    textData.append("   ");

                    difference = 7 - total.length();
                    for (int d = 0; d < difference; d++) {
                        textData.append(" ");
                    }
                    textData.append("" + total + "\n");

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            for (int i = 0; i < 45; i++) {
                textData.append("-");
            }
            textData.append("\n");

            String bTot = "Total : " + String.format("%.2f", billTotal);
            int difference = 45 - bTot.length();
            for (int d = 0; d < difference; d++) {
                textData.append(" ");
            }
            textData.append(bTot + "\n");

            String disc = "Discount : " + discount + " %";
            difference = 45 - disc.length();
            for (int d = 0; d < difference; d++) {
                textData.append(" ");
            }
            textData.append(disc + "\n");

            for (int i = 0; i < 45; i++) {
                textData.append("-");
            }
            textData.append("\n");

            double discAmt = ((billTotal * discount) / 100);
            double temp = billTotal - discAmt;

            String grandTotal = "GRAND TOTAL : " + String.format("%.2f", temp);
            difference = 45 - grandTotal.length();
            for (int d = 0; d < difference; d++) {
                textData.append(" ");
            }
            textData.append(grandTotal + "\n");

            for (int i = 0; i < 45; i++) {
                textData.append("-");
            }
            textData.append("\n");

            // textData.append("GSTNo : 27ABGPJ9389N1ZP\n\n\n");


            mPrinter.addText(textData.toString());
            Log.e("Print --", "\n\n" + textData.toString());
            mPrinter.addCut(Printer.CUT_FEED);

//            mPrinter.addText(textData.toString());
//            Log.e("p\n", "\n\n" + textData.toString());
//            mPrinter.addCut(Printer.CUT_FEED);


        } catch (Exception e) {
            e.printStackTrace();
            ShowMsg.showException(e, method, activity, false);
            return false;
        }
        return true;
    }

    private boolean createVoidKOTReceipt(ArrayList<OrderDetailsList> orderItems, String tableName) {
        String method = "";
        StringBuilder textData = new StringBuilder();

        try {
            method = "addTextAlign";
            mPrinter.addTextAlign(Printer.ALIGN_LEFT);
            method = "addFeedLine";
            mPrinter.addFeedLine(1);
            textData.append("\t\t\tVOID KOT\n");

            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy  hh:mm a");

            textData.append("Date :- " + sdf.format(calendar.getTimeInMillis()) + "\n");

            textData.append("Table No :- " + tableName + "\n\n");

            Log.e("OrderItems : ", "-----------" + orderItems.toString());

            mPrinter.addTextStyle(Printer.FALSE, Printer.FALSE, Printer.TRUE, Printer.COLOR_1);

            textData.append("Item");
            for (int s = 0; s < 24; s++) {
                textData.append(" ");
            }
            textData.append("    Qty\n");
            textData.append("-------------------------------------\n");

            for (int i = 0; i < orderItems.size(); i++) {
                try {

                    String strName = orderItems.get(i).getItemName();
                    if (strName.length() >= 28) {
                        String itemName = orderItems.get(i).getItemName().substring(0, 28);
                        textData.append("" + itemName);
                    } else if (strName.length() < 28) {
                        textData.append("" + strName);
                        int difference = 28 - strName.length();

                        for (int d = 0; d < difference; d++) {
                            textData.append(" ");
                        }
                    }

                    String qty = String.valueOf(orderItems.get(i).getQuantity());

                    textData.append("    " + qty + "\n");

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            textData.append("\n");


            mPrinter.addText(textData.toString());
            Log.e("Print ", "\n\n" + textData.toString());

            mPrinter.addCut(Printer.CUT_FEED);


        } catch (Exception e) {
            e.printStackTrace();
            ShowMsg.showException(e, method, activity, false);
            return false;
        }
        return true;
    }

    private boolean createReGeneraterBillReceipt(BillHeaderModel billHeaderModel) {
        String method = "";
        StringBuilder textData = new StringBuilder();

        try {
            ArrayList<BillDetail> orderItems = new ArrayList<>();
            orderItems = (ArrayList<BillDetail>) billHeaderModel.getBillDetails();

            method = "addTextAlign";
            mPrinter.addTextAlign(Printer.ALIGN_CENTER);
            method = "addFeedLine";
            mPrinter.addFeedLine(1);
            String date = billHeaderModel.getBillDate();
            textData.append("\t\t\bThe Happy Feast Co.\n");
            mPrinter.addTextAlign(Printer.ALIGN_LEFT);

            if (billHeaderModel.getEnterBy() == 2) {
                textData.append("\bParcel\n");
            }

            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
            Calendar calendar = Calendar.getInstance();

            textData.append("Date :- " + date + "  " + sdf.format(calendar.getTimeInMillis()) + "\n");
            textData.append("Invoice No :- " + billHeaderModel.getBillNo() + "\n");
            textData.append("Table No :- " + billHeaderModel.getTableNo() + "\n\n");

            if (billHeaderModel.getEnterBy() == 2) {
                textData.append("Customer :- " + billHeaderModel.getName() + "\n");
                textData.append("Mobile :- " + billHeaderModel.getMobileNo() + "\n\n");
            }

            Log.e("OrderItems : ", "-----------" + orderItems.toString());

            textData.append("Item");
            for (int i = 0; i < 16; i++) {
                textData.append(" ");
            }
            textData.append("   Qty");
            textData.append("     Rate");
            textData.append("    Amount\n");

            for (int i = 0; i < 45; i++) {
                textData.append("-");
            }
            textData.append("\n");

            mPrinter.addTextStyle(Printer.FALSE, Printer.FALSE, Printer.TRUE, Printer.COLOR_1);

            double billTotal = 0;

            for (int i = 0; i < orderItems.size(); i++) {

                String strName = orderItems.get(i).getItemName();
                if (strName.length() >= 20) {
                    String itemName = orderItems.get(i).getItemName().substring(0, 20);
                    textData.append(itemName);

                } else if (strName.length() < 20) {
                    textData.append(strName);
                    int difference = 20 - strName.length();

                    for (int d = 0; d < difference; d++) {
                        textData.append(" ");
                    }
                }

                String qty = String.valueOf(orderItems.get(i).getQuantity());
                double totalDouble = orderItems.get(i).getRate() * orderItems.get(i).getQuantity();
                //String rate = String.valueOf(rateDouble);
                String total = String.format("%.1f", totalDouble);
                String rate = String.valueOf(orderItems.get(i).getRate());

                billTotal = billTotal + totalDouble;


                try {

                    textData.append("   " + qty);
                    int difference = 3 - qty.length();
                    for (int d = 0; d < difference; d++) {
                        textData.append(" ");
                    }

                    textData.append("   ");

                    difference = 6 - rate.length();
                    for (int d = 0; d < difference; d++) {
                        textData.append(" ");
                    }
                    textData.append("" + rate);

                    textData.append("   ");

                    difference = 7 - total.length();
                    for (int d = 0; d < difference; d++) {
                        textData.append(" ");
                    }
                    textData.append("" + total + "\n");

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            for (int i = 0; i < 45; i++) {
                textData.append("-");
            }
            textData.append("\n");

            String bTot = "Total : " + billHeaderModel.getGrandTotal();
            int difference = 45 - bTot.length();
            for (int d = 0; d < difference; d++) {
                textData.append(" ");
            }
            textData.append(bTot + "\n");

            String disc = "Discount : " + billHeaderModel.getDiscount() + " %";
            difference = 45 - disc.length();
            for (int d = 0; d < difference; d++) {
                textData.append(" ");
            }
            textData.append(disc + "\n");

            for (int i = 0; i < 45; i++) {
                textData.append("-");
            }
            textData.append("\n");

            String grandTotal = "GRAND TOTAL : " + billHeaderModel.getPayableAmt();
            difference = 45 - grandTotal.length();
            for (int d = 0; d < difference; d++) {
                textData.append(" ");
            }
            textData.append(grandTotal + "\n");

            for (int i = 0; i < 45; i++) {
                textData.append("-");
            }
            textData.append("\n");

            // textData.append("GSTNo : 27ABGPJ9389N1ZP\n\n\n");


            mPrinter.addText(textData.toString());
            Log.e("Print --", "\n\n" + textData.toString());
            mPrinter.addCut(Printer.CUT_FEED);

//            mPrinter.addText(textData.toString());
//            Log.e("p\n", "\n\n" + textData.toString());
//            mPrinter.addCut(Printer.CUT_FEED);


        } catch (Exception e) {
            e.printStackTrace();
            ShowMsg.showException(e, method, activity, false);
            return false;
        }
        return true;
    }


    public boolean runPrintReceiptSequence() {
        try {
            if (!initializeObject()) {
                return false;
            }

            if (!createReceiptData()) {
                finalizeObject();
                return false;
            }

            if (!printData()) {
                finalizeObject();
                return false;
            }

        } catch (Exception e) {
        }

        return true;
    }

    private boolean printData() {
        if (mPrinter == null) {
            return false;
        }

        if (!connectPrinter()) {
            return false;
        }

        PrinterStatusInfo status = mPrinter.getStatus();
        if (!isPrintable(status)) {
            ShowMsg.showMsg(makeErrorMessage(status), activity, false);
            try {
                mPrinter.disconnect();
            } catch (Exception ex) {
                // Do nothing
            }
            return false;
        }

        try {
            mPrinter.sendData(Printer.PARAM_DEFAULT);
        } catch (Exception e) {
            ShowMsg.showException(e, "sendData", activity, false);
            try {
                mPrinter.disconnect();
            } catch (Exception ex) {
                // Do nothing
            }
            return false;
        }

        return true;
    }

    private boolean initializeObject() {
        try {
            mPrinter = new Printer(modelConstant,
                    Printer.MODEL_ANK,
                    activity);
        } catch (UnsatisfiedLinkError e) {
            Log.e("UnsatisfiedLinkError", "-----initializeObject" + e.getMessage());
            Toast.makeText(activity, "Please Check Printer IP, Printer Must Be In Same Network", Toast.LENGTH_SHORT).show();
        } catch (NoClassDefFoundError e) {
            Log.e("NoClassDefFoundError", "-----initializeObject" + e.getMessage());
            Toast.makeText(activity, "Please Check Printer IP, Printer Must Be In Same Network", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("initializeObject", "-----------------------------------------------------------------");
            ShowMsg.showException(e, "Printer", activity, false);
            return false;
        }

        mPrinter.setReceiveEventListener(this);
        return true;
    }

    /**
     * Release all printer resources
     */
    private void finalizeObject() {
        if (mPrinter == null) {
            return;
        }
        mPrinter.clearCommandBuffer();
        mPrinter.setReceiveEventListener(null);
        mPrinter = null;
    }

    private boolean connectPrinter() {
        Log.e("connectPrinter", "----------------------------");
        boolean isBeginTransaction = false;

        if (mPrinter == null) {
            return false;
        }

        try {
            Log.e("connectPrinter", "----------------------------printerAddress" + printerAddress);
            mPrinter.connect(printerAddress, Printer.PARAM_DEFAULT);
        } catch (Exception e) {
            Log.e("connectPrinter", "----------------------------showException" + e.getMessage());
            e.printStackTrace();
            Log.e("connectPrinter", "---------------------------------------------------------------------------");
            ShowMsg.showException(e, "connect", activity, false);
            return false;
        }

        try {
            mPrinter.beginTransaction();
            isBeginTransaction = true;
        } catch (Exception e) {
            Log.e("connectPrinter", "----------------------------Exception");
            Log.e("connectPrinter", "beginTransaction---------------------------------------------------------------------------");
            ShowMsg.showException(e, "beginTransaction", activity, false);
        }

        if (isBeginTransaction == false) {
            try {
                mPrinter.disconnect();
            } catch (Epos2Exception e) {
                // Do nothing
                return false;
            }
        }

        return true;
    }

    private void disconnectPrinter() {
        if (mPrinter == null) {
            return;
        }

        try {
            mPrinter.endTransaction();
        } catch (final Exception e) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public synchronized void run() {
                    Log.e("disconnectPrinter", "endTransaction---------------------------------------------------------------------------");
                    ShowMsg.showException(e, "endTransaction", activity, false);
                }
            });
        }

        try {
            mPrinter.disconnect();
        } catch (final Exception e) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public synchronized void run() {
                    Log.e("disconnectPrinter", "---------------------------------------------------------------------------");
                    ShowMsg.showException(e, "disconnect", activity, false);
                }
            });
        }

        finalizeObject();
    }

    private boolean isPrintable(PrinterStatusInfo status) {
        if (status == null) {
            return false;
        }

        if (status.getConnection() == Printer.FALSE) {
            return false;
        } else if (status.getOnline() == Printer.FALSE) {
            return false;
        } else {
            ;//print available
        }

        return true;
    }

    private String makeErrorMessage(PrinterStatusInfo status) {
        String msg = "";

        if (status.getOnline() == Printer.FALSE) {
            msg += activity.getResources().getString(R.string.handlingmsg_err_offline);
        }
        if (status.getConnection() == Printer.FALSE) {
            msg += activity.getResources().getString(R.string.handlingmsg_err_no_response);
        }
        if (status.getCoverOpen() == Printer.TRUE) {
            msg += activity.getResources().getString(R.string.handlingmsg_err_cover_open);
        }
        if (status.getPaper() == Printer.PAPER_EMPTY) {
            msg += activity.getResources().getString(R.string.handlingmsg_err_receipt_end);
        }
        if (status.getPaperFeed() == Printer.TRUE || status.getPanelSwitch() == Printer.SWITCH_ON) {
            msg += activity.getResources().getString(R.string.handlingmsg_err_paper_feed);
        }
        if (status.getErrorStatus() == Printer.MECHANICAL_ERR || status.getErrorStatus() == Printer.AUTOCUTTER_ERR) {
            msg += activity.getResources().getString(R.string.handlingmsg_err_autocutter);
            msg += activity.getResources().getString(R.string.handlingmsg_err_need_recover);
        }
        if (status.getErrorStatus() == Printer.UNRECOVER_ERR) {
            msg += activity.getResources().getString(R.string.handlingmsg_err_unrecover);
        }
        if (status.getErrorStatus() == Printer.AUTORECOVER_ERR) {
            if (status.getAutoRecoverError() == Printer.HEAD_OVERHEAT) {
                msg += activity.getResources().getString(R.string.handlingmsg_err_overheat);
                msg += activity.getResources().getString(R.string.handlingmsg_err_head);
            }
            if (status.getAutoRecoverError() == Printer.MOTOR_OVERHEAT) {
                msg += activity.getResources().getString(R.string.handlingmsg_err_overheat);
                msg += activity.getResources().getString(R.string.handlingmsg_err_motor);
            }
            if (status.getAutoRecoverError() == Printer.BATTERY_OVERHEAT) {
                msg += activity.getResources().getString(R.string.handlingmsg_err_overheat);
                msg += activity.getResources().getString(R.string.handlingmsg_err_battery);
            }
            if (status.getAutoRecoverError() == Printer.WRONG_PAPER) {
                msg += activity.getResources().getString(R.string.handlingmsg_err_wrong_paper);
            }
        }
        if (status.getBatteryLevel() == Printer.BATTERY_LEVEL_0) {
            msg += activity.getResources().getString(R.string.handlingmsg_err_battery_real_end);
        }

        return msg;
    }

    @Override
    public void onPtrReceive(final Printer printerObj, final int code, final PrinterStatusInfo status, final String printJobId) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public synchronized void run() {
                Log.e("onPtrReceive", "---------------------------------------------------------------------------");
                ShowMsg.showResult(code, makeErrorMessage(status), activity, false);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        disconnectPrinter();
                    }
                }).start();
            }
        });
    }

}
