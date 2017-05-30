package com.avans.easypaykassa.ASyncTasks;

import android.os.AsyncTask;
import android.util.Log;

import com.avans.easypaykassa.DomainModel.Customer;
import com.avans.easypaykassa.DomainModel.Employee;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Felix on 10-5-2017.
 */

public class LoginTask extends AsyncTask<String, Void, String> {

    private static final String TAG = LoginTask.class.getSimpleName();
    private OnEmployeeAvailable listener = null;

    public LoginTask(OnEmployeeAvailable listener) {
        this.listener = listener;
    }

    @Override
    protected String doInBackground(String... params) {

        InputStream inputStream = null;
        String response = "";
        int responseCode = -1;
        String klantURL = params[0];
        try {
            URL url = new URL(klantURL);
            URLConnection urlConn = url.openConnection();

            if (!(urlConn instanceof HttpURLConnection)) {
                return null;
            }

            //initialise HTTP connection
            HttpURLConnection httpConn = (HttpURLConnection) urlConn;
            httpConn.setAllowUserInteraction(false);
            httpConn.setInstanceFollowRedirects(true);
            httpConn.setRequestMethod("GET");

            //request connection using given URL
            httpConn.connect();

            responseCode = httpConn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                inputStream = httpConn.getInputStream();
                response = getStringFromInputStream(inputStream);
            } else {
                Log.e(TAG, "Error, invalid response");
            }
        } catch (MalformedURLException e) {
            Log.e(TAG, "doInBackground | MalformedURLException " + e.getLocalizedMessage());
        } catch (IOException e) {
            Log.e(TAG, "doInBackground | IOException" + e.getLocalizedMessage());
        }
        return response;
    }

    @Override
    protected void onPostExecute(String response) {
        //check if response is valid
        if (response == null || response.equals("")) {
            return;
        }

        JSONObject json;

        try {
            json = new JSONObject(response);
            JSONArray items = json.getJSONArray("items");
            JSONObject employee = items.optJSONObject(0);

            if (employee != null) {
                int employeeId = employee.optInt("KassamedewerkerId");
                String username = employee.optString("Gebruikersnaam");
                String password = employee.optString("Wachtwoord");
                String email = employee.optString("Email");
                String firstname = employee.optString("Voornaam");
                String lastname = employee.optString("Achternaam");
                String bankAccountNumber = employee.optString("Bankrekeningnummer");
                int hoursWorked = employee.optInt("UrenGewerkt");

                //call back with customer that was been searched for
                Employee e = new Employee(employeeId, username, password, email, firstname, lastname, bankAccountNumber, hoursWorked);
                listener.onEmployeeAvailable(e);
            } else {
                //return null if no customer was found
                listener.onEmployeeAvailable(null);
            }
        } catch (JSONException e) {
            e.printStackTrace();

        }
    }

    private static String getStringFromInputStream(InputStream is) {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }

    //call back interface
    public interface OnEmployeeAvailable {
        void onEmployeeAvailable(Employee employee);
    }
}
