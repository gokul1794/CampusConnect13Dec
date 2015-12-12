package com.campusconnect.activity;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.campusconnect.R;
import com.campusconnect.adapter.GroupPageAdapterActivity;
import com.campusconnect.adapter.GroupPage_infoActivity;
import com.campusconnect.bean.GroupBean;
import com.campusconnect.communicator.WebRequestTask;
import com.campusconnect.communicator.WebServiceDetails;
import com.campusconnect.constant.AppConstants;
import com.campusconnect.utility.CircularImageView;
import com.campusconnect.utility.SharedpreferenceUtility;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GroupPageActivity extends ActionBarActivity {

    RecyclerView group_page;
    String g_name, g_icon, member_count;
    Integer m_count = 0, f_count = 0;
    int follow_click_count = 0, member_click_count = 0;
    int yes_dialog_box_click_count = 0;

    TextView group_name, followers_count, members_count;
    CircularImageView group_icon;
    ToggleButton tbtn_follow, tbtn_member;
    String clubId;
    int call_web_api;

    private static final String LOG_TAG = "GroupPageActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_page);

        group_name = (TextView) findViewById(R.id.group_name);
        members_count = (TextView) findViewById(R.id.tv_members_count);
        followers_count = (TextView) findViewById(R.id.tv_followers_count);
        group_icon = (CircularImageView) findViewById(R.id.group_image);
        tbtn_follow = (ToggleButton) findViewById(R.id.tb_followers);
        tbtn_member = (ToggleButton) findViewById(R.id.tb_members);

        group_page = (RecyclerView) findViewById(R.id.recycler_group_page);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        group_page.setLayoutManager(llm);
        group_page.setHasFixedSize(true);
        group_page.setItemAnimator(new DefaultItemAnimator());
        GroupBean bean = (GroupBean) getIntent().getSerializableExtra("BEAN");
        if (bean != null) {

            String club_id = bean.getClubId();
            WebApiGetGroup(club_id);

        }
        tbtn_follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (follow_click_count % 2 == 0) {
                    f_count++;

                } else {
                    f_count--;

                }
                follow_click_count++;


            }
        });
        tbtn_member.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (follow_click_count % 2 == 0) {
                    m_count++;
                } else {
                    m_count--;
                }
                member_click_count++;
                MemberConfirmationDialog confirmDialog = new MemberConfirmationDialog(GroupPageActivity.this);
                Window window = confirmDialog.getWindow();
                window.setLayout(450, ViewGroup.LayoutParams.WRAP_CONTENT);
                confirmDialog.show();
            }
        });
    }

    private List<GroupPage_infoActivity> createList_group_page(int size) {
        List<GroupPage_infoActivity> result = new ArrayList<GroupPage_infoActivity>();
        for (int i = 1; i <= size; i++) {
            GroupPage_infoActivity ci = new GroupPage_infoActivity();
            result.add(ci);
        }
        return result;
    }

    public void WebApiGetGroup(String clubId) {
        try {
            String pid = SharedpreferenceUtility.getInstance(GroupPageActivity.this).getString(AppConstants.PERSON_PID);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("club_id", clubId);
            List<NameValuePair> param = new ArrayList<NameValuePair>();
            String url = WebServiceDetails.DEFAULT_BASE_URL + "getClub";
            new WebRequestTask(GroupPageActivity.this, param, _handler, WebRequestTask.POST, jsonObject, WebServiceDetails.PID_GET_CLUB_DETAIL,
                    true, url).execute();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private final Handler _handler = new Handler() {
        public void handleMessage(Message msg) {
            int response_code = msg.what;
            if (response_code != 0 && response_code != 204) {
                String strResponse = (String) msg.obj;
                Log.v("Response", strResponse);
                if (strResponse != null && strResponse.length() > 0) {
                    switch (response_code) {
                        case WebServiceDetails.PID_GET_CLUB_DETAIL: {
                            try {
                                JSONObject jsonObject = new JSONObject(strResponse);
                                String followcount = jsonObject.optString("followercount");
                                g_name = jsonObject.optString("name");
                                String admin = jsonObject.optString("admin");
                                clubId = jsonObject.optString("club_id");
                                String collegeName = jsonObject.optString("collegeName");
                                String abbreviation = jsonObject.optString("abbreviation");
                                member_count = jsonObject.optString("membercount");
                                String description = jsonObject.optString("description");
                                String photourl = jsonObject.optString("photoUrl");

                                //Setting value in the view
                                group_name.setText(g_name);
                                members_count.setText("" + member_count);
                                followers_count.setText("" + followcount);
                                try {
                                    if (photourl.equalsIgnoreCase("None") || photourl == null) {
                                        Picasso.with(GroupPageActivity.this).load(R.mipmap.spark_session).into(group_icon);
                                    } else {
                                        Picasso.with(GroupPageActivity.this).load(photourl).into(group_icon);
                                    }

                                } catch (Exception e) {
                                    Picasso.with(GroupPageActivity.this).load(R.mipmap.spark_session).into(group_icon);
                                }
                                GroupBean bean = new GroupBean();
                                bean.setName(g_name);
                                bean.setAdmin(admin);
                                bean.setDescription(description);
                                bean.setClubId(clubId);
                                bean.setAbb(abbreviation);
                                bean.setFollowCount(followcount);
                                bean.setMemberCount(member_count);
                                bean.setCollegeName(collegeName);

                                GroupPageAdapterActivity gp = new GroupPageAdapterActivity(
                                        createList_group_page(4), bean);
                                group_page.setAdapter(gp);


                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                        case WebServiceDetails.PID_CLUB_MEMEBER_JOIN: {

                        }
                        break;
                        default:
                            break;
                    }
                } else {
                    Toast.makeText(GroupPageActivity.this, "SERVER_ERROR", Toast.LENGTH_LONG).show();
                }
            } else if (response_code == 204) {
                if (call_web_api == 1) {
                    Toast.makeText(GroupPageActivity.this, "Your request to join group has been sent to admin for approval", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(GroupPageActivity.this, "SERVER_ERROR", Toast.LENGTH_LONG).show();
            }
        }
    };

    public class MemberConfirmationDialog extends Dialog implements
            android.view.View.OnClickListener {

        public Activity c;
        public Dialog d;
        public TextView yes, no;
        public TextView dialog_info;


        public MemberConfirmationDialog(Activity a) {
            super(a);
            // TODO Auto-generated constructor stub
            this.c = a;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.member_confirmation_dialog);
            yes = (TextView) findViewById(R.id.btn_yes);
            no = (TextView) findViewById(R.id.btn_no);
            dialog_info = (TextView) findViewById(R.id.tv_dialog_info);
            dialog_info.setText(GroupPageActivity.this.g_name);
            if (yes_dialog_box_click_count % 2 != 0) {
                dialog_info.setText("Are you sure you want to leave the group?");
            } else {
                dialog_info.setText("Become a member of " + GroupPageActivity.this.g_name);
            }
            yes.setOnClickListener(this);
            no.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_yes: {
                    yes_dialog_box_click_count++;
                    webApiGroupJoin();
                    dismiss();
                    break;
                }
                case R.id.btn_no:
                    yes_dialog_box_click_count--;
                    dismiss();
                    break;
                default:
                    break;
            }
        }
    }

    public void webApiGroupJoin() {
        try {
            String pid = SharedpreferenceUtility.getInstance(GroupPageActivity.this).getString(AppConstants.PERSON_PID);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("club_id", clubId);
            jsonObject.put("from_pid", pid);
            call_web_api = 1;
            List<NameValuePair> param = new ArrayList<NameValuePair>();
            String url = WebServiceDetails.DEFAULT_BASE_URL + "joinClub";
            new WebRequestTask(GroupPageActivity.this, param, _handler, WebRequestTask.POST, jsonObject, WebServiceDetails.PID_CLUB_MEMEBER_JOIN,
                    true, url).execute();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


}
