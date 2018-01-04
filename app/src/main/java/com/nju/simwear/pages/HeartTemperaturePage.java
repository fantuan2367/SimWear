package com.nju.simwear.pages;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nju.simwear.R;


/**
 * Created by Onigiri on 2017/12/26.
 */

public class HeartTemperaturePage extends Fragment {
    @Nullable
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState){
        return inflater.inflate(R.layout.heart_temperature_page, container, false);
    }
}
