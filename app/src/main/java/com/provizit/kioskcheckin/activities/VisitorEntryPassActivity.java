package com.provizit.kioskcheckin.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.provizit.kioskcheckin.logins.VisitorLoginActivity;
import com.provizit.kioskcheckin.R;
import com.provizit.kioskcheckin.services.Conversions;
import com.provizit.kioskcheckin.api.DataManger;
import com.provizit.kioskcheckin.services.GetCVisitorDetailsModel;
import com.provizit.kioskcheckin.config.Preferences;

import java.text.SimpleDateFormat;
import java.util.Date;

public class VisitorEntryPassActivity extends AppCompatActivity {
    TextView name, designation, mobile, organization, badge, e_name, e_desiganation, date, cabin, validUpto;
    LinearLayout badgeNo;
    ImageView emp_pic;

    GetCVisitorDetailsModel model;
    String badge_b = "";
    String filename = "";

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visitor_entry_pass);
        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        if(b!=null) {
            badge_b = b.getString("badge");
            filename = b.getString("filename");
            model = (GetCVisitorDetailsModel) intent.getSerializableExtra("model_key");
        }

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String VersionName = sharedPreferences.getString("VersionName", "1.0");
        TextView txtVersion = findViewById(R.id.txtVersion);
        txtVersion.setText(VersionName);


        new android.os.Handler().postDelayed(
                () -> {
                    Intent intent1 = new Intent(getApplicationContext(), VisitorLoginActivity.class);
                    startActivity(intent1);
                }, 20000);

        name = findViewById(R.id.name);
        designation = findViewById(R.id.designation);
        mobile = findViewById(R.id.mobile);
        organization = findViewById(R.id.organization);
        badge = findViewById(R.id.badge);
        badgeNo = findViewById(R.id.badgeNo);
        e_name = findViewById(R.id.e_name);
        e_desiganation = findViewById(R.id.e_desiganation);
        date = findViewById(R.id.date);
        cabin = findViewById(R.id.cabin);
        validUpto = findViewById(R.id.validUpto);
        emp_pic = findViewById(R.id.emp_pic);

        name.setText(model.getIncomplete_data().getName());
        designation.setText(model.getIncomplete_data().getDesignation());
        mobile.setText(model.getIncomplete_data().getMobile());
        organization.setText(model.getIncomplete_data().getCompany());
        date.setText("");
        cabin.setText("");

//        e_name.setText(model.getItems().getEmployee().getName());
//        e_desiganation.setText(model.getItems().getEmployee().getDesignation());

        Float meeting_status = model.getItems().getMeetingStatus();
        Float checkin_status = model.getItems().getCheckINStatus();
        Float visitor_status = model.getItems().getVisitorStatus();

        e_name.setText("");
        e_desiganation.setText("");
        badge.setText("");
        if (checkin_status == 1) {
            badgeNo.setVisibility(View.VISIBLE);
            e_name.setText(model.getItems().getEmployee().getName());
            e_desiganation.setText(model.getItems().getEmployee().getDesignation());
            badge.setText(model.getTotal_counts().getBadge());
        }
        if (meeting_status == 1) {
            date.setText(Conversions.millitotime((model.getItems().getStart() + Conversions.timezone()) * 1000, false)
                    + " - " + Conversions.millitotime((model.getItems().getEnd() + 1 + Conversions.timezone()) * 1000, false));
            cabin.setText(model.getItems().getMeetingrooms().getName());
            e_name.setText(model.getItems().getEmployee().getName());
            e_desiganation.setText(model.getItems().getEmployee().getDesignation());
        } else if (checkin_status == 0){
            e_name.setText(model.getItems().getEmployee().getDepartment());
        }

        if (model.getItems().getEmployee().getPic() != null && model.getItems().getEmployee().getPic().size() != 0) {
            //preferences
            String Comp_id = Preferences.loadStringValue(getApplicationContext(),Preferences.Comp_id,"");
            Glide.with(VisitorEntryPassActivity.this).load(DataManger.IMAGE_URL + "/uploads/" + Comp_id + "/" + model.getItems().getEmployee().getPic().get(model.getItems().getEmployee().getPic().size() - 1))
                    .into(emp_pic);
        }

//        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
//        LocalDateTime now = LocalDateTime.now();
//        date.setText(Conversions.millitotime((model.getItems().getStart()+Conversions.timezone()) * 1000,false)
//                +" - "+Conversions.millitotime((model.getItems().getEnd()+1+Conversions.timezone()) * 1000,false));

        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy");
        String str = formatter.format(date);
        validUpto.setText(str);
//        cabin.setText(model.getItems().getMeetingrooms().getName());

//        cabin.setText(model.getItems().getMeetingrooms().getName());

        ImageView p_image = findViewById(R.id.p_image);
        p_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnimationSet animation = Conversions.animation();
                view.startAnimation(animation);

                Intent intent = new Intent(getApplicationContext(), VisitorLoginActivity.class);
                startActivity(intent);
            }
        });
        if (model.getItems().getCheckINStatus() == 1){
            if (model.getIncomplete_data().getPic() != null && model.getIncomplete_data().getPic().size() != 0) {
                //preferences
                String Comp_id = Preferences.loadStringValue(getApplicationContext(),Preferences.Comp_id,"");
                Glide.with(VisitorEntryPassActivity.this).load(DataManger.IMAGE_URL + "/uploads/" + Comp_id + "/" + model.getIncomplete_data().getPic().get(model.getIncomplete_data().getPic().size() - 1))
                        .into(p_image);
            }
        }else {
            badge.setText(badge_b);

            String Comp_id = Preferences.loadStringValue(getApplicationContext(),Preferences.Comp_id,"");
            Glide.with(VisitorEntryPassActivity.this).load(DataManger.IMAGE_URL + "/uploads/" + Comp_id + "/" +filename)
                    .into(p_image);
        }

        RelativeLayout scrollview = findViewById(R.id.scrollview);
        scrollview.setOnClickListener(view -> {
            AnimationSet animation = Conversions.animation();
            view.startAnimation(animation);

            Intent intent12 = new Intent(getApplicationContext(), VisitorLoginActivity.class);
            startActivity(intent12);
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(getApplicationContext(), VisitorLoginActivity.class);
        startActivity(intent);
    }
}