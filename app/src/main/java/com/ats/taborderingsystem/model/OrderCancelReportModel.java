package com.ats.taborderingsystem.model;

public class OrderCancelReportModel {

    private int orderDetailsId;
    private int orderId;
    private String itemName;
    private int quantity;
    private int status;
    private String remark;

    public int getOrderDetailsId() {
        return orderDetailsId;
    }

    public void setOrderDetailsId(int orderDetailsId) {
        this.orderDetailsId = orderDetailsId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "OrderCancelReportModel{" +
                "orderDetailsId=" + orderDetailsId +
                ", orderId=" + orderId +
                ", itemName='" + itemName + '\'' +
                ", quantity=" + quantity +
                ", status=" + status +
                ", remark='" + remark + '\'' +
                '}';
    }
}
