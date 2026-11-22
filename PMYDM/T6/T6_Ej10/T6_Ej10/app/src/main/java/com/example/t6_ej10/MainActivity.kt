package com.example.t6_ej10
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.t6_ej10.ui.theme.Crema
import com.example.t6_ej10.ui.theme.Marron
import com.example.t6_ej10.ui.theme.NaranjaCheck
import com.example.t6_ej10.ui.theme.T6_Ej10Theme
import com.example.t6_ej10.ui.theme.TextoOscuro

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            T6_Ej10Theme {
                // el scaffold deja hueco para las barras del sistema
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = Crema // color de fondo de toda la pantalla
                ) { innerPadding ->
                    VisitaApp(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

// la funcionalidad del boton guardar visita va en una funcion
fun guardarVisita(context: Context, seleccionados: List<String>) {
    // segun si hay monumentos marcados muestro el mensaje
    val mensaje = if (seleccionados.isEmpty()) {
        context.getString(R.string.sin_monumentos)
    } else {
        context.getString(R.string.visita_guardada, seleccionados.joinToString(", "))
    }
    // mensaje emergente
    Toast.makeText(context, mensaje, Toast.LENGTH_LONG).show()
}

@Composable
fun VisitaApp(modifier: Modifier = Modifier) {
    // contexto del emergente
    val context = LocalContext.current

    // un estado por cada monumento, asi se pueden marcar varios
    var bigBen by rememberSaveable { mutableStateOf(false) }
    var cabina by rememberSaveable { mutableStateOf(false) }
    var noria by rememberSaveable { mutableStateOf(false) }
    var puente by rememberSaveable { mutableStateOf(false) }

    // los nombres los saco aqui para poder usarlos dentro del boton
    val nombreBigBen = stringResource(R.string.big_ben)
    val nombreCabina = stringResource(R.string.cabina)
    val nombreNoria = stringResource(R.string.noria)
    val nombrePuente = stringResource(R.string.puente)

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = stringResource(R.string.titulo),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = Marron
        )

        // primera fila de la rejilla, el weight le da la mitad del alto que sobra
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // dentro de la fila el weight reparte el ancho entre las dos fotos
            Monumento(R.drawable.bigbeng, nombreBigBen, bigBen, Modifier.weight(1f)) { bigBen = it }
            Monumento(R.drawable.cabina_roja, nombreCabina, cabina, Modifier.weight(1f)) { cabina = it }
        }

        // segunda fila de la rejilla
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Monumento(R.drawable.noria, nombreNoria, noria, Modifier.weight(1f)) { noria = it }
            Monumento(R.drawable.puente, nombrePuente, puente, Modifier.weight(1f)) { puente = it }
        }

        Button(
            onClick = {
                // voy metiendo en la lista los que estan marcados
                val lista = mutableListOf<String>()
                if (bigBen) lista.add(nombreBigBen)
                if (cabina) lista.add(nombreCabina)
                if (noria) lista.add(nombreNoria)
                if (puente) lista.add(nombrePuente)
                guardarVisita(context, lista)
            },
            shape = RoundedCornerShape(50), // boton redondeado
            colors = ButtonDefaults.buttonColors(containerColor = Marron, contentColor = Color.White),
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            Text(
                text = stringResource(R.string.guardar),
                fontWeight = FontWeight.Bold
            )
        }
    }
}

// composable propio para no repetir cuatro veces lo mismo
@Composable
fun Monumento(
    @DrawableRes imagen: Int,
    nombre: String,
    marcado: Boolean,
    modifier: Modifier = Modifier, // aqui llega el weight de la fila
    onCambio: (Boolean) -> Unit // avisa de que el checkbox ha cambiado
) {
    Column(modifier = modifier) {
        Image(
            painter = painterResource(id = imagen),
            contentDescription = nombre, // descripcion para accesibilidad
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f) // ocupa todo el hueco que queda encima del checkbox
                .clip(RoundedCornerShape(12.dp)), // esquinas redondeadas
            contentScale = ContentScale.Crop // recorta la foto para que no se deforme
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = marcado,
                onCheckedChange = onCambio,
                colors = CheckboxDefaults.colors(checkedColor = NaranjaCheck)
            )
            Text(
                text = nombre,
                style = MaterialTheme.typography.bodyMedium,
                color = TextoOscuro // asi se lee bien aunque el movil este en modo oscuro
            )
        }
    }
}
