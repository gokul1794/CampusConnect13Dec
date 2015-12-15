package com.campusconnect.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.campusconnect.R;
import com.campusconnect.constant.AppConstants;
import com.campusconnect.utility.SharedpreferenceUtility;
import com.flurry.android.FlurryAgent;

/**
 * Created by rkd on 3/12/15.
 */
public class FlashActivity extends AppCompatActivity {

    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashpage);


        FlurryAgent.setLogEnabled(false);
        FlurryAgent.init(this, "N26DF2K5GN459DRKK558");
       /* sharedpreferences = getSharedPreferences(AppConstants.SHARED_PREFS, Context.MODE_PRIVATE);

        Boolean loggedIn=sharedpreferences.getBoolean(AppConstants.LOG_IN_STATUS,false);*/
       Thread thread = new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    sleep(1500);
                    Boolean loggedIn = SharedpreferenceUtility.getInstance(FlashActivity.this).getBoolean(AppConstants.LOG_IN_STATUS);
                    if (loggedIn) {
                        Intent next = new Intent(FlashActivity.this, MainActivity.class);
                        startActivity(next);
                        finish();
                    } else {
                        Intent next = new Intent(FlashActivity.this, SelectCollegeActivity.class);
                        startActivity(next);
                        finish();
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        };
        thread.start();


    }
}
