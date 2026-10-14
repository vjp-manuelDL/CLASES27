package com.example.botseguridadapp

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
import com.example.botseguridadapp.ui.theme.BotSeguridadAPPTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BotSeguridadAPPTheme {
                val info = Persona("Manuel", 20, listOf("Series", "Juegos", "Musica"))

                botSeguridad(info)
            }
        }
    }

    fun botSeguridad(persona: Persona) {
        if(persona.name != "Manuel") {
            Log.e("BotAPP", "Acceso denegado")
        } else {
            if(persona.age < 18 || persona.age in 65..100) {
                Log.e("BotAPP", "Acceso denegado")
            } else {
                Log.d("BotAPP", "Acceso permitido")
            }
        }
    }
}





