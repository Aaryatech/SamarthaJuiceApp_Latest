package com.ats.taborderingsystem.model;

public class ItemHSNCodeReportModel {

    private String hsnCode;
    private float cgst;
    private float sgst;
    private float totalTax;
    private float taxableAmount;

    public String getHsnCode() {
        return hsnCode;
    }

    public void setHsnCode(String hsnCode) {
        this.hsnCode = hsnCode;
    }

    public float getCgst() {
        return cgst;
    }

    public void setCgst(float cgst) {
        this.cgst = cgst;
    }

    public float getSgst() {
        return sgst;
    }

    public void setSgst(float sgst) {
        this.sgst = sgst;
    }

    public float getTotalTax() {
        return totalTax;
    }

    public void setTotalTax(float totalTax) {
        this.totalTax = totalTax;
    }

    public float getTaxableAmount() {
        return taxableAmount;
    }

    public void setTaxableAmount(float taxableAmount) {
        this.taxableAmount = taxableAmount;
    }

    @Override
    public String toString() {
        return "ItemHSNCodeReportModel{" +
                "hsnCode='" + hsnCode + '\'' +
                ", cgst=" + cgst +
                ", sgst=" + sgst +
                ", totalTax=" + totalTax +
                ", taxableAmount=" + taxableAmount +
                '}';
    }
}
