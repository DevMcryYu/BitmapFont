package com.devmcryyu.bitmapfont;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.net.Socket;
import java.util.List;

/**
 * Created by 92075 on 2018/5/29.
 */

public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.ViewHolder> {
    private List<device> mDeviceList;
    private Context mContext;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView deviceMAC;
        TextView deviceIP;
        EditText userName;
        Button btn_display;

        public ViewHolder(View view) {
            super(view);
            deviceMAC = view.findViewById(R.id.deviceMAC);
            deviceIP = view.findViewById(R.id.deviceIP);
            userName = view.findViewById(R.id.userName);
            btn_display =view.findViewById(R.id.btn_display);
        }
    }

    public DeviceAdapter(List<device> deviceList, Context context) {
        mDeviceList = deviceList;
        mContext=context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.device,parent,false);
        final ViewHolder holder =new ViewHolder(view);
        holder.btn_display.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                int position =holder.getAdapterPosition();
                final device device =mDeviceList.get(position);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //TODO: 发送信息
                        try {
                            byte[][] content=new bitmapFont(mContext).toBitmapFont(holder.userName.getText().toString());
                            Log.i(mainActivity.TAG,"向"+device.getIpAddress()+"发送数据");
                            sendUtils.sendBitMapFontBySocket(new Socket(device.getIpAddress(),333),content);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        device device=mDeviceList.get(position);
        holder.deviceMAC.setText(device.getMAC());
        holder.deviceIP.setText(device.getIpAddress());
    }

    @Override
    public int getItemCount() {
        return mDeviceList.size();
    }

}
