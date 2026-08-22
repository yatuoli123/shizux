package com.shizux.app

import android.app.Activity
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.Window
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import rikka.shizuku.Shizuku

class MainActivity : Activity() {

    private val requestCode = 10001

    private val listener = object : Shizuku.OnRequestPermissionResultListener {
        override fun onRequestPermissionResult(code: Int, grantResult: Int) {
            if (code == requestCode) {
                val ok = grantResult == PackageManager.PERMISSION_GRANTED
                runOnUiThread {
                    Toast.makeText(this@MainActivity,
                        if (ok) "已授权，运行正常" else "你拒绝了授权",
                        Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)

        val root = LinearLayout(this)
        root.orientation = LinearLayout.VERTICAL
        root.gravity = Gravity.CENTER
        root.setBackgroundColor(Color.WHITE)

        val btn = Button(this)
        btn.textSize = 18f
        btn.setTextColor(Color.WHITE)
        btn.text = "点击授权 Shizuku"
        btn.setBackgroundResource(R.drawable.bg_capsule)
        btn.setPadding(80, 24, 80, 24)

        btn.setOnClickListener { handleClick() }

        root.addView(btn)
        setContentView(root)

        try {
            Shizuku.addRequestPermissionResultListener(listener)
        } catch (e: Exception) {
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            Shizuku.removeRequestPermissionResultListener(listener)
        } catch (e: Exception) {
        }
    }

    private fun handleClick() {
        try {
            if (!Shizuku.pingBinder()) {
                showDialog("未检测到 Shizuku", "请先安装并启动 Shizuku 应用，然后回到这里重试。")
                return
            }
            if (Shizuku.checkSelfPermission() == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "已授权，运行正常", Toast.LENGTH_LONG).show()
            } else {
                Shizuku.requestPermission(requestCode)
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Shizuku 错误: " + e.javaClass.simpleName, Toast.LENGTH_LONG).show()
        }
    }

    private fun showDialog(title: String, msg: String) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(msg)
            .setPositiveButton("好", null)
            .show()
    }
}
