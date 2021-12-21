package com.mart.mymartbee.view.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mart.mymartbee.R;
import com.mart.mymartbee.commons.CommonMethods;
import com.mart.mymartbee.model.Dashboard_Model;

import java.util.ArrayList;

public class PendingOrdersAdapter extends RecyclerView.Adapter<PendingOrdersAdapter.ViewHolder> {

    private ArrayList<Dashboard_Model.PendingOrdersList> pendingOrdersLists;
    private Context context;
    String strName = "";

    public PendingOrdersAdapter(ArrayList<Dashboard_Model.PendingOrdersList> pendingOrdersLists, Context context) {
        this.pendingOrdersLists = pendingOrdersLists;
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
        holder.order_no.setText(pendingOrdersLists.get(position).getStrOrderId());
        strName = CommonMethods.getContactName(context,  pendingOrdersLists.get(position).getStrPhone());
        if(strName.equalsIgnoreCase("")) {
            holder.ordered_user_mobile.setText(pendingOrdersLists.get(position).getStrCountryCode() + " " +
                    pendingOrdersLists.get(position).getStrPhone());
        } else {
            holder.ordered_user_mobile.setText(strName);
        }

        String product_price = pendingOrdersLists.get(position).getStrTotalAmount().replace(".00", "");
        holder.order_total.setText("RM " + product_price);

        holder.order_status.setText(pendingOrdersLists.get(position).getStrStatus());
        holder.status_layout.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return pendingOrdersLists.size();
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

        }
    }
}
