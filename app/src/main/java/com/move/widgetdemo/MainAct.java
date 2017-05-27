package com.move.widgetdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.move.widget.RelativeSizeTextView;
import com.move.widget.StateView1;

public class MainAct extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);
        RelativeSizeTextView tv = (RelativeSizeTextView) findViewById(R.id.tv);
        tv.setTagText("傻逼");

    }

    public void gotoIndicator(View v) {
        StateView1 sv1 = (StateView1) findViewById(R.id.sv1);
        //sv1.disPlay(new String[]{"小金子", "小金子", "小金子"}, 0);
        sv1.setProgress(1);
        // startActivity(new Intent(this, IndicatorAct.class));
    }

}
