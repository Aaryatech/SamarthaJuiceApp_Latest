package com.ats.samarthajuice.model;

public class OrderDetailsList {

    private int orderDetailsId;
    private int orderId;
    private int itemId;
    private String itemName;
    private int quantity;
    private float rate;
    private int status;
    private int isMixer;
    private String remark;
    float total;
    boolean isChecked;

    public OrderDetailsList(int orderId, int itemId, int quantity, float rate, int status, int isMixer, String remark) {
        this.orderId = orderId;
        this.itemId = itemId;
        this.quantity = quantity;
        this.rate = rate;
        this.status = status;
        this.isMixer = isMixer;
        this.remark = remark;
    }

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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getIsMixer() {
        return isMixer;
    }

    public void setIsMixer(int isMixer) {
        this.isMixer = isMixer;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    @Override
    public String toString() {
        return "OrderDetailsList{" +
                "orderDetailsId=" + orderDetailsId +
                ", orderId=" + orderId +
                ", itemId=" + itemId +
                ", itemName='" + itemName + '\'' +
                ", quantity=" + quantity +
                ", rate=" + rate +
                ", status=" + status +
                ", isMixer=" + isMixer +
                ", remark='" + remark + '\'' +
                ", total=" + total +
                ", isChecked=" + isChecked +
                '}';
    }
}
