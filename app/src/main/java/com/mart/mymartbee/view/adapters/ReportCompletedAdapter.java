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
import com.mart.mymartbee.commons.CommonMethods;
import com.mart.mymartbee.model.Orders_Model;
import com.mart.mymartbee.model.Reports_Model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ReportCompletedAdapter extends RecyclerView.Adapter<ReportCompletedAdapter.ViewHolder> {

    private ArrayList<Reports_Model.ReportsList> reportsLists;
    private Context context;
    String strName = "";

    public ReportCompletedAdapter(ArrayList<Reports_Model.ReportsList> reportsLists, Context context) {
        this.reportsLists = reportsLists;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comp_reports_new, parent,
                false);
        return new ViewHolder(v);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.order_number.setText("#" + reportsLists.get(position).getStrOrderId() );
        holder.order_date.setText(formatDate(reportsLists.get(position).getStrOrderedDate()));
        holder.order_status.setText(reportsLists.get(position).getStrStatus());
        strName = CommonMethods.getContactName(context,  reportsLists.get(position).getStrPhone());
        if(strName.equalsIgnoreCase("")) {
            holder.ordered_mobile_number.setText(reportsLists.get(position).getStrCountryCode() + " " +
                    reportsLists.get(position).getStrPhone());
        } else {
            holder.ordered_mobile_number.setText(strName);
        }

        String product_price = reportsLists.get(position).getStrTotalAmount().replace(".00", "");
        holder.order_total_price.setText( "RM. " + product_price);

        if(reportsLists.get(position).getProductsList().size() > 0 ) {

            if(reportsLists.get(position).getProductsList().size() == 1) {
                holder.order_quantity.setText(reportsLists.get(position).getProductsList().size() + " Item");
            } else {
                holder.order_quantity.setText(reportsLists.get(position).getProductsList().size() + " Items");
            }

            String image = reportsLists.get(position).getProductsList().get(0).getStrImage();
            if(image != null) {
                if(!image.equalsIgnoreCase("")) {
                    Glide.with(context).load(image).into(holder.order_image);
                }
            }
        }


    }

    private String formatDate(String inputDate) {
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
        return reportsLists.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        /*TextView report_no, report_total, report_payment_type, report_user_mobile,
                report_completed_date;*/

        TextView order_number, order_date, order_quantity, order_total_price,
                ordered_mobile_number, order_status;
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



            /*report_completed_date = (TextView) itemView.findViewById(R.id.report_completed_date);
            report_user_mobile = (TextView) itemView.findViewById(R.id.report_user_mobile);
            report_no = (TextView) itemView.findViewById(R.id.report_no);
            report_total = (TextView) itemView.findViewById(R.id.report_total);
            report_payment_type = (TextView) itemView.findViewById(R.id.report_payment_type);*/

        }
    }
}
