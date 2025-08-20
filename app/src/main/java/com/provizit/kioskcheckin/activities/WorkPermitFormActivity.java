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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.provizit.kioskcheckin.adapters.ContractorAdapter;
import com.provizit.kioskcheckin.adapters.SubContractorAdapter;
import com.provizit.kioskcheckin.config.ConnectionReceiver;
import com.provizit.kioskcheckin.config.DateRangeTimestamps;
import com.provizit.kioskcheckin.config.Preferences;
import com.provizit.kioskcheckin.logins.VisitorLoginActivity;
import com.provizit.kioskcheckin.mvvm.ApiViewModel;
import com.provizit.kioskcheckin.services.Contractor;
import com.provizit.kioskcheckin.services.GetCVisitorDetailsModel;
import com.provizit.kioskcheckin.services.LocationAddressList;
import com.provizit.kioskcheckin.services.MobileData;
import com.provizit.kioskcheckin.services.SubContractor;
import com.provizit.kioskcheckin.services.WorkVisitTypeList;
import com.provizit.kioskcheckin.services.WorkingDaysList;
import com.provizit.kioskcheckin.utilities.Getdocuments;
import com.provizit.kioskcheckin.utilities.Getnationality;
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

public class WorkPermitFormActivity extends AppCompatActivity implements View.OnClickListener {

    GetCVisitorDetailsModel model;
    String compId = "";

    //internet connection
    BroadcastReceiver broadcastReceiver;
    RelativeLayout relative_internet;
    RelativeLayout relative_ui;
    ImageView company_logo;
    ImageView back_image;
    Button btnAddMoreContractors, btnAddMoreSubContractors;
    LinearLayout linearContractor, linearSubContractor;

    //Contractor
    LinearLayout linearC;
    ImageView imgContractorInfo;
    String ContractorSelectId;
    String ContractorPosition;
    String ContractorNationality;
    List<Contractor> contractorList = new ArrayList<>();

    //subContractor
    LinearLayout linearS;
    ImageView imgSubInfo;
    String SubContractorSelectId;
    String SubContractorPosition;
    String SubContractorNationality;
    List<SubContractor> subContractorList = new ArrayList<>();


    Spinner spinnerLocation;
    String locationItem;
    String locationIndexPosition;


    Spinner spinnerVisitType, spinnerPlaceOfWork, spinnerSelectScopeOfWork;
    EditText EditPlaceOfWork, EditScopeOfWork;

    String StatusEditScopeOfWork = "";
    String StatusEditPlaceOfWork = "";
    String workVisitType = "";
    String placeOfWorkVisitType = "";
    String scopeOfWorkTextArea = "";

    //Date
    TextView txtFromDate, txtToDate;
    Calendar fromDateCalendar = Calendar.getInstance();
    Calendar toDateCalendar = Calendar.getInstance();
    String fromSelectDate = "";
    String toSelectDate = "";
    String fromDateTimeStamp = "";
    String toDateTimeStamp = "";

    //checkBox
    LinearLayout linearTime;
    CheckBox checkBox24HR, checkBoxWorkingHR;
    ArrayList<WorkingDaysList> WorkingHRWorkingList = new ArrayList<>();

    //time List
    Spinner spinnerStartTime, spinnerEndTime;
    String fromSelectTime = "";
    String toSelectTime = "";
    List<String> allTimeList = new ArrayList<>();
    List<String> filteredStartList = new ArrayList<>(allTimeList);


    Button btnNext;

    ApiViewModel apiViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_permit_form);

        Intent intent = getIntent();
        model = (GetCVisitorDetailsModel) intent.getSerializableExtra("model_key");
        compId = Preferences.loadStringValue(getApplicationContext(), Preferences.Comp_id, "");


        apiViewModel = new ViewModelProvider(WorkPermitFormActivity.this).get(ApiViewModel.class);




        inits();

    }

    private void inits() {
        back_image = findViewById(R.id.back_image);
        linearC = findViewById(R.id.linearC);
        imgContractorInfo = findViewById(R.id.imgContractorInfo);
        linearContractor = findViewById(R.id.linearContractor);
        btnAddMoreContractors = findViewById(R.id.btnAddMoreContractors);
        linearS = findViewById(R.id.linearS);
        imgSubInfo = findViewById(R.id.imgSubInfo);
        linearSubContractor = findViewById(R.id.linearSubContractor);
        btnAddMoreSubContractors = findViewById(R.id.btnAddMoreSubContractors);
        spinnerLocation = findViewById(R.id.spinnerLocation);
        spinnerVisitType = findViewById(R.id.spinnerVisitType);
        spinnerPlaceOfWork = findViewById(R.id.spinnerPlaceOfWork);
        spinnerSelectScopeOfWork = findViewById(R.id.spinnerSelectScopeOfWork);
        EditPlaceOfWork = findViewById(R.id.EditPlaceOfWork);
        EditScopeOfWork = findViewById(R.id.EditScopeOfWork);
        txtFromDate = findViewById(R.id.txtFromDate);
        txtToDate = findViewById(R.id.txtToDate);
        linearTime = findViewById(R.id.linearTime);
        checkBox24HR = findViewById(R.id.checkBox24HR);
        checkBoxWorkingHR = findViewById(R.id.checkBoxWorkingHR);
        spinnerStartTime = findViewById(R.id.spinnerStartTime);
        spinnerEndTime = findViewById(R.id.spinnerEndTime);
        btnNext = findViewById(R.id.btnNext);
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
            Glide.with(WorkPermitFormActivity.this).load(c_Logo).into(company_logo);
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


        //Spinners List Set
        SpinnersListApis();
        //Time List start and End
        StartAndEndTimeList(true);


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


        checkBox24HR.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    checkBoxWorkingHR.setChecked(false);
                    linearTime.setVisibility(GONE);
                    Toast.makeText(getApplicationContext(), "Checked", Toast.LENGTH_SHORT).show();
                } else {
                    if (checkBoxWorkingHR.isChecked()) {
                        linearTime.setVisibility(GONE);
                    } else {
                        linearTime.setVisibility(View.VISIBLE);
                    }
                    Toast.makeText(getApplicationContext(), "Unchecked", Toast.LENGTH_SHORT).show();
                }
            }
        });
        checkBoxWorkingHR.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    checkBox24HR.setChecked(false);
                    linearTime.setVisibility(GONE);
                    Toast.makeText(getApplicationContext(), "Checked", Toast.LENGTH_SHORT).show();
                } else {
                    if (checkBox24HR.isChecked()) {
                        linearTime.setVisibility(GONE);
                    } else {
                        linearTime.setVisibility(View.VISIBLE);
                    }
                    Toast.makeText(getApplicationContext(), "Unchecked", Toast.LENGTH_SHORT).show();
                }
            }
        });


        //submit workPermit form
        apiViewModel.actionworkpermita_response().observe(this, model -> {
            if (model.getResult().equals(200)){
                Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), VisitorLoginActivity.class);
                startActivity(intent);
            }else {
                Toast.makeText(getApplicationContext(), "Failed Please Try Again", Toast.LENGTH_SHORT).show();
            }
        });


        back_image.setOnClickListener(this);
        linearContractor.setOnClickListener(this);
        btnAddMoreContractors.setOnClickListener(this);
        linearSubContractor.setOnClickListener(this);
        btnAddMoreSubContractors.setOnClickListener(this);
        txtFromDate.setOnClickListener(this);
        txtToDate.setOnClickListener(this);
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
            case R.id.linearContractor:
                AddContractorPopUp();
                break;
            case R.id.btnAddMoreContractors:
                AddContractorPopUp();
                break;
            case R.id.linearSubContractor:
                AddSubContractorPopUp();
                break;
            case R.id.btnAddMoreSubContractors:
                AddSubContractorPopUp();
                break;
            case R.id.txtFromDate:
                showFromDatePicker();
                break;
            case R.id.txtToDate:
                showToDatePicker();
                break;
            case R.id.btnNext:

                if (StatusEditPlaceOfWork.equalsIgnoreCase("true")) {
                    if (!EditPlaceOfWork.getText().toString().equalsIgnoreCase("")){
                        placeOfWorkVisitType = EditPlaceOfWork.getText().toString();
                    }
                }
                if (StatusEditScopeOfWork.equalsIgnoreCase("true")) {
                    if (!EditScopeOfWork.getText().toString().equalsIgnoreCase("")){
                        scopeOfWorkTextArea = EditScopeOfWork.getText().toString();
                    }
                }


                if (contractorList.isEmpty() && subContractorList.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Add Contractor", Toast.LENGTH_SHORT).show();
                } else if (locationItem.equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Please Select Location", Toast.LENGTH_SHORT).show();
                } else if (workVisitType.equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Please Select Work / visit Type", Toast.LENGTH_SHORT).show();
                } else if (placeOfWorkVisitType.equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Please Select Place of work/visit Type", Toast.LENGTH_SHORT).show();
                } else if (StatusEditPlaceOfWork.equalsIgnoreCase("true") && EditPlaceOfWork.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Please Enter Place of work/visit Type", Toast.LENGTH_SHORT).show();
                } else if (scopeOfWorkTextArea.equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Please Select Scope of work text area", Toast.LENGTH_SHORT).show();
                } else if (StatusEditScopeOfWork.equalsIgnoreCase("true") && EditScopeOfWork.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Please Enter Scope of work text area", Toast.LENGTH_SHORT).show();
                } else if (txtFromDate.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Please Select From Date", Toast.LENGTH_SHORT).show();
                } else if (txtToDate.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Please Select To Date", Toast.LENGTH_SHORT).show();
                } else {
                    JsonObject json = createWorkPermitSubmit();
                    apiViewModel.actionworkpermita(getApplicationContext(), json);

                }
                break;
        }
    }

    private JsonObject createWorkPermitSubmit() {

        JsonObject gsonObject = new JsonObject();
        JSONObject jsonObj_ = new JSONObject();
        JSONObject contractorData_ = new JSONObject();
        JSONArray approveStatistics = new JSONArray();
        JSONArray assignDepartments = new JSONArray();
        JSONArray contractors_Data = new JSONArray();
        JSONArray sub_ContractorsData = new JSONArray();
        JSONArray startTimesArray = new JSONArray();
        JSONArray endTimesArray = new JSONArray();

        //empty contractorData
        try {
            contractorData_.put("name", "");
            contractorData_.put("company", "");
            contractorData_.put("id_type", "");
            contractorData_.put("id_name", "");
            contractorData_.put("nationality", "");
            contractorData_.put("id_number", "");
            contractorData_.put("email", "");
            contractorData_.put("mobile", "");
            contractorData_.put("mobileData","");
            contractorData_.put("performing_work", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //contractor List
        Gson gson = new Gson();
        try {
            for (Contractor contractor : contractorList) {
                String jsonString = gson.toJson(contractor);
                JSONObject jsonObject = new JSONObject(jsonString);
                contractors_Data.put(jsonObject);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //sub-Contractor List
        try {
            sub_ContractorsData = new JSONArray();
            for (int i = 0; i < subContractorList.size(); i++) {
                sub_ContractorsData.put(subContractorList.get(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //company name
        String companyName = "";
        if (!contractorList.isEmpty()) {
            companyName = contractorList.get(0).companyName;
        } else if (!subContractorList.isEmpty()) {
            companyName = subContractorList.get(0).companyName;
        }

        //checkBox conditions
        boolean workingHours;
        boolean twentyFourHours;
        if (checkBoxWorkingHR.isChecked()) {
            workingHours = true;
            fromSelectTime = "00:00";
            toSelectTime = "23:59";
        } else {
            workingHours = false;
            fromSelectTime = spinnerStartTime.getSelectedItem().toString();
            toSelectTime = spinnerEndTime.getSelectedItem().toString();
        }
        if (checkBox24HR.isChecked()) {
            twentyFourHours = true;
            fromSelectTime = WorkingHRWorkingList.get(1).getStart();
            toSelectTime = WorkingHRWorkingList.get(1).getEnd();
        } else {
            twentyFourHours = false;
            fromSelectTime = spinnerStartTime.getSelectedItem().toString();
            toSelectTime = spinnerEndTime.getSelectedItem().toString();
        }

        //start and end datetime array list
        //start Date 8/08/2025 and Start Time 10:15
        String StartDateTimeArray = DateRangeTimestamps.getTimestampArrayString(fromSelectDate, fromSelectTime, toSelectDate, toSelectTime);
        try {
            startTimesArray = new JSONArray(StartDateTimeArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //End and Start datetime array list
        //start Date 8/08/2025 and End Time 10:15
        String EndDateTimeArray = DateRangeTimestamps.getTimestampArrayString(fromSelectDate, toSelectTime, toSelectDate, toSelectTime);
        try {
            endTimesArray = new JSONArray(EndDateTimeArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            jsonObj_.put("worktype", workVisitType);
            jsonObj_.put("c_empId", model.getIncomplete_data().get_id().get$oid());
            jsonObj_.put("sc_empId", "");
            jsonObj_.put("comp_id", "");
            jsonObj_.put("work_scope", scopeOfWorkTextArea);
            jsonObj_.put("sc_name", "");
            jsonObj_.put("sc_email", "");
            jsonObj_.put("sc_mobileData", "");
            jsonObj_.put("sc_mobile", "");
            jsonObj_.put("work_loc", placeOfWorkVisitType);
            jsonObj_.put("d_start", fromDateTimeStamp);
            jsonObj_.put("d_end", toDateTimeStamp);
            jsonObj_.put("t_start", fromSelectTime);
            jsonObj_.put("t_end", toSelectTime);
            jsonObj_.put("sc_perwork", "");
            jsonObj_.put("starts", startTimesArray);
            jsonObj_.put("ends", endTimesArray);
            jsonObj_.put("formtype", "insert");
            jsonObj_.put("c_workpermit", "");
            jsonObj_.put("sc_status", true);
            jsonObj_.put("contractorData", contractorData_);
            jsonObj_.put("contractorsData", contractors_Data);
            jsonObj_.put("companyName", companyName);
            jsonObj_.put("subcontractorsData", sub_ContractorsData);
            jsonObj_.put("approver_statistics", approveStatistics);
            jsonObj_.put("assign_departments", assignDepartments);
            jsonObj_.put("l_id", locationIndexPosition);
            jsonObj_.put("workinghours", workingHours);
            jsonObj_.put("twentyfourhours", twentyFourHours);

            JsonParser jsonParser = new JsonParser();
            gsonObject = (JsonObject) jsonParser.parse(jsonObj_.toString());
            System.out.println("createjsongsonObject::" + gsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return gsonObject;

    }

    //Spinners List Set
    private void SpinnersListApis() {

        //Working Hours Only
        apiViewModel.getworkingdays(getApplicationContext(), compId);
        apiViewModel.getworkingdays_response().observe(this, dModel -> {
            WorkingHRWorkingList = dModel.getItems().get(0).getWorkingdays();
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
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

        });

        //Work/visit Type
        apiViewModel.getworktypes(getApplicationContext(), compId);
        apiViewModel.getworktypes_response().observe(this, dModel -> {
            ArrayList<WorkVisitTypeList> documentsList = dModel.getItems();
            ArrayList<String> visitTypeList = new ArrayList<>();

            // Prepare list for spinner
            for (WorkVisitTypeList doc : documentsList) {
                if (doc.getActive()) {
                    visitTypeList.add(doc.getName());
                }
            }

            //setAdapter
            ArrayAdapter<String> selectIDAdapter = new ArrayAdapter<>(
                    this, R.layout.custom_spinner_item, visitTypeList
            );
            selectIDAdapter.setDropDownViewResource(R.layout.custom_spinner_item);
            spinnerVisitType.setAdapter(selectIDAdapter);
            // Select Spinner Listener
            spinnerVisitType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String selectedName = (String) parent.getItemAtPosition(position);

                    for (WorkVisitTypeList item : documentsList) {
                        if (item.getActive() && item.getName().equals(selectedName)) {
                            workVisitType = item.get_id().get$oid();
                            break;
                        }
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

        });

        //Place of work/visit Type
        apiViewModel.getworklocation(getApplicationContext(), compId);
        apiViewModel.getworklocation_response().observe(this, dModel -> {
            ArrayList<WorkVisitTypeList> documentsList = dModel.getItems();
            ArrayList<String> PlaceOfWorkVisitTypeList = new ArrayList<>();

            // Prepare list for spinner
            for (WorkVisitTypeList doc : documentsList) {
                if (doc.getActive()) {
                    PlaceOfWorkVisitTypeList.add(doc.getName());
                }
            }
            PlaceOfWorkVisitTypeList.add("Others");

            //setAdapter
            ArrayAdapter<String> selectIDAdapter = new ArrayAdapter<>(
                    this, R.layout.custom_spinner_item, PlaceOfWorkVisitTypeList
            );
            selectIDAdapter.setDropDownViewResource(R.layout.custom_spinner_item);
            spinnerPlaceOfWork.setAdapter(selectIDAdapter);
            // Select Spinner Listener
            spinnerPlaceOfWork.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    placeOfWorkVisitType = parent.getItemAtPosition(position).toString();
                    if (placeOfWorkVisitType.equalsIgnoreCase("Others")) {
                        StatusEditPlaceOfWork = "true";
                        EditPlaceOfWork.setVisibility(View.VISIBLE);
                    } else {
                        StatusEditPlaceOfWork = "";
                        EditPlaceOfWork.setVisibility(GONE);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

        });

        //Scope of work text area
        apiViewModel.getworkpurposes(getApplicationContext(), compId);
        apiViewModel.getworkpurposes_response().observe(this, dModel -> {
            ArrayList<WorkVisitTypeList> documentsList = dModel.getItems();
            ArrayList<String> ScopeOfWorkList = new ArrayList<>();

            // Prepare list for spinner
            for (WorkVisitTypeList doc : documentsList) {
                if (doc.getActive()) {
                    ScopeOfWorkList.add(doc.getName());
                }
            }
            ScopeOfWorkList.add("Others");

            //setAdapter
            ArrayAdapter<String> selectIDAdapter = new ArrayAdapter<>(
                    this, R.layout.custom_spinner_item, ScopeOfWorkList
            );
            selectIDAdapter.setDropDownViewResource(R.layout.custom_spinner_item);
            spinnerSelectScopeOfWork.setAdapter(selectIDAdapter);
            // Select Spinner Listener
            spinnerSelectScopeOfWork.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    scopeOfWorkTextArea = parent.getItemAtPosition(position).toString();
                    if (scopeOfWorkTextArea.equalsIgnoreCase("Others")) {
                        StatusEditScopeOfWork = "true";
                        EditScopeOfWork.setVisibility(View.VISIBLE);
                    } else {
                        StatusEditScopeOfWork = "";
                        EditScopeOfWork.setVisibility(GONE);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

        });

    }

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

                    //currentDate
                    SimpleDateFormat sdf = new SimpleDateFormat("d/M/yyyy", Locale.getDefault());
                    String currentDate = sdf.format(Calendar.getInstance().getTime());
                    if (currentDate.equalsIgnoreCase(fromSelectDate)) {
                        StartAndEndTimeList(true);
                    } else {
                        StartAndEndTimeList(false);
                    }

                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        // ✅ Disable past dates
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

        datePickerDialog.show();
    }

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

    //Time List start and End
    private void StartAndEndTimeList(boolean isToday) {
        allTimeList.clear();
        filteredStartList.clear();

        // Fill time list from 00:00 to 23:45
        for (int hour = 0; hour < 24; hour++) {
            for (int minute = 0; minute < 60; minute += 15) {
                String time = String.format("%02d:%02d", hour, minute);
                allTimeList.add(time);
            }
        }

        // ✅ Filter past time slots if today
        if (isToday) {
            Calendar now = Calendar.getInstance();
            int currentHour = now.get(Calendar.HOUR_OF_DAY);
            int currentMinute = now.get(Calendar.MINUTE);

            // Round up to next 15 min interval
            int roundedMinute = ((currentMinute + 14) / 15) * 15;
            if (roundedMinute == 60) {
                currentHour += 1;
                roundedMinute = 0;
            }

            if (currentHour >= 24) {
                // No time available
                filteredStartList.clear();
            } else {
                String roundedTime = String.format("%02d:%02d", currentHour, roundedMinute);

                // Remove time slots before current time
                boolean found = false;
                List<String> tempList = new ArrayList<>();
                for (String time : allTimeList) {
                    if (time.compareTo(roundedTime) >= 0) {
                        tempList.add(time);
                        found = true;
                    }
                }

                filteredStartList = tempList;

                if (!found) {
                    filteredStartList.clear(); // No valid times left
                }
            }
        } else {
            filteredStartList = allTimeList;
        }

        // Set Start Time Spinner
        ArrayAdapter<String> startAdapter = new ArrayAdapter<>(
                WorkPermitFormActivity.this, R.layout.custom_spinner_item, filteredStartList
        );
        startAdapter.setDropDownViewResource(R.layout.custom_spinner_item);
        spinnerStartTime.setAdapter(startAdapter);

        // Auto-select first available time (if any)
        if (!filteredStartList.isEmpty()) {
            spinnerStartTime.setSelection(0);
        }

        // Set listener for filtering End Time
        spinnerStartTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                fromSelectTime = filteredStartList.get(position);

                // End time = any slot after selected Start Time
                List<String> filteredEndTimes = new ArrayList<>();
                for (int i = position + 1; i < filteredStartList.size(); i++) {
                    filteredEndTimes.add(filteredStartList.get(i));
                }

                if (filteredEndTimes.isEmpty()) {
                    filteredEndTimes.add("23:59");
                }

                ArrayAdapter<String> endAdapter = new ArrayAdapter<>(
                        WorkPermitFormActivity.this, R.layout.custom_spinner_item, filteredEndTimes
                );
                endAdapter.setDropDownViewResource(R.layout.custom_spinner_item);
                spinnerEndTime.setAdapter(endAdapter);
                spinnerEndTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        toSelectTime = filteredStartList.get(position);

                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    //Contractor
    private void AddContractorPopUp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.add_contractor_popup_layout, null);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        dialog.show();

        // Apply transparent background to allow rounded corners to show
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        ImageView imgClose = dialogView.findViewById(R.id.imgClose);
        EditText WorkPermitEmail = dialogView.findViewById(R.id.WorkPermitEmail);
        CountryCodePicker workPermitCCP = dialogView.findViewById(R.id.workPermitCCP);
        EditText workPermitMobile = dialogView.findViewById(R.id.workPermitMobile);
        EditText WorkPermitCompanyName = dialogView.findViewById(R.id.WorkPermitCompanyName);
        EditText WorkPermitName = dialogView.findViewById(R.id.WorkPermitName);
        Spinner spinnerSelectID = dialogView.findViewById(R.id.spinnerSelectID);
        Spinner spinnerNationality = dialogView.findViewById(R.id.spinnerNationality);
        EditText WorkPermitIDNumber = dialogView.findViewById(R.id.WorkPermitIDNumber);
        Button btnAddMore = dialogView.findViewById(R.id.btnAddMore);

        workPermitCCP.setDefaultCountryUsingPhoneCode(Integer.parseInt("+966"));
        workPermitCCP.setCountryForNameCode("+966");


        if (!subContractorList.isEmpty()){
            WorkPermitCompanyName.setText(subContractorList.get(0).companyName);
            WorkPermitCompanyName.setClickable(false);
            WorkPermitCompanyName.setFocusable(false);
            WorkPermitCompanyName.setFocusableInTouchMode(false);
        }

        if (!contractorList.isEmpty()){
            WorkPermitCompanyName.setText(contractorList.get(0).companyName);
            WorkPermitCompanyName.setClickable(false);
            WorkPermitCompanyName.setFocusable(false);
            WorkPermitCompanyName.setFocusableInTouchMode(false);
        }

        setDataForContractor(spinnerSelectID, spinnerNationality);

        btnAddMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = WorkPermitName.getText().toString();
                String companyName = WorkPermitCompanyName.getText().toString();
                String idType = ContractorPosition;
                String id_name = ContractorSelectId;
                String nationality = ContractorNationality;
                String idNumber = WorkPermitIDNumber.getText().toString();
                String email = WorkPermitEmail.getText().toString();
                String mobile = workPermitCCP.getSelectedCountryCode() + workPermitMobile.getText().toString();


                if (email.equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Enter Email", Toast.LENGTH_SHORT).show();
                } else if (workPermitMobile.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Enter Mobile Number", Toast.LENGTH_SHORT).show();
                } else if (companyName.equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Enter Company Name", Toast.LENGTH_SHORT).show();
                } else if (name.equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Enter Name", Toast.LENGTH_SHORT).show();
                } else if (id_name.equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Select ID", Toast.LENGTH_SHORT).show();
                } else if (nationality.equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Select Nationality", Toast.LENGTH_SHORT).show();
                } else if (idNumber.equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Enter ID Number", Toast.LENGTH_SHORT).show();
                } else {
                    String number = workPermitMobile.getText().toString();
                    String internationalNumber = workPermitCCP.getSelectedCountryCodeWithPlus() + workPermitMobile.getText().toString();
                    String nationalNumber = "0"+workPermitMobile.getText().toString();
                    String e164Number = workPermitCCP.getSelectedCountryCodeWithPlus() + workPermitMobile.getText().toString().trim();
                    String countryCode = workPermitCCP.getSelectedCountryNameCode();
                    String dialCode = workPermitCCP.getSelectedCountryCodeWithPlus();

                    // Build MobileData object
                    MobileData mobileData = new MobileData(
                            number,
                            internationalNumber,
                            nationalNumber,
                            e164Number,
                            countryCode,
                            dialCode
                    );

                    // Optional
                    String performing_work = "";
                    boolean common_nation = false;
                    boolean disabledStatus = false;

                    // Create SubContractor object
                    Contractor scontractor = new Contractor(
                            name,
                            "",
                            companyName,
                            idType,
                            id_name,
                            nationality,
                            idNumber,
                            email,
                            "",
                            mobileData,
                            performing_work,
                            common_nation,
                            disabledStatus
                    );

                    //list
                    contractorList.add(scontractor);
                    setRecyclerContractorList();
                    dialog.dismiss();
                }

            }
        });

        imgClose.setOnClickListener(v -> dialog.dismiss());

    }

    private void setDataForContractor(Spinner spinnerSelectID, Spinner spinnerNationality) {
        //api
        apiViewModel.getdocuments(getApplicationContext());
        apiViewModel.getResponseforSelectedId_list().observe(this, d_model -> {

            ArrayList<Getdocuments> documentsList = d_model.getItems();
            ArrayList<Getdocuments> activeDocuments = new ArrayList<>();
            ArrayList<String> selectIDNames = new ArrayList<>();
            ArrayList<String> nationalityNames = new ArrayList<>();

            // Prepare list for spinner
            for (Getdocuments doc : documentsList) {
                if (doc.getActive()) {
                    activeDocuments.add(doc); // keep full object
                    selectIDNames.add(doc.getName());
                }
            }

            // Adapter for Select ID
            ArrayAdapter<String> selectIDAdapter = new ArrayAdapter<>(
                    this, R.layout.custom_spinner_item, selectIDNames
            );
            selectIDAdapter.setDropDownViewResource(R.layout.custom_spinner_item);
            spinnerSelectID.setAdapter(selectIDAdapter);

            // Adapter for Nationality
            ArrayAdapter<String> nationalityAdapter = new ArrayAdapter<>(
                    this, R.layout.custom_spinner_item, nationalityNames
            );
            nationalityAdapter.setDropDownViewResource(R.layout.custom_spinner_item);
            spinnerNationality.setAdapter(nationalityAdapter);
            // Spinner Listener
            spinnerNationality.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    ContractorNationality = parent.getItemAtPosition(position).toString();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

            // Spinner Listener
            spinnerSelectID.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    ContractorNationality = "";
                    ContractorPosition = position + "";
                    Getdocuments selectedDoc = activeDocuments.get(position);
                    String selectedDocId = selectedDoc.get_id().get$oid();
                    ContractorSelectId = selectedDoc.getName();

                    // Update nationality spinner
                    nationalityNames.clear();
                    for (Getnationality nationality : selectedDoc.getNationlities()) {
                        if (nationality.getActive()) {
                            nationalityNames.add(nationality.getName());
                        }
                    }

                    nationalityAdapter.notifyDataSetChanged();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

        });
    }

    private void setRecyclerContractorList() {
        RecyclerView recyclerView = findViewById(R.id.recyclerviewContractor);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ContractorAdapter adapter = new ContractorAdapter(contractorList);
        recyclerView.setAdapter(adapter);

        if (contractorList.isEmpty()) {
            linearC.setVisibility(GONE);
            imgContractorInfo.setVisibility(GONE);
        } else {
            linearC.setVisibility(View.VISIBLE);
            imgContractorInfo.setVisibility(View.VISIBLE);
        }
    }

    //    Sub Contractor
    private void AddSubContractorPopUp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.add_sub_contractor_popup_layout, null);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        dialog.show();

        // Apply transparent background to allow rounded corners to show
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        ImageView imgClose = dialogView.findViewById(R.id.imgClose);
        EditText WorkPermitSubEmail = dialogView.findViewById(R.id.WorkPermitSubEmail);
        CountryCodePicker MaterialPermitCCP = dialogView.findViewById(R.id.MaterialPermitCCP);
        EditText MaterialPermitMobile = dialogView.findViewById(R.id.MaterialPermitMobile);
        EditText WorkPermitSubCompanyName = dialogView.findViewById(R.id.WorkPermitSubCompanyName);
        EditText WorkPermitSubName = dialogView.findViewById(R.id.WorkPermitSubName);
        Spinner spinnerWorkPermitSubSelectID = dialogView.findViewById(R.id.spinnerWorkPermitSubSelectID);
        Spinner spinnerMaterialNationality = dialogView.findViewById(R.id.spinnerMaterialNationality);
        EditText WorkPermitSubIDNumber = dialogView.findViewById(R.id.WorkPermitSubIDNumber);
        Button btnAddMore = dialogView.findViewById(R.id.btnAddMore);

        MaterialPermitCCP.setDefaultCountryUsingPhoneCode(Integer.parseInt("+966"));
        MaterialPermitCCP.setCountryForNameCode("+966");

        if (!contractorList.isEmpty()){
            WorkPermitSubCompanyName.setText(contractorList.get(0).companyName);
            WorkPermitSubCompanyName.setClickable(false);
            WorkPermitSubCompanyName.setFocusable(false);
            WorkPermitSubCompanyName.setFocusableInTouchMode(false);
        }

        if (!subContractorList.isEmpty()){
            WorkPermitSubCompanyName.setText(subContractorList.get(0).companyName);
            WorkPermitSubCompanyName.setClickable(false);
            WorkPermitSubCompanyName.setFocusable(false);
            WorkPermitSubCompanyName.setFocusableInTouchMode(false);
        }

        setDataForSubContractor(spinnerWorkPermitSubSelectID, spinnerMaterialNationality);

        btnAddMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = WorkPermitSubName.getText().toString();
                String companyName = WorkPermitSubCompanyName.getText().toString();
                String idType = SubContractorPosition;
                String id_name = SubContractorSelectId;
                String nationality = SubContractorNationality;
                String idNumber = WorkPermitSubIDNumber.getText().toString();
                String email = WorkPermitSubEmail.getText().toString();
                String mobile = MaterialPermitCCP.getSelectedCountryCode() + MaterialPermitMobile.getText().toString();


                if (email.equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Enter Email", Toast.LENGTH_SHORT).show();
                } else if (MaterialPermitMobile.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Enter Mobile Number", Toast.LENGTH_SHORT).show();
                } else if (companyName.equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Enter Company Name", Toast.LENGTH_SHORT).show();
                } else if (name.equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Enter Name", Toast.LENGTH_SHORT).show();
                } else if (id_name.equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Select ID", Toast.LENGTH_SHORT).show();
                } else if (nationality.equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Select Nationality", Toast.LENGTH_SHORT).show();
                } else if (idNumber.equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Enter ID Number", Toast.LENGTH_SHORT).show();
                } else {
                    String number = MaterialPermitMobile.getText().toString();
                    String internationalNumber = MaterialPermitCCP.getSelectedCountryCodeWithPlus() + MaterialPermitMobile.getText().toString();
                    String nationalNumber = "0"+MaterialPermitMobile.getText().toString();
                    String e164Number = MaterialPermitCCP.getSelectedCountryCodeWithPlus() + MaterialPermitMobile.getText().toString().trim();
                    String countryCode = MaterialPermitCCP.getSelectedCountryNameCode();
                    String dialCode = MaterialPermitCCP.getSelectedCountryCodeWithPlus();

                    // Build MobileData object
                    MobileData mobileData = new MobileData(
                            number,
                            internationalNumber,
                            nationalNumber,
                            e164Number,
                            countryCode,
                            dialCode
                    );

                    // Optional
                    String performing_work = "";
                    boolean common_nation = false;
                    boolean disabledStatus = false;

                    // Create SubContractor object
                    SubContractor scontractor = new SubContractor(
                            name,
                            "",
                            companyName,
                            idType,
                            id_name,
                            nationality,
                            idNumber,
                            email,
                            "",
                            mobileData,
                            performing_work,
                            common_nation,
                            disabledStatus
                    );

                    //list
                    subContractorList.add(scontractor);
                    dialog.dismiss();
                    setRecyclerSubList();
                }

            }
        });


        imgClose.setOnClickListener(v -> dialog.dismiss());
    }

    private void setDataForSubContractor(Spinner spinnerWorkPermitSubSelectID, Spinner spinnerMaterialNationality) {
        //api Sub Contractor
        apiViewModel.getdocuments(getApplicationContext());
        apiViewModel.getResponseforSelectedId_list().observe(this, d_model -> {

            ArrayList<Getdocuments> documentsList = d_model.getItems();
            ArrayList<Getdocuments> activeDocuments = new ArrayList<>();
            ArrayList<String> selectIDNames = new ArrayList<>();
            ArrayList<String> nationalityNamesList = new ArrayList<>();

            // Prepare list for spinner
            for (Getdocuments doc : documentsList) {
                if (doc.getActive()) {
                    activeDocuments.add(doc); // keep full object
                    selectIDNames.add(doc.getName());
                }
            }

            // Adapter for Select ID
            ArrayAdapter<String> selectIDAdapter = new ArrayAdapter<>(
                    this, R.layout.custom_spinner_item, selectIDNames
            );
            selectIDAdapter.setDropDownViewResource(R.layout.custom_spinner_item);
            spinnerWorkPermitSubSelectID.setAdapter(selectIDAdapter);

            // Adapter for Nationality
            ArrayAdapter<String> nationalityAdapter = new ArrayAdapter<>(
                    this, R.layout.custom_spinner_item, nationalityNamesList
            );
            nationalityAdapter.setDropDownViewResource(R.layout.custom_spinner_item);
            spinnerMaterialNationality.setAdapter(nationalityAdapter);
            // Spinner Listener
            spinnerMaterialNationality.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    SubContractorNationality = parent.getItemAtPosition(position).toString();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

            // SelectID Spinner Listener
            spinnerWorkPermitSubSelectID.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    SubContractorNationality = "";
                    SubContractorPosition = position + "";
                    Getdocuments selectedDoc = activeDocuments.get(position);
                    String selectedDocId = selectedDoc.get_id().get$oid();
                    SubContractorSelectId = selectedDoc.getName();
                    // Update nationality spinner
                    nationalityNamesList.clear();
                    for (Getnationality nationality : selectedDoc.getNationlities()) {
                        if (nationality.getActive()) {
                            nationalityNamesList.add(nationality.getName());
                        }
                    }
                    nationalityAdapter.notifyDataSetChanged();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        });
    }

    private void setRecyclerSubList() {

        RecyclerView recyclerView = findViewById(R.id.recyclerviewSubContractor);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        SubContractorAdapter adapter = new SubContractorAdapter(subContractorList);
        recyclerView.setAdapter(adapter);

        if (subContractorList.isEmpty()) {
            linearS.setVisibility(GONE);
            imgSubInfo.setVisibility(GONE);
        } else {
            linearS.setVisibility(View.VISIBLE);
            imgSubInfo.setVisibility(View.VISIBLE);
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