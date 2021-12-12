package com.mart.mymartbee.view.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mart.mymartbee.R;
import com.mart.mymartbee.model.NewReportProducts_Model;
import com.mart.mymartbee.model.NewReportSales_Model;

import java.util.ArrayList;

public class SalesReportCategoryProgressAdapter extends RecyclerView.Adapter<SalesReportCategoryProgressAdapter.ViewHolder> {

    private ArrayList<NewReportSales_Model.CategoryRevenue> categoryRevenuesList;
    private Context context;

    public SalesReportCategoryProgressAdapter(ArrayList<NewReportSales_Model.CategoryRevenue> categoryRevenuesList, Context context) {
        this.categoryRevenuesList = categoryRevenuesList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_salesreportcategoryprogressview, parent,
                false);
        return new ViewHolder(v);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.item_srcp_tv.setText(categoryRevenuesList.get(position).getStrCategory());
        holder.item_srcp_percent_tv.setText(categoryRevenuesList.get(position).getStrPercent() + "%");
        holder.item_srcp_amount_tv.setText("RM " + categoryRevenuesList.get(position).getStrTotal());
        holder.category_progress.setProgress(Integer.parseInt(categoryRevenuesList.get(position).getStrPercent()));
            }

    @Override
    public int getItemCount() {
        return categoryRevenuesList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView item_srcp_tv, item_srcp_percent_tv, item_srcp_amount_tv;
        ProgressBar category_progress;

        ViewHolder(View itemView) {
            super(itemView);

            item_srcp_tv = (TextView) itemView.findViewById(R.id.item_srcp_tv);
            item_srcp_percent_tv = (TextView) itemView.findViewById(R.id.item_srcp_percent_tv);
            item_srcp_amount_tv = (TextView) itemView.findViewById(R.id.item_srcp_amount_tv);
            category_progress = (ProgressBar) itemView.findViewById(R.id.category_progress);

        }
    }
}
