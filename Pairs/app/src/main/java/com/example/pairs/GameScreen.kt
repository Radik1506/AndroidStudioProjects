package com.example.pairs

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun GameScreen(viewModel: GameViewModel = viewModel()) {
    Box(modifier = Modifier.fillMaxSize()) {

        Image(
            painter = painterResource(id = R.drawable.fondo1),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Movimientos: ${viewModel.moves}", fontSize = 28.sp, color = Color.White)

            Spacer(modifier = Modifier.height(16.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                itemsIndexed(viewModel.cards) { index, card ->
                    CardView(card = card, onClick = { viewModel.onCardClicked(index) })
                }
            }

            if (viewModel.won && viewModel.cards.size/2 == viewModel.moves) {
                Text(
                    text = "Ganaste en ${viewModel.moves} movimientos!\nPerfecto!",
                    fontSize = 24.sp,
                    color = Color.White
                )
            } else if (viewModel.won && viewModel.cards.size/2 != viewModel.moves) {
                Text(
                    text = "Ganaste en ${viewModel.moves} movimientos!",
                    fontSize = 24.sp,
                    color = Color.White
                )
            }

            Row(
                modifier = Modifier.padding(top = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = { viewModel.validate() },
                    enabled = viewModel.canValidate
                ) {
                    Text(text = "Validate", fontSize = 20.sp)
                }
                Button(onClick = { viewModel.restart() }) {
                    Text(text = "Restart", fontSize = 20.sp)
                }
            }
        }
    }
}

@Composable
fun CardView(card: Card, onClick: () -> Unit) {
    val showFace = card.isFaceUp || card.isMatched

    Image(
        painter = painterResource(id = if (showFace) card.image else R.drawable.deck),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .aspectRatio(0.75f)
            .clip(RoundedCornerShape(12.dp))
            .alpha(if (card.isMatched) 0.6f else 1f)
            .clickable(onClick = onClick)
    )
}