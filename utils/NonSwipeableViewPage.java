package com.example.insta.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.DecelerateInterpolator;
import android.widget.Scroller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import java.lang.reflect.Field;


public class NonSwipeableViewPage extends ViewPager {
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }

    @Override
    public boolean onFilterTouchEventForSecurity(MotionEvent event) {
        return false;
    }

    public NonSwipeableViewPage(@NonNull Context context) {
        super(context);
        setMyScroller();
    }

    private void setMyScroller() {
        try {
            Class<?> viewpager= ViewPager.class;
            Field scoller=viewpager.getDeclaredField("mScroller");
            scoller.setAccessible(true);
            scoller.set(this, new MyScoller(getContext()));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

    }

    public NonSwipeableViewPage(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setMyScroller();
    }

    private class MyScoller extends Scroller {
        public MyScoller(Context context) {
        super(context , new DecelerateInterpolator());
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, 400);
        }
    }
}
