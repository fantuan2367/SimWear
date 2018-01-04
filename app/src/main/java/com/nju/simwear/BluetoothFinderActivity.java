package com.nju.simwear;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.nju.simwear.models.DeviceItem;

import java.util.ArrayList;
import java.util.List;

public class BluetoothFinderActivity extends AppCompatActivity {
    private ListView mDevicesList = null;
    private DeviceListAdapter mAdapter = null;
    private Button mScanButton = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_finder);
        setTitle("蓝牙连接");
        List<DeviceItem> devices = new ArrayList<DeviceItem>();
        mAdapter = new DeviceListAdapter(this, devices);

        //设备列表点击
        mDevicesList = (ListView) findViewById(R.id.bluetooth_devices);
        mDevicesList.setAdapter(mAdapter);
        mDevicesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mScanButton.setText("开始扫描");
                DeviceItem item = (DeviceItem) view.getTag();
                Intent intent = new Intent(BluetoothFinderActivity.this, BluetoothActivity.class);
                intent.putExtra("device", item.getAddress());
                startActivity(intent);
            }
        });

        //按钮点击
        mScanButton = (Button)findViewById(R.id.scan_button);
        mScanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mScanButton.getText().equals("开始扫描")) {
                    startScanning();
                    mScanButton.setText("停止扫描");
                }
                else {
                    stopScanning();
                    mScanButton.setText("开始扫描");
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        startScanning();
        mScanButton.setText("停止扫描");
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopScanning();
        mScanButton.setText("开始扫描");
    }

    private void startScanning() {
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        mAdapter.clear();
        BluetoothFinderActivity.this.registerReceiver(mBluetoothReceiver, filter);
        BluetoothAdapter.getDefaultAdapter().startDiscovery();
    }

    private void stopScanning() {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter.isDiscovering()) {
            this.unregisterReceiver(mBluetoothReceiver);
            adapter.cancelDiscovery();
        }
    }

    private final BroadcastReceiver mBluetoothReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                DeviceItem newDevice = new DeviceItem(device.getName(), device.getAddress(), "false");
                mAdapter.add(newDevice);
            }
        }
    };
}
