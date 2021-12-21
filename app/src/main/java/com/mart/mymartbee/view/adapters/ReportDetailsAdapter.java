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
import com.mart.mymartbee.model.Orders_Model;
import com.mart.mymartbee.model.Reports_Model;

import java.util.ArrayList;

public class ReportDetailsAdapter extends RecyclerView.Adapter<ReportDetailsAdapter.ViewHolder> {

    private ArrayList<Reports_Model.ReportsProducts> reportProductsArrayList;
    private Context context;

    public ReportDetailsAdapter(ArrayList<Reports_Model.ReportsProducts> reportProductsArrayList, Context context) {
        this.reportProductsArrayList = reportProductsArrayList;
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
        holder.od_prod_name.setText(reportProductsArrayList.get(position).getStrTitle());
        holder.od_prod_qty.setText(reportProductsArrayList.get(position).getStrQuantity());
        String strPrice = reportProductsArrayList.get(position).getStrPrice().replace(".00", "");
        holder.od_prod_price.setText("RM " + strPrice);
        String strTotal = reportProductsArrayList.get(position).getStrTotal().replace(".00", "");
        holder.od_prod_total.setText(strTotal);
        Glide.with(context).load(reportProductsArrayList.get(position).getStrImage()).into(holder.product_image);
    }

    @Override
    public int getItemCount() {
        return reportProductsArrayList.size();
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
