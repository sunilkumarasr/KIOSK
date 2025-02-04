package com.provizit.kioskcheckin.activities;

import static android.view.View.GONE;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.provizit.kioskcheckin.R;
import com.provizit.kioskcheckin.config.Preferences;
import com.provizit.kioskcheckin.logins.VisitorLoginActivity;
import com.provizit.kioskcheckin.mvvm.ApiViewModel;
import com.provizit.kioskcheckin.services.Conversions;
import com.provizit.kioskcheckin.utilities.WorkPermit.ContractorsData;
import com.provizit.kioskcheckin.utilities.WorkPermit.LocationData;
import com.provizit.kioskcheckin.utilities.WorkPermit.WorkLocationData;
import com.provizit.kioskcheckin.utilities.WorkPermit.WorkTypeData;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class WorkPermitActivity extends AppCompatActivity implements View.OnClickListener {

    ApiViewModel apiViewModel;
    ProgressDialog progress;

    LinearLayout LinearDetails,line1,linearWarning;
    ImageView back_image, logo, imgContractor;
    TextView txtCName, txtCompany, txtWorkName, txtTime, txtDate, txtLocation;
    Button btnCancel, btnNext, btnOk;

    ArrayList<String> StartsList;
    ArrayList<String> EndList;
    ArrayList<ContractorsData> contractorsDataList;
    String comp_id = "";
    String inputValue = "";
    String valueType = "";
    String permitType = "";
    String id = "";
    String ndaStatus = "";

    long currentMillis;
    String statusCheckIn = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_permit);

        LinearDetails = findViewById(R.id.LinearDetails);
        line1 = findViewById(R.id.line1);
        linearWarning = findViewById(R.id.linearWarning);
        back_image = findViewById(R.id.back_image);
        logo = findViewById(R.id.logo);
        imgContractor = findViewById(R.id.imgContractor);
        txtCName = findViewById(R.id.txtCName);
        txtCompany = findViewById(R.id.txtCompany);
        txtWorkName = findViewById(R.id.txtWorkName);
        txtTime = findViewById(R.id.txtTime);
        txtDate = findViewById(R.id.txtDate);
        txtLocation = findViewById(R.id.txtLocation);
        btnCancel = findViewById(R.id.btnCancel);
        btnNext = findViewById(R.id.btnNext);
        btnOk = findViewById(R.id.btnOk);

        comp_id = getIntent().getStringExtra("comp_id");
        inputValue = getIntent().getStringExtra("inputValue");
        valueType = getIntent().getStringExtra("valueType");
        permitType = getIntent().getStringExtra("permitType");
        ndaStatus = getIntent().getStringExtra("ndaStatus");

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String VersionName = sharedPreferences.getString("VersionName", "1.0");
        TextView txtVersion = findViewById(R.id.txtVersion);
        txtVersion.setText(VersionName);
        //company logo
        String c_Logo = Preferences.loadStringValue(getApplicationContext(), Preferences.company_Logo, "");
        if (c_Logo.equalsIgnoreCase("")) {
        } else {
            Glide.with(WorkPermitActivity.this).load(c_Logo)
                    .into(logo);
        }

        Calendar calendar = Calendar.getInstance();
        long currentTimeMillis = calendar.getTimeInMillis();
        TimeZone timeZone = calendar.getTimeZone();
        int offsetMillis = timeZone.getOffset(calendar.getTimeInMillis());
        currentMillis = currentTimeMillis - offsetMillis;

        progress = new ProgressDialog(this);
        progress.setTitle(getResources().getString(R.string.Loading));
        progress.setMessage(getResources().getString(R.string.whileloading));
        progress.setCancelable(true);
        progress.setCanceledOnTouchOutside(true);

        apiViewModel = new ViewModelProvider(WorkPermitActivity.this).get(ApiViewModel.class);


        //workDetails
        apiViewModel.getworkpermitDetails(getApplicationContext(), comp_id);
        progress.show();
        apiViewModel.getworkpermitDetails_response().observe(this, model -> {
            progress.dismiss();
            StartsList = new ArrayList<>();
            StartsList.clear();
            EndList = new ArrayList<>();
            EndList.clear();
            contractorsDataList = new ArrayList<>();
            try {
                if (model != null && model.getItems() != null && model.getItems().getContractorsData() != null) {

                    txtCompany.setText(model.getItems().getCompanyName());
                    id = model.getItems().get_id().get$oid();
                    //Start time
                    String s_time = "";
                    String e_time = "";
                    StartsList.addAll(model.getItems().getStarts());
                    if (!StartsList.isEmpty()) {
                        long startTimestamp = (long) (Double.parseDouble(StartsList.get(0)) + Conversions.timezone());
                        s_time = Conversions.millitotime(startTimestamp * 1000, false);
                    }
                    //End time
                    EndList.addAll(model.getItems().getEnds());

                    //date expair condition
                    Date date = new Date();  // Get current date and time
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String formattedDate = sdf.format(date);
                    try {
                        Date dates = sdf.parse(formattedDate);
                        long currentMillis = dates.getTime();
                        if (!EndList.isEmpty()) {
                            String endTimeStr = EndList.get(0);
                            double endStamp = Double.parseDouble(endTimeStr);
                            long endMillis = (long) (endStamp * 1000);
                            if (currentMillis == endMillis || currentMillis < endMillis) {
                                LinearDetails.setVisibility(View.VISIBLE);
                                line1.setVisibility(View.VISIBLE);
                                linearWarning.setVisibility(GONE);
                            }else {
                                LinearDetails.setVisibility(GONE);
                                line1.setVisibility(GONE);
                                linearWarning.setVisibility(View.VISIBLE);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    if (!EndList.isEmpty()) {
                        long startTimestamp = (long) (Double.parseDouble(EndList.get(0)) + Conversions.timezone());
                        e_time = Conversions.millitotime(startTimestamp * 1000, false);
                    }
                    String stateDate = Conversions.millitodateD((model.getItems().getStart() + Conversions.timezone()) * 1000);
                    String endDate = Conversions.millitodateD((model.getItems().getEnd() + Conversions.timezone()) * 1000);

                    txtTime.setText(s_time + " to " + e_time);
                    txtDate.setText(stateDate + " to " + endDate);



                    //name
                    WorkTypeData workTypeData = model.getItems().getWorktypeData();
                    if (workTypeData != null) {
                        txtWorkName.setText(workTypeData.getName());
                    }
                    //location
                    WorkLocationData workLocation = model.getItems().getWorklocationData();
                    LocationData locationData = model.getItems().getLocations_Data();
                    if (workLocation != null) {
                        txtLocation.setText(workLocation.getName() + "," + locationData.getName());
                    }


                    contractorsDataList.addAll(model.getItems().getContractorsData());
                    if (!contractorsDataList.isEmpty()) {
                        for (int i = 0; i < contractorsDataList.size(); i++) {
                            ContractorsData contractor = contractorsDataList.get(i);
                            if (contractor != null && contractor.getEmail() != null && contractor.getEmail().equalsIgnoreCase(inputValue)) {
                                txtCName.setText(contractor.getName());
                                if (contractor.getCheckin() == (0)) {
                                    btnNext.setText(getString(R.string.CheckIn));
                                    statusCheckIn = "Check-In";
                                } else if (contractor.getCheckin() == (1)) {
                                    btnNext.setText(getString(R.string.CheckOut));
                                    statusCheckIn = "Check-Out";
                                } else {
                                    btnNext.setVisibility(GONE);
                                }
                            }
                        }
                    }


                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        //updateCheckIn
        apiViewModel.updateworkpermitaDetails_response().observe(this, model -> {
            progress.dismiss();
            try {
                if (model != null) {
                    Integer statuscode = model.getResult();
                    if (statuscode.equals(200)) {
                        if (statusCheckIn.equalsIgnoreCase("Check-In")) {
                            Intent intents = new Intent(getApplicationContext(), ChekInPermitStatusActivity.class);
                            intents.putExtra("status", "1");
                            startActivity(intents);
                        } else {
                            Intent intents = new Intent(getApplicationContext(), ChekInPermitStatusActivity.class);
                            intents.putExtra("status", "2");
                            startActivity(intents);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Try again", Toast.LENGTH_LONG).show();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                progress.dismiss();
            }
        });

        back_image.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        btnOk.setOnClickListener(this);
    }

    //disable auto click action after scann
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER) {
            // Barcode scanner has scanned a barcode, disable triggered items
            return true;
        } else {
            disableTriggeredItems();
        }
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
                default:
                    char keyChar = (char) event.getUnicodeChar();
                    return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }

    private void disableTriggeredItems() {
        back_image.setFocusable(false);
        back_image.setFocusableInTouchMode(false);
        btnCancel.setFocusable(false);
        btnCancel.setFocusableInTouchMode(false);
        btnNext.setFocusable(false);
        btnNext.setFocusableInTouchMode(false);
        btnOk.setFocusable(false);
        btnOk.setFocusableInTouchMode(false);
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

                back_image.setFocusable(true);
                back_image.setFocusableInTouchMode(true);
                btnCancel.setFocusable(true);
                btnCancel.setFocusableInTouchMode(true);
                btnNext.setFocusable(true);
                btnNext.setFocusableInTouchMode(true);
                btnOk.setFocusable(true);
                btnOk.setFocusableInTouchMode(true);
            }
        }, 500);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_image:
                Intent intent1 = new Intent(getApplicationContext(), VisitorLoginActivity.class);
                startActivity(intent1);
                break;
            case R.id.btnOk:
                Intent inten = new Intent(getApplicationContext(), VisitorLoginActivity.class);
                startActivity(inten);
                break;
            case R.id.btnCancel:
                Intent intent = new Intent(getApplicationContext(), VisitorLoginActivity.class);
                startActivity(intent);
                break;
            case R.id.btnNext:
                updateCheckIn();
                break;
        }
    }

    private void updateCheckIn() {

        String Emp_id = Preferences.loadStringValue(getApplicationContext(), Preferences.Emp_id, "");

        if (statusCheckIn.equalsIgnoreCase("Check-In")) {
            JsonObject gsonObject = new JsonObject();
            JSONObject jsonObj_ = new JSONObject();
            try {
                jsonObj_.put("formtype", "checkin");
                jsonObj_.put("email", inputValue);
                jsonObj_.put("id", id);
                jsonObj_.put("emp_id", Emp_id);
                JsonParser jsonParser = new JsonParser();
                gsonObject = (JsonObject) jsonParser.parse(jsonObj_.toString());
                System.out.println("gsonObject::" + gsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            apiViewModel.updateworkpermita(getApplicationContext(), gsonObject);
            progress.show();
        } else if (statusCheckIn.equalsIgnoreCase("Check-Out")) {
            JsonObject gsonObject = new JsonObject();
            JSONObject jsonObj_ = new JSONObject();
            try {
                jsonObj_.put("formtype", "checkout");
                jsonObj_.put("email", inputValue);
                jsonObj_.put("id", id);
                jsonObj_.put("emp_id", Emp_id);
                JsonParser jsonParser = new JsonParser();
                gsonObject = (JsonObject) jsonParser.parse(jsonObj_.toString());
                System.out.println("gsonObject::" + gsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            apiViewModel.updateworkpermita(getApplicationContext(), gsonObject);
            progress.show();
        } else {

        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent1 = new Intent(getApplicationContext(), VisitorLoginActivity.class);
        startActivity(intent1);
    }
}
