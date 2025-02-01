package com.provizit.kioskcheckin.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
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
import com.provizit.kioskcheckin.logins.AdminLoginActivity;
import com.provizit.kioskcheckin.logins.OTPPermitActivity;
import com.provizit.kioskcheckin.R;
import com.provizit.kioskcheckin.api.DataManger;
import com.provizit.kioskcheckin.config.Preferences;
import com.provizit.kioskcheckin.logins.VisitorLoginActivity;

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
        if (language.equals("ar")){
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
                startActivity( new Intent( getApplicationContext(), VisitorLoginActivity.class ) );
//                Intent intent = new Intent(getApplicationContext(), NDAPermitActivity.class);
//                intent.putExtra("comp_id", "679a2be30635066f680ac1ec");
//                intent.putExtra("valueType", "email");
//                intent.putExtra("qrValue", "aj169408@gmail.com");
//                intent.putExtra("permitType", "material");
//                intent.putExtra("ndaStatus", "ndaStatus");
//                startActivity(intent);
            }
        }, 3000);
    }

    //material###ftprovizitstc***679a2be30635066f680ac1ec###aj169408@gmail.com
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}