package com.me.shots.Adapter;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

/**
 * Created by Half_BlooD PrincE on 10/6/2017.
 */

public class VerticalViewPager extends ViewPager {

    boolean flag=true;
    Context context;

    public VerticalViewPager(Context context) {
        super(context);
        init();
        this.context=context;
    }

    public VerticalViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    private void init() {
        // The majority of the magic happens here
        setPageTransformer(flag, new VerticalPageTransformer());
        // The easiest way to get rid of the overscroll drawing that happens on the left and right
        setOverScrollMode(OVER_SCROLL_NEVER);
    }
    private static class VerticalPageTransformer implements PageTransformer {
        private static float MIN_SCALE = 0.85f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();

//            view.setBackgroundColor(Color.parseColor("RED"));
            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0);



            } else if (position <= 0) { // [-1,0]
                // Use the default slide transition when moving to the left page
                view.setAlpha(1);
                //view.setTranslationX(1);
                view.setScaleX(1);
                view.setScaleY(1);
                float yPosition = position * view.getHeight();
                view.setTranslationY(yPosition);
                view.setTranslationX(-1 * view.getWidth() * position);


            } else if (position <= 1) { // (0,1]
                // Fade the page out.
                view.setAlpha(1-position);

                view.setTranslationX(-1 * view.getWidth() * position);

//                 Scale the page down (between MIN_SCALE and 1)
                float scaleFactor = MIN_SCALE
                        + (1 - MIN_SCALE) * (1 - Math.abs(position));
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0);
            }

        }
    }

    private float x1,x2,y1,y2;
    static final int MIN_DISTANCE = 5;

    private MotionEvent swapXY(MotionEvent ev) {
        switch(ev.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                x1 = ev.getX();
                y1=ev.getY();
                break;
            case MotionEvent.ACTION_UP:
                x2 = ev.getX();
                y2=ev.getY();

                float deltaX = x2 - x1;
                float deltaY = y2 - y1;

                if (Math.abs(deltaX) > MIN_DISTANCE)
                {
                    setPageTransformer(false, new VerticalPageTransformer());
                    flag=true;
                }

                if (Math.abs(deltaY) > MIN_DISTANCE)
                {
                    setPageTransformer(true, new VerticalPageTransformer());
                    flag=false;
                }
                break;
        }
//        return super.onTouchEvent(ev);
        float width = getWidth();
        float height = getHeight();



        float newX = (ev.getY() / height) * width;
        float newY = (ev.getX() / width) * height;

        ev.setLocation(newX, newY);

        return ev;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev){
        boolean intercepted = super.onInterceptTouchEvent(swapXY(ev));
        swapXY(ev); // return touch coordinates to original reference frame for any child views
        return intercepted;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return super.onTouchEvent(swapXY(ev));
    }

}
