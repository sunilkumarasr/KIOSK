package com.provizit.kioskcheckin.config;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;
import android.content.SharedPreferences;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Log;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import com.google.gson.Gson;

import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import javax.crypto.KeyGenerator;

public class Preferences {

    private static final String PREFERENCE_NAME = "CHECKIN_DATA";

    //KEYS
    public final static String Login_Status = "Login_Status";
    public final static String Emp_id = "Emp_id";
    public final static String Comp_id = "Comp_id";
    public final static String company_Logo = "company_Logo";
    public final static String email_id = "email_id";
    public final static String email = "email";
    public final static String nda_Data = "nda_Data";
    public final static String badge_Data = "badge_Data";
    public final static String pic_Data = "pic_Data";
    public final static String accept_Data = "accept_Data";
    public final static String P_policy = "P_policy";
    public final static String otp = "otp";
    public final static String country_Code = "country_Code";
    public final static String barcodeData = "barcodeData";
    public final static String nda_id = "nda_id";
    public final static String badge = "badge";
    public final static String nda_terms = "nda_terms";
    public final static String email_mobile_type = "email_mobile_type";
    public final static String logoPass = "logoPass";
    public final static String senderId = "senderId";
    public final static String subdomain = "subdomain";
    public final static String qrUrl = "qrUrl";
    public final static String location_id = "location_id";
    public final static String language = "language";
    public final static String pic = "pic";
    public final static String blocking = "blocking";
    public final static String VersionName = "VersionName";
    public final static String meetingId = "meetingId";


    public static void saveFloatValue(Context context, String key, float value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).edit();
        editor.putFloat(key, value);
        editor.apply();

    }

    public static float loadFloatValue(Context mContext, String from, float defValue) {
        return mContext.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).getFloat(from, defValue);
    }

    public static void saveStringValue(Context context, String key, String value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String loadStringValue(Context mContext, String from, String defValue) {
        return mContext.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).getString(from, defValue);
    }

    public static void saveLongValue(Context context, String key, long value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).edit();
        editor.putLong(key, value);
        editor.apply();
    }

    public static long loadLongValue(Context mContext, String from, long defValue) {
        return mContext.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).getLong(from, defValue);
    }

    public static void saveIntegerValue(Context context, String key, int value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static int loadIntegerValue(Context mContext, String from, int defValue) {
        return mContext.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).getInt(from, defValue);
    }

    public static void saveBooleanValue(Context context, String key, boolean value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).edit();
        editor.putBoolean(key, value);
        editor.apply();
    }
    public static boolean loadBooleanValue(Context context, String key, boolean defValue) {
        return context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).getBoolean(key, defValue);
    }

    public static void deleteSharedPreferences(Context context){
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).edit();
        editor.clear().commit();
    }

    public static void saveContactAsynParser(Context context, String key, String value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).edit();
        Gson gson = new Gson();
        String json = gson.toJson(value);
        editor.putString(key,json);
        editor.apply();
    }

    public static String loadContactAsynParser(Context mContext, String from, String defValue) {
        return mContext.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).getString(from, defValue);
    }

}