package com.campusconnect.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appspot.campus_connect_2015.clubs.Clubs;
import com.appspot.campus_connect_2015.clubs.model.ModelsClubListResponse;
import com.appspot.campus_connect_2015.clubs.model.ModelsClubMiniForm;
import com.appspot.campus_connect_2015.clubs.model.ModelsClubRetrievalMiniForm;
import com.appspot.campus_connect_2015.clubs.model.ModelsCollegeFeed;
import com.appspot.campus_connect_2015.clubs.model.ModelsFeed;
import com.appspot.campus_connect_2015.clubs.model.ModelsFollowClubMiniForm;
import com.appspot.campus_connect_2015.clubs.model.ModelsGetInformation;
import com.campusconnect.R;
import com.campusconnect.activity.AdminPageActivity;
import com.campusconnect.activity.CreateGroupActivity;
import com.campusconnect.activity.CreatePostActivity;
import com.campusconnect.activity.GroupPageActivity;
import com.campusconnect.adapter.CollegeCampusFeedAdapter;
import com.campusconnect.adapter.CollegeMyFeedAdapter;
import com.campusconnect.bean.CampusFeedBean;
import com.campusconnect.bean.GroupBean;
import com.campusconnect.communicator.WebRequestTask;
import com.campusconnect.communicator.WebServiceDetails;
import com.campusconnect.constant.AppConstants;
import com.campusconnect.database.DatabaseHandler;
import com.campusconnect.gcm.PubSubHelper;
import com.campusconnect.slidingtab.SlidingTabLayout_home;
import com.campusconnect.utility.DividerItemDecoration;
import com.campusconnect.utility.NetworkAvailablity;
import com.campusconnect.utility.SharedpreferenceUtility;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.common.base.Strings;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


/**
 * Created by RK on 17-09-2015.
 */
public class HomeFragment extends Fragment {

    View mRootView;
    CollegeMyFeedAdapter tn;//change to myfeed
    GroupListAdapterActivity gl;
    //CollegeMyFeedAdapter cf;//change to campusfeed
    CollegeCampusFeedAdapter cf;
    RecyclerView group_list;
    RecyclerView college_feed;
    FrameLayout frame_layout;
    LinearLayout admin, add_post, settings;
    ImageButton i_admin,i_add_post,i_settings;
    ImageButton noti, profile, home, calendar, search;
    static TextView title, title_two;
    ViewPager pager;
    ViewPagerAdapter_home adapter;
    SlidingTabLayout_home tabs;
    CharSequence Titles[] = {"MyFeed", "CampusFeed", "Groups"};
    int Numboftabs = 3;
    static int indexMyfeed = 1;
    static int indexCollegeFeed = 1;
    static int checkWebApi = 0;
    DatabaseHandler db;

    private static String mEmailAccount = "";
    private static final String LOG_TAG = "HomeFragment";
    static SharedPreferences sharedPreferences;
    static public ArrayList<GroupBean> groupList = new ArrayList<GroupBean>();
    public ArrayList<CampusFeedBean> campusFeedList = new ArrayList<CampusFeedBean>();
    public ArrayList<CampusFeedBean> myFeedList = new ArrayList<>();
    public String personalCompleted = "";
    public String campusCompleted = "";
    private PubSubHelper mPubSubHelper;
    String gcm_token;

    public HomeFragment() {
    }

    private final Handler _handler = new Handler() {
        public void handleMessage(Message msg) {
            int response_code = msg.what;

            if (response_code != 0 && response_code != 204) {
                String strResponse = (String) msg.obj;
                Log.v("Response", strResponse);
                if (strResponse != null && strResponse.length() > 0) {


                    switch (response_code) {
                        case WebServiceDetails.PID_GET_PERSONAL_FEED: {
                            try {
                                JSONObject jsonObject = new JSONObject(strResponse);
                                personalCompleted = jsonObject.optString("completed");
                                if (jsonObject.has("items")) {
                                    JSONArray campusArry = jsonObject.getJSONArray("items");
                                    if (campusArry.length() > 0) {
                                        for (int i = 0; i < campusArry.length(); i++) {
                                            JSONObject innerObj = campusArry.getJSONObject(i);
                                            CampusFeedBean bean = new CampusFeedBean();

                                            String eventCreator = innerObj.optString("event_creator");
                                            String description = innerObj.optString("description");
                                            String views = innerObj.optString("views");
                                            String photo = innerObj.optString("photo");
                                            String clubid = innerObj.optString("club_id");
                                            String pid = innerObj.optString("pid");
                                            String timeStamp = innerObj.optString("timestamp");
                                            String title = innerObj.optString("title");
                                            String collegeId = innerObj.optString("collegeId");
                                            String kind = innerObj.optString("kind");

                                            bean.setEventCreator(eventCreator);
                                            bean.setDescription(description);
                                            bean.setViews(views);
                                            bean.setClubid(clubid);
                                            bean.setPid(pid);

                                            bean.setPhoto(photo);
                                            bean.setTimeStamp(timeStamp);
                                            bean.setTitle(title);
                                            bean.setCollegeId(collegeId);
                                            bean.setKind(kind);


                                            ArrayList<String> attendList = new ArrayList<>();
                                            if (innerObj.has("attendees")) {
                                                JSONArray attArray = innerObj.getJSONArray("attendees");
                                                if (attArray.length() > 0) {
                                                    for (int j = 0; j < attArray.length(); j++) {
                                                        String attendStr = attArray.getString(j);
                                                        attendList.add(attendStr);
                                                    }
                                                }
                                            }
                                            bean.setAttendees(attendList);

                                            String endDate = innerObj.optString("end_date");
                                            String startDate = innerObj.optString("start_date");
                                            String startTime = innerObj.optString("start_time");
                                            String endTime = innerObj.optString("end_time");
                                            String venue = innerObj.optString("venue");
                                            String clubphoto = innerObj.optString("");
                                            String liker = innerObj.optString("");
                                            String complete = innerObj.optString("completed");


                                            bean.setEnd_date("" + endDate);
                                            bean.setStart_date("" + startDate);
                                            bean.setStart_time("" + startTime);
                                            bean.setEnd_time("" + endTime);
                                            bean.setVenue("" + venue);


                                            bean.setClubphoto("" + clubphoto);
                                            bean.setLikers("" + liker);
                                            bean.setCompleted("" + complete);
                                            ArrayList<String> tagList = new ArrayList<>();
                                            try {

                                                if (innerObj.has("tags")) {

                                                    JSONArray tagArray = innerObj.getJSONArray("tags");
                                                    if (tagArray.length() > 0) {
                                                        for (int l = 0; l < tagArray.length(); l++) {
                                                            String tag = tagArray.getString(l);
                                                            tagList.add(tag);
                                                        }

                                                    }
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                            bean.setTag(tagList);


                                            myFeedList.add(bean);
                                        }

                                      /*  tn = new CollegeMyFeedAdapter(myFeedList
                                                , getActivity());
                                        college_feed.setAdapter(tn);*/
                                        tn.notifyDataSetChanged();
                                    }

                                } else {
                                    Toast.makeText(getActivity(), "Follow interesting groups to create your feed!", Toast.LENGTH_SHORT).show();
                                }

                            /*  for(int i =0;i<10;i++){
                                  CampusFeedBean bean = new CampusFeedBean();
                                  myFeedList.add(bean);
                              }
                                tn = new CollegeMyFeedAdapter(myFeedList,getActivity());
                                tn.notifyDataSetChanged();
                                college_feed.setAdapter(tn);*/
                                tn.notifyDataSetChanged();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                        case WebServiceDetails.PID_GET_CAMPUS_FEED: {
                            try {
                                JSONObject campusFeedObj = new JSONObject(strResponse);
                                campusCompleted = campusFeedObj.optString("completed");

                                if (campusFeedObj.has("items")) {
                                    JSONArray campusArry = campusFeedObj.optJSONArray("items");
                                    if (campusArry.length() > 0) {
                                        for (int i = 0; i < campusArry.length(); i++) {
                                            JSONObject innerObj = campusArry.optJSONObject(i);
                                            CampusFeedBean bean = new CampusFeedBean();

                                            String eventCreator = innerObj.optString("event_creator");
                                            String description = innerObj.optString("description");
                                            String views = innerObj.optString("views");
                                            String photo = innerObj.optString("photo");
                                            String clubid = innerObj.optString("club_id");
                                            String pid = innerObj.optString("pid");
                                            String timeStamp = innerObj.optString("timestamp");
                                            String title = innerObj.optString("title");
                                            String collegeId = innerObj.optString("collegeId");
                                            String kind = innerObj.optString("kind");

                                            bean.setEventCreator(eventCreator);
                                            bean.setDescription(description);
                                            bean.setViews(views);
                                            bean.setClubid(clubid);
                                            bean.setPid(pid);

                                            bean.setPhoto(photo);
                                            bean.setTimeStamp(timeStamp);
                                            bean.setTitle(title);
                                            bean.setCollegeId(collegeId);
                                            bean.setKind(kind);


                                            ArrayList<String> attendList = new ArrayList<>();
                                            if (innerObj.has("attendees")) {
                                                JSONArray attArray = innerObj.getJSONArray("attendees");
                                                if (attArray.length() > 0) {
                                                    for (int j = 0; j < attArray.length(); j++) {
                                                        String attendStr = attArray.getString(j);
                                                        attendList.add(attendStr);
                                                    }
                                                }
                                            }
                                            bean.setAttendees(attendList);

                                            String endDate = innerObj.optString("end_date");
                                            String startDate = innerObj.optString("start_date");
                                            String startTime = innerObj.optString("start_time");
                                            String endTime = innerObj.optString("end_time");
                                            String venue = innerObj.optString("venue");
                                            String clubphoto = innerObj.optString("");
                                            String liker = innerObj.optString("");
                                            String complete = innerObj.optString("completed");


                                            bean.setEnd_date("" + endDate);
                                            bean.setStart_date("" + startDate);
                                            bean.setStart_time("" + startTime);
                                            bean.setEnd_time("" + endTime);
                                            bean.setVenue("" + venue);


                                            bean.setClubphoto("" + clubphoto);
                                            bean.setLikers("" + liker);
                                            bean.setCompleted("" + complete);
                                            ArrayList<String> tagList = new ArrayList<>();
                                            try {

                                                if (innerObj.has("tags")) {

                                                    JSONArray tagArray = innerObj.getJSONArray("tags");
                                                    if (tagArray.length() > 0) {
                                                        for (int l = 0; l < tagArray.length(); l++) {
                                                            String tag = tagArray.getString(l);
                                                            tagList.add(tag);
                                                        }

                                                    }
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                            bean.setTag(tagList);
                                            //End of getting data here
                                            campusFeedList.add(bean);
                                        }
                                       /* cf = new CollegeCampusFeedAdapter(campusFeedList
                                                , getActivity());
                                        college_feed.setAdapter(cf);*/
                                        cf.notifyDataSetChanged();
                                    }
                                } else {
                                    Toast.makeText(getActivity(), "Create posts to create your campus feed!", Toast.LENGTH_SHORT).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.e("Error", "" + e);
                            }
                        }
                        break;
                        case WebServiceDetails.PID_GET_GROUPS: {
                            try {
                                groupList.clear();

                                JSONObject grpJson = new JSONObject(strResponse);
                                String kind = grpJson.optString("kind");
                                String etag = grpJson.optString("etag");
                                if (grpJson.has("list")) {
                                    JSONArray grpArray = grpJson.getJSONArray("list");
                                    if (grpArray.length() > 0) {
                                        for (int i = 0; i < grpArray.length(); i++) {

                                            JSONObject innerGrpObj = grpArray.getJSONObject(i);
                                            String description = innerGrpObj.optString("description");
                                            String admin = innerGrpObj.optString("admin");
                                            String clubId = innerGrpObj.optString("club_id");
                                            String abb = innerGrpObj.optString("abbreviation");
                                            String name = innerGrpObj.optString("name");
                                            String status = innerGrpObj.optString("status");

                                            GroupBean bean = new GroupBean();
                                            bean.setAbb(abb);
                                            bean.setName(name);
                                            bean.setAdmin(admin);
                                            bean.setClubId(clubId);
                                            bean.setFollow("0");
                                            bean.setDescription(description);

                                            groupList.add(bean);

                                            if (db.didClubExist(clubId)) {
                                            } else {
                                                db.addGroupItem(bean);
                                            }
                                        }
                                        groupList = db.getAllClubData();

                                        gl = new GroupListAdapterActivity(groupList);
                                        group_list.setAdapter(gl);

                                    }
                                } else {
                                    Toast.makeText(getActivity(), "No Group Available", Toast.LENGTH_SHORT).show();

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                        case WebServiceDetails.PID_FOLLOW_UP: {
                            try {
                                JSONObject jsonResponse = new JSONObject(strResponse);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                        case WebServiceDetails.PID_UNFOLLOW_UP: {
                            try {
                                JSONObject jsonResponse = new JSONObject(strResponse);


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                        default:
                            break;
                    }
                } else {
                    Toast.makeText(getActivity(), "SERVER_ERROR", Toast.LENGTH_LONG).show();
                }
            } else if (response_code == 204) {
                if (checkWebApi == 1) {
                    Toast.makeText(getActivity(), "No content", Toast.LENGTH_LONG).show();
                } else if (checkWebApi == 2) {
                    Toast.makeText(getActivity(), "No content", Toast.LENGTH_LONG).show();
                } else if (checkWebApi == 3) {
                    Toast.makeText(getActivity(), "no Content", Toast.LENGTH_LONG).show();
                } else if (checkWebApi == 4) {
                    Toast.makeText(getActivity(), "following group", Toast.LENGTH_LONG).show();
                } else if (checkWebApi == 5) {
                    Toast.makeText(getActivity(), "unfollowing group", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getActivity(), "SERVER_ERROR", Toast.LENGTH_LONG).show();
            }
        }
    };


    @Override
    public void onAttach(Context context) {

        super.onAttach(context);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mRootView != null) {
            ViewGroup parent = (ViewGroup) mRootView.getParent();
            if (parent != null)
                parent.removeView(mRootView);
        }
        try {
            mRootView = inflater.inflate(R.layout.activity_home, container, false);
            db = new DatabaseHandler(getActivity());
            Log.d("HomeFragment", "Entered");
            admin = (LinearLayout) mRootView.findViewById(R.id.admin);
            add_post = (LinearLayout) mRootView.findViewById(R.id.plus);
            settings = (LinearLayout) mRootView.findViewById(R.id.settings);

            i_admin = (ImageButton) mRootView.findViewById(R.id.ib_admin);
            i_add_post = (ImageButton) mRootView.findViewById(R.id.ib_create_post);
            i_settings = (ImageButton) mRootView.findViewById(R.id.ib_settings);

            pager = (ViewPager) mRootView.findViewById(R.id.pager);
            tabs = (SlidingTabLayout_home) mRootView.findViewById(R.id.tabs);
            adapter = new ViewPagerAdapter_home(getActivity().getSupportFragmentManager(), Titles, Numboftabs, getActivity());
            pager.setAdapter(adapter);
            pager.setCurrentItem(1);
            tabs.setDistributeEvenly(true);
            tabs.setViewPager(pager);


            sharedPreferences = getActivity().getSharedPreferences(AppConstants.SHARED_PREFS, Context.MODE_PRIVATE);
            //       mEmailAccount = sharedPreferences.getString(AppConstants.EMAIL_KEY, null);
            mEmailAccount = SharedpreferenceUtility.getInstance(getActivity()).getString(AppConstants.EMAIL_KEY);
            GroupBean bean = null;
            //    webApiFollow(bean);
            //    WebApiGetGroups();
            mPubSubHelper = new PubSubHelper(getActivity());
            gcm_token = sharedPreferences.getString("gcm_token", null);

            admin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent_temp = new Intent(v.getContext(), AdminPageActivity.class);
                    startActivity(intent_temp);
                }
            });
            i_admin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent_temp = new Intent(v.getContext(), AdminPageActivity.class);
                    startActivity(intent_temp);
                }
            });
            add_post.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent_temp = new Intent(v.getContext(), CreatePostActivity.class);
                    startActivity(intent_temp);

                }
            });
            i_add_post.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent_temp = new Intent(v.getContext(), CreatePostActivity.class);
                    startActivity(intent_temp);

                }
            });


        } catch (InflateException e) {
            e.printStackTrace();
        }
        return mRootView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void WebApiGetPersonalFeed(int index) {
        if (NetworkAvailablity.hasInternetConnection(getActivity())) {
            try {
                String pid = SharedpreferenceUtility.getInstance(getActivity()).getString(AppConstants.PERSON_PID);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("pid", pid);
                jsonObject.put("pageNumber", "" + index);

          /*  jsonObject.put("college_id", collegeId);
            jsonObject.put("pid", pid);*/

                // jsonObject.put("index", indexMyfeed);
                List<NameValuePair> param = new ArrayList<NameValuePair>();
                String url = WebServiceDetails.DEFAULT_BASE_URL + "myFeed";
                Log.e("Home Fragment", url);
                Log.e("request personal feed", "___________" + jsonObject);
                checkWebApi = 1;
                new WebRequestTask(getActivity(), param, _handler, WebRequestTask.POST, jsonObject, WebServiceDetails.PID_GET_PERSONAL_FEED,
                        true, url).execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getActivity(), "Network is not available.", Toast.LENGTH_SHORT).show();
        }
    }

    public void webApiCampusFeed(int index) {
        if (NetworkAvailablity.hasInternetConnection(getActivity())) {
            try {


                String collegeId = SharedpreferenceUtility.getInstance(getActivity()).getString(AppConstants.COLLEGE_ID);
                String pid = SharedpreferenceUtility.getInstance(getActivity()).getString(AppConstants.PERSON_PID);

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("collegeId", collegeId);
                /*jsonObject.put("collegeId", "" + collegeId);*/
                jsonObject.put("pageNumber", "" + indexCollegeFeed);
                jsonObject.toString();
/*
            "collegeId": "5644309118320640",
                    "pageNumber": "1"*/

/*
            jsonObject.put("collegeId", collegeId);
            // jsonObject.put("pid", pid);*/
                String url = WebServiceDetails.DEFAULT_BASE_URL + "mainFeed";
                Log.e("url", url);
                Log.e("JSOn String", jsonObject.toString());

                List<NameValuePair> param = new ArrayList<NameValuePair>();
         /* // param.add(new BasicNameValuePair("collegeId", collegeId));
          // param.add(new BasicNameValuePair("pid", pid));*/

                checkWebApi = 2;
                new WebRequestTask(getActivity(), param, _handler, WebRequestTask.POST, jsonObject, WebServiceDetails.PID_GET_CAMPUS_FEED,
                        true, url).execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getActivity(), "Network is not available.", Toast.LENGTH_SHORT).show();
        }
    }

    public void WebApiGetGroups() {
        if (NetworkAvailablity.hasInternetConnection(getActivity())) {
            try {
                String collegeId = SharedpreferenceUtility.getInstance(getActivity()).getString(AppConstants.COLLEGE_ID);
                String pid = SharedpreferenceUtility.getInstance(getActivity()).getString(AppConstants.PERSON_PID);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("college_id", "" + collegeId);
                //     jsonObject.put("pid", "" + pid);
                List<NameValuePair> param = new ArrayList<NameValuePair>();
                String url = WebServiceDetails.DEFAULT_BASE_URL + "getClubList";
                Log.e("getGroup", jsonObject.toString());
                Log.e("", url);
                checkWebApi = 3;
                new WebRequestTask(getActivity(), param, _handler, WebRequestTask.POST, jsonObject, WebServiceDetails.PID_GET_GROUPS,
                        true, url).execute();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getActivity(), "Network is not available.", Toast.LENGTH_SHORT).show();
        }
    }

    public void webApiFollow(GroupBean bean) {

        try {
            String personPid = SharedpreferenceUtility.getInstance(getActivity()).getString(AppConstants.PERSON_PID);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("club_id", "" + bean.getClubId());
            jsonObject.put("from_pid", "" + personPid);
        /*    jsonObject.put("club_id", "5197870353350656");*/
           /* jsonObject.put("from_pid", "5688424874901504");*/

            List<NameValuePair> param = new ArrayList<NameValuePair>();
            String url = WebServiceDetails.DEFAULT_BASE_URL + "followClub";
            Log.e("follow", "" + jsonObject.toString());
            Log.e("unfollow", url);
            checkWebApi = 4;
            new WebRequestTask(getActivity(), param, _handler, WebRequestTask.POST, jsonObject, WebServiceDetails.PID_FOLLOW_UP,
                    true, url).execute();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void webApiUnFollow(GroupBean bean) {
        try {
            String personPid = SharedpreferenceUtility.getInstance(getActivity()).getString(AppConstants.PERSON_PID);
            JSONObject jsonObject = new JSONObject();
          /*  jsonObject.put("club_id", bean.getClubId());*/
         /*   jsonObject.put("club_id", "5197870353350656");
            jsonObject.put("from_pid", "5688424874901504");*/
            jsonObject.put("club_id", "" + bean.getClubId());
            jsonObject.put("from_pid", "" + personPid);
            List<NameValuePair> param = new ArrayList<NameValuePair>();
            String url = WebServiceDetails.DEFAULT_BASE_URL + "unfollowclub";
            checkWebApi = 5;
            Log.e("follow", "" + jsonObject.toString());
            Log.e("unfollow", url);
            new WebRequestTask(getActivity(), param, _handler, WebRequestTask.POST, jsonObject, WebServiceDetails.PID_UNFOLLOW_UP,
                    true, url).execute();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private boolean isSignedIn() {
        if (!Strings.isNullOrEmpty(mEmailAccount)) {
            return true;
        } else {
            return false;
        }
    }

    private List<ModelsClubMiniForm> displayClubs(ModelsClubListResponse... response) {
        Log.e(LOG_TAG, response.toString());

        if (response == null || response.length < 1) {
            return null;
        } else {
            Log.d(LOG_TAG, "Displaying " + response.length + " colleges.");
            List<ModelsClubListResponse> clubList = Arrays.asList(response);
            return clubList.get(0).getList();
        }
    }

    private List<ModelsFeed> displayCampusFeed(ModelsCollegeFeed... response) {
        Log.e(LOG_TAG, "inside");

        if (response == null) {
            Log.e(LOG_TAG, "null");
            return null;
        } else {
            Log.e(LOG_TAG, "Displaying " + response.length + " colleges.");
            List<ModelsCollegeFeed> clubList = Arrays.asList(response);
            //Log.e(LOG_TAG, clubList.toString());
            Log.e(LOG_TAG, clubList.get(0).getItems().toString());
            return clubList.get(0).getItems();
        }
    }

    public class FragmentGroups extends Fragment {

        private static final String LOG_TAG = "FragmentGroups";
        RelativeLayout create_group;
        TextView create_group_text;
        private String mEmailAccount = "";
        SharedPreferences sharedPreferences;

        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

            //getActivity().getGroups();
            View v = inflater.inflate(R.layout.fragment_groups, container, false);


            group_list = (RecyclerView) v.findViewById(R.id.rv_group_list);
            create_group = (RelativeLayout) v.findViewById(R.id.create_group_group);
            create_group_text = (TextView) v.findViewById(R.id.b_create_group);

            Typeface r_reg = Typeface.createFromAsset(v.getContext().getAssets(), "font/Roboto_Regular.ttf");
            create_group_text.setTypeface(r_reg);

            group_list.setHasFixedSize(false);
            LinearLayoutManager llm = new LinearLayoutManager(v.getContext());
            llm.setOrientation(LinearLayoutManager.VERTICAL);
            group_list.setLayoutManager(llm);
            group_list.setItemAnimator(new DefaultItemAnimator());
            group_list.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
            if (gl == null) {
                try {
                    gl = new GroupListAdapterActivity(createList_gl(1));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            group_list.setAdapter(gl);

            //TODO comment to remove crash .Remove comment
            // gl.notifyDataSetChanged();

            create_group.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent_temp = new Intent(v.getContext(), CreateGroupActivity.class);
                    startActivity(intent_temp);

                }
            });
            return v;
        }

        public List<GroupBean> createList_gl(int size) {
            List<GroupBean> result = new ArrayList<GroupBean>();
            for (int i = 1; i <= size; i++) {
                GroupBean ci = new GroupBean();

                result.add(ci);
            }
            return result;
        }
    }
    public class FragmentCampusFeed extends Fragment {
        private static final String LOG_TAG = "FragmentCampusFeed";
        //  FloatingActionButton fab;
        String collegeId;
        SwipeRefreshLayout swipeRefreshLayout;
        String Tag = "FragmentCampusFeed";
        String mEmailAccount = "";


        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.fragment_events, container, false);

          /*  fab = (FloatingActionButton) v.findViewById(R.id.fab_add);*/
            college_feed = (RecyclerView) v.findViewById(R.id.rv_college_feed);
            college_feed.setHasFixedSize(false);
            swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipeRefreshLayout);
            swipeRefreshLayout.setColorScheme(new int[]{
                    android.R.color.holo_blue_bright,
                    android.R.color.holo_green_light,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_red_light});

            final LinearLayoutManager llm = new LinearLayoutManager(v.getContext());
            llm.setOrientation(LinearLayoutManager.VERTICAL);
            college_feed.setLayoutManager(llm);
            college_feed.setItemAnimator(new DefaultItemAnimator());
            if (cf == null) {
                for (int i = 0; i <= 1; i++) {
                    CampusFeedBean ci = new CampusFeedBean();
                    campusFeedList.add(ci);
                }
                cf = new CollegeCampusFeedAdapter(campusFeedList
                        , getActivity());
                college_feed.setAdapter(cf);
                campusFeedList.clear();
            }
            SharedPreferences sharedpreferences = v.getContext().getSharedPreferences(AppConstants.SHARED_PREFS, Context.MODE_PRIVATE);
            collegeId = sharedpreferences.getString(AppConstants.COLLEGE_ID, null);
            mEmailAccount = sharedpreferences.getString(AppConstants.EMAIL_KEY, null);
            /*fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent_temp = new Intent(v.getContext(), CreatePostActivity.class);
                    startActivity(intent_temp);
                }
            });*/
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    swipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(getActivity(), "Refreshing", Toast.LENGTH_LONG).show();
                    campusFeedList.clear();
                    indexCollegeFeed = 1;
                    webApiCampusFeed(indexCollegeFeed);

                }
            });

            college_feed.setOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    int limit = llm.findLastVisibleItemPosition();
                    Log.e("scroll", "" + limit);


                    if (limit == campusFeedList.size() - 1) {
                        if (campusCompleted.equalsIgnoreCase("0")) {
                            indexCollegeFeed++;
                            webApiCampusFeed(indexCollegeFeed);

                        } else if (campusCompleted.equalsIgnoreCase("1")) {
                            Log.e(Tag, "No more data avaialble");
                            Toast.makeText(getActivity(),"No more data available",Toast.LENGTH_SHORT).show();
                        } else {
                        }
                    }
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    //   Toast.makeText(getActivity(),"scrolled ",Toast.LENGTH_SHORT).show();

                }
            });


            return v;
        }


        private List<CampusFeedBean> createList_cca(int size) {
            List<CampusFeedBean> result = new ArrayList<>();
            for (int i = 1; i <= size; i++) {
                CampusFeedBean ci = new CampusFeedBean();
                result.add(ci);

            }

            return result;
        }
    }

    private List<CampusFeedBean> createList_cf(int size) {
        List<CampusFeedBean> result = new ArrayList<>();
        for (int i = 1; i <= size; i++) {
            CampusFeedBean ci = new CampusFeedBean();
            result.add(ci);
        }
        return result;
    }

    public class FragmentMyFeed extends Fragment {

        // FloatingActionButton fab;
        SwipeRefreshLayout swipeRefreshLayout;
        public int pos = 0;
        String tag = "FragmentMyFeed";

        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.fragment_top_news, container, false);

            //  fab = (FloatingActionButton) v.findViewById(R.id.fab_add);
            college_feed = (RecyclerView) v.findViewById(R.id.rv_top_news);
            swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipeRefreshLayout);
            college_feed.setHasFixedSize(false);
            final LinearLayoutManager llm = new LinearLayoutManager(v.getContext());
            llm.setOrientation(LinearLayoutManager.VERTICAL);
            college_feed.setLayoutManager(llm);
            college_feed.setItemAnimator(new DefaultItemAnimator());
            if (tn == null) {
                for (int i = 0; i <= 1; i++) {
                    CampusFeedBean ci = new CampusFeedBean();
                    myFeedList.add(ci);
                }
                tn = new CollegeMyFeedAdapter(
                        myFeedList, getActivity());
                college_feed.setAdapter(tn);
                myFeedList.clear();
            }


            college_feed.setOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    int limit = llm.findLastVisibleItemPosition();
                    Log.e("scroll", "" + limit);
                    if (limit == myFeedList.size() - 1) {
    /*                        completed = 0 , implies there is more feed to return.
                            completed = 1, implies that this is the last page, no more feed left.*/

                        if (personalCompleted.equalsIgnoreCase("0")) {
                            indexMyfeed++;
                            WebApiGetPersonalFeed(indexMyfeed);

                        } else if (personalCompleted.equalsIgnoreCase("1")) {
                            Log.e(tag, "no more data");

                        } else {


                        }


                    }
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    //   Toast.makeText(getActivity(),"scrolled ",Toast.LENGTH_SHORT).show();

                }
            });

            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    Toast.makeText(getActivity(), "refresh", Toast.LENGTH_LONG).show();
                    indexMyfeed = 1;
                    myFeedList.clear();
                    WebApiGetPersonalFeed(indexMyfeed);
                         /*   tn = new CollegeMyFeedAdapter(
                                createList_cf(10), getActivity());*/
                    swipeRefreshLayout.setRefreshing(false);
                }
            });
            return v;
        }


        private List<CampusFeedBean> createList_cf(int size) {
            List<CampusFeedBean> result = new ArrayList<CampusFeedBean>();
            for (int i = 1; i <= size; i++) {
                CampusFeedBean ci = new CampusFeedBean();
                result.add(ci);

            }
            return result;
        }
    }

    public class ViewPagerAdapter_home extends FragmentPagerAdapter {

        CharSequence Titles[]; // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapter_home is created
        int NumbOfTabs; // Store the number of tabs, this will also be passed when the ViewPagerAdapter_home is created

        private Context mContext;


        // Build a Constructor and assign the passed Values to appropriate values in the class
        public ViewPagerAdapter_home(FragmentManager fm, CharSequence mTitles[], int mNumbOfTabsumb, Context context) {
            super(fm);
            this.Titles = mTitles;
            this.NumbOfTabs = mNumbOfTabsumb;
            this.mContext = context;
        }

        //This method return the fragment for the every position in the View Pager
        @Override
        public Fragment getItem(int position) {

               /* if (position == 0) {
                    HomeFragment.FragmentLive fraglive = new HomeFragment.FragmentLive();
                    return fraglive;
                } else*/
            if (position == 0) {
                FragmentMyFeed fragtopnews = new FragmentMyFeed();
                //   getPersonalFeed();
                WebApiGetPersonalFeed(indexMyfeed);
                return fragtopnews;
            } else if (position == 1) {
                FragmentCampusFeed fragevents = new FragmentCampusFeed();
                //    getCampusFeed();
                webApiCampusFeed(indexCollegeFeed);
                return fragevents;
            } else {
                FragmentGroups fraggroups = new FragmentGroups();
                //   getGroups();
                WebApiGetGroups();
                return fraggroups;
            }
        }

        private int[] ICONS = new int[]{
                R.drawable.selector_news,
                R.drawable.selector_events,
                R.drawable.selector_group
        };

        @Override
        public CharSequence getPageTitle(int position) {
            return Titles[position];
        }


        // This method return the Number of tabs for the tabs Strip

        @Override
        public int getCount() {
            return NumbOfTabs;
        }

        public int getDrawableId(int position) {
            return ICONS[position];
        }
    }


    /*________________________Adapter____________________________*/

    public class GroupListAdapterActivity extends
            RecyclerView.Adapter<GroupListAdapterActivity.GroupListViewHolder> {

        private List<GroupBean> GroupList;
        private List<String> itemsName;
        int posi = 0;
        private int[] GroupLogo = new int[]{
                R.mipmap.cell_logo,
                R.mipmap.football_logo,
                R.mipmap.ie_logo,
                R.mipmap.roto_logo
        };
        private int[] followers_count = new int[]{2, 3, 4, 2};
        private int[] members_count = new int[]{1, 2, 1, 2};
        public String dbFollow = "1";
        public String dbUnFollow = "0";

        private HashMap<String, String> followingMap = new HashMap<>();
        private List<Boolean> followingFlag = null;

        public GroupListAdapterActivity(List<GroupBean> GroupList) throws IOException {
            this.GroupList = GroupList;
            try {
                File f = new File(getActivity().getFilesDir(), "Follows.txt");
                BufferedReader bfr = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
                String temp;
                while ((temp = bfr.readLine()) != null) {
                    String key = temp.substring(0, temp.indexOf('|'));
                    String value = temp.substring(temp.indexOf('|') + 1, temp.length());
                    followingMap.put(key, value);
                }
                bfr.close();

                followingFlag = new ArrayList<Boolean>();
                for (GroupBean groupBean : GroupList) {

                    if (followingMap.containsKey(groupBean.getClubId())) {
                        followingFlag.add(Boolean.TRUE);
                    } else {
                        followingFlag.add(Boolean.FALSE);
                    }
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }


        @Override
        public int getItemCount() {
            return GroupList.size();
        }

        public void add(int location, String item) {
            itemsName.add(location, item);
            notifyItemInserted(location);
        }

        @Override
        public void onBindViewHolder(GroupListViewHolder group_listViewHolder, int i) {
            //ModelsClubMiniForm ci = GroupList.get(i);

            GroupBean bean = GroupList.get(posi);
            group_listViewHolder.group_title.setText(GroupList.get(i).getAbb());
        /*    if (followingFlag != null) {
                if (followingFlag.get(i)) {
                    group_listViewHolder.following.setVisibility(View.VISIBLE);
                    group_listViewHolder.follow.setVisibility(View.GONE);


                }
            }*/
            String str = GroupList.get(posi).getFollow();

            if (GroupList != null && str != null) {

                if (GroupList.get(i).getFollow().equals(dbFollow)) {
                    group_listViewHolder.following.setVisibility(View.VISIBLE);
                    group_listViewHolder.follow.setVisibility(View.GONE);
                } else if (GroupList.get(i).getFollow().equals(dbUnFollow)) {

                    group_listViewHolder.following.setVisibility(View.GONE);
                    group_listViewHolder.follow.setVisibility(View.VISIBLE);
                }

            }
        }

        @Override
        public GroupListViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(
                    R.layout.activity_card_layout_group_list, viewGroup, false);

            return new GroupListViewHolder(itemView);
        }

        public void removeLineFromFile(File inFile, String lineToRemove) {

            try {

//                File inFile = new File(file);

                if (!inFile.isFile()) {
                    System.out.println("Parameter is not an existing file");
                    return;
                }

                //Construct the new file that will later be renamed to the original filename.
                File tempFile = new File(inFile.getAbsolutePath() + ".tmp");
                BufferedReader br = new BufferedReader(new FileReader(inFile));
                PrintWriter pw = new PrintWriter(new FileWriter(tempFile));

                String line = null;

                //Read from the original file and write to the new
                //unless content matches data to be removed.
                while ((line = br.readLine()) != null) {

                    if (!line.trim().equals(lineToRemove)) {

                        pw.println(line);
                        pw.flush();
                    }
                }
                pw.close();
                br.close();

                //Delete the original file
                if (!inFile.delete()) {
                    System.out.println("Could not delete file");
                    return;
                }

                //Rename the new file to the filename the original file had.
                if (!tempFile.renameTo(inFile))
                    System.out.println("Could not rename file");

            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        public void writeLineToFile(File f, String line) {
            Log.e(LOG_TAG + "345", "here");
            if (f == null) {
                Log.e("NULL", "adf");
            }
            try {
                FileOutputStream fos = getActivity().openFileOutput(f.getName(), getActivity().MODE_APPEND);
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fos);
                outputStreamWriter.write(line);
                outputStreamWriter.flush();
                outputStreamWriter.close();
                Log.e(LOG_TAG + "345", "made it here");
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(LOG_TAG + "345", "didnt come here");
                Log.e("EXcpe", e.getMessage());
            }
            Log.e(LOG_TAG + "345", "here now");

        }

        public void printFileContents(File f) {
            try {
                String line;
                BufferedReader bfr = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
                while ((line = bfr.readLine()) != null) {
                    Log.e(LOG_TAG + "123", line);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        public class GroupListViewHolder extends RecyclerView.ViewHolder {

            CardView group_list;
            TextView follow, following, group_title;


            public GroupListViewHolder(View v) {
                super(v);

                group_list = (CardView) v.findViewById(R.id.group_list_card);
                group_title = (TextView) v.findViewById(R.id.tv_group_name);
                follow = (TextView) v.findViewById(R.id.tv_follow);
                following = (TextView) v.findViewById(R.id.tv_following);

                group_list.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent_temp = new Intent(v.getContext(), GroupPageActivity.class);
                        posi = getPosition();


                        GroupBean bean = GroupList.get(posi);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("BEAN", bean);
                        intent_temp.putExtras(bundle);

                        v.getContext().startActivity(intent_temp);

                    }
                });

                follow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            posi = getPosition();
                            follow.setVisibility(View.GONE);
                            following.setVisibility(View.VISIBLE);
                            followingMap.put(GroupList.get(posi).getClubId(), GroupList.get(posi).getName());
                            webApiFollow(GroupList.get(posi));

                            String clubId = GroupList.get(posi).getClubId();
                            // updating value in the database;
                            int i = db.updateFollow(clubId, dbFollow);
                            //  Toast.makeText(getActivity(), "" + i, Toast.LENGTH_SHORT).show();
                            GroupList.clear();
                            GroupList = db.getAllClubData();
                            gl.notifyDataSetChanged();


                            followingFlag.set(posi, Boolean.TRUE);
                            File f = new File(getActivity().getFilesDir(), "Follows.txt");
                            writeLineToFile(f, GroupList.get(posi).getClubId() + "|" + GroupList.get(posi).getName() + "\n");
                            printFileContents(f);
                            Log.e("check", GroupList.get(posi).getName() + posi);
                            mPubSubHelper.subscribeTopic("245400873255", gcm_token, GroupList.get(posi).getClubId(), null);
                            //followGroup(GroupList.get(posi));

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                following.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            posi = getPosition();
                            follow.setVisibility(View.VISIBLE);
                            following.setVisibility(View.GONE);
                            webApiUnFollow(GroupList.get(posi));
                            String clubId = GroupList.get(posi).getClubId();
                            // updating value in the database;
                            int i = db.updateFollow(clubId, dbUnFollow);
                            GroupList.clear();
                            GroupList = db.getAllClubData();
                            gl.notifyDataSetChanged();


                            //  Toast.makeText(getActivity(), "" + i, Toast.LENGTH_SHORT).show();
                            followingFlag.set(posi, Boolean.FALSE);
                            followingMap.remove(GroupList.get(posi).getClubId());


                            File f = new File(getActivity().getFilesDir(), "Follows.txt");
                            removeLineFromFile(f, GroupList.get(posi).getClubId() + "|" + GroupList.get(posi).getName());
                            printFileContents(f);
                            Log.e("check", GroupList.get(posi).getName() + posi);
                            //  unFollowGroup(GroupList.get(posi));

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

            }

        }
    }

}





