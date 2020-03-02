package com.ats.samarthajuice.model;

public class CategoryModel {

    private int catId;
    private String catName;
    private String catDesc;
    private String catImage;
    private int delStatus;
    private int userId;
    private String updatedDate;

    public CategoryModel(int catId, String catName, String catDesc, String catImage, int delStatus, int userId, String updatedDate) {
        this.catId = catId;
        this.catName = catName;
        this.catDesc = catDesc;
        this.catImage = catImage;
        this.delStatus = delStatus;
        this.userId = userId;
        this.updatedDate = updatedDate;
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

    @Override
    public String toString() {
        return "CategoryModel{" +
                "catId=" + catId +
                ", catName='" + catName + '\'' +
                ", catDesc='" + catDesc + '\'' +
                ", catImage='" + catImage + '\'' +
                ", delStatus=" + delStatus +
                ", userId=" + userId +
                ", updatedDate='" + updatedDate + '\'' +
                '}';
    }
}
