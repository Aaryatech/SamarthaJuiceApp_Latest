package com.ats.samarthajuice.model;

public class DateWiseItemReportModel {

    private int billDetailsId;
    private String itemName;
    private String billDate;
    private int quantity;
    private float total;
    private float payableAmt;

    public int getBillDetailsId() {
        return billDetailsId;
    }

    public void setBillDetailsId(int billDetailsId) {
        this.billDetailsId = billDetailsId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getBillDate() {
        return billDate;
    }

    public void setBillDate(String billDate) {
        this.billDate = billDate;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public float getPayableAmt() {
        return payableAmt;
    }

    public void setPayableAmt(float payableAmt) {
        this.payableAmt = payableAmt;
    }

    @Override
    public String toString() {
        return "DateWiseItemReportModel{" +
                "billDetailsId=" + billDetailsId +
                ", itemName='" + itemName + '\'' +
                ", billDate='" + billDate + '\'' +
                ", quantity=" + quantity +
                ", total=" + total +
                ", payableAmt=" + payableAmt +
                '}';
    }
}
