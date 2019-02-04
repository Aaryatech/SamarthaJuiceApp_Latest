package com.ats.taborderingsystem.model;

public class TableWiseReportModel {

    private int billId;
    private int tableNo;
    private String tableName;
    private float total;
    private float payableAmount;

    public int getBillId() {
        return billId;
    }

    public void setBillId(int billId) {
        this.billId = billId;
    }

    public int getTableNo() {
        return tableNo;
    }

    public void setTableNo(int tableNo) {
        this.tableNo = tableNo;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
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
        return "TableWiseReportModel{" +
                "billId=" + billId +
                ", tableNo=" + tableNo +
                ", tableName='" + tableName + '\'' +
                ", total=" + total +
                ", payableAmount=" + payableAmount +
                '}';
    }
}
