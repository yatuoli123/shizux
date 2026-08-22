com.shizux.app
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

private lateinit var root: LinearLayout
    private lateinit var log: TextView
    private var currentPage = 0

override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)

root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(Color.WHITE)
            fitsSystemWindows = true
        }

val container = FrameLayout(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 0, 1f
            )
        }

val homePage = homePage()
        val settingsPage = settingsPage()
        val toolsPage = toolsPage()

container.addView(homePage)
        container.addView(settingsPage)
        container.addView(toolsPage)

val nav = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            setBackgroundColor(Color.parseColor("#E0E0E0"))
            setPadding(0, 8, 0, 8)
            fitsSystemWindows = true
        }

nav.addView(navBtn("首页", 0))
        nav.addView(navBtn("设置", 1))
        nav.addView(navBtn("工具箱", 2))

root.addView(container)
        root.addView(nav)

setContentView(root)
        showPage(0)
    }

private fun showPage(index: Int) {
        currentPage = index
        val container = root.getChildAt(0) as FrameLayout
        for (i in 0 until container.childCount) {
            container.getChildAt(i).visibility = if (i == index) View.VISIBLE else View.GONE
        }
    }

private fun navBtn(name: String, idx: Int): TextView {
        val btn = TextView(this).apply {
            text = name
            textSize = 14f
            setTextColor(Color.parseColor("#7C4DFF"))
            gravity = Gravity.CENTER
            setPadding(0, 12, 0, 12)
            layoutParams = LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.MATCH_PARENT, 1f
            )
            setOnClickListener { showPage(idx) }
        }
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
            layoutParams = LinearLayout.LayoutParams(500, 80).also { it.topMargin = 40 }
        }
        val out = TextView(this).apply {
            textSize = 12f
            setTextColor(Color.DKGRAY)
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 0, 1f
            ).also { it.topMargin = 30 }
        }
        btn.setOnClickListener {
            out.text = "正在读取...\n"
            Thread {
                val result = try {
                    Runtime.getRuntime().exec(arrayOf("sh", "-c", "pm list packages"))
                        .inputStream.bufferedReader().use { it.readText() }
                } catch (e: Exception) { "错误: {e.message}" }
                runOnUiThread { out.text = result }
            }.start()
        }
        v.addView(t)
        v.addView(btn)
        v.addView(out)
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
        val btnP = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, 70
        ).apply { setMargins(0, 15, 0, 15) }

        fun Button.setup(text: String, cmd: String) {
            this.text = text
            this.textSize = 17f
            this.setTextColor(Color.WHITE)
            this.setBackgroundResource(R.drawable.bg_capsule)
            this.layoutParams = btnP
            this.setOnClickListener {
                val ok = Shizuku.checkSelfPermission() == android.content.pm.PackageManager.PERMISSION_GRANTED
                if (!ok) {
                    log.text = "请先在 ShizuX 内点击授权按钮"
                    return@setOnClickListener
                }
                log.text = "执行中..."
                Thread {
                    val result = try {
                        Runtime.getRuntime().exec(arrayOf("sh", "-c", cmd))
                            .inputStream.bufferedReader().use { it.readText() }
                    } catch (e: Exception) { "错误:{e.message}" }
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
}
