package com.ats.samarthajuice.printer;

/**
 * Created by DeepakD on 12-06-2016.
 */
public interface PrintReceiptType
{
    int BILL=0;
    int KOT=1;
    int TEST=2;
    int BILL_PARCEL=3;
    int KOT_PARCEL=4;
    int VOID_KOT=5;
    int RE_GENERATE_BILL=6;
    int TAX_PRINT=7;
}
