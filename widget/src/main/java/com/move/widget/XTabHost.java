package com.move.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by cxj on 2017/2/19.
 * 模仿qq主界面的选项卡
 */
public class XTabHost extends LinearLayout implements View.OnClickListener {


    public XTabHost(Context context) {
        this(context, null);
    }

    public XTabHost(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public XTabHost(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //设置孩子排列的方向是水平的
        setOrientation(HORIZONTAL);

        //读取自定义属性
        readAttr(context, attrs);

        //显示效果
        sove();

    }

    /**
     * 读取自定义属性
     *
     * @param context
     * @param attrs
     */
    private void readAttr(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.XTabHost);

        //获取自定义属性

        curIndex = a.getInt(R.styleable.XTabHost_default_index, 0);
        radius = a.getDimensionPixelSize(R.styleable.XTabHost_radius, dpToPx(radius));
        backBg = a.getColor(R.styleable.XTabHost_bg, Color.WHITE);
        unSelectTabBg = a.getColor(R.styleable.XTabHost_tab_unselect_color, Color.parseColor("#51B5EF"));
        selectTabBg = a.getColor(R.styleable.XTabHost_tab_select_color, Color.WHITE);
        unSelectTextColor = a.getColor(R.styleable.XTabHost_text_unselect_color, Color.WHITE);
        selectTextColor = a.getColor(R.styleable.XTabHost_text_select_color, Color.parseColor("#51B5EF"));
        textSize = a.getDimensionPixelSize(R.styleable.XTabHost_text_size, 16);
        space = a.getDimensionPixelSize(R.styleable.XTabHost_tab_space, 1);
        tabWidth = a.getDimensionPixelSize(R.styleable.XTabHost_tab_width, dpToPx(tabWidth));
        tabHeight = a.getDimensionPixelSize(R.styleable.XTabHost_tab_height, -1);

        CharSequence[] arr = a.getTextArray(R.styleable.XTabHost_src);
        if (arr != null) {
            String[] tArr = new String[arr.length];
            for (int i = 0; i < arr.length; i++) {
                tArr[i] = String.valueOf(arr[i]);
            }
            textArr = tArr;
        }

        a.recycle();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {


        //获取计算模式
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);

        //获取推荐的宽和高
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);

        if (modeWidth == MeasureSpec.EXACTLY) { //如果是确定的

        } else { //如果是包裹的或者在横向的列表中
            if (tabWidth > -1) {
                for (int i = 0; i < getChildCount(); i++) {
                    TextView view = (TextView) getChildAt(i);
                    LayoutParams lp = (LayoutParams) view.getLayoutParams();
                    lp.width = tabWidth;
                }
            }
        }

        if (modeHeight == MeasureSpec.EXACTLY) { //如果是确定的

        } else { //如果是包裹的或者在纵向的列表中
            if (tabHeight > -1) {
                for (int i = 0; i < getChildCount(); i++) {
                    TextView view = (TextView) getChildAt(i);
                    LayoutParams lp = (LayoutParams) view.getLayoutParams();
                    lp.height = tabHeight;
                }
            }

        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    /**
     * 自身控件的背景
     */
    private int backBg = Color.WHITE;

    /**
     * 没有选中的tab的背景
     */
    private int unSelectTabBg = Color.BLUE;

    /**
     * 选中的tab的背景
     */
    private int selectTabBg = Color.WHITE;

    /**
     * 一个Tab的宽和高,在自身是包裹的时候会被用到
     * -1表示不起作用,计算的时候按照包裹孩子处理
     * 80是dp的单位
     */
    private int tabWidth = 80, tabHeight = -1;

    /**
     * 未选中的文本的颜色
     */
    private int unSelectTextColor = Color.WHITE;

    /**
     * 选中的文本的颜色
     */
    private int selectTextColor = Color.BLUE;

    /**
     * 默认的字体大小,sp
     */
    private int textSize = 16;

    /**
     * 间距,px
     */
    private int space = 1;

    /**
     * 圆角半径,dp
     */
    private int radius = 0;

    /**
     * 当前的下标
     */
    private int curIndex = 1;

    /**
     * 所有要显示的文本
     */
    private String[] textArr = new String[]{};


    /**
     * 根据所有的参数,弄出效果
     */
    private void sove() {

        GradientDrawable dd = new GradientDrawable();
        //设置圆角
        dd.setCornerRadii(new float[]{radius, radius, radius, radius, radius, radius, radius, radius});
        //设置背景颜色
        dd.setColor(backBg);
        //兼容低版本
        if (Build.VERSION.SDK_INT >= 16) {
            setBackground(dd);
        } else {
            setBackgroundDrawable(dd);
        }

        //移除所有的孩子
        removeAllViews();

        if (curIndex >= textArr.length || curIndex < 0) {
            curIndex = 0;
        }

        for (int i = 0; i < textArr.length; i++) {

            //创建一个文本
            TextView tv = new TextView(getContext());

            //创建文本的布局对象
            LayoutParams params = new LayoutParams(
                    0, ViewGroup.LayoutParams.MATCH_PARENT
            );

            if (i > 0) {
                params.leftMargin = space;
            }

            GradientDrawable d = getFitGradientDrawable(i);

            //如果选中了设置选中的颜色和背景
            if (curIndex == i) {
                tv.setTextColor(selectTextColor);
                d.setColor(selectTabBg);
            } else {
                tv.setTextColor(unSelectTextColor);
                d.setColor(unSelectTabBg);
            }

            //设置文本
            tv.setText(textArr[i]);
            //设置文本显示在中间
            tv.setGravity(Gravity.CENTER);
            //设置文本大小
            tv.setTextSize(textSize);
            //设置文本的背景,兼容低版本
            if (Build.VERSION.SDK_INT >= 16) {
                tv.setBackground(d);
            } else {
                //noinspection deprecation
                tv.setBackgroundDrawable(d);
            }

            //设置文本(也就是tab)的权重
            params.weight = 1;

            tv.setLayoutParams(params);

            tv.setTag(i);
            tv.setOnClickListener(this);

            //添加孩子
            addView(tv);

        }

    }

    /**
     * 获取每一个tab的背景图,最左边是左边有圆角效果的
     * 最右边是右边有圆角效果的
     * 即是左边又是右边的是四个角都有圆角的
     *
     * @param index tab的下标
     * @return
     */
    private GradientDrawable getFitGradientDrawable(int index) {
        GradientDrawable d = null;
        //根据下标决定圆角
        if (index == 0 && index == textArr.length - 1) {
            d = new GradientDrawable();
            //设置圆角
            d.setCornerRadii(new float[]{radius, radius, radius, radius, radius, radius, radius, radius});
        } else if (index == 0) {
            d = new GradientDrawable();
            //设置圆角
            d.setCornerRadii(new float[]{radius, radius, 0, 0, 0, 0, radius, radius});
        } else if (index == textArr.length - 1) {
            d = new GradientDrawable();
            //设置圆角
            d.setCornerRadii(new float[]{0, 0, radius, radius, radius, radius, 0, 0});
        } else {
            d = new GradientDrawable();
            //设置圆角
            d.setCornerRadii(new float[]{0, 0, 0, 0, 0, 0, 0, 0});
        }
        return d;
    }


    @Override
    public void onClick(View v) {

        //拿到下标
        int index = (int) v.getTag();

        //如果点击的是同一个,不做处理
        if (index == curIndex) {
            return;
        }

        //拿到当前的TextView
        TextView tv = (TextView) getChildAt(curIndex);
        //设置为没有被选中的文本和没有被选中的背景
        tv.setTextColor(unSelectTextColor);
        GradientDrawable d = getFitGradientDrawable(curIndex);
        d.setColor(unSelectTabBg);

        if (Build.VERSION.SDK_INT >= 16) {
            tv.setBackground(d);
        } else {
            //noinspection deprecation
            tv.setBackgroundDrawable(d);
        }

        //记录被选中的下标
        curIndex = index;

        //拿到当前被选中的TextView
        tv = (TextView) getChildAt(curIndex);
        //设置为被选中的文本和被选中的背景
        tv.setTextColor(selectTextColor);
        d = getFitGradientDrawable(curIndex);
        d.setColor(selectTabBg);
        if (Build.VERSION.SDK_INT >= 16) {
            tv.setBackground(d);
        } else {
            //noinspection deprecation
            tv.setBackgroundDrawable(d);
        }

        //如果使用者监听了就通知一下
        if (mOnSelectListener != null) {
            mOnSelectListener.onSelect(index, textArr[index]);
        }

    }

    /**
     * dp的单位转换为px的
     *
     * @param dps
     * @return
     */
    int dpToPx(int dps) {
        return Math.round(getResources().getDisplayMetrics().density * dps);
    }

    /**
     * sp转px
     *
     * @param spVal
     * @return
     */
    int spToPx(float spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, getResources().getDisplayMetrics());
    }

    private OnSelectListener mOnSelectListener;

    /**
     * 设置监听
     *
     * @param mOnSelectListener
     */
    public void setOnSelectListener(OnSelectListener mOnSelectListener) {
        this.mOnSelectListener = mOnSelectListener;
    }

    /**
     * 回调接口
     */
    public interface OnSelectListener {

        /**
         * 回调方法
         *
         * @param index
         * @param text
         */
        void onSelect(int index, String text);

    }

}
