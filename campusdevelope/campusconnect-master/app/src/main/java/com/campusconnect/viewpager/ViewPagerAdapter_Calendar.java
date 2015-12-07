package com.campusconnect.viewpager;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.campusconnect.fragment.calender.FragmentFriday;
import com.campusconnect.fragment.calender.FragmentMonday;
import com.campusconnect.fragment.calender.FragmentSaturday;
import com.campusconnect.fragment.calender.FragmentSunday;
import com.campusconnect.fragment.calender.FragmentThursday;
import com.campusconnect.fragment.calender.FragmentTuesday;
import com.campusconnect.fragment.calender.FragmentWednesday;


/**
 * Created by RK on 08-10-2015.
 */
public class ViewPagerAdapter_Calendar extends FragmentPagerAdapter {

    CharSequence Titles[]; // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapter_home is created
    int NumbOfTabs; // Store the number of tabs, this will also be passed when the ViewPagerAdapter_home is created

    private Context mContext;
    // Build a Constructor and assign the passed Values to appropriate values in the class
    public ViewPagerAdapter_Calendar(FragmentManager fm, CharSequence mTitles[], int mNumbOfTabsumb, Context context) {
        super(fm);
        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;
        this.mContext = context;
    }

    //This method return the fragment for the every position in the View Pager
    @Override
    public Fragment getItem(int position) {

        if(position == 0)
        {
            FragmentMonday frag_mon = new FragmentMonday();
            return frag_mon;
        }
        else if(position == 1)
        {
            FragmentTuesday frag_tue = new FragmentTuesday();
            return frag_tue;
        }
        else if(position == 2)
        {
            FragmentWednesday frag_wed = new FragmentWednesday();
            return frag_wed;
        }
        else if(position == 3)
        {
            FragmentThursday frag_thu = new FragmentThursday();
            return frag_thu;
        }
        else if(position == 4)
        {
            FragmentFriday frag_fri = new FragmentFriday();
            return frag_fri;
        }
        else if(position == 5)
        {
            FragmentSaturday frag_sat = new FragmentSaturday();
            return frag_sat;
        }
        else
        {
            FragmentSunday frag_sun = new FragmentSunday();
            return frag_sun;
        }

    }



    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }


    // This method return the Number of tabs for the tabs Strip

    @Override
    public int getCount() {
        return NumbOfTabs;
    }
}

