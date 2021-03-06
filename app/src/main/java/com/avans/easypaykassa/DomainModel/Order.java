package com.avans.easypaykassa.DomainModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Felix on 22-5-2017.
 */

public class Order implements Serializable{

    private int orderId, customerId, orderNumber;
    private String status, location;
    private Date date;
    private ArrayList<Product> products;
    private ArrayList<Integer> productsIDs;

    public Order(int orderId, int customerId, Date date, String location, ArrayList<Product> selectedProducts, int orderNumber, String status) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.date = date;
        this.location = location;
        this.products = selectedProducts;
        this.orderNumber = orderNumber;
        this.status = status;
    }
    //deze constructor voor het aanmaken van een order verspreid over meerdere activities.
    public Order() {

    }

    public Order(int orderId, int customerId, Date date, ArrayList<Integer> selectedProducts, String location, int orderNumber, String status) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.date = date;
        this.location = location;
        this.productsIDs = selectedProducts;
        this.orderNumber = orderNumber;
        this.status = status;
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

    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLocation() {
        return location;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public Date getDate() {
        return date;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public ArrayList<Integer> getProductsIDs() {
        return productsIDs;
    }

    public void setProductsIDs(ArrayList<Integer> productsIDs) {
        this.productsIDs = productsIDs;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", customerId=" + customerId +
                ", orderNumber=" + orderNumber +
                ", status='" + status + '\'' +
                ", location='" + location + '\'' +
                ", date=" + date +
                ", products=" + products +
                '}';
    }
}
