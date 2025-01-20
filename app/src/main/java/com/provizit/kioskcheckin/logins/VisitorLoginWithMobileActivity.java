package com.provizit.kioskcheckin.logins;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AnimationSet;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;
import com.provizit.kioskcheckin.R;
import com.provizit.kioskcheckin.services.Conversions;
import com.provizit.kioskcheckin.services.GetCVisitorDetailsModel;

public class VisitorLoginWithMobileActivity extends AppCompatActivity {

    Button btn_proceed;
    TextInputLayout textInputLayout;
    EditText visitor_email;
    String emailPattern;

    GetCVisitorDetailsModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visitorlogin_withmobile);

        Intent intent = getIntent();
        model = (GetCVisitorDetailsModel) intent.getSerializableExtra("model_key");


        Button btn_proceed=findViewById(R.id.btn_proceed);
        btn_proceed.setOnClickListener(view -> {
            AnimationSet animation = Conversions.animation();
            view.startAnimation(animation);

            Intent inten=new Intent(getApplicationContext(), OTPActivity.class);
            inten.putExtra("model_key", model);
            startActivity(inten);
        });


    }
}