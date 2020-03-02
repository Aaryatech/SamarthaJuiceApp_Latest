package com.ats.samarthajuice.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DateWiseReportModel {

    @SerializedName("billId")
    @Expose
    private Integer billId;
    @SerializedName("billDate")
    @Expose
    private String billDate;
    @SerializedName("total")
    @Expose
    private float total;
    @SerializedName("payableAmount")
    @Expose
    private float payableAmount;
    @SerializedName("type")
    @Expose
    private Integer type;

    public Integer getBillId() {
        return billId;
    }

    public void setBillId(Integer billId) {
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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "DateWiseReportModel{" +
                "billId=" + billId +
                ", billDate='" + billDate + '\'' +
                ", total=" + total +
                ", payableAmount=" + payableAmount +
                ", type=" + type +
                '}';
    }
}
