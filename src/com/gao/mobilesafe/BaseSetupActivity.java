
package com.gao.mobilesafe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.OnGestureListener;
import android.widget.Toast;

public abstract class BaseSetupActivity extends Activity {
    // 1.定义一个手势识别器
    private GestureDetector mGestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 2.实例化手势识别器
        mGestureDetector = new GestureDetector(this, new OnGestureListener() {

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public void onShowPress(MotionEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                // TODO Auto-generated method stub

            }

            /**
             * 当我们的手指在上面滑动的时候回调
             */
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                // 屏蔽在X滑动很慢的情形
                if (Math.abs(velocityX) < 200) {
                    Toast.makeText(getApplicationContext(), "滑动的太慢了",
                            Toast.LENGTH_LONG).show();
                    return true;
                }

                // 屏蔽斜滑动这种情况
                if (Math.abs(e2.getRawY() - e1.getRawY()) > 100) {
                    Toast.makeText(getApplicationContext(), "Can not fling in this way!",
                            Toast.LENGTH_LONG).show();
                    return true;
                }

                if ((e2.getRawX() - e1.getRawX()) > 200) {
                    // Show pre page
                    showPre();
                    return true;
                }

                if ((e1.getRawX() - e2.getRawY()) > 200) {
                    // Show next page
                    showNext();
                    return true;
                }
                return false;
            }

            @Override
            public boolean onDown(MotionEvent e) {
                // TODO Auto-generated method stub
                return false;
            }
        });
    }

    protected abstract void showNext();

    protected abstract void showPre();

    /**
     * 下一步点击事件
     * 
     * @param view
     */
    public void next(View view) {
        showNext();
    }

    /**
     * 上一步的点击事件
     * 
     * @param view
     */
    public void pre(View view) {
        showPre();
    }

    /**
     * 3. 使用手势识别器
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 將屏幕的触摸事件传递给滑动事件。
        mGestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
}
