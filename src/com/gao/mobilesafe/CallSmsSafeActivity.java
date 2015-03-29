
package com.gao.mobilesafe;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gao.mobilesafe.db.dao.BlackNumberDao;
import com.gao.mobilesafe.db.domain.BlackNumberInfo;

import java.util.List;

public class CallSmsSafeActivity extends Activity {
    private ListView mCallSmsSafeListView;
    private List<BlackNumberInfo> mInfos;
    private BlackNumberDao mDao;
    private CallSmsSafeAdapter mAdapter;
    private LinearLayout mLoadingLayout;
    private int offset = 0;
    private int maxnumber = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_sms_safe);
        mCallSmsSafeListView = (ListView) findViewById(R.id.lv_callsms_safe);
        mLoadingLayout = (LinearLayout) findViewById(R.id.ll_loading);
        mDao = new BlackNumberDao(this);
        fillData();
        // listview注册一个滚动事件的监听器。
        mCallSmsSafeListView.setOnScrollListener(new OnScrollListener() {
            // 当滚动的状态发生变化的时候。
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                case OnScrollListener.SCROLL_STATE_IDLE:// 空闲状态
                    // 判断当前listview滚动的位置
                    // 获取最后一个可见条目在集合里面的位置。
                    int lastposition = mCallSmsSafeListView.getLastVisiblePosition();

                    // 集合里面有20个item 位置从0开始的 最后一个条目的位置 19
                    if (lastposition == (mInfos.size() - 1)) {
                        System.out.println("列表被移动到了最后一个位置，加载更多的数据。。。");
                        offset += maxnumber;
                        fillData();
                    }

                    break;
                case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:// 手指触摸滚动
                    break;
                case OnScrollListener.SCROLL_STATE_FLING:// 惯性滑行状态
                    break;
                }
            }

            // 滚动的时候调用的方法。
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                    int visibleItemCount, int totalItemCount) {

            }
        });
    }

    private void fillData() {
        mLoadingLayout.setVisibility(View.VISIBLE);
        new Thread(){
            public void run() {
                if (mInfos == null) {
                    mInfos = mDao.findPart(offset, maxnumber);
                } else {
                    mInfos.addAll(mDao.findPart(offset, maxnumber));
                }
                runOnUiThread(new Runnable() {
                    
                    @Override
                    public void run() {
                        mLoadingLayout.setVisibility(View.INVISIBLE);
                        if (mAdapter ==  null) {
                            mAdapter = new CallSmsSafeAdapter();
                            mCallSmsSafeListView.setAdapter(mAdapter);
                        } else {
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                });
            };
        }.start();
    }
    
    private class CallSmsSafeAdapter extends BaseAdapter {

        //有多少条目被显示，这个方法就会被调用多少次。
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view;
            ViewHolder holder;
            // 1.减少内存中view对象的创建个数。
            if (null == convertView) {
                view = View.inflate(CallSmsSafeActivity.this, R.layout.list_item_callsms, null);
                // 2.减少字孩子查询的次数，因为每次查询findViewById都需要遍历整个控件树。
                holder = new ViewHolder();
                holder.tv_number = (TextView) view.findViewById(R.id.tv_black_number);
                holder.tv_mode = (TextView) view.findViewById(R.id.tv_black_mode);
                holder.iv_delete = (ImageView) view.findViewById(R.id.iv_delete);
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
            holder.iv_delete.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new Builder(CallSmsSafeActivity.this);
                    builder.setTitle("警告");
                    builder.setMessage("确定要删除这条记录么？");
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //删除数据库的内容
                            mDao.delete(mInfos.get(position).getNumber());
                            //更新界面。
                            mInfos.remove(position);
                            //通知listview数据适配器更新
                            mAdapter.notifyDataSetChanged();
                        }
                    });
                    builder.setNegativeButton("取消", null);
                    builder.show();
                }
            });
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
        ImageView iv_delete;
    }

    private EditText et_blacknumber;
    private CheckBox cb_phone;
    private CheckBox cb_sms;
    private Button bt_ok;
    private Button bt_cancel;

    public void addBlackNumber(View view){
        AlertDialog.Builder builder = new Builder(this);
        final AlertDialog dialog = builder.create();
        View contentView = View.inflate(this, R.layout.dialog_add_blacknumber, null);
        et_blacknumber = (EditText) contentView.findViewById(R.id.et_blacknumber);
        cb_phone = (CheckBox) contentView.findViewById(R.id.cb_phone);
        cb_sms = (CheckBox) contentView.findViewById(R.id.cb_sms);
        bt_cancel = (Button) contentView.findViewById(R.id.cancel);
        bt_ok = (Button) contentView.findViewById(R.id.ok);
        dialog.setView(contentView, 0, 0, 0, 0);
        dialog.show();
        bt_cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        bt_ok.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String blacknumber = et_blacknumber.getText().toString().trim();
                if(TextUtils.isEmpty(blacknumber)){
                    Toast.makeText(getApplicationContext(), "黑名单号码不能为空", 0).show();
                    return;
                }
                String mode ;
                if(cb_phone.isChecked()&&cb_sms.isChecked()){
                    //全部拦截
                    mode = "3";
                }else if(cb_phone.isChecked()){
                    //电话拦截
                    mode = "1";
                }else if(cb_sms.isChecked()){
                    //短信拦截
                    mode = "2";
                }else{
                    Toast.makeText(getApplicationContext(), "请选择拦截模式", 0).show();
                    return;
                }
                //数据被加到数据库
                mDao.add(blacknumber, mode);
                //更新listview集合里面的内容。
                BlackNumberInfo info = new BlackNumberInfo();
                info.setMode(mode);
                info.setNumber(blacknumber);
                mInfos.add(0, info);
                //通知listview数据适配器数据更新了。
                mAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });

    }
}
