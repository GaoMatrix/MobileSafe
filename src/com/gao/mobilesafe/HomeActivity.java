
package com.gao.mobilesafe;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gao.mobilesafe.utils.MD5Utils;

public class HomeActivity extends Activity {
    protected static final String TAG = "HomeActivity";
    private GridView mGridView;
    private GrideAdapter mAdapter;
    private SharedPreferences mSharedPreferences;
    
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
        mSharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
        mAdapter = new GrideAdapter();
        mGridView.setAdapter(mAdapter);
        mGridView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 8:// Setting
                        Intent intent = new Intent(HomeActivity.this, SettingActivity.class);
                        startActivity(intent);
                        break;
                    case 0:// 手机防盗
                        showLostFindDialog();
                        break;
                    case 7:// 进入高级工具
                        Intent intentTools = new Intent(HomeActivity.this, AtoolsActivity.class);
                        startActivity(intentTools);
                        break;

                    default:
                        break;
                }
            }
            
        });
    }

    private void showLostFindDialog() {
        if(isSetupPwd()){
            showEnterDialog();
        }else{
            showSetupPwdDialog();
        }
    }

    private EditText et_setup_pwd;
    private EditText et_setup_confirm;
    private Button ok;
    private Button cancel;
    private AlertDialog dialog;
    
    /**
     * 设置密码对话框
     */
    private void showSetupPwdDialog() {
        AlertDialog.Builder builder = new Builder(HomeActivity.this);
        // 自定义一个布局文件
        View view = View.inflate(HomeActivity.this, R.layout.dialog_setup_password, null);
        et_setup_pwd = (EditText) view.findViewById(R.id.et_setup_pwd);
        et_setup_confirm = (EditText) view.findViewById(R.id.et_setup_confirm);
        ok = (Button) view.findViewById(R.id.ok);
        cancel = (Button) view.findViewById(R.id.cancel);
        cancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // 把这个对话框取消掉
                dialog.dismiss();
            }
        });
        ok.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // 取出密码
                String password = et_setup_pwd.getText().toString().trim();
                String password_confirm = et_setup_confirm.getText().toString().trim();
                if (TextUtils.isEmpty(password) || TextUtils.isEmpty(password_confirm)) {
                    Toast.makeText(HomeActivity.this, "密码为空", 0).show();
                    return;
                }
                // 判断是否一致才去保存
                if (password.equals(password_confirm)) {
                    // 一致的话，就保存密码，把对话框消掉，还要进入手机防盗页面
                    Editor editor = mSharedPreferences.edit();
                    editor.putString("password", MD5Utils.md5Password(password));// 保存加密后的
                    editor.commit();
                    dialog.dismiss();
                    Log.i(TAG, "一致的话，就保存密码，把对话框消掉，还要进入手机防盗页面");
                    Intent intent = new Intent(HomeActivity.this, LostFindActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(HomeActivity.this, "密码不一致", 0).show();
                    return;
                }
            }
        });
        dialog = builder.create();
        //dialog.setView(view);
        //设置view在dialog的边距为0，因为Android不同版本的间距显示不一样，都是0就统一了。
        dialog.setView(view, 0, 0, 0, 0);
        dialog.show();

    }

    /**
     * 输入密码对话框
     */
    private void showEnterDialog() {

        AlertDialog.Builder builder = new Builder(HomeActivity.this);
        // 自定义一个布局文件
        View view = View.inflate(HomeActivity.this, R.layout.dialog_enter_password, null);
        et_setup_pwd = (EditText) view.findViewById(R.id.et_setup_pwd);
        ok = (Button) view.findViewById(R.id.ok);
        cancel = (Button) view.findViewById(R.id.cancel);
        cancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // 把这个对话框取消掉
                dialog.dismiss();
            }
        });
        ok.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // 取出密码
                String password = et_setup_pwd.getText().toString().trim();
                String savePassword = mSharedPreferences.getString("password", "");// 取出加密后的
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(HomeActivity.this, "密码为空", 1).show();
                    return;
                }

                if (MD5Utils.md5Password(password).equals(savePassword)) {
                    // 输入的密码是我之前设置的密码
                    // 把对话框消掉，进入主页面；
                    dialog.dismiss();
                    Log.i(TAG, "把对话框消掉，进入手机防盗页面");
                    Intent intent = new Intent(HomeActivity.this, LostFindActivity.class);
                    startActivity(intent);

                } else {
                    Toast.makeText(HomeActivity.this, "密码错误", 1).show();
                    et_setup_pwd.setText("");
                    return;
                }
            }
        });
        dialog = builder.create();
        dialog.setView(view, 0, 0, 0, 0);
        dialog.show();

    }


    private boolean isSetupPwd() {
        String password = mSharedPreferences.getString("password", null);
//      if(TextUtils.isEmpty(password)){
//          return false;
//      }else{
//          return true;
//      }
        return !TextUtils.isEmpty(password);
        
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
