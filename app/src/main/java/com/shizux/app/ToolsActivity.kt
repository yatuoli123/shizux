package com.shizux.app
import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import rikka.shizuku.Shizuku

class ToolsActivity : Activity() {

    private lateinit var log: TextView

    override fun onCreate(b: Bundle?) {
        super.onCreate(b)
        requestWindowFeature(Window.FEATURE_NO_TITLE)

        val root = LinearLayout(this)
        root.orientation = LinearLayout.VERTICAL
        root.setPadding(24, 24, 24, 24)

        val title = TextView(this)
        title.text = "工具箱"
        title.textSize = 26f
        title.setTextColor(Color.parseColor("#7C4DFF"))

        val freezeBtn = makeCapsuleButton("① 冻结全部应用")
        val memBtn = makeCapsuleButton("② 查看内存信息")
        val clearBtn = makeCapsuleButton("③ 清理缓存")

        freezeBtn.setOnClickListener { runCmd("pm list packages") }
        memBtn.setOnClickListener { runCmd("cat /proc/meminfo") }
        clearBtn.setOnClickListener { runCmd("pm list packages | head") }

        log = TextView(this)
        log.setTextColor(Color.DKGRAY)
        log.textSize = 12f
        log.setPadding(0, 12, 0, 12)

        val scroll = ScrollView(this)
        scroll.addView(log)

        root.addView(title)
        root.addView(freezeBtn)
        root.addView(memBtn)
        root.addView(clearBtn)
        root.addView(scroll, LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f))

        setContentView(root)
        log.setText("点击上方按钮开始\n\n（Shizuku 授权后才会返回结果）")
    }

    private fun makeCapsuleButton(text: String): Button {
        val btn = Button(this)
        btn.text = text
        btn.textSize = 16f
        btn.setTextColor(Color.WHITE)
        btn.setBackgroundResource(R.drawable.bg_capsule)
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, 60)
        params.setMargins(0, 12, 0, 12)
        btn.layoutParams = params
        return btn
    }

    private fun runCmd(cmd: String) {
        val ok = Shizuku.pingBinder() && Shizuku.checkSelfPermission() == android.content.pm.PackageManager.PERMISSION_GRANTED
        if (!ok) {
            log.text = "尚未授权 Shizuku"
            return
        }
        log.text = "执行中…"
        Thread {
            val out = try {
                Runtime.getRuntime().exec(arrayOf("sh", "-c", cmd)).inputStream.bufferedReader().readText()
            } catch (e: Exception) {
                "错误: ${e.message}"
            }
            runOnUiThread {
                log.text = out
            }
        }.start()
    }
}
