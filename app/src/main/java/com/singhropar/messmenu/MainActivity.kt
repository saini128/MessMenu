package com.singhropar.messmenu

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxSize
import com.singhropar.messmenu.ui.screens.MessMenuScreen
import com.singhropar.messmenu.ui.theme.MessMenuTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MessMenuTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    MessMenuScreen()
                }
            }
        }
    }
}
