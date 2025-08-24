package com.provizit.kioskcheckin.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.widget.TextView;

import com.provizit.kioskcheckin.config.ViewController;
import com.provizit.kioskcheckin.logins.AdminLoginActivity;
import com.provizit.kioskcheckin.logins.OTPPermitActivity;
import com.provizit.kioskcheckin.R;
import com.provizit.kioskcheckin.api.DataManger;
import com.provizit.kioskcheckin.config.Preferences;
import com.provizit.kioskcheckin.logins.VisitorLoginActivity;
import com.provizit.kioskcheckin.mvvm.ApiViewModel;

import java.util.Locale;

public class SplashActivity extends AppCompatActivity {

    TextView txtVersion;

    String[] PERMISSIONS = {
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    int PERMISSION_ALL = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        txtVersion = findViewById(R.id.txtVersion);

        versionCheck();

        methodRun();

        SplashAnimation();


    }

    private void versionCheck() {
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            int versionCode = packageInfo.versionCode;
            String versionName = packageInfo.versionName;
            txtVersion.setText(getString(R.string.Version) + " " + versionName);
            SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("VersionName",  txtVersion.getText().toString());
            editor.apply();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void methodRun() {
        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }else{
            SplashAnimation();
        }
        String language = Preferences.loadStringValue(getApplicationContext(),Preferences.language,"");
        if (language.equalsIgnoreCase("ar")){
            Locale myLocale = new Locale("ar");
            DataManger.appLanguage = "ar";
            Resources res = getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = myLocale;
            conf.setLayoutDirection(myLocale);
            res.updateConfiguration(conf, dm);
        }
        else {
            Locale myLocale = new Locale("en");
            DataManger.appLanguage = "en";
            Resources res = getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = myLocale;
            conf.setLayoutDirection(myLocale);
            res.updateConfiguration(conf, dm);
        }
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (hasPermissions(this, PERMISSIONS)) {
                    SplashAnimation();
                }
            }
        }
    }

    private void SplashAnimation() {
        String Login_Status = Preferences.loadStringValue(getApplicationContext(), Preferences.Login_Status, "");
        new Handler().postDelayed(() -> {
            if (Login_Status.equalsIgnoreCase("")) {
                startActivity( new Intent( getApplicationContext(), AdminLoginActivity.class ) );
            } else {
//                startActivity( new Intent( getApplicationContext(), VisitorLoginActivity.class ) );

//                Intent intent = new Intent(getApplicationContext(), WorkPermitFormActivity.class);
//                intent.putExtra("model_key", "model");
//                startActivity(intent);

//                Intent intent = new Intent(getApplicationContext(), NDAPermitActivity.class);
//                intent.putExtra("comp_id", "6810a810068b1114eb361f5d");
//                intent.putExtra("valueType", "email");
//                intent.putExtra("inputValue", "aj169408@gmail.com");
//                intent.putExtra("permitType", "workpermit");
//                intent.putExtra("ndaStatus", "true");
//                startActivity(intent);

            }
        }, 3000);
    }
//    workpermit###ftprovizitstc***680b2e0490c70a68ed2a6e66###aj169408@gmail.com
//    mailto:material###ftprovizitstc***67bd9a2be4bc7c77c5c55b05###aj169408@gmail.com
//    mailto:material###ftprovizitstc***67bda6dce4bc7c77c5c55b0f###aj169408@gmail.com
//    mailto:workpermit###ftprovizitstc***67bda73ce4bc7c77c5c55b10###aj169408@gmail.com
//    workpermit###ftprovizitstc***67bff639e4bc7c77c5c55b9a###aj169408@gmail.com
//    workpermit###ftprovizitstc***67bff536e4bc7c77c5c55b98###aj169408@gmail.com
//    mailto:workpermit###ftprovizitstc***67c156abe4bc7c77c5c55bf2###aj169408@gmail.com
//    workpermit###ftprovizitstc***67c6d4b89aa7e824a9bf5ff0###aj169408@gmail.com
//    material###ftprovizitstc***67c6d2e89aa7e824a9bf5fef###aj169408@gmail.com
//    mailto:material###ftprovizitstc***67c949b99aa7e824a9bf602a###aj169408@gmail.com

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}