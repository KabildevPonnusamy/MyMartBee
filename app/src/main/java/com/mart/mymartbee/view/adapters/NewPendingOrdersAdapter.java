package com.mart.mymartbee.view.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mart.mymartbee.R;
import com.mart.mymartbee.model.Dashboard_Model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NewPendingOrdersAdapter extends RecyclerView.Adapter<NewPendingOrdersAdapter.ViewHolder> {

    private ArrayList<Dashboard_Model.PendingOrdersList> pendingOrdersLists;
    private Context context;

    public NewPendingOrdersAdapter(ArrayList<Dashboard_Model.PendingOrdersList> pendingOrdersLists, Context context) {
        this.pendingOrdersLists = pendingOrdersLists;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pendingorders_new, parent,
                false);
        return new ViewHolder(v);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.order_number.setText("#" + pendingOrdersLists.get(position).getStrOrderId() );
        holder.order_date.setText(formatDate(pendingOrdersLists.get(position).getStrOrderDate()));

        if(pendingOrdersLists.get(position).getPendingProductsList().size() == 1) {
            holder.order_quantity.setText(pendingOrdersLists.get(position).getPendingProductsList().size() + " Item");
        } else {
            holder.order_quantity.setText(pendingOrdersLists.get(position).getPendingProductsList().size() + " Items");
        }

        String product_price = pendingOrdersLists.get(position).getStrTotalAmount().replace(".00", "");
        holder.order_total_price.setText( "RM. " + product_price);
        String image = pendingOrdersLists.get(position).getPendingProductsList().get(0).getStrProductImage();

        if(image != null) {
            if(!image.equalsIgnoreCase("")) {
                Glide.with(context).load(image).into(holder.order_image);
            }
        }
    }

    public String formatDate(String inputDate) {
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
        return pendingOrdersLists.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView order_number, order_date, order_quantity, order_total_price;
        ImageView order_image;

        ViewHolder(View itemView) {
            super(itemView);

            order_image = itemView.findViewById(R.id.order_image);
            order_number = itemView.findViewById(R.id.order_number);
            order_date = itemView.findViewById(R.id.order_date);
            order_quantity = itemView.findViewById(R.id.order_quantity);
            order_total_price = itemView.findViewById(R.id.order_total_price);

        }
    }
}
