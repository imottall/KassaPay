package com.avans.easypaykassa;

import android.content.Context;

import java.text.DecimalFormat;
import com.avans.easypaykassa.DomainModel.Product;
import java.util.ArrayList;


public class ProductsTotal {
    private ArrayList<ArrayList<Product>> products;

    public ProductsTotal(Context context, ArrayList<ArrayList<Product>> products) {

        this.products = products;

    }

    public String getPriceTotal() {

        double d = 0;

        for (ArrayList<Product> specificProducts : products) {

            for (Product product : specificProducts) {

                Product p;

                p = product;
                d += p.getProductPrice();
            }
        }

        DecimalFormat df = new DecimalFormat("0.00##");

        return "Subtotaal: €" + df.format(d);
    }

    public String getTotal() {

        int total = 0;

        for (ArrayList<Product> specificProducts : products) {

            for (Product product : specificProducts) {

                total += 1;
            }
        }

        return total + " Producten";
    }

    public double getPriceTotalDouble() {

        double d = 0;

        for (ArrayList<Product> specificProducts : products) {

            for (Product product : specificProducts) {

                Product p;

                p = product;
                d += p.getProductPrice();
            }
        }

        return d;
    }

    public interface OnTotalChanged {

        void onTotalChanged(String priceTotal, String total, ArrayList<ArrayList<Product>> products);
    }

}
