package com.ats.samarthajuice.model;

import java.util.List;

public class BillHeaderModel {

    private int billId;
    private String billDate;
    private int delStatus;
    private int userId;
    private int enterBy;
    private int billClose;
    private float discount;
    private float grandTotal;
    private float payableAmt;
    private int tableNo;
    private String billNo;
    private int venueId;
    private float cgst;
    private float sgst;
    private float taxableAmount;
    private String name;
    private String mobileNo;
    List<BillDetail> billDetails;

    public BillHeaderModel(int billId, String billDate, int delStatus, int userId, int enterBy, int billClose, float discount, float grandTotal, float payableAmt, int tableNo, String billNo, int venueId, float cgst, float sgst, float taxableAmount, String name, String mobileNo, List<BillDetail> billDetails) {
        this.billId = billId;
        this.billDate = billDate;
        this.delStatus = delStatus;
        this.userId = userId;
        this.enterBy = enterBy;
        this.billClose = billClose;
        this.discount = discount;
        this.grandTotal = grandTotal;
        this.payableAmt = payableAmt;
        this.tableNo = tableNo;
        this.billNo = billNo;
        this.venueId = venueId;
        this.cgst = cgst;
        this.sgst = sgst;
        this.taxableAmount = taxableAmount;
        this.name = name;
        this.mobileNo = mobileNo;
        this.billDetails = billDetails;
    }

    public int getBillId() {
        return billId;
    }

    public void setBillId(int billId) {
        this.billId = billId;
    }

    public String getBillDate() {
        return billDate;
    }

    public void setBillDate(String billDate) {
        this.billDate = billDate;
    }

    public int getDelStatus() {
        return delStatus;
    }

    public void setDelStatus(int delStatus) {
        this.delStatus = delStatus;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getEnterBy() {
        return enterBy;
    }

    public void setEnterBy(int enterBy) {
        this.enterBy = enterBy;
    }

    public int getBillClose() {
        return billClose;
    }

    public void setBillClose(int billClose) {
        this.billClose = billClose;
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

    public float getPayableAmt() {
        return payableAmt;
    }

    public void setPayableAmt(float payableAmt) {
        this.payableAmt = payableAmt;
    }

    public int getTableNo() {
        return tableNo;
    }

    public void setTableNo(int tableNo) {
        this.tableNo = tableNo;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public int getVenueId() {
        return venueId;
    }

    public void setVenueId(int venueId) {
        this.venueId = venueId;
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

    public List<BillDetail> getBillDetails() {
        return billDetails;
    }

    public void setBillDetails(List<BillDetail> billDetails) {
        this.billDetails = billDetails;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    @Override
    public String toString() {
        return "BillHeaderModel{" +
                "billId=" + billId +
                ", billDate='" + billDate + '\'' +
                ", delStatus=" + delStatus +
                ", userId=" + userId +
                ", enterBy=" + enterBy +
                ", billClose=" + billClose +
                ", discount=" + discount +
                ", grandTotal=" + grandTotal +
                ", payableAmt=" + payableAmt +
                ", tableNo=" + tableNo +
                ", billNo='" + billNo + '\'' +
                ", venueId=" + venueId +
                ", cgst=" + cgst +
                ", sgst=" + sgst +
                ", taxableAmount=" + taxableAmount +
                ", billDetails=" + billDetails +
                ", name='" + name + '\'' +
                ", mobileNo='" + mobileNo + '\'' +
                '}';
    }
}
