package com.nju.simwear;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.MapView;
import com.nju.simwear.Utils.DataUpdateUtils;
import com.nju.simwear.pages.TheFragmentPagerAdapter;
import com.nju.simwear.views.CustomViewPager;

import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private TabLayout tabLayout;
    private CustomViewPager viewPager;
    private TheFragmentPagerAdapter theFragmentPagerAdapter;
    private static boolean updateStarted = false;

    private static class DataUpdateHandler extends Handler{
        private WeakReference<MainActivity> weakReference;

        public DataUpdateHandler(MainActivity mainActivity){
            weakReference = new WeakReference<MainActivity>(mainActivity);
        }

        @Override
        public void handleMessage(Message message){
            final MainActivity mainActivity = weakReference.get();
            if(message.obj != null) {
                mainActivity.showText(message.obj.toString());
            } else {
                Bundle bundle = message.getData();
                mainActivity.setTemperature(bundle.getString("temperature"));
                mainActivity.setHeartRate(bundle.getString("heartRate"));
            }
        }
    }

    private void initFragment(){
        //使用适配器将ViewPager与Fragment绑定在一起
        viewPager= findViewById(R.id.viewPager);
        theFragmentPagerAdapter = new TheFragmentPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(theFragmentPagerAdapter);
        viewPager.setPagingEnabled(false);


        //将TabLayout与ViewPager绑定在一起
        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        TextView tv1 = (TextView)(((LinearLayout)((LinearLayout)tabLayout.getChildAt(0)).getChildAt(0)).getChildAt(1));
        tv1.setScaleY(-1);
        TextView tv2 = (TextView)(((LinearLayout)((LinearLayout)tabLayout.getChildAt(0)).getChildAt(1)).getChildAt(1));
        tv2.setScaleY(-1);
    }

    private void initDraw(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void initMap(){
        SDKInitializer.initialize(getApplicationContext());
    }

    private void startUpdate(){
        if(!updateStarted) {
            DataUpdateHandler dataUpdateHandler = new DataUpdateHandler(this);
            DataUpdateUtils.startUpdateInfo(dataUpdateHandler);
            updateStarted = true;
        }
    }

    public void showText(String text){
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    public void setTemperature(String temperature){
        TextView temperatureTextView = findViewById(R.id.tempreature_text);
        temperatureTextView.setText(temperature);
    }

    public void setHeartRate(String heartRate){
        TextView heartRateTextView = findViewById(R.id.heart_rate_text);
        heartRateTextView.setText(heartRate);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initFragment();
        initDraw();
        initMap();
    }

    //返回键操作
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus){
        super.onWindowFocusChanged(hasFocus);
        MapView mapView = findViewById(R.id.baidu_map);

        DataUpdateUtils.setMapLocation(mapView, 32.12, 118.9658);

        startUpdate();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id){
            case R.id.nav_bluetooth:{
                BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
                if (adapter != null) {
                    if (!adapter.isEnabled()) {
                        Toast.makeText(this, "请打开蓝牙", Toast.LENGTH_LONG).show();
                        break;
                    }
                }
                else {
                    Toast.makeText(this, "没有检测到蓝牙模块", Toast.LENGTH_LONG).show();
                    break;
                }
                Intent intent = new Intent(this, BluetoothFinderActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.nav_setting:{
                Toast.makeText(this, "已点击设置", Toast.LENGTH_LONG).show();
                break;
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}