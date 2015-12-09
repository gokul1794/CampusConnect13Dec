package com.campusconnect.fragment;

/**
 * Created by RK on 17-09-2015.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appspot.campus_connect_2015.clubs.model.ModelsFeed;
import com.campusconnect.R;
import com.campusconnect.adapter.CollegeFeedAdapterActivity;
import com.campusconnect.constant.AppConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hp1 on 21-01-2015.
 */
public class FragmentEvents extends Fragment {
    private static final String LOG_TAG="FragmentEvents";
    RecyclerView college_feed;
    FloatingActionButton fab;
    String collegeId;
    String mEmailAccount = "";
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.fragment_events,container,false);

/*        fab=(FloatingActionButton)v.findViewById(R.id.fab_add);*/
        college_feed = (RecyclerView) v.findViewById(R.id.rv_college_feed);
        college_feed.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(v.getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        college_feed.setLayoutManager(llm);
        college_feed.setItemAnimator(new DefaultItemAnimator());
        CollegeFeedAdapterActivity cf = new CollegeFeedAdapterActivity(
                createList_cf(7));
        college_feed.setAdapter(cf);

        SharedPreferences sharedpreferences = v.getContext().getSharedPreferences(AppConstants.SHARED_PREFS, Context.MODE_PRIVATE);
        collegeId=sharedpreferences.getString(AppConstants.COLLEGE_ID,null);
        mEmailAccount=sharedpreferences.getString(AppConstants.EMAIL_KEY,null);

       /* fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent_temp = new Intent(v.getContext(), CreatePostActivity.class);
                startActivity(intent_temp);
            }
        });*/
        return v;
    }
    private List<ModelsFeed> createList_cf(int size) {
        List<ModelsFeed> result = new ArrayList<ModelsFeed>();
        for (int i = 1; i <= size; i++) {
            ModelsFeed ci = new ModelsFeed();
            result.add(ci);

        }

        return result;
    }
}
