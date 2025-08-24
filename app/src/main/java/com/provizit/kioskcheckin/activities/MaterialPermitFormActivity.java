package com.provizit.kioskcheckin.activities;

import static android.view.View.GONE;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hbb20.CountryCodePicker;
import com.provizit.kioskcheckin.R;
import com.provizit.kioskcheckin.adapters.MaterialDetailsSetAdapter;
import com.provizit.kioskcheckin.adapters.SupplierDetailsAdapter;
import com.provizit.kioskcheckin.config.ConnectionReceiver;
import com.provizit.kioskcheckin.config.DateRangeTimestamps;
import com.provizit.kioskcheckin.config.Preferences;
import com.provizit.kioskcheckin.logins.VisitorLoginActivity;
import com.provizit.kioskcheckin.mvvm.ApiViewModel;
import com.provizit.kioskcheckin.services.Contractor;
import com.provizit.kioskcheckin.services.GetCVisitorDetailsModel;
import com.provizit.kioskcheckin.services.LocationAddressList;
import com.provizit.kioskcheckin.services.MaterialDetailsSet;
import com.provizit.kioskcheckin.services.MaterialItemsList;
import com.provizit.kioskcheckin.services.SupplierDetails;
import com.provizit.kioskcheckin.services.SupplierMobile;
import com.provizit.kioskcheckin.utilities.GetSearchEmployees;
import com.provizit.kioskcheckin.utilities.Getdocuments;
import com.provizit.kioskcheckin.utilities.Getnationality;
import com.provizit.kioskcheckin.utilities.Getsubhierarchys;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class MaterialPermitFormActivity extends AppCompatActivity implements View.OnClickListener {


    //internet connection
    BroadcastReceiver broadcastReceiver;
    RelativeLayout relative_internet;
    RelativeLayout relative_ui;
    ImageView company_logo;
    ImageView back_image;
    LinearLayout linearSupplierDetails;

    RadioGroup radioGroup ;
    RadioButton radioEntry, radioExit;
    String EntryType = "1";

    //Supplier Details
    List<SupplierDetails> supplierDetailsList = new ArrayList<>();
    LinearLayout linearC;
    ImageView imgSuppliersInfo;
    Button btnAddMoreSupplierDetails;

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

    //Ref Document
    TextView txtRefDoc;
    View viewRef;
    Spinner spinnerRefDoc;
    EditText EditSpinnerRefDoc;
    String StatusEditspinnerRefDoc;
    String RefDocItem;

    //Purpose
    Spinner spinnerPurpose;
    String PurposeItem;
    String PurposeId;
    boolean PurposeReturn;

    //Pertains
    Spinner spinnerPertains;
    String PertainsItem;
    String DepartmentId;

    //Employee
    Spinner spinnerEmployee;
    String EmployeeItem;
    String EmployeeId = "";

    Spinner spinnerLocation;
    String locationItem;
    String locationIndexPosition;

    //add material Details
    LinearLayout linearAddMaterialDetails;
    TextView txtMaterial;
    private int counter = 1;
    List<MaterialDetailsSet> materialDetailsList = new ArrayList<>();

    Button btnNext;
    ApiViewModel apiViewModel;

    ProgressDialog progress;

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

        radioGroup = findViewById(R.id.radioGroup);
        radioEntry = findViewById(R.id.radioEntry);
        radioExit = findViewById(R.id.radioExit);
        linearSupplierDetails = findViewById(R.id.linearSupplierDetails);
        linearC = findViewById(R.id.linearC);
        imgSuppliersInfo = findViewById(R.id.imgSuppliersInfo);
        btnAddMoreSupplierDetails = findViewById(R.id.btnAddMoreSupplierDetails);
        txtFromDate = findViewById(R.id.txtFromDate);
        txtToDate = findViewById(R.id.txtToDate);
        txtRefDoc = findViewById(R.id.txtRefDoc);
        viewRef = findViewById(R.id.viewRef);
        spinnerRefDoc = findViewById(R.id.spinnerRefDoc);
        EditSpinnerRefDoc = findViewById(R.id.EditSpinnerRefDoc);
        spinnerPurpose = findViewById(R.id.spinnerPurpose);
        spinnerPertains = findViewById(R.id.spinnerPertains);
        spinnerEmployee = findViewById(R.id.spinnerEmployee);
        spinnerLocation = findViewById(R.id.spinnerLocation);
        linearAddMaterialDetails = findViewById(R.id.linearAddMaterialDetails);
        txtMaterial = findViewById(R.id.txtMaterial);
        btnNext = findViewById(R.id.btnNext);
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

        progress = new ProgressDialog(this);
        progress.setTitle(getResources().getString(R.string.Loading));
        progress.setMessage(getResources().getString(R.string.whileloading));
        progress.setCancelable(true);
        progress.setCanceledOnTouchOutside(true);

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


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioEntry) {
                    //Entry selected
                    EntryType = "1";
                    viewRef.setVisibility(View.VISIBLE);
                    txtRefDoc.setVisibility(View.VISIBLE);
                    spinnerRefDoc.setVisibility(View.VISIBLE);
                    SpinnersListApis();
                } else if (checkedId == R.id.radioExit) {
                    //Exit selected
                    EntryType = "2";
                    txtRefDoc.setVisibility(GONE);
                    viewRef.setVisibility(GONE);
                    spinnerRefDoc.setVisibility(GONE);
                    EditSpinnerRefDoc.setVisibility(GONE);
                    SpinnersListApis();
                }
            }
        });


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


        //submit Material Permit form
        apiViewModel.actionentrypermitrequest_response().observe(this, model -> {
            progress.dismiss();
            if (model.getResult().equals(200)){
                Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), VisitorLoginActivity.class);
                startActivity(intent);
            }else {
                Toast.makeText(getApplicationContext(), "Failed Please Try Again", Toast.LENGTH_SHORT).show();
            }
        });

        back_image.setOnClickListener(this);
        linearSupplierDetails.setOnClickListener(this);
        btnAddMoreSupplierDetails.setOnClickListener(this);
        txtFromDate.setOnClickListener(this);
        txtToDate.setOnClickListener(this);
        linearAddMaterialDetails.setOnClickListener(this);
        btnNext.setOnClickListener(this);
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
            case R.id.btnAddMoreSupplierDetails:
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
            case R.id.btnNext:


                if (StatusEditspinnerRefDoc.equalsIgnoreCase("true")){
                    if (!EditSpinnerRefDoc.getText().toString().equalsIgnoreCase("")){
                        RefDocItem = EditSpinnerRefDoc.getText().toString();
                    }
                }


                if (supplierDetailsList.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Add Supplier Details", Toast.LENGTH_SHORT).show();
                }else if (txtFromDate.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Please Select From Date", Toast.LENGTH_SHORT).show();
                }else if (txtToDate.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Please Select To Date", Toast.LENGTH_SHORT).show();
                }else if (EntryType.equalsIgnoreCase("1") && RefDocItem.equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Please Select Ref Document", Toast.LENGTH_SHORT).show();
                }else if (EntryType.equalsIgnoreCase("1") && StatusEditspinnerRefDoc.equalsIgnoreCase("true")  && EditSpinnerRefDoc.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Enter Ref Document", Toast.LENGTH_SHORT).show();
                }else if (PurposeItem.equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Please Select Purpose", Toast.LENGTH_SHORT).show();
                }else if (locationItem.equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Please Select Location", Toast.LENGTH_SHORT).show();
                }else if (PertainsItem.equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Please Select Pertains", Toast.LENGTH_SHORT).show();
                }else if (materialDetailsList.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Add Material Details", Toast.LENGTH_SHORT).show();
                }else {

                    JsonObject json = createMaterialPermitSubmit();
                    apiViewModel.actionentrypermitrequest(getApplicationContext(), json);
                    progress.show();
                }

                break;
        }
    }

    private JsonObject createMaterialPermitSubmit() {

        JsonObject gsonObject = new JsonObject();
        JSONObject jsonObj_ = new JSONObject();
        JSONArray supplierDetailsData = new JSONArray();
        JSONArray materialDetailsData = new JSONArray();


        //supplier Details Data List
        Gson gson = new Gson();
        try {
            for (SupplierDetails supplierDetails : supplierDetailsList) {
                String jsonString = gson.toJson(supplierDetails);
                JSONObject jsonObject = new JSONObject(jsonString);
                supplierDetailsData.put(jsonObject);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        //material Details List
        Gson gsonm = new Gson();
        try {
            for (MaterialDetailsSet materialDetailsSet : materialDetailsList) {
                String jsonString = gsonm.toJson(materialDetailsSet);
                JSONObject jsonObject = new JSONObject(jsonString);
                materialDetailsData.put(jsonObject);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            jsonObj_.put("formtype", "insert");
            jsonObj_.put("type", EntryType);
            jsonObj_.put("start", fromDateTimeStamp);
            jsonObj_.put("end", toDateTimeStamp);

            if (EntryType.equalsIgnoreCase("1")){
                jsonObj_.put("ref_document", RefDocItem);
            }else {
                jsonObj_.put("ref_document", "");
            }

            jsonObj_.put("purpose", PurposeItem);
            jsonObj_.put("purpose_id", PurposeId);
            jsonObj_.put("purpose_return", PurposeReturn);
            jsonObj_.put("l_id", locationIndexPosition);

            if (EmployeeId != null && !EmployeeId.isEmpty()) {
                jsonObj_.put("employee", EmployeeId);
            }else {
                jsonObj_.put("employee", "");
            }

            jsonObj_.put("department", DepartmentId);
            jsonObj_.put("material_details", materialDetailsData);
            jsonObj_.put("supplier_name", supplierDetailsList.get(0).contact_person);
            jsonObj_.put("supplier_details", supplierDetailsData);
            jsonObj_.put("contact_person", "");
            jsonObj_.put("driver_mobile", "");
            jsonObj_.put("driver_name", "");
            jsonObj_.put("id_number", "");
            jsonObj_.put("supplier_mobile", "");
            jsonObj_.put("nationality", "");
            jsonObj_.put("plate_no", "");
            jsonObj_.put("vehicle_type", "");
            jsonObj_.put("emp_id", model.getIncomplete_data().get_id().get$oid());
            jsonObj_.put("comp_id", compId);
            jsonObj_.put("location", "");
            JsonParser jsonParser = new JsonParser();
            gsonObject = (JsonObject) jsonParser.parse(jsonObj_.toString());
            System.out.println("createjsongsonObject::" + gsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return gsonObject;

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
        Button btnSubmit = dialogView.findViewById(R.id.btnSubmit);

        CCP.setDefaultCountryUsingPhoneCode(Integer.parseInt("+966"));
        CCP.setCountryForNameCode("+966");

        setDataForSupplier(spinnerNationality);

        imgClose.setOnClickListener(v -> dialog.dismiss());

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = EditSupplierName.getText().toString();
                String email = EditEmail.getText().toString();
                String companyName = EditName.getText().toString();
                String idNumber = EditIDNumber.getText().toString();
                String nationality = Nationality;
                String vehicleNumber = EditVehicleNumber.getText().toString();
                String vehicleType = EditVehicleType.getText().toString();
                String mobileWithCountryCode = CCP.getSelectedCountryCode() + EditMobile.getText().toString();


                if (name.equalsIgnoreCase("")){
                    Toast.makeText(getApplicationContext(), "Enter SupplierName", Toast.LENGTH_SHORT).show();
                } else if (email.equalsIgnoreCase("")){
                    Toast.makeText(getApplicationContext(), "Enter email", Toast.LENGTH_SHORT).show();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(getApplicationContext(), "Enter a valid email address", Toast.LENGTH_SHORT).show();
                } else if (companyName.equalsIgnoreCase("")){
                    Toast.makeText(getApplicationContext(), "Enter name", Toast.LENGTH_SHORT).show();
                }else if (nationality.equalsIgnoreCase("")){
                    Toast.makeText(getApplicationContext(), "Select Nationality", Toast.LENGTH_SHORT).show();
                }else {

                    String number = EditMobile.getText().toString();
                    String internationalNumber = CCP.getSelectedCountryCodeWithPlus() + EditMobile.getText().toString();
                    String nationalNumber = "0"+EditMobile.getText().toString();
                    String e164Number = CCP.getSelectedCountryCodeWithPlus() + EditMobile.getText().toString().trim();
                    String countryCode = CCP.getSelectedCountryNameCode();
                    String dialCode = CCP.getSelectedCountryCodeWithPlus();


                    if (number.equalsIgnoreCase("") || number == null){
                        number = "";
                        internationalNumber = "";
                        nationalNumber = "";
                        e164Number = "";
                        countryCode = "";
                        dialCode = "";
                    }

                    // Build MobileData object
                    SupplierMobile mobileData = new SupplierMobile(
                            number,
                            internationalNumber,
                            nationalNumber,
                            e164Number,
                            countryCode,
                            dialCode
                    );

                    if (idNumber.equalsIgnoreCase("") || idNumber == null){
                        idNumber = "";
                    }
                    if (vehicleNumber.equalsIgnoreCase("") || vehicleNumber == null){
                        vehicleNumber = "";
                    }
                    if (vehicleType.equalsIgnoreCase("") || vehicleType == null){
                        vehicleType = "";
                    }


                    // Create SubContractor object
                    SupplierDetails supplierDetails = new SupplierDetails(
                            name,
                            email,
                            mobileData,
                            idNumber,
                            nationality,
                            vehicleNumber,
                            vehicleType,
                            false
                    );


                    //list
                    supplierDetailsList.add(supplierDetails);
                    dialog.dismiss();
                    setRecyclersupplierDetailsList();

                }
            }
        });

    }

    private void setRecyclersupplierDetailsList() {
        RecyclerView recyclerView = findViewById(R.id.recyclerviewSuppliers);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        SupplierDetailsAdapter adapter = new SupplierDetailsAdapter(supplierDetailsList);
        recyclerView.setAdapter(adapter);

        if (supplierDetailsList.isEmpty()) {
            linearC.setVisibility(GONE);
            imgSuppliersInfo.setVisibility(GONE);
        } else {
            linearC.setVisibility(View.VISIBLE);
            imgSuppliersInfo.setVisibility(View.VISIBLE);
        }
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

        //Ref Documents
        apiViewModel.getrefdocuments(getApplicationContext(), compId);
        apiViewModel.getrefdocuments_response().observe(this, dModel -> {
            ArrayList<MaterialItemsList> documentsList = dModel.getItems();
            ArrayList<String> RefDocumentsList = new ArrayList<>();

            // Prepare list for spinner
            for (MaterialItemsList doc : documentsList) {
                if (doc.getActive()) {
                    RefDocumentsList.add(doc.getName());
                }
            }
            RefDocumentsList.add("Others");

            //setAdapter
            ArrayAdapter<String> selectIDAdapter = new ArrayAdapter<>(
                    this, R.layout.custom_spinner_item, RefDocumentsList
            );
            selectIDAdapter.setDropDownViewResource(R.layout.custom_spinner_item);
            spinnerRefDoc.setAdapter(selectIDAdapter);
            // Select Spinner Listener
            spinnerRefDoc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    RefDocItem = parent.getItemAtPosition(position).toString();
                    if (RefDocItem.equalsIgnoreCase("Others")) {
                        StatusEditspinnerRefDoc = "true";
                        EditSpinnerRefDoc.setVisibility(View.VISIBLE);
                    } else {
                        StatusEditspinnerRefDoc = "";
                        EditSpinnerRefDoc.setVisibility(GONE);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

        });

        //Purpose
        apiViewModel.getentrypurposes(getApplicationContext(), compId);
        apiViewModel.getentrypurposes_response().observe(this, dModel -> {
            ArrayList<MaterialItemsList> documentsList = dModel.getItems();
            ArrayList<String> PurposeList = new ArrayList<>();

            // Prepare list for spinner
            for (MaterialItemsList doc : documentsList) {
                if (doc.getActive()) {
                    PurposeList.add(doc.getName());
                }
            }

            //setAdapter
            ArrayAdapter<String> selectIDAdapter = new ArrayAdapter<>(
                    this, R.layout.custom_spinner_item, PurposeList
            );
            selectIDAdapter.setDropDownViewResource(R.layout.custom_spinner_item);
            spinnerPurpose.setAdapter(selectIDAdapter);
            // Select Spinner Listener
            spinnerPurpose.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    PurposeItem = parent.getItemAtPosition(position).toString();

                    for (MaterialItemsList item : documentsList) {
                        if (item.getName().equals(PurposeItem)) {
                            PurposeId = item.get_id().get$oid();
                            PurposeReturn = item.getReturnType();
                            break;
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

        });

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

                    //Pertains
                    PertainsApi("0"+locationIndexPosition);

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

        });


    }

    private void PertainsApi(String position) {

        //Pertains clear
        ArrayAdapter<String> adapterPertains = (ArrayAdapter<String>) spinnerPertains.getAdapter();
        if (adapterPertains != null) {
            adapterPertains.clear();                 // remove all items
            adapterPertains.notifyDataSetChanged();  // refresh spinner
        }
        //Employee clear
        ArrayAdapter<String> adapterEmployee = (ArrayAdapter<String>) spinnerEmployee.getAdapter();
        if (adapterEmployee != null) {
            adapterEmployee.clear();                 // remove all items
            adapterEmployee.notifyDataSetChanged();  // refresh spinner
        }

        apiViewModel.getsubhierarchysmaterial(getApplicationContext(), compId, position);
        apiViewModel.getsubhierarchysmaterial_response().observe(this, dModel -> {
            ArrayList<Getsubhierarchys> documentsList = dModel.getItems();
            ArrayList<String> PertainsList = new ArrayList<>();
            PertainsList.clear();

            // Prepare list for spinner
            for (Getsubhierarchys doc : documentsList) {
                PertainsList.add(doc.getName()+"("+doc.getHierarchy()+")");
            }

            //setAdapter
            ArrayAdapter<String> selectIDAdapter = new ArrayAdapter<>(
                    this, R.layout.custom_spinner_item, PertainsList
            );
            selectIDAdapter.setDropDownViewResource(R.layout.custom_spinner_item);
            spinnerPertains.setAdapter(selectIDAdapter);
            // Select Spinner Listener
            spinnerPertains.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    PertainsItem = parent.getItemAtPosition(position).toString();

                    for (Getsubhierarchys item : documentsList) {
                        String dd = item.getName()+"("+item.getHierarchy()+")";
                        if (dd.equalsIgnoreCase(PertainsItem)) {
                            DepartmentId = item.get_id().get$oid();
                            break;
                        }
                    }
                    //employeesList
                    employeesList(position+"",DepartmentId);

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

        });
    }

    private void employeesList(String position, String departmentId) {
        apiViewModel.getsearchemployeesmaterial(getApplicationContext(), position, departmentId, "");
        apiViewModel.getResponseforSearchEmployees().observe(this, model -> {
            ArrayList<GetSearchEmployees> documentsList = model.getItems();
            ArrayList<String> employeesList = new ArrayList<>();
            employeesList.clear();
            EmployeeId = "";
            // Prepare list for spinner
            for (GetSearchEmployees doc : documentsList) {
                employeesList.add(doc.getName());
            }

            //setAdapter
            ArrayAdapter<String> selectIDAdapter = new ArrayAdapter<>(
                    this, R.layout.custom_spinner_item, employeesList
            );
            selectIDAdapter.setDropDownViewResource(R.layout.custom_spinner_item);
            spinnerEmployee.setAdapter(selectIDAdapter);
            // Select Spinner Listener
            spinnerEmployee.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    EmployeeItem = parent.getItemAtPosition(position).toString();
                    for (GetSearchEmployees item : documentsList) {
                        if (item.getName().equals(EmployeeItem)) {
                            EmployeeId = item.get_id().get$oid();
                            break;
                        }
                    }
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
        LinearLayout linearDecrement = dialogView.findViewById(R.id.linearDecrement);
        TextView cartQty = dialogView.findViewById(R.id.cartQty);
        LinearLayout linearIncrement = dialogView.findViewById(R.id.linearIncrement);
        TextView txtQuantity = dialogView.findViewById(R.id.txtQuantity);
        Button btnAdd = dialogView.findViewById(R.id.btnAdd);

        counter = 1;

        // Decrement (but not less than 1)
        linearDecrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (counter > 1) {
                    counter--;
                    cartQty.setText(String.valueOf(counter));
                    txtQuantity.setText(String.valueOf(counter));
                }
            }
        });
        //Increment
        linearIncrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter++;
                cartQty.setText(String.valueOf(counter));
                txtQuantity.setText(String.valueOf(counter));
            }
        });

        imgClose.setOnClickListener(v -> dialog.dismiss());

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (EditName.getText().toString().equalsIgnoreCase("")){
                    Toast.makeText(getApplicationContext(),"Enter Name and Description",Toast.LENGTH_SHORT).show();
                }else if (EditSerialNo.getText().toString().equalsIgnoreCase("")){
                    Toast.makeText(getApplicationContext(),"Enter Serial No",Toast.LENGTH_SHORT).show();
                }else {

                    // Create SubContractor object
                    MaterialDetailsSet materialDetailsSet = new MaterialDetailsSet(
                            EditName.getText().toString(),
                            counter,
                            EditSerialNo.getText().toString(),
                            false
                    );

                    //list
                    materialDetailsList.add(materialDetailsSet);
                    dialog.dismiss();
                    setRecyclerMaterialDetailsList();

                }
            }
        });

    }

    private void setRecyclerMaterialDetailsList() {
        RecyclerView recyclerView = findViewById(R.id.recyclerViewMeetingDetailsList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        MaterialDetailsSetAdapter adapter = new MaterialDetailsSetAdapter(materialDetailsList);
        recyclerView.setAdapter(adapter);

        if (materialDetailsList.isEmpty()) {
            recyclerView.setVisibility(GONE);
            txtMaterial.setText("Material Permit Details");
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            txtMaterial.setText("Add More");
        }
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