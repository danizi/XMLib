package com.xm.lib.media.test;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.Window;

import com.xm.lib.media.R;
import com.xm.lib.media.attachment.AttachmentComplete;
import com.xm.lib.media.attachment.AttachmentGesture;
import com.xm.lib.media.attachment.AttachmentPre;
import com.xm.lib.media.attachment.control.AttachmentControl;
import com.xm.lib.media.base.XmVideoView;

public class MainActivity extends AppCompatActivity {
    ViewGroup view;
    XmVideoView xmVideoView;
//    RxPermissions rxPermissions; // where this is an Activity or Fragment instance

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //设置窗体为没有标题的模式
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        super.onCreate(savedInstanceState);
        view = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.activity_main, null, false);
        setContentView(view);


        //绑定数据
        xmVideoView = findViewById(R.id.video);
        String preUrl = "https://img-blog.csdn.net/20160413112832792?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQv/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/Center";
        AttachmentPre attachmentPre = new AttachmentPre(this, preUrl);
        attachmentPre.setUrl("http://hls.videocc.net/26de49f8c2/9/26de49f8c273bbc8f6812d1422a11b39_2.m3u8");
        AttachmentControl attachmentControl = new AttachmentControl(this);
        AttachmentGesture attachmentGesture = new AttachmentGesture(this);
        AttachmentComplete attachmentComplete = new AttachmentComplete(this);

        xmVideoView.bindAttachmentView(attachmentPre);      //预览附着页面
        xmVideoView.bindAttachmentView(attachmentControl);  //控制器附着页面
        xmVideoView.bindAttachmentView(attachmentGesture);  //手势附着页面(调节亮度和音量)
        xmVideoView.bindAttachmentView(attachmentComplete); //播放完成附着页面

        xmVideoView.addPlayerObserver(attachmentPre);
        xmVideoView.addGestureObserver(attachmentPre);
        xmVideoView.addPhoneStateObserver(attachmentPre);

        xmVideoView.addPlayerObserver(attachmentControl);
        xmVideoView.addGestureObserver(attachmentControl);
        xmVideoView.addPhoneStateObserver(attachmentControl);

        xmVideoView.addPlayerObserver(attachmentGesture);
        xmVideoView.addGestureObserver(attachmentGesture);
        xmVideoView.addPhoneStateObserver(attachmentGesture);

        xmVideoView.addPlayerObserver(attachmentComplete);
        xmVideoView.addGestureObserver(attachmentComplete);
        xmVideoView.addPhoneStateObserver(attachmentComplete);

        //绑定播放服务
    }

    @Override
    protected void onPause() {
        super.onPause();
        xmVideoView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        xmVideoView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        xmVideoView.onDestroy();
    }
}
