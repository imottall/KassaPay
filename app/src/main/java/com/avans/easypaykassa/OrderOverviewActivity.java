package com.avans.easypaykassa;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.avans.easypaykassa.DomainModel.Order;

import java.util.ArrayList;

import static android.R.attr.order;
import static com.avans.easypaykassa.MainActivity.LOCATION;

public class OrderOverviewActivity extends AppCompatActivity implements ListView.OnItemClickListener,
        EasyPayAPIOrdersConnector.OnOrdersAvailable{

    private OrderOverviewAdapter adapter;
    private ArrayList<Order> orders = new ArrayList<>();
    private ArrayList<Integer> orderNumbers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_overview);
        Bundle bundle = getIntent().getExtras();
        int locationID = (int) bundle.get(LOCATION);

        //get orders from DB
        EasyPayAPIOrdersConnector get = new EasyPayAPIOrdersConnector(this);
        get.execute("https://easypayserver.herokuapp.com/api/bestelling/location/" + locationID);

        //initalise toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ImageView home = (ImageView) findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrderOverviewActivity.this, MainActivity.class);
                finish();
                startActivity(intent);
            }
        });
        ImageView scan = (ImageView) findViewById(R.id.go_to_scan);
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderOverviewActivity.this, ScanActivity.class);
                startActivity(intent);
            }
        });

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
        long dateInMillis = order.getDate().getTime() + Double.valueOf(2.16e+7).longValue();
        i.putExtra("dateInMillis", dateInMillis);
        startActivity(i);
    }
}
