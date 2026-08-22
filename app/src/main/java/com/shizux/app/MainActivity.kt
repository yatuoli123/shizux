import rikka.shizuku.Shizuku
package com.shizux.app

import android.app.Activity
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)

        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL
        layout.setBackgroundColor(Color.WHITE)

        val btn = Button(this)
        btn.setTextColor(Color.WHITE)
        btn.setBackgroundColor(Color.parseColor("#7C4DFF"))
        updateButton(btn)

        btn.setOnClickListener {
            handleClick(btn)
        }

        layout.addView(btn)
        setContentView(layout)
    }

    private fun updateButton(btn: Button) {
        btn.text = if (shizukuReady()) "已授权，点击测试" else "点击授权 Shizuku"
    }

    private fun shizukuReady(): Boolean {
        return try {
            Shizuku.pingBinder() &&
                    Shizuku.checkSelfPermission() == PackageManager.PERMISSION_GRANTED
        } catch (e: Exception) {
            false
        }
    }

    private fun handleClick(btn: Button) {
        try {
            if (!Shizuku.pingBinder()) {
                Toast.makeText(this, "请先打开 Shizuku 应用", Toast.LENGTH_LONG).show()
                return
            }
            if (shizukuReady()) {
                Toast.makeText(this, "已授权 ✓ 运行正常", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "弹出授权窗口，请授予", Toast.LENGTH_LONG).show()
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Shizuku 错误: " + e.javaClass.simpleName, Toast.LENGTH_LONG).show()
        }
    }
}
