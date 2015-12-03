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
public class Signup_4Activity extends AppCompatActivity {

    RecyclerView group_list_at_sign_up;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_4);

        group_list_at_sign_up = (RecyclerView) findViewById(R.id.rv_college_list);

        group_list_at_sign_up.setHasFixedSize(false);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        group_list_at_sign_up.setLayoutManager(llm);
        group_list_at_sign_up.setItemAnimator(new DefaultItemAnimator());
        //group_list_at_sign_up.addItemDecoration(new DividerItemDecoration_college_list(this, LinearLayoutManager.VERTICAL));

    }
}
