package com.me.shots;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * Created by J Girish on 08-10-2017.
 */

public class Non_extendable_ListView extends ListView {
    public Non_extendable_ListView(Context context) {
        super(context);
    }

    public Non_extendable_ListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Non_extendable_ListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heightMeasureSpec_custom = MeasureSpec.makeMeasureSpec(
                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec_custom);
        ViewGroup.LayoutParams params = getLayoutParams();
        params.height = getMeasuredHeight();
    }


    }

