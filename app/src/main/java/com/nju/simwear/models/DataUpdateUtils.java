package com.nju.simwear.models;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.nju.simwear.R;

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
}
