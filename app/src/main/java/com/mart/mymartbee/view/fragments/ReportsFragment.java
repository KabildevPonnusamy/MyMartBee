package com.mart.mymartbee.view.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
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
import com.mart.mymartbee.model.Orders_Model;
import com.mart.mymartbee.model.Reports_Model;
import com.mart.mymartbee.storage.MyPreferenceDatas;
import com.mart.mymartbee.storage.StorageDatas;
import com.mart.mymartbee.view.OrderUpdate;
import com.mart.mymartbee.view.ReportDetails;
import com.mart.mymartbee.view.adapters.OrdersAdapter;
import com.mart.mymartbee.view.adapters.ReportCompletedAdapter;
import com.mart.mymartbee.view.adapters.ReportRejectedAdapter;
import com.mart.mymartbee.viewmodel.implementor.ReportsViewModelImpl;
import com.mart.mymartbee.viewmodel.interfaces.ReportsViewModel;

import java.util.ArrayList;

public class ReportsFragment extends Fragment implements View.OnClickListener, Constants {

    RecyclerView report_complete_recycler, report_reject_recycler;
    LinearLayout completed_layout, rejected_layout, reports_title_layout,
            reports_found_layout, no_reports_found_layout;
//    TextView completed_count, rejected_count;
    ProgressDialog progressDialog;

    MyPreferenceDatas preferenceDatas;
    String myKeyValue = "";
    String strSellerId;
    int report_layout_status = 0;
    ArrayList<Reports_Model.ReportsList> mainReportsList;
    ArrayList<Reports_Model.ReportsList> completedReportList;
    ArrayList<Reports_Model.ReportsList> completedReportListTemp;
    ArrayList<Reports_Model.ReportsList> rejectedReportList;
    ArrayList<Reports_Model.ReportsList> rejectedReportListTemp;
    LinearLayout search_compreport_layout, search_rejreport_layout;
    EditText search_compreport_edit, search_rejreport_edit;

    LinearLayoutManager lManager = null;
    ReportsViewModel reportsViewModel;
    ReportCompletedAdapter reportCompleteAdapter;
    ReportRejectedAdapter reportRejectedAdapter;
    Reports_Model.ReportsList reportListObj;
    int compListPosition = 0;
    int rejListPosition = 0;
    CardView completed_cardView, rejected_cardView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_reports, container, false);

        reportsViewModel = ViewModelProviders.of(this).get(ReportsViewModelImpl.class);
        initView(view);
        myPreferenceDatas();
        getMyReports();
        return view;
    }

    private void initView(View view) {
        completed_cardView = view.findViewById(R.id.completed_cardView);
        rejected_cardView = view.findViewById(R.id.rejected_cardView);
        search_compreport_layout = view.findViewById(R.id.search_compreport_layout);
        search_rejreport_layout = view.findViewById(R.id.search_rejreport_layout);
        search_compreport_edit = view.findViewById(R.id.search_compreport_edit);
        search_rejreport_edit = view.findViewById(R.id.search_rejreport_edit);

        mainReportsList = new ArrayList<Reports_Model.ReportsList>();
        completedReportList = new ArrayList<Reports_Model.ReportsList>();
        completedReportListTemp = new ArrayList<Reports_Model.ReportsList>();
        rejectedReportList = new ArrayList<Reports_Model.ReportsList>();
        rejectedReportListTemp = new ArrayList<Reports_Model.ReportsList>();
        report_complete_recycler = view.findViewById(R.id.report_complete_recycler);
        report_reject_recycler = view.findViewById(R.id.report_reject_recycler);
        completed_layout = view.findViewById(R.id.completed_layout);
        rejected_layout = view.findViewById(R.id.rejected_layout);
        reports_title_layout = view.findViewById(R.id.reports_title_layout);
        reports_found_layout = view.findViewById(R.id.reports_found_layout);
        no_reports_found_layout = view.findViewById(R.id.no_reports_found_layout);
//        rejected_count = view.findViewById(R.id.rejected_count);
//        completed_count = view.findViewById(R.id.completed_count);
        completed_layout.setOnClickListener(this);
        rejected_layout.setOnClickListener(this);
        /*completed_cardView.setOnClickListener(this);
        rejected_cardView.setOnClickListener(this);*/
        setListeners();
    }

    private void myPreferenceDatas() {
        myKeyValue = getActivity().getResources().getString(R.string.myTripleKey);
        preferenceDatas = new MyPreferenceDatas(getActivity());
        strSellerId = TripleDes.getDESDecryptValue(preferenceDatas.getPrefString(MyPreferenceDatas.SELLER_ID), myKeyValue);
    }

    private void setListeners() {

        search_compreport_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                shortCompleteList(s);
            }
        });

        search_rejreport_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                shortRejectedList(s);
            }
        });

        reportsViewModel.reportsProgressUpdateLV().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean progressObserve) {
                if (progressObserve) {
                    showProgress();
                } else {
                    hideProgress();
                }
            }
        });

        report_complete_recycler.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
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
                    compListPosition = rv.getChildAdapterPosition(child);
                    reportListObj = completedReportList.get(compListPosition);
                    StorageDatas.getInstance().setReportListObj(reportListObj);

                    movetoCompletedReports();

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

        report_reject_recycler.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
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
                    rejListPosition = rv.getChildAdapterPosition(child);
                    reportListObj = rejectedReportList.get(rejListPosition);
                    StorageDatas.getInstance().setReportListObj(reportListObj);
                    moveToRejectedReports();

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

    public void movetoCompletedReports() {
        if (NetworkAvailability.isNetworkAvailable(getActivity())) {
            Intent intent = new Intent(getActivity(), ReportDetails.class);
            startActivityForResult(intent, REPORT_FRAG_to_REPORT_DETAILS);
        } else {
            noInternetConnection(MOVE_COMPLETED_REPORTS);
        }
    }

    public void moveToRejectedReports() {
        if (NetworkAvailability.isNetworkAvailable(getActivity())) {
            Intent intent = new Intent(getActivity(), ReportDetails.class);
            startActivityForResult(intent, REPORT_FRAG_to_REPORT_DETAILS);
        } else {
            noInternetConnection(MOVE_REJECTED_REPORTS);
        }
    }

    private void getMyReports() {
        if (NetworkAvailability.isNetworkAvailable(getActivity())) {
            reportsViewModel.getReports(strSellerId);
        } else {
            noInternetConnection(GET_MY_REPORTS);
        }

        reportsViewModel.getReportsLD().observe(this, new Observer<Reports_Model>() {
            @Override
            public void onChanged(Reports_Model reports_model) {
                if (reports_model != null) {
                    mainReportsList = reports_model.getReportsList();
                    if (mainReportsList != null) {
                        if (mainReportsList.size() > 0) {
                            validateReportList(mainReportsList);
                        } else {
                            search_compreport_layout.setVisibility(View.GONE);
                            report_complete_recycler.setVisibility(View.GONE);
                            search_rejreport_layout.setVisibility(View.GONE);
                            report_reject_recycler.setVisibility(View.GONE);
                            no_reports_found_layout.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        });
    }

    public void validateReportList(ArrayList<Reports_Model.ReportsList> mainReportsList) {
        for (int i = 0; i < mainReportsList.size(); i++) {
            if (mainReportsList.get(i).getStrStatus().equalsIgnoreCase("completed")) {
                completedReportList.add(mainReportsList.get(i));
            } else {
                rejectedReportList.add(mainReportsList.get(i));
            }
        }

        Log.e("appSample", "CompletedListSize: " + completedReportList.size());
        Log.e("appSample", "RejectedListSize: " + rejectedReportList.size());

        completedReportListTemp.addAll(completedReportList);
        rejectedReportListTemp.addAll(rejectedReportList);
        report_complete_recycler.setVisibility(View.VISIBLE);
        report_reject_recycler.setVisibility(View.GONE);
        search_compreport_layout.setVisibility(View.VISIBLE);
        search_rejreport_layout.setVisibility(View.GONE);
        setReportCompleteAdapter();
    }

    private void setReportCompleteAdapter() {

        if(completedReportList.size() > 0) {
            report_complete_recycler.setVisibility(View.VISIBLE);
            report_reject_recycler.setVisibility(View.GONE);
            search_compreport_layout.setVisibility(View.VISIBLE);
            search_rejreport_layout.setVisibility(View.GONE);
            no_reports_found_layout.setVisibility(View.GONE);

            report_complete_recycler.setHasFixedSize(true);
            lManager = new LinearLayoutManager(getActivity());
            report_complete_recycler.setLayoutManager(lManager);
            reportCompleteAdapter = new ReportCompletedAdapter(completedReportList, getActivity());
            report_complete_recycler.setAdapter(reportCompleteAdapter);
        } else {
            report_complete_recycler.setVisibility(View.GONE);
            report_reject_recycler.setVisibility(View.GONE);
            search_compreport_layout.setVisibility(View.GONE);
            search_rejreport_layout.setVisibility(View.GONE);
            no_reports_found_layout.setVisibility(View.VISIBLE);
        }
    }

    private void setReportRejectedAdapter() {

        if(rejectedReportList.size() > 0) {
            report_complete_recycler.setVisibility(View.GONE);
            report_reject_recycler.setVisibility(View.VISIBLE);
            search_compreport_layout.setVisibility(View.GONE);
            search_rejreport_layout.setVisibility(View.VISIBLE);
            no_reports_found_layout.setVisibility(View.GONE);

            report_reject_recycler.setHasFixedSize(true);
            lManager = new LinearLayoutManager(getActivity());
            report_reject_recycler.setLayoutManager(lManager);
            reportRejectedAdapter = new ReportRejectedAdapter(rejectedReportList, getActivity());
            report_reject_recycler.setAdapter(reportRejectedAdapter);
        } else {
            report_complete_recycler.setVisibility(View.GONE);
            report_reject_recycler.setVisibility(View.GONE);
            search_compreport_layout.setVisibility(View.GONE);
            search_rejreport_layout.setVisibility(View.GONE);
            no_reports_found_layout.setVisibility(View.VISIBLE);
        }
    }

    public void setMargin(CardView view, int marginValue, int cardElevation, int radius) {

        /*LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        params.setMargins(marginValue, marginValue, marginValue, marginValue);
        view.setLayoutParams(params);
//        view.setMaxCardElevation(cardElevation);
        view.setRadius(radius);*/
//        view.setCardBackgroundColor(getActivity().getResources().getColor(color));
    }

    public void setDesignforLayout(CardView cardView, int cardBgColor) {
        cardView.setCardBackgroundColor(cardBgColor);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.completed_layout:
//                completed_layout.setBackgroundResource(R.drawable.main_color_bgnd);
//                rejected_layout.setBackgroundResource(R.drawable.btn_bgnd);
//                setMargin(completed_cardView, 15, 15, 8);
//                setMargin(rejected_cardView, 0, 0, 0);

//                setDesignforLayout(completed_cardView, R.color.white);
//                setDesignforLayout(rejected_cardView, R.color.snow_color);

                completed_cardView.setCardBackgroundColor(getActivity().getResources().getColor(R.color.white));
                rejected_cardView.setCardBackgroundColor(getActivity().getResources().getColor(R.color.snow_color));
                report_complete_recycler.setVisibility(View.VISIBLE);
                report_reject_recycler.setVisibility(View.GONE);
                search_compreport_layout.setVisibility(View.VISIBLE);
                search_rejreport_layout.setVisibility(View.GONE);
                setReportCompleteAdapter();
                break;
            case R.id.rejected_layout:
//                setMargin(completed_cardView, 0, 0, 0);
//                setMargin(rejected_cardView, 15, 15, 8);
//                completed_layout.setBackgroundResource(R.drawable.btn_bgnd);
//                rejected_layout.setBackgroundResource(R.drawable.main_color_bgnd);

//                setDesignforLayout(completed_cardView, R.color.snow_color);
//                setDesignforLayout(rejected_cardView, R.color.white);

                completed_cardView.setCardBackgroundColor(getActivity().getResources().getColor(R.color.snow_color));
                rejected_cardView.setCardBackgroundColor(getActivity().getResources().getColor(R.color.white));
                report_complete_recycler.setVisibility(View.GONE);
                report_reject_recycler.setVisibility(View.VISIBLE);
                search_compreport_layout.setVisibility(View.GONE);
                search_rejreport_layout.setVisibility(View.VISIBLE);
                setReportRejectedAdapter();
                break;
        }
    }

    private void showStatus() {
        if (report_layout_status == 0) {

        } else {

        }
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
        if(requestCode == GET_MY_REPORTS) {
            getMyReports();
        }
    }

    public void shortCompleteList(Editable s) {
        if (completedReportListTemp != null) {
            if (completedReportListTemp.size() > 0) {
                String value = "" + s;
                value = value.trim();
                completedReportList.clear();

                if (value != null) {
                    if (value.equals("")) {
                        completedReportList.addAll(completedReportListTemp);
                    } else {
                        for (int i = 0; i < completedReportListTemp.size(); i++) {
                            if (completedReportListTemp.get(i).getStrOrderId().toLowerCase().
                                    contains(value.toLowerCase())) {
                                Reports_Model.ReportsList item = new Reports_Model.ReportsList();
                                item.setStrOrderId(completedReportListTemp.get(i).getStrOrderId());
                                item.setStrCountryCode(completedReportListTemp.get(i).getStrCountryCode());
                                item.setStrPhone(completedReportListTemp.get(i).getStrPhone());
                                item.setStrAddress(completedReportListTemp.get(i).getStrAddress());
                                item.setStrSellerComment(completedReportListTemp.get(i).getStrSellerComment());
                                item.setStrStatus(completedReportListTemp.get(i).getStrStatus());
                                item.setStrOrderedDate(completedReportListTemp.get(i).getStrOrderedDate());
                                item.setStrTotalAmount(completedReportListTemp.get(i).getStrTotalAmount());
                                item.setStrPaymentType(completedReportListTemp.get(i).getStrPaymentType());
                                item.setOrderHistoryList(completedReportListTemp.get(i).getOrderHistoryList());
                                item.setProductsList(completedReportListTemp.get(i).getProductsList());
                                completedReportList.add(item);
                            }
                        }
                    }
                }
                reportCompleteAdapter.notifyDataSetChanged();
            }
        }
    }

    public void shortRejectedList(Editable s) {
        if (rejectedReportListTemp != null) {
            if (rejectedReportListTemp.size() > 0) {
                String value = "" + s;
                value = value.trim();
                rejectedReportList.clear();

                if (value != null) {
                    if (value.equals("")) {
                        rejectedReportList.addAll(rejectedReportListTemp);
                    } else {
                        for (int i = 0; i < rejectedReportListTemp.size(); i++) {
                            if (rejectedReportListTemp.get(i).getStrOrderId().toLowerCase().
                                    contains(value.toLowerCase())) {
                                Reports_Model.ReportsList item = new Reports_Model.ReportsList();
                                item.setStrOrderId(rejectedReportListTemp.get(i).getStrOrderId());
                                item.setStrCountryCode(rejectedReportListTemp.get(i).getStrCountryCode());
                                item.setStrPhone(rejectedReportListTemp.get(i).getStrPhone());
                                item.setStrAddress(rejectedReportListTemp.get(i).getStrAddress());
                                item.setStrSellerComment(rejectedReportListTemp.get(i).getStrSellerComment());
                                item.setStrStatus(rejectedReportListTemp.get(i).getStrStatus());
                                item.setStrOrderedDate(rejectedReportListTemp.get(i).getStrOrderedDate());
                                item.setStrTotalAmount(rejectedReportListTemp.get(i).getStrTotalAmount());
                                item.setStrPaymentType(rejectedReportListTemp.get(i).getStrPaymentType());
                                item.setOrderHistoryList(rejectedReportListTemp.get(i).getOrderHistoryList());
                                item.setProductsList(rejectedReportListTemp.get(i).getProductsList());
                                rejectedReportList.add(item);
                            }
                        }
                    }
                }
                reportCompleteAdapter.notifyDataSetChanged();
            }
        }
    }

}
