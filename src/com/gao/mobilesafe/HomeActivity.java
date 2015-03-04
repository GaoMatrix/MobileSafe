
package com.gao.mobilesafe;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class HomeActivity extends Activity {
    private GridView mGridView;
    private GrideAdapter mAdapter;
    private String[] mGridNames = {
            "手机防盗", "通讯卫士", "软件管理", "进程管理", "流量统计", "手机杀毒", "缓存清理", "高级工具", "设置中心"
    };

    private static int[] mGrideImages = {
            R.drawable.safe, R.drawable.callmsgsafe, R.drawable.app, R.drawable.taskmanager,
            R.drawable.netmanager, R.drawable.trojan, R.drawable.sysoptimize, R.drawable.atools,
            R.drawable.settings

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mGridView = (GridView) findViewById(R.id.gv_home);
        mAdapter = new GrideAdapter();
        mGridView.setAdapter(mAdapter);
    }

    private class GrideAdapter extends BaseAdapter {

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(HomeActivity.this, R.layout.gride_item_home, null);
            ImageView imageView = (ImageView) view.findViewById(R.id.iv_image);
            TextView nameText = (TextView) view.findViewById(R.id.tv_name);
            nameText.setText(mGridNames[position]);
            imageView.setImageResource(mGrideImages[position]);
            return view;
        }

        @Override
        public int getCount() {
            return mGridNames.length;
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return 0;
        }

    }
}
