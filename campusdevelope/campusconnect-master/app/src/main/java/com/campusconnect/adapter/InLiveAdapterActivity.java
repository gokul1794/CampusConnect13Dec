package com.campusconnect.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.campusconnect.R;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by RK on 09-10-2015.
 */
public class InLiveAdapterActivity extends
        RecyclerView.Adapter<InLiveAdapterActivity.InLiveViewHolder> {

    private List<String> itemsName;
    private LayoutInflater layoutInflater;

    public InLiveAdapterActivity(Context context) {
        layoutInflater = LayoutInflater.from(context);
        itemsName = new ArrayList<String>();
    }

    @Override
    public int getItemCount() {
        return itemsName.size();
    }

    public void add(int location, String iName){
        itemsName.add(location, iName);
        notifyItemInserted(location);
    }

    @Override
    public void onBindViewHolder(InLiveViewHolder in_live_listViewHolder, int i) {
        in_live_listViewHolder.comments.setText(itemsName.get(i));

    }

    @Override
    public InLiveViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.activity_card_layout_in_live, viewGroup, false);

        return new InLiveViewHolder(itemView);
    }

    public static class InLiveViewHolder extends RecyclerView.ViewHolder {

        TextView comments;
        public InLiveViewHolder(View v) {
            super(v);
            comments = (TextView)v.findViewById(R.id.tv_comment);

        }
        
    }
}
