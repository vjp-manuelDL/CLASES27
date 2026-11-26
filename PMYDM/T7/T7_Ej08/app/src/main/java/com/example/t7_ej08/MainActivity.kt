package com.example.t7_ej08
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.t7_ej08.ui.theme.T7_Ej08Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            T7_Ej08Theme {
                PantallaActividades()
            }
        }
    }
}

// muestra un toast con un texto de strings.xml y sus parametros si los tiene
private fun toast(context: Context, @StringRes mensaje: Int, vararg args: Any) =
    Toast.makeText(context, context.getString(mensaje, *args), Toast.LENGTH_SHORT).show()



// icono de favoritas de la barra superior
fun mostrarFavoritas(context: Context, lista: List<Actividad>) {
    if (lista.isEmpty()) return toast(context, R.string.lista_vacia)
    // me quedo solo con las marcadas y saco sus nombres
    val favoritas = lista.filter { it.favorita }.map { it.nombre }
    if (favoritas.isEmpty()) toast(context, R.string.sin_favoritas)
    else toast(context, R.string.lista_favoritas, favoritas.joinToString(", "))
}

// icono de informacion de la barra superior
fun mostrarInformacion(context: Context, lista: List<Actividad>) {
    if (lista.isEmpty()) return toast(context, R.string.lista_vacia)
    toast(context, R.string.total_actividades, lista.size)
}

// boton de anadir, devuelve true si se ha anadido para limpiar la caja de texto
fun anadirActividad(context: Context, lista: SnapshotStateList<Actividad>, nombre: String): Boolean {
    if (nombre.isBlank()) {
        toast(context, R.string.error_actividad)
        return false
    }
    lista.add(Actividad(nombre.trim()))
    toast(context, R.string.actividad_anadida)
    return true
}

// boton flotante
fun eliminarTodas(context: Context, lista: SnapshotStateList<Actividad>) {
    if (lista.isEmpty()) return toast(context, R.string.lista_ya_vacia)
    lista.clear()
    toast(context, R.string.actividades_eliminadas)
}

// cambia el checkbox de una actividad
fun cambiarFavorita(lista: SnapshotStateList<Actividad>, posicion: Int, favorita: Boolean) {
    // hay que reemplazar el elemento entero para que la lista se entere del cambio
    lista[posicion] = lista[posicion].copy(favorita = favorita)
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaActividades() {
    // contexto del emergente
    val context = LocalContext.current

    // actividades iniciales desde strings.xml
    val iniciales = stringArrayResource(R.array.actividades_iniciales)

    // lista observable, al anadir o borrar se repinta la pantalla sola
    val actividades = remember { iniciales.map { Actividad(it) }.toMutableStateList() }

    // lo que el usuario va escribiendo
    var nueva by remember { mutableStateOf("") }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.titulo)) },
                actions = {
                    IconButton(onClick = { mostrarFavoritas(context, actividades) }) {
                        Icon(Icons.Filled.Favorite, contentDescription = stringResource(R.string.favoritas))
                    }
                    IconButton(onClick = { mostrarInformacion(context, actividades) }) {
                        Icon(Icons.Filled.Info, contentDescription = stringResource(R.string.informacion))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { eliminarTodas(context, actividades) },
                containerColor = MaterialTheme.colorScheme.errorContainer
            ) {
                Icon(Icons.Filled.Delete, contentDescription = stringResource(R.string.eliminar_todas))
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding) // hueco de la barra superior, si no la tapa
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = nueva,
                    onValueChange = { nueva = it }, // it es lo que acaba de escribir
                    label = { Text(stringResource(R.string.nueva)) },
                    singleLine = true,
                    modifier = Modifier.weight(1f) // ocupa el hueco y deja sitio al boton
                )
                IconButton(onClick = { if (anadirActividad(context, actividades, nueva)) nueva = "" }) {
                    Icon(
                        Icons.Filled.AddCircle,
                        contentDescription = stringResource(R.string.anadir),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            // lazycolumn solo pinta las filas que se ven
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(bottom = 80.dp) // asi el boton flotante no tapa la ultima
            ) {
                // necesito la posicion para poder cambiar el checkbox
                itemsIndexed(actividades) { posicion, actividad ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.padding(start = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = actividad.nombre,
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.weight(1f)
                            )
                            Checkbox(
                                checked = actividad.favorita,
                                onCheckedChange = { cambiarFavorita(actividades, posicion, it) }
                            )
                        }
                    }
                }
            }
        }
    }
}
