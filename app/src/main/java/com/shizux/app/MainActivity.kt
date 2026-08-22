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

  private var currentPage = 0
  private lateinit var log: TextView

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    requestWindowFeature(Window.FEATURE_NO_TITLE)

    val root = LinearLayout(this).apply {
      orientation = LinearLayout.VERTICAL
      setBackgroundColor(Color.WHITE)
      fitsSystemWindows = true
    }

    val container = FrameLayout(this).apply {
      layoutParams = LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.MATCH_PARENT, 0, 1f
      )
    }

    val nav = LinearLayout(this).apply {
      orientation = LinearLayout.HORIZONTAL
      setBackgroundColor(Color.parseColor("#E0E0E0"))
      setPadding(0, 8, 0, 8)
      fitsSystemWindows = true
    }

    val homePage = homePage()
    val settingsPage = settingsPage()
    val toolsPage = toolsPage()

    container.addView(homePage)
    container.addView(settingsPage)
    container.addView(toolsPage)

    showPage(0)

    nav.addView(navBtn("首页", 0, homePage))
    nav.addView(navBtn("设置", 1, settingsPage))
    nav.addView(navBtn("工具箱", 2, toolsPage))

    root.addView(container)
    root.addView(nav)

    setContentView(root)
  }

  private fun showPage(index: Int) {
    currentPage = index
    val container = (root as LinearLayout).getChildAt(0) as FrameLayout
    for (i in 0 until container.childCount) {
      container.getChildAt(i).visibility = if (i == index) View.VISIBLE else View.GONE
    }
  }

  private fun navBtn(name: String, idx: Int, view: View): View {
    val btn = TextView(this)
    btn.text = name
    btn.textSize = 14f
    btn.setTextColor(Color.parseColor("#7C4DFF"))
    btn.gravity = Gravity.CENTER
    btn.setPadding(0, 12, 0, 12)
    btn.layoutParams = LinearLayout.LayoutParams(
      LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f
    )
    btn.setOnClickListener { showPage(idx) }
    return btn
  }

  private fun homePage(): LinearLayout {
    val v = LinearLayout(this).apply {
      orientation = LinearLayout.VERTICAL
      gravity = Gravity.CENTER
      setBackgroundColor(Color.WHITE)
    }
    val t = TextView(this).apply {
      text = "ShizuX 主面板"
      textSize = 24f
      setTextColor(Color.parseColor("#7C4DFF"))
    }
    val btn = Button(this).apply {
      text = "读取已安装应用列表"
      textSize = 16f
      setTextColor(Color.WHITE)
      setBackgroundResource(R.drawable.bg_capsule)
    }
    val out = TextView(this).apply { textSize = 12f; setTextColor(Color.DKGRAY) }
    btn.setOnClickListener {
      out.text = "正在读取...\n"
      Thread {
        val result = try {
          val p = Runtime.getRuntime().exec(arrayOf("sh", "-c", "pm list packages"))
          p.inputStream.bufferedReader().use { it.readText() }
        } catch (e: Exception) { "错误: ${e.message}" }
        runOnUiThread { out.text = result }
      }.start()
    }
    v.addView(t)
    v.addView(btn, FrameLayout.LayoutParams(500, 80).also { it.topMargin = 40 })
    v.addView(out, LinearLayout.LayoutParams(
      LinearLayout.LayoutParams.MATCH_PARENT, 0, 1f).also { it.topMargin = 30 })
    return v
  }

  private fun settingsPage(): LinearLayout {
    val v = LinearLayout(this).apply {
      orientation = LinearLayout.VERTICAL
      gravity = Gravity.CENTER
      setBackgroundColor(Color.WHITE)
    }
    v.addView(TextView(this).apply {
      text = "设置页面"
      textSize = 22f
      setTextColor(Color.parseColor("#7C4DFF"))
    })
    return v
  }

  private fun toolsPage(): LinearLayout {
    val v = LinearLayout(this).apply {
      orientation = LinearLayout.VERTICAL
      setPadding(20, 40, 20, 20)
      setBackgroundColor(Color.WHITE)
    }
    v.addView(TextView(this).apply {
      text = "工具箱"
      textSize = 26f
      setTextColor(Color.parseColor("#7C4DFF"))
    })
    fun Button.setup(text: String, cmd: String) {
      this.text = text
      this.textSize = 17f
      this.setTextColor(Color.WHITE)
      this.setBackgroundResource(R.drawable.bg_capsule)
      val p = LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.MATCH_PARENT, 70
      ).apply { setMargins(0, 15, 0, 15) }
      this.layoutParams = p
      this.setOnClickListener {
        val ok = Shizuku.checkSelfPermission() == android.content.pm.PackageManager.PERMISSION_GRANTED
        if (!ok) {
          log.text = "请先在 ShizuX 内点击授权按钮"
          return@setOnClickListener
        }
        log.text = "执行中..."
        Thread {
          val result = try {
            Runtime.getRuntime().exec(arrayOf("sh", "-c", cmd)).inputStream.bufferedReader().use { it.readText() }
          } catch (e: Exception) { "错误: ${e.message}" }
          runOnUiThread { log.text = result }
        }.start()
      }
    }
    v.addView(Button(this).apply { setup("① 冻结全部应用", "pm list packages") })
    v.addView(Button(this).apply { setup("② 查看内存信息", "cat /proc/meminfo") })
    v.addView(Button(this).apply { setup("③ 清理缓存", "pm list packages | head") })
    log = TextView(this).apply {
      textSize = 14f
      setTextColor(Color.DKGRAY)
      setPadding(0, 20, 0, 20)
    }
    v.addView(ScrollView(this).apply {
      addView(log)
      layoutParams = LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.MATCH_PARENT, 0, 1f
      )
    })
    return v
  }

  companion object {
    lateinit var root: LinearLayout
  }
}
