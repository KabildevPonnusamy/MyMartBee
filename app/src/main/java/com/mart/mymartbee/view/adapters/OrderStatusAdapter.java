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

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.mart.mymartbee.R;
import com.mart.mymartbee.model.Order_Status_Model;
import com.mart.mymartbee.model.SubCategory_Model;

import java.util.ArrayList;

public class OrderStatusAdapter extends RecyclerView.Adapter<OrderStatusAdapter.ViewHolder> {

    private ArrayList<Order_Status_Model.OrdersStatusList> ordersStatusLists;
    private Context context;
    private int selectedId;

    public OrderStatusAdapter(ArrayList<Order_Status_Model.OrdersStatusList> ordersStatusLists, Context context, int selectedId) {
        this.ordersStatusLists = ordersStatusLists;
        this.context = context;
        this.selectedId = selectedId;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_orderstatus_list, parent,
                false);
        return new ViewHolder(v);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.orderstatus_title.setText(ordersStatusLists.get(position).getStrOrderStatusName());

        if (selectedId == Integer.parseInt(ordersStatusLists.get(position).getStrOrderStatusId())) {
            holder.orderstatus_border.setBackgroundResource(R.drawable.order_status_selected);
            holder.orderstatus_title.setTypeface(null, Typeface.BOLD);
            holder.orderstatus_title.setTextColor(ContextCompat.getColor(context, R.color.white));
        } else {
            holder.orderstatus_title.setTextColor(ContextCompat.getColor(context, R.color.black));
            holder.orderstatus_border.setBackgroundResource(R.drawable.order_status_unselected);
            holder.orderstatus_title.setTypeface(null, Typeface.NORMAL);
        }
    }

    @Override
    public int getItemCount() {
        return ordersStatusLists.size();
    }

    public void updateAdapter(int selectedId) {
        this.selectedId = selectedId;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView orderstatus_title;
        LinearLayout orderstatus_border;

        ViewHolder(View itemView) {
            super(itemView);
            orderstatus_title = (TextView) itemView.findViewById(R.id.orderstatus_title);
            orderstatus_border = (LinearLayout) itemView.findViewById(R.id.orderstatus_border);

        }
    }
}
