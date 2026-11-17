package com.example.pairs

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel() {

    private val model = ModelGame()

    var cards by mutableStateOf(model.createCards())
        private set
    var moves by mutableIntStateOf(0)
        private set
    var won by mutableStateOf(false)
        private set

    private var selected by mutableStateOf<List<Int>>(emptyList())

    val canValidate: Boolean
        get() = selected.size == 2 && !won

    fun onCardClicked(index: Int) {
        if (cards[index].isMatched) return
        if (index in selected) return
        if (selected.size >= 2) return

        setFaceUp(index, true)
        selected = selected + index
    }

    fun validate() {
        if (selected.size != 2) return
        val (a, b) = selected

        moves++
        if (model.isMatch(cards[a], cards[b])) {
            setMatched(a)
            setMatched(b)
            won = model.isGameWon(cards)
        } else {
            setFaceUp(a, false)
            setFaceUp(b, false)
        }
        selected = emptyList()
    }

    fun restart() {
        cards = model.createCards()
        moves = 0
        won = false
        selected = emptyList()
    }

    private fun setFaceUp(index: Int, up: Boolean) {
        cards = cards.toMutableList().also { it[index] = it[index].copy(isFaceUp = up) }
    }

    private fun setMatched(index: Int) {
        cards = cards.toMutableList().also { it[index] = it[index].copy(isMatched = true) }
    }
}