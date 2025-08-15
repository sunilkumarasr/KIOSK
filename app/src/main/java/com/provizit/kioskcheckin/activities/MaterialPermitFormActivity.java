package com.provizit.kioskcheckin.activities;

import static android.view.View.GONE;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;
import com.hbb20.CountryCodePicker;
import com.provizit.kioskcheckin.R;
import com.provizit.kioskcheckin.config.ConnectionReceiver;
import com.provizit.kioskcheckin.config.Preferences;
import com.provizit.kioskcheckin.logins.VisitorLoginActivity;
import com.provizit.kioskcheckin.mvvm.ApiViewModel;
import com.provizit.kioskcheckin.services.Contractor;
import com.provizit.kioskcheckin.services.GetCVisitorDetailsModel;
import com.provizit.kioskcheckin.services.LocationAddressList;
import com.provizit.kioskcheckin.services.MobileData;
import com.provizit.kioskcheckin.services.WorkVisitTypeList;
import com.provizit.kioskcheckin.utilities.Getdocuments;
import com.provizit.kioskcheckin.utilities.Getnationality;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;

public class MaterialPermitFormActivity extends AppCompatActivity implements View.OnClickListener {



    //internet connection
    BroadcastReceiver broadcastReceiver;
    RelativeLayout relative_internet;
    RelativeLayout relative_ui;
    ImageView company_logo;
    ImageView back_image;
    LinearLayout linearSupplierDetails;


    //Date
    TextView txtFromDate, txtToDate;
    Calendar fromDateCalendar = Calendar.getInstance();
    Calendar toDateCalendar = Calendar.getInstance();
    String fromSelectDate = "";
    String toSelectDate = "";
    String fromDateTimeStamp = "";
    String toDateTimeStamp = "";


    GetCVisitorDetailsModel model;
    String compId = "";
    String Nationality;


    Spinner spinnerLocation;
    String locationItem;
    String locationIndexPosition;
    //add material Details
    LinearLayout linearAddMaterialDetails;


    ApiViewModel apiViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_permit_form);

        Intent intent = getIntent();
        model = (GetCVisitorDetailsModel) intent.getSerializableExtra("model_key");
        compId = Preferences.loadStringValue(getApplicationContext(), Preferences.Comp_id, "");

        apiViewModel = new ViewModelProvider(MaterialPermitFormActivity.this).get(ApiViewModel.class);


        inits();
    }

    private void inits() {


        linearSupplierDetails = findViewById(R.id.linearSupplierDetails);
        txtFromDate = findViewById(R.id.txtFromDate);
        txtToDate = findViewById(R.id.txtToDate);
        spinnerLocation = findViewById(R.id.spinnerLocation);
        linearAddMaterialDetails = findViewById(R.id.linearAddMaterialDetails);

        back_image = findViewById(R.id.back_image);
        // Check if the layout direction is right-to-left
        if (isRightToLeft()) {
            // If layout direction is right-to-left, mirror the image
            back_image.setScaleX(-1f);
        } else {
            // If layout direction is left-to-right, reset the image scaling
            back_image.setScaleX(1f);
        }
        //company logo
        company_logo = findViewById(R.id.company_logo);
        String c_Logo = Preferences.loadStringValue(getApplicationContext(), Preferences.company_Logo, "");
        if (c_Logo.equalsIgnoreCase("")) {
        } else {
            Glide.with(MaterialPermitFormActivity.this).load(c_Logo).into(company_logo);
        }

        //internet connection
        relative_internet = findViewById(R.id.relative_internet);
        relative_ui = findViewById(R.id.relative_ui);
        broadcastReceiver = new ConnectionReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (isConnecteds(context)) {
                    relative_internet.setVisibility(GONE);
                    relative_ui.setVisibility(View.VISIBLE);
                } else {
                    relative_internet.setVisibility(View.VISIBLE);
                    relative_ui.setVisibility(GONE);
                }
            }
        };
        registoreNetWorkBroadcast();


        //current Date
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String currentDate = dateFormat.format(calendar.getTime());
        fromSelectDate =currentDate;
        txtFromDate.setText(fromSelectDate);
        //toDate
        toDateCalendar = (Calendar) calendar.clone();
        toSelectDate = currentDate;
        txtToDate.setText(toSelectDate);
        //timeStamp
        try {
            SimpleDateFormat sd = new SimpleDateFormat("d/MM/yyyy", Locale.getDefault());
            sd.setTimeZone(TimeZone.getTimeZone("UTC")); // Ensure UTC if you want exact UNIX time
            Date date = sd.parse(fromSelectDate);
            long timestamp = date.getTime() / 1000;
            fromDateTimeStamp = timestamp+"";
            toDateTimeStamp = timestamp+"";
            Log.e("fromtimestamp",timestamp+"");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        //Spinners List Set
        SpinnersListApis();


        back_image.setOnClickListener(this);
        linearSupplierDetails.setOnClickListener(this);
        txtFromDate.setOnClickListener(this);
        txtToDate.setOnClickListener(this);
        linearAddMaterialDetails.setOnClickListener(this);
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_image:
                Intent intent = new Intent(getApplicationContext(), VisitorLoginActivity.class);
                startActivity(intent);
                break;
            case R.id.linearSupplierDetails:
                SupplierDetailsPopUp();
                break;
            case R.id.txtFromDate:
                showFromDatePicker();
                break;
            case R.id.txtToDate:
                showToDatePicker();
                break;
            case R.id.linearAddMaterialDetails:
                AddMaterialDetailsPopUp();
                break;
        }
    }


    //add Supplier Details
    private void SupplierDetailsPopUp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.add_supplierdetails_popup_layout, null);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        dialog.show();

        // Apply transparent background to allow rounded corners to show
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        ImageView imgClose = dialogView.findViewById(R.id.imgClose);
        EditText EditSupplierName = dialogView.findViewById(R.id.EditSupplierName);
        EditText EditEmail = dialogView.findViewById(R.id.EditEmail);
        EditText EditName = dialogView.findViewById(R.id.EditName);
        CountryCodePicker CCP = dialogView.findViewById(R.id.CCP);
        EditText EditMobile = dialogView.findViewById(R.id.EditMobile);
        EditText EditIDNumber = dialogView.findViewById(R.id.EditIDNumber);
        Spinner spinnerNationality = dialogView.findViewById(R.id.spinnerNationality);
        EditText EditVehicleNumber = dialogView.findViewById(R.id.EditVehicleNumber);
        EditText EditVehicleType = dialogView.findViewById(R.id.EditVehicleType);
        Button btnNext = dialogView.findViewById(R.id.btnNext);

        CCP.setDefaultCountryUsingPhoneCode(Integer.parseInt("+966"));
        CCP.setCountryForNameCode("+966");

        setDataForSupplier(spinnerNationality);

        imgClose.setOnClickListener(v -> dialog.dismiss());

    }

    private void setDataForSupplier(Spinner spinnerNationality) {
        //api Nationality List
        apiViewModel.getdocuments(getApplicationContext());
        apiViewModel.getResponseforSelectedId_list().observe(this, dModel -> {

            ArrayList<Getdocuments> DocumentsList = dModel.getItems();
            ArrayList<String> NationalityNamesList = new ArrayList<>();

            for (Getdocuments doc : DocumentsList) {
                // Ensure this document is active
                if (doc.getActive()) {

                    // Check if this document has a list of nationalities
                    if (doc.getNationlities() != null && !doc.getNationlities().isEmpty()) {
                        for (Getnationality nationality : doc.getNationlities()) {
                            if (nationality.getActive()) {
                                NationalityNamesList.add(nationality.getName());
                            }
                        }
                    }
                }
            }

            // Adapter for Nationality
            ArrayAdapter<String> nationalityAdapter = new ArrayAdapter<>(
                    this, R.layout.custom_spinner_item, NationalityNamesList
            );
            nationalityAdapter.setDropDownViewResource(R.layout.custom_spinner_item);
            spinnerNationality.setAdapter(nationalityAdapter);
            // Spinner Listener
            spinnerNationality.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Nationality = parent.getItemAtPosition(position).toString();

                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

        });

    }



    //from Date
    private void showFromDatePicker() {
        final Calendar calendar = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    fromDateCalendar.set(year, month, dayOfMonth);
                    fromSelectDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                    txtFromDate.setText(fromSelectDate);

                    //timeStamp
                    try {
                        SimpleDateFormat sd = new SimpleDateFormat("d/MM/yyyy", Locale.getDefault());
                        sd.setTimeZone(TimeZone.getTimeZone("UTC")); // Ensure UTC if you want exact UNIX time
                        Date date = sd.parse(fromSelectDate);
                        long timestamp = date.getTime() / 1000;
                        fromDateTimeStamp = timestamp+"";
                        Log.e("fromtimestamp",timestamp+"");
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }


                    // Reset To Date
                    txtToDate.setText("");
                    txtToDate.setHint("To");
                    toDateCalendar = (Calendar) fromDateCalendar.clone();

                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        // ✅ Disable past dates
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

        datePickerDialog.show();
    }

    //To Date
    private void showToDatePicker() {
        if (toSelectDate.equalsIgnoreCase("Select From Date")) {
            Toast.makeText(this, "Please select From Date first", Toast.LENGTH_SHORT).show();
            return;
        }
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    toDateCalendar.set(year, month, dayOfMonth);
                    toSelectDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                    txtToDate.setText(toSelectDate);

                    //timeStamp
                    try {
                        SimpleDateFormat sd = new SimpleDateFormat("d/MM/yyyy", Locale.getDefault());
                        sd.setTimeZone(TimeZone.getTimeZone("UTC")); // Ensure UTC if you want exact UNIX time
                        Date date = sd.parse(toSelectDate);
                        long timestamp = date.getTime() / 1000;
                        toDateTimeStamp = timestamp+"";
                        Log.e("fromtimestamp",timestamp+"");
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }

                },
                toDateCalendar.get(Calendar.YEAR),
                toDateCalendar.get(Calendar.MONTH),
                toDateCalendar.get(Calendar.DAY_OF_MONTH)
        );
        // Set minimum date to fromDate
        datePickerDialog.getDatePicker().setMinDate(fromDateCalendar.getTimeInMillis());
        datePickerDialog.show();
    }


    //Spinners List Set
    private void SpinnersListApis() {

        //Location
        apiViewModel.getuserDetails(getApplicationContext(), "address");
        apiViewModel.getuserDetails_response().observe(this, dModel -> {
            ArrayList<LocationAddressList> documentsList = dModel.getItems().getAddress();
            ArrayList<String> visitTypeList = new ArrayList<>();

            // Prepare list for spinner
            for (LocationAddressList doc : documentsList) {
                visitTypeList.add(doc.getName());
            }

            //setAdapter
            ArrayAdapter<String> selectIDAdapter = new ArrayAdapter<>(
                    this, R.layout.custom_spinner_item, visitTypeList
            );
            selectIDAdapter.setDropDownViewResource(R.layout.custom_spinner_item);
            spinnerLocation.setAdapter(selectIDAdapter);
            // Select Spinner Listener
            spinnerLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    locationIndexPosition = position + "";
                    locationItem = parent.getItemAtPosition(position).toString();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

        });


    }


    //add Material Details
    private void AddMaterialDetailsPopUp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.add_material_details_popup_layout, null);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        dialog.show();

        // Apply transparent background to allow rounded corners to show
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        ImageView imgClose = dialogView.findViewById(R.id.imgClose);
        EditText EditName = dialogView.findViewById(R.id.EditName);
        EditText EditSerialNo = dialogView.findViewById(R.id.EditSerialNo);
        EditText EditQuantity = dialogView.findViewById(R.id.EditQuantity);
        Button btnNext = dialogView.findViewById(R.id.btnNext);

        imgClose.setOnClickListener(v -> dialog.dismiss());

    }


    // Function to check if the layout direction is right-to-left
    private boolean isRightToLeft() {
        return getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_RTL;
    }

    protected void registoreNetWorkBroadcast() {
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,
                new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), VisitorLoginActivity.class);
        startActivity(intent);
    }

}