package com.example.ejercicio10

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
import com.example.ejercicio10.ui.theme.Ejercicio10Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val precios: ArrayList<Double> = arrayListOf(8.50, 15.0, 4.50, 20.0, 12.0, 7.0)
        setContent {
            Ejercicio10Theme {
                // Cambia este valor (1 al 5) para probar cada apartado
                var opcion: Int = 5
                when (opcion) {
                    // 1. Obtener una lista con los productos cuyo precio sea 10 € o superior.
                    1 -> mayorA10(precios).forEach { Log.d("Ejercicio10", "Precio : $it") }

                    // 2. Sobre los productos obtenidos en el punto anterior, aplica un 10% de descuento.
                    2 -> descuento(mayorA10(precios)).forEach { Log.d("Ejercicio10", "Descuento : $it") }

                    // 3. Muestra por pantalla el precio final de cada producto.
                    3 -> descuento(mayorA10(precios)).forEach { Log.d("Ejercicio10", "Precio Final : $it") }

                    // 4. Busca en la lista original el primer producto cuyo precio sea superior a 18 € y muéstralo.
                    4 -> buscarMayor18(precios)

                    // 5. Realiza los apartados 1, 2 y 3 encadenando las funciones de orden superior.
                    5 -> precios.filter { it >= 10 }.map { it * 0.90 }.forEach { Log.d("Ejercicio10", "Encadenado Precio Final : $it") }
                }
            }
        }
    }

    fun mayorA10(listaprecios: ArrayList<Double>): List<Double> {
        listaprecios.filter { it >= 10 }
        return listaprecios.filter { it >= 10 }
    }

    fun descuento(listadescuento: List<Double>): List<Double> {
        return listadescuento.map { it * 0.90 }
    }

    fun buscarMayor18(listaprecios: ArrayList<Double>) {
        val resultado = listaprecios.find { it > 18 }
        Log.d("Ejercicio10", "Primer producto > 18€ : $resultado")
    }
}