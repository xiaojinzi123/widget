package com.move.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import static android.animation.ValueAnimator.ofInt;

/**
 * Created by cxj on 2017/6/12.
 * 实现一个不可再向上或者向下拖动的列表的OverScroll效果
 * 现在实现了下拉刷新的,如果没有设置{@link OverScrollerView#setHeaderView(IHeaderView)}
 * 就只有上下的overscroll效果
 */
public class OverScrollerView extends ViewGroup {

    public OverScrollerView(Context context) {
        this(context, null);
    }

    public OverScrollerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OverScrollerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        int childCount = getChildCount();
        if (childCount != 1 && childCount != 2) {
            throw new RuntimeException("the childCount must between [1,2]");
        }
    }


    /**
     * 初始化
     *
     * @param context
     */
    private void init(Context context) {
    }


    private View contentView;
    private View headerView;

    private boolean isHeaderPulling = false;
    private boolean isFootPulling = false;
    private boolean isDispatchActionDown = false;
    private boolean isDispatchActionCancel = false;

    private boolean isHeaderFreshing;    // 正在刷新
    private boolean isHeaderPreFreshing; // 准备刷新

    /**
     * 使用拖拽的帮助类,可以拿到移动的dy dx,并且支持了多个手指,这个类在这里并不是发挥其本身的拖拽功能
     * 只是为了拿到他计算的dy 方便做计算
     */
    private ViewDragHelper dragHelper = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return true;
        }

        /**
         * 现在就先考虑下拉刷新的
         *
         * @param child
         * @param top
         * @param dy
         * @return
         */
        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {

            int result = top - dy;

            // 拿到当前视图的偏移量
            int mScrollY = getScrollY();

            // 如果指示图定到头了并且是刷新的动作
            if ((mScrollY - dy) < 0 && !canChildScrollDown(contentView)) {
                isHeaderPulling = true;
            } else if ((mScrollY - dy) > 0 && !canChildScrollUp(contentView)) {
                isFootPulling = true;
            } else {
                isHeaderPulling = false;
                isFootPulling = false;
            }

            if ((dy > 0 && isHeaderPulling) || dy < 0 && isFootPulling) {
                //计算滑动出来的高度和头部的高度的比值
                float percent = ((Number) Math.abs(mScrollY)).floatValue() / (800);
                if (percent > 1f) {
                    percent = 1f;
                }
                // 此处是为了让滑动的变得越来越困难
                dy = (int) ((1 - (percent)) * dy);
            }

            if (isHeaderPulling && (mScrollY - dy) >= 0) {
                isHeaderPulling = false;
                isFootPulling = false;
            }

            if (isFootPulling && (mScrollY - dy) <= 0) {
                isHeaderPulling = false;
                isFootPulling = false;
            }

            if (isHeaderPulling == false && isFootPulling == false) {
                dy = mScrollY;
            }

            dispatchListener();

            scrollBy(0, -dy);

            return result;
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {

            if (headerView == null) {
                smothTo(0);
                return;
            }

            // 如果要刷新了
            if (!isFinishing && getScrollY() <= -headerView.getHeight()) {
                smothTo(-headerView.getHeight());
                if (!isHeaderFreshing) { // 如果没在刷新的,回调方法,保证只调用一次
                    if (iHeaderView != null) {
                        iHeaderView.onFreshing();
                    }
                    isHeaderFreshing = true;
                }
            } else {
                smothTo(0);
            }
        }


    });


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev) || true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {

        dragHelper.processTouchEvent(e);

        MotionEvent mEvent = MotionEvent.obtain(e);
        mEvent.offsetLocation(0, getScrollY());

        //拿到事件的动作
        int action = mEvent.getActionMasked();

        if (action == MotionEvent.ACTION_DOWN) { //按下的时候记录下位置
            isHeaderPulling = false;
            isFootPulling = false;
            isDispatchActionDown = false;
            isDispatchActionCancel = false;
            contentView.dispatchTouchEvent(mEvent);
            isDispatchActionDown = true;
        } else if (action == MotionEvent.ACTION_MOVE) {

            if (isHeaderPulling || isFootPulling) { // 如果正在pull header

                if (!isDispatchActionCancel) {
                    mEvent.setAction(MotionEvent.ACTION_CANCEL);
                    contentView.dispatchTouchEvent(mEvent);
                    mEvent.setAction(action);
                    isDispatchActionCancel = true;
                    isDispatchActionDown = false;
                }

            } else { // 如果不是的话事件传递给下面的孩子
                if (!isDispatchActionDown) { // 如果没有分发过按下的事件,就分发一个
                    mEvent.setAction(MotionEvent.ACTION_DOWN);
                    contentView.dispatchTouchEvent(mEvent);
                    mEvent.setAction(action);
                    isDispatchActionDown = true;
                    isDispatchActionCancel = false;
                } else {
                    contentView.dispatchTouchEvent(mEvent);
                }
            }

        } else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
            isHeaderPulling = false;
            isFootPulling = false;
            contentView.dispatchTouchEvent(mEvent);
            isDispatchActionDown = false;
            isDispatchActionCancel = false;
        }

        return true;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        // 获取孩子的个数
        int childCount = getChildCount();
        // 获取到其中的内容视图和头
        if (childCount == 1) {
            contentView = getChildAt(0);
        } else {
            headerView = getChildAt(0);
            contentView = getChildAt(1);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        int childCount = getChildCount();

        if (childCount == 1) {

            View contentView = getChildAt(0);
            contentView.layout(l, 0, r, b);

        } else if (childCount == 2) {

            View header = getChildAt(0);
            header.layout(l, -header.getMeasuredHeight(), r, 0);

            View contentView = getChildAt(1);
            contentView.layout(l, 0, r, b);

        } else if (childCount == 3) {

            View header = getChildAt(0);
            header.layout(l, -header.getMeasuredHeight(), r, 0);

            View contentView = getChildAt(1);
            contentView.layout(l, 0, r, b);

            View foot = getChildAt(2);
            foot.layout(l, b, r, b + foot.getMeasuredHeight());

        }


    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (getScrollY() >= 0) { // 表示header不可见了
            if (isHeaderFreshing) ;
        }
    }

    /**
     * 根据现在的滚动的dy,智能调用回调方法
     */
    private void dispatchListener() {

        if (headerView == null || iHeaderView == null) {
            return;
        }

        int mScrollY = getScrollY();

        if (mScrollY >= 0) { // 如果整个Header 已经不见了
            if (isHeaderFreshing) { // 如果此时是正在刷新的状态,需要取消
                iHeaderView.onCancelFresh();
                isHeaderFreshing = false;
            }
            return;
        }

        if (isFinishing) {
            return;
        }


        // 计算headerView 显示出来的比例

        // 是一个正数
        int headerHeight = headerView.getHeight();
        // 得到滚动的绝对值
        float mFloatScrollY = Math.abs(mScrollY + 0f);

        if (mFloatScrollY > headerHeight) { // 如果当前滚动的距离已经超过了Header的高度,说明已经是准备刷新的状态

            if (isHeaderFreshing) { // 如果已经是刷新的状态了
                return;
            }
            if (isHeaderPreFreshing) { // 如果已经是准备刷新的状态了
                return;
            }

            // 准备刷新肯定已经是headerView整个都出现了,所以调用一下显示100%状态的
            iHeaderView.onPull(1f);
            // 紧接着调用准备刷新
            iHeaderView.onPreRefresh();

            // 标记准备刷新
            isHeaderPreFreshing = true;

        } else { // 如果滚动的距离还没有超过Header的高度

            if (isHeaderFreshing) { // 如果已经是刷新的状态了
                return;
            }

            // 取消准备刷新的状态
            isHeaderPreFreshing = false;

            if (headerHeight <= 0) {
                return;
            }

            // 通知当前拖拽的程度
            iHeaderView.onPull(Math.min(1, mFloatScrollY / headerHeight));

        }


    }

    /**
     * 平滑的滚动到某个位置,这里针对竖直方向
     *
     * @param toValue 目标y
     */
    private void smothTo(int toValue) {
        ValueAnimator objectAnimator = //
                ofInt(getScrollY(), toValue)//
                        .setDuration(200);
        //设置更新数据的监听
        objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                scrollTo(0, value);
            }
        });

        objectAnimator.start();
    }

    private boolean canChildScrollDown(View mTarget) {
        return mTarget.canScrollVertically(-1);
    }

    private boolean canChildScrollUp(View mTarget) {
        return mTarget.canScrollVertically(1);
    }

    private IHeaderView iHeaderView;

    private boolean isFinishing;

    /**
     * end fresh
     */
    public void finishRefresh(boolean isSuccess) {

        if (!isHeaderFreshing) { // 如果已经被取消掉了
            return;
        }

        isHeaderPulling = false;
        isFootPulling = false;
        isDispatchActionDown = false;
        isDispatchActionCancel = false;

        isHeaderFreshing = false;
        isHeaderPreFreshing = false;

        if (iHeaderView != null) {
            if (isSuccess) {
                iHeaderView.onFinishRefresh();
            }else {
                iHeaderView.onRefreshFail();
            }
        }

        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                smothTo(0);
                isFinishing = false;
            }
        };

        isFinishing = true;

        handler.sendEmptyMessageDelayed(0, 800);

    }

    /**
     * set the headerView
     *
     * @param iHeaderView
     */
    public void setHeaderView(@NonNull IHeaderView iHeaderView) {

        this.iHeaderView = iHeaderView;

        addView(iHeaderView.getView(), 0);

    }

    /**
     * the listener of headerView
     */
    public interface IHeaderView {

        /**
         * get the Header View
         *
         * @return
         */
        View getView();

        /**
         * will be called when drag the header
         *
         * @param ratio the pull ratio of headerView, between [0,1]
         */
        void onPull(float ratio);

        /**
         * prepare refresh
         */
        void onPreRefresh();

        /**
         * freshing
         */
        void onFreshing();

        /**
         * cancel reFresh
         */
        void onCancelFresh();

        /**
         * finish reFresh
         */
        void onFinishRefresh();

        /**
         * reFresh fail
         */
        void onRefreshFail();

    }

}
