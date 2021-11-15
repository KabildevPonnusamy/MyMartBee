package com.mart.mymartbee.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.mart.mymartbee.R;
import com.mart.mymartbee.model.Products_Model;

import java.util.ArrayList;

public class ViewPagerAdapter extends PagerAdapter {

    private Context mContext;
    ArrayList<Products_Model.ProductCategories.ProductsList.OtherImages> imagesArrayList;

    public ViewPagerAdapter(Context context, ArrayList<Products_Model.ProductCategories.ProductsList.OtherImages> imagesArrayList) {
        mContext = context;
        this.imagesArrayList = imagesArrayList;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.item_viewpager, collection, false);

        ImageView image_view = (ImageView) layout.findViewById(R.id.image_view);
        String url = imagesArrayList.get(position).getStrImage();
        Glide.with(mContext).load(url).into(image_view);
        collection.addView(layout);
        return layout;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return imagesArrayList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


}