package com.example.ejercicio4t4

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
import com.example.ejercicio4t4.ui.theme.Ejercicio4T4Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Ejercicio4T4Theme {


            }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d("Start ","Empezando")

    }
    override fun onRestart() {
        super.onRestart()
        Log.d("Restart ","Reiniciando")
    }
    override fun onResume() {
        super.onResume()
        Log.d("Resume ","Volviendo")

    }  override fun onPause() {
        super.onPause()
        Log.d("Pause ","Pausa")

    }  override fun onStop() {
        super.onStop()
        Log.d("Stop","Parada")

    }
    override fun onDestroy() {
        super.onDestroy()
        Log.d("Destroy ","Destruyendo")

    }



}
