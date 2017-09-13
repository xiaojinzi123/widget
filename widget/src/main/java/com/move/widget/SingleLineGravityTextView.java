package com.move.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;

/**
 * Created by cxj on 2017/4/20.
 * 这个控件用于显示一个文本中间是空的,比如这样子
 * 你好世界
 * 你  好
 * 中间可以空出若干个空格的
 */
public class SingleLineGravityTextView extends AppCompatTextView {

    public SingleLineGravityTextView(Context context) {
        this(context, null);
    }

    public SingleLineGravityTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SingleLineGravityTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);


        // 开始读取自定义属性
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SingleLineGravityTextView);

        realText = a.getString(R.styleable.SingleLineGravityTextView_single_line_gravity_real_text);

        a.recycle();


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private String realText = "";

    @Override
    protected void onDraw(Canvas c) {
        super.onDraw(c);

        c.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

        int width = getWidth();
        int height = getHeight();

        TextPaint paint = getPaint();

        if (paint == null || TextUtils.isEmpty(realText)) {
            return;
        }

        int realTextLength = realText.length();

        if (realTextLength <= 1) {
            throw new IllegalArgumentException("the realText's length must bigger than 1");
        }

        int lineCount = getLineCount();
        if (lineCount > 1) {
            throw new IllegalArgumentException("the lineCount must be 1");
        }

        int restWidth = width;
        float startX = 0f;

        Paint.FontMetrics fm = paint.getFontMetrics();

        float top = Math.abs(fm.top - fm.leading);

        String start = realText.substring(0, 1);
        float textWidth = paint.measureText(start);
        c.drawText(start, 0, top, paint);
        restWidth -= textWidth;
        startX += textWidth;

        String end = realText.substring(realTextLength - 1, realTextLength);
        textWidth = paint.measureText(end);
        c.drawText(end, width - textWidth, top, paint);
        restWidth -= textWidth;

        if (realTextLength == 2) {
            return;
        }

        float eachTextWidthInterval = (restWidth + 0f) / (realTextLength - 2 + 1);

        for (int i = 1; i < realTextLength - 1; i++) {
            String currText = realText.substring(i, i + 1);
            textWidth = paint.measureText(currText);
            startX += eachTextWidthInterval;
            c.drawText(currText, startX - textWidth / 2, top, paint);
        }

    }

    public String getRealText() {
        return realText;
    }

    public void setRealText(String realText) {
        this.realText = realText;
        invalidate();
    }

}
