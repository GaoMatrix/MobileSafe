
package com.gao.mobilesafe;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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
        mPhoneNumEditText.addTextChangedListener(new TextWatcher() {

            /**
             * 当文本发生变化的时候回调
             */
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s != null && s.length() >= 3) {
                    // 查询数据库，并且显示结果
                    String address = NumberAddressQueryUtils.queryNumber(s.toString());
                    mResultTextView.setText(address);
                }

            }

            /**
             * 当文本发生变化之前回调
             */
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                    int after) {
                // TODO Auto-generated method stub

            }

            /**
             * 当文本发生变化之后回调
             */
            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });
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
