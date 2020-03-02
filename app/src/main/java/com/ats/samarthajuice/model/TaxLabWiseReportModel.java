package com.ats.samarthajuice.model;

public class TaxLabWiseReportModel {

    private int billDetailsId;
    private float tax;
    private float taxableAmount;
    private float totalTax;

    public int getBillDetailsId() {
        return billDetailsId;
    }

    public void setBillDetailsId(int billDetailsId) {
        this.billDetailsId = billDetailsId;
    }

    public float getTax() {
        return tax;
    }

    public void setTax(float tax) {
        this.tax = tax;
    }

    public float getTaxableAmount() {
        return taxableAmount;
    }

    public void setTaxableAmount(float taxableAmount) {
        this.taxableAmount = taxableAmount;
    }

    public float getTotalTax() {
        return totalTax;
    }

    public void setTotalTax(float totalTax) {
        this.totalTax = totalTax;
    }

    @Override
    public String toString() {
        return "TaxLabWiseReportModel{" +
                "billDetailsId=" + billDetailsId +
                ", tax=" + tax +
                ", taxableAmount=" + taxableAmount +
                ", totalTax=" + totalTax +
                '}';
    }

}
