package com.campusconnect.activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.campusconnect.R;
import com.campusconnect.adapter.GroupMembersByGroupAdapterActivity;
import com.campusconnect.supportClasses.GroupMembersByGroup_infoActivity;
import com.campusconnect.utility.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by RK on 05/11/2015.
 */
public class GroupMembersByGroupActivity extends ActionBarActivity {

    RecyclerView members_list;
    LinearLayout close;
    TextView members_text;
    Typeface r_med;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_members);

        r_med = Typeface.createFromAsset(getAssets(), "font/Roboto_Medium.ttf");


        close = (LinearLayout) findViewById(R.id.cross_button);
        members_text = (TextView) findViewById(R.id.tv_title);
        members_text.setTypeface(r_med);

        members_list = (RecyclerView) findViewById(R.id.rv_members_list);
        members_list.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        members_list.setLayoutManager(llm);
        members_list.setItemAnimator(new DefaultItemAnimator());
        members_list.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        GroupMembersByGroupAdapterActivity gm = new GroupMembersByGroupAdapterActivity(
                createList_gm(3));
        members_list.setAdapter(gm);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });
    }

    private List<GroupMembersByGroup_infoActivity> createList_gm(int size) {
        List<GroupMembersByGroup_infoActivity> result = new ArrayList<GroupMembersByGroup_infoActivity>();
        for (int i = 1; i <= size; i++) {
            GroupMembersByGroup_infoActivity ci = new GroupMembersByGroup_infoActivity();
            result.add(ci);
        }

        return result;
    }


}


