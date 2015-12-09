package com.campusconnect.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.campusconnect.R;
import com.campusconnect.constant.AppConstants;
import com.campusconnect.utility.SharedpreferenceUtility;

/**
 * Created by rkd on 3/12/15.
 */
public class FlashActivity extends AppCompatActivity {

    SharedPreferences sharedpreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashpage);
       /* sharedpreferences = getSharedPreferences(AppConstants.SHARED_PREFS, Context.MODE_PRIVATE);

        Boolean loggedIn=sharedpreferences.getBoolean(AppConstants.LOG_IN_STATUS,false);*/

        Boolean loggedIn= SharedpreferenceUtility.getInstance(FlashActivity.this).getBoolean(AppConstants.LOG_IN_STATUS);

        if(loggedIn){
            Intent next=new Intent(FlashActivity.this,MainActivity.class);
            startActivity(next);
        }
        else{
            Intent next=new Intent(FlashActivity.this,SelectCollegeActivity.class);
            startActivity(next);
        }

    }
}
