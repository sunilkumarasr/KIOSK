package com.provizit.kioskcheckin.api;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.provizit.kioskcheckin.calendar.mycalendardata;
import com.provizit.kioskcheckin.services.BlockedvisitorrequestsModel;
import com.provizit.kioskcheckin.services.Blocklist_Model;
import com.provizit.kioskcheckin.services.CheckinoutModel;
import com.provizit.kioskcheckin.services.CommonModel;
import com.provizit.kioskcheckin.services.CommoncheckuserModel;
import com.provizit.kioskcheckin.services.CompanyDetailsModel;
import com.provizit.kioskcheckin.services.GetCVisitorDetailsModel;
import com.provizit.kioskcheckin.services.GetNdaActiveDetailsModel;
import com.provizit.kioskcheckin.services.GetSearchEmployeesModel;
import com.provizit.kioskcheckin.services.GetdocumentsModel;
import com.provizit.kioskcheckin.services.GetnationalityModel;
import com.provizit.kioskcheckin.services.GetpurposesModel;
import com.provizit.kioskcheckin.services.GetsubhierarchysModel;
import com.provizit.kioskcheckin.services.MeetingDetailsModel;
import com.provizit.kioskcheckin.services.Model;
import com.provizit.kioskcheckin.services.Privacypolicymodel;
import com.provizit.kioskcheckin.services.TvisitorsListModel;
import com.provizit.kioskcheckin.services.VcheckuserModel;
import com.provizit.kioskcheckin.services.VisitorActionModel;
import com.provizit.kioskcheckin.services.VisitorformDetailsModel;
import com.provizit.kioskcheckin.config.Preferences;
import com.provizit.kioskcheckin.utilities.EntryPermit.EntryPermitModel;
import com.provizit.kioskcheckin.utilities.WorkPermit.WorkPermitModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DataManger {


    //    public static final String Base_url = "https://devapi.provizit.com/provizit/resources/";
//    public static final String Base_url = "https://stcapi.provizit.com/provizit/resources/";
//    public static final String Base_url = "http://192.168.1.20:8080/provizit/resources/";
    public static final String Baseurl = "https://liveapi.provizit.com/provizit/resources/";
    public static final String IMAGE_URL = "https://provizit.com";
    private static final String TAG = "DataManger";
    private static final String BEARER_PREFIX = "Bearer "; // Define the constant for Bearer


    public static String appLanguage = "en";
    private static DataManger dataManager;
    private static AlertDialog dialog1;


    private Retrofit retrofit1, retrofit2;

    private DataManger() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.connectTimeout(30, TimeUnit.SECONDS);
        httpClient.readTimeout(30, TimeUnit.SECONDS);
        httpClient.writeTimeout(30, TimeUnit.SECONDS);
        httpClient.addInterceptor(logging);
        HttpLoggingInterceptor logging1 = new HttpLoggingInterceptor();
        logging1.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient1 = new OkHttpClient.Builder();
        httpClient1.connectTimeout(30, TimeUnit.SECONDS);
        httpClient1.readTimeout(120, TimeUnit.SECONDS);
        httpClient1.writeTimeout(120, TimeUnit.SECONDS);
        httpClient1.addInterceptor(logging1);


        HttpLoggingInterceptor loggingSecurity = new HttpLoggingInterceptor();
        loggingSecurity.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClientSecurity = new OkHttpClient.Builder();

        httpClientSecurity.connectTimeout(30, TimeUnit.SECONDS);
        httpClientSecurity.readTimeout(30, TimeUnit.SECONDS);
        httpClientSecurity.writeTimeout(30, TimeUnit.SECONDS);
        httpClientSecurity.addInterceptor(loggingSecurity);

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        retrofit1 = new Retrofit.Builder().baseUrl(IMAGE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient.build())
                .build();
        retrofit2 = new Retrofit.Builder()
                .baseUrl(Baseurl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient1.build())
                .build();
    }

    public static DataManger getDataManager() {
        if (dataManager == null) {
            dataManager = new DataManger();
        }
        return dataManager;
    }

    public static String Pwd_encrypt(Context context, String pwd, String val) {
        AESUtil aesUtil = new AESUtil(context);
        if (context != null) {
            return aesUtil.encrypt(pwd, val);
        }
        return "";
    }


    public static void internetpopup(Context context) {
        AlertDialog.Builder mbuilder = new AlertDialog.Builder(context);
        dialog1 = mbuilder.create();
        dialog1.show();
        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog1.setCancelable(false);
        dialog1.setCanceledOnTouchOutside(false);

    }

//    public String encrypt(Context context, Boolean isAdmin) {
//        AESUtil aesUtil = new AESUtil(context);
//        Calendar calendar = Calendar.getInstance();
//        if (isAdmin) {
//            return aesUtil.encrypt("admin_" + ((calendar.getTimeInMillis() / 1000) - timezone() - 60), "egems_2013_grms_2017_provizit_2020");
//        } else {
//            if (context != null) {
//                Log.e(TAG, "encrypt:Comp_id " + Preferences.loadStringValue(context, Preferences.Comp_id, ""));
//                System.out.println(Preferences.loadStringValue(context, Preferences.Comp_id, ""));
//                Log.e(TAG, "encrypt:Comp_id " + Preferences.loadStringValue(context, Preferences.Comp_id, ""));
//                return aesUtil.encrypt(Preferences.loadStringValue(context, Preferences.Comp_id, "") + "_" + ((calendar.getTimeInMillis() / 1000) - timezone() - 60), "egems_2013_grms_2017_provizit_2020");
//
//            }
//            return "";
//        }
//    }
    public String encrypt(Context context, Boolean isAdmin) {
        AESUtil aesUtil = new AESUtil(context);
        Calendar calendar = Calendar.getInstance();
        if (isAdmin) {
            return aesUtil.encrypt("admin_" + ((calendar.getTimeInMillis() / 1000) - timezone() - 60), "egems_2013_grms_2017_provizit_2020");
        } else {
            if (context != null) {
                String Comp_id = Preferences.loadStringValue(context, Preferences.Comp_id, "");
                SharedPreferences sharedPreferences1 = context.getSharedPreferences("EGEMSS_DATA", MODE_PRIVATE);
                Log.e(TAG, "encrypt:Comp_id " + Comp_id);
                return aesUtil.encrypt(sharedPreferences1.getString("company_id", null) + "_" + ((calendar.getTimeInMillis() / 1000) - timezone() - 60), "egems_2013_grms_2017_provizit_2020");
            }
            return "";
        }
    }



    public String Login_encrypt(Context context, Boolean isAdmin) {
        AESUtil aesUtil = new AESUtil(context);
        Calendar calendar = Calendar.getInstance();
        if (isAdmin) {
            return aesUtil.encrypt("admin_" + ((calendar.getTimeInMillis() / 1000) - timezone() - 60), "egems_2013_grms_2017_provizit_2020");
        } else {
            if (context != null) {
                return aesUtil.encrypt("STCPROAA02" + "_" + ((calendar.getTimeInMillis() / 1000) - timezone() - 60), "egems_2013_grms_2017_provizit_2020");
            }
            return "";
        }

    }

    public static int timezone() {
        Date d = new Date();
        int timeZone = d.getTimezoneOffset() * 60;
        return timeZone;
    }

    public void checkinuserlogin(Callback<Model> cb, Context context, JSONObject init_data) {
        API apiService = retrofit2.create(API.class);
        String newEncrypt = Login_encrypt(context, false);
        String bearer = BEARER_PREFIX + newEncrypt;
        try {
            String pwdEncrypt = Pwd_encrypt(context, init_data.getString("password"), init_data.getString("val"));
            init_data.put("password", pwdEncrypt);
        }catch (Exception e) {
            Log.e(TAG, "Error processing password: ", e);  // Proper logging
        }
        JsonObject data = new JsonObject();
        JsonParser jsonParser = new JsonParser();
        data = (JsonObject) jsonParser.parse(init_data.toString());
        Call<Model> call = apiService.checkinuserlogin(bearer, newEncrypt, data);
        call.enqueue((Callback<Model>) cb);
    }

    public void getndafdetials(Callback<GetNdaActiveDetailsModel> cb, Context context, String id, String type) {
        API apiService = retrofit2.create(API.class);
        String newEncrypt = encrypt(context, false);
        String bearer = BEARER_PREFIX + newEncrypt;
        Call<GetNdaActiveDetailsModel> call = apiService.getndafdetials(bearer, newEncrypt, id, type);
        call.enqueue((Callback<GetNdaActiveDetailsModel>) cb);
    }



    public void getcvisitordetails(Callback<GetCVisitorDetailsModel> cb, Context context, String comp_id, String emp_id, String id, String l_id) {
        API apiService = retrofit2.create(API.class);
        String newEncrypt = encrypt(context, false);
        String bearer = BEARER_PREFIX + newEncrypt;
        Long date = (new mycalendardata(0).getTimeMilli());
        Long datetime = date + (24 * 60 * 60);
        Call<GetCVisitorDetailsModel> call = apiService.getcvisitordetails(bearer, newEncrypt, comp_id, emp_id, id, l_id, date, datetime);
        call.enqueue((Callback<GetCVisitorDetailsModel>) cb);

    }


    public void getdocuments(Callback<GetdocumentsModel> cb, Context context) {
        API apiService = retrofit2.create(API.class);
        String newEncrypt = encrypt(context, false);
        String bearer = BEARER_PREFIX + newEncrypt;
        Call<GetdocumentsModel> call = apiService.getdocuments(bearer, newEncrypt);
        call.enqueue((Callback<GetdocumentsModel>) cb);
    }


    public void getpurposes(Callback<GetpurposesModel> cb, Context context) {
        API apiService = retrofit2.create(API.class);
        String newEncrypt = encrypt(context, false);
        String bearer = BEARER_PREFIX + newEncrypt;
        Call<GetpurposesModel> call = apiService.getpurposes(bearer, newEncrypt);
        call.enqueue((Callback<GetpurposesModel>) cb);
    }

    public void visitorformdetails(Callback<VisitorformDetailsModel> cb, Context context) {
        API apiService = retrofit2.create(API.class);
        String newEncrypt = encrypt(context, false);
        String bearer = BEARER_PREFIX + newEncrypt;
        Call<VisitorformDetailsModel> call = apiService.visitorformdetails(bearer, newEncrypt);
        call.enqueue((Callback<VisitorformDetailsModel>) cb);
    }

    public void getsubhierarchys(Callback<GetsubhierarchysModel> cb, Context context, String indexid, String type) {
        API apiService = retrofit2.create(API.class);
        String newEncrypt = encrypt(context, false);
        String bearer = BEARER_PREFIX + newEncrypt;
        Call<GetsubhierarchysModel> call = apiService.getsubhierarchys(bearer, newEncrypt, indexid, type);
        call.enqueue((Callback<GetsubhierarchysModel>) cb);
    }

    public void getsearchemployees(Callback<GetSearchEmployeesModel> cb, Context context, String l_id, String type, String h_id) {
        API apiService = retrofit2.create(API.class);
        String newEncrypt = encrypt(context, false);
        String bearer = BEARER_PREFIX + newEncrypt;
        Call<GetSearchEmployeesModel> call = apiService.getsearchemployees(bearer, newEncrypt, l_id, type, h_id);
        call.enqueue((Callback<GetSearchEmployeesModel>) cb);
    }

    public void getnationality(Callback<GetnationalityModel> cb, Context context, String doc_id) {
        API apiService = retrofit2.create(API.class);
        String newEncrypt = encrypt(context, false);
        String bearer = BEARER_PREFIX + newEncrypt;
        Call<GetnationalityModel> call = apiService.getnationality(bearer, newEncrypt, doc_id);
        call.enqueue((Callback<GetnationalityModel>) cb);
    }

    public void actionvisitor(Callback<VisitorActionModel> cb, Context context, JSONObject init_data, GetCVisitorDetailsModel model) {
        API apiService = retrofit2.create(API.class);
        String newEncrypt = encrypt(context, false);
        String bearer = BEARER_PREFIX + newEncrypt;
        JsonObject data = new JsonObject();
        JsonParser jsonParser = new JsonParser();
        data = (JsonObject) jsonParser.parse(init_data.toString());
        Call<VisitorActionModel> call = apiService.actionvisitor(bearer, newEncrypt, data);
        call.enqueue((Callback<VisitorActionModel>) cb);
    }

    public void visitorSignup(Callback<VisitorActionModel> cb, Context context, JSONObject init_data) {
        API apiService = retrofit2.create(API.class);
        String newEncrypt = encrypt(context, false);
        String bearer = BEARER_PREFIX + newEncrypt;
        JsonObject data = new JsonObject();
        JsonParser jsonParser = new JsonParser();
        data = (JsonObject) jsonParser.parse(init_data.toString());
        Call<VisitorActionModel> call = apiService.visitorSignup(bearer, newEncrypt, data);
        call.enqueue((Callback<VisitorActionModel>) cb);
    }

    public void guestentry(Callback<VisitorActionModel> cb, Context context, JSONObject init_data) {
        API apiService = retrofit2.create(API.class);
        String newEncrypt = encrypt(context, false);
        String bearer = BEARER_PREFIX + newEncrypt;
        JsonObject data = new JsonObject();
        JsonParser jsonParser = new JsonParser();
        data = (JsonObject) jsonParser.parse(init_data.toString());
        Call<VisitorActionModel> call = apiService.guestentry(bearer, newEncrypt, data);
        System.out.println("actiondata:" + data);
        call.enqueue((Callback<VisitorActionModel>) cb);
    }

    public void otpsendemailclient(Callback<VisitorActionModel> cb, Context context, JSONObject init_data) {
        API apiService = retrofit2.create(API.class);
        String newEncrypt = encrypt(context, false);
        String bearer = BEARER_PREFIX + newEncrypt;
        try {
            String pwdEncrypt = Pwd_encrypt(context, init_data.getString("otp"), init_data.getString("val"));
            String sotpEncrypt = Pwd_encrypt(context, init_data.getString("otp"), "egems_2013_grms_2017_provizit_2020");
            init_data.put("sotp", sotpEncrypt);
            init_data.put("otp", pwdEncrypt);
        }catch (Exception e) {
            Log.e(TAG, "Error processing otp: ", e);  // Proper logging
        }
        JsonObject data = new JsonObject();
        JsonParser jsonParser = new JsonParser();
        data = (JsonObject) jsonParser.parse(init_data.toString());
        Call<VisitorActionModel> call = apiService.otpsendemailclient(bearer, newEncrypt, data);
        call.enqueue((Callback<VisitorActionModel>) cb);
    }

    public void verifylinkmobile(Callback<VisitorActionModel> cb, Context context, JSONObject init_data) {
        API apiService = retrofit2.create(API.class);
        String newEncrypt = encrypt(context, false);
        String bearer = BEARER_PREFIX + newEncrypt;
        try {
            String pwdEncrypt = Pwd_encrypt(context, init_data.getString("otp"), init_data.getString("mobile"));
            init_data.put("otp", pwdEncrypt);
        } catch (Exception e) {
            Log.e(TAG, "Error processing pwdEncrypt: ", e);  // Proper logging
        }
        JsonObject data = new JsonObject();
        JsonParser jsonParser = new JsonParser();
        data = (JsonObject) jsonParser.parse(init_data.toString());
        Call<VisitorActionModel> call = apiService.verifylinkmobile(bearer, newEncrypt, data);
        call.enqueue((Callback<VisitorActionModel>) cb);
    }

    public void qrindex(Callback<String> cb, Context context, JsonObject imdata) {
        API apiService = retrofit1.create(API.class);
        String newEncrypt = encrypt(context, false);
        String bearer = BEARER_PREFIX + newEncrypt;
        Call<String> call = apiService.qrindex(bearer, newEncrypt, imdata);
        call.enqueue((Callback<String>) cb);
    }

    public void getuserLDetails(Callback<CompanyDetailsModel> cb, Context context, String type) {
        API apiService = retrofit2.create(API.class);
        String newEncrypt = encrypt(context, false);
        String bearer = BEARER_PREFIX + newEncrypt;
        Call<CompanyDetailsModel> call = apiService.getuserLDetails(bearer, newEncrypt, type);
        call.enqueue((Callback<CompanyDetailsModel>) cb);
    }

    public void gettvisitors(Callback<TvisitorsListModel> cb, Context context) {
        API apiService = retrofit2.create(API.class);
        String newEncrypt = encrypt(context, false);
        String bearer = BEARER_PREFIX + newEncrypt;
        Call<TvisitorsListModel> call = apiService.gettvisitors(bearer, newEncrypt);
        call.enqueue((Callback<TvisitorsListModel>) cb);
    }

    public void vcheckuser(Callback<VcheckuserModel> cb, Context context, String usertype, String val) {
        API apiService = retrofit2.create(API.class);
        String newEncrypt = encrypt(context, false);
        String bearer = BEARER_PREFIX + newEncrypt;
        Call<VcheckuserModel> call = apiService.vcheckuser(bearer, newEncrypt, usertype, val);
        call.enqueue((Callback<VcheckuserModel>) cb);
    }

    public void getuserslotdetails(Callback<CommonModel> cb, Context context, String id) {
        API apiService = retrofit2.create(API.class);
        String newEncrypt = encrypt(context, false);
        String bearer = BEARER_PREFIX + newEncrypt;
        Call<CommonModel> call = apiService.getuserslotdetails(bearer, newEncrypt, id);
        call.enqueue((Callback<CommonModel>) cb);
    }

    public void actioncheckinout(Callback<CheckinoutModel> cb, Context context, JSONObject init_data) {
        API apiService = retrofit2.create(API.class);
        String newEncrypt = encrypt(context, false);
        String bearer = BEARER_PREFIX + newEncrypt;
        JsonObject data = new JsonObject();
        JsonParser jsonParser = new JsonParser();
        data = (JsonObject) jsonParser.parse(init_data.toString());
        Call<CheckinoutModel> call = apiService.actioncheckinout(bearer, newEncrypt, data);
        call.enqueue((Callback<CheckinoutModel>) cb);
    }

    public void commoncheckuser(Callback<CommoncheckuserModel> cb, Context context, String usertype, String val) {
        API apiService = retrofit2.create(API.class);
        String newEncrypt = encrypt(context, false);
        String bearer = BEARER_PREFIX + newEncrypt;
        Call<CommoncheckuserModel> call = apiService.commoncheckuser(bearer, newEncrypt, usertype, val);
        call.enqueue((Callback<CommoncheckuserModel>) cb);
    }

    public void getprivacypolicydetials(Callback<Privacypolicymodel> cb, Context context, String id, String type) {
        API apiService = retrofit2.create(API.class);
        String newEncrypt = encrypt(context, false);
        String bearer = BEARER_PREFIX + newEncrypt;
        Call<Privacypolicymodel> call = apiService.getprivacypolicydetials(bearer, newEncrypt, id, type);
        call.enqueue((Callback<Privacypolicymodel>) cb);
    }

    //Visitor Blocklist
    public void getcblacklistdetails(Callback<Blocklist_Model> cb, Context context, String comp_id) {
        API apiService = retrofit2.create(API.class);
        String newEncrypt = encrypt(context, false);
        String bearer = BEARER_PREFIX + newEncrypt;
        Call<Blocklist_Model> call = apiService.getcblacklistdetails(bearer, newEncrypt, comp_id);
        call.enqueue((Callback<Blocklist_Model>) cb);
    }

    //blockedvisitorrequests

    public void getblockedvisitorrequests(Callback<BlockedvisitorrequestsModel> cb, Context context, String comp_id, String emp_id) {
        API apiService = retrofit2.create(API.class);
        String newEncrypt = encrypt(context, false);
        String bearer = BEARER_PREFIX + newEncrypt;
        Call<BlockedvisitorrequestsModel> call = apiService.getblockedvisitorrequests(bearer, newEncrypt, comp_id, emp_id);
        call.enqueue((Callback<BlockedvisitorrequestsModel>) cb);
    }

    //work permit
    public void getworkpermitDetails(Callback<WorkPermitModel> cb, Context context, String id) {
        API apiService = retrofit2.create(API.class);
        String newEncrypt = encrypt(context, false);
        String bearer = BEARER_PREFIX + newEncrypt;
        Call<WorkPermitModel> call = apiService.getworkpermitDetails(bearer, newEncrypt, id);
        call.enqueue((Callback<WorkPermitModel>) cb);
    }

    //work permit checkIn Status
    public void updateworkpermita(Callback<WorkPermitModel> cb, Context context, JsonObject jsonObject) {
        API apiService = retrofit2.create(API.class);
        String newEncrypt = encrypt(context, false);
        String bearer = BEARER_PREFIX + newEncrypt;
        JsonObject data = new JsonObject();
        JsonParser jsonParser = new JsonParser();
        data = (JsonObject) jsonParser.parse(jsonObject.toString());
        Call<WorkPermitModel> call = apiService.updateworkpermita(bearer, newEncrypt, data);
        call.enqueue((Callback<WorkPermitModel>) cb);
    }

    public void getentrypermitdetails(Callback<EntryPermitModel> cb, Context context, String id) {
        API apiService = retrofit2.create(API.class);
        String newEncrypt = encrypt(context, false);
        String bearer = BEARER_PREFIX + newEncrypt;
        Call<EntryPermitModel> call = apiService.getentrypermitdetails(bearer, newEncrypt, id);
        call.enqueue((Callback<EntryPermitModel>) cb);
    }



    public void materialcheckin(Callback<EntryPermitModel> cb, Context context, JsonObject jsonObject) {
        API apiService = retrofit2.create(API.class);
        String newEncrypt = encrypt(context, false);
        String bearer = BEARER_PREFIX + newEncrypt;
        JsonObject data = new JsonObject();
        JsonParser jsonParser = new JsonParser();
        data = (JsonObject) jsonParser.parse(jsonObject.toString());
        Call<EntryPermitModel> call = apiService.materialcheckin(bearer, newEncrypt, data);
        call.enqueue((Callback<EntryPermitModel>) cb);
    }

    public void getmeetingdetails(Callback<MeetingDetailsModel> cb, Context context, String id) {
        API apiService = retrofit2.create(API.class);
        String newEncrypt = encrypt(context, false);
        String bearer = BEARER_PREFIX + newEncrypt;
        Call<MeetingDetailsModel> call = apiService.getmeetingdetails(bearer, newEncrypt, id);
        call.enqueue((Callback<MeetingDetailsModel>) cb);
    }


}
