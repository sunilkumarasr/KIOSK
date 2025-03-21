package com.provizit.kioskcheckin.activities.WarningScreens;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.provizit.kioskcheckin.R;
import com.provizit.kioskcheckin.logins.VisitorLoginActivity;

public class InValidPermitActivity extends AppCompatActivity {

    Button btnOk;
    TextView txtNote;

    String message = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_validation_permit);

        message = getIntent().getStringExtra("message");

        btnOk = findViewById(R.id.btnOk);
        txtNote = findViewById(R.id.txtNote);

        txtNote.setText(message);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), VisitorLoginActivity.class);
                startActivity(intent);
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), VisitorLoginActivity.class);
                startActivity(intent);
            }
        }, 5000);

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), VisitorLoginActivity.class);
        startActivity(intent);
    }

}