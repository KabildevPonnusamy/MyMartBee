package com.mart.mymartbee.view.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mart.mymartbee.R;
import com.mart.mymartbee.commons.CustomLinearLayoutManager;
import com.mart.mymartbee.model.Products_Model;
import com.mart.mymartbee.view.fragments.HomeFragment;
import com.mart.mymartbee.view.fragments.ProductsFragment;

import java.util.ArrayList;

public class SubCateTitleAdapter extends RecyclerView.Adapter<SubCateTitleAdapter.ViewHolder> {

    private ArrayList<Products_Model.ProductCategories> productsArrayList;
    private Context context;
    ProductsFragment productFragmentObj;
    LinearLayoutManager layoutManager;

    public SubCateTitleAdapter(ArrayList<Products_Model.ProductCategories> productsArrayList, Context context,
                               ProductsFragment productFragmentObj, LinearLayoutManager layoutManager) {
        this.productsArrayList = productsArrayList;
        this.context = context;
        this.productFragmentObj = productFragmentObj;
        this.layoutManager = layoutManager;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_subcategory_title, parent,
                false);
        return new ViewHolder(v);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.product_title.setText(productsArrayList.get(position).getStrCateName());
        boolean isExpanded = productsArrayList.get(position).isExpanded();

        if(isExpanded) {
            holder.product_arrow.setImageResource(getImage("arrow_down"));
            holder.subcate_item_layout.setBackgroundResource(R.drawable.homesubcate_selected);
            holder.product_title.setTextColor(ContextCompat.getColor(context, R.color.white));
        } else {
            holder.product_arrow.setImageResource(getImage("arrow_up"));
            holder.subcate_item_layout.setBackgroundResource(R.drawable.homesubcate_unselected);
            holder.product_title.setTextColor(ContextCompat.getColor(context, R.color.black));
        }
        holder.products_list_layout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);

        ArrayList<Products_Model.ProductCategories.ProductsList> prodItems = new ArrayList<Products_Model.ProductCategories.ProductsList>();
        prodItems.clear();

        if(productsArrayList.get(position).getProductsLists().size() > 0) {
            prodItems = productsArrayList.get(position).getProductsLists();
        } else {
            prodItems = new ArrayList<Products_Model.ProductCategories.ProductsList>();
        }

        holder.product_view_option.setVisibility(prodItems.size() > 4 ? View.VISIBLE : View.GONE);

        Log.e("appSample", "Size: " + prodItems.size());
        holder.my_products_recycle.setHasFixedSize(true);
//        holder.my_products_recycle.setLayoutManager(new GridLayoutManager(context, 2));
        holder.my_products_recycle.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        holder.homeProductsAdapter= new HomeProductsAdapter(prodItems, context, productFragmentObj,
                productsArrayList.get(position).getStrCateName(), productsArrayList.get(position).getStrCateId());
        holder.my_products_recycle.setAdapter(holder.homeProductsAdapter);
    }

    @Override
    public int getItemCount() {
        return productsArrayList.size();
    }

    public void updateAdapter(ArrayList<Products_Model.ProductCategories> productsArrayList) {
        this.productsArrayList = productsArrayList;
        notifyDataSetChanged();
    }

    public int getImage(String imageName) {
        int drawableResourceId = context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());
        return drawableResourceId;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView product_arrow;
        TextView product_title, product_view_option;
        RelativeLayout subcate_item_layout;
        LinearLayout products_list_layout;
        RecyclerView my_products_recycle;
        HomeProductsAdapter homeProductsAdapter;

        ViewHolder(View itemView) {
            super(itemView);
            product_arrow = (ImageView) itemView.findViewById(R.id.product_arrow);
            product_title = (TextView) itemView.findViewById(R.id.product_title);
            product_view_option = (TextView) itemView.findViewById(R.id.product_view_option);
            subcate_item_layout = (RelativeLayout) itemView.findViewById(R.id.subcate_item_layout);
            products_list_layout = (LinearLayout) itemView.findViewById(R.id.products_list_layout);
            my_products_recycle = (RecyclerView) itemView.findViewById(R.id.my_products_recycle);

            product_view_option.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((ProductsFragment) productFragmentObj).movetoProductViewAll(productsArrayList.get(getAdapterPosition()).getProductsLists(),
                            productsArrayList.get(getAdapterPosition()).getStrCateName(), productsArrayList.get(getAdapterPosition()).getStrCateId());
                }
            });

            subcate_item_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Products_Model.ProductCategories products = productsArrayList.get(getAdapterPosition());
                    products.setExpanded(!products.isExpanded());
                    notifyItemChanged(getAdapterPosition());
                }
            });
        }
    }
}
