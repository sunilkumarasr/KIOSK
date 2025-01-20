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

import com.provizit.kioskcheckin.R;
import com.provizit.kioskcheckin.services.Conversions;
import com.provizit.kioskcheckin.services.GetCVisitorDetailsModel;

public class EnterYourVehicleDetailsActivity extends AppCompatActivity {

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