package com.ats.samarthajuice.interfaces;

import com.ats.samarthajuice.model.AdminModel;
import com.ats.samarthajuice.model.BillHeaderModel;
import com.ats.samarthajuice.model.BillWiseReportModel;
import com.ats.samarthajuice.model.BillWiseTaxReportModel;
import com.ats.samarthajuice.model.CancelMessageModel;
import com.ats.samarthajuice.model.CategoryItemModel;
import com.ats.samarthajuice.model.CategoryMenuModel;
import com.ats.samarthajuice.model.CategoryModel;
import com.ats.samarthajuice.model.CategoryWiseReportModel;
import com.ats.samarthajuice.model.DateWiseItemReportModel;
import com.ats.samarthajuice.model.DateWiseReportModel;
import com.ats.samarthajuice.model.ErrorMessage;
import com.ats.samarthajuice.model.ItemCancelReportModel;
import com.ats.samarthajuice.model.ItemHSNCodeReportModel;
import com.ats.samarthajuice.model.ItemModel;
import com.ats.samarthajuice.model.ItemReportModel;
import com.ats.samarthajuice.model.LoginModel;
import com.ats.samarthajuice.model.MonthWiseBillReportModel;
import com.ats.samarthajuice.model.OrderCancelReportModel;
import com.ats.samarthajuice.model.OrderHeaderModel;
import com.ats.samarthajuice.model.OrderModel;
import com.ats.samarthajuice.model.ParcelOrderHeaderModel;
import com.ats.samarthajuice.model.TableBusyModel;
import com.ats.samarthajuice.model.TableCategoryModel;
import com.ats.samarthajuice.model.TableFreeModel;
import com.ats.samarthajuice.model.TableModel;
import com.ats.samarthajuice.model.TableWiseReportModel;
import com.ats.samarthajuice.model.TaxLabWiseReportModel;
import com.ats.samarthajuice.model.TaxableDataForBillPrint;

import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface MyInterface {

    @POST("saveAdmin")
    Call<AdminModel> saveAdminUser(@Body AdminModel adminModel);

    @GET("getAllAdminByIsUsed")
    Call<ArrayList<AdminModel>> getAllAdminUser();

    @POST("deleteAdmin")
    Call<ErrorMessage> deleteAdminUser(@Query("adminId") int adminId);

    @POST("saveCategory")
    Call<CategoryModel> saveCategory(@Body CategoryModel categoryModel);

    @GET("getAllCatByIsUsed")
    Call<ArrayList<CategoryModel>> getAllCategory();

    @Multipart
    @POST("photoUpload")
    Call<JSONObject> imageUpload(@Part MultipartBody.Part file, @Part("imageName") RequestBody name, @Part("type") RequestBody type);

    @POST("deleteCategory")
    Call<ErrorMessage> deleteCategory(@Query("catId") int catId);

    @GET("getAllItemByIsUsed")
    Call<ArrayList<ItemModel>> getAllItems();

    @GET("getAllItem")
    Call<ArrayList<ItemModel>> getAllItemsByDelStatus();

    @POST("saveItem")
    Call<ItemModel> saveItem(@Body ItemModel itemModel);

    @POST("deleteItem")
    Call<ErrorMessage> deleteItem(@Query("itemId") int itemId);

    @POST("saveTableCat")
    Call<TableCategoryModel> saveTableCategory(@Body TableCategoryModel tableCategoryModel);

    @GET("getAllTablesCatByIsUsed")
    Call<ArrayList<TableCategoryModel>> getAllTableCategory();

    @POST("deleteTableCat")
    Call<ErrorMessage> deleteTableCategory(@Query("tableCatId") int tableCatId);

    @GET("getAllTablesByIsUsed")
    Call<ArrayList<TableModel>> getAllTables();

    @POST("saveTable")
    Call<TableModel> saveTable(@Body TableModel tableModel);

    @POST("deleteTable")
    Call<ErrorMessage> deleteTable(@Query("tableId") int tableId);

    @GET("getBsyTableList")
    Call<ArrayList<TableBusyModel>> getAllBusyTables();

    @POST("getBsyTableListByCatId")
    Call<ArrayList<TableBusyModel>> getAllBusyTables(@Query("catId") int catId);

    @GET("getFreeTableList")
    Call<ArrayList<TableFreeModel>> getAllFreeTables();

    @POST("getFreeTableListByCatId")
    Call<ArrayList<TableFreeModel>> getAllFreeTables(@Query("catId") int catId);

    @POST("orderListByTableNo")
    Call<ArrayList<OrderHeaderModel>> getOrdersByTable(@Query("tableNo") int tableNo);

    @GET("getAllCategoryWithItemList")
    Call<ArrayList<CategoryItemModel>> getAllCategoryWiseItems();

    @POST("saveOrder")
    Call<OrderHeaderModel> saveOrder(@Body OrderModel order);

    @POST("canceOrderItem")
    Call<ErrorMessage> cancelOrder(@Query("orderDetailId") ArrayList<Integer> orderDetailIds, @Query("status") int status, @Query("remark") String remark);

    @POST("adminLogin")
    Call<LoginModel> loginUser(@Query("userName") String userName, @Query("pass") String pass);

    @POST("generateBill")
    Call<ErrorMessage> generateBill(@Query("userId") int userId, @Query("discount") float discount, @Query("tableNo") int tableNo, @Query("venueId") int venueId);

    @POST("saveParcelOrder")
    Call<ParcelOrderHeaderModel> saveParcelOrder(@Body ParcelOrderHeaderModel order);

    @POST("generateBillForParcelOrder")
    Call<ErrorMessage> generateBillForParcel(@Query("userId") int userId, @Query("discount") float discount, @Query("parcelOrderId") int parcelOrderId, @Query("venueId") int venueId);

    @GET("getAllMessage")
    Call<ArrayList<CancelMessageModel>> getAllMessages();

    @POST("getItemwiseReport")
    Call<ArrayList<ItemReportModel>> getItemwiseReport(@Query("fromDate") String fromDate, @Query("toDate") String toDate);

    @POST("getItemCategorywiseReport")
    Call<ArrayList<CategoryWiseReportModel>> getCategorywiseReport(@Query("fromDate") String fromDate, @Query("toDate") String toDate);

    @POST("getBillWiseReport")
    Call<ArrayList<BillWiseReportModel>> getBillwiseReport(@Query("fromDate") String fromDate, @Query("toDate") String toDate);

    @POST("getDatewiseReport")
    Call<ArrayList<DateWiseReportModel>> getDatewiseReport(@Query("fromDate") String fromDate, @Query("toDate") String toDate);

    @POST("getDateItemwiseReport")
    Call<ArrayList<DateWiseItemReportModel>> getDatewiseItemReport(@Query("fromDate") String fromDate, @Query("toDate") String toDate, @Query("status") int status, @Query("itemIdList") ArrayList<Integer> itemIdList);

    @POST("getItemhsoncodewiseReport")
    Call<ArrayList<ItemHSNCodeReportModel>> getItemHSNReport(@Query("fromDate") String fromDate, @Query("toDate") String toDate);

    @POST("getBillMonthWiseReport")
    Call<ArrayList<MonthWiseBillReportModel>> getMonthWiseBillReport(@Query("fromDate") String fromDate, @Query("toDate") String toDate);

    @POST("getTableWiseReport")
    Call<ArrayList<TableWiseReportModel>> getTableWiseReport(@Query("fromDate") String fromDate, @Query("toDate") String toDate);

    @POST("getDatewiseBillReportGroupByTax")
    Call<ArrayList<BillWiseTaxReportModel>> getDateWiseBillTaxReport(@Query("fromDate") String fromDate, @Query("toDate") String toDate);

    @POST("getBillwiseBillReportGroupByTax")
    Call<ArrayList<BillWiseTaxReportModel>> getBillWiseBillTaxReport(@Query("fromDate") String fromDate, @Query("toDate") String toDate);

    @POST("getTaxLabewiseReport")
    Call<ArrayList<TaxLabWiseReportModel>> getTaxLabWiseReport(@Query("fromDate") String fromDate, @Query("toDate") String toDate);

    @POST("getOrderCancellationWiseReport")
    Call<ArrayList<OrderCancelReportModel>> getOrderCancelReport(@Query("fromDate") String fromDate, @Query("toDate") String toDate);

    @POST("getAllItemwiseCanReport")
    Call<ArrayList<ItemCancelReportModel>> getItemCancelReport(@Query("fromDate") String fromDate, @Query("toDate") String toDate);

    @GET("getCategoryInfo")
    Call<ArrayList<CategoryMenuModel>> getCategoryMenu();

    @POST("getBillHeaderAndDetail")
    Call<ArrayList<BillHeaderModel>> getAllBills(@Query("date") String date, @Query("type") int type, @Query("bill_closed") int bill_close);

    @POST("editBill")
    Call<BillHeaderModel> editBill(@Body BillHeaderModel billHeaderModel);

    //VenuId=card=1,googlePay=2,patTM=3,cash=4,foodPanda=5,swiggy=6,uber_eat=7,zomato=8;


    @POST("getTaxableDataForBillPrintByDate")
    Call<ArrayList<TaxableDataForBillPrint>> getTaxDataFroBillPrintByDate(@Query("fromDate") String fromDate, @Query("toDate") String toDate);

    @POST("getTaxableDataForBillPrintByBillId")
    Call<ArrayList<TaxableDataForBillPrint>> getTaxDataFroBillPrintByBillId(@Query("billIds") ArrayList<Integer> billIds);

    @POST("changeTableNoInOrder")
    Call<ErrorMessage> changeTableNoInOrder(@Query("tableNo") int tableNo,@Query("orderId") int orderId);




}
