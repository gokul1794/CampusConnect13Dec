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
import com.campusconnect.supportClasses.JoinRequestsinfoActivity;
import com.campusconnect.utility.CustomTypefaceSpan;

import java.util.List;

/**
 * Created by RK on 23-09-2015.
 */
public class JoinRequestsAdapterActivity extends
        RecyclerView.Adapter<JoinRequestsAdapterActivity.JoinRequestsViewHolder> {

    private List<JoinRequestsinfoActivity> JoinRequestsList;
    public static SpannableStringBuilder request;
    public static TypefaceSpan robotoRegularSpan_for_name, robotoRegularSpan_for_subject;
    String name_requested_by="";
    String request_type_one = " requested to join.";
    String request_type_two = " requested to post ";
    String subject = "";
    CharSequence RequestedPeople[]={"Shon","Pratheek"};

    public JoinRequestsAdapterActivity(List<JoinRequestsinfoActivity> JoinRequestsList) {
        this.JoinRequestsList = JoinRequestsList;
    }

    @Override
    public int getItemCount() {
        return JoinRequestsList.size();
    }

    @Override
    public void onBindViewHolder(JoinRequestsViewHolder join_requestsViewHolder, int i) {
        JoinRequestsinfoActivity ci = JoinRequestsList.get(i);

        //Following i conditions are to be replaced by appropriate request types
        if(i==0)  //This is the request to join a group
        {
            name_requested_by = RequestedPeople[i].toString();
            request = new SpannableStringBuilder(name_requested_by+request_type_one);
            request.setSpan(robotoRegularSpan_for_name, 0, name_requested_by.length(), 0);
        }
        else if(i==1)
        {
            name_requested_by = RequestedPeople[i].toString();
            subject = "The Apprentice Competetion";
            request = new SpannableStringBuilder(name_requested_by+request_type_two+subject+".");
            request.setSpan(robotoRegularSpan_for_name, 0, name_requested_by.length(), 0);
            request.setSpan(robotoRegularSpan_for_subject, name_requested_by.length()+request_type_two.length(), name_requested_by.length()+request_type_two.length()+subject.length()+1, 0);
        }
        join_requestsViewHolder.request.setText(request);

    }

    @Override
    public JoinRequestsViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.activity_card_layout_join_request, viewGroup, false);

        return new JoinRequestsViewHolder(itemView);
    }

    public static class JoinRequestsViewHolder extends RecyclerView.ViewHolder {

        TextView request;
        public JoinRequestsViewHolder(View v) {
            super(v);
            request = (TextView) v.findViewById(R.id.tv_request);

            Typeface r_lig = Typeface.createFromAsset(v.getContext().getAssets(), "font/Roboto_Light.ttf");
            Typeface r_reg = Typeface.createFromAsset(v.getContext().getAssets(), "font/Roboto_Regular.ttf");
            request.setTypeface(r_lig);
            robotoRegularSpan_for_name = new CustomTypefaceSpan("", r_reg);
            robotoRegularSpan_for_subject = new CustomTypefaceSpan("", r_reg);
            
        }

    }
}

