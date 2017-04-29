package com.move.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by cxj on 2017/3/30.
 * 这是一个流式布局,但是并不是一个列表
 * 如果想实现很长的列表的形式,对不起这个不适合你
 * 流式布局这个可以放任何控件,但是每一个孩子高度都是一样的
 * 关于宽度,此控件并没有处理测量,都是直接采用父容器推荐的宽,所以不能放在水平的列表或者水平的滚动ScrollerView中
 * 关于高度,控件支持了包裹和填充父容器,也支持在竖直的列表和滚动ScrollerView中
 */
public class XFlowLayout extends ViewGroup {

    public XFlowLayout(Context context) {
        this(context, null);
    }

    public XFlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public XFlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.XFlowLayout);

        //获取横纵向的间距

        mHSpace = a.getDimensionPixelSize(R.styleable.XFlowLayout_h_space, dpToPx(10));
        mVSpace = a.getDimensionPixelSize(R.styleable.XFlowLayout_v_space, dpToPx(10));
        mMaxLines = a.getInt(R.styleable.XFlowLayout_maxlines, -1);

        a.recycle();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // 拿到父容器推荐的宽和高以及计算模式

        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);

        //测量孩子的大小
        measureChildren(widthMeasureSpec, heightMeasureSpec);

        //寻找孩子中最高的一个孩子,找到的值会放在mChildMaxHeight变量中
        findMaxChildMaxHeight();

        //初始化值
        int left = getPaddingLeft(), top = getPaddingTop();

        // 几行
        int lines = 1;

        //循环所有的孩子
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = getChildAt(i);

            if (left != getPaddingLeft()) { //是否是一行的开头
                if ((left + view.getMeasuredWidth()) > sizeWidth - getPaddingRight()) { //需要换行了,因为放不下啦
                    // 如果到了最大的行数,就跳出,top就是当前的
                    if (mMaxLines != -1 && mMaxLines <= lines) {
                        break;
                    }
                    //计算出下一行的top
                    top += mChildMaxHeight + mVSpace;
                    left = getPaddingLeft();
                    lines++;
                }
            }

            left += view.getMeasuredWidth() + mHSpace;

        }

        if (modeHeight == MeasureSpec.EXACTLY) {
            //直接使用父类推荐的宽和高
            setMeasuredDimension(sizeWidth, sizeHeight);
        } else if (modeHeight == MeasureSpec.AT_MOST) {
            setMeasuredDimension(sizeWidth, (top + mChildMaxHeight + getPaddingBottom()) > sizeHeight ? sizeHeight : top + mChildMaxHeight + getPaddingBottom());
        } else {
            setMeasuredDimension(sizeWidth, top + mChildMaxHeight + getPaddingBottom());
        }

    }

    /**
     * -1表示不限制,最多显示几行
     */
    private int mMaxLines = -1;

    /**
     * 孩子中最高的一个
     */
    private int mChildMaxHeight;

    /**
     * 每一个孩子的左右的间距
     * 20是默认值,单位是px
     */
    private int mHSpace = 20;

    /**
     * 每一行的上下的间距
     * 20是默认值,单位是px
     */
    private int mVSpace = 20;

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        findMaxChildMaxHeight();

        //开始安排孩子的位置

        //初始化值
        int left = getPaddingLeft(), top = getPaddingTop();

        // 几行
        int lines = 1;

        //循环所有的孩子
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = getChildAt(i);

            if (left != getPaddingLeft()) { //是否是一行的开头
                if ((left + view.getMeasuredWidth()) > getWidth() - getPaddingRight()) { //需要换行了,因为放不下啦
                    // 如果到了最大的行数,就跳出,top就是当前的
                    if (mMaxLines != -1 && mMaxLines <= lines) {
                        break;
                    }
                    //计算出下一行的top
                    top += mChildMaxHeight + mVSpace;
                    left = getPaddingLeft();
                    lines++;
                }
            }

            int dy = (mChildMaxHeight - view.getMeasuredHeight()) / 2;

            //安排孩子的位置
            view.layout(left, top + dy, left + view.getMeasuredWidth(), top + view.getMeasuredHeight() + dy);

            left += view.getMeasuredWidth() + mHSpace;

        }

    }

    /**
     * 寻找孩子中最高的一个孩子
     */
    private void findMaxChildMaxHeight() {
        mChildMaxHeight = 0;
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = getChildAt(i);
            if (view.getMeasuredHeight() > mChildMaxHeight) {
                mChildMaxHeight = view.getMeasuredHeight();
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

    public void setHSpace(int hSpace) {
        this.mHSpace = hSpace;
    }

    public void setVSpace(int vSpace) {
        this.mVSpace = vSpace;
    }

}
