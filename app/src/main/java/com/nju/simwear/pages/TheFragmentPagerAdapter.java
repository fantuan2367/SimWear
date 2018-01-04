package com.nju.simwear.pages;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Onigiri on 2017/12/26.
 */

public class TheFragmentPagerAdapter extends FragmentPagerAdapter {

    private String[] titles = new String[]{"定位", "体温心率"};

    public TheFragmentPagerAdapter(FragmentManager fragmentManager){
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 1:{
                return new HeartTemperaturePage();
            }
            default:{
                return new LocationPage();
            }
        }
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public CharSequence getPageTitle(int position){
        return titles[position];
    }
}
