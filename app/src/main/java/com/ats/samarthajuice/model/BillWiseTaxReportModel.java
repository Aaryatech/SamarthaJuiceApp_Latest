package com.ats.samarthajuice.model;

public class BillWiseTaxReportModel {

    private int billDetailsId;
    private int billId;
    private String billNo;
    private String billDate;
    private float tax;
    private float taxableAmount;
    private float totalTax;

    public int getBillDetailsId() {
        return billDetailsId;
    }

    public void setBillDetailsId(int billDetailsId) {
        this.billDetailsId = billDetailsId;
    }

    public int getBillId() {
        return billId;
    }

    public void setBillId(int billId) {
        this.billId = billId;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getBillDate() {
        return billDate;
    }

    public void setBillDate(String billDate) {
        this.billDate = billDate;
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
        return "BillWiseTaxReportModel{" +
                "billDetailsId=" + billDetailsId +
                ", billId=" + billId +
                ", billNo='" + billNo + '\'' +
                ", billDate='" + billDate + '\'' +
                ", tax=" + tax +
                ", taxableAmount=" + taxableAmount +
                ", totalTax=" + totalTax +
                '}';
    }
}
