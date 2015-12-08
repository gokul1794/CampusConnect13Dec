package com.campusconnect.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.campusconnect.R;
import com.campusconnect.supportClasses.GroupMembersByGroup_infoActivity;

import java.util.List;

/**
 * Created by RK on 05/11/2015.
 */
public class GroupMembersByGroupAdapterActivity extends
        RecyclerView.Adapter<GroupMembersByGroupAdapterActivity.GroupMembersByGroupViewHolder> {

    private List<GroupMembersByGroup_infoActivity> GroupMembersByGroupList;

    public GroupMembersByGroupAdapterActivity(List<GroupMembersByGroup_infoActivity> GroupMembersByGroupList) {
        this.GroupMembersByGroupList = GroupMembersByGroupList;
    }

    @Override
    public int getItemCount() {
        return GroupMembersByGroupList.size();
    }

    @Override
    public void onBindViewHolder(GroupMembersByGroupViewHolder group_membersbygroupViewHolder, int i) {
        GroupMembersByGroup_infoActivity ci = GroupMembersByGroupList.get(i);
    }

    @Override
    public GroupMembersByGroupViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.activity_card_layout_member, viewGroup, false);

        return new GroupMembersByGroupViewHolder(itemView);
    }

    public static class GroupMembersByGroupViewHolder extends RecyclerView.ViewHolder {

        public GroupMembersByGroupViewHolder(View v) {
            super(v);

        }

    }
}

