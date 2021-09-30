package com.mart.mymartbee.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.mart.mymartbee.BuildConfig;
import com.mart.mymartbee.R;
import com.mart.mymartbee.constants.Constants;
import com.mart.mymartbee.storage.MyPreferenceDatas;
import com.mart.mymartbee.storage.StorageDatas;
import com.suke.widget.SwitchButton;

public class SettingsAct extends AppCompatActivity implements View.OnClickListener, Constants {

    ImageView settings_back;
    LinearLayout logout_layout, share_whatsapp_layout;
    TextView version_number;
    MyPreferenceDatas preferenceDatas;
    String myKeyValue = "";
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
    }

    private void initView() {
        notification_switch = findViewById(R.id.notification_switch);
        settings_back = findViewById(R.id.settings_back);
        logout_layout = findViewById(R.id.logout_layout);
        share_whatsapp_layout = findViewById(R.id.share_whatsapp_layout);
        version_number = findViewById(R.id.version_number);
        version_number.setText( "MART" + BuildConfig.VERSION_NAME);
        setListeners();
    }

    private void setListeners() {
        settings_back.setOnClickListener(this);
        logout_layout.setOnClickListener(this);
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

        case R.id.logout_layout:
                preferenceDatas.clearPreference(getApplicationContext());
                setResult(LOGOUT);
                finish();
                break;
        }
    }

    private void shareOnWhatsapp() {
        Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
        whatsappIntent.setType("text/plain");
        whatsappIntent.setPackage("com.whatsapp");
        whatsappIntent.putExtra(Intent.EXTRA_TEXT, StorageDatas.getInstance().getStoreWhatsappLink() + " \n\n   Please visit my store to buy electronics products on very low cost.");
        try {
            startActivity(whatsappIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getApplicationContext(), "WhatsApp have not been installed.", Toast.LENGTH_SHORT).show();
        }
    }
}
