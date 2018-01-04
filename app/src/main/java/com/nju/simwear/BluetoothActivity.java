package com.nju.simwear;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.CompoundButton;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.kyleduo.switchbutton.SwitchButton;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class BluetoothActivity extends AppCompatActivity {

    private static final UUID BLUETOOTH_UUID =
            UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private BluetoothSocket mSocket = null;
    private boolean mStopReading = false;
    private SwitchButton mGPSButton = null;
    private SwitchButton mGPRSButton = null;
    private Handler mHander = null;
    private Gson mGson = new Gson();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
        setTitle("蓝牙控制");

        findViews();
        installHanlders();
        connectBluetooth();
    }

    private void findViews() {
        mGPSButton = (SwitchButton)findViewById(R.id.gps_button);
        mGPRSButton = (SwitchButton) findViewById(R.id.gprs_button);
    }

    private void installHanlders() {
        mHander = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                String message = (String)msg.obj;
                JsonObject jobj = mGson.fromJson(message, JsonObject.class);
                if (jobj.has("GPS")) {
                    String result = jobj.get("GPS").getAsString().toUpperCase();
                    if (result.equals("ON")) {
                        mGPSButton.setCheckedImmediatelyNoEvent(true);
                    }
                    else {
                        mGPSButton.setCheckedImmediatelyNoEvent(false);
                    }
                }
                if (jobj.has("GPRS")) {
                    String result = jobj.get("GPRS").getAsString().toUpperCase();
                    if (result.equals("ON")) {
                        mGPRSButton.setCheckedImmediatelyNoEvent(true);
                    }
                    else {
                        mGPRSButton.setCheckedImmediatelyNoEvent(false);
                    }
                }
            }
        };
        mGPSButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    sendData("bluetooth:turnGPS:1");
                }
                else {
                    sendData("bluetooth:turnGPS:0");
                }
            }
        });
        mGPRSButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    sendData("bluetooth:activateGPRS:1");
                }
                else {
                    sendData("bluetooth:activateGPRS:0");
                }
            }
        });
    }

    private void connectBluetooth() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Intent intent = getIntent();
                String bluetoothMac = intent.getStringExtra("device");

                BluetoothDevice device = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(bluetoothMac);
                if (device != null) {
                    try {
                        mSocket = device.createRfcommSocketToServiceRecord(BLUETOOTH_UUID);
                        mSocket.connect();
                        sendData("bluetooth:querystatus");
                        InputStream inputStream = BluetoothActivity.this.mSocket.getInputStream();
                        byte[] buffer = new byte[1024];
                        int bytes;
                        while (true) {
                            if (mStopReading) {
                                break;
                            }
                            if (inputStream.available() > 0) {
                                bytes = inputStream.read(buffer);
                                String message = new String(buffer, 0, bytes);
                                Message msg = new Message();
                                msg.obj = message;
                                mHander.sendMessage(msg);
                            }
                            else {
                                if (mStopReading) {
                                    break;
                                }
                                try {
                                    Thread.sleep(100);
                                }
                                catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                        try {
                            if (mSocket != null) {
                                mSocket.close();
                                mSocket = null;
                            }
                        }
                        catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            }
        }).start();
    }

    private void sendData(final String data) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OutputStream outputStream = BluetoothActivity.this.mSocket.getOutputStream();
                    outputStream.write(data.getBytes());
                    outputStream.flush();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSocket != null) {
            mStopReading = true;
            try {
                Thread.sleep(150);
            }
            catch (InterruptedException e) {}
            try {
                mSocket.close();
            }
            catch (IOException e) {}
        }
    }
}
