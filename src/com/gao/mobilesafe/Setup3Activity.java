
package com.gao.mobilesafe;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class Setup3Activity extends BaseSetupActivity {
    private EditText et_setup3_phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup3);
        et_setup3_phone = (EditText) findViewById(R.id.et_setup3_phone);
        et_setup3_phone.setText(mSharedPreferences.getString("safenumber", ""));
    }

    @Override
    protected void showNext() {
        Intent intent = new Intent(this, Setup4Activity.class);
        startActivity(intent);
        finish();
        // 要求在finish()或者startActivity(intent);后面执行
        overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
    }

    /**
     * 选择联系人的点击事件
     * 
     * @param view
     */
    public void selectContact(View view) {
        Intent intent = new Intent(this, SelectContactActivity.class);
        startActivityForResult(intent, 0);

    }

    @Override
    protected void showPre() {
        Intent intent = new Intent(this, Setup2Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.tran_pre_in, R.anim.tran_pre_out);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null)
            return;

        String phone = data.getStringExtra("phone").replace("-", "");
        et_setup3_phone.setText(phone);

    }
}
