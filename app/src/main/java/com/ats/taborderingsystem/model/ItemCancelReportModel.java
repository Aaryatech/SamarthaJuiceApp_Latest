package com.ats.taborderingsystem.model;

public class ItemCancelReportModel {

    private String itemName;
    private String chargable;
    private String nc1;
    private String nc2;
    private String nc3;


    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getChargable() {
        return chargable;
    }

    public void setChargable(String chargable) {
        this.chargable = chargable;
    }

    public String getNc1() {
        return nc1;
    }

    public void setNc1(String nc1) {
        this.nc1 = nc1;
    }

    public String getNc2() {
        return nc2;
    }

    public void setNc2(String nc2) {
        this.nc2 = nc2;
    }

    public String getNc3() {
        return nc3;
    }

    public void setNc3(String nc3) {
        this.nc3 = nc3;
    }

    @Override
    public String toString() {
        return "ItemCancelReportModel{" +
                "itemName='" + itemName + '\'' +
                ", chargable='" + chargable + '\'' +
                ", nc1='" + nc1 + '\'' +
                ", nc2='" + nc2 + '\'' +
                ", nc3='" + nc3 + '\'' +
                '}';
    }

}
