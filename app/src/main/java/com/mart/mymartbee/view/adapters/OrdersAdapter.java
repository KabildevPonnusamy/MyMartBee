package com.mart.mymartbee.view.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
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
import com.mart.mymartbee.R;
import com.mart.mymartbee.commons.CommonMethods;
import com.mart.mymartbee.model.Category_Model;
import com.mart.mymartbee.model.Orders_Model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.ViewHolder> {

    private ArrayList<Orders_Model.OrdersList> ordersLists;
    private Context context;
    String strName = "";

    public OrdersAdapter(ArrayList<Orders_Model.OrdersList> ordersLists, Context context) {
        this.ordersLists = ordersLists;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_orders, parent,
                false);
        return new ViewHolder(v);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.order_no.setText(ordersLists.get(position).getStrOrderId());
        holder.order_status.setText(ordersLists.get(position).getStrStatus());

        strName = CommonMethods.getContactName(context,  ordersLists.get(position).getStrPhone());
        if(strName.equalsIgnoreCase("")) {
            holder.ordered_user_mobile.setText(ordersLists.get(position).getStrCountryCode() + " " +
                    ordersLists.get(position).getStrPhone());
        } else {
            holder.ordered_user_mobile.setText(strName);
        }

        String product_price = ordersLists.get(position).getStrTotalAmount().replace(".00", "");
        holder.order_total.setText("RM. " + product_price);
        holder.status_layout.setBackgroundResource(R.drawable.pending_layout);
    }


    @Override
    public int getItemCount() {
        return ordersLists.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView order_no, order_total, order_status, ordered_user_mobile;
        LinearLayout status_layout;

        ViewHolder(View itemView) {
            super(itemView);

            ordered_user_mobile = (TextView) itemView.findViewById(R.id.ordered_user_mobile);
            order_no = (TextView) itemView.findViewById(R.id.order_no);
            order_total = (TextView) itemView.findViewById(R.id.order_total);
            order_status = (TextView) itemView.findViewById(R.id.order_status);
            status_layout = (LinearLayout) itemView.findViewById(R.id.status_layout);

//            ordered_address = (TextView) itemView.findViewById(R.id.ordered_address);
//            order_items_count = (TextView) itemView.findViewById(R.id.order_items_count);
//            order_time = (TextView) itemView.findViewById(R.id.order_time);

        }
    }
}
