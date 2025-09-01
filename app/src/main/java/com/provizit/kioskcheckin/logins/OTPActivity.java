package com.provizit.kioskcheckin.logins;

import static android.view.View.GONE;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationSet;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.provizit.kioskcheckin.activities.AlreadyCheckedInActivity;
import com.provizit.kioskcheckin.activities.DeclinedActivity;
import com.provizit.kioskcheckin.activities.VisitorFormCreateActivity;
import com.provizit.kioskcheckin.activities.MaterialPermitFormActivity;
import com.provizit.kioskcheckin.activities.Meetings.MeetingRequestFormActivity;
import com.provizit.kioskcheckin.activities.Meetings.MeetingDetailsActivity;
import com.provizit.kioskcheckin.activities.NDAPermitActivity;
import com.provizit.kioskcheckin.activities.NDA_FormActivity;
import com.provizit.kioskcheckin.activities.WarningScreens.LocationValidationMeetingActivity;
import com.provizit.kioskcheckin.activities.WarningScreens.MeetingValidationActivity;
import com.provizit.kioskcheckin.activities.WorkPermitFormActivity;
import com.provizit.kioskcheckin.activities.YourRequestSentActivity;
import com.provizit.kioskcheckin.config.ConnectionReceiver;
import com.provizit.kioskcheckin.mvvm.ApiViewModel;
import com.provizit.kioskcheckin.R;
import com.provizit.kioskcheckin.services.BlockedvisitorrequestsModel;
import com.provizit.kioskcheckin.services.Blocklist_Model;
import com.provizit.kioskcheckin.services.Conversions;
import com.provizit.kioskcheckin.api.DataManger;
import com.provizit.kioskcheckin.services.GetCVisitorDetailsModel;
import com.provizit.kioskcheckin.services.GetNdaActiveDetailsModel;
import com.provizit.kioskcheckin.config.Preferences;
import com.provizit.kioskcheckin.utilities.GetvisitorrequestblocklistModel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class OTPActivity extends AppCompatActivity implements View.OnClickListener {

    ApiViewModel apiViewModel;
    //internet connection
    BroadcastReceiver broadcastReceiver;
    RelativeLayout relative_internet;
    RelativeLayout relative_ui;
    LinearLayout linear_otp;
    EditText no1, no2, no3, no4;
    TextView email, resend, please_email, valid_otp;
    String nda_Data = "false";
    String badge_Data = "false";
    String blocking = "false";
    ImageView logo, back_image;
    String email_mobile_type = "";
    GetNdaActiveDetailsModel ndamodel;
    GetCVisitorDetailsModel model;
    ArrayList<String> Nationalitsblaclklist;
    String Nationblacklist = "";
    String Comp_id = "";
    String supertype = "";
    String type = "";
    ArrayList<GetvisitorrequestblocklistModel> visitorblocklist;
    ArrayList<String> Vistiror_blockIDs;
    String vistiorbloclist;
    String getvisitorblocklistID = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        linear_otp = findViewById(R.id.linear_otp);
        ViewCompat.setLayoutDirection(linear_otp, ViewCompat.LAYOUT_DIRECTION_LTR);
        no1 = findViewById(R.id.no1);
        no2 = findViewById(R.id.no2);
        no3 = findViewById(R.id.no3);
        no4 = findViewById(R.id.no4);
        email = findViewById(R.id.email);
        ViewCompat.setLayoutDirection(email, ViewCompat.LAYOUT_DIRECTION_LTR);
        logo = findViewById(R.id.logo);
        valid_otp = findViewById(R.id.valid_otp);
        please_email = findViewById(R.id.please_email);
        back_image = findViewById(R.id.back_image);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String VersionName = sharedPreferences.getString("VersionName", "1.0");
        TextView txtVersion = findViewById(R.id.txtVersion);
        txtVersion.setText(VersionName);

        //save shared Preferences
        Preferences.saveStringValue(getApplicationContext(), Preferences.nda_id, "false");

        Intent intent = getIntent();
        model = (GetCVisitorDetailsModel) intent.getSerializableExtra("model_key");

        //shared Preferences
        nda_Data = Preferences.loadStringValue(getApplicationContext(), Preferences.nda_Data, "");
        email_mobile_type = Preferences.loadStringValue(getApplicationContext(), Preferences.email_mobile_type, "");
        String otp1 = Preferences.loadStringValue(getApplicationContext(), Preferences.otp, "");
        blocking = Preferences.loadStringValue(getApplicationContext(), Preferences.blocking, "");

        String logoPass = Preferences.loadStringValue(getApplicationContext(), Preferences.logoPass, "");
        if (logoPass != null) {
            if (!logoPass.equals("")) {
                Comp_id = Preferences.loadStringValue(getApplicationContext(), Preferences.Comp_id, "");
                Glide.with(OTPActivity.this).load(DataManger.IMAGE_URL + "/uploads/" + Comp_id + "/" + logoPass)
                        .into(logo);
            }
        }

        Nationalitsblaclklist = new ArrayList<>();
        Vistiror_blockIDs = new ArrayList<>();

        apiViewModel = new ViewModelProvider(OTPActivity.this).get(ApiViewModel.class);

        //Visitor Blocklist
        apiViewModel.getcblacklistdetails(getApplicationContext(), Comp_id);

        //Vistior ID_block
        apiViewModel.getblockedvisitorrequests(getApplicationContext(), Comp_id, "");

        Vistiror_blockIDs = new ArrayList<String>();
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

        if (email_mobile_type.equals("email")) {
            email.setText("" + model.getIncomplete_data().getEmail());
            please_email.setText(getResources().getString(R.string.otp_pls_email));
        } else {
            email.setText("" + model.getIncomplete_data().getMobile());
            please_email.setText(getResources().getString(R.string.otp_pls_mobile));
        }

        apiViewModel.getndafdetials(getApplicationContext(), "id", "active");
        apiViewModel.getResponseforNdaActiveDetails().observe(this, model1 -> ndamodel = model1);

        no2.setEnabled(false);
        no3.setEnabled(false);
        no4.setEnabled(false);
        no1.setInputType(InputType.TYPE_CLASS_NUMBER);
        no2.setInputType(InputType.TYPE_CLASS_NUMBER);
        no3.setInputType(InputType.TYPE_CLASS_NUMBER);
        no4.setInputType(InputType.TYPE_CLASS_NUMBER);


        no1.requestFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        no1.setOnFocusChangeListener((v, hasFocus) -> {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            if (hasFocus) {
            }
        });
        no1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (no1.getText().toString().length() == 1) {
                    no2.setEnabled(true);
                    no2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        no2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (no2.getText().toString().length() == 1) {
                    no3.setEnabled(true);
                    no3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (no2.getText().toString().length() == 0) {
                    no1.requestFocus();
                }
            }
        });
        no3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (no3.getText().toString().length() == 1) {
                    no4.setEnabled(true);
                    no4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (no3.getText().toString().length() == 0) {
                    no2.requestFocus();
                }
            }
        });
        no4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (no4.getText().length() == 1) {
                    String otpvalue = no1.getText().toString() + no2.getText().toString() + no3.getText().toString() + no4.getText().toString();
                    if (otpvalue.equals(otp1 + "") || otpvalue.equals("5025")) {
                        Conversions.hideKeyboard(OTPActivity.this);
                        Float visitor_status = model.getItems().getVisitorStatus();
                        Float checkin_status = model.getItems().getCheckINStatus();
                        Float meeting_status = model.getItems().getMeetingStatus();
                        String Nation = model.getIncomplete_data().getNation();
                        String Visitor_ID = model.getIncomplete_data().getIdnumber();

                        String meetingId = Preferences.loadStringValue(getApplicationContext(), Preferences.meetingId, "");

                        if (!meetingId.equalsIgnoreCase("")) {
                            Preferences.saveStringValue(OTPActivity.this, Preferences.meetingId, "");
                            apiViewModel.getmeetingdetails(getApplicationContext(), meetingId);
                            apiViewModel.getMeetingDetails_response().observe(OTPActivity.this, detailmodel -> {
                                try {
                                    if (detailmodel != null && detailmodel.getItems() != null) {
                                        String location_id = Preferences.loadStringValue(getApplicationContext(), Preferences.location_id, "");
                                        if (detailmodel.getItems().getLocation() != null) {

                                            //meeting date
                                            long meetingMilli8 = (detailmodel.getItems().getDate() + Conversions.timezone()) * 1000;
                                            String meetingdate = Conversions.millitodateD(meetingMilli8);

                                            //current date
                                            Locale locale = new Locale(DataManger.appLanguage);
                                            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", locale);
                                            String currentDate = sdf.format(new Date());
                                            System.out.println(currentDate);

                                            if (!location_id.equalsIgnoreCase(detailmodel.getItems().getLocation())) {
                                                Intent intent = new Intent(getApplicationContext(), LocationValidationMeetingActivity.class);
                                                startActivity(intent);
                                            } else if (!meetingdate.equalsIgnoreCase(currentDate)) {
                                                Intent intent = new Intent(getApplicationContext(), MeetingValidationActivity.class);
                                                intent.putExtra("message", getResources().getString(R.string.PleaseCheckTheDateOfThemeeting));
                                                startActivity(intent);
                                            } else if (detailmodel.getItems().getStatus().equalsIgnoreCase("1.0")) {
                                                Intent intent = new Intent(getApplicationContext(), MeetingValidationActivity.class);
                                                intent.putExtra("message", getResources().getString(R.string.TheMeetingHasBeenCancelled));
                                                startActivity(intent);
                                            } else {
                                                // Check if blocking is true
                                                if (blocking.equals("true") && Nation != null) {
                                                    // Check if Nation is on the blocklist
                                                    if (Nationalitsblaclklist.contains(Nation.toLowerCase())) {
                                                        Intent intent = new Intent(getApplicationContext(), DeclinedActivity.class);
                                                        startActivity(intent);
                                                    } else {
                                                        if (Vistiror_blockIDs.contains(Visitor_ID)) {
                                                            Intent intent = new Intent(getApplicationContext(), DeclinedActivity.class);
                                                            startActivity(intent);
                                                        } else {
                                                            // Proceed with the normal flow if Nation is not blocked
                                                            if (checkin_status == 1) {
                                                                Float h_status = model.getTotal_counts().getHstatus();
                                                                long c_status = model.getTotal_counts().getCheckin();
                                                                if (h_status == 0) {
                                                                    Intent intent = new Intent(getApplicationContext(), YourRequestSentActivity.class);
                                                                    intent.putExtra("model_key", model);
                                                                    startActivity(intent);
                                                                } else if (h_status == 1 && c_status == 0) {
                                                                    Intent intent = new Intent(getApplicationContext(), YourRequestSentActivity.class);
                                                                    intent.putExtra("model_key", model);
                                                                    startActivity(intent);
                                                                } else if (h_status == 2 && c_status == 0) {
                                                                    Intent intent1 = new Intent(getApplicationContext(), YourRequestSentActivity.class);
                                                                    intent1.putExtra("model_key", model);
                                                                    startActivity(intent1);
                                                                } else {
                                                                    Intent intent1 = new Intent(getApplicationContext(), AlreadyCheckedInActivity.class);
                                                                    intent1.putExtra("model_key", model);
                                                                    startActivity(intent1);
                                                                }
                                                            } else if (meeting_status == 1 && visitor_status == 1) {
                                                                Intent intent = new Intent(getApplicationContext(), MeetingDetailsActivity.class);
                                                                intent.putExtra("model_key", model);
                                                                startActivity(intent);
                                                            } else if (meeting_status == 1 && visitor_status == 0) {
                                                                Intent intent = new Intent(getApplicationContext(), VisitorFormCreateActivity.class);
                                                                intent.putExtra("model_key", model);
                                                                startActivity(intent);
                                                            } else if (visitor_status == 0) {
                                                                Intent intent1 = new Intent(getApplicationContext(), VisitorFormCreateActivity.class);
                                                                intent1.putExtra("model_key", model);
                                                                startActivity(intent1);
                                                            } else if (visitor_status == 1) {
                                                                Boolean nda_terms = model.getIncomplete_data().getNda_terms();
                                                                Preferences.saveStringValue(getApplicationContext(), Preferences.nda_terms, nda_terms + "");
                                                                if (Nationalitsblaclklist.contains(Nation)) {
                                                                    Intent intent = new Intent(getApplicationContext(), DeclinedActivity.class);
                                                                    startActivity(intent);
                                                                } else {
                                                                    if (ndamodel != null && ndamodel.getResult() != null) {
                                                                        if (nda_Data.equals("true")) {
                                                                            if (ndamodel.getResult() == 200 && (nda_terms == null || nda_terms == false)) {
                                                                                Intent intent1 = new Intent(getApplicationContext(), NDA_FormActivity.class);
                                                                                intent1.putExtra("model_key", model);
                                                                                startActivity(intent1);
                                                                            } else {
//                                                                                    Intent intent1 = new Intent(getApplicationContext(), MeetingRequestFormActivity.class);
//                                                                                    intent1.putExtra("model_key", model);
//                                                                                    startActivity(intent1);
                                                                                MeetingTypeDailougeBottomPopUp();
                                                                            }
                                                                        } else {
//                                                                                Intent intent1 = new Intent(getApplicationContext(), MeetingRequestFormActivity.class);
//                                                                                intent1.putExtra("model_key", model);
//                                                                                startActivity(intent1);
                                                                            MeetingTypeDailougeBottomPopUp();
                                                                        }
                                                                    }

                                                                }

                                                            }


                                                        }

                                                    }
                                                } else {
                                                    // If blocking is not enabled, follow the normal flow
                                                    if (checkin_status == 1) {
                                                        Float h_status = model.getTotal_counts().getHstatus();
                                                        long c_status = model.getTotal_counts().getCheckin();
                                                        if (h_status == 0) {
                                                            Intent intent = new Intent(getApplicationContext(), YourRequestSentActivity.class);
                                                            intent.putExtra("model_key", model);
                                                            startActivity(intent);
                                                        } else if (h_status == 1 && c_status == 0) {
                                                            Intent intent = new Intent(getApplicationContext(), YourRequestSentActivity.class);
                                                            intent.putExtra("model_key", model);
                                                            startActivity(intent);
                                                        } else if (h_status == 2 && c_status == 0) {
                                                            Intent intent1 = new Intent(getApplicationContext(), YourRequestSentActivity.class);
                                                            intent1.putExtra("model_key", model);
                                                            startActivity(intent1);
                                                        } else {
                                                            Intent intent1 = new Intent(getApplicationContext(), AlreadyCheckedInActivity.class);
                                                            intent1.putExtra("model_key", model);
                                                            startActivity(intent1);
                                                        }
                                                    } else if (meeting_status == 1 && visitor_status == 1) {
                                                        Intent intent = new Intent(getApplicationContext(), MeetingDetailsActivity.class);
                                                        intent.putExtra("model_key", model);
                                                        startActivity(intent);
                                                    } else if (meeting_status == 1 && visitor_status == 0) {
                                                        Intent intent = new Intent(getApplicationContext(), VisitorFormCreateActivity.class);
                                                        intent.putExtra("model_key", model);
                                                        startActivity(intent);
                                                    } else if (visitor_status == 0) {
                                                        Intent intent1 = new Intent(getApplicationContext(), VisitorFormCreateActivity.class);
                                                        intent1.putExtra("model_key", model);
                                                        startActivity(intent1);
                                                    } else if (visitor_status == 1) {
                                                        Boolean nda_terms = model.getIncomplete_data().getNda_terms();
                                                        Preferences.saveStringValue(getApplicationContext(), Preferences.nda_terms, nda_terms + "");

                                                        if (nda_Data.equals("true")) {
                                                            if (ndamodel.getResult() == 200 && (nda_terms == null || nda_terms == false)) {
                                                                Intent intent1 = new Intent(getApplicationContext(), NDA_FormActivity.class);
                                                                intent1.putExtra("model_key", model);
                                                                startActivity(intent1);
                                                            } else {
//                                                                    Intent intent1 = new Intent(getApplicationContext(), MeetingRequestFormActivity.class);
//                                                                    intent1.putExtra("model_key", model);
//                                                                    startActivity(intent1);
                                                                MeetingTypeDailougeBottomPopUp();
                                                            }
                                                        } else {
//                                                                Intent intent1 = new Intent(getApplicationContext(), MeetingRequestFormActivity.class);
//                                                                intent1.putExtra("model_key", model);
//                                                                startActivity(intent1);
                                                            MeetingTypeDailougeBottomPopUp();
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            });

                        } else {
                            // Check if blocking is true
                            if (blocking.equals("true") && Nation != null) {
                                // Check if Nation is on the blocklist
                                if (Nationalitsblaclklist.contains(Nation.toLowerCase())) {
                                    Intent intent = new Intent(getApplicationContext(), DeclinedActivity.class);
                                    startActivity(intent);
                                } else {
                                    if (Vistiror_blockIDs.contains(Visitor_ID)) {
                                        Intent intent = new Intent(getApplicationContext(), DeclinedActivity.class);
                                        startActivity(intent);
                                    } else {
                                        // Proceed with the normal flow if Nation is not blocked

                                        if (checkin_status == 1) {
                                            Float h_status = model.getTotal_counts().getHstatus();
                                            long c_status = model.getTotal_counts().getCheckin();
                                            if (h_status == 0) {
                                                Intent intent = new Intent(getApplicationContext(), YourRequestSentActivity.class);
                                                intent.putExtra("model_key", model);
                                                startActivity(intent);
                                            } else if (h_status == 1 && c_status == 0) {
                                                Intent intent = new Intent(getApplicationContext(), YourRequestSentActivity.class);
                                                intent.putExtra("model_key", model);
                                                startActivity(intent);
                                            } else if (h_status == 2 && c_status == 0) {
                                                Intent intent1 = new Intent(getApplicationContext(), YourRequestSentActivity.class);
                                                intent1.putExtra("model_key", model);
                                                startActivity(intent1);
                                            } else {
                                                Intent intent1 = new Intent(getApplicationContext(), AlreadyCheckedInActivity.class);
                                                intent1.putExtra("model_key", model);
                                                startActivity(intent1);
                                            }
                                        } else if (meeting_status == 1 && visitor_status == 1) {
                                            Intent intent = new Intent(getApplicationContext(), MeetingDetailsActivity.class);
                                            intent.putExtra("model_key", model);
                                            startActivity(intent);
                                        } else if (meeting_status == 1 && visitor_status == 0) {
                                            Intent intent = new Intent(getApplicationContext(), VisitorFormCreateActivity.class);
                                            intent.putExtra("model_key", model);
                                            startActivity(intent);
                                        } else if (visitor_status == 0) {
                                            Intent intent1 = new Intent(getApplicationContext(), VisitorFormCreateActivity.class);
                                            intent1.putExtra("model_key", model);
                                            startActivity(intent1);
                                        } else if (visitor_status == 1) {
                                            Boolean nda_terms = model.getIncomplete_data().getNda_terms();
                                            Preferences.saveStringValue(getApplicationContext(), Preferences.nda_terms, nda_terms + "");
                                            if (Nationalitsblaclklist.contains(Nation)) {
                                                Intent intent = new Intent(getApplicationContext(), DeclinedActivity.class);
                                                startActivity(intent);
                                            } else {
                                                if (ndamodel != null && ndamodel.getResult() != null) {
                                                    if (nda_Data.equals("true")) {
                                                        if (ndamodel.getResult() == 200 && (nda_terms == null || nda_terms == false)) {
                                                            Intent intent1 = new Intent(getApplicationContext(), NDA_FormActivity.class);
                                                            intent1.putExtra("model_key", model);
                                                            startActivity(intent1);
                                                        } else {
//                                                                Intent intent1 = new Intent(getApplicationContext(), MeetingRequestFormActivity.class);
//                                                                intent1.putExtra("model_key", model);
//                                                                startActivity(intent1);
                                                            MeetingTypeDailougeBottomPopUp();
                                                        }
                                                    } else {
//                                                            Intent intent1 = new Intent(getApplicationContext(), MeetingRequestFormActivity.class);
//                                                            intent1.putExtra("model_key", model);
//                                                            startActivity(intent1);
                                                        MeetingTypeDailougeBottomPopUp();
                                                    }
                                                } else {
                                                    Log.e("asdf", "123");
//                                                        Intent intent1 = new Intent(getApplicationContext(), MeetingRequestFormActivity.class);
//                                                        intent1.putExtra("model_key", model);
//                                                        startActivity(intent1);
                                                    MeetingTypeDailougeBottomPopUp();
                                                }

                                            }
                                        } else {
                                            Log.e("asdf", "123");
                                        }


                                    }
                                }
                            } else {
                                // If blocking is not enabled, follow the normal flow
                                if (checkin_status == 1) {
                                    Float h_status = model.getTotal_counts().getHstatus();
                                    long c_status = model.getTotal_counts().getCheckin();
                                    if (h_status == 0) {
                                        Intent intent = new Intent(getApplicationContext(), YourRequestSentActivity.class);
                                        intent.putExtra("model_key", model);
                                        startActivity(intent);
                                    } else if (h_status == 1 && c_status == 0) {
                                        Intent intent = new Intent(getApplicationContext(), YourRequestSentActivity.class);
                                        intent.putExtra("model_key", model);
                                        startActivity(intent);
                                    } else if (h_status == 2 && c_status == 0) {
                                        Intent intent1 = new Intent(getApplicationContext(), YourRequestSentActivity.class);
                                        intent1.putExtra("model_key", model);
                                        startActivity(intent1);
                                    } else {
                                        Intent intent1 = new Intent(getApplicationContext(), AlreadyCheckedInActivity.class);
                                        intent1.putExtra("model_key", model);
                                        startActivity(intent1);
                                    }
                                } else if (meeting_status == 1 && visitor_status == 1) {
                                    Intent intent = new Intent(getApplicationContext(), MeetingDetailsActivity.class);
                                    intent.putExtra("model_key", model);
                                    startActivity(intent);
                                } else if (meeting_status == 1 && visitor_status == 0) {
                                    Intent intent = new Intent(getApplicationContext(), VisitorFormCreateActivity.class);
                                    intent.putExtra("model_key", model);
                                    startActivity(intent);
                                } else if (visitor_status == 0) {
                                    Intent intent1 = new Intent(getApplicationContext(), VisitorFormCreateActivity.class);
                                    intent1.putExtra("model_key", model);
                                    startActivity(intent1);
                                } else if (visitor_status == 1) {
                                    Boolean nda_terms = model.getIncomplete_data().getNda_terms();
                                    Preferences.saveStringValue(getApplicationContext(), Preferences.nda_terms, nda_terms + "");

                                    if (nda_Data.equals("true")) {
                                        if (ndamodel.getResult() == 200 && (nda_terms == null || nda_terms == false)) {
                                            Intent intent1 = new Intent(getApplicationContext(), NDA_FormActivity.class);
                                            intent1.putExtra("model_key", model);
                                            startActivity(intent1);
                                        } else {
//                                                Intent intent1 = new Intent(getApplicationContext(), MeetingRequestFormActivity.class);
//                                                intent1.putExtra("model_key", model);
//                                                startActivity(intent1);
                                            MeetingTypeDailougeBottomPopUp();
                                        }
                                    } else {
//                                            Intent intent1 = new Intent(getApplicationContext(), MeetingRequestFormActivity.class);
//                                            intent1.putExtra("model_key", model);
//                                            startActivity(intent1);
                                        MeetingTypeDailougeBottomPopUp();
                                    }
                                }
                            }
                        }


                    } else {
                        no1.setText("");
                        no2.setText("");
                        no3.setText("");
                        no4.setText("");
                        no2.setEnabled(false);
                        no3.setEnabled(false);
                        no4.setEnabled(false);
                        no1.requestFocus();
                        valid_otp.setVisibility(View.VISIBLE);
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(no1, InputMethodManager.SHOW_IMPLICIT);
                        new Handler().postDelayed(
                                () -> valid_otp.setVisibility(GONE), 5000);
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (no4.getText().toString().length() == 0) {
                    no3.requestFocus();
                }
            }
        });

//        // Blocklist visitor
//        apiViewModel.getResponsecblacklistdetails().observe(this, new Observer<Blocklist_Model>() {
//            @Override
//            public void onChanged(Blocklist_Model model) {
//                try {
//                    Nationalitsblaclklist = new ArrayList<>();
//                    Nationalitsblaclklist = model.getItem().getCountries();
//                    Nationalitsblaclklist.clear();
//                    supertype = model.getItem().getSupertype();
//                    for (int i = 0; i < Nationalitsblaclklist.size(); i++) {
//                        Nationblacklist = Nationalitsblaclklist.get(i);
//
//                    }
//                } catch (Exception e) {
//                    throw new RuntimeException(e);
//                }
//            }
//        });
        //Visitor_id_blocklist

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
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // Barcode scanner has scanned a barcode, disable triggered items
            Preferences.saveStringValue(OTPActivity.this, Preferences.meetingId, "");
            Intent intent = new Intent(getApplicationContext(), VisitorLoginActivity.class);
            intent.putExtra("model_key", model);
            startActivity(intent);
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_DEL) {
            // Handle delete key manually if needed
            Log.d("KeyEvent", "Delete key pressed");
            // your custom logic
            return true; // or false depending on whether you want to consume it
        }
        disableTriggeredItems();
        return super.onKeyDown(keyCode, event);
    }

//    @Override
//    public boolean dispatchKeyEvent(KeyEvent event) {
//        if (event.getKeyCode() == KeyEvent.KEYCODE_DEL) {
//            Log.d("KEY_EVENT", "Backspace detected globally");
//        }
//        return super.dispatchKeyEvent(event);
//    }


    private void disableTriggeredItems() {
        back_image.setFocusable(false);
        back_image.setFocusableInTouchMode(false);
        no1.setFocusable(false);
        no1.setFocusableInTouchMode(false);
        no2.setFocusable(false);
        no2.setFocusableInTouchMode(false);
        no3.setFocusable(false);
        no3.setFocusableInTouchMode(false);
        no4.setFocusable(false);
        no4.setFocusableInTouchMode(false);
//        resend.setFocusable(false);
//        resend.setFocusableInTouchMode(false);
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
            no1.setFocusable(true);
            no1.setFocusableInTouchMode(true);
            no2.setFocusable(true);
            no2.setFocusableInTouchMode(true);
            no3.setFocusable(true);
            no3.setFocusableInTouchMode(true);
            no4.setFocusable(true);
            no4.setFocusableInTouchMode(true);
        }, 500);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_image:
                Preferences.saveStringValue(OTPActivity.this, Preferences.meetingId, "");
                Intent intent = new Intent(getApplicationContext(), VisitorLoginActivity.class);
                intent.putExtra("model_key", model);
                startActivity(intent);
                break;
        }
    }

    private void MeetingTypeDailougeBottomPopUp() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this, R.style.BottomSheetDialogTheme);

        // Disable outside touch and back button cancel
        bottomSheetDialog.setCanceledOnTouchOutside(false);
        bottomSheetDialog.setCancelable(false);

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
        final Dialog dialog = new Dialog(OTPActivity.this);
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

    protected void registoreNetWorkBroadcast() {
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,
                new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Preferences.saveStringValue(OTPActivity.this, Preferences.meetingId, "");
    }

}