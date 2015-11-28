package com.campusconnect.adapter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.appspot.campus_connect_2015.clubs.model.ModelsClubMiniForm;
import com.campusconnect.activity.GroupPageActivity;
import com.campusconnect.R;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * Created by RK on 21-09-2015.
 */
public class GroupListAdapterActivity extends
        RecyclerView.Adapter<GroupListAdapterActivity.GroupListViewHolder> {

    private List<ModelsClubMiniForm> GroupList;
    private List<String> itemsName;
    int posi=0;
    private static int[] GroupLogo = new int[] {
            R.mipmap.cell_logo,
            R.mipmap.football_logo,
            R.mipmap.ie_logo,
            R.mipmap.roto_logo
    };
    private static int[] followers_count = new int[] { 2,3,4,2};
    private static int[] members_count = new int[] { 1,2,1,2};

    private HashMap<String,String> followingMap=new HashMap<>();
    private List<Boolean> following ;

    public GroupListAdapterActivity(List<ModelsClubMiniForm> GroupList) throws IOException {
        this.GroupList = GroupList;
    }

    @Override
    public int getItemCount() {
        return GroupList.size();
    }

    public void add(int location, String item){
        itemsName.add(location, item);
        notifyItemInserted(location);
    }

    @Override
    public void onBindViewHolder(GroupListViewHolder group_listViewHolder, int i) {
        //ModelsClubMiniForm ci = GroupList.get(i);
        group_listViewHolder.group_title.setText(GroupList.get(i).getAbbreviation());
        if(following.get(i)){
            group_listViewHolder.following.setVisibility(View.VISIBLE);
            group_listViewHolder.follow.setVisibility(View.GONE);
        }
    }

    @Override
    public GroupListViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.activity_card_layout_group_list, viewGroup, false);

        return new GroupListViewHolder(itemView);
    }


    public class GroupListViewHolder extends RecyclerView.ViewHolder {

        CardView group_list;
        TextView follow,following, group_title;

        public GroupListViewHolder(View v) {
            super(v);

            group_list = (CardView) v.findViewById(R.id.group_list_card);
            group_title = (TextView) v.findViewById(R.id.tv_group_name);
            follow = (TextView) v.findViewById(R.id.tv_follow);
            following = (TextView) v.findViewById(R.id.tv_following);

            group_list.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent_temp = new Intent(v.getContext(), GroupPageActivity.class);
                    posi=getPosition();

                    Bundle bundle = new Bundle();
                    bundle.putString("G_NAME", (String) GroupList.get(posi).getName());
                    bundle.putInt("G_ICON", GroupLogo[posi]);//have to change this
                    bundle.putInt("F_COUNT", GroupList.get(posi).getFollowers().length());
                    bundle.putInt("M_COUNT",GroupList.get(posi).getMembers().length());
                    intent_temp.putExtras(bundle);

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