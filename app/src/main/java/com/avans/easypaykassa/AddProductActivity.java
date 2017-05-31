package com.avans.easypaykassa;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by TB on 5/10/2017.
 */

public class AddProductActivity extends AppCompatActivity {
    private Button btn_add, btn_cancel;
    private Context context;
    private EditText productName, productPrice;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        context = getApplicationContext();
        btn_cancel = (Button) findViewById(R.id.add_product_cancel);
        //btn_cancel.setOnClickListener(this);
        btn_add = (Button) findViewById(R.id.add_product_confirm);
        //btn_add.setOnClickListener(this);
        productName = (EditText) findViewById(R.id.add_product_name);
        productPrice = (EditText) findViewById(R.id.add_product_price);

        //Setting up the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ImageView home = (ImageView) findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddProductActivity.this, MainActivity.class);
                finish();
                startActivity(intent);
            }
        });

        ImageView scan = (ImageView) findViewById(R.id.go_to_scan);
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddProductActivity.this, ScanActivity.class);
                startActivity(intent);
            }
        });

        addListenerOnSpinnerItemSelection();
        addListenerOnButton();
    }

    public void startAddProductTask() {
        String url = "https://easypayserver.herokuapp.com/api/product/addproduct/" + productName.getText().toString() + "/" + productPrice.getText().toString() + "/" + String.valueOf(spinner.getSelectedItem());
        new EasyPayAPIPUTConnector().execute(url);
    }

    public void addListenerOnSpinnerItemSelection() {
        spinner = (Spinner) findViewById(R.id.category_spinner);
        spinner.setOnItemSelectedListener(new OnCategorySelectedListener());
    }

    public void addListenerOnButton() {
        spinner = (Spinner) findViewById(R.id.category_spinner);
        btn_add = (Button) findViewById(R.id.add_product_confirm);

        btn_add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (productName.getText().length() == 0) {
                    Toast.makeText(AddProductActivity.this,
                            "Vul een productnaam in.",
                            Toast.LENGTH_SHORT).show();
                } else if (productPrice.length() == 0) {
                    Toast.makeText(AddProductActivity.this,
                            "Ga zelf gratis dingen verkopen ofzo wtf doe normaal.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    startAddProductTask();
                    Toast.makeText(AddProductActivity.this,
                            " NIEUW PRODUCT " +
                                    "\nNaam : " + productName.getText() +
                                    "\nPrijs : " + productPrice.getText() +
                                    "\nCategorie : " + String.valueOf(spinner.getSelectedItem()),
                            Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }
}