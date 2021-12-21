package com.mart.mymartbee.view;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mart.mymartbee.R;
import com.mart.mymartbee.commons.CommonMethods;
import com.mart.mymartbee.constants.Constants;
import com.mart.mymartbee.model.Reports_Model;
import com.mart.mymartbee.storage.StorageDatas;
import com.mart.mymartbee.view.adapters.OrdersDetailsAdapter;
import com.mart.mymartbee.view.adapters.ReportDetailsAdapter;
import com.mart.mymartbee.view.adapters.ReportOrderHistoryAdapter;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ReportDetails extends AppCompatActivity implements View.OnClickListener, Constants {

    ImageView report_details_back, reason_arrow, product_arrow, orderhis_arrow;
    TextView report_number, ordered_user_number, ordered_address, order_status_time,
            report_reason_text, total_amount, status_text;
    RecyclerView report_product_recycler, orderhis_recyclerview;
    LinearLayout report_products_layout, orderhis_layout, reason_layout;
    RelativeLayout reject_reason_layout, prod_summary_layout, order_his_layout;
    Reports_Model.ReportsList reportsList;
    ReportDetailsAdapter ordersDetailsAdapter;
    ReportOrderHistoryAdapter reportOrderHistoryAdapter;
    String strName = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_details);

        initView();
        getStorageDatas();

    }

    public void initView() {
        order_his_layout = findViewById(R.id.order_his_layout);
        prod_summary_layout = findViewById(R.id.prod_summary_layout);
        reject_reason_layout = findViewById(R.id.reject_reason_layout);
        report_details_back = findViewById(R.id.report_details_back);
        reason_arrow = findViewById(R.id.reason_arrow);
        product_arrow = findViewById(R.id.product_arrow);
        orderhis_arrow = findViewById(R.id.orderhis_arrow);
        report_number = findViewById(R.id.report_number);
        ordered_user_number = findViewById(R.id.ordered_user_number);
        ordered_address = findViewById(R.id.ordered_address);
        order_status_time = findViewById(R.id.order_status_time);
        report_reason_text = findViewById(R.id.report_reason_text);
        total_amount = findViewById(R.id.total_amount);
        status_text = findViewById(R.id.status_text);
        report_product_recycler = findViewById(R.id.report_product_recycler);
        orderhis_recyclerview = findViewById(R.id.orderhis_recyclerview);
        report_products_layout = findViewById(R.id.report_products_layout);
        reason_layout = findViewById(R.id.reason_layout);
        orderhis_layout = findViewById(R.id.orderhis_layout);

        ViewCompat.setNestedScrollingEnabled(report_product_recycler, false);
        ViewCompat.setNestedScrollingEnabled(orderhis_recyclerview, false);

        reason_layout.setVisibility(View.GONE);
        report_reason_text.setVisibility(View.GONE);
        report_products_layout.setVisibility(View.GONE);
        orderhis_layout.setVisibility(View.GONE);

        setListeners();
    }

    public void setListeners() {
        report_details_back.setOnClickListener(this);
        reject_reason_layout.setOnClickListener(this);
        prod_summary_layout.setOnClickListener(this);
        order_his_layout.setOnClickListener(this);
    }

    public void getStorageDatas() {
        reportsList = StorageDatas.getInstance().getReportListObj();
        setValues();
    }

    public void setValues() {
        strName = CommonMethods.getContactName(getApplicationContext(), reportsList.getStrPhone());
        if(strName.equalsIgnoreCase("")) {
            ordered_user_number.setText(reportsList.getStrCountryCode() + " " + reportsList.getStrPhone());
        } else {
            ordered_user_number.setText(strName);
        }

        report_number.setText(reportsList.getStrOrderId());
        ordered_address.setText(reportsList.getStrAddress());
        String priceAmount = reportsList.getStrTotalAmount().replace(".00", "");
        total_amount.setText("RM " + priceAmount);
        String strDate = "";

        for (int i=0; i<reportsList.getOrderHistoryList().size(); i++) {
            strDate = reportsList.getOrderHistoryList().get(i).getStrDateAdded();
        }

        if(reportsList.getStrStatus().equalsIgnoreCase("Completed")) {
            status_text.setText(getResources().getString(R.string.delivered_on));
            reason_layout.setVisibility(View.GONE);
        } else {
            status_text.setText(getResources().getString(R.string.rejected_on));
            reason_layout.setVisibility(View.VISIBLE);
            report_reason_text.setText(reportsList.getStrSellerComment());
        }
        order_status_time.setText(formatDate(strDate));

        if (reportsList.getProductsList() != null) {
            if (reportsList.getProductsList().size() > 0) {
                report_product_recycler.setHasFixedSize(true);
                report_product_recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                ordersDetailsAdapter = new ReportDetailsAdapter(reportsList.getProductsList(), getApplicationContext());
                report_product_recycler.setAdapter(ordersDetailsAdapter);
            }
        }

        if(reportsList.getOrderHistoryList() != null) {
            if(reportsList.getOrderHistoryList().size() > 0) {
                orderhis_recyclerview.setHasFixedSize(true);
                orderhis_recyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                reportOrderHistoryAdapter = new ReportOrderHistoryAdapter(reportsList.getOrderHistoryList(), getApplicationContext());
                orderhis_recyclerview.setAdapter(reportOrderHistoryAdapter);
            }
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.report_details_back:
                finish();
                break;

            case R.id.reject_reason_layout:
                if(report_reason_text.getVisibility() == View.VISIBLE) {
                    report_reason_text.setVisibility(View.GONE);
                    reason_arrow.setImageResource(getImage("arrow_up"));
                } else {
                    report_reason_text.setVisibility(View.VISIBLE);
                    reason_arrow.setImageResource(getImage("arrow_down"));
                }
                break;

            case R.id.prod_summary_layout:
                if(report_products_layout.getVisibility() == View.VISIBLE) {
                    report_products_layout.setVisibility(View.GONE);
                    product_arrow.setImageResource(getImage("arrow_up"));
                } else {
                    report_products_layout.setVisibility(View.VISIBLE);
                    product_arrow.setImageResource(getImage("arrow_down"));
                }
                break;

            case R.id.order_his_layout:
                if(orderhis_layout.getVisibility() == View.VISIBLE) {
                    orderhis_layout.setVisibility(View.GONE);
                    orderhis_arrow.setImageResource(getImage("arrow_up"));
                } else {
                    orderhis_layout.setVisibility(View.VISIBLE);
                    orderhis_arrow.setImageResource(getImage("arrow_down"));
                }
                break;

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

    public int getImage(String imageName) {
        int drawableResourceId = getResources().getIdentifier(imageName, "drawable", getPackageName());
        return drawableResourceId;
    }
}
