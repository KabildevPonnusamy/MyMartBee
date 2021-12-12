package com.mart.mymartbee.view.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mart.mymartbee.R;
import com.mart.mymartbee.model.NewReportProducts_Model;
import com.mart.mymartbee.model.NewReportSales_Model;

import java.util.ArrayList;

public class ProductReportViewsCategoryAdapter extends RecyclerView.Adapter<ProductReportViewsCategoryAdapter.ViewHolder> {

    private ArrayList<NewReportProducts_Model.CategoryViews> categoryViewsArrayList;
    private Context context;

    public ProductReportViewsCategoryAdapter(ArrayList<NewReportProducts_Model.CategoryViews> categoryViewsArrayList, Context context) {
        this.categoryViewsArrayList = categoryViewsArrayList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_catesalesreport, parent,
                false);
        return new ViewHolder(v);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.item_salescatename.setText(categoryViewsArrayList.get(position).getStrCategory());
        holder.color_layout.setBackgroundColor(Color.parseColor("#" + categoryViewsArrayList.get(position).getStrColor()));
    }

    @Override
    public int getItemCount() {
        return categoryViewsArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView item_salescatename;
        LinearLayout color_layout;

        ViewHolder(View itemView) {
            super(itemView);
            item_salescatename = (TextView) itemView.findViewById(R.id.item_salescatename);
            color_layout = (LinearLayout) itemView.findViewById(R.id.color_layout);
        }
    }
}
