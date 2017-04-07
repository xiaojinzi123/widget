package com.move.widgetdemo;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by cxj on 2016/7/13.
 * ViewPager+View的适配器
 */
public class ViewPagerViewAdapter extends PagerAdapter {

    /**
     * 要显示的集合的集合
     */
    private List<View> viewLists;

    private List<String> titles;

    /**
     * 是否循环播放
     */
    private boolean isLoop;

    public void setTitles(List<String> titles) {
        this.titles = titles;
    }

    public ViewPagerViewAdapter(List<View> lists) {
        viewLists = lists;
    }

    public ViewPagerViewAdapter(List<View> lists, boolean isLoop) {
        viewLists = lists;
        this.isLoop = isLoop;
    }

    @Override
    public int getCount() { //获得size
        if (isLoop) { //如果循环
            return viewLists.size() == 0 ? 0 : Integer.MAX_VALUE;
        } else {
            return viewLists.size();
        }
    }

    @Override
    public boolean isViewFromObject(View view, Object obj) {
        return view == obj;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {           //销毁一个条目
        if (isLoop) { //如果循环
            position = position % viewLists.size();
        }
        ((ViewPager) container).removeView(viewLists.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {      //实例化Item
        if (isLoop) { //如果循环
            position = position % viewLists.size();
        }
        View view = viewLists.get(position);
        if (view.getParent() != null) {
            ((ViewGroup)view.getParent()).removeView(view);
        }
        ((ViewPager) container).addView(view, 0);
        return view;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (titles != null) {
            if (position != -1 && position < titles.size()) {
                return titles.get(position);
            }
        }
        return super.getPageTitle(position);
    }
}
