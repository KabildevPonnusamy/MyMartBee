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
import com.mart.mymartbee.model.Products_Model;
import com.mart.mymartbee.view.fragments.CategoriesFragment;

import java.util.ArrayList;

public class HomeProductsAdapter extends RecyclerView.Adapter<HomeProductsAdapter.ViewHolder> {

    private ArrayList<Products_Model.ProductCategories.ProductsList> productsArrayList;
    private Context context;
//    HomeFragment homeFragmentObj;
    CategoriesFragment productFragmentObj;
    String strSubCateName;
    String strSubCateId;

    public HomeProductsAdapter(ArrayList<Products_Model.ProductCategories.ProductsList> productsArrayList, Context context,
                               CategoriesFragment productFragmentObj, String strSubCateName, String strSubCateId) {
        this.productsArrayList = productsArrayList;
        this.context = context;
        this.productFragmentObj = productFragmentObj;
        this.strSubCateName = strSubCateName;
        this.strSubCateId = strSubCateId;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_productslist_home, parent,
                false);
        return new ViewHolder(v);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        String product_price = productsArrayList.get(position).getStrProduct_price().replace(".00", "");
        holder.itm_product_price.setText(product_price);
        holder.itm_product_name.setText(productsArrayList.get(position).getStrProduct_title());
        Glide.with(context).load(productsArrayList.get(position).getStrProduct_image()).into(holder.itm_product_image);
    }

    @Override
    public int getItemCount() {
        if(productsArrayList.size() > 4) {
            return 4;
        } else {
            return productsArrayList.size();
        }

    }

    public void updateAdapter(ArrayList<Products_Model.ProductCategories.ProductsList> productsArrayList) {
        this.productsArrayList = productsArrayList;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView itm_product_image;
        TextView itm_product_price, itm_product_name;
        LinearLayout item_product_layout;

        ViewHolder(View itemView) {
            super(itemView);
            itm_product_image = (ImageView) itemView.findViewById(R.id.itm_product_image);
            itm_product_price = (TextView) itemView.findViewById(R.id.itm_product_price);
            itm_product_name = (TextView) itemView.findViewById(R.id.itm_product_name);
            item_product_layout = (LinearLayout) itemView.findViewById(R.id.item_product_layout);

            item_product_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((CategoriesFragment) productFragmentObj).movetoProductEdit(productsArrayList.get(getAdapterPosition()),
                            strSubCateName, strSubCateId);
                }
            });

        }
    }
}
