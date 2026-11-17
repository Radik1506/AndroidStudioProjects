package com.example.pairs

data class Card(
    val id: Int,
    val image: Int,
    val isFaceUp: Boolean = false,
    val isMatched: Boolean = false
)

class ModelGame {
    private val images = listOf(
        R.drawable.king_of_hearts,
        R.drawable.queen_of_clubs,
        R.drawable.jack_of_diamonds
    )

    fun createCards(): List<Card> {
        return (images + images)
            .shuffled()
            .mapIndexed { index, image -> Card(id = index, image = image) }
    }

    fun isMatch(a: Card, b: Card): Boolean = a.image == b.image

    fun isGameWon(cards: List<Card>): Boolean = cards.all { it.isMatched }
}