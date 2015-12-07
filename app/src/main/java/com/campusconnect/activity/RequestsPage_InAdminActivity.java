package com.campusconnect.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.campusconnect.adapter.JoinRequestsAdapterActivity;
import com.campusconnect.supportClasses.JoinRequestsinfoActivity;
import com.campusconnect.R;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by RK on 23-09-2015.
 */
public class RequestsPage_InAdminActivity extends AppCompatActivity {

    RecyclerView join_requests;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests_in_admin);

        join_requests = (RecyclerView) findViewById(R.id.rv_join_requests);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        join_requests.setLayoutManager(llm);
        join_requests.setHasFixedSize(true);
        join_requests.setItemAnimator(new DefaultItemAnimator());

        JoinRequestsAdapterActivity jr = new JoinRequestsAdapterActivity(
                createList_join_requests(2));

        join_requests.setAdapter(jr);
    }

    private List<JoinRequestsinfoActivity> createList_join_requests(int size) {

        List<JoinRequestsinfoActivity> result = new ArrayList<JoinRequestsinfoActivity>();
        for (int i = 1; i <= size; i++) {
            JoinRequestsinfoActivity ci = new JoinRequestsinfoActivity();
            result.add(ci);

        }

        return result;
    }
}
