package com.mart.mymartbee.view.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mart.mymartbee.R;
import com.mart.mymartbee.model.SubCategory_Model;

import java.util.ArrayList;

public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.ViewHolder> {

    private ArrayList<SubCategory_Model.SubCategory> subcategoryList;
    private Context context;
    private int selectedId;

    public SubCategoryAdapter(ArrayList<SubCategory_Model.SubCategory> subcategoryList, Context context, int selectedId) {
        this.subcategoryList = subcategoryList;
        this.context = context;
        this.selectedId = selectedId;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_subcategory_list, parent,
                false);
        return new ViewHolder(v);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.subcategory_title.setText(subcategoryList.get(position).getStrSubCateName());
        Log.e("appSample", "SubCateId: " + selectedId);

        if (selectedId == Integer.parseInt(subcategoryList.get(position).getStrSubCateId())) {
            holder.subcategory_border.setBackgroundResource(R.drawable.selected_border);
            holder.subcategory_title.setTypeface(null, Typeface.BOLD);
        } else {
            holder.subcategory_border.setBackgroundResource(R.drawable.unselect_border);
            holder.subcategory_title.setTypeface(null, Typeface.NORMAL);
        }
    }

    @Override
    public int getItemCount() {
        return subcategoryList.size();
    }

    public void updateAdapter(ArrayList<SubCategory_Model.SubCategory> subcategoryList) {
        this.subcategoryList = subcategoryList;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView subcategory_title;
        LinearLayout subcategory_border;

        ViewHolder(View itemView) {
            super(itemView);
            subcategory_title = (TextView) itemView.findViewById(R.id.subcategory_title);
            subcategory_border = (LinearLayout) itemView.findViewById(R.id.subcategory_border);

        }
    }
}
