package com.campusconnect.activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.campusconnect.R;
import com.campusconnect.adapter.UpcomingEventsAdapterActivity;
import com.campusconnect.supportClasses.UpcomingEvents_infoActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by RK on 05/11/2015.
 */
public class UpcomingEventsActivity extends ActionBarActivity {

    RecyclerView upcoming_events;
    LinearLayout close;
    Typeface r_med;
    TextView upcoming_events_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming_events);

        r_med = Typeface.createFromAsset(getAssets(), "font/Roboto_Medium.ttf");

        close = (LinearLayout) findViewById(R.id.cross_button);
        upcoming_events_text = (TextView) findViewById(R.id.tv_upcoming_events);
        upcoming_events_text.setTypeface(r_med);


        upcoming_events = (RecyclerView) findViewById(R.id.rv_upcoming_events);
        upcoming_events.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        upcoming_events.setLayoutManager(llm);
        upcoming_events.setItemAnimator(new DefaultItemAnimator());
        UpcomingEventsAdapterActivity upcomingEventsAdapterActivity = new UpcomingEventsAdapterActivity(
                createList_upcomingEventsAdapterActivity(3));
        upcoming_events.setAdapter(upcomingEventsAdapterActivity);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });

    }

    private List<UpcomingEvents_infoActivity> createList_upcomingEventsAdapterActivity(int size) {
        List<UpcomingEvents_infoActivity> result = new ArrayList<UpcomingEvents_infoActivity>();
        for (int i = 1; i <= size; i++) {
            UpcomingEvents_infoActivity ci = new UpcomingEvents_infoActivity();
            result.add(ci);
        }

        return result;
    }


}

