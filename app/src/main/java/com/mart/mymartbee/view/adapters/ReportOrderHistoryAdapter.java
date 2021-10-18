package com.mart.mymartbee.view.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.github.vipulasri.timelineview.TimelineView;
import com.mart.mymartbee.R;
import com.mart.mymartbee.model.Reports_Model;
import com.mart.mymartbee.model.ShopStatusModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ReportOrderHistoryAdapter extends RecyclerView.Adapter<ReportOrderHistoryAdapter.TimeLineViewHolder> {

    private ArrayList<Reports_Model.OrderHistory> orderHistoryArrayList;
    private Context context;

    public ReportOrderHistoryAdapter(ArrayList<Reports_Model.OrderHistory> orderHistoryArrayList, Context context) {
        this.orderHistoryArrayList = orderHistoryArrayList;
        this.context = context;
    }

    @Override
    public TimeLineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_timeline_history, parent,
                false);
        return new TimeLineViewHolder(v, viewType);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(final TimeLineViewHolder holder, final int position) {
        holder.status_name.setText(orderHistoryArrayList.get(position).getStrName());
        holder.status_date.setText(formatDate(orderHistoryArrayList.get(position).getStrDateAdded()));
    }

    private String formatDate(String inputDate) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date d = formatter.parse(inputDate);
            SimpleDateFormat formatter2 = new SimpleDateFormat("dd MMM yyyy, hh:mm aaa.");
            return "" + formatter2.format(d);
        } catch (Exception e) {
            return "";
        }
    }

    @Override
    public int getItemCount() {
        return orderHistoryArrayList.size();
    }

    class TimeLineViewHolder extends RecyclerView.ViewHolder {

        TimelineView timelineView;
        TextView status_name, status_date;

        TimeLineViewHolder(View itemView, int viewType) {
            super(itemView);
            timelineView = (TimelineView) itemView.findViewById(R.id.timelineView);
            status_name = (TextView) itemView.findViewById(R.id.status_name);
            status_date = (TextView) itemView.findViewById(R.id.status_date);
            timelineView.initLine(viewType);

        }
    }
}
