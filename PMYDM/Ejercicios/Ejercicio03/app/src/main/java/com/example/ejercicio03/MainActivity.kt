package com.example.ejercicio03

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
import com.example.ejercicio03.ui.theme.Ejercicio03Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Ejercicio03Theme {
                var usuario = "Usuario1"

                var contra :String? = "admin123"

                contra?.let {

                    Log.d("Ejercicio03","Bienvenido : $usuario")
                    Log.d("Ejercicio03", "El password introducido es: $contra")
                } ?:
                run {
                    Log.e("Ejercicio03","Error: $usuario no ha introducido el password requerido")
                }




            }
        }
    }
}

