package com.move.widgetdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.move.widget.RelativeSizeTextView;

public class MainAct extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);
        RelativeSizeTextView tv = (RelativeSizeTextView) findViewById(R.id.tv);
        tv.setTagText("傻逼");
    }

    public void gotoIndicator(View v) {
        startActivity(new Intent(this, IndicatorAct.class));
    }

}
