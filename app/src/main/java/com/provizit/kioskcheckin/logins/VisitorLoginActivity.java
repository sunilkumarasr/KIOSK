package com.provizit.kioskcheckin.logins;

import static android.view.View.GONE;
import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;
import static com.provizit.kioskcheckin.services.Conversions.convertArabicToEnglish;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.ViewCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AnimationSet;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.hbb20.CountryCodePicker;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;
import com.provizit.kioskcheckin.BuildConfig;
import com.provizit.kioskcheckin.activities.AlreadyCheckedInActivity;
import com.provizit.kioskcheckin.activities.MaterialPermitActivity;
import com.provizit.kioskcheckin.activities.WarningScreens.LocationValidationMeetingActivity;
import com.provizit.kioskcheckin.activities.WarningScreens.InValidPermitActivity;
import com.provizit.kioskcheckin.activities.WorkPermitActivity;
import com.provizit.kioskcheckin.config.ConnectionReceiver;
import com.provizit.kioskcheckin.config.InterNetConnectivityCheck;
import com.provizit.kioskcheckin.config.ViewController;
import com.provizit.kioskcheckin.mvvm.ApiViewModel;
import com.provizit.kioskcheckin.R;
import com.provizit.kioskcheckin.services.BlockedvisitorrequestsModel;
import com.provizit.kioskcheckin.services.Blocklist_Model;
import com.provizit.kioskcheckin.services.Conversions;
import com.provizit.kioskcheckin.api.DataManger;
import com.provizit.kioskcheckin.services.GetNdaActiveDetailsModel;
import com.provizit.kioskcheckin.utilities.CompanyData;
import com.provizit.kioskcheckin.utilities.GetvisitorrequestblocklistModel;
import com.provizit.kioskcheckin.utilities.IncompleteData;
import com.provizit.kioskcheckin.config.Preferences;
import com.provizit.kioskcheckin.utilities.WorkPermit.ContractorsData;
import com.provizit.kioskcheckin.utilities.WorkPermit.SubContractorsData;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import android.text.InputFilter;
import android.widget.Toast;

public class VisitorLoginActivity extends AppCompatActivity implements View.OnClickListener {

    //internet connection
    BroadcastReceiver broadcastReceiver;
    RelativeLayout relative_internet;
    RelativeLayout relative_ui;
    LinearLayout l_logout, Are_signout;
    ProgressDialog progress;
    Button btn_proceed, btn_signout, btn_cancle;
    TextInputLayout EmailInput, MobileInput, txt_input_password;
    EditText visitor_email, visitor_mobile, password_signout;
    String emailPattern, emp_id, location_id, comp_id_val;
    TextView text_english;
    ImageView clearEmail, clearMobile;
    LinearLayout linaer_logout, linearEmail;
    ImageView img_qr, img_menu, logo, logo1;
    LinearLayout linear_Switch_selection;
    String status_check = "0";
    TextView txt_input_type, text_visitorselfservice;
    Switch use_mobile;
    ScrollView scrollView;
    LinearLayout linear_mobile;
    CardView cardview_usb;
    CardView cardview_barcode;
    boolean text_isClick = false;
    LinearLayout relative2;
    CountryCodePicker ccp;
    ApiViewModel apiViewModel;

    //usb qr scanner
    String usbScannedData = "";
    String countrycode = "+966";
    boolean spaceValstatus = false;
    int spaceVal = 0;

    //current date and time stamp
    long todayStartTimestamp = 0;
    long cTimeStamp = 0;

    //without otp
    String nda_Data = "false";
    GetNdaActiveDetailsModel ndamodel;
    ArrayList<String> Nationalitsblaclklist;
    String supertype = "";
    String type = "";
    String Nationblacklist = "";
    ArrayList<GetvisitorrequestblocklistModel> visitorblocklist;
    String getvisitorblocklistID = "";
    String vistiorbloclist;
    ArrayList<String> Vistiror_blockIDs;
    String blocking = "false";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visitor_login);

        inits();

        emailPattern = "[a-zA-Z0-9._-]+@[a-z-]+\\.+[a-z]+";

        progress = new ProgressDialog(this);
        progress.setTitle(getResources().getString(R.string.Loading));
        progress.setMessage(getResources().getString(R.string.whileloading));
        progress.setCancelable(true);
        progress.setCanceledOnTouchOutside(true);

        txt_input_password = findViewById(R.id.txt_input_password);
        ViewCompat.setLayoutDirection(txt_input_password, ViewCompat.LAYOUT_DIRECTION_LTR);
        password_signout = findViewById(R.id.password_signout);
        img_menu = findViewById(R.id.img_menu);
        l_logout = findViewById(R.id.l_logout);
        Are_signout = findViewById(R.id.Are_signout);
        btn_signout = findViewById(R.id.btn_signout);
        btn_cancle = findViewById(R.id.btn_cancle);
        scrollView = findViewById(R.id.scrollView);
        EmailInput = findViewById(R.id.EmailInput);
        MobileInput = findViewById(R.id.MobileInput);
        btn_proceed = findViewById(R.id.btn_proceed);
        btn_proceed.setTypeface(ResourcesCompat.getFont(this, R.font.arbfonts_dinnextttlrabic_medium));
        EmailInput.setTypeface(ResourcesCompat.getFont(this, R.font.arbfonts_dinnextttlrabic_regular));
        MobileInput.setTypeface(ResourcesCompat.getFont(this, R.font.arbfonts_dinnextttlrabic_regular));
        visitor_mobile = findViewById(R.id.visitor_mobile);
//        visitor_mobile.requestFocus();
        visitor_email = findViewById(R.id.visitor_email);
        text_english = findViewById(R.id.text_english);
        clearEmail = findViewById(R.id.clearEmail);
        clearMobile = findViewById(R.id.clearMobile);
        linearEmail = findViewById(R.id.linearEmail);
//        visitor_mobile.setInputType(InputType.TYPE_CLASS_NUMBER);
        linear_Switch_selection = findViewById(R.id.linear_Switch_selection);
        txt_input_type = findViewById(R.id.txt_input_type);
        use_mobile = findViewById(R.id.use_mobile);
        linaer_logout = findViewById(R.id.linaer_logout);
        text_visitorselfservice = (TextView) findViewById(R.id.text_visitorselfservice);
        linear_mobile = findViewById(R.id.linear_mobile);
        logo = findViewById(R.id.logo);
        logo1 = findViewById(R.id.logo1);
        cardview_usb = findViewById(R.id.cardview_usb);
        cardview_barcode = findViewById(R.id.cardview_barcode);
        img_qr = findViewById(R.id.img_qr);
        relative2 = findViewById(R.id.relative2);
        ViewCompat.setLayoutDirection(relative2, ViewCompat.LAYOUT_DIRECTION_LTR);
        ccp = (CountryCodePicker) findViewById(R.id.ccp);
        ccp.setDefaultCountryUsingPhoneCode(Integer.parseInt("966"));
        ccp.setCountryForNameCode("966");
        emp_id = Preferences.loadStringValue(getApplicationContext(), Preferences.email_id, "");
        comp_id_val = Preferences.loadStringValue(getApplicationContext(), Preferences.Comp_id, "");
        location_id = Preferences.loadStringValue(getApplicationContext(), Preferences.location_id, "");
        String language = Preferences.loadStringValue(getApplicationContext(), Preferences.language, "");
        apiViewModel = new ViewModelProvider(VisitorLoginActivity.this).get(ApiViewModel.class);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String VersionName = sharedPreferences.getString("VersionName", "1.0");
        TextView txtVersion = findViewById(R.id.txtVersion);
        txtVersion.setText(VersionName);

        //save Shared Preferences
        Preferences.saveStringValue(VisitorLoginActivity.this, Preferences.Login_Status, "Login");
        Preferences.saveStringValue(VisitorLoginActivity.this, Preferences.nda_Data, "false");
        Preferences.saveStringValue(VisitorLoginActivity.this, Preferences.badge_Data, "false");
        Preferences.saveStringValue(VisitorLoginActivity.this, Preferences.pic_Data, "false");
        Preferences.saveStringValue(VisitorLoginActivity.this, Preferences.accept_Data, "false");

        //without otp
        nda_Data = Preferences.loadStringValue(getApplicationContext(), Preferences.nda_Data, "");
        blocking = Preferences.loadStringValue(getApplicationContext(), Preferences.blocking, "");
        apiViewModel.getndafdetials(getApplicationContext(), "id", "active");
        apiViewModel.getResponseforNdaActiveDetails().observe(this, model1 -> ndamodel = model1);
        Nationalitsblaclklist = new ArrayList<>();
        Vistiror_blockIDs = new ArrayList<>();
        String Comp_id = Preferences.loadStringValue(getApplicationContext(), Preferences.Comp_id, "");
        //Visitor Blocklist
        apiViewModel.getcblacklistdetails(getApplicationContext(), Comp_id);
        //Vistior ID_block
        apiViewModel.getblockedvisitorrequests(getApplicationContext(), Comp_id, "");
        // Blocklist visitor
        apiViewModel.getResponsecblacklistdetails().observe(this, new Observer<Blocklist_Model>() {
            @Override
            public void onChanged(Blocklist_Model model) {
                try {
                    // Check if the blocklist is not null and clear the cached blocklist
                    if (Nationalitsblaclklist != null) {
                        Nationalitsblaclklist.clear(); // Clear the cached blocklist
                    } else {
                        // Initialize it if it's null
                        Nationalitsblaclklist = new ArrayList<>();
                    }

                    // Update blocklist with new data from the API
                    Nationalitsblaclklist.addAll(model.getItem().getCountries());

                    supertype = model.getItem().getSupertype();
                    type = model.getItem().getType();

                    for (int i = 0; i < Nationalitsblaclklist.size(); i++) {
                        Nationblacklist = Nationalitsblaclklist.get(i);
                    }

                    // Log the updated blocklist for debugging purposes
                    Log.d("Blocklist", "Updated Blocklist: " + Nationalitsblaclklist);

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
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

        currentDate();

        visitor_mobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
//                // Remove non-digit characters
//                String digits = s.toString();
//
//                // Get base max length from country code
//                String countryCode = ccp.getSelectedCountryCode();
//                int maxLength = getMaxLengthForCountryCode(countryCode);
//                Log.e("countryCode_",countryCode);
//
//                // Special UAE logic: 10 digits if starts with 0, else 9
//                if (countryCode.equalsIgnoreCase("966")) {
//                    if (digits.startsWith("0")) {
//                        Log.e("countrydigits_",digits);
//                        maxLength = 10;
//                        visitor_mobile.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
//                    } else {
//                        maxLength = 9;
//                    }
//                }
//                // Special India logic: 10 digits if starts with 0, else 9
//                if (countryCode.equalsIgnoreCase("91")) {
//                    if (digits.startsWith("0")) {
//                        Log.e("countrydigits_",digits);
//                        maxLength = 11;
//                        visitor_mobile.setFilters(new InputFilter[]{new InputFilter.LengthFilter(11)});
//                    } else {
//                        maxLength = 10;
//                    }
//                }
//
//                // Trim to allowed length
//                if (digits.length() > maxLength) {
//                    digits = digits.substring(0, maxLength);
//                }
//                // Prevent infinite loop
//                if (!s.toString().equalsIgnoreCase(digits)) {
//                    visitor_mobile.removeTextChangedListener(this);
//                    s.replace(0, s.length(), digits);
//                    visitor_mobile.addTextChangedListener(this);
//                }

                // Remove non-numeric characters from the EditText
                removeNonNumericCharacters(s);
            }
        });

        // NoSpaceInputFilter
        InputFilter[] filters = new InputFilter[]{new NoSpaceInputFilter()};
        visitor_email.setFilters(filters);

        // QR code generation
        String qrUrl = Preferences.loadStringValue(getApplicationContext(), Preferences.qrUrl, "");
        if (!qrUrl.isEmpty()) {  // Check if QR URL is not empty
            MultiFormatWriter mWriter = new MultiFormatWriter();
            try {
                BitMatrix mMatrix = mWriter.encode(qrUrl, BarcodeFormat.QR_CODE, 400, 400);
                BarcodeEncoder mEncoder = new BarcodeEncoder();
                Bitmap mBitmap = mEncoder.createBitmap(mMatrix);
                img_qr.setImageBitmap(mBitmap);
            } catch (WriterException e) {
                // Handle the exception with proper logging
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, "WriterException caught: " + e.getMessage(), e);
                } else {
                    Log.e(TAG, "Error generating QR code.", e);
                }
            }
        } else {
            // Optional: Handle the case where qrUrl is empty
            Log.w(TAG, "QR URL is empty. QR code will not be generated.");
        }

        if (language.equals("ar")) {
            text_english.setText("English");
            text_isClick = true;
            Locale myLocale = new Locale("ar");
            DataManger.appLanguage = "ar";
            Resources res = getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = myLocale;
            conf.setLayoutDirection(myLocale);
            res.updateConfiguration(conf, dm);
        } else {
            text_isClick = false;
            Locale myLocale = new Locale("en");
            DataManger.appLanguage = "en";
            Resources res = getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = myLocale;
            conf.setLayoutDirection(myLocale);
            res.updateConfiguration(conf, dm);
        }

        apiViewModel.getResponsecheckinuserlogin().observe(this, model -> {
            progress.dismiss();
            try {
                if (model != null) {
                    Integer statuscode = model.getResult();
                    Integer successcode = 200, failurecode = 401, not_verified = 404;
                    if (statuscode.equals(failurecode)) {

                    } else if (statuscode.equals(not_verified)) {

                    } else if (statuscode.equals(successcode)) {
                        CompanyData items = new CompanyData();
                        items = model.getItems();
                        if (items.getRoleDetails().isCheckin()) {
                            ViewController.clearCache(VisitorLoginActivity.this);
                            Preferences.saveStringValue(getApplicationContext(), "status", "failed");
                            Preferences.deleteSharedPreferences(getApplicationContext());
                            Intent intent = new Intent(VisitorLoginActivity.this, AdminLoginActivity.class);
                            startActivity(intent);
                        }
                    } else {
                        showAlertDialog();
                    }
                } else {

                    AlertDialog alertDialog = new AlertDialog.Builder(VisitorLoginActivity.this)
                            .setMessage("Please provide valid credentials!")
                            .setTitle("Access denied")
                            .setCancelable(false)
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = getIntent();
                                    finish(); // Finish the current activity
                                    overridePendingTransition(0, 0); // Disable animation
                                    startActivity(intent);
                                    overridePendingTransition(0, 0);
                                }
                            })
                            .show();
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        });

        apiViewModel.getuserLDetails(getApplicationContext(), "visitor");

        apiViewModel.getResponseforcompany().observe(this, model -> {
            try {
                if (model.getItems().getPic() != null) {
                    if (model.getItems().getPic() != null && model.getItems().getPic().size() != 0) {
                        //preferences
                        if (language.equals("ar")) {
                            Glide.with(VisitorLoginActivity.this)
                                    .load(DataManger.IMAGE_URL + "/uploads/" + Comp_id + "/" + model.getItems().getA_pic().get(model.getItems().getA_pic().size() - 1))
                                    .into(logo1);

                            Preferences.saveStringValue(VisitorLoginActivity.this, Preferences.company_Logo, DataManger.IMAGE_URL + "/uploads/" + Comp_id + "/" + model.getItems().getA_pic().get(model.getItems().getA_pic().size() - 1));

                            Preferences.saveStringValue(VisitorLoginActivity.this, Preferences.logoPass, model.getItems().getA_pic().get(model.getItems().getA_pic().size() - 1));
                        } else {
                            Glide.with(VisitorLoginActivity.this)
                                    .load(DataManger.IMAGE_URL + "/uploads/" + Comp_id + "/" + model.getItems().getPic().get(model.getItems().getPic().size() - 1))
                                    .into(logo1);
                            Preferences.saveStringValue(VisitorLoginActivity.this, Preferences.company_Logo, DataManger.IMAGE_URL + "/uploads/" + Comp_id + "/" + model.getItems().getPic().get(model.getItems().getPic().size() - 1));


                            Preferences.saveStringValue(VisitorLoginActivity.this, Preferences.logoPass, model.getItems().getPic().get(model.getItems().getPic().size() - 1));
                        }
                    }
                }
                if (model.getItems().getVisitor().getNdaform() != null) {

                    if (model.getItems().getVisitor().getNdaform()) {
                        Preferences.saveStringValue(VisitorLoginActivity.this, Preferences.nda_Data, "true");
                    } else {
                        Preferences.saveStringValue(VisitorLoginActivity.this, Preferences.nda_Data, "false");
                    }
                }

                if (model.getItems().getVisitor().getBadge() != null) {

                    if (model.getItems().getVisitor().getBadge()) {
                        Preferences.saveStringValue(VisitorLoginActivity.this, Preferences.badge_Data, "true");
                    } else {
                        Preferences.saveStringValue(VisitorLoginActivity.this, Preferences.badge_Data, "false");
                    }

                }

                if (model.getItems().getVisitor().getPic() != null) {

                    if (model.getItems().getVisitor().getPic()) {
                        Preferences.saveStringValue(VisitorLoginActivity.this, Preferences.pic_Data, "true");
                    } else {
                        Preferences.saveStringValue(VisitorLoginActivity.this, Preferences.pic_Data, "false");
                    }
                }

                if (model.getItems().getVisitor().getAcceptance() != null) {

                    if (model.getItems().getVisitor().getAcceptance()) {
                        Preferences.saveStringValue(VisitorLoginActivity.this, Preferences.accept_Data, "true");
                    } else {
                        Preferences.saveStringValue(VisitorLoginActivity.this, Preferences.accept_Data, "false");
                    }
                }

                if (model.getItems().getVisitor().getP_policy() != null) {

                    if (model.getItems().getVisitor().getP_policy()) {
                        Preferences.saveStringValue(VisitorLoginActivity.this, Preferences.P_policy, "true");
                    } else {
                        Preferences.saveStringValue(VisitorLoginActivity.this, Preferences.P_policy, "false");
                    }

                }

                if (model.getItems().getVisitor().getBlocking() != null) {

                    if (model.getItems().getVisitor().getBlocking()) {
                        Preferences.saveStringValue(VisitorLoginActivity.this, Preferences.blocking, "true");
                    } else {
                        Preferences.saveStringValue(VisitorLoginActivity.this, Preferences.blocking, "false");
                    }

                }


            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        });

        //switch to mail to mobile
        use_mobile.setOnCheckedChangeListener((compoundButton, b) -> {
            visitor_email.setText("");
            visitor_mobile.setText("");
            if (b) {
                status_check = "1";
                use_mobile.setChecked(true);
                txt_input_type.setText(getResources().getString(R.string.visitor_Usemobile));
                linear_mobile.setVisibility(View.GONE);
                linearEmail.setVisibility(View.VISIBLE);
                visitor_email.requestFocus();
            } else {
                status_check = "0";
                use_mobile.setChecked(false);
                txt_input_type.setText(getResources().getString(R.string.visitor_Useemail));
                linear_mobile.setVisibility(View.VISIBLE);
                linearEmail.setVisibility(View.GONE);
                visitor_mobile.requestFocus();
            }
        });

        //mobile length for country code bases
        int initialMaxLength = getMaxLengthForCountryCode(ccp.getSelectedCountryCode());
        visitor_mobile.setFilters(new InputFilter[]{new InputFilter.LengthFilter(initialMaxLength)});

        ccp.setOnCountryChangeListener(() -> {
            int maxLength = getMaxLengthForCountryCode(ccp.getSelectedCountryCode());
            visitor_mobile.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
            visitor_mobile.getText().clear();
        });

        clearMobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int length = visitor_mobile.getText().length();

                if (length > 0) {
                    visitor_mobile.requestFocus();
                    visitor_mobile.setSelection(length); // Cursor at the end

                    // Simulate a single backspace (delete last character)
                    visitor_mobile.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
                    visitor_mobile.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DEL));
                }
            }
        });


        clearEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int length = visitor_email.getText().length();

                if (length > 0) {
                    visitor_email.requestFocus();
                    visitor_email.setSelection(length); // Cursor at the end

                    // Simulate a single backspace (delete last character)
                    visitor_email.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
                    visitor_email.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DEL));
                }
            }
        });


        linear_Switch_selection.setOnClickListener(this);
        text_visitorselfservice.setOnClickListener(this);
        btn_cancle.setOnClickListener(this);
        btn_signout.setOnClickListener(this);
        btn_proceed.setOnClickListener(this);
        cardview_usb.setOnClickListener(this);
        text_english.setOnClickListener(this);
        cardview_barcode.setOnClickListener(this);
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

    //usb scanner
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
//        visitor_mobile.clearFocus();

        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (event.getKeyCode()) {
                case KeyEvent.KEYCODE_DEL:
                    if (status_check.equalsIgnoreCase("0")) {
                        int cursorPosition = visitor_mobile.getSelectionStart();
                        String text = visitor_mobile.getText().toString();

                        if (!text.isEmpty() && cursorPosition > 0) {
                            // Delete the character before the cursor
                            String newText = text.substring(0, cursorPosition - 1) + text.substring(cursorPosition);
                            visitor_mobile.setText(newText);

                            // Move cursor to correct position
                            visitor_mobile.setSelection(cursorPosition - 1);
                        }
                    } else {
                        int cursorPosition = visitor_email.getSelectionStart();
                        String text = visitor_email.getText().toString();

                        if (!text.isEmpty() && cursorPosition > 0) {
                            // Delete the character before the cursor
                            String newText = text.substring(0, cursorPosition - 1) + text.substring(cursorPosition);
                            visitor_email.setText(newText);

                            // Move cursor to correct position
                            visitor_email.setSelection(cursorPosition - 1);
                        }
                    }
                    return true;
                case KeyEvent.KEYCODE_BACK:
                    ViewController.exitPopup(VisitorLoginActivity.this);
                    return true;
                case KeyEvent.KEYCODE_ENTER:
                    handleBarcodeScan(usbScannedData);
//                    visitor_mobile.setFocusable(false);
//                    visitor_mobile.setFocusableInTouchMode(false);
//                    visitor_email.setFocusable(false);
//                    visitor_email.setFocusableInTouchMode(false);
                    // Reset usbScannedData if needed for the next scan
                    usbScannedData = "";
//                    if (use_mobile.isChecked()) {
//                        if (InterNetConnectivityCheck.isOnline(getApplicationContext())) {
//                            emailMethod();
//                        } else {
//                            DataManger.internetpopup(VisitorLoginActivity.this);
//                        }
//                    } else {
//                        if (InterNetConnectivityCheck.isOnline(getApplicationContext())) {
//                            mobileMethod();
//                        } else {
//                            DataManger.internetpopup(VisitorLoginActivity.this);
//                        }
//                    }
                    return true;
                default:
                    char keyChar = (char) event.getUnicodeChar();
                    if (String.valueOf(keyChar).equalsIgnoreCase("#")) {
                        if (!spaceValstatus && spaceVal == 0) {
                            spaceValstatus = true;
                            usbScannedData += " ";
                        } else {
                            spaceVal++;
                        }
                        if (spaceVal == 3) {
                            spaceVal = 0;
                            spaceValstatus = false;
                        }
                    } else if (String.valueOf(keyChar).equalsIgnoreCase(" ")) {

                    } else {
                        usbScannedData += String.valueOf(keyChar);
                    }
                    return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }

    private void handleBarcodeScan(String barResult) {

        spaceValstatus = false;
        spaceVal = 0;
        usbScannedData = "";

        if (!barResult.equalsIgnoreCase("")) {
            barResult = barResult.trim();
            String[] spre = barResult.split(" ");
            if (spre[0].trim().equalsIgnoreCase("meeting")) {
                String input = spre[1].trim();
                String last24Chars = "";
                if (input.length() >= 24) {
                    last24Chars = input.substring(input.length() - 24);
                    Preferences.saveStringValue(VisitorLoginActivity.this, Preferences.meetingId, last24Chars);
                }
                if (spre.length == 3) {
                    String newVal = spre[2].toString().trim();
                    char first = newVal.charAt(0);
                    String str = String.valueOf(first);
                    if (str.equalsIgnoreCase("+")) {
                        countrycode = extractCountryCode(newVal);
                        String newStr = newVal.substring(countrycode.length());
                        countrycode = countrycode.substring(1);
                        ccp.setDefaultCountryUsingPhoneCode(Integer.parseInt(countrycode));
                        ccp.setCountryForNameCode(countrycode);
                        visitor_mobile.setText(newStr);
                        ScanneMobileMethod(last24Chars);
                    } else {
                        newVal = newVal.trim().replaceAll("\u0000", "");
                        visitor_email.setText(newVal.trim());
                        ScanneEmailMethod(newVal.trim(), last24Chars);
                    }
                } else {
                    visitor_mobile.getText().clear();
                    visitor_mobile.setHint(getString(R.string.visitor_mobile));
                    visitor_email.getText().clear();
                    visitor_email.setHint(getString(R.string.visitor_email));
                    ViewController.worngingPopup(VisitorLoginActivity.this, "Sorry Wrong QR code");
                }
            } else if (spre[0].trim().equalsIgnoreCase("checkin")) {
                String input = spre[1].trim();
                String last24Chars = "";
                String valueType;
                String valueData;
                if (input.length() >= 24) {
                    last24Chars = input.substring(input.length() - 24);
                }
                if (spre.length == 3) {
                    String newVal = spre[2].toString().trim();
                    char first = newVal.charAt(0);
                    String str = String.valueOf(first);
                    if (str.equalsIgnoreCase("+")) {
                        valueType = "mobile";
                        countrycode = extractCountryCode(newVal);
                        Log.e("countrycode_",countrycode);
                        String newStr = newVal.substring(countrycode.length());
                        countrycode = countrycode.substring(1);
                        ccp.setDefaultCountryUsingPhoneCode(Integer.parseInt(countrycode));
                        ccp.setCountryForNameCode(countrycode);
                        valueData =  countrycode+newStr.trim();
                        Log.e("newStr_",newStr);

                        newVal = "+" + ccp.getSelectedCountryCode() + newStr;
                        visitor_mobile.setText(newVal);
                    } else {
                        valueType = "email";
                        newVal = newVal.trim().replaceAll("\u0000", "");
                        visitor_email.setText(newVal.trim());
                        valueData = newVal.trim();
                    }
//                    valueType = "checkin";
                    String locationId = Preferences.loadStringValue(getApplicationContext(), Preferences.location_id, "");
                    String compId = Preferences.loadStringValue(getApplicationContext(), Preferences.Comp_id, "");
                    apiViewModel.getqrcodeStatus(getApplicationContext(), locationId, "checkin", last24Chars, newVal, compId);
                    apiViewModel.getQrcodeStatus_response().observe(this, Qrmodel -> {
                        try {
                            if (Qrmodel.getResult() == 200) {
                                apiViewModel.getcvisitordetails(getApplicationContext(), comp_id_val, emp_id, valueData, locationId);
                                apiViewModel.getResponseforCVisitor().observe(this, model -> {
                                    try {
                                        if (model.getResult() == 200) {
                                            if (valueType.equalsIgnoreCase("mobile")){
                                                Preferences.saveStringValue(VisitorLoginActivity.this, Preferences.email_mobile_type, "mobile");
                                            }else {
                                                Preferences.saveStringValue(VisitorLoginActivity.this, Preferences.email_mobile_type, "email");
                                            }

                                            if (Qrmodel.getItems().getCheckin() != (0) && Qrmodel.getItems().getCheckout() == (0)) {
                                                //checkout
                                                Intent intent1 = new Intent(getApplicationContext(), AlreadyCheckedInActivity.class);
                                                intent1.putExtra("model_key", model);
                                                startActivity(intent1);
                                            } else {
                                                visitor_mobile.getText().clear();
                                                visitor_mobile.setHint(getString(R.string.visitor_mobile));
                                                visitor_email.getText().clear();
                                                visitor_email.setHint(getString(R.string.visitor_email));
                                                ViewController.worngingPopup(VisitorLoginActivity.this, "Invalid QR code");
                                            }

                                        } else {
                                            showAlertDialog();
                                        }
                                    } catch (Exception e) {
                                        throw new RuntimeException(e);
                                    }

                                });


                            }
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    });

                } else {
                    valueType = "";
                    visitor_mobile.getText().clear();
                    visitor_mobile.setHint(getString(R.string.visitor_mobile));
                    visitor_email.getText().clear();
                    visitor_email.setHint(getString(R.string.visitor_email));
                    ViewController.worngingPopup(VisitorLoginActivity.this, "Sorry Wrong QR code");
                }
            } else if (spre[0].trim().equalsIgnoreCase("material")) {
                String input = spre[1].trim();
                if (spre.length == 3) {

                    String valueType = "";
                    String qrValue = "";

                    String newVal = spre[2].toString().trim();
                    char first = newVal.charAt(0);
                    String str = String.valueOf(first);
                    if (str.equalsIgnoreCase("+")) {
                        countrycode = extractCountryCode(newVal);
                        String newStr = newVal.substring(countrycode.length());
                        countrycode = countrycode.substring(1);
                        ccp.setDefaultCountryUsingPhoneCode(Integer.parseInt(countrycode));
                        ccp.setCountryForNameCode(countrycode);
                        visitor_mobile.setText(newStr);
                        valueType = "mobile";
                        qrValue = "+" + ccp.getSelectedCountryCode() + visitor_mobile.getText().toString();
                    } else {
                        qrValue = newVal.trim().replaceAll("\u0000", "");
                        visitor_email.setText(qrValue.trim());
                        valueType = "email";
                    }
                    if (input.length() >= 24) {
                        // Get the last 24 characters from the string
                        //ftprovizitstc***6788e1613674e95fb517ad62 get last 24 characters
                        String last24Chars = input.substring(input.length() - 24);

                        //Details
                        apiViewModel.getentrypermitdetails(getApplicationContext(), last24Chars);
                        String finalValueType = valueType;
                        String finalQrValue = qrValue;
                        apiViewModel.getentrypermitDetails_response().observe(this, model -> {
                            progress.dismiss();
                            try {
                                if (model != null && model.getItems() != null && model.getItems().getSupplier_details() != null) {

                                    //date status check
                                    String dateStatus;
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

                                    if (todayStartTimestamp == startMillis || todayStartTimestamp > startMillis && todayStartTimestamp < endMillis) {
                                        dateStatus = "1";
                                    } else {
                                        dateStatus = "0";
                                    }

                                    //checkout chek
                                    ArrayList<ContractorsData> contractorsDataList;
                                    contractorsDataList = new ArrayList<>();

                                    String location_id = Preferences.loadStringValue(getApplicationContext(), Preferences.location_id, "");
                                    if (!location_id.equalsIgnoreCase(model.getItems().getL_id())) {
                                        Intent intent = new Intent(getApplicationContext(), LocationValidationMeetingActivity.class);
                                        startActivity(intent);
                                    } else if (model.getItems().getStatus().equalsIgnoreCase("2.0")) {
                                        //cancel meeting
                                        Intent intent = new Intent(getApplicationContext(), InValidPermitActivity.class);
                                        intent.putExtra("message", getResources().getString(R.string.TheMaterialPermitHasBeenCancelled));
                                        startActivity(intent);
                                    } else {

                                        //checkout status check whitOut OTP
                                        String compId = Preferences.loadStringValue(getApplicationContext(), Preferences.Comp_id, "");
                                        apiViewModel.getqrcodeStatus(getApplicationContext(), location_id, "material", last24Chars, newVal, compId);
                                        apiViewModel.getQrcodeStatus_response().observe(this, QRmodel -> {
                                            try {
                                                if (QRmodel.getResult() == 200) {
                                                    if (QRmodel.getItems().getCheckin() != 0 && QRmodel.getItems().getCheckout() == 0) {
                                                        //checkOut whitOut OTP
                                                        Intent intent = new Intent(getApplicationContext(), MaterialPermitActivity.class);
                                                        intent.putExtra("comp_id", last24Chars);
                                                        intent.putExtra("inputValue", finalQrValue);
                                                        intent.putExtra("valueType", finalValueType);
                                                        intent.putExtra("permitType", "material");
                                                        intent.putExtra("ndaStatus", "false");
                                                        startActivity(intent);
                                                    } else {
                                                        if (dateStatus.equalsIgnoreCase("0")) {
                                                            Intent intent = new Intent(getApplicationContext(), InValidPermitActivity.class);
                                                            intent.putExtra("type", "material");
                                                            intent.putExtra("message", getResources().getString(R.string.PleaseCheckTheDateOfTheMaterialPermit));
                                                            startActivity(intent);
                                                        } else {
                                                            Intent intent = new Intent(getApplicationContext(), OTPPermitActivity.class);
                                                            intent.putExtra("comp_id", last24Chars);
                                                            intent.putExtra("valueType", finalValueType);
                                                            intent.putExtra("inputValue", finalQrValue);
                                                            intent.putExtra("permitType", "material");
                                                            startActivity(intent);
                                                        }
                                                    }
                                                }
                                            } catch (Exception e) {
                                                throw new RuntimeException(e);
                                            }
                                        });

                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });
                    } else {
                        visitor_mobile.getText().clear();
                        visitor_mobile.setHint(getString(R.string.visitor_mobile));
                        visitor_email.getText().clear();
                        visitor_email.setHint(getString(R.string.visitor_email));
                        ViewController.worngingPopup(VisitorLoginActivity.this, "Not valid");
                    }
                } else {
                    visitor_mobile.getText().clear();
                    visitor_mobile.setHint(getString(R.string.visitor_mobile));
                    visitor_email.getText().clear();
                    visitor_email.setHint(getString(R.string.visitor_email));
                    ViewController.worngingPopup(VisitorLoginActivity.this, "Sorry Wrong QR code");
                }
            } else if (spre[0].trim().equalsIgnoreCase("workpermit")) {

                String input = spre[1].trim();
                if (spre.length == 3) {
                    String valueType = "";
                    String qrValue = "";

                    String newVal = spre[2].toString().trim();
                    char first = newVal.charAt(0);
                    String str = String.valueOf(first);
                    if (str.equalsIgnoreCase("+")) {
                        valueType = "mobile";
                        countrycode = extractCountryCode(newVal);
                        String newStr = newVal.substring(countrycode.length());
                        countrycode = countrycode.substring(1);
                        ccp.setDefaultCountryUsingPhoneCode(Integer.parseInt(countrycode));
                        ccp.setCountryForNameCode(countrycode);
                        visitor_mobile.setText(newStr);
                        qrValue =  "+" + ccp.getSelectedCountryCode() + visitor_mobile.getText().toString();
                    } else {
                        qrValue = newVal.trim().replaceAll("\u0000", "");
                        visitor_email.setText(qrValue.trim());
                        valueType = "email";
                    }
                    if (input.length() >= 24) {

                        // Get the last 24 characters from the string
                        //ftprovizitstc***6788e1613674e95fb517ad62 get last 24 characters
                        String last24Chars = input.substring(input.length() - 24);

                        //validation check
                        apiViewModel.getworkpermitDetails(getApplicationContext(), last24Chars);
                        String finalValueType = valueType;
                        String finalQrValue = qrValue;
                        apiViewModel.getworkpermitDetails_response().observe(this, model -> {
                            progress.dismiss();
                            try {
                                if (model != null && model.getItems() != null && model.getItems().getContractorsData() != null) {
                                    //date condition check
                                    String dateStatus;
                                    ArrayList<String> StartsList = new ArrayList<>();
                                    ArrayList<String> EndList = new ArrayList<>();
                                    //Start time
                                    StartsList.addAll(model.getItems().getStarts());
                                    //End time
                                    EndList.addAll(model.getItems().getEnds());

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

                                    // Get the current timestamp
                                    long currents = System.currentTimeMillis();
                                    // Convert to readable date/time
                                    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa", Locale.ENGLISH);
                                    sdf.setTimeZone(TimeZone.getDefault()); // Set to device's timezone
                                    String formattedTime = sdf.format(new Date(currents));

                                    Date cdate = sdf.parse(formattedTime);
                                    long cTimeStamp = cdate.getTime();

                                    String endTime = "";
                                    if (!EndList.isEmpty()) {
                                        long startTimestamp = (long) (Double.parseDouble(EndList.get(0)) + Conversions.timezone());
                                        // Add 1 minute (60 seconds = 60000 milliseconds) before converting
                                        endTime = Conversions.millitotime((startTimestamp * 1000) + 60000, false);
                                    }
                                    endTime = convertArabicToEnglish(endTime);
                                    endTime = endTime.replace("ص", "AM").replace("م", "PM");

                                    Date Edate = sdf.parse(endTime);
                                    long Etimestamp = Edate.getTime();

                                    if (todayStartTimestamp == startMillis || todayStartTimestamp > startMillis && todayStartTimestamp < endMillis) {
                                        System.out.println("Converted 1: " + "1");
                                        if (!StartsList.isEmpty()) {
                                            if (cTimeStamp < Etimestamp) {
                                                System.out.println("Converted 1: " + "5");
                                                dateStatus = "1";
                                            } else {
                                                System.out.println("Converted 1: " + "6");
                                                dateStatus = "0";
                                            }
                                        } else {
                                            dateStatus = "0";
                                        }
                                    } else {
                                        System.out.println("Converted 1: " + "2");
                                        dateStatus = "0";
                                    }

                                    //condition check
                                    String location_id = Preferences.loadStringValue(getApplicationContext(), Preferences.location_id, "");
                                    if (!location_id.equalsIgnoreCase(model.getItems().getL_id())) {
                                        Intent intent = new Intent(getApplicationContext(), LocationValidationMeetingActivity.class);
                                        startActivity(intent);
                                    } else if (model.getItems().getStatus().equalsIgnoreCase("2.0")) {
                                        //cancel meeting
                                        Intent intent = new Intent(getApplicationContext(), InValidPermitActivity.class);
                                        intent.putExtra("message", getResources().getString(R.string.TheWorkPermitHasBeenCancelled));
                                        startActivity(intent);
                                    } else {

                                        ArrayList<ContractorsData> contractorsDataList;
                                        contractorsDataList = new ArrayList<>();
                                        try {
                                            if (model != null && model.getItems() != null && model.getItems().getContractorsData() != null) {
                                                contractorsDataList.addAll(model.getItems().getContractorsData());
                                                if (!contractorsDataList.isEmpty()) {
                                                    for (int j = 0; j < contractorsDataList.size(); j++) {
                                                        //Contractor
                                                        ContractorsData contractor = contractorsDataList.get(j);
                                                        if (contractor != null && contractor.getEmail() != null && contractor.getEmail().equalsIgnoreCase(finalQrValue) || contractor != null && contractor.getMobile() != null && contractor.getMobile().equalsIgnoreCase(finalQrValue)) {
                                                            if (contractor.getCheckin() != 0 && contractor.getCheckout() == 0) {
                                                                Intent intent = new Intent(getApplicationContext(), WorkPermitActivity.class);
                                                                intent.putExtra("_id", last24Chars);
                                                                intent.putExtra("inputValue", finalQrValue);
                                                                intent.putExtra("valueType", finalValueType);
                                                                intent.putExtra("permitType", "workpermit");
                                                                intent.putExtra("ndaStatus", "false");
                                                                startActivity(intent);
                                                            } else {
                                                                //date check
                                                                if (dateStatus.equalsIgnoreCase("0")) {
                                                                    Intent intent = new Intent(getApplicationContext(), InValidPermitActivity.class);
                                                                    intent.putExtra("message", getResources().getString(R.string.PleaseCheckTheDateOfTheWorkPermit));
                                                                    startActivity(intent);
                                                                } else {
                                                                    Intent intent = new Intent(getApplicationContext(), OTPPermitActivity.class);
                                                                    intent.putExtra("comp_id", last24Chars);
                                                                    intent.putExtra("valueType", finalValueType);
                                                                    intent.putExtra("inputValue", finalQrValue);
                                                                    intent.putExtra("permitType", "workpermit");
                                                                    startActivity(intent);
                                                                }
                                                            }
                                                        }

                                                    }
                                                }
                                            }
                                            //Sub Contractor
                                            ArrayList<SubContractorsData> subcontractorsDataList;
                                            subcontractorsDataList = new ArrayList<>();
                                            if (model != null && model.getItems() != null && model.getItems().getSubcontractorsData() != null) {
                                                subcontractorsDataList.addAll(model.getItems().getSubcontractorsData());
                                                if (!subcontractorsDataList.isEmpty()) {
                                                    for (int k = 0; k < subcontractorsDataList.size(); k++) {
                                                        SubContractorsData sublist = subcontractorsDataList.get(k);
                                                        if (sublist != null && sublist.getEmail() != null && sublist.getEmail().equalsIgnoreCase(finalQrValue)  || sublist != null && sublist.getMobile() != null && sublist.getMobile().equalsIgnoreCase(finalQrValue)) {
                                                            if (sublist.getCheckin() != 0 && sublist.getCheckout() == 0) {
                                                                Intent intent = new Intent(getApplicationContext(), WorkPermitActivity.class);
                                                                intent.putExtra("_id", last24Chars);
                                                                intent.putExtra("inputValue", finalQrValue);
                                                                intent.putExtra("valueType", finalValueType);
                                                                intent.putExtra("permitType", "workpermit");
                                                                intent.putExtra("ndaStatus", "false");
                                                                startActivity(intent);
                                                            } else {
                                                                //date check
                                                                if (dateStatus.equalsIgnoreCase("0")) {
                                                                    Intent intent = new Intent(getApplicationContext(), InValidPermitActivity.class);
                                                                    intent.putExtra("message", getResources().getString(R.string.PleaseCheckTheDateOfTheWorkPermit));
                                                                    startActivity(intent);
                                                                } else {
                                                                    Intent intent = new Intent(getApplicationContext(), OTPPermitActivity.class);
                                                                    intent.putExtra("comp_id", last24Chars);
                                                                    intent.putExtra("valueType", finalValueType);
                                                                    intent.putExtra("inputValue", finalQrValue);
                                                                    intent.putExtra("permitType", "workpermit");
                                                                    startActivity(intent);
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                    }

                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });

                    } else {
                        visitor_mobile.getText().clear();
                        visitor_email.getText().clear();
                        ViewController.worngingPopup(VisitorLoginActivity.this, "Not valid");
                    }
                } else {
                    visitor_mobile.getText().clear();
                    visitor_email.getText().clear();
                    ViewController.worngingPopup(VisitorLoginActivity.this, "Sorry Wrong QR code");
                }

            } else {
                visitor_mobile.getText().clear();
                visitor_mobile.setHint(getString(R.string.visitor_mobile));
                visitor_email.getText().clear();
                visitor_email.setHint(getString(R.string.visitor_email));
                delayedReload();
                ViewController.worngingPopup(VisitorLoginActivity.this, "Try Again");
            }
        }

    }

    public void delayedReload() {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }, 1500); // 2-second delay
    }

    private void currentDate() {
        try {
            //current date and time
            Calendar ca = Calendar.getInstance();
            ca.set(Calendar.HOUR_OF_DAY, 0);
            ca.set(Calendar.MINUTE, 0);
            ca.set(Calendar.SECOND, 0);
            ca.set(Calendar.MILLISECOND, 0);
            todayStartTimestamp = ca.getTimeInMillis();
            System.out.println("Today's Start Timestamp: " + todayStartTimestamp);

            // Get the current timestamp
            long currents = System.currentTimeMillis();
            // Convert to readable date/time
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa"); // 12-hour format with AM/PM
            sdf.setTimeZone(TimeZone.getDefault()); // Set to device's timezone
            String formattedTime = sdf.format(new Date(currents));

            Date cdate = sdf.parse(formattedTime);
            cTimeStamp = cdate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    //mobile length for country code bases logic
    private int getMaxLengthForCountryCode(String countryCode) {
//        if (ccp.getSelectedCountryCode().length() == 3) {
//            return 9;
//        } else if (ccp.getSelectedCountryCode().length() == 2) {
//            return 10;
//        } else if (ccp.getSelectedCountryCode().length() == 1) {
//            return 11;
//        } else {
//            return 13;
//        }
        if (ccp.getSelectedCountryCode().length() == 3) {
            return 15;
        } else if (ccp.getSelectedCountryCode().length() == 2) {
            return 15;
        } else if (ccp.getSelectedCountryCode().length() == 1) {
            return 15;
        } else {
            return 15;
        }
    }

    @SuppressLint("ResourceType")
    private void inits() {


        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= Build.VERSION_CODES.LOLLIPOP_MR1) {

            if (ContextCompat.checkSelfPermission(VisitorLoginActivity.this,
                    Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(VisitorLoginActivity.this,
                        Manifest.permission.CAMERA)) {

                } else {

                    ActivityCompat.requestPermissions(VisitorLoginActivity.this,
                            new String[]{Manifest.permission.CAMERA},
                            1);
                }

            }

        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.linear_Switch_selection:
                visitor_email.setText("");
                visitor_mobile.setText("");
                if (status_check.equalsIgnoreCase("0")) {
                    status_check = "1";
                    use_mobile.setChecked(true);
                    txt_input_type.setText(getResources().getString(R.string.visitor_Usemobile));
                    linear_mobile.setVisibility(View.GONE);
                    EmailInput.setVisibility(View.VISIBLE);
                } else {
                    status_check = "0";
                    use_mobile.setChecked(false);
                    txt_input_type.setText(getResources().getString(R.string.visitor_Useemail));
                    linear_mobile.setVisibility(View.VISIBLE);
                    EmailInput.setVisibility(View.GONE);
                }
                break;
            case R.id.btn_cancle:
                AnimationSet animation = Conversions.animation();
                v.startAnimation(animation);
                Intent intent = new Intent(getApplicationContext(), VisitorLoginActivity.class);
                startActivity(intent);
                break;
            case R.id.text_visitorselfservice:
                animation = Conversions.animation();
                v.startAnimation(animation);
                intent = new Intent(getApplicationContext(), LogoutActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_signout:
                animation = Conversions.animation();
                v.startAnimation(animation);
                if (password_signout.getText().length() == 0) {
                    txt_input_password.setErrorEnabled(true);
                    txt_input_password.setError("Enter password");
                } else {
                    JSONObject jsonObj_ = new JSONObject();
                    String Comp_id = Preferences.loadStringValue(getApplicationContext(), Preferences.Comp_id, "");
                    String Login_email = Preferences.loadStringValue(getApplicationContext(), Preferences.email, "");
                    try {
                        jsonObj_.put("id", Comp_id);
                        jsonObj_.put("mverify", 0);
                        jsonObj_.put("type", "email");
                        jsonObj_.put("val", Login_email);
                        jsonObj_.put("password", password_signout.getText().toString().trim());
                    } catch (JSONException e) {
                        if (BuildConfig.DEBUG) {
                            Log.e(TAG, "Error creating JSON for sign out", e);
                        }
                    }
                    apiViewModel.checkinuserlogin(getApplicationContext(), jsonObj_);
                    progress.show();
                }
                break;
            case R.id.btn_proceed:
                animation = Conversions.animation();
                v.startAnimation(animation);
                //hide keyboard
                ViewController.hideKeyboard(VisitorLoginActivity.this, visitor_email);
                if (use_mobile.isChecked()) {
                    if (InterNetConnectivityCheck.isOnline(getApplicationContext())) {
                        emailMethod();
                    } else {
                        DataManger.internetpopup(VisitorLoginActivity.this);
                    }
                } else {
                    if (InterNetConnectivityCheck.isOnline(getApplicationContext())) {
                        mobileMethod();
                    } else {
                        DataManger.internetpopup(VisitorLoginActivity.this);
                    }
                }
                break;
            case R.id.cardview_usb:
//                AnimationSet animation1 = Conversions.animation();
//                v.startAnimation(animation1);
//                Preferences.saveStringValue(VisitorLoginActivity.this, Preferences.country_Code, ccp.getSelectedCountryCode());
//                intent = new Intent(getApplicationContext(), USBScannerActivity.class);
//                startActivity(intent);
                break;
            case R.id.text_english:
                AnimationSet ani = Conversions.animation();
                v.startAnimation(ani);
                if (!text_isClick) {
                    text_isClick = true;
                    text_english.setText("عربى");
                    DataManger.appLanguage = "ar";
                    setLocale("ar");
                    Preferences.saveStringValue(VisitorLoginActivity.this, Preferences.language, "ar");
                } else {
                    text_isClick = false;
                    text_english.setText("English");
                    DataManger.appLanguage = "en";
                    setLocale("en");
                    Preferences.saveStringValue(VisitorLoginActivity.this, Preferences.language, "en");
                }
                break;
            case R.id.cardview_barcode:
//                AnimationSet anima = Conversions.animation();
//                v.startAnimation(anima);
//                ScanOptions options = new ScanOptions();
//                int frontCameraId = -1;
//                int numberOfCameras = android.hardware.Camera.getNumberOfCameras();
//                for (int i = 0; i < numberOfCameras; i++) {
//                    Camera.CameraInfo cameraInfo = new android.hardware.Camera.CameraInfo();
//                    Camera.getCameraInfo(i, cameraInfo);
//                    if (cameraInfo.facing == android.hardware.Camera.CameraInfo.CAMERA_FACING_FRONT) {
//                        frontCameraId = i;
//                        break;
//                    }
//                }
//                if (frontCameraId != -1) {
//                    options.setCameraId(frontCameraId);
//                }
//                options.setPrompt("Volume up to flash on");
//                options.setBeepEnabled(true);
//                options.setOrientationLocked(true);
//                options.setCaptureActivity(CaptureAct.class);
//                barLaucher.launch(options);
                break;
            default:
                // Handle other cases or ignore
                break;
        }
    }

    protected void registoreNetWorkBroadcast() {
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,
                new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    ActivityResultLauncher<ScanOptions> barLaucher = registerForActivityResult(new ScanContract(), result -> {
        if (result.getContents() != null) {
//            String re = result.getContents();
//            if (re == null) {
//            } else {
//                String regex = "###";
//                // split the string object
//                String[] output = re.split(regex);
//                String newVal = output[output.length - 1];
//                char first = newVal.charAt(0);
//                String str = String.valueOf(first);
//
//
//                if (str.equalsIgnoreCase("+")) {
//                    String newStr = newVal.substring(4);
//                    visitor_mobile.setText(newStr);
//                    mobileMethod();
//                } else {
//                    visitor_email.setText(newVal.trim());
//                    emailMethod();
//                }
//            }

            String re = result.getContents();
            if (re != null) {
                String regex = "###";  // First delimiter
                String[] output = re.split(regex);  // Split by "###"

                if (output.length >= 3) {
                    String material = output[0];  // Material value (e.g., "material")
                    String idOrMobile = output[1]; // ID or mobile value (e.g., "proaa02")
                    String emailOrPhone = output[2]; // Email or phone (e.g., "678894fb3674e95fb517ad29" or "anilcherry205@gmail.com")

                    // If email or phone number starts with '+', treat it as a mobile number
                    char firstChar = emailOrPhone.charAt(0);
                    String resultStr = String.valueOf(firstChar);

                    if (resultStr.equalsIgnoreCase("+")) {
                        // It's a mobile number, handle accordingly
                        String newStr = emailOrPhone.substring(4);  // Remove the first 4 characters of the mobile number
                        visitor_mobile.setText(newStr);  // Update visitor's mobile field
                        mobileMethod();  // Call method for mobile number handling
                    } else {
                        // It's an email address, handle accordingly
                        visitor_email.setText(emailOrPhone.trim());  // Update visitor's email field
                        emailMethod();  // Call method for email handling
                    }

                    Log.d("Material Value", material); // Log or use material value

                    Toast.makeText(getApplicationContext(), "Result: " + material, Toast.LENGTH_LONG).show();
                }
            }


        }
    });

    public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        conf.setLayoutDirection(myLocale);
        res.updateConfiguration(conf, dm);
        Intent refresh = new Intent(this, VisitorLoginActivity.class);
        refresh.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(refresh);
    }

    private void mobileMethod() {

        Preferences.saveStringValue(VisitorLoginActivity.this, Preferences.country_Code, ccp.getSelectedCountryCode());

        String input = visitor_mobile.getText().toString();
        final String numberDigit = input.startsWith("0") ? input.substring(1) : input;

        Log.e("countryCode_",numberDigit);

        // Special UAE logic: 10 digits if starts with 0, else 9
        String regexStr = "^[0-9]*$";
//        int minLength;
//        int maxLength;
//
//        if (ccp.getSelectedCountryCode().length() == 2) { // India
//            minLength = 10;
//            maxLength = 10;
//        }else {
//            minLength = 9; // Default minLength for other countries
//            maxLength = 13; // Default maxLength for other countries
//        }

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        visitor_mobile.setError(null);
                    }
                }, 5000);

        if (numberDigit.length() == 0) {
//            visitor_mobile.requestFocus();
            visitor_mobile.setError(getResources().getString(R.string.Mobile_number));
        }
//        else if (numberDigit.length() < minLength || numberDigit.length() > maxLength) {
////            visitor_mobile.requestFocus();
//            visitor_mobile.setError(getResources().getString(R.string.Invalidmobile_number));
//        } else if (!numberDigit.trim().matches(regexStr)) {
////            visitor_mobile.requestFocus();
//            visitor_mobile.setError(getResources().getString(R.string.Mobile_number));
//        }
        else if (numberDigit.trim().matches(regexStr)) {
            progress.show();
            int otp = Conversions.getNDigitRandomNumber(4);
            Preferences.saveStringValue(VisitorLoginActivity.this, Preferences.otp, otp + "");
            String senderId = Preferences.loadStringValue(getApplicationContext(), Preferences.senderId, "");
            JSONObject jsonObj_ = new JSONObject();
            try {
                String newMobile = "+" + ccp.getSelectedCountryCode() + numberDigit;
                jsonObj_.put("mobile", newMobile);
                jsonObj_.put("otp", otp);
                jsonObj_.put("senderid", senderId);
            } catch (Exception ignored) {

            }
            apiViewModel.verifylinkmobile(getApplicationContext(), jsonObj_);
            apiViewModel.getcvisitordetails(getApplicationContext(),comp_id_val , emp_id, ccp.getSelectedCountryCode() + numberDigit, location_id);
            apiViewModel.getResponseforCVisitor().observe(this, model -> {
                progress.dismiss();
                try {
                    if (model.getResult() == 200) {
                        Float visitor_status = model.getItems().getVisitorStatus();
                        if (visitor_status == 0) {
                            model.setIncomplete_data(new IncompleteData());
                            model.getIncomplete_data().setEmail("");
                            model.getIncomplete_data().setMobile("+" + ccp.getSelectedCountryCode() + numberDigit);
                        }
                        Preferences.saveStringValue(VisitorLoginActivity.this, Preferences.email_mobile_type, "mobile");

                        Intent intent = new Intent(getApplicationContext(), OTPActivity.class);
                        intent.putExtra("model_key", model);
                        intent.putExtra("finalQrValue",  "+" + ccp.getSelectedCountryCode() + numberDigit);
                        intent.putExtra("finalValueType",  "mobile");
                        startActivity(intent);

                    } else {
                        showAlertDialog();
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            });

        }
    }

    private void emailMethod() {
        Preferences.saveStringValue(VisitorLoginActivity.this, Preferences.country_Code, ccp.getSelectedCountryCode());

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        visitor_email.setError(null);
                    }
                }, 5000);

        if (visitor_email.getText().length() == 0) {
//            visitor_email.requestFocus();
            visitor_email.setError("Enter email");
        } else if (!isEmailValid(visitor_email.getText().toString())) {
//            visitor_email.requestFocus();
            visitor_email.setError("Enter valid email");
        } else {
            progress.show();
            String Comp_id = Preferences.loadStringValue(getApplicationContext(), Preferences.Comp_id, "");
            int otp = Conversions.getNDigitRandomNumber(4);
            Preferences.saveStringValue(VisitorLoginActivity.this, Preferences.otp, otp + "");
            JSONObject jsonObj_ = new JSONObject();
            try {
                jsonObj_.put("comp_id", Comp_id);
                jsonObj_.put("email", visitor_email.getText().toString().trim());
                jsonObj_.put("val", visitor_email.getText().toString().trim());
                jsonObj_.put("otp", otp);
                jsonObj_.put("sotp", otp);
                apiViewModel.otpsendemailclient(getApplicationContext(), jsonObj_);
            } catch (Exception e) {

            }
            apiViewModel.getcvisitordetails(getApplicationContext(), comp_id_val, emp_id, visitor_email.getText().toString().trim(), location_id);
            apiViewModel.getResponseforCVisitor().observe(this, model -> {
                progress.dismiss();
                try {
                    if (model.getResult() == 200) {

                        Float visitor_status = model.getItems().getVisitorStatus();
                        if (visitor_status == 0) {
                            model.setIncomplete_data(new IncompleteData());
                            model.getIncomplete_data().setEmail(visitor_email.getText().toString().trim());
                            model.getIncomplete_data().setMobile("");
                        }

                        Preferences.saveStringValue(VisitorLoginActivity.this, Preferences.email_mobile_type, "email");

                        Intent intent = new Intent(getApplicationContext(), OTPActivity.class);
                        intent.putExtra("model_key", model);
                        intent.putExtra("finalQrValue",  visitor_email.getText().toString().trim());
                        intent.putExtra("finalValueType",  "email");
                        startActivity(intent);

//                        String location_id = Preferences.loadStringValue(getApplicationContext(), Preferences.location_id, "");
//                        if (model.getItems().getLocation()!=null){
//                            if (!location_id.equalsIgnoreCase(model.getItems().getLocation())){
//                                Intent intent = new Intent(getApplicationContext(), LocationValidationMeetingActivity.class);
//                                startActivity(intent);
//                            }else {
//
//                            }
//                        }

                    } else {
                        showAlertDialog();
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        }
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

    private void ScanneMobileMethod(String last24Chars) {

        Preferences.saveStringValue(VisitorLoginActivity.this, Preferences.country_Code, ccp.getSelectedCountryCode());

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

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        visitor_mobile.setError(null);
                    }
                }, 5000);

        if (visitor_mobile.getText().length() == 0) {
//            visitor_mobile.requestFocus();
            visitor_mobile.setError(getResources().getString(R.string.Mobile_number));
        } else if (visitor_mobile.length() < minLength || visitor_mobile.length() > maxLength) {
//            visitor_mobile.requestFocus();
            visitor_mobile.setError(getResources().getString(R.string.Invalidmobile_number));
        } else if (!visitor_mobile.getText().toString().trim().matches(regexStr)) {
//            visitor_mobile.requestFocus();
            visitor_mobile.setError(getResources().getString(R.string.Mobile_number));
        } else if (visitor_mobile.getText().toString().trim().matches(regexStr)) {
            progress.show();
            int otp = Conversions.getNDigitRandomNumber(4);
            Preferences.saveStringValue(VisitorLoginActivity.this, Preferences.otp, otp + "");
            String senderId = Preferences.loadStringValue(getApplicationContext(), Preferences.senderId, "");
            JSONObject jsonObj_ = new JSONObject();
            try {
                String newMobile = "+" + ccp.getSelectedCountryCode() + visitor_mobile.getText().toString();
                jsonObj_.put("mobile", newMobile);
                jsonObj_.put("otp", otp);
                jsonObj_.put("senderid", senderId);
            } catch (Exception ignored) {

            }
            apiViewModel.verifylinkmobile(getApplicationContext(), jsonObj_);
            apiViewModel.getcvisitordetails(getApplicationContext(), comp_id_val, emp_id, ccp.getSelectedCountryCode() + visitor_mobile.getText().toString(), location_id);
            apiViewModel.getResponseforCVisitor().observe(this, model -> {
                progress.dismiss();
                try {
                    if (model.getResult() == 200) {
                        Float visitor_status = model.getItems().getVisitorStatus();
                        if (visitor_status == 0) {
                            model.setIncomplete_data(new IncompleteData());
                            model.getIncomplete_data().setEmail("");
                            model.getIncomplete_data().setMobile("+" + ccp.getSelectedCountryCode() + visitor_mobile.getText().toString());
                        }
                        Preferences.saveStringValue(VisitorLoginActivity.this, Preferences.email_mobile_type, "mobile");

                        String locationId = Preferences.loadStringValue(getApplicationContext(), Preferences.location_id, "");
                        String compId = Preferences.loadStringValue(getApplicationContext(), Preferences.Comp_id, "");


                        //checkout status check whitOut OTP
                        apiViewModel.getqrcodeStatus(getApplicationContext(), locationId, "meeting", last24Chars, "mobile", compId);
                        apiViewModel.getQrcodeStatus_response().observe(this, QRmodel -> {
                            try {
                                if (QRmodel.getResult() == 200) {
                                    if (QRmodel.getItems().getCheckin() != (0) && QRmodel.getItems().getCheckout() == (0)) {
                                        //checkOut whitOut OTP
                                        Intent intent1 = new Intent(getApplicationContext(), AlreadyCheckedInActivity.class);
                                        intent1.putExtra("model_key", model);
                                        startActivity(intent1);
                                    } else {
                                        Intent intent = new Intent(getApplicationContext(), OTPActivity.class);
                                        intent.putExtra("model_key", model);
                                        startActivity(intent);
                                    }
                                }
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        });

                    } else {
                        showAlertDialog();
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            });

        }
    }

    private void ScanneEmailMethod(String decodeResult, String last24Chars) {
        Preferences.saveStringValue(VisitorLoginActivity.this, Preferences.country_Code, ccp.getSelectedCountryCode());

        //exaple%00@gmail.com remove %00
        String decodedEmail = decodeResult.replaceAll("\u0000", "");
        if (isEmailValid(decodedEmail)) {
            progress.show();
            int otp = Conversions.getNDigitRandomNumber(4);
            Preferences.saveStringValue(VisitorLoginActivity.this, Preferences.otp, otp + "");
            JSONObject jsonObj_ = new JSONObject();
            try {
                jsonObj_.put("email", decodedEmail);
                jsonObj_.put("val", decodedEmail);
                jsonObj_.put("otp", otp);
                apiViewModel.otpsendemailclient(getApplicationContext(), jsonObj_);
            } catch (Exception e) {

            }
            apiViewModel.getcvisitordetails(getApplicationContext(), comp_id_val, emp_id, decodedEmail, location_id);

            apiViewModel.getResponseforCVisitor().observe(this, model -> {
                progress.dismiss();
                try {
                    if (model.getResult() == 200) {
                        Float visitor_status = model.getItems().getVisitorStatus();
                        if (visitor_status == 0) {
                            model.setIncomplete_data(new IncompleteData());
                            model.getIncomplete_data().setEmail(decodedEmail);
                            model.getIncomplete_data().setMobile("");
                        }

                        Preferences.saveStringValue(VisitorLoginActivity.this, Preferences.email_mobile_type, "email");

                        String locationId = Preferences.loadStringValue(getApplicationContext(), Preferences.location_id, "");
                        String compId = Preferences.loadStringValue(getApplicationContext(), Preferences.Comp_id, "");

                        //checkout status check whitOut OTP
                        apiViewModel.getqrcodeStatus(getApplicationContext(), locationId, "meeting", last24Chars, "email", compId);
                        apiViewModel.getQrcodeStatus_response().observe(this, QRmodel -> {
                            try {
                                if (QRmodel.getResult() == 200) {
                                    if (QRmodel.getItems().getCheckin() != (0) && QRmodel.getItems().getCheckout() == (0)) {
                                        //checkOut whitOut OTP
                                        Intent intent1 = new Intent(getApplicationContext(), AlreadyCheckedInActivity.class);
                                        intent1.putExtra("model_key", model);
                                        startActivity(intent1);
                                    } else {
                                        Intent intent = new Intent(getApplicationContext(), OTPActivity.class);
                                        intent.putExtra("model_key", model);
                                        startActivity(intent);
                                        ;
                                    }
                                }
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        });

                    } else {
                        showAlertDialog();
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        } else {

        }
    }

    private void showAlertDialog() {
        // Create a custom TextView
        TextView textView = new TextView(this);
        textView.setText(getResources().getString(R.string.number_email_belongs));
        textView.setPadding(20, 20, 20, 20);
        textView.setTextSize(18);

        textView.setTypeface(ResourcesCompat.getFont(this, R.font.arbfonts_dinnextttlrabic_medium));

        AlertDialog alertDialog = new AlertDialog.Builder(VisitorLoginActivity.this)
                .setTitle("Access denied")
                .setCancelable(false)
                .setView(textView) // Set the custom TextView as the message view
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = getIntent();
                        finish(); // Finish the current activity
                        overridePendingTransition(0, 0); // Disable animation
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                    }
                })
                .show();
    }

    private static boolean isEmailValid(String email) {
        String expression = "^[\\w._%+-]+@[\\w.-]+\\.[A-Za-z]{2,}$";
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(email.trim()); // Trim input to remove extra spaces
        return matcher.matches();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ViewController.exitPopup(VisitorLoginActivity.this);
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