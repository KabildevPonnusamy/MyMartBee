package com.mart.mymartbee.commons;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.location.Location;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.BaseColumns;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
//import com.latticegulf.louvre.screens.reciver.MyReceiver;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.SortedSet;
import java.util.TimeZone;
import java.util.TreeSet;

import javax.crypto.SecretKey;

/**
 * Created by latticegulf on 9/21/16.
 */
public class Util {

    Activity activity;
    Dialog dialog;

    public static String screensize(Activity activity1) {
        Activity activity = activity1;

        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        double x = Math.pow(dm.widthPixels / dm.xdpi, 2);
        double y = Math.pow(dm.heightPixels / dm.ydpi, 2);
        double screenInches = Math.sqrt(x + y);
        int a = (int) Math.round(screenInches);
        String b = String.valueOf(a);

        return b;
    }

    public static View screen(Activity activity1, View view, int size) {
        Activity activity = activity1;

        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        double x = Math.pow(dm.widthPixels / dm.xdpi, 2);
        double y = Math.pow(dm.heightPixels / dm.ydpi, 2);
        double screenInches = Math.sqrt(x + y);
        int a = (int) Math.round(screenInches);
        String b = String.valueOf(a);

        return view;
    }

    @SuppressLint("MissingPermission")
    public static String deviceId(Activity activity) {

        String a = Settings.Secure.getString(activity.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        /*TelephonyManager telephonyManager = (TelephonyManager) activity.getSystemService(activity.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return null;
        }
        telephonyManager.getDeviceId();

        String a = telephonyManager.getDeviceId();*/

        return a;
    }

    public static File getOutputMediaFile(){
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM).getAbsolutePath(),"Feid_Camera");

        if (!mediaStorageDir.exists()){
            if (!mediaStorageDir.mkdirs()){
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(mediaStorageDir.getAbsolutePath() + File.separator +
                "IMG_"+ timeStamp + ".jpg");
    }

    public static File createImageFile(Activity activity) throws IOException {

        String imageFilePath = null;
        String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss",
                        Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        imageFilePath = image.getAbsolutePath();
        return image;
    }

    public static String getdate() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String strDate = sdf.format(c.getTime());

        return strDate;
    }

    public static String getDateFormate(String s) {
        Calendar c = Calendar.getInstance();
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy");

        Date date = null;
        String strDate = null;

        try {
            date = inputFormat.parse(s);
            strDate = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }



        return strDate;
    }

    public static String getdateandtime() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        String strDate = sdf.format(c.getTime());

        return strDate;
    }

    public static String getdateandtime2() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh-mm-ss-SSS");
        String strDate = sdf.format(c.getTime());

        return strDate;
    }

    public static String getdateandtimeWithFormate(String formate) {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(formate);
        String strDate = sdf.format(c.getTime());

        return strDate;
    }

    public static String getTime() {
        DateFormat df = DateFormat.getTimeInstance();
        df.setTimeZone(TimeZone.getTimeZone("UTC+04:00"));
//        df.switchTimezone(/* your desired timezone in string format */);
        String gmtTime = df.format(new Date());


        return gmtTime;
    }

    public static String getCurrentTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm");
        String strDate = mdformat.format(calendar.getTime());

        return strDate;
    }

    public static String GetUTCdatetimeAsString() {
        final String DATEFORMAT = "yyyy-MM-dd HH:mm:ss";
        final SimpleDateFormat sdf = new SimpleDateFormat(DATEFORMAT);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        final String utcTime = sdf.format(new Date());

        return utcTime;
    }

    public static String getDateYYYY_MM_DD(Activity activity, String s1){
        String date = null;

        DateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy");
        DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
        String inputDateStr = s1;
        Date date1 = null;
        String outputDateStr = null;

        try {
            date1 = inputFormat.parse(inputDateStr);
            outputDateStr = outputFormat.format(date1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        date = outputDateStr;

        return date;
    }

    public static String getDateDD_MMM_YYYY(Activity activity, String s1){
        String date = null;

        DateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy");
        DateFormat outputFormat = new SimpleDateFormat("dd-MMM-yyyy");
        String inputDateStr = s1;
        Date date1 = null;
        String outputDateStr = null;

        try {
            date1 = inputFormat.parse(inputDateStr);
            outputDateStr = outputFormat.format(date1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        date = outputDateStr;

        return date;
    }

    public static String getTimeUsingZone() {
        String s = null;

        try {
            String dtc = "2014-04-02T07:59:02.111Z";
            SimpleDateFormat readDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            readDate.setTimeZone(TimeZone.getTimeZone("GMT")); // missing line
            Date date = null;
            date = readDate.parse(dtc);
            SimpleDateFormat writeDate = new SimpleDateFormat("dd.MM.yyyy, HH.mm");
            writeDate.setTimeZone(TimeZone.getTimeZone("GMT+04:00"));
            s = writeDate.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return  s;
    }

    public static String compressImage(Activity activity, String imageUri) {

        String filePath = getRealPathFromURI(activity,imageUri);
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath,options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;
        float maxHeight = 816.0f;
        float maxWidth = 612.0f;
        float imgRatio;
        if (actualWidth > actualHeight)
            imgRatio = actualWidth / actualHeight;
        else
            imgRatio = actualHeight / actualWidth;

        float maxRatio = maxWidth / maxHeight;

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;
            }
        }

        options.inSampleSize = Util.calculateInSampleSize(options, actualWidth, actualHeight);
        options.inJustDecodeBounds = false;
        options.inDither = false;
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16*1024];

        try{
            bmp = BitmapFactory.decodeFile(filePath,options);
        }
        catch(OutOfMemoryError exception){
            exception.printStackTrace();

        }
        try{
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        }
        catch(OutOfMemoryError exception){
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float)options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth()/2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));


        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
            } else if (orientation == 3) {
                matrix.postRotate(180);
            } else if (orientation == 8) {
                matrix.postRotate(270);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileOutputStream out = null;
        String filename = Util.getFilename();
        try {
            out = new FileOutputStream(filename);
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG,80, out);
//            Util.deleteImageFromGaller(activity,imageUri);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return filename;

    }

    private static String getRealPathFromURI(Activity activity, String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = activity.getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;

        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }

    public static String getFilename() {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "Pictures/CameraDemo");
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
        return uriSting;

    }

    public static void dialog(Activity activity , String s) {
        AlertDialog.Builder builder=new AlertDialog.Builder(activity);

        builder.setMessage(s);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                dialog.dismiss();
            }
        });
        AlertDialog dialog=builder.create();
        dialog.show();
    }

    public static void alertScreenChangeDialog(final Activity activity , String s, final Class aClass) {
        AlertDialog.Builder builder=new AlertDialog.Builder(activity);

        builder.setMessage(s);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                activity.startActivity(new Intent(activity,aClass));
            }
        });
        AlertDialog dialog=builder.create();
        dialog.show();
    }

    public static String DateFormate(String fromDateFormat, String toDateFormat, String date){

        String result;

        DateFormat inputFormat = new SimpleDateFormat(fromDateFormat, Locale.US);
        DateFormat outputFormat = new SimpleDateFormat(toDateFormat, Locale.US);
        String inputDateStr = date;
        Date date1 = null;
        String outputDateStr = null;

        try {
            date1 = inputFormat.parse(inputDateStr);
            outputDateStr = outputFormat.format(date1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        result = outputDateStr;

        return result;
    }

    public static String TimeFormate(String fromDateFormat, String toDateFormat, String date){

        String result;

        DateFormat inputFormat = new SimpleDateFormat(fromDateFormat);
        DateFormat outputFormat = new SimpleDateFormat(toDateFormat);
        String inputDateStr = date;
        Date date1 = null;
        String outputDateStr = null;

        try {
            date1 = inputFormat.parse(inputDateStr);
            outputDateStr = outputFormat.format(date1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        result = outputDateStr;

        return result;
    }

    public static float convertion(Double lat, Double lng, Double currentLat, Double currentLon){

        Location locationA = new Location("point A");
        locationA.setLatitude(lat);
        locationA.setLongitude(lng);
        Location locationB = new Location("point B");
        locationB.setLatitude(currentLat);
        locationB.setLongitude(currentLon);
        float distanc = locationA.distanceTo(locationB)/1000 ;
//        float distanc = locationA.distanceTo(locationB) ;

//        double earthRadius = 6371000; //meters
//        double dLat = Math.toRadians(currentLat - lat);
//        double dLng = Math.toRadians(currentLon - lng);
//        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
//                Math.cos(Math.toRadians(lat)) * Math.cos(Math.toRadians(lng)) *
//                        Math.sin(dLng / 2) * Math.sin(dLng / 2);
//        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
//        float distanc = (float) (earthRadius * c)/1000;

        return distanc;

    }

    public static double getDistanceInfo(Double lat, Double lng, Double currentLat, Double currentLon) {
//        StringBuilder stringBuilder = new StringBuilder();
        Double dist = 0.0;
//        try {
//
////            destinationAddress = destinationAddress.replaceAll(" ","%20");
//            String url = "http://maps.googleapis.com/maps/api/directions/json?origin=" + currentLat + "," + currentLon + "&destination=" + lat + "," + lng + "&mode=driving&sensor=false";
//
//            HttpPost httppost = new HttpPost(url);
//
//            HttpClient client = new DefaultHttpClient();
//            HttpResponse response;
//            stringBuilder = new StringBuilder();
//
//
//            response = client.execute(httppost);
//            HttpEntity entity = response.getEntity();
//            InputStream stream = entity.getContent();
//            int b;
//            while ((b = stream.read()) != -1) {
//                stringBuilder.append((char) b);
//            }
//        } catch (ClientProtocolException e) {
//        } catch (IOException e) {
//        }
//
//        JSONObject jsonObject = new JSONObject();
//        try {
//
//            jsonObject = new JSONObject(stringBuilder.toString());
//
//            JSONArray array = jsonObject.getJSONArray("routes");
//
//            JSONObject routes = array.getJSONObject(0);
//
//            JSONArray legs = routes.getJSONArray("legs");
//
//            JSONObject steps = legs.getJSONObject(0);
//
//            JSONObject distance = steps.getJSONObject("distance");
//
//            Log.i("Distance", distance.toString());
//            dist = Double.parseDouble(distance.getString("text").replaceAll("[^\\.0123456789]","") );
//
//        } catch (JSONException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }

        TextView textView;
        String a;
//        GetDistance method = new GetDistance(textView);
        try {
//            method.execute(lat,lng,currentLat,currentLon);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }



        return dist;
    }

    public static String specialCharecterRestriction(String s){

        String result = s;

        result = result.replaceAll("'","");
        result = result.replaceAll("^","");
        result = result.replaceAll("@","");
        result = result.replaceAll("~","");
        result = result.replaceAll("`","");
        result = result.replaceAll("|","");
        result = result.replaceAll("^","");
        result = result.replaceAll("003eu003cdiv style=\"font-size:0.9em\"u003","");


        return  result;
    }

    public static String getDirectionsUrl(LatLng origin, LatLng dest){

        // Origin of route
        String str_origin = "origin="+origin.latitude+","+origin.longitude;

        // Destination of route 25.249398, 55.340508
        String str_dest = "destination="+"25.249398"+","+"55.340508";
//        String str_dest = "destination="+dest.latitude+","+dest.longitude;


        // Sensor enabled
        String sensor = "sensor=false";

        String makani = "destination="+"1379071964";

        // Building the parameters to the web service
//        String parameters = str_origin+"&"+str_dest+"&"+sensor;
        String parameters = str_origin+"&"+str_dest+"&"+sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;


        return url;
    }

    public static ArrayList<String> getFilePaths(Activity activity) {
        Uri u = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Images.ImageColumns.DATA};
        Cursor c = null;
        SortedSet<String> dirList = new TreeSet<String>();
        ArrayList<String> resultIAV = new ArrayList<String>();

        String[] directories = null;
        if (u != null)
        {
            c = activity.managedQuery(u, projection, null, null, null);
        }

        if ((c != null) && (c.moveToFirst()))
        {
            do
            {
                String tempDir = c.getString(0);
                tempDir = tempDir.substring(0, tempDir.lastIndexOf("/"));
                try{
                    dirList.add(tempDir);
                }
                catch(Exception e)
                {

                }
            }
            while (c.moveToNext());
            directories = new String[dirList.size()];
            dirList.toArray(directories);

        }

        for(int i=0;i<dirList.size();i++)
        {
            File imageDir = new File(directories[i]);
            File[] imageList = imageDir.listFiles();
            if(imageList == null)
                continue;
            for (File imagePath : imageList) {
                try {

                    if(imagePath.isDirectory())
                    {
                        imageList = imagePath.listFiles();

                    }
                    if ( imagePath.getName().contains(".jpg")|| imagePath.getName().contains(".JPG")
                            || imagePath.getName().contains(".jpeg")|| imagePath.getName().contains(".JPEG")
                            || imagePath.getName().contains(".png") || imagePath.getName().contains(".PNG")
                            || imagePath.getName().contains(".gif") || imagePath.getName().contains(".GIF")
                            || imagePath.getName().contains(".bmp") || imagePath.getName().contains(".BMP")
                            )
                    {



                        String path= imagePath.getAbsolutePath();
                        resultIAV.add(path);

                    }
                }
                //  }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return resultIAV;


    }

    public static void deleteImageFromGaller(Activity activity, String path) {
        File target = new File(path);
        if (target.exists() && target.isFile() && target.canWrite()) {
            try {
                target.getCanonicalFile().delete();
                activity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(path))));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void deleteLastCapturedImage(Activity activity) {
        String[] projection = {
                MediaStore.Images.ImageColumns.SIZE,
                MediaStore.Images.ImageColumns.DISPLAY_NAME,
                MediaStore.Images.ImageColumns.DATA,
                BaseColumns._ID
        };

        Cursor c = null;
        Uri u = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        try {
            if (u != null) {
                c = activity.managedQuery(u, projection, null, null, null);
            }
            if ((c != null) && (c.moveToLast())) {

                ContentResolver cr = activity.getContentResolver();
                int i = cr.delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, BaseColumns._ID + "=" + c.getString(c.getColumnIndex(BaseColumns._ID)), null);

            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
//        finally {
//            if (c != null) {
//                c.close();
//            }
//        }

    }

    public static String twoDates(String firstDate, String secDate){

        String diffrrence = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/M/yyyy hh:mm:ss");

        try {
            Date date1 = simpleDateFormat.parse(firstDate);
            Date date2 = simpleDateFormat.parse(secDate);

           diffrrence =  printDifference(date1, date2);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return diffrrence;
    }

    public static String printDifference(Date startDate, Date endDate) {
        //milliseconds
        String date = null;
        long different = endDate.getTime() - startDate.getTime();

        System.out.println("startDate : " + startDate);
        System.out.println("endDate : "+ endDate);
        System.out.println("different : " + different);

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

        System.out.printf(
                "%d days, %d hours, %d minutes, %d seconds%n",
                elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds);

        date = elapsedDays+ "d "+elapsedHours+"h "+elapsedMinutes+"m"/*+elapsedSeconds+" s"*/;
        return date;
    }

    public static Bitmap decodeBase64(String input) {
        byte[] decodedBytes = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    public static String getFileToBase64(String filePath){
        Bitmap bmp = null;
        ByteArrayOutputStream bos = null;
        byte[] bt = null;
        String encodeString = null;
        try{
            bmp = BitmapFactory.decodeFile(filePath);
            bos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bt = bos.toByteArray();
            encodeString = Base64.encodeToString(bt, Base64.DEFAULT);
        }catch (Exception e){
            e.printStackTrace();
        }
        return encodeString;
    }

    public static void netWorkIssueDialg(Context activity){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        builder.setMessage("Network Issue");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static void slideDown(View view){
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                0,                 // fromYDelta
                view.getHeight()); // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }

    public static double distance(double lat1, double lng1, double lat2, double lng2) {

        double earthRadius = 3958.75; // in miles, change to 6371 for kilometer output

        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);

        double sindLat = Math.sin(dLat / 2);
        double sindLng = Math.sin(dLng / 2);

        double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
                * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double dist = earthRadius * c;// output distance, in MILES

        double d = dist /** 0.0006213711*/;// converted to meter.

        return d;
    }

    public static double distance2(double startLat, double endLat, double StartLon, double EndLon) {
        Double d = 0.0;
        try{
            float[] results;
            results = new float[]{1};

            Location.distanceBetween( startLat,  StartLon,  endLat,  EndLon, results);
            d = Double.valueOf(results[0]);
            d = d*0.0006213711;
        }
        catch (Exception e)
        {

        }

        return d;
    }

    public static int dateDiffrence(String strdate){
        int b = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date strDate = null;
        try {
            strDate = sdf.parse(strdate);
            b = new Date().compareTo(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return b;
    }

    /*public static void  stopBgService(Context context) {
        PendingIntent pendingIntent;
        final Intent intent = new Intent(context, MyReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
        context.stopService(intent);
    }*/

    public static boolean checktimings(String time, String currenttime) {

        String pattern = "HH:mm";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);

        try {
            Date date1 = sdf.parse(time);
            Date date2 = sdf.parse(currenttime);

            if(date1.before(date2)) {
                return true;
            } else {
                return false;
            }
        } catch (ParseException e){
            e.printStackTrace();
        }
        return false;
    }

    @SuppressLint("NewApi")
    public static String getVideoPath(Context context, Uri videoUri) {
        Cursor cursor = null;
        String retVal = null;
        try {
            if (Build.VERSION.SDK_INT < 19) {
                String[] proj = { MediaStore.Images.Media.DATA };
                cursor = context.getContentResolver().query(videoUri, proj, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                retVal = cursor.getString(column_index);
            }
            else {
                // Will return "image:x*"
                String wholeID = DocumentsContract.getDocumentId(videoUri);

                // Split at colon, use second item in the array
                String id = wholeID.split(":")[1];

                String[] column = { MediaStore.Images.Media.DATA };

                // where id is equal to
                String sel = MediaStore.Images.Media._ID + "=?";

                cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, column, sel, new String[] { id }, null);

                int columnIndex = cursor.getColumnIndex(column[0]);

                if (cursor.moveToFirst()) {
                    retVal = cursor.getString(columnIndex);
                }
            }

        }
        catch (Exception ex) {
            ex.printStackTrace();
            // return null;
        }
        finally {
            if (cursor != null)
                cursor.close();
            cursor = null;
        }
        return retVal;
    }
}
