package com.campusconnect.activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableStringBuilder;
import android.text.style.TypefaceSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.campusconnect.R;
import com.campusconnect.utility.CircularImageView;
import com.campusconnect.utility.CustomTypefaceSpan;

/**
 * Created by RK on 22-09-2015.
 */
public class InEventActivity extends AppCompatActivity {

    Typeface r_lig, r_reg, r_med;
    TypefaceSpan robotoRegularSpan_for_attendees;

    String attending=" attending";
    SpannableStringBuilder attendees_text;
    int going_click_count=0, no_of_attendees;
    int share_click_count=0;
    Boolean flag_news,flag_selected_share,flag_selected_attend_like;
    Boolean flag_attended_clicked=false, flag_share_clicked=false;
    ImageView event_photo,location_icon, going,share;
    TextView e_name, e_time, e_date, g_name, v_name, e_description, attendees_count;
    CircularImageView g_icon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_event);

        r_lig = Typeface.createFromAsset(getAssets(), "font/Roboto_Light.ttf");
        r_reg = Typeface.createFromAsset(getAssets(), "font/Roboto_Regular.ttf");
        r_med = Typeface.createFromAsset(getAssets(), "font/Roboto_Medium.ttf");
        robotoRegularSpan_for_attendees = new CustomTypefaceSpan("", r_reg);

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
        attendees_count = (TextView) findViewById(R.id.tv_attendees_count);


        no_of_attendees = 255; //Value has to be taken from the server
        attendees_text = new SpannableStringBuilder("+"+no_of_attendees + attending);
        attendees_text.setSpan(robotoRegularSpan_for_attendees, 0, Integer.toString(no_of_attendees).length()+1, 0);
        attendees_count.setText(attendees_text);

        e_name.setTypeface(r_med);
        e_time.setTypeface(r_reg);
        e_date.setTypeface(r_reg);
        e_description.setTypeface(r_reg);
        g_name.setTypeface(r_reg);
        v_name.setTypeface(r_reg);
        attendees_count.setTypeface(r_lig);

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
        flag_news = bundle.getBoolean("FLAG_NEWS");
        flag_selected_share = bundle.getBoolean("FLAG_SELECTED_SHARE");
        flag_selected_attend_like = bundle.getBoolean("FLAG_SELECTED_ATTEND/LIKE");

        e_name.setText(e_Name);
        e_time.setText(e_Time);
        e_date.setText(e_Date);
        g_name.setText(g_Name);
        v_name.setText(v_Name);
        e_description.setText(e_Description);
        event_photo.setImageResource(e_Photo);
        g_icon.setImageResource(g_Logo);

        if (flag_news) {
            location_icon.setVisibility(View.GONE);
            going.setImageResource(R.drawable.selector_heart);
        }
        else{
            location_icon.setVisibility(View.VISIBLE);
            going.setImageResource(R.mipmap.going);
        }

        if(flag_selected_share)
        {
            share.setAlpha((float) 1);
            flag_share_clicked = true;
        }
        else
        {
            share.setAlpha((float)0.5);
            flag_share_clicked = false;
        }
        if(flag_selected_attend_like){
            if(flag_news)
                going.setImageResource(R.mipmap.heart_selected);
            else
                going.setImageResource(R.mipmap.going_selected);
            flag_attended_clicked = true;
        }
        else{
            if(flag_news)
                going.setImageResource(R.mipmap.heart);
            else
                going.setImageResource(R.mipmap.going);
            flag_attended_clicked = false;
        }

        going.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag_attended_clicked) {
                    if(flag_news)
                        going.setImageResource(R.mipmap.heart);
                    else
                        going.setImageResource(R.mipmap.going);
                    flag_attended_clicked = false;
                } else {
                    if(flag_news)
                        going.setImageResource(R.mipmap.heart_selected);
                    else
                        going.setImageResource(R.mipmap.going_selected);
                    flag_attended_clicked = true;
                }
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag_share_clicked) {
                    share.setAlpha((float) 0.5);
                    flag_share_clicked = false;
                }
                else {
                    share.setAlpha((float) 1);
                    flag_share_clicked = true;
                }
            }
        });



    }
}
