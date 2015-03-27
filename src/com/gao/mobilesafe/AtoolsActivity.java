
package com.gao.mobilesafe;

import com.gao.mobilesafe.utils.SmsUtils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class AtoolsActivity extends Activity {
    private ProgressDialog mProgressDialog;

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
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("正在备份");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.show();
        new Thread() {
            public void run() {
                try {
                    SmsUtils.backupSms(AtoolsActivity.this, mProgressDialog);
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            Toast.makeText(AtoolsActivity.this, "备份成功", Toast.LENGTH_LONG).show();
                        }
                    });
                } catch (Exception e) {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            Toast.makeText(AtoolsActivity.this, "备份失败", Toast.LENGTH_LONG).show();
                        }
                    });
                    e.printStackTrace();
                } finally {
                    mProgressDialog.dismiss();
                }
            };
        }.start();
    }

    /**
     * 点击事件，短信的还原
     * @param view
     */
    public void smsRestore(View view) {

    }
}
