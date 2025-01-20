package com.provizit.kioskcheckin.config;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import androidx.annotation.NonNull;

public class InterNetConnectivityCheck {
    public static boolean isOnline(@NonNull Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nInfo = cm.getActiveNetworkInfo();
            return nInfo != null && nInfo.isConnected();
        }
        catch (Exception e) {
            Log.e(TAG, "Error processing vehicles: ", e);  // Proper logging
            return false;
        }

    }
}
