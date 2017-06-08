package com.avans.easypaykassa;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.avans.easypaykassa.DomainModel.Balance;
import com.avans.easypaykassa.SQLite.BalanceDAO;
import com.avans.easypaykassa.SQLite.DAOFactory;

public class CustomerDataActivity extends AppCompatActivity {

     //Creating all the necessary variables
    TextView name;
    TextView balance;
    TextView ID;
    private Customer customer;

    public CustomerDataActivity(){
    }

    public CustomerDataActivity(Customer customer){
        this.customer = customer;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_data);

        //Setting up the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ImageView home = (ImageView) findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view){
                Intent intent = new Intent(CustomerDataActivity.this, MainActivity.class);
                finish();
                startActivity(intent);
            }
        });

        ImageView scan = (ImageView) findViewById(R.id.go_to_scan);
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CustomerDataActivity.this, ScanActivity.class);
                startActivity(intent);
            }
        });

        //Getting our textfields
        name = (TextView) findViewById(R.id.customer_data_name);
        balance = (TextView) findViewById(R.id.customer_data_balance);
        ID = (TextView) findViewById(R.id.customer_data_customer_id);

        if(customer != null){
            name.setText(customer.getLastname());
            String balanceText = "" + customer.getBalance();
            String idText = "" + customer.getCustomerId();
            balance.setText(balanceText);
            ID.setText(idText);
        } else{
            String unavailable = "Unavailable";
            name.setText(unavailable);
            balance.setText(unavailable);
            ID.setText(unavailable);
        }
    }


}
