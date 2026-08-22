package com.shizux.app

import android.app.Activity
import android.os.Bundle
import android.widget.TextView
import android.graphics.Color

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tv = TextView(this)
        tv.text = "ShizuX Running OK"
        tv.setTextColor(Color.WHITE)
        tv.setBackgroundColor(Color.parseColor("#7C4DFF"))
        tv.textSize = 24f
        setContentView(tv)
    }
}
