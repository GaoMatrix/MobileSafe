package com.gao.mobilesafe.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gao.mobilesafe.R;

/**
 * 自定义组合控件，他里面有两个TextView,还有一个CheckBox,还有一个View
 * @author gaochengquan
 *
 */
public class SettingItemView extends RelativeLayout{
    private CheckBox mStatusCheckBox;
    private TextView mDescTextView;
    private TextView mTitleTextView;

    private void initView(Context context) {
        // 把一个布局文件--》转换成一个View，并且加载在SettingItemView
        // 参数中的ViewGroup root 设置成SettingItemView
        View.inflate(context, R.layout.setting_view_item, this);
        mStatusCheckBox = (CheckBox) this.findViewById(R.id.cb_status);
        mDescTextView = (TextView) this.findViewById(R.id.tv_description);
        mTitleTextView = (TextView) this.findViewById(R.id.tv_title);
    }

    public SettingItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

    public SettingItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public SettingItemView(Context context) {
        super(context);
        initView(context);
    }
    
    public boolean isChecked() {
        return mStatusCheckBox.isChecked();
    }
    
    public void setChecked(boolean checked) {
        mStatusCheckBox.setChecked(checked);
    }

    public void setDesc(String desc) {
        mDescTextView.setText(desc);
    }
}
