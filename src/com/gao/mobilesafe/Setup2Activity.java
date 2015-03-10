
package com.gao.mobilesafe;

import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;

import com.gao.mobilesafe.ui.SettingItemView;

public class Setup2Activity extends BaseSetupActivity {
    private SettingItemView mSettingItemView;
    /**
     * 读取手机sim的信息
     */
    private TelephonyManager mTelephonyManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup2);
        mSettingItemView = (SettingItemView) findViewById(R.id.siv_setup2_sim);
        mTelephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        String sim = mSharedPreferences.getString("sim", null);
        if (TextUtils.isEmpty(sim)) {
            // 没有绑定
            mSettingItemView.setChecked(false);
        } else {
            // 已经绑定
            mSettingItemView.setChecked(true);
        }

        mSettingItemView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Editor editor = mSharedPreferences.edit();
                if (mSettingItemView.isChecked()) {
                    mSettingItemView.setChecked(false);
                    // 保存sim卡的序列号
                    editor.putString("sim", null);
                } else {
                    mSettingItemView.setChecked(true);
                    // 保存sim卡的序列号
                    String sim = mTelephonyManager.getSimSerialNumber();
                    editor.putString("sim", sim);

                }
                editor.commit();

            }
        });
    }

    @Override
    protected void showNext() {
        Intent intent = new Intent(this, Setup3Activity.class);
        startActivity(intent);
        finish();
        // 要求在finish()或者startActivity(intent);后面执行
        overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
    }

    @Override
    protected void showPre() {
        Intent intent = new Intent(this, Setup1Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.tran_pre_in, R.anim.tran_pre_out);
    }
}
