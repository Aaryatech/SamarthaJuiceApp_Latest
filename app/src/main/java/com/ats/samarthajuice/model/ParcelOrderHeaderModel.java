package com.ats.samarthajuice.model;

import java.util.List;

public class ParcelOrderHeaderModel {


    private int parcelOrderId;
    private int userId;
    private String name;
    private String mobileNo;
    private int billStatus;
    private String orderDate;
    private String orderDateTime;
    private int delStatus;
    List<ParcelOrderDetails> parcelOrderDetailsList;

    public ParcelOrderHeaderModel(int parcelOrderId, int userId, String name, String mobileNo, int billStatus, String orderDate, String orderDateTime, int delStatus, List<ParcelOrderDetails> parcelOrderDetailsList) {
        this.parcelOrderId = parcelOrderId;
        this.userId = userId;
        this.name = name;
        this.mobileNo = mobileNo;
        this.billStatus = billStatus;
        this.orderDate = orderDate;
        this.orderDateTime = orderDateTime;
        this.delStatus = delStatus;
        this.parcelOrderDetailsList = parcelOrderDetailsList;
    }

    public int getParcelOrderId() {
        return parcelOrderId;
    }

    public void setParcelOrderId(int parcelOrderId) {
        this.parcelOrderId = parcelOrderId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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

    public int getBillStatus() {
        return billStatus;
    }

    public void setBillStatus(int billStatus) {
        this.billStatus = billStatus;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderDateTime() {
        return orderDateTime;
    }

    public void setOrderDateTime(String orderDateTime) {
        this.orderDateTime = orderDateTime;
    }

    public int getDelStatus() {
        return delStatus;
    }

    public void setDelStatus(int delStatus) {
        this.delStatus = delStatus;
    }

    public List<ParcelOrderDetails> getParcelOrderDetailsList() {
        return parcelOrderDetailsList;
    }

    public void setParcelOrderDetailsList(List<ParcelOrderDetails> parcelOrderDetailsList) {
        this.parcelOrderDetailsList = parcelOrderDetailsList;
    }

    @Override
    public String toString() {
        return "ParcelOrderHeaderModel{" +
                "parcelOrderId=" + parcelOrderId +
                ", userId=" + userId +
                ", name='" + name + '\'' +
                ", mobileNo='" + mobileNo + '\'' +
                ", billStatus=" + billStatus +
                ", orderDate='" + orderDate + '\'' +
                ", orderDateTime='" + orderDateTime + '\'' +
                ", delStatus=" + delStatus +
                ", parcelOrderDetailsList=" + parcelOrderDetailsList +
                '}';
    }
}
