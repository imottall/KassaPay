package com.avans.easypaykassa;

import android.animation.AnimatorInflater;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.avans.easypaykassa.HCE.LoyaltyCardReader;

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

//        if (!nfc.isEnabled()) {
//            new AlertDialog.Builder(this).setCancelable(true).setMessage("NFC staat momenteel uit. Aanzetten?")
//                    .setPositiveButton("Ja", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//                            dialog.dismiss();
//                            Intent settingsIntent = new Intent(Settings.ACTION_SETTINGS);
//                            startActivity(settingsIntent);
//                        }
//                    })
//                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//                            dialog.dismiss();
//                            finish();
//                        }
//                    })
//                    .create().show();
//        }
    }

    @Override
    public void onPause() {
        super.onPause();
        disableReaderMode();
    }

    private void enableReaderMode() {
        Log.i(TAG, "Enabling reader mode");
        NfcAdapter nfc = NfcAdapter.getDefaultAdapter(getApplicationContext());
        if (nfc != null) {
            nfc.enableReaderMode(this, loyaltyCardReader, READER_FLAGS, null);
        }
    }

    private void disableReaderMode() {
        Log.i(TAG, "Disabling reader mode");
        NfcAdapter nfc = NfcAdapter.getDefaultAdapter(getApplicationContext());
        if (nfc != null) {
            nfc.disableReaderMode(this);
        }
    }

    //after NFC connection was made...
    @Override
    public void onAccountReceived(final String number) {
        // This callback is run on a background thread, but updates to UI elements must be performed
        // on the UI thread.

        //show feedback on UI
        checkMarkAnimFeedback();

//        if (number.equals("PAID")) {
//            Log.i(TAG, "---------------------------------");
//            Log.i(TAG, "Order status = PAID");
//            Log.i(TAG, "---------------------------------");
//            //update database, so that the order has a status of 'RECEIVED'
//            new EasyPayAPIPUTConnector().execute(URL + orderNumber + "PAID");
//        } else {
            //update order status (in new thread)
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    orderNumber = Integer.parseInt(number);

                    messageOutput.setText("Bestelling nummer: " + orderNumber + " ontvangen.");
                    Log.i(TAG, "Put request wordt gedaan met de volgende URL");
                    Log.i(TAG, URL + orderNumber);

                    //update database, so that the order has a status of 'PAID'
                    new EasyPayAPIPUTConnector().execute(URL + orderNumber + "/RECEIVED");

                    Intent i = new Intent(getApplicationContext(), OrderOverviewDetailActivity.class);
                    startActivity(i);
                }
            });
//        }
    }

    public void checkMarkAnimFeedback() {
        //fade out scan image
        Animation fadeOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_fade_out);
        scanImage1.setAnimation(fadeOut);
        scanImage2.setAnimation(fadeOut);

        //fade in check mark image
        Animation fadeIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_fade_in);
        checkmarkImage.startAnimation(fadeIn);
        checkmarkImage.setVisibility(View.VISIBLE);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //animate check mark image
                scanImage1.setVisibility(View.GONE);
                scanImage2.setVisibility(View.GONE);
                Animation animationClockwise = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_clockwise);
                checkmarkImage.startAnimation(animationClockwise);
            }
        }, 500);
    }
}