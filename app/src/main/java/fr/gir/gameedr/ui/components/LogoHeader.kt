package fr.gir.gameedr.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import fr.gir.gameedr.R
import fr.gir.gameedr.ui.theme.DarkSoil

@Composable
fun LogoHeader(
    modifier: Modifier = Modifier,
    title: String? = null,
    size: Dp = 180.dp
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Surface(
            color = DarkSoil.copy(alpha = 0.32f),
            shape = CircleShape,
            tonalElevation = 4.dp,
            shadowElevation = 10.dp
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_gameedr),
                contentDescription = "Logo GameEDR",
                modifier = Modifier.size(size)
            )
        }
        if (!title.isNullOrBlank()) {
            Text(
                text = title,
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}
