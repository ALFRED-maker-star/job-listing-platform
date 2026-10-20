package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.ui.WorkFindMainApp
import com.example.ui.theme.BackgroundSlate
import com.example.ui.theme.WorkFindTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WorkFindTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = BackgroundSlate
                ) {
                    WorkFindMainApp()
                }
            }
        }
    }
}
