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
import com.provizit.kioskcheckin.config.Preferences;
import com.provizit.kioskcheckin.services.Conversions;
import com.provizit.kioskcheckin.services.GetCVisitorDetailsModel;

public class EnterYourVehicleDetailsActivity extends AppCompatActivity {

    ImageView company_logo;
    GetCVisitorDetailsModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_your_vehicle_details);
        Intent iin = getIntent();
        Bundle b = iin.getExtras();
        try {
            if (b != null) {
                model = (GetCVisitorDetailsModel) iin.getSerializableExtra("model_key");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        company_logo = findViewById(R.id.company_logo);

        //company logo
        String c_Logo = Preferences.loadStringValue(getApplicationContext(), Preferences.company_Logo, "");
        if (c_Logo.equalsIgnoreCase("")){
        }else {
            Glide.with(EnterYourVehicleDetailsActivity.this).load(c_Logo)
                    .into(company_logo);
        }

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String VersionName = sharedPreferences.getString("VersionName", "1.0");
        TextView txtVersion = findViewById(R.id.txtVersion);
        txtVersion.setText(VersionName);

        Button btn_next=findViewById(R.id.btn_next);
        btn_next.setOnClickListener(view -> {
            AnimationSet animation1 = Conversions.animation();
            view.startAnimation(animation1);
            Intent intent=new Intent(getApplicationContext(),EnterYourBelogingsActivity.class);
            intent.putExtra("model_key", model);
            startActivity(intent);
        });
        ImageView back_image=findViewById(R.id.back_image);
        back_image.setOnClickListener(view -> {
            AnimationSet animation1 = Conversions.animation();
            view.startAnimation(animation1);
            Intent intent=new Intent(getApplicationContext(),EnterYourDetailsActivity.class);
            intent.putExtra("model_key", model);
            startActivity(intent);
        });
    }
}