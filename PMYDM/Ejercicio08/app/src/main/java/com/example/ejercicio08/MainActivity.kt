package com.example.ejercicio08

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
import com.example.ejercicio08.ui.theme.Ejercicio08Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Ejercicio08Theme {
                calcularPrimeros()
            }
        }
    }

    fun calcularPrimeros() {
        var posicion = 0
        var numero: Int = 1

        // Tu bucle do-while
        do {
            if (esPrimo(numero)) {
                Log.d("Ejercicio08", "[$posicion] -> ${numero}")
                posicion++

            }
            numero++


        } while (posicion <= 80)


    }

    // Tu función de comprobación separada correctamente
    fun esPrimo(n: Int): Boolean {
        // Adaptado para que el 1 cuente como primo según el ejemplo del PDF
        if (n == 1) return true
        if (n <= 0) return false
        if (n == 2) return true
        if (n % 2 == 0) return false  // Descartamos los pares

        // Solo probamos impares hasta la raíz cuadrada de n
        var i = 3
        while (i * i <= n) {
            if (n % i == 0) return false
            i += 2
        }
        return true
    }
}