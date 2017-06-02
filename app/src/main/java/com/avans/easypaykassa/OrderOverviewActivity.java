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

public class OrderOverviewActivity extends AppCompatActivity implements ListView.OnItemClickListener,
        EasyPayAPIOrdersConnector.OnOrdersAvailable{

    private OrderOverviewAdapter adapter;
    private ArrayList<Order> orders = new ArrayList<>();
    private EasyPayAPIOrdersConnector get;
    private ArrayList<Integer> orderNumbers = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_overview);


        get = new EasyPayAPIOrdersConnector(this);
        get.execute("https://easypayserver.herokuapp.com/api/bestelling/");


        //initialise listview
        ListView orderListview = (ListView) findViewById(R.id.orderOverviewListview);
        //initialise adapter and attach to listview
        adapter = new OrderOverviewAdapter(this, orders);
        orderListview.setAdapter(adapter);

        //set listener(s)
        orderListview.setOnItemClickListener(this);
    }

    @Override
    public void onOrdersAvailable(Order order) {

        Log.i("ORDER", order.toString());

        if (!orderNumbers.isEmpty()){
            if (!orderNumbers.contains(order.getOrderNumber())){
                orderNumbers.add(order.getOrderNumber());
                orders.add(order);
                adapter.notifyDataSetChanged();
            } else
                return;
        } else {
            orders.add(order);
            orderNumbers.add(order.getOrderNumber());
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Order order = orders.get(position);
        Intent i = new Intent(this, OrderOverviewDetailActivity.class);
        i.putExtra("order", order);
        startActivity(i);
    }
}
