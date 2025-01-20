package com.provizit.kioskcheckin.activities.Meetings;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationSet;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.provizit.kioskcheckin.activities.YourRequestSentActivity;
import com.provizit.kioskcheckin.config.InterNetConnectivityCheck;
import com.provizit.kioskcheckin.R;
import com.provizit.kioskcheckin.services.Conversions;
import com.provizit.kioskcheckin.api.DataManger;
import com.provizit.kioskcheckin.services.GetCVisitorDetailsModel;
import com.provizit.kioskcheckin.services.VisitorActionModel;
import com.provizit.kioskcheckin.utilities.Belongings;
import com.provizit.kioskcheckin.config.Preferences;
import com.provizit.kioskcheckin.utilities.Vehicles;
import com.provizit.kioskcheckin.utilities.VisitorFormDetailsOtherArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MeetingRequestDetailsActivity1 extends AppCompatActivity {
    private static final String TAG = "MeetingRequestDetailsAc";
    Button proceed;
    ImageView emp_img,visitor_img;
    TextView name,mobile,email,organization,designation,emp_name,emp_department,emp_designation,emp_subject,meeting_room,meeting_st;

    Uri ImageUri;

    GetCVisitorDetailsModel model;
    String uri = "",hid="",hiid="", mid="",host="",tvisitor="";
    String filename = "";
    Float meeting_status;
    String badge = "";
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_request_details);
        Intent iin= getIntent();
        Bundle b = iin.getExtras();
        if(b!=null) {
            host = iin.getStringExtra("host");
            hid = iin.getStringExtra("hid");
            hiid = iin.getStringExtra("hiid");
            tvisitor = b.getString("tvisitor");
            uri = b.getString("uri");
            filename = b.getString("filename");
            model = (GetCVisitorDetailsModel) iin.getSerializableExtra("model_key");
        }

        proceed=findViewById(R.id.proceed);
        emp_img = findViewById(R.id.emp_img);
        visitor_img = findViewById(R.id.visitor_img);
        name = findViewById(R.id.name);
        mobile = findViewById(R.id.mobile);
        email = findViewById(R.id.email);
        organization = findViewById(R.id.organization);
        designation = findViewById(R.id.designation);
        emp_name =findViewById(R.id.emp_name);
        emp_department =findViewById(R.id.emp_department);
        emp_designation =findViewById(R.id.emp_designation);
        emp_subject =findViewById(R.id.emp_subject);
        meeting_room =findViewById(R.id.meeting_room);
        meeting_st =findViewById(R.id.meeting_st);

        ImageUri = Uri.parse(uri);
        image_set();


//        if (Uri.parse(uri) ==null){
//            emp_img.setImageResource(R.drawable.no_image_found);
//        }else {
//            Glide.with(getApplicationContext())
//                    .load(Uri.parse(uri))
//                    .centerCrop()
//                    .error(R.drawable.no_image_found)
//                    .into(emp_img);
//        }
        Log.e(TAG, "onCreate: imguri"+uri );

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String VersionName = sharedPreferences.getString("VersionName", "1.0");
        TextView txtVersion = findViewById(R.id.txtVersion);
        txtVersion.setText(VersionName);

        name.setText(model.getIncomplete_data().getName());
        mobile.setText(model.getIncomplete_data().getMobile());
        email.setText(model.getIncomplete_data().getEmail());
        organization.setText(model.getIncomplete_data().getCompany());
        designation.setText(model.getIncomplete_data().getDesignation());
        meeting_status = model.getItems().getMeetingStatus();
        if (model.getItems().getEmployee().getPic() != null && model.getItems().getEmployee().getPic().size() != 0) {
            //preferences
            String Comp_id = Preferences.loadStringValue(getApplicationContext(),Preferences.Comp_id,"");
            Glide.with(MeetingRequestDetailsActivity1.this).load(DataManger.IMAGE_URL + "/uploads/" + Comp_id + "/" + model.getItems().getEmployee().getPic().get(model.getItems().getEmployee().getPic().size() - 1))
                    .into(visitor_img);
        }

        if(meeting_status==1){
            mid=model.getItems().get_id().get$oid();
            host=model.getItems().getEmployee().get_id().get$oid();
            hid = model.getItems().getEmployee().getHierarchy_id();
            hiid = model.getItems().getEmployee().getHierarchy_indexid();
            emp_name.setText(model.getItems().getEmployee().getName());
            emp_department.setText(model.getItems().getEmployee().getDepartment());
            emp_designation.setText(model.getItems().getEmployee().getDesignation());
            emp_subject.setText(model.getItems().getSubject());
            meeting_room.setText(model.getItems().getMeetingrooms().getName());
            meeting_st.setText(Conversions.millitotime((model.getItems().getStart()+Conversions.timezone()) * 1000,false)
                    +" - "+Conversions.millitotime((model.getItems().getEnd()+1+Conversions.timezone()) * 1000,false));
        }else {
            emp_name.setText(model.getItems().getEmployee().getName());
            emp_department.setText(model.getItems().getEmployee().getDepartment());
            emp_designation.setText(model.getItems().getEmployee().getDesignation());
            emp_subject.setText(model.getItems().getSubject());
            meeting_room.setText("");
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            meeting_st.setText(dtf.format(now));
        }

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnimationSet animation1 = Conversions.animation();
                view.startAnimation(animation1);

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
                System.out.println("jsonObj_::"+other1.getData());
                VisitorFormDetailsOtherArray other2 = new VisitorFormDetailsOtherArray();
                other2.setData(model.getIncomplete_data().getCompany());
                other2.setLabel("Organization");
                other2.setStatus(true);
                other2.setActive(true);
                other2.setDisabled(true);
                other2.setDepends(false);
                other2.setProfessional(true);
                other2.setModel("company");
                System.out.println("jsonObj_::"+other2.getData());
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
                System.out.println("jsonObj_::"+other3.getData());

                Preferences.saveStringValue(getApplicationContext(), Preferences.badge, String.valueOf(Conversions.getNDigitRandomNumber(3)));

                JSONArray pic = new JSONArray();
                JSONArray live_pic = new JSONArray();
                live_pic.put(filename);
                if (model.getIncomplete_data().getPic().size() == 0){
                    ArrayList picArray = new ArrayList<>();
                    picArray.add(filename);
                    pic.put(filename);
                }else {
                    ArrayList<String> pics = model.getIncomplete_data().getPic();
                    try {
                        for (int i = 0; i < pics.size(); i++) {
                            pic.put(pics.get(i));
                        }
                    }catch (Exception e) {
                        Log.e(TAG, "Error processing vehicles: ", e);  // Proper logging
                    }
                    pic.put(filename);
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
                }catch (Exception e) {
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
                    jsonObj_.put("email",model.getIncomplete_data().getEmail());
                    jsonObj_.put("formtype", "edit");
                    jsonObj_.put("idnumber", model.getIncomplete_data().getIdnumber());
                    jsonObj_.put("mobile", model.getIncomplete_data().getMobile());
//                    jsonObj_.put("nda_id", model.getIncomplete_data().getNda_id());
//                    jsonObj_.put("nda_terms", model.getIncomplete_data().getNda_terms());
//                    jsonObj_.put("nda_time", model.getIncomplete_data().getNda_time());
                    jsonObj_.put("mobilecode", model.getIncomplete_data().getMobilecode());
                    jsonObj_.put("mobiledata", model.getIncomplete_data().getMobiledata().getMobiledata());
                    jsonObj_.put("mverify", model.getIncomplete_data().getMverify());
                    jsonObj_.put("name", model.getIncomplete_data().getName());
                    jsonObj_.put("other",others1);
                    jsonObj_.put("pic", pic);
                    jsonObj_.put("random", "4458");
                    jsonObj_.put("vehicles", vehicles1);
                    jsonObj_.put("verify", model.getIncomplete_data().getVerify());
                    jsonObj_.put("emp_id", Preferences.loadStringValue(getApplicationContext(),Preferences.email_id,""));
                    jsonObj_.put("id",model.getIncomplete_data().get_id().get$oid());
                    jsonObj_.put("purpose",model.getItems().getSubject());
                    jsonObj_.put("livepic",  live_pic);
                    jsonObj_.put("vlocation", Preferences.loadStringValue(getApplicationContext(),Preferences.location_id,""));
                    jsonObj_.put("hierarchy_id",hid);
                    jsonObj_.put("hierarchy_indexid",hiid);
                    jsonObj_.put("host",host);
                    jsonObj_.put("tvisitor",tvisitor);
                    jsonObj_.put("mid",mid);
                    jsonObj_.put("badge",badge);
                    if (InterNetConnectivityCheck.isOnline(getApplicationContext())) {
                        guestentry(jsonObj_);
                    } else{
                        DataManger.internetpopup(MeetingRequestDetailsActivity1.this);
//                        internetpopup();
                    }
//                    actioncheckinout(jsonObj_);
                } catch (Exception e) {
                    Log.e(TAG, "Error processing vehicles: ", e);  // Proper logging
                }
                System.out.println("jsonObj_"+jsonObj_);



            }
        });

        ImageView back_image=findViewById(R.id.back_image);
        back_image.setOnClickListener(view -> {
            AnimationSet animation1 = Conversions.animation();
            view.startAnimation(animation1);

            finish();
        });

    }

    private void guestentry(JSONObject jsonObj_) {
        DataManger dataManger = DataManger.getDataManager();
        Intent intent=new Intent(getApplicationContext(), YourRequestSentActivity.class);
        intent.putExtra("model_key",model);
        startActivity(intent);
        dataManger.guestentry(new Callback<VisitorActionModel>() {
            @Override
            public void onResponse(Call<VisitorActionModel> call, Response<VisitorActionModel> response) {
                Log.e(TAG, "actioncheckinout:-"+ response);
            }

            @Override
            public void onFailure(Call<VisitorActionModel> call, Throwable t) {
                Log.e(TAG, "onFailure"+t);
            }
        }, MeetingRequestDetailsActivity1.this, jsonObj_);
    }


    private void image_set() {
        Bitmap bmp = null;
        try {
            bmp = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver() , Uri.parse(uri));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        int w = bmp.getWidth();
        int h = bmp.getHeight();
        Matrix mtx = new Matrix();
        mtx.postRotate(0);
        Bitmap rotatedBMP = Bitmap.createBitmap(bmp, 0, 0, w, h, mtx, true);
        BitmapDrawable bmd = new BitmapDrawable(rotatedBMP);
        emp_img.setImageDrawable(bmd);
    }


}
