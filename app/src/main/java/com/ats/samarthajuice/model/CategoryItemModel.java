package com.ats.samarthajuice.model;

import java.util.List;

public class CategoryItemModel {

    private int catId;
    private String catName;
    private String catDesc;
    private String catImage;
    private int delStatus;
    private int userId;
    private String updatedDate;
    private List<Item> itemList;

    public CategoryItemModel(int catId, String catName, String catDesc, String catImage, int delStatus, int userId, String updatedDate, List<Item> itemList) {
        this.catId = catId;
        this.catName = catName;
        this.catDesc = catDesc;
        this.catImage = catImage;
        this.delStatus = delStatus;
        this.userId = userId;
        this.updatedDate = updatedDate;
        this.itemList = itemList;
    }

    public int getCatId() {
        return catId;
    }

    public void setCatId(int catId) {
        this.catId = catId;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public String getCatDesc() {
        return catDesc;
    }

    public void setCatDesc(String catDesc) {
        this.catDesc = catDesc;
    }

    public String getCatImage() {
        return catImage;
    }

    public void setCatImage(String catImage) {
        this.catImage = catImage;
    }

    public int getDelStatus() {
        return delStatus;
    }

    public void setDelStatus(int delStatus) {
        this.delStatus = delStatus;
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

    public List<Item> getItemList() {
        return itemList;
    }

    public void setItemList(List<Item> itemList) {
        this.itemList = itemList;
    }

    @Override
    public String toString() {
        return "CategoryItemModel{" +
                "catId=" + catId +
                ", catName='" + catName + '\'' +
                ", catDesc='" + catDesc + '\'' +
                ", catImage='" + catImage + '\'' +
                ", delStatus=" + delStatus +
                ", userId=" + userId +
                ", updatedDate='" + updatedDate + '\'' +
                ", itemList=" + itemList +
                '}';
    }
}
