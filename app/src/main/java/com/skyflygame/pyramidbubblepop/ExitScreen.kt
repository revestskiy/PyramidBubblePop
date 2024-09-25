package com.skyflygame.pyramidbubblepop

import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.skyflygame.pyramidbubblepop.ui.theme.nujnoefont

@Composable
fun ExitConfirmationScreen(
    onReturn: () -> Unit
) {
    val contextActivity = LocalContext.current as ComponentActivity
    // Main background
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
                .align(Alignment.Center)
                .paint(
                    painter = painterResource(id = R.drawable.backgroundoptions),
                    contentScale = ContentScale.Fit
                )
                .padding(16.dp)
                .fillMaxWidth(0.8f)

        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // EXIT text
                Text(
                    text = "EXIT",
                    fontFamily = nujnoefont,
                    fontSize = 44.sp,
                    color = Color(0xFF572330),
                    modifier = Modifier
                        .offset(y = (-100).dp)
                )


                // ARE YOU SURE? text
                Text(
                    text = "ARE YOU SURE?",
                    fontFamily = nujnoefont,
                    fontSize = 25.sp,
                    color = Color(0xFF572330) // Adjust this to fit your design
                )

                // Buttons (NO and YES)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    // No Button
                    Image(
                        painter = painterResource(id = R.drawable.nobutton),
                        contentDescription = "No Button",
                        modifier = Modifier
                            .size(80.dp)
                            .clickable {
                                onReturn()
                            }
                    )

                    // Yes Button
                    Image(
                        painter = painterResource(id = R.drawable.yesbutton),
                        contentDescription = "Yes Button",
                        modifier = Modifier
                            .size(80.dp)
                            .clickable {
                                contextActivity.finish()
                            }
                    )
                }
            }
        }
    }
}
