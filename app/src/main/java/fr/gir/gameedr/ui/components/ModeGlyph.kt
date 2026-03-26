package fr.gir.gameedr.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import fr.gir.gameedr.domain.model.GameMode
import fr.gir.gameedr.ui.theme.Brass
import fr.gir.gameedr.ui.theme.SandLight
import fr.gir.gameedr.ui.theme.TacticalOlive

@Composable
fun ModeGlyph(
    mode: GameMode,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.size(54.dp)) {
        when (mode) {
            GameMode.NETWORK_CALC -> {
                drawCircle(
                    color = Brass,
                    radius = size.minDimension * 0.12f,
                    center = Offset(size.width * 0.22f, size.height * 0.5f)
                )
                drawCircle(
                    color = Brass,
                    radius = size.minDimension * 0.12f,
                    center = Offset(size.width * 0.78f, size.height * 0.5f)
                )
                drawLine(
                    color = SandLight,
                    start = Offset(size.width * 0.34f, size.height * 0.5f),
                    end = Offset(size.width * 0.66f, size.height * 0.5f),
                    strokeWidth = 6f,
                    cap = StrokeCap.Round
                )
                drawRect(
                    color = TacticalOlive,
                    topLeft = Offset(size.width * 0.38f, size.height * 0.28f),
                    size = Size(size.width * 0.24f, size.height * 0.44f),
                    style = Stroke(width = 6f)
                )
            }

            GameMode.SUBNETTING -> {
                drawRect(
                    color = Brass,
                    topLeft = Offset(size.width * 0.16f, size.height * 0.24f),
                    size = Size(size.width * 0.68f, size.height * 0.52f),
                    style = Stroke(width = 6f)
                )
                drawLine(
                    color = SandLight,
                    start = Offset(size.width * 0.5f, size.height * 0.24f),
                    end = Offset(size.width * 0.5f, size.height * 0.76f),
                    strokeWidth = 5f
                )
                drawLine(
                    color = SandLight,
                    start = Offset(size.width * 0.16f, size.height * 0.5f),
                    end = Offset(size.width * 0.84f, size.height * 0.5f),
                    strokeWidth = 5f
                )
            }

            GameMode.VLSM -> {
                drawRect(
                    color = Brass,
                    topLeft = Offset(size.width * 0.16f, size.height * 0.2f),
                    size = Size(size.width * 0.18f, size.height * 0.6f)
                )
                drawRect(
                    color = TacticalOlive,
                    topLeft = Offset(size.width * 0.4f, size.height * 0.35f),
                    size = Size(size.width * 0.18f, size.height * 0.45f)
                )
                drawRect(
                    color = SandLight,
                    topLeft = Offset(size.width * 0.64f, size.height * 0.48f),
                    size = Size(size.width * 0.18f, size.height * 0.32f)
                )
                drawLine(
                    color = SandLight,
                    start = Offset(size.width * 0.16f, size.height * 0.86f),
                    end = Offset(size.width * 0.84f, size.height * 0.86f),
                    strokeWidth = 4f
                )
            }
        }
    }
}
