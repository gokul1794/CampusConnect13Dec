package com.campusconnect.adapter;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.campusconnect.utility.CircularImageView;
import com.campusconnect.R;

import java.util.List;

/**
 * Created by RK on 07-10-2015.
 */
public class GroupPageAdapterActivity extends
        RecyclerView.Adapter<GroupPageAdapterActivity.GroupPageHolder> {

    private List<GroupPage_infoActivity> GroupPageList;
    String g_name;
    Integer g_icon,m_count,f_count;
    int follow_click_count=0,member_click_count=0;

    GroupInfoViewHolder holder1;
    ExtraGroupInfoListHolder holder2;
    CharSequence GroupInfoAttributes[]={"About","Members","Upcoming Events","News Posts","Previous Events"};

    public GroupPageAdapterActivity(List<GroupPage_infoActivity> GroupsJoinedList) {
        this.GroupPageList = GroupsJoinedList;
    }

    @Override
    public int getItemCount() {
        return GroupPageList.size();
    }

    @Override
    public void onBindViewHolder(GroupPageHolder groupViewHolder, int i) {
        if(getItemViewType(i)==0) {
            holder1 = (GroupInfoViewHolder) groupViewHolder;
            holder1.group_name.setText(g_name);
            holder1.group_icon.setImageResource(g_icon);
            holder1.members_count.setText(m_count.toString());
            holder1.followers_count.setText(f_count.toString());
        }
        else {
            holder2 = (ExtraGroupInfoListHolder) groupViewHolder;
            holder2.g_name_joined.setText(GroupInfoAttributes[i-1]);
        }

    }

    @Override
    public int getItemViewType(int position) {
        int viewType;
        if(position==0)
            viewType = 0;
        else
            viewType=1;

        return viewType;
    }

    @Override
    public GroupPageHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        GroupPageHolder holder;
        if(i==0)
        {
            View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(
                    R.layout.activity_card_layout_group_info, viewGroup, false);
            holder = new GroupInfoViewHolder(itemView);
        }
        else
        {
            View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(
                    R.layout.activity_card_layout_extra_group_info, viewGroup, false);
            holder = new ExtraGroupInfoListHolder(itemView);
        }

        return holder;
    }

    public static class GroupPageHolder extends RecyclerView.ViewHolder {

        public GroupPageHolder(View v) {
            super(v);
        }

    }
    public class GroupInfoViewHolder extends GroupPageAdapterActivity.GroupPageHolder{

        TextView group_name,members_count,followers_count;
        CircularImageView group_icon;
        ToggleButton tbtn_follow,tbtn_member;
        public GroupInfoViewHolder(View itemView) {
            super(itemView);
            group_name = (TextView)itemView.findViewById(R.id.group_name);
            members_count = (TextView)itemView.findViewById(R.id.tv_members_count);
            followers_count = (TextView)itemView.findViewById(R.id.tv_followers_count);
            group_icon = (CircularImageView)itemView.findViewById(R.id.group_image);
            tbtn_follow = (ToggleButton)itemView.findViewById(R.id.tb_followers);
            tbtn_member = (ToggleButton)itemView.findViewById(R.id.tb_members);

            Bundle bundle =((Activity)itemView.getContext()).getIntent().getExtras();
            g_name = bundle.getString("G_NAME");
            g_icon = bundle.getInt("G_ICON");
            m_count = bundle.getInt("M_COUNT");
            f_count = bundle.getInt("F_COUNT");


            tbtn_follow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(follow_click_count%2==0) {
                        f_count++;
                        notifyDataSetChanged();
                    }
                    else {
                        f_count--;
                        notifyDataSetChanged();
                    }
                    follow_click_count++;
                }
            });
            tbtn_member.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(follow_click_count%2==0) {
                        m_count++;
                        notifyDataSetChanged();
                    }
                    else {
                        m_count--;
                        notifyDataSetChanged();
                    }
                    member_click_count++;
                }
            });

        }
    }

    public class ExtraGroupInfoListHolder extends GroupPageAdapterActivity.GroupPageHolder{

        TextView g_name_joined;
        public ExtraGroupInfoListHolder(View itemView) {
            super(itemView);
            g_name_joined = (TextView)itemView.findViewById(R.id.tv_group_joined_name);

        }
    }

}

