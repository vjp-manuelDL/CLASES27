package com.example.ejercicio07

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
import com.example.ejercicio07.ui.theme.Ejercicio07Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Ejercicio07Theme {
                var opcion: Int = 3

                var num1: Int = 10
                var num2: Int = 5
                var resultado : Int = 0
                when {
                    opcion == 1 -> resultado = sumarNumeros(num1, num2)
                    opcion == 2 -> resultado = restarNumeros(num1, num2)
                    opcion == 3 -> resultado = multiNumeros(num1, num2)
                    opcion == 4 -> resultado = divNumeros(num1, num2)
                }




                Log.d("Ejercicio07", "Resultado: $resultado")
            }


        }

    }

    fun sumarNumeros(a: Int, b: Int): Int {
        return a + b
    }

    fun restarNumeros(a: Int, b: Int): Int {
        return a - b
    }
    fun multiNumeros(a: Int, b: Int): Int {
        return a * b
    }

    fun divNumeros(a: Int, b: Int): Int {
        return a / b
    }
}


