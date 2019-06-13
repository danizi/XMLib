package com.xm.lib.component.test

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import com.xm.lib.component.R
import android.widget.EditText
import com.xm.lib.component.Xm7DaySignView


class SignViewActivity : AppCompatActivity() {

    private var ui: ViewHolder? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_view)
        if (ui == null) {
            ui = ViewHolder.create(this)
        }
        ui?.btnAdd?.setOnClickListener {
            ui?.viewSign?.addDay()
        }
        ui?.btnDel?.setOnClickListener {
            ui?.viewSign?.delDay()
        }
        var et = ""
        ui?.etDay?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                et = s.toString()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
        ui?.btnEtDay?.setOnClickListener {
            ui?.viewSign?.setSelect(et.toInt(), false)
        }
    }


    private class ViewHolder private constructor(val viewSign: Xm7DaySignView, val btnDel: Button, val btnAdd: Button, val etDay: EditText, val btnEtDay: Button) {
        companion object {

            fun create(rootView: AppCompatActivity): ViewHolder {
                val viewSign = rootView.findViewById<View>(R.id.view_sign) as Xm7DaySignView
                val btnDel = rootView.findViewById<View>(R.id.btn_del) as Button
                val btnAdd = rootView.findViewById<View>(R.id.btn_add) as Button
                val etDay = rootView.findViewById<View>(R.id.et_day) as EditText
                val btnEtDay = rootView.findViewById<View>(R.id.btn_et_day) as Button
                return ViewHolder(viewSign, btnDel, btnAdd, etDay, btnEtDay)
            }
        }
    }

}
