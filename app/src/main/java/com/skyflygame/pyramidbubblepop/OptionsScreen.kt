package com.skyflygame.pyramidbubblepop

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gameodd.MysterySpheres.Prefs
import com.gameodd.MysterySpheres.SoundManager
import com.skyflygame.pyramidbubblepop.ui.theme.nujnoefont

@Composable
fun OptionsScreen(
    onReturn: () -> Unit
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
                .align(Alignment.Center)
                .fillMaxWidth(0.8f)
                .paint(
                    painter = painterResource(id = R.drawable.backgroundoptions),
                    contentScale = ContentScale.Fit
                )
        ) {

            var isMusicEnabled by remember { mutableStateOf(Prefs.musicVolume > 0) }
            var isSoundEnabled by remember { mutableStateOf(Prefs.soundVolume > 0) }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "OPTIONS",
                    fontFamily = nujnoefont,
                    fontSize = 44.sp,
                    color = Color(0xFF572330),
                    modifier = Modifier
                        .offset(y = 40.dp)
                )
                Spacer(modifier = Modifier.height(90.dp))
                Text(
                    text = "AUDIO",
                    fontFamily = nujnoefont,
                    fontSize = 24.sp,
                    color = Color(0xFF572330),
                    modifier = Modifier
                )

                SettingItem(
                    title = "MUSIC",
                    checked = isMusicEnabled,
                    onCheckedChange = {
                        isMusicEnabled = it
                        Prefs.musicVolume = if (isMusicEnabled) 0.5f else 0f
                        SoundManager.setMusicVolume()
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))


                SettingItem(
                    title = "SOUND",
                    checked = isSoundEnabled,
                    onCheckedChange = {
                        isSoundEnabled = it
                        Prefs.soundVolume = if (isSoundEnabled) 0.5f else 0f
                        SoundManager.setSoundVolume()
                    }
                )

                Image(painter = painterResource(id = R.drawable.okbutton),
                    contentDescription = "",
                    modifier = Modifier
                        .padding(top = 49.dp)
                        .size(78.dp)
                        .clickable { onReturn() },
                    contentScale = ContentScale.Fit
                )
            }
        }
    }
}


@Composable
fun SettingItem(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 48.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween

    ) {

        Text(
            text = title,
            fontFamily = nujnoefont,
            fontSize = 19.sp,
            color = Color(0xFFFFDC00),
            modifier = Modifier.weight(1f)
        )


        IconButton(onClick = { onCheckedChange(!checked) },
            modifier = Modifier
                .size(72.dp)
        ) {
            Icon(
                painter = painterResource(
                    id = if (checked) R.drawable.on_btn else R.drawable.off_btn
                ),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier
                    .size(64.dp)
            )
        }
    }
}

