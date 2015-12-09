package com.campusconnect.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.campusconnect.R;
import com.campusconnect.slidingtab.SlidingTabLayout_Calendar;
import com.campusconnect.supportClasses.Notification_infoActivity;
import com.campusconnect.viewpager.ViewPagerAdapter_Calendar;

import java.util.ArrayList;
import java.util.List;

public class CalenderFragment extends Fragment {

    ImageButton noti,profile,home,calendar,search;

    ViewPager pager_calendar;
    ViewPagerAdapter_Calendar adapter_calendar;
    SlidingTabLayout_Calendar tabs;
    CharSequence Titles[]={"Mon 5","Tue 6","Wed 7","Thu 8","Fri 9","Sat 10","Sun 12"};
    int Numboftabs = 7;


    View  mRootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mRootView != null) {
            ViewGroup parent = (ViewGroup) mRootView.getParent();
            if (parent != null)
                parent.removeView(mRootView);
        }
        try {
            mRootView = inflater.inflate(R.layout.activity_calendar, container, false);

            pager_calendar = (ViewPager) mRootView.findViewById(R.id.pager_cal);
            tabs = (SlidingTabLayout_Calendar) mRootView.findViewById(R.id.tabs_calendar);
            adapter_calendar =  new ViewPagerAdapter_Calendar(getActivity().getSupportFragmentManager(),Titles,Numboftabs,getActivity());

            pager_calendar.setAdapter(adapter_calendar);

            tabs.setDistributeEvenly(true);
            tabs.setViewPager(pager_calendar);

        } catch (InflateException e) {
            e.printStackTrace();
        }
        return mRootView;
    }
   /*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_to_be_deleted);
        overridePendingTransition(R.anim.righttocenter, R.anim.pushback);

        home = (ImageButton) findViewById(R.id.ib_home);
        noti = (ImageButton) findViewById(R.id.ib_notification);
        profile = (ImageButton) findViewById(R.id.ib_profile);
        calendar = (ImageButton) findViewById(R.id.ib_calendar);
        search = (ImageButton) findViewById(R.id.ib_search);
*//*
        pager = (ViewPager) findViewById(R.id.pager);
        tabs = (SlidingTabLayout_Calendar) findViewById(R.id.tabs_calendar);
        adapter =  new ViewPagerAdapter_Calendar(getSupportFragmentManager(),Titles,Numboftabs,CalenderFragment.this);

        pager.setAdapter(adapter);

        tabs.setDistributeEvenly(true);
        tabs.setViewPager(pager);   *//*


        home.setOnClickListener(this);
        noti.setOnClickListener(this);
        profile.setOnClickListener(this);
        search.setOnClickListener(this);
    }
*/

    /*//TODO  changes in the listner
    @Override
    public void onClick(View v) {
        switch (v.getId()){
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
            case R.id.ib_notification:
                Intent intent_noti = new Intent(v.getContext(), NotificationFragment.class);
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


  /*  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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
*/
    /*
    private List<Notification_infoActivity> createList_nl(int size) {
        List<Notification_infoActivity> result = new ArrayList<Notification_infoActivity>();
        for (int i = 1; i <= size; i++) {
            Notification_infoActivity ci = new Notification_infoActivity();
            result.add(ci);
        }

        return result;
    }

*/
}

