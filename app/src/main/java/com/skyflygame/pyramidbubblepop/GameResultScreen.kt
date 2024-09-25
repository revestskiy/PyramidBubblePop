package com.skyflygame.pyramidbubblepop

import android.graphics.Paint.Align
import android.graphics.Paint.Style
import android.graphics.Typeface
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
@Preview
@Composable
fun GameResultScreen(
    title: String = "Defeat",
    buttonText: String = "Next Level",
    level: Int = 1,
    time: String = "00:35",
    onButtonClick: () -> Unit = {}
) {
    // Background image
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
    ) {
        Image(
            painter = painterResource(id = R.drawable.backgroundmain),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Title (WIN or Defeat)
            AppThemeText(
                text = title,
                fontSize = 48.sp,
                modifier = Modifier
            )

            Spacer(modifier = Modifier.height(72.dp))

            // Level
            AppThemeText(
                text = "Level $level",
                fontSize = 24.sp,
                modifier = Modifier
                    .offset(x = (50.dp))
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Time
            AppThemeText(
                text = if (time == "00:00") "Time is Over" else "Time: $time",
                fontSize = 24.sp,
                modifier = Modifier
                    .offset(x = (90.dp))
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Button
            Box(
                modifier = Modifier
                    .shadow(
                        elevation = 0.dp,
                        shape = CircleShape
                    )
                    .paint(
                        painter = painterResource(id = R.drawable.btn_text),
                        contentScale = ContentScale.Fit
                    ).clickable {
                        onButtonClick()

                    },
                contentAlignment = Alignment.Center) {
                Text(
                    text = buttonText,
                    fontSize = 24.sp,
                    color = Color(0xFFFFDC00)
                )
            }
        }
    }
}
val appTypeface: Typeface
    @Composable
    get() = Typeface.createFromAsset(LocalContext.current.assets, "fugaz.ttf")

@Composable
fun AppThemeText(text: String, modifier: Modifier = Modifier, textAlign: Align = Align.LEFT, fontSize: TextUnit = 38.sp) {
    val appTypeface = appTypeface
    val ShadowColor = Color(0x80000000)
    // Canvas для создания текста с обводкой
    Canvas(
        modifier = modifier
            .offset(x=((-text.length * 15).dp), y=((-32).dp))
    ) {
        drawIntoCanvas { canvas ->
            val paint = Paint().asFrameworkPaint().apply {
                isAntiAlias = true
                textSize = fontSize.toPx()
                color = android.graphics.Color.BLACK // Цвет обводки
                style = Style.STROKE
                strokeWidth = 3f
                this.textAlign = textAlign
                setShadowLayer(
                    3f,
                    3f,
                    3f,
                    ShadowColor.toArgb()
                )
                this.typeface = appTypeface
            }

            // Рисуем текст с обводкой
            canvas.nativeCanvas.drawText(
                text,
                0f,
                size.height / 2 + (paint.textSize / 2),
                paint
            )

            // Рисуем текст с основным цветом
            paint.apply {
                color = Color(0xFFFFDC00).toArgb() // Цвет текста
                style = Style.FILL
            }

            canvas.nativeCanvas.drawText(
                text,
                0f,
                size.height / 2 + (paint.textSize / 2),
                paint
            )
        }
    }
}
