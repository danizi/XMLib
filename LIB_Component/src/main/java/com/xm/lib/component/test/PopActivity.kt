package com.xm.lib.component.test

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.xm.lib.component.R
import android.widget.LinearLayout



class PopActivity : AppCompatActivity() {

    private class ViewHolder private constructor(val btnLeft: Button, val btnRight: Button, val btnTop: Button, val btnBottom: Button) {
        companion object {

            fun create(rootView: AppCompatActivity): ViewHolder {
                val btnLeft = rootView.findViewById<View>(R.id.btn_left) as Button
                val btnRight = rootView.findViewById<View>(R.id.btn_right) as Button
                val btnTop = rootView.findViewById<View>(R.id.btn_top) as Button
                val btnBottom = rootView.findViewById<View>(R.id.btn_bottom) as Button
                return ViewHolder(btnLeft, btnRight, btnTop, btnBottom)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pop)
    }
}
