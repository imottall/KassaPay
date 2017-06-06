package com.avans.easypaykassa;

import android.content.Context;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.avans.easypaykassa.DomainModel.Order;
import com.avans.easypaykassa.HCE.LoyaltyCardReader;

import es.dmoral.toasty.Toasty;

/**
 * Created by TB on 5/6/2017.
 */

public class ScanActivity extends AppCompatActivity implements LoyaltyCardReader.AccountCallback {

    private final String TAG = this.getClass().getSimpleName();
    public static int READER_FLAGS =
            NfcAdapter.FLAG_READER_NFC_A | NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK;
    private LoyaltyCardReader loyaltyCardReader;

    private Button button;
    private TextView messageOutput;
    private ImageView scanImage1, scanImage2, checkmarkImage;

    private String URL = "https://easypayserver.herokuapp.com/api/bestelling/update/";
    private int orderNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        //initialise scan images
        scanImage1 = (ImageView) findViewById(R.id.scan_indicator_imageview1);
        scanImage2 = (ImageView) findViewById(R.id.scan_indicator_imageview2);
        //start infinite animation, until NFC succeeded
        Animation pulse = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.pulse);
        scanImage1.startAnimation(pulse);
        scanImage2.startAnimation(pulse);
        checkmarkImage = (ImageView) findViewById(R.id.checkmark_imageview);

        messageOutput = (TextView) findViewById(R.id.message_textview);

        button = (Button) findViewById(R.id.button_scan);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ScanActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        //HCE components
        loyaltyCardReader = new LoyaltyCardReader(this);
        // Disable Android Beam and register our card reader callback
        enableReaderMode();
    }

    @Override
    public void onResume() {
        super.onResume();
        enableReaderMode();
    }

    @Override
    public void onPause() {
        super.onPause();
        disableReaderMode();
    }

    private void enableReaderMode() {
        Log.i(TAG, "Enabling reader mode");
        NfcAdapter nfc = NfcAdapter.getDefaultAdapter(getApplicationContext());
        nfc.setNdefPushMessage(null, ScanActivity.this);
        if (nfc != null) {
            nfc.enableReaderMode(this, loyaltyCardReader, READER_FLAGS, null);
        }
    }

    private void disableReaderMode() {
        Log.i(TAG, "Disabling reader mode");
        NfcAdapter nfc = NfcAdapter.getDefaultAdapter(getApplicationContext());
        nfc.setNdefPushMessage(null, ScanActivity.this);
        if (nfc != null) {
            nfc.disableReaderMode(this);
        }
    }

    //after NFC connection was made...
    @Override
    public void onAccountReceived(final String number) {
        // This callback is run on a background thread, but updates to UI elements must be performed
        // on the UI thread.

        //update order status (in new thread)
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                orderNumber = Integer.parseInt(number);

                //update database, so that the order has a status of 'PAID'
                new EasyPayAPIPUTConnector().execute(URL + orderNumber + "/RECEIVED");

                //proceed to OrderOverviewDetailActivity and send order intent extra
                Intent i = new Intent(getApplicationContext(), OrderOverviewDetailActivity.class);
                Order order = new Order();
                order.setOrderNumber(orderNumber);
                i.putExtra("order", order);
                startActivity(i);
                Toasty.success(ScanActivity.this, "Bestelling is ontvangen 1/2.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}