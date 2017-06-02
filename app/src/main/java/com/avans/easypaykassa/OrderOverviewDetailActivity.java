package com.avans.easypaykassa;

import android.app.ProgressDialog;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
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

    private String TAG = this.getClass().getSimpleName();

    private ArrayList<Product> productList;
    private ListView listview;
    private TextView total_price, id, location, date;
    private CheckBox checkbox;
    private double price;
    private ProductAdapter adapter;

    private Order order;

    //NFC attributes
    public static int READER_FLAGS =
            NfcAdapter.FLAG_READER_NFC_A | NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK;
    private LoyaltyCardReader loyaltyCardReader;

    //url to update order status
    private String URL = "https://easypayserver.herokuapp.com/api/bestelling/update/";

    //ProgressDialog
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_overview_detail);

        //get orderNumber that has either been received by OrderOverviewActivity or from NFC scan
        order = (Order) getIntent().getSerializableExtra("order");

        //initialise views
        listview = (ListView) findViewById(R.id.order_detailed_list);
        total_price = (TextView) findViewById(R.id.order_price_detailed);
        id = (TextView) findViewById(R.id.order_number_detailed);
        location = (TextView) findViewById(R.id.order_location_detailed);
        date = (TextView) findViewById(R.id.order_date_detailed);
        checkbox = (CheckBox) findViewById(R.id.checkBox);

        //initalise toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ImageView home = (ImageView) findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent intent = new Intent(OrderOverviewDetailActivity.this, MainActivity.class);
                finish();
                startActivity(intent);
            }
        });
        ImageView scan = (ImageView) findViewById(R.id.go_to_scan);
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderOverviewDetailActivity.this, ScanActivity.class);
                startActivity(intent);
            }
        });

        //initialise productlist & fill it with data
        productList = new ArrayList<>();
        getOrder(order.getOrderNumber());

        LayoutInflater inflater = LayoutInflater.from(this);
        adapter = new ProductAdapter(this, inflater, productList);
        listview.setAdapter(adapter);

        //NFC stuff
        loyaltyCardReader = new LoyaltyCardReader(this);
        enableReaderMode();

        //show progress dialog
        pd = new ProgressDialog(this);
        pd.setMessage("Bestelling ophalen...");
        pd.show();

        //set initial textview values
        total_price.setText("");
        id.setText("");
        location.setText("");
        date.setText("");
    }

    private void getOrder(int orderNumber){
        //get
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

        pd.cancel();
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

    //NFC functionality
    @Override
    public void onResume() {
        super.onResume();
        enableReaderMode();
    }

    @Override
    public void onPause() {
        super.onPause();
        disableReaderMode();
    }

    private void enableReaderMode() {
        Log.i(TAG, "Enabling reader mode");
        NfcAdapter nfc = NfcAdapter.getDefaultAdapter(getApplicationContext());
        if (nfc != null) {
            nfc.enableReaderMode(this, loyaltyCardReader, READER_FLAGS, null);
        }
    }

    private void disableReaderMode() {
        Log.i(TAG, "Disabling reader mode");
        NfcAdapter nfc = NfcAdapter.getDefaultAdapter(getApplicationContext());
        if (nfc != null) {
            nfc.disableReaderMode(this);
        }
    }

    @Override
    public void onAccountReceived(String msg) {
        if (msg.equals("PAID")) {
            //update database, so that the order has a status of 'PAID'
            new EasyPayAPIPUTConnector().execute(URL + order.getOrderNumber() + "/PAID");
            Log.i(this.getClass().getSimpleName(), "RECEIVED ORDERNR: " + order.getOrderNumber());

            //if order is paid, check the checkbox
            checkbox.setChecked(true);
        }
    }
}
