package com.example.hello_world

import  android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.random.Random

class MainComposeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TrileroGame()
        }
    }

    @Composable
    fun TrileroGame(shellCount: Int = 2) {
        val model = remember { ModelGame() }

        var resultText by remember { mutableStateOf("Welcome!") }
        var buttonText by remember { mutableStateOf("Start") }
        var canPlay by remember { mutableStateOf(false) }

        val shells = remember(shellCount) {
            List(shellCount) { R.drawable.cubilete }.toMutableStateList()
        }

        fun startRound() {
            shells.indices.forEach { shells[it] = R.drawable.cubilete }
            model.ModelStartRound(shells.size)
            canPlay = model.getCanPlay()
            resultText = "Pick a cup!"
        }

        fun pickShell(index: Int) {
            if (!model.getCanPlay()) return
            model.setCanPlay(false)
            canPlay = model.getCanPlay()

            val winningPos = model.getWinningPos()
            if (index == winningPos) {
                shells[index] = R.drawable.levantaok
                shells.add(R.drawable.cubilete)
                resultText = "You Win!!! \nNext round: ${shells.size} cups"
            } else {
                shells[index] = R.drawable.levantafail
                shells[winningPos] = R.drawable.levantaok
                while (shells.size > shellCount) shells.removeAt(shells.lastIndex)
                resultText = "You lost"
            }
            buttonText = "Try Again"
        }

        Box(
            modifier = Modifier.fillMaxSize().paint(
                painter = painterResource(id = R.drawable.fondo),
                contentScale = ContentScale.Crop
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 120.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                shells.withIndex().chunked(4).forEach { rowItems ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        rowItems.forEach { (index, image) ->
                            Image(
                                painter = painterResource(id = image),
                                contentDescription = "Cube ${index + 1}",
                                modifier = Modifier
                                    .size(90.dp)
                                    .clickable { pickShell(index) }
                            )
                        }
                    }
                }

                Button(
                    onClick = { startRound() },
                    enabled = !canPlay,
                    modifier = Modifier
                        .padding(24.dp)
                        .width(150.dp)
                        .height(45.dp)
                ) {
                    Text(text = buttonText, fontSize = 18.sp)
                }

                Text(
                    text = resultText,
                    fontSize = 30.sp,
                    color = Color.Black
                )
            }
        }
    }
    @Preview(showBackground = true)
    @Composable
    fun PreviewTrileroGame() {
        TrileroGame()
    }
}