package com.move.widgetdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.move.widget.RelativeSizeTextView;

public class MainAct extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);
        RelativeSizeTextView tv = (RelativeSizeTextView) findViewById(R.id.tv);
        tv.setTagText("傻逼");

    }

}
