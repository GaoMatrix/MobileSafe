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
    private String mDescOn;
    private String mDescOff;

    private void initView(Context context) {
        // 把一个布局文件--》转换成一个View，并且加载在SettingItemView
        // 参数中的ViewGroup root 设置成SettingItemView
        View.inflate(context, R.layout.setting_view_item, this);
        mStatusCheckBox = (CheckBox) this.findViewById(R.id.cb_status);
        mDescTextView = (TextView) this.findViewById(R.id.tv_description);
        mTitleTextView = (TextView) this.findViewById(R.id.tv_title);
    }

    /**
     * 放要自定义样式也就是第四个参数defStyle的时候使用这个构造方法
     * @param context
     * @param attrs
     * @param defStyle
     */
    public SettingItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

    /**
     * 布局文件实例化类的时候使用的是两个参数的构造方法
     * 布局文件xml中的属性会被封装成AttributeSet attrs属性集合这个类。
     * 
     * @param context
     * @param attrs
     */
    public SettingItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
        String title = attrs.getAttributeValue("http://schemas.android.com/apk/res/com.gao.mobilesafe", "title");
        mDescOn = attrs.getAttributeValue("http://schemas.android.com/apk/res/com.gao.mobilesafe", "desc_on");
        mDescOff = attrs.getAttributeValue("http://schemas.android.com/apk/res/com.gao.mobilesafe", "desc_off");
        mTitleTextView.setText(title);
        setDesc(mDescOff);
    }

    /**
     * 当new出这个对象的时候，调用这个一个参数的构造方法
     * @param context
     */
    public SettingItemView(Context context) {
        super(context);
        initView(context);
    }
    
    public boolean isChecked() {
        return mStatusCheckBox.isChecked();
    }
    
    public void setChecked(boolean checked) {
        if (checked) {
            setDesc(mDescOn);
        } else {
            setDesc(mDescOff);
        }
        mStatusCheckBox.setChecked(checked);
    }

    public void setDesc(String desc) {
        mDescTextView.setText(desc);
    }
}
