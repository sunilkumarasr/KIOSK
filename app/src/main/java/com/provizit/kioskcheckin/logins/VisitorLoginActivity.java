package com.provizit.kioskcheckin.logins;

import static android.view.View.GONE;
import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;
import static com.provizit.kioskcheckin.logins.AdminLoginActivity.isEmailValid;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.ViewCompat;
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
import android.text.Editable;
import android.text.InputType;
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
import com.provizit.kioskcheckin.activities.MaterialPermitActivity;
import com.provizit.kioskcheckin.activities.WorkPermitActivity;
import com.provizit.kioskcheckin.config.ConnectionReceiver;
import com.provizit.kioskcheckin.config.InterNetConnectivityCheck;
import com.provizit.kioskcheckin.config.ViewController;
import com.provizit.kioskcheckin.mvvm.ApiViewModel;
import com.provizit.kioskcheckin.R;
import com.provizit.kioskcheckin.services.Conversions;
import com.provizit.kioskcheckin.api.DataManger;
import com.provizit.kioskcheckin.utilities.CompanyData;
import com.provizit.kioskcheckin.utilities.IncompleteData;
import com.provizit.kioskcheckin.config.Preferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Locale;
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
    String emailPattern, emp_id, location_id;
    TextView text_english;
    LinearLayout linaer_logout;
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


        visitor_mobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
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
                    Log.d(TAG, "WriterException caught: " + e.getMessage(),e);
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
                        String Comp_id = Preferences.loadStringValue(getApplicationContext(), Preferences.Comp_id, "");

                        if (language.equals("ar")) {
                            Glide.with(VisitorLoginActivity.this).load(DataManger.IMAGE_URL + "/uploads/" + Comp_id + "/" + model.getItems().getA_pic().get(model.getItems().getA_pic().size() - 1))
                                    .into(logo1);

                            Preferences.saveStringValue(VisitorLoginActivity.this, Preferences.company_Logo, DataManger.IMAGE_URL + "/uploads/" + Comp_id + "/" + model.getItems().getA_pic().get(model.getItems().getA_pic().size() - 1));

                            Preferences.saveStringValue(VisitorLoginActivity.this, Preferences.logoPass, model.getItems().getA_pic().get(model.getItems().getA_pic().size() - 1));
                        } else {
                            Glide.with(VisitorLoginActivity.this).load(DataManger.IMAGE_URL + "/uploads/" + Comp_id + "/" + model.getItems().getPic().get(model.getItems().getPic().size() - 1))
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
                EmailInput.setVisibility(View.VISIBLE);
//                visitor_email.requestFocus();
            } else {
                status_check = "0";
                use_mobile.setChecked(false);
                txt_input_type.setText(getResources().getString(R.string.visitor_Useemail));
                linear_mobile.setVisibility(View.VISIBLE);
                EmailInput.setVisibility(View.GONE);
//                visitor_mobile.requestFocus();
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
        visitor_mobile.clearFocus();

        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (event.getKeyCode()) {
                case KeyEvent.KEYCODE_BACK:
                    ViewController.exitPopup(VisitorLoginActivity.this);
                    return true;
                case KeyEvent.KEYCODE_ENTER:
                    handleBarcodeScan(usbScannedData);
//                    visitor_mobile.setFocusable(false);
//                    visitor_mobile.setFocusableInTouchMode(false);
                    visitor_email.setFocusable(false);
                    visitor_email.setFocusableInTouchMode(false);
                    // Reset usbScannedData if needed for the next scan
                    usbScannedData = "";
                    if (use_mobile.isChecked()) {
                        if (InterNetConnectivityCheck.isOnline(getApplicationContext())) {
                            email_method();
                        } else {
                            DataManger.internetpopup(VisitorLoginActivity.this);
                        }
                    } else {
                        if (InterNetConnectivityCheck.isOnline(getApplicationContext())) {
                            mobile_method();
                        } else {
                            DataManger.internetpopup(VisitorLoginActivity.this);
                        }
                    }
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
            if (spre[0].trim().equalsIgnoreCase("meeting") || spre[0].trim().equalsIgnoreCase("checkin")) {
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
                        mobile_method();
                    } else {
                        visitor_email.setText(newVal.trim());
                        email_decode_method(newVal.trim());
                    }
                } else {
                    ViewController.worngingPopup(VisitorLoginActivity.this, "Sorry Wrong QR code");
                }
            }
            else if (spre[0].trim().equalsIgnoreCase("material")){
                //ftprovizitstc***6788e1613674e95fb517ad62 get last 24 characters
                String input = spre[1].trim();
                String inputValue = spre[2].toString().trim();

                String inputType = "";
                if (isEmailValid(input)) {
                    inputType = "email";
                }else {
                    inputType = "phone";
                }

                if (input.length() >= 24) {
                    // Get the last 24 characters from the string
                    String last24Chars = input.substring(input.length() - 24);
                    Intent intent = new Intent(getApplicationContext(), OTPPermitActivity.class);
                    intent.putExtra("comp_id", last24Chars);
                    intent.putExtra("inputType", inputType);
                    intent.putExtra("inputValue", inputValue);
                    intent.putExtra("permitType", "material");
                    startActivity(intent);
                } else {
                    ViewController.worngingPopup(VisitorLoginActivity.this, "Not valid");
                }
            }
            else if (spre[0].trim().equalsIgnoreCase("workpermit")){
                //ftprovizitstc***6788e1613674e95fb517ad62 get last 24 characters
                String input = spre[1].trim();
                String inputValue = spre[2].toString().trim();

                String inputType = "";
                if (isEmailValid(input)) {
                    inputType = "email";
                }else {
                    inputType = "phone";
                }

                if (input.length() >= 24) {
                    // Get the last 24 characters from the string
                    String last24Chars = input.substring(input.length() - 24);
                    Intent intent = new Intent(getApplicationContext(), OTPPermitActivity.class);
                    intent.putExtra("comp_id", last24Chars);
                    intent.putExtra("inputType", inputType);
                    intent.putExtra("inputValue", inputValue);
                    intent.putExtra("permitType", "workpermit");
                    startActivity(intent);
                } else {
                    ViewController.worngingPopup(VisitorLoginActivity.this, "Not valid");
                }
            }else {
                ViewController.worngingPopup(VisitorLoginActivity.this, "Try Again");

            }
        }

    }


//    private void handleBarcodeScan(String barResult) {
//
//        Toast.makeText(getApplicationContext(),"barResult: "+barResult,Toast.LENGTH_LONG).show();
//
//        spaceValstatus = false;
//        spaceVal = 0;
//        usbScannedData = "";
//
//        if (!barResult.equalsIgnoreCase("")) {
//            barResult = barResult.trim();
//
//            // Split the input string based on '###' delimiter
//            String[] parts = barResult.split("###");
//
//            if (parts.length >= 4) {
//                String material = parts[0].trim();  // Extract material
//                String idOrValue = parts[1].trim();  // Extract ID or value (e.g., proaa02)
//                String id = parts[2].trim();  // Extract email or phone (e.g., anilcherry205@gmail.com or mobile number)
//                String emailOrPhone = parts[3].trim();  // Extract email or phone (e.g., anilcherry205@gmail.com or mobile number)
//
//                // Log the values for debugging
//                Log.d("Material", material);
//                Log.d("ID/Value", idOrValue);
//                Log.d("ID/", id);
//                Log.d("Email/Phone", emailOrPhone);
//
//                // Determine if it's a phone number or email based on the first character
//                char firstChar = emailOrPhone.charAt(0);
//
//                if (firstChar == '+') {
//                    // If it's a phone number, handle it
//                    String phoneNumber = emailOrPhone.substring(1); // Remove the '+' from the phone number
//                    visitor_mobile.setText(phoneNumber); // Set phone number to the mobile field
//                    mobile_method(); // Call mobile method
//                } else {
//                    // If it's an email address, handle it
//                    visitor_email.setText(emailOrPhone); // Set email to the email field
//                    email_decode_method(emailOrPhone); // Call the email method
//                }
//
//                // Process the material value as needed
//                // For example, log or display the material
//                Log.d("Material Value", material);
//
//                Toast.makeText(getApplicationContext(),"Result: "+material,Toast.LENGTH_LONG).show();
//
//            } else {
//                // If the barcode doesn't contain the expected number of parts, show an error
//                ViewController.worngingPopup(VisitorLoginActivity.this, barResult);
//            }
//
//        }
//    }


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
                        email_method();
                    } else {
                        DataManger.internetpopup(VisitorLoginActivity.this);
                    }
                } else {
                    if (InterNetConnectivityCheck.isOnline(getApplicationContext())) {
                        mobile_method();
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
//                    mobile_method();
//                } else {
//                    visitor_email.setText(newVal.trim());
//                    email_method();
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
                        mobile_method();  // Call method for mobile number handling
                    } else {
                        // It's an email address, handle accordingly
                        visitor_email.setText(emailOrPhone.trim());  // Update visitor's email field
                        email_method();  // Call method for email handling
                    }

                    Log.d("Material Value", material); // Log or use material value

                    Toast.makeText(getApplicationContext(),"Result: "+material,Toast.LENGTH_LONG).show();
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

    private void mobile_method() {

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
            apiViewModel.getcvisitordetails(getApplicationContext(), "comp_id", emp_id, ccp.getSelectedCountryCode() + visitor_mobile.getText().toString(), location_id);
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

                        Intent intent = new Intent(getApplicationContext(), OTPActivity.class);
                        intent.putExtra("model_key", model);
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

    private void email_method() {
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
            int otp = Conversions.getNDigitRandomNumber(4);
            Preferences.saveStringValue(VisitorLoginActivity.this, Preferences.otp, otp + "");
            JSONObject jsonObj_ = new JSONObject();
            try {
                jsonObj_.put("email", visitor_email.getText().toString().trim());
                jsonObj_.put("val", visitor_email.getText().toString().trim());
                jsonObj_.put("otp", otp);
                apiViewModel.otpsendemailclient(getApplicationContext(), jsonObj_);
            } catch (Exception e) {

            }
            apiViewModel.getcvisitordetails(getApplicationContext(), "comp_id", emp_id, visitor_email.getText().toString().trim(), location_id);

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
                        startActivity(intent);
//                    } else if(model.getResult() == 201) {
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

    private void email_decode_method(String decodeResult) {
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
            apiViewModel.getcvisitordetails(getApplicationContext(), "comp_id", emp_id, decodedEmail, location_id);

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

                        Intent intent = new Intent(getApplicationContext(), OTPActivity.class);
                        intent.putExtra("model_key", model);
                        startActivity(intent);
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
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
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