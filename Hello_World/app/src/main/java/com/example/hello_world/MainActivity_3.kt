package com.example.hello_world

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.ComponentActivity

class MainActivity_3 : ComponentActivity() {

    private lateinit var bStart: Button
    private lateinit var winCond: TextView
    private lateinit var coconuts: List<ImageView>

    private val model = ModelGame();

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_3)
        initElements()
        setupListeners()
        resetBoard()
        bStart.text = "Start Game"
    }

    private fun initElements() {
        winCond = findViewById(R.id.winCond)
        bStart = findViewById(R.id.bStart)
        coconuts = listOf(
            findViewById(R.id.coconut1),
            findViewById(R.id.coconut2),
            findViewById(R.id.coconut3)
        )
    }

    // Listeners are set once; canPlay decides whether a tap counts.
    private fun setupListeners() {
        bStart.setOnClickListener { startRound() }
        coconuts.forEachIndexed { index, coconut ->
            coconut.setOnClickListener { pickCoconut(index) }
        }
    }

    private fun resetBoard() {
        coconuts.forEach { it.setImageResource(R.drawable.cubilete) }
        winCond.visibility = View.INVISIBLE
    }

    private fun startRound() {
        resetBoard()
        model.ModelStartRound(3)
        bStart.visibility = View.INVISIBLE
    }

    private fun pickCoconut(pos: Int) {
        if (!model.getCanPlay()) return      // ignore taps before start / after the round ends
        model.setCanPlay(false)

        if (pos == model.getWinningPos()) {
            coconuts[pos].setImageResource(R.drawable.levantaok)
            endRound("You Win!!!")
        } else {
            coconuts[pos].setImageResource(R.drawable.levantafail)
            coconuts[model.getWinningPos()].setImageResource(R.drawable.levantaok) // show where it was
            endRound("You lost")
        }
    }

    private fun endRound(message: String) {
        winCond.text = message
        winCond.visibility = View.VISIBLE
        bStart.text = "Try Again"
        bStart.visibility = View.VISIBLE
    }
}