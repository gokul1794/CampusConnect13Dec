package com.campusconnect.activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.campusconnect.R;
import com.campusconnect.adapter.GroupMembersByGroupAdapterActivity;
import com.campusconnect.bean.GroupMemberBean;
import com.campusconnect.communicator.WebRequestTask;
import com.campusconnect.communicator.WebServiceDetails;
import com.campusconnect.constant.AppConstants;
import com.campusconnect.supportClasses.GroupMembersByGroup_infoActivity;
import com.campusconnect.utility.DividerItemDecoration;
import com.campusconnect.utility.SharedpreferenceUtility;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

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

    ArrayList<GroupMemberBean> memberList= new ArrayList<GroupMemberBean>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_members);

        String clubId = getIntent().getStringExtra("clubId");

        WebApiGetMembers(clubId);


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

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
    }

    public void WebApiGetMembers(String clubId) {
        try {
            String pid = SharedpreferenceUtility.getInstance(GroupMembersByGroupActivity.this).getString(AppConstants.PERSON_PID);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("club_id", clubId);
            List<NameValuePair> param = new ArrayList<NameValuePair>();
            String url = WebServiceDetails.DEFAULT_BASE_URL + "getClubMembers";
            new WebRequestTask(GroupMembersByGroupActivity.this, param, _handler, WebRequestTask.POST, jsonObject, WebServiceDetails.PID_GET_CLUB_MEMBER,
                    true, url).execute();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private List<GroupMembersByGroup_infoActivity> createList_gm(int size) {
        List<GroupMembersByGroup_infoActivity> result = new ArrayList<GroupMembersByGroup_infoActivity>();
        for (int i = 1; i <= size; i++) {
            GroupMembersByGroup_infoActivity ci = new GroupMembersByGroup_infoActivity();
            result.add(ci);
        }

        return result;
    }

    private final Handler _handler = new Handler() {
        public void handleMessage(Message msg) {
            int response_code = msg.what;
            if (response_code != 0 && response_code != 204) {
                String strResponse = (String) msg.obj;
                Log.v("Response", strResponse);
                if (strResponse != null && strResponse.length() > 0) {
                    switch (response_code) {
                        case WebServiceDetails.PID_GET_CLUB_MEMBER: {
                            try {

                                JSONObject jsonObject = new JSONObject(strResponse);
                                if (jsonObject.has("items")) {

                                    JSONArray array = jsonObject.getJSONArray("items");
                                    if (array.length() > 0) {
                                        for (int i = 0; i < array.length(); i++) {
                                            JSONObject innerObject = array.getJSONObject(i);
                                            GroupMemberBean bean = new GroupMemberBean();
                                            bean.setPhotoUrl(innerObject.optString("photoUrl"));
                                            bean.setName(innerObject.optString("name"));
                                            bean.setBatch(innerObject.optString("batch"));
                                            bean.setBranch(innerObject.optString("branch"));
                                            bean.setKind(innerObject.optString("kind"));
                                           memberList.add(bean);
                                        }
                                        GroupMembersByGroupAdapterActivity gm = new GroupMembersByGroupAdapterActivity(memberList
                                               , GroupMembersByGroupActivity.this  );
                                        members_list.setAdapter(gm);
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                        break;

                        default:
                            break;
                    }
                } else {
                    Toast.makeText(GroupMembersByGroupActivity.this, "SERVER_ERROR", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(GroupMembersByGroupActivity.this, "SERVER_ERROR", Toast.LENGTH_LONG).show();
            }
        }
    };

}


