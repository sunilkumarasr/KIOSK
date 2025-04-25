package com.provizit.kioskcheckin.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AnimationSet;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.provizit.kioskcheckin.activities.Meetings.MeetingRequestActivity;
import com.provizit.kioskcheckin.activities.Meetings.MeetingDetailsActivity;
import com.provizit.kioskcheckin.config.InterNetConnectivityCheck;
import com.provizit.kioskcheckin.logins.VisitorLoginActivity;
import com.provizit.kioskcheckin.mvvm.ApiViewModel;
import com.provizit.kioskcheckin.R;
import com.provizit.kioskcheckin.services.Conversions;
import com.provizit.kioskcheckin.api.DataManger;
import com.provizit.kioskcheckin.services.GetCVisitorDetailsModel;
import com.provizit.kioskcheckin.services.VisitorformDetailsModel;
import com.provizit.kioskcheckin.config.Preferences;
import com.provizit.kioskcheckin.utilities.Getdocuments;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConfirmationActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView company_logo, back_image;

    LinearLayout linaerEmail,linearDesignation,linearOrganization;
    LinearLayout linear_nationality, linaer_id_number,linear_idtype;
    TextView name, designation, email, mobile, organization, address,Id_type, nationality, Idnumber;
    GetCVisitorDetailsModel model;
    Button btn_confirm;
    ApiViewModel apiViewModel;


    VisitorformDetailsModel visiter_model;
    // Define a constant for "model_key"
    private static final String MODEL_KEY = "model_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

        linaerEmail = findViewById(R.id.linaerEmail);
        linearDesignation = findViewById(R.id.linearDesignation);
        linearOrganization = findViewById(R.id.linearOrganization);
        btn_confirm = findViewById(R.id.btn_confirm);
        linear_nationality = findViewById(R.id.linear_nationality);
        linaer_id_number = findViewById(R.id.linaer_id_number);
        linear_idtype = findViewById(R.id.linear_idtype);
        name = findViewById(R.id.name);
        designation = findViewById(R.id.designation);
        email = findViewById(R.id.email);
        mobile = findViewById(R.id.mobile);
        ViewCompat.setLayoutDirection(mobile, ViewCompat.LAYOUT_DIRECTION_LTR);
        organization = findViewById(R.id.organization);
        address = findViewById(R.id.address);
        Id_type = findViewById(R.id.Id_type);
        nationality = findViewById(R.id.nationality);
        Idnumber = findViewById(R.id.Idnumber);
        ViewCompat.setLayoutDirection(Idnumber, ViewCompat.LAYOUT_DIRECTION_LTR);
        company_logo = findViewById(R.id.company_logo);
        back_image = findViewById(R.id.back_image);
        visiter_model = new VisitorformDetailsModel();

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String VersionName = sharedPreferences.getString("VersionName", "1.0");
        TextView txtVersion = findViewById(R.id.txtVersion);
        txtVersion.setText(VersionName);

        visitorformdetails();

        Intent intent = getIntent();
        model = (GetCVisitorDetailsModel) intent.getSerializableExtra(MODEL_KEY);

        apiViewModel = new ViewModelProvider(ConfirmationActivity.this).get(ApiViewModel.class);

        apiViewModel.getdocuments(getApplicationContext());

        apiViewModel.getResponseforSelectedId_list().observe(this, d_model -> {
            ArrayList<Getdocuments> documents_list = new ArrayList<>();
            documents_list = d_model.getItems();
            int idtypeVal = 1;

            if(model.getIncomplete_data().getDocument()!=null){
               idtypeVal =  (int) model.getIncomplete_data().getDocument().floatValue();
            }
            if(documents_list.size()>0){
                Id_type.setText(documents_list.get(idtypeVal-1).getName());
            }else {
                linear_idtype.setVisibility(View.GONE);
            }

        });

        //company logo
        String c_Logo = Preferences.loadStringValue(getApplicationContext(), Preferences.company_Logo, "");
        if (c_Logo.equalsIgnoreCase("")) {
        } else {
            Glide.with(ConfirmationActivity.this).load(c_Logo)
                    .into(company_logo);
        }


        name.setText(model.getIncomplete_data().getName());
        mobile.setText(model.getIncomplete_data().getMobile());
        address.setText(model.getIncomplete_data().getCaddress());
        email.setText(model.getIncomplete_data().getEmail());


        btn_confirm.setOnClickListener(this);
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
        back_image.setFocusable(false);
        back_image.setFocusableInTouchMode(false);
        btn_confirm.setFocusable(false);
        btn_confirm.setFocusableInTouchMode(false);
        runthred();
    }

    private void runthred() {
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(() -> {

            finish();

            // Use ActivityOptionsCompat to set transitions
            Intent intent = getIntent();
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this);
            startActivity(intent, options.toBundle());

            back_image.setFocusable(true);
            back_image.setFocusableInTouchMode(true);
            btn_confirm.setFocusable(true);
            btn_confirm.setFocusableInTouchMode(true);

        }, 500);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_confirm:
                AnimationSet animation1 = Conversions.animation();
                v.startAnimation(animation1);
                if (InterNetConnectivityCheck.isOnline(getApplicationContext())) {
                    Float meeting_status = model.getItems().getMeetingStatus();
                    Float checkin_status = model.getItems().getCheckINStatus();
                    if (checkin_status == 1) {
                        Float h_status = model.getTotal_counts().getHstatus();
                        if (h_status == 0) {
                            Intent intent = new Intent(getApplicationContext(), YourRequestSentActivity.class);
                            intent.putExtra(MODEL_KEY, model);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(getApplicationContext(), AlreadyCheckedInActivity.class);
                            intent.putExtra(MODEL_KEY, model);
                            startActivity(intent);
                        }
                    } else if (meeting_status == 1) {
                        Intent intent = new Intent(getApplicationContext(), MeetingDetailsActivity.class);
                        intent.putExtra(MODEL_KEY, model);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(getApplicationContext(), MeetingRequestActivity.class);
                        intent.putExtra(MODEL_KEY, model);
                        startActivity(intent);
                    }
                } else {
                    DataManger.internetpopup(ConfirmationActivity.this);

                }
                break;
            case R.id.back_image:
                animation1 = Conversions.animation();
                v.startAnimation(animation1);
                Intent intent = new Intent(getApplicationContext(), VisitorLoginActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }


    private void visitorformdetails() {
        DataManger dataManger = DataManger.getDataManager();
        dataManger.visitorformdetails(new Callback<VisitorformDetailsModel>() {
            @Override
            public void onResponse(Call<VisitorformDetailsModel> call, Response<VisitorformDetailsModel> response) {
                visiter_model = response.body();
                try {
                    if (visiter_model.getItem() != null) {
                        for (int i = 0; i < visiter_model.getItem().getOther().size(); i++) {
                            if (visiter_model.getItem().getOther().get(i).getStatus() && visiter_model.getItem().getOther().get(i).getModel().equalsIgnoreCase("nation")) {
                                //search
                                linear_nationality.setVisibility(View.VISIBLE);
                                nationality.setText(model.getIncomplete_data().getNation());
                                //id n
                                linaer_id_number.setVisibility(View.VISIBLE);
                                Idnumber.setText(model.getIncomplete_data().getIdnumber());
                                //type
                                linear_idtype.setVisibility(View.VISIBLE);
                            }
                            if (visiter_model.getItem().getOther().get(i).getStatus() && visiter_model.getItem().getOther().get(i).getModel().equalsIgnoreCase("designation")) {
                                linearDesignation.setVisibility(View.VISIBLE);
                                designation.setText(model.getIncomplete_data().getDesignation());
                            }
                            if (visiter_model.getItem().getOther().get(i).getStatus() && visiter_model.getItem().getOther().get(i).getModel().equalsIgnoreCase("company")) {
                                linearOrganization.setVisibility(View.VISIBLE);
                                organization.setText(model.getIncomplete_data().getCompany());
                            }
                        }

                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(Call<VisitorformDetailsModel> call, Throwable t) {
                throw new UnsupportedOperationException("Failure handling is not supported here.");
            }

        }, ConfirmationActivity.this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), VisitorLoginActivity.class);
        startActivity(intent);
    }

}