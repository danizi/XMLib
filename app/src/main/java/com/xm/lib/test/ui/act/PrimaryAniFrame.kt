package com.xm.lib.test.ui.act

import android.graphics.drawable.AnimationDrawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.xm.lib.test.R
import android.graphics.drawable.Drawable



class PrimaryAniFrame : AppCompatActivity() {

    private var isAnimByCode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_primary_ani_frame)

        val gif = findViewById<ImageView>(R.id.gif)
        val animationDrawable = gif.background as AnimationDrawable
        animationDrawable.stop()
        animationDrawable.start()

        val animationDrawable2 = AnimationDrawable()
        for (i in 0..12) {
            val id = resources.getIdentifier("frame_0$i", "drawable", packageName)
            val drawable = resources.getDrawable(id)
            animationDrawable.addFrame(drawable, 100)
        }
        animationDrawable.stop()
        animationDrawable2.start()
        gif.setImageDrawable(animationDrawable2)
    }

}
