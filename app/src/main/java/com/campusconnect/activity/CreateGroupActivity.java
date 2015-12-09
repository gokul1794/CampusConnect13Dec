package com.campusconnect.activity;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
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
import com.campusconnect.R;
import com.campusconnect.communicator.WebRequestTask;
import com.campusconnect.communicator.WebServiceDetails;
import com.campusconnect.constant.AppConstants;
import com.campusconnect.utility.SharedpreferenceUtility;
import com.google.common.base.Strings;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
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
        createGroup.setTypeface(r_reg);
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
              /*  Intent gallery_Intent = new Intent(getApplicationContext(), GalleryUtil.class);
                startActivityForResult(gallery_Intent, GALLERY_ACTIVITY_CODE);*/
                //break;
                uploadImage();

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
            jsonObject.put("from_pid", "5688424874901504");
            jsonObject.put("college_id", ""+collegeId);
            Log.e("Json String", jsonObject.toString());


/*

            {
                "abbreviation": "test",
                    "club_name": "TEST1",
                    "college_id": "5644309118320640",
                    "from_pid": "5688424874901504",
                    "description": "test description"
            }

*/


            List<NameValuePair> param = new ArrayList<NameValuePair>();
            String url = WebServiceDetails.DEFAULT_BASE_URL + "club";
            new WebRequestTask(CreateGroupActivity.this, param, _handler, WebRequestTask.POST, jsonObject, WebServiceDetails.PID_CREATE_GROUP,
                    true, url).execute();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void uploadImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(CreateGroupActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, 0);
                } else if (items[item].equals("Choose from Library")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(intent, "Select File"), 1);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();

    }

    public byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        return stream.toByteArray();
    }

    public String getPath(Uri uri, Activity activity) {
        String[] projection = {MediaStore.MediaColumns.DATA};
        @SuppressWarnings("deprecation")
        Cursor cursor = activity
                .managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Bitmap bitmap = null;

        switch (requestCode) {
            case 0:
                try {
                    bitmap = (Bitmap) data.getExtras().get("data");
                    //  _addPhotoBitmap = bitmap;
                    upload.setImageBitmap(bitmap);
                    upload.setScaleType(ImageView.ScaleType.FIT_XY);

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                    byte[] _byteArray = baos.toByteArray();
                    encodedImageStr = Base64.encodeToString(_byteArray, Base64.DEFAULT);


                } catch (OutOfMemoryError e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 1:
                try {
                    Uri selectedImageUri = data.getData();
                    String tempPath = getPath(selectedImageUri, CreateGroupActivity.this);
                    Bitmap bm;
                    BitmapFactory.Options btmapOptions = new BitmapFactory.Options();
                    bm = BitmapFactory.decodeFile(tempPath, btmapOptions);
                    upload.setImageBitmap(bm);
                    encodedImageStr = Base64.encodeToString(getBytesFromBitmap(bm), Base64.NO_WRAP);

                } catch (OutOfMemoryError e) {
                    e.printStackTrace();
                    Toast.makeText(CreateGroupActivity.this, "Image size is too large.Please upload small image.", Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
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
            if(response_code!=0) {
                if (response_code !=204) {
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
                }else{
                    Toast.makeText(CreateGroupActivity.this, "Your group request has been sent for approval.", Toast.LENGTH_LONG).show();
                    finish();
                }
            }else {

                Toast.makeText(CreateGroupActivity.this, "SERVER_ERROR", Toast.LENGTH_LONG).show();

            }
        }
    };


}
