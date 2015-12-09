package com.campusconnect.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.campusconnect.R;
import com.campusconnect.supportClasses.JoinRequestsinfoActivity;

import java.util.List;

/**
 * Created by RK on 23-09-2015.
 */
public class JoinRequestsAdapterActivity extends
        RecyclerView.Adapter<JoinRequestsAdapterActivity.JoinRequestsViewHolder> {

    private List<JoinRequestsinfoActivity> JoinRequestsList;
    CharSequence RequestedPeople[]={"Shon requested to join.","Pratheek requested to join."};

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
        join_requestsViewHolder.request.setText(RequestedPeople[i]);

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
            
        }

    }
}

