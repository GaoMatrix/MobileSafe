
package com.gao.mobilesafe;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.gao.mobilesafe.db.dao.BlackNumberDao;
import com.gao.mobilesafe.db.domain.BlackNumberInfo;

public class CallSmsSafeActivity extends Activity {
    private ListView mCallSmsSafeListView;
    private List<BlackNumberInfo> mInfos;
    private BlackNumberDao mDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_sms_safe);
        mCallSmsSafeListView = (ListView) findViewById(R.id.lv_callsms_safe);
        mDao = new BlackNumberDao(this);
        mInfos = mDao.findAll();
        mCallSmsSafeListView.setAdapter(new CallSmsSafeAdapter());
    }
    
    private class CallSmsSafeAdapter extends BaseAdapter {

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(CallSmsSafeActivity.this, R.layout.list_item_callsms, null);
            TextView tv_black_number = (TextView) view.findViewById(R.id.tv_black_number);
            TextView tv_black_mode = (TextView) view.findViewById(R.id.tv_black_mode);
            tv_black_number.setText(mInfos.get(position).getNumber());
            String mode = mInfos.get(position).getMode();
            if ("1".equals(mode)) {
                tv_black_mode.setText("电话拦截");
            } else if ("2".equals(mode)) {
                tv_black_mode.setText("短信拦截");
            } else {
                tv_black_mode.setText("全部拦截");
            }
            return view;
        }

        @Override
        public int getCount() {
            return mInfos.size();
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
