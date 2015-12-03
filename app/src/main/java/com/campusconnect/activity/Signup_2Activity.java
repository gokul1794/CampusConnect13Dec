package com.campusconnect.activity;

import android.content.Context;
import android.content.Intent;
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
import android.view.View;
import android.widget.LinearLayout;
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
public class Signup_2Activity extends AppCompatActivity {

    LinearLayout temporary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_2);

        temporary = (LinearLayout)findViewById(R.id.temp);

        temporary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_temp = new Intent(v.getContext(), GetProfileDetailsActivity.class);
                startActivity(intent_temp);

            }
        });


    }


}
