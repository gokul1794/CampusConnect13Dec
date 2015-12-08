package com.campusconnect.activity;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appspot.campus_connect_2015.clubs.model.ModelsClubRequestMiniForm;
import com.appspot.campus_connect_2015.clubs.Clubs;
import com.campusconnect.R;
import com.campusconnect.communicator.WebRequestTask;
import com.campusconnect.communicator.WebServiceDetails;
import com.campusconnect.constant.AppConstants;
import com.campusconnect.utility.GalleryUtil;
import com.campusconnect.utility.NetworkAvailablity;
import com.campusconnect.utility.SharedpreferenceUtility;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.common.base.Strings;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by RK on 23-09-2015.
 */
public class CreateGroupActivity extends AppCompatActivity {

    TextView create_group_text;
    ImageView upload;
    Button createGroup;
    EditText groupAbbreviation, groupName, groupDescription, groupType;
    LinearLayout close;
    ModelsClubRequestMiniForm cbr;
    private int PICK_IMAGE_REQUEST = 1;
    static SharedPreferences sharedPreferences;
    private String mEmailAccount = "";
    private static final String LOG_TAG = "CreateGroupActivity";


    private final int GALLERY_ACTIVITY_CODE = 200;
    private final int RESULT_CROP = 400;

    String encodedImageStr;

    private boolean isSignedIn() {
        if (!Strings.isNullOrEmpty(mEmailAccount)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        Typeface r_reg = Typeface.createFromAsset(getAssets(), "font/Roboto_Regular.ttf");
        Typeface r_med = Typeface.createFromAsset(getAssets(), "font/Roboto_Medium.ttf");

        close = (LinearLayout) findViewById(R.id.cross_button);

        create_group_text = (TextView)findViewById(R.id.tv_create_group);
        createGroup = (Button) findViewById(R.id.et_createGroup);
        groupType = (EditText) findViewById(R.id.et_group_type);
        groupName = (EditText) findViewById(R.id.et_group_name);
        groupAbbreviation = (EditText) findViewById(R.id.et_group_abbreviation);
        groupDescription = (EditText) findViewById(R.id.et_group_description);
        upload = (ImageView) findViewById(R.id.group_icon_group);

        create_group_text.setTypeface(r_med);
        groupType.setTypeface(r_reg);
        groupName.setTypeface(r_reg);
        groupAbbreviation.setTypeface(r_reg);
        groupDescription.setTypeface(r_reg);

        sharedPreferences = getSharedPreferences(AppConstants.SHARED_PREFS, Context.MODE_PRIVATE);
        mEmailAccount = sharedPreferences.getString(AppConstants.EMAIL_KEY, null);

        createGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webApiCreateGroup();

             /*   SharedPreferences sharedPreferences = v.getContext().getSharedPreferences(AppConstants.SHARED_PREFS, Context.MODE_PRIVATE);
                cbr = new ModelsClubRequestMiniForm();
                cbr.setClubName(groupName.getText().toString());
                cbr.setDescription(groupDescription.getText().toString());
                cbr.setAbbreviation(groupAbbreviation.getText().toString());
                cbr.setFromPid(sharedPreferences.getString(AppConstants.PERSON_PID, null));
                cbr.setCollegeId(sharedPreferences.getString(AppConstants.COLLEGE_ID, null));
                cbr.setPicture(encodedImageStr);
                Log.e("TYPE", groupName.getText().toString());
                createGroup(cbr);*/
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Start Activity To Select Image From Gallery
                Intent gallery_Intent = new Intent(getApplicationContext(), GalleryUtil.class);
                startActivityForResult(gallery_Intent, GALLERY_ACTIVITY_CODE);
                //break;


            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });
    }

//TODO getting error  from the response
    /*Getting server error no respones to show*/
  /*  "abbreviation" -> "tcgcgcgcgc"
            "club_name" -> "dttctctctf"
            "college_id" -> "6231550869897216"
            "from_pid" -> "5702666986455040"
            "description" -> "gcfcfcfcgcgcgv"*/






    public void webApiCreateGroup() {
        try {
            JSONObject jsonObject = new JSONObject();
            String collegeId = SharedpreferenceUtility.getInstance(CreateGroupActivity.this).getString(AppConstants.COLLEGE_ID);
            String pid = SharedpreferenceUtility.getInstance(CreateGroupActivity.this).getString(AppConstants.PERSON_PID);
            //   club_name,club_description,abbreviation,from_pid(profile ID),college_id

            jsonObject.put("club_name", ""+groupName.getText().toString());
            jsonObject.put("description", ""+groupDescription.getText().toString());
            jsonObject.put("abbreviation", ""+groupAbbreviation.getText().toString());
            jsonObject.put("from_pid", ""+pid);
            jsonObject.put("college_id", ""+collegeId);
            Log.e("Json String", jsonObject.toString());

            List<NameValuePair> param = new ArrayList<NameValuePair>();
            String url = WebServiceDetails.DEFAULT_BASE_URL + "club";
            new WebRequestTask(CreateGroupActivity.this, param, _handler, WebRequestTask.POST, jsonObject, WebServiceDetails.PID_CREATE_GROUP,
                    true, url).execute();


        } catch (Exception e) {
            e.printStackTrace();
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
                upload.setImageBitmap(selectedBitmap);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                selectedBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                //Bitmap decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));
                byte[] b = baos.toByteArray();
                encodedImageStr = Base64.encodeToString(b, Base64.DEFAULT);
                upload.setScaleType(ImageView.ScaleType.CENTER);
            }
        }
    }


    public void createGroup(final ModelsClubRequestMiniForm modelsClubRequestMiniForm) {
        //TODO network check

        if (NetworkAvailablity.hasInternetConnection(CreateGroupActivity.this)) {
            if (!isSignedIn()) {
                Toast.makeText(CreateGroupActivity.this, "You must sign in for this action.", Toast.LENGTH_LONG).show();
                return;
            }

            AsyncTask<Void, Void, Void> createGroup =
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... unused) {
                            if (!isSignedIn()) {
                                return null;
                            }
                            ;

                            if (!AppConstants.checkGooglePlayServicesAvailable(CreateGroupActivity.this)) {
                                return null;
                            }

                            // Create a Google credential since this is an authenticated request to the API.
                            GoogleAccountCredential credential = GoogleAccountCredential.usingAudience(
                                    CreateGroupActivity.this, AppConstants.AUDIENCE);
                            credential.setSelectedAccountName(mEmailAccount);

                            // Retrieve service handle using credential since this is an authenticated call.
                            Clubs apiServiceHandle = AppConstants.getApiServiceHandle(credential);

                            try {
                                //ModelsRequestMiniForm modelsRequestMiniForm=new ModelsRequestMiniForm();
                                //modelsClubRequestMiniForm.setAbbreviation(modelsClubRequestMiniForm.getAbbreviation());
                                //modelsClubRequestMiniForm.setFromPid(modelsClubRequestMiniForm.getFromPid());
                                //modelsClubRequestMiniForm.setDescription(modelsClubRequestMiniForm.getDescription());
                                //modelsClubRequestMiniForm.setClubName(modelsClubRequestMiniForm.getClubName());
                                //modelsClubRequestMiniForm.setCollegeId(modelsClubRequestMiniForm.getCollegeId());

                                Clubs.CreateClubRequest res = apiServiceHandle.createClubRequest(modelsClubRequestMiniForm);


                                Void createGroupRes = res.execute();
                                Log.e(LOG_TAG, "SUCCESS");
                                //Log.e(LOG_TAG, createGroupRes.toPrettyString());
                                return createGroupRes;
                            } catch (IOException e) {
                                Log.e(LOG_TAG, "Exception during API call", e);
                            }
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void cList) {
                            if (cList != null) {
                            } else {
                                //Log.e(LOG_TAG, "No clubs were returned by the API.");
                            }
                        }
                    };

            createGroup.execute((Void) null);
        } else {
            Toast.makeText(CreateGroupActivity.this, "Network is not available.", Toast.LENGTH_SHORT).show();
        }

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
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
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
                        case WebServiceDetails.PID_CREATE_GROUP: {
                            try {
                                JSONObject jsonResponse = new JSONObject(strResponse);


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                        default:
                            break;
                    }
                } else {
                    Toast.makeText(CreateGroupActivity.this, "SERVER_ERROR", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(CreateGroupActivity.this, "SERVER_ERROR", Toast.LENGTH_LONG).show();
            }
        }
    };


}
