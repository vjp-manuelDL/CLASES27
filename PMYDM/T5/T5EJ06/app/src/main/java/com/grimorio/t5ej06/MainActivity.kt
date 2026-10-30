package com.grimorio.t5ej06

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.benchmark.traceprocessor.Row
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.grimorio.t5ej06.ui.theme.AmarilloCuadrante
import com.grimorio.t5ej06.ui.theme.AzulCuadrante
import com.grimorio.t5ej06.ui.theme.RojoCuadrante
import com.grimorio.t5ej06.ui.theme.T5EJ06Theme
import com.grimorio.t5ej06.ui.theme.VerdeCuadrante

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            T5EJ06Theme {
                PantallaCuadrantes()

            }
        }
    }
}

@Composable
fun PantallaCuadrantes() {
    // Columna principal que divide la pantalla en la mitad superior y la inferior
    Column(modifier = Modifier.fillMaxSize()) {

        // Fila Superior Cuadrante 1 y Cuadrante 2
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f) // Ocupa la mitad superior
        ) {
            // Cuadrante 1 Izquierda Superior - Verde
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .background(VerdeCuadrante),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Texto 1",
                    style = MaterialTheme.typography.titleLarge,// Aplicando MaterialTheme
                    color = Color.Blue
                )
            }

            // Cuadrante 2 (Derecha Superior) - Rojo
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .background(RojoCuadrante),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Texto 2",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.Blue
                )
            }
        }

        // Fila Inferior (Cuadrante 3 y Cuadrante 4)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f) // Ocupa la mitad inferior
        ) {
            // Cuadrante 3 (Izquierda Inferior) - Azul
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .background(AzulCuadrante),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Texto 3",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.Blue
                )
            }

            // Cuadrante 4 (Derecha Inferior) - Amarillo
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .background(AmarilloCuadrante),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Texto 4",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.Blue
                )
            }
        }
    }
}

