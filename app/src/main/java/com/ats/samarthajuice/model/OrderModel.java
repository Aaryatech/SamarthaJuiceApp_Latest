package com.ats.samarthajuice.model;

import java.util.List;

public class OrderModel {

    private int orderId;
    private int userId;
    private int tableNo;
    private int billStatus;
    private String orderDate;
    private String orderDateTime;
    private int delStatus;
    List<OrderDetails> orderDetailList;

    public OrderModel(int orderId, int userId, int tableNo, int billStatus, String orderDate, String orderDateTime, int delStatus, List<OrderDetails> orderDetailList) {
        this.orderId = orderId;
        this.userId = userId;
        this.tableNo = tableNo;
        this.billStatus = billStatus;
        this.orderDate = orderDate;
        this.orderDateTime = orderDateTime;
        this.delStatus = delStatus;
        this.orderDetailList = orderDetailList;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getTableNo() {
        return tableNo;
    }

    public void setTableNo(int tableNo) {
        this.tableNo = tableNo;
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

    public List<OrderDetails> getOrderDetailList() {
        return orderDetailList;
    }

    public void setOrderDetailList(List<OrderDetails> orderDetailList) {
        this.orderDetailList = orderDetailList;
    }

    @Override
    public String toString() {
        return "OrderModel{" +
                "orderId=" + orderId +
                ", userId=" + userId +
                ", tableNo=" + tableNo +
                ", billStatus=" + billStatus +
                ", orderDate='" + orderDate + '\'' +
                ", orderDateTime='" + orderDateTime + '\'' +
                ", delStatus=" + delStatus +
                ", orderDetailList=" + orderDetailList +
                '}';
    }
}
