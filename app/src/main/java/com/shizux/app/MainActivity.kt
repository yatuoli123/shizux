package com.shizux.app
import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView

class MainActivity : Activity() {

    private lateinit var pageHome: LinearLayout
    private lateinit var pageSettings: LinearLayout
    private lateinit var btnHome: Button
    private lateinit var btnSettings: Button

    override fun onCreate(b: Bundle?) {
        super.onCreate(b)
        requestWindowFeature(Window.FEATURE_NO_TITLE)

        val root = LinearLayout(this)
        root.orientation = LinearLayout.VERTICAL
        root.setBackgroundColor(Color.parseColor("#F5F5F5"))

        val content = LinearLayout(this)
        content.orientation = LinearLayout.VERTICAL
        content.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f)

        pageHome = buildPageHome()
        pageSettings = buildPageSettings()
        content.addView(pageHome)
        content.addView(pageSettings)

        val bottom = LinearLayout(this)
        bottom.orientation = LinearLayout.HORIZONTAL
        btnHome = makeNavButton("主页")
        btnSettings = makeNavButton("设置")
        btnHome.setOnClickListener { showPage(0) }
        btnSettings.setOnClickListener { showPage(1) }
        bottom.addView(btnHome)
        bottom.addView(btnSettings)

        // 避开系统底部手势导航条
        val resId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
        val navH = if (resId > 0) resources.getDimensionPixelSize(resId) else 0
        bottom.setPadding(0, 0, 0, navH)

        root.addView(content)
        root.addView(bottom)
        setContentView(root)
        showPage(0)
    }

    private fun showPage(index: Int) {
        pageHome.visibility = if (index == 0) View.VISIBLE else View.GONE
        pageSettings.visibility = if (index == 1) View.VISIBLE else View.GONE
        val active = Color.parseColor("#7C4DFF")
        btnHome.setTextColor(if (index == 0) active else Color.GRAY)
        btnSettings.setTextColor(if (index == 1) active else Color.GRAY)
    }

    private fun makeNavButton(label: String): Button {
        val b = Button(this)
        b.text = label
        b.setBackgroundColor(Color.TRANSPARENT)
        b.layoutParams = LinearLayout.LayoutParams(0, 80, 1f)
        return b
    }

    private fun buildPageHome(): LinearLayout {
        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL
        layout.gravity = Gravity.CENTER
        layout.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)

        val title = TextView(this)
        title.text = "ShizuX"
        title.textSize = 30f
        title.gravity = Gravity.CENTER
        title.setTextColor(Color.parseColor("#7C4DFF"))

        val hint = TextView(this)
        hint.text = "Unlock Android, Root-Free."
        hint.textSize = 14f
        hint.setTextColor(Color.GRAY)
        hint.gravity = Gravity.CENTER

        val btn = Button(this)
        btn.text = "读取应用列表"
        btn.setTextColor(Color.WHITE)
        btn.textSize = 16f
        btn.setBackgroundResource(R.drawable.bg_capsule)
        btn.layoutParams = LinearLayout.LayoutParams(220, 96).apply { gravity = Gravity.CENTER }

        val tv = TextView(this)
        tv.setTextColor(Color.DKGRAY)
        tv.textSize = 12f
        tv.setPadding(20, 12, 20, 12)

        btn.setOnClickListener {
            val granted = rikka.shizuku.Shizuku.checkSelfPermission() == android.content.pm.PackageManager.PERMISSION_GRANTED
            val ok = rikka.shizuku.Shizuku.pingBinder() && granted
            tv.text = if (ok) {
                try {
                    Runtime.getRuntime().exec(arrayOf("sh", "-c", "pm list packages"))
                        .inputStream.bufferedReader().readText()
                } catch (e: Exception) { "执行失败" }
            } else "未授权 Shizuku"
        }

        layout.addView(title)
        layout.addView(hint)
        layout.addView(btn)
        layout.addView(tv)
        return layout
    }

    private fun buildPageSettings(): LinearLayout {
        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL
        layout.gravity = Gravity.CENTER
        layout.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)

        val t = TextView(this)
        t.text = "设置页"
        t.textSize = 24f
        t.setTextColor(Color.parseColor("#7C4DFF"))
        t.gravity = Gravity.CENTER

        val info = TextView(this)
        info.text = "在这里可以配置玩法。\n功能开发中…"
        info.textSize = 14f
        info.setTextColor(Color.GRAY)
        info.gravity = Gravity.CENTER

        layout.addView(t)
        layout.addView(info)
        return layout
    }
}
