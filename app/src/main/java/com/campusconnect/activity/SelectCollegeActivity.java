package com.campusconnect.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.appspot.campus_connect_2015.clubs.Clubs;
import com.appspot.campus_connect_2015.clubs.model.ModelsColleges;
import com.appspot.campus_connect_2015.clubs.model.ModelsGetCollege;
import com.campusconnect.R;
import com.campusconnect.adapter.CollegeListAdapterActivity;
import com.campusconnect.bean.CollegeListInfoBean;
import com.campusconnect.communicator.WebRequestTask;
import com.campusconnect.communicator.WebServiceDetails;
import com.campusconnect.constant.AppConstants;
import com.campusconnect.utility.DividerItemDecoration_college_list;
import com.campusconnect.utility.NetworkAvailablity;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.common.base.Strings;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by RK on 23-09-2015.
 */
public class SelectCollegeActivity extends AppCompatActivity {

    private static final String LOG_TAG = "SelectCollegeActivity";

    //private AuthorizationCheckTask mAuthTask;
    private String mEmailAccount = "";


    RecyclerView college_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_college);

        college_list = (RecyclerView) findViewById(R.id.rv_college_list);

        college_list.setHasFixedSize(false);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        college_list.setLayoutManager(llm);
        college_list.setItemAnimator(new DefaultItemAnimator());
        college_list.addItemDecoration(new DividerItemDecoration_college_list(this, LinearLayoutManager.HORIZONTAL));
        SharedPreferences sharedpreferences = getSharedPreferences(AppConstants.SHARED_PREFS, Context.MODE_PRIVATE);
        mEmailAccount = sharedpreferences.getString(AppConstants.EMAIL_KEY, null);

        //TODO network check

        if (NetworkAvailablity.hasInternetConnection(SelectCollegeActivity.this)) {


            GetAllCollegesWebAPI();
            //TODO remove old WEB api getColleges
            // getColleges();
        } else {
            Toast.makeText(SelectCollegeActivity.this, "Network is not available.", Toast.LENGTH_SHORT).show();

        }
    }

    private void GetAllCollegesWebAPI() {
        JSONObject jsonObject = new JSONObject();
        List<NameValuePair> param = new ArrayList<NameValuePair>();
        String url = WebServiceDetails.DEFAULT_BASE_URL + "getColleges";
        new WebRequestTask(SelectCollegeActivity.this, param, _handler, WebRequestTask.GET, jsonObject, WebServiceDetails.PID_SELECT_COLLEGE,
                true, url).execute();
    }

    public void getColleges() {
        if (!isSignedIn()) {
            Toast.makeText(this, "You must sign in for this action.", Toast.LENGTH_LONG).show();
            return;
        }

        AsyncTask<Void, Void, ModelsColleges> getCollegesAndPopulate =
                new AsyncTask<Void, Void, ModelsColleges>() {
                    @Override
                    protected ModelsColleges doInBackground(Void... unused) {
                        if (!isSignedIn()) {
                            return null;
                        }

                        if (!AppConstants.checkGooglePlayServicesAvailable(SelectCollegeActivity.this)) {
                            return null;
                        }

                        // Create a Google credential since this is an authenticated request to the API.
                        GoogleAccountCredential credential = GoogleAccountCredential.usingAudience(
                                SelectCollegeActivity.this, AppConstants.AUDIENCE);
                        credential.setSelectedAccountName(mEmailAccount);
                        // Retrieve service handle using credential since this is an authenticated call.
                        Clubs apiServiceHandle = AppConstants.getApiServiceHandle(credential);

                        try {
                            Clubs.GetColleges gc = apiServiceHandle.getColleges();
                            ModelsColleges collegeList = gc.execute();
                            Log.e(LOG_TAG, "SUCCESS");
                            Log.e(LOG_TAG, collegeList.toPrettyString());
                            Log.e(LOG_TAG, collegeList.toPrettyString());
                            return collegeList;
                        } catch (IOException e) {
                            Log.e(LOG_TAG, "Exception during API call", e);
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(ModelsColleges cList) {
                        if (cList != null) {
                            CollegeListAdapterActivity cl = new CollegeListAdapterActivity(
                                    displayColleges(cList));
                            college_list.setAdapter(cl);

                        } else {
                            Log.e(LOG_TAG, "No clubs were returned by the API.");
                        }
                    }
                };

        getCollegesAndPopulate.execute((Void) null);
    }

    private boolean isSignedIn() {
        if (!Strings.isNullOrEmpty(mEmailAccount)) {
            return true;
        } else {
            return false;
        }
    }

    private List<CollegeListInfoBean> displayColleges(ModelsColleges... response) {
        String msg = "nothing";
        List<CollegeListInfoBean> cList = new ArrayList<CollegeListInfoBean>();

        Log.e(LOG_TAG, response.toString());

        if (response == null || response.length < 1) {
            msg = "Colleges were not present";
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
            return null;
        } else {
            {
                Log.d(LOG_TAG, "Displaying " + response.length + " colleges.");
                StringBuilder sb = new StringBuilder();
                List<ModelsColleges> collegeList = Arrays.asList(response);
                Log.d(LOG_TAG, collegeList.toString());

                List<ModelsGetCollege> correctList = (List<ModelsGetCollege>) collegeList.get(0).get("collegeList");

                for (ModelsGetCollege gc : correctList) {
                    sb.append(gc.getAbbreviation() + "\n");
                    CollegeListInfoBean ci = new CollegeListInfoBean();
                    ci.setName(gc.getName());
                    ci.setLocation(gc.getLocation());
                    ci.setCollegeId(gc.getCollegeId());
                    cList.add(ci);
                    Log.d(LOG_TAG, gc.toString());
                }
                msg = sb.toString();
                Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
            }
            return cList;
        }
    }

    private final Handler _handler = new Handler() {
        public void handleMessage(Message msg) {
            int response_code = msg.what;
            if (response_code != 0) {
                String strResponse = (String) msg.obj;
                Log.v("Response", strResponse);
                if (strResponse != null && strResponse.length() > 0) {
                    switch (response_code) {
                        case WebServiceDetails.PID_SELECT_COLLEGE: {
                            try {

                                List<CollegeListInfoBean> list = new ArrayList<CollegeListInfoBean>();
                                JSONObject collegeObj = new JSONObject(strResponse);
                                if (collegeObj.has("collegeList")) {
                                    JSONArray collegeArr = collegeObj.getJSONArray("collegeList");
                                    for (int i = 0; i < collegeArr.length(); i++) {
                                        JSONObject obj = collegeArr.getJSONObject(i);
                                        String abbreviation = obj.optString("abbreviation");
                                        String location = obj.optString("location");
                                        String name = obj.optString("name");
                                        String collegeid = obj.optString("collegeId");


                                        CollegeListInfoBean bean = new CollegeListInfoBean();
                                        bean.setCollegeId(collegeid);
                                        bean.setLocation(location);
                                        bean.setName(name);
                                        list.add(bean);
                                    }
                                    CollegeListAdapterActivity cl = new CollegeListAdapterActivity(list);
                                    college_list.setAdapter(cl);

                                } else {
                                    Toast.makeText(SelectCollegeActivity.this, "error", Toast.LENGTH_SHORT).show();

                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                        break;

                        default:
                            break;
                    }
                } else {
                    Toast.makeText(SelectCollegeActivity.this, "SERVER_ERROR", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(SelectCollegeActivity.this, "SERVER_ERROR", Toast.LENGTH_LONG).show();
            }
        }
    };


}
