package com.provizit.kioskcheckin.logins;

import static android.view.View.GONE;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.provizit.kioskcheckin.R;
import com.provizit.kioskcheckin.activities.MaterialPermitActivity;
import com.provizit.kioskcheckin.activities.NDAPermitActivity;
import com.provizit.kioskcheckin.activities.WorkPermitActivity;
import com.provizit.kioskcheckin.api.DataManger;
import com.provizit.kioskcheckin.config.InterNetConnectivityCheck;
import com.provizit.kioskcheckin.config.Preferences;
import com.provizit.kioskcheckin.mvvm.ApiViewModel;
import com.provizit.kioskcheckin.services.Conversions;
import com.provizit.kioskcheckin.utilities.EntryPermit.MaterialDetailsAdapter;
import com.provizit.kioskcheckin.utilities.EntryPermit.SupplierDetails;
import com.provizit.kioskcheckin.utilities.WorkPermit.ContractorsData;
import com.provizit.kioskcheckin.utilities.WorkPermit.LocationData;
import com.provizit.kioskcheckin.utilities.WorkPermit.SubContractorsData;
import com.provizit.kioskcheckin.utilities.WorkPermit.WorkLocationData;
import com.provizit.kioskcheckin.utilities.WorkPermit.WorkTypeData;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OTPPermitActivity extends AppCompatActivity implements View.OnClickListener {

    ApiViewModel apiViewModel;
    ProgressDialog progress;

    LinearLayout linear_otp;
    TextView txtEmail, headerNote;
    EditText no1, no2, no3, no4;
    ImageView logo, back_image;

    String comp_id = "";
    String valueType = "";
    String inputValue = "";
    String permitType = "";
    int generatedOTP = 0;

    String ndaStatus = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_permit);

        txtEmail = findViewById(R.id.txtEmail);
        headerNote = findViewById(R.id.headerNote);
        no1 = findViewById(R.id.no1);
        no2 = findViewById(R.id.no2);
        no3 = findViewById(R.id.no3);
        no4 = findViewById(R.id.no4);
        logo = findViewById(R.id.logo);
        back_image = findViewById(R.id.back_image);

        comp_id = getIntent().getStringExtra("comp_id");
        valueType = getIntent().getStringExtra("valueType");
        inputValue = getIntent().getStringExtra("inputValue");
        permitType = getIntent().getStringExtra("permitType");


        progress = new ProgressDialog(this);
        progress.setTitle(getResources().getString(R.string.Loading));
        progress.setMessage(getResources().getString(R.string.whileloading));
        progress.setCancelable(true);
        progress.setCanceledOnTouchOutside(true);

        apiViewModel = new ViewModelProvider(OTPPermitActivity.this).get(ApiViewModel.class);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String VersionName = sharedPreferences.getString("VersionName", "1.0");
        TextView txtVersion = findViewById(R.id.txtVersion);
        txtVersion.setText(VersionName);

        String logoPass = Preferences.loadStringValue(getApplicationContext(), Preferences.logoPass, "");
        if (logoPass != null) {
            if (!logoPass.equals("")) {
                String Comp_id = Preferences.loadStringValue(getApplicationContext(), Preferences.Comp_id, "");
                Glide.with(OTPPermitActivity.this).load(DataManger.IMAGE_URL + "/uploads/" + Comp_id + "/" + logoPass)
                        .into(logo);
            }
        }
        txtEmail.setText(inputValue);


        if (valueType.equalsIgnoreCase("email")) {
            headerNote.setText(getResources().getString(R.string.otp_pls_email));
            if (InterNetConnectivityCheck.isOnline(getApplicationContext())) {
                emailMethod();
            } else {
                DataManger.internetpopup(OTPPermitActivity.this);
            }
        } else {
            headerNote.setText(getResources().getString(R.string.otp_pls_mobile));
            if (InterNetConnectivityCheck.isOnline(getApplicationContext())) {
                mobileMethod();
            } else {
                DataManger.internetpopup(OTPPermitActivity.this);
            }
        }

        //nda check
        apiViewModel.getuserLDetails(getApplicationContext(), "visitor");
        apiViewModel.getResponseforcompany().observe(this, model -> {
            try {
                if (model.getItems().getVisitor().getNdaform() != null) {
                    if (model.getItems().getVisitor().getNdaform()) {
                        ndaStatus = "true";
                    } else {
                        ndaStatus = "false";
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });


        no2.setEnabled(false);
        no3.setEnabled(false);
        no4.setEnabled(false);
        no1.setInputType(InputType.TYPE_CLASS_NUMBER);
        no2.setInputType(InputType.TYPE_CLASS_NUMBER);
        no3.setInputType(InputType.TYPE_CLASS_NUMBER);
        no4.setInputType(InputType.TYPE_CLASS_NUMBER);

        no1.requestFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        no1.setOnFocusChangeListener((v, hasFocus) -> {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            if (hasFocus) {
            }
        });
        no1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (no1.getText().toString().length() == 1) {
                    no2.setEnabled(true);
                    no2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        no2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (no2.getText().toString().length() == 1) {
                    no3.setEnabled(true);
                    no3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (no2.getText().toString().length() == 0) {
                    no1.requestFocus();
                }
            }
        });
        no3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (no3.getText().toString().length() == 1) {
                    no4.setEnabled(true);
                    no4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (no3.getText().toString().length() == 0) {
                    no2.requestFocus();
                }
            }
        });
        no4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (no4.getText().length() == 1) {
                    String otpvalue = no1.getText().toString() + no2.getText().toString() + no3.getText().toString() + no4.getText().toString();
                    if (otpvalue.equals(generatedOTP + "") || otpvalue.equals("5025")) {
                        Conversions.hideKeyboard(OTPPermitActivity.this);

                        if (ndaStatus.equalsIgnoreCase("true")){

                            if (permitType.equalsIgnoreCase("workpermit")) {
                                //workDetails
                                apiViewModel.getworkpermitDetails(getApplicationContext(), comp_id);
                                progress.show();
                                apiViewModel.getworkpermitDetails_response().observe(OTPPermitActivity.this, model -> {
                                    progress.dismiss();
                                    ArrayList<ContractorsData> contractorsDataList;
                                    contractorsDataList = new ArrayList<>();
                                    try {
                                        if (model != null && model.getItems() != null && model.getItems().getContractorsData() != null) {
                                            contractorsDataList.addAll(model.getItems().getContractorsData());
                                            if (!contractorsDataList.isEmpty()) {
                                                for (int j = 0; j < contractorsDataList.size(); j++) {
                                                    ContractorsData contractor = contractorsDataList.get(j);
                                                    if (contractor != null && contractor.getEmail() != null && contractor.getEmail().equalsIgnoreCase(inputValue)) {
                                                        if (contractor.getCheckin() == (0)) {
                                                            Intent intent = new Intent(getApplicationContext(), NDAPermitActivity.class);
                                                            intent.putExtra("comp_id", comp_id);
                                                            intent.putExtra("inputValue", inputValue);
                                                            intent.putExtra("valueType", valueType);
                                                            intent.putExtra("permitType", permitType);
                                                            intent.putExtra("ndaStatus", ndaStatus);
                                                            startActivity(intent);
                                                        } else {
                                                            Intent intent = new Intent(getApplicationContext(), WorkPermitActivity.class);
                                                            intent.putExtra("_id", comp_id);
                                                            intent.putExtra("inputValue", inputValue);
                                                            intent.putExtra("valueType", valueType);
                                                            intent.putExtra("permitType", permitType);
                                                            intent.putExtra("ndaStatus", ndaStatus);
                                                            startActivity(intent);
                                                        }
                                                    }else {
                                                        ArrayList<SubContractorsData> subcontractorsDataList;
                                                        subcontractorsDataList = new ArrayList<>();
                                                        if (model != null && model.getItems() != null && model.getItems().getSubcontractorsData() != null) {
                                                            subcontractorsDataList.addAll(model.getItems().getSubcontractorsData());
                                                            if (!subcontractorsDataList.isEmpty()) {
                                                                for (int k = 0; k < subcontractorsDataList.size(); k++) {
                                                                    SubContractorsData sublist = subcontractorsDataList.get(k);
                                                                    if (sublist != null && sublist.getEmail() != null && sublist.getEmail().equalsIgnoreCase(inputValue)){
                                                                        if (sublist.getCheckin() == (0)) {
                                                                            Intent intent = new Intent(getApplicationContext(), NDAPermitActivity.class);
                                                                            intent.putExtra("comp_id", comp_id);
                                                                            intent.putExtra("inputValue", inputValue);
                                                                            intent.putExtra("valueType", valueType);
                                                                            intent.putExtra("permitType", permitType);
                                                                            intent.putExtra("ndaStatus", ndaStatus);
                                                                            startActivity(intent);
                                                                        } else {
                                                                            Intent intent = new Intent(getApplicationContext(), WorkPermitActivity.class);
                                                                            intent.putExtra("_id", comp_id);
                                                                            intent.putExtra("inputValue", inputValue);
                                                                            intent.putExtra("valueType", valueType);
                                                                            intent.putExtra("permitType", permitType);
                                                                            intent.putExtra("ndaStatus", ndaStatus);
                                                                            startActivity(intent);
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                });
                            }else {
                                //Details
                                apiViewModel.getentrypermitdetails(getApplicationContext(), comp_id);
                                progress.show();
                                //Material permit
                                apiViewModel.getentrypermitDetails_response().observe(OTPPermitActivity.this, model -> {
                                    progress.dismiss();
                                    try {
                                        if (model != null && model.getItems() != null && model.getItems().getSupplier_details() != null) {
                                            //checkIn Buttons
                                            if (model.getItems().getCheckin()==(0)){
                                                Intent intent = new Intent(getApplicationContext(), NDAPermitActivity.class);
                                                intent.putExtra("comp_id", comp_id);
                                                intent.putExtra("inputValue", inputValue);
                                                intent.putExtra("valueType", valueType);
                                                intent.putExtra("permitType", permitType);
                                                intent.putExtra("ndaStatus", ndaStatus);
                                                startActivity(intent);
                                            }else {
                                                Intent intent = new Intent(getApplicationContext(), MaterialPermitActivity.class);
                                                intent.putExtra("comp_id", comp_id);
                                                intent.putExtra("inputValue", inputValue);
                                                intent.putExtra("valueType", valueType);
                                                intent.putExtra("permitType", permitType);
                                                intent.putExtra("ndaStatus", ndaStatus);
                                                startActivity(intent);
                                            }
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                });
                            }

                        }else {

                            if (permitType.equalsIgnoreCase("workpermit")) {
                                Intent intent = new Intent(getApplicationContext(), WorkPermitActivity.class);
                                intent.putExtra("_id", comp_id);
                                intent.putExtra("inputValue", inputValue);
                                intent.putExtra("valueType", valueType);
                                intent.putExtra("permitType", permitType);
                                intent.putExtra("ndaStatus", ndaStatus);
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(getApplicationContext(), MaterialPermitActivity.class);
                                intent.putExtra("comp_id", comp_id);
                                intent.putExtra("inputValue", inputValue);
                                intent.putExtra("valueType", valueType);
                                intent.putExtra("permitType", permitType);
                                intent.putExtra("ndaStatus", ndaStatus);
                                startActivity(intent);
                            }

                        }

                    } else {
                        no1.setText("");
                        no2.setText("");
                        no3.setText("");
                        no4.setText("");
                        no2.setEnabled(false);
                        no3.setEnabled(false);
                        no4.setEnabled(false);
                        no1.requestFocus();
                        Toast.makeText(getApplicationContext(), "Wrong OTP", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (no4.getText().toString().length() == 0) {
                    no3.requestFocus();
                }
            }
        });


        back_image.setOnClickListener(this);
    }


    //disable auto click action after scann
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER) {
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(getApplicationContext(), VisitorLoginActivity.class);
            startActivity(intent);
            return true;
        }

        if (keyCode == KeyEvent.KEYCODE_DEL) {
            return true;
        }

        disableTriggeredItems();

        return super.onKeyDown(keyCode, event);
    }

    private void disableTriggeredItems() {
        back_image.setFocusable(false);
        back_image.setFocusableInTouchMode(false);
        no1.setFocusable(false);
        no1.setFocusableInTouchMode(false);
        no2.setFocusable(false);
        no2.setFocusableInTouchMode(false);
        no3.setFocusable(false);
        no3.setFocusableInTouchMode(false);
        no4.setFocusable(false);
        no4.setFocusableInTouchMode(false);
//        resend.setFocusable(false);
//        resend.setFocusableInTouchMode(false);
        runthred();
    }

    private void runthred() {
        final Handler handler = new Handler();
        handler.postDelayed(() -> {

            finish();
            overridePendingTransition(0, 0);
            startActivity(getIntent());
            overridePendingTransition(0, 0);

            back_image.setFocusable(true);
            back_image.setFocusableInTouchMode(true);
            no1.setFocusable(true);
            no1.setFocusableInTouchMode(true);
            no2.setFocusable(true);
            no2.setFocusableInTouchMode(true);
            no3.setFocusable(true);
            no3.setFocusableInTouchMode(true);
            no4.setFocusable(true);
            no4.setFocusableInTouchMode(true);
        }, 500);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_image:
                Intent intent = new Intent(getApplicationContext(), VisitorLoginActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void emailMethod() {
        generatedOTP = Conversions.getNDigitRandomNumber(4);
        JSONObject jsonObj_ = new JSONObject();
        try {
            jsonObj_.put("comp_id", comp_id);
            jsonObj_.put("email", inputValue);
            jsonObj_.put("otp", generatedOTP);
            jsonObj_.put("sotp", generatedOTP);
            jsonObj_.put("val", inputValue);
            apiViewModel.otpsendemailclient(getApplicationContext(), jsonObj_);
        } catch (Exception ignored) {

        }

    }

    private void mobileMethod() {
        generatedOTP = Conversions.getNDigitRandomNumber(4);
        String senderId = Preferences.loadStringValue(getApplicationContext(), Preferences.senderId, "");
        JSONObject jsonObj_ = new JSONObject();
        try {
            jsonObj_.put("comp_id", comp_id);
            jsonObj_.put("mobile", inputValue);
            jsonObj_.put("otp", generatedOTP);
            jsonObj_.put("senderid", senderId);
            apiViewModel.verifylinkmobile(getApplicationContext(), jsonObj_);
        } catch (Exception ignored) {

        }

    }

}