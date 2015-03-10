
package com.gao.mobilesafe;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;

public class Setup4Activity extends BaseSetupActivity {
    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup4);
        mSharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
    }

    @Override
    protected void showNext() {
        Editor editor = mSharedPreferences.edit();
        editor.putBoolean("configed", true);
        editor.commit();

        Intent intent = new Intent(this, LostFindActivity.class);
        startActivity(intent);
        finish();
        // 要求在finish()或者startActivity(intent);后面执行
        overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
    }

    @Override
    protected void showPre() {
        Intent intent = new Intent(this, Setup3Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.tran_pre_in, R.anim.tran_pre_out);
    }
}
