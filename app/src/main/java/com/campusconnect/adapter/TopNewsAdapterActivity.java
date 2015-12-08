package com.campusconnect.adapter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.campusconnect.activity.InEventActivity;
import com.campusconnect.supportClasses.OnItemClickListener;
import com.campusconnect.supportClasses.TopNews_infoActivity;
import com.campusconnect.utility.CircularImageView;
import com.campusconnect.R;

import java.util.List;

/**
 * Created by RK on 20-09-2015.
 */
public class TopNewsAdapterActivity extends
        RecyclerView.Adapter<TopNewsAdapterActivity.TopNewsViewHolder> {

    private List<TopNews_infoActivity> TopNewsList;
    public static int posi=0;
    int flag_news=0;
    int going_click_count=0;
    int share_click_count=0;
    OnItemClickListener mItemClickListener;
    CharSequence EventTitles[]={"E-Cell Startup Selection Drive","New Recruits List","Football Team Tryouts","Football Team Reaches Quarters of Independence Cup"};
    CharSequence GroupNames[]={"E-Cell","E-Cell","Football Team","Football Team"};
    CharSequence Timestamp[]={"Posted 10 days ago","Posted 15 days ago","Posted 20 days ago","Posted 30 days ago"};
    private static int[] event_photos = new int[] {
            R.mipmap.cell_event,
            R.mipmap.cell_news,
            R.mipmap.football_event,
            R.mipmap.football_news
    };
    CharSequence Day[]={"MON","","MON",""};
    CharSequence Date_Month[]={"21 Sep","","14 Sep",""};
    CharSequence Time_[]={"5:30 PM","","5:30 PM",""};
    private static int[] GroupLogo = new int[] {
            R.mipmap.cell_logo,
            R.mipmap.cell_logo,
            R.mipmap.football_logo,
            R.mipmap.football_logo

    };
    CharSequence Venue[]={"MSH","","Football Ground",""};
    CharSequence Description[]={"Have an Idea? or want to Startup? Present your idea to us and you can win access to STEP resources and great mentorship.",
            "The new recruits are the following:\n14EE201 Abhijay Kumar Pandit\n14ME139 Parikshit\n14EC202 Aditya Nishtala\n14CO132 Prajwal Kailas\n14CH02 Aishwarya Sanjeev Kumar\n14CV136 Pralay S","Be a part of the football team! Come and show us what you got. Talented and enthusiastic first years are welcome.",
            "The NITK Football team won 2 games to reach the quarter finals of the independence cup. They lost their third game by a small 2-1 margin which put them out of the tournament."};

    public TopNewsAdapterActivity(List<TopNews_infoActivity> TopNewsList) {
        this.TopNewsList = TopNewsList;
    }

    @Override
    public int getItemCount() {
        return TopNewsList.size();
    }


    @Override
    public void onBindViewHolder(TopNewsViewHolder top_newsViewHolder, int i) {
        TopNews_infoActivity ci = TopNewsList.get(i);
        top_newsViewHolder.event_title.setText(EventTitles[i]);
        top_newsViewHolder.group_name.setText(GroupNames[i]);
        top_newsViewHolder.timestamp.setText(Timestamp[i]);
        top_newsViewHolder.event_photo.setImageResource(event_photos[i]);
        if(i==0)
            top_newsViewHolder.day.setText(Day[i]);
            top_newsViewHolder.date_month.setText(Date_Month[i]);
            top_newsViewHolder.time.setText(Time_[i]);
        top_newsViewHolder.news_icon.setVisibility(View.GONE);
        top_newsViewHolder.going.setImageResource(R.mipmap.going);
        if(i==1)
        {
            top_newsViewHolder.day.setVisibility(View.GONE);
            top_newsViewHolder.date_month.setVisibility(View.GONE);
            top_newsViewHolder.time.setVisibility(View.GONE);
            top_newsViewHolder.news_icon.setVisibility(View.VISIBLE);
            flag_news=1;
            top_newsViewHolder.going.setImageResource(R.drawable.selector_heart);

        }

        if(i==2) {
            top_newsViewHolder.day.setText(Day[i]);
            top_newsViewHolder.date_month.setText(Date_Month[i]);
            top_newsViewHolder.time.setText(Time_[i]);
            top_newsViewHolder.news_icon.setVisibility(View.GONE);
            top_newsViewHolder.going.setImageResource(R.mipmap.going);
        }
        if(i==3)
        {
            top_newsViewHolder.day.setVisibility(View.GONE);
            top_newsViewHolder.date_month.setVisibility(View.GONE);
            top_newsViewHolder.time.setVisibility(View.GONE);
            top_newsViewHolder.news_icon.setVisibility(View.VISIBLE);
            top_newsViewHolder.going.setImageResource(R.drawable.selector_heart);
            flag_news=1;
        }
        top_newsViewHolder.group_icon.setImageResource(GroupLogo[i]);

    }

    @Override
    public TopNewsViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.activity_card_layout_my_feed, viewGroup, false);

        return new TopNewsViewHolder(itemView);
    }

    public void SetOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }


    public class TopNewsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        CardView my_feed;
        TextView event_title, group_name, timestamp, day,date_month,time;
        ImageView event_photo,news_icon,going,share;
        CircularImageView group_icon;

        public TopNewsViewHolder(View v) {
            super(v);

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

            my_feed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent_temp = new Intent(v.getContext(), InEventActivity.class);
                    posi=getPosition();
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
                    intent_temp.putExtras(bundle);
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
        public void onClick(View v) {
            mItemClickListener.onItemClick(v, getPosition());



        }

    }
}
