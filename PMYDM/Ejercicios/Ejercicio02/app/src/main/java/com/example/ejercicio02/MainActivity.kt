package com.example.ejercicio02

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
import com.example.ejercicio02.ui.theme.Ejercicio02Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Ejercicio02Theme {

                var cantidad = 130

                var cant1 = cantidad/50
                var  cant2 = cantidad % 50 /10

                Log.d("Ejercicio02","Cantidad total : $cantidad")
                Log.d("Ejercicio02","Billetes de 50 : $cant1")
                Log.d("Ejercicio02","Billetes de 10 : $cant2")

            }
        }
    }
}
