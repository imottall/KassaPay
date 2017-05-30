package com.avans.easypaykassa;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class AssortmentActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btn_view, btn_add, btn_remove;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assortment);
        context = getApplicationContext();
        btn_view = (Button) findViewById(R.id.assortment_view);
        btn_view.setOnClickListener(this);
        btn_add = (Button) findViewById(R.id.assortment_add);
        btn_add.setOnClickListener(this);
        btn_remove = (Button) findViewById(R.id.assortment_remove);
        btn_remove.setOnClickListener(this);

        //Setting up the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ImageView home = (ImageView) findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view){
                Intent intent = new Intent(AssortmentActivity.this, MainActivity.class);
                finish();
                startActivity(intent);
            }
        });

        ImageView scan = (ImageView) findViewById(R.id.go_to_scan);
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AssortmentActivity.this, ScanActivity.class);
                startActivity(intent);
            }
        });

    }
//      Hier mogelijk tags doorgeven vanuit database om assortiment te filteren op locatie
    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.assortment_view:
                Intent view = new Intent(context, TabbedActivity.class);
                startActivity(view);
                break;

            case R.id.assortment_add:
                Intent add = new Intent(context, AddProductActivity.class);
                startActivity(add);
                break;

            case R.id.assortment_remove:
                Intent remove = new Intent(context, TabbedRemoveProductsActivity.class);
                startActivity(remove);
                break;
        }

    }
}
