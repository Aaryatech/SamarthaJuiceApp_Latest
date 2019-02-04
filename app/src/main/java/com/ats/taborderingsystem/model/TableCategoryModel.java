package com.ats.taborderingsystem.model;

public class TableCategoryModel {

    private int tableCatId;
    private String tableCatName;
    private String tableCatDesc;
    private int isActive;

    public TableCategoryModel(int tableCatId, String tableCatName, String tableCatDesc, int isActive) {
        this.tableCatId = tableCatId;
        this.tableCatName = tableCatName;
        this.tableCatDesc = tableCatDesc;
        this.isActive = isActive;
    }

    public int getTableCatId() {
        return tableCatId;
    }

    public void setTableCatId(int tableCatId) {
        this.tableCatId = tableCatId;
    }

    public String getTableCatName() {
        return tableCatName;
    }

    public void setTableCatName(String tableCatName) {
        this.tableCatName = tableCatName;
    }

    public String getTableCatDesc() {
        return tableCatDesc;
    }

    public void setTableCatDesc(String tableCatDesc) {
        this.tableCatDesc = tableCatDesc;
    }

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }

    @Override
    public String toString() {
        return "TableCategoryModel{" +
                "tableCatId=" + tableCatId +
                ", tableCatName='" + tableCatName + '\'' +
                ", tableCatDesc='" + tableCatDesc + '\'' +
                ", isActive=" + isActive +
                '}';
    }
}
