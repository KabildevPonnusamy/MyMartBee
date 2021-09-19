package com.mart.mymartbee.view.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.github.vipulasri.timelineview.TimelineView;
import com.mart.mymartbee.R;
import com.mart.mymartbee.model.Category_Model;
import com.mart.mymartbee.model.ShopStatusModel;

import java.util.ArrayList;

public class ShopStatusAdapter extends RecyclerView.Adapter<ShopStatusAdapter.TimeLineViewHolder> {

    private ArrayList<ShopStatusModel> shopStatusModels;
    private Context context;

    public ShopStatusAdapter(ArrayList<ShopStatusModel> shopStatusModels, Context context) {
        this.shopStatusModels = shopStatusModels;
        this.context = context;
    }

    @Override
    public TimeLineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_timeline, parent,
                false);
        return new TimeLineViewHolder(v, viewType);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(final TimeLineViewHolder holder, final int position) {
        holder.store_info.setText(shopStatusModels.get(position).getStatus_title());
        holder.store_link.setText(shopStatusModels.get(position).getStatus_link());

        if(shopStatusModels.get(position).getStatus_status().equalsIgnoreCase("1")) {
            holder.timelineView.setMarker(context.getResources().getDrawable(R.drawable.ic_marker_active));
            holder.store_info.setEnabled(true);
            holder.store_info.setClickable(true);
            holder.store_link.setEnabled(true);
            holder.store_link.setClickable(true);
            holder.store_info.setTextColor(ContextCompat.getColor(context, R.color.black));
            holder.store_link.setTextColor(ContextCompat.getColor(context, R.color.purple_700));
        } else {
            holder.timelineView.setMarker(context.getResources().getDrawable(R.drawable.ic_marker_inactive));
            holder.store_info.setEnabled(false);
            holder.store_info.setClickable(false);
            holder.store_link.setEnabled(false);
            holder.store_link.setClickable(false);
            holder.store_info.setTextColor(ContextCompat.getColor(context, R.color.shadow_color));
            holder.store_link.setTextColor(ContextCompat.getColor(context, R.color.shadow_color));
        }

        holder.store_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position == 0) {

                } else if (position == 1) {

                }
                Log.e("appSample", "Clicked: " + position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return shopStatusModels.size();
    }

    class TimeLineViewHolder extends RecyclerView.ViewHolder {

        TimelineView timelineView;
        TextView store_info, store_link;

        TimeLineViewHolder(View itemView, int viewType) {
            super(itemView);
            timelineView = (TimelineView) itemView.findViewById(R.id.timelineView);
            store_info = (TextView) itemView.findViewById(R.id.store_info);
            store_link = (TextView) itemView.findViewById(R.id.store_link);
            timelineView.initLine(viewType);

        }
    }
}
