package com.ats.samarthajuice.model;

public class TaxableDataForBillPrint {

    private String id;
    private String billDate;
    private float gst;
    private float sgst;
    private float cgst;
    private float taxableAmt;
    private float totalSgst;
    private float totalCgst;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBillDate() {
        return billDate;
    }

    public void setBillDate(String billDate) {
        this.billDate = billDate;
    }

    public float getGst() {
        return gst;
    }

    public void setGst(float gst) {
        this.gst = gst;
    }

    public float getSgst() {
        return sgst;
    }

    public void setSgst(float sgst) {
        this.sgst = sgst;
    }

    public float getCgst() {
        return cgst;
    }

    public void setCgst(float cgst) {
        this.cgst = cgst;
    }

    public float getTaxableAmt() {
        return taxableAmt;
    }

    public void setTaxableAmt(float taxableAmt) {
        this.taxableAmt = taxableAmt;
    }

    public float getTotalSgst() {
        return totalSgst;
    }

    public void setTotalSgst(float totalSgst) {
        this.totalSgst = totalSgst;
    }

    public float getTotalCgst() {
        return totalCgst;
    }

    public void setTotalCgst(float totalCgst) {
        this.totalCgst = totalCgst;
    }


    @Override
    public String toString() {
        return "TaxableDataForBillPrint{" +
                "id='" + id + '\'' +
                ", billDate='" + billDate + '\'' +
                ", gst=" + gst +
                ", sgst=" + sgst +
                ", cgst=" + cgst +
                ", taxableAmt=" + taxableAmt +
                ", totalSgst=" + totalSgst +
                ", totalCgst=" + totalCgst +
                '}';
    }
}
