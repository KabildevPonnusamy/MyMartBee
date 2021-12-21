package com.mart.mymartbee.view.fragments;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mart.mymartbee.R;
import com.mart.mymartbee.algorithm.TripleDes;
import com.mart.mymartbee.commons.CommonMethods;
import com.mart.mymartbee.constants.Constants;
import com.mart.mymartbee.custom.NetworkAvailability;
import com.mart.mymartbee.custom.SweetAlert.SweetAlertDialog;
import com.mart.mymartbee.model.Dashboard_Model;
import com.mart.mymartbee.model.Orders_Model;
import com.mart.mymartbee.storage.MyPreferenceDatas;
import com.mart.mymartbee.storage.StorageDatas;
import com.mart.mymartbee.view.OrderUpdate;
import com.mart.mymartbee.view.PendingOrders;
import com.mart.mymartbee.view.adapters.NewPendingOrdersAdapter;
import com.mart.mymartbee.view.adapters.PendingOrdersAdapter;
import com.mart.mymartbee.view.ProductViewsAct;
import com.mart.mymartbee.viewmodel.implementor.DashboardViewModelImpl;
import com.mart.mymartbee.viewmodel.interfaces.DashboardViewModel;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements View.OnClickListener, Constants {

    String whatsAppLink = "", strCategoryName = "", strSelleShop = "", strSellerMobile = "",
            strCountryCode= "";
    String strCateId, strSellerId, strShortValue = "all";
    LinearLayout whatsapp_layout;
    TextView store_link, store_view_value, product_view_value, total_orders_value, revenue_value,
            status_short_value, shop_tv;
    MyPreferenceDatas preferenceDatas;
    String myKeyValue = "";
    DashboardViewModel dashboardViewModel;
    ProgressDialog progressDialog;
    RecyclerView pending_orders_recycler;

    LinearLayout copy_link, product_views_layout, order_views_layout, revenue_layout, no_orders_found_layout;
    ArrayList<Dashboard_Model.ViewedProductList> viewedProductLists;
    ArrayList<Dashboard_Model.PendingOrdersList> pendingOrdersLists;
    Dashboard_Model dashboard_model;
    private ClipboardManager myClipboard;
    private ClipData myClip;
    LinearLayoutManager lManager = null;
    NewPendingOrdersAdapter ordersAdapter;
    BottomNavigationView bottom_navigation;
    Orders_Model.OrdersList ordersListObj;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_home, container, false);

        dashboardViewModel = ViewModelProviders.of(getActivity()).get(DashboardViewModelImpl.class);
        initView(view);
        getMyPreferences();

        return view;
    }

    private void initView(View view) {
        ordersListObj = new Orders_Model.OrdersList();
        bottom_navigation = getActivity().findViewById(R.id.bottom_navigation);
        pending_orders_recycler = (RecyclerView) view.findViewById(R.id.pending_orders_recycler);
        ViewCompat.setNestedScrollingEnabled(pending_orders_recycler, false);

        copy_link = (LinearLayout) view.findViewById(R.id.copy_link);
        order_views_layout = (LinearLayout) view.findViewById(R.id.order_views_layout);
        revenue_layout = (LinearLayout) view.findViewById(R.id.revenue_layout);
        no_orders_found_layout = (LinearLayout) view.findViewById(R.id.no_orders_found_layout);
        product_views_layout = (LinearLayout) view.findViewById(R.id.product_views_layout);
        viewedProductLists = new ArrayList<Dashboard_Model.ViewedProductList>();
        pendingOrdersLists = new ArrayList<Dashboard_Model.PendingOrdersList>();
        whatsapp_layout = (LinearLayout) view.findViewById(R.id.whatsapp_layout);
        store_link = (TextView) view.findViewById(R.id.store_link);
        shop_tv = (TextView) view.findViewById(R.id.shop_tv);
        status_short_value = (TextView) view.findViewById(R.id.status_short_value);
        store_view_value = (TextView) view.findViewById(R.id.store_view_value);
        product_view_value = (TextView) view.findViewById(R.id.product_view_value);
        total_orders_value = (TextView) view.findViewById(R.id.total_orders_value);
        revenue_value = (TextView) view.findViewById(R.id.revenue_value);
        whatsapp_layout.setOnClickListener(this);
        copy_link.setOnClickListener(this);
        order_views_layout.setOnClickListener(this);
        revenue_layout.setOnClickListener(this);
        product_views_layout.setOnClickListener(this);
        status_short_value.setOnClickListener(this);
        setListeners();
    }

    public void setListeners() {

        pending_orders_recycler.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            GestureDetector gestureDetector = new GestureDetector(getActivity(),
                    new GestureDetector.SimpleOnGestureListener() {
                        @Override
                        public boolean onSingleTapUp(MotionEvent e) {
                            return true;
                        }
                    });
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                View child = rv.findChildViewUnder(e.getX(), e.getY());
                if (child != null && gestureDetector.onTouchEvent(e)) {
                    int position = rv.getChildAdapterPosition(child);

                    ordersListObj.setStrOrderId(pendingOrdersLists.get(position).getStrOrderId());
                    ordersListObj.setStrStatus(pendingOrdersLists.get(position).getStrStatus());
                    ordersListObj.setStrOrderDate(pendingOrdersLists.get(position).getStrOrderDate());
                    ordersListObj.setStrTotalAmount(pendingOrdersLists.get(position).getStrTotalAmount());
                    ordersListObj.setStrPhone(pendingOrdersLists.get(position).getStrPhone());
                    ordersListObj.setStrCountryCode(pendingOrdersLists.get(position).getStrCountryCode());
                    ordersListObj.setStrAddress(pendingOrdersLists.get(position).getStrAddress());
                    ordersListObj.setStrPaymentType(pendingOrdersLists.get(position).getStrPaymentType());
                    ordersListObj.setStrPaymentReceipt(pendingOrdersLists.get(position).getStrPaymentReceipt());

                    ArrayList<Orders_Model.OrdersList.OrderHistory> listitem = new ArrayList<>();
                    ArrayList<Orders_Model.OrdersList.OrderedProducts> productItem = new ArrayList<>();

                    for( int i=0; i < pendingOrdersLists.get(position).getPendingOrderHistoryList().size(); i++ ) {
                        Orders_Model.OrdersList.OrderHistory item = new Orders_Model.OrdersList.OrderHistory();
                        item.setStrAddedDate(pendingOrdersLists.get(position).getPendingOrderHistoryList().get(i).getStrDateodAdded());
                        item.setStrStatusName(pendingOrdersLists.get(position).getPendingOrderHistoryList().get(i).getStrName());
                        listitem.add(item);
                    }

                    for(int j=0; j < pendingOrdersLists.get(position).getPendingProductsList().size(); j++) {
                        Orders_Model.OrdersList.OrderedProducts item = new Orders_Model.OrdersList.OrderedProducts();
                        item.setStrProductId(pendingOrdersLists.get(position).pendingProductsList.get(j).getStrProductId());
                        item.setStrProductImage(pendingOrdersLists.get(position).pendingProductsList.get(j).getStrProductImage());
                        item.setStrProductPrice(pendingOrdersLists.get(position).pendingProductsList.get(j).getStrProductPrice());
                        item.setStrProductQuantity(pendingOrdersLists.get(position).pendingProductsList.get(j).getStrProductQuantity());
                        item.setStrProductTotal(pendingOrdersLists.get(position).pendingProductsList.get(j).getStrProductTotal());
                        item.setStrProductTitle(pendingOrdersLists.get(position).pendingProductsList.get(j).getStrproductTitle());
                        productItem.add(item);
                    }

                    ordersListObj.setOrderHistoryList(listitem);
                    ordersListObj.setOrderedProductsList(productItem);

                    StorageDatas.getInstance().setOrdersListObj(ordersListObj);
                    sentToOrderDetails(pendingOrdersLists.get(position).getStrStatus(),
                            pendingOrdersLists.get(position).getStrOrderId());

                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
            }
        });
    }

    public void sentToOrderDetails(String strStatus, String strOrderId) {
        if (NetworkAvailability.isNetworkAvailable(getActivity())) {
//            Bundle bundle = new Bundle();
            StorageDatas.getInstance().setStrOrderId(strOrderId);
            StorageDatas.getInstance().setStrOrderStatus(strStatus);
            StorageDatas.getInstance().setFromNotification(false);
//            bundle.putString("orderStatus", strStatus);
//            bundle.putString("orderId", strOrderId);
            Intent intent = new Intent(getActivity(), PendingOrders.class); // PendingOrders
//            intent.putExtras(bundle);
            startActivityForResult(intent, HOME_FRAG_to_ORDER_DETAILS);
        } else {
            noInternetConnection();
        }
    }

    public void showStatusPopup() {
        PopupMenu popup = new PopupMenu(getActivity(), status_short_value);
        popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                status_short_value.setText(item.getTitle());
                if(item.getTitle().toString().equalsIgnoreCase(getActivity().getResources().getString(R.string.menu_today))) {
                    strShortValue = "today";
                    getDashboardDatas(strShortValue);
                } else if(item.getTitle().toString().equalsIgnoreCase(getActivity().getResources().getString(R.string.menu_yesterday))) {
                    strShortValue = "yesterday";
                    getDashboardDatas(strShortValue);
                } else if(item.getTitle().toString().equalsIgnoreCase(getActivity().getResources().getString(R.string.menu_last_week))) {
                    strShortValue = "last_week";
                    getDashboardDatas(strShortValue);
                } else if(item.getTitle().toString().equalsIgnoreCase(getActivity().getResources().getString(R.string.menu_last_month))) {
                    strShortValue = "last_month";
                    getDashboardDatas(strShortValue);
                } else if(item.getTitle().toString().equalsIgnoreCase(getActivity().getResources().getString(R.string.menu_all))) {
                    strShortValue = "all";
                    getDashboardDatas(strShortValue);
                }

                return true;
            }
        });
        popup.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.revenue_layout:
                bottom_navigation.setSelectedItemId(R.id.menu_nav_reports);
                break;

            case R.id.order_views_layout:
                bottom_navigation.setSelectedItemId(R.id.menu_nav_order);
                break;

            case R.id.status_short_value:
                showStatusPopup();
                break;

            case R.id.product_views_layout:
                StorageDatas.getInstance().setViewedProductLists(dashboard_model.getViewedProductList());
                Intent intent = new Intent(getActivity(), ProductViewsAct.class);
                startActivityForResult(intent, HOME_FRAG_to_PRODUCTVIEWS);
                break;

            case R.id.copy_link:
                myClipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                String text = store_link.getText().toString();

                myClip = ClipData.newPlainText("text", text);
                myClipboard.setPrimaryClip(myClip);
                CommonMethods.LinkCopiedToast(getActivity(), "Link Copied");
                break;

            case R.id.whatsapp_layout:
                shareOnWhatsapp();
                break;
        }
    }

    private void shareOnWhatsapp() {
        Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
        whatsappIntent.setType("text/plain");
        whatsappIntent.setPackage("com.whatsapp");
        whatsappIntent.putExtra(Intent.EXTRA_TEXT, " \n  Hello! \nNow you can order from " + "*" +  strSelleShop  + "*"+ " using this link: " + whatsAppLink +
                "\nFeel free to call us on " + strCountryCode + " " +  strSellerMobile + " if you need any help with ordering. \n\n Thank you." );
        try {
            startActivity(whatsappIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            CommonMethods.Toast(getActivity(), "WhatsApp have not been installed.");
        }
    }

    private void getMyPreferences() {
        myKeyValue = getActivity().getResources().getString(R.string.myTripleKey);
        preferenceDatas = new MyPreferenceDatas(getActivity());
        strCateId = TripleDes.getDESDecryptValue(preferenceDatas.getPrefString(MyPreferenceDatas.SELLER_CATEGORY), myKeyValue);
        strSellerId = TripleDes.getDESDecryptValue(preferenceDatas.getPrefString(MyPreferenceDatas.SELLER_ID), myKeyValue);
        strCategoryName = TripleDes.getDESDecryptValue(preferenceDatas.getPrefString(MyPreferenceDatas.SELLER_CATEGORY_NAME), myKeyValue);
        strSelleShop = TripleDes.getDESDecryptValue(preferenceDatas.getPrefString(MyPreferenceDatas.SELLER_SHOP), myKeyValue);
        strSellerMobile = TripleDes.getDESDecryptValue(preferenceDatas.getPrefString(MyPreferenceDatas.SELLER_MOBILE), myKeyValue);
        strCountryCode = TripleDes.getDESDecryptValue(preferenceDatas.getPrefString(MyPreferenceDatas.SELLER_COUNTRY_CODE), myKeyValue);

        shop_tv.setText(TripleDes.getDESDecryptValue(preferenceDatas.getPrefString(MyPreferenceDatas.SELLER_SHOP), myKeyValue));
        viewModelInits();
    }

    public void viewModelInits() {
        dashboardViewModel.progressbarDASHObservable().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean progressObserve) {
                if (progressObserve) {
                    showProgress();
                } else {
                    hideProgress();
                }
            }
        });
        getDashboardDatas(strShortValue);
    }

    public void getDashboardDatas(String strShortValue) {
        Log.e("appSample", "SellerId: "  + strSellerId);
        if (NetworkAvailability.isNetworkAvailable(getActivity())) {
            dashboardViewModel.getDashboardDatas(strSellerId, strShortValue);
        } else {
            noInternetConnection();
        }
        dashboardViewModel.getDashboardDatasLD().observe(this, new Observer<Dashboard_Model>() {
            @Override
            public void onChanged(Dashboard_Model d_model) {
                if (d_model.isStrStatus() == true) {
                    Log.e("appSample", "TRUE");
                    viewedProductLists = d_model.getViewedProductList();
                    dashboard_model = d_model;
                    updateValues(dashboard_model);
                }
            }
        });
    }

    public void updateValues(Dashboard_Model dashboard_model) {
        store_view_value.setText(dashboard_model.getStrPageViewCount());
        product_view_value.setText(dashboard_model.getStrProductViewCount());
        total_orders_value.setText(dashboard_model.getStrOrderCount());
        revenue_value.setText("RM " + dashboard_model.getStrTotalRevenue());
        store_link.setText(dashboard_model.getStrStoreLink());
        whatsAppLink = dashboard_model.getStrStoreLink();
        StorageDatas.getInstance().setStoreWhatsappLink(whatsAppLink);
        store_link.setPaintFlags(store_link.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        if (dashboard_model.getPendingOrdersLists() != null) {
            if (dashboard_model.getPendingOrdersLists().size() > 0) {
                pendingOrdersLists = dashboard_model.getPendingOrdersLists();
                pending_orders_recycler.setVisibility(View.VISIBLE);
                no_orders_found_layout.setVisibility(View.GONE);
                setPendingOrdersAdapter();
            } else {
                pending_orders_recycler.setVisibility(View.GONE);
                no_orders_found_layout.setVisibility(View.VISIBLE);
            }
        } else {
            pending_orders_recycler.setVisibility(View.GONE);
            no_orders_found_layout.setVisibility(View.VISIBLE);
        }
    }

    private void setPendingOrdersAdapter() {
        pending_orders_recycler.setHasFixedSize(true);
        lManager = new LinearLayoutManager(getActivity());
        pending_orders_recycler.setLayoutManager(lManager);
        ordersAdapter = new NewPendingOrdersAdapter(pendingOrdersLists, getActivity());
        pending_orders_recycler.setAdapter(ordersAdapter);
    }

    private void showProgress() {
        progressDialog = new ProgressDialog(getActivity());
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

    public void noInternetConnection() {
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE);
        sweetAlertDialog.setTitleText(getActivity().getResources().getString(R.string.no_internet));
        sweetAlertDialog.setContentText(getActivity().getResources().getString(R.string.open_settings));
        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.show();

        sweetAlertDialog.setCancelText(getActivity().getResources().getString(R.string.option_no));
        sweetAlertDialog.setConfirmText(getActivity().getResources().getString(R.string.option_yes));
        sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.dismiss();
                startActivityForResult(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS), GET_HOME_PRODUCTS);
            }
        });
        sweetAlertDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.dismiss();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == HOME_FRAG_to_ORDER_DETAILS) {
            if(resultCode == ORDER_STATUS_UPDATE_success) {
                if(pendingOrdersLists == null) {
                    pendingOrdersLists = new ArrayList<Dashboard_Model.PendingOrdersList>();
                } else {
                    pendingOrdersLists.clear();
                }

                getDashboardDatas(strShortValue);
            }
        }
    }
}
