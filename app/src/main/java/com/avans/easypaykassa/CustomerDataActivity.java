package com.avans.easypaykassa;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.avans.easypaykassa.DomainModel.Balance;
import com.avans.easypaykassa.SQLite.BalanceDAO;
import com.avans.easypaykassa.SQLite.DAOFactory;

public class CustomerDataActivity extends AppCompatActivity {

    private DAOFactory factory;
    private BalanceDAO balanceDAO;
    private ImageView home;
    private TextView balance;

    private Balance b;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_data);

    }


}
