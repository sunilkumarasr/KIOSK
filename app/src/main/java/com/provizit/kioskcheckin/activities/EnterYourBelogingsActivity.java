package com.provizit.kioskcheckin.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.animation.AnimationSet;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.provizit.kioskcheckin.R;
import com.provizit.kioskcheckin.services.Conversions;
import com.provizit.kioskcheckin.services.GetCVisitorDetailsModel;
import com.provizit.kioskcheckin.config.Preferences;

public class EnterYourBelogingsActivity extends AppCompatActivity {

    ImageView company_logo;
    GetCVisitorDetailsModel model;
    // Define a constant for "model_key"
    private static final String MODEL_KEY = "model_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_your_belogings);
        Intent intent = getIntent();
        model = (GetCVisitorDetailsModel) intent.getSerializableExtra(MODEL_KEY);


        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String VersionName = sharedPreferences.getString("VersionName", "1.0");
        TextView txtVersion = findViewById(R.id.txtVersion);
        txtVersion.setText(VersionName);


        Button btn_next=findViewById(R.id.btn_next);
        company_logo = findViewById(R.id.company_logo);
        btn_next.setOnClickListener(view -> {
            AnimationSet animation1 = Conversions.animation();
            view.startAnimation(animation1);
            Intent intent12 =new Intent(getApplicationContext(),ConfirmationActivity.class);
            intent12.putExtra(MODEL_KEY, model);
            startActivity(intent12);
        });


        ImageView back_image=findViewById(R.id.back_image);
        back_image.setOnClickListener(view -> {
            AnimationSet animation1 = Conversions.animation();
            view.startAnimation(animation1);
            Intent intent1 =new Intent(getApplicationContext(),EnterYourVehicleDetailsActivity.class);
            intent1.putExtra(MODEL_KEY, model);
            startActivity(intent1);
        });

        //company logo
        String c_Logo = Preferences.loadStringValue(getApplicationContext(), Preferences.company_Logo, "");
        if (c_Logo.equalsIgnoreCase("")){
        }else {
            Glide.with(EnterYourBelogingsActivity.this).load(c_Logo)
                    .into(company_logo);
        }


    }

}