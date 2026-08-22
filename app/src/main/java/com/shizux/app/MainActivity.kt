package com.shizux.app

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import android.graphics.Color

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 双保险：代码里也隐藏标题栏
        requestWindowFeature(android.view.Window.FEATURE_NO_TITLE)

        val btn = Button(this)
        btn.text = "点我"
        btn.setBackgroundColor(Color.parseColor("#7C4DFF"))
        btn.setOnClickListener {
            Toast.makeText(this, "运行成功！", Toast.LENGTH_LONG).show()
        }
        setContentView(btn)
    }
}
