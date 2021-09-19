package com.mart.mymartbee.view;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mart.mymartbee.R;
import com.mart.mymartbee.algorithm.TripleDes;
import com.mart.mymartbee.storage.MyPreferenceDatas;
import com.mart.mymartbee.view.fragments.AccountsFragment;
import com.mart.mymartbee.view.fragments.HomeFragment;
import com.mart.mymartbee.view.fragments.OrdersFragment;
import com.mart.mymartbee.view.fragments.ProductsFragment;
import com.mart.mymartbee.view.fragments.ReportsFragment;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    BottomNavigationView bottom_navigation;
    MyPreferenceDatas preferenceDatas;
    String myKeyValue = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_home);

        initView();
//        getMyPreferences();
        moveHome();
    }

    private void getMyPreferences() {
        myKeyValue = getResources().getString(R.string.myTripleKey);
        preferenceDatas = new MyPreferenceDatas(HomeActivity.this);
        Log.e("appSample", "MBL: " + TripleDes.getDESDecryptValue(preferenceDatas.getPrefString(MyPreferenceDatas.SELLER_MOBILE), myKeyValue));
    }

    private void initView() {
        myKeyValue = getResources().getString(R.string.myTripleKey);
        bottom_navigation = findViewById(R.id.bottom_navigation);
        navigation_listener();
    }

    private void navigation_listener() {
        bottom_navigation.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        Fragment fragment = null;
                        switch (menuItem.getItemId()) {
                            case R.id.menu_nav_home:
                                moveHome();
                                return true;
                            case R.id.menu_nav_products:
                                moveProducts();
                                return true;
                            case R.id.menu_nav_order:
                                moveOrders();
                                return true;
                            case R.id.menu_reports:
                                moveReports();
                                return true;
                            case R.id.menu_nav_account:
                                moveAccounts();
                                return true;
                        }
                        return false;
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }

    private void moveHome(){
        Fragment homeFragment = new HomeFragment();
        if(homeFragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frame_layout, homeFragment);
            ft.commit();
        }
    }

    private void moveProducts(){
        Fragment productFragment = new ProductsFragment();
        if(productFragment != null) {
            FragmentTransaction pft = getSupportFragmentManager().beginTransaction();
            pft.replace(R.id.frame_layout, productFragment);
            pft.commit();
        }
    }

    private void moveOrders(){
        Fragment ordersFragment = new OrdersFragment();
        if(ordersFragment != null) {
            FragmentTransaction oft = getSupportFragmentManager().beginTransaction();
            oft.replace(R.id.frame_layout, ordersFragment);
            oft.commit();
        }
    }

    private void moveReports(){
        Fragment reportFragment = new ReportsFragment();
        if(reportFragment != null) {
            FragmentTransaction rft = getSupportFragmentManager().beginTransaction();
            rft.replace(R.id.frame_layout, reportFragment);
            rft.commit();
        }
    }

    private void moveAccounts() {
        Fragment accountsFragment = new AccountsFragment();
        if(accountsFragment != null) {
            FragmentTransaction aft = getSupportFragmentManager().beginTransaction();
            aft.replace(R.id.frame_layout, accountsFragment);
            aft.commit();
        }
    }
}
