package com.campusconnect.adapter;

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.style.TypefaceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.campusconnect.R;
import com.campusconnect.bean.NotificationBean;
import com.campusconnect.utility.CircularImageView;
import com.campusconnect.utility.CustomTypefaceSpan;

import java.util.List;

/**
 * Created by RK on 22-09-2015.
 */
public class NotificationAdapterActivity extends
        RecyclerView.Adapter<NotificationAdapterActivity.NotificationViewHolder> {

    private List<NotificationBean> NotificationList;

    public static SpannableStringBuilder g_name;
    public static Typeface r_lig, r_reg;
    public static TypefaceSpan robotoRegularSpan_for_group_name, robotoRegularSpan_for_subject;
    public static String group_name = " ", subject = " ", end = "";

    public NotificationAdapterActivity(List<NotificationBean> NotificationList) {
        this.NotificationList = NotificationList;
    }

    @Override
    public int getItemCount() {
        return NotificationList.size();
    }

    @Override
    public void onBindViewHolder(NotificationViewHolder notification_listViewHolder, int i) {
        NotificationBean ci = NotificationList.get(i);

        String intro = "";
        String middle = "";

        if(ci.getType()!=null) {

            if (ci.getType().equalsIgnoreCase("Approved Join Request")) {

                intro = "This just in! ";
                middle = " has posted a news post ";
                group_name = "" + ci.getGroup_name();            //To be gotten from server
                subject = "Club Recruitment List";      ////To be gotten from server

                g_name = new SpannableStringBuilder(intro + group_name + middle + subject + ".");
                g_name.setSpan(robotoRegularSpan_for_group_name, intro.length(), intro.length() + group_name.length(), 0);
                g_name.setSpan(robotoRegularSpan_for_subject, middle.length() + intro.length() + group_name.length(), middle.length() + intro.length() + group_name.length() + subject.length() + 1, 0);
            } else if (ci.getType().equalsIgnoreCase("post")) {

                intro = "Check it out! ";
                middle = " has posted an event post ";
                group_name = "" + ci.getGroup_name();            //To be gotten from server
                subject = "Play Auditions";      ////To be gotten from server

                g_name = new SpannableStringBuilder(intro + group_name + middle + subject + ".");
                g_name.setSpan(robotoRegularSpan_for_group_name, intro.length(), intro.length() + group_name.length(), 0);
                g_name.setSpan(robotoRegularSpan_for_subject, middle.length() + intro.length() + group_name.length(), middle.length() + intro.length() + group_name.length() + subject.length() + 1, 0);
            } else if (ci.getType().equalsIgnoreCase("Event")) {
                intro = "Get Ready! ";
                middle = " posted by ";
                end = " will begin in half hour ";
                group_name = "" + ci.getGroup_name();           //To be gotten from server
                subject = "Play Auditions";      ////To be gotten from server

                g_name = new SpannableStringBuilder(intro + subject + middle + group_name + end + ".");
                g_name.setSpan(robotoRegularSpan_for_group_name, intro.length(), intro.length() + subject.length(), 0);
                g_name.setSpan(robotoRegularSpan_for_subject, middle.length() + intro.length() + subject.length(), middle.length() + intro.length() + group_name.length() + subject.length() + 1, 0);

            } else if (ci.getType().equalsIgnoreCase("Reminder")) {

                intro = "Congratulations! Your request to join ";
                end = " has been accepted";
                group_name = "" + ci.getGroup_name();          //To be gotten from server

                g_name = new SpannableStringBuilder(intro + group_name + end + ".");
                g_name.setSpan(robotoRegularSpan_for_group_name, intro.length(), intro.length() + group_name.length(), 0);

            } else{

            }


                //i value has been used just for testing....if condition to be based on notification type
           /* if (i == 0) {    //Notification for posting a news post

            } else if (i == 1) {    //Notification for posting an event post

            } else if (i == 2) {    //Notification for an about to start event

            } else if (i == 3) {    //Notification for 'request accepted'
                intro = "Congratulations! Your request to join ";
                end = " has been accepted";
                group_name = "Rotaract Club";            //To be gotten from server

                g_name = new SpannableStringBuilder(intro + group_name + end + ".");
                g_name.setSpan(robotoRegularSpan_for_group_name, intro.length(), intro.length() + group_name.length(), 0);
            }*/
                notification_listViewHolder.notification.setText(g_name);
        }
    }

    @Override
    public NotificationViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.activity_card_layout_notification, viewGroup, false);
        return new NotificationViewHolder(itemView);
    }

    public static class NotificationViewHolder extends RecyclerView.ViewHolder {

        TextView notification, notification_ts;
        CircularImageView group_icon_notification;

        public NotificationViewHolder(View v) {
            super(v);
            notification = (TextView) v.findViewById(R.id.tv_notification);
            group_icon_notification = (CircularImageView) v.findViewById(R.id.group_icon_notification);
            notification_ts = (TextView) v.findViewById(R.id.tv_notification_timestamp);

            r_lig = Typeface.createFromAsset(v.getContext().getAssets(), "font/Roboto_Light.ttf");
            r_reg = Typeface.createFromAsset(v.getContext().getAssets(), "font/Roboto_Regular.ttf");

            robotoRegularSpan_for_group_name = new CustomTypefaceSpan("", r_reg);
            robotoRegularSpan_for_subject = new CustomTypefaceSpan("", r_reg);

            notification.setTypeface(r_lig);
            notification_ts.setTypeface(r_lig);
        }

    }
}


