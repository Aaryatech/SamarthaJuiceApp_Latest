package com.ats.taborderingsystem.model;

public class ItemReportModel {

    private int billDetailsId;
    private String itemName;
    private int quantity;
    private float rate;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
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
        return "ItemReportModel{" +
                "billDetailsId=" + billDetailsId +
                ", itemName='" + itemName + '\'' +
                ", quantity=" + quantity +
                ", rate=" + rate +
                ", total=" + total +
                ", payableAmt=" + payableAmt +
                '}';
    }
}
