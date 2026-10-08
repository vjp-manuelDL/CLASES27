package com.example.ejercicio04

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
import androidx.compose.ui.platform.LocalGraphicsContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.ejercicio04.ui.theme.Ejercicio04Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Ejercicio04Theme {
                mostarNumeros(1,5,6)
                sumarNumeros(1,5,6)
                mediaNumeros(1,5,6)
                var media : Int = mediaNumeros(1,5,6,)
                operaciones(media,6)
            }
        }
    }

    fun  mostarNumeros( num1 : Int ,num2 : Int,num3 : Int){
        Log.d("Ejercicio04", "Num1 : $num1" )
        Log.d("Ejercicio04", "Num2 : $num2" )
        Log.d("Ejercicio04", "Num3º : $num3" )

    }


    fun sumarNumeros (num1 : Int, num2 : Int, num3 : Int){
        var suma : Int = 0
        suma = (num1 + num2 + num3)

        Log.d("Ejercicio04","Suma : $suma")
    }

    fun mediaNumeros (num1 : Int, num2 : Int, num3 : Int): Int {
        var suma : Int = 0
        suma = (num1 + num2 + num3)
        var media : Int = suma/3

        Log.d("Ejercicio04","Media : $media")

        return media
    }

    fun operaciones (media : Int , num3 : Int){
        var calculo : Int = media * num3

        Log.d("Ejercicio04","Operaciones : $calculo")
    }



}

