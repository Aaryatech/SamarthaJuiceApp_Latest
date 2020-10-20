package com.ats.samarthajuice.model;

public class TableFreeModel {

    private int tableId;
    private int tableNo;
    private String tableName;
    private int isDelete;
    private int isActive;
    private int userId;
    private String updatedDate;

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

    @Override
    public String toString() {
        return "TableFreeModel{" +
                "tableId=" + tableId +
                ", tableNo=" + tableNo +
                ", tableName='" + tableName + '\'' +
                ", isDelete=" + isDelete +
                ", isActive=" + isActive +
                ", userId=" + userId +
                ", updatedDate='" + updatedDate + '\'' +
                '}';
    }
}
