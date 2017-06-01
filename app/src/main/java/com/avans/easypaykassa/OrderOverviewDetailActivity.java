package com.avans.easypaykassa;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ListView;
import android.widget.TextView;

import com.avans.easypaykassa.DomainModel.Order;
import com.avans.easypaykassa.DomainModel.Product;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderOverviewDetailActivity extends AppCompatActivity implements EasyPayAPIConnector.OnProductAvailable,
EasyPayAPIGETOrderConnector.OnOrdersAvailable{

    private ArrayList<Product> productList;
    private ListView listview;
    private TextView total_price, id, location, date;
    private double price;
    private ProductAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_overview_detail);

        total_price = (TextView) findViewById(R.id.order_price_detailed);
        id = (TextView) findViewById(R.id.order_number_detailed);
        location = (TextView) findViewById(R.id.order_location_detailed);
        date = (TextView) findViewById(R.id.order_date_detailed);

//        id.setText(""+1);
//        location.setText("Pizza paleis");
//        date.setText(new Date().toString());

        productList = new ArrayList<>();
//        getProductItems();
//        getOrder(8);
        listview = (ListView) findViewById(R.id.order_detailed_list);

        LayoutInflater inflater = LayoutInflater.from(this);
        adapter = new ProductAdapter(this, inflater, productList);
        listview.setAdapter(adapter);
    }

//    public void getProductItems() {
//        String[] URL = {
//                "https://easypayserver.herokuapp.com/api/product/food"
//                //bij andere locaties zal er iets met de endpoint moeten worden aangepast: "link/api/product/" + tabname
//        };
//
//        new EasyPayAPIConnector(this).execute(URL);
//    }

    private void getOrder(int orderNumber){

        String URL = "https://easypayserver.herokuapp.com/api/bestelling/" + orderNumber;

        new EasyPayAPIGETOrderConnector(this).execute(URL);
    }

    @Override
    public void onProductAvailable(Product product) {
        price = 0;
        Log.i("", "ProductAvailable: " + product);
        productList.add(product);
        Log.i("", "onProductAvailable: " + productList);

        for (int i = 0; i < productList.size() ; i++) {
           price = price + productList.get(i).getProductPrice();
        }

        total_price.setText("€" + price);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onOrdersAvailable(Order order) {

        id.setText(order.getOrderNumber()+"");
        location.setText(order.getLocation());
        date.setText(order.getDate().toString());

        for (int i = 0; i < order.getProducts().size() ; i++) {


            String[] URL = {
                    "https://easypayserver.herokuapp.com/api/product/" + order.getProducts().get(i)
            };

            new EasyPayAPIGETOrderConnector(this).execute();
        }
    }
}
