package com.mart.mymartbee.receiver;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.mart.mymartbee.R;
import com.mart.mymartbee.algorithm.TripleDes;
import com.mart.mymartbee.model.Orders_Model;
import com.mart.mymartbee.storage.MyPreferenceDatas;
import com.mart.mymartbee.storage.StorageDatas;
import com.mart.mymartbee.view.PendingOrders;
import com.mart.mymartbee.view.Splash;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    MyPreferenceDatas preferenceDatas;
    String myKeyValue = "";
    String strSellerId = "";

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        myKeyValue = getResources().getString(R.string.myTripleKey);
        preferenceDatas = new MyPreferenceDatas(getApplicationContext());
        strSellerId = TripleDes.getDESDecryptValue(preferenceDatas.getPrefString(MyPreferenceDatas.SELLER_ID), myKeyValue);

        if(strSellerId != null && !strSellerId.equalsIgnoreCase("")) {
            updatePushData(remoteMessage);
        } else {
            Log.e("appSample", "onLoggedOut");
        }
    }

    public  void updatePushData(RemoteMessage remoteMessage) {
        Orders_Model.OrdersList ordersListObj = new Orders_Model.OrdersList();
        JSONObject jsonObject = null;

        Log.e("appSample", "onMessageReceived: " + remoteMessage.getData());
        Log.e("appSample", "Title: " + remoteMessage.getData().get("title"));
        Log.e("appSample", "Message: " + remoteMessage.getData().get("message"));

        String str = remoteMessage.getData().get("order_datas");
        Log.e("appSample", "MyData: " + str);
        try {
            jsonObject = new JSONObject(str);
            Log.e("appSample", "OrderId: " + jsonObject.get("order_id"));

            ordersListObj.setStrOrderId(jsonObject.getString("order_id"));
            ordersListObj.setStrStatus(jsonObject.getString("status"));
            ordersListObj.setStrOrderDate(jsonObject.getString("order_date"));
            ordersListObj.setStrTotalAmount(jsonObject.getString("total_amount") );
            ordersListObj.setStrPhone(jsonObject.getString("phone") );
            ordersListObj.setStrCountryCode(jsonObject.getString("country_code") );
            ordersListObj.setStrAddress(jsonObject.getString("address") );
            ordersListObj.setStrPaymentType(jsonObject.getString("payment_type"));
            ordersListObj.setStrPaymentReceipt(jsonObject.getString("payment_receipt"));

            StorageDatas.getInstance().setStrOrderId(jsonObject.getString("order_id"));
            StorageDatas.getInstance().setStrOrderStatus(jsonObject.getString("status"));

            ArrayList<Orders_Model.OrdersList.OrderHistory> listitem = new ArrayList<>();
            ArrayList<Orders_Model.OrdersList.OrderedProducts> productItem = new ArrayList<>();

            JSONArray historyArray = jsonObject.getJSONArray("order_history");

            for( int i=0; i < historyArray.length(); i++ ) {
                JSONObject jsonObject1 = historyArray.getJSONObject(i);

                Orders_Model.OrdersList.OrderHistory item = new Orders_Model.OrdersList.OrderHistory();
                item.setStrAddedDate(jsonObject1.getString("date_added"));
                item.setStrStatusName(jsonObject1.getString("name"));
                listitem.add(item);
            }

            JSONArray productsArray = jsonObject.getJSONArray("products");

            for(int j=0; j < productsArray.length(); j++) {
                JSONObject jsonObject2 = productsArray.getJSONObject(j);

                Orders_Model.OrdersList.OrderedProducts item = new Orders_Model.OrdersList.OrderedProducts();
                item.setStrProductId(jsonObject2.getString("product_id"));
                item.setStrProductImage(jsonObject2.getString("image"));
                item.setStrProductPrice(jsonObject2.getString("price"));
                item.setStrProductQuantity(jsonObject2.getString("quantity"));
                item.setStrProductTotal(jsonObject2.getString("total"));
                item.setStrProductTitle(jsonObject2.getString("title"));
                productItem.add(item);
            }

            ordersListObj.setOrderHistoryList(listitem);
            ordersListObj.setOrderedProductsList(productItem);

            StorageDatas.getInstance().setOrdersListObj(ordersListObj);
            StorageDatas.getInstance().setFromNotification(true);

            if(remoteMessage.getData().size() > 0) {
                createNotification(remoteMessage.getData().get("message"), remoteMessage.getData().get("title"));
            }

        }catch (JSONException err) {
            Log.d("Error", err.toString());
        }
    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Log.e("appSample", "Token: " + s);
    }

    /*private void createNotificationNew(String messageBody, String messageTitle) {

        Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                + "://" + getApplicationContext().getPackageName() + "/raw/notification");

        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            Log.e("appSample", "App is in Foreground");

            Intent resultIntent = new Intent(getApplicationContext(), Splash.class);
            showing_notification(messageTitle, messageBody, alarmSound, getApplicationContext());
        } else {
            Log.e("appSample", "App is in background");
            Intent resultIntent = new Intent(getApplicationContext(), Splash.class);
            resultIntent.putExtra("message", messageTitle);
            showing_notification(messageTitle, messageBody, alarmSound, getApplicationContext());
        }
    }*/

    /*private void showing_notification(String title, String message, Uri alarmSound, Context context) {
        Intent intent1 = new Intent(this, Splash.class);
        intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntent(intent1);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent1, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
        notificationBuilder.setContentTitle(title);
        notificationBuilder.setContentText(message);
        notificationBuilder.setSmallIcon(R.drawable.appicon_temp);
        notificationBuilder.setSound(alarmSound);
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setColor(getResources().getColor(R.color.main_color));
        notificationBuilder.setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        int notificationId = 1;
        String channelId = "channel-01";
        String channelName = "Channel_UrbanFriends";
        int importance = NotificationManager.IMPORTANCE_HIGH;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }
        notificationBuilder.setChannelId(channelId);
        notificationManager.notify(notificationId, notificationBuilder.build());
    }*/

    private void createNotification( String messageBody,String messageTitle) {
        Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getPackageName() + "/" + R.raw.tone);

        String CHANNEL_ID = "1";
        String CHANNEL_NAME = "MYMARTBEE";

        int notification_id = (int) System.currentTimeMillis();
        NotificationManager notificationManager = null;
        NotificationCompat.Builder mBuilder;

        String title = messageTitle;
        String body = messageBody;

        //Set pending intent to builder
        Intent intent = new Intent( this , PendingOrders.class );
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent resultIntent = PendingIntent.getActivity( this , 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        //Notification builder
        if (notificationManager == null){
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = notificationManager.getNotificationChannel(CHANNEL_ID);

            AudioAttributes attributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build();
            if (mChannel == null){
                mChannel = new NotificationChannel(CHANNEL_ID, "ChannelMine", importance);
                mChannel.setDescription("Desc");
                mChannel.enableVibration(true);
                mChannel.setLightColor(Color.GREEN);
                mChannel.setSound(alarmSound, attributes);
                mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                notificationManager.createNotificationChannel(mChannel);
            }

            mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID);
            mBuilder.setContentTitle(title)
                    .setSmallIcon(R.drawable.appicon_temp)
                    .setContentText(body) //show icon on status bar
                    .setContentIntent(resultIntent)
//                    .setSound(alarmSound, attributes)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true)
                    .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400})
                    .setDefaults(Notification.DEFAULT_ALL);
        } else {
            mBuilder = new NotificationCompat.Builder(this);
            mBuilder.setContentTitle(title)
                    .setSmallIcon(R.drawable.appicon_temp)
                    .setColor(Color.TRANSPARENT)
                    .setContentText(body)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setContentIntent(resultIntent)
                    .setSound(alarmSound)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true)
                    .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400})
                    .setDefaults(Notification.DEFAULT_VIBRATE);
        }

        notificationManager.notify(1002, mBuilder.build());

    }
}
