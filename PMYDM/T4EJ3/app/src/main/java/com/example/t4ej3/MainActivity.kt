package com.example.t4ej3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.t4ej3.ui.theme.T4EJ3Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            T4EJ3Theme {
                Column(modifier = Modifier.fillMaxSize()
                    .padding( all = 16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                )
                {
                    Text(
                        text = stringResource(id = R.string.PRIMERO),
                        modifier = Modifier
                            .background(Color.Yellow)
                            .border(width = 3.dp,color = Color.Black)
                            .padding(horizontal = 120.dp)
                            .padding(vertical = 10.dp)

                    )
                    Text(
                        text = stringResource(id = R.string.segundo),
                        modifier = Modifier
                            .size(250.dp)
                            .fillMaxSize()
                            .background(Color.LightGray)
                            .border(width = 8.dp, color = Color.Blue)
                            .padding(all = 10.dp)
                    )

                }
            }
        }
    }
}

