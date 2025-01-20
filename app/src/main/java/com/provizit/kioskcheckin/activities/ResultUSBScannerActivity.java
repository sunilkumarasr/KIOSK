package com.provizit.kioskcheckin.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.provizit.kioskcheckin.logins.OTPActivity;
import com.provizit.kioskcheckin.mvvm.ApiViewModel;
import com.provizit.kioskcheckin.R;
import com.provizit.kioskcheckin.services.Conversions;
import com.provizit.kioskcheckin.services.GetCVisitorDetailsModel;
import com.provizit.kioskcheckin.utilities.IncompleteData;
import com.provizit.kioskcheckin.config.Preferences;

import org.json.JSONObject;

public class ResultUSBScannerActivity extends AppCompatActivity {
    private static final String TAG = "ResultUSBScannerActivit";

    ProgressDialog progress;
    ApiViewModel apiViewModel;
    String nda_Data = "";
    String emp_id = "";
    String location_id = "";
    String logoPass = "";
    String badge_Data = "false";
    String pic_Data = "false";
    String barcodeData = "";
    String ccp = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_usbscanner);

        //shared Preferences
        nda_Data = Preferences.loadStringValue(getApplicationContext(), Preferences.nda_Data, "");
        logoPass = Preferences.loadStringValue(getApplicationContext(), Preferences.logoPass, "");
        badge_Data = Preferences.loadStringValue(getApplicationContext(), Preferences.badge_Data, "");
        pic_Data = Preferences.loadStringValue(getApplicationContext(), Preferences.pic_Data, "");
        ccp = Preferences.loadStringValue(getApplicationContext(), Preferences.country_Code, "");
        barcodeData = Preferences.loadStringValue(getApplicationContext(), Preferences.barcodeData, "");

        apiViewModel = new ViewModelProvider(ResultUSBScannerActivity.this).get(ApiViewModel.class);

        progress = new ProgressDialog(this);
        progress.setTitle(getResources().getString(R.string.Loading));
        progress.setMessage(getResources().getString(R.string.whileloading));
        progress.setCancelable(true);
        progress.setCanceledOnTouchOutside(true);


        emp_id = Preferences.loadStringValue(getApplicationContext(), Preferences.email_id, "");
        location_id = Preferences.loadStringValue(getApplicationContext(), Preferences.location_id, "");


        qr_split();

    }


    private void qr_split() {
        Log.e("data_g",barcodeData);
        //Toast.makeText(getApplicationContext(),barcodeData,Toast.LENGTH_LONG).show();

//        String str = String.valueOf(barcodeData.charAt(0));

//        if (str.equalsIgnoreCase("+")){
//            String newStr = barcodeData.substring(4);
//            mobile_method(barcodeData);
//            progress.show();
//        }else if (ViewController.isValidEmail(barcodeData.trim())){
//            email_method(barcodeData.trim());
//            progress.show();
//        }else {
//            Toast.makeText(getApplicationContext(),"Wrong QR Scanned"+barcodeData,Toast.LENGTH_SHORT).show();
//
//            Intent intent = new Intent(getApplicationContext(), VisitorLoginActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//            startActivity(intent);
//        }

        String[] spre = barcodeData.split("###");

        for (String word : spre) {
            Toast.makeText(getApplicationContext(), word, Toast.LENGTH_SHORT).show();
        }

//        String regex = "###";
//        // split the string object
//        String[] output = barcodeData.split(regex);
//        String newVal = output[output.length - 1];
//        char first = newVal.charAt(0);
//        String str = String.valueOf(first);
//
//        if (str.equalsIgnoreCase("+")) {
//            String newStr = newVal.substring(4);
////            visitor_mobile.setText(newStr);
////            mobile_method();
//            Toast.makeText(getApplicationContext(),""+newStr,Toast.LENGTH_SHORT).show();
//        } else {
////            visitor_email.setText(newVal.trim());
////            email_method();
//                        Toast.makeText(getApplicationContext(),""+newVal.trim(),Toast.LENGTH_SHORT).show();
//        }
    }


    private void mobile_method(String newStr) {
        int otp = Conversions.getNDigitRandomNumber(4);
        Preferences.saveStringValue(ResultUSBScannerActivity.this, Preferences.otp, otp+"");
        String senderId = Preferences.loadStringValue(getApplicationContext(), Preferences.senderId, "");
        JSONObject jsonObj_ = new JSONObject();
        try {
            String newMobile = "+" + ccp + newStr;
            jsonObj_.put("mobile", newMobile);
            jsonObj_.put("otp", otp);
            jsonObj_.put("senderid", senderId);
            apiViewModel.verifylinkmobile(getApplicationContext(), jsonObj_);

        } catch (Exception e) {

        }
        apiViewModel.getcvisitordetails(getApplicationContext(), "comp_id", emp_id, ccp + newStr, location_id);
        apiViewModel.getResponseforCVisitor().observe(this, model -> {
            progress.dismiss();
            if (model.getResult() == 200) {
                Float visitor_status = model.getItems().getVisitorStatus();
                if (visitor_status == 0) {
                    model.setIncomplete_data(new IncompleteData());
                    model.getIncomplete_data().setEmail("");
                    model.getIncomplete_data().setMobile(newStr);
                }
                Preferences.saveStringValue(ResultUSBScannerActivity.this, Preferences.email_mobile_type, "mobile");

                Intent intent = new Intent(getApplicationContext(), OTPActivity.class);
                intent.putExtra("model_key", model);
                startActivity(intent);
            } else {
                AlertDialog alertDialog = new AlertDialog.Builder(ResultUSBScannerActivity.this)
                        .setMessage("You don't have access")
                        .setTitle("Access denied")
                        .setPositiveButton(android.R.string.ok, null)
                        .show();
            }
        });

    }

    public String testUnescapeJava(String myString) {
        String text = myString.replace("\u0000", "");
        return text;
    }

    private void email_method(String email_r) {

        int otp = Conversions.getNDigitRandomNumber(4);
        Preferences.saveStringValue(ResultUSBScannerActivity.this, Preferences.otp, otp+"");
        JSONObject jsonObj_ = new JSONObject();
        try {
            jsonObj_.put("email", testUnescapeJava(email_r));
            jsonObj_.put("val", testUnescapeJava(email_r));
            jsonObj_.put("otp", otp);
            apiViewModel.otpsendemailclient(getApplicationContext(), jsonObj_);
            apiViewModel.getResponseforotpsendemail().observe(this, model -> {
                if (model != null) {
                    Log.e(TAG, "onChangedOTP: " + model.getResult());

//                        Gson gson = new Gson();
//                        Toast.makeText(getApplicationContext(), gson.toJson(model.getResult()) + "", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(getApplicationContext(), "Model null", Toast.LENGTH_LONG).show();
                }
            });
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Exception " + e + "", Toast.LENGTH_LONG).show();
        }
        apiViewModel.getcvisitordetails(getApplicationContext(), "comp_id", emp_id, testUnescapeJava(email_r), location_id);
        apiViewModel.getResponseforCVisitor().observe(this, new Observer<GetCVisitorDetailsModel>() {
            @Override
            public void onChanged(GetCVisitorDetailsModel model) {
                progress.dismiss();
                if(model!=null){
                    try {
                        if (model.getResult() == 200) {
                            Float visitor_status = model.getItems().getVisitorStatus();
                            if (visitor_status == 0) {
                                model.setIncomplete_data(new IncompleteData());
                                model.getIncomplete_data().setEmail(testUnescapeJava(email_r));
                                model.getIncomplete_data().setMobile("");
                            }
                            Preferences.saveStringValue(ResultUSBScannerActivity.this, Preferences.email_mobile_type, "email");

                            Intent intent = new Intent(getApplicationContext(), OTPActivity.class);
                            intent.putExtra("model_key", model);
                            startActivity(intent);

                        } else {
                            AlertDialog alertDialog = new AlertDialog.Builder(ResultUSBScannerActivity.this)
                                    .setMessage("You don't have access")
                                    .setTitle("Access denied")
                                    .setPositiveButton(android.R.string.ok, null)
                                    .show();
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }

                }

            }

        });

    }


}