package com.campusconnect.fragment;

/**
 * Created by RK on 17-09-2015.
 */

import android.content.Intent;
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

import com.campusconnect.activity.CreatePostActivity;
import com.campusconnect.adapter.TopNewsAdapterActivity;
import com.campusconnect.supportClasses.OnItemClickListener;
import com.campusconnect.supportClasses.TopNews_infoActivity;
import com.campusconnect.R;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hp1 on 21-01-2015.
 */
public class FragmentTopNews extends Fragment {

    RecyclerView topnews;
    FloatingActionButton fab;
    public static int pos=0;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.fragment_top_news,container,false);

       /* fab=(FloatingActionButton)v.findViewById(R.id.fab_add);*/
        topnews = (RecyclerView) v.findViewById(R.id.rv_top_news);
        topnews.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(v.getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        topnews.setLayoutManager(llm);
        topnews.setItemAnimator(new DefaultItemAnimator());
        TopNewsAdapterActivity tn = new TopNewsAdapterActivity(
                createList_tn(4));
        topnews.setAdapter(tn);
        tn.SetOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(View v , int position) {
                pos=position;
            }
        });

/*        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent_temp = new Intent(v.getContext(), CreatePostActivity.class);
                startActivity(intent_temp);

            }
        });*/

        return v;
    }



    private List<TopNews_infoActivity> createList_tn(int size) {
        List<TopNews_infoActivity> result = new ArrayList<TopNews_infoActivity>();
        for (int i = 1; i <= size; i++) {
            TopNews_infoActivity ci = new TopNews_infoActivity();

            result.add(ci);
        }
        return result;
    }
}
