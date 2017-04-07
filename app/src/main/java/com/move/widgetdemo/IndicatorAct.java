package com.move.widgetdemo;

import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.move.widget.XIndicator;

import java.util.ArrayList;
import java.util.List;

public class IndicatorAct extends AppCompatActivity {

    private int[] colors = new int[]{
            Color.DKGRAY,
            Color.GREEN,
            Color.GRAY,
            Color.BLUE,
            Color.CYAN,
            Color.LTGRAY,
            Color.MAGENTA,
            Color.YELLOW
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_indicator);

        ViewPager vp = (ViewPager) findViewById(R.id.vp);

        List<View> views = new ArrayList<View>();
        for (int i = 0; i < 8; i++) {
            View v = new View(this);
            v.setBackgroundColor(colors[i]);
            views.add(v);
        }

        vp.setAdapter(new ViewPagerViewAdapter(views));

        XIndicator indicator = (XIndicator) findViewById(R.id.indicator);
        indicator.setUpViewPager(vp);


    }
}
