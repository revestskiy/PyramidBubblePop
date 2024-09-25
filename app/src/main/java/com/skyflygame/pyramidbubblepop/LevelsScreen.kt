package com.skyflygame.pyramidbubblepop

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
val drawables = listOf(
    R.drawable.lvl1,
    R.drawable.lvl2,
    R.drawable.lvl3,
    R.drawable.lvl4,
    R.drawable.lvl5,
    R.drawable.lvl6,
    R.drawable.lvl7,
    R.drawable.lvl8,
    R.drawable.lvl9,
    R.drawable.lvl10,
    R.drawable.lvl11,
    R.drawable.lvl12,
    R.drawable.lvl13,
    R.drawable.lvl14
)
@Composable
fun LevelsScreen(
    onReturn: () -> Unit,
    onLevelSelected: (Int) -> Unit
) {
    // Main background image
    Box(
        modifier = Modifier
            .fillMaxSize()
            .paint(painter = painterResource(id = R.drawable.backgroundmain),
                contentScale = ContentScale.Crop)
    ) {
        // Column for header, levels, and back button

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Levels header image
            Image(
                painter = painterResource(id = R.drawable.levels),
                contentDescription = "Levels",
                modifier = Modifier
                    .padding(top = 16.dp)
                    .size(200.dp) // Adjust the size if necessary
            )

            // Levels grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(16.dp)
            ) {
                items(14) { index ->
                    val level = index + 1
                    Image(
                        painter = painterResource(id = drawables[index]),
                        contentDescription = "Level $level",
                        modifier = Modifier
                            .size(50.dp)
                            .shadow(
                                elevation = 0.dp,
                                shape = CircleShape
                            )
                            .clickable {
                                onLevelSelected(level)
                            }
                    )
                }
            }

            // Back button
            Image(
                painter = painterResource(id = R.drawable.backbutton),
                contentDescription = "Back Button",
                modifier = Modifier
                    .padding(bottom = 26.dp)
                    .size(120.dp)
                    .clickable {
                        onReturn()
                    }
            )
        }
    }
}
