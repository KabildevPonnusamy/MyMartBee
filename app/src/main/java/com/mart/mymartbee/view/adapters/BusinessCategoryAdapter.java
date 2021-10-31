package com.mart.mymartbee.view.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mart.mymartbee.R;
import com.mart.mymartbee.model.BusinessCategory_Model;

import java.util.ArrayList;

public class BusinessCategoryAdapter extends RecyclerView.Adapter<BusinessCategoryAdapter.ViewHolder> {

    private ArrayList<BusinessCategory_Model.Categorys> categoryList;
    private Context context;
    private int selectedId;

    public BusinessCategoryAdapter(ArrayList<BusinessCategory_Model.Categorys> categoryList, Context context, int selectedId) {
        this.categoryList = categoryList;
        this.context = context;
        this.selectedId = selectedId;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent,
                false);
        return new ViewHolder(v);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.category_title.setText(categoryList.get(position).getStrCateGoryName());

        if (!categoryList.get(position).getStrCategoryImage().equalsIgnoreCase("")) {
            Glide.with(context).load(categoryList.get(position).getStrCategoryImage()).into(holder.category_image);
        } else {
            holder.category_image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.appicon_temp));
        }

        if (selectedId == Integer.parseInt(categoryList.get(position).getStrCategoryId())) {
            holder.category_border.setBackgroundResource(R.drawable.selected_border);
            /*holder.category_image.setColorFilter(ContextCompat.getColor(context, R.color.main_color),
                    PorterDuff.Mode.MULTIPLY);*/
            holder.category_title.setTypeface(null, Typeface.BOLD);
        } else {
            holder.category_border.setBackgroundResource(R.drawable.unselect_border);
            /*holder.category_image.setColorFilter(ContextCompat.getColor(context, R.color.black),
                    PorterDuff.Mode.MULTIPLY);*/
            holder.category_title.setTypeface(null, Typeface.NORMAL);
        }
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public void updateAdapter(ArrayList<BusinessCategory_Model.Categorys> categoryList) {
        this.categoryList = categoryList;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView category_image;
        TextView category_title;
        LinearLayout category_border;


        ViewHolder(View itemView) {
            super(itemView);
            category_image = (ImageView) itemView.findViewById(R.id.category_image);
            category_title = (TextView) itemView.findViewById(R.id.category_title);
            category_border = (LinearLayout) itemView.findViewById(R.id.category_border);

        }
    }
}
