package com.xm.lib.test.ui.act

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.xm.lib.test.R
import android.content.Context.AUDIO_SERVICE
import android.content.Intent
import android.support.v4.content.ContextCompat.getSystemService
import android.media.AudioManager
import android.os.AsyncTask
import android.os.Handler
import android.os.Message
import java.net.URL


class PrimaryContentProvider : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_primary_content_provider)
    }
}
