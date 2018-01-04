package com.nju.simwear.pages;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.nju.simwear.R;


/**
 * Created by Onigiri on 2017/12/26.
 */

public class LocationPage extends Fragment {

    @Nullable
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.location_page, container, false);
        return view;
    }

    public void onWindowFocusChanged(boolean hasFocus){

    }
}
