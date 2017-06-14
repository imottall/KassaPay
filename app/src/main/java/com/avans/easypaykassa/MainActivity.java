package com.avans.easypaykassa;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.avans.easypaykassa.SQLite.BalanceDAO;
import com.avans.easypaykassa.SQLite.DAOFactory;
import com.avans.easypaykassa.SQLite.SQLiteDAOFactory;

import static com.avans.easypaykassa.LoginActivity.LOCATIONID;

public class MainActivity extends AppCompatActivity {

    private DAOFactory factory;
    private BalanceDAO balanceDAO;

    private ImageView logout;
    private int locationID = 0;
    public static final String LOCATION = "location";
    //Balance balance = new Balance(20.00f, new Date());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        exception_handler eh = new exception_handler(this);
        Thread.setDefaultUncaughtExceptionHandler(eh);
        
        factory = new SQLiteDAOFactory(getApplicationContext());
        balanceDAO = factory.createBalanceDAO();

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            locationID = (int) extras.get(LOCATIONID);
            Log.i("LocatieID", "" + locationID);
        }



        //Setting up the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        logout = (ImageView) findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view){
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                finish();
                startActivity(intent);
            }
        });

        ImageView scan = (ImageView) findViewById(R.id.go_to_scan);
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ScanActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume(){
        super.onResume();

    }

    //OnClick
    public void assortButton(View v) {
        Intent intent = new Intent(this, AssortmentActivity.class);
        startActivity(intent);
    }

    public void orderButton(View v){
        Intent intent= new Intent(this, OrderOverviewActivity.class);
        intent.putExtra(LOCATION, locationID);
        startActivity(intent);
    }


    public void balanceButton(View v) {
        Intent intent = new Intent(this, CustomerDataActivity.class);
        startActivity(intent);
    }

    public void accountButton(View v) {
        Intent intent = new Intent(this, UserDataActivity.class);
        startActivity(intent);
    }
}
