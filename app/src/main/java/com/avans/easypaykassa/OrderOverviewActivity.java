package com.avans.easypaykassa;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.avans.easypaykassa.DomainModel.Order;
import com.avans.easypaykassa.DomainModel.Product;

import java.util.ArrayList;
import java.util.Date;

public class OrderOverviewActivity extends AppCompatActivity implements ListView.OnItemClickListener {

    private OrderOverviewAdapter adapter;
    private ArrayList<Order> orders = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_overview);

        //-------------------
        //test order objects
        ArrayList<Product> products = new ArrayList<>();
        //list of products
        products.add(new Product("Schoenzool", 3.20, 1));
        products.add(new Product("Oreo", 1.40, 2));
        products.add(new Product("Skippiebal", 12.99, 3));
        //list of orders, including the product list
        orders.add(new Order(1, 1, new Date(), "Pizzahut", products, 1, "WAITING"));
        orders.add(new Order(1, 2, new Date(), "Hotdogkraam", products, 2, "PAID"));
        orders.add(new Order(1, 3, new Date(), "Pizzahut", products, 3, "CANCELED"));
        //-------------------

        //initialise listview
        ListView orderListview = (ListView) findViewById(R.id.orderOverviewListview);
        //initialise adapter and attach to listview
        adapter = new OrderOverviewAdapter(this, orders);
        orderListview.setAdapter(adapter);

        //set listener(s)
        orderListview.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Order order = orders.get(position);
        Intent i = new Intent(this, OrderOverviewDetailActivity.class);
        i.putExtra("order", order);
        startActivity(i);
    }
}
