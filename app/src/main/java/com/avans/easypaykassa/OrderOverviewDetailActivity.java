package com.avans.easypaykassa;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ListView;
import android.widget.TextView;

import com.avans.easypaykassa.DomainModel.Order;
import com.avans.easypaykassa.DomainModel.Product;
import com.avans.easypaykassa.HCE.LoyaltyCardReader;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderOverviewDetailActivity extends AppCompatActivity implements EasyPayAPIConnector.OnProductAvailable,
EasyPayAPIGETOrderConnector.OnOrdersAvailable, LoyaltyCardReader.AccountCallback {

    private ArrayList<Product> productList;
    private ListView listview;
    private TextView total_price, id, location, date;
    private double price;
    private ProductAdapter adapter;

    //url to update order status
    private String URL = "https://easypayserver.herokuapp.com/api/bestelling/update/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_overview_detail);

        Order order = (Order) getIntent().getSerializableExtra("order");

        total_price = (TextView) findViewById(R.id.order_price_detailed);
        id = (TextView) findViewById(R.id.order_number_detailed);
        location = (TextView) findViewById(R.id.order_location_detailed);
        date = (TextView) findViewById(R.id.order_date_detailed);

        productList = new ArrayList<>();
        getOrder(order.getOrderNumber());
        listview = (ListView) findViewById(R.id.order_detailed_list);

        LayoutInflater inflater = LayoutInflater.from(this);
        adapter = new ProductAdapter(this, inflater, productList);
        listview.setAdapter(adapter);
    }


    private void getOrder(int orderNumber){

        String URL = "https://easypayserver.herokuapp.com/api/bestelling/" + orderNumber;

        new EasyPayAPIGETOrderConnector(this).execute(URL);
    }

    @Override
    public void onProductAvailable(Product product) {
        price = 0;
        productList.add(product);

        for (int i = 0; i < productList.size() ; i++) {
           price = price + productList.get(i).getProductPrice();
        }

        total_price.setText("€" + price);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onOrdersAvailable(Order order) {

        //Stop loading screen

        id.setText(order.getOrderNumber()+"");
        location.setText(order.getLocation());
        date.setText(order.getDate().toString());

        for (int i = 0; i < order.getProductsIDs().size() ; i++) {

            String[] URL = {
                    "https://easypayserver.herokuapp.com/api/product/" + order.getProductsIDs().get(i)
            };

            Log.i("URL", URL[0]);

            new EasyPayAPIConnector(this).execute(URL);
        }
    }

    @Override
    public void onAccountReceived(String msg) {
        if (msg.equals("PAID")) {
            int orderNumber = Integer.parseInt(getIntent().getStringExtra("orderNumber"));
            Log.i(this.getClass().getSimpleName(), "RECEIVED ORDERNR: " + orderNumber);
            new EasyPayAPIPUTConnector().execute(URL + orderNumber + "/PAID");
        }
    }
}
