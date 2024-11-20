package com.example.examenmvvm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.examenmvvm.ui.theme.ExamenMVVMTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // inicializamos ViewModel
        val miViewModel: MyViewModel = MyViewModel()
        enableEdgeToEdge()
        setContent {
            ExamenMVVMTheme {
                // llamamos a la IU pasando el ViewModel
                IU(miViewModel)
            }
        }
    }
}