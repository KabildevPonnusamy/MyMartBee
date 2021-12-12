package com.mart.mymartbee.view.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
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
import com.mart.mymartbee.model.NewReportSales_Model;

import java.util.ArrayList;

public class SalesReportCategoryAdapter extends RecyclerView.Adapter<SalesReportCategoryAdapter.ViewHolder> {

    private ArrayList<NewReportSales_Model.CategoryRevenue> categoryRevenuesList;
    private Context context;

    public SalesReportCategoryAdapter(ArrayList<NewReportSales_Model.CategoryRevenue> categoryRevenuesList, Context context) {
        this.categoryRevenuesList = categoryRevenuesList;
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
        holder.item_salescatename.setText(categoryRevenuesList.get(position).getStrCategory());
        holder.color_layout.setBackgroundColor(Color.parseColor("#" + categoryRevenuesList.get(position).getStrColor()));
    }

    @Override
    public int getItemCount() {
        return categoryRevenuesList.size();
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
