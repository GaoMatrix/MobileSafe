
package com.gao.mobilesafe.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.xmlpull.v1.XmlSerializer;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.util.Xml;

public class SmsUtils {

    /**
     * 这里抛出异常，让函数的调用者知道抛出了异常，而不是在内部捕获异常。
     * 
     * @param context
     * @throws IOException
     * @throws IllegalStateException
     * @throws IllegalArgumentException
     */
    public static void backupSms(Context context) throws Exception {
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
        while (cursor.moveToNext()) {// 如果cursor可以移動到下一个
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
        }
        cursor.close();
        serializer.endTag(null, "smss");
        serializer.endDocument();

    }
}
