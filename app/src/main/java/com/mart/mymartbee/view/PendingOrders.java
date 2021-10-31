package com.mart.mymartbee.view;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mart.mymartbee.R;
import com.mart.mymartbee.algorithm.TripleDes;
import com.mart.mymartbee.commons.CommonMethods;
import com.mart.mymartbee.constants.Constants;
import com.mart.mymartbee.custom.HintAdapter;
import com.mart.mymartbee.custom.NetworkAvailability;
import com.mart.mymartbee.custom.SweetAlert.SweetAlertDialog;
import com.mart.mymartbee.model.Order_Status_Model;
import com.mart.mymartbee.model.Orders_Model;
import com.mart.mymartbee.storage.MyPreferenceDatas;
import com.mart.mymartbee.storage.StorageDatas;
import com.mart.mymartbee.view.adapters.OrderHistoryAdapter;
import com.mart.mymartbee.view.adapters.OrdersDetailsAdapter;
import com.mart.mymartbee.viewmodel.implementor.OrdersViewModelImpl;
import com.mart.mymartbee.viewmodel.interfaces.OrdersViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PendingOrders extends AppCompatActivity implements View.OnClickListener, Constants {

    ImageView ord_details_back;
    RecyclerView orders_details_recycler, orders_history_recycler;
    RelativeLayout total_amount_layout;
    TextView total_amount;
    Orders_Model.OrdersList ordersListObj;
    OrdersDetailsAdapter ordersDetailsAdapter;
    OrdersViewModel ordersViewModel;
    ProgressDialog progressDialog;
    ArrayList<String> ordersStatusStrList;
    String strOrderStatus = "", strSellerId = "", strOrderId = "", strStatusId = "";
    int statusValue = 0;
    MyPreferenceDatas preferenceDatas;
    String myKeyValue = "";
    TextView order_time, ordered_address, ordered_number;
    RelativeLayout orderhist_layout, prod_simm_layout;
    ImageView orderhis_arrow, productsumm_arrow;
    String strName = "", strComments = "";
    TextView accepted_view, rejected_view;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pending_orders);

        ordersViewModel = ViewModelProviders.of(this).get(OrdersViewModelImpl.class);
        initView();
        getMyPreferences();
        getBundles();
        observeProgress();
    }

    private void getBundles() {
        Bundle bundle = getIntent().getExtras();
        strOrderStatus = bundle.getString("orderStatus");
        strOrderId = bundle.getString("orderId");
    }

    private void getMyPreferences() {
        myKeyValue = getResources().getString(R.string.myTripleKey);
        preferenceDatas = new MyPreferenceDatas(PendingOrders.this);
        strSellerId = TripleDes.getDESDecryptValue(preferenceDatas.getPrefString(MyPreferenceDatas.SELLER_ID), myKeyValue);
    }

    private void observeProgress() {
        ordersViewModel.getProgressUpdateLV().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean progressObserve) {
                if (progressObserve) {
                    showProgress();
                } else {
                    hideProgress();
                }
            }
        });
    }

    private void initView() {
        ordersListObj = StorageDatas.getInstance().getOrdersListObj();

        accepted_view = findViewById(R.id.accepted_view);
        rejected_view = findViewById(R.id.rejected_view);
        orderhist_layout = findViewById(R.id.orderhist_layout);
        prod_simm_layout = findViewById(R.id.prod_simm_layout);
        orderhis_arrow = findViewById(R.id.orderhis_arrow);
        productsumm_arrow = findViewById(R.id.productsumm_arrow);
        order_time = findViewById(R.id.order_time);
        ordered_number = findViewById(R.id.ordered_number);
        ordered_address = findViewById(R.id.ordered_address);
        ordersStatusStrList = new ArrayList<String>();

        ord_details_back = findViewById(R.id.ord_details_back);
        total_amount_layout = findViewById(R.id.total_amount_layout);
        orders_details_recycler = findViewById(R.id.orders_details_recycler);
        orders_history_recycler = findViewById(R.id.orders_history_recycler);
        ViewCompat.setNestedScrollingEnabled(orders_details_recycler, false);
        ViewCompat.setNestedScrollingEnabled(orders_history_recycler, false);
        total_amount = findViewById(R.id.total_amount);
        ord_details_back.setOnClickListener(this);
        orderhist_layout.setOnClickListener(this);
        prod_simm_layout.setOnClickListener(this);
        accepted_view.setOnClickListener(this);
        rejected_view.setOnClickListener(this);

        setValues();
    }

    private void setValues() {
        orders_details_recycler.setVisibility(View.GONE);
        orders_history_recycler.setVisibility(View.GONE);
        total_amount_layout.setVisibility(View.GONE);

        order_time.setText(formatDate(ordersListObj.getStrOrderDate()));
        ordered_address.setText(ordersListObj.getStrAddress());
        strName = CommonMethods.getContactName(getApplicationContext(), ordersListObj.getStrPhone());
        if(strName.equalsIgnoreCase("")) {
            ordered_number.setText(ordersListObj.getStrCountryCode() + " " + ordersListObj.getStrPhone());
        } else {
            ordered_number.setText(strName);
        }

        String oldPrice = ordersListObj.getStrTotalAmount().replace(".00", "");
        total_amount.setText("RM. " + oldPrice);
//        total_amount.setText("RM. " + ordersListObj.getStrTotalAmount());

        if(ordersListObj.getOrderHistoryList() != null) {
            if(ordersListObj.getOrderHistoryList().size() > 0) {
                orders_history_recycler.setHasFixedSize(true);
                orders_history_recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                OrderHistoryAdapter orderHistoryAdapter = new OrderHistoryAdapter(ordersListObj.getOrderHistoryList(), getApplicationContext());
                orders_history_recycler.setAdapter(orderHistoryAdapter);
            }
        }

        if (ordersListObj.getOrderedProductsList() != null) {
            if (ordersListObj.getOrderedProductsList().size() > 0) {
                orders_details_recycler.setHasFixedSize(true);
                orders_details_recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                ordersDetailsAdapter = new OrdersDetailsAdapter(ordersListObj.getOrderedProductsList(), getApplicationContext());
                orders_details_recycler.setAdapter(ordersDetailsAdapter);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.accepted_view:
                strStatusId = "2";
                strComments = "";
                orderUpdate(strStatusId, strComments);
                break;

            case R.id.rejected_view:
                strStatusId = "5";
                strComments = "Rejected by Seller.";
                orderUpdate(strStatusId, strComments);
                break;

            case R.id.orderhist_layout:
                if(orders_history_recycler.getVisibility() == View.VISIBLE) {
                    orders_history_recycler.setVisibility(View.GONE);
                    orderhis_arrow.setImageResource(getImage("arrow_up"));
                } else {
                    orders_history_recycler.setVisibility(View.VISIBLE);
                    orderhis_arrow.setImageResource(getImage("arrow_down"));
                }
                break;

            case R.id.prod_simm_layout:
                if(orders_details_recycler.getVisibility() == View.VISIBLE) {
                    total_amount_layout.setVisibility(View.GONE);
                    orders_details_recycler.setVisibility(View.GONE);
                    productsumm_arrow.setImageResource(getImage("arrow_up"));
                } else {
                    total_amount_layout.setVisibility(View.VISIBLE);
                    orders_details_recycler.setVisibility(View.VISIBLE);
                    productsumm_arrow.setImageResource(getImage("arrow_down"));
                }

                break;

            case R.id.ord_details_back:
                finish();
                break;
        }
    }

    public void orderUpdate(String strStatusId, String strComments) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("seller_id", strSellerId);
        params.put("order_id", strOrderId);
        params.put("status", strStatusId);
        params.put("seller_comment", strComments);

        Log.e("appSample", "SellerId: " + strSellerId);
        Log.e("appSample", "OrderId: " + strOrderId);
        Log.e("appSample", "StatusId: " + strStatusId);
        Log.e("appSample", "Comments: " + strComments);

        if (NetworkAvailability.isNetworkAvailable(PendingOrders.this)) {
            ordersViewModel.updateOrderStatusList(params);
        } else {
            NetworkAvailability networkAvailability = new NetworkAvailability(this);
            networkAvailability.noInternetConnection(PendingOrders.this, UPDATE_ORDER_STATUS);
        }

        ordersViewModel.updateOrderStatusLV().observe(this, new Observer<Orders_Model>() {
            @Override
            public void onChanged(Orders_Model orders_model) {
                Log.e("appSample", "Status: " + orders_model.isStrStatus());
                if(orders_model.isStrStatus() == true) {
                    showSuccessDialog(orders_model, "Orders updated successfully.", ORDER_STATUS_UPDATE_success);
                }
            }
        });
    }

    private void showSuccessDialog(Orders_Model orders_model, String strMessage, int resultCode) {
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(PendingOrders.this, SweetAlertDialog.SUCCESS_TYPE);
        sweetAlertDialog.setTitleText("Success");
        sweetAlertDialog.setContentText(strMessage);
        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.show();
        sweetAlertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                sweetAlertDialog.dismiss();
                StorageDatas.getInstance().setOrders_model(orders_model);
                setResult(resultCode);
                finish();
            }
        });
    }

    private void showProgress() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void hideProgress() {
        if (progressDialog != null) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
                progressDialog = null;
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

    public int getImage(String imageName) {
        int drawableResourceId = getResources().getIdentifier(imageName, "drawable", getPackageName());
        return drawableResourceId;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == NETWORK_ENABLE_SETTINGS) {

        }
    }
}
