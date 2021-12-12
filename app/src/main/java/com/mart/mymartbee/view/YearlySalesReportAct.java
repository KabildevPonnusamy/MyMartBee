package com.mart.mymartbee.view;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Utils;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.mart.mymartbee.R;
import com.mart.mymartbee.algorithm.TripleDes;
import com.mart.mymartbee.commons.Util;
import com.mart.mymartbee.constants.Constants;
import com.mart.mymartbee.custom.HintAdapter;
import com.mart.mymartbee.custom.NetworkAvailability;
import com.mart.mymartbee.model.NewReportProducts_Model;
import com.mart.mymartbee.model.NewReportSales_Model;
import com.mart.mymartbee.storage.MyPreferenceDatas;
import com.mart.mymartbee.storage.StorageDatas;
import com.mart.mymartbee.viewmodel.implementor.NewReportViewModelImpl;
import com.mart.mymartbee.viewmodel.interfaces.NewReportViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class YearlySalesReportAct extends AppCompatActivity implements View.OnClickListener {

    ImageView yearly_report_back;
    MyPreferenceDatas preferenceDatas;
    String myKeyValue = "";
    ProgressDialog progressDialog;
    NewReportViewModel newReportViewModel;
    String strCateId = "";
    String strSellerId = "";
    String strSubCateId = "";
    String strSubCateName = "";
    String strYear = "";
    String strSelectFrom = "";
    ArrayList<NewReportSales_Model.CategoryRevenue> categoryRevenuesList;
    Spinner years_spinner, categories_spinner;
    ArrayList<String> yearlyDatas;
    ArrayList<String> categoryDatas;
    ArrayList<NewReportProducts_Model.CategoryViews> cateViewsList;
    ArrayList<NewReportSales_Model.CategoryRevenue> cateRevenuesList; // It is for getting the data from previous activity.

    HorizontalBarChart yearly_barchart;
    BarData barData;
    BarDataSet barDataSet;
    ArrayList barEntries;

    private static int MAX_X_VALUE = 12;
    private static int MAX_Y_VALUE = 45;
    private static int MIN_Y_VALUE = 5;
    private static final String SET_LABEL = "Average Temperature";
    private static String[] DAYS; // = { "JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC" };
    public int LAUNCH_FIRST_RUN = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yearlysalesreport);

        newReportViewModel = ViewModelProviders.of(this).get(NewReportViewModelImpl.class);
        getBundle();
        initView();
        myPreferenceDatas();

        progressObserve();

    }

    public void getBundle() {
        Bundle bundle = getIntent().getExtras();
        strYear = bundle.getString("selectYear");
        strSubCateId = bundle.getString("selectSubCateId");
        strSubCateName = bundle.getString("selectSubCateName");
        strSelectFrom = bundle.getString("selectFrom");
        Log.e("appSample", "Subcatename: " + strSubCateName);
    }

    public void initView() {
        yearlyDatas = new ArrayList<String>();
        categoryDatas = new ArrayList<String>();
        years_spinner = findViewById(R.id.years_spinner);
        categories_spinner = findViewById(R.id.categories_spinner);
        yearly_barchart = findViewById(R.id.yearly_barchart);
        yearly_report_back = findViewById(R.id.yearly_report_back);
        cateRevenuesList = new ArrayList<NewReportSales_Model.CategoryRevenue>();
        cateViewsList = new ArrayList<NewReportProducts_Model.CategoryViews>();
        categoryRevenuesList = new ArrayList<NewReportSales_Model.CategoryRevenue>();

        if (strSelectFrom.equalsIgnoreCase("Product")) {
            cateViewsList = StorageDatas.getInstance().getCategoryViewsList();

            for (int i = 0; i < cateViewsList.size(); i++) {
                categoryDatas.add(cateViewsList.get(i).getStrCategory());
            }

        } else if (strSelectFrom.equalsIgnoreCase("Sales")) {
            cateRevenuesList = StorageDatas.getInstance().getCategoryRevenuesList();

            for (int i = 0; i < cateRevenuesList.size(); i++) {
                categoryDatas.add(cateRevenuesList.get(i).getStrCategory());
            }
        }

        categoryDatas.add("Select");

        yearlyDatas.add("2021");
        yearlyDatas.add("2022");
        yearlyDatas.add("Select");


        yearly_report_back.setOnClickListener(this);
        spinnerViews();

//        chartDatas();
    }

    public void spinnerViews() {
        int yearPosition = 0;
        for (int i = 0; i < yearlyDatas.size(); i++) {
            if (yearlyDatas.get(i).equalsIgnoreCase(strYear)) {
                yearPosition = i;
            }
        }

        HintAdapter dataAdapter = new HintAdapter(this, android.R.layout.simple_spinner_item, yearlyDatas);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        years_spinner.setAdapter(dataAdapter);
//        years_spinner.setSelection(dataAdapter.getCount());
        years_spinner.setSelection(yearPosition);

        years_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strYear = years_spinner.getSelectedItem().toString();
                Log.e("appSample", "Year: " + strYear);
                if (!strYear.equalsIgnoreCase("Select")) {
                    if(LAUNCH_FIRST_RUN != 0) {
                        getSalesReportByYearAPI();
                    } else {
                        LAUNCH_FIRST_RUN = 1;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        int categoryPosition = 0;
        for (int i = 0; i < categoryDatas.size(); i++) {
            if (categoryDatas.get(i).equalsIgnoreCase(strSubCateName)) {
                categoryPosition = i;
            }
        }

        HintAdapter dataAdapterCate = new HintAdapter(this, android.R.layout.simple_spinner_item, categoryDatas);
        dataAdapterCate.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categories_spinner.setAdapter(dataAdapterCate);
//        categories_spinner.setSelection(dataAdapterCate.getCount());
        categories_spinner.setSelection(categoryPosition);

        categories_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String category = categories_spinner.getSelectedItem().toString();
                getSubCategoryId(category);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    public void getSubCategoryId(String category) {

        if (strSelectFrom.equalsIgnoreCase("Product")) {

            for (int i = 0; i < cateViewsList.size(); i++) {
                if (cateViewsList.get(i).getStrCategory().equalsIgnoreCase(category)) {
                    strSubCateId = cateViewsList.get(i).getStrCat_id();
                }
            }

        } else if (strSelectFrom.equalsIgnoreCase("Sales")) {

            for (int i = 0; i < cateRevenuesList.size(); i++) {
                if (cateRevenuesList.get(i).getStrCategory().equalsIgnoreCase(category)) {
                    strSubCateId = cateRevenuesList.get(i).getStrCateId();
                }
            }
        }

        Log.e("appSample", "Category: " + strSubCateId);
        if (!strSubCateId.equalsIgnoreCase("Select")) {
            getSalesReportByYearAPI();
        }

    }

    public void chartDatas(ArrayList<NewReportSales_Model.CategoryRevenue> categoryRevenue) {

        DAYS = new String[categoryRevenue.size()];

        for (int i = 0; i < categoryRevenue.size(); i++) {
            DAYS[i] = categoryRevenue.get(i).getStrMonth();
        }

        float min = Float.parseFloat(categoryRevenue.get(0).getStrTotal());
        float max = Float.parseFloat(categoryRevenue.get(0).getStrTotal());

        int n = categoryRevenue.size();

        for (int i = 1; i < n; i++) {
            if (Float.parseFloat(categoryRevenue.get(i).getStrTotal()) < min) {
                min = Float.parseFloat(categoryRevenue.get(i).getStrTotal());
            }
        }

        for (int i = 1; i < n; i++) {
            if (Float.parseFloat(categoryRevenue.get(i).getStrTotal()) > max) {
                max = Float.parseFloat(categoryRevenue.get(i).getStrTotal());
            }
        }

        Log.e("appSample", "MinimumValue: " + min);
        Log.e("appSample", "MaximumValue: " + max);

        MAX_X_VALUE = categoryRevenue.size();
        MAX_Y_VALUE = Math.round(max);
        MIN_Y_VALUE = Math.round(min);

//        Random r = new Random();

        ArrayList<BarEntry> values = new ArrayList<>();
        for (int i = 0; i < MAX_X_VALUE; i++) {
            float x = i;
            float y = Float.parseFloat(categoryRevenue.get(i).getStrTotal());
//            float y = MIN_Y_VALUE + r.nextFloat() * (MAX_Y_VALUE - MIN_Y_VALUE);
            values.add(new BarEntry(x, y));
        }
        BarDataSet set1 = new BarDataSet(values, SET_LABEL);

        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);

        BarData data = new BarData(dataSets);

        yearly_barchart.getDescription().setEnabled(false);

        XAxis xAxis = yearly_barchart.getXAxis();
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return DAYS[(int) value];
            }
        });

        data.setValueTextSize(12f);
        yearly_barchart.setData(data);
        yearly_barchart.invalidate();

        /*setData();

        barDataSet = new BarDataSet(barEntries, "");
        barData = new BarData(barDataSet);
        yearly_barchart.setData(barData);
        barDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(12f);*/
    }

    /*private void setData() {
        barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(2f, 2));
        barEntries.add(new BarEntry(4f, 4));
        barEntries.add(new BarEntry(6f, 6));
        barEntries.add(new BarEntry(8f, 8));
        barEntries.add(new BarEntry(10f, 10));
        barEntries.add(new BarEntry(3f, 3));
        barEntries.add(new BarEntry(6f, 6));
        barEntries.add(new BarEntry(9f, 9));
        barEntries.add(new BarEntry(4f, 4));
        barEntries.add(new BarEntry(8f, 8));
        barEntries.add(new BarEntry(5f, 5));
        barEntries.add(new BarEntry(10f, 10));
    }*/

    public void progressObserve() {
        newReportViewModel.getReportsNewUpdation().observe(this, new Observer<Boolean>() {
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

    private void myPreferenceDatas() {
        myKeyValue = getResources().getString(R.string.myTripleKey);
        preferenceDatas = new MyPreferenceDatas(YearlySalesReportAct.this);
        strCateId = TripleDes.getDESDecryptValue(preferenceDatas.getPrefString(MyPreferenceDatas.SELLER_CATEGORY), myKeyValue);
        strSellerId = TripleDes.getDESDecryptValue(preferenceDatas.getPrefString(MyPreferenceDatas.SELLER_ID), myKeyValue);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.yearly_report_back:
                finish();
                break;
        }
    }

    public void getSalesReportByYearAPI() {

        if (!strYear.equals("0") && !strYear.equalsIgnoreCase("Select") &&
                !strSubCateId.equals("0") && !strSubCateId.equalsIgnoreCase("Select")) { //
            Map<String, String> params = new HashMap<String, String>();
            params.put("seller_id", strSellerId);
            params.put("cat_id", strCateId);
            params.put("sub_cat_id", strSubCateId);
            params.put("year", strYear);

            if (NetworkAvailability.isNetworkAvailable(YearlySalesReportAct.this)) {
                newReportViewModel.getSalesReportsByYear(params);
            } else {
                NetworkAvailability networkAvailability = new NetworkAvailability(this);
                networkAvailability.noInternetConnection(YearlySalesReportAct.this, Constants.NETWORK_ENABLE_SETTINGS);
            }

            newReportViewModel.getSalesReportsByYearLV().observe(this, new Observer<NewReportSales_Model>() {
                @Override
                public void onChanged(NewReportSales_Model newReportSales_model) {
                    if (newReportSales_model.isStrStatus() == true) {
                        if (newReportSales_model.getStrTotalRevenue() != null) {
                            categoryRevenuesList.clear();
                            categoryRevenuesList = newReportSales_model.getCategoryRevenue();

                            if (categoryRevenuesList.size() > 0) {
                                chartDatas(categoryRevenuesList);
                            }
                        }
                    }
                }
            });
        }
    }
}
