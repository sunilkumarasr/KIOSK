package com.provizit.kioskcheckin.activities.Meetings;

import static android.view.View.GONE;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.provizit.kioskcheckin.activities.YourRequestSentActivity;
import com.provizit.kioskcheckin.config.ConnectionReceiver;
import com.provizit.kioskcheckin.config.InterNetConnectivityCheck;
import com.provizit.kioskcheckin.mvvm.ApiViewModel;
import com.provizit.kioskcheckin.R;
import com.provizit.kioskcheckin.services.Conversions;
import com.provizit.kioskcheckin.api.DataManger;
import com.provizit.kioskcheckin.services.GetCVisitorDetailsModel;
import com.provizit.kioskcheckin.services.GetNdaActiveDetailsModel;
import com.provizit.kioskcheckin.services.VisitorActionModel;
import com.provizit.kioskcheckin.utilities.Belongings;
import com.provizit.kioskcheckin.config.Preferences;
import com.provizit.kioskcheckin.utilities.TotalCounts;
import com.provizit.kioskcheckin.utilities.Vehicles;
import com.provizit.kioskcheckin.utilities.VisitorFormDetailsOtherArray;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MeetingRequestDetailsActivity2 extends AppCompatActivity implements View.OnClickListener {
    //internet connection
    BroadcastReceiver broadcastReceiver;
    RelativeLayout relative_internet;
    RelativeLayout relative_ui;
    ImageView company_logo;
    Button proceed;
    ImageView emp_img, visitor_img, back_image;
    TextView txt_host, meeting_details, name, label_subject, mobile, email, organization, designation, emp_name, emp_name_2, emp_department, emp_designation, emp_subject, meeting_room, meeting_st, txt_time, static_time, txt_department, host_leble;
    Uri ImageUri;
    LinearLayout linear_layout, linear_host_name, linear_department, linear_date_time, linear_with;
    GetCVisitorDetailsModel model;
    String uri = "", hid = "", hiid = "", mid = "", host = "", hierarchyname = "";
    String nda_id = "";
    String filename = "";
    String encodedString = "";
    Float meeting_status;
    String accept_Data = "false";
    String badge_Data = "false";
    String tvisitor = "";
    String badge = "";
    ApiViewModel apiViewModel;
    GetNdaActiveDetailsModel ndamodel;
    String Comp_id = "";
    String pic_Data = "false";

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_request_details2);
        Intent iin = getIntent();
        Bundle b = iin.getExtras();
        try {
            if (b != null) {
                host = iin.getStringExtra("host");
                hid = iin.getStringExtra("hid");
                hiid = iin.getStringExtra("hiid");
//                System.out.println("hierarchy_id::"+hid);
//                System.out.println("hierarchy_id::"+hiid);
                tvisitor = iin.getStringExtra("tvisitor");
                hierarchyname = iin.getStringExtra("hierarchyname");
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


        Comp_id = Preferences.loadStringValue(getApplicationContext(), Preferences.Comp_id, "");

        //shared Preferences
        pic_Data = Preferences.loadStringValue(getApplicationContext(), Preferences.pic_Data, "");

        nda_id = Preferences.loadStringValue(getApplicationContext(), Preferences.nda_id, "");
        accept_Data = Preferences.loadStringValue(getApplicationContext(), Preferences.accept_Data, "");
        badge_Data = Preferences.loadStringValue(getApplicationContext(), Preferences.badge_Data, "");

        proceed = findViewById(R.id.proceed);
        emp_img = findViewById(R.id.emp_img);
        visitor_img = findViewById(R.id.visitor_img);
        txt_host = findViewById(R.id.txt_host);
        meeting_details = findViewById(R.id.meeting_details);
        linear_host_name = findViewById(R.id.linear_host_name);
        name = findViewById(R.id.name);
        mobile = findViewById(R.id.mobile);
        ViewCompat.setLayoutDirection(mobile, ViewCompat.LAYOUT_DIRECTION_LTR);
        email = findViewById(R.id.email);
        organization = findViewById(R.id.organization);
        designation = findViewById(R.id.designation);
        emp_name = findViewById(R.id.emp_name);
        emp_name_2 = findViewById(R.id.emp_name_2);
        emp_department = findViewById(R.id.emp_department);
        emp_designation = findViewById(R.id.emp_designation);
        emp_subject = findViewById(R.id.emp_subject);
        meeting_room = findViewById(R.id.meeting_room);
        label_subject = findViewById(R.id.label_subject);
        meeting_st = findViewById(R.id.meeting_st);
        txt_time = findViewById(R.id.txt_time);
        static_time = findViewById(R.id.static_time);
        linear_layout = findViewById(R.id.linear_layout);
        linear_department = findViewById(R.id.linear_department);
        linear_date_time = findViewById(R.id.linear_date_time);
        linear_with = findViewById(R.id.linear_with);
        txt_department = findViewById(R.id.txt_department);
        host_leble = findViewById(R.id.host_leble);
        company_logo = findViewById(R.id.company_logo);
        back_image = findViewById(R.id.back_image);

        apiViewModel = new ViewModelProvider(MeetingRequestDetailsActivity2.this).get(ApiViewModel.class);

        apiViewModel.getndafdetials(getApplicationContext(), "id", "active");

        if (uri.equalsIgnoreCase("")) {
            emp_img.setImageResource(R.drawable.ic_user_white);
        } else {
            ImageUri = Uri.parse(uri);
            image_set();
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

        //company logo
        String c_Logo = Preferences.loadStringValue(getApplicationContext(), Preferences.company_Logo, "");
        if (c_Logo.equalsIgnoreCase("")) {
        } else {
            Glide.with(MeetingRequestDetailsActivity2.this).load(c_Logo)
                    .into(company_logo);
        }

        name.setText(model.getIncomplete_data().getName());
        mobile.setText(model.getIncomplete_data().getMobile());
        email.setText(model.getIncomplete_data().getEmail());

        if (model.getIncomplete_data().getCompany().equalsIgnoreCase("") || model.getIncomplete_data().getCompany() == null) {
            organization.setVisibility(View.GONE);
        } else {
            organization.setText(model.getIncomplete_data().getCompany());
        }
        designation.setText(model.getIncomplete_data().getDesignation());
        meeting_status = model.getItems().getMeetingStatus();
//        System.out.println("emp_pic::"+DataManger.IMAGE_URL + "/uploads/" + Comp_id + "/" + model.getItems().getEmployee().getPic().get(model.getItems().getEmployee().getPic().size() - 1));
        if (model.getItems().getEmployee().getPic() != null && model.getItems().getEmployee().getPic().size() != 0) {
            if (model.getItems().getEmployee().getPic().get(model.getItems().getEmployee().getPic().size() - 1).equalsIgnoreCase("") || model.getItems().getEmployee().getPic().get(model.getItems().getEmployee().getPic().size() - 1) == null) {
                visitor_img.setImageResource(R.drawable.ic_user_white);
            } else {
                Glide.with(MeetingRequestDetailsActivity2.this).load(DataManger.IMAGE_URL + "/uploads/" + Comp_id + "/" + model.getItems().getEmployee().getPic().get(model.getItems().getEmployee().getPic().size() - 1)).into(visitor_img);
            }
        } else {
            visitor_img.setImageResource(R.drawable.ic_user_white);
        }

        if (meeting_status == 1) {
            meeting_details.setText(getResources().getString(R.string.meetingdetails));
            linear_host_name.setVisibility(View.VISIBLE);
            linear_department.setVisibility(View.GONE);
            linear_layout.setVisibility(View.VISIBLE);
            linear_date_time.setVisibility(View.VISIBLE);
            linear_with.setVisibility(View.VISIBLE);
            mid = model.getItems().get_id().get$oid();
            host = model.getItems().getEmployee().get_id().get$oid();
            hid = model.getItems().getEmployee().getHierarchy_id();
            hiid = model.getItems().getEmployee().getHierarchy_indexid();
//            System.out.println("hierarchy_id::"+hid);
//            System.out.println("hierarchy_id::"+hiid);
            hierarchyname = model.getItems().getEmployee().getDepartment();
            emp_name.setText(model.getItems().getMeetingrooms().getName());
            emp_name_2.setText(model.getItems().getEmployee().getName());
            if (model.getItems().getEmployee().getDepartment().equalsIgnoreCase("") || model.getItems().getEmployee().getDepartment() == null) {
                txt_department.setVisibility(View.GONE);
                emp_department.setVisibility(GONE);
            } else {
                txt_department.setText(hierarchyname);
                emp_department.setText(hierarchyname);
                host_leble.setVisibility(View.VISIBLE);
            }

            txt_host.setText(model.getItems().getEmployee().getName());
            emp_designation.setText(model.getItems().getEmployee().getDesignation());
            emp_subject.setText(model.getItems().getSubject());
            label_subject.setText(getResources().getString(R.string.Subject));
            meeting_room.setText(model.getItems().getMeetingrooms().getName());
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("EE dd MMMM yyyy");
                LocalDateTime now = LocalDateTime.now();
                meeting_st.setText(dtf.format(now));
            }
            txt_time.setText(Conversions.millitotime((model.getItems().getStart() + Conversions.timezone()) * 1000, false) + " - " + Conversions.millitotime((model.getItems().getEnd() + 1 + Conversions.timezone()) * 1000, false));

        } else {
            meeting_details.setText(getResources().getString(R.string.CheckInDetails));
            linear_department.setVisibility(View.VISIBLE);
            linear_layout.setVisibility(View.GONE);
            linear_date_time.setVisibility(View.GONE);
            linear_with.setVisibility(View.GONE);

            if (model.getItems().getEmployee().getName().equalsIgnoreCase("") || model.getItems().getEmployee().getName() == null) {
                linear_host_name.setVisibility(View.GONE);
            } else {
                linear_host_name.setVisibility(View.VISIBLE);
                txt_host.setText(model.getItems().getEmployee().getName());
            }

            emp_name.setText("");
            emp_name_2.setText(model.getItems().getEmployee().getName());
            txt_department.setText(model.getItems().getEmployee().getDepartment());
            emp_designation.setText(model.getItems().getEmployee().getDesignation());
            emp_subject.setText(model.getItems().getSubject());
            meeting_room.setText("");
            linear_layout.setVisibility(View.GONE);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("EE dd MMMM yyyy - hh:mm a");
                LocalDateTime now = LocalDateTime.now();
                meeting_st.setText(dtf.format(now));
                txt_time.setText("");
                static_time.setText("");
            }

        }

        apiViewModel.getResponseforNdaActiveDetails().observe(this, model -> {
            ndamodel = model;
            if (ndamodel.getResult() == 201) {
                nda_id = "";
            } else {
                nda_id = ndamodel.getItems().get_id().get$oid();
            }
        });
        apiViewModel.getResponseforCVisitor().observe(this, ndamodel -> {
            try {
                if (ndamodel.getResult() == 200) {
                    Intent intent;
                    float meeting_status = model.getItems().getMeetingStatus();
                    if (meeting_status == 1) {
                        intent = new Intent(getApplicationContext(), MeetingDetailsActivity.class);
                    }else{
                        intent = new Intent(getApplicationContext(), MeetingRequestFormActivity.class);
                    }
                    intent.putExtra("model_key", ndamodel);
                    startActivity(intent);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        });

        //capture
        JsonObject gsonObject = new JsonObject();
        JSONObject jsonObj_ = new JSONObject();
        try {
            jsonObj_.put("cid", Comp_id);
            jsonObj_.put("key", filename);
            jsonObj_.put("img", encodedString);
            JsonParser jsonParser = new JsonParser();
            gsonObject = (JsonObject) jsonParser.parse(jsonObj_.toString());
            qrindex(gsonObject);
            progressDialog = new ProgressDialog(MeetingRequestDetailsActivity2.this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
        }catch (Exception e) {
            Log.e(TAG, "Error processing vehicles: ", e);  // Proper logging
        }

        proceed.setOnClickListener(this);
        back_image.setOnClickListener(this);
    }

    //disable auto click action after scann
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_ENTER) {
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
                   String emp_id = Preferences.loadStringValue(getApplicationContext(), Preferences.email_id, "");
                   String location_id = Preferences.loadStringValue(getApplicationContext(), Preferences.location_id, "");
                   String val_email =  model.getIncomplete_data().getEmail();
                   String val_mobile =  model.getIncomplete_data().getMobile();
                    if (!val_email.equals("")) {
                        apiViewModel.getcvisitordetails(getApplicationContext(), "", emp_id, val_email, location_id);
                    } else {
                        apiViewModel.getcvisitordetails(getApplicationContext(), "", emp_id, val_mobile, location_id);
                    }
                    progressDialog = new ProgressDialog(MeetingRequestDetailsActivity2.this);
                    progressDialog.setCancelable(false);
                    progressDialog.setMessage("Loading...");
                    progressDialog.show();
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
        proceed.setFocusable(false);
        proceed.setFocusableInTouchMode(false);
        back_image.setFocusable(false);
        back_image.setFocusableInTouchMode(false);

        runthred();
    }

    private void runthred() {
        final Handler handler = new Handler();
        handler.postDelayed(() -> {

            finish();
            overridePendingTransition(0, 0);
            startActivity(getIntent());
            overridePendingTransition(0, 0);

            proceed.setFocusable(true);
            proceed.setFocusableInTouchMode(true);
            back_image.setFocusable(true);
            back_image.setFocusableInTouchMode(true);

        }, 500);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.proceed:
                AnimationSet animation1 = Conversions.animation();
                view.startAnimation(animation1);
                JSONObject jsonObj_ = new JSONObject();
                ArrayList<VisitorFormDetailsOtherArray> others = new ArrayList<>();
                VisitorFormDetailsOtherArray other1 = new VisitorFormDetailsOtherArray();
                other1.setData(model.getIncomplete_data().getDesignation());
                other1.setLabel("Designation");
                other1.setStatus(true);
                other1.setActive(true);
                other1.setDisabled(true);
                other1.setDepends(false);
                other1.setProfessional(true);
                other1.setModel("designation");
                VisitorFormDetailsOtherArray other2 = new VisitorFormDetailsOtherArray();
                other2.setData(model.getIncomplete_data().getCompany());
                other2.setLabel("Organization");
                other2.setStatus(true);
                other2.setActive(true);
                other2.setDisabled(true);
                other2.setDepends(false);
                other2.setProfessional(true);
                other2.setModel("company");
                VisitorFormDetailsOtherArray other3 = new VisitorFormDetailsOtherArray();
                other3.setData(model.getIncomplete_data().getNation());
                other3.setLabel("Nationality");
                other3.setStatus(true);
                other3.setActive(true);
                other3.setDisabled(true);
                other3.setDepends(false);
                other3.setProfessional(false);
                other3.setModel("nation");
                others.add(other1);
                others.add(other2);
                others.add(other3);
//                if (model.getIncomplete_data().getCompany().equalsIgnoreCase("") || model.getIncomplete_data().getCompany() == null) {
//                    Preferences.saveStringValue(getApplicationContext(), Preferences.badge, String.valueOf(Conversions.getNDigitRandomNumber(0)));
//                    System.out.println("badge_Data::"+1);
//                } else {
                if (badge_Data.equalsIgnoreCase("true")) {
                    // Generate a random four-digit code
                    badge = "" + Conversions.getNDigitRandomNumber(3);
                }
//                }

                JSONArray pic = new JSONArray();
                JSONArray livePic = new JSONArray();
                if (!filename.equalsIgnoreCase("")) {
                    livePic.put(filename);
                }
                if (model.getIncomplete_data().getPic().size() == 0) {
                    ArrayList picArray = new ArrayList<>();
                    if (!filename.equalsIgnoreCase("")) {
                        picArray.add(filename);
                        pic.put(filename);
                    }
                } else {
                    ArrayList<String> pics = model.getIncomplete_data().getPic();
                    try {
                        for (int i = 0; i < pics.size(); i++) {
                            pic.put(pics.get(i));
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Error processing vehicles: ", e);  // Proper logging
                    }
                    if (!filename.equalsIgnoreCase("")) {
                        pic.put(filename);
                    }
                }

                JSONArray vehicles1 = new JSONArray();
                JSONArray belongings1 = new JSONArray();
                JSONArray others1 = new JSONArray();
                ArrayList<Vehicles> vehicles = model.getIncomplete_data().getVehicles();
                ArrayList<Belongings> belongings = model.getIncomplete_data().getBelongings();
                try {
                    vehicles1 = new JSONArray();
                    for (int i = 0; i < vehicles.size(); i++) {
                        vehicles1.put(vehicles.get(i).getvehicles());
                    }
                }catch (Exception e) {
                    Log.e(TAG, "Error processing vehicles: ", e);  // Proper logging
                }
                try {
                    others1 = new JSONArray();
                    for (int i = 0; i < others.size(); i++) {
                        others1.put(others.get(i).getothers());
                    }
                }catch (Exception e) {
                    Log.e(TAG, "Error processing vehicles: ", e);  // Proper logging
                }
                try {
                    belongings1 = new JSONArray();
                    for (int i = 0; i < belongings.size(); i++) {
                        belongings1.put(belongings.get(i).getbelongings());
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Error processing vehicles: ", e);  // Proper logging
                }
                try {
                    jsonObj_.put("belongings", belongings1);
                    jsonObj_.put("dob", model.getIncomplete_data().getDob());
                    jsonObj_.put("comp_id", "");
                    jsonObj_.put("document", model.getIncomplete_data().getDocument());
                    jsonObj_.put("documents", new JSONArray());
                    jsonObj_.put("email", model.getIncomplete_data().getEmail());
                    jsonObj_.put("formtype", "edit");
                    jsonObj_.put("idnumber", model.getIncomplete_data().getIdnumber());
                    jsonObj_.put("mobile", model.getIncomplete_data().getMobile());
                    jsonObj_.put("mobilecode", model.getIncomplete_data().getMobilecode());
                    try {
                        if (model.getIncomplete_data().getMobiledata().getMobiledata() != null) {
                            jsonObj_.put("mobiledata", model.getIncomplete_data().getMobiledata().getMobiledata());
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Error processing vehicles: ", e);  // Proper logging
                    }
                    jsonObj_.put("mverify", model.getIncomplete_data().getMverify());
                    jsonObj_.put("name", model.getIncomplete_data().getName());
                    jsonObj_.put("other", others1);
                    jsonObj_.put("pic", pic);
                    jsonObj_.put("random", "4458");
                    jsonObj_.put("vehicles", vehicles1);
                    jsonObj_.put("verify", model.getIncomplete_data().getVerify());
                    jsonObj_.put("emp_id", Preferences.loadStringValue(getApplicationContext(), Preferences.email_id, ""));
                    jsonObj_.put("id", model.getIncomplete_data().get_id().get$oid());
                    jsonObj_.put("purpose", model.getItems().getSubject());
                    jsonObj_.put("livepic", livePic);
                    jsonObj_.put("vlocation", Preferences.loadStringValue(getApplicationContext(), Preferences.location_id, ""));
                    jsonObj_.put("hierarchy_id", hid);
                    jsonObj_.put("hierarchy_indexid", hiid);
                    jsonObj_.put("hierarchyname", hierarchyname);
                    jsonObj_.put("host", host);
                    jsonObj_.put("tvisitor", tvisitor);
                    jsonObj_.put("mid", mid);
                    jsonObj_.put("badge", badge);
                    jsonObj_.put("nda_terms", "true");
                    jsonObj_.put("nda_id", nda_id);
                    if (InterNetConnectivityCheck.isOnline(getApplicationContext())) {
                        actioncheckinout(jsonObj_);
                    } else {
                        DataManger.internetpopup(MeetingRequestDetailsActivity2.this);
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Error processing vehicles: ", e);  // Proper logging
                }
                break;
            case R.id.back_image:
                animation1 = Conversions.animation();
                view.startAnimation(animation1);
                String emp_id = Preferences.loadStringValue(getApplicationContext(), Preferences.email_id, "");
                String location_id = Preferences.loadStringValue(getApplicationContext(), Preferences.location_id, "");
                String val_email =  model.getIncomplete_data().getEmail();
                String val_mobile =  model.getIncomplete_data().getMobile();
                if (!val_email.equals("")) {
                    apiViewModel.getcvisitordetails(getApplicationContext(), "", emp_id, val_email, location_id);
                } else {
                    apiViewModel.getcvisitordetails(getApplicationContext(), "", emp_id, val_mobile, location_id);
                }
                progressDialog = new ProgressDialog(MeetingRequestDetailsActivity2.this);
                progressDialog.setCancelable(false);
                progressDialog.setMessage("Loading...");
                progressDialog.show();
                break;

        }
    }

    private void qrindex(JsonObject gsonObject) {
        DataManger dataManager = DataManger.getDataManager();
        dataManager.qrindex(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                progressDialog.dismiss();
                String model = response.body();
                try {
                    if (model != null) {
                        String statuscode = model;
                        String successcode = "200", failurecode = "401", not_verified = "404";
                        if (statuscode.equals(failurecode)) {

                        } else if (statuscode.equals(not_verified)) {

                        } else if (statuscode.equals(successcode)) {

                        }
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                progressDialog.dismiss();
            }
        }, MeetingRequestDetailsActivity2.this, gsonObject);
    }

    protected void registoreNetWorkBroadcast() {
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,
                new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    private void actioncheckinout(JSONObject jsonObj_) {
        DataManger dataManger = DataManger.getDataManager();
        model.setTotal_counts(new TotalCounts());
        if (accept_Data.equalsIgnoreCase("true") && model.getItems().getMeetingStatus() == 0) {
            model.getTotal_counts().setHstatus(Float.parseFloat("0"));
        } else {
            model.getTotal_counts().setHstatus(Float.parseFloat("1"));
        }
        Intent intent = new Intent(getApplicationContext(), YourRequestSentActivity.class);
        intent.putExtra("model_key", model);
        startActivity(intent);
        dataManger.guestentry(new Callback<VisitorActionModel>() {
            @Override
            public void onResponse(Call<VisitorActionModel> call, Response<VisitorActionModel> response) {
            }

            @Override
            public void onFailure(Call<VisitorActionModel> call, Throwable t) {

            }
        }, MeetingRequestDetailsActivity2.this, jsonObj_);
    }

    //all mobiles
    private void image_set() {
        Bitmap bmp = null;
        try {
            bmp = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), Uri.parse(uri));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        int w = bmp.getWidth();
        int h = bmp.getHeight();
        Matrix mtx = new Matrix();
        mtx.postRotate(90);
//        mtx.postScale(-1, 1, w/2,h/2);
        Bitmap rotatedBMP = Bitmap.createBitmap(bmp, 0, 0, w, h, mtx, true);
        BitmapDrawable bmd = new BitmapDrawable(rotatedBMP);
        emp_img.setImageDrawable(bmd);
    }

}