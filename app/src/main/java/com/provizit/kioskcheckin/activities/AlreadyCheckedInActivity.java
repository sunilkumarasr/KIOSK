package com.provizit.kioskcheckin.activities;

import static android.view.View.GONE;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkRequest;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationSet;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.provizit.kioskcheckin.BuildConfig;
import com.provizit.kioskcheckin.config.ConnectionReceiver;
import com.provizit.kioskcheckin.logins.VisitorLoginActivity;
import com.provizit.kioskcheckin.mvvm.ApiViewModel;
import com.provizit.kioskcheckin.R;
import com.provizit.kioskcheckin.services.Conversions;
import com.provizit.kioskcheckin.services.GetCVisitorDetailsModel;
import com.provizit.kioskcheckin.config.Preferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class AlreadyCheckedInActivity extends AppCompatActivity implements View.OnClickListener {

    //internet connection
    BroadcastReceiver broadcastReceiver;
    RelativeLayout relativeinternet;
    RelativeLayout relativeui;
    ImageView backimage;
    ImageView companylogo;
    Button btncancel;
    Button btncheckout;
    ApiViewModel apiViewModel;
    GetCVisitorDetailsModel model;
    Handler handler = new Handler(Looper.getMainLooper());
    private static final String TAG_CONFIRMATION_POPUP = "ConfirmationPopup"; // Define a constant for the log tag

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_already_checked_in);
        Intent intent = getIntent();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            model = (GetCVisitorDetailsModel) intent.getSerializableExtra("model_key");
        } else {
            model = (GetCVisitorDetailsModel) intent.getSerializableExtra("model_key");
        }

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String VersionName = sharedPreferences.getString("VersionName", "1.0");
        TextView txtVersion = findViewById(R.id.txtVersion);
        txtVersion.setText(VersionName);

        backimage = findViewById(R.id.backimage);
        btncheckout = findViewById(R.id.btn_checkout);
        companylogo = findViewById(R.id.company_logo);
        btncancel = findViewById(R.id.btn_cancel);

        Runnable checkInTimeoutRunnable = () -> {
            // Your code here
            Intent intents = new Intent(getApplicationContext(), VisitorLoginActivity.class);
            startActivity(intents);
        };
        handler.postDelayed(checkInTimeoutRunnable, 10000);




        //internet connection
        relativeinternet = findViewById(R.id.relative_internet);
        relativeui = findViewById(R.id.relative_ui);
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
        registerNetworkBroadcast();

        // Company logo
        String companyLogo = Preferences.loadStringValue(getApplicationContext(), Preferences.company_Logo, "");

        if (!companyLogo.equalsIgnoreCase("")) {
            // Load the company logo if it's available
            Glide.with(AlreadyCheckedInActivity.this)
                    .load(companyLogo)
                    .into(companylogo);
        } else {

            companylogo.setVisibility(View.GONE);
        }

        btncheckout.setOnClickListener(this);
        btncancel.setOnClickListener(this);
        backimage.setOnClickListener(this);

        // Register the back press behavior using OnBackPressedDispatcher
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                handleBackPressed(); // Call your custom back pressed logic
            }
        });

    }

    //disable auto click action after scann
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_ENTER) {
            // Barcode scanner has scanned a barcode, disable triggered items
            return true;
        } else {
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
                    return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }

    private void disableTriggeredItems() {
        btncheckout.setFocusable(false);
        btncheckout.setFocusableInTouchMode(false);
        btncancel.setFocusable(false);
        btncancel.setFocusableInTouchMode(false);
        backimage.setFocusable(false);
        backimage.setFocusableInTouchMode(false);
        handler.post(myRunnable);
    }

    Runnable myRunnable = () -> {
        finish();
        // Remove deprecated calls to overridePendingTransition
        startActivity(getIntent());
        // The second call to overridePendingTransition is also removed.

        // Set focusable properties for buttons and image
        btncheckout.setFocusable(true);
        btncheckout.setFocusableInTouchMode(true);
        btncancel.setFocusable(true);
        btncancel.setFocusableInTouchMode(true);
        backimage.setFocusable(true);
        backimage.setFocusableInTouchMode(true);
    };


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_checkout:
                AnimationSet animation1 = Conversions.animation();
                view.startAnimation(animation1);
                conformationpopup();
                break;
            case R.id.btn_cancel:
                animation1 = Conversions.animation();
                view.startAnimation(animation1);
                Intent intent = new Intent(getApplicationContext(), VisitorLoginActivity.class);
                startActivity(intent);
                handler.removeCallbacksAndMessages(null);
                break;
            case R.id.backimage:
                animation1 = Conversions.animation();
                view.startAnimation(animation1);
                intent = new Intent(getApplicationContext(), VisitorLoginActivity.class);
                startActivity(intent);
                handler.removeCallbacksAndMessages(null);
                break;
            default:
                break;
        }
    }

    private void conformationpopup() {

        // Create and configure the dialog
        final Dialog dialog = new Dialog(AlreadyCheckedInActivity.this);
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.confirmation_popup);

        // Ensure the dialog window is not null before applying background
        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }

        // Initialize layout buttons
        RelativeLayout btyes = dialog.findViewById(R.id.bt_yes);
        RelativeLayout btno = dialog.findViewById(R.id.bt_no);

        btyes.setOnClickListener(v -> {
            // Apply animation
            AnimationSet animationp = Conversions.animation();
            v.startAnimation(animationp);

            // Create the JSON object for check-out action
            JSONObject jsonObject = new JSONObject();
            try {
                long checkinValue = model.getTotal_counts().getCheckin();  // Retrieve checkin value
                jsonObject.put("formtype", "checkout");
                jsonObject.put("id", model.getTotal_counts().getUser_id());
                jsonObject.put("checkin", checkinValue);
                jsonObject.put("host", model.getTotal_counts().getHost());
                jsonObject.put("status", 1);

                // Initiate API ViewModel for checkin/checkout action
                apiViewModel = new ViewModelProvider(AlreadyCheckedInActivity.this).get(ApiViewModel.class);
                apiViewModel.actioncheckinout(getApplicationContext(), jsonObject);

                // Observe API response
                apiViewModel.getResponseforactioncheckinout().observe(AlreadyCheckedInActivity.this, responseModel -> {
                    if (responseModel != null) {
                        // Handle successful response
                        Log.d(TAG_CONFIRMATION_POPUP, "Check-in/Check-out action response received: " + responseModel);
                    } else {
                        // Log no action required
                        Log.d(TAG_CONFIRMATION_POPUP, "No action required for the response.");
                    }
                });

            } catch (JSONException ex) {
                // Handle the JSONException appropriately
                if (BuildConfig.DEBUG) {
                    Log.d(TAG_CONFIRMATION_POPUP, "JSON Exception: " + ex.getMessage(),ex);  // Debugging
                } else {
                    Log.e(TAG_CONFIRMATION_POPUP, "Error occurred while creating JSON object.", ex);  // Production log
                }
            }

            // Start VisitorLoginActivity after API call
            Intent intent = new Intent(getApplicationContext(), VisitorLoginActivity.class);
            startActivity(intent);

            // Clean up callbacks and dismiss the dialog
            handler.removeCallbacksAndMessages(null);
            dialog.dismiss();
        });

        // Cancel button logic to dismiss the dialog
        btno.setOnClickListener(v -> {
            AnimationSet animationp = Conversions.animation();
            v.startAnimation(animationp);
            dialog.dismiss();
        });

        dialog.show();  // Display the dialog
    }


    protected void registerNetworkBroadcast() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkRequest.Builder builder = new NetworkRequest.Builder();
            connectivityManager.registerNetworkCallback(builder.build(), new ConnectivityManager.NetworkCallback() {
                @Override
                public void onAvailable(Network network) {
                    // Handle network available
                }

                @Override
                public void onLost(Network network) {
                    // Handle network lost
                }
            });
        }
    }


    // Custom back pressed method
    private void handleBackPressed() {
        Intent intent = new Intent(getApplicationContext(), VisitorLoginActivity.class);
        startActivity(intent);
    }

}