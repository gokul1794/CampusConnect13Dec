package com.campusconnect.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ImageButton;

import com.campusconnect.R;

/**
 * Created by RK on 05/11/2015.
 */
public class AboutGroupActivity extends ActionBarActivity {
    ImageButton close;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_group);

        close = (ImageButton) findViewById(R.id.ib_cancel);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });
    }
}
