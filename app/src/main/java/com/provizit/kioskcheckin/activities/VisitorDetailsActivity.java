package com.provizit.kioskcheckin.activities;

import static android.view.View.GONE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.provizit.kioskcheckin.config.ConnectionReceiver;
import com.provizit.kioskcheckin.mvvm.ApiViewModel;
import com.provizit.kioskcheckin.R;
import com.provizit.kioskcheckin.services.Conversions;
import com.provizit.kioskcheckin.api.DataManger;
import com.provizit.kioskcheckin.services.GetCVisitorDetailsModel;
import com.provizit.kioskcheckin.config.Preferences;

import de.hdodenhof.circleimageview.CircleImageView;

public class VisitorDetailsActivity extends AppCompatActivity {
    private static final String TAG = "";

    //internet connection
    BroadcastReceiver broadcastReceiver;
    RelativeLayout relative_internet;
    RelativeLayout relative_ui;

    TextView purpose,time,name,email,mobile,nationality,alreadycheckedout;
    CircleImageView V_image;
    Button btn_checkin,btn_checkout;
    ApiViewModel apiViewModel;

    GetCVisitorDetailsModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visitor_details);
        Intent intent = getIntent();
        model = (GetCVisitorDetailsModel) intent.getSerializableExtra("model_key");

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String VersionName = sharedPreferences.getString("VersionName", "1.0");
        TextView txtVersion = findViewById(R.id.txtVersion);
        txtVersion.setText(VersionName);

        purpose = findViewById(R.id.purpose);
        time = findViewById(R.id.time);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        mobile = findViewById(R.id.mobile);
        nationality = findViewById(R.id.nationality);
        V_image = findViewById(R.id.V_image);


        //internet connection
        relative_internet = findViewById(R.id.relative_internet);
        relative_ui = findViewById(R.id.relative_ui);
        broadcastReceiver = new ConnectionReceiver(){
            @Override
            public void onReceive(Context context, Intent intent) {
                if (isConnecteds(context)){
                    relative_internet.setVisibility(GONE);
                    relative_ui.setVisibility(View.VISIBLE);
                }else {
                    relative_internet.setVisibility(View.VISIBLE);
                    relative_ui.setVisibility(GONE);
                }
            }
        };
        registoreNetWorkBroadcast();


        apiViewModel = new ViewModelProvider(VisitorDetailsActivity.this).get(ApiViewModel.class);

        apiViewModel.getuserslotdetails(getApplicationContext(),"642d1473cb32b84138641d74");

        apiViewModel.getResponseforslotDetails().observe(this, model -> {
            Log.e(TAG, "Getslots::"+ model);

            if(model!= null){
                Integer statuscode = model.getResult();
                Integer successcode = 200, failurecode = 401, not_verified = 404;
                if(statuscode.equals(successcode)) {
                    name.setText(model.getItems().getUserDetails().getName());
                    email.setText(model.getItems().getUserDetails().getEmail());
                    mobile.setText(model.getItems().getUserDetails().getMobile());
                    nationality.setText(model.getItems().getUserDetails().getNation());
                    purpose.setText(model.getItems().getPurpose().getName());
                    time.setText(Conversions.millitotime((model.getItems().getStart()+Conversions.timezone()) * 1000,false)
                            +" - "+Conversions.millitotime((model.getItems().getEnd()+1+Conversions.timezone()) * 1000,false));

                    if (model.getItems().getUserDetails().getPic() != null && model.getItems().getUserDetails().getPic().size() != 0) {
                        //preferences
                        String Comp_id = Preferences.loadStringValue(getApplicationContext(),Preferences.Comp_id,"");
                        Glide.with(VisitorDetailsActivity.this).load(DataManger.IMAGE_URL + "/uploads/" + Comp_id + "/" + model.getItems().getUserDetails().getPic().get(model.getItems().getUserDetails().getPic().size() - 1))
                                .into(V_image);
                    }

                }else if (statuscode.equals(failurecode)){
                    System.out.println("Getslotsfail");
                }
            }

        });

    }

    protected void registoreNetWorkBroadcast() {
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,
                new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

}