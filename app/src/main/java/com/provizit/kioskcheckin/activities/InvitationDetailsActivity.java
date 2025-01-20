package com.provizit.kioskcheckin.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.animation.AnimationSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.provizit.kioskcheckin.R;
import com.provizit.kioskcheckin.services.Conversions;
import com.provizit.kioskcheckin.services.GetCVisitorDetailsModel;

public class InvitationDetailsActivity extends AppCompatActivity {


    GetCVisitorDetailsModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitation_details);

        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        if(b!=null) {
            model = (GetCVisitorDetailsModel) intent.getSerializableExtra("model_key");
        }

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String VersionName = sharedPreferences.getString("VersionName", "1.0");
        TextView txtVersion = findViewById(R.id.txtVersion);
        txtVersion.setText(VersionName);

        ImageView back_image=findViewById(R.id.back_image);
        back_image.setOnClickListener(view -> {
            AnimationSet animation1 = Conversions.animation();
            view.startAnimation(animation1);
            Intent inten=new Intent(getApplicationContext(),InvitationActivity.class);
            inten.putExtra("model_key",model);
            startActivity(inten);
        });

    }


    //disable auto click action after scann
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_ENTER) {
            // Barcode scanner has scanned a barcode, disable triggered items
            return true;
        }else {
        }


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
                    Intent inten=new Intent(getApplicationContext(),InvitationActivity.class);
                    inten.putExtra("model_key",model);
                    startActivity(inten);
                    return true;
                default:
                    char keyChar = (char) event.getUnicodeChar();
                    return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent inten=new Intent(getApplicationContext(),InvitationActivity.class);
        inten.putExtra("model_key",model);
        startActivity(inten);
    }
}