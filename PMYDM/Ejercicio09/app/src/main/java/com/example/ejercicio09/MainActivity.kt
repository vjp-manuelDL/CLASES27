package com.example.ejercicio09

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.ejercicio09.ui.theme.Ejercicio09Theme // Asegúrate de que el nombre del theme coincida con tu proyecto

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()


        setContent {
            Ejercicio09Theme {
                gestionarAlumnos()
            }
        }
    }

    private fun gestionarAlumnos() {
        // 1. Creamos el listado inmutable con los nombres
        val nombres: List<String> = listOf("Pepe", "Juan", "Ana", "Marta", "Pedro", "María")

        // 2. Creamos el listado inmutable con las notas
        val notas: List<Double> = listOf(7.5, 6.0, 9.2, 8.8, 5.4, 9.8)

        // 3. Variable con la opción elegida
        var opcion = 2

        // 4. llamo la opcion
        when (opcion) {
            1 -> mostrarNotas(nombres,notas)
            2 -> mostrarMejorAlumno(nombres, notas)
            else -> Log.d("Ejercicio09", "Opción no válida")
        }
    }

    // Operación 1: Mostrar las notas almacenadas
    fun mostrarNotas(nombres: List<String>, notas: List<Double>) {
        Log.d("Ejercicio09", "--- LISTADO DE NOTAS ---")

        // Recorremos los listados por su posición
        for (i: Int in 0 until notas.size) {
            // Accedemos a ambos listados usando la misma posición 'i'
            Log.d("Ejercicio09", "Alumno: ${nombres[i]} - Nota: ${notas[i]}")
        }
    }

    // Operación 2: Mostrar el nombre y la nota del mejor alumno
     fun mostrarMejorAlumno(nombres: List<String>, notas: List<Double>) {
        var notaMasAlta = 0.0
        var indiceMejorAlumno = 0

        // Recorremos los listados por posición (índice) usando until
        for (i: Int in 0 until notas.size) {

            // Condicional simple para encontrar la nota mayor
            if (notas[i] > notaMasAlta) {
                notaMasAlta = notas[i]
                indiceMejorAlumno = i // Guardamos la posición del mejor alumno
            }
        }

        // Mostramos el resultado accediendo a la posición guardada en ambos listados
        Log.d("Ejercicio09", "El alumno con mejor nota es ${nombres[indiceMejorAlumno]} con un $notaMasAlta")
    }
}