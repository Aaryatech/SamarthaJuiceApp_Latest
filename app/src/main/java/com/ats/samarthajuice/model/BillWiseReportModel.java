package com.ats.samarthajuice.model;

public class BillWiseReportModel {

    private int billId;
    private String billNo;
    private String billDate;
    private float cgst;
    private float sgst;
    private float taxableAmount;
    private float discount;
    private float grandTotal;
    private float payableAmount;

    public int getBillId() {
        return billId;
    }

    public void setBillId(int billId) {
        this.billId = billId;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getBillDate() {
        return billDate;
    }

    public void setBillDate(String billDate) {
        this.billDate = billDate;
    }

    public float getCgst() {
        return cgst;
    }

    public void setCgst(float cgst) {
        this.cgst = cgst;
    }

    public float getSgst() {
        return sgst;
    }

    public void setSgst(float sgst) {
        this.sgst = sgst;
    }

    public float getTaxableAmount() {
        return taxableAmount;
    }

    public void setTaxableAmount(float taxableAmount) {
        this.taxableAmount = taxableAmount;
    }

    public float getDiscount() {
        return discount;
    }

    public void setDiscount(float discount) {
        this.discount = discount;
    }

    public float getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(float grandTotal) {
        this.grandTotal = grandTotal;
    }

    public float getPayableAmount() {
        return payableAmount;
    }

    public void setPayableAmount(float payableAmount) {
        this.payableAmount = payableAmount;
    }

    @Override
    public String toString() {
        return "BillWiseReportModel{" +
                "billId=" + billId +
                ", billNo='" + billNo + '\'' +
                ", billDate='" + billDate + '\'' +
                ", cgst=" + cgst +
                ", sgst=" + sgst +
                ", taxableAmount=" + taxableAmount +
                ", discount=" + discount +
                ", grandTotal=" + grandTotal +
                ", payableAmount=" + payableAmount +
                '}';
    }
}


