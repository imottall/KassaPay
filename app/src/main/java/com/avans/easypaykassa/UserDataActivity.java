package com.avans.easypaykassa;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.avans.easypaykassa.DomainModel.Employee;

import java.util.regex.Pattern;

import es.dmoral.toasty.Toasty;

public class UserDataActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView firstnameText, lastnameText, usernameText;
    private EditText emailInput, bankNumberInput, hoursWorkedInput;
    private ImageView emailEditBtn, bankNumberEditBtn, hoursWorkedEditBtn, home;

    private Boolean emailEditable = false;
    private Boolean bankNumberEditable = false;
    private Boolean hoursWorkedEditable = false;

    private String currentEmail, currentBankNumber, currentHoursWorked;

    private EasyPayAPIPUTConnector putRequest;

    Employee employee;

    SharedPreferences employeePref;
    SharedPreferences.Editor employeeEdit;

    public static final String PREFERENCEEMPLOYEE = "EMPLOYEE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_data);


        employeePref = getSharedPreferences(PREFERENCEEMPLOYEE, Context.MODE_PRIVATE);
        employeeEdit = employeePref.edit();

        employee = new Employee(employeePref.getInt("ID", 0), employeePref.getString("Username", "")
                , employeePref.getString("Password", ""), employeePref.getString("Email", "")
                , employeePref.getString("FirstName", ""), employeePref.getString("LastName", "")
                , employeePref.getString("Bank", ""), employeePref.getInt("HoursWorked", 0));

        Log.i(this.getClass().getSimpleName(), employee.toString());

        //Setting up the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        home = (ImageView) findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserDataActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        //initialise xml elements
        firstnameText = (TextView) findViewById(R.id.firstname_textview);
        lastnameText = (TextView) findViewById(R.id.lastname_textview);
        usernameText = (TextView) findViewById(R.id.username_textview);
        emailInput = (EditText) findViewById(R.id.email_edittext);
        bankNumberInput = (EditText) findViewById(R.id.banknumber_edittext);
        hoursWorkedInput = (EditText) findViewById(R.id.hours_worked_edittext);

        //Setting text with given employee
        firstnameText.setText(employee.getFirstname());
        lastnameText.setText(employee.getLastname());
        usernameText.setText(employee.getUsername());
        if (employee.getEmail().equals("null")) {
            emailInput.setText("");
        } else {
            emailInput.setText(employee.getEmail());
        }
        if (employee.getBankAccountNumber().equals("null")) {
            bankNumberInput.setText("");
        } else {
            bankNumberInput.setText(employee.getBankAccountNumber());
        }
        hoursWorkedInput.setText(employee.getHoursWorked()+"");

        emailEditBtn = (ImageView) findViewById(R.id.email_edit_btn);
        bankNumberEditBtn = (ImageView) findViewById(R.id.banknumber_edit_btn);
        hoursWorkedEditBtn = (ImageView) findViewById(R.id.hours_worked_edit_btn);

        //add listeners to elements
        emailEditBtn.setOnClickListener(this);
        bankNumberEditBtn.setOnClickListener(this);
        hoursWorkedEditBtn.setOnClickListener(this);

        //start with uneditable EditTexts
        emailInput.setEnabled(false);
        bankNumberInput.setEnabled(false);
        hoursWorkedInput.setEnabled(false);

        //get current user data
        currentEmail = emailInput.getText().toString();
        currentBankNumber = bankNumberInput.getText().toString();
        currentHoursWorked = (hoursWorkedInput.getText().toString());

        //hide keyboard on start
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private boolean validEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
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
                        if (validEmail(emailInput.getText().toString().trim())) {
                            putRequest = new EasyPayAPIPUTConnector();
                            putRequest.execute("https://easypayserver.herokuapp.com/api/kassamedewerker/id="
                                    + employee.getEmployeeId() + "/email=" + emailInput.getText());
                            employeeEdit.putString("Email", emailInput.getText().toString());
                            employeeEdit.commit();
                            Toasty.success(this, "Email gewijzigd.", Toast.LENGTH_LONG).show();
                            currentEmail = emailInput.getText().toString().trim();
                        } else {
                            Toasty.error(this, "Geen geldig email address", Toast.LENGTH_SHORT).show();
                            emailInput.setEnabled(true);
                            emailInput.requestFocus();
                            emailEditBtn.setBackgroundResource(R.drawable.ic_check);
                            emailEditable = true;
                            emailInput.setText(currentEmail);
                        }
                    } else {
                        Toasty.error(this, "Email is hetzelfde als huidige email", Toast.LENGTH_SHORT).show();
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
                        putRequest = new EasyPayAPIPUTConnector();
                        putRequest.execute("https://easypayserver.herokuapp.com/api/kassamedewerker/id="
                                + employee.getEmployeeId()+"/bank="+bankNumberInput.getText());
                        employeeEdit.putString("Bank", bankNumberInput.getText().toString());
                        employeeEdit.commit();
                        Toasty.success(this, "Bankrekeningnummer gewijzigd.", Toast.LENGTH_SHORT).show();
                        currentBankNumber = bankNumberInput.getText().toString().trim();
                    } else {
                        Toasty.error(this, "Bankrekeningnummer is hetzelfde als huidig bankrekeningnummer", Toast.LENGTH_SHORT).show();
                    }
                }
                break;

            case R.id.hours_worked_edit_btn:
                //set hours worked editable
                if (!hoursWorkedEditable) {
                    hoursWorkedInput.setEnabled(true);
                    hoursWorkedInput.requestFocus();
                    hoursWorkedEditBtn.setBackgroundResource(R.drawable.ic_check);
                    hoursWorkedEditable = true;

                    //confirm changes, set hours worked uneditable
                } else {
                    hoursWorkedInput.setEnabled(false);
                    hoursWorkedInput.clearFocus();
                    hoursWorkedEditBtn.setBackgroundResource(R.drawable.ic_data_editable);
                    hoursWorkedEditable = false;
                    if (!(Integer.parseInt(currentHoursWorked) == Integer.parseInt(hoursWorkedInput.getText().toString().trim()))) {
                        putRequest = new EasyPayAPIPUTConnector();
                        putRequest.execute("https://easypayserver.herokuapp.com/api/kassamedewerker/id="
                                + employee.getEmployeeId() + "/hours=" + hoursWorkedInput.getText());
                        employeeEdit.putInt("HoursWorked", Integer.parseInt(hoursWorkedInput.getText().toString()));
                        employeeEdit.commit();
                        Toasty.success(this, "Aantal gewerkte uren gewijzigd.", Toast.LENGTH_SHORT).show();
                        currentHoursWorked = hoursWorkedInput.getText().toString().trim();
                    } else {
                        Toasty.error(this, "Aantal gewerkte uren is hetzelfde als huidig aantal gewerkte uren.", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }
}
