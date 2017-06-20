package com.avans.easypaykassa;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.avans.easypaykassa.ASyncTasks.CustomerDataTask;
import com.avans.easypaykassa.DomainModel.Customer;
import com.avans.easypaykassa.DomainModel.Order;
import com.avans.easypaykassa.DomainModel.Product;
import com.avans.easypaykassa.HCE.LoyaltyCardReader;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import es.dmoral.toasty.Toasty;

public class OrderOverviewDetailWithNFCActivity extends AppCompatActivity implements EasyPayAPIConnector.OnProductAvailable,
        EasyPayAPIGETOrderConnector.OnOrdersAvailable, LoyaltyCardReader.AccountCallback, CustomerDataTask.OnCustomerAvailable {

    private String TAG = this.getClass().getSimpleName();

    private ArrayList<Product> productList;
    private ListView listview;
    private TextView total_price, id, location, date, scanInstructionOutput;
    private CheckBox checkbox;
    private ImageView xCheckbox, scanImage1, scanImage2, person;

    private SharedPreferences locationPref;

    private double price;
    private ProductAdapter adapter;

    private Order order;

    private boolean statusPaid = false;

    //NFC attributes
    public static int READER_FLAGS =
            NfcAdapter.FLAG_READER_NFC_A | NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK;
    private LoyaltyCardReader loyaltyCardReader;

    //url to update order status
    private String URL = "https://easypayserver.herokuapp.com/api/bestelling/update/";

    //ProgressDialog
    ProgressDialog pd;

    //variable to get rid of difference in DB order time and actual Dutch time
    private long dateInMillis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_overview_detail_with_nfc);

        locationPref = getSharedPreferences(LoginActivity.PREFERENCELOCATION, Context.MODE_PRIVATE);

        //get orderNumber that has either been received by OrderOverviewActivity or from NFC scan
        order = (Order) getIntent().getSerializableExtra("order");
        dateInMillis = getIntent().getLongExtra("dateInMillis", 0);

        Log.i("DATE IN LONG", dateInMillis + "");

        //initialise views
        listview = (ListView) findViewById(R.id.order_detailed_list);
        total_price = (TextView) findViewById(R.id.order_price_detailed);
        id = (TextView) findViewById(R.id.order_number_detailed);
        location = (TextView) findViewById(R.id.order_location_detailed);
        date = (TextView) findViewById(R.id.order_date_detailed);
        scanInstructionOutput = (TextView) findViewById(R.id.scan_instruction_textview);
        scanImage1 = (ImageView) findViewById(R.id.scan_image1);
        scanImage2 = (ImageView) findViewById(R.id.scan_image2);
        person = (ImageView) findViewById(R.id.person_imageView);

        checkbox = (CheckBox) findViewById(R.id.status_checkbox);
        xCheckbox = (ImageView) findViewById(R.id.status_imageview);


        //initalise toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ImageView home = (ImageView) findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrderOverviewDetailWithNFCActivity.this, MainActivity.class);
                finish();
                startActivity(intent);
            }
        });
        ImageView scan = (ImageView) findViewById(R.id.go_to_scan);
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderOverviewDetailWithNFCActivity.this, ScanActivity.class);
                startActivity(intent);
            }
        });

        //initialise productlist & fill it with data from DB
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

    private void getOrder(int orderNumber) {
        //get order data from DB
        String URL = "https://easypayserver.herokuapp.com/api/bestelling/" + orderNumber;
        new EasyPayAPIGETOrderConnector(this).execute(URL);
    }

    @Override
    public void onProductAvailable(Product product) {
        price = 0;

        productList.add(product);

        for (int i = 0; i < productList.size(); i++) {
            price = price + productList.get(i).getProductPrice();
        }

        total_price.setText("Totaalprijs €" + String.format("%.2f", price));
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onOrdersAvailable(Order order) {
        this.order = order;

        String url =  "http://easypayserver.herokuapp.com/api/kassamedewerker/getcustomer/" + order.getCustomerId();
        new CustomerDataTask(this).execute(url);

        Log.i("DetailDateFORMAT", order.getDate() + "");
        id.setText("Bestelnummer #" + order.getOrderNumber() + "");
        location.setText(locationPref.getString(order.getLocation(), "Geen locatie"));
        if (dateInMillis != 0) {
            date.setText(formatDateFromMillis(dateInMillis));
        } else {
            //convert Date to milliseconds and add to intent
            long dateInMillis = (order.getDate().getTime() + new Double(2.16e+7).longValue()) - (3600000 * 6);
            date.setText(formatDateFromMillis(dateInMillis));
        }
        //check order status, show adequate view (x/unchecked checkmark/checked checkmark)
        checkStatusForCheckbox(order.getStatus());


        //get all products from this order from DB
        for (int i = 0; i < order.getProductsIDs().size(); i++) {
            String[] URL = {
                    "https://easypayserver.herokuapp.com/api/product/" + order.getProductsIDs().get(i)
            };
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
            order.setStatus("PAID");
            //update database, so that the order has a status of 'PAID'
            new EasyPayAPIPUTConnector().execute(URL + order.getOrderNumber() + "/PAID");
            Log.i(this.getClass().getSimpleName(), "RECEIVED ORDERNR: " + order.getOrderNumber());
            updateCustomerBalance();

            startActivity(getIntent());
            finish();
            Toasty.success(OrderOverviewDetailWithNFCActivity.this, "Bestelling is betaald 2/2.", Toast.LENGTH_SHORT).show();
        }
    }

    public String formatDateFromMillis(long dateInMillis) {
        //convert long dateInMillis back to Date
        Date date = new Date(dateInMillis);

        //format the date
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm - dd/MM/yyyy");
        return sdf.format(date);
    }

    public void checkStatusForCheckbox(String status) {
        switch (status) {
            case "PAID":
                checkbox.setVisibility(View.VISIBLE);
                checkbox.setChecked(true);
                xCheckbox.setVisibility(View.GONE);
                //remove scan instruction images
                scanImage1.setVisibility(View.GONE);
                scanImage2.setVisibility(View.GONE);
                scanInstructionOutput.setVisibility(View.GONE);
                break;
            case "WAITING":
                checkbox.setVisibility(View.VISIBLE);
                checkbox.setChecked(false);
                xCheckbox.setVisibility(View.GONE);
                break;
            case "CANCELED":
                checkbox.setVisibility(View.GONE);
                xCheckbox.setVisibility(View.VISIBLE);
                //remove scan instruction images
                scanImage1.setVisibility(View.GONE);
                scanImage2.setVisibility(View.GONE);
                scanInstructionOutput.setVisibility(View.GONE);
                break;
        }
    }

    public void updateCustomerBalance() {
        if (!statusPaid) {
            //update database, so that the customer balance is updated
            String paymentURL = "https://easypayserver.herokuapp.com/api/klant/afrekening/" +
                    order.getCustomerId() + "/" + price * 100;
            new EasyPayAPIPUTConnector().execute(paymentURL);
            Log.i(TAG, paymentURL);
            Log.i(TAG, order.toString());
            statusPaid = true;
            onResume();
        }
    }

    @Override
    public void onCustomerAvailable(Customer customer) {


        person = (ImageView) findViewById(R.id.person_imageView);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("ID: " + customer.getCustomerId() + "\n" +
                "Achternaam: " + customer.getLastname() + "\n" +
                "Saldo: " + "€" + String.format("%.2f", customer.getBalance().getAmount())).setTitle("Klant:");
        final AlertDialog dialog = builder.create();
        person.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });

        //Stop loading screen
        pd.cancel();

    }
}
