package com.provizit.kioskcheckin.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.animation.AnimationSet;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.provizit.kioskcheckin.activities.Meetings.MeetingRequestActivity;
import com.provizit.kioskcheckin.R;
import com.provizit.kioskcheckin.config.Preferences;
import com.provizit.kioskcheckin.services.Conversions;
import com.provizit.kioskcheckin.services.GetCVisitorDetailsModel;

public class TakeaPictureActivity extends AppCompatActivity {
    private static final String TAG = "TakeaPictureActivity";

    ImageView company_logo;
    ContentValues values;
    Uri imageUri;
    Button takesnap;

    GetCVisitorDetailsModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_takea_picture);

        Intent intent = getIntent();
        model = (GetCVisitorDetailsModel) intent.getSerializableExtra("model_key");

        //company logo
        company_logo = findViewById(R.id.company_logo);
        String c_Logo = Preferences.loadStringValue(getApplicationContext(), Preferences.company_Logo, "");
        if (c_Logo.equalsIgnoreCase("")){
        }else {
            Glide.with(TakeaPictureActivity.this).load(c_Logo)
                    .into(company_logo);
        }

        takesnap=findViewById(R.id.takesnap);
        takesnap.setOnClickListener(view -> {
            AnimationSet animation1 = Conversions.animation();
            view.startAnimation(animation1);
            dispatchTakePictureIntent();
        });
        ImageView backImage=findViewById(R.id.back_image);
        backImage.setOnClickListener(view -> {
            AnimationSet animation1 = Conversions.animation();
            view.startAnimation(animation1);

            Intent intent1 =new Intent(getApplicationContext(), MeetingRequestActivity.class);
            intent1.putExtra("model_key", model);
            startActivity(intent1);
        });


    }

    private void dispatchTakePictureIntent() {

        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(TakeaPictureActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, (dialog, item) -> {
            if (options[item].equals("Take Photo")){
                    Log.e(TAG, "onClick:"+"cam" );
                    values = new ContentValues();
                    values.put(MediaStore.Images.Media.TITLE, "New Picture");
                    values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
                    imageUri = getContentResolver().insert(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1 && Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                        intent.putExtra("android.media.action.IMAGE_CAPTURE", 1);
                    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        intent.putExtra("aandroid.media.action.IMAGE_CAPTURE",1);
                        intent.putExtra("android.media.action.IMAGE_CAPTURE", true);
                    } else {
                        intent.putExtra("android.intent.extras.CAMERA_FACING", 1);
                    }
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

                    startActivityForResult(intent, 1);

            }




            else if (options[item].equals("Choose from Gallery"))
            {

                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);

            }
            else if (options[item].equals("Cancel")) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

}