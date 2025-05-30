package com.provizit.kioskcheckin.activities;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AnimationSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.provizit.kioskcheckin.R;
import com.provizit.kioskcheckin.activities.Meetings.MeetingRequestDetailsActivity2;
import com.provizit.kioskcheckin.api.DataManger;
import com.provizit.kioskcheckin.config.Preferences;
import com.provizit.kioskcheckin.config.ShowCamera;
import com.provizit.kioskcheckin.logins.VisitorLoginActivity;
import com.provizit.kioskcheckin.mvvm.ApiViewModel;
import com.provizit.kioskcheckin.services.Conversions;
import com.provizit.kioskcheckin.services.GetCVisitorDetailsModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PermitCaptureImageActivity extends AppCompatActivity implements View.OnClickListener {

    ApiViewModel apiViewModel;
    private static final int RE = 1;
    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;
    FrameLayout fragment_complaint_image_set;
    LinearLayout relative_capture;
    ImageView img_back;
    ImageView click_img;
    ImageView img_gallery;
    android.hardware.Camera camera;
    ShowCamera showCamera;
    //profile image upload
    private int PICK_IMAGE_REQUEST = 0;
    private Uri filepath;
    private Bitmap bitmap;
    String filename;
    String image_name;
    String accept_Data = "false";
    String badge_Data = "false";
    String email = "";
    String id = "";
    String emp_id = "";
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permit_capture_image);

        email = getIntent().getStringExtra("email");
        id = getIntent().getStringExtra("id");
        emp_id = getIntent().getStringExtra("emp_id");

        Log.e("email",email);
        Log.e("id",id);
        Log.e("emp_id",emp_id);


        apiViewModel = new ViewModelProvider(PermitCaptureImageActivity.this).get(ApiViewModel.class);


        //shared Preferences
        accept_Data = Preferences.loadStringValue(getApplicationContext(), Preferences.accept_Data, "");
        badge_Data = Preferences.loadStringValue(getApplicationContext(), Preferences.badge_Data, "");

        img_back = findViewById(R.id.img_back);
        click_img = findViewById(R.id.click_img);

        if ((ContextCompat.checkSelfPermission(PermitCaptureImageActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        ) {
            ActivityCompat.requestPermissions(PermitCaptureImageActivity.this, new String[]{Manifest.permission.CAMERA}, RE);
        } else {
            method_citizen();
        }

        //updateCheckIn
        apiViewModel.updateworkpermitaDetails_response().observe(this, updateModel -> {
            progressDialog.dismiss();
            try {
                if (updateModel != null) {
                    Integer scode = updateModel.getResult();
                    if (scode.equals(200)) {
                        Intent intents = new Intent(getApplicationContext(), ChekInPermitStatusActivity.class);
                        intents.putExtra("status", "1");
                        startActivity(intents);
                    } else {
                        Toast.makeText(getApplicationContext(), "Try again", Toast.LENGTH_LONG).show();
                    }
                }
            } catch (Exception e) {
                progressDialog.dismiss();
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_LONG).show();

            }
        });

        img_back.setOnClickListener(this);
        click_img.setOnClickListener(this);
        img_gallery.setOnClickListener(this);
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
                    Intent intent1 = new Intent(getApplicationContext(), VisitorLoginActivity.class);
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
        img_back.setFocusable(false);
        img_back.setFocusableInTouchMode(false);
        click_img.setFocusable(false);
        click_img.setFocusableInTouchMode(false);
        img_gallery.setFocusable(false);
        img_gallery.setFocusableInTouchMode(false);
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

                img_back.setFocusable(true);
                img_back.setFocusableInTouchMode(true);
                click_img.setFocusable(true);
                click_img.setFocusableInTouchMode(true);
                img_gallery.setFocusable(true);
                img_gallery.setFocusableInTouchMode(true);
            }
        }, 500);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                AnimationSet animation = Conversions.animation();
                view.startAnimation(animation);

                finish();
                break;
            case R.id.click_img:
                AnimationSet animation1 = Conversions.animation();
                view.startAnimation(animation1);
                if (camera != null) {
                    camera.autoFocus(mAutoFocusCallback);
                }
                break;
            case R.id.img_gallery:
                AnimationSet animation2 = Conversions.animation();
                view.startAnimation(animation2);
                requestStoragePermission();
                break;
            default:
                // Default case for unhandled view IDs
                Log.d("CaptureImageActivity", "Unhandled view ID: " + view.getId());
                break;
        }
    }


    Camera.AutoFocusCallback mAutoFocusCallback = new Camera.AutoFocusCallback() {
        @Override
        public void onAutoFocus(boolean success, Camera camera) {
            camera.takePicture(null, null, mPictutureCallBack);
        }
    };


    Camera.PictureCallback mPictutureCallBack = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            if (data != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                if (bitmap != null) {
                    relative_capture.setVisibility(View.GONE);
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream);
                    String path1 = MediaStore.Images.Media.insertImage(getApplicationContext().getContentResolver(), bitmap, "IMG_" + System.currentTimeMillis(), null);
                    filepath = Uri.parse(path1);
                    img_gallery.setImageBitmap(bitmap);
                    try {
                        Bitmap thumbnail = MediaStore.Images.Media.getBitmap(
                                getContentResolver(), filepath);
                        String path = getRealPathFromURI(filepath);
                        Bitmap bitmapsa = BitmapFactory.decodeFile(path);
                        String encodedString = encodeTobase64(BitmapFactory.decodeFile(path), true);
                        filename = Conversions.datemillirandstring() + ".jpeg";

                        String Comp_id = Preferences.loadStringValue(PermitCaptureImageActivity.this, Preferences.Comp_id, "");
                        Log.e("Comp_id_",Comp_id);
                        //call in background
                        startQrUpload(
                                PermitCaptureImageActivity.this,
                                Comp_id,
                                filename,
                                encodedString,
                                email,
                                id,
                                emp_id
                        );

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                }
            }
        }
    };

    public void startQrUpload(Context context, String cid, String filename, String encodedImage,
                              String email, String id, String emp_id) {
        try {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("cid", cid);
            jsonObj.put("key", filename);
            jsonObj.put("img", encodedImage);
            JsonObject gsonObject = JsonParser.parseString(jsonObj.toString()).getAsJsonObject();
            DataManger.getDataManager().qrindex(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    String code = response.body();
                    if ("200".equals(code)) {
                        try {
                            JSONObject checkinJson = new JSONObject();
                            JSONArray picArray = new JSONArray();
                            picArray.put(filename);

                            checkinJson.put("formtype", "checkin");
                            checkinJson.put("email", email);
                            checkinJson.put("id", id);
                            checkinJson.put("emp_id", emp_id);
                            checkinJson.put("live_pic", picArray);

                            JsonObject finalJson = JsonParser.parseString(checkinJson.toString()).getAsJsonObject();

                            ApiViewModel apiViewModel = new ApiViewModel();
                            apiViewModel.updateworkpermita(context, finalJson);

                            Intent intent = new Intent(context, ChekInPermitStatusActivity.class);
                            intent.putExtra("status", "1");
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);

                        } catch (Exception e) {
                            Log.e("startQrUpload", "Check-in JSON error", e);
                        }
                    } else {
                        Log.w("startQrUpload", "qrindex failed: " + code);
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.e("startQrUpload", "qrindex failed", t);
                }
            }, context, gsonObject);

        } catch (Exception e) {
            Log.e("startQrUpload", "Exception", e);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                method_citizen();
            } else {
                Toast.makeText(getApplicationContext(), "Permisstion Denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void method_citizen() {
        relative_capture = findViewById(R.id.relative_capture);
        img_gallery = findViewById(R.id.img_gallery);
        click_img = findViewById(R.id.click_img);
        fragment_complaint_image_set = findViewById(R.id.fragment_complaint_image_set);

        int frontCameraId = -1;
        int numberOfCameras = android.hardware.Camera.getNumberOfCameras();

        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo cameraInfo = new android.hardware.Camera.CameraInfo();
            Camera.getCameraInfo(i, cameraInfo);

            if (cameraInfo.facing == android.hardware.Camera.CameraInfo.CAMERA_FACING_FRONT) {
                frontCameraId = i;
                break;
            }
        }

        if (frontCameraId != -1) {
            camera = Camera.open(frontCameraId);
            // Use the camera for your desired operations
        }

        showCamera = new ShowCamera(getApplicationContext(), camera);
        fragment_complaint_image_set.addView(showCamera);
        relative_capture.setVisibility(View.VISIBLE);
    }

    // Request storage permission to upload an image
    private void requestStoragePermission() {
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                ShowFileChooser(); // Show the file chooser when permission is granted
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                // Handle the case when the permission is denied
                // This can include showing a message to the user explaining why the permission is needed
                Log.d("Permission", "Storage permission denied. Please allow access to upload images.");
                // Optionally, you can show a Snackbar or Toast here to inform the user
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                // Show rationale to the user for why this permission is needed
                // This could involve displaying a dialog explaining the importance of the permission
                Log.d("Permission", "Rationale for storage permission should be shown.");
                // Here you can call permissionToken.continuePermissionRequest(); to show rationale, or
                // permissionToken.cancelPermissionRequest(); to cancel the request
            }
        };


    }


    //gallery
    private void ShowFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && data != null && data.getData() != null) {
            click_img.setVisibility(View.GONE);
            img_gallery.setVisibility(View.GONE);
            filepath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), filepath);
                img_gallery.setImageBitmap(bitmap);
                String path = getPath(filepath);

            } catch (Exception ex) {
            }
        } else {
            method_citizen();

        }
    }

    @SuppressLint("Range")
    private String getPath(Uri uri) {
        String path = null;
        Cursor cursor = null;

        try {
            cursor = getApplicationContext().getContentResolver().query(uri, null, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                // Get the file name
                int nameIndex = cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME);
                image_name = cursor.getString(nameIndex);

                // Get the document ID and clean it
                String document_id = cursor.getString(0);
                if (document_id != null) {
                    document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
                    cursor.close(); // Close the cursor before opening another one

                    // Query the MediaStore for the actual file path
                    cursor = getApplicationContext().getContentResolver().query(
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            null,
                            android.provider.BaseColumns._ID + " = ? ",
                            new String[]{document_id},
                            null);

                    if (cursor != null && cursor.moveToFirst()) {
                        // Use static access with MediaStore.MediaColumns for "DATA"
                        path = cursor.getString(cursor.getColumnIndexOrThrow(android.provider.MediaStore.MediaColumns.DATA));
                    }
                }
            }
        } catch (Exception e) {
            Log.e("getPath", "Error getting file path from URI", e);
        } finally {
            if (cursor != null) {
                cursor.close(); // Ensure cursor is always closed
            }
        }
        return path;
    }


    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getApplicationContext().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        //image name
        int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        image_name = cursor.getString(nameIndex);

        int idx = cursor.getColumnIndex(MediaStore.MediaColumns.DATA);
        return cursor.getString(idx);
    }

    //only kiosk checkin
    //capture image convert to base64
    public static String encodeTobase64(Bitmap image, Boolean Status) {
        int w = image.getWidth();
        int h = image.getHeight();
        Matrix mtx = new Matrix();
        mtx.postRotate(0);
        mtx.postScale(-1, 1, w / 2, h / 2);
//        Bitmap scaledBitmap = Bitmap.createBitmap(image, 0, 0, w, h, mtx, true);
//        Bitmap scaledBitmap = Bitmap.createScaledBitmap(image, image.getWidth(), image.getHeight(), true);
        Bitmap rotatedBitmap = Bitmap.createBitmap(image, 0, 0, image.getWidth(), image.getHeight(), mtx, true);
        Bitmap immagex = rotatedBitmap;


        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.JPEG, 20, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        Log.e("LOOK", imageEncoded);
        return imageEncoded;
    }


    @Override
    protected void onResume() {
        super.onResume();
        if ((ContextCompat.checkSelfPermission(PermitCaptureImageActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        ) {
            ActivityCompat.requestPermissions(PermitCaptureImageActivity.this, new String[]{Manifest.permission.CAMERA}, RE);
        } else {
            method_citizen();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
       finish();
    }

}