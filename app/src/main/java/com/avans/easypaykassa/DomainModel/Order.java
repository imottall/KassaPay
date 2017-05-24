package com.avans.easypaykassa.DomainModel;

import java.io.Serializable;

/**
 * Created by Felix on 22-5-2017.
 */

public class Order implements Serializable {

    private int orderId, customerId, productId, orderNumber;
    private enum Status {WAITING, PAID, CANCELED}

    public Order(int orderId, int customerId, int productId, int orderNumber) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.productId = productId;
        this.orderNumber = orderNumber;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", customerId=" + customerId +
                ", productId=" + productId +
                ", orderNumber=" + orderNumber +
                '}';
    }
}
