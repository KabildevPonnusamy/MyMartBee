package com.mart.mymartbee.view.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mart.mymartbee.R;
import com.mart.mymartbee.algorithm.TripleDes;
import com.mart.mymartbee.constants.Constants;
import com.mart.mymartbee.custom.HintAdapter;
import com.mart.mymartbee.custom.NetworkAvailability;
import com.mart.mymartbee.custom.SweetAlert.SweetAlertDialog;
import com.mart.mymartbee.model.NewReportProducts_Model;
import com.mart.mymartbee.model.NewReportSales_Model;
import com.mart.mymartbee.model.Orders_Model;
import com.mart.mymartbee.model.Products_Model;
import com.mart.mymartbee.storage.MyPreferenceDatas;
import com.mart.mymartbee.storage.StorageDatas;
import com.mart.mymartbee.view.YearlySalesReportAct;
import com.mart.mymartbee.view.adapters.NewPendingOrdersAdapter;
import com.mart.mymartbee.view.adapters.ProductReportViewsCategoryAdapter;
import com.mart.mymartbee.view.adapters.ProductReportViewsProgressAdapter;
import com.mart.mymartbee.view.adapters.SalesReportCategoryAdapter;
import com.mart.mymartbee.view.adapters.SalesReportCategoryProgressAdapter;
import com.mart.mymartbee.viewmodel.implementor.NewReportViewModelImpl;
import com.mart.mymartbee.viewmodel.interfaces.NewReportViewModel;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class NewReportsFragment extends Fragment implements View.OnClickListener, Constants {

    ProgressDialog progressDialog;
    CardView sales_card, product_card;
    TextView sales_tv, product_tv;
    MyPreferenceDatas preferenceDatas;
    String myKeyValue = "";

    ArrayList<String> monthDatas;
    ArrayList<String> yearDatas;

    private PieChart pie_chart_sales;
    private float i1 = 13.5f;
    private float i2 = 25.5f;
    private float i3 = 20.5f;
    private float i4 = 40.5f;

    LinearLayout sales_layout, product_layout;

    NewReportViewModel newReportViewModel;
    ArrayList<NewReportProducts_Model.ProductViews> productViewsList;
    ArrayList<NewReportProducts_Model.CategoryViews> categoryViewsList;
    ArrayList<NewReportSales_Model.CategoryRevenue> categoryRevenuesList;
    TextView total_sales_tv;
    RecyclerView sales_category_recyclerView;
    SalesReportCategoryAdapter salesReportCategoryAdapter;
    LinearLayoutManager lManager = null;
    Spinner month_sales_spinner, year_sales_spinner;
    Spinner month_product_spinner, year_product_spinner;
    TextView total_product_views_tv;
    private PieChart pie_chart_product;
    RecyclerView product_views_recyclerView, progress_sales_recycler, progress_productview_recycler;
    ProductReportViewsCategoryAdapter productReportViewsCategoryAdapter;
    SalesReportCategoryProgressAdapter salesReportCategoryProgressAdapter;
    ProductReportViewsProgressAdapter productReportViewsProgressAdapter;
    String strCateId, strSellerId, strSalesMonthVal = "0", strProductMonthVal = "0", strSalesYearVal = "0",
            strProductYearVal = "0";
    ScrollView sales_report_scroll, product_report_scroll;
    LinearLayout no_product_report_layout, no_sales_report_layout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_frag_reports, container, false);

        newReportViewModel = ViewModelProviders.of(getActivity()).get(NewReportViewModelImpl.class);
        initView(view);
        myPreferenceDatas();
        progressObserve();
        setListeners();
//        getSalesProductsAPI();

        return view;
    }

    public void progressObserve() {
        newReportViewModel.getReportsNewUpdation().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean progressObserve) {
                if(progressObserve) {
                    showProgress();
                } else {
                    hideProgress();
                }
            }
        });
    }

    public void getSalesAPI() {
        Log.e("appSample", "getSalesAPI: calls");
        Log.e("appSample", "strSalesYearVal: " + strSalesYearVal);
        Log.e("appSample", "strSalesMonthVal: " + strSalesMonthVal);

        if(!strSalesYearVal.equals("Select") && !strSalesMonthVal.equals("Select") &&
                !strSalesYearVal.equals("0") && !strSalesMonthVal.equals("0")) {
            Map<String, String> params = new HashMap<String, String>();
            params.put("seller_id", strSellerId);
            params.put("cat_id", strCateId);
//            params.put("sub_cat_id", "90");
            params.put("year", strSalesYearVal);
            params.put("month", strSalesMonthVal);

            if (NetworkAvailability.isNetworkAvailable(getActivity())) {
                newReportViewModel.getSalesReports(params);
            } else {
                NetworkAvailability networkAvailability = new NetworkAvailability(getActivity());
                networkAvailability.noInternetConnection(getActivity(), Constants.NETWORK_ENABLE_SETTINGS);
            }

            newReportViewModel.getSalesReportsLV().observe(this, new Observer<NewReportSales_Model>() {
                @Override
                public void onChanged(NewReportSales_Model newReportSales_model) {
                    if(newReportSales_model.isStrStatus() == true) {

                        product_layout.setVisibility(View.GONE);

                        if(newReportSales_model.getStrTotalRevenue() == null) {
                              sales_report_scroll.setVisibility(View.GONE);
                              no_sales_report_layout.setVisibility(View.VISIBLE);
                        } else {
                            sales_report_scroll.setVisibility(View.VISIBLE);
                            no_sales_report_layout.setVisibility(View.GONE);

                            categoryRevenuesList.clear();
                            categoryRevenuesList = newReportSales_model.getCategoryRevenue();
                            if(categoryRevenuesList.size() > 0) {
                                addToPieChart(newReportSales_model.getStrTotalRevenue());
                                setCateSalesAdapter();
                                setSalesCategoryProductProgressAdapter();
                            }
                        }
                    }
                }
            });
        }

    }

    public void getProductsAPI() {
        Log.e("appSample", "getProductsAPI: calls");
        if(!strProductYearVal.equals("Select") && !strProductMonthVal.equals("Select") &&
                !strProductYearVal.equals("0") && !strProductMonthVal.equals("0")) {
            Map<String, String> params1 = new HashMap<String, String>();
            params1.put("seller_id", strSellerId);
            params1.put("cat_id", strCateId);
//        params1.put("sub_cat_id", "90");
            params1.put("year", strProductYearVal);
            params1.put("month", strProductMonthVal);

            if (NetworkAvailability.isNetworkAvailable(getActivity())) {
                newReportViewModel.getProductReports(params1);
            } else {
                NetworkAvailability networkAvailability = new NetworkAvailability(getActivity());
                networkAvailability.noInternetConnection(getActivity(), Constants.NETWORK_ENABLE_SETTINGS);
            }

            newReportViewModel.getProductReportsLV().observe(this, new Observer<NewReportProducts_Model>() {
                @Override
                public void onChanged(NewReportProducts_Model newReportProducts_model) {
                    if(newReportProducts_model.isStrStatus() == true) {
                        sales_layout.setVisibility(View.GONE);

                        if(newReportProducts_model.getStrTotalViews() == null) {
                            product_report_scroll.setVisibility(View.GONE);
                            no_product_report_layout.setVisibility(View.VISIBLE);
                        } else {
                            product_report_scroll.setVisibility(View.VISIBLE);
                            no_product_report_layout.setVisibility(View.GONE);

                            productViewsList.clear();
                            categoryViewsList.clear();
                            productViewsList = newReportProducts_model.getProductViews();
                            categoryViewsList = newReportProducts_model.getCategoryViews();

                            if(categoryViewsList.size() > 0) {
                                Log.e("appSample", "CateSize: " + categoryViewsList.size());
                                pieChartForViewsCategory(newReportProducts_model.getStrTotalViews());//total_product_views_tv
                                setCateProductReportAdapter();
                            }

                            if(productViewsList.size() > 0) {
                                Log.e("appSample", "Size: " + productViewsList.size());
                                setProductViewsProgressAdapter();
                            }
                        }


                    }
                }
            });
        }

    }

    public void setListeners() {

        sales_category_recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
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
                    Log.e("appSample", "CateIdSales: " + categoryRevenuesList.get(position).getStrCateId());

                    StorageDatas.getInstance().setCategoryRevenuesList(categoryRevenuesList);
                    Intent intent = new Intent(getActivity(), YearlySalesReportAct.class);
                    intent.putExtra("selectSubCateId", categoryRevenuesList.get(position).getStrCateId());
                    intent.putExtra("selectSubCateName", categoryRevenuesList.get(position).getStrCategory());
                    intent.putExtra("selectYear", strSalesYearVal);
                    intent.putExtra("selectFrom", "Sales");
                    startActivity(intent);
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

        product_views_recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
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
                    Log.e("appSample", "CateIdSales: " + categoryViewsList.get(position).getStrCateId());

                    StorageDatas.getInstance().setCategoryViewsList(categoryViewsList);
                    Intent intent = new Intent(getActivity(), YearlySalesReportAct.class);
                    intent.putExtra("selectSubCateId", categoryViewsList.get(position).getStrCateId());
                    intent.putExtra("selectSubCateName", categoryViewsList.get(position).getStrCategory());
                    intent.putExtra("selectYear", strSalesYearVal);
                    intent.putExtra("selectFrom", "Product");
                    startActivity(intent);

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

    public void setProductViewsProgressAdapter() {

        Collections.sort(productViewsList, (t1, t2) -> t1.getStrViews().compareTo(t2.getStrViews()));

        progress_productview_recycler.setHasFixedSize(true);
        lManager = new LinearLayoutManager(getActivity());
        lManager.setReverseLayout(true);
        lManager.setStackFromEnd(true);
        progress_productview_recycler.setLayoutManager(lManager);
        productReportViewsProgressAdapter = new ProductReportViewsProgressAdapter(productViewsList, getActivity());
        progress_productview_recycler.setAdapter(productReportViewsProgressAdapter);
    }

    public void setSalesCategoryProductProgressAdapter() {

        Collections.sort(categoryRevenuesList, (t1, t2) -> t1.getStrTotal().compareTo(t2.getStrTotal()));

        progress_sales_recycler.setHasFixedSize(true);
        lManager = new LinearLayoutManager(getActivity());
        lManager.setReverseLayout(true);
        lManager.setStackFromEnd(true);
        progress_sales_recycler.setLayoutManager(lManager);
        salesReportCategoryProgressAdapter = new SalesReportCategoryProgressAdapter(categoryRevenuesList, getActivity());
        progress_sales_recycler.setAdapter(salesReportCategoryProgressAdapter);
    }

    public void setCateProductReportAdapter() {
        product_views_recyclerView.setHasFixedSize(true);
        lManager = new LinearLayoutManager(getActivity());
        product_views_recyclerView.setLayoutManager(lManager);
        productReportViewsCategoryAdapter = new ProductReportViewsCategoryAdapter(categoryViewsList, getActivity());
        product_views_recyclerView.setAdapter(productReportViewsCategoryAdapter);
    }

    public void pieChartForViewsCategory(String strTotalViews) {
        if(strTotalViews == null) {
            strTotalViews = "0";
        }

        pie_chart_product.clearChart();
        total_product_views_tv.setText("Total Viwes " + strTotalViews);
        for(int i=0; i <categoryViewsList.size(); i++) {

            if(!categoryViewsList.get(i).getStrPercent().equalsIgnoreCase("0")) {
                pie_chart_product.addPieSlice(new PieModel(
                        categoryViewsList.get(i).getStrCategory(),
                        Float.parseFloat(categoryViewsList.get(i).getStrPercent()),
                        Color.parseColor("#" + categoryViewsList.get(i).getStrColor())
                ));
            }
        }

        pie_chart_product.startAnimation();
    }

    public void initView(View view) {
        sales_layout = view.findViewById(R.id.sales_layout);
        product_layout = view.findViewById(R.id.product_layout);
        sales_report_scroll = view.findViewById(R.id.sales_report_scroll);
        product_report_scroll = view.findViewById(R.id.product_report_scroll);
        no_product_report_layout = view.findViewById(R.id.no_product_report_layout);
        no_sales_report_layout = view.findViewById(R.id.no_sales_report_layout);

        sales_report_scroll.setVisibility(View.GONE);
        product_report_scroll.setVisibility(View.GONE);
        no_product_report_layout.setVisibility(View.GONE);
        no_sales_report_layout.setVisibility(View.GONE);

        month_product_spinner = view.findViewById(R.id.month_product_spinner);
        year_product_spinner = view.findViewById(R.id.year_product_spinner);
        total_product_views_tv = view.findViewById(R.id.total_product_views_tv);
        pie_chart_product = view.findViewById(R.id.pie_chart_product);
        product_views_recyclerView = view.findViewById(R.id.product_views_recyclerView);
        progress_sales_recycler = view.findViewById(R.id.progress_sales_recycler);
        progress_productview_recycler = view.findViewById(R.id.progress_productview_recycler);

        ViewCompat.setNestedScrollingEnabled(product_views_recyclerView, false);
        ViewCompat.setNestedScrollingEnabled(progress_sales_recycler, false);
        ViewCompat.setNestedScrollingEnabled(progress_productview_recycler, false);

        categoryRevenuesList = new ArrayList<NewReportSales_Model.CategoryRevenue>();
        productViewsList = new ArrayList<NewReportProducts_Model.ProductViews>();
        categoryViewsList = new ArrayList<NewReportProducts_Model.CategoryViews>();

        sales_category_recyclerView = view.findViewById(R.id.sales_category_recyclerView);
        ViewCompat.setNestedScrollingEnabled(sales_category_recyclerView, false);
        total_sales_tv = view.findViewById(R.id.total_sales_tv);
        pie_chart_sales = view.findViewById(R.id.pie_chart_sales);
        monthDatas = new ArrayList<String>();
        yearDatas = new ArrayList<String>();
        month_sales_spinner = view.findViewById(R.id.month_sales_spinner);
        year_sales_spinner = view.findViewById(R.id.year_sales_spinner);
        sales_card = view.findViewById(R.id.sales_card);
        product_card = view.findViewById(R.id.product_card);
        sales_tv = view.findViewById(R.id.sales_tv);
        product_tv = view.findViewById(R.id.product_tv);
        sales_card.setOnClickListener(this);
        product_card.setOnClickListener(this);
        addMonthData();
        addYearData();

        monthSalesSpinnerInfo();
        monthProductSpinnerInfo();

        yearSalesSpinnerInfo();
        yearProductSpinnerInfo();

    }

    public void addYearData() {
        yearDatas.add("2021");
        yearDatas.add("2022");
        yearDatas.add("Select");
    }

    public void addMonthData() {
        monthDatas.add("Jan");
        monthDatas.add("Feb");
        monthDatas.add("Mar");
        monthDatas.add("Apr");
        monthDatas.add("May");
        monthDatas.add("Jun");
        monthDatas.add("Jul");
        monthDatas.add("Aug");
        monthDatas.add("Sep");
        monthDatas.add("Oct");
        monthDatas.add("Nov");
        monthDatas.add("Dec");

        monthDatas.add("Select");
    }

    public void setCateSalesAdapter() {
        sales_category_recyclerView.setHasFixedSize(true);
        lManager = new LinearLayoutManager(getActivity());
        sales_category_recyclerView.setLayoutManager(lManager);
        salesReportCategoryAdapter = new SalesReportCategoryAdapter(categoryRevenuesList, getActivity());
        sales_category_recyclerView.setAdapter(salesReportCategoryAdapter);
    }

    public void addToPieChart(String totalRevenue) {
        if(totalRevenue == null) {
            totalRevenue = "0";
        }

        total_sales_tv.setText("Total Sales\n RM " + totalRevenue);

        pie_chart_sales.clearChart();

        for(int i=0; i <categoryRevenuesList.size(); i++) {
            if(!categoryRevenuesList.get(i).getStrPercent().equalsIgnoreCase("0")) {
                pie_chart_sales.addPieSlice(new PieModel(
                        categoryRevenuesList.get(i).getStrCategory(),
                        Float.parseFloat(categoryRevenuesList.get(i).getStrPercent()),
                        Color.parseColor("#" + categoryRevenuesList.get(i).getStrColor())
                ));
            }
        }

        /*pie_chart_sales.addPieSlice(new PieModel("Computer", i1, Color.parseColor("#FFA726")));
        pie_chart_sales.addPieSlice(new PieModel("Laptops", i2, Color.parseColor("#66BB6A")));
        pie_chart_sales.addPieSlice(new PieModel("Mouse", i3, Color.parseColor("#EF5350")));
        pie_chart_sales.addPieSlice(new PieModel("Keyboards", i4, Color.parseColor("#2986F6")));*/

        pie_chart_sales.startAnimation();
    }

    public void yearProductSpinnerInfo() {
        HintAdapter dataAdapter = new HintAdapter(getActivity(), android.R.layout.simple_spinner_item, yearDatas);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        year_product_spinner.setAdapter(dataAdapter);
        year_product_spinner.setSelection(dataAdapter.getCount());

        year_product_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strProductYearVal = year_product_spinner.getSelectedItem().toString();
                Log.e("appSample", "YearVal: " + strProductYearVal);
                if(!strProductYearVal.equalsIgnoreCase("Select")) {
                    Log.e("appSample", "YearNotSelect: ");
                    getProductsAPI();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public void monthProductSpinnerInfo() {
        HintAdapter dataAdapter = new HintAdapter(getActivity(), android.R.layout.simple_spinner_item, monthDatas);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        month_product_spinner.setAdapter(dataAdapter);
        month_product_spinner.setSelection(dataAdapter.getCount());

        month_product_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String monthName = month_product_spinner.getSelectedItem().toString();
                Log.e("appSample", "MonthName: " + monthName);
                int val = position + 1;

                if(val == 13) {
                    strProductMonthVal = "0";
                } else {
                    strProductMonthVal = "" + val;
                }


                if(!monthName.equalsIgnoreCase("Select")) {
                    Log.e("appSample", "MonthNotSelect: ");
                    getProductsAPI();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public void monthSalesSpinnerInfo() {
        HintAdapter dataAdapter = new HintAdapter(getActivity(), android.R.layout.simple_spinner_item, monthDatas);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        month_sales_spinner.setAdapter(dataAdapter);
        month_sales_spinner.setSelection(dataAdapter.getCount());

        month_sales_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String monthName = month_sales_spinner.getSelectedItem().toString();
                int val = position + 1;
                if(val == 13) {
                    strSalesMonthVal = "0";
                } else {
                    strSalesMonthVal = "" + val;
                }

                Log.e("appSample", "MonthName: " + monthName);
                Log.e("appSample", "MonthValue: " + strSalesMonthVal);
                if(!monthName.equalsIgnoreCase("Select")) {
                    getSalesAPI();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public void yearSalesSpinnerInfo() {
        HintAdapter dataAdapter = new HintAdapter(getActivity(), android.R.layout.simple_spinner_item, yearDatas);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        year_sales_spinner.setAdapter(dataAdapter);
        year_sales_spinner.setSelection(dataAdapter.getCount());

        year_sales_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strSalesYearVal = year_sales_spinner.getSelectedItem().toString();
                Log.e("appSample", "Year: " + strSalesYearVal);
                if(!strSalesYearVal.equalsIgnoreCase("Select")) {
                    getSalesAPI();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void myPreferenceDatas() {
        myKeyValue = getActivity().getResources().getString(R.string.myTripleKey);
        preferenceDatas = new MyPreferenceDatas(getActivity());
        strSellerId = TripleDes.getDESDecryptValue(preferenceDatas.getPrefString(MyPreferenceDatas.SELLER_ID), myKeyValue);
        strCateId = TripleDes.getDESDecryptValue(preferenceDatas.getPrefString(MyPreferenceDatas.SELLER_CATEGORY), myKeyValue);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sales_card:
                sales_card.setCardBackgroundColor(getResources().getColor(R.color.white));
                product_card.setCardBackgroundColor(getResources().getColor(R.color.main_color));

                sales_tv.setTextColor(ContextCompat.getColor(getActivity(), R.color.main_color));
                product_tv.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));

                sales_layout.setVisibility(View.VISIBLE);
                product_layout.setVisibility(View.GONE);
                break;

            case R.id.product_card:
                sales_card.setCardBackgroundColor(getResources().getColor(R.color.main_color));
                product_card.setCardBackgroundColor(getResources().getColor(R.color.white));

                sales_tv.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                product_tv.setTextColor(ContextCompat.getColor(getActivity(), R.color.main_color));

                sales_layout.setVisibility(View.GONE);
                product_layout.setVisibility(View.VISIBLE);
                break;
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

    public void closeKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
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

    }


}
