package com.example.baodi.zhihu.profile;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import com.example.baodi.zhihu.R;

/**
 * Created by lancelot on 2018/12/21.
 */

public class FragmentViewPager extends ViewPager {
    public FragmentViewPager(Context context) {
        super(context);
    }

    public FragmentViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {


        int height = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            int h = child.getMeasuredHeight();
            if (h > height) height = h;
        }
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        boolean ret = super.dispatchTouchEvent(ev);
//        if (ret) {
//            requestDisallowInterceptTouchEvent(true);
//        }
//        return ret;
//    }
}
