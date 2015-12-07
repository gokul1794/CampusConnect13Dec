package com.campusconnect.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.campusconnect.activity.CategoryActivity;
import com.campusconnect.bean.CollegeListInfoBean;
import com.campusconnect.constant.AppConstants;
import com.campusconnect.R;

import java.util.List;


/**
 * Created by RK on 21-09-2015.
 */
public class CollegeListAdapterActivity extends
        RecyclerView.Adapter<CollegeListAdapterActivity.CollegeListViewHolder> {

   private static List<CollegeListInfoBean> CollegeList;

    static int pos;

    public CollegeListAdapterActivity(List<CollegeListInfoBean> CollegeList) {
        this.CollegeList = CollegeList;
    }

    @Override
    public int getItemCount() {
        return CollegeList.size();
    }

    @Override
    public void onBindViewHolder(CollegeListViewHolder group_listViewHolder, int i) {
        CollegeListInfoBean ci = CollegeList.get(i);
        group_listViewHolder.college_name.setText(ci.getName());
    }

    @Override
    public CollegeListViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.activity_card_layout_college_list, viewGroup, false);

        return new CollegeListViewHolder(itemView);
    }

    public static class CollegeListViewHolder extends RecyclerView.ViewHolder {

        CardView college_list;
        TextView college_name;
        public CollegeListViewHolder(View v) {
            super(v);

            college_list = (CardView) v.findViewById(R.id.college_list);
            college_name = (TextView) v.findViewById(R.id.tv_college_name);

            college_list.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pos=getPosition();

                    SharedPreferences sharedpreferences = v.getContext().getSharedPreferences(AppConstants.SHARED_PREFS, Context.MODE_PRIVATE);
                    SharedPreferences.Editor edit=sharedpreferences.edit();
                    edit.putString(AppConstants.COLLEGE_NAME,CollegeList.get(pos).getName());
                    edit.putString(AppConstants.COLLEGE_LOCATION,CollegeList.get(pos).getLocation());
                    edit.putString(AppConstants.COLLEGE_ID,CollegeList.get(pos).getCollegeId());
                    edit.commit();

                    Intent intent_temp = new Intent(v.getContext(), CategoryActivity.class);
                    v.getContext().startActivity(intent_temp);

                }
            });

        }

    }
}