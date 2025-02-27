package com.provizit.kioskcheckin.logins;

import static android.view.View.GONE;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.lifecycle.ViewModelProvider;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AnimationSet;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.provizit.kioskcheckin.BuildConfig;
import com.provizit.kioskcheckin.config.ConnectionReceiver;
import com.provizit.kioskcheckin.config.InterNetConnectivityCheck;
import com.provizit.kioskcheckin.config.ViewController;
import com.provizit.kioskcheckin.mvvm.ApiViewModel;
import com.provizit.kioskcheckin.R;
import com.provizit.kioskcheckin.services.Conversions;
import com.provizit.kioskcheckin.api.DataManger;
import com.provizit.kioskcheckin.utilities.CompanyData;
import com.provizit.kioskcheckin.config.Preferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AdminLoginActivity extends AppCompatActivity implements View.OnClickListener {


    // Using AlertDialog instead of deprecated ProgressDialog
    private AlertDialog progressDialog;

    //internet connection
    BroadcastReceiver broadcastReceiver;
    TextView txtVersion;
    RelativeLayout relativeinternet;
    RelativeLayout relativeui;
    EditText loginemail;
    TextInputLayout txt_input_password;
    EditText loginpassword;
    Button btnlogin;
    String emailPattern;
    TextInputLayout txtinputemail;
    TextInputLayout txtinputpassword;
    ApiViewModel apiViewModel;


    SharedPreferences.Editor editor1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginactivity);

        inits();

        // Initialize AlertDialog as a replacement for ProgressDialog
        progressDialog = new AlertDialog.Builder(this)
                .setCancelable(false)
                .setMessage("Loading...")
                .create();

        txtVersion = findViewById(R.id.txtVersion);
        loginemail = findViewById(R.id.login_email);
        loginpassword = findViewById(R.id.login_password);
        txtinputemail = findViewById(R.id.txt_input_email);
        ViewCompat.setLayoutDirection(txtinputemail, ViewCompat.LAYOUT_DIRECTION_LTR);
        txtinputpassword = findViewById(R.id.txt_input_password);
        ViewCompat.setLayoutDirection(txtinputpassword, ViewCompat.LAYOUT_DIRECTION_LTR);
        btnlogin = findViewById(R.id.btn_login);
        emailPattern = "[a-zA-Z0-9._-]+@[a-z-]+\\.+[a-z]+";


        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String VersionName = sharedPreferences.getString("VersionName", "1.0");
        txtVersion.setText(VersionName);

        //internet connection
        relativeinternet = findViewById(R.id.relative_internet);
        relativeui = findViewById(R.id.relative_ui);
        registerNetworkBroadcast();
        apiViewModel = new ViewModelProvider(AdminLoginActivity.this).get(ApiViewModel.class);

        apiViewModel.getResponsecheckinuserlogin().observe(this, model -> {
            progressDialog.dismiss();
            try {
                if (model != null) {
                    Integer statusCode = model.getResult(); // Renamed to follow camelCase
                    Integer successCode = 200;
                    Integer failureCode = 401;
                    Integer notVerifiedCode = 404;

                    if (statusCode.equals(failureCode)) {
                        // Handle failure case
                    } else if (statusCode.equals(notVerifiedCode)) {
                        // Handle not verified case
                    } else if (statusCode.equals(successCode)) {
                        // Handle success case
                        CompanyData items = model.getItems(); // Directly assign without reassignment

                        if (items.getRoleDetails().isCheckin()) {

                            // Save preferences
                            Preferences.saveStringValue(AdminLoginActivity.this, Preferences.Emp_id, items.getEmp_id());
                            Preferences.saveStringValue(AdminLoginActivity.this, Preferences.Comp_id, items.getComp_id());
                            Preferences.saveStringValue(AdminLoginActivity.this, Preferences.email_id, items.getEmpData().get_id().get$oid());
                            Preferences.saveStringValue(AdminLoginActivity.this, Preferences.location_id, items.getEmpData().getLocation());
                            Preferences.saveStringValue(AdminLoginActivity.this, Preferences.email, items.getEmpData().getEmail());
                            Preferences.saveStringValue(AdminLoginActivity.this, Preferences.senderId, items.getSenderID());
                            Preferences.saveStringValue(AdminLoginActivity.this, Preferences.subdomain, items.getSubdomain());
                            Preferences.saveStringValue(AdminLoginActivity.this, Preferences.qrUrl, "https://" + items.getSubdomain() + "/touchless/" + items.getEmpData().getLocation());

                            SharedPreferences sharedPreferences1 = AdminLoginActivity.this.getSharedPreferences("EGEMSS_DATA", MODE_PRIVATE);
                            editor1 = sharedPreferences1.edit();
                            editor1.putString("company_id", items.getComp_id());
                            editor1.commit();
                            editor1.apply();

                            // Navigate to VisitorLoginActivity
                            Intent intent = new Intent(getApplicationContext(), VisitorLoginActivity.class);
                            startActivity(intent);
                        }
                    } else {
                        // Display AlertDialog when access is denied
                        new AlertDialog.Builder(AdminLoginActivity.this)
                                .setMessage("You don't have access")
                                .setTitle("Access denied")
                                .setCancelable(false)
                                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                                    // Restart the current activity without animation
                                    Intent intent = getIntent();
                                    finish();
                                    overridePendingTransition(0, 0);
                                    startActivity(intent);
                                    overridePendingTransition(0, 0);
                                })
                                .show();
                    }
                }
            } catch (Exception e) {
                // Handle the exception appropriately based on environment
                // Handle the exception with proper logging
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, "JSONException caught: " + e.getMessage(), e);
                } else {
                    Log.e(TAG, "Error creating JSON object for login.", e);
                }
                // Re-throw the exception if necessary for debugging or crash reporting
                throw new RuntimeException(e);
            }
        });

        btnlogin.setOnClickListener(this);
    }


    private void inits() {
        broadcastReceiver = new ConnectionReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (isConnecteds(context)) {
                    relativeinternet.setVisibility(GONE);
                    relativeui.setVisibility(View.VISIBLE);
                } else {
                    relativeinternet.setVisibility(View.VISIBLE);
                    relativeui.setVisibility(GONE);
                }
            }
        };

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                // Start animation on button click
                AnimationSet animation1 = Conversions.animation();
                v.startAnimation(animation1);

                // Hide the keyboard
                ViewController.hideKeyboard(AdminLoginActivity.this, loginpassword);

                // Clear previous error states
                txtinputemail.setErrorEnabled(false);
                txtinputpassword.setErrorEnabled(false);

                // Validate email and password inputs
                String email = loginemail.getText().toString().trim();
                String password = loginpassword.getText().toString().trim();

                if (email.isEmpty()) {
                    txtinputemail.setErrorEnabled(true);
                    txtinputemail.setError("Enter your email");
                } else if (password.isEmpty()) {
                    txtinputpassword.setErrorEnabled(true);
                    txtinputpassword.setError("Enter password");
                } else if (isEmailValid(email)) {
                    // Prepare JSON object for API request
                    JSONObject jsonObj_ = new JSONObject();
                    try {
                        jsonObj_.put("id", "STCPROAA02");
                        jsonObj_.put("mverify", 0);
                        jsonObj_.put("type", "email");
                        jsonObj_.put("val", email);
                        jsonObj_.put("password", password);
                    } catch (JSONException e) {
                        // Handle the exception with proper logging
                        if (BuildConfig.DEBUG) {
                            Log.d(TAG, "JSONException caught: " + e.getMessage(), e);
                        } else {
                            Log.e(TAG, "Error creating JSON object for login.", e);
                        }
                    }

                    // Check internet connectivity before making API call
                    if (InterNetConnectivityCheck.isOnline(getApplicationContext())) {
                        apiViewModel.checkinuserlogin(getApplicationContext(), jsonObj_);
                        progressDialog.show();
                    } else {
                        DataManger.internetpopup(AdminLoginActivity.this);
                    }
                } else {
                    txtinputemail.setErrorEnabled(true);
                    txtinputemail.setError("Invalid Email");
                }
                break;

            default:
                // Handle other cases or ignore
                break;
        }
    }

    private static final String TAG = "NetworkCallback";

    protected void registerNetworkBroadcast() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null) {
            Log.e(TAG, "ConnectivityManager is null");
            return; // Exit if connectivity manager is not available
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // Create a NetworkRequest to monitor network status
            NetworkRequest networkRequest = new NetworkRequest.Builder()
                    .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                    .build();

            try {
                connectivityManager.registerNetworkCallback(networkRequest, new ConnectivityManager.NetworkCallback() {
                    @Override
                    public void onAvailable(Network network) {
                        Log.d(TAG, "Network is available");
                    }

                    @Override
                    public void onLost(Network network) {
                        Log.d(TAG, "Network is lost");
                    }
                });
            } catch (SecurityException e) {
                Log.e(TAG, "Failed to register network callback", e);
            }

        } else {
            // Register broadcast receiver safely
            try {
                IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);

                // Restrict the broadcast to your app's context for added security
                registerReceiver(broadcastReceiver, filter, "com.provizit.kioskcheckin.PERMISSION_RECEIVE_NETWORK", null);
            } catch (SecurityException e) {
                Log.e(TAG, "SecurityException: Error registering broadcast receiver", e);
            } catch (Exception e) {
                Log.e(TAG, "Exception: Error registering broadcast receiver", e);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            try {
                unregisterReceiver(broadcastReceiver);
            } catch (Exception e) {
                Log.e(TAG, "Error unregistering broadcast receiver", e);
            }
        }
    }


    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}