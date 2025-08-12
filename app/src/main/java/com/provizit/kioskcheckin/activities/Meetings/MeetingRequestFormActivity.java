package com.provizit.kioskcheckin.activities.Meetings;

import static android.view.View.GONE;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationSet;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.provizit.kioskcheckin.activities.YourRequestSentActivity;
import com.provizit.kioskcheckin.adapters.CustomAdapter;
import com.provizit.kioskcheckin.adapters.DepartmentsAdapter;
import com.provizit.kioskcheckin.adapters.PurposesAdapter;
import com.provizit.kioskcheckin.adapters.TvisitorAdapter;
import com.provizit.kioskcheckin.config.ConnectionReceiver;
import com.provizit.kioskcheckin.config.InterNetConnectivityCheck;
import com.provizit.kioskcheckin.config.ViewController;
import com.provizit.kioskcheckin.logins.VisitorLoginActivity;
import com.provizit.kioskcheckin.mvvm.ApiViewModel;
import com.provizit.kioskcheckin.R;
import com.provizit.kioskcheckin.services.Conversions;
import com.provizit.kioskcheckin.api.DataManger;
import com.provizit.kioskcheckin.services.GetCVisitorDetailsModel;
import com.provizit.kioskcheckin.services.VisitorActionModel;
import com.provizit.kioskcheckin.utilities.Belongings;
import com.provizit.kioskcheckin.utilities.Employee;
import com.provizit.kioskcheckin.utilities.GetSearchEmployees;
import com.provizit.kioskcheckin.utilities.Getpurposes;
import com.provizit.kioskcheckin.utilities.Getsubhierarchys;
import com.provizit.kioskcheckin.config.Preferences;
import com.provizit.kioskcheckin.utilities.TotalCounts;
import com.provizit.kioskcheckin.utilities.TvisitorsList;
import com.provizit.kioskcheckin.utilities.Vehicles;
import com.provizit.kioskcheckin.utilities.VisitorFormDetailsOtherArray;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MeetingRequestFormActivity extends AppCompatActivity implements View.OnClickListener {

    //internet connection
    BroadcastReceiver broadcastReceiver;
    RelativeLayout relative_internet;
    RelativeLayout relative_ui;
    ImageView company_logo, back_image;
    TextView txtVersion;
    CircleImageView host_img;
    LinearLayout or_model, host_details;
    Button btn_Yes, btn_No, search_btn, btn_next;
    TextView name, department1, designation;
    TextView txt_no_data, text_clikyes;
    EditText otherpurpose, editTextEmail;
    ArrayList<Getpurposes> purposes;
    ArrayList<Getsubhierarchys> departments;
    ArrayList<TvisitorsList> tvisitors;
    String emp_pic = "", selectedDepartment = "", selectedVisitortype = "", selectedPurpose = "", hierarchy_id = "", hierarchy_indexid = "", emp_name = "", host = "", selectedDesignation = "";
    AutoCompleteTextView dept_search, purpose_search, visitor_search, auto_search;
    ArrayList<GetSearchEmployees> data_list;
    Boolean Visitor_status = false;
    GetCVisitorDetailsModel model;
    ApiViewModel apiViewModel;
    String pic_Data = "false";
    String badge_Data = "false";
    String tvisitor = "false";
    String hid = "";
    String hiid = "false";
    String emailPattern;
    private boolean btnYesPressed = false;
    String uri = "", mid = "", hierarchyname = "";
    String nda_id = "";
    String accept_Data = "false";
    String badge = "";
    String Comp_id = "";
    //capture camera
    String filename = "";
    Uri fileUri;
    Bitmap thumbnail;
    Float meeting_status;
    Bitmap bitmap;
    private String encodedString;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_request_form_create);

        inits();
        registoreNetWorkBroadcast();

        //shared Preferences
        pic_Data = Preferences.loadStringValue(getApplicationContext(), Preferences.pic_Data, "");
        Comp_id = Preferences.loadStringValue(getApplicationContext(), Preferences.Comp_id, "");

        Intent intent = getIntent();
        host = intent.getStringExtra("host");
        hid = intent.getStringExtra("hid");
        hiid = intent.getStringExtra("hiid");
        tvisitor = intent.getStringExtra("tvisitor");
        hierarchyname = intent.getStringExtra("hierarchyname");
//        uri = intent.getStringExtra("uri");
//        filename = intent.getStringExtra("filename");
//        encodedString = intent.getStringExtra("encodedString");
        model = (GetCVisitorDetailsModel) intent.getSerializableExtra("model_key");

        or_model = findViewById(R.id.or_model);
        btn_Yes = findViewById(R.id.btn_Yes);
        btn_No = findViewById(R.id.btn_No);
        btn_Yes.setTypeface(ResourcesCompat.getFont(this, R.font.arbfonts_dinnextttlrabic_medium));
        btn_No.setTypeface(ResourcesCompat.getFont(this, R.font.arbfonts_dinnextttlrabic_medium));
//        search_btn = findViewById(R.id.search_btn);
        host_details = findViewById(R.id.host_details);
        name = findViewById(R.id.name);
        department1 = findViewById(R.id.department1);
        designation = findViewById(R.id.designation);
        txt_no_data = findViewById(R.id.txt_no_data);
        otherpurpose = findViewById(R.id.otherpurpose);
        dept_search = findViewById(R.id.dept_search);
        visitor_search = findViewById(R.id.visitor_search);
        purpose_search = findViewById(R.id.purpose_search);
        host_img = findViewById(R.id.host_img);
//        editTextEmail = findViewById(R.id.editTextEmail);
//        editTextEmail.requestFocus();
        company_logo = findViewById(R.id.company_logo);
        text_clikyes = findViewById(R.id.text_clikyes);
        btn_next = findViewById(R.id.btn_next);
        btn_next.setTypeface(ResourcesCompat.getFont(this, R.font.arbfonts_dinnextttlrabic_medium));
        back_image = findViewById(R.id.back_image);
        auto_search = findViewById(R.id.auto_search);
        departments = new ArrayList<Getsubhierarchys>();
        purposes = new ArrayList<Getpurposes>();
        tvisitors = new ArrayList<TvisitorsList>();
        selectedPurpose = "";

        emp_pic = "";

        emailPattern = "[a-zA-Z0-9._-]+@[a-z-]+\\.+[a-z]+";
        apiViewModel = new ViewModelProvider(MeetingRequestFormActivity.this).get(ApiViewModel.class);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String VersionName = sharedPreferences.getString("VersionName", "1.0");
        txtVersion = findViewById(R.id.txtVersion);
        txtVersion.setText(VersionName);

        //shared Preferences
        badge_Data = Preferences.loadStringValue(getApplicationContext(), Preferences.badge_Data, "");
        nda_id = Preferences.loadStringValue(getApplicationContext(), Preferences.nda_id, "");
        accept_Data = Preferences.loadStringValue(getApplicationContext(), Preferences.accept_Data, "");
        String c_Logo = Preferences.loadStringValue(getApplicationContext(), Preferences.company_Logo, "");
        if (c_Logo.equalsIgnoreCase("")) {
        } else {
            Glide.with(MeetingRequestFormActivity.this).load(c_Logo)
                    .into(company_logo);
        }
        meeting_status = model.getItems().getMeetingStatus();

        if (meeting_status == 1) {
            mid = model.getItems().get_id().get$oid();
            host = model.getItems().getEmployee().get_id().get$oid();
            hid = model.getItems().getEmployee().getHierarchy_id();
            hiid = model.getItems().getEmployee().getHierarchy_indexid();
            hierarchyname = model.getItems().getEmployee().getDepartment();
        }

        // Get the current layout direction
        int layoutDirection = getResources().getConfiguration().getLayoutDirection();

        // If layout direction is right-to-left, move the icon to the left
        if (layoutDirection == View.LAYOUT_DIRECTION_RTL) {
            btn_next.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_arrow_forward_24, 0);
        }

        // Check if the layout direction is right-to-left
        if (isRightToLeft()) {
            // If layout direction is right-to-left, mirror the image
            back_image.setScaleX(-1f);
        } else {
            // If layout direction is left-to-right, reset the image scaling
            back_image.setScaleX(1f);
        }

        // NoSpaceInputFilter
        InputFilter[] filters = new InputFilter[]{new NoSpaceInputFilter()};
        auto_search.setFilters(filters);
        dept_search.setFilters(filters);
        purpose_search.setFilters(filters);
        otherpurpose.setFilters(filters);


        apiViewModel.getpurposes(getApplicationContext());
        apiViewModel.getsubhierarchys(getApplicationContext(), "", "coordinator");

//        //enter press
//        editTextEmail.setOnEditorActionListener((v, actionId, event) -> {
//            if (actionId == EditorInfo.IME_ACTION_DONE) {
//                if (!editTextEmail.getText().toString().equals("")) {
//                    //hide keyboard
//                    ViewController.hideKeyboard(MeetingRequestActivity.this, editTextEmail);
//                    if (containsAlphabets(editTextEmail.getText().toString())) {
//                        apiViewModel.commoncheckuser(getApplicationContext(), "email", editTextEmail.getText().toString());
//                    } else {
//                        apiViewModel.commoncheckuser(getApplicationContext(), "mobile", editTextEmail.getText().toString());
//                    }
//                } else {
//                }
//                return true;
//            }
//            return false;
//        });
//
//        editTextEmail.setImeOptions(EditorInfo.IME_ACTION_DONE);

        apiViewModel.getResponsecommoncheckuser().observe(this, model1 -> {
            try {
                if (model1 != null) {
                    if (model1.getResult() == 200) {
                        if (model1.getItems().getRolename().equals("Check in/out")) {
                            txt_no_data.setVisibility(View.VISIBLE);
                            txt_no_data.setText(R.string.no_host);
                        } else {
                            txt_no_data.setVisibility(GONE);
                            dept_search.setVisibility(View.GONE);
                            name.setText(model1.getItems().getName());
                            designation.setText(model1.getItems().getDesignation());
                            department1.setText(model1.getItems().getHierarchyname());
                            host_details.setVisibility(View.VISIBLE);
                            host = model1.getItems().get_id().get$oid();
                            hid = model1.getItems().getHierarchy_id();
                            hiid = model1.getItems().getHierarchy_indexid();
                            emp_name = model1.getItems().getName();
                            hierarchyname = model1.getItems().getHierarchyname();
                            selectedDesignation = model1.getItems().getDesignation();
                            if (model1.getItems().getPic() != null && model1.getItems().getPic().size() != 0) {
                                emp_pic = model1.getItems().getPic().get(model1.getItems().getPic().size() - 1);
//
                                //preferences
                                String Comp_id = Preferences.loadStringValue(getApplicationContext(), Preferences.Comp_id, "");
                                Glide.with(MeetingRequestFormActivity.this).load(DataManger.IMAGE_URL + "/uploads/" + Comp_id + "/" + model1.getItems().getPic().get(model1.getItems().getPic().size() - 1))
                                        .into(host_img);
                            } else {
                                host_img.setImageResource(R.drawable.ic_user_white);
                            }
                        }

                    } else {
                        txt_no_data.setVisibility(View.VISIBLE);
                        txt_no_data.setText(R.string.no_host);
                    }
                }

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        apiViewModel.gettvisitors(getApplicationContext());

        apiViewModel.getResponseforTvisitorsList().observe(this, model -> {
            ArrayList<TvisitorsList> visitor_list = new ArrayList<>();
            visitor_list = model.getItems();
            TvisitorsList purposeOther = new TvisitorsList();
            purposeOther.setName("others");
            visitor_list.add(purposeOther);
            tvisitors = new ArrayList<>();
            tvisitors.clear();
            try {
                if (visitor_list != null && visitor_list.size() > 0) {
                    for (int i = 0; i < visitor_list.size(); i++) {
                        TvisitorsList getpurposes = new TvisitorsList();
                        getpurposes.setName(visitor_list.get(i).getName());
                        getpurposes.setActive(visitor_list.get(i).getActive());
                        tvisitors.add(getpurposes);
                        TvisitorAdapter invitesadapter = new TvisitorAdapter(MeetingRequestFormActivity.this, R.layout.row, R.id.lbl_name, tvisitors);
                        visitor_search.setThreshold(0);
                        visitor_search.setAdapter(invitesadapter);
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        apiViewModel.getResponsesubhierarchys().observe(this, model -> {
            ArrayList<Getsubhierarchys> departments_list = new ArrayList<>();
            departments_list = model.getItems();
            departments = new ArrayList<>();
            departments.clear();
            try {
                if (departments_list != null && departments_list.size() > 0) {
                    for (int i = 0; i < departments_list.size(); i++) {
                        if (departments_list.get(i).getCoordinator() > 0) {
                            Getsubhierarchys getsubhierarchys = new Getsubhierarchys();
                            getsubhierarchys.setName(departments_list.get(i).getName());
                            getsubhierarchys.set_id(departments_list.get(i).get_id());
                            getsubhierarchys.setId(departments_list.get(i).getId());
                            departments.add(getsubhierarchys);
                            DepartmentsAdapter invitesadapter = new DepartmentsAdapter(MeetingRequestFormActivity.this, R.layout.row, R.id.lbl_name, departments);
                            dept_search.setThreshold(0);
                            dept_search.setAdapter(invitesadapter);
                        }
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        apiViewModel.getResponseforpurposes().observe(this, model -> {
            ArrayList<Getpurposes> purposes_list = new ArrayList<>();
            purposes_list = model.getItems();
            Getpurposes purposeOther = new Getpurposes();
            purposeOther.setName("others");
            purposes_list.add(purposeOther);
            purposes = new ArrayList<>();
            purposes.clear();
            try {
                if (purposes_list != null && purposes_list.size() > 0) {
                    for (int i = 0; i < purposes_list.size(); i++) {
                        Getpurposes getpurposes = new Getpurposes();
                        getpurposes.setName(purposes_list.get(i).getName());
                        purposes.add(getpurposes);
                        PurposesAdapter invitesadapter = new PurposesAdapter(MeetingRequestFormActivity.this, R.layout.row, R.id.lbl_name, purposes);
                        purpose_search.setThreshold(0);
                        purpose_search.setAdapter(invitesadapter);
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        apiViewModel.getuserLDetails(getApplicationContext(), "visitor");

        apiViewModel.getResponseforcompany().observe(this, model -> {
            if (model != null) {
                try {
                    if (model.getItems().getVisitor().getTvisitor() == true) {
                        Visitor_status = true;
                        visitor_search.setVisibility(View.VISIBLE);
                    } else {
                        Visitor_status = false;
                        visitor_search.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });

        auto_search.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GetSearchEmployees getSearchEmployees = data_list.get(position);
                btn_No.setText(getResources().getString(R.string.No));
                btn_Yes.setVisibility(View.VISIBLE);
                auto_search.getText().clear();
                auto_search.setEnabled(false);
                host = getSearchEmployees.get_id().get$oid();
                emp_name = getSearchEmployees.getName();
                selectedDesignation = getSearchEmployees.getDesignation();
                selectedDepartment = getSearchEmployees.getHierarchyname();
                hierarchy_id = getSearchEmployees.getHierarchy_id();
                hierarchy_indexid = getSearchEmployees.getHierarchy_indexid();
                hid = getSearchEmployees.getHierarchy_id();
                hiid = getSearchEmployees.getHierarchy_indexid();

                host_details.setVisibility(View.VISIBLE);
                name.setText(emp_name);
                dept_search.setText(selectedDepartment);
                dept_search.setEnabled(false);
                department1.setText(selectedDepartment);
                designation.setText(getSearchEmployees.getDesignation());
                if (getSearchEmployees.getPic() != null && getSearchEmployees.getPic().size() != 0) {
                    emp_pic = getSearchEmployees.getPic().get(getSearchEmployees.getPic().size() - 1);
                    //preferences
                    String Comp_id = Preferences.loadStringValue(getApplicationContext(), Preferences.Comp_id, "");
                    Glide.with(MeetingRequestFormActivity.this).load(DataManger.IMAGE_URL + "/uploads/" + Comp_id + "/" + getSearchEmployees.getPic().get(getSearchEmployees.getPic().size() - 1))
                            .into(host_img);
                } else {
                    host_img.setImageResource(R.drawable.ic_user_white);

                }

            }
        });

        auto_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Do nothing
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() >= 4) {
                    auto_search.showDropDown();
                } else {
                    auto_search.dismissDropDown();
                }
            }
        });

        auto_search.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // This will make sure that the dropdown is shown only when the text is at least 3 characters long
                if (auto_search.getText().length() >= 4) {
                    auto_search.showDropDown();
                }
                return false;
            }
        });


        dept_search.setOnTouchListener((v, event) -> {
            dept_search.showDropDown();
            return false;
        });

        purpose_search.setOnTouchListener((v, event) -> {
            purpose_search.showDropDown();
            return false;
        });

        visitor_search.setOnTouchListener((view, motionEvent) -> {
            visitor_search.showDropDown();
            return false;
        });

        purpose_search.setOnItemClickListener((parent, view, position, id) -> {
            Getpurposes getpurposes = purposes.get(position);
            ViewController.hideKeyboard(MeetingRequestFormActivity.this, purpose_search);
            selectedPurpose = getpurposes.getName();

        });

        visitor_search.setOnItemClickListener((adapterView, view, position, l) -> {

            ViewController.hideKeyboard(MeetingRequestFormActivity.this, visitor_search);

            TvisitorsList tvisitorsList = tvisitors.get(position);
            selectedVisitortype = tvisitorsList.getName();
        });

        dept_search.setOnItemClickListener((parent, view, position, id) -> {
            ViewController.hideKeyboard(MeetingRequestFormActivity.this, dept_search);
            txt_no_data.setVisibility(GONE);
            Getsubhierarchys getDepartments = departments.get(position);
            host = "";
            selectedDesignation = "";
            emp_pic = "";
            emp_name = "";
            hierarchyname = getDepartments.getName();
            hid = getDepartments.get_id().get$oid();
            hiid = getDepartments.getId();
            host_details.setVisibility(GONE);
            auto_search.getText().clear();
            purpose_search.setVisibility(View.VISIBLE);
            btnYesPressed = true;
        });

        apiViewModel.getsearchemployees(getApplicationContext(), "", "", "");

        apiViewModel.getResponseforSearchEmployees().observe(this, model -> {
            ArrayList<GetSearchEmployees> empList = new ArrayList<>();
            empList = model.getItems();
            data_list = new ArrayList<>();
            data_list.clear();
            try {
                if (empList != null && empList.size() > 0) {
                    for (int i = 0; i < empList.size(); i++) {
                        GetSearchEmployees getSearchEmployees = new GetSearchEmployees();
                        getSearchEmployees.setName(empList.get(i).getName());
                        getSearchEmployees.setEmail(empList.get(i).getEmail());
                        getSearchEmployees.setMobile(empList.get(i).getMobile());
                        getSearchEmployees.set_id(empList.get(i).get_id());
                        getSearchEmployees.setHierarchy_id(empList.get(i).getHierarchy_id());
                        getSearchEmployees.setHierarchy_indexid(empList.get(i).getHierarchy_indexid());
                        getSearchEmployees.setDesignation(empList.get(i).getDesignation());
                        getSearchEmployees.setHierarchyname(empList.get(i).getHierarchyname());
                        getSearchEmployees.setPic(empList.get(i).getPic());
                        data_list.add(getSearchEmployees);
                        CustomAdapter invitesadapter = new CustomAdapter(MeetingRequestFormActivity.this, R.layout.row, R.id.lbl_name, data_list);
                        auto_search.setThreshold(0);
                        auto_search.setAdapter(invitesadapter);
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        btn_next.setOnClickListener(this);
        btn_Yes.setOnClickListener(this);
        btn_No.setOnClickListener(this);
//        search_btn.setOnClickListener(this);
        back_image.setOnClickListener(this);
    }

    //disable auto click action after scann
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        editTextEmail.setInputType(InputType.TYPE_CLASS_NUMBER);
//        otherpurpose.setInputType(InputType.TYPE_CLASS_NUMBER);
//        dept_search.setInputType(InputType.TYPE_CLASS_NUMBER);
//        visitor_search.setInputType(InputType.TYPE_CLASS_NUMBER);
//        purpose_search.setInputType(InputType.TYPE_CLASS_NUMBER);

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
                    float meeting_status = model.getItems().getMeetingStatus();
                    Intent intent;
                    if (meeting_status == 1) {
                        intent = new Intent(getApplicationContext(), MeetingDetailsActivity.class);
                    } else {
                        intent = new Intent(getApplicationContext(), VisitorLoginActivity.class);
                    }
                    intent.putExtra("model_key", model);
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
        btn_next.setFocusable(false);
        btn_next.setFocusableInTouchMode(false);
        btn_Yes.setFocusable(false);
        btn_Yes.setFocusableInTouchMode(false);
        btn_No.setFocusable(false);
        btn_No.setFocusableInTouchMode(false);
        auto_search.setFocusable(false);
        auto_search.setFocusableInTouchMode(false);
        dept_search.setFocusable(false);
        dept_search.setFocusableInTouchMode(false);
        visitor_search.setFocusable(false);
        visitor_search.setFocusableInTouchMode(false);
        purpose_search.setFocusable(false);
        purpose_search.setFocusableInTouchMode(false);
        otherpurpose.setFocusable(false);
        otherpurpose.setFocusableInTouchMode(false);
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
            btn_next.setFocusable(true);
            btn_next.setFocusableInTouchMode(true);
            btn_Yes.setFocusable(true);
            btn_Yes.setFocusableInTouchMode(true);
            btn_No.setFocusable(true);
            btn_No.setFocusableInTouchMode(true);
            auto_search.setFocusable(true);
            auto_search.setFocusableInTouchMode(true);
            dept_search.setFocusable(true);
            dept_search.setFocusableInTouchMode(true);
            visitor_search.setFocusable(true);
            visitor_search.setFocusableInTouchMode(true);
            purpose_search.setFocusable(true);
            purpose_search.setFocusableInTouchMode(true);
            otherpurpose.setFocusable(true);
            otherpurpose.setFocusableInTouchMode(true);
        }, 500);
    }

    private void inits() {
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

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_next:
                AnimationSet animation1 = Conversions.animation();
                v.startAnimation(animation1);
                //hide keyboard
//                ViewController.hideKeyboard(MeetingRequestActivity.this, editTextEmail);
                if (InterNetConnectivityCheck.isOnline(getApplicationContext())) {
                    new Handler().postDelayed(
                            () -> {
                                auto_search.setError(null);
                                dept_search.setError(null);
                                purpose_search.setError(null);
                                visitor_search.setError(null);
//                                editTextEmail.setError(null);
                                otherpurpose.setError(null);
                            }, 5000);

                    if (!btnYesPressed && (hid == null || hid.equals(""))) {
                        auto_search.requestFocus();
                        auto_search.setError("Please enter host's email or mobile number or Please select department");
                        dept_search.setError("Please select department");

                    } else if (!TextUtils.isEmpty(host) && !btnYesPressed) {
                        auto_search.requestFocus();
                        auto_search.setError("Please Click 'YES' and confirm the host!");

                    } else if (TextUtils.isEmpty(visitor_search.getText().toString()) && Visitor_status) {
                        visitor_search.requestFocus();
                        visitor_search.setError("Please select visitor type");

                    } else if (TextUtils.isEmpty(purpose_search.getText().toString())) {
                        purpose_search.requestFocus();
                        purpose_search.setError("Please select purpose of visit");

                    } else if (selectedPurpose.equals("others") && TextUtils.isEmpty(otherpurpose.getText().toString())) {
                        otherpurpose.requestFocus();
                        otherpurpose.setError("Write the purpose / Note of your visit!");

                    } else {
                        if (pic_Data.equalsIgnoreCase("true")) {
                            // Start camera intent to capture image
                            ContentValues values = new ContentValues();
                            values.put(MediaStore.Images.Media.TITLE, "New Picture");
                            values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
                            fileUri = getContentResolver().insert(
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1 && Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                                intent.putExtra("android.media.action.IMAGE_CAPTURE", 1);
                            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                intent.putExtra("aandroid.media.action.IMAGE_CAPTURE", 1);
                                intent.putExtra("android.media.action.IMAGE_CAPTURE", true);
                            } else {
                                intent.putExtra("android.intent.extras.CAMERA_FACING", 1);
                            }
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                            startActivityForResult(intent, 1);

                        } else {
                            model.getItems().setMeetingStatus(Float.parseFloat("0"));

                            model.getItems().setSubject(selectedPurpose);
                            model.getItems().setEmployee(new Employee());
                            model.getItems().getEmployee().setDepartment(hierarchyname);
                            model.getItems().getEmployee().setDesignation(selectedDesignation);
                            model.getItems().getEmployee().setName(emp_name);
                            ArrayList<String> pic1 = new ArrayList<String>();
                            pic1.add(emp_pic);
                            model.getItems().getEmployee().setPic(pic1);
                            if (selectedPurpose.equals("others")) {
                                model.getItems().setSubject(selectedPurpose);
                            }

//                            if (!otherpurpose.getText().toString().equals("")) {
                            model.getItems().setNote_val(otherpurpose.getText().toString());

//                            }

                            Object_post();
                        }
                    }
                } else {
                    DataManger.internetpopup(MeetingRequestFormActivity.this);
                }
//                btnYesPressed = false;
                break;
            case R.id.back_image:
                animation1 = Conversions.animation();
                v.startAnimation(animation1);
                float meeting_status = model.getItems().getMeetingStatus();
                Intent intent;
                if (meeting_status == 1) {
                    intent = new Intent(getApplicationContext(), MeetingDetailsActivity.class);
                } else {
                    intent = new Intent(getApplicationContext(), VisitorLoginActivity.class);
                }
                intent.putExtra("model_key", model);
                startActivity(intent);
                break;
            case R.id.btn_Yes:
                animation1 = Conversions.animation();
                v.startAnimation(animation1);
                or_model.setVisibility(View.GONE);
                dept_search.setVisibility(View.GONE);
                btn_Yes.setVisibility(View.GONE);
                btn_No.setVisibility(GONE);
                text_clikyes.setVisibility(GONE);
                purpose_search.setVisibility(View.VISIBLE);
//                btn_No.setText(getResources().getString(R.string.Change));
                btnYesPressed = true;
                break;
            case R.id.btn_No:
                animation1 = Conversions.animation();
                v.startAnimation(animation1);
                auto_search.setEnabled(true);
                host = "";
                emp_name = "";
                emp_pic = "";
                selectedDesignation = "";
                selectedDepartment = "";
                hid = "";
                hiid = "";
                dept_search.setText("");
                dept_search.setEnabled(true);
                host_details.setVisibility(View.GONE);
                or_model.setVisibility(View.VISIBLE);
                dept_search.setVisibility(View.VISIBLE);
                purpose_search.setVisibility(View.VISIBLE);
                break;
            default:
                break;
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

    //capture camera
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    if (Build.VERSION.SDK_INT >= 29) {
                        try (ParcelFileDescriptor pfd = MeetingRequestFormActivity.this.getContentResolver().openFileDescriptor(fileUri, "r")) {
                            if (pfd != null) {
                                String path = getRealPathFromURI(fileUri);
                                bitmap = BitmapFactory.decodeFileDescriptor(pfd.getFileDescriptor());
                                encodedString = encodeTobase64(BitmapFactory.decodeFile(path), true);
                            }
                        } catch (Exception e) {
                            Log.e(TAG, "Error processing vehicles: ", e);  // Proper logging
                        }
                    } else {
                        thumbnail = MediaStore.Images.Media.getBitmap(getContentResolver(), fileUri);
                        String imageurl = getRealPathFromURI(fileUri);
                        bitmap = BitmapFactory.decodeFile(imageurl);
                        encodedString = encodeTobase64(BitmapFactory.decodeFile(imageurl), true);
                    }
                    filename = Conversions.datemillirandstring() + ".jpeg";
                    Matrix matrix = new Matrix();
                    matrix.postRotate(90);
                    Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);
                    Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);

                    model.getItems().setMeetingStatus(Float.parseFloat("0"));
                    model.getItems().setSubject(selectedPurpose);
                    model.getItems().setEmployee(new Employee());
                    model.getItems().getEmployee().setDepartment(hierarchyname);
                    model.getItems().getEmployee().setDesignation(selectedDesignation);
                    model.getItems().getEmployee().setName(emp_name);
                    ArrayList<String> pic1 = new ArrayList<String>();
                    pic1.add(emp_pic);
                    model.getItems().getEmployee().setPic(pic1);
                    if (selectedPurpose.equals("others")) {
                        model.getItems().setSubject(selectedPurpose);
                    }
                    model.getItems().setNote_val(otherpurpose.getText().toString());

                    //capture
                    JsonObject gsonObject = new JsonObject();
                    JSONObject jsonObj_1 = new JSONObject();
                    try {
                        jsonObj_1.put("cid", Comp_id);
                        jsonObj_1.put("key", filename);
                        jsonObj_1.put("img", encodedString);
                        JsonParser jsonParser = new JsonParser();
                        gsonObject = (JsonObject) jsonParser.parse(jsonObj_1.toString());
                        qrindex(gsonObject);
                        progressDialog = new ProgressDialog(MeetingRequestFormActivity.this);
                        progressDialog.setCancelable(false);
                        progressDialog.setMessage("Loading...");
                        progressDialog.show();
                    } catch (Exception e) {
                        Log.e(TAG, "Error processing vehicles: ", e);  // Proper logging
                    }

                    //object post

                    Object_post();

                } catch (Exception e) {
                    Log.e(TAG, "Error processing vehicles: ", e);  // Proper logging
                }

            } else {
//                finish();
            }
        }
    }

    private void Object_post() {
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
        if (badge_Data.equalsIgnoreCase("true")) {
            // Generate a random four-digit code
            badge = "" + Conversions.getNDigitRandomNumber(3);
        }
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
            }  catch (Exception e) {
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
        }  catch (Exception e) {
            Log.e(TAG, "Error processing vehicles: ", e);  // Proper logging
        }

        try {
            others1 = new JSONArray();
            for (int i = 0; i < others.size(); i++) {
                others1.put(others.get(i).getothers());
            }
        }  catch (Exception e) {
            Log.e(TAG, "Error processing vehicles: ", e);  // Proper logging
        }

        try {
            belongings1 = new JSONArray();
            for (int i = 0; i < belongings.size(); i++) {
                belongings1.put(belongings.get(i).getbelongings());
            }
        }  catch (Exception e) {
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
            }  catch (Exception e) {
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
            jsonObj_.put("note_val", model.getItems().getNote_val());
            jsonObj_.put("livepic", livePic);
            jsonObj_.put("vlocation", Preferences.loadStringValue(getApplicationContext(), Preferences.location_id, ""));
            jsonObj_.put("hierarchy_id", hid);
            jsonObj_.put("hierarchy_indexid", hiid);
            jsonObj_.put("hierarchyname", hierarchyname);
            jsonObj_.put("host", host);
            jsonObj_.put("tvisitor", tvisitor);
            jsonObj_.put("mid", "");
            jsonObj_.put("badge", badge);
            jsonObj_.put("nda_terms", "true");
            jsonObj_.put("nda_id", nda_id);
            if (InterNetConnectivityCheck.isOnline(getApplicationContext())) {
                actioncheckinout(jsonObj_);
            } else {
                DataManger.internetpopup(MeetingRequestFormActivity.this);
            }
        }  catch (Exception e) {
            Log.e(TAG, "Error processing vehicles: ", e);  // Proper logging
        }


        Intent intent = new Intent(getApplicationContext(), YourRequestSentActivity.class);
        intent.putExtra("model_key", model);
        intent.putExtra("tvisitor", selectedVisitortype);
        intent.putExtra("host", host);
        intent.putExtra("hid", hid);
        intent.putExtra("hiid", hiid);
        intent.putExtra("hierarchyname", hierarchyname);
        intent.putExtra("uri", fileUri + "");
        intent.putExtra("filename", filename);
        intent.putExtra("encodedString", encodedString);
        startActivity(intent);

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
                    Log.e(TAG, "Error processing vehicles: ", e);  // Proper logging
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                progressDialog.dismiss();
            }
        }, MeetingRequestFormActivity.this, gsonObject);
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
        }, MeetingRequestFormActivity.this, jsonObj_);
    }


    public String getRealPathFromURI(Uri contentUri) {
        String path = "";
        if (getContentResolver() != null) {
            Cursor cursor = getContentResolver().query(contentUri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                path = cursor.getString(idx);
                cursor.close();
            }
        }
        return path;
    }

    public static String encodeTobase64(Bitmap image, Boolean Status) {
        Matrix matrix = new Matrix();
        matrix.postRotate(90);

        Bitmap scaledBitmap = Bitmap.createScaledBitmap(image, image.getWidth(), image.getHeight(), true);
        Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
        Bitmap immagex = rotatedBitmap;

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.JPEG, 20, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        return imageEncoded;
    }

    public class NoSpaceInputFilter implements InputFilter {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            if (dstart == 0 && source.length() > 0 && Character.isWhitespace(source.charAt(0))) {
                return "";
            }
            return null;
        }
    }
}