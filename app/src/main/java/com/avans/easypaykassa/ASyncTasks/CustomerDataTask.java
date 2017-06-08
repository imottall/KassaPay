package com.avans.easypaykassa.ASyncTasks;

import android.os.AsyncTask;
import android.util.Log;

import com.avans.easypaykassa.DomainModel.Balance;
import com.avans.easypaykassa.DomainModel.Customer;

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
import java.util.Date;

/**
 * Created by Felix on 10-5-2017.
 * Altered by TB. on 5/30/2017.
 */

public class CustomerDataTask extends AsyncTask<String, Void, String> {

    private static final String TAG = CustomerDataTask.class.getSimpleName();
    private OnCustomerAvailable listener = null;

    public CustomerDataTask(OnCustomerAvailable listener) {
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

        JSONArray jsa;

        try {
            int customerId = 0;
            String lastname = "unavailable";
            float balanceInt = 0;
            float b = 0;

            jsa = new JSONArray(response);
            Log.d("JSON:","" + jsa);

            JSONObject jso = jsa.getJSONObject(0);
            Log.d("JSO","" + jso);
            if (jso != null && jso.length() > 0) {
                customerId = jso.optInt("KlantId");
                lastname = jso.optString("achternaam");
                balanceInt = (float) jso.optInt("saldo");
                b = balanceInt/100;

                Balance balance = new Balance(b, new Date());
                Customer c = new Customer(customerId, lastname, balance);

                //call back with customer that was been searched for
                listener.onCustomerAvailable(c);
            } else {
                //return null if no customer was found
                listener.onCustomerAvailable(null);
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
    public interface OnCustomerAvailable {
        void onCustomerAvailable(Customer customer);
    }
}
