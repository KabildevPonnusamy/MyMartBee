package com.mart.mymartbee.view.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mart.mymartbee.R;
import com.mart.mymartbee.commons.CommonMethods;
import com.mart.mymartbee.model.Dashboard_Model;
import com.mart.mymartbee.model.UploadingImageList;

import java.io.File;
import java.util.ArrayList;

public class ImageUploadingAdapter extends RecyclerView.Adapter<ImageUploadingAdapter.ViewHolder> {

    private ArrayList<UploadingImageList> imageList;
    private Context context;
    private ImageRemoveClickListener imageRemoveClickListener;

    public interface ImageRemoveClickListener {
        void onItemClick(int position);
    }

    public ImageUploadingAdapter(ArrayList<UploadingImageList> imageList, Context context, ImageRemoveClickListener imageRemoveClickListener ) {
        this.imageList = imageList;
        this.context = context;
        this.imageRemoveClickListener = imageRemoveClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_upload_img, parent,
                false);
        return new ViewHolder(v);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        Uri compressUri = Uri.fromFile(new File(imageList.get(position).getImage()));

        Glide.with(context)
                .load(compressUri)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(holder.product_image);

        holder.delete_image_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imageRemoveClickListener != null) {
                    imageRemoveClickListener.onItemClick(position);
                }
                Log.e("appSample", "Clicked: " + position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout delete_image_layout;
        ImageView product_image;

        ViewHolder(View itemView) {
            super(itemView);

            product_image = (ImageView) itemView.findViewById(R.id.product_image);
            delete_image_layout = (LinearLayout) itemView.findViewById(R.id.delete_image_layout);

        }
    }
}
