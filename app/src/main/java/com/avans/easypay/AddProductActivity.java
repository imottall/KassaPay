package com.avans.easypay;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

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