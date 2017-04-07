package com.move.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by cxj on 2017/4/6.
 * 这也是一个轮播图的指示器
 * 控件的设计只有包裹的效果,无论在什么情况下
 * 支持内边距设置
 */
public class XIndicator extends View implements ViewPager.OnPageChangeListener {

    public XIndicator(Context context) {
        this(context, null);
    }

    public XIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public XIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
        readAttr(context, attrs);

        if (indicatorCount < 0) {
            indicatorCount = 0;
        }

    }

    /**
     * 读取自定义属性
     *
     * @param context 上下文
     * @param attrs   属性集合
     */
    private void readAttr(Context context, AttributeSet attrs) {
        // read attr
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.XIndicator);

        indicatorWidth = a.getDimensionPixelSize(R.styleable.XIndicator_indicatorWidth, indicatorWidth);
        indicatorHeight = a.getDimensionPixelSize(R.styleable.XIndicator_indicatorHeight, indicatorHeight);

        indicatorHorizontalSpace = a.getDimensionPixelSize(R.styleable.XIndicator_indicatorSpace, indicatorHorizontalSpace);

        selectedIndicatorWidth = a.getDimensionPixelSize(R.styleable.XIndicator_selectedIndicatorWidth, selectedIndicatorWidth);
        selectedIndicatorHeight = a.getDimensionPixelSize(R.styleable.XIndicator_selectedIndicatorHeight, selectedIndicatorHeight);

        unSelectedIndicatorColor = a.getColor(R.styleable.XIndicator_indicatorColor, unSelectedIndicatorColor);
        selectedIndicatorColor = a.getColor(R.styleable.XIndicator_selectedIndicatorColor, selectedIndicatorColor);

        indicatorCount = a.getInt(R.styleable.XIndicator_count, 0);
        if (indicatorCount < 0) {
            indicatorCount = 0;
        }

        indicatorIndex = a.getInt(R.styleable.XIndicator_index, 0);
        if (indicatorIndex < 0) {
            indicatorIndex = 0;
        }

        if (indicatorIndex > indicatorCount - 1) {
            indicatorIndex = indicatorCount - 1;
        }

        a.recycle();

    }

    /**
     * 初始化
     *
     * @param context
     */
    private void init(Context context) {

        // 转化数据
        indicatorWidth = dpToPx(indicatorWidth);
        indicatorHeight = dpToPx(indicatorHeight);
        selectedIndicatorWidth = dpToPx(selectedIndicatorWidth);
        selectedIndicatorHeight = dpToPx(selectedIndicatorHeight);

    }

    /**
     * 指示器的宽度,单位是dp
     */
    private int indicatorWidth = 10;

    /**
     * 指示器的高度,单位是dp
     */
    private int indicatorHeight = 4;

    /**
     * 被选中的指示器的宽度,单位是dp
     */
    private int selectedIndicatorWidth = 20;

    /**
     * 被选中的指示器的高度,单位是dp
     */
    private int selectedIndicatorHeight = 4;

    /**
     * 每一个指示器之间的间距,单位是dp
     */
    private int indicatorHorizontalSpace = 2;

    /**
     * 被选中的指示器的颜色
     */
    private int selectedIndicatorColor = Color.parseColor("#ED5C55");

    /**
     * 没被选中的指示器的颜色
     */
    private int unSelectedIndicatorColor = Color.WHITE;

    /**
     * 指示器的个数
     */
    private int indicatorCount = 0;

    /**
     * 当前选中的指示器下标
     */
    private int indicatorIndex = 0;

    /**
     * 偏移的比例,只有>0的,因为ViewPager中如果从0滑动到1,那么这个值就是这么变化的:
     * 0->0.999->0
     * 下标变化:
     * 0->0->1
     * 如果从1滑动到0,那么这个值就是这么变化的:
     * 0->0.999->0
     * 下标变化:
     * 1->0->0
     */
    private float offSet = 0f;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // calculate width of no selected index
        int width = indicatorCount * indicatorWidth + (indicatorCount - 1) * indicatorHorizontalSpace;

        // get the bigger height
        int height = Math.max(indicatorHeight, selectedIndicatorHeight);

        // if have selected indicator
        if (indicatorIndex >= 0 && indicatorIndex < indicatorCount) {
            width += selectedIndicatorWidth - indicatorWidth;
        }

        // add the padding
        width += getPaddingLeft() + getPaddingRight();
        height += getPaddingTop() + getPaddingBottom();

        // only have wrap

        // save the width and height
        setMeasuredDimension(width, height);

    }

    @Override
    protected void onDraw(Canvas c) {
        super.onDraw(c);

        calculateRectFs();

        //绘制的起点

        Paint p = new Paint();
        p.setColor(Color.GREEN);
        p.setAntiAlias(true);// 设置画笔的锯齿效果

        if (rectFs == null) {
            return;
        }

        p.setColor(unSelectedIndicatorColor);

        for (int i = 0; i < rectFs.length; i++) {

            //不绘制选中的
            if (indicatorIndex == i) {
                continue;
            }

            //拿到位置信息
            RectF r = rectFs[i];

            //计算出半径
            float radius = Math.min(Math.abs((r.bottom - r.top) / 2), Math.abs((r.right - r.left) / 2));

            //绘制圆角矩形
            c.drawRoundRect(r, radius, radius, p);

        }

        if (indicatorIndex >= 0 && indicatorIndex < indicatorCount) {

            p.setColor(selectedIndicatorColor);

            //拿到位置信息
            RectF r = rectFs[indicatorIndex];

            //计算出半径
            float radius = Math.min(Math.abs((r.bottom - r.top) / 2), Math.abs((r.right - r.left) / 2));

            //绘制圆角矩形
            c.drawRoundRect(r, radius, radius, p);

        }
    }

    /**
     * 如果指示器个数>0
     */
    private RectF[] rectFs;

    /**
     * 记录所有指示器的位置信息,包括选中的那个
     */
    private void calculateRectFs() {

        if (rectFs == null || rectFs.length != indicatorCount) {
            rectFs = new RectF[indicatorCount];
        }

        int startLeft = getPaddingLeft();
        int startTop = getPaddingTop();
        int startBottom = getHeight() - getPaddingBottom();

        //除去内边距可绘制的区域的高度
        int drawHeight = getHeight() - getPaddingTop() - getPaddingBottom();

        /* 绘制的时候,因为绘制的指示器有高度不同的情况
         所以如果高度小一点的,那么就有一个绘制的top起点就有一个dy需要加上去
         才能保证绘制所有的指示器都在中间*/
        int dy;

        int currentIndicatorWidth = indicatorWidth;

        // 循环计算
        for (int i = 0; i < indicatorCount; i++) {

            //如果是选中的那个指示器
            if (i == indicatorIndex) {
                dy = (drawHeight - selectedIndicatorHeight) / 2;
                currentIndicatorWidth = selectedIndicatorWidth;
            } else {
                dy = (drawHeight - indicatorHeight) / 2;
                currentIndicatorWidth = indicatorWidth;
            }

            // 创建矩形,填入数据
            RectF rectF = new RectF(startLeft, startTop + dy,
                    startLeft + currentIndicatorWidth, startBottom - dy);

            startLeft += currentIndicatorWidth + indicatorHorizontalSpace;

            rectFs[i] = rectF;

        }

        // 这是就是根据偏移的百分比,在正常基础上左右偏移坐标
        // 但是这里不针对选择的下标是最后一个的情况

        if (indicatorIndex != indicatorCount - 1) {

            // 拿到选中那个位置信息和选中的下一个的位置信息
            RectF rectFSelected = rectFs[indicatorIndex];
            RectF next = rectFs[indicatorIndex + 1];

            // 计算两者的偏移量
            float selectedOffsetPx = (next.right - rectFSelected.right) * offSet;
            float nextOffsetPx = (rectFSelected.left - next.left) * offSet;

            // 在原来的基础上加上偏移量

            rectFSelected.left += selectedOffsetPx;
            rectFSelected.right += selectedOffsetPx;

            next.left += nextOffsetPx;
            next.right += nextOffsetPx;

        }

    }

    /**
     * 和ViewPager的滑动事件绑定
     * 此方法必须在ViewPager设置适配器之后
     *
     * @param vp
     */
    public void setUpViewPager(@NonNull ViewPager vp) {
        setUpViewPager(vp, false);
    }


    /**
     * 和ViewPager的滑动事件绑定
     * 此方法必须在ViewPager设置适配器之后
     *
     * @param vp
     * @param isReLayout 是否重新测量自己
     */
    public void setUpViewPager(@NonNull ViewPager vp, boolean isReLayout) {

        PagerAdapter adapter = vp.getAdapter();

        if (adapter != null) {
            indicatorCount = adapter.getCount();
        }

        indicatorIndex = vp.getCurrentItem();

        vp.removeOnPageChangeListener(this);
        vp.addOnPageChangeListener(this);

        //需要重新测量自己哦
        requestLayout();

    }

    //=======================Setter=========================start

    public void setIndicatorWidth(int indicatorWidth,boolean isReLayout) {
        this.indicatorWidth = indicatorWidth;
        if (isReLayout) {
            requestLayout();
        }
    }

    public void setIndicatorHeight(int indicatorHeight,boolean isReLayout) {
        this.indicatorHeight = indicatorHeight;
        if (isReLayout) {
            requestLayout();
        }
    }

    public void setSelectedIndicatorWidth(int selectedIndicatorWidth,boolean isReLayout) {
        this.selectedIndicatorWidth = selectedIndicatorWidth;
        if (isReLayout) {
            requestLayout();
        }
    }

    public void setSelectedIndicatorHeight(int selectedIndicatorHeight,boolean isReLayout) {
        this.selectedIndicatorHeight = selectedIndicatorHeight;
        if (isReLayout) {
            requestLayout();
        }
    }

    public void setIndicatorHorizontalSpace(int indicatorHorizontalSpace,boolean isReLayout) {
        this.indicatorHorizontalSpace = indicatorHorizontalSpace;
        if (isReLayout) {
            requestLayout();
        }
    }

    public void setSelectedIndicatorColor(int selectedIndicatorColor,boolean isReLayout) {
        this.selectedIndicatorColor = selectedIndicatorColor;
        if (isReLayout) {
            requestLayout();
        }
    }

    public void setUnSelectedIndicatorColor(int unSelectedIndicatorColor,boolean isReLayout) {
        this.unSelectedIndicatorColor = unSelectedIndicatorColor;
        if (isReLayout) {
            requestLayout();
        }
    }

    public void setIndicatorCount(int indicatorCount,boolean isReLayout) {
        this.indicatorCount = indicatorCount;
        offSet = 0f;
        if (this.indicatorIndex > this.indicatorCount - 1) {
            this.indicatorCount = this.indicatorCount - 1;
        }
        if (this.indicatorIndex < 0) {
            this.indicatorIndex = 0;
        }
        if (isReLayout) {
            requestLayout();
        }
    }

    public void setIndicatorIndex(int indicatorIndex,boolean isReLayout) {
        this.indicatorIndex = indicatorIndex;
        offSet = 0f;
        if (this.indicatorIndex > this.indicatorCount - 1) {
            this.indicatorCount = this.indicatorCount - 1;
        }
        if (this.indicatorIndex < 0) {
            this.indicatorIndex = 0;
        }
        if (isReLayout) {
            requestLayout();
        }
    }

    //=======================Setter=========================end

    /**
     * dp的单位转换为px的
     *
     * @param dps
     * @return
     */
    public int dpToPx(int dps) {
        return Math.round(getResources().getDisplayMetrics().density * dps);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        indicatorIndex = position;
        offSet = positionOffset;
        postInvalidate();
    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

}
