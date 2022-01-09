package com.mart.mymartbee.view.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mart.mymartbee.R;
import com.mart.mymartbee.commons.CommonMethods;
import com.mart.mymartbee.model.Dashboard_Model;
import com.mart.mymartbee.model.Orders_Model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NewOrdersAdapter extends RecyclerView.Adapter<NewOrdersAdapter.ViewHolder> {

    private ArrayList<Orders_Model.OrdersList> pendingOrdersLists;
    private Context context;
    String strName = "";

    public NewOrdersAdapter(ArrayList<Orders_Model.OrdersList> pendingOrdersLists, Context context) {
        this.pendingOrdersLists = pendingOrdersLists;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_orders_new, parent,
                false);
        return new ViewHolder(v);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.order_number.setText("#" + pendingOrdersLists.get(position).getStrOrderId() );
        holder.order_date.setText(formatDate(pendingOrdersLists.get(position).getStrOrderDate()));
        holder.order_status.setText(pendingOrdersLists.get(position).getStrStatus());

        if(pendingOrdersLists.get(position).getStrPaymentType().equalsIgnoreCase("cod")) {
            holder.order_payment_type.setText("Cash on Delivery");
        } else if(pendingOrdersLists.get(position).getStrPaymentType().equalsIgnoreCase("bank_transfer")) {
            holder.order_payment_type.setText("Bank Transfer");
        } else {
            holder.order_payment_type.setText(pendingOrdersLists.get(position).getStrPaymentType());
        }
        strName = CommonMethods.getContactName(context,  pendingOrdersLists.get(position).getStrPhone());
        if(strName.equalsIgnoreCase("")) {
            holder.ordered_mobile_number.setText(pendingOrdersLists.get(position).getStrCountryCode() + " " +
                    pendingOrdersLists.get(position).getStrPhone());
        } else {
            holder.ordered_mobile_number.setText(strName);
        }

        if(pendingOrdersLists.get(position).getOrderedProductsList().size() == 1) {
            holder.order_quantity.setText(pendingOrdersLists.get(position).getOrderedProductsList().size() + " Item");
        } else {
            holder.order_quantity.setText(pendingOrdersLists.get(position).getOrderedProductsList().size() + " Items");
        }

        String product_price = pendingOrdersLists.get(position).getStrTotalAmount().replace(".00", "");
        holder.order_total_price.setText( "RM " + product_price);

        if(pendingOrdersLists.get(0).getOrderedProductsList().size() > 0) {
            try {

                String image = pendingOrdersLists.get(position).getOrderedProductsList().get(0).getStrProductImage();
                if (image != null) {
                    if (!image.equalsIgnoreCase("")) {
                        Glide.with(context).load(image).into(holder.order_image);
                    }
                }

            } catch (Exception e) {
                Log.e("appSample", "Excep: " + e.getMessage());
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

        TextView order_number, order_date, order_quantity, order_total_price,
                ordered_mobile_number, order_status, order_payment_type;
        ImageView order_image;

        ViewHolder(View itemView) {
            super(itemView);

            order_image = itemView.findViewById(R.id.order_image);
            order_number = itemView.findViewById(R.id.order_number);
            order_date = itemView.findViewById(R.id.order_date);
            order_quantity = itemView.findViewById(R.id.order_quantity);
            order_total_price = itemView.findViewById(R.id.order_total_price);
            ordered_mobile_number = itemView.findViewById(R.id.ordered_mobile_number);
            order_status = itemView.findViewById(R.id.order_status);
            order_payment_type = itemView.findViewById(R.id.order_payment_type);

        }
    }
}
