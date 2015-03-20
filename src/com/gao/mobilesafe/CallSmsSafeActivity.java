
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

        //有多少条目被显示，这个方法就会被调用多少次。
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            ViewHolder holder;
            // 1.减少内存中view对象的创建个数。
            if (null == convertView) {
                view = View.inflate(CallSmsSafeActivity.this, R.layout.list_item_callsms, null);
                // 2.减少字孩子查询的次数，因为每次查询findViewById都需要遍历整个控件树。
                holder = new ViewHolder();
                holder.tv_number = (TextView) view.findViewById(R.id.tv_black_number);
                holder.tv_mode = (TextView) view.findViewById(R.id.tv_black_mode);
                // 当孩子生出来的时候找到他们的引用，存放到记事本，放到父亲的口袋。
                view.setTag(holder);
            } else {//复用listview缓存的对象
                view = convertView;
                holder = (ViewHolder) view.getTag();
            }
            holder.tv_number.setText(mInfos.get(position).getNumber());
            String mode = mInfos.get(position).getMode();
            if ("1".equals(mode)) {
                holder.tv_mode.setText("电话拦截");
            } else if ("2".equals(mode)) {
                holder.tv_mode.setText("短信拦截");
            } else {
                holder.tv_mode.setText("全部拦截");
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

    /**
     * view对象的容器，记录孩子的地址，相当于一个记事本
     * @author gaochengquan
     *
     */
    static class ViewHolder {
        TextView tv_number;
        TextView tv_mode;
    }
}
