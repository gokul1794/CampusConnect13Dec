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
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
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
import com.campusconnect.adapter.CollegeFeedAdapterActivity;
import com.campusconnect.bean.CampusFeedBean;
import com.campusconnect.bean.GroupBean;
import com.campusconnect.communicator.WebRequestTask;
import com.campusconnect.communicator.WebServiceDetails;
import com.campusconnect.constant.AppConstants;
import com.campusconnect.slidingtab.SlidingTabLayout_home;
import com.campusconnect.supportClasses.Live_infoActivity;
import com.campusconnect.utility.DividerItemDecoration;
import com.campusconnect.utility.NetworkAvailablity;
import com.campusconnect.utility.SharedpreferenceUtility;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.common.base.Strings;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

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
    CollegeFeedAdapterActivity tn;//change to myfeed
    GroupListAdapterActivity gl;
    CollegeFeedAdapterActivity cf;//change to campusfeed
    RecyclerView group_list;
    RecyclerView college_feed;
    RecyclerView topnews;
    FrameLayout frame_layout;
    LinearLayout admin, add_post, settings;
    ImageButton i_admin,i_add_post,i_settings;
    ImageButton noti, profile, home, calendar, search;
    ViewPager pager;
    ViewPagerAdapter_home adapter;
    SlidingTabLayout_home tabs;
    CharSequence Titles[] = {"MyFeed", "CampusFeed", "Groups"};
    int Numboftabs = 3;

    private static String mEmailAccount = "";
    private static final String LOG_TAG = "HomeFragment";
    static SharedPreferences sharedPreferences;
    public ArrayList<GroupBean> groupList = new ArrayList<GroupBean>();
    public ArrayList<CampusFeedBean> campusFeedList = new ArrayList<CampusFeedBean>();

    public HomeFragment() {

    }

    private final Handler _handler = new Handler() {
        public void handleMessage(Message msg) {
            int response_code = msg.what;
            if (response_code != 0) {
                String strResponse = (String) msg.obj;
                Log.v("Response", strResponse);
                if (strResponse != null && strResponse.length() > 0) {
                    switch (response_code) {
                        case WebServiceDetails.PID_GET_PERSONAL_FEED: {
                            try {
                                JSONObject jsonResponse = new JSONObject(strResponse);


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                        case WebServiceDetails.PID_GET_CAMPUS_FEED: {
                            try {
                                campusFeedList.clear();
                                JSONObject campusFeedObj = new JSONObject(strResponse);
                                if (campusFeedObj.has("items")) {
                                    JSONArray campusArry = campusFeedObj.getJSONArray("items");
                                    for (int i = 0; i < campusArry.length(); i++) {
                                        JSONObject innerObj = campusArry.getJSONObject(i);
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

                                        CampusFeedBean bean = new CampusFeedBean();
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
                                        campusFeedList.add(bean);
                                    }
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                        case WebServiceDetails.PID_GET_GROUPS: {
                            try {
                                groupList.clear();
                                JSONObject grpJson = new JSONObject(strResponse);
                                if (grpJson.has("list")) {
                                    JSONArray grpArray = grpJson.getJSONArray("list");
                                    for (int i = 0; i < grpArray.length(); i++) {

                                        JSONObject innerGrpObj = grpArray.getJSONObject(i);
                                        String description = innerGrpObj.optString("description");
                                        String admin = innerGrpObj.optString("admin");
                                        String clubId = innerGrpObj.optString("club_id");
                                        String abb = innerGrpObj.optString("abbreviation");
                                        String name = innerGrpObj.optString("name");

                                        GroupBean bean = new GroupBean();
                                        bean.setAbb(abb);
                                        bean.setName(name);
                                        bean.setAdmin(admin);
                                        bean.setClubId(clubId);
                                        bean.setDescription(description);
                                        groupList.add(bean);
                                    }

                                }


                            } catch (JSONException e) {
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
            } else {
                Toast.makeText(getActivity(), "SERVER_ERROR", Toast.LENGTH_LONG).show();
            }
        }
    };


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
            webApiFollow(bean);
            //    WebApiGetGroups();

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

    public void getPersonalFeed() {
        if (!isSignedIn()) {
            Toast.makeText(getActivity(), "You must sign in for this action.", Toast.LENGTH_LONG).show();
            return;
        }
        //TODO network check
        if (NetworkAvailablity.hasInternetConnection(getActivity())) {

            // WEbApiGetPersonalFeed();
            AsyncTask<Void, Void, ModelsCollegeFeed> getPersonalFeed =
                    new AsyncTask<Void, Void, ModelsCollegeFeed>() {
                        @Override
                        protected ModelsCollegeFeed doInBackground(Void... unused) {
                            if (!isSignedIn()) {
                                return null;
                            }
                            ;

                            if (!AppConstants.checkGooglePlayServicesAvailable(getActivity())) {
                                return null;
                            }

                            // Create a Google credential since this is an authenticated request to the API.
                            GoogleAccountCredential credential = GoogleAccountCredential.usingAudience(
                                    getActivity(), AppConstants.AUDIENCE);
                            credential.setSelectedAccountName(mEmailAccount);

                            // Retrieve service handle using credential since this is an authenticated call.
                            try {
                                //TODO network check
                                if (NetworkAvailablity.hasInternetConnection(getActivity())) {
                                    Clubs apiServiceHandle = AppConstants.getApiServiceHandle(credential);

                                    ModelsGetInformation modelsGetInformation = new ModelsGetInformation();

                                    String collegeId = sharedPreferences.getString(AppConstants.COLLEGE_ID, null);
                                    String person_pid = sharedPreferences.getString(AppConstants.PERSON_PID, null);

                                    modelsGetInformation.setCollegeId(sharedPreferences.getString(AppConstants.COLLEGE_ID, "null"));
                                    modelsGetInformation.setPid(sharedPreferences.getString(AppConstants.PERSON_PID, "null"));

                                    Clubs.PersonalFeed getFeed = apiServiceHandle.personalFeed(modelsGetInformation);
                                    ModelsCollegeFeed pf = getFeed.execute();
                                    Log.e(LOG_TAG, "getPersonalFeed" + "SUCCESS");
                                    Log.e(LOG_TAG, pf.toPrettyString());
                                    return pf;
                                } else {

                                    Toast.makeText(getActivity(), "Network is not available.", Toast.LENGTH_SHORT).show();
                                }


                            } catch (IOException e) {
                                Log.e(LOG_TAG, "getPersonalFeed Exception during API call", e);
                            }
                            return null;
                        }

                        @Override
                        protected void onPostExecute(ModelsCollegeFeed cList) {
                            if (cList != null) {
                                try {
                                    Log.e(LOG_TAG, cList.toPrettyString());
                                    tn = new CollegeFeedAdapterActivity(displayCampusFeed(cList));
                                    tn.notifyDataSetChanged();
                                    topnews.setAdapter(tn);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                //   Log.e(LOG_TAG, "No clubs were returned by the API.");
                            }
                        }
                    };
            getPersonalFeed.execute((Void) null);


        } else {
            Toast.makeText(getActivity(), "Network is not available.", Toast.LENGTH_SHORT).show();
        }
    }

    public void WebApiGetPersonalFeed() {

        try {

            String collegeId = SharedpreferenceUtility.getInstance(getActivity()).getString(AppConstants.COLLEGE_ID);
            String pid = SharedpreferenceUtility.getInstance(getActivity()).getString(AppConstants.PERSON_PID);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("college_id", collegeId);
            jsonObject.put("pid", pid);
            List<NameValuePair> param = new ArrayList<NameValuePair>();
            String url = WebServiceDetails.DEFAULT_BASE_URL + "myFeed";
            Log.e("Home Fragment", url);
            new WebRequestTask(getActivity(), param, _handler, WebRequestTask.POST, jsonObject, WebServiceDetails.PID_GET_PERSONAL_FEED,
                    true, url).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void webApiCampusFeed() {
        try {

            String collegeId = SharedpreferenceUtility.getInstance(getActivity()).getString(AppConstants.COLLEGE_ID);
            String pid = SharedpreferenceUtility.getInstance(getActivity()).getString(AppConstants.PERSON_PID);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("collegeId", collegeId);
            // jsonObject.put("pid", pid);
            jsonObject.toString();
            Log.e("JSOn String", jsonObject.toString());

            List<NameValuePair> param = new ArrayList<NameValuePair>();
         /* // param.add(new BasicNameValuePair("collegeId", collegeId));
          // param.add(new BasicNameValuePair("pid", pid));*/
            String url = WebServiceDetails.DEFAULT_BASE_URL + "mainFeed";

            new WebRequestTask(getActivity(), param, _handler, WebRequestTask.POST, jsonObject, WebServiceDetails.PID_GET_CAMPUS_FEED,
                    true, url).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void WebApiGetGroups() {
        try {
            String collegeId = SharedpreferenceUtility.getInstance(getActivity()).getString(AppConstants.COLLEGE_ID);
            String pid = SharedpreferenceUtility.getInstance(getActivity()).getString(AppConstants.PERSON_PID);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("college_id", collegeId);
            jsonObject.put("pid", pid);
            List<NameValuePair> param = new ArrayList<NameValuePair>();
            String url = WebServiceDetails.DEFAULT_BASE_URL + "getClubList";
            new WebRequestTask(getActivity(), param, _handler, WebRequestTask.POST, jsonObject, WebServiceDetails.PID_GET_GROUPS,
                    true, url).execute();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void webApiFollow(GroupBean bean) {

        try {
            String personPid = SharedpreferenceUtility.getInstance(getActivity()).getString(AppConstants.PERSON_PID);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("club_id", "5109799364591616");
            jsonObject.put("from_pid", personPid);


            List<NameValuePair> param = new ArrayList<NameValuePair>();
            String url = WebServiceDetails.DEFAULT_BASE_URL + "followClub";
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
            jsonObject.put("club_id", bean.getClubId());
            jsonObject.put("from_pid", personPid);

            List<NameValuePair> param = new ArrayList<NameValuePair>();
            String url = WebServiceDetails.DEFAULT_BASE_URL + "unfollowclub";
            new WebRequestTask(getActivity(), param, _handler, WebRequestTask.POST, jsonObject, WebServiceDetails.PID_UNFOLLOW_UP,
                    true, url).execute();

        } catch (Exception e) {
            e.printStackTrace();

        }

    }


    public void getCampusFeed() {
        if (!isSignedIn()) {
            Toast.makeText(getActivity(), "You must sign in for this action.", Toast.LENGTH_LONG).show();
            return;
        }
        //TODO network Check

        if (NetworkAvailablity.hasInternetConnection(getActivity())) {

            AsyncTask<Void, Void, ModelsCollegeFeed> getCampusFeed =
                    new AsyncTask<Void, Void, ModelsCollegeFeed>() {
                        @Override
                        protected ModelsCollegeFeed doInBackground(Void... unused) {
                            if (!isSignedIn()) {
                                return null;
                            }
                            ;

                            if (!AppConstants.checkGooglePlayServicesAvailable(getActivity())) {
                                return null;
                            }
                            try {
                                // Create a Google credential since this is an authenticated request to the API.
                                GoogleAccountCredential credential = GoogleAccountCredential.usingAudience(
                                        getActivity(), AppConstants.AUDIENCE);
                                credential.setSelectedAccountName(mEmailAccount);

                                // Retrieve service handle using credential since this is an authenticated call.

                                Clubs apiServiceHandle = AppConstants.getApiServiceHandle(credential);
                                ModelsGetInformation modelsGetInformation = new ModelsGetInformation();
                                String collegeId = sharedPreferences.getString(AppConstants.COLLEGE_ID, null);
                                modelsGetInformation.setCollegeId(sharedPreferences.getString(AppConstants.COLLEGE_ID, "null"));
                                Clubs.CollegeFeed getCollegeFeed = apiServiceHandle.collegeFeed(modelsGetInformation);
                                ModelsCollegeFeed cf = getCollegeFeed.execute();

                                Log.e(LOG_TAG, "SUCCESS");
                                Log.e(LOG_TAG, cf.toPrettyString());
                                return cf;
                            } catch (IOException e) {
                                Log.e(LOG_TAG, "getCampusFeed+Exception during API call", e);
                            }
                            return null;
                        }

                        @Override
                        protected void onPostExecute(ModelsCollegeFeed cList) {
                            if (cList != null) {
                                   try {
                                    Log.e(LOG_TAG, cList.toPrettyString());
                                    cf = new CollegeFeedAdapterActivity(displayCampusFeed(cList));
                                    cf.notifyDataSetChanged();
                                    college_feed.setAdapter(cf);
                                    //displayCampusFeed(cList);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                //Log.e(LOG_TAG, "No clubs were returned by the API.");
                            }
                        }
                    };
            getCampusFeed.execute((Void) null);
        } else {
            Toast.makeText(getActivity(), "Network is not available.", Toast.LENGTH_SHORT).show();

        }
    }


    public void getGroups() {
        if (!isSignedIn()) {
            Toast.makeText(getActivity(), "You must sign in for this action.", Toast.LENGTH_LONG).show();
            return;
        }
        //TODO network Check
        if (NetworkAvailablity.hasInternetConnection(getActivity())) {
            AsyncTask<Void, Void, ModelsClubListResponse> getClubsAndPopulate =
                    new AsyncTask<Void, Void, ModelsClubListResponse>() {
                        @Override
                        protected ModelsClubListResponse doInBackground(Void... unused) {
                            if (!isSignedIn()) {
                                return null;
                            }
                            ;

                            if (!AppConstants.checkGooglePlayServicesAvailable(getActivity())) {
                                return null;
                            }

                            // Create a Google credential since this is an authenticated request to the API.
                            GoogleAccountCredential credential = GoogleAccountCredential.usingAudience(
                                    getActivity(), AppConstants.AUDIENCE);
                            credential.setSelectedAccountName(mEmailAccount);

                            // Retrieve service handle using credential since this is an authenticated call.
                            Clubs apiServiceHandle = AppConstants.getApiServiceHandle(credential);

                            try {
                                ModelsClubRetrievalMiniForm clubRetrievalMiniForm = new ModelsClubRetrievalMiniForm();
                                clubRetrievalMiniForm.setCollegeId(sharedPreferences.getString(AppConstants.COLLEGE_ID, "null"));
                                Clubs.GetClubList gcl = apiServiceHandle.getClubList(clubRetrievalMiniForm);
                                ModelsClubListResponse clubListResponse = gcl.execute();
                                Log.e(LOG_TAG, "SUCCESS");
                                Log.e(LOG_TAG, clubListResponse.toPrettyString());
                                return clubListResponse;
                            } catch (IOException e) {
                                Log.e(LOG_TAG, "getGroups+Exception during API call", e);
                            }
                            return null;
                        }

                        @Override
                        protected void onPostExecute(ModelsClubListResponse cList) {
                            if (cList != null) {
                                try {
                                    Log.e(LOG_TAG, cList.toPrettyString());
                                    gl = new GroupListAdapterActivity(displayClubs(cList));
                                    gl.notifyDataSetChanged();
                                    group_list.setAdapter(gl);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                //Log.e(LOG_TAG, "No clubs were returned by the API.");
                            }
                        }
                    };

            getClubsAndPopulate.execute((Void) null);
        } else {
            Toast.makeText(getActivity(), "Network is not available.", Toast.LENGTH_SHORT).show();

        }
    }


    public void followGroup(final ModelsClubMiniForm modelsClubMiniForm) {
        if (!isSignedIn()) {
            Toast.makeText(getActivity(), "You must sign in for this action.", Toast.LENGTH_LONG).show();
            return;
        }
        //TODO network Check
        if (NetworkAvailablity.hasInternetConnection(getActivity())) {
            AsyncTask<Void, Void, Void> followGroup =
                    new AsyncTask<Void, Void, Void>() {

                        @Override
                        protected Void doInBackground(Void... unused) {
                            if (!isSignedIn()) {
                                return null;
                            }
                            ;

                            if (!AppConstants.checkGooglePlayServicesAvailable(getActivity())) {
                                return null;
                            }

                            // Create a Google credential since this is an authenticated request to the API.
                            GoogleAccountCredential credential = GoogleAccountCredential.usingAudience(
                                    getActivity(), AppConstants.AUDIENCE);
                            credential.setSelectedAccountName(mEmailAccount);

                            // Retrieve service handle using credential since this is an authenticated call.
                            Clubs apiServiceHandle = AppConstants.getApiServiceHandle(credential);
                            //apiServiceHandle.followClub()
                            try {
                                ModelsFollowClubMiniForm modelsFollowClubMiniForm = new ModelsFollowClubMiniForm();
                                modelsFollowClubMiniForm.setClubId(modelsClubMiniForm.getClubId());
                                modelsFollowClubMiniForm.setFromPid(sharedPreferences.getString(AppConstants.PERSON_PID, null));
                                Log.e(LOG_TAG + "as", modelsFollowClubMiniForm.toPrettyString());
                                Clubs.FollowClub followClub = apiServiceHandle.followClub(modelsFollowClubMiniForm);
                                Void followClubResponse = followClub.execute();
                                Log.e(LOG_TAG, "SUCCESS followed");
//                            Log.e(LOG_TAG, followClubResponse.toString());
                                return followClubResponse;
                            } catch (IOException e) {
                                Log.e(LOG_TAG, "followGroup Exception during API call", e);
                            }
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void cList) {
                            if (cList != null) {
                            } else {
                                //  Log.e(LOG_TAG, "No clubs were returned by the API.");
                            }
                        }
                    };

            followGroup.execute((Void) null);
        } else {
            Toast.makeText(getActivity(), "Network is not available.", Toast.LENGTH_SHORT).show();

        }

    }

    public void unFollowGroup(final ModelsClubMiniForm modelsClubMiniForm) {
        if (!isSignedIn()) {
            Toast.makeText(getActivity(), "You must sign in for this action.", Toast.LENGTH_LONG).show();
            return;
        }
        //TODO network Check
        if (NetworkAvailablity.hasInternetConnection(getActivity())) {
            AsyncTask<Void, Void, Void> unFollowGroup =
                    new AsyncTask<Void, Void, Void>() {

                        @Override
                        protected Void doInBackground(Void... unused) {
                            if (!isSignedIn()) {
                                return null;
                            }
                            ;

                            if (!AppConstants.checkGooglePlayServicesAvailable(getActivity())) {
                                return null;
                            }
                            // Create a Google credential since this is an authenticated request to the API.
                            GoogleAccountCredential credential = GoogleAccountCredential.usingAudience(
                                    getActivity(), AppConstants.AUDIENCE);
                            credential.setSelectedAccountName(mEmailAccount);

                            // Retrieve service handle using credential since this is an authenticated call.
                            Clubs apiServiceHandle = AppConstants.getApiServiceHandle(credential);
                            //apiServiceHandle.followClub()
                            try {
                                ModelsFollowClubMiniForm modelsFollowClubMiniForm = new ModelsFollowClubMiniForm();
                                modelsFollowClubMiniForm.setClubId(modelsClubMiniForm.getClubId());
                                modelsFollowClubMiniForm.setFromPid(sharedPreferences.getString(AppConstants.PERSON_PID, null));

                                Clubs.UnfClub unfollowClub = apiServiceHandle.unfClub(modelsFollowClubMiniForm);
                                Void unfollowClubResponse = unfollowClub.execute();
                                Log.e(LOG_TAG, "SUCCESS unfollowed");
                                //Log.e(LOG_TAG, unfollowClubResponse.toString());
                                return unfollowClubResponse;
                            } catch (IOException e) {
                                Log.e(LOG_TAG, "unFollowGroup Exception during API call", e);
                            }
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void cList) {
                            if (cList != null) {
                            } else {
                                // Log.e(LOG_TAG, "No clubs were returned by the API.");
                            }
                        }
                    };

            unFollowGroup.execute((Void) null);
        } else {
            Toast.makeText(getActivity(), "Network is not available.", Toast.LENGTH_SHORT).show();
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

    class FragmentGroups extends Fragment {

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

            //group_list.setHasFixedSize(true);
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

        public List<ModelsClubMiniForm> createList_gl(int size) {
            List<ModelsClubMiniForm> result = new ArrayList<ModelsClubMiniForm>();
            for (int i = 1; i <= size; i++) {
                ModelsClubMiniForm ci = new ModelsClubMiniForm();

                result.add(ci);
            }
            return result;
        }
    }


    public class FragmentCampusFeed extends Fragment {
        private static final String LOG_TAG = "FragmentCampusFeed";
        String collegeId;
        String mEmailAccount = "";

        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.fragment_events, container, false);

            college_feed = (RecyclerView) v.findViewById(R.id.rv_college_feed);
            college_feed.setHasFixedSize(false);
            LinearLayoutManager llm = new LinearLayoutManager(v.getContext());
            llm.setOrientation(LinearLayoutManager.VERTICAL);
            college_feed.setLayoutManager(llm);
            college_feed.setItemAnimator(new DefaultItemAnimator());
            if (cf == null) {
                cf = new CollegeFeedAdapterActivity(
                        createList_cf(7));
            }
            college_feed.setAdapter(cf);

            SharedPreferences sharedpreferences = v.getContext().getSharedPreferences(AppConstants.SHARED_PREFS, Context.MODE_PRIVATE);
            collegeId = sharedpreferences.getString(AppConstants.COLLEGE_ID, null);
            mEmailAccount = sharedpreferences.getString(AppConstants.EMAIL_KEY, null);


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


    public class FragmentMyFeed extends Fragment {

        public int pos = 0;

        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.fragment_top_news, container, false);

            topnews = (RecyclerView) v.findViewById(R.id.rv_top_news);
            topnews.setHasFixedSize(true);
            LinearLayoutManager llm = new LinearLayoutManager(v.getContext());
            llm.setOrientation(LinearLayoutManager.VERTICAL);
            topnews.setLayoutManager(llm);
            topnews.setItemAnimator(new DefaultItemAnimator());
            if (tn == null) {
                tn = new CollegeFeedAdapterActivity(
                        createList_cf(4));
            }
            topnews.setAdapter(tn);


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

    public class FragmentLive extends Fragment {

        RecyclerView live;

        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.fragment_live_to_be_deleted, container, false);

            return v;
        }

        private List<Live_infoActivity> createList_l(int size) {
            List<Live_infoActivity> result = new ArrayList<Live_infoActivity>();
            for (int i = 1; i <= size; i++) {
                Live_infoActivity ci = new Live_infoActivity();

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

            if (position == 0) {
                HomeFragment.FragmentMyFeed fragtopnews = new FragmentMyFeed();
                 //   getPersonalFeed();
                 WebApiGetPersonalFeed();

                return fragtopnews;
            } else if (position == 1) {
                HomeFragment.FragmentCampusFeed fragevents = new FragmentCampusFeed();
                   //    getCampusFeed();
                 webApiCampusFeed();
                return fragevents;
            } else   {
                HomeFragment.FragmentGroups fraggroups = new HomeFragment.FragmentGroups();
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

    public class GroupListAdapterActivity extends
            RecyclerView.Adapter<GroupListAdapterActivity.GroupListViewHolder> {

        private List<ModelsClubMiniForm> GroupList;
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

        private HashMap<String, String> followingMap = new HashMap<>();
        private List<Boolean> followingFlag = null;

        public GroupListAdapterActivity(List<ModelsClubMiniForm> GroupList) throws IOException {
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
                for (ModelsClubMiniForm modelsClubMiniForm : GroupList) {

                    if (followingMap.containsKey(modelsClubMiniForm.getClubId())) {
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
            group_listViewHolder.group_title.setText(GroupList.get(i).getAbbreviation());
            if (followingFlag != null) {
                if (followingFlag.get(i)) {
                    group_listViewHolder.following.setVisibility(View.VISIBLE);
                    group_listViewHolder.follow.setVisibility(View.GONE);
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

                        Typeface r_reg = Typeface.createFromAsset(v.getContext().getAssets(), "font/Roboto_Regular.ttf");
                        group_title.setTypeface(r_reg);
                        follow.setTypeface(r_reg);
                        following.setTypeface(r_reg);

                        Intent intent_temp = new Intent(v.getContext(), GroupPageActivity.class);
                        posi = getPosition();

                        Bundle bundle = new Bundle();
                        bundle.putString("G_NAME", (String) GroupList.get(posi).getName());
                        bundle.putInt("G_ICON", GroupLogo[posi]);//have to change this
                        if (GroupList.get(posi).getFollowers() == null) {
                            bundle.putInt("F_COUNT", 0);
                        } else {
                            bundle.putInt("F_COUNT", GroupList.get(posi).getFollowers().length());
                        }
                        if (GroupList.get(posi).getMembers() == null) {
                            bundle.putInt("M_COUNT", 0);
                        } else {
                            bundle.putInt("M_COUNT", GroupList.get(posi).getMembers().length());
                        }
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
                            followingFlag.set(posi, Boolean.TRUE);

                            File f = new File(getActivity().getFilesDir(), "Follows.txt");
                            writeLineToFile(f, GroupList.get(posi).getClubId() + "|" + GroupList.get(posi).getName() + "\n");
                            printFileContents(f);
                            Log.e("check", GroupList.get(posi).getName() + posi);
                            followGroup(GroupList.get(posi));
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
                            followingFlag.set(posi, Boolean.FALSE);
                            followingMap.remove(GroupList.get(posi).getClubId());

                            File f = new File(getActivity().getFilesDir(), "Follows.txt");
                            removeLineFromFile(f, GroupList.get(posi).getClubId() + "|" + GroupList.get(posi).getName());
                            printFileContents(f);
                            Log.e("check", GroupList.get(posi).getName() + posi);
                            unFollowGroup(GroupList.get(posi));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

            }

        }
    }






}