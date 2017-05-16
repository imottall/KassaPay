package com.avans.easypay;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class AssortmentActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btn_view, btn_alter, btn_add, btn_remove;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assortment);
        context = getApplicationContext();
        btn_view = (Button) findViewById(R.id.assortment_view);
        btn_view.setOnClickListener(this);
        btn_alter = (Button) findViewById(R.id.assortment_alter);
        btn_alter.setOnClickListener(this);
        btn_add = (Button) findViewById(R.id.assortment_add);
        btn_add.setOnClickListener(this);
        btn_remove = (Button) findViewById(R.id.assortment_remove);
        btn_remove.setOnClickListener(this);

    }
//      Hier mogelijk tags doorgeven vanuit database om assortiment te filteren op locatie
    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.assortment_view:
                Intent view = new Intent(context, TabbedViewActivity.class);
                startActivity(view);
                break;

            case R.id.assortment_alter:
                Intent alter = new Intent(context, TabbedAlterAmountsActivity.class);
                startActivity(alter);
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
