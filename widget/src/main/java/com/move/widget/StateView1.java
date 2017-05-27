package com.move.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by cxj on 2017/5/27.
 * 类似于显示物流信息的控件,但是这个是水平显示的
 */
public class StateView1 extends ViewGroup {

    public StateView1(Context context) {
        this(context, null);
    }

    public StateView1(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StateView1(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        // 读取自定义属性
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.StateView1);

        hSpace = a.getDimensionPixelOffset(R.styleable.StateView1_sv1_horizontal_space, dpToPx(10));
        vSpace = a.getDimensionPixelOffset(R.styleable.StateView1_sv1_vertical_space, dpToPx(2));

        textSize = a.getInt(R.styleable.StateView1_sv1_text_size, 16);
        unSelectedTextColor = a.getColor(R.styleable.StateView1_sv1_text_color, Color.GREEN);
        selectedTextColor = a.getColor(R.styleable.StateView1_sv1_text_selected_color, Color.GRAY);

        circleBorderWidth = a.getDimensionPixelOffset(R.styleable.StateView1_sv1_circle_border_width, dpToPx(4));
        circleRadius = a.getDimensionPixelOffset(R.styleable.StateView1_sv1_circle_radius, dpToPx(8));
        unSelectedCircleColor = a.getColor(R.styleable.StateView1_sv1_circle_color, Color.GREEN);
        selectedCircleColor = a.getColor(R.styleable.StateView1_sv1_circle_selected_color, Color.GRAY);

        lineHeight = a.getDimensionPixelOffset(R.styleable.StateView1_sv1_line_height, dpToPx(2));
        lineMargin = a.getDimensionPixelOffset(R.styleable.StateView1_sv1_line_margin, dpToPx(4));
        unSelectedLineColor = a.getColor(R.styleable.StateView1_sv1_line_color, Color.GREEN);
        selectedLineColor = a.getColor(R.styleable.StateView1_sv1_line_selected_color, Color.GRAY);

        progress = a.getInt(R.styleable.StateView1_sv1_progress, -1);

        int resourceId = a.getResourceId(R.styleable.StateView1_sv1_data, -1);
        if (resourceId != -1) {
            String[] strings = getResources().getStringArray(resourceId);
            disPlay(strings, progress);
        }

        a.recycle();

        init(context);

    }

    /**
     * 初始化
     *
     * @param context
     */
    private void init(Context context) {
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // get recommend width and height
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);

        measureChildren(
                MeasureSpec.makeMeasureSpec(sizeWidth, MeasureSpec.AT_MOST),
                MeasureSpec.makeMeasureSpec(sizeHeight, MeasureSpec.AT_MOST)
        );

        int width = 0;
        int height = 0;

        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = getChildAt(i);
            width += view.getMeasuredWidth() + hSpace;
            if (view.getMeasuredHeight() > height) {
                height = view.getMeasuredHeight();
            }
        }

        height += circleRadius * 2 + vSpace + circleBorderWidth;

        // 加上内边距

        height += getPaddingTop() + getPaddingBottom();
        width += getPaddingLeft() + getPaddingRight();

        setMeasuredDimension(width, height);

    }

    @Override
    protected void dispatchDraw(Canvas c) {
        super.dispatchDraw(c);

        int childCount = getChildCount();

        int top = getPaddingTop();

        // 绘制横线
        for (int i = 0; i < childCount; i++) {

            View view = getChildAt(i);

            float centerX = (view.getLeft() + view.getRight()) / 2;
            float centerY = top + view.getMeasuredHeight() + circleRadius + vSpace;

            if (i > 0) {
                view = getChildAt(i - 1);
                float preCenterX = (view.getLeft() + view.getRight()) / 2;
                float preCenterY = top + view.getMeasuredHeight() + circleRadius + vSpace;
                if (i <= progress) {
                    linePaint.setColor(selectedLineColor);
                } else {
                    linePaint.setColor(unSelectedLineColor);
                }
                c.drawLine(
                        preCenterX + circleRadius + lineMargin,
                        preCenterY, centerX - circleRadius - lineMargin,
                        centerY, linePaint
                );
            }


        }

        // 绘制圆点

        for (int i = 0; i < childCount; i++) {

            View view = getChildAt(i);

            float centerX = (view.getLeft() + view.getRight()) / 2;
            float centerY = top + view.getMeasuredHeight() + circleRadius + vSpace;

            if (i <= progress) {
                circlePaint.setStyle(Paint.Style.FILL);
                circlePaint.setColor(selectedCircleColor);
                circlePaint.setStrokeWidth(circleBorderWidth);
                c.drawCircle(centerX, centerY, circleRadius + (circleBorderWidth / 2), circlePaint);
            } else {
                circlePaint.setStyle(Paint.Style.STROKE);
                circlePaint.setColor(unSelectedCircleColor);
                circlePaint.setStrokeWidth(circleBorderWidth);
                c.drawCircle(centerX, centerY, circleRadius, circlePaint);
            }

        }

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

    private Paint circlePaint = new Paint();
    private Paint linePaint = new Paint();

    // 一些颜色信息

    private int selectedTextColor = Color.GREEN;
    private int unSelectedTextColor = Color.GRAY;
    private int textSize;

    private int selectedCircleColor = Color.GREEN;
    private int unSelectedCircleColor = Color.GRAY;
    private int circleBorderWidth;
    private int circleRadius;

    private int selectedLineColor = Color.GREEN;
    private int unSelectedLineColor = Color.GRAY;
    private int lineHeight;
    private int lineMargin;

    private int hSpace;
    private int vSpace;


    private int progress;

    private String[] arr;

    public void setProgress(int progress) {
        this.progress = progress;
        adjustText();
        // 重绘界面
        postInvalidate();
    }

    /**
     * 显示
     *
     * @param arr
     * @param progress
     */
    public void disPlay(String[] arr, int progress) {

        this.progress = progress;
        this.arr = arr;

        int childCount = getChildCount();

        if (arr.length >= childCount) { // 如果是超出的
            for (int i = 0; i < arr.length - childCount; i++) {
                TextView tv = new TextView(getContext());
                tv.setLayoutParams(new LayoutParams(
                        LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT
                ));
                addView(tv);
            }
        } else {
            for (int i = 0; i < childCount - arr.length; i++) {
                removeViewAt(i);
            }
        }

        adjustText();

        // 重绘界面
        postInvalidate();


    }

    /**
     * 调整文本部分
     */
    private void adjustText() {

        int childCount = getChildCount();

        // 调整属性
        for (int i = 0; i < childCount; i++) {

            TextView tv = (TextView) getChildAt(i);

            tv.setMaxLines(1);
            tv.setEllipsize(TextUtils.TruncateAt.END);
            tv.setText(arr[i]);
            tv.setTextSize(textSize);

            if (i <= progress) {
                tv.setTextColor(selectedTextColor);
            } else {
                tv.setTextColor(unSelectedTextColor);
            }
        }

    }

    /**
     * dp的单位转换为px的
     *
     * @param dps
     * @return
     */
    public int dpToPx(int dps) {
        return Math.round(getResources().getDisplayMetrics().density * dps);
    }


}
