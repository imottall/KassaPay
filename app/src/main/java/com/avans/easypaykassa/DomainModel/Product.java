package com.avans.easypaykassa.DomainModel;

import android.media.Image;

import java.io.Serializable;

public class Product implements Serializable {
    private String productName;
    private double productPrice;
    private int productId;
    private int amount;

    public Product(String productName, double productPrice, int productId) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.productId = productId;
    }

    public Product(String productName, double productPrice, int amount, int productId) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.productId = productId;
        this.amount = amount;
    }

    public String getProductName() {
        return productName;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public int getAmount() { return amount; }

    public void setAmount(int amount) { this.amount = amount; }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    @Override
    public String toString() {
        return "Product{" +
                "productName='" + productName  +
                ", productPrice=" + productPrice +
                '}';
    }

    public String getFullImageUrl() {
        String url = "https://easypayserver.github.io/" + productId + ".png";
        return url;
    }
}