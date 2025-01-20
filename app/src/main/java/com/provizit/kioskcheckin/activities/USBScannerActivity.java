package com.provizit.kioskcheckin.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.provizit.kioskcheckin.R;
import com.provizit.kioskcheckin.config.Preferences;

public class USBScannerActivity extends AppCompatActivity {

    TextView txt_valid;
    ImageView back_image;
    //usb scanner
    private String usbScannedData = "";
    String nda_Data = "";
    String badge_Data = "";
    String pic_Data = "";
    String logoPass = "";
    String ccp = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usbscanner);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String VersionName = sharedPreferences.getString("VersionName", "1.0");
        TextView txtVersion = findViewById(R.id.txtVersion);
        txtVersion.setText(VersionName);


        //shared Preferences
        nda_Data = Preferences.loadStringValue(getApplicationContext(), Preferences.nda_Data, "");
        logoPass = Preferences.loadStringValue(getApplicationContext(), Preferences.logoPass, "");
        badge_Data = Preferences.loadStringValue(getApplicationContext(), Preferences.badge_Data, "");
        pic_Data = Preferences.loadStringValue(getApplicationContext(), Preferences.pic_Data, "");
        ccp = Preferences.loadStringValue(getApplicationContext(), Preferences.country_Code, "");


        txt_valid = findViewById(R.id.txt_valid);
        back_image = findViewById(R.id.back_image);
        back_image.setOnClickListener(v -> finish());

    }

    //usb scanner
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (event.getKeyCode()) {
                case KeyEvent.KEYCODE_ENTER:
                    handleBarcodeScan(usbScannedData);
                    usbScannedData = "";
                    return true;
                default:
                    char keyChar = (char) event.getUnicodeChar();
                    System.out.println("anil");
                    if (String.valueOf(keyChar).equalsIgnoreCase("#")) {
                        usbScannedData = "";
                    } else if (String.valueOf(keyChar).equalsIgnoreCase(" ")) {

                    } else {
                        usbScannedData += String.valueOf(keyChar);
                    }
                    return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }

    private void handleBarcodeScan(String barcodeData) {

        Toast.makeText(getApplicationContext(), barcodeData, Toast.LENGTH_SHORT).show();


        if (!barcodeData.equalsIgnoreCase("")) {
            txt_valid.setVisibility(View.GONE);
            Intent intent = new Intent(getApplicationContext(), ResultUSBScannerActivity.class);
            overridePendingTransition(0, 0);
            intent.putExtra("barcodeData", barcodeData);
            intent.putExtra("ccp", ccp);
            intent.putExtra("logoPass", logoPass);
            startActivity(intent);
            finish();
        } else {
            txt_valid.setVisibility(View.VISIBLE);
        }


    }


}