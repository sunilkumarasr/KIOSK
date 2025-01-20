package com.provizit.kioskcheckin.activities;

import static android.view.View.GONE;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AnimationSet;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.provizit.kioskcheckin.R;
import com.provizit.kioskcheckin.api.DataManger;
import com.provizit.kioskcheckin.config.ConnectionReceiver;
import com.provizit.kioskcheckin.config.Preferences;
import com.provizit.kioskcheckin.config.ViewController;
import com.provizit.kioskcheckin.logins.AdminLoginActivity;
import com.provizit.kioskcheckin.logins.VisitorLoginActivity;
import com.provizit.kioskcheckin.mvvm.ApiViewModel;
import com.provizit.kioskcheckin.services.CompanyDetailsModel;
import com.provizit.kioskcheckin.services.Conversions;
import com.provizit.kioskcheckin.utilities.CompanyData;
import com.provizit.kioskcheckin.utilities.EntryPermit.MaterialDetails;
import com.provizit.kioskcheckin.utilities.EntryPermit.MaterialDetailsAdapter;
import com.provizit.kioskcheckin.utilities.EntryPermit.SupplierDetails;
import com.provizit.kioskcheckin.utilities.WorkPermit.ContractorsData;
import com.provizit.kioskcheckin.utilities.WorkPermit.WorkLocationData;
import com.provizit.kioskcheckin.utilities.WorkPermit.WorkPermitModel;
import com.provizit.kioskcheckin.utilities.WorkPermit.WorkTypeData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MaterialPermitActivity extends AppCompatActivity implements View.OnClickListener {
    
    ApiViewModel apiViewModel;
    ProgressDialog progress;

    ImageView back_image,logo;
    TextView txtEnter,txtName,txtCompany,txtVehicleType,txtVehicleNumber;
    RecyclerView recyclerview;
    Button btnCancel,btnNext;

    String comp_id = "";
    String inputValue = "";
    String id = "";
    boolean checkInType = false;

    ArrayList<SupplierDetails> supplier_details;
    //adapter for vehicle
    ArrayList<MaterialDetails> material_details;
    ArrayList<MaterialDetails> material_details_final;
    MaterialDetailsAdapter materialDetailsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_materialpermit);

        comp_id = getIntent().getStringExtra("comp_id");
        inputValue = getIntent().getStringExtra("inputValue");

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

        progress = new ProgressDialog(this);
        progress.setTitle(getResources().getString(R.string.Loading));
        progress.setMessage(getResources().getString(R.string.whileloading));
        progress.setCancelable(true);
        progress.setCanceledOnTouchOutside(true);

        apiViewModel = new ViewModelProvider(MaterialPermitActivity.this).get(ApiViewModel.class);

        //Details
        apiViewModel.getentrypermitdetails(getApplicationContext(), comp_id,"today");
        progress.show();
        //Visitor_id_blocklist
        apiViewModel.getentrypermitDetails_response().observe(this, model -> {
            progress.dismiss();
            try {
                if (model != null && model.getItems() != null && model.getItems().getSupplier_details() != null) {

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
                        btnNext.setText("CheckIn");
                    }else if (model.getItems().getCheckin()==(0)){
                        if (model.getItems().getPurpose_return()){
                            btnNext.setText("CheckOut");
                        }else {
                            btnNext.setVisibility(GONE);
                        }
                    }else {
                        btnNext.setVisibility(GONE);
                    }

                    txtName.setText(model.getItems().getSupplier_name());

                    supplier_details = new ArrayList<>();
                    supplier_details.addAll(model.getItems().getSupplier_details());
                    if (!supplier_details.isEmpty()) {
                        for (int i = 0; i < supplier_details.size(); i++) {
                            SupplierDetails supplierDetails = supplier_details.get(i);
                            if (supplierDetails != null && supplierDetails.getSupplier_email() != null && supplierDetails.getSupplier_email().equalsIgnoreCase(inputValue)) {
                                txtCompany.setText(supplierDetails.getContact_person());
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

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        //update CheckIn
        apiViewModel.materialcheckin_response().observe(this, model -> {
            try {
                if (model != null) {
                    Integer statuscode = model.getResult();
                    if (statuscode.equals(200)) {
                        if (btnNext.getText().toString().equalsIgnoreCase("CheckIn")){
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
    }

    //disable auto click action after scann
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER) {
            // Barcode scanner has scanned a barcode, disable triggered items
            return true;
        }else {
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
        }
    }

    private void updateCheckIn() {


        if (btnNext.getText().toString().equalsIgnoreCase("CheckIn")){
            JsonObject gsonObject = new JsonObject();
            JSONObject jsonObj_ = new JSONObject();
            try {
                jsonObj_.put("formtype", "checkin");
                jsonObj_.put("id", id);
                JsonParser jsonParser = new JsonParser();
                gsonObject = (JsonObject) jsonParser.parse(jsonObj_.toString());
                System.out.println("gsonObject::" + gsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            apiViewModel.materialcheckin(getApplicationContext(), gsonObject);
        }else if (btnNext.getText().toString().equalsIgnoreCase("CheckOut")){
            JsonObject gsonObject = new JsonObject();
            JSONObject jsonObj_ = new JSONObject();
            try {
                jsonObj_.put("formtype", "checkout");
                jsonObj_.put("id", id);
                JsonParser jsonParser = new JsonParser();
                gsonObject = (JsonObject) jsonParser.parse(jsonObj_.toString());
                System.out.println("gsonObject::" + gsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            apiViewModel.materialcheckin(getApplicationContext(), gsonObject);
        }else {

        }

    }

}
