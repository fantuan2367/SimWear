package com.nju.simwear;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.nju.simwear.models.DeviceItem;

import java.util.List;

public class DeviceListAdapter extends ArrayAdapter<DeviceItem> {

    public DeviceListAdapter(Context context, List items) {
        super(context, 0, items);
    }

    /**
     *
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    public View getView(int position, View convertView, ViewGroup parent) {
        DeviceItem item = (DeviceItem)getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.device_list_item, parent, false);
        }
        TextView deviceName = (TextView) convertView.findViewById(R.id.device_name);
        TextView macAddress = (TextView) convertView.findViewById(R.id.mac_address);

        deviceName.setText(item.getDeviceName());
        macAddress.setText(item.getAddress());
        convertView.setTag(item);

        return convertView;
    }
}
