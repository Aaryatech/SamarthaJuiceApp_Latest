package com.ats.samarthajuice.model;

public class ParcelOrderDetails {

    private int parcelOrderDetailId;
    private int parcelOrderId;
    private int itemId;
    private int quantity;
    private float rate;
    private int status;
    private int isMixer;
    private String remark;
    private String itemName;

    public ParcelOrderDetails(int parcelOrderDetailId, int parcelOrderId, int itemId, int quantity, float rate, int status, int isMixer, String remark) {
        this.parcelOrderDetailId = parcelOrderDetailId;
        this.parcelOrderId = parcelOrderId;
        this.itemId = itemId;
        this.quantity = quantity;
        this.rate = rate;
        this.status = status;
        this.isMixer = isMixer;
        this.remark = remark;
    }

    public ParcelOrderDetails(int parcelOrderDetailId, int parcelOrderId, int itemId, int quantity, float rate, int status, int isMixer, String remark, String itemName) {
        this.parcelOrderDetailId = parcelOrderDetailId;
        this.parcelOrderId = parcelOrderId;
        this.itemId = itemId;
        this.quantity = quantity;
        this.rate = rate;
        this.status = status;
        this.isMixer = isMixer;
        this.remark = remark;
        this.itemName = itemName;
    }

    public int getParcelOrderDetailId() {
        return parcelOrderDetailId;
    }

    public void setParcelOrderDetailId(int parcelOrderDetailId) {
        this.parcelOrderDetailId = parcelOrderDetailId;
    }

    public int getParcelOrderId() {
        return parcelOrderId;
    }

    public void setParcelOrderId(int parcelOrderId) {
        this.parcelOrderId = parcelOrderId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
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

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    @Override
    public String toString() {
        return "ParcelOrderDetails{" +
                "parcelOrderDetailId=" + parcelOrderDetailId +
                ", parcelOrderId=" + parcelOrderId +
                ", itemId=" + itemId +
                ", quantity=" + quantity +
                ", rate=" + rate +
                ", status=" + status +
                ", isMixer=" + isMixer +
                ", remark='" + remark + '\'' +
                ", itemName='" + itemName + '\'' +
                '}';
    }
}
