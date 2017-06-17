package com.move.widgetdemo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

import com.move.widget.OverScrollerView;
import com.move.widget.RelativeSizeTextView;

public class MainAct extends AppCompatActivity {

    private Handler h = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            view_over_scroll.finishRefresh(true);

        }
    };

    private OverScrollerView view_over_scroll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);
        RelativeSizeTextView tv = (RelativeSizeTextView) findViewById(R.id.tv);
        tv.setTagText("傻逼");

        view_over_scroll = (OverScrollerView) findViewById(R.id.view_over_scroll);

        HeaderView headerView = new HeaderView(this);

        view_over_scroll.setHeaderView(headerView);

        headerView.setOnListener(new HeaderView.OnListener() {
            @Override
            public void onFresh() {
                h.sendEmptyMessageDelayed(0, 2000);
            }
        });

    }

}
