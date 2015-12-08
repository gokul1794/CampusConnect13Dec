package com.campusconnect.adapter;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.campusconnect.R;
import com.campusconnect.activity.InEventActivity;
import com.campusconnect.supportClasses.NewsPostsByGroup_infoActivity;
import com.campusconnect.utility.CircularImageView;

import java.util.List;

/**
 * Created by RK on 06/11/2015.
 */
public class NewsPostsByGroupAdapterActivity extends
        RecyclerView.Adapter<NewsPostsByGroupAdapterActivity.NewsPostsByGroupViewHolder> {

    private List<NewsPostsByGroup_infoActivity> NewsPostsByGroupList;
    int going_click_count=0;
    int share_click_count=0;

    public NewsPostsByGroupAdapterActivity(List<NewsPostsByGroup_infoActivity> NewsPostsByGroupList) {
        this.NewsPostsByGroupList = NewsPostsByGroupList;
    }

    @Override
    public int getItemCount() {
        return NewsPostsByGroupList.size();
    }


    @Override
    public void onBindViewHolder(NewsPostsByGroupViewHolder news_posts_by_groupViewHolder, int i) {
        NewsPostsByGroup_infoActivity ci = NewsPostsByGroupList.get(i);


    }

    @Override
    public NewsPostsByGroupViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.activity_card_layout_news_posts, viewGroup, false);

        return new NewsPostsByGroupViewHolder(itemView);
    }



    public class NewsPostsByGroupViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        CardView news_feed;
        TextView event_title, group_name, timestamp;
        ImageView event_photo,news_icon,like,share;
        CircularImageView group_icon;

        public NewsPostsByGroupViewHolder(View v) {
            super(v);

            news_feed = (CardView) v.findViewById(R.id.news_feed_card);
            event_title = (TextView) v.findViewById(R.id.tv_event);
            group_name = (TextView) v.findViewById(R.id.tv_group);
            timestamp = (TextView) v.findViewById(R.id.tv_timestamp);
            event_photo = (ImageView) v.findViewById(R.id.iv_event_photo);
            news_icon = (ImageView) v.findViewById(R.id.iv_news_icon);
            like = (ImageView) v.findViewById(R.id.iv_like);
            share = (ImageView) v.findViewById(R.id.iv_share);
            group_icon = (CircularImageView) v.findViewById(R.id.group_image);

            news_feed.setOnClickListener(new View.OnClickListener() {
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
            like.setAlpha((float) 0.5);
            like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (going_click_count % 2 == 0) {
                        like.setAlpha((float) 1);
                    } else {
                        like.setAlpha((float) 0.5);
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


