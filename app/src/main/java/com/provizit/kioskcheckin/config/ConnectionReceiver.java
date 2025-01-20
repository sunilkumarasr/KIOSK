package com.provizit.kioskcheckin.config;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;

import com.provizit.kioskcheckin.BuildConfig;

public class ConnectionReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (UsbManager.ACTION_USB_DEVICE_ATTACHED.equals(action)) {
            // A USB device is attached
            // Check if the attached device is your USB reader
            // Process the USB device according to your app's logic
        } else if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)) {
            // A USB device is detached
            // Handle USB device detachment
        }
    }

    public boolean isConnecteds(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        // Check if ConnectivityManager is null
        if (cm == null) {
            Log.e("NetworkError", "ConnectivityManager is null");
            return false;
        }

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // For Android M (API 23) and above
                Network network = cm.getActiveNetwork();
                if (network != null) {
                    NetworkCapabilities capabilities = cm.getNetworkCapabilities(network);
                    return capabilities != null && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
                }
            } else {
                // Fallback for devices below API 23
                Network[] networks = cm.getAllNetworks();
                for (Network network : networks) {
                    NetworkCapabilities capabilities = cm.getNetworkCapabilities(network);
                    if (capabilities != null && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)) {
                        return true;
                    }
                }
            }
        } catch (NullPointerException e) {
            // Handle the exception with proper logging
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "JSONException caught: " + e.getMessage(),e);
            } else {
                Log.e(TAG, "Error creating JSON object for login.", e);
            }
        } catch (Exception e) {
            Log.e("NetworkError", "Unexpected error occurred while checking network availability", e);
        }

        return false; // Return false if no internet capability is found
    }
}
