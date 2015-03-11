
package com.gao.mobilesafe;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gao.mobilesafe.db.dao.NumberAddressQueryUtils;

public class NumberAddressQueryActivity extends Activity {
    private EditText mPhoneNumEditText;
    private TextView mResultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number_address_query);
        mPhoneNumEditText = (EditText) findViewById(R.id.ed_phone);
        mResultTextView = (TextView) findViewById(R.id.result);
    }

    /**
     * 点击事件,查询号码归属地
     * 
     * @param view
     */
    public void numberAddressQuery(View view) {
        String phone = mPhoneNumEditText.getText().toString().trim();
        if (!TextUtils.isEmpty(phone)) {
            String address = NumberAddressQueryUtils.queryNumber(phone);
            mResultTextView.setText(address);
        }
    }
}
