package com.campusconnect.adapter;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.appspot.campus_connect_2015.clubs.model.ModelsClubMiniForm;
import com.campusconnect.R;
import com.campusconnect.activity.GroupPageActivity;
import com.campusconnect.activity.MainActivity;
import com.campusconnect.supportClasses.GroupsJoined_infoActivity;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * Created by RK on 03/12/2015.
 */
public class SelectGroupAtSignUpAdapter extends
        RecyclerView.Adapter<SelectGroupAtSignUpAdapter.GroupListViewHolder> {

    private List<GroupsJoined_infoActivity> GroupList;
    private List<String> itemsName;

    public SelectGroupAtSignUpAdapter(List<GroupsJoined_infoActivity> GroupList) throws IOException {
        this.GroupList = GroupList;
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
        if(i==0) {
            group_listViewHolder.group_title.setTypeface(null, Typeface.BOLD);
            group_listViewHolder.group_title.setGravity(Gravity.CENTER);
            group_listViewHolder.i_divider.setVisibility(View.GONE);
            group_listViewHolder.t_divider.setVisibility(View.VISIBLE);
        } else {
            group_listViewHolder.group_title.setTypeface(null, Typeface.NORMAL);
            group_listViewHolder.group_title.setGravity(Gravity.LEFT);
            group_listViewHolder.i_divider.setVisibility(View.VISIBLE);
            group_listViewHolder.t_divider.setVisibility(View.GONE);
        }

    }

    @Override
    public GroupListViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.activity_card_layout_group_list_sign_up, viewGroup, false);

        return new GroupListViewHolder(itemView);
    }


    public class GroupListViewHolder extends RecyclerView.ViewHolder {

        CardView group_list;
        TextView follow, following, group_title;
        View t_divider, i_divider;

        public GroupListViewHolder(View v) {
            super(v);

            group_list = (CardView) v.findViewById(R.id.group_list_card);
            group_title = (TextView) v.findViewById(R.id.tv_group_name);
            follow = (TextView) v.findViewById(R.id.tv_follow);
            following = (TextView) v.findViewById(R.id.tv_following);
            t_divider = (View) v.findViewById(R.id.title_divider);
            i_divider = (View) v.findViewById(R.id.item_divider);

            group_list.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent_temp = new Intent(v.getContext(), MainActivity.class);

                    v.getContext().startActivity(intent_temp);

                }
            });

            follow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    follow.setVisibility(View.GONE);
                    following.setVisibility(View.VISIBLE);
                }
            });
            following.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    follow.setVisibility(View.VISIBLE);
                    following.setVisibility(View.GONE);
                }
            });

        }

    }
}
