package com.xm.lib.media.test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.xm.lib.media.R;
import com.xm.lib.media.base.XmVideoView;

public class MainActivity extends AppCompatActivity {
    ViewGroup view;
    XmVideoView xmVideoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //设置窗体为没有标题的模式
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        view = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.activity_main, null, false);
        setContentView(view);

        xmVideoView = findViewById(R.id.video);
        findViewById(R.id.btn_media2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xmVideoView.test();
            }
        });
        findViewById(R.id.btn_set).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xmVideoView.set();
            }
        });
    }
}
