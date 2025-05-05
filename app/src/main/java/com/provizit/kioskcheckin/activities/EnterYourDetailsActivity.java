package com.provizit.kioskcheckin.activities;

import static android.view.View.GONE;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;
import static com.provizit.kioskcheckin.logins.AdminLoginActivity.isEmailValid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.ViewCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationSet;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.hbb20.CountryCodePicker;
import com.provizit.kioskcheckin.activities.Meetings.MeetingRequestActivity;
import com.provizit.kioskcheckin.adapters.DocumentsAdapter;
import com.provizit.kioskcheckin.adapters.NationalitysAdapter;
import com.provizit.kioskcheckin.config.ConnectionReceiver;
import com.provizit.kioskcheckin.config.InterNetConnectivityCheck;
import com.provizit.kioskcheckin.logins.VisitorLoginActivity;
import com.provizit.kioskcheckin.mvvm.ApiViewModel;
import com.provizit.kioskcheckin.R;
import com.provizit.kioskcheckin.services.BlockedvisitorrequestsModel;
import com.provizit.kioskcheckin.services.Blocklist_Model;
import com.provizit.kioskcheckin.services.Conversions;
import com.provizit.kioskcheckin.services.CountryCodes;
import com.provizit.kioskcheckin.api.DataManger;
import com.provizit.kioskcheckin.services.GetCVisitorDetailsModel;
import com.provizit.kioskcheckin.services.GetNdaActiveDetailsModel;
import com.provizit.kioskcheckin.services.GetnationalityModel;
import com.provizit.kioskcheckin.services.Privacypolicymodel;
import com.provizit.kioskcheckin.services.VcheckuserModel;
import com.provizit.kioskcheckin.services.VisitorformDetailsModel;
import com.provizit.kioskcheckin.utilities.Belongings;
import com.provizit.kioskcheckin.utilities.Getdocuments;
import com.provizit.kioskcheckin.utilities.Getnationality;
import com.provizit.kioskcheckin.utilities.GetvisitorrequestblocklistModel;
import com.provizit.kioskcheckin.utilities.MobileDataAddress;
import com.provizit.kioskcheckin.config.Preferences;
import com.provizit.kioskcheckin.utilities.Vehicles;
import com.provizit.kioskcheckin.utilities.VisitorFormDetailsOtherArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EnterYourDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    //internet connection
    BroadcastReceiver broadcastReceiver;
    RelativeLayout relative_internet;
    RelativeLayout relative_ui;
    ImageView company_logo;
    EditText email, mobile, Id_number, e_pname;
    TextView text_mobile, text_email;
    LinearLayout linaer_privacy_policy;
    LinearLayout liner_ccp, linear_ccp, linear, linear_others;
    Button btn_next;
    ImageView back_image;
    ArrayList<Getdocuments> documents;
    ArrayList<Getdocuments> documents_list;
    ArrayList<String> nationalitys;
    int doc;
    CountryCodePicker ccp, ccp1;
    String docId, emp_id, location_id, nationality = "";
    AutoCompleteTextView document_search, nationality_search;
    String nda_Data = "false";
    String country_Code = "";
    ApiViewModel apiViewModel;
    String getNationality_Status = "";
    GetNdaActiveDetailsModel ndamodel;
    Getdocuments getdocuments;
    Getdocuments getDocumentss;
    GetCVisitorDetailsModel model;
    String Id_numberkeyboard_type = "";
    Privacypolicymodel privicymodel;
    LinearLayout linaer_p, linear_checkbox_selection;
    CheckBox checkBox;
    VisitorformDetailsModel visiter_model;
    String P_policy = "";
    //    form list
    RecyclerView recyclerview;
    ArrayList<VisitorFormDetailsOtherArray> VisitorformDetails_others;
    FormCustomAdapter formCustomAdapter;
    ArrayList<String> Nationalitsblaclklist;
    String Nationblacklist = "";
    String getvisitorblocklistID = "";
    String Comp_id = "";
    ArrayList<GetvisitorrequestblocklistModel> visitorblocklist;
    ArrayList<String> Vistiror_blockIDs;
    String vistiorbloclist;
    String blocking = "false";

    boolean nationalityStatus = false;
    boolean nationalityActive = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_your_details);
        mobile = findViewById(R.id.mobile);
        text_mobile = findViewById(R.id.text_mobile);
        text_email = findViewById(R.id.text_email);
        linear_ccp = findViewById(R.id.linear_ccp);
        ViewCompat.setLayoutDirection(linear_ccp, ViewCompat.LAYOUT_DIRECTION_LTR);
        liner_ccp = findViewById(R.id.liner_ccp);
        ViewCompat.setLayoutDirection(linear_ccp, ViewCompat.LAYOUT_DIRECTION_LTR);
        email = findViewById(R.id.email);
        email.requestFocus();
        e_pname = findViewById(R.id.e_pname);
        Id_number = findViewById(R.id.Id_number);
        btn_next = findViewById(R.id.btn_next);
        btn_next.setTypeface(ResourcesCompat.getFont(this, R.font.arbfonts_dinnextttlrabic_medium));
        back_image = findViewById(R.id.back_image);
        emp_id = Preferences.loadStringValue(getApplicationContext(), Preferences.email_id, "");
        location_id = Preferences.loadStringValue(getApplicationContext(), Preferences.location_id, "");
        Comp_id = Preferences.loadStringValue(getApplicationContext(), Preferences.Comp_id, "");
        blocking = Preferences.loadStringValue(getApplicationContext(), Preferences.blocking, "");


        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String VersionName = sharedPreferences.getString("VersionName", "1.0");
        TextView txtVersion = findViewById(R.id.txtVersion);
        txtVersion.setText(VersionName);

        ccp = (CountryCodePicker) findViewById(R.id.ccp);
        ccp.setDefaultCountryUsingPhoneCode(Integer.parseInt("966"));
        ccp.setCountryForNameCode("966");
        ccp1 = (CountryCodePicker) findViewById(R.id.ccp1);
        linear = findViewById(R.id.linear);
        recyclerview = findViewById(R.id.recyclerview);
        company_logo = findViewById(R.id.company_logo);
        document_search = findViewById(R.id.document_search);
        linaer_privacy_policy = findViewById(R.id.linaer_privacy_policy);
        document_search.setKeyListener(null);
        nationality_search = findViewById(R.id.nationality_search);
        linaer_p = findViewById(R.id.linaer_p);
        linear_checkbox_selection = findViewById(R.id.linear_checkbox_selection);
        checkBox = findViewById(R.id.checkbox);
        linear_others = findViewById(R.id.linear_others);
        e_pname.setInputType(InputType.TYPE_CLASS_TEXT);
        mobile.setInputType(InputType.TYPE_CLASS_TEXT);
        email.setInputType(InputType.TYPE_CLASS_TEXT);
        Id_number.setInputType(InputType.TYPE_CLASS_TEXT);
        privicymodel = new Privacypolicymodel();
        ndamodel = new GetNdaActiveDetailsModel();
        Intent intent = getIntent();
        model = (GetCVisitorDetailsModel) intent.getSerializableExtra("model_key");

        apiViewModel = new ViewModelProvider(EnterYourDetailsActivity.this).get(ApiViewModel.class);
        //Nda active details
        apiViewModel.getndafdetials(getApplicationContext(), "id", "active");
        // privacy policy
        apiViewModel.getprivacypolicydetials(getApplicationContext(), "id", "active");
        //Visitor Blocklist
        apiViewModel.getcblacklistdetails(getApplicationContext(), Comp_id);

        //Vistior ID_block
        apiViewModel.getblockedvisitorrequests(getApplicationContext(), Comp_id, "");

        visiter_model = new VisitorformDetailsModel();
        P_policy = Preferences.loadStringValue(getApplicationContext(), Preferences.P_policy, "");

        if (P_policy.equalsIgnoreCase("true")) {
            linaer_p.setVisibility(View.VISIBLE);
            btn_next.setBackgroundColor(btn_next.getContext().getResources().getColor(R.color.light_gray));
        } else {
            linaer_p.setVisibility(GONE);
            btn_next.setBackgroundColor(btn_next.getContext().getResources().getColor(R.color.colorPrimary));
        }

        // Get the current layout direction
        int layoutDirection = getResources().getConfiguration().getLayoutDirection();

        // If layout direction is right-to-left, move the icon to the left
        if (layoutDirection == View.LAYOUT_DIRECTION_RTL) {
            btn_next.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_arrow_forward_24, 0);

        }

        //shared Preferences
        nda_Data = Preferences.loadStringValue(getApplicationContext(), Preferences.nda_Data, "");
        country_Code = Preferences.loadStringValue(getApplicationContext(), Preferences.country_Code, "");

        mobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not used, but required to implement
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Not used, but required to implement
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Remove non-numeric characters from the EditText
                removeNonNumericCharacters(s);
            }
        });


        try {
            if (!country_Code.equals("")) {
                ccp.setDefaultCountryUsingPhoneCode(Integer.parseInt(country_Code));
                ccp.setCountryForNameCode(country_Code);
                ccp1.setDefaultCountryUsingPhoneCode(Integer.parseInt(country_Code));
                ccp1.setCountryForNameCode(country_Code);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
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

        // Check if the layout direction is right-to-left
        if (isRightToLeft()) {
            // If layout direction is right-to-left, mirror the image
            back_image.setScaleX(-1f);
        } else {
            // If layout direction is left-to-right, reset the image scaling
            back_image.setScaleX(1f);
        }


        Id_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String input = editable.toString();
                String alphanumericOnly = input.replaceAll("[^a-zA-Z0-9]", "");

                if (Id_numberkeyboard_type.equalsIgnoreCase("numbers")) {
                    removeNonNumericCharacters(editable);
                } else if (Id_numberkeyboard_type.equalsIgnoreCase("text")) {
                    if (!input.equals(alphanumericOnly)) {
                        Id_number.setText(alphanumericOnly);
                        Id_number.setSelection(alphanumericOnly.length());
                    }
                }
            }
        });

        doc = 0;
        nationalitys = new ArrayList<>();
        documents = new ArrayList<>();
        Vistiror_blockIDs = new ArrayList<>();

        //form list
        VisitorformDetails_others = new ArrayList<>();
        apiViewModel.getdocuments(getApplicationContext());

        apiViewModel.getResponseforSelectedId_list().observe(this, model -> {
            documents_list = new ArrayList<>();
            documents_list = model.getItems();
            documents = new ArrayList<>();
            documents.clear();
            try {
                if (documents_list != null && documents_list.size() > 0) {
                    for (int i = 0; i < documents_list.size(); i++) {
                        if (documents_list.get(i).getActive() && documents_list.get(i).getCommon()) {
                            getdocuments = new Getdocuments();
                            getdocuments.setName(documents_list.get(i).getName());
                            getdocuments.set_id(documents_list.get(i).get_id());
                            getdocuments.setDoc_type(documents_list.get(i).getDoc_type());
                            getdocuments.setCommon(documents_list.get(i).getCommon());
                            getdocuments.setActive(documents_list.get(i).getActive());
                            documents.add(getdocuments);
                            DocumentsAdapter invitesadapter = new DocumentsAdapter(EnterYourDetailsActivity.this, R.layout.row, R.id.lbl_name, documents);
                            document_search.setThreshold(0);
                            document_search.setAdapter(invitesadapter);
                        } else if (documents_list.get(i).getActive() && documents_list.get(i).getNationlities().size() != 0) {
                            getdocuments = new Getdocuments();
                            getdocuments.setName(documents_list.get(i).getName());
                            getdocuments.set_id(documents_list.get(i).get_id());
                            getdocuments.setDoc_type(documents_list.get(i).getDoc_type());
                            getdocuments.setCommon(documents_list.get(i).getCommon());
                            getdocuments.setActive(documents_list.get(i).getActive());
                            documents.add(getdocuments);
                            DocumentsAdapter invitesadapter = new DocumentsAdapter(EnterYourDetailsActivity.this, R.layout.row, R.id.lbl_name, documents);
                            document_search.setThreshold(0);
                            document_search.setAdapter(invitesadapter);
                        }

                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        document_search.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getDocumentss = documents.get(position);
                nationality_search.setText("");
                Id_number.setText("");
                Id_number.setVisibility(GONE);
                for (int i = 0; i < documents_list.size(); i++) {
                    if (getDocumentss.get_id().get$oid().equals(documents_list.get(i).get_id().get$oid())) {
                        doc = i;
                        docId = documents_list.get(i).get_id().get$oid();
                    }
                }

                try {
                    if (getDocumentss.getCommon() != null && getDocumentss.getCommon()) {
                        countryList();
                    } else {
                        apiViewModel.getnationality(getApplicationContext(), docId);
                    }
                    if (getDocumentss.getDoc_type() != null && getDocumentss.getDoc_type()) {
                        Id_numberkeyboard_type = "numbers";
                    } else {
                        Id_numberkeyboard_type = "text";
                    }
                    nationality_search.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });

        apiViewModel.getResponseforNationalty().observe(this, new Observer<GetnationalityModel>() {
            @Override
            public void onChanged(GetnationalityModel model) {
                ArrayList<Getnationality> nationalist_list = new ArrayList<>();
                nationalist_list = model.getItems();
                nationalitys = new ArrayList<>();
                nationalitys.clear();
                try {
                    if (nationalist_list != null && nationalist_list.size() > 0) {
                        for (int i = 0; i < nationalist_list.size(); i++) {
                            Getnationality getNationalitys = new Getnationality();
                            if (nationalist_list.get(i).getActive()) {
                                getNationalitys.setName(nationalist_list.get(i).getName());
                                nationalitys.add(nationalist_list.get(i).getName());
                            }
                        }
                        NationalitysAdapter invitesadapter = new NationalitysAdapter(EnterYourDetailsActivity.this, R.layout.row, R.id.lbl_name, nationalitys);
                        nationality_search.setThreshold(0);
                        nationality_search.setAdapter(invitesadapter);
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });

        document_search.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                document_search.showDropDown();
                return false;
            }
        });

        nationality_search.addTextChangedListener(new TextWatcher() {
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
                if (s.length() == 0) {
                    nationality = "";
                }

            }
        });

        nationality_search.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String getnationalitys = nationalitys.get(position);
                Id_number.setText("");
                Id_number.setVisibility(View.VISIBLE);
                nationality = getnationalitys;

                if (nationality_search.getText().toString().equalsIgnoreCase("الالعربية السعودية") || nationality_search.getText().toString().equalsIgnoreCase("سعودي") || nationality_search.getText().toString().equalsIgnoreCase("Saudi") || nationality_search.getText().toString().equalsIgnoreCase("SaudiArabia") || nationality_search.getText().toString().equalsIgnoreCase("Saudi Arabia")) {
                    int maxLength = 10;
                    InputFilter[] filters = new InputFilter[1];
                    filters[0] = new InputFilter.LengthFilter(maxLength);
                    Id_number.setFilters(filters);
                } else {
                    int maxLength = 30;
                    InputFilter[] filters = new InputFilter[1];
                    filters[0] = new InputFilter.LengthFilter(maxLength);
                    Id_number.setFilters(filters);
                }

            }
        });

        nationality_search.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                nationality_search.showDropDown();
                return false;
            }
        });

        apiViewModel.getResponseforVcheckuserMobile().observe(this, new Observer<VcheckuserModel>() {
            @Override
            public void onChanged(VcheckuserModel model) {
                try {
                    if (model != null) {
                        if (model.getResult() == 400 || model.getResult() == 404) {
                            new android.os.Handler().postDelayed(new Runnable() {
                                public void run() {
                                    mobile.setError(null);
                                }
                            }, 5000);
                            mobile.requestFocus();
                            mobile.setError("Mobile Number Already exist. Please use another Mobile Number");
                        } else {
                            SignupObjectPrepare();
                        }

                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            }
        });

        apiViewModel.getResponseforVcheckuserEmail().observe(this, new Observer<VcheckuserModel>() {
            @Override
            public void onChanged(VcheckuserModel model) {
                try {
                    if (model != null) {
                        if (model.getResult() == 404 || model.getResult() == 400) {
                            new android.os.Handler().postDelayed(new Runnable() {
                                public void run() {
                                    email.setError(null);
                                }
                            }, 5000);
                            email.requestFocus();
                            email.setError("This email is Already exist. Please use another email");
                        } else {
                            SignupObjectPrepare();
                        }
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
        // Blocklist visitor
        apiViewModel.getResponsecblacklistdetails().observe(this, new Observer<Blocklist_Model>() {
            @Override
            public void onChanged(Blocklist_Model model) {
                try {
                    if (model != null) {
                        Nationalitsblaclklist = model.getItem().getCountries();
                        for (int i = 0; i < Nationalitsblaclklist.size(); i++) {
                            Nationblacklist = Nationalitsblaclklist.get(i);
                        }
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });

        //Visitor_id_blocklist
        apiViewModel.getResponsevisitorblocklistdetails().observe(this, new Observer<BlockedvisitorrequestsModel>() {
            @Override
            public void onChanged(BlockedvisitorrequestsModel model) {
                try {
                    if (model != null) {
                        visitorblocklist = model.getItems();
                        for (int i = 0; i < visitorblocklist.size(); i++) {
                            getvisitorblocklistID = visitorblocklist.get(i).getIdnumber();
                            Vistiror_blockIDs.add(getvisitorblocklistID);
                        }

                        for (int i = 0; i < Vistiror_blockIDs.size(); i++) {
                            vistiorbloclist = Vistiror_blockIDs.get(i);
                        }
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            }
        });

        //mobile length for country code bases
        int initialMaxLength = getMaxLengthForCountryCode(ccp.getSelectedCountryCode());
        mobile.setFilters(new InputFilter[]{new InputFilter.LengthFilter(initialMaxLength)});

        ccp.setOnCountryChangeListener(() -> {
            int maxLength = getMaxLengthForCountryCode(ccp.getSelectedCountryCode());
            mobile.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
            mobile.getText().clear();
        });

        setData();
        visitorformdetails();


        InputFilter[] filters = new InputFilter[]{new NoSpaceInputFilter()};
        e_pname.setFilters(filters);
        nationality_search.setFilters(filters);

        btn_next.setOnClickListener(this);
        back_image.setOnClickListener(this);
        checkBox.setOnClickListener(this);
        linaer_privacy_policy.setOnClickListener(this);


    }

    // Function to check if the layout direction is right-to-left
    private boolean isRightToLeft() {
        return getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_RTL;
    }

    // Method to remove non-numeric characters from the Editable text
    private void removeNonNumericCharacters(Editable editable) {
        // Loop through each character in the text
        for (int i = 0; i < editable.length(); i++) {
            // Check if the character is not a digit
            if (!Character.isDigit(editable.charAt(i))) {
                // Remove the non-numeric character
                editable.delete(i, i + 1);
            }
        }
    }

    //disable auto click action after scann
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        e_pname.setInputType(InputType.TYPE_CLASS_NUMBER);
//        mobile.setInputType(InputType.TYPE_CLASS_NUMBER);
//        email.setInputType(InputType.TYPE_CLASS_NUMBER);
//        Id_number.setInputType(InputType.TYPE_CLASS_NUMBER);

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


    private static String extractCountryCode(String numberStr) {
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        String newcountryCode = "+966";
        try {
            Phonenumber.PhoneNumber numberProto = phoneUtil.parse(numberStr, "");
            newcountryCode = "+" + numberProto.getCountryCode();
            //This prints "Country code: 91"
        } catch (NumberParseException e) {
            System.err.println("NumberParseException was thrown: " + e.toString());
        }
        return newcountryCode;
    }

    private void disableTriggeredItems() {

        back_image.setFocusable(false);
        back_image.setFocusableInTouchMode(false);
        email.setFocusable(false);
        email.setFocusableInTouchMode(false);
        mobile.setFocusable(false);
        mobile.setFocusableInTouchMode(false);
        Id_number.setFocusable(false);
        Id_number.setFocusableInTouchMode(false);
        e_pname.setFocusable(false);
        e_pname.setFocusableInTouchMode(false);
        document_search.setFocusable(false);
        document_search.setFocusableInTouchMode(false);
        nationality_search.setFocusable(false);
        nationality_search.setFocusableInTouchMode(false);
        btn_next.setFocusable(false);
        btn_next.setFocusableInTouchMode(false);
        ccp.setFocusable(false);
        ccp.setFocusableInTouchMode(false);
        linear_ccp.setFocusable(false);
        linear_ccp.setFocusableInTouchMode(false);
        linear.setFocusable(false);
        linear.setFocusableInTouchMode(false);
        ccp1.setFocusable(false);
        ccp1.setFocusableInTouchMode(false);

        runthred();
    }

    private void runthred() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                finish();
                overridePendingTransition(0, 0);
                startActivity(getIntent());
                overridePendingTransition(0, 0);
                email.setInputType(InputType.TYPE_CLASS_NUMBER);
                back_image.setFocusable(true);
                back_image.setFocusableInTouchMode(true);
                email.setFocusable(true);
                email.setFocusableInTouchMode(true);
                mobile.setFocusable(true);
                mobile.setFocusableInTouchMode(true);
                Id_number.setFocusable(true);
                Id_number.setFocusableInTouchMode(true);
                e_pname.setFocusable(true);
                e_pname.setFocusableInTouchMode(true);
                document_search.setFocusable(true);
                document_search.setFocusableInTouchMode(true);
                nationality_search.setFocusable(true);
                nationality_search.setFocusableInTouchMode(true);
                btn_next.setFocusable(true);
                btn_next.setFocusableInTouchMode(true);
                ccp.setFocusable(true);
                ccp.setFocusableInTouchMode(true);
                linear_ccp.setFocusable(true);
                linear_ccp.setFocusableInTouchMode(true);
                linear.setFocusable(true);
                linear.setFocusableInTouchMode(true);
                ccp1.setFocusable(true);
                ccp1.setFocusableInTouchMode(true);
            }
        }, 500);
    }

    private void setData() {
        //company logo
        String c_Logo = Preferences.loadStringValue(getApplicationContext(), Preferences.company_Logo, "");
        if (c_Logo.equalsIgnoreCase("")) {
        } else {
            Glide.with(EnterYourDetailsActivity.this).load(c_Logo).into(company_logo);
        }
        String newVal = model.getIncomplete_data().getMobile();
        if (!newVal.equalsIgnoreCase("")) {
            String countrycode = extractCountryCode(newVal);
            newVal = newVal.substring(countrycode.length());
            countrycode = countrycode.substring(1);
            ccp.setDefaultCountryUsingPhoneCode(Integer.parseInt(countrycode));
            ccp.setCountryForNameCode(countrycode);
            ccp1.setDefaultCountryUsingPhoneCode(Integer.parseInt(countrycode));
            ccp1.setCountryForNameCode(countrycode);

        }
        email.setText(model.getIncomplete_data().getEmail().trim());
        mobile.setText(newVal);

        try {
            if (!model.getIncomplete_data().getEmail().equals("")) {
                liner_ccp.setVisibility(GONE);
                linear_ccp.setVisibility(View.VISIBLE);
                text_email.setVisibility(View.VISIBLE);
                text_email.setText(model.getIncomplete_data().getEmail());
                email.setEnabled(false);
                mobile.setEnabled(true);
                mobile.requestFocus();
                email.setVisibility(GONE);
            } else {
                text_mobile.setText(mobile.getText().toString());
                text_email.setVisibility(GONE);
                liner_ccp.setVisibility(View.VISIBLE);
                linear_ccp.setVisibility(GONE);
                mobile.setEnabled(false);
                ccp.setCcpClickable(false);
                ccp1.setCcpClickable(false);
                email.setEnabled(true);

                // Set layout direction to default (left-to-right)
                liner_ccp.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //mobile length for country code bases logic
    private int getMaxLengthForCountryCode(String countryCode) {
        if (ccp.getSelectedCountryCode().length() == 3) {
            return 9;
        } else if (ccp.getSelectedCountryCode().length() == 2) {
            return 10;
        } else if (ccp.getSelectedCountryCode().length() == 1) {
            return 11;
        } else {
            return 13;
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.linaer_privacy_policy:
                privacypolicypopup();
                break;
            case R.id.checkbox:
                if (checkBox.isChecked()) {
                    btn_next.setBackgroundColor(btn_next.getContext().getResources().getColor(R.color.colorPrimary));
                } else {
                    btn_next.setBackgroundColor(btn_next.getContext().getResources().getColor(R.color.light_gray));
                }
                break;
            case R.id.btn_next:
                AnimationSet animation1 = Conversions.animation();
                v.startAnimation(animation1);
                String regexStr = "^[0-9]*$";
                int minLength;
                int maxLength;
                if (ccp.getSelectedCountryCode().length() == 2) { // India
                    minLength = 10;
                    maxLength = 10;
                } else {
                    minLength = 9; // Default minLength for other countries
                    maxLength = 13; // Default maxLength for other countries
                }

                new Handler().postDelayed(() -> {
                    email.setError(null);
                    mobile.setError(null);
                    document_search.setError(null);
                    e_pname.setError(null);
                    Id_number.setError(null);
                    nationality_search.setError(null);
                }, 5000);

                if (email.getText().toString().length() > 0 && !isEmailValid(email.getText().toString())) {
                    email.requestFocus();
                    email.setError("Please enter valid email");
                } else if (mobile.getText().length() == 0) {
                    mobile.requestFocus();
                    mobile.setError("Enter your mobile");
                } else if (mobile.length() < minLength || mobile.length() > maxLength) {
                    mobile.requestFocus();
                    mobile.setError(getResources().getString(R.string.Invalidmobile_number));
                } else if (!mobile.getText().toString().trim().matches(regexStr)) {
                    mobile.requestFocus();
                    mobile.setError("Enter valid mobile");
                }
                else if (document_search.getText().length() == 0 && nationalityActive) {
                    document_search.requestFocus();
                    document_search.setError("Please select ID type");
                } else if (nationality.equals("") && nationalityActive) {
                    nationality_search.requestFocus();
                    nationality_search.setError("Please select nationality");
                } else if (!nationalitys.contains(nationality_search.getText().toString()) && nationalityActive) {
                    nationality_search.requestFocus();
                    nationality_search.setError("Please select nationality");
                } else if (Id_number.getText().length() == 0 && nationalityActive) {
                    Id_number.requestFocus();
                    Id_number.setError("Please enter ID number");
                } else if (e_pname.getText().length() == 0) {
                    e_pname.requestFocus();
                    e_pname.setError("Please enter name");
                } else {
                    boolean allFieldsValid = true;
                    for (int i = 0; i < VisitorformDetails_others.size(); i++) {
                        FormCustomAdapter.ViewHolderInput viewHolder = (FormCustomAdapter.ViewHolderInput) recyclerview.findViewHolderForAdapterPosition(i);
                        if (viewHolder != null) {
                            if (!VisitorformDetails_others.get(i).getLabel().equals("Nationality") && !VisitorformDetails_others.get(i).getLabel().equals("Belongings") && !VisitorformDetails_others.get(i).getLabel().equals("Vehicles") && VisitorformDetails_others.get(i).getActive() && viewHolder.edit_form.getText().length() == 0) {
                                viewHolder.edit_form.requestFocus();
                                viewHolder.edit_form.setError("Enter " + VisitorformDetails_others.get(i).getLabel());
                                allFieldsValid = false;
                                break;
                            }
                        }
                    }

                    if (allFieldsValid) {
                        if (P_policy.equalsIgnoreCase("true")) {
                            if (checkBox.isChecked()) {
                                if (getNationality_Status.equalsIgnoreCase("true")) {
                                    if (InterNetConnectivityCheck.isOnline(getApplicationContext())) {
//
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

                                            if (blocking.equals("true") && nationality != null && Nationalitsblaclklist.stream().map(String::toLowerCase).collect(Collectors.toList()).contains(nationality.toLowerCase())) {
                                                // Match nationality with blocklist (case-insensitive)
                                                SignupObjectPrepare();
                                                Intent intent1 = new Intent(getApplicationContext(), DeclinedActivity.class);
                                                startActivity(intent1);
                                            } else if (Vistiror_blockIDs.contains(Id_number.getText().toString())) {
                                                SignupObjectPrepare();
                                                Intent intent1 = new Intent(getApplicationContext(), DeclinedActivity.class);
                                                startActivity(intent1);
                                            } else {
                                                if (!model.getIncomplete_data().getEmail().equals("")) {
                                                    apiViewModel.vcheckuserMobile(getApplicationContext(), "mobile", ccp.getSelectedCountryCode() + mobile.getText().toString());
                                                } else {
                                                    if (email.getText().toString().equals("")) {
                                                        SignupObjectPrepare();
                                                    } else {
                                                        apiViewModel.vcheckuserEmail(getApplicationContext(), "email", email.getText().toString());
                                                    }
                                                }
                                            }
                                        }
                                    } else {
                                        DataManger.internetpopup(EnterYourDetailsActivity.this);
                                    }

                                } else {
                                    if (InterNetConnectivityCheck.isOnline(getApplicationContext())) {
//
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

                                            if (blocking.equals("true") && nationality != null && Nationalitsblaclklist.stream().map(String::toLowerCase).collect(Collectors.toList()).contains(nationality.toLowerCase())) {
                                                // Match nationality with blocklist (case-insensitive)
                                                SignupObjectPrepare();
                                                Intent intent1 = new Intent(getApplicationContext(), DeclinedActivity.class);
                                                startActivity(intent1);
                                            } else if (Vistiror_blockIDs.contains(Id_number.getText().toString())) {
                                                SignupObjectPrepare();
                                                Intent intent1 = new Intent(getApplicationContext(), DeclinedActivity.class);
                                                startActivity(intent1);
                                            } else {
                                                if (!model.getIncomplete_data().getEmail().equals("")) {
                                                    apiViewModel.vcheckuserMobile(getApplicationContext(), "mobile", ccp.getSelectedCountryCode() + mobile.getText().toString());
                                                } else {
                                                    if (email.getText().toString().equals("")) {
                                                        SignupObjectPrepare();
                                                    } else {
                                                        apiViewModel.vcheckuserEmail(getApplicationContext(), "email", email.getText().toString());
                                                    }
                                                }
                                            }
                                        }
                                    } else {
                                        DataManger.internetpopup(EnterYourDetailsActivity.this);
                                    }
                                }
                            } else {
                                // Create custom Toast
                                LayoutInflater inflater = getLayoutInflater();
                                View layout = inflater.inflate(R.layout.toast_layout, (ViewGroup) findViewById(R.id.toast_layout_root));

                                Toast toast = new Toast(getApplicationContext());
                                toast.setGravity(Gravity.BOTTOM, 0, 430);
                                toast.setDuration(Toast.LENGTH_SHORT);
                                toast.setView(layout);
                                toast.show();

                            }
                        } else {
                            if (getNationality_Status.equalsIgnoreCase("true")) {
                                if (InterNetConnectivityCheck.isOnline(getApplicationContext())) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

                                        if (blocking.equals("true") && nationality != null && Nationalitsblaclklist.stream().map(String::toLowerCase).collect(Collectors.toList()).contains(nationality.toLowerCase())) {
                                            // Match nationality with blocklist (case-insensitive)
                                            SignupObjectPrepare();
                                            Intent intent1 = new Intent(getApplicationContext(), DeclinedActivity.class);
                                            startActivity(intent1);
                                        } else if (Vistiror_blockIDs.contains(Id_number.getText().toString())) {
                                            SignupObjectPrepare();
                                            Intent intent1 = new Intent(getApplicationContext(), DeclinedActivity.class);
                                            startActivity(intent1);
                                        } else {
                                            if (!model.getIncomplete_data().getEmail().equals("")) {
                                                apiViewModel.vcheckuserMobile(getApplicationContext(), "mobile", ccp.getSelectedCountryCode() + mobile.getText().toString());
                                            } else {
                                                if (email.getText().toString().equals("")) {
                                                    SignupObjectPrepare();
                                                } else {
                                                    apiViewModel.vcheckuserEmail(getApplicationContext(), "email", email.getText().toString());
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    DataManger.internetpopup(EnterYourDetailsActivity.this);
                                }
                            } else {
                                if (InterNetConnectivityCheck.isOnline(getApplicationContext())) {//

                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

                                        if (blocking.equals("true") && nationality != null && Nationalitsblaclklist.stream().map(String::toLowerCase).collect(Collectors.toList()).contains(nationality.toLowerCase())) {
                                            // Match nationality with blocklist (case-insensitive)
                                            SignupObjectPrepare();
                                            Intent intent1 = new Intent(getApplicationContext(), DeclinedActivity.class);
                                            startActivity(intent1);
                                        } else if (Vistiror_blockIDs.contains(Id_number.getText().toString())) {
                                            SignupObjectPrepare();
                                            Intent intent1 = new Intent(getApplicationContext(), DeclinedActivity.class);
                                            startActivity(intent1);
                                        } else {
                                            if (!model.getIncomplete_data().getEmail().equals("")) {
                                                apiViewModel.vcheckuserMobile(getApplicationContext(), "mobile", ccp.getSelectedCountryCode() + mobile.getText().toString());
                                            } else {
                                                if (email.getText().toString().equals("")) {
                                                    SignupObjectPrepare();
                                                } else {
                                                    apiViewModel.vcheckuserEmail(getApplicationContext(), "email", email.getText().toString());
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    DataManger.internetpopup(EnterYourDetailsActivity.this);
                                }
                            }

                        }
                    }


                }
                break;
            case R.id.back_image:
                Intent intent = new Intent(getApplicationContext(), VisitorLoginActivity.class);
                startActivity(intent);
                break;
        }
    }

    protected void registoreNetWorkBroadcast() {
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,
                new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    private void visitorformdetails() {
        DataManger dataManger = DataManger.getDataManager();
        dataManger.visitorformdetails(new Callback<VisitorformDetailsModel>() {
            @Override
            public void onResponse(Call<VisitorformDetailsModel> call, Response<VisitorformDetailsModel> response) {
                final VisitorformDetailsModel model = response.body();
                try {
                    if (model.getItem() != null) {
                        for (int i = 0; i < model.getItem().getOther().size(); i++) {

                            if (model.getItem().getOther().get(i).getModel().equalsIgnoreCase("nation")){
                                nationalityStatus =  model.getItem().getOther().get(i).getStatus();
                                nationalityActive =  model.getItem().getOther().get(i).getActive();
                                if (nationalityStatus){
                                    document_search.setVisibility(View.VISIBLE);
                                }
                            }


                            if (model.getItem().getOther().get(i).getStatus()) {
                                VisitorFormDetailsOtherArray other = new VisitorFormDetailsOtherArray();
                                other.setLabel(model.getItem().getOther().get(i).getLabel());
                                other.setModel(model.getItem().getOther().get(i).getModel());
                                other.setData(model.getItem().getOther().get(i).getData());
                                other.setStatus(model.getItem().getOther().get(i).getStatus());
                                other.setDepends(model.getItem().getOther().get(i).getDepends());
                                other.setProfessional(model.getItem().getOther().get(i).getProfessional());
                                other.setDisabled(model.getItem().getOther().get(i).getDisabled());
                                other.setActive(model.getItem().getOther().get(i).getActive());
                                other.set_id(model.getItem().getOther().get(i).get_id());

                                VisitorformDetails_others.add(other);

                            }
                        }

                        //today
                        formCustomAdapter = new FormCustomAdapter(getApplicationContext(), VisitorformDetails_others);
                        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
                        recyclerview.setLayoutManager(manager);
                        //set adapter
                        recyclerview.setAdapter(formCustomAdapter);


                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(Call<VisitorformDetailsModel> call, Throwable t) {
            }
        }, EnterYourDetailsActivity.this);
    }

    private void SignupObjectPrepare() {

        model.getIncomplete_data().setMobile("+" + ccp.getSelectedCountryCode() + mobile.getText().toString());
        model.getIncomplete_data().setEmail(email.getText().toString());
        model.getIncomplete_data().setNation(nationality);
        model.getIncomplete_data().setDocument((float) doc);
        model.getIncomplete_data().setIdnumber(Id_number.getText().toString());
        model.getIncomplete_data().setName(e_pname.getText().toString());

        JSONObject jsonObj_ = new JSONObject();
        ArrayList<Belongings> belongings = new ArrayList<>();
        Belongings belong1 = new Belongings();
        belong1.setData("");
        belong1.setLabel("None");
        belong1.setStatus(false);
        Belongings belong2 = new Belongings();
        belong2.setData("");
        belong2.setLabel("Others");
        belong2.setStatus(false);
        Belongings belong3 = new Belongings();
        belong3.setData("");
        belong3.setLabel("Laptop");
        belong3.setStatus(false);
        Belongings belong4 = new Belongings();
        belong4.setData("");
        belong4.setLabel("Toolbox");
        belong4.setStatus(false);
        belongings.add(belong1);
        belongings.add(belong2);
        belongings.add(belong3);
        belongings.add(belong4);
        ArrayList<VisitorFormDetailsOtherArray> others = new ArrayList<>();
        VisitorFormDetailsOtherArray other1 = new VisitorFormDetailsOtherArray();
        VisitorFormDetailsOtherArray other3 = new VisitorFormDetailsOtherArray();
        other3.setData(nationality);
        other3.setLabel("Nationality");
        other3.setStatus(true);
        other3.setActive(true);
        other3.setDisabled(true);
        other3.setDepends(false);
        other3.setProfessional(false);
        other3.setModel("nation");
        VisitorformDetails_others.add(other3);
        ArrayList<Vehicles> vehicles = new ArrayList<>();
        Vehicles Vehicle1 = new Vehicles();
        Vehicle1.setData("");
        Vehicle1.setLabel("Name");
        Vehicle1.setStatus(true);
        Vehicle1.setModel("name");
        Vehicles Vehicle2 = new Vehicles();
        Vehicle2.setData("");
        Vehicle2.setLabel("Number");
        Vehicle2.setStatus(true);
        Vehicle2.setModel("number");
        vehicles.add(Vehicle1);
        vehicles.add(Vehicle2);
        JSONArray vehicles1 = new JSONArray();
        JSONArray belongings1 = new JSONArray();
        JSONArray others1 = new JSONArray();
        try {
            vehicles1 = new JSONArray();
            for (int i = 0; i < vehicles.size(); i++) {
                vehicles1.put(vehicles.get(i).getvehicles());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            others1 = new JSONArray();
            for (int i = 0; i < VisitorformDetails_others.size(); i++) {
                others1.put(VisitorformDetails_others.get(i).getothers());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            belongings1 = new JSONArray();
            for (int i = 0; i < belongings.size(); i++) {
                belongings1.put(belongings.get(i).getbelongings());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        MobileDataAddress mobiledata = new MobileDataAddress();
        mobiledata.setCountryCode(ccp.getSelectedCountryNameCode());
        mobiledata.setE164Number("+" + ccp.getSelectedCountryCode() + mobile.getText().toString());
        mobiledata.setInternationalNumber("+" + ccp.getSelectedCountryCode() + mobile.getText().toString());
        mobiledata.setNationalNumber("0" + mobile.getText().toString());
        mobiledata.setNumber(mobile.getText().toString());
        mobiledata.setDialCode("+" + ccp.getSelectedCountryCode());

        try {
            jsonObj_.put("belongings", belongings1);
            jsonObj_.put("dob", "");
            jsonObj_.put("comp_id", "");
            jsonObj_.put("document", doc + 1);
            jsonObj_.put("documents", new JSONArray());
            jsonObj_.put("email", email.getText().toString().trim());
            jsonObj_.put("formtype", "insert");
            jsonObj_.put("idnumber", Id_number.getText().toString());
            jsonObj_.put("mobile", "+" + ccp.getSelectedCountryCode() + mobile.getText().toString());
            jsonObj_.put("mobilecode", "IN");
            jsonObj_.put("mobiledata", mobiledata.getMobiledata());
            jsonObj_.put("mverify", 1);
            jsonObj_.put("name", e_pname.getText().toString());
            jsonObj_.put("other", others1);
            jsonObj_.put("pic", new JSONArray());
            jsonObj_.put("random", "4458");
            jsonObj_.put("vehicles", vehicles1);
            jsonObj_.put("verify", 1);
            apiViewModel.visitorSignup(getApplicationContext(), jsonObj_);

            apiViewModel.getResponseforVisitorSignup().observe(this, model -> {
                if (!email.getText().toString().equals("")) {
                    apiViewModel.getcvisitordetails(getApplicationContext(), "", emp_id, email.getText().toString(), location_id);
                } else {
                    apiViewModel.getcvisitordetails(getApplicationContext(), "", emp_id, ccp.getSelectedCountryCode() + mobile.getText().toString(), location_id);
                }
            });


            apiViewModel.getResponseforNdaActiveDetails().observe(this, model1 -> ndamodel = model1);

            apiViewModel.getResponseforCVisitor().observe(this, model -> {

                try {
                    if (ndamodel.getResult() == 200) {
                        if (nda_Data.equals("true")) {
                            Intent intent1 = new Intent(getApplicationContext(), NDA_FormActivity.class);
                            intent1.putExtra("model_key", model);
                            startActivity(intent1);
                        } else {
                            Intent intent1 = new Intent(getApplicationContext(), MeetingRequestActivity.class);
                            intent1.putExtra("model_key", model);
                            startActivity(intent1);
                        }
                    } else {
                        Intent intent1 = new Intent(getApplicationContext(), MeetingRequestActivity.class);
                        intent1.putExtra("model_key", model);
                        startActivity(intent1);
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            });
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

    public void countryList() {
        String jsonFileString = getJsonFromAssets(getApplicationContext(), "countrycodes.json");
        Gson gson = new Gson();
        Type listUserType = new TypeToken<List<CountryCodes>>() {
        }.getType();
        List<CountryCodes> nationalist_list = gson.fromJson(jsonFileString, listUserType);
        nationalitys = new ArrayList<>();
        nationalitys.clear();
        if (nationalist_list != null && nationalist_list.size() > 0) {
            for (int i = 0; i < nationalist_list.size(); i++) {
                nationalitys.add(nationalist_list.get(i).getName());

                NationalitysAdapter invitesadapter = new NationalitysAdapter(EnterYourDetailsActivity.this, R.layout.row, R.id.lbl_name, nationalitys);
                nationality_search.setThreshold(0);
                nationality_search.setAdapter(invitesadapter);
            }

        }
    }

    private void privacypolicypopup() {

        final Dialog dialog = new Dialog(EnterYourDetailsActivity.this);
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.privacypolicy_popup);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        Button btn_ok = dialog.findViewById(R.id.btn_ok);
        TextView txt_content = dialog.findViewById(R.id.txt_content);


        apiViewModel.getResponseforPrivacypolicyDetails().observe(this, model1 -> {
            privicymodel = model1;
            if (privicymodel != null && privicymodel.getItems() != null && privicymodel.getItems().getContent() != null) {
                try {
                    String htmlText = privicymodel.getItems().getContent();
                    Document doc = Jsoup.parse(htmlText);
                    String xmlText = doc.outerHtml();
                    txt_content.setText(Html.fromHtml(xmlText));

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationSet animationp = Conversions.animation();
                v.startAnimation(animationp);
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    static String getJsonFromAssets(Context context, String fileName) {
        String jsonString;
        try {
            InputStream is = context.getAssets().open(fileName);

            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            jsonString = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return jsonString;
    }

    public class FormCustomAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        ArrayList<VisitorFormDetailsOtherArray> form_ls;
        private Context context;

        public FormCustomAdapter(Context context, ArrayList<VisitorFormDetailsOtherArray> inputData) {
            this.form_ls = inputData;
            this.context = context;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = null;
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.form_custom_list_items, parent, false);
            return new ViewHolderInput(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
            ViewHolderInput Holder = (ViewHolderInput) holder;
            VisitorFormDetailsOtherArray formData = form_ls.get(position);

            if (formData.getLabel().equals("Nationality") || formData.getLabel().equals("Belongings") || formData.getLabel().equals("Vehicles")) {
                Holder.linear_form.setVisibility(View.GONE);
            } else {
                if (formData.getStatus()) {
                    formData.setData("");
                    Holder.linear_form.setVisibility(View.VISIBLE);
                    Holder.edit_form.setHint(formData.getLabel());
//                    Holder.edit_form.setText("");
                    if (formData.getLabel().equals("Organization")) {
                        Holder.edit_form.setHint(getResources().getString(R.string.enter_EnterOrganization));
                    }
                    if (formData.getLabel().equals("Designation")) {
                        Holder.edit_form.setHint(getResources().getString(R.string.enter_Enterdesignation));
                    }
                    if (formData.getActive()) {
                        Holder.edit_form.setHint(formData.getLabel() + " *");
                        if (formData.getLabel().equals("Organization")) {
                            Holder.edit_form.setHint(getResources().getString(R.string.enter_EnterOrganization) + " *");
                        }
                        if (formData.getLabel().equals("Designation")) {
                            Holder.edit_form.setHint(getResources().getString(R.string.enter_Enterdesignation) + " *");
                        }
                    }
                    // Reset error
                    Holder.edit_form.setError(null);
                } else {
                    Holder.linear_form.setVisibility(View.GONE);
                }

                InputFilter[] filters = new InputFilter[]{new EnterYourDetailsActivity.NoSpaceInputFilter()};

                Holder.edit_form.setFilters(filters);
            }

            Holder.edit_form.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    // Update data in the list when text changes
                    formData.setData(s.toString());

                }

            });


        }


        @Override
        public int getItemCount() {
            return form_ls.size();
        }

        public class ViewHolderInput extends RecyclerView.ViewHolder {
            //_today
            public LinearLayout linear_form;
            public EditText edit_form;

            public ViewHolderInput(@NonNull View itemView) {
                super(itemView);
                linear_form = itemView.findViewById(R.id.linear_form);
                edit_form = itemView.findViewById(R.id.edit_form);
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), VisitorLoginActivity.class);
        startActivity(intent);
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