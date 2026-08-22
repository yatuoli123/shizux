package com.shizux.app
import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.Window
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import rikka.shizuku.Shizuku

class MainActivity : Activity() {
    override fun onCreate(s: Bundle?) {
        super.onCreate(s)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setBackgroundColor(Color.WHITE)
            setPadding(40, 80, 40, 40)
        }
        val title = TextView(this).apply {
            text = "ShizuX OK"
            textSize = 32f
            setTextColor(Color.parseColor("#7C4DFF"))
            gravity = Gravity.CENTER
        }
        val btn = Button(this).apply {
            text = "读取应用列表"
            textSize = 18f
            setTextColor(Color.WHITE)
            background = GradientDrawable().apply {
                setColor(Color.parseColor("#7C4DFF"))
                cornerRadius = 60f
            }
        }
        val out = TextView(this).apply {
            textSize = 12f
            setTextColor(Color.DKGRAY)
            setPadding(0, 30, 0, 0)
        }
        btn.setOnClickListener {
            val ok = Shizuku.checkSelfPermission() == android.content.pm.PackageManager.PERMISSION_GRANTED
            if (!ok) { out.text = "未授权"; return@setOnClickListener }
            out.text = "读取中..."
            Thread {
                val r = try {
                    Runtime.getRuntime().exec(arrayOf("sh","-c","pm list packages"))
                        .inputStream.bufferedReader().use { it.readText() }
                } catch (e: Exception) { "错误: ${e.message}" }
                runOnUiThread { out.text = r }
            }.start()
        }
        root.addView(title)
        root.addView(btn, LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT, 80).also { it.topMargin = 50 })
        val scroll = ScrollView(this)
        scroll.addView(out)
        root.addView(scroll, LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, 0, 1f))
        setContentView(root)
    }
}
