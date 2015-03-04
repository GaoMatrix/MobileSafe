package com.gao.mobilesafe;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.gao.mobilesafe.ui.SettingItemView;

public class SettingActivity extends Activity{
    private SettingItemView mSettingItemView;
    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        mSharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
        mSettingItemView = (SettingItemView) findViewById(R.id.siv_update);
        
        boolean update = mSharedPreferences.getBoolean("update", false);
        if (update) {
            mSettingItemView.setChecked(true);
            mSettingItemView.setDesc("自动升级已经开启");
        } else {
            mSettingItemView.setChecked(false);
            mSettingItemView.setDesc("自动升级已经关闭");
        }
        
        mSettingItemView.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                Editor editor = mSharedPreferences.edit();
                if (mSettingItemView.isChecked()) {
                    mSettingItemView.setChecked(false);
                    mSettingItemView.setDesc("自动升级已经关闭");
                    editor.putBoolean("update", false);
                } else {
                    mSettingItemView.setChecked(true);
                    mSettingItemView.setDesc("自动升级已经开启");
                    editor.putBoolean("update", true);
                }
                editor.commit();
            }
        });
    }
}
