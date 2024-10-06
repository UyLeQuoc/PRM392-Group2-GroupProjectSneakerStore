package com.group2.prm392_group2_sneakerzone.model;

public class ProductSize {
    private int productSizeId;
    private int productId;
    private String size;
    private int quantity;
    private String createdDate;
    private String updatedDate;

    public ProductSize(int productSizeId, int productId, String size, int quantity, String createdDate, String updatedDate) {
        this.productSizeId = productSizeId;
        this.productId = productId;
        this.size = size;
        this.quantity = quantity;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
    }

    // Getter và Setter
    public int getProductSizeId() {
        return productSizeId;
    }

    public void setProductSizeId(int productSizeId) {
        this.productSizeId = productSizeId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }
}

