
package com.gao.mobilesafe.utils;

import java.io.File;
import java.io.FileOutputStream;

import org.xmlpull.v1.XmlSerializer;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.util.Xml;
import android.widget.ProgressBar;

public class SmsUtils {
    /**
     * 备份短信的回调接口
     * 把经常变的逻辑抽取出来，做成接口
     */
    public interface BackUpCallBack {
        /**
         * 开始备份的时候，设置进度的最大值
         * @param max 总进度
         */
        public void beforeBackup(int max);

        /**
         * 备份过程中，增加进度
         * @param progress
         */
        public void onSmsBackup(int progress);
    }

    /**
     * 这里抛出异常，让函数的调用者知道抛出了异常，而不是在内部捕获异常。
     * @param context
     * @param progressDialog 这里客户需要一个进度条对话框，就添加一个进度条对话框，代码耦合，如果需要ProgressBar那就要添加
     * ProgressBar代码高度耦合。
     * @throws Exception
     */
    public static void backupSms(Context context, BackUpCallBack callback) throws Exception {
        ContentResolver resolver = context.getContentResolver();
        File file = new File(Environment.getExternalStorageDirectory(), "backup.xml");
        FileOutputStream fos = new FileOutputStream(file);
        // 把用户的短信一条一条读出来，按照一定的格式写到文件里。 xml保存文件，跨平台。
        // 把用户的短信一条一条读出来，按照一定的格式写到文件里
        XmlSerializer serializer = Xml.newSerializer(); // 获取xml文件的生成器(序列化器)
        // 初始化生成器
        serializer.setOutput(fos, "utf-8");
        serializer.startDocument("utf-8", true);
        serializer.startTag(null, "smss");
        Uri uri = Uri.parse("content://sms/");
        Cursor cursor = resolver.query(uri, new String[] {
                "body", "address", "type", "date"
        }, null, null, null);
        // 开始备份的时候，设置进度条的最大值
        int max = cursor.getCount();
        //progressDialog.setMax(max);
        callback.beforeBackup(max);
        int progress = 0;
        while (cursor.moveToNext()) {// 如果cursor可以移動到下一个
            Thread.sleep(500);
            String body = cursor.getString(0);
            String address = cursor.getString(1);
            String type = cursor.getString(2);
            String date = cursor.getString(3);

            serializer.startTag(null, "sms");

            serializer.startTag(null, "body");
            serializer.text(body);
            serializer.endTag(null, "body");
            serializer.startTag(null, "address");
            serializer.text(address);
            serializer.endTag(null, "address");
            serializer.startTag(null, "type");
            serializer.text(type);
            serializer.endTag(null, "type");
            serializer.startTag(null, "date");
            serializer.text(date);
            serializer.endTag(null, "date");

            serializer.endTag(null, "sms");
            //备份过程中，增加进度
            progress++;
            //progressDialog.setProgress(progress);
            callback.onSmsBackup(progress);
        }
        cursor.close();
        serializer.endTag(null, "smss");
        serializer.endDocument();

    }
    
    /**
     * 还原短信
     * @param context
     * @param flag 是否清理原来的短信
     */
    public static void restoreSms(Context context, boolean flag) {
        Uri uri = Uri.parse("content://sms/");
        if (flag) {
            context.getContentResolver().delete(uri, null, null);
        }
     // 1.读取sd卡上的xml文件
        // Xml.newPullParser();

        // 2.读取max

        // 3.读取每一条短信信息，body date type address

        // 4.把短信插入到系统短息应用。
        ContentValues values = new ContentValues();
        values.put("body", "woshi duanxin de neirong");
        values.put("date", "1395045035573");
        values.put("type", "1");
        values.put("address", "5558");
        context.getContentResolver().insert(uri, values);
    }
}
