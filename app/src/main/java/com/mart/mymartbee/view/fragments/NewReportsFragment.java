package com.mart.mymartbee.view.fragments;

import android.app.ProgressDialog;
import android.content.Context;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
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
import com.mart.mymartbee.model.Reports_Model;
import com.mart.mymartbee.storage.MyPreferenceDatas;
import com.mart.mymartbee.storage.StorageDatas;
import com.mart.mymartbee.view.ReportDetails;
import com.mart.mymartbee.view.adapters.ReportCompletedAdapter;
import com.mart.mymartbee.view.adapters.ReportRejectedAdapter;
import com.mart.mymartbee.viewmodel.implementor.ReportsViewModelImpl;
import com.mart.mymartbee.viewmodel.interfaces.ReportsViewModel;

//import org.eazegraph.lib.charts.PieChart;
//import org.eazegraph.lib.models.PieModel;

import java.util.ArrayList;
import java.util.List;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
//import com.github.mikephil.charting.charts.PieChart;

import com.intrusoft.scatter.ChartData;
import com.intrusoft.scatter.PieChart;
import com.intrusoft.scatter.SimpleChart;

public class NewReportsFragment extends Fragment implements View.OnClickListener, Constants {

    ProgressDialog progressDialog;
    CardView sales_card, product_card;
    TextView sales_tv, product_tv;
    MyPreferenceDatas preferenceDatas;
    String myKeyValue = "";
    String strSellerId;
    Spinner month_spinner;
    ArrayList<String> monthDatas;

    List<ChartData> data1;
    PieChart  simpleChart;

    /*private PieChart pie_chart;
    private float i1 = 13.5f;
    private float i2 = 25.5f;
    private float i3 = 20.5f;
    private float i4 = 40.5f;*/
//    private PieChart pieChart;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_frag_reports, container, false);

        initView(view);
        myPreferenceDatas();

        return view;
    }

    public void initView(View view) {
//        pieChart = view.findViewById(R.id.activity_main_piechart);
//        pie_chart = view.findViewById(R.id.pie_chart);

        simpleChart = (PieChart ) view.findViewById(R.id.simple_pie_chart);
        data1 = new ArrayList<>();

        monthDatas = new ArrayList<String>();
        month_spinner = view.findViewById(R.id.month_spinner);
        sales_card = view.findViewById(R.id.sales_card);
        product_card = view.findViewById(R.id.product_card);
        sales_tv = view.findViewById(R.id.sales_tv);
        product_tv = view.findViewById(R.id.product_tv);
        sales_card.setOnClickListener(this);
        product_card.setOnClickListener(this);
        spinnerInfos();

        addSimplePirChartDatas();
//        addToPieChart();

        /*setupPieChart();
        loadPieChartData();*/
    }

    public void addSimplePirChartDatas() {
        data1 = new ArrayList<>();
        data1.add(new ChartData("35%", 35, Color.WHITE, Color.parseColor("#0091EA")));
        data1.add(new ChartData("25%", 25, Color.WHITE, Color.parseColor("#33691E")));
        data1.add(new ChartData("30%", 30, Color.DKGRAY, Color.parseColor("#F57F17")));
        data1.add(new ChartData("10%", 10, Color.DKGRAY, Color.parseColor("#FFD600")));
        simpleChart.setChartData(data1);
    }
/*
    private void setupPieChart() {
        pieChart.setDrawHoleEnabled(true);
        pieChart.setUsePercentValues(true);
        pieChart.setEntryLabelTextSize(12);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setCenterText("Spending by Category");
        pieChart.setCenterTextSize(16);
        pieChart.getDescription().setEnabled(false);

        Legend l = pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setEnabled(true);
    }

    private void loadPieChartData() {
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(0.2f, "Food & Dining"));
        entries.add(new PieEntry(0.15f, "Medical"));
        entries.add(new PieEntry(0.10f, "Entertainment"));
        entries.add(new PieEntry(0.25f, "Electricity and Gas"));
        entries.add(new PieEntry(0.3f, "Housing"));

        ArrayList<Integer> colors = new ArrayList<>();
        for (int color: ColorTemplate.MATERIAL_COLORS) {
            colors.add(color);
        }

        for (int color: ColorTemplate.VORDIPLOM_COLORS) {
            colors.add(color);
        }

        PieDataSet dataSet = new PieDataSet(entries, "Expense Category");
        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setDrawValues(true);
        data.setValueFormatter(new PercentFormatter(pieChart));
        data.setValueTextSize(12f);
        data.setValueTextColor(Color.BLACK);

        pieChart.setData(data);
        pieChart.invalidate();

        pieChart.animateY(1400, Easing.EaseInOutQuad);
    }
*/

    /*public void addToPieChart() {
        pie_chart.addPieSlice(new PieModel("Computer", i1, Color.parseColor("#FFA726")));
        pie_chart.addPieSlice(new PieModel("Laptops", i2, Color.parseColor("#66BB6A")));
        pie_chart.addPieSlice(new PieModel("Mouse", i3, Color.parseColor("#EF5350")));
        pie_chart.addPieSlice(new PieModel("Keyboards", i4, Color.parseColor("#2986F6")));

        pie_chart.startAnimation();
    }*/

    public void spinnerInfos() {
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

        ArrayAdapter dataAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, monthDatas);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        month_spinner.setAdapter(dataAdapter);

        month_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String monthName = month_spinner.getSelectedItem().toString();
                Log.e("appSample", "MonthName: " + monthName);
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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sales_card:
                sales_card.setCardBackgroundColor(getResources().getColor(R.color.white));
                product_card.setCardBackgroundColor(getResources().getColor(R.color.main_color));

                sales_tv.setTextColor(ContextCompat.getColor(getActivity(), R.color.main_color));
                product_tv.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                break;

            case R.id.product_card:
                sales_card.setCardBackgroundColor(getResources().getColor(R.color.main_color));
                product_card.setCardBackgroundColor(getResources().getColor(R.color.white));

                sales_tv.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                product_tv.setTextColor(ContextCompat.getColor(getActivity(), R.color.main_color));
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
