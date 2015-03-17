
package com.gao.mobilesafe;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Address;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.gao.mobilesafe.service.AddressService;
import com.gao.mobilesafe.ui.SettingItemView;
import com.gao.mobilesafe.utils.ServiceUtils;

public class SettingActivity extends Activity {
    // 设置是否开启自动更新
    private SettingItemView mSettingUpdate;
    private SharedPreferences mSharedPreferences;
    // 设置是否开启显示归属地
    private SettingItemView mSettingAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        mSharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
        mSettingUpdate = (SettingItemView) findViewById(R.id.siv_update);
        mSettingAddress = (SettingItemView) findViewById(R.id.siv_show_address);

        boolean update = mSharedPreferences.getBoolean("update", false);
        if (update) {
            mSettingUpdate.setChecked(true);
        } else {
            mSettingUpdate.setChecked(false);
        }

        mSettingUpdate.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Editor editor = mSharedPreferences.edit();
                if (mSettingUpdate.isChecked()) {
                    mSettingUpdate.setChecked(false);
                    editor.putBoolean("update", false);
                } else {
                    mSettingUpdate.setChecked(true);
                    editor.putBoolean("update", true);
                }
                editor.commit();
            }
        });
        boolean isServiceRunning = ServiceUtils.isServiceRunning(SettingActivity.this,
                "com.gao.mobilesafe.service.AddressService");
        if (isServiceRunning) {
            // 监听来电服务是开启的
            mSettingAddress.setChecked(true);
        } else {
            mSettingAddress.setChecked(false);
        }
        mSettingAddress = (SettingItemView) findViewById(R.id.siv_show_address);
        mSettingAddress.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SettingActivity.this, AddressService.class);
                if (mSettingAddress.isChecked()) {
                    mSettingAddress.setChecked(false);
                    stopService(intent);
                } else {
                    mSettingAddress.setChecked(true);
                    startService(intent);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        boolean isServiceRunning = ServiceUtils.isServiceRunning(SettingActivity.this,
                "com.gao.mobilesafe.service.AddressService");
        if (isServiceRunning) {
            // 监听来电服务是开启的
            mSettingAddress.setChecked(true);
        } else {
            mSettingAddress.setChecked(false);
        }
    }
}
