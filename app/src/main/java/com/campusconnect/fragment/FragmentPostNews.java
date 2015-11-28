package com.campusconnect.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appspot.campus_connect_2015.clubs.model.ModelsPostMiniForm;
import com.campusconnect.R;
/**
 * Created by RK on 07-10-2015.
 */
public class FragmentPostNews extends Fragment {
    RelativeLayout group_name_post;
    TextView group_selected_text_post;
    EditText et_title,et_description,et_date,et_time,et_venue,et_tags;
    ModelsPostMiniForm pmf;
    int position;
    final String[] items = {
            "E-Cell", "IE NITK", "Football Team","Rotaract Club"
    };
    private  static final  String LOG_TAG="GetProfileDetails";


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_post_news, container, false);

        group_name_post = (RelativeLayout) v.findViewById(R.id.group_select_when_posting);
        group_selected_text_post = (TextView) v.findViewById(R.id.tv_group_name_selected_when_posting);

        et_title = (EditText) v.findViewById(R.id.et_post_title);
        et_description = (EditText) v.findViewById(R.id.et_post_description);
        et_date = (EditText) v.findViewById(R.id.et_date);
        et_time = (EditText) v.findViewById(R.id.et_time);
        et_tags = (EditText) v.findViewById(R.id.et_tags);

        group_name_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Group:");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        // Do something with the selection
                        position = item;
                        group_selected_text_post.setText(items[item]);
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();


            }
        });

       /* CreatePostActivity.post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pmf = new ModelsPostMiniForm();

                String test = et_title.getText().toString();
                //CreatePostActivity.post.setText(test);
                SharedPreferences
                        sharedPreferences=v.getContext().getSharedPreferences(AppConstants.SHARED_PREFS, Context.MODE_PRIVATE);

                Log.e(LOG_TAG, et_title.getText().toString());
                Log.e(LOG_TAG, et_description.getText().toString());
                Log.e(LOG_TAG, et_date.getText().toString());
                Log.e(LOG_TAG, et_time.getText().toString());
                Log.e(LOG_TAG, et_tags.getText().toString());
                Log.e(LOG_TAG, items[position]);
                //Log.e(LOG_TAG, sharedPreferences.getString(AppConstants.));
                Log.e(LOG_TAG, sharedPreferences.getString(AppConstants.COLLEGE_ID, null));
                Log.e(LOG_TAG, sharedPreferences.getString(AppConstants.PERSON_PID, null));

                //Log.e(LOG_TAG, et_tags.getText().toString());


                pmf.setDate(et_date.getText().toString());
                pmf.setTime(et_time.getText().toString());
                pmf.setTitle(et_title.getText().toString());
                pmf.setDescription(et_description.getText().toString());
                pmf.setClubId(sharedPreferences.getString(AppConstants.COLLEGE_ID, null));
                pmf.setFromPid(sharedPreferences.getString(AppConstants.PERSON_PID, null));
                String clubID = "5109799364591616"; // Remove this
                pmf.setClubId(clubID);




            }
        });
*/



        return v;
    }


}
