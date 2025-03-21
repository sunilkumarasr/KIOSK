package com.provizit.kioskcheckin.services;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;

import com.provizit.kioskcheckin.api.DataManger;

import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Conversions {
    private static Locale locale;

    // Hide the keyboard in the given activity
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        // Use static access with "android.content.Context" for "INPUT_METHOD_SERVICE".

        // Find the currently focused view to get the window token from it
        View view = activity.getCurrentFocus();

        // If no view currently has focus, create a new one to grab a window token
        if (view == null) {
            view = new View(activity);
        }

        // Hide the keyboard
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    // Generate a random number with n digits
    public static int getNDigitRandomNumber(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("The number of digits must be greater than 0.");
        }

        SecureRandom secureRandom = new SecureRandom();
        int minValue = (int) Math.pow(10, n - 1); // Minimum n-digit number
        int maxValue = (int) Math.pow(10, n) - 1; // Maximum n-digit number
        return secureRandom.nextInt((maxValue - minValue + 1)) + minValue;
    }

    public static int timezone() {
        Date d = new Date();
        int timeZone = d.getTimezoneOffset() * 60;
        return timeZone;
    }

    public static String millitotime(Long millSec, Boolean is24hours) {
        locale = new Locale(DataManger.appLanguage);
        DateFormat simple = new SimpleDateFormat("hh:mm aa", locale);

        if (is24hours) {
            simple = new SimpleDateFormat("HH:mm", locale);
        }
        Date result = new Date(millSec);
        String time = simple.format(result) + "";
        return time;
    }

    private static final String ALPHANUMERIC_STRING =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvwxyz";
    private static final SecureRandom secureRandom = new SecureRandom();

    public static String datemillirandstring() {

        // Get the current time in milliseconds
        long currentMillis = System.currentTimeMillis();

        StringBuilder sb = new StringBuilder(10);

        for (int i = 0; i < 10; i++) {
            // Generate a random index for the alphanumeric string
            int index = secureRandom.nextInt(ALPHANUMERIC_STRING.length());
            sb.append(ALPHANUMERIC_STRING.charAt(index));
        }

        // Combine the random string with the current timestamp
        return sb.toString() + currentMillis;
    }

    public static void main(String[] args) {
        // Test the method
        String result = datemillirandstring();
        System.out.println(result);
    }

    public static AnimationSet animation() {
        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
        fadeIn.setDuration(300);
        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator()); //and this
        fadeOut.setStartOffset(300);
        fadeOut.setDuration(300);
        AnimationSet animation = new AnimationSet(false); //change to false
        animation.addAnimation(fadeIn);

        return animation;
    }

    public static String millitodateD(Long millSec) {
        locale = new Locale(DataManger.appLanguage);
        DateFormat simple = new SimpleDateFormat("dd MMM yyyy", locale);
        Date result = new Date(millSec);
        String time = simple.format(result) + "";
        return time;
    }

    public static String millitodateandtime(Long millSec) {
        locale = new Locale(DataManger.appLanguage);
        DateFormat simple = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", locale);
        Date result = new Date(millSec);
        String time = simple.format(result) + "";
        return time;
    }

    // Function to convert Arabic numerals to English numerals
    public static String convertArabicToEnglish(String input) {
        return input.replace("٠", "0")
                .replace("١", "1")
                .replace("٢", "2")
                .replace("٣", "3")
                .replace("٤", "4")
                .replace("٥", "5")
                .replace("٦", "6")
                .replace("٧", "7")
                .replace("٨", "8")
                .replace("٩", "9");
    }

}
