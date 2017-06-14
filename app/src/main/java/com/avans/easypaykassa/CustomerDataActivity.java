package com.avans.easypaykassa;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.avans.easypaykassa.ASyncTasks.CustomerDataTask;
import com.avans.easypaykassa.DomainModel.Balance;
import com.avans.easypaykassa.DomainModel.Customer;
import com.avans.easypaykassa.SQLite.BalanceDAO;
import com.avans.easypaykassa.SQLite.DAOFactory;

import java.io.BufferedReader;
import java.io.Console;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class CustomerDataActivity extends AppCompatActivity implements CustomerDataTask.OnCustomerAvailable{

    //Creating all the necessary variables
    TextView name;
    TextView balance;
    TextView ID;
    Customer customer;
    String cId;
    Bundle bundle;

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

        try {
            String FILENAME = "cid_file";
            File file = getFileStreamPath(FILENAME);
            if (file.exists()) {
                System.out.println("File EXISTS");
                FileInputStream fis = openFileInput(FILENAME);
                InputStreamReader inputStreamReader = new InputStreamReader(fis);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(receiveString);
                }

                fis.close();
                cId = stringBuilder.toString();
            }
        } catch(Exception e){
            System.out.println("Error: "+ e);
        }


        //Getting our textfields
        name = (TextView) findViewById(R.id.customer_data_lastname);
        balance = (TextView) findViewById(R.id.customer_data_balance);
        ID = (TextView) findViewById(R.id.customer_data_id);

        startConnectionTask(cId);


    }

    private void startConnectionTask(String cid) {
        new CustomerDataTask(this).execute("https://easypayserver.herokuapp.com/api/kassamedewerker/getcustomer/"+cid);
    }

    @Override
    public void onCustomerAvailable(Customer customer) {
        name.setText(customer.getLastname());
        String balanceText = "" + customer.getBalance().getAmount();
        String idText = "" + customer.getCustomerId();
        balance.setText(balanceText);
        ID.setText(idText);
    }
}
