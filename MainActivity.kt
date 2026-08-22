package com.shizux.app

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            setContent { ShizuXScreen() }
        } catch (e: Throwable) {
            // 兜底：避免白屏，吐司提示
            Toast.makeText(this, "初始化失败: " + e.message, Toast.LENGTH_LONG).show()
            finish()
        }
    }
}

@Composable
fun ShizuXScreen() {
    MaterialTheme {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("ShizuX", style = MaterialTheme.typography.headlineMedium)
            Text("Unlock Android, Root-Free.")
        }
    }
}
