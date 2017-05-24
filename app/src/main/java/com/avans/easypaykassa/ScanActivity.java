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

    private final String TAG = "CARD EMULATOR";
    public static int READER_FLAGS =
            NfcAdapter.FLAG_READER_NFC_A | NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK;
    private LoyaltyCardReader loyaltyCardReader;

    private Button button;
    private TextView messageOutput;

    private String URL = "https://easypayserver.herokuapp.com/api/bestelling/update/";
    private int orderNumber, nextActivityDelay = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        messageOutput = (TextView) findViewById(R.id.message_textview);

        button = (Button) findViewById(R.id.button_scan);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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

    @Override
    public void onAccountReceived(final String number) {
        // This callback is run on a background thread, but updates to UI elements must be performed
        // on the UI thread.
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                orderNumber = Integer.parseInt(number);

                messageOutput.setText("Bestelling nummer: " + orderNumber + " ontvangen.");
                Log.i(TAG, "Put request wordt gedaan met de volgende URL");
                Log.i(TAG, URL + orderNumber);

                //update database, so that the order has a status of 'PAID'
                new EasyPayAPIPUTConnector().execute(URL + orderNumber);

                Intent i = new Intent(getApplicationContext(), OverviewCurrentOrdersActivity.class);
                startActivity(i);

                //show feedback on UI
                ImageView checkmarkImage = (ImageView) findViewById(R.id.nfc_connected_checkmark_image);
                Animation checkmarkAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.check_mark_anim);
                checkmarkImage.startAnimation(checkmarkAnimation);
            }
        });
    }
}