package com.gao.mobilesafe;

import android.app.Activity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.widget.TextView;

import com.gao.mobilesafe.utils.SystemInfoUtils;

public class TaskManagerActivity extends Activity {
	private TextView tv_process_count;
	private TextView tv_mem_info;

	private int processCount;
	private long availMem;
	private long totalMem;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task_manager);
		tv_mem_info = (TextView) findViewById(R.id.tv_mem_info);
		tv_process_count = (TextView) findViewById(R.id.tv_process_count);
		setTitle();
	}

	private void setTitle() {
        processCount = SystemInfoUtils.getRunningProcessCount(this);
        tv_process_count.setText("运行中的进程：" + processCount + "个");
        availMem = SystemInfoUtils.getAvailMem(this);
        totalMem = SystemInfoUtils.getTotalMem(this);
        tv_mem_info.setText("剩余/总内存："
                + Formatter.formatFileSize(this, availMem) + "/"
                + Formatter.formatFileSize(this, totalMem));
    }
}
