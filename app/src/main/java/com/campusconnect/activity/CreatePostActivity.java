package com.campusconnect.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.appspot.campus_connect_2015.clubs.Clubs;
import com.appspot.campus_connect_2015.clubs.model.ModelsClubListResponse;
import com.appspot.campus_connect_2015.clubs.model.ModelsClubMiniForm;
import com.appspot.campus_connect_2015.clubs.model.ModelsClubRetrievalMiniForm;
import com.appspot.campus_connect_2015.clubs.model.ModelsEventMiniForm;
import com.appspot.campus_connect_2015.clubs.model.ModelsMessageResponse;
import com.appspot.campus_connect_2015.clubs.model.ModelsPostMiniForm;
import com.campusconnect.R;
import com.campusconnect.bean.GroupBean;
import com.campusconnect.communicator.WebRequestTask;
import com.campusconnect.communicator.WebServiceDetails;
import com.campusconnect.constant.AppConstants;
import com.campusconnect.slidingtab.SlidingTabLayout_CreatePost;
import com.campusconnect.utility.GalleryUtil;
import com.campusconnect.utility.NetworkAvailablity;
import com.campusconnect.utility.SharedpreferenceUtility;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.common.base.Strings;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by RK on 07-10-2015.
 */
public class CreatePostActivity extends AppCompatActivity {

    private static final String LOG_TAG = "CreatePostActivity";
    TextView create_post_title;
    ViewPager pager;
    LinearLayout close;
    ViewPagerAdapter_CreatePost adapter;
    SlidingTabLayout_CreatePost tabs;
    //   public static Button  post;
    CharSequence Titles[] = {"Event", "News"};
    public ArrayList<GroupBean> groupList = new ArrayList<GroupBean>();
    List<ModelsClubMiniForm> modelsClubMiniForms;

    int flag_coming_from_group_page=0;
    String group_name_from_group_page;

    static SharedPreferences sharedPreferences;
    private String mEmailAccount = "";

    int Numboftabs = 2;

    Typeface r_med;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        r_med = Typeface.createFromAsset(getAssets(), "font/Roboto_Medium.ttf");

        close = (LinearLayout) findViewById(R.id.cross_button);
        create_post_title = (TextView) findViewById(R.id.tv_create_post);
        create_post_title.setTypeface(r_med);

        pager = (ViewPager) findViewById(R.id.pager);
        tabs = (SlidingTabLayout_CreatePost) findViewById(R.id.tabs_createpost);
        adapter = new ViewPagerAdapter_CreatePost(getSupportFragmentManager(), Titles, Numboftabs, CreatePostActivity.this);

        pager.setAdapter(adapter);

        tabs.setDistributeEvenly(true);
        tabs.setViewPager(pager);
        sharedPreferences = getSharedPreferences(AppConstants.SHARED_PREFS, Context.MODE_PRIVATE);
        mEmailAccount = sharedPreferences.getString(AppConstants.EMAIL_KEY, null);

        Bundle bun = getIntent().getExtras();
        if(bun!=null) {
            group_name_from_group_page = bun.getString("G_NAME");
            flag_coming_from_group_page = bun.getInt("FLAG");
        }

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });

    }

    //TODO DONE getGroup in createpostActivity
    public void webApiGetGroups() {
        try {
            String collegeId = SharedpreferenceUtility.getInstance(CreatePostActivity.this).getString(AppConstants.COLLEGE_ID);
            String pid = SharedpreferenceUtility.getInstance(CreatePostActivity.this).getString(AppConstants.PERSON_PID);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("college_id", collegeId);
            jsonObject.put("pid", pid);
            List<NameValuePair> param = new ArrayList<NameValuePair>();
            String url = WebServiceDetails.DEFAULT_BASE_URL + "getClubList";
            new WebRequestTask(CreatePostActivity.this, param, _handler, WebRequestTask.POST, jsonObject, WebServiceDetails.PID_GET_GROUPS,
                    true, url).execute();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void getGroups() {
        //TODO network check
        if (NetworkAvailablity.hasInternetConnection(CreatePostActivity.this)) {
            if (!isSignedIn()) {
                Toast.makeText(CreatePostActivity.this, "You must sign in for this action.", Toast.LENGTH_LONG).show();
                return;
            }

            AsyncTask<Void, Void, ModelsClubListResponse> getClubsAndPopulate =
                    new AsyncTask<Void, Void, ModelsClubListResponse>() {
                        @Override
                        protected ModelsClubListResponse doInBackground(Void... unused) {
                            if (!isSignedIn()) {
                                return null;
                            }
                            ;

                            if (!AppConstants.checkGooglePlayServicesAvailable(CreatePostActivity.this)) {
                                return null;
                            }

                            // Create a Google credential since this is an authenticated request to the API.
                            GoogleAccountCredential credential = GoogleAccountCredential.usingAudience(
                                    CreatePostActivity.this, AppConstants.AUDIENCE);
                            credential.setSelectedAccountName(mEmailAccount);

                            // Retrieve service handle using credential since this is an authenticated call.
                            Clubs apiServiceHandle = AppConstants.getApiServiceHandle(credential);

                            try {
                                ModelsClubRetrievalMiniForm clubRetrievalMiniForm = new ModelsClubRetrievalMiniForm();
                                clubRetrievalMiniForm.setCollegeId(sharedPreferences.getString(AppConstants.COLLEGE_ID, "null"));
                                Clubs.GetClubList gcl = apiServiceHandle.getClubList(clubRetrievalMiniForm);
                                ModelsClubListResponse clubListResponse = gcl.execute();
                                Log.e(LOG_TAG, "SUCCESS");
                                Log.e(LOG_TAG, clubListResponse.toPrettyString());
                                return clubListResponse;
                            } catch (IOException e) {
                                Log.e(LOG_TAG, "Exception during API call", e);
                            }
                            return null;
                        }

                        @Override
                        protected void onPostExecute(ModelsClubListResponse cList) {
                            if (cList != null) {
                                try {
                                    Log.e(LOG_TAG, cList.toPrettyString());
                                    modelsClubMiniForms = displayClubs(cList);
                                    Log.e(LOG_TAG, modelsClubMiniForms.toString());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                Log.e(LOG_TAG, "No clubs were returned by the API.");
                            }
                        }
                    };

            getClubsAndPopulate.execute((Void) null);
        } else {
            Toast.makeText(CreatePostActivity.this, "Network is not available.", Toast.LENGTH_SHORT).show();
        }
    }


    private List<ModelsClubMiniForm> displayClubs(ModelsClubListResponse... response) {
        Log.e(LOG_TAG, response.toString());

        if (response == null || response.length < 1) {
            return null;
        } else {
            Log.d(LOG_TAG, "Displaying " + response.length + " colleges.");
            List<ModelsClubListResponse> clubList = Arrays.asList(response);
            return clubList.get(0).getList();
        }
    }

    private boolean isSignedIn() {
        if (!Strings.isNullOrEmpty(mEmailAccount)) {
            return true;
        } else {
            return false;
        }
    }


    public void webApiCreatePost(JSONObject jsonObject) {
        try {
/*
            "date" -> "2015-11-28"
            "description" ->

            "from_pid" -> "5702666986455040"
            "photo" ->
            "time" -> "04:29:51"
            "title" ->*/

          /*  from_pid,club_id,title,description,likers(not compulsory),date(eg 2012-02-12),time(11:12:12)*/

            List<NameValuePair> param = new ArrayList<NameValuePair>();
            String url = WebServiceDetails.DEFAULT_BASE_URL + "eventEntry";
            new WebRequestTask(CreatePostActivity.this, param, _handler, WebRequestTask.POST, jsonObject, WebServiceDetails.PID_CREATE_POST,
                    true, url).execute();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void createPost(ModelsPostMiniForm postMiniForm) {
        if (!isSignedIn()) {
            Toast.makeText(CreatePostActivity.this, "You must sign in for this action.", Toast.LENGTH_LONG).show();
            return;
        }

        AsyncTask<ModelsPostMiniForm, Void, ModelsMessageResponse> createFeed =
                new AsyncTask<ModelsPostMiniForm, Void, ModelsMessageResponse>() {


                    @Override
                    protected ModelsMessageResponse doInBackground(ModelsPostMiniForm... params) {
                        if (!isSignedIn()) {
                            return null;
                        }
                        ;

                        if (!AppConstants.checkGooglePlayServicesAvailable(CreatePostActivity.this)) {
                            return null;
                        }

                        // Create a Google credential since this is an authenticated request to the API.
                        GoogleAccountCredential credential = GoogleAccountCredential.usingAudience(
                                CreatePostActivity.this, AppConstants.AUDIENCE);
                        credential.setSelectedAccountName(mEmailAccount);
                      //   createpost methode
                        // Retrieve service handle using credential since this is an authenticated call.
                        try {
                            Clubs apiServiceHandle = AppConstants.getApiServiceHandle(credential);
                            Clubs.PostEntry postEntry = apiServiceHandle.postEntry(params[0]);
                            //Log.e(LOG_TAG+"123",params[0].toPrettyString());
                            ModelsMessageResponse res = postEntry.execute();
                            Log.e(LOG_TAG, "SUCCESS");
                            return res;
//                            Log.e(LOG_TAG, res.toString());
                        } catch (IOException e) {
                            Log.e(LOG_TAG, "Exception during API call", e);
                        }
                        return null;

                    }


                };
        createFeed.execute((ModelsPostMiniForm) postMiniForm);
    }


    public void webApiCreateEvent(JSONObject jsonObject) {


        List<NameValuePair> param = new ArrayList<NameValuePair>();
        String url = WebServiceDetails.DEFAULT_BASE_URL + "eventEntry";
        new WebRequestTask(CreatePostActivity.this, param, _handler, WebRequestTask.POST, jsonObject, WebServiceDetails.PID_CREATE_EVENT,
                true, url).execute();


    }


    public void createEvent(ModelsEventMiniForm eventMiniForm) {
        if (!isSignedIn()) {
            Toast.makeText(CreatePostActivity.this, "You must sign in for this action.", Toast.LENGTH_LONG).show();
            return;
        }
        //set network
        AsyncTask<ModelsEventMiniForm, Void, Void> createEvent =
                new AsyncTask<ModelsEventMiniForm, Void, Void>() {


                    @Override
                    protected Void doInBackground(ModelsEventMiniForm... params) {
                        if (!isSignedIn()) {
                            return null;
                        }
                        ;

                        if (!AppConstants.checkGooglePlayServicesAvailable(CreatePostActivity.this)) {
                            return null;
                        }

                        // Create a Google credential since this is an authenticated request to the API.
                        GoogleAccountCredential credential = GoogleAccountCredential.usingAudience(
                                CreatePostActivity.this, AppConstants.AUDIENCE);
                        credential.setSelectedAccountName(mEmailAccount);

                        // Retrieve service handle using credential since this is an authenticated call.
                        try {
                            //create event calling

                            Clubs apiServiceHandle = AppConstants.getApiServiceHandle(credential);
                            Clubs.EventEntry eventEntry = apiServiceHandle.eventEntry(params[0]);
                            //Log.e(LOG_TAG+"123",params[0].toPrettyString());
                            ModelsMessageResponse res = eventEntry.execute();
                            Log.e(LOG_TAG, "SUCCESS");
                            //Toast.makeText(CreatePostActivity.this, "SUCCESS", Toast.LENGTH_LONG).show();

                            Log.e(LOG_TAG, res.toString());
                        } catch (IOException e) {
                            Log.e(LOG_TAG, "Exception during API call", e);
                        }
                        return null;
                    }


                };

        createEvent.execute((ModelsEventMiniForm) eventMiniForm);
    }

    public class ViewPagerAdapter_CreatePost extends FragmentPagerAdapter {

        CharSequence Titles[]; // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapter_home is created
        int NumbOfTabs; // Store the number of tabs, this will also be passed when the ViewPagerAdapter_home is created

        private Context mContext;

        // Build a Constructor and assign the passed Values to appropriate values in the class
        public ViewPagerAdapter_CreatePost(FragmentManager fm, CharSequence mTitles[], int mNumbOfTabsumb, Context context) {
            super(fm);
            this.Titles = mTitles;
            this.NumbOfTabs = mNumbOfTabsumb;
            this.mContext = context;
        }

        //This method return the fragment for the every position in the View Pager
        @Override
        public Fragment getItem(int position) {

            //TODO removed old api here
           // webApiGetGroups();
              CreatePostActivity.this.getGroups();
            Fragment fragment=null;
            if (position == 0) {
                fragment= new FragmentPostEvent();
                return fragment;
            } else if (position == 1) {
                fragment= new FragmentPostNews();
                return fragment;
            }
            return fragment;

        }


        @Override
        public CharSequence getPageTitle(int position) {
            return Titles[position];
        }


        // This method return the Number of tabs for the tabs Strip

        @Override
        public int getCount() {
            return NumbOfTabs;
        }
    }


    public class FragmentPostNews extends Fragment {
        RelativeLayout group_name_post;
        TextView group_selected_text_post;
        EditText et_title,et_post_description, et_tags;
        ImageView iv_upload;
        Button post;
        ModelsPostMiniForm pmf = new ModelsPostMiniForm();
        int position;
        String encodedImageStr = "";

        private final int GALLERY_ACTIVITY_CODE = 200;
        private final int RESULT_CROP = 400;

        private static final String LOG_TAG = "CreatePostActivity";


        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.fragment_post_news, container, false);

            Typeface r_reg = Typeface.createFromAsset(v.getContext().getAssets(), "font/Roboto_Regular.ttf");

            group_name_post = (RelativeLayout) v.findViewById(R.id.group_select_when_posting);
            group_selected_text_post = (TextView) v.findViewById(R.id.tv_group_name_selected_when_posting);

            et_title = (EditText) v.findViewById(R.id.et_post_title);
            et_post_description = (EditText) v.findViewById(R.id.et_post_description);
            //et_date = (EditText) v.findViewById(R.id.et_date);
            //et_time = (EditText) v.findViewById(R.id.et_time);
            et_tags = (EditText) v.findViewById(R.id.et_tags);
            iv_upload = (ImageView) v.findViewById(R.id.iv_upload);
            post = (Button)v.findViewById(R.id.b_post);

            et_title.setTypeface(r_reg);
            et_post_description.setTypeface(r_reg);
            et_tags.setTypeface(r_reg);
            group_selected_text_post.setTypeface(r_reg);
            post.setTypeface(r_reg);

            Toast.makeText(getActivity(), "Fragment post news", Toast.LENGTH_LONG).show();
            //et_date.setText("bsjbfjdskfjdsfkbdsk");
            group_name_post.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder.setTitle("Group:");
                    if (CreatePostActivity.this.modelsClubMiniForms == null) {
                        group_selected_text_post.setText("Loading Groups");
                    } else {
                        String[] groupList = new String[CreatePostActivity.this.modelsClubMiniForms.size()];
                        for (int i = 0; i < modelsClubMiniForms.size(); i++) {
                            groupList[i] = modelsClubMiniForms.get(i).getAbbreviation();
                        }
                        builder.setItems(groupList, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {
                                // Do something with the selection
                                position = item;
                                group_selected_text_post.setText(CreatePostActivity.this.modelsClubMiniForms.get(position).getAbbreviation());
                                pmf.setClubId(CreatePostActivity.this.modelsClubMiniForms.get(position).getClubId());
                                Log.e(LOG_TAG + "CLUB", pmf.getClubId() + " " + group_selected_text_post);
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }


                }
            });

            iv_upload.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    //Start Activity To Select Image From Gallery
                    Intent gallery_Intent = new Intent(getApplicationContext(), GalleryUtil.class);
                    startActivityForResult(gallery_Intent, GALLERY_ACTIVITY_CODE);
                    //break;
                }
            });


            post.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO network Check
                    if (NetworkAvailablity.hasInternetConnection(CreatePostActivity.this)) {
                        String test = et_title.getText().toString();
                        //CreatePostActivity.post.setText(test);
                        SharedPreferences
                                sharedPreferences = v.getContext().getSharedPreferences(AppConstants.SHARED_PREFS, Context.MODE_PRIVATE);

/*
                        Date cDate = new Date();
                        String date = new SimpleDateFormat("yyyy-MM-dd").format(cDate);
                        String time = new SimpleDateFormat("hh:mm:ss").format(cDate);


                        Log.e(LOG_TAG, "" + date);
                        Log.e(LOG_TAG, "" + time);
*/
                        Log.e(LOG_TAG, "" + et_title.getText().toString());
                        Log.e(LOG_TAG, "" + et_post_description.getText().toString());

                        //Log.e(LOG_TAG, "" + et_date.getText().toString());
                        //Log.e(LOG_TAG, "" + et_time.getText().toString());
                        Log.e(LOG_TAG, "" + et_tags.getText().toString());
                        Log.e(LOG_TAG, "" + sharedPreferences.getString(AppConstants.COLLEGE_ID, null));
                        Log.e(LOG_TAG, "" + sharedPreferences.getString(AppConstants.PERSON_PID, null));

                        Log.e(LOG_TAG, "" + pmf.getClubId());
/*
                        pmf.setDate(date);
                        pmf.setTime(time);
*/
                        pmf.setTitle(et_title.getText().toString());
                        pmf.setDescription(et_post_description.getText().toString());
                        pmf.setPhoto(encodedImageStr);
                        pmf.setFromPid(sharedPreferences.getString(AppConstants.PERSON_PID, null));


                        try {
                            String pid = SharedpreferenceUtility.getInstance(CreatePostActivity.this).getString(AppConstants.PERSON_PID);
                            JSONObject jsonObject = new JSONObject();
                          //  jsonObject.put("time", time);
                          //  jsonObject.put("date", date);
                            jsonObject.put("title", et_title.getText().toString());
                            jsonObject.put("description", et_post_description.getText().toString());
                            jsonObject.put("photo", encodedImageStr);
                            jsonObject.put("from_pid", pid);
                            jsonObject.put("clud_id", pid);

                            //    webApiCreatePost(jsonObject);


                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                        CreatePostActivity.this.createPost(pmf);
                    } else {
                        Toast.makeText(CreatePostActivity.this, "Network is not available.", Toast.LENGTH_SHORT).show();
                    }
                }
            });


            return v;
        }


        private void performCrop(String picUri) {
            try {
                //Start Crop Activity

                Intent cropIntent = new Intent("com.android.camera.action.CROP");
                // indicate image type and Uri
                File f = new File(picUri);
                Uri contentUri = Uri.fromFile(f);

                cropIntent.setDataAndType(contentUri, "image/*");
                // set crop properties
                cropIntent.putExtra("crop", "true");
                // indicate aspect of desired crop
                cropIntent.putExtra("aspectX", 1);
                cropIntent.putExtra("aspectY", 1);
                // indicate output X and Y
                cropIntent.putExtra("outputX", 280);
                cropIntent.putExtra("outputY", 280);

                // retrieve data on return
                cropIntent.putExtra("return-data", true);
                // start the activity - we handle returning in onActivityResult
                startActivityForResult(cropIntent, RESULT_CROP);
            }
            // respond to users whose devices do not support the crop action
            catch (ActivityNotFoundException anfe) {
                // display an error message
                String errorMessage = "your device doesn't support the crop action!";
                Toast toast = Toast.makeText(CreatePostActivity.this, errorMessage, Toast.LENGTH_SHORT);
                toast.show();
            }
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == GALLERY_ACTIVITY_CODE) {
                if (resultCode == Activity.RESULT_OK) {
                    String picturePath = data.getStringExtra("picturePath");
                    //perform Crop on the Image Selected from Gallery
                    performCrop(picturePath);
                }
            }

            if (requestCode == RESULT_CROP) {
                if (resultCode == Activity.RESULT_OK) {
                    Bundle extras = data.getExtras();
                    Bitmap selectedBitmap = extras.getParcelable("data");
                    // Set The Bitmap Data To ImageView
                    iv_upload.setImageBitmap(selectedBitmap);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    selectedBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                    //Bitmap decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));
                    byte[] b = baos.toByteArray();
                    encodedImageStr = Base64.encodeToString(b, Base64.DEFAULT);
                    iv_upload.setScaleType(ImageView.ScaleType.CENTER);
                }
            }
        }
    }

    public class FragmentPostEvent extends Fragment {

        RelativeLayout group_name_post;
        TextView group_selected_text_post;
        ModelsEventMiniForm eventMiniForm = new ModelsEventMiniForm();
        EditText et_title,et_post_description,et_date,et_time,et_venue,et_tags;
        TextView s_date, e_date, s_time, e_time;
        ImageView dropdown_indicator;
        int position;
        Button post;

        Calendar myCalendar_s_date, myCalendar_e_date;
        Context context;
        DatePickerDialog.OnDateSetListener start_date, end_date;
        int start_hour, start_min;

        String test;

        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.fragment_post_event, container, false);

            Typeface r_reg = Typeface.createFromAsset(v.getContext().getAssets(), "font/Roboto_Regular.ttf");

            context = v.getContext();

            group_name_post = (RelativeLayout) v.findViewById(R.id.group_select_when_posting);
            group_selected_text_post = (TextView) v.findViewById(R.id.tv_group_name_selected_when_posting);

            et_title = (EditText) v.findViewById(R.id.et_post_title);
            et_post_description = (EditText) v.findViewById(R.id.et_post_description);
            s_date = (TextView) v.findViewById(R.id.et_start_date);
            e_date = (TextView) v.findViewById(R.id.et_end_date);
            s_time = (TextView) v.findViewById(R.id.et_start_time);
            e_time = (TextView) v.findViewById(R.id.et_end_time);
            et_tags = (EditText) v.findViewById(R.id.et_tags);
            dropdown_indicator = (ImageView) v.findViewById(R.id.iv_downarrow);
            post = (Button) v.findViewById(R.id.b_post);

            et_title.setTypeface(r_reg);
            et_post_description.setTypeface(r_reg);
            s_date.setTypeface(r_reg);
            e_date.setTypeface(r_reg);
            s_time.setTypeface(r_reg);
            e_time.setTypeface(r_reg);
            et_tags.setTypeface(r_reg);
            group_selected_text_post.setTypeface(r_reg);
            post.setTypeface(r_reg);

            Toast.makeText(getActivity(), "Fragment post", Toast.LENGTH_LONG).show();

            if(flag_coming_from_group_page==0) {
            group_name_post.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder.setTitle("Group:");
                    if (CreatePostActivity.this.modelsClubMiniForms == null) {
                        group_selected_text_post.setText("Loading Groups");
                    } else {
                        String[] groupList = new String[CreatePostActivity.this.modelsClubMiniForms.size()];
                        for (int i = 0; i < modelsClubMiniForms.size(); i++) {
                            groupList[i] = modelsClubMiniForms.get(i).getAbbreviation();
                        }
                        builder.setItems(groupList, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {
                                position = item;
                                group_selected_text_post.setText(CreatePostActivity.this.modelsClubMiniForms.get(position).getAbbreviation());
                                eventMiniForm.setClubId(CreatePostActivity.this.modelsClubMiniForms.get(position).getClubId());
                                Log.e(LOG_TAG + "CLUB", eventMiniForm.getClubId() + " " + group_selected_text_post);
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                }
            });
            }
            else
            {
                flag_coming_from_group_page = 0;
                group_selected_text_post.setText(group_name_from_group_page);
                dropdown_indicator.setVisibility(View.GONE);
            }



            post.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    //TODO network check
                    if (NetworkAvailablity.hasInternetConnection(CreatePostActivity.this)) {
                        String test = et_title.getText().toString();
                        SharedPreferences
                                sharedPreferences = v.getContext().getSharedPreferences(AppConstants.SHARED_PREFS, Context.MODE_PRIVATE);


                        Date cDate = new Date();
                        String date = new SimpleDateFormat("yyyy-MM-dd").format(cDate);
                        String time = new SimpleDateFormat("hh:mm:ss").format(cDate);


                        Log.e(LOG_TAG,""+ date);
                        Log.e(LOG_TAG,""+ time);
                        Log.e(LOG_TAG,""+ et_title.getText().toString());
                        Log.e(LOG_TAG, ""+et_post_description.getText().toString());

                        Log.e(LOG_TAG,""+ sharedPreferences.getString(AppConstants.COLLEGE_ID, null));
                        Log.e(LOG_TAG,""+ sharedPreferences.getString(AppConstants.PERSON_PID, null));

                        Log.e(LOG_TAG,""+ eventMiniForm.getClubId());


                        //eventMiniForm.setClubId();
                        //eventMiniForm.setCompleted();
                        //eventMiniForm.setAttendees();
                        eventMiniForm.setDate(date);//timestamp
                        eventMiniForm.setTime(time);//timestamp
                        eventMiniForm.setDescription(et_post_description.getText().toString());
                        //eventMiniForm.setStartDate();
                        //eventMiniForm.setStartTime();
                        //eventMiniForm.setEndDate();
                        //eventMiniForm.setEndTime();
                        eventMiniForm.setEventCreator(sharedPreferences.getString(AppConstants.PERSON_PID, null));
                        //eventMiniForm.setTags();
                        eventMiniForm.setIsAlumni(sharedPreferences.getString(AppConstants.ALUMNI, null));
                         //eventMiniForm.setTitle();
                        //eventMiniForm.setVenue();
                        //eventMiniForm.set
                        try {

                            /*title,
                            description
                            ,clubId
                            ,views(not compulsory)
                            ,event_creator
                            ,venue,date
                            ,time,
                            ,start_time
                            ,start_date
                            ,start_date
                            ,end_time,
                            attendees(PID)
                            ,tags
                            ,views*/
                            String pid = SharedpreferenceUtility.getInstance(getActivity()).getString(AppConstants.PERSON_PID);
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("title", et_title.getText().toString());
                            jsonObject.put("description", ""+et_post_description.getText().toString());
                            jsonObject.put("clubId", ""+eventMiniForm.getClubId());
                            jsonObject.put("views", "");
                            jsonObject.put("event_creator", ""+pid);
                            jsonObject.put("venue", "indore");
                            jsonObject.put("date", "11-12-2015");
                            jsonObject.put("time", "12:25 Am");
                            jsonObject.put("start_time", "12:25 Am");
                            jsonObject.put("start_date", "12-12-2015");
                            jsonObject.put("end_time", "");
                            jsonObject.put("attendees", "225315151");
                            jsonObject.put("tags", "gfhgfhfgh");
                            jsonObject.put("views", "fghfghfgfg");



                            webApiCreateEvent(jsonObject);

                        } catch (Exception e) {

                        }
                        CreatePostActivity.this.createEvent(eventMiniForm);
                    } else {
                        Toast.makeText(CreatePostActivity.this, "Network is not available.", Toast.LENGTH_SHORT).show();
                    }
                }
            });


            myCalendar_s_date = Calendar.getInstance();
            myCalendar_e_date = Calendar.getInstance();
            start_date = new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear,
                                      int dayOfMonth) {
                    // TODO Auto-generated method stub
                    myCalendar_s_date.set(Calendar.YEAR, year);
                    myCalendar_s_date.set(Calendar.MONTH, monthOfYear);
                    myCalendar_s_date.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    updateLabel_start();
                }

            };

            end_date = new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear,
                                      int dayOfMonth) {
                    // TODO Auto-generated method stub
                    myCalendar_e_date.set(Calendar.YEAR, year);
                    myCalendar_e_date.set(Calendar.MONTH, monthOfYear);
                    myCalendar_e_date.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    updateLabel_end();
                }
            };

            s_date.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    DatePickerDialog start_date_picker = new DatePickerDialog(context, start_date, myCalendar_s_date
                            .get(Calendar.YEAR), myCalendar_s_date.get(Calendar.MONTH),
                            myCalendar_s_date.get(Calendar.DAY_OF_MONTH));

                    start_date_picker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                    start_date_picker.show();
                }
            });

            e_date.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    DatePickerDialog end_date_picker = new DatePickerDialog(context, end_date, myCalendar_e_date
                            .get(Calendar.YEAR), myCalendar_e_date.get(Calendar.MONTH),
                            myCalendar_e_date.get(Calendar.DAY_OF_MONTH));
                    end_date_picker.getDatePicker().setMinDate(System.currentTimeMillis() + 1000);
                    end_date_picker.show();
                }
            });

            s_time.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Calendar mcurrentTime = Calendar.getInstance();
                    final int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                    final int minute = mcurrentTime.get(Calendar.MINUTE);
                    TimePickerDialog mTimePicker;
                    mTimePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                            start_hour = selectedHour;
                            start_min = selectedMinute;
                            if (selectedHour > hour)
                                s_time.setText("" + selectedHour + ":" + selectedMinute);
                            else if (selectedHour == hour) {
                                if (selectedMinute > minute)
                                    s_time.setText("" + selectedHour + ":" + selectedMinute);
                                else
                                    Toast.makeText(getActivity().getApplicationContext(), "The start time you entered occurred before the current time.",
                                            Toast.LENGTH_SHORT).show();
                            } else
                                Toast.makeText(getActivity().getApplicationContext(), "The start time you entered occurred before the current time.",
                                        Toast.LENGTH_SHORT).show();

                        }
                    }, hour, minute, true);
                    mTimePicker.setTitle("Select Time");
                    mTimePicker.show();

                }
            });

            e_time.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Calendar mcurrentTime = Calendar.getInstance();
                    final int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                    final int minute = mcurrentTime.get(Calendar.MINUTE);
                    TimePickerDialog mTimePicker;
                    mTimePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                            if(selectedHour>start_hour)
                                e_time.setText("" + selectedHour + ":" + selectedMinute);
                            else if(selectedHour==start_hour){
                                if(selectedMinute>start_min)
                                    e_time.setText("" + selectedHour + ":" + selectedMinute);
                                else
                                    Toast.makeText(getActivity().getApplicationContext(), "The end time you entered occurred before the start time.",
                                            Toast.LENGTH_SHORT).show();
                            }
                            else
                                Toast.makeText(getActivity().getApplicationContext(), "The end time you entered occurred before the start time.",
                                        Toast.LENGTH_SHORT).show();
                        }
                    }, hour, minute, true);
                    mTimePicker.setTitle("Select Time");
                    mTimePicker.show();

                }
            });


            return v;
        }

        private void updateLabel_start() {

            String myFormat = "MM/dd/yy"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

            if(Calendar.getInstance().get(Calendar.DAY_OF_MONTH) > myCalendar_s_date.get(Calendar.DAY_OF_MONTH)) {
                s_date.setText("");
                Toast.makeText(getActivity().getApplicationContext(), "The start date you entered occurred before the current date.",
                        Toast.LENGTH_SHORT).show();
            }
            else
                s_date.setText(sdf.format(myCalendar_s_date.getTime()));

        }
        private void updateLabel_end() {

            String myFormat = "MM/dd/yy"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            if(myCalendar_s_date.get(Calendar.DAY_OF_MONTH) > myCalendar_e_date.get(Calendar.DAY_OF_MONTH)) {
                e_date.setText("");
                Toast.makeText(getActivity().getApplicationContext(), "The end date you entered occurred before the start date.",
                        Toast.LENGTH_SHORT).show();
            }
            else
                e_date.setText(sdf.format(myCalendar_e_date.getTime()));
        }


    }


    private final Handler _handler = new Handler() {
        public void handleMessage(Message msg) {
            int response_code = msg.what;
            if (response_code != 0) {
                String strResponse = (String) msg.obj;
                Log.v("Response", strResponse);
                if (strResponse != null && strResponse.length() > 0) {
                    switch (response_code) {

                        case WebServiceDetails.PID_GET_GROUPS: {
                            try {
                                groupList.clear();
                                JSONObject grpJson = new JSONObject(strResponse);
                                if (grpJson.has("list")) {
                                    JSONArray grpArray = grpJson.getJSONArray("list");
                                    for (int i = 0; i < grpArray.length(); i++) {

                                        JSONObject innerGrpObj = grpArray.getJSONObject(i);
                                        String description = innerGrpObj.optString("description");
                                        String admin = innerGrpObj.optString("admin");
                                        String clubId = innerGrpObj.optString("club_id");
                                        String abb = innerGrpObj.optString("abbreviation");
                                        String name = innerGrpObj.optString("name");

                                        GroupBean bean = new GroupBean();
                                        bean.setAbb(abb);
                                        bean.setName(name);
                                        bean.setAdmin(admin);
                                        bean.setClubId(clubId);
                                        bean.setDescription(description);
                                        groupList.add(bean);
                                    }

                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                        case WebServiceDetails.PID_CREATE_POST: {


                        }
                        break;
                        case WebServiceDetails.PID_CREATE_EVENT:

                            break;
                        default:
                            break;
                    }
                } else {
                    Toast.makeText(CreatePostActivity.this, "SERVER_ERROR", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(CreatePostActivity.this, "SERVER_ERROR", Toast.LENGTH_LONG).show();
            }
        }
    };

}
