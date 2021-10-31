package com.mart.mymartbee.view.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.github.vipulasri.timelineview.TimelineView;
import com.mart.mymartbee.R;
import com.mart.mymartbee.model.Orders_Model;
import com.mart.mymartbee.model.Products_Model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SubcateOptionsAdapter extends RecyclerView.Adapter<SubcateOptionsAdapter.TimeLineViewHolder> {

    private ArrayList<Products_Model.ProductCategories> subcategoryList;
    private Context context;
    private OptionsClickListener optionsClickListener;
    private int resId;

    public interface OptionsClickListener {
        void onItemClick(int position, String subcate_id, String subcate_name);
    }

    public SubcateOptionsAdapter(ArrayList<Products_Model.ProductCategories> subcategoryList, Context context,
                                 OptionsClickListener optionsClickListener, int resId) {
        this.subcategoryList = subcategoryList;
        this.context = context;
        this.optionsClickListener = optionsClickListener;
        this.resId = resId;
    }

    @Override
    public TimeLineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_subcategory_options, parent,
                false);
        return new TimeLineViewHolder(v, viewType);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(final TimeLineViewHolder holder, final int position) {
        holder.subcate_title.setText(subcategoryList.get(position).getStrCateName());
        holder.subcate_option.setImageResource(resId);

        holder.subcate_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                optionsClickListener.onItemClick(position, subcategoryList.get(position).getStrCateId(),
                        subcategoryList.get(position).getStrCateName());
            }
        });
    }

    @Override
    public int getItemCount() {
        return subcategoryList.size();
    }

    class TimeLineViewHolder extends RecyclerView.ViewHolder {

        TextView subcate_title;
        ImageView subcate_option;

        TimeLineViewHolder(View itemView, int viewType) {
            super(itemView);
            subcate_title = (TextView) itemView.findViewById(R.id.subcate_title);
            subcate_option = (ImageView) itemView.findViewById(R.id.subcate_option);
        }
    }
}
