
package com.gao.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

public class BootCompleteReceiver extends BroadcastReceiver {

    private SharedPreferences mSharedPreferences;
    private TelephonyManager mTelephonyManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("BootCompleteReceiver", "BootCompleteReceiver");
        mSharedPreferences = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        mTelephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        // 读取之前保存的SiM信息；
        String saveSim = mSharedPreferences.getString("sim", "") + "afu";
        // 读取当前的sim卡信息
        String realSim = mTelephonyManager.getSimSerialNumber();

        // 比较是否一样
        if (saveSim.equals(realSim)) {
            // sim没有变更，还是同一个哥们
        } else {
            // sim 已经变更 发一个短信给安全号码
            System.out.println("sim 已经变更");
            Toast.makeText(context, "sim 已经变更", 1).show();
            SmsManager.getDefault().sendTextMessage(mSharedPreferences.getString("safenumber", ""),
                    null,
                    "sim changing....", null, null);
        }
    }
}
