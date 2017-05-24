package com.avans.easypaykassa;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

/**
 * Created by TB on 5/10/2017.
 */

public class AddProductActivity extends AppCompatActivity implements View.OnClickListener{
    private Button btn_add, btn_cancel;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        context = getApplicationContext();
        btn_cancel = (Button) findViewById(R.id.add_product_cancel);
        btn_cancel.setOnClickListener(this);
        btn_add = (Button) findViewById(R.id.add_product_confirm);
        btn_add.setOnClickListener(this);

        //Setting up the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ImageView home = (ImageView) findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view){
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
    }
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.add_product_confirm:
                finish();
                break;

            case R.id.add_product_cancel:
                finish();
                break;
        }

    }
}