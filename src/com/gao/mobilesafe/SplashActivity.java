
package com.gao.mobilesafe;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;
import android.widget.Toast;

import com.gao.mobilesafe.utils.StreamTools;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class SplashActivity extends Activity {
    protected static final String TAG = SplashActivity.class.getSimpleName();
    protected static final int MSG_ENTER_HOME = 0;
    protected static final int MSG_SHOW_UPDATE_DIALOG = 1;
    protected static final int MSG__URL_ERROR = 2;
    protected static final int MSG__NETWORK_ERROR = 3;
    protected static final int MSG__JSON_ERROR = 4;

    private TextView mVersionTextView;
    private TextView mUpdateInfoTextView;
    protected String mDescription;
    protected String mApkurl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mVersionTextView = (TextView) findViewById(R.id.version);
        mUpdateInfoTextView = (TextView) findViewById(R.id.tv_update_info);
        mVersionTextView.setText("版本号" + getVersionName());

        // check update
        checkUdate();
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.2f, 1.0f);
        alphaAnimation.setDuration(500);
        findViewById(R.id.rl_splash).startAnimation(alphaAnimation);

    }

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case MSG_SHOW_UPDATE_DIALOG:
                    Log.i(TAG, "Show update dialog!");
                    showUpdateDialog();
                    break;
                case MSG_ENTER_HOME:
                    enterHome();
                    break;
                case MSG__URL_ERROR:
                    enterHome();
                    Toast.makeText(SplashActivity.this, "URL Error", Toast.LENGTH_LONG).show();
                    break;
                case MSG__NETWORK_ERROR:
                    enterHome();
                    Toast.makeText(SplashActivity.this, "Network Error", Toast.LENGTH_LONG).show();
                    break;
                case MSG__JSON_ERROR:
                    enterHome();
                    Toast.makeText(SplashActivity.this, "Json Error", Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }
        };
    };

    /**
     * 检查是否有新版本，如果有就升级
     */
    private void checkUdate() {
        new Thread() {
            public void run() {
                // URL http://192.168.1.36:8080/updateinfo.html
                Message message = Message.obtain();
                long startTime = System.currentTimeMillis();
                try {
                    URL url = new URL(getString(R.string.serverurl));
                    // 联网
                    HttpURLConnection httpsURLConnection = (HttpURLConnection) url.openConnection();
                    httpsURLConnection.setRequestMethod("GET");
                    httpsURLConnection.setConnectTimeout(4000);
                    int code = httpsURLConnection.getResponseCode();
                    if (code == 200) {
                        // 联网成功
                        InputStream inputStream = httpsURLConnection.getInputStream();
                        // 把流转成String
                        String result = StreamTools.readFromStream(inputStream);
                        Log.i(TAG, "联网成功了" + result);
                        // json解析
                        JSONObject obj = new JSONObject(result);
                        // 得到服务器的版本信息
                        String version = (String) obj.get("version");
                        mDescription = (String) obj.get("description");
                        mApkurl = (String) obj.get("apkurl");
                        // 校验是否有新版本
                        if (getVersionName().equals(version)) {
                            // 版本一致，没有新版本，进入主页面
                            message.what = MSG_ENTER_HOME;
                        } else {
                            // 有新版本，弹出一升级对话框
                            message.what = MSG_SHOW_UPDATE_DIALOG;
                        }
                    }
                } catch (MalformedURLException e) {
                    message.what = MSG__URL_ERROR;
                    e.printStackTrace();
                } catch (IOException e) {
                    message.what = MSG__NETWORK_ERROR;
                    e.printStackTrace();
                } catch (JSONException e) {
                    message.what = MSG__JSON_ERROR;
                    e.printStackTrace();
                } finally {
                    long endTime = System.currentTimeMillis();
                    long useTime = endTime - startTime;
                    if (useTime < 2000) {
                        try {
                            Thread.sleep(2000 - useTime);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                    mHandler.sendMessage(message);
                }
            };
        }.start();
    }

    protected void showUpdateDialog() {
        AlertDialog.Builder builder = new Builder(this);
        builder.setTitle("提示升级");
//        builder.setCancelable(false);//强制升级
        builder.setOnCancelListener(new OnCancelListener() {
            
            @Override
            public void onCancel(DialogInterface dialog) {
                enterHome();
                dialog.dismiss();
            }
        });
        builder.setMessage(mDescription);
        builder.setPositiveButton("立即升级", new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    FinalHttp finalHttp = new FinalHttp();
                    finalHttp.download(mApkurl, Environment.getExternalStorageDirectory()
                            .getAbsolutePath() + "/mobilesafe2.0.apk", new AjaxCallBack<File>() {

                        @Override
                        public void onFailure(Throwable t, int errorNo, String strMsg) {
                            t.printStackTrace();
                            Toast.makeText(SplashActivity.this, "下载失败", Toast.LENGTH_LONG).show();
                            super.onFailure(t, errorNo, strMsg);
                        }

                        @Override
                        public void onLoading(long count, long current) {
                            super.onLoading(count, current);
                            int progress = (int) (current * 100 / count);
                            mUpdateInfoTextView.setText("下载进度： " + progress + "%");
                        }

                        @Override
                        public void onSuccess(File t) {
                            super.onSuccess(t);
                            installAPK(t);
                        }

                        private void installAPK(File t) {
                            Intent intent = new Intent();
                            intent.setAction("android.intent.action.VIEW");
                            intent.addCategory("android.intent.category.DEFAULT");
                            intent.setDataAndType(Uri.fromFile(t), "application/vnd.android.package-archive");
                            
                            startActivity(intent);
                        }

                    });
                } else {
                    Toast.makeText(SplashActivity.this, "没有sdcard，请安装上再试", Toast.LENGTH_LONG)
                            .show();
                    return;
                }
            }
        });
        builder.setNegativeButton("下次再说", new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub

            }
        });
        builder.show();
    }

    protected void enterHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        // close current activity.
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.splash, menu);
        return true;
    }

    /**
     * 得到应用程序的版本名称
     */
    private String getVersionName() {
        // 用来管理手机的APK
        PackageManager pm = getPackageManager();

        try {
            // 得到知道APK的功能清单文件
            PackageInfo info = pm.getPackageInfo(getPackageName(), 0);
            return info.versionName;
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        }

    }

}
