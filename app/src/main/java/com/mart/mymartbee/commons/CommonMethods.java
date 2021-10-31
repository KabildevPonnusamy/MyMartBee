package com.mart.mymartbee.commons;

import android.app.Application;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mart.mymartbee.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CommonMethods extends Application  {

    Context mContext;

    public CommonMethods(Context context) {
        mContext = context;
    }

    public static void Toast(Context context, String message) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.toast, null); // (ViewGroup) findViewById(R.id.toast_layout_root)

        TextView msg_content = (TextView) layout.findViewById(R.id.msg_content);
        msg_content.setText(message);

        Toast toast = new Toast(context);
        toast.setGravity(Gravity.BOTTOM, 0, 200);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();

        /*Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        View view = toast.getView();
        view.setBackgroundResource(R.drawable.main_color_bgnd);
        view.setPadding(30,30,30,30);
        TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
        v.setTextColor(Color.WHITE);
        v.setText(message);
        toast.show();*/
    }

    public static void LinkCopiedToast(Context context, String message) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.toast_whatsapp, null); // (ViewGroup) findViewById(R.id.toast_layout_root)

        TextView msg_content = (TextView) layout.findViewById(R.id.msg_content);
        msg_content.setText(message);

        Toast toast = new Toast(context);
        toast.setGravity(Gravity.BOTTOM, 0, 150);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();

        /*Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        View view = toast.getView();
        view.setBackgroundResource(R.drawable.link_copied_bgnd);
        view.setPadding(25,25,25,25);
        TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
        v.setTextColor(Color.WHITE);
        v.setText(message);
        toast.show();*/
    }

    public static String getContactName(Context context, String number) {
        Uri lookupUri = Uri.withAppendedPath(
                ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
                Uri.encode(number));
        String[] mPhoneNumberProjection = { ContactsContract.PhoneLookup._ID, ContactsContract.PhoneLookup.NUMBER,
                ContactsContract.PhoneLookup.DISPLAY_NAME };
        Cursor cur = context.getContentResolver().query(lookupUri,mPhoneNumberProjection,
                null, null, null);
        String phoneUserName = "";

        try {
            if (cur.moveToFirst()) {
                phoneUserName = cur.getString(cur.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
                Log.e("appSample", "Name: " + phoneUserName);
                cur.close();
                return phoneUserName;
            }
        } finally {
            if (cur != null)
                cur.close();
        }
        return phoneUserName;
    }

    public static String formatDate(String inputDate) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date d = formatter.parse(inputDate);
            SimpleDateFormat formatter2 = new SimpleDateFormat("dd MMM yyyy, hh:mm aaa.");
            return "" + formatter2.format(d);
        } catch (Exception e) {
            return "";
        }
    }
}
