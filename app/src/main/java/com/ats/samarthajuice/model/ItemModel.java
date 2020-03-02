package com.ats.samarthajuice.model;

public class ItemModel {

    private int itemId;
    private String itemName;
    private String itemDesc;
    private String itemImage;
    private float mrpGame;
    private float mrpRegular;
    private float mrpSpecial;
    private float openingRate;
    private float maxRate;
    private float minRate;
    private int currentStock;
    private int catId;
    private float sgst;
    private float cgst;
    private int isMixerApplicable;
    private int userId;
    private String updatedDate;
    private int delStatus;
    private int minStock;
    private String hsnCode;
    private int qty;
    private boolean cancelStatus;
    private String remark;

    public ItemModel(int itemId, String itemName, String itemDesc, String itemImage, float mrpGame, float mrpRegular, float mrpSpecial, float openingRate, float maxRate, float minRate, int currentStock, int catId, float sgst, float cgst, int isMixerApplicable, int userId, String updatedDate, int delStatus, int minStock) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.itemDesc = itemDesc;
        this.itemImage = itemImage;
        this.mrpGame = mrpGame;
        this.mrpRegular = mrpRegular;
        this.mrpSpecial = mrpSpecial;
        this.openingRate = openingRate;
        this.maxRate = maxRate;
        this.minRate = minRate;
        this.currentStock = currentStock;
        this.catId = catId;
        this.sgst = sgst;
        this.cgst = cgst;
        this.isMixerApplicable = isMixerApplicable;
        this.userId = userId;
        this.updatedDate = updatedDate;
        this.delStatus = delStatus;
        this.minStock = minStock;
    }

    public ItemModel(int itemId, String itemName, String itemDesc, String itemImage, float mrpGame, float mrpRegular, float mrpSpecial, float openingRate, float maxRate, float minRate, int currentStock, int catId, float sgst, float cgst, int isMixerApplicable, int userId, String updatedDate, int delStatus, int minStock, String hsnCode) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.itemDesc = itemDesc;
        this.itemImage = itemImage;
        this.mrpGame = mrpGame;
        this.mrpRegular = mrpRegular;
        this.mrpSpecial = mrpSpecial;
        this.openingRate = openingRate;
        this.maxRate = maxRate;
        this.minRate = minRate;
        this.currentStock = currentStock;
        this.catId = catId;
        this.sgst = sgst;
        this.cgst = cgst;
        this.isMixerApplicable = isMixerApplicable;
        this.userId = userId;
        this.updatedDate = updatedDate;
        this.delStatus = delStatus;
        this.minStock = minStock;
        this.hsnCode = hsnCode;
    }

    public ItemModel(int itemId, String itemName, String itemDesc, String itemImage, float mrpGame, float mrpRegular, float mrpSpecial, float openingRate, float maxRate, float minRate, int currentStock, int catId, float sgst, float cgst, int isMixerApplicable, int userId, String updatedDate, int delStatus, int minStock, String hsnCode, int qty, boolean cancelStatus) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.itemDesc = itemDesc;
        this.itemImage = itemImage;
        this.mrpGame = mrpGame;
        this.mrpRegular = mrpRegular;
        this.mrpSpecial = mrpSpecial;
        this.openingRate = openingRate;
        this.maxRate = maxRate;
        this.minRate = minRate;
        this.currentStock = currentStock;
        this.catId = catId;
        this.sgst = sgst;
        this.cgst = cgst;
        this.isMixerApplicable = isMixerApplicable;
        this.userId = userId;
        this.updatedDate = updatedDate;
        this.delStatus = delStatus;
        this.minStock = minStock;
        this.hsnCode = hsnCode;
        this.qty = qty;
        this.cancelStatus = cancelStatus;
    }

    public ItemModel(int itemId, String itemName, String itemDesc, String itemImage, float mrpGame, float mrpRegular, float mrpSpecial, float openingRate, float maxRate, float minRate, int currentStock, int catId, float sgst, float cgst, int isMixerApplicable, int userId, String updatedDate, int delStatus, int minStock, String hsnCode, int qty, boolean cancelStatus,String remark) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.itemDesc = itemDesc;
        this.itemImage = itemImage;
        this.mrpGame = mrpGame;
        this.mrpRegular = mrpRegular;
        this.mrpSpecial = mrpSpecial;
        this.openingRate = openingRate;
        this.maxRate = maxRate;
        this.minRate = minRate;
        this.currentStock = currentStock;
        this.catId = catId;
        this.sgst = sgst;
        this.cgst = cgst;
        this.isMixerApplicable = isMixerApplicable;
        this.userId = userId;
        this.updatedDate = updatedDate;
        this.delStatus = delStatus;
        this.minStock = minStock;
        this.hsnCode = hsnCode;
        this.qty = qty;
        this.cancelStatus = cancelStatus;
        this.remark = remark;
    }

    public ItemModel(int itemId, String itemName, float mrpRegular, int qty, String remark) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.mrpRegular = mrpRegular;
        this.qty = qty;
        this.remark = remark;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemDesc() {
        return itemDesc;
    }

    public void setItemDesc(String itemDesc) {
        this.itemDesc = itemDesc;
    }

    public String getItemImage() {
        return itemImage;
    }

    public void setItemImage(String itemImage) {
        this.itemImage = itemImage;
    }

    public float getMrpGame() {
        return mrpGame;
    }

    public void setMrpGame(float mrpGame) {
        this.mrpGame = mrpGame;
    }

    public float getMrpRegular() {
        return mrpRegular;
    }

    public void setMrpRegular(float mrpRegular) {
        this.mrpRegular = mrpRegular;
    }

    public float getMrpSpecial() {
        return mrpSpecial;
    }

    public void setMrpSpecial(float mrpSpecial) {
        this.mrpSpecial = mrpSpecial;
    }

    public float getOpeningRate() {
        return openingRate;
    }

    public void setOpeningRate(float openingRate) {
        this.openingRate = openingRate;
    }

    public float getMaxRate() {
        return maxRate;
    }

    public void setMaxRate(float maxRate) {
        this.maxRate = maxRate;
    }

    public float getMinRate() {
        return minRate;
    }

    public void setMinRate(float minRate) {
        this.minRate = minRate;
    }

    public int getCurrentStock() {
        return currentStock;
    }

    public void setCurrentStock(int currentStock) {
        this.currentStock = currentStock;
    }

    public int getCatId() {
        return catId;
    }

    public void setCatId(int catId) {
        this.catId = catId;
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

    public int getIsMixerApplicable() {
        return isMixerApplicable;
    }

    public void setIsMixerApplicable(int isMixerApplicable) {
        this.isMixerApplicable = isMixerApplicable;
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

    public int getDelStatus() {
        return delStatus;
    }

    public void setDelStatus(int delStatus) {
        this.delStatus = delStatus;
    }

    public int getMinStock() {
        return minStock;
    }

    public void setMinStock(int minStock) {
        this.minStock = minStock;
    }

    public String getHsnCode() {
        return hsnCode;
    }

    public void setHsnCode(String hsnCode) {
        this.hsnCode = hsnCode;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public boolean isCancelStatus() {
        return cancelStatus;
    }

    public void setCancelStatus(boolean cancelStatus) {
        this.cancelStatus = cancelStatus;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "ItemModel{" +
                "itemId=" + itemId +
                ", itemName='" + itemName + '\'' +
                ", itemDesc='" + itemDesc + '\'' +
                ", itemImage='" + itemImage + '\'' +
                ", mrpGame=" + mrpGame +
                ", mrpRegular=" + mrpRegular +
                ", mrpSpecial=" + mrpSpecial +
                ", openingRate=" + openingRate +
                ", maxRate=" + maxRate +
                ", minRate=" + minRate +
                ", currentStock=" + currentStock +
                ", catId=" + catId +
                ", sgst=" + sgst +
                ", cgst=" + cgst +
                ", isMixerApplicable=" + isMixerApplicable +
                ", userId=" + userId +
                ", updatedDate='" + updatedDate + '\'' +
                ", delStatus=" + delStatus +
                ", minStock=" + minStock +
                ", hsnCode='" + hsnCode + '\'' +
                ", qty=" + qty +
                ", cancelStatus=" + cancelStatus +
                ", remark='" + remark + '\'' +
                '}';
    }
}
