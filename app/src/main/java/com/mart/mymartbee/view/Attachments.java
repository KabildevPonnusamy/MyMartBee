package com.mart.mymartbee.view;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.mart.mymartbee.R;

public class Attachments extends AppCompatActivity implements View.OnClickListener{

    ImageView attachment_back, attachment_image;
    String strImage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attachment);

        initView();
        getBundle();

    }

    public void getBundle() {
        Bundle bundle = getIntent().getExtras();
        strImage = bundle.getString("attachedImage");
        Glide.with(getApplicationContext()).load(strImage).into(attachment_image);
    }

    public void initView() {
        attachment_back = findViewById(R.id.attachment_image);
        attachment_image = findViewById(R.id.attachment_image);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.attachment_back:
                finish();
                break;
        }
    }
}
