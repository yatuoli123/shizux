package com.shizux.app
import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import rikka.shizuku.Shizuku

class MainActivity : Activity() {
    private lateinit var container: FrameLayout
    private lateinit var log: TextView

    override fun onCreate(saved: Bundle?) {
        super.onCreate(saved)
        requestWindowFeature(Window.FEATURE_NO_TITLE)

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(Color.WHITE)
            fitsSystemWindows = true
        }

        container = FrameLayout(this)
        container.addView(pageHome())
        container.addView(pageSettings())
        container.addView(pageTools())

        val nav = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            setBackgroundColor(Color.parseColor("#EEEEEE"))
            setPadding(0, 10, 0, 10)
            fitsSystemWindows = true
        }
        nav.addView(navItem("首页", 0))
        nav.addView(navItem("设置", 1))
        nav.addView(navItem("工具箱", 2))

        root.addView(container, LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, 0, 1f))
        root.addView(nav)

        setContentView(root)
        showPage(0)
    }

    private fun showPage(i: Int) {
        for (j in 0 until container.childCount) {
            container.getChildAt(j).visibility = if (j == i) View.VISIBLE else View.GONE
        }
    }

    private fun navItem(txt: String, idx: Int): TextView {
        return TextView(this).apply {
            text = txt
            textSize = 14f
            setTextColor(Color.parseColor("#7C4DFF"))
            gravity = Gravity.CENTER
            layoutParams = LinearLayout.LayoutParams(0,
                LinearLayout.LayoutParams.MATCH_PARENT, 1f)
            setOnClickListener { showPage(idx) }
        }
    }

    private fun pageHome(): LinearLayout {
        val v = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setBackgroundColor(Color.WHITE)
        }
        v.addView(TextView(this).apply {
            text = "ShizuX 主面板"
            textSize = 26f
            setTextColor(Color.parseColor("#7C4DFF"))
        })
        val btn = Button(this).apply {
            text = "读取已安装应用列表"
            textSize = 16f
            setTextColor(Color.WHITE)
            (background as android.graphics.drawable.GradientDrawable).apply {
                setColor(Color.parseColor("#7C4DFF"))
                cornerRadius = 40f
            }
        }
        val out = TextView(this).apply {
            textSize = 12f
            setTextColor(Color.DKGRAY)
        }
        btn.setOnClickListener {
            out.text = "正在读取..."
            Thread {
                val r = try {
                    Runtime.getRuntime().exec(arrayOf("sh","-c","pm list packages"))
                        .inputStream.bufferedReader().use { it.readText() }
                } catch (e: Exception) { "错误: ${e.message}" }
                runOnUiThread { out.text = r }
            }.start()
        }
        v.addView(btn, LinearLayout.LayoutParams(500, 80).also { it.topMargin = 50 })
        v.addView(out, LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, 0, 1f).also { it.topMargin = 30 })
        return v
    }

    private fun pageSettings(): LinearLayout {
        val v = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setBackgroundColor(Color.WHITE)
        }
        v.addView(TextView(this).apply {
            text = "设置页面"
            textSize = 24f
            setTextColor(Color.parseColor("#7C4DFF"))
        })
        return v
    }

    private fun pageTools(): LinearLayout {
        val v = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(24, 40, 24, 24)
            setBackgroundColor(Color.WHITE)
        }
        v.addView(TextView(this).apply {
            text = "工具箱"
            textSize = 26f
            setTextColor(Color.parseColor("#7C4DFF"))
        })
        v.addView(capsule("① 冻结全部应用", "pm list packages"))
        v.addView(capsule("② 查看内存信息", "cat /proc/meminfo"))
        v.addView(capsule("③ 清理缓存", "pm list packages | head"))
        log = TextView(this).apply {
            textSize = 13f
            setTextColor(Color.DKGRAY)
            setPadding(0, 20, 0, 0)
        }
        v.addView(ScrollView(this).apply {
            addView(log)
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 0, 1f)
        })
        return v
    }

    private fun capsule(t: String, cmd: String): Button {
        val b = Button(this)
        b.text = t
        b.textSize = 17f
        b.setTextColor(Color.WHITE)
        (b.background as android.graphics.drawable.GradientDrawable).apply {
            setColor(Color.parseColor("#7C4DFF"))
            cornerRadius = 40f
        }
        b.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, 70).apply {
            setMargins(0, 15, 0, 15)
        }
        b.setOnClickListener {
            val ok = Shizuku.checkSelfPermission() == android.content.pm.PackageManager.PERMISSION_GRANTED
            if (!ok) { log.text = "请先在 ShizuX 内点击授权按钮"; return@setOnClickListener }
            log.text = "执行中..."
            Thread {
                val r = try {
                    Runtime.getRuntime().exec(arrayOf("sh","-c",cmd))
                        .inputStream.bufferedReader().use { it.readText() }
                } catch (e: Exception) { "错误: ${e.message}" }
                runOnUiThread { log.text = r }
            }.start()
        }
        return b
    }
}
