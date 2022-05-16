package com.tryine.sdgq.view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;

public class MyNestedScrollView extends NestedScrollView {
    private OnMyScrollChanged onMyNestedScrollView;

    public MyNestedScrollView(@NonNull Context context) {
        this(context, null);
    }

    public MyNestedScrollView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyNestedScrollView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (onMyNestedScrollView != null) {
            onMyNestedScrollView.onScroll(l, t, oldl, oldt);
        }
    }

    public void setMyNestedScrollView(OnMyScrollChanged onMyNestedScrollView) {
        this.onMyNestedScrollView = onMyNestedScrollView;
    }

    public interface OnMyScrollChanged {
        /**
         * 滑动的方法
         *
         * @param left    左边
         * @param top     上边
         * @param oldLeft 之前的左边
         * @param oldTop  之前的上边
         */
        void onScroll(int left, int top, int oldLeft, int oldTop);
    }
}

