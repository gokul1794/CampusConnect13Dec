package com.campusconnect.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.campusconnect.R;

/**
 * Created by RK on 23-09-2015.
 */
public class Signup_2Activity extends AppCompatActivity {

    LinearLayout temporary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_2);

        temporary = (LinearLayout)findViewById(R.id.temp);

        temporary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_temp = new Intent(v.getContext(), GetProfileDetailsActivity.class);
                startActivity(intent_temp);

            }
        });


    }


}
