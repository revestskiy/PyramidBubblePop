package com.skyflygame.pyramidbubblepop

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.skyflygame.pyramidbubblepop.ui.theme.PyramidBubblePopTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SoundManager.init(application)
        Prefs.init(application)
        setContent {
            PyramidBubblePopTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = "loading"
                ) {
                    composable("loading") {
                        LoadingScreen(onNext = {
                            navController.navigate("menu") {
                                popUpTo("loading") { inclusive = true }
                            }
                        })
                    }
                    composable("menu") {
                        MenuScreen(
                            onNext = {
                                SoundManager.playSound()
                                navController.navigate("levels")
                            },
                            onExit = {
                                SoundManager.playSound()
                                navController.navigate("exit")
                            },
                            onOptions = {
                                SoundManager.playSound()
                                navController.navigate("options")
                            })
                    }
                    composable("options") {
                        OptionsScreen(onReturn = {
                            SoundManager.playSound()
                            navController.navigateUp()
                        })
                    }
                    composable("game/{level}") {
                        val level = it.arguments?.getString("level")?.toIntOrNull() ?: 1
                        GameScreen(level) {
                            SoundManager.playSound()
                            navController.navigateUp()
                        }
                    }
                    composable("levels") {
                        LevelsScreen(onReturn = {
                            SoundManager.playSound()
                            navController.navigateUp()
                        },
                            onLevelSelected = {
                                SoundManager.playSound()
                                navController.navigate("game/$it")
                            })
                    }
                    composable("exit") {
                        ExitConfirmationScreen(onReturn = {
                            SoundManager.playSound()
                            navController.navigateUp()
                        })
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        SoundManager.resumeMusic()
    }

    override fun onPause() {
        super.onPause()
        SoundManager.pauseMusic()
    }

    override fun onDestroy() {
        super.onDestroy()
        SoundManager.onDestroy()
    }
}

@Composable
fun LoadingScreen(
    onNext: () -> Unit
) {
    LaunchedEffect(Unit) {
        delay(2000)
        onNext()
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .paint(
                painter = painterResource(id = R.drawable.loading),
                contentScale = ContentScale.Crop
            )
    ) {
        LinearProgressIndicator(
            color = Color(0xffB99FFF),
            trackColor = Color.White,
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(4.dp)
                .height(16.dp)
                .align(Alignment.BottomCenter)
                .clip(CircleShape),
        )
    }
}


