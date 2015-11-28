package com.campusconnect.fragment.calender;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.campusconnect.adapter.TopNewsAdapterActivity;
import com.campusconnect.supportClasses.TopNews_infoActivity;
import com.campusconnect.R;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by RK on 08-10-2015.
 */
public class FragmentTuesday extends Fragment {

    RecyclerView topnews;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_top_news, container, false);

        topnews = (RecyclerView) v.findViewById(R.id.rv_top_news);
        topnews.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(v.getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        topnews.setLayoutManager(llm);
        topnews.setItemAnimator(new DefaultItemAnimator());
        TopNewsAdapterActivity tn = new TopNewsAdapterActivity(
                createList_tn(30));
        topnews.setAdapter(tn);

        return v;
    }
    private List<TopNews_infoActivity> createList_tn(int size) {
        List<TopNews_infoActivity> result = new ArrayList<TopNews_infoActivity>();
        for (int i = 1; i <= size; i++) {
            TopNews_infoActivity ci = new TopNews_infoActivity();
            //ci.name = TopNews_infoActivity.NAME + i;
            result.add(ci);

        }

        return result;
    }
}
