package com.example.chandrabhagacollection;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.ServerValue;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Products implements Serializable {

    public String brandName, type, catalogName, quantity, price, productId;
    public Long createdDateTime;

    public Products() {

    }

    Products(String brandName, String type, String catalogName, String quantity, String price,
             String productId) {

        this.createdDateTime = createdDateTime;
        this.brandName = brandName;
        this.type = type;
        this.catalogName = catalogName;
        this.quantity = quantity;
        this.price = price;
        this.productId = productId;

    }


    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCatalogName() {
        return catalogName;
    }

    public void setCatalogName(String catalogName) {
        this.catalogName = catalogName;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Long getCreatedDateTimeLong() {
        return createdDateTime;
    }

    public Map<String, String> getCreatedDateTime() {
        return ServerValue.TIMESTAMP;
    }

    public void setCreatedDateTime(Long createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    @Exclude
    public Map getLeedStatusMap() {
        Map<String, Object> leedMap = new HashMap();

        leedMap.put("createdDateTime", getCreatedDateTime());
        leedMap.put("brandName", getBrandName());
        leedMap.put("type", getType());
        leedMap.put("catalogName", getCatalogName());
        leedMap.put("quantity", getQuantity());
        leedMap.put("price", getPrice());
        leedMap.put("productId", getProductId());

        return leedMap;

    }
}
