package com.avans.easypaykassa;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.avans.easypaykassa.HCE.LoyaltyCardReader;

/**
 * Created by TB on 5/6/2017.
 */

public class ScanActivity extends AppCompatActivity implements LoyaltyCardReader.OrderCallback {

    private final String TAG = "CARD EMULATOR";
    private NfcAdapter nfcAdapter;
    private LoyaltyCardReader loyaltyCardReader;
    public static int READER_FLAGS =
            NfcAdapter.FLAG_READER_NFC_A | NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK;

    private Button button;
    private TextView messageOutput;

    private int bestellingId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        messageOutput = (TextView) findViewById(R.id.message_textview);

        button = (Button) findViewById(R.id.button_scan);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
//        nfcAdapter = NfcAdapter.getDefaultAdapter(this);


//            if (!nfcAdapter.isEnabled()) {
//                new AlertDialog.Builder(this).setCancelable(true).setMessage("NFC staat momenteel uit. Aanzetten?")
//                        .setPositiveButton("Ja", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                dialog.dismiss();
//                                Intent settingsIntent = new Intent(Settings.ACTION_SETTINGS);
//                                startActivity(settingsIntent);
//                            }
//                        })
//                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                dialog.dismiss();
//                                finish();
//                            }
//                        })
//                        .create().show();
//            }

    }
    @Override
    public void onBestellingReceived(final String account) {
        // This callback is run on a background thread, but updates to UI elements must be performed
        // on the UI thread.
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                bestellingId = 1337;
                messageOutput.setText(bestellingId);
            }
        });
    }

    private void enableReaderMode() {
        Log.i(TAG, "Enabling reader mode");
//        Activity activity = getActivity();
        NfcAdapter nfc = NfcAdapter.getDefaultAdapter(this);
        if (nfc != null) {
            nfc.enableReaderMode(this, loyaltyCardReader, READER_FLAGS, null);
        }
    }

    private void disableReaderMode() {
        Log.i(TAG, "Disabling reader mode");
//        Activity activity = getActivity();
        NfcAdapter nfc = NfcAdapter.getDefaultAdapter(this);
        if (nfc != null) {
            nfc.disableReaderMode(this);
        }
    }
}