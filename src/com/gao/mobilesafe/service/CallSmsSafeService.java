
package com.gao.mobilesafe.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.telephony.gsm.SmsMessage;
import android.util.Log;

import com.gao.mobilesafe.db.dao.BlackNumberDao;

public class CallSmsSafeService extends Service {
    public static final String TAG = CallSmsSafeService.class.getSimpleName();
    private InnerSmsReceiver mReceiver;
    private BlackNumberDao mDao;

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    private class InnerSmsReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // 短信到来了
            Log.d(TAG, "内部广播接收者，短信到来");
            //检查发件人是否是黑名单号码，设置短信拦截或全部拦截
            Object[] objs = (Object[]) intent.getExtras().get("pdus");
            for (Object object : objs) {
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[])object);
                //得到短信发件人
                String sender = smsMessage.getOriginatingAddress();
                String result = mDao.findMode(sender);
                if ("2".equals(result) || "3".equals(result)) {
                    Log.d(TAG, "拦截短信");
                    abortBroadcast();
                }
                String body = smsMessage.getMessageBody();
                //模拟智能短信，实际是应该从数据库中匹配关键字
                if (body.contains("fapiao")) {
                    //你的头发票亮的很  包含“发票” 需要语言分词技术 Lucene
                    abortBroadcast();
                }
            }
        }
    }

    @Override
    public void onCreate() {
        mDao = new BlackNumberDao(this);
        mReceiver = new InnerSmsReceiver();
        IntentFilter filter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        registerReceiver(mReceiver, filter);
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(mReceiver);
        mReceiver = null;
        super.onDestroy();
    }
}
