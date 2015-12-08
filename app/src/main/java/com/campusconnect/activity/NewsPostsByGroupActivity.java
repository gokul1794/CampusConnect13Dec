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
import com.campusconnect.adapter.NewsPostsByGroupAdapterActivity;
import com.campusconnect.supportClasses.NewsPostsByGroup_infoActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by RK on 06/11/2015.
 */
public class NewsPostsByGroupActivity extends ActionBarActivity {

    RecyclerView news_posts_by_group;
    LinearLayout close;
    Typeface r_med;
    TextView news_posts_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_posts_by_group);

        r_med = Typeface.createFromAsset(getAssets(), "font/Roboto_Medium.ttf");

        close = (LinearLayout) findViewById(R.id.cross_button);
        news_posts_text = (TextView) findViewById(R.id.tv_news_posts_text);
        news_posts_text.setTypeface(r_med);

        news_posts_by_group = (RecyclerView) findViewById(R.id.rv_news_posts_by_group);
        news_posts_by_group.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        news_posts_by_group.setLayoutManager(llm);
        news_posts_by_group.setItemAnimator(new DefaultItemAnimator());
        NewsPostsByGroupAdapterActivity newsPostsByGroupAdapterActivity = new NewsPostsByGroupAdapterActivity(
                createList_newsPostsByGroupAdapterActivity(3));
        news_posts_by_group.setAdapter(newsPostsByGroupAdapterActivity);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });

    }

    private List<NewsPostsByGroup_infoActivity> createList_newsPostsByGroupAdapterActivity(int size) {
        List<NewsPostsByGroup_infoActivity> result = new ArrayList<NewsPostsByGroup_infoActivity>();
        for (int i = 1; i <= size; i++) {
            NewsPostsByGroup_infoActivity ci = new NewsPostsByGroup_infoActivity();
            result.add(ci);
        }

        return result;
    }


}


