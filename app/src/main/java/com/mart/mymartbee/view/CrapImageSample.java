package com.mart.mymartbee.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.iceteck.silicompressorr.SiliCompressor;
import com.mart.mymartbee.R;
import com.mart.mymartbee.commons.PathUtil;
import com.mart.mymartbee.commons.CommonMethods;
import com.mart.mymartbee.constants.Constants;
//import com.theartofdev.edmodo.cropper.CropImage;
//import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.net.URISyntaxException;

public class CrapImageSample extends AppCompatActivity implements Constants {

    /*ProgressDialog progressDialog;
    File tempPath, finalPath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAllowFlipping(false)
                .setActivityTitle("Select Image")
                .setCropShape(CropImageView.CropShape.RECTANGLE)
                .setCropMenuCropButtonTitle("Save")
                .setRequestedSize(200, 200)
                .start(CrapImageSample.this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri uri = result.getUri();

                if(uri != null) {

                    try {
                        String filePath = PathUtil.getPath(getApplicationContext(), uri);
                        Log.e("appSample", "File_Path: " + filePath);

                        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
                        File directory = contextWrapper.getDir(getFilesDir().getName(), Context.MODE_PRIVATE);

                        Intent intent = new Intent();
                        intent.putExtra("FilePath", "" + filePath);
                        setResult(CROP_success, intent);
                        finish();

//                        tempPath = new File(directory + "/uploads/");
//                        new ImageCompressionAsyncTask(this, filePath).execute(uri.toString(), "" + tempPath);

                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                CommonMethods.Toast(CrapImageSample.this,  getString(R.string.some_issues_text));
            } else {
                finish();
            }
        }
    }

    class ImageCompressionAsyncTask extends AsyncTask<String, Void, String> {

        Context mContext;
        Uri compressUri = null;
        String filePath;

        public ImageCompressionAsyncTask(Context context, String filePath) {
            mContext = context;
            this.filePath = filePath;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(CrapImageSample.this);
            progressDialog.show();
            progressDialog.setCancelable(false);
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            progressDialog.setContentView(R.layout.progressdialog);
        }

        @Override
        protected String doInBackground(String... params) {
            String filePath = SiliCompressor.with(mContext).compress(params[0], new File(params[1]));
            return filePath;
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
            finalPath = new File(s);
            compressUri = Uri.fromFile(finalPath);
            Log.e("appSample", "FilePath_2: " + s);

            Intent intent = new Intent();
            intent.putExtra("FilePath", "" + s);
            setResult(CROP_success, intent);
            finish();

        }
    }*/
}
