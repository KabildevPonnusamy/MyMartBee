package com.mart.mymartbee.view.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mart.mymartbee.R;
import com.mart.mymartbee.model.Orders_Model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class OrdersDetailsAdapter extends RecyclerView.Adapter<OrdersDetailsAdapter.ViewHolder> {

    private ArrayList<Orders_Model.OrdersList.OrderedProducts> orderedProductsArrayList;
    private Context context;

    public OrdersDetailsAdapter(ArrayList<Orders_Model.OrdersList.OrderedProducts> orderedProductsArrayList, Context context) {
        this.orderedProductsArrayList = orderedProductsArrayList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_details, parent,
                false);
        return new ViewHolder(v);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.od_prod_name.setText(orderedProductsArrayList.get(position).getStrProductTitle());
        holder.od_prod_qty.setText(orderedProductsArrayList.get(position).getStrProductQuantity());
        String product_price = orderedProductsArrayList.get(position).getStrProductPrice().replace(".00", "");
        holder.od_prod_price.setText("RM. " + product_price);
        String strTotal = orderedProductsArrayList.get(position).getStrProductTotal().replace(".00", "");
        holder.od_prod_total.setText(strTotal);
        Glide.with(context).load(orderedProductsArrayList.get(position).getStrProductImage()).into(holder.product_image);
    }

    @Override
    public int getItemCount() {
        return orderedProductsArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView product_image;
        TextView od_prod_name, od_prod_qty, od_prod_price, od_prod_total;

        ViewHolder(View itemView) {
            super(itemView);
            od_prod_name = (TextView) itemView.findViewById(R.id.od_prod_name);
            od_prod_qty = (TextView) itemView.findViewById(R.id.od_prod_qty);
            od_prod_price = (TextView) itemView.findViewById(R.id.od_prod_price);
            od_prod_total = (TextView) itemView.findViewById(R.id.od_prod_total);
            product_image = (ImageView) itemView.findViewById(R.id.product_image);
        }
    }
}
