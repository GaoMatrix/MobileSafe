package com.gao.mobilesafe;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gao.mobilesafe.db.domain.AppInfo;
import com.gao.mobilesafe.engine.AppInfoProvider;

import java.util.ArrayList;
import java.util.List;

public class AppManagerActivity extends Activity{
    private static final String TAG = "AppManagerActivity";
    private TextView tv_avail_rom;
    private TextView tv_avail_sd;

    private ListView lv_app_manager;
    private LinearLayout ll_loading;

    /**
     * 所有的应用程序包信息
     */
    private List<AppInfo> appInfos;

    /**
     * 用户应用程序的集合
     */
    private List<AppInfo> userAppInfos;

    /**
     * 系统应用程序的集合
     */
    private List<AppInfo> systemAppInfos;

    /**
     * 当前程序信息的状态。
     */
    private TextView tv_status;


    private AppManagerAdapter adapter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_manager);
        tv_status = (TextView) findViewById(R.id.tv_status);
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

        lv_app_manager = (ListView) findViewById(R.id.lv_app_manager);
        ll_loading = (LinearLayout) findViewById(R.id.ll_loading);

        fillData();
        
     // 给listview注册一个滚动的监听器
        lv_app_manager.setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            // 滚动的时候调用的方法。
            // firstVisibleItem 第一个可见条目在listview集合里面的位置。
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                    int visibleItemCount, int totalItemCount) {
                if (userAppInfos != null && systemAppInfos != null) {
                    if (firstVisibleItem > userAppInfos.size()) {
                        tv_status.setText("系统程序：" + systemAppInfos.size() + "个");
                    } else {
                        tv_status.setText("用户程序：" + userAppInfos.size() + "个");
                    }
                }
            }
        });
    }

    private void fillData() {
        ll_loading.setVisibility(View.VISIBLE);
        new Thread() {
            public void run() {
                appInfos = AppInfoProvider.getAppInfos(AppManagerActivity.this);
                userAppInfos = new ArrayList<AppInfo>();
                systemAppInfos = new ArrayList<AppInfo>();
                for (AppInfo info : appInfos) {
                    if (info.isUserApp()) {
                        userAppInfos.add(info);
                    } else {
                        systemAppInfos.add(info);
                    }
                }
                // 加载listview的数据适配器
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (adapter == null) {
                            adapter = new AppManagerAdapter();
                            lv_app_manager.setAdapter(adapter);
                        } else {
                            adapter.notifyDataSetChanged();
                        }
                        ll_loading.setVisibility(View.INVISIBLE);
                    }
                });
            };
        }.start();
    }

    private class AppManagerAdapter extends BaseAdapter {

        // 控制listview有多少个条目
        @Override
        public int getCount() {
            // return appInfos.size();
            return userAppInfos.size() + 1 + systemAppInfos.size() + 1;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            AppInfo appInfo;
            if (position == 0) {// 显示的是用程序有多少个的小标签
                TextView tv = new TextView(getApplicationContext());
                tv.setTextColor(Color.WHITE);
                tv.setBackgroundColor(Color.GRAY);
                tv.setText("用户程序：" + userAppInfos.size() + "个");
                return tv;
            } else if (position == (userAppInfos.size() + 1)) {
                TextView tv = new TextView(getApplicationContext());
                tv.setTextColor(Color.WHITE);
                tv.setBackgroundColor(Color.GRAY);
                tv.setText("系统程序：" + systemAppInfos.size() + "个");
                return tv;
            } else if (position <= userAppInfos.size()) {// 用户程序
                int newposition = position - 1;// 因为多了一个textview的文本占用了位置
                appInfo = userAppInfos.get(newposition);
            } else {// 系统程序
                int newposition = position - 1 - userAppInfos.size() - 1;
                appInfo = systemAppInfos.get(newposition);
            }
            View view;
            ViewHolder holder;

            // if(position<userAppInfos.size()){//这些位置是留个用户程序显示的。
            // appInfo = userAppInfos.get(position);
            // }else{//这些位置是留个系统程序的。
            // int newposition = position - userAppInfos.size();
            // appInfo = systemAppInfos.get(newposition);
            // }
            if (convertView != null && convertView instanceof RelativeLayout) {
                // 不仅需要检查是否为空，还要判断是否是合适的类型去复用，因为TextView不能去复用
                view = convertView;
                holder = (ViewHolder) view.getTag();
            } else {
                view = View.inflate(getApplicationContext(),
                        R.layout.list_item_appinfo, null);
                holder = new ViewHolder();
                holder.iv_icon = (ImageView) view
                        .findViewById(R.id.iv_app_icon);
                holder.tv_location = (TextView) view
                        .findViewById(R.id.tv_app_location);
                holder.tv_name = (TextView) view.findViewById(R.id.tv_app_name);
                view.setTag(holder);
            }
            holder.iv_icon.setImageDrawable(appInfo.getIcon());
            holder.tv_name.setText(appInfo.getName());
            if (appInfo.isInRom()) {
                holder.tv_location.setText("手机内存"+"uid:"+appInfo.getUid());
            } else {
                holder.tv_location.setText("外部存储"+"uid:"+appInfo.getUid());
            }
            return view;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

    }

    static class ViewHolder {
        TextView tv_name;
        TextView tv_location;
        ImageView iv_icon;
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
