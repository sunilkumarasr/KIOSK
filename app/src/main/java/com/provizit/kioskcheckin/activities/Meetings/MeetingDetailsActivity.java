package com.provizit.kioskcheckin.activities.Meetings;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.provizit.kioskcheckin.activities.YourRequestSentActivity;
import com.provizit.kioskcheckin.config.InterNetConnectivityCheck;
import com.provizit.kioskcheckin.logins.VisitorLoginActivity;
import com.provizit.kioskcheckin.mvvm.ApiViewModel;
import com.provizit.kioskcheckin.R;
import com.provizit.kioskcheckin.services.Conversions;
import com.provizit.kioskcheckin.api.DataManger;
import com.provizit.kioskcheckin.services.GetCVisitorDetailsModel;
import com.provizit.kioskcheckin.config.Preferences;
import com.provizit.kioskcheckin.services.GetNdaActiveDetailsModel;
import com.provizit.kioskcheckin.services.VisitorActionModel;
import com.provizit.kioskcheckin.utilities.Belongings;
import com.provizit.kioskcheckin.utilities.TotalCounts;
import com.provizit.kioskcheckin.utilities.Vehicles;
import com.provizit.kioskcheckin.utilities.VisitorFormDetailsOtherArray;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MeetingDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    GetCVisitorDetailsModel model;
    String pic_Data = "false";
    String tvisitor = "false";
    String badge_Data = "false";
    TextView name, date, Where, department, txt_time, btn_newmeeting, pointer, visitor_name,subject,traning_text;
    Button btn_continue;
    ImageView emp_img, back_image, company_logo;
    String hid = "", hiid = "", mid = "", host = "", hierarchyname = "";
    String badge = "";
    ApiViewModel apiViewModel;
    GetNdaActiveDetailsModel ndamodel;
    String Comp_id = "";
    String nda_id = "";
    String accept_Data = "false";
    String filename="";
    Uri fileUri;
    Bitmap thumbnail;
    Bitmap bitmap;
    String encodedString;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_meeting_request);
        Intent iin = getIntent();
        Bundle b = iin.getExtras();
        try {
            if (b != null) {
                hid = iin.getStringExtra("hid");
                hiid = iin.getStringExtra("hiid");
                tvisitor = iin.getStringExtra("tvisitor");
                hierarchyname = iin.getStringExtra("hierarchyname");
                model = (GetCVisitorDetailsModel) iin.getSerializableExtra("model_key");
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        //shared Preferences
        Comp_id = Preferences.loadStringValue(getApplicationContext(), Preferences.Comp_id, "");
        pic_Data = Preferences.loadStringValue(getApplicationContext(), Preferences.pic_Data, "");
        badge_Data = Preferences.loadStringValue(getApplicationContext(), Preferences.badge_Data, "");
        nda_id = Preferences.loadStringValue(getApplicationContext(), Preferences.nda_id, "");
        accept_Data = Preferences.loadStringValue(getApplicationContext(), Preferences.accept_Data, "");
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String VersionName = sharedPreferences.getString("VersionName", "1.0");
        TextView txtVersion = findViewById(R.id.txtVersion);
        txtVersion.setText(VersionName);

        back_image = findViewById(R.id.back_image);
        name = findViewById(R.id.name);
        date = findViewById(R.id.date);
        Where = findViewById(R.id.Where);
        traning_text = findViewById(R.id.traning_text);
        pointer = findViewById(R.id.pointer);
        btn_continue = findViewById(R.id.btn_continue);
        btn_continue.setTypeface(ResourcesCompat.getFont(this, R.font.arbfonts_dinnextttlrabic_medium));
        btn_newmeeting = findViewById(R.id.btn_newmeeting);
        emp_img = findViewById(R.id.emp_img);
        txt_time = findViewById(R.id.txt_time);
        company_logo = findViewById(R.id.company_logo);
        department = findViewById(R.id.department);
        visitor_name = findViewById(R.id.visitor_name);
        subject = findViewById(R.id.subject);

        apiViewModel = new ViewModelProvider(MeetingDetailsActivity.this).get(ApiViewModel.class);

        apiViewModel.getndafdetials(getApplicationContext(), "id", "active");

        mid = model.getItems().get_id().get$oid();
        host = model.getItems().getEmployee().get_id().get$oid();
        hid = model.getItems().getEmployee().getHierarchy_id();
        hiid = model.getItems().getEmployee().getHierarchy_indexid();
        hierarchyname = model.getItems().getEmployee().getDepartment();

        visitor_name.setText(model.getIncomplete_data().getName());
        name.setText(model.getItems().getEmployee().getName());
        department.setText(model.getItems().getEmployee().getDepartment());
        subject.setText(model.getItems().getSubject());

        if (model != null && model.getItems() != null && model.getItems().getEmployee() != null) {
            String trdAccess = String.valueOf(model.getItems().getEmployee().getTrd_access());

            if (trdAccess.equalsIgnoreCase("true")) {
                traning_text.setText(getResources().getString(R.string.YouHaveATrainingWith));
            }else if(model.getItems().getTrain_meet().equalsIgnoreCase("meeting")){
                traning_text.setText(getResources().getString(R.string.YouHaveAMeetingWith));
            }else {
                traning_text.setText(getResources().getString(R.string.YouHaveATrainingWith));
            }

//            if(model.getItems().getTrain_meet().equalsIgnoreCase("meeting")){
//                traning_text.setText("You have a meeting with");
//            }else {
//                traning_text.setText("You have a training with");
//            }

        } else {
            traning_text.setText(getResources().getString(R.string.EmployeeOrMeetingDetailsAreMissing));
        }
        Where.setText(model.getItems().getMeetingrooms().getName());
        pointer.setText(model.getItems().getMeetingrooms().getPointer());
        DateTimeFormatter dtf = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            dtf = DateTimeFormatter.ofPattern("EE dd MMMM yyyy");
        }
        LocalDateTime now = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            now = LocalDateTime.now();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            date.setText(dtf.format(now));
        }
        txt_time.setText((Conversions.millitotime((model.getItems().getStart() + Conversions.timezone()) * 1000, false) + " to " + Conversions.millitotime((model.getItems().getEnd() + 1 + Conversions.timezone()) * 1000, false)));
        if (model.getItems().getEmployee().getPic() != null && model.getItems().getEmployee().getPic().size() != 0) {

            if (model.getItems().getEmployee().getPic().get(model.getItems().getEmployee().getPic().size() - 1).equalsIgnoreCase("") || model.getItems().getEmployee().getPic().get(model.getItems().getEmployee().getPic().size() - 1) == null) {
                emp_img.setImageResource(R.drawable.ic_user_white);
            } else {
                Glide.with(MeetingDetailsActivity.this).load(DataManger.IMAGE_URL + "/uploads/" + Comp_id + "/" + model.getItems().getEmployee().getPic().get(model.getItems().getEmployee().getPic().size() - 1)).into(emp_img);
            }
        } else {
            emp_img.setImageResource(R.drawable.ic_user_white);
        }

       //company logo
        String c_Logo = Preferences.loadStringValue(getApplicationContext(), Preferences.company_Logo, "");
        if (c_Logo.equalsIgnoreCase("")) {
        } else {
            Glide.with(MeetingDetailsActivity.this).load(c_Logo)
                    .into(company_logo);
        }
        // Check if the layout direction is right-to-left
        if (isRightToLeft()) {
            // If layout direction is right-to-left, mirror the image
            back_image.setScaleX(-1f);
        } else {
            // If layout direction is left-to-right, reset the image scaling
            back_image.setScaleX(1f);
        }
        apiViewModel.getResponseforNdaActiveDetails().observe(this, model -> {
            ndamodel = model;
            if (ndamodel.getResult() == 201) {
                nda_id = "";
            } else {
                nda_id = ndamodel.getItems().get_id().get$oid();
            }
        });
        apiViewModel.getResponseforCVisitor().observe(this, ndamodel -> {
            try {
                if (ndamodel.getResult() == 200) {
                    Intent intent;
                    float meeting_status = model.getItems().getMeetingStatus();
                    if (meeting_status == 1) {
                        intent = new Intent(getApplicationContext(), MeetingDetailsActivity.class);
                    } else {
                        intent = new Intent(getApplicationContext(), MeetingRequestActivity.class);
                    }
                    intent.putExtra("model_key", ndamodel);
                    startActivity(intent);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        });
        back_image.setOnClickListener(this);
        btn_continue.setOnClickListener(this);
        btn_newmeeting.setOnClickListener(this);
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
                    intent.putExtra("model_key", model);
                    startActivity(intent);
                    return true;
                default:
                    char keyChar = (char) event.getUnicodeChar();
                    return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }

    private void disableTriggeredItems() {
        back_image.setFocusable(true);
        back_image.setFocusableInTouchMode(true);
        btn_continue.setFocusable(false);
        btn_continue.setFocusableInTouchMode(false);
        btn_newmeeting.setFocusable(false);
        btn_newmeeting.setFocusableInTouchMode(false);

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
            btn_continue.setFocusable(true);
            btn_continue.setFocusableInTouchMode(true);
            btn_newmeeting.setFocusable(true);
            btn_newmeeting.setFocusableInTouchMode(true);
        }, 500);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_image:
                Intent intent1 = new Intent(getApplicationContext(), VisitorLoginActivity.class);
                startActivity(intent1);
                break;
            case R.id.btn_continue:
                if (pic_Data.equalsIgnoreCase("true")) {

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

                    Object_post();
                }
                break;
            case R.id.btn_newmeeting:
                Intent intents = new Intent(getApplicationContext(), MeetingRequestActivity.class);
                intents.putExtra("model_key", model);
                startActivity(intents);
                break;
        }
    }

    // Function to check if the layout direction is right-to-left
    private boolean isRightToLeft() {
        return getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_RTL;
    }

    //capture camera
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    if (Build.VERSION.SDK_INT >= 29) {
                        try (ParcelFileDescriptor pfd = MeetingDetailsActivity.this.getContentResolver().openFileDescriptor(fileUri, "r")) {
                            if (pfd != null) {
                                String path = getRealPathFromURI(fileUri);
                                bitmap = BitmapFactory.decodeFileDescriptor(pfd.getFileDescriptor());
                                encodedString = encodeTobase64(BitmapFactory.decodeFile(path), true);
                            }
                        }catch (Exception e) {
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
                        progressDialog = new ProgressDialog(MeetingDetailsActivity.this);
                        progressDialog.setCancelable(false);
                        progressDialog.setMessage("Loading...");
                        progressDialog.show();
                    } catch (Exception e) {
                        Log.e(TAG, "Error processing vehicles: ", e);  // Proper logging
                    }
                   Object_post();

                } catch (Exception e) {
                    Log.e(TAG, "Error processing vehicles: ", e);  // Proper logging
                }
            } else {
            }
        }
    }

    private void Object_post() {
//                    Object post
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
            } catch (Exception e) {
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
        } catch (Exception e) {
            Log.e(TAG, "Error processing vehicles: ", e);  // Proper logging
        }
        try {
            others1 = new JSONArray();
            for (int i = 0; i < others.size(); i++) {
                others1.put(others.get(i).getothers());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error processing vehicles: ", e);  // Proper logging
        }
        try {
            belongings1 = new JSONArray();
            for (int i = 0; i < belongings.size(); i++) {
                belongings1.put(belongings.get(i).getbelongings());
            }
        } catch (Exception e) {
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
            } catch (Exception e) {
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
            jsonObj_.put("livepic", livePic);
            jsonObj_.put("vlocation", Preferences.loadStringValue(getApplicationContext(), Preferences.location_id, ""));
            jsonObj_.put("hierarchy_id", hid);
            jsonObj_.put("hierarchy_indexid", hiid);
            jsonObj_.put("hierarchyname", hierarchyname);
            jsonObj_.put("host", host);
            jsonObj_.put("tvisitor", tvisitor);
            jsonObj_.put("mid", mid);
            jsonObj_.put("badge", badge);
            jsonObj_.put("nda_terms", "true");
            jsonObj_.put("nda_id", nda_id);
            if (InterNetConnectivityCheck.isOnline(getApplicationContext())) {
                actioncheckinout(jsonObj_);
            } else {
                DataManger.internetpopup(MeetingDetailsActivity.this);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error processing vehicles: ", e);  // Proper logging
        }

        Intent intent = new Intent(getApplicationContext(), YourRequestSentActivity.class);
        intent.putExtra("tvisitor", "");
        intent.putExtra("model_key", model);
        intent.putExtra("host", "");
        intent.putExtra("hid", "");
        intent.putExtra("hiid", "");
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
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                progressDialog.dismiss();
            }
        }, MeetingDetailsActivity.this, gsonObject);
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
        }, MeetingDetailsActivity.this, jsonObj_);
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


}