package com.ats.taborderingsystem.model;

public class DateWiseReportModel {

    private int billId;
    private String billDate;
    private float total;
    private float payableAmount;

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

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public float getPayableAmount() {
        return payableAmount;
    }

    public void setPayableAmount(float payableAmount) {
        this.payableAmount = payableAmount;
    }

    @Override
    public String toString() {
        return "DateWiseReportModel{" +
                "billId=" + billId +
                ", billDate='" + billDate + '\'' +
                ", total=" + total +
                ", payableAmount=" + payableAmount +
                '}';
    }
}
