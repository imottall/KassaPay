package com.avans.easypaykassa;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.avans.easypaykassa.DomainModel.Balance;
import com.avans.easypaykassa.SQLite.BalanceDAO;
import com.avans.easypaykassa.SQLite.DAOFactory;
import com.avans.easypaykassa.SQLite.SQLiteDAOFactory;

public class AssistantDataActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView firstnameText, lastnameText, usernameText, passwordText;
    private EditText emailInput, bankNumberInput, newPassInput1, newPassInput2;
    private Button emailEditBtn, confirmPassBtn, cancelPassBtn;
    private ImageView bankNumberEditBtn, home;

    private LinearLayout newPassLayout;

    private Boolean passEditable = false;
    private Boolean emailEditable = false;
    private Boolean bankNumberEditable = false;

    private String currentEmail, currentBankNumber;

    private DAOFactory factory;
    private BalanceDAO balanceDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assistant_data);

        factory = new SQLiteDAOFactory(getApplicationContext());
        balanceDAO = factory.createBalanceDAO();

        //Setting up the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ImageView home = (ImageView) findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view){
                Intent intent = new Intent(AssistantDataActivity.this, MainActivity.class);
                finish();
                startActivity(intent);
            }
        });

        ImageView scan = (ImageView) findViewById(R.id.go_to_scan);
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AssistantDataActivity.this, ScanActivity.class);
                startActivity(intent);
            }
        });

        //initialise xml elements
        firstnameText = (TextView) findViewById(R.id.firstname_textview);
        lastnameText = (TextView) findViewById(R.id.lastname_textview);
        usernameText = (TextView) findViewById(R.id.username_textview);
        passwordText = (TextView) findViewById(R.id.password_textview);
        emailInput = (EditText) findViewById(R.id.email_edittext);
        bankNumberInput = (EditText) findViewById(R.id.banknumber_edittext);

        emailEditBtn = (Button) findViewById(R.id.email_edit_btn);
        bankNumberEditBtn = (ImageView) findViewById(R.id.banknumber_edit_btn);

        newPassInput1 = (EditText) findViewById(R.id.new_password_edittext_1);
        newPassInput2 = (EditText) findViewById(R.id.new_password_edittext_2);
        confirmPassBtn = (Button) findViewById(R.id.confirm_password_btn);
        cancelPassBtn = (Button) findViewById(R.id.cancel_password_btn);

        newPassLayout = (LinearLayout) findViewById(R.id.new_password_layout);

        //add listeners to elements
        emailEditBtn.setOnClickListener(this);
        bankNumberEditBtn.setOnClickListener(this);

        confirmPassBtn.setOnClickListener(this);
        cancelPassBtn.setOnClickListener(this);

        //start with uneditable EditTexts
        emailInput.setEnabled(false);
        bankNumberInput.setEnabled(false);

        //get current user data
        currentEmail = emailInput.getText().toString();
        currentBankNumber = bankNumberInput.getText().toString();

        //hide keyboard on start
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
    }

    @Override
    protected void onResume(){
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.email_edit_btn:
                //set email editable
                if (!emailEditable) {
                    emailInput.setEnabled(true);
                    emailInput.requestFocus();
                    emailEditBtn.setBackgroundResource(R.drawable.ic_check);
                    emailEditable = true;

                    //confirm changes, set email uneditable
                } else {
                    emailInput.setEnabled(false);
                    emailInput.clearFocus();
                    emailEditBtn.setBackgroundResource(R.drawable.ic_data_editable);
                    emailEditable = false;
                    if (!currentEmail.equals(emailInput.getText().toString().trim())) {
                        Toast.makeText(this, "Email gewijzigd.", Toast.LENGTH_LONG).show();
                        currentEmail = emailInput.getText().toString().trim();
                    }
                }
                break;

            case R.id.banknumber_edit_btn:
                //set banknumber editable
                if (!bankNumberEditable) {
                    bankNumberInput.setEnabled(true);
                    bankNumberInput.requestFocus();
                    bankNumberEditBtn.setBackgroundResource(R.drawable.ic_check);
                    bankNumberEditable = true;

                    //confirm changes, set banknumber uneditable
                } else {
                    bankNumberInput.setEnabled(false);
                    bankNumberInput.clearFocus();
                    bankNumberEditBtn.setBackgroundResource(R.drawable.ic_data_editable);
                    bankNumberEditable = false;
                    if (!currentBankNumber.equals(bankNumberInput.getText().toString().trim())) {
                        Toast.makeText(this, "Bankrekeningnummer gewijzigd.", Toast.LENGTH_SHORT).show();
                        currentBankNumber = bankNumberInput.getText().toString().trim();
                    }
                }
                break;
        }
    }
}
