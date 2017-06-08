package com.avans.easypaykassa.ASyncTasks;

import android.os.AsyncTask;
import android.util.Log;

import com.avans.easypaykassa.DomainModel.Balance;
import com.avans.easypaykassa.DomainModel.Customer;
import com.avans.easypaykassa.DomainModel.Order;

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
 * Created by TB. on 5/30/2017.
 */

public class OrderDataTask extends AsyncTask<String, Void, String> {

    private static final String TAG = CustomerDataTask.class.getSimpleName();
    private OnOrderAvailable listener = null;

    public OrderDataTask(OnOrderAvailable listener) {
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

        JSONObject jso, jsot;
        JSONArray jsa;
        int orderId, customerId, orderNumber;
        String location, status;

        try {
            jso = new JSONObject(response);
            Log.d("JSO:","" + jso);
            jsa = jso.getJSONArray("items");
            Log.d("JSA:","" + jsa);
            jsot = jsa.getJSONObject(0);
            Log.d("JSOT:","" + jsot);

            if (jsot != null && jsot.length() > 0) {
                orderId = jsot.optInt("BestellingID");
                customerId = jsot.optInt("KlantId");
                orderNumber = jsot.optInt("bestellingNummer");
                location = jsot.optString("Locatie");
                status = jsot.optString("Status");

                //Order o = new Order(orderId,customerId,productId,orderNumber);

                //call back with customer that was been searched for
                //listener.onOrderAvailable(o);
            } else {
                //return null if no customer was found
                listener.onOrderAvailable(null);
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
    public interface OnOrderAvailable {
        void onOrderAvailable(Order order);
    }
}


//int total;
//for(int i = 0; i < productlist.size; i++){
//        total += productslist.get(i).getProductPrice;
//        };
//        if(total > Customer.getBalance);