package com.provizit.kioskcheckin.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.TextView;

import com.provizit.kioskcheckin.activities.Meetings.MeetingRequestDetailsActivity2;
import com.provizit.kioskcheckin.config.Preferences;
import com.provizit.kioskcheckin.R;
import com.provizit.kioskcheckin.services.Conversions;
import com.provizit.kioskcheckin.services.GetCVisitorDetailsModel;

import java.io.ByteArrayOutputStream;

public class CamActivity extends AppCompatActivity {

    // Define the pic id
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    String filename;


    GetCVisitorDetailsModel model;
    String hid = "";
    String hiid = "";
    String host = "";
    String tvisitor = "";
    String acceptdata = "false";
    String badgedata = "false";
    // Define a constant for "model_key"
    private static final String MODEL_KEY = "model_key"; // New constant definition

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cam);
        Intent iin = getIntent();
        Bundle b = iin.getExtras();
        try {
            if (b != null) {
                host = iin.getStringExtra("host");
                hid = iin.getStringExtra("hid");
                hiid = iin.getStringExtra("hiid");
                tvisitor = iin.getStringExtra("tvisitor");
                model = (GetCVisitorDetailsModel) iin.getSerializableExtra(MODEL_KEY);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        //shared Preferences
        acceptdata = Preferences.loadStringValue(getApplicationContext(), Preferences.accept_Data, "");
        badgedata = Preferences.loadStringValue(getApplicationContext(), Preferences.badge_Data, "");


        // Start camera intent to capture image
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
        }

    }


    //disable auto click action after scann
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_ENTER) {
            // Barcode scanner has scanned a barcode, disable triggered items
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_DEL) {
            return true;
        }

        disableTriggeredItems();

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
                    Intent intent1 = new Intent(getApplicationContext(), ConfirmationActivity.class);
                    intent1.putExtra(MODEL_KEY, model);
                    startActivity(intent1);
                    return true;
                case KeyEvent.KEYCODE_DEL:
                    Log.d("KeyEvent", "DEL in dispatchKeyEvent");
                    break;
                default:
                    char keyChar = (char) event.getUnicodeChar();
                    if (Character.isLetterOrDigit(keyChar)) {
                        return true;
                    }
            }
        }
        return super.dispatchKeyEvent(event);
    }

    private void disableTriggeredItems() {
        runthred();
    }

    private void runthred() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                finish();
                overridePendingTransition(0, 0);
                startActivity(getIntent());
                overridePendingTransition(0, 0);

            }
        }, 500);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            // Get the captured image bitmap
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
            String path1 = MediaStore.Images.Media.insertImage(getApplicationContext().getContentResolver(), bitmap, "IMG_" + System.currentTimeMillis(), null);
            Uri filepath = Uri.parse(path1);

            filename = Conversions.datemillirandstring() + ".jpeg";


            //rotation
            int w = bitmap.getWidth();
            int h = bitmap.getHeight();
            Matrix mtx = new Matrix();
            mtx.postRotate(90);
//          mtx.postScale(-1, 1, w/2,h/2);
            Bitmap rotatedBMP = Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true);


            String encodedString = encodeToBase64(rotatedBMP, Bitmap.CompressFormat.JPEG, 50);

            Log.e("filepath", filepath +"");
            Log.e("filename",filename+"");
            Log.e("encodedString",encodedString+"");


            Intent intent = new Intent(getApplicationContext(), MeetingRequestDetailsActivity2.class);
            intent.putExtra("tvisitor", tvisitor);
            intent.putExtra(MODEL_KEY, model);
            intent.putExtra("host", host);
            intent.putExtra("hid", hid);
            intent.putExtra("hiid", hiid);
            intent.putExtra("uri", filepath + "");
            intent.putExtra("filename", filename);
            intent.putExtra("encodedString", encodedString);
            startActivity(intent);


        }
    }


    public String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent1 = new Intent(getApplicationContext(), ConfirmationActivity.class);
        intent1.putExtra(MODEL_KEY, model);
        startActivity(intent1);
    }


}