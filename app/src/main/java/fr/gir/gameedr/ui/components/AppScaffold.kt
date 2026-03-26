package fr.gir.gameedr.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import fr.gir.gameedr.ui.theme.DarkSoil

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScaffold(
    title: String = "",
    subtitle: String? = null,
    onBack: (() -> Unit)? = null,
    onHome: (() -> Unit)? = null,
    homeAsNavigationIcon: Boolean = false,
    bottomBar: @Composable (() -> Unit)? = null,
    content: @Composable (PaddingValues) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        TacticalBackdrop()
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = Color.Transparent,
            topBar = {
                if (title.isNotBlank() || subtitle != null || onBack != null || onHome != null) {
                    Surface(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                        color = DarkSoil.copy(alpha = 0.78f),
                        contentColor = MaterialTheme.colorScheme.onBackground,
                        shape = MaterialTheme.shapes.large,
                        tonalElevation = 6.dp,
                        shadowElevation = 10.dp
                    ) {
                        CenterAlignedTopAppBar(
                            title = {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = if (subtitle.isNullOrBlank()) title else "$title - $subtitle",
                                        style = MaterialTheme.typography.titleLarge
                                    )
                                }
                            },
                            navigationIcon = {
                                if (onBack != null) {
                                    TextButton(onClick = onBack) {
                                        Text(text = "Retour")
                                    }
                                } else if (homeAsNavigationIcon && onHome != null) {
                                    IconButton(
                                        onClick = onHome,
                                        modifier = Modifier.size(36.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Filled.Home,
                                            contentDescription = "Retour accueil",
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            },
                            actions = {
                                if (!homeAsNavigationIcon && onHome != null) {
                                    TextButton(onClick = onHome) {
                                        Text(text = "Accueil")
                                    }
                                }
                            },
                            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                                containerColor = Color.Transparent,
                                titleContentColor = MaterialTheme.colorScheme.onBackground,
                                navigationIconContentColor = MaterialTheme.colorScheme.primary,
                                actionIconContentColor = MaterialTheme.colorScheme.primary
                            )
                        )
                    }
                }
            },
            bottomBar = {
                if (bottomBar != null) {
                    bottomBar()
                }
            }
        ) { innerPadding ->
            content(
                PaddingValues(
                    start = 20.dp,
                    top = innerPadding.calculateTopPadding() + 12.dp,
                    end = 20.dp,
                    bottom = innerPadding.calculateBottomPadding() + 16.dp
                )
            )
        }
    }
}
