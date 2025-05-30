package com.provizit.kioskcheckin.activities;

import static android.view.View.GONE;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.provizit.kioskcheckin.R;
import com.provizit.kioskcheckin.activities.WarningScreens.LocationValidationMeetingActivity;
import com.provizit.kioskcheckin.config.Preferences;
import com.provizit.kioskcheckin.logins.VisitorLoginActivity;
import com.provizit.kioskcheckin.mvvm.ApiViewModel;
import com.provizit.kioskcheckin.services.Conversions;
import com.provizit.kioskcheckin.utilities.EntryPermit.MaterialDetails;
import com.provizit.kioskcheckin.utilities.EntryPermit.MaterialDetailsAdapter;
import com.provizit.kioskcheckin.utilities.EntryPermit.SupplierDetails;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class MaterialPermitActivity extends AppCompatActivity implements View.OnClickListener {

    ApiViewModel apiViewModel;
    ProgressDialog progress;

    ImageView back_image,logo;
    TextView txtEnter,txtName,txtCompany,txtVehicleType,txtVehicleNumber;
    RecyclerView recyclerview;
    Button btnCancel,btnNext,btnOk;

    LinearLayout LinearDetails,line1,linearWarning;

    String comp_id = "";
    String inputValue = "";
    String valueType = "";
    String permitType = "";
    String id = "";
    String ndaStatus = "";
    boolean checkInType = false;

    ArrayList<SupplierDetails> supplier_details;
    //adapter for vehicle
    ArrayList<MaterialDetails> material_details;
    ArrayList<MaterialDetails> material_details_final;
    MaterialDetailsAdapter materialDetailsAdapter;

    long currentMillis;

    String statusCheckIn = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_materialpermit);

        comp_id = getIntent().getStringExtra("comp_id");
        inputValue = getIntent().getStringExtra("inputValue");
        valueType = getIntent().getStringExtra("valueType");
        permitType = getIntent().getStringExtra("permitType");
        ndaStatus = getIntent().getStringExtra("ndaStatus");

        back_image = findViewById(R.id.back_image);
        logo = findViewById(R.id.logo);
        txtEnter = findViewById(R.id.txtEnter);
        txtName = findViewById(R.id.txtName);
        txtCompany = findViewById(R.id.txtCompany);
        txtVehicleType = findViewById(R.id.txtVehicleType);
        txtVehicleNumber = findViewById(R.id.txtVehicleNumber);
        recyclerview = findViewById(R.id.recyclerview);
        btnCancel = findViewById(R.id.btnCancel);
        btnNext = findViewById(R.id.btnNext);
        LinearDetails = findViewById(R.id.LinearDetails);
        line1 = findViewById(R.id.line1);
        linearWarning = findViewById(R.id.linearWarning);
        btnOk = findViewById(R.id.btnOk);


        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String VersionName = sharedPreferences.getString("VersionName", "1.0");
        TextView txtVersion = findViewById(R.id.txtVersion);
        txtVersion.setText(VersionName);
        //company logo
        String c_Logo = Preferences.loadStringValue(getApplicationContext(), Preferences.company_Logo, "");
        if (c_Logo.equalsIgnoreCase("")){
        }else {
            Glide.with(MaterialPermitActivity.this).load(c_Logo)
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

        apiViewModel = new ViewModelProvider(MaterialPermitActivity.this).get(ApiViewModel.class);

        //Details
        apiViewModel.getentrypermitdetails(getApplicationContext(), comp_id);
        progress.show();
        //Visitor_id_blocklist
        apiViewModel.getentrypermitDetails_response().observe(this, model -> {
            progress.dismiss();
            try {
                if (model != null && model.getItems() != null && model.getItems().getSupplier_details() != null) {

                    //date status check
                    Calendar ca = Calendar.getInstance();
                    ca.set(Calendar.HOUR_OF_DAY, 0);
                    ca.set(Calendar.MINUTE, 0);
                    ca.set(Calendar.SECOND, 0);
                    ca.set(Calendar.MILLISECOND, 0);
                    long todayStartTimestamp = ca.getTimeInMillis();
                    System.out.println("Today's Start Timestamp: " + todayStartTimestamp);

                    long startMillis = (model.getItems().getStart() + Conversions.timezone()) * 1000;
                    long endMillis = (model.getItems().getEnd() + Conversions.timezone()) * 1000;
                    System.out.println("startMillis: " + startMillis);
                    System.out.println("endMillis: " + endMillis);


                    if (todayStartTimestamp == startMillis || todayStartTimestamp > startMillis && todayStartTimestamp < endMillis ){
                        LinearDetails.setVisibility(View.VISIBLE);
                        line1.setVisibility(View.VISIBLE);
                        linearWarning.setVisibility(GONE);
                    }else {
                        LinearDetails.setVisibility(GONE);
                        line1.setVisibility(GONE);
                        linearWarning.setVisibility(View.VISIBLE);
                    }

                    id = model.getItems().get_id().get$oid();

                    if (model.getItems().getType().equalsIgnoreCase("1")){
                        txtEnter.setBackgroundColor(Color.parseColor("#00821d"));
                        if (model.getItems().getPurpose_return()){
                            checkInType = true;
                            txtEnter.setText("Entry - (Return)");
                        }else {
                            txtEnter.setText("Entry");
                        }
                    }else {
                        txtEnter.setBackgroundColor(Color.parseColor("#8B0000"));
                        if (model.getItems().getPurpose_return()){
                            checkInType = true;
                            txtEnter.setText("Exit - (Return)");
                        }else {
                            txtEnter.setText("Exit");
                        }
                    }

                    //checkIn Buttons
                    if (model.getItems().getCheckin()==(0)){
                        btnNext.setText(getString(R.string.CheckIn));
                        statusCheckIn = "Check-In";
                    }else if (model.getItems().getCheckout()==(0)){
                        if (model.getItems().getPurpose_return()){
                            btnNext.setText(getString(R.string.CheckOut));
                            statusCheckIn = "Check-Out";
                        }else {
                            btnNext.setVisibility(GONE);
                        }
                    }else {
                        btnNext.setVisibility(GONE);
                    }

                    txtCompany.setText(model.getItems().getSupplier_name());

                    supplier_details = new ArrayList<>();
                    supplier_details.addAll(model.getItems().getSupplier_details());
                    if (!supplier_details.isEmpty()) {
                        for (int i = 0; i < supplier_details.size(); i++) {
                            SupplierDetails supplierDetails = supplier_details.get(i);
                            if (supplierDetails != null && supplierDetails.getSupplier_email() != null && supplierDetails.getSupplier_email().equalsIgnoreCase(inputValue)) {
                                txtName.setText(supplierDetails.getContact_person());
                                txtVehicleType.setText(supplierDetails.getVehicle_type());
                                txtVehicleNumber.setText(supplierDetails.getVehicle_no());
                            }
                        }
                    }

                    material_details = new ArrayList<>();
                    material_details_final = new ArrayList<>();
                    material_details.addAll(model.getItems().getMaterial_details());
                    if (!material_details.isEmpty()) {
                        for (int i = 0; i < material_details.size(); i++) {
                            material_details_final.add(material_details.get(i));
                        }
                    }
                    materialDetailsAdapter = new MaterialDetailsAdapter(getApplicationContext(), material_details_final);
                    LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
                    recyclerview.setLayoutManager(manager);
                    recyclerview.setAdapter(materialDetailsAdapter);

                    //meeting cancel
                    Log.e("status_",model.getItems().getStatus());
                    if (model.getItems().getStatus().equalsIgnoreCase("2.0")){
                        txtEnter.setBackgroundColor(Color.parseColor("#8B0000"));
                        txtEnter.setText("The Material Permit has been Cancelled");
                        line1.setVisibility(GONE);
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        //update CheckIn
        apiViewModel.materialcheckin_response().observe(this, model -> {
            progress.dismiss();
            try {
                if (model != null) {
                    Integer statuscode = model.getResult();
                    if (statuscode.equals(200)) {
                        if (statusCheckIn.equalsIgnoreCase("Check-In")){
                            Intent intents = new Intent(getApplicationContext(), ChekInPermitStatusActivity.class);
                            intents.putExtra("status","1");
                            startActivity(intents);
                        }else {
                            Intent intents = new Intent(getApplicationContext(), ChekInPermitStatusActivity.class);
                            intents.putExtra("status","2");
                            startActivity(intents);
                        }
                    }else {
                        Toast.makeText(getApplicationContext(),"Try again",Toast.LENGTH_LONG).show();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
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
            case R.id.btnCancel:
                Intent intent = new Intent(getApplicationContext(), VisitorLoginActivity.class);
                startActivity(intent);
                break;
            case R.id.btnNext:
                updateCheckIn();
                break;
            case R.id.btnOk:
                Intent intent2 = new Intent(getApplicationContext(), VisitorLoginActivity.class);
                startActivity(intent2);
                break;
        }
    }

    private void updateCheckIn() {

        String Emp_id = Preferences.loadStringValue(getApplicationContext(), Preferences.Emp_id, "");

        if (statusCheckIn.equalsIgnoreCase("Check-In")){
            JsonObject gsonObject = new JsonObject();
            JSONObject jsonObj_ = new JSONObject();
            try {
                jsonObj_.put("formtype", "checkin");
                jsonObj_.put("id", id);
                jsonObj_.put("email", inputValue);
                jsonObj_.put("emp_id", Emp_id);
                JsonParser jsonParser = new JsonParser();
                gsonObject = (JsonObject) jsonParser.parse(jsonObj_.toString());
                System.out.println("gsonObject::" + gsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            apiViewModel.materialcheckin(getApplicationContext(), gsonObject);
            progress.show();
        }else if (statusCheckIn.equalsIgnoreCase("Check-Out")){
            JsonObject gsonObject = new JsonObject();
            JSONObject jsonObj_ = new JSONObject();
            try {
                jsonObj_.put("formtype", "checkout");
                jsonObj_.put("id", id);
                jsonObj_.put("email", inputValue);
                jsonObj_.put("emp_id", Emp_id);
                JsonParser jsonParser = new JsonParser();
                gsonObject = (JsonObject) jsonParser.parse(jsonObj_.toString());
                System.out.println("gsonObject::" + gsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            apiViewModel.materialcheckin(getApplicationContext(), gsonObject);
            progress.show();
        }else {

        }

    }

}
