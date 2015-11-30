package com.campusconnect.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.campusconnect.R;
import com.campusconnect.utility.CircularImageView;

/**
 * Created by RK on 22-09-2015.
 */
public class InEventActivity extends AppCompatActivity {

    int going_click_count=0;
    int share_click_count=0;
    Integer flag_news_top,pos_top,pos_cf;
    Integer flag_news_college_feed;
    ImageView event_photo,location_icon, going,share;
    TextView e_name, e_time, e_date, g_name, v_name, e_description;
    CircularImageView g_icon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_event);

        event_photo = (ImageView) findViewById(R.id.iv_event);
        location_icon = (ImageView) findViewById(R.id.iv_location);
        going = (ImageView) findViewById(R.id.heart_going);
        share = (ImageView) findViewById(R.id.iv_share);
        e_name = (TextView) findViewById(R.id.tv_event_name);
        e_time = (TextView) findViewById(R.id.tv_time);
        e_date = (TextView) findViewById(R.id.tv_date);
        g_name = (TextView) findViewById(R.id.tv_group_name);
        e_description = (TextView) findViewById(R.id.tv_event_description);
        v_name = (TextView) findViewById(R.id.tv_venue);
        g_icon = (CircularImageView) findViewById(R.id.group_icon);

        Bundle bundle = getIntent().getExtras();

//Extract the data…
        String e_Name = bundle.getString("E_NAME");
        String e_Time = bundle.getString("E_TIME");
        String e_Date = bundle.getString("E_DATE");
        String g_Name = bundle.getString("G_NAME");
        String v_Name = bundle.getString("V_NAME");
        String e_Description = bundle.getString("E_DESCRIPTION");
        Integer e_Photo = bundle.getInt("E_PHOTO");
        Integer g_Logo = bundle.getInt("G_PHOTO");
        flag_news_top = bundle.getInt("FLAG_NEWS_TOP");
        flag_news_college_feed = bundle.getInt("FLAG_NEWS_CF");
        pos_top = bundle.getInt("POSITION_TOP");
        pos_cf = bundle.getInt("POSITION_CF");

        e_name.setText(e_Name);
        e_time.setText(e_Time);
        e_date.setText(e_Date);
        g_name.setText(g_Name);
        v_name.setText(v_Name);
        e_description.setText(e_Description);
        event_photo.setImageResource(e_Photo);
        g_icon.setImageResource(g_Logo);

        if (pos_top == 1 || pos_top==3 || pos_cf==4 || pos_cf==6) {
            location_icon.setVisibility(View.INVISIBLE);
            going.setImageResource(R.drawable.selector_heart);
        }
        else{
            location_icon.setVisibility(View.VISIBLE);
            going.setImageResource(R.mipmap.going);
        }

        going.setAlpha((float) 0.5);
        going.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (going_click_count % 2 == 0) {
                    going.setAlpha((float) 1);
                } else {
            going.setImageResource(R.drawable.selector_heart);
                    going.setAlpha((float) 0.5);
                }
                going_click_count++;
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(share_click_count%2==0) {
                    share.setAlpha((float) 1);
                }
                else {
                    share.setAlpha((float) 0.5);
                }
                share_click_count++;
            }
        });


    }
}
