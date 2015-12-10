package com.campusconnect.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.campusconnect.R;
import com.campusconnect.adapter.GroupPageAdapterActivity;
import com.campusconnect.adapter.GroupPage_infoActivity;
import com.campusconnect.bean.GroupBean;

import java.util.ArrayList;
import java.util.List;

public class GroupPageActivity extends ActionBarActivity {

    RecyclerView group_page;

    private static final String LOG_TAG="GroupPageActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_page);

        group_page = (RecyclerView) findViewById(R.id.recycler_group_page);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        group_page.setLayoutManager(llm);
        group_page.setHasFixedSize(true);
        group_page.setItemAnimator(new DefaultItemAnimator());


        GroupBean bean = (GroupBean) getIntent().getSerializableExtra("BEAN");
        if(bean!=null){


        }

        GroupPageAdapterActivity gp = new GroupPageAdapterActivity(
                createList_group_page(6));

        group_page.setAdapter(gp);
    }


    private List<GroupPage_infoActivity> createList_group_page(int size) {

        List<GroupPage_infoActivity> result = new ArrayList<GroupPage_infoActivity>();
        for (int i = 1; i <= size; i++) {
            GroupPage_infoActivity ci = new GroupPage_infoActivity();

            result.add(ci);

        }

        return result;
    }

}
