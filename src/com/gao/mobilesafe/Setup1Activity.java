
package com.gao.mobilesafe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;

public class Setup1Activity extends Activity {
    // 1.定义一个手势识别器
    private GestureDetector mGestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup1);
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
                if ((e2.getRawX() - e1.getRawX()) > 200) {
                    // Show next page
                    return true;
                }

                if ((e1.getRawX() - e2.getRawY()) > 200) {
                    // Show pre page
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

    /**
     * 下一步点击事件
     * 
     * @param view
     */
    public void next(View view) {
        showNext();
    }

    private void showNext() {
        Intent intent = new Intent(this, Setup2Activity.class);
        startActivity(intent);
        finish();
        // 要求在finish()或者startActivity(intent);后面执行
        overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
    }

    /**
     * 使用手势识别器
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 將屏幕的触摸事件传递给滑动事件。
        mGestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
}
