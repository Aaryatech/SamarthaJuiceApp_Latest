package com.ats.taborderingsystem.interfaces;

import com.ats.taborderingsystem.model.AdminModel;
import com.ats.taborderingsystem.model.BillHeaderModel;
import com.ats.taborderingsystem.model.BillWiseReportModel;
import com.ats.taborderingsystem.model.BillWiseTaxReportModel;
import com.ats.taborderingsystem.model.CancelMessageModel;
import com.ats.taborderingsystem.model.CategoryItemModel;
import com.ats.taborderingsystem.model.CategoryMenuModel;
import com.ats.taborderingsystem.model.CategoryModel;
import com.ats.taborderingsystem.model.CategoryWiseReportModel;
import com.ats.taborderingsystem.model.DateWiseItemReportModel;
import com.ats.taborderingsystem.model.DateWiseReportModel;
import com.ats.taborderingsystem.model.ErrorMessage;
import com.ats.taborderingsystem.model.ItemCancelReportModel;
import com.ats.taborderingsystem.model.ItemHSNCodeReportModel;
import com.ats.taborderingsystem.model.ItemModel;
import com.ats.taborderingsystem.model.ItemReportModel;
import com.ats.taborderingsystem.model.LoginModel;
import com.ats.taborderingsystem.model.MonthWiseBillReportModel;
import com.ats.taborderingsystem.model.OrderCancelReportModel;
import com.ats.taborderingsystem.model.OrderHeaderModel;
import com.ats.taborderingsystem.model.OrderModel;
import com.ats.taborderingsystem.model.ParcelOrderHeaderModel;
import com.ats.taborderingsystem.model.TableBusyModel;
import com.ats.taborderingsystem.model.TableCategoryModel;
import com.ats.taborderingsystem.model.TableFreeModel;
import com.ats.taborderingsystem.model.TableModel;
import com.ats.taborderingsystem.model.TableWiseReportModel;
import com.ats.taborderingsystem.model.TaxLabWiseReportModel;

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
    Call<ArrayList<BillHeaderModel>> getAllBills(@Query("date") String date, @Query("type") int type);

    @POST("editBill")
    Call<BillHeaderModel> editBill(@Body BillHeaderModel billHeaderModel);

}
