package com.provizit.kioskcheckin.activities.WarningScreens;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.provizit.kioskcheckin.R;
import com.provizit.kioskcheckin.logins.VisitorLoginActivity;

public class TimeValidationPermitActivity extends AppCompatActivity {

    Button btnOk;
    TextView txtNote;

    String type = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_validation_permit);

        type = getIntent().getStringExtra("type");

        btnOk = findViewById(R.id.btnOk);
        txtNote = findViewById(R.id.txtNote);

        if (type.equalsIgnoreCase("material")){
            txtNote.setText(getResources().getString(R.string.PleaseCheckTheDateOfTheMaterialPermit));
        }else {
            txtNote.setText(getResources().getString(R.string.PleaseCheckTheDateOfTheWorkPermit));
        }

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), VisitorLoginActivity.class);
                startActivity(intent);
            }
        });

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), VisitorLoginActivity.class);
        startActivity(intent);
    }

}