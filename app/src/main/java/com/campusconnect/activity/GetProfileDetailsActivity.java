package com.campusconnect.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.appspot.campus_connect_2015.clubs.Clubs;
import com.appspot.campus_connect_2015.clubs.model.ModelsProfileMiniForm;
import com.campusconnect.R;
import com.campusconnect.bean.CollegeListInfoBean;
import com.campusconnect.communicator.WebRequestTask;
import com.campusconnect.communicator.WebServiceDetails;
import com.campusconnect.constant.AppConstants;
import com.campusconnect.utility.NetworkAvailablity;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.common.base.Strings;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by RK on 26-09-2015.
 */
public class GetProfileDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    Button cont;
    EditText batch, branch;

    static ModelsProfileMiniForm pmf;
    private static final String LOG_TAG = "GetProfileDetails";

    private String mEmailAccount = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_profile_details);

        batch = (EditText) findViewById(R.id.et_batch);
        branch = (EditText) findViewById(R.id.et_branch);
        cont = (Button) findViewById(R.id.b_continue);
        SharedPreferences sharedpreferences = getSharedPreferences(AppConstants.SHARED_PREFS, Context.MODE_PRIVATE);
        mEmailAccount = sharedpreferences.getString(AppConstants.EMAIL_KEY, null);
        cont.setOnClickListener(this);
    }


    private void createProfile(View v) {
        Log.d(LOG_TAG, "entered cp");
        pmf = new ModelsProfileMiniForm();
        SharedPreferences
                sharedPreferences = v.getContext().getSharedPreferences(AppConstants.SHARED_PREFS, Context.MODE_PRIVATE);
        pmf.setName(sharedPreferences.getString(AppConstants.PERSON_NAME, null));
        pmf.setEmail(sharedPreferences.getString(AppConstants.EMAIL_KEY, null));
        if (sharedPreferences.getString(AppConstants.PROFILE_CATEGORY, null).equals(AppConstants.ALUMNI)) {
            pmf.setIsAlumni("Y");
        } else {
            pmf.setIsAlumni("N");
        }

        String str_batch = batch.getText().toString();
        if (str_batch == null) {
            str_batch = "";
        }
        String str_branch = branch.getText().toString();
        if (str_branch == null) {
            str_branch = "";
        }
        pmf.setBatch(str_batch);
        pmf.setBranch(str_branch);


        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(AppConstants.BATCH, pmf.getBatch());
        edit.putString(AppConstants.BRANCH, pmf.getBranch());
        Log.e("Follows", String.valueOf(pmf.getFollows()));
        //edit.putString(AppConstants.BRANCH, pmf.getFollows();
        edit.commit();
        pmf.setCollegeId(sharedPreferences.getString(AppConstants.COLLEGE_ID, null));
        saveProfile();
    }


  public void   WebApiSaveProfile(){
      JSONObject jsonObject = new JSONObject();

      List<NameValuePair> param = new ArrayList<NameValuePair>();
      String url = WebServiceDetails.DEFAULT_BASE_URL + "profile";
      new WebRequestTask(GetProfileDetailsActivity.this, param, _handler, WebRequestTask.GET, jsonObject, WebServiceDetails.PID_SAVE_PROFILE,
              true, url).execute();


    }



//TODO save profile calling remain statud code 203
    public void saveProfile() {

        Log.d(LOG_TAG, "starting save profile");
        if (!isSignedIn()) {
            Toast.makeText(this, "You must sign in for this action.", Toast.LENGTH_LONG).show();
            return;
        }

        //TODO network check

        if (NetworkAvailablity.hasInternetConnection(GetProfileDetailsActivity.this)) {
            AsyncTask<Void, Void, ModelsProfileMiniForm> saveProfile =
                    new AsyncTask<Void, Void, ModelsProfileMiniForm>() {
                        @Override
                        protected ModelsProfileMiniForm doInBackground(Void... unused) {
                            if (!isSignedIn()) {
                                return null;
                            };

                            if (!AppConstants.checkGooglePlayServicesAvailable(GetProfileDetailsActivity.this)) {
                                return null;
                            }

                            // Create a Google credential since this is an authenticated request to the API.
                            GoogleAccountCredential credential = GoogleAccountCredential.usingAudience(
                                    GetProfileDetailsActivity.this, AppConstants.AUDIENCE);
                            credential.setSelectedAccountName(mEmailAccount);

                            // Retrieve service handle using credential since this is an authenticated call.
                            Clubs apiServiceHandle = AppConstants.getApiServiceHandle(credential);

                            try {
                                Clubs.SaveProfile sp = apiServiceHandle.saveProfile(pmf);

                                ModelsProfileMiniForm mpf = sp.execute();
                                Log.e(LOG_TAG, "SUCCESS");
                                Log.e(LOG_TAG, mpf.toPrettyString());
                                return mpf;
                            } catch (IOException e) {
                                Log.e(LOG_TAG, "Exception during API call", e);
                            }
                            return null;
                        }

                        @Override
                        protected void onPostExecute(ModelsProfileMiniForm prof) {
                            if (prof != null) {
                                Log.d(LOG_TAG, "saved successfully");
                            } else {
                                Log.e(LOG_TAG, "couldnt save");
                            }
                        }
                    };

            saveProfile.execute((Void) null);
        } else {
            Toast.makeText(GetProfileDetailsActivity.this, "Network is not available.", Toast.LENGTH_SHORT).show();

        }

    }

    private boolean isSignedIn() {
        if (!Strings.isNullOrEmpty(mEmailAccount)) {
            return true;
        } else {
            return false;
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.b_continue:
                createProfile(v);
                Intent intent_temp = new Intent(v.getContext(), Signup_4Activity.class);
                startActivity(intent_temp);
                break;
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
                        case WebServiceDetails.PID_SAVE_PROFILE: {
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



                                } else {
                                    Toast.makeText(GetProfileDetailsActivity.this, "error", Toast.LENGTH_SHORT).show();

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
                    Toast.makeText(GetProfileDetailsActivity.this, "SERVER_ERROR", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(GetProfileDetailsActivity.this, "SERVER_ERROR", Toast.LENGTH_LONG).show();
            }
        }
    };

}