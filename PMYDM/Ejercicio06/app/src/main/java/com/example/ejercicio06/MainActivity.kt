package com.example.ejercicio06

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.ejercicio06.ui.theme.Ejercicio06Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Ejercicio06Theme {
                var nota : Int = 6
                when{
                    nota in 0..4 -> Log.d("Ejercicio06","Suspenso")
                    nota in 5..6 -> Log.d("Ejercicio06","Bien")
                    nota in 7..8 -> Log.d("Ejercicio06","Notable")
                    nota in 9..10 -> Log.d("Ejercicio06","Sobresaliente")

                }
            }
        }
    }
}

