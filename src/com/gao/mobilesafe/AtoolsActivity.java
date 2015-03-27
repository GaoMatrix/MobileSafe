
package com.gao.mobilesafe;

import com.gao.mobilesafe.utils.SmsUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class AtoolsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_atools);
    }

    /**
     * 点击事件，进入号码归属地查询的页面
     * @param view
     */
    public void numberQuery(View view) {
        Intent intent = new Intent(this, NumberAddressQueryActivity.class);
        startActivity(intent);
    }

    /**
     * 点击事件，短信的备份
     * @param view
     */
    public void smsBackup(View view) {
        try {
            SmsUtils.backupSms(this);
            Toast.makeText(this, "备份成功", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(this, "备份失败", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    /**
     * 点击事件，短信的还原
     * @param view
     */
    public void smsRestore(View view) {

    }
}
