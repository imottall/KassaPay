package com.avans.easypaykassa;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.avans.easypaykassa.ASyncTasks.LoginTask;
import com.avans.easypaykassa.DomainModel.Employee;

public class LoginActivity extends AppCompatActivity implements LoginTask.OnEmployeeAvailable {

    private String TAG = this.getClass().getSimpleName();

    private TextView usernameInput, passwordInput;
    private Employee employee;

    private CheckBox check;

    private String username, password;
    private int loginDelay = 400;

    //Progress Dialog
    ProgressDialog pd;

    private SharedPreferences loginPref;
    private SharedPreferences.Editor loginEdit;

    private SharedPreferences employeePref;
    private SharedPreferences.Editor employeeEdit;

    public static final String PREFERENCEEMPLOYEE = "EMPLOYEE";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //initialise xml elements
        usernameInput = (TextView) findViewById(R.id.username_textview);
        passwordInput = (TextView) findViewById(R.id.password_textview);

        //initialise SharedPreferences objects
        employeePref = getSharedPreferences(PREFERENCEEMPLOYEE, Context.MODE_PRIVATE);
        employeeEdit = employeePref.edit();
    }

    public void loginBtn(View v) {

        //show loading animation
        username = usernameInput.getText().toString().trim().toLowerCase();
        password = passwordInput.getText().toString();

        //EditTexts empty?
        if (!username.equals("") && !password.equals("")) {
            pd = new ProgressDialog(this);
            pd.setMessage("Aan het inloggen...");
            pd.show();

            startLoginTask();
        } else {
            Toast.makeText(this, "Een of meer velden zijn niet ingevuld.", Toast.LENGTH_LONG).show();
        }
    }

    //start LoginTask (AsyncTask)
    public void startLoginTask() {
        new LoginTask(this).execute("https://easypayserver.herokuapp.com/api/kassamedewerker/login/" + username);
    }

    @Override
    public void onEmployeeAvailable(Employee employee) {
        this.employee = employee;

        //end ProgressDialog
        pd.cancel();

        //LoginTask did not return a employee, so username = invalid
        if (employee == null) {
            Toast.makeText(LoginActivity.this, "Gebruikersnaam bestaat niet.", Toast.LENGTH_LONG).show();
            passwordInput.setText("");

            //username and password input is a valid employee
        } else if (username.equals(employee.getUsername()) && password.equals(employee.getPassword())) {
            Intent i = new Intent(LoginActivity.this, MainActivity.class);
            employeeEdit.putInt("ID", employee.getEmployeeId());
            employeeEdit.putString("Username", employee.getUsername());
            employeeEdit.putString("Password", employee.getUsername());
            employeeEdit.putString("Email", employee.getEmail());
            employeeEdit.putString("FirstName", employee.getFirstname());
            employeeEdit.putString("LastName", employee.getLastname());
            employeeEdit.putString("Bank", employee.getBankAccountNumber());
            employeeEdit.putInt("HoursWorked", employee.getHoursWorked());
            employeeEdit.commit();
            startActivity(i);
            finish();

            //username exists, but password is invalid
        } else {
            Toast.makeText(LoginActivity.this, "Gegevens zijn onjuist.", Toast.LENGTH_LONG).show();
            passwordInput.setText("");
        }
    }
}
