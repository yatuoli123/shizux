package com.shizux.app

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import androidx.activity.ComponentActivity

class ShizuMainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)

        val root = TextView(this)
        root.text = "ShizuX - Shizuku 引导"
        root.gravity = Gravity.CENTER
        root.setTextColor(Color.WHITE)
        root.textSize = 24f
        root.setBackgroundColor(Color.parseColor("#7C4DFF"))
        root.setOnClickListener {
            if (ShizukuHelper.checkPermission()) {
                Toast.makeText(this, "已授权", Toast.LENGTH_LONG).show()
            } else {
                ShizukuHelper.requestPermission(this)
            }
        }
        setContentView(root)
    }
}

object ShizukuHelper {
    fun checkPermission(): Boolean = !isNullOrEmpty()


}
