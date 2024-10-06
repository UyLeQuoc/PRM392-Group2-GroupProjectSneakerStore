package com.group2.prm392_group2_sneakerzone.model;

public class Brand {
    private int brandId;
    private String brandName;
    private int createdBy;
    private String createdDate;

    public Brand(int brandId, String brandName, int createdBy, String createdDate) {
        this.brandId = brandId;
        this.brandName = brandName;
        this.createdBy = createdBy;
        this.createdDate = createdDate;
    }

    // Getter và Setter
    public int getBrandId() {
        return brandId;
    }

    public void setBrandId(int brandId) {
        this.brandId = brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }
}
