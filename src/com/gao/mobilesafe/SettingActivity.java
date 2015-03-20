
package com.gao.mobilesafe;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.gao.mobilesafe.service.AddressService;
import com.gao.mobilesafe.service.CallSmsSafeService;
import com.gao.mobilesafe.ui.SettingClickView;
import com.gao.mobilesafe.ui.SettingItemView;
import com.gao.mobilesafe.utils.ServiceUtils;

public class SettingActivity extends Activity {
    // 设置是否开启自动更新
    private SettingItemView mSettingUpdate;
    private SharedPreferences mSharedPreferences;
    // 设置是否开启显示归属地
    private SettingItemView mSettingAddress;
    // 设置归属地显示背景
    private SettingClickView mSettingChangeBack;
    //黑名单拦截的设置
    private SettingItemView mSettingCallSmsSafe;
    private Intent mCallSmsSafeIntent;

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

        //黑名单拦截的设置
        mSettingCallSmsSafe = (SettingItemView) findViewById(R.id.siv_callsms_safe);
        mCallSmsSafeIntent = new Intent(this, CallSmsSafeService.class);
        mSettingCallSmsSafe.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SettingActivity.this, AddressService.class);
                if (mSettingCallSmsSafe.isChecked()) {
                    mSettingCallSmsSafe.setChecked(false);
                    stopService(mCallSmsSafeIntent);
                } else {
                    mSettingCallSmsSafe.setChecked(true);
                    startService(mCallSmsSafeIntent);
                }
            }
        });


        mSettingChangeBack = (SettingClickView) findViewById(R.id.scv_changebg);
        mSettingChangeBack.setTitle("归属地的提示框风格");
        final String[] items = {"半透明", "活力橙", "卫士蓝", "金属灰", "苹果绿"};
        int which = mSharedPreferences.getInt("which", 0);
        mSettingChangeBack.setDesc(items[which]);
        mSettingChangeBack.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                int select = mSharedPreferences.getInt("which", 0);
                AlertDialog.Builder builder = new Builder(SettingActivity.this);
                builder.setTitle("归属地的提示框风格");
                builder.setSingleChoiceItems(items, select, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Editor editor = mSharedPreferences.edit();
                        editor.putInt("which", which);
                        editor.commit();
                        mSettingChangeBack.setDesc(items[which]);

                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("cancel", null);
                builder.show();
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

        boolean isCallSmsServiceRunning = ServiceUtils.isServiceRunning(SettingActivity.this,
                "com.gao.mobilesafe.service.CallSmsSafeService");
        mSettingCallSmsSafe.setChecked(isCallSmsServiceRunning);
    }
}
