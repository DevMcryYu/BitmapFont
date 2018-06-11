package com.devmcryyu.bitmapfont;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by 92075 on 2018/1/22.
 */

public class mainActivity extends AppCompatActivity {
    final static String TAG = "smartTAG";
    private DrawerLayout mDrawerLayout;
    public Context mContext;
    RecyclerView recyclerView;
    NavigationView navigationView;
    DeviceAdapter adapter;
    IPUtils mIPUtils;
    public bitmapFont bitmapFont;
    List<device> devices;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.bitmapfont_layout);
        bitmapFont = new bitmapFont(this);
        devices = new ArrayList<>();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.menu);
        }
        navigationView=findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);                                                   //修改默认NavigationView图标颜色
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                mDrawerLayout.closeDrawers();
                switch (item.getItemId()){
                    case R.id.sendAll:
                        Toast.makeText(mContext,"你点击了"+item.getTitle(),Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.about:
                        Toast.makeText(mContext,"你点击了"+item.getTitle(),Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });
        mIPUtils = new IPUtils();
        mIPUtils.setOnScanListener(new IPUtils.OnScanListener() {
            @Override
            public void scan(Map<String, String> resultMap) {
                Log.i(TAG, "初始化设备");
                initDevices(resultMap);
                Log.i(TAG, "初始化设备完成");
                Log.i(TAG, "scan更新数据");
                adapter.notifyDataSetChanged();
            }
        });
        recyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        Log.i(TAG, "开始扫描");
        mIPUtils.startScan();
        if (devices != null) {
            adapter = new DeviceAdapter(devices, mContext);
            recyclerView.setAdapter(adapter);
            recyclerView.addItemDecoration(new SpacesItemDecoration(30));
        }
    }

    private void refresh() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mIPUtils.startScan();
                Log.i(TAG, "refresh更新数据");
            }
        }).start();
        adapter.notifyDataSetChanged();
    }

    private void initDevices(Map<String, String> resultMap) {
        Iterator<Map.Entry<String, String>> entries = resultMap.entrySet().iterator();
        devices.clear();
        while (entries.hasNext()) {
            Map.Entry<String, String> entry = entries.next();
            device device = new device(entry.getValue(), entry.getKey(), com.devmcryyu.bitmapfont.device.ONLINE);
            Log.i(TAG, device.toString());
            //TODO: DEBUG
            devices.add(device);
        }
        Log.i(TAG, "初始化" + devices.size() + "个设备");
    }

    @Override
    protected void onResume() {
        super.onResume();
//        mIPUtils.startScan();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
//                Toast.makeText(this,"打开菜单",Toast.LENGTH_SHORT).show();
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.refresh:
//                Toast.makeText(this, "刷新列表", Toast.LENGTH_SHORT).show();
                refresh();

        }
        return true;
    }

}
