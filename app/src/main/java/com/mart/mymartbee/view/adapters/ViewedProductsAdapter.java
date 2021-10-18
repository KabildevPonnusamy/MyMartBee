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
import com.mart.mymartbee.model.Products_Model;

import java.util.ArrayList;

public class ViewedProductsAdapter extends RecyclerView.Adapter<ViewedProductsAdapter.ViewHolder> {

    private ArrayList<Dashboard_Model.ViewedProductList> viewedProductLists;
    private Context context;

    public ViewedProductsAdapter(ArrayList<Dashboard_Model.ViewedProductList> viewedProductLists, Context context) {
        this.viewedProductLists = viewedProductLists;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_viewedproducts, parent,
                false);
        return new ViewHolder(v);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.itm_viewedproduct_name.setText(viewedProductLists.get(position).getStrProductName());
        holder.itm_viewedproduct_count.setText(viewedProductLists.get(position).getStrViewed());
        Glide.with(context).load(viewedProductLists.get(position).getStrProductImage()).into(holder.itm_viewedproduct_image);
    }

    @Override
    public int getItemCount() {
        return viewedProductLists.size();
    }

    public void updateAdapter(ArrayList<Dashboard_Model.ViewedProductList> viewedProductLists) {
        this.viewedProductLists = viewedProductLists;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView itm_viewedproduct_image;
        TextView itm_viewedproduct_name, itm_viewedproduct_count;

        ViewHolder(View itemView) {
            super(itemView);
            itm_viewedproduct_image = (ImageView) itemView.findViewById(R.id.itm_viewedproduct_image);
            itm_viewedproduct_count = (TextView) itemView.findViewById(R.id.itm_viewedproduct_count);
            itm_viewedproduct_name = (TextView) itemView.findViewById(R.id.itm_viewedproduct_name);
        }
    }
}
