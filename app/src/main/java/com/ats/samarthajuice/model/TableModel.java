package com.ats.samarthajuice.model;

public class TableModel {

    private int tableId;
    private int tableNo;
    private String tableName;
    private int isDelete;
    private int isActive;
    private int userId;
    private String updatedDate;
    private int venueId;

    public TableModel(int tableId, int tableNo, String tableName, int isDelete, int isActive, int userId, String updatedDate) {
        this.tableId = tableId;
        this.tableNo = tableNo;
        this.tableName = tableName;
        this.isDelete = isDelete;
        this.isActive = isActive;
        this.userId = userId;
        this.updatedDate = updatedDate;
    }

    public TableModel(int tableId, int tableNo, String tableName, int isDelete, int isActive, int userId, String updatedDate, int venueId) {
        this.tableId = tableId;
        this.tableNo = tableNo;
        this.tableName = tableName;
        this.isDelete = isDelete;
        this.isActive = isActive;
        this.userId = userId;
        this.updatedDate = updatedDate;
        this.venueId = venueId;
    }

    public int getTableId() {
        return tableId;
    }

    public void setTableId(int tableId) {
        this.tableId = tableId;
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

    public int getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(int isDelete) {
        this.isDelete = isDelete;
    }

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

    public int getVenueId() {
        return venueId;
    }

    public void setVenueId(int venueId) {
        this.venueId = venueId;
    }

    @Override
    public String toString() {
        return "TableModel{" +
                "tableId=" + tableId +
                ", tableNo=" + tableNo +
                ", tableName='" + tableName + '\'' +
                ", isDelete=" + isDelete +
                ", isActive=" + isActive +
                ", userId=" + userId +
                ", updatedDate='" + updatedDate + '\'' +
                ", venueId=" + venueId +
                '}';
    }
}
