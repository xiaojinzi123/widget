package com.move.widgetdemo;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.move.widget.OverScrollerView;

/**
 * Created by cxj on 2017/6/16.
 */
public class HeaderView extends FrameLayout implements OverScrollerView.IHeaderView {

    public HeaderView(Context context) {
        this(context, null);
    }

    public HeaderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
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

    private TextView tv;

    @Override
    public View getView() {

        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        setLayoutParams(lp);

        View header = View.inflate(getContext(), R.layout.header, null);
        tv = (TextView) header.findViewById(R.id.tv);
        addView(header);
        return this;
    }

    @Override
    public void onPull(float ratio) {
        tv.setText("ratio = " + ratio);
    }

    @Override
    public void onPreRefresh() {
        tv.setText("onPreRefresh");
        System.out.println("--------------------准备刷新");
    }

    @Override
    public void onFreshing() {
        tv.setText("onFreshing");
        if (mOnListener != null) {
            mOnListener.onFresh();
        }
        System.out.println("--------------------正在刷新");
    }

    @Override
    public void onCancelFresh() {
        tv.setText("取消了");
        System.out.println("--------------------被取消了");
    }

    @Override
    public void onFinishRefresh() {
        tv.setText("刷新成功");
        System.out.println("--------------------刷新成功");
    }

    @Override
    public void onRefreshFail() {
        tv.setText("刷新失败");
        System.out.println("--------------------刷新失败");
    }

    private OnListener mOnListener;

    public void setOnListener(OnListener l) {
        this.mOnListener = l;
    }

    public interface OnListener {
        void onFresh();
    }

}
