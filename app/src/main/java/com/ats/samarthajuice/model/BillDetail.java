package com.ats.samarthajuice.model;

public class BillDetail {

    private int billDetailsId;
    private int billId;
    private int orderId;
    private int delStatus;
    private int itemId;
    private String itemName;
    private int quantity;
    private float rate;
    private float sgst;
    private float cgst;
    private float total;
    private float taxableAmt;
    private float totalTax;

    public BillDetail(int billDetailsId, int billId, int orderId, int delStatus, int itemId, String itemName, int quantity, float rate, float sgst, float cgst, float total, float taxableAmt, float totalTax) {
        this.billDetailsId = billDetailsId;
        this.billId = billId;
        this.orderId = orderId;
        this.delStatus = delStatus;
        this.itemId = itemId;
        this.itemName = itemName;
        this.quantity = quantity;
        this.rate = rate;
        this.sgst = sgst;
        this.cgst = cgst;
        this.total = total;
        this.taxableAmt = taxableAmt;
        this.totalTax = totalTax;
    }

    public int getBillDetailsId() {
        return billDetailsId;
    }

    public void setBillDetailsId(int billDetailsId) {
        this.billDetailsId = billDetailsId;
    }

    public int getBillId() {
        return billId;
    }

    public void setBillId(int billId) {
        this.billId = billId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getDelStatus() {
        return delStatus;
    }

    public void setDelStatus(int delStatus) {
        this.delStatus = delStatus;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
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

    public float getSgst() {
        return sgst;
    }

    public void setSgst(float sgst) {
        this.sgst = sgst;
    }

    public float getCgst() {
        return cgst;
    }

    public void setCgst(float cgst) {
        this.cgst = cgst;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public float getTaxableAmt() {
        return taxableAmt;
    }

    public void setTaxableAmt(float taxableAmt) {
        this.taxableAmt = taxableAmt;
    }

    public float getTotalTax() {
        return totalTax;
    }

    public void setTotalTax(float totalTax) {
        this.totalTax = totalTax;
    }

    @Override
    public String toString() {
        return "BillDetail{" +
                "billDetailsId=" + billDetailsId +
                ", billId=" + billId +
                ", orderId=" + orderId +
                ", delStatus=" + delStatus +
                ", itemId=" + itemId +
                ", itemName='" + itemName + '\'' +
                ", quantity=" + quantity +
                ", rate=" + rate +
                ", sgst=" + sgst +
                ", cgst=" + cgst +
                ", total=" + total +
                ", taxableAmt=" + taxableAmt +
                ", totalTax=" + totalTax +
                '}';
    }
}
