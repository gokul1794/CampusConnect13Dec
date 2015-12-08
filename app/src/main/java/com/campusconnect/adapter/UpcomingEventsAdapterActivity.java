package com.campusconnect.adapter;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.campusconnect.R;
import com.campusconnect.activity.InEventActivity;
import com.campusconnect.supportClasses.UpcomingEvents_infoActivity;
import com.campusconnect.utility.CircularImageView;

import java.util.List;

/**
 * Created by RK on 05/11/2015.
 */
public class UpcomingEventsAdapterActivity extends
        RecyclerView.Adapter<UpcomingEventsAdapterActivity.UpcomingEventsViewHolder> {

    private List<UpcomingEvents_infoActivity> UpcomingEventsList;
    int going_click_count=0;
    int share_click_count=0;

    public UpcomingEventsAdapterActivity(List<UpcomingEvents_infoActivity> UpcomingEventsList) {
        this.UpcomingEventsList = UpcomingEventsList;
    }

    @Override
    public int getItemCount() {
        return UpcomingEventsList.size();
    }


    @Override
    public void onBindViewHolder(UpcomingEventsViewHolder upcoming_eventsViewHolder, int i) {
        UpcomingEvents_infoActivity ci = UpcomingEventsList.get(i);


    }

    @Override
    public UpcomingEventsViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.activity_card_layout_my_feed, viewGroup, false);

        return new UpcomingEventsViewHolder(itemView);
    }



    public class UpcomingEventsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        CardView my_feed;
        TextView event_title, group_name, timestamp, day,date_month,time;
        ImageView event_photo,news_icon,going,share;
        CircularImageView group_icon;

        public UpcomingEventsViewHolder(View v) {
            super(v);

            Typeface r_med = Typeface.createFromAsset(v.getContext().getAssets(), "font/Roboto_Medium.ttf");
            Typeface r_reg = Typeface.createFromAsset(v.getContext().getAssets(), "font/Roboto_Regular.ttf");

            my_feed = (CardView) v.findViewById(R.id.college_feed_card);
            event_title = (TextView) v.findViewById(R.id.tv_event);
            group_name = (TextView) v.findViewById(R.id.tv_group);
            timestamp = (TextView) v.findViewById(R.id.tv_timestamp);
            event_photo = (ImageView) v.findViewById(R.id.iv_event_photo);
            news_icon = (ImageView) v.findViewById(R.id.iv_news_icon);
            going = (ImageView) v.findViewById(R.id.iv_going);
            share = (ImageView) v.findViewById(R.id.iv_share);
            day = (TextView) v.findViewById(R.id.tv_day);
            date_month = (TextView) v.findViewById(R.id.tv_date_month);
            time = (TextView) v.findViewById(R.id.tv_time);
            group_icon = (CircularImageView) v.findViewById(R.id.group_image);

            event_title.setTypeface(r_med);
            group_name.setTypeface(r_reg);
            timestamp.setTypeface(r_reg);

            my_feed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent_temp = new Intent(v.getContext(), InEventActivity.class);
                /*    posi=getPosition();
                    //Create the bundle
                    Bundle bundle = new Bundle();
                    bundle.putString("E_NAME", (String) EventTitles[posi]);
                    bundle.putString("E_TIME",(String) Time_[posi]);
                    bundle.putString("E_DATE",(String) Date_Month[posi]);
                    bundle.putString("G_NAME",(String) GroupNames[posi]);
                    bundle.putString("V_NAME",(String) Venue[posi]);
                    bundle.putString("E_DESCRIPTION",(String) Description[posi]);
                    bundle.putInt("E_PHOTO", event_photos[posi]);
                    bundle.putInt("G_PHOTO",GroupLogo[posi]);
                    bundle.putInt("FLAG_NEWS_TOP",posi);
                    bundle.putInt("POSITION_TOP",posi);
                    intent_temp.putExtras(bundle);  */
                    v.getContext().startActivity(intent_temp);
                }
            });
            going.setAlpha((float) 0.5);
            going.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (going_click_count % 2 == 0) {
                        going.setAlpha((float) 1);
                    } else {
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

        @Override
        public void onClick(View view) {

        }
    }
}

