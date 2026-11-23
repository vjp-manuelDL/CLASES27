package com.example.t6_ej08
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.t6_ej08.ui.theme.T6_Ej08Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            T6_Ej08Theme {
                // el scaffold deja hueco para las barras del sistema
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    JugadorApp(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

// la funcionalidad del boton guardar va en una funcion
fun guardarJugador(context: Context, nombre: String, personaje: String) {
    // segun lo que falte muestro el mensaje
    val mensaje = when {
        nombre.isBlank() && personaje.isEmpty() -> context.getString(R.string.error_todo)
        nombre.isBlank() -> context.getString(R.string.error_nombre)
        personaje.isEmpty() -> context.getString(R.string.error_personaje)
        else -> context.getString(R.string.jugador_guardado, nombre.trim(), personaje)
    }
    // mensaje emergente
    Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show()
}

@Composable
fun JugadorApp(modifier: Modifier = Modifier) {
    // contexto del emergente
    val context = LocalContext.current

    // la lista de personajes
    val personajes = stringArrayResource(R.array.personajes)

    // estados, cuando cambian se vuelve a pintar la pantalla sola
    var nombre by rememberSaveable { mutableStateOf("") }
    var personaje by rememberSaveable { mutableStateOf("") } // vacio = ninguno seleccionado

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = stringResource(R.string.titulo),
            style = MaterialTheme.typography.headlineMedium
        )

        // campo de texto para escribir el nombre del jugador
        TextField(
            value = nombre,
            onValueChange = { nombre = it }, // it es lo que el usuario acaba de escribir
            label = { Text(stringResource(R.string.nombre)) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Text(
            text = stringResource(R.string.tipo),
            style = MaterialTheme.typography.titleMedium
        )

        // bucle para crear una fila por cada personaje de la lista
        personajes.forEach { tipo ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { personaje = tipo }, // asi vale tocar en toda la fila
                verticalAlignment = Alignment.CenterVertically
            ) {
                // solo se marca el que coincide con el estado
                RadioButton(
                    selected = personaje == tipo,
                    onClick = { personaje = tipo }
                )
                Text(text = tipo)
            }
        }

        Button(
            onClick = { guardarJugador(context, nombre, personaje) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.guardar))
        }

        // empuja la imagen hasta abajo del todo
        Spacer(modifier = Modifier.height(24.dp))

        // imagen de abajo
        Image(
            painter = painterResource(R.drawable.dnd),
            contentDescription = stringResource(R.string.imagen),
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(10.dp)), // esquinas redondeadas
            contentScale = ContentScale.Crop // recorta la foto para que no se deforme
        )
    }
}
