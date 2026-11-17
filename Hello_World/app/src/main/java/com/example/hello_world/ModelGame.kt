package com.example.hello_world

import kotlin.random.Random

class ModelGame {
    private var winningPos = 0
    private var canPlay = false

    fun ModelStartRound(size: Int) {
        winningPos = Random.nextInt(0, size)
        canPlay = true
    }

    fun getWinningPos(): Int {
        return winningPos
    }

    fun getCanPlay(): Boolean {
        return canPlay
    }

    fun setCanPlay(playable: Boolean) {
        canPlay = playable
    }
}