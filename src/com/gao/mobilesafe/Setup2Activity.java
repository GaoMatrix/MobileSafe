
package com.gao.mobilesafe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Setup2Activity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup2);
    }

    /**
     * 上一页
     * 
     * @param view
     */
    public void next(View view) {
        Intent intent = new Intent(this, Setup3Activity.class);
        startActivity(intent);
        finish();
        // 要求在finish()或者startActivity(intent);后面执行
        overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
    }

    /**
     * 下一页
     * 
     * @param view
     */
    public void pre(View view) {
        Intent intent = new Intent(this, Setup1Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.tran_pre_in, R.anim.tran_pre_out);
    }
}