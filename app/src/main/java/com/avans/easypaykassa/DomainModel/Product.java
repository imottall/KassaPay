package com.avans.easypaykassa.DomainModel;

import android.media.Image;

import java.io.Serializable;

public class Product implements Serializable {
    private String productName;
    private double productPrice;
    private int productId;
    private int amount;
    private boolean isChecked;

    public Product(String productName, double productPrice, int productId) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.productId = productId;
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
                "productName='" + productName + '\'' +
                ", productPrice=" + productPrice +
                ", productId=" + productId +
                ", amount=" + amount +
                ", isChecked=" + isChecked +
                '}';
    }

    public String getFullImageUrl() {
        String url = "https://raw.githubusercontent.com/bartaveld/EasyPayImages/master/" + productId + ".png";
        return url;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean val) {
        this.isChecked = val;
    }
}
