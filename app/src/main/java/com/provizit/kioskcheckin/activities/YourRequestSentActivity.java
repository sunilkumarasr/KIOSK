package com.provizit.kioskcheckin.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AnimationSet;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.provizit.kioskcheckin.api.DataManger;
import com.provizit.kioskcheckin.logins.VisitorLoginActivity;
import com.provizit.kioskcheckin.R;
import com.provizit.kioskcheckin.services.Conversions;
import com.provizit.kioskcheckin.services.GetCVisitorDetailsModel;
import com.provizit.kioskcheckin.config.Preferences;
import com.provizit.kioskcheckin.utilities.TotalCounts;

import java.io.IOException;

public class YourRequestSentActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView company_logo, back_image, visitor_img, purpose_icon, org_icon, desg_icon;
    TextView your_request, txt_please, text_hostaccept, visitor_name, organization, designation, collect_entrypass, decline, purpose_visit;
    GetCVisitorDetailsModel model;
    Button btn_ok;
    String accept_Data = "";
    Handler handler = new Handler();
    Uri ImageUri;
    String uri = "", hid = "", hiid = "", mid = "", host = "", hierarchyname = "";
    String filename = "";
    String encodedString = "";
    String Comp_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_request_sent);

        Intent iin = getIntent();
        Bundle b = iin.getExtras();
        try {
            if (b != null) {
                host = b.getString("host");
                hid = b.getString("hid");
                hiid = b.getString("hiid");
                hierarchyname = b.getString("hierarchyname");
                uri = b.getString("uri");
                filename = b.getString("filename");
                encodedString = b.getString("encodedString");
                model = (GetCVisitorDetailsModel) iin.getSerializableExtra("model_key");

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String VersionName = sharedPreferences.getString("VersionName", "1.0");
        TextView txtVersion = findViewById(R.id.txtVersion);
        txtVersion.setText(VersionName);


        company_logo = findViewById(R.id.company_logo);
        your_request = findViewById(R.id.your_request);
        txt_please = findViewById(R.id.txt_please);
        text_hostaccept = findViewById(R.id.text_hostaccept);
        btn_ok = findViewById(R.id.btn_ok);
        btn_ok.setTypeface(ResourcesCompat.getFont(this, R.font.arbfonts_dinnextttlrabic_medium));
        back_image = findViewById(R.id.back_image);
        visitor_name = findViewById(R.id.visitor_name);
        organization = findViewById(R.id.organization);
        designation = findViewById(R.id.designation);
        visitor_img = findViewById(R.id.visitor_img);
        collect_entrypass = findViewById(R.id.collect_entrypass);
        decline = findViewById(R.id.decline);
        purpose_visit = findViewById(R.id.purpose_visit);
        purpose_icon = findViewById(R.id.purpose_icon);
        desg_icon = findViewById(R.id.desg_icon);
        org_icon = findViewById(R.id.org_icon);

        //shared Preferences
        Comp_id = Preferences.loadStringValue(getApplicationContext(), Preferences.Comp_id, "");
        //shared Preferences
        accept_Data = Preferences.loadStringValue(getApplicationContext(), Preferences.accept_Data, "");

        //company logo
        String c_Logo = Preferences.loadStringValue(getApplicationContext(), Preferences.company_Logo, "");
        if (c_Logo.equalsIgnoreCase("")) {
        } else {
            Glide.with(YourRequestSentActivity.this).load(c_Logo)
                    .into(company_logo);
        }

        if (uri != null) {
            ImageUri = Uri.parse(uri);
            image_set();
        } else if (model.getIncomplete_data().getPic() != null && model.getIncomplete_data().getPic().size() != 0) {

            Glide.with(YourRequestSentActivity.this).load(DataManger.IMAGE_URL + "/uploads/" + Comp_id + "/" + model.getIncomplete_data().getPic().get(model.getIncomplete_data().getPic().size() - 1)).into(visitor_img);

        } else {
            visitor_img.setImageResource(R.drawable.ic_user_white);
        }

        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                // Your code here
                Intent intents = new Intent(getApplicationContext(), VisitorLoginActivity.class);
                startActivity(intents);
            }
        };
        handler.postDelayed(myRunnable, 10000);

        Float visitor_status = model.getItems().getVisitorStatus();
        Float checkin_status = model.getItems().getCheckINStatus();
        Float meeting_status = model.getItems().getMeetingStatus();

        if (model != null) {
            visitor_name.setText(model.getIncomplete_data().getName());
            organization.setText(model.getIncomplete_data().getCompany());
            designation.setText(model.getIncomplete_data().getDesignation());

            if (model.getIncomplete_data().getCompany() == null || model.getIncomplete_data().getCompany().isEmpty()) {
                org_icon.setVisibility(View.GONE);
                organization.setVisibility(View.GONE);
            }
            if (model.getIncomplete_data().getDesignation() == null || model.getIncomplete_data().getDesignation().isEmpty()) {
                desg_icon.setVisibility(View.GONE);
                designation.setVisibility(View.GONE);
            }
        }


        if (meeting_status != 1) {
            purpose_icon.setVisibility(View.VISIBLE);
            purpose_visit.setVisibility(View.VISIBLE);
            purpose_visit.setText(model.getItems().getSubject());
        }


        if (checkin_status == 1) {
            Float h_status = model.getTotal_counts().getHstatus();
            long c_status = model.getTotal_counts().getCheckin();
            if (h_status == 1 && c_status == 0) {
                collect_entrypass.setVisibility(View.VISIBLE);
                txt_please.setVisibility(View.GONE);
                your_request.setVisibility(View.GONE);
                purpose_icon.setVisibility(View.GONE);
            } else if (h_status == 2 && c_status == 0) {
                decline.setVisibility(View.VISIBLE);
                txt_please.setVisibility(View.GONE);
                your_request.setVisibility(View.GONE);
                purpose_icon.setVisibility(View.GONE);
            } else {
                purpose_icon.setVisibility(View.GONE);
            }
        }

        if (accept_Data.equalsIgnoreCase("false")) {
            your_request.setVisibility(View.GONE);
        }

        TotalCounts totalCounts = model.getTotal_counts();
        if (totalCounts != null && totalCounts.getHstatus() != null) {
            if (model.getTotal_counts().getHstatus().toString().equalsIgnoreCase("0.0") && accept_Data.equalsIgnoreCase("true")) {
                text_hostaccept.setVisibility(View.VISIBLE);
                txt_please.setVisibility(View.GONE);
            } else if (model.getItems().getMeetingStatus() == 1) {
                your_request.setVisibility(View.GONE);
                purpose_icon.setVisibility(View.GONE);
            }
        } else {
            your_request.setVisibility(View.GONE);
            purpose_icon.setVisibility(View.GONE);
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

    // All mobiles
    private void image_set() {
        if (uri != null) {
            try {
                Bitmap bmp = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), Uri.parse(uri));
                int w = bmp.getWidth();
                int h = bmp.getHeight();
                Matrix mtx = new Matrix();
                mtx.postRotate(90);
                Bitmap rotatedBMP = Bitmap.createBitmap(bmp, 0, 0, w, h, mtx, true);
                BitmapDrawable bmd = new BitmapDrawable(rotatedBMP);
                visitor_img.setImageDrawable(bmd);
            } catch (IOException e) {
                e.printStackTrace();
                // Handle error loading image
            }
        } else {
            // Handle null URI
            // For example, set a default image
            visitor_img.setImageResource(R.drawable.ic_user_white);
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), VisitorLoginActivity.class);
        startActivity(intent);
    }


}