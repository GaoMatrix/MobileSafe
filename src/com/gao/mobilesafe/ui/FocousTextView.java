package com.gao.mobilesafe.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 自定义TextView,一出生就有焦点
 * @author gaochengquan
 *
 */
public class FocousTextView extends TextView{

    public FocousTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
    }

    public FocousTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    public FocousTextView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }
    
    @Override
    public boolean isFocused() {
        return true;
    }

}
