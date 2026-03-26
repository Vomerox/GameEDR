package fr.gir.gameedr.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import fr.gir.gameedr.ui.theme.BorderMuted
import fr.gir.gameedr.ui.theme.Brass
import fr.gir.gameedr.ui.theme.SteelBlue
import fr.gir.gameedr.ui.theme.TacticalOlive

@Composable
fun TacticalBackdrop(
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.fillMaxSize()) {
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color(0xFF16110D),
                    Color(0xFF1D1712),
                    Color(0xFF283021)
                )
            )
        )

        val majorSpacing = size.width / 6f
        val minorSpacing = size.width / 18f

        var x = 0f
        while (x <= size.width) {
            drawLine(
                color = if ((x / majorSpacing).toInt() % 2 == 0) BorderMuted.copy(alpha = 0.14f) else SteelBlue.copy(alpha = 0.08f),
                start = Offset(x, 0f),
                end = Offset(x, size.height),
                strokeWidth = if (x % majorSpacing < 1f) 1.5f else 1f
            )
            x += minorSpacing
        }

        var y = 0f
        while (y <= size.height) {
            drawLine(
                color = TacticalOlive.copy(alpha = 0.09f),
                start = Offset(0f, y),
                end = Offset(size.width, y),
                strokeWidth = if (y % (minorSpacing * 3f) < 1f) 1.4f else 1f
            )
            y += minorSpacing * 1.1f
        }

        repeat(3) { index ->
            drawCircle(
                color = Brass.copy(alpha = 0.06f - index * 0.012f),
                radius = size.minDimension * (0.34f + index * 0.11f),
                center = Offset(size.width * 0.88f, size.height * 0.17f),
                style = Stroke(width = 2f)
            )
        }

        repeat(4) { index ->
            val startY = size.height * (0.58f + index * 0.08f)
            drawLine(
                brush = Brush.horizontalGradient(
                    colors = listOf(Color.Transparent, BorderMuted.copy(alpha = 0.12f), Color.Transparent)
                ),
                start = Offset(size.width * 0.08f, startY),
                end = Offset(size.width * 0.92f, startY - 28f),
                strokeWidth = 2f,
                cap = StrokeCap.Round
            )
        }

        drawLine(
            color = Brass.copy(alpha = 0.12f),
            start = Offset(size.width * 0.08f, size.height * 0.12f),
            end = Offset(size.width * 0.26f, size.height * 0.12f),
            strokeWidth = 3f,
            cap = StrokeCap.Round
        )
        drawLine(
            color = Brass.copy(alpha = 0.12f),
            start = Offset(size.width * 0.74f, size.height * 0.12f),
            end = Offset(size.width * 0.92f, size.height * 0.12f),
            strokeWidth = 3f,
            cap = StrokeCap.Round
        )
    }
}
