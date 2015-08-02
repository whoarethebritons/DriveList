package com.gamma.drivelist.app;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * Created by eden on 8/2/15.
 */
public class NonScrollable extends ListView {
    public NonScrollable(Context context) {
        super(context);
    }
    public NonScrollable(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public NonScrollable(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //shift off last two bits
        int customHeight = MeasureSpec.makeMeasureSpec(
                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, customHeight);
        ViewGroup.LayoutParams params = getLayoutParams();
        params.height = getMeasuredHeight();
    }
}
