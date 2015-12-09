
package com.campusconnect.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.campusconnect.R;
import com.campusconnect.activity.InEventActivity;
import com.campusconnect.bean.CampusFeedBean;
import com.campusconnect.communicator.WebRequestTask;
import com.campusconnect.communicator.WebServiceDetails;
import com.campusconnect.constant.AppConstants;
import com.campusconnect.utility.CircularImageView;
import com.campusconnect.utility.SharedpreferenceUtility;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by canopus on 30/11/15.
 */
public class CollegeCampusFeedAdapter extends RecyclerView.Adapter<CollegeCampusFeedAdapter.CollegeCampusFeedViewHolder> {

    public static Typeface r_med, r_reg;
    private List<CampusFeedBean> CollegeFeedList;
    boolean[] flag_news;
    boolean[] flag_attending_clicked, flag_share_clicked;
    int posi = 0;
    int going_click_count = 0;
    int share_click_count = 0;
    Context context;

      /*  CharSequence EventTitles[] = {"Roto Annual Play Auditions", "Spark Session 4", "E-Cell Startup Selection Drive", "NITK Beach Clean-Up Drive", "New Recruits List", "Football Team Tryouts", "Football Team Reaches Quarters of Independence Cup"};
        CharSequence GroupNames[] = {"Rotaract Club", "IE NITK", "E-Cell", "Rotaract Club", "E-Cell", "Football Team", "Football Team"};
        CharSequence Timestamp[] = {"1 day ago", "2 days ago", "Posted 10 days ago", "11 days ago", "Posted 15 days ago", "Posted 20 days ago", "Posted 30 days ago"};
        private static int[] event_photos = new int[]{
                R.mipmap.play_audition,
                R.mipmap.spark_session,
                R.mipmap.cell_event,
                R.mipmap.beach_clean_up,
                R.mipmap.cell_news,
                R.mipmap.football_event,
                R.mipmap.football_news
        };
        CharSequence Day[] = {"MON", "WED", "MON", "SAT", "", "MON", ""};
        CharSequence Date_Month[] = {"26 Oct", "4 Oct", "21 Sep", "19 Sept", "", "14 Sep", ""};
        CharSequence Time_[] = {"5:30 PM", "5:30 PM", "5:30 PM", "7:30 AM", "", "5:30 PM", ""};
        private static int[] GroupLogo = new int[]{
                R.mipmap.roto_logo,
                R.mipmap.ie_logo,
                R.mipmap.cell_logo,
                R.mipmap.roto_logo,
                R.mipmap.cell_logo,
                R.mipmap.football_logo,
                R.mipmap.football_logo
        };
        CharSequence Venue[] = {"Main Building", "ATB Seminar Hall", "MSH", "NITK Beach Entrance", "", "Football Ground", ""};
        CharSequence Description[] = {"Be part of the greatest dramatics show of the college. If you love the stage, you will love to be a part of our show. Open to all.", "SPARK is inspired by TEDx Talks where you will have a chance to interact, ask questions and learn from amazing people from your own campus!", "Have an Idea? or want to Startup? Present your idea to us and you can win access to STEP resources and great mentorship.", "All students are cordially invited for the NITK Beach Clean-Up Drive on September 19th (Saturday) as a part of the 'International Coastal Clean-Up Day' Celebration. This event is being held in association with NSS NITK & the Rotaract Club of our college.\n", "The new recruits are the following:\n14EE201 Abhijay Kumar Pandit\n14ME139 Parikshit\n14EC202 Aditya Nishtala\n14CO132 Prajwal Kailas\n14CH02 Aishwarya Sanjeev Kumar\n14CV136 Pralay S", "Be a part of the football team! Come and show us what you got. Talented and enthusiastic first years are welcome.",
                "The NITK Football team won 2 games to reach the quarter finals of the independence cup. They lost their third game by a small 2-1 margin which put them out of the tournament."};*/

    public CollegeCampusFeedAdapter(List<CampusFeedBean> CollegeFeedList, Context contect) {
        this.CollegeFeedList = CollegeFeedList;
        this.context = contect;
        flag_news = new boolean[getItemCount()];
        flag_attending_clicked = new boolean[getItemCount()];
        flag_share_clicked = new boolean[getItemCount()];
    }

    @Override
    public int getItemCount() {
        return CollegeFeedList.size();
    }

    @Override
    public void onBindViewHolder(CollegeCampusFeedViewHolder college_feedViewHolder, int i) {
        CampusFeedBean cf = CollegeFeedList.get(i);


        college_feedViewHolder.event_title.setText(cf.getTitle());

        college_feedViewHolder.timestamp.setText("" + timeAgo(cf.getTimeStamp()));
        college_feedViewHolder.group_name.setText(cf.getClubid());
        String url = "http://admin.bookieboost.com/admin/images/2015-02-0116-17-50.jpg";
        Picasso.with(context).load(url).into(college_feedViewHolder.group_icon);
        Picasso.with(context).load(url).into(college_feedViewHolder.event_photo);


      /*  Picasso.with(context).load(url).into(college_feedViewHolder.news_icon);*/


           /* "event_creator":"Anirudh",
                    "description":"SAMPLE PHOTO",
                    "views":"0",
                    "photo":"/9j/45c8k3aLbSnKKvzKT2glvt0bVz/9k=",
                    "club_id":"Institute of Engineers",
                    "pid":"5073076857339904",
                    "timestamp":"2017-12-12 11:11:11",
                    "title":"PHOTO OP",
                    "collegeId":"National Institute of Technology Karnataka",
                    "kind":"clubs#resourcesItem"*/




       /* if (encodedImage != null && !encodedImage.isEmpty()) {
            if (encodedImage.equals("None")) {
                college_feedViewHolder.event_photo.setImageResource(R.mipmap.spark_session);
            } else {
                byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                //college_feedViewHolder.event_photo.setImageResource(event_photos[i]);
                college_feedViewHolder.event_photo.setImageBitmap(decodedByte);
            }
        }*/ /*else {*/
        //    college_feedViewHolder.event_photo.setImageResource(R.mipmap.spark_session);


        //news
        if (cf.getAttendees() == null || cf.getAttendees().size() == 0) {
            college_feedViewHolder.day.setVisibility(View.GONE);
            college_feedViewHolder.date_month.setVisibility(View.GONE);
            college_feedViewHolder.time.setVisibility(View.GONE);
            college_feedViewHolder.news_icon.setVisibility(View.VISIBLE);

            flag_news[i]=true;
            college_feedViewHolder.going.setImageResource(R.mipmap.heart);

        } else {
            flag_news[i]=false;
            SimpleDateFormat inFormat = new SimpleDateFormat("dd-MM-yyyy");
            Date date = null;
            try {
                date = inFormat.parse(cf.getStart_date());
                Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
                calendar.setTime(date);


                SimpleDateFormat outFormat = new SimpleDateFormat("EEEE");
                String goal = outFormat.format(date);
                Log.e("day", goal);
                //  Log.e("entry", cf.getStartDate());
                SimpleDateFormat monthFormat = new SimpleDateFormat("MMM");
                String month = monthFormat.format(date);
                Log.e("month", month);

                SimpleDateFormat dateformate = new SimpleDateFormat("dd");
                String dayFormate = monthFormat.format(date);
                Log.e("day", "" + calendar.get(Calendar.DAY_OF_MONTH));
                if(goal.length()>3)
                goal = goal.substring(0, 3);

                college_feedViewHolder.day.setText(goal.toUpperCase());

                String day = "" + calendar.get(Calendar.DAY_OF_MONTH);
                Log.e("day of moth",day);

                if(month.length()>0){
                    month=month.substring(0,3);
                }

                college_feedViewHolder.date_month.setText(day + "" + month);
                college_feedViewHolder.time.setText(cf.getStart_time());


            } catch (ParseException e) {
                e.printStackTrace();
            }

            // college_feedViewHolder.date_month.setText(Date_Month[i]);
            //college_feedViewHolder.time.setText(Time_[i]);
            college_feedViewHolder.news_icon.setVisibility(View.GONE);
            college_feedViewHolder.going.setImageResource(R.mipmap.going);
        }
    }

    @Override
    public CollegeCampusFeedViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.activity_card_layout_college_feed, viewGroup, false);
        return new CollegeCampusFeedViewHolder(itemView);
    }

    public class CollegeCampusFeedViewHolder extends RecyclerView.ViewHolder {

        CardView college_feed;
        TextView event_title, group_name, timestamp, day, date_month, time;
        ImageView event_photo, news_icon, going, share;
        CircularImageView group_icon;

        public CollegeCampusFeedViewHolder(View v) {
            super(v);

            r_med = Typeface.createFromAsset(v.getContext().getAssets(), "font/Roboto_Medium.ttf");
            r_reg = Typeface.createFromAsset(v.getContext().getAssets(), "font/Roboto_Regular.ttf");

            college_feed = (CardView) v.findViewById(R.id.college_feed_card);
            event_title = (TextView) v.findViewById(R.id.tv_event);
            group_name = (TextView) v.findViewById(R.id.tv_group);
            timestamp = (TextView) v.findViewById(R.id.tv_timestamp);
            event_photo = (ImageView) v.findViewById(R.id.iv_event_photo);
            going = (ImageView) v.findViewById(R.id.iv_going);
            share = (ImageView) v.findViewById(R.id.iv_share);
            news_icon = (ImageView) v.findViewById(R.id.iv_news_icon);
            day = (TextView) v.findViewById(R.id.tv_day);
            date_month = (TextView) v.findViewById(R.id.tv_date_month);
            time = (TextView) v.findViewById(R.id.tv_time);
            group_icon = (CircularImageView) v.findViewById(R.id.group_image);

            event_title.setTypeface(r_med);
            group_name.setTypeface(r_reg);
            timestamp.setTypeface(r_reg);

            college_feed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    posi = getAdapterPosition();
                    Intent intent_temp = new Intent(v.getContext(), InEventActivity.class);
                    Bundle bundle = new Bundle();
                    CampusFeedBean bean = CollegeFeedList.get(posi);
                    bundle.putSerializable("BEAN", bean);
                    bundle.putBoolean("FLAG_NEWS", flag_news[posi]);
                    bundle.putBoolean("FLAG_SELECTED_SHARE", flag_share_clicked[posi]);
                    bundle.putBoolean("FLAG_SELECTED_ATTEND/LIKE", flag_attending_clicked[posi]);
                    intent_temp.putExtras(bundle);
                    context.startActivity(intent_temp);
                    String pid = bean.getPid();


                }
            });

            going.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {
                        String persoPid = SharedpreferenceUtility.getInstance(context).getString(AppConstants.PERSON_PID);
                        String pid = CollegeFeedList.get(posi).getPid();
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("eventId", "4713227854282752");
                        jsonObject.put("from_pid", persoPid);
                        WebApiAttending(jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    int pos_for_going = getAdapterPosition();
                    if (flag_attending_clicked[pos_for_going]) {
                        if(flag_news[pos_for_going])
                            going.setImageResource(R.mipmap.heart);
                        else
                            going.setImageResource(R.mipmap.going);
                        flag_attending_clicked[pos_for_going] = false;
                    } else {
                        if(flag_news[pos_for_going])
                            going.setImageResource(R.mipmap.heart_selected);
                        else
                            going.setImageResource(R.mipmap.going_selected);
                        flag_attending_clicked[pos_for_going] = true;
                    }
                }
            });

            share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos_for_share = getAdapterPosition();
                    if(flag_share_clicked[pos_for_share]) {
                        share.setAlpha((float) 0.5);
                        flag_share_clicked[pos_for_share] = false;
                    }
                    else {
                        share.setAlpha((float) 1);
                        flag_share_clicked[pos_for_share] = true;
                    }
                }
            });
        }

        public void WebApiAttending(JSONObject jsonObject) {

            List<NameValuePair> param = new ArrayList<NameValuePair>();
            String url = WebServiceDetails.DEFAULT_BASE_URL + "attendEvent";
            Log.e("", jsonObject.toString());
            Log.e("", url);
            new WebRequestTask(context, param, _handler, WebRequestTask.POST, jsonObject, WebServiceDetails.PID_ATTENDING,
                    true, url).execute();

        }
    }


    public String timeAgo(String createTimeStr) {
        try {
            SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date d = simpleDateFormat.parse(createTimeStr);

//        java.util.Date d = f.parse(createTimeStr);
            String currentDateandTime = f.format(new Date());
            Date d1 = f.parse(currentDateandTime);
            long milliseconds = d.getTime();
            long millisecondsCurrent = d1.getTime();
            long diff_Milli = millisecondsCurrent - milliseconds;
            long minutes = Math.abs((millisecondsCurrent - milliseconds) / 60000);
            long seconds = Math.abs((diff_Milli) / 1000);
            long hours = Math.abs((minutes) / 60);
            long days = Math.abs((hours) / 24);
            long weeks = Math.abs((days) / 7);
            if (days > 7) {
                createTimeStr = String.valueOf(weeks);
                createTimeStr = createTimeStr + " Weeks Ago ";
            } else if (hours > 24) {
                createTimeStr = String.valueOf(days);
                createTimeStr = createTimeStr + " Days Ago ";
            } else if (minutes > 60) {
                createTimeStr = String.valueOf(hours);
                createTimeStr = createTimeStr + " Hours Ago ";
            } else {
                createTimeStr = String.valueOf(minutes);
                createTimeStr = createTimeStr + " Minutes Ago ";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return createTimeStr;

    }

    private final Handler _handler = new Handler() {
        public void handleMessage(Message msg) {
            int response_code = msg.what;
            if (response_code != 0 && response_code != 204) {
                String strResponse = (String) msg.obj;
                Log.v("Response", strResponse);
                if (strResponse != null && strResponse.length() > 0) {
                    switch (response_code) {
                        case WebServiceDetails.PID_ATTENDING: {
                            try {
                                JSONObject jsonObject = new JSONObject(strResponse);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        break;

                        default:
                            break;
                    }
                } else {
                    Toast.makeText(context, "SERVER_ERROR", Toast.LENGTH_LONG).show();
                }
            }
            if (response_code == 204) {
                Toast.makeText(context, "Attending", Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(context, "SERVER_ERROR", Toast.LENGTH_LONG).show();
            }
        }
    };

}

