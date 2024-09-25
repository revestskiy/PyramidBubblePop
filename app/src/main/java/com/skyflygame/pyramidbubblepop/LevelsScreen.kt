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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

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
                        painter = painterResource(id = R.drawable.lvl1 + index),
                        contentDescription = "Level $level",
                        modifier = Modifier
                            .size(50.dp) 
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
