package com.campusconnect.fragment;

import android.graphics.Typeface;
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
import android.widget.TextView;

import com.campusconnect.R;
import com.campusconnect.adapter.ProfilePageAdapterActivity;
import com.campusconnect.supportClasses.MyScrollListenerProfilePage;
import com.campusconnect.supportClasses.ProfilePage_infoActivity;
import com.campusconnect.utility.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;


public class ProfilePageFragment extends Fragment   {

    RecyclerView groups_joined;
    TextView profile_text;
    int top=0;
    ImageButton noti,profile,home,calendar,search;




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
            mRootView = inflater.inflate(R.layout.activity_profile_page, container, false);
            Typeface r_med = Typeface.createFromAsset(mRootView.getContext().getAssets(), "font/Roboto_Medium.ttf");

            groups_joined = (RecyclerView)mRootView.findViewById(R.id.recycler_groups);
            profile_text = (TextView)mRootView.findViewById(R.id.tv_profile_text);

            LinearLayoutManager llm = new LinearLayoutManager(getActivity());
            llm.setOrientation(LinearLayoutManager.VERTICAL);
            groups_joined.setLayoutManager(llm);
            groups_joined.setHasFixedSize(true);
            groups_joined.setItemAnimator(new DefaultItemAnimator());
            groups_joined.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));

            profile_text.setTypeface(r_med);

            ProfilePageAdapterActivity gj = new ProfilePageAdapterActivity(
                    createList_groups_joined(3));

            groups_joined.setAdapter(gj);

            groups_joined.setOnScrollListener(new MyScrollListenerProfilePage(getActivity()) {

                @Override
                public void onMoved(int distance) {
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    top += dy;
                }
            });

        } catch (InflateException e) {
            e.printStackTrace();
        }
        return mRootView;
    }



    /*@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);
        overridePendingTransition(R.anim.pushfront, R.anim.centertoright);

        home = (ImageButton) findViewById(R.id.ib_home);
        noti = (ImageButton) findViewById(R.id.ib_notification);
        profile = (ImageButton) findViewById(R.id.ib_profile);
        calendar = (ImageButton) findViewById(R.id.ib_calendar);
        search = (ImageButton) findViewById(R.id.ib_search);


        noti.setOnClickListener(this);
        home.setOnClickListener(this);
        calendar.setOnClickListener(this);
        search.setOnClickListener(this);
    }*/

//TODO  changes in the listner
  /*  @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ib_search:
                Intent intent_serch = new Intent(v.getContext(), SearchFragment.class);
                startActivity(intent_serch);
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
            case R.id.ib_calendar:
                Intent intent_cal = new Intent(v.getContext(), CalenderFragment.class);
                startActivity(intent_cal);
                finish();
                break;
        }
    }*/

/*

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
*/


   /* @Override
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
    private List<ProfilePage_infoActivity> createList_groups_joined(int size) {

        List<ProfilePage_infoActivity> result = new ArrayList<ProfilePage_infoActivity>();
        for (int i = 1; i <= size; i++) {
            ProfilePage_infoActivity ci = new ProfilePage_infoActivity();

            result.add(ci);

        }

        return result;
    }

}
