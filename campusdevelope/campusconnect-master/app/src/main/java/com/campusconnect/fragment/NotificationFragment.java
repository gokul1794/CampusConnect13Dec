package com.campusconnect.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.campusconnect.R;
import com.campusconnect.adapter.NotificationAdapterActivity;
import com.campusconnect.supportClasses.Notification_infoActivity;

import java.util.ArrayList;
import java.util.List;


public class NotificationFragment extends Fragment  {

    RecyclerView notification_list;
    ImageButton noti, profile, home, calendar, search;
    View mRootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mRootView != null) {
            ViewGroup parent = (ViewGroup) mRootView.getParent();
            if (parent != null)
                parent.removeView(mRootView);
        }
        try {
            mRootView = inflater.inflate(R.layout.activity_notification, container, false);


            notification_list = (RecyclerView) mRootView.findViewById(R.id.rv_notification);
            notification_list.setHasFixedSize(true);
            LinearLayoutManager llm = new LinearLayoutManager(getActivity());
            llm.setOrientation(LinearLayoutManager.VERTICAL);
            notification_list.setLayoutManager(llm);
            notification_list.setItemAnimator(new DefaultItemAnimator());
            NotificationAdapterActivity nl = new NotificationAdapterActivity(
                    createList_nl(3));
            notification_list.setAdapter(nl);

        } catch (InflateException e) {
            e.printStackTrace();
        }
        return mRootView;
    }



  /*  @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        overridePendingTransition(R.anim.pushfront, R.anim.centertoright);*/

       /* home = (ImageButton) findViewById(R.id.ib_home);
        noti = (ImageButton) findViewById(R.id.ib_notification);
        profile = (ImageButton) findViewById(R.id.ib_profile);
        calendar = (ImageButton) findViewById(R.id.ib_calendar);
        search = (ImageButton) findViewById(R.id.ib_search);*/


        //     home.setOnClickListener(this);
        //   calendar.setOnClickListener(this);
        //  profile.setOnClickListener(this);
        //  search.setOnClickListener(this);

        //   }

   /* //TODO  changes in the listner
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_profile:
                Intent intent_profile = new Intent(v.getContext(), ProfilePageFragment.class);
                startActivity(intent_profile);
                finish();
                break;
            case R.id.ib_home:
                Intent intent_home = new Intent(v.getContext(), HomeFragment.class);
                startActivity(intent_home);
                overridePendingTransition(R.anim.pushfront, R.anim.centertoright);
                finish();
                break;
            case R.id.ib_calendar:
                Intent intent_noti = new Intent(v.getContext(), CalenderFragment.class);
                startActivity(intent_noti);
                finish();
                break;
            case R.id.ib_search:
                Intent intent_cal = new Intent(v.getContext(), SearchFragment.class);
                startActivity(intent_cal);
                finish();
                break;
        }
    }*/


    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
*/
  /*  @Override
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
    }*/



    private List<Notification_infoActivity> createList_nl ( int size){
        List<Notification_infoActivity> result = new ArrayList<Notification_infoActivity>();
        for (int i = 1; i <= size; i++) {
            Notification_infoActivity ci = new Notification_infoActivity();
            result.add(ci);
        }

        return result;
    }
}
