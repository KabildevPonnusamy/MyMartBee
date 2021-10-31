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

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
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

        if (ordersStatusLists.get(position).getStrOrderStatusCount() == null) {
            holder.orderstatus_count.setText("(0)");
        } else {
            holder.orderstatus_count.setText("(" + ordersStatusLists.get(position).getStrOrderStatusCount() + ")");
        }

        Typeface typeface = ResourcesCompat.getFont(context, R.font.museosans);

        if (selectedId == Integer.parseInt(ordersStatusLists.get(position).getStrOrderStatusId())) {
//            holder.orderstatus_border.setBackgroundResource(R.drawable.order_status_selected);
            holder.status_cardView.setCardBackgroundColor(context.getResources().getColor(R.color.white));
            holder.orderstatus_title.setTypeface(typeface, Typeface.BOLD);
            holder.orderstatus_title.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));

            holder.orderstatus_count.setTypeface(typeface, Typeface.BOLD);
            holder.orderstatus_count.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
        } else {
//            holder.orderstatus_border.setBackgroundResource(R.drawable.order_status_unselected);
            holder.status_cardView.setCardBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
            holder.orderstatus_title.setTextColor(ContextCompat.getColor(context, R.color.white));
            holder.orderstatus_title.setTypeface(typeface, Typeface.NORMAL);

            holder.orderstatus_count.setTextColor(ContextCompat.getColor(context, R.color.white));
            holder.orderstatus_count.setTypeface(typeface, Typeface.NORMAL);
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

        TextView orderstatus_title, orderstatus_count;
        LinearLayout orderstatus_border;
        CardView status_cardView;

        ViewHolder(View itemView) {
            super(itemView);
            orderstatus_count = (TextView) itemView.findViewById(R.id.orderstatus_count);
            orderstatus_title = (TextView) itemView.findViewById(R.id.orderstatus_title);
            orderstatus_border = (LinearLayout) itemView.findViewById(R.id.orderstatus_border);
            status_cardView = (CardView) itemView.findViewById(R.id.status_cardView);

        }
    }
}
