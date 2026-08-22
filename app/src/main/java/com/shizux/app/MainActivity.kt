package com.shizux.app

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.view.ViewGroup
import android.graphics.Color

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val btn = Button(this)
        btn.text = "点我"
        btn.setOnClickListener {
            android.widget.Toast.makeText(this, "运行成功！", android.widget.Toast.LENGTH_LONG).show()
        }
        btn.setBackgroundColor(Color.parseColor("#7C4DFF"))
        setContentView(btn)
    }
}
