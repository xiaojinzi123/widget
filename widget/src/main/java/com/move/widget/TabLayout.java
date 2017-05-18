package com.move.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by cxj on 2017/5/15.
 * 这是一个选项卡的自定义控件
 * 1.支持不滚动的模式,平分宽度
 * 2.支持滚动的模式
 * 3.支持个数少的时候不滚动平分宽度,个数多了,能滚动
 */
public class TabLayout extends ViewGroup {

    public TabLayout(Context context) {
        this(context, null);
    }

    public TabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /**
     * 初始化
     *
     * @param context
     */
    private void init(Context context) {

    }

    /**
     * 选项卡的显示模式
     */
    private int tabMode;

    private int indicatorHeight = 4;

    private int hSpace = 10;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // get calculate mode of width and height
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);

        // get recommend width and height
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);

        int childCount = getChildCount();

        int measureWidth = MeasureSpec.makeMeasureSpec(sizeWidth, MeasureSpec.AT_MOST);
        int measureHeight = MeasureSpec.makeMeasureSpec(sizeHeight - indicatorHeight - getPaddingTop() - getPaddingBottom(), MeasureSpec.AT_MOST);

        measureChildren(measureWidth, measureHeight);

        int width = getPaddingLeft() + getPaddingRight();

        for (int i = 0; i < childCount; i++) {
            View view = getChildAt(i);
            width += view.getMeasuredWidth();
        }

        setMeasuredDimension(sizeWidth, sizeHeight);

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        int left = getPaddingLeft();
        int top = getPaddingTop();
        for (int i = 0; i < childCount; i++) {
            View view = getChildAt(i);
            view.layout(left, top, left + view.getMeasuredWidth(), top + view.getMeasuredHeight());
            left += view.getMeasuredWidth() + hSpace;
        }
    }


    /**
     * 设置数据
     *
     * @param arr
     */
    public void setData(String[] arr) {

        for (int i = 0; i < arr.length; i++) {
            String text = arr[i];

            TextView tv = new TextView(getContext());
            tv.setText(text);

            addView(tv);

        }

    }

}
