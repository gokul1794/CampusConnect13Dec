package com.campusconnect.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.campusconnect.R;
import com.campusconnect.adapter.SelectGroupAtSignUpAdapter;
import com.campusconnect.supportClasses.GroupsJoined_infoActivity;
import com.campusconnect.supportClasses.Notification_infoActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by RK on 23-09-2015.
 */
public class Signup_4Activity extends AppCompatActivity {

    RecyclerView group_list_at_sign_up;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_4);

        group_list_at_sign_up = (RecyclerView)findViewById(R.id.rv_group_list_for_signup);

        group_list_at_sign_up.setHasFixedSize(false);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        group_list_at_sign_up.setLayoutManager(llm);
        group_list_at_sign_up.setItemAnimator(new DefaultItemAnimator());
        SelectGroupAtSignUpAdapter sg = null;
        try {
            sg = new SelectGroupAtSignUpAdapter(
                    createList_sg(3));
        } catch (IOException e) {
            e.printStackTrace();
        }
        group_list_at_sign_up.setAdapter(sg);
    }

    private List<GroupsJoined_infoActivity> createList_sg ( int size){
        List<GroupsJoined_infoActivity> result = new ArrayList<GroupsJoined_infoActivity>();
        for (int i = 1; i <= size; i++) {
            GroupsJoined_infoActivity ci = new GroupsJoined_infoActivity();
            result.add(ci);
        }

        return result;
    }
}
