package com.mart.mymartbee.view.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mart.mymartbee.R;
import com.mart.mymartbee.algorithm.TripleDes;
import com.mart.mymartbee.constants.Constants;
import com.mart.mymartbee.custom.NetworkAvailability;
import com.mart.mymartbee.custom.SweetAlert.SweetAlertDialog;
import com.mart.mymartbee.model.Order_Status_Model;
import com.mart.mymartbee.model.Orders_Model;
import com.mart.mymartbee.model.Products_Model;
import com.mart.mymartbee.storage.MyPreferenceDatas;
import com.mart.mymartbee.storage.StorageDatas;
import com.mart.mymartbee.view.OrderUpdate;
import com.mart.mymartbee.view.adapters.OrderStatusAdapter;
import com.mart.mymartbee.view.adapters.OrdersAdapter;
import com.mart.mymartbee.view.adapters.OrdersDetailsAdapter;
import com.mart.mymartbee.viewmodel.implementor.OrdersViewModelImpl;
import com.mart.mymartbee.viewmodel.interfaces.OrdersViewModel;

import java.util.ArrayList;

public class OrdersFragment extends Fragment implements Constants {

    RecyclerView orders_recycler, orders_status_recycler;
    LinearLayout no_orders_found_layout, search_order_layout;
    ProgressDialog progressDialog;
    EditText search_orders_edit;

    MyPreferenceDatas preferenceDatas;
    String myKeyValue = "";
    String strSellerId;
    ArrayList<Orders_Model.OrdersList> ordersLists;
    ArrayList<Orders_Model.OrdersList> ordersListsTemp;
    ArrayList<Orders_Model.OrdersList> mainOrdersList;
    Orders_Model.OrdersList ordersListObj;
    ArrayList<Orders_Model.OrdersList.OrderedProducts> orderedProductsList;
    LinearLayoutManager lManager = null;
    OrdersViewModel ordersViewModel;
    OrdersAdapter ordersAdapter;
//    int orderListposition = 0;
    ArrayList<Order_Status_Model.OrdersStatusList> ordersStatusLists;
    int selectedId = 0;
    OrderStatusAdapter orderStatusAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_orders, container, false);
        Log.e("appSample", "OrdersFragement");
        ordersViewModel = ViewModelProviders.of(getActivity()).get(OrdersViewModelImpl.class);
        initView(view);
        myPreferenceDatas();
        getOrderStatus();
        return view;
    }

    private void initView(View view) {
        ordersStatusLists = new ArrayList<Order_Status_Model.OrdersStatusList>();
        ordersLists = new ArrayList<Orders_Model.OrdersList>();
        mainOrdersList = new ArrayList<Orders_Model.OrdersList>();
        ordersListsTemp = new ArrayList<Orders_Model.OrdersList>();
        orderedProductsList = new ArrayList<Orders_Model.OrdersList.OrderedProducts>();
        search_orders_edit = view.findViewById(R.id.search_orders_edit);
        orders_recycler = view.findViewById(R.id.orders_recycler);
        orders_status_recycler = view.findViewById(R.id.orders_status_recycler);
        no_orders_found_layout = view.findViewById(R.id.no_orders_found_layout);
        search_order_layout = view.findViewById(R.id.search_order_layout);
        setListeners();
    }

    private void myPreferenceDatas() {
        myKeyValue = getActivity().getResources().getString(R.string.myTripleKey);
        preferenceDatas = new MyPreferenceDatas(getActivity());
        strSellerId = TripleDes.getDESDecryptValue(preferenceDatas.getPrefString(MyPreferenceDatas.SELLER_ID), myKeyValue);
    }

    private void setListeners() {
        search_orders_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                shortList(s);
            }
        });

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

        orders_status_recycler.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            GestureDetector gestureDetector = new GestureDetector(getActivity(), new GestureDetector.SimpleOnGestureListener() {
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
                    selectedId = Integer.parseInt(ordersStatusLists.get(position).getStrOrderStatusId());
                    Log.e("appSample", "SelectedId: " + selectedId);
                    orderStatusAdapter.updateAdapter(selectedId);
                    statusUpdateAdapter(ordersStatusLists.get(position).getStrOrderStatusName());
//                    orderStatusAdapter.notifyDataSetChanged();
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

        orders_recycler.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
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
                    orderedProductsList = ordersLists.get(position).getOrderedProductsList();
                    ordersListObj = ordersLists.get(position);
                    StorageDatas.getInstance().setOrdersListObj(ordersListObj);

                    sentToOrderDetails(ordersLists.get(position).getStrStatus(),
                            ordersLists.get(position).getStrOrderId());

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

    public void statusUpdateAdapter(String strStatusName) {
        search_orders_edit.setText("");
        ordersLists.clear();
        ordersListsTemp.clear();
        if(strStatusName.equalsIgnoreCase("All")) {
            ordersLists.addAll(mainOrdersList);
            ordersListsTemp.addAll(mainOrdersList);
        } else {
            for(int i=0; i<mainOrdersList.size(); i++) {
                if(mainOrdersList.get(i).getStrStatus().toLowerCase().equalsIgnoreCase(strStatusName)) {

                    Orders_Model.OrdersList item = new Orders_Model.OrdersList();
                    item.setStrOrderId(mainOrdersList.get(i).getStrOrderId());
                    item.setStrStatus(mainOrdersList.get(i).getStrStatus());
                    item.setStrOrderDate(mainOrdersList.get(i).getStrOrderDate());
                    item.setStrTotalAmount(mainOrdersList.get(i).getStrTotalAmount());
                    item.setStrCountryCode(mainOrdersList.get(i).getStrCountryCode());
                    item.setStrPhone(mainOrdersList.get(i).getStrPhone());
                    item.setStrAddress(mainOrdersList.get(i).getStrAddress());
                    item.setOrderedProductsList(mainOrdersList.get(i).getOrderedProductsList());
                    item.setOrderHistoryList(mainOrdersList.get(i).getOrderHistoryList());
                    ordersLists.add(item);
                    ordersListsTemp.add(item);

                }
            }
        }


        if(ordersListsTemp.size() > 0) {
            ordersFound();
            if(ordersAdapter != null) {
                ordersAdapter.notifyDataSetChanged();
            } else {
                setOrdersAdapter();
            }
        } else {
            noOrdersFound();
        }
    }

    public void shortList(Editable s) {
        if (ordersListsTemp != null) {
            if (ordersListsTemp.size() > 0) {
                String value = "" + s;
                value = value.trim();
                ordersLists.clear();

                if (value != null) {
                    if (value.equals("")) {
                        ordersLists.addAll(ordersListsTemp);
                    } else {
                        for (int i = 0; i < ordersListsTemp.size(); i++) {
                            if (ordersListsTemp.get(i).getStrOrderId().toLowerCase().
                                    contains(value.toLowerCase()) ) {
                                Orders_Model.OrdersList item = new Orders_Model.OrdersList();
                                item.setStrOrderId(ordersListsTemp.get(i).getStrOrderId());
                                item.setStrStatus(ordersListsTemp.get(i).getStrStatus());
                                item.setStrOrderDate(ordersListsTemp.get(i).getStrOrderDate());
                                item.setStrTotalAmount(ordersListsTemp.get(i).getStrTotalAmount());
                                item.setStrCountryCode(ordersListsTemp.get(i).getStrCountryCode());
                                item.setStrPhone(ordersListsTemp.get(i).getStrPhone());
                                item.setStrAddress(ordersListsTemp.get(i).getStrAddress());
                                item.setOrderedProductsList(ordersListsTemp.get(i).getOrderedProductsList());
                                item.setOrderHistoryList(ordersListsTemp.get(i).getOrderHistoryList());
                                ordersLists.add(item);
                            }
                        }
                    }
                }
                ordersAdapter.notifyDataSetChanged();
            }
        }
    }

    public void sentToOrderDetails(String strStatus, String strOrderId) {
        if (NetworkAvailability.isNetworkAvailable(getActivity())) {
            Bundle bundle = new Bundle();
            bundle.putString("orderStatus", strStatus);
            bundle.putString("orderId", strOrderId);
            Intent intent = new Intent(getActivity(), OrderUpdate.class);
            intent.putExtras(bundle);
            startActivityForResult(intent, ORDER_FRAG_to_ORDER_DETAILS);
        } else {
            noInternetConnection(MOVE_ORDER_DETAILS);
        }
    }

    public void setOrderStatusAdapter() {
        if(ordersStatusLists != null) {
            if(ordersStatusLists.size() > 0) {
                orders_status_recycler.setHasFixedSize(true);
                orders_status_recycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                orderStatusAdapter = new OrderStatusAdapter(ordersStatusLists, getActivity(), selectedId);
                orders_status_recycler.setAdapter(orderStatusAdapter);
            }
        }
    }

    public void getMyOrders() {
        if (NetworkAvailability.isNetworkAvailable(getActivity())) {
            ordersViewModel.getOrders(strSellerId);
        } else {
            noInternetConnection(GET_MY_ORDERS);
        }

        ordersViewModel.getOrdersLV().observe(this, new Observer<Orders_Model>() {
            @Override
            public void onChanged(Orders_Model orders_model) {
                if (orders_model != null) {
                    mainOrdersList.clear();
                    ordersLists.clear();
                    ordersListsTemp.clear();

                    /*Log.e("appSample", "MOListSize: " + mainOrdersList.size());
                    Log.e("appSample", "OListSize: " + mainOrdersList.size());
                    Log.e("appSample", "TempOListSize: " + mainOrdersList.size());*/
                    mainOrdersList = orders_model.getOrdersList();
                    ordersLists.addAll(mainOrdersList);
                    ordersListsTemp.addAll(ordersLists);
                    if (ordersLists != null) {
                        if (ordersLists.size() > 0) {
                            ordersFound();
                            setOrdersAdapter();
                        } else {
                            noOrdersFound();
                        }
                    } else {
                        noOrdersFound();
                    }
                }
            }
        });
    }

    public void getOrderStatus() {
        if (NetworkAvailability.isNetworkAvailable(getActivity())) {
            ordersViewModel.getOrderStatusList();
        } else {
            noInternetConnection(GET_MY_ORDERS);
        }
        ordersViewModel.getOrderStatusListLV().observe(this, new Observer<Order_Status_Model>() {
            @Override
            public void onChanged(Order_Status_Model order_status_model) {
                ordersStatusLists.clear();

                if(order_status_model != null) {
                    Order_Status_Model.OrdersStatusList item = new Order_Status_Model.OrdersStatusList();
                    item.setStrOrderStatusId("0");
                    item.setStrOrderStatusName("All");
                    ordersStatusLists.add(item);
                }

                for(int i=0; i<order_status_model.getOrdersStatusList().size(); i++) {
                    if(order_status_model.getOrdersStatusList().get(i).getStrOrderStatusName().equalsIgnoreCase("completed")) {
                        order_status_model.getOrdersStatusList().remove(i);
                    }
                    if(order_status_model.getOrdersStatusList().get(i).getStrOrderStatusName().equalsIgnoreCase("rejected")) {
                        order_status_model.getOrdersStatusList().remove(i);
                    }
                }

                ordersStatusLists.addAll(order_status_model.getOrdersStatusList());
//                ordersStatusLists = order_status_model.getOrdersStatusList();
                if (ordersStatusLists != null) {
                    if (ordersStatusLists.size() > 0) {
                        setOrderStatusAdapter();
                        getMyOrders();
                    }
                }
            }
        });
    }

    private void setOrdersAdapter() {
        orders_recycler.setHasFixedSize(true);
        lManager = new LinearLayoutManager(getActivity());
        orders_recycler.setLayoutManager(lManager);
        ordersAdapter = new OrdersAdapter(ordersLists, getActivity());
        orders_recycler.setAdapter(ordersAdapter);
    }

    private void noOrdersFound() {
        orders_recycler.setVisibility(View.GONE);
        search_order_layout.setVisibility(View.INVISIBLE);
        no_orders_found_layout.setVisibility(View.VISIBLE);
    }

    private void ordersFound() {
        no_orders_found_layout.setVisibility(View.GONE);
        search_order_layout.setVisibility(View.VISIBLE);
        orders_recycler.setVisibility(View.VISIBLE);
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

    public void noInternetConnection(int requestCode) {
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
                startActivityForResult(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS), requestCode);
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

        if(requestCode == GET_MY_ORDERS) {
            getOrderStatus();
        } /*else if (requestCode == MOVE_ORDER_DETAILS) {
            sentToOrderDetails();
        }*/

        if(requestCode == ORDER_FRAG_to_ORDER_DETAILS) {
            if(resultCode == ORDER_STATUS_UPDATE_success) {
                if(ordersLists == null) {
                    ordersLists = new ArrayList<Orders_Model.OrdersList>();
                } else {
                    ordersLists.clear();
                }

                getOrderStatus();
            }
        }
    }
}
