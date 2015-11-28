package com.campusconnect.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.campusconnect.R;
import com.campusconnect.supportClasses.Notification_infoActivity;
import com.campusconnect.utility.CircularImageView;

import java.util.List;

/**
 * Created by RK on 22-09-2015.
 */
public class NotificationAdapterActivity extends
        RecyclerView.Adapter<NotificationAdapterActivity.NotificationViewHolder> {

    private List<Notification_infoActivity> NotificationList;
    CharSequence notifications[]={"Rotaract Club posted news NEW Club Recruit List.","Rotaract Club posted an event Apprentice Competition.","Your request to join Rotaract Club has been accepted."};

    public NotificationAdapterActivity(List<Notification_infoActivity> NotificationList) {
        this.NotificationList = NotificationList;
    }

    @Override
    public int getItemCount() {
        return NotificationList.size();
    }

    @Override
    public void onBindViewHolder(NotificationViewHolder notification_listViewHolder, int i) {
        Notification_infoActivity ci = NotificationList.get(i);
        notification_listViewHolder.notification.setText(notifications[i]);
        notification_listViewHolder.group_icon_notification.setImageResource(R.mipmap.roto_logo);
    }

    @Override
    public NotificationViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.activity_card_layout_notification, viewGroup, false);
        return new NotificationViewHolder(itemView);
    }

    public static class NotificationViewHolder extends RecyclerView.ViewHolder {

        TextView notification;
        CircularImageView group_icon_notification;
        public NotificationViewHolder(View v) {
            super(v);
            notification = (TextView)v.findViewById(R.id.tv_notification);
            group_icon_notification = (CircularImageView)v.findViewById(R.id.group_icon_notification);

        }

    }
}


