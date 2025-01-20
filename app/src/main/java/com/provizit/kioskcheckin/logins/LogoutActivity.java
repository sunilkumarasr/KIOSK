package com.provizit.kioskcheckin.logins;

import static android.view.View.GONE;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.View;
import android.view.animation.AnimationSet;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputLayout;
import com.provizit.kioskcheckin.config.ConnectionReceiver;
import com.provizit.kioskcheckin.config.ViewController;
import com.provizit.kioskcheckin.mvvm.ApiViewModel;
import com.provizit.kioskcheckin.R;
import com.provizit.kioskcheckin.services.CompanyDetailsModel;
import com.provizit.kioskcheckin.services.Conversions;
import com.provizit.kioskcheckin.api.DataManger;
import com.provizit.kioskcheckin.utilities.CompanyData;
import com.provizit.kioskcheckin.config.Preferences;
import org.json.JSONException;
import org.json.JSONObject;

public class LogoutActivity extends AppCompatActivity implements View.OnClickListener {
    BroadcastReceiver broadcastReceiver;
    RelativeLayout relative_internet;
    LinearLayout LinearLayout_ui;
    ImageView logo;
    EditText password_signout;
    Button btn_signout, btn_cancle;
    TextInputLayout txt_input_password;
    ApiViewModel apiViewModel;
    ProgressDialog progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);

        progress = new ProgressDialog(this);
        progress.setTitle(getResources().getString(R.string.Loading));
        progress.setMessage(getResources().getString(R.string.whileloading));
        progress.setCancelable(true);
        progress.setCanceledOnTouchOutside(true);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String VersionName = sharedPreferences.getString("VersionName", "1.0");
        TextView txtVersion = findViewById(R.id.txtVersion);
        txtVersion.setText(VersionName);


        logo = findViewById(R.id.logo);
        password_signout = findViewById(R.id.password_signout);
        btn_signout = findViewById(R.id.btn_signout);
        btn_cancle = findViewById(R.id.btn_cancle);
        LinearLayout_ui = findViewById(R.id.LinearLayout_ui);
        txt_input_password = findViewById(R.id.txt_input_password);
        ViewCompat.setLayoutDirection(txt_input_password, ViewCompat.LAYOUT_DIRECTION_LTR);

        apiViewModel = new ViewModelProvider(LogoutActivity.this).get(ApiViewModel.class);

        //shared Preferences
        String logoPass = Preferences.loadStringValue(getApplicationContext(), Preferences.logoPass, "");
        if (logoPass != null) {
            if (!logoPass.equals("")) {
                String Comp_id = Preferences.loadStringValue(getApplicationContext(), Preferences.Comp_id, "");
                Glide.with(LogoutActivity.this).load(DataManger.IMAGE_URL + "/uploads/" + Comp_id + "/" + logoPass)
                        .into(logo);
            }
        }

        //internet connection
        relative_internet = findViewById(R.id.relative_internet);
        broadcastReceiver = new ConnectionReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (isConnecteds(context)) {
                    relative_internet.setVisibility(GONE);
                    LinearLayout_ui.setVisibility(View.VISIBLE);
                } else {
                    relative_internet.setVisibility(View.VISIBLE);
                    LinearLayout_ui.setVisibility(GONE);
                }
            }
        };
        registoreNetWorkBroadcast();

        apiViewModel.getuserLDetails(getApplicationContext(), "visitor");

        apiViewModel.getResponseforcompany().observe(this, new Observer<CompanyDetailsModel>() {
            @Override
            public void onChanged(CompanyDetailsModel model) {
                try {
                    if (model.getItems().getPic() != null) {
                        if (model.getItems().getPic() != null && model.getItems().getPic().size() != 0) {
                            //preferences
                            String Comp_id = Preferences.loadStringValue(getApplicationContext(), Preferences.Comp_id, "");
                            Preferences.saveStringValue(LogoutActivity.this, Preferences.company_Logo, DataManger.IMAGE_URL + "/uploads/" + Comp_id + "/" + model.getItems().getPic().get(model.getItems().getPic().size() - 1));
                            Glide.with(LogoutActivity.this).load(DataManger.IMAGE_URL + "/uploads/" + Comp_id + "/" + model.getItems().getPic().get(model.getItems().getPic().size() - 1))
                                    .into(logo);
                        }
                    }

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            }
        });

        btn_cancle.setOnClickListener(this);
        btn_signout.setOnClickListener(this);

    }


//    //disable auto click action after scann
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//
//        if (keyCode == KeyEvent.KEYCODE_ENTER) {
//            // Barcode scanner has scanned a barcode, disable triggered items
//            return true;
//        }else {
//            disableTriggeredItems();
//        }
//
//
//        return super.onKeyDown(keyCode, event);
//    }
//
//    //usb scanner
//    @Override
//    public boolean dispatchKeyEvent(KeyEvent event) {
//        if (event.getAction() == KeyEvent.ACTION_DOWN) {
//            switch (event.getKeyCode()) {
//                case KeyEvent.KEYCODE_ENTER:
//                    return true;
//                default:
//                    char keyChar = (char) event.getUnicodeChar();
//                    return true;
//            }
//        }
//        return super.dispatchKeyEvent(event);
//    }

//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        // Check if the key pressed is the Enter key
//        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
//            // Barcode scanner has scanned a barcode, disable triggered items
//            // Disable auto click action here
//            disableTriggeredItems();
//            return true; // Consume the event, preventing default behavior
//        } else {
//            // If another key is pressed, perform your regular handling
//            disableTriggeredItems();
//        }
//        return super.onKeyDown(keyCode, event);
//    }
//
//    @Override
//    public boolean dispatchKeyEvent(KeyEvent event) {
//
//        password_signout.setInputType(InputType.TYPE_CLASS_NUMBER);
//
//        if (event.getAction() == KeyEvent.ACTION_DOWN) {
//            // Check if the key pressed is the Enter key
//            if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
//                // Disable auto click action here
//                disableTriggeredItems();
//                return true; // Consume the event, preventing default behavior
//            } else {
//                char keyChar = (char) event.getUnicodeChar();
//                // Handle other key events if needed
//            }
//        }
//        return super.dispatchKeyEvent(event);
//    }


    private void disableTriggeredItems() {
        password_signout.setFocusable(false);
        password_signout.setFocusableInTouchMode(false);
        btn_signout.setFocusable(false);
        btn_signout.setFocusableInTouchMode(false);
        btn_cancle.setFocusable(false);
        btn_cancle.setFocusableInTouchMode(false);

        runthred();
    }

    private void runthred() {
        final Handler handler = new Handler();
        handler.postDelayed(() -> {

            finish();
            overridePendingTransition(0, 0);
            startActivity(getIntent());
            overridePendingTransition(0, 0);

            password_signout.setFocusable(true);
            password_signout.setFocusableInTouchMode(true);
            password_signout.setInputType(InputType.TYPE_CLASS_NUMBER);

        }, 500);
    }


    protected void registoreNetWorkBroadcast() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            registerReceiver(broadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_signout:
                AnimationSet animation = Conversions.animation();
                v.startAnimation(animation);
                if (password_signout.getText().toString().equalsIgnoreCase("")) {
                    AlertDialog alertDialog = new AlertDialog.Builder(LogoutActivity.this)
                            .setMessage("Enter password")
                            .setTitle("warning!")
                            .setPositiveButton(android.R.string.ok, null)
                            .show();
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
                        e.printStackTrace();
                    }
                    apiViewModel.checkinuserlogin(getApplicationContext(), jsonObj_);
                    progress.show();
                }
                break;

            case R.id.btn_cancle:
                animation = Conversions.animation();
                v.startAnimation(animation);
                Intent intent = new Intent(getApplicationContext(), VisitorLoginActivity.class);
                startActivity(intent);
                break;
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
                            ViewController.clearCache(LogoutActivity.this);
                            Preferences.saveStringValue(getApplicationContext(), "status", "failed");
                            Preferences.deleteSharedPreferences(getApplicationContext());
                            Intent intent = new Intent(LogoutActivity.this, AdminLoginActivity.class);
                            startActivity(intent);
                        }else {
                            AlertDialog alertDialog = new AlertDialog.Builder(LogoutActivity.this)
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
                    } else {
                        AlertDialog alertDialog = new AlertDialog.Builder(LogoutActivity.this)
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
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), VisitorLoginActivity.class);
        startActivity(intent);
    }
}
