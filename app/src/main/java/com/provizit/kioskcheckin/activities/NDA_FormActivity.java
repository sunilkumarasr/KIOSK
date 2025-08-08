package com.provizit.kioskcheckin.activities;

import static android.view.View.GONE;
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
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationSet;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.provizit.kioskcheckin.activities.Meetings.MeetingRequestFormActivity;
import com.provizit.kioskcheckin.activities.Meetings.MeetingDetailsActivity;
import com.provizit.kioskcheckin.config.ConnectionReceiver;
import com.provizit.kioskcheckin.config.InterNetConnectivityCheck;
import com.provizit.kioskcheckin.logins.VisitorLoginActivity;
import com.provizit.kioskcheckin.mvvm.ApiViewModel;
import com.provizit.kioskcheckin.R;
import com.provizit.kioskcheckin.services.Conversions;
import com.provizit.kioskcheckin.api.DataManger;
import com.provizit.kioskcheckin.services.GetCVisitorDetailsModel;
import com.provizit.kioskcheckin.services.GetNdaActiveDetailsModel;
import com.provizit.kioskcheckin.config.Preferences;
import org.json.JSONException;
import org.json.JSONObject;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


public class NDA_FormActivity extends AppCompatActivity implements View.OnClickListener {

    //internet connection
    BroadcastReceiver broadcastReceiver;
    RelativeLayout relative_internet;
    RelativeLayout relative_ui;
    ImageView company_logo;
    TextView name, mobile, email, time, nda_name;
    LinearLayout linear_checkbox_selection,linaerEmail;
    String status_check = "0";
    CheckBox checkBox;
    GetNdaActiveDetailsModel ndamodel;
    ApiViewModel apiViewModel;
    Button btn_accept;

    ImageView back_image;

    GetCVisitorDetailsModel model;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nda_form);
        inits();
        btn_accept = findViewById(R.id.btn_accept);
        btn_accept.setTypeface(ResourcesCompat.getFont(this, R.font.arbfonts_dinnextttlrabic_medium));
        back_image = findViewById(R.id.back_image);
        linear_checkbox_selection = findViewById(R.id.linear_checkbox_selection);
        checkBox = findViewById(R.id.checkbox);
        name = findViewById(R.id.name);
        mobile = findViewById(R.id.mobile);
        linaerEmail = findViewById(R.id.linaerEmail);
        email = findViewById(R.id.email);
        time = findViewById(R.id.time);
        nda_name = findViewById(R.id.nda_name);
        company_logo = findViewById(R.id.company_logo);


        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String VersionName = sharedPreferences.getString("VersionName", "1.0");
        TextView txtVersion = findViewById(R.id.txtVersion);
        txtVersion.setText(VersionName);

        Intent intent = getIntent();
        model = (GetCVisitorDetailsModel) intent.getSerializableExtra("model_key");
        Float meeting_status = model.getItems().getMeetingStatus();
        name.setText(model.getIncomplete_data().getName());
        mobile.setText(model.getIncomplete_data().getMobile());
        if (!model.getIncomplete_data().getEmail().equalsIgnoreCase("") && model.getIncomplete_data().getEmail()!=null){
            linaerEmail.setVisibility(View.VISIBLE);
            email.setText(model.getIncomplete_data().getEmail());
        }
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        time.setText(dtf.format(now));
        TextView textView = (TextView) findViewById(R.id.textView);
        ndamodel = new GetNdaActiveDetailsModel();
        apiViewModel = new ViewModelProvider(NDA_FormActivity.this).get(ApiViewModel.class);

        //internet connection
        relative_internet = findViewById(R.id.relative_internet);

        registoreNetWorkBroadcast();

        // Get the current layout direction
        int layoutDirection = getResources().getConfiguration().getLayoutDirection();

        // If layout direction is right-to-left, move the icon to the left
        if (layoutDirection == View.LAYOUT_DIRECTION_RTL) {
            btn_accept.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_arrow_forward_24, 0);

        }

        // Check if the layout direction is right-to-left
        if (isRightToLeft()) {
            // If layout direction is right-to-left, mirror the image
            back_image.setScaleX(-1f);
        } else {
            // If layout direction is left-to-right, reset the image scaling
            back_image.setScaleX(1f);
        }

        //company logo
        String c_Logo = Preferences.loadStringValue(getApplicationContext(), Preferences.company_Logo, "");
        if (c_Logo.equalsIgnoreCase("")) {
        } else {
            Glide.with(NDA_FormActivity.this).load(c_Logo)
                    .into(company_logo);
        }

        apiViewModel.getndafdetials(getApplicationContext(), "id", "active");
        apiViewModel.getResponseforNdaActiveDetails().observe(this, model1 -> {
            ndamodel = model1;
            try {
                if (ndamodel != null) {
                    try {
                        String htmlText = ndamodel.getItems().getContent();
                        Document doc = Jsoup.parse(htmlText);
                        String xmlText = doc.outerHtml();
                        textView.setText(Html.fromHtml(xmlText));
                        nda_name.setText(ndamodel.getItems().getName());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        apiViewModel.getResponseactionvisitor().observe(this, model1 -> {
            try {
                if(meeting_status==1){
                    Intent intent1 = new Intent(getApplicationContext(), MeetingDetailsActivity.class);
                    intent1.putExtra("model_key", model);
                    startActivity(intent1);
                }else {
                    MeetingTypeDailougeBottomPopUp();
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        linear_checkbox_selection.setOnClickListener(this);
        checkBox.setOnClickListener(this);
        btn_accept.setOnClickListener(this);
        back_image.setOnClickListener(this);
    }

    // Function to check if the layout direction is right-to-left
    private boolean isRightToLeft() {
        return getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_RTL;
    }

    //disable auto click action after scann
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_ENTER) {
            // Barcode scanner has scanned a barcode, disable triggered items
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

    private void disableTriggeredItems() {
        back_image.setFocusable(false);
        back_image.setFocusableInTouchMode(false);
        linear_checkbox_selection.setFocusable(false);
        linear_checkbox_selection.setFocusableInTouchMode(false);
        checkBox.setFocusable(false);
        checkBox.setFocusableInTouchMode(false);
        btn_accept.setFocusable(false);
        btn_accept.setFocusableInTouchMode(false);
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
            linear_checkbox_selection.setFocusable(true);
            linear_checkbox_selection.setFocusableInTouchMode(true);
            checkBox.setFocusable(true);
            checkBox.setFocusableInTouchMode(true);
            btn_accept.setFocusable(true);
            btn_accept.setFocusableInTouchMode(true);

        }, 500);
    }

    private void inits() {
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

            case R.id.linear_checkbox_selection:
                AnimationSet animation1 = Conversions.animation();
                v.startAnimation(animation1);
                if (status_check.equalsIgnoreCase("0")) {
                    status_check = "1";
                    checkBox.setChecked(true);
                    btn_accept.setEnabled(true);
                    btn_accept.setBackgroundColor(btn_accept.getContext().getResources().getColor(R.color.colorPrimary));
                } else {
                    status_check = "0";
                    checkBox.setChecked(false);
                    btn_accept.setEnabled(false);
                    btn_accept.setBackgroundColor(btn_accept.getContext().getResources().getColor(R.color.light_gray));
                }
                break;
            case R.id.checkbox:
                if (checkBox.isChecked()) {
                    status_check = "1";
                    btn_accept.setEnabled(true);
                    btn_accept.setBackgroundColor(btn_accept.getContext().getResources().getColor(R.color.colorPrimary));
                } else {
                    status_check = "0";
                    btn_accept.setEnabled(false);
                    btn_accept.setBackgroundColor(btn_accept.getContext().getResources().getColor(R.color.light_gray));
                }
                break;
            case R.id.btn_accept:
                if (checkBox.isChecked()) {
                    if (model.getItems().getVisitorStatus() == 1) {
                        JSONObject jsonObj_ = new JSONObject();
                        try {
                            jsonObj_.put("id", model.getIncomplete_data().get_id().get$oid());
                            jsonObj_.put("formtype", "nda");
                            jsonObj_.put("nda_terms", "true");
                            jsonObj_.put("nda_id", ndamodel.getItems().get_id().get$oid());
                            jsonObj_.put("emp_id", model.getIncomplete_data().get_id().get$oid());

                        } catch (JSONException ex) {
                            ex.printStackTrace();
                        }
                        if (InterNetConnectivityCheck.isOnline(getApplicationContext())) {
                            apiViewModel.actionvisitor(getApplicationContext(), jsonObj_, model);
                        } else {
                            DataManger.internetpopup(NDA_FormActivity.this);
                        }

                    } else {
//                        Intent intent = new Intent(getApplicationContext(), MeetingRequestFormActivity.class);
//                        intent.putExtra("model_key", model);
//                        startActivity(intent);
                        MeetingTypeDailougeBottomPopUp();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please Accept the terms and conditions", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.back_image:
                animation1 = Conversions.animation();
                v.startAnimation(animation1);
                Intent intent = new Intent(getApplicationContext(), VisitorLoginActivity.class);
                startActivity(intent);
                break;
        }
    }

    protected void registoreNetWorkBroadcast() {
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,
                new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    private void MeetingTypeDailougeBottomPopUp() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this, R.style.BottomSheetDialogTheme);
        View sheetView = getLayoutInflater().inflate(R.layout.bottom_sheet_meetingtype_selection, null);
        bottomSheetDialog.setContentView(sheetView);

        ImageView imgClose = sheetView.findViewById(R.id.imgClose);
        LinearLayout linearNewMeeting = sheetView.findViewById(R.id.linearNewMeeting);
        LinearLayout linearNewWorkingPermit = sheetView.findViewById(R.id.linearNewWorkingPermit);
        LinearLayout linearNewMaterialPermit = sheetView.findViewById(R.id.linearNewMaterialPermit);

        imgClose.setOnClickListener(v -> {
            AnimationSet animation = Conversions.animation();
            v.startAnimation(animation);
            exitPopUp();
        });

        linearNewMeeting.setOnClickListener(v -> {
            AnimationSet animation = Conversions.animation();
            v.startAnimation(animation);
            Intent intent = new Intent(getApplicationContext(), MeetingRequestFormActivity.class);
            intent.putExtra("model_key", model);
            startActivity(intent);
            bottomSheetDialog.dismiss();
        });

        linearNewWorkingPermit.setOnClickListener(v -> {
            AnimationSet animation = Conversions.animation();
            v.startAnimation(animation);
            Intent intent = new Intent(getApplicationContext(), WorkPermitFormActivity.class);
            intent.putExtra("model_key", model);
            startActivity(intent);
            bottomSheetDialog.dismiss();
        });

        linearNewMaterialPermit.setOnClickListener(v -> {
            AnimationSet animation = Conversions.animation();
            v.startAnimation(animation);
            Intent intent = new Intent(getApplicationContext(), MaterialPermitFormActivity.class);
            intent.putExtra("model_key", model);
            startActivity(intent);
            bottomSheetDialog.dismiss();
        });

        bottomSheetDialog.show();
    }
    private void exitPopUp() {
        final Dialog dialog = new Dialog(NDA_FormActivity.this);
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.setup_meeting_exit);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        RelativeLayout bt_yes = (RelativeLayout) dialog.findViewById(R.id.bt_yes);
        RelativeLayout bt_no = (RelativeLayout) dialog.findViewById(R.id.bt_no);

        bt_yes.setOnClickListener(v -> {
            AnimationSet animationp = Conversions.animation();
            v.startAnimation(animationp);
            Intent intent = new Intent(getApplicationContext(), VisitorLoginActivity.class);
            startActivity(intent);
            dialog.dismiss();
        });
        bt_no.setOnClickListener(v -> {
            AnimationSet animationp = Conversions.animation();
            v.startAnimation(animationp);
            dialog.dismiss();
        });
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), VisitorLoginActivity.class);
        startActivity(intent);
    }

}

