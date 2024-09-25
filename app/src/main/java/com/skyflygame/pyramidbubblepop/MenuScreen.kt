package com.skyflygame.pyramidbubblepop

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp


@Composable
fun MenuScreen(
    onNext: () -> Unit,
    onExit: () -> Unit,
    onOptions: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .paint(
                painter = painterResource(id = R.drawable.backgroundmain),
                contentScale = ContentScale.Crop
            )
    ) {
        Box(
            modifier = Modifier
                .paint(painter = painterResource(id = R.drawable.backgroundmenu),
                    contentScale = ContentScale.Fit)

        ){
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 80.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Image(
                    painter = painterResource(id = R.drawable.playbutton),
                    contentDescription = "Start Button",
                    modifier = Modifier
                        .size(width = 250.dp, height = 80.dp)
                        .shadow(
                            elevation = 0.dp,
                            shape = CircleShape
                        )
                        .clickable {
                            onNext()
                        }
                )
                Spacer(modifier = Modifier.height(20.dp))
                Image(
                    painter = painterResource(id = R.drawable.optionsbutton),
                    contentDescription = "Exit Button",
                    modifier = Modifier
                        .size(width = 250.dp, height = 80.dp)
                        .shadow(
                            elevation = 0.dp,
                            shape = CircleShape
                        )
                        .clickable {

                            onOptions()
                        }
                )
                Spacer(modifier = Modifier.height(20.dp))
                Image(
                    painter = painterResource(id = R.drawable.exit),
                    contentDescription = "Exit Button",
                    modifier = Modifier
                        .size(width = 250.dp, height = 80.dp)
                        .shadow(
                            elevation = 0.dp,
                            shape = CircleShape
                        )
                        .clickable {
                            onExit()
                        }
                )
            }
        }
    }
}