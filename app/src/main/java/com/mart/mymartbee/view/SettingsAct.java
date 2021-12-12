package com.mart.mymartbee.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.mart.mymartbee.R;
import com.mart.mymartbee.algorithm.TripleDes;
import com.mart.mymartbee.commons.CommonMethods;
import com.mart.mymartbee.constants.Constants;
import com.mart.mymartbee.storage.MyPreferenceDatas;
import com.mart.mymartbee.storage.StorageDatas;
import com.suke.widget.SwitchButton;

public class SettingsAct extends AppCompatActivity implements View.OnClickListener, Constants {

    ImageView settings_back;
    LinearLayout share_whatsapp_layout; // logout_layout
    MyPreferenceDatas preferenceDatas;
    String myKeyValue = "", strCateName = "", strSelleShop = "", strSellerMobile = "";
    SwitchButton notification_switch;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        initView();
        getMyPreferences();

    }

    private void getMyPreferences() {
        myKeyValue = getResources().getString(R.string.myTripleKey);
        preferenceDatas = new MyPreferenceDatas(SettingsAct.this);
        strCateName = TripleDes.getDESDecryptValue(preferenceDatas.getPrefString(MyPreferenceDatas.SELLER_CATEGORY_NAME), myKeyValue);
        strSelleShop = TripleDes.getDESDecryptValue(preferenceDatas.getPrefString(MyPreferenceDatas.SELLER_SHOP), myKeyValue);
        strSellerMobile = TripleDes.getDESDecryptValue(preferenceDatas.getPrefString(MyPreferenceDatas.SELLER_MOBILE), myKeyValue);
    }

    private void initView() {
        notification_switch = findViewById(R.id.notification_switch);
        settings_back = findViewById(R.id.settings_back);
        share_whatsapp_layout = findViewById(R.id.share_whatsapp_layout);
        setListeners();
    }

    private void setListeners() {
        settings_back.setOnClickListener(this);
        share_whatsapp_layout.setOnClickListener(this);

        notification_switch.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (isChecked) {
                    //True
                } else {
                    //False
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.settings_back:
                finish();
                break;

            case R.id.share_whatsapp_layout:
                shareOnWhatsapp();
                break;

        }
    }

    private void shareOnWhatsapp() {
        Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
        whatsappIntent.setType("text/plain");
        whatsappIntent.setPackage("com.whatsapp");

        whatsappIntent.putExtra(Intent.EXTRA_TEXT, " \n  Hello! \nNow you can order from " + "*" +  strSelleShop  + "*"+ " using this link: " + StorageDatas.getInstance().getStoreWhatsappLink() +
                "\nFeel free to call us on " + strSellerMobile + " if you need any help with ordering. \n\n Thank you." );


//        whatsappIntent.putExtra(Intent.EXTRA_TEXT, StorageDatas.getInstance().getStoreWhatsappLink() + " \n\n   Please visit my store to buy " + strCateName +" products on very low cost.");
        try {
            startActivity(whatsappIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            CommonMethods.Toast(SettingsAct.this,  "WhatsApp have not been installed.");
        }
    }
}
