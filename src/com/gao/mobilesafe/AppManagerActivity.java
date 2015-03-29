
package com.gao.mobilesafe;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.text.format.Formatter;
import android.widget.TextView;

public class AppManagerActivity extends Activity {
    private static final String TAG = "AppManagerActivity";
    private TextView tv_avail_rom;
    private TextView tv_avail_sd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_manager);
        tv_avail_rom = (TextView) findViewById(R.id.tv_avail_rom);
        tv_avail_sd = (TextView) findViewById(R.id.tv_avail_sd);
        long sdsize = getAvailSpace(Environment.getExternalStorageDirectory()
                .getAbsolutePath());
        long romsize = getAvailSpace(Environment.getDataDirectory()
                .getAbsolutePath());
        tv_avail_sd
                .setText("SD卡可用空间：" + Formatter.formatFileSize(this, sdsize));
        tv_avail_rom.setText("内存可用空间："
                + Formatter.formatFileSize(this, romsize));
    }

    /**
     * 获取某个目录的可用空间
     * 
     * @param path
     * @return
     */
    private long getAvailSpace(String path) {
        StatFs statf = new StatFs(path);
        statf.getBlockCount();// 获取分区的个数
        long size = statf.getBlockSize();// 获取分区的大小
        long count = statf.getAvailableBlocks();// 获取可用的区块的个数
        return size * count;
    }
}
