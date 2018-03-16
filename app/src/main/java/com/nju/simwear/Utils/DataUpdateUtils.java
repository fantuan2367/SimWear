package com.nju.simwear.Utils;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.nju.simwear.Exception.InternetErrorException;
import com.nju.simwear.R;

import java.util.Hashtable;

public class DataUpdateUtils {

    public static void setMapLocation(MapView mapView, double latitude, double longitude){
        BaiduMap baiduMap = mapView.getMap();
        //开启定位图层
        baiduMap.setMyLocationEnabled(true);
        //构造定位数据
        MyLocationData myLocationData = new MyLocationData.Builder()
                .latitude(latitude)     //此处设置维度
                .longitude(longitude)   //此处设置经度
                .build();
        //设置定位数据
        baiduMap.setMyLocationData(myLocationData);
        // 设置定位图层的配置（定位模式，是否允许方向信息，用户自定义定位图标）
        MyLocationConfiguration config = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL,
                true,
                BitmapDescriptorFactory.fromResource(R.drawable.location_icon));
        baiduMap.setMyLocationConfiguration(config);

        //移动地图到指定位置
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(new LatLng(latitude, longitude)).zoom(17);
        baiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
    }

    public static void startUpdateInfo(final Handler handler){
        new Thread(){
            private final int UPDATE_INTERVAL = 1000;


            public void run(){
                while(!Thread.currentThread().isInterrupted()){
                    Bundle bundle = new Bundle();
                    Message message = new Message();

                    try{
                        bundle = InternetUtils.getInfo();
                    } catch (InternetErrorException e) {
                        message.obj = "Internet Error";
                        handler.sendMessage(message);
                        break;
                    }

                    message.setData(bundle);
                    handler.sendMessage(message);

                    try {
                        Thread.sleep(UPDATE_INTERVAL);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }.start();
    }
}