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
    private lateinit var log: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

        val readBtn = Button(this).apply {
            text = "读取应用列表"
            textSize = 18f
            setTextColor(Color.WHITE)
            background = GradientDrawable().apply {
                setColor(Color.parseColor("#7C4DFF"))
                cornerRadius = 60f
            }
        }

        val freezeBtn = Button(this).apply {
            text = "① 冻结全部应用"
            textSize = 17f
            setTextColor(Color.WHITE)
            background = GradientDrawable().apply {
                setColor(Color.parseColor("#7C4DFF"))
                cornerRadius = 60f
            }
        }

        val memBtn = Button(this).apply {
            text = "② 查看内存信息"
            textSize = 17f
            setTextColor(Color.WHITE)
            background = GradientDrawable().apply {
                setColor(Color.parseColor("#7C4DFF"))
                cornerRadius = 60f
            }
        }

        val clearBtn = Button(this).apply {
            text = "③ 清理缓存"
            textSize = 17f
            setTextColor(Color.WHITE)
            background = GradientDrawable().apply {
                setColor(Color.parseColor("#7C4DFF"))
                cornerRadius = 60f
            }
        }

        log = TextView(this).apply {
            textSize = 12f
            setTextColor(Color.DKGRAY)
            setPadding(0, 30, 0, 0)
        }

        val scroll = ScrollView(this)
        scroll.addView(log)

        root.addView(title)
        root.addView(readBtn, LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT, 80).also { it.topMargin = 50 })
        root.addView(freezeBtn)
        root.addView(memBtn)
        root.addView(clearBtn)
        root.addView(scroll, LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, 0, 1f))

        setContentView(root)

        // 按钮点击逻辑
        readBtn.setOnClickListener { runCmd("pm list packages") }
        freezeBtn.setOnClickListener { runCmd("pm list packages") }
        memBtn.setOnClickListener { runCmd("cat /proc/meminfo") }
        clearBtn.setOnClickListener { runCmd("pm list packages | head") }
    }

    private fun runCmd(cmd: String) {
        val ok = Shizuku.checkSelfPermission() == android.content.pm.PackageManager.PERMISSION_GRANTED
        if (!ok) {
            log.text = "请先在 ShizuX 内点击授权按钮"
            return
        }
        log.text = "执行中..."
        Thread {
            val result = try {
                Runtime.getRuntime().exec(arrayOf("sh", "-c", cmd))
                    .inputStream.bufferedReader().use { it.readText() }
            } catch (e: Exception) {
                "错误: ${e.message}"
            }
            runOnUiThread {
                log.text = result
            }
        }.start()
    }
}
