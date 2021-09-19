package com.mart.mymartbee.view.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mart.mymartbee.R;
import com.mart.mymartbee.algorithm.TripleDes;
import com.mart.mymartbee.model.ShopStatusModel;
import com.mart.mymartbee.storage.MyPreferenceDatas;
import com.mart.mymartbee.view.HomeActivity;
import com.mart.mymartbee.view.adapters.ShopStatusAdapter;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements View.OnClickListener {

    ArrayList<ShopStatusModel> shopStatusModel;
    RecyclerView recyclerView;
    ShopStatusAdapter shopStatusAdapter;
    RelativeLayout product_creation_layout, product_layout;
    MyPreferenceDatas preferenceDatas;
    String myKeyValue = "";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_home, container, false);

        initView(view);

        return view;
    }

    private void initView(View view) {
        myKeyValue = getActivity().getResources().getString(R.string.myTripleKey);
        preferenceDatas = new MyPreferenceDatas(getActivity());
        shopStatusModel = new ArrayList<ShopStatusModel>();
        product_creation_layout = (RelativeLayout) view.findViewById(R.id.product_creation_layout);
        product_layout = (RelativeLayout) view.findViewById(R.id.product_layout);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        if(TripleDes.getDESDecryptValue(preferenceDatas.getPrefString(MyPreferenceDatas.SELLER_PRODUCTS_COUNT), myKeyValue).equalsIgnoreCase("0")) {
            product_creation_layout.setVisibility(View.VISIBLE);
            product_layout.setVisibility(View.GONE);
            ShopStatusModel item1 = new ShopStatusModel("1", getActivity().getResources().getString(R.string.maintain_store),
                    getActivity().getResources().getString(R.string.vist_edit_store), "1");
            ShopStatusModel item2 = new ShopStatusModel("2", getActivity().getResources().getString(R.string.advertise_store),
                    getActivity().getResources().getString(R.string.share_store_link),
                    "0");
            shopStatusModel.add(item1);
            shopStatusModel.add(item2);
            setAdapter();
        } else {
            product_creation_layout.setVisibility(View.GONE);
            product_layout.setVisibility(View.VISIBLE);
        }

    }

    private void setAdapter() {
//        shopStatusModel.get(1).setStatus_status("1");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        shopStatusAdapter = new ShopStatusAdapter(shopStatusModel, getActivity());
        recyclerView.setAdapter(shopStatusAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }
}
