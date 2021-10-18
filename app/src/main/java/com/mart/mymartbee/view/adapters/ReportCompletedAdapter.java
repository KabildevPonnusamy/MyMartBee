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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_completed_reports, parent,
                false);
        return new ViewHolder(v);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.report_no.setText(reportsLists.get(position).getStrOrderId());
        String product_price = reportsLists.get(position).getStrTotalAmount().replace(".00", "");
        holder.report_total.setText("RM. " + product_price);
        strName = CommonMethods.getContactName(context, reportsLists.get(position).getStrPhone());
        if (strName.equalsIgnoreCase("")) {
            holder.report_user_mobile.setText(reportsLists.get(position).getStrCountryCode() + " " +
                    reportsLists.get(position).getStrPhone());
        } else {
            holder.report_user_mobile.setText(strName);
        }

        holder.report_payment_type.setText(reportsLists.get(position).getStrPaymentType());
        String strDate = "";
        for (int i = 0; i < reportsLists.get(position).getOrderHistoryList().size(); i++) {
            strDate = reportsLists.get(position).getOrderHistoryList().get(i).getStrDateAdded();
        }
        holder.report_completed_date.setText(formatDate(strDate));
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
        TextView report_no, report_total, report_payment_type, report_user_mobile,
                report_completed_date;

        ViewHolder(View itemView) {
            super(itemView);
            report_completed_date = (TextView) itemView.findViewById(R.id.report_completed_date);
            report_user_mobile = (TextView) itemView.findViewById(R.id.report_user_mobile);
            report_no = (TextView) itemView.findViewById(R.id.report_no);
            report_total = (TextView) itemView.findViewById(R.id.report_total);
            report_payment_type = (TextView) itemView.findViewById(R.id.report_payment_type);
        }
    }
}
