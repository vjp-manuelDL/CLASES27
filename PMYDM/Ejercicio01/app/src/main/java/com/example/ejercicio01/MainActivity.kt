package com.example.ejercicio01

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
import com.example.ejercicio01.ui.theme.Ejercicio01Theme




class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Ejercicio01Theme {

                var asignatura = "Matematicas"

                var nota1 = 8.0
                var nota2 = 9.0
                var media = (nota1 + nota2)/2

                Log.d("Ejercicio01","Asignatura: $asignatura")
                Log.d("Ejercicio01","Nota1: $nota1")
                Log.d("Ejercicio01","Nota2: $nota2")
                Log.d("Ejercicio01","Media: $media")







            }
        }
    }
}

