package com.ats.samarthajuice.printer;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.ats.samarthajuice.model.BillDetail;
import com.ats.samarthajuice.model.BillHeaderModel;
import com.ats.samarthajuice.model.OrderDetails;
import com.ats.samarthajuice.model.OrderDetailsList;
import com.ats.samarthajuice.model.OrderHeaderModel;
import com.ats.samarthajuice.model.ParcelOrderDetails;
import com.ats.samarthajuice.model.ParcelOrderHeaderModel;
import com.ats.samarthajuice.R;
import com.ats.samarthajuice.model.TaxableDataForBillPrint;
import com.epson.epos2.Epos2Exception;
import com.epson.epos2.printer.Printer;
import com.epson.epos2.printer.PrinterStatusInfo;
import com.epson.epos2.printer.ReceiveListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TreeSet;

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

    private ArrayList<TaxableDataForBillPrint> taxData;

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
        this.modelConstant = Printer.TM_M30;
        this.orderDisplays = orderDisplay;
        this.orderDetails = orderDetails;
        this.tableName = tableName;
        this.printReceiptType = printReceiptType;
    }

    //------BILL-----------------
    public PrintHelper(Activity activity, String printerAddress, ArrayList<OrderHeaderModel> orderHeaderArray, String tableName, float discount, String billNo, ArrayList<TaxableDataForBillPrint> taxData, int printReceiptType) {
        this.activity = activity;
        this.printerAddress = printerAddress;
        this.modelConstant = Printer.TM_T82; //ModelConstant;
        this.orderHeaderArray = orderHeaderArray;
        this.tableName = tableName;
        this.discount = discount;
        this.billNo = billNo;
        this.taxData = taxData;
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
    public PrintHelper(Activity activity, String printerAddress, BillHeaderModel billHeader, ArrayList<TaxableDataForBillPrint> taxData, int printReceiptType) {
        this.activity = activity;
        this.printerAddress = printerAddress;
        this.modelConstant = Printer.TM_T82; //ModelConstant;
        this.billHeader = billHeader;
        this.taxData = taxData;
        this.printReceiptType = printReceiptType;
    }

    //----------TAX_PRINT--------------
    public PrintHelper(Activity activity, String printerAddress, ArrayList<TaxableDataForBillPrint> taxData, int printReceiptType) {
        this.activity = activity;
        this.printerAddress = printerAddress;
        this.modelConstant = Printer.TM_T82; //ModelConstant;
        this.taxData = taxData;
        this.printReceiptType = printReceiptType;
    }


    public boolean createReceiptData() {
        if (mPrinter == null) {
            return false;
        }

        if (printReceiptType == PrintReceiptType.BILL) {
            //create bill invoice
            return createBillReceipt(orderHeaderArray, tableName, discount, billNo, taxData);
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
        } else if (printReceiptType == PrintReceiptType.TAX_PRINT) {
            //create VOID KOT invoice
            return createTaxPrint(taxData);
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
            // mPrinter.addFeedLine(1);
            String date = orderDisplays.getOrderDate();
            textData.append("\t\tKOT\n");

            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
            Calendar calendar = Calendar.getInstance();

            // textData.append("Captain Name :- "+captainName + "\n");
            textData.append("Date :- " + date + "  " + sdf.format(calendar.getTimeInMillis()) + "\n");

            textData.append("Order No :- " + orderDisplays.getOrderId()+ "\n");
            textData.append("Table No :- " + tableName + "\n\n");

           // Log.e("OrderItems : ", "-----------" + orderItems.toString());

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

                    String remark = orderItems.get(i).getRemark();
                    String strRemark = "(" + orderItems.get(i).getRemark() + ")";
                    if (!remark.isEmpty()) {
                        if (strRemark.length() >= 28) {
                            String rm = orderItems.get(i).getRemark().substring(0, 28);
                            textData.append("" + rm);
                        } else if (strRemark.length() < 28) {
                            textData.append("" + strRemark);
                            int difference = 28 - strRemark.length();

                            for (int d = 0; d < difference; d++) {
                                textData.append(" ");
                            }
                        }

                        textData.append("\n");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            textData.append("-------------------------------------");
            /*String remark ="Remark :- " + orderItems.get(0).getRemark().replaceAll("(.{30})(?!$)", "$1#");
           // textData.append("Remark :- " + orderItems.get(0).getRemark());
            String [] strArray= remark.split("#");
            for(int i=0;i<strArray.length;i++)
            {
                textData.append(""+strArray[i]+ "\n");
                Log.e("Remark "+i,"----------------"+strArray[i]);
            }

*/
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

/*
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
            textData.append("\t\t\bSamarth Juice Center\n");
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

           */
/* for(int i=0;i<2;i++) {
                mPrinter.addText(textData.toString());
                Log.e("Print --", "\n\n" + textData.toString());
                mPrinter.addCut(Printer.CUT_FEED);
            }*//*

            mPrinter.addText(textData.toString());
            Log.e("p\n", "\n\n" + textData.toString());
            mPrinter.addCut(Printer.CUT_FEED);


        } catch (Exception e) {
            e.printStackTrace();
            ShowMsg.showException(e, method, activity, false);
            return false;
        }
        return true;
    }
*/


    private boolean createBillReceipt(ArrayList<OrderHeaderModel> orderHeaderModels, String tableName, float discount, String bill, ArrayList<TaxableDataForBillPrint> taxData) {
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
            textData.append("\t\t\bShree Samarth Juice Center\n");
            mPrinter.addTextAlign(Printer.ALIGN_LEFT);

            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
            Calendar calendar = Calendar.getInstance();

            textData.append("Date :- " + date + "  " + sdf.format(calendar.getTimeInMillis()) + "\n");
            textData.append("Invoice No :- " + bill + "\n");
            textData.append("Table No :- " + tableName + "\n\n");
            textData.append("GSTIN No :- " +"27ABAFS0059G1ZD" + "\n\n");


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


            textData.append("TAX %   ");
            textData.append("        TAXBL VAL");
            textData.append("      CGST");
            textData.append("      SGST\n");

            for (int t = 0; t < taxData.size(); t++) {

                for (int d = 0; d < 25; d++) {
                    textData.append(" ");
                }

                String cgstPerVal = String.format("%.1f", taxData.get(t).getCgst()) + "%";
                if (cgstPerVal.length() >= 10) {
                    String cgstVal1 = cgstPerVal.substring(0, 10);
                    textData.append(cgstVal1);

                } else if (cgstPerVal.length() < 10) {

                    int difference = 10 - cgstPerVal.length();

                    for (int d = 0; d < difference; d++) {
                        textData.append(" ");
                    }

                    textData.append(cgstPerVal);
                }


                String sgstPerVal = String.format("%.1f", taxData.get(t).getSgst()) + "%";
                if (sgstPerVal.length() >= 10) {
                    String sgstVal1 = sgstPerVal.substring(0, 10);
                    textData.append(sgstVal1 + "\n");

                } else if (sgstPerVal.length() < 10) {

                    int difference = 10 - sgstPerVal.length();

                    for (int d = 0; d < difference; d++) {
                        textData.append(" ");
                    }

                    textData.append(sgstPerVal + "\n");
                }


                String gst = taxData.get(t).getGst() + "%";
                if (gst.length() >= 8) {
                    String gst1 = gst.substring(0, 8);
                    textData.append(gst1);

                } else if (gst.length() < 8) {
                    textData.append(gst);
                    int difference = 8 - gst.length();

                    for (int d = 0; d < difference; d++) {
                        textData.append(" ");
                    }
                }

                String taxVal = String.format("%.2f", taxData.get(t).getTaxableAmt());
                if (taxVal.length() >= 17) {
                    String taxVal1 = taxVal.substring(0, 17);
                    textData.append(taxVal1);

                } else if (taxVal.length() < 17) {

                    int difference = 17 - taxVal.length();

                    for (int d = 0; d < difference; d++) {
                        textData.append(" ");
                    }

                    textData.append(taxVal);
                }


                String cgstVal = String.format("%.2f", taxData.get(t).getTotalCgst());
                if (cgstVal.length() >= 10) {
                    String cgstVal1 = cgstVal.substring(0, 10);
                    textData.append(cgstVal1);

                } else if (cgstVal.length() < 10) {

                    int difference = 10 - cgstVal.length();

                    for (int d = 0; d < difference; d++) {
                        textData.append(" ");
                    }

                    textData.append(cgstVal);
                }


                String sgstVal = String.format("%.2f", taxData.get(t).getTotalSgst());
                if (sgstVal.length() >= 10) {
                    String sgstVal1 = sgstVal.substring(0, 10);
                    textData.append(sgstVal1 + "\n");

                } else if (sgstVal.length() < 10) {

                    int difference = 10 - sgstVal.length();

                    for (int d = 0; d < difference; d++) {
                        textData.append(" ");
                    }

                    textData.append(sgstVal + "\n");
                }

            }


            for (int i = 0; i < 45; i++) {
                textData.append("-");
            }
            textData.append("\n");

            String bTot = String.format("%.2f", billTotal);
            textData.append("TOTAL");
            int difference = 40 - bTot.length();
            for (int d = 0; d < difference; d++) {
                textData.append(" ");
            }
            textData.append(bTot + "\n");

            /*String disc = "Discount : " + discount + " %";
            difference = 45 - disc.length();
            for (int d = 0; d < difference; d++) {
                textData.append(" ");
            }
            textData.append(disc + "\n");*/

            for (int i = 0; i < 45; i++) {
                textData.append("-");
            }
            textData.append("\n");

           /* double discAmt = ((billTotal * discount) / 100);
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
            textData.append("\n");*/


            mPrinter.addText(textData.toString());
            Log.e("p\n", "\n\n" + textData.toString());
            mPrinter.addCut(Printer.CUT_FEED);


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

            textData.append("Order No :- " + orderDisplays.getParcelOrderId() + "\n\n");

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

            textData.append("-------------------------------------\n");
            String remark = "Remark :- " + orderItems.get(0).getRemark().replaceAll("(.{30})(?!$)", "$1#");
            // textData.append("Remark :- " + orderItems.get(0).getRemark());
            String[] strArray = remark.split("#");
            for (int i = 0; i < strArray.length; i++) {
                textData.append("" + strArray[i] + "\n");
                Log.e("Remark " + i, "----------------" + strArray[i]);
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
            textData.append("\t\t\bSamarth Juice Center\n\n");
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
            for (int i = 0; i < 2; i++) {
                mPrinter.addText(textData.toString());
                Log.e("Print --", "\n\n" + textData.toString());
                mPrinter.addCut(Printer.CUT_FEED);
            }


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

    /*private boolean createReGeneraterBillReceipt(BillHeaderModel billHeaderModel) {
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
            textData.append("\t\t\bSamarth Juice Center\n");
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
            Log.e("Print --", "\n\n" + textData.toString());
            ShowMsg.showException(e, method, activity, false);

            return false;
        }
        return true;
    }*/

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
            textData.append("\t\t\bSamarth Juice Center\n");
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


            textData.append("TAX %   ");
            textData.append("        TAXBL VAL");
            textData.append("      CGST");
            textData.append("      SGST\n");

            for (int t = 0; t < taxData.size(); t++) {


                for (int d = 0; d < 25; d++) {
                    textData.append(" ");
                }

                String cgstPerVal = String.format("%.1f", taxData.get(t).getCgst()) + "%";
                if (cgstPerVal.length() >= 10) {
                    String cgstVal1 = cgstPerVal.substring(0, 10);
                    textData.append(cgstVal1);

                } else if (cgstPerVal.length() < 10) {

                    int difference = 10 - cgstPerVal.length();

                    for (int d = 0; d < difference; d++) {
                        textData.append(" ");
                    }

                    textData.append(cgstPerVal);
                }


                String sgstPerVal = String.format("%.1f", taxData.get(t).getSgst()) + "%";
                if (sgstPerVal.length() >= 10) {
                    String sgstVal1 = sgstPerVal.substring(0, 10);
                    textData.append(sgstVal1 + "\n");

                } else if (sgstPerVal.length() < 10) {

                    int difference = 10 - sgstPerVal.length();

                    for (int d = 0; d < difference; d++) {
                        textData.append(" ");
                    }

                    textData.append(sgstPerVal + "\n");
                }


                String gst = taxData.get(t).getGst() + "%";
                if (gst.length() >= 8) {
                    String gst1 = gst.substring(0, 8);
                    textData.append(gst1);

                } else if (gst.length() < 8) {
                    textData.append(gst);
                    int difference = 8 - gst.length();

                    for (int d = 0; d < difference; d++) {
                        textData.append(" ");
                    }
                }


                String taxVal = String.format("%.2f", taxData.get(t).getTaxableAmt());
                if (taxVal.length() >= 17) {
                    String taxVal1 = taxVal.substring(0, 17);
                    textData.append(taxVal1);

                } else if (taxVal.length() < 17) {

                    int difference = 17 - taxVal.length();

                    for (int d = 0; d < difference; d++) {
                        textData.append(" ");
                    }

                    textData.append(taxVal);
                }


                String cgstVal = String.format("%.2f", taxData.get(t).getTotalCgst());
                if (cgstVal.length() >= 10) {
                    String cgstVal1 = cgstVal.substring(0, 10);
                    textData.append(cgstVal1);

                } else if (cgstVal.length() < 10) {

                    int difference = 10 - cgstVal.length();

                    for (int d = 0; d < difference; d++) {
                        textData.append(" ");
                    }

                    textData.append(cgstVal);
                }


                String sgstVal = String.format("%.2f", taxData.get(t).getTotalSgst());
                if (sgstVal.length() >= 10) {
                    String sgstVal1 = sgstVal.substring(0, 10);
                    textData.append(sgstVal1 + "\n");

                } else if (sgstVal.length() < 10) {

                    int difference = 10 - sgstVal.length();

                    for (int d = 0; d < difference; d++) {
                        textData.append(" ");
                    }

                    textData.append(sgstVal + "\n");
                }

            }


            for (int i = 0; i < 45; i++) {
                textData.append("-");
            }
            textData.append("\n");


            String bTot = "" + billHeaderModel.getGrandTotal();
            textData.append("TOTAL");
            int difference = 40 - bTot.length();
            for (int d = 0; d < difference; d++) {
                textData.append(" ");
            }
            textData.append(bTot + "\n");


            for (int i = 0; i < 45; i++) {
                textData.append("-");
            }
            textData.append("\n");

           /* String disc = "Discount : " + billHeaderModel.getDiscount() + " %";
            difference = 45 - disc.length();
            for (int d = 0; d < difference; d++) {
                textData.append(" ");
            }
            textData.append(disc + "\n");

            for (int i = 0; i < 45; i++) {
                textData.append("-");
            }
            textData.append("\n");*/

          /*  String grandTotal = "GRAND TOTAL : " + billHeaderModel.getPayableAmt();
            difference = 45 - grandTotal.length();
            for (int d = 0; d < difference; d++) {
                textData.append(" ");
            }
            textData.append(grandTotal + "\n");

            for (int i = 0; i < 45; i++) {
                textData.append("-");
            }
            textData.append("\n");*/


            mPrinter.addText(textData.toString());
            Log.e("Print --", "\n\n" + textData.toString());
            mPrinter.addCut(Printer.CUT_FEED);


        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Print --", "\n\n" + textData.toString());
            ShowMsg.showException(e, method, activity, false);

            return false;
        }
        return true;
    }


    private boolean createTaxPrint(ArrayList<TaxableDataForBillPrint> taxData) {
        String method = "";
        StringBuilder textData = new StringBuilder();

        try {
            ArrayList<BillDetail> orderItems = new ArrayList<>();

            method = "addTextAlign";
            mPrinter.addTextAlign(Printer.ALIGN_CENTER);
            method = "addFeedLine";
            mPrinter.addFeedLine(1);
          //  textData.append("\t\t\bSamarth Juice Center\n");
//            textData.append("\t\t\bTAX REPORT\n");
            mPrinter.addTextAlign(Printer.ALIGN_LEFT);

            if (taxData != null) {
                if (taxData.size() > 0) {

                    TreeSet<String> uniqueDate = new TreeSet<>();

                    for (int i = 0; i < taxData.size(); i++) {

                        uniqueDate.add(taxData.get(i).getBillDate());

                    }

                    Log.e("TAX DATE", "------------PRINT------------- " + uniqueDate);

                    ArrayList<String> billDateArray = new ArrayList<>();
                    billDateArray.addAll(uniqueDate);

                    if (billDateArray.size() > 0) {
                        for (int i = 0; i < billDateArray.size(); i++) {

                            textData.append("\t\t\bTAX REPORT\n");

                            float totalCgstVal = 0, totalSgstVal = 0, totalTaxblVal = 0;

                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
                            SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");

                            Date d = sdf1.parse(billDateArray.get(i));

                            textData.append("DATE : " + sdf.format(d.getTime()) + "\t\t TIME : " + sdf2.format(Calendar.getInstance().getTimeInMillis()) + "\n");

                            for (int l = 0; l < 45; l++) {
                                textData.append("-");
                            }
                            textData.append("\n");

                            textData.append("AMOUNT       ");
                            textData.append("   TAX %");
                            textData.append("        CGST");
                            textData.append("        SGST\n");


                            for (int l = 0; l < 45; l++) {
                                textData.append("-");
                            }
                            textData.append("\n");

                            for (int j = 0; j < taxData.size(); j++) {

                                if (billDateArray.get(i).equalsIgnoreCase(taxData.get(j).getBillDate())) {

                                    totalCgstVal = totalCgstVal + taxData.get(j).getTotalCgst();
                                    totalSgstVal = totalSgstVal + taxData.get(j).getTotalSgst();
                                    totalTaxblVal = totalTaxblVal + taxData.get(j).getTaxableAmt();

                                    String strTaxblAmt = String.format("%.2f", taxData.get(j).getTaxableAmt());
                                    if (strTaxblAmt.length() >= 13) {
                                        String txblAmt = orderItems.get(i).getItemName().substring(0, 13);
                                        textData.append(txblAmt);

                                    } else if (strTaxblAmt.length() < 13) {
                                        textData.append(strTaxblAmt);
                                        int difference = 13 - strTaxblAmt.length();

                                        for (int df = 0; df < difference; df++) {
                                            textData.append(" ");
                                        }
                                    }


                                    String gst = taxData.get(j).getGst() + "%";
                                    if (gst.length() >= 8) {
                                        String gst1 = gst.substring(0, 8);
                                        textData.append(gst1);

                                    } else if (gst.length() < 8) {

                                        int difference = 8 - gst.length();
                                        for (int df = 0; df < difference; df++) {
                                            textData.append(" ");
                                        }
                                        textData.append(gst);
                                    }


                                    String cgstVal = String.format("%.2f", taxData.get(j).getTotalCgst());
                                    if (cgstVal.length() >= 12) {
                                        String cgstVal1 = cgstVal.substring(0, 12);
                                        textData.append(cgstVal1);

                                    } else if (cgstVal.length() < 12) {

                                        int difference = 12 - cgstVal.length();

                                        for (int df = 0; df < difference; df++) {
                                            textData.append(" ");
                                        }

                                        textData.append(cgstVal);
                                    }


                                    String sgstVal = String.format("%.2f", taxData.get(j).getTotalSgst());
                                    if (sgstVal.length() >= 12) {
                                        String sgstVal1 = sgstVal.substring(0, 12);
                                        textData.append(sgstVal1 + "\n");

                                    } else if (sgstVal.length() < 12) {

                                        int difference = 12 - sgstVal.length();

                                        for (int df = 0; df < difference; df++) {
                                            textData.append(" ");
                                        }

                                        textData.append(sgstVal + "\n");
                                    }

                                }

                            }

                            for (int l = 0; l < 45; l++) {
                                textData.append("-");
                            }
                            textData.append("\n");


                            textData.append("TOTAL CGST");

                            String totCgstVal = String.format("%.2f", totalCgstVal);
                            int difference = 35 - totCgstVal.length();
                            for (int df = 0; df < difference; df++) {
                                textData.append(" ");
                            }
                            textData.append(totCgstVal + "\n");

                            textData.append("TOTAL SGST");
                            String totSgstVal = String.format("%.2f", totalSgstVal);
                            int difference1 = 35 - totSgstVal.length();
                            for (int df = 0; df < difference1; df++) {
                                textData.append(" ");
                            }
                            textData.append(totSgstVal + "\n");

                            float total=totalCgstVal+totalSgstVal;
                            textData.append("TOTAL TAX");
                            String totTaxblVal = String.format("%.2f", total);
                            int difference2 = 36 - totTaxblVal.length();
                            for (int df = 0; df < difference2; df++) {
                                textData.append(" ");
                            }
                            textData.append(totTaxblVal + "\n");

                            for (int l = 0; l < 45; l++) {
                                textData.append("-");
                            }
                            textData.append("\n");


                            mPrinter.addText(textData.toString());
                            Log.e("Print --", "\n\n" + textData.toString());
                            mPrinter.addCut(Printer.CUT_FEED);
                        }
                    }


                }
            }




        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Print --", "\n\n" + textData.toString());
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
