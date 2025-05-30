package com.provizit.kioskcheckin.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AnimationSet;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.provizit.kioskcheckin.R;
import com.provizit.kioskcheckin.config.Preferences;
import com.provizit.kioskcheckin.logins.VisitorLoginActivity;
import com.provizit.kioskcheckin.mvvm.ApiViewModel;
import com.provizit.kioskcheckin.services.Conversions;

import org.json.JSONArray;
import org.json.JSONObject;


public class ChekInPermitStatusActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView back_image, company_logo;
    TextView txtNote;
    Button btn_ok;
    Handler handler = new Handler();

    String status = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkin_status);

        status = getIntent().getStringExtra("status");


        back_image = findViewById(R.id.back_image);
        btn_ok = findViewById(R.id.btn_ok);
        company_logo = findViewById(R.id.company_logo);
        txtNote = findViewById(R.id.txtNote);

        // Define your Runnable
        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                // Your code here
                Intent intents = new Intent(getApplicationContext(), VisitorLoginActivity.class);
                startActivity(intents);
            }
        };
        handler.postDelayed(myRunnable, 5000);


        if (status.equalsIgnoreCase("1")) {
            txtNote.setText(R.string.SuccessfullyCheckIn);
        } else {
            txtNote.setText(R.string.SuccessfullyCheckOut);
        }

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String VersionName = sharedPreferences.getString("VersionName", "1.0");
        TextView txtVersion = findViewById(R.id.txtVersion);
        txtVersion.setText(VersionName);

        //company logo
        String c_Logo = Preferences.loadStringValue(getApplicationContext(), Preferences.company_Logo, "");
        if (c_Logo.equalsIgnoreCase("")) {
        } else {
            Glide.with(ChekInPermitStatusActivity.this).load(c_Logo)
                    .into(company_logo);
        }

        btn_ok.setOnClickListener(this);
        back_image.setOnClickListener(this);
    }


    //disable auto click action after scann
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_ENTER) {
            // Barcode scanner has scanned a barcode, disable triggered items
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_DEL) {
            return true;
        }

        disableTriggeredItems();


        return super.onKeyDown(keyCode, event);
    }

    //usb scanner
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (event.getKeyCode()) {
                case KeyEvent.KEYCODE_ENTER:
                    return true;
                case KeyEvent.KEYCODE_BACK:
                    Intent intent = new Intent(getApplicationContext(), VisitorLoginActivity.class);
                    startActivity(intent);
                    return true;
                case KeyEvent.KEYCODE_DEL:
                    Log.d("KeyEvent", "DEL in dispatchKeyEvent");
                    break;
                default:
                    char keyChar = (char) event.getUnicodeChar();
                    if (Character.isLetterOrDigit(keyChar)) {
                        return true;
                    }
            }
        }
        return super.dispatchKeyEvent(event);
    }

    private void disableTriggeredItems() {
        btn_ok.setFocusable(false);
        btn_ok.setFocusableInTouchMode(false);
        back_image.setFocusable(false);
        back_image.setFocusableInTouchMode(false);

        runthred();
    }

    private void runthred() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                overridePendingTransition(0, 0);
                startActivity(getIntent());
                overridePendingTransition(0, 0);

                btn_ok.setFocusable(true);
                btn_ok.setFocusableInTouchMode(true);
                back_image.setFocusable(true);
                back_image.setFocusableInTouchMode(true);
            }
        }, 500);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_ok:
                AnimationSet animation1 = Conversions.animation();
                view.startAnimation(animation1);
                Intent intent = new Intent(getApplicationContext(), VisitorLoginActivity.class);
                startActivity(intent);
                handler.removeCallbacksAndMessages(null);
                break;
            case R.id.back_image:
                animation1 = Conversions.animation();
                view.startAnimation(animation1);
                Intent intent1 = new Intent(getApplicationContext(), VisitorLoginActivity.class);
                startActivity(intent1);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), VisitorLoginActivity.class);
        startActivity(intent);
    }

}