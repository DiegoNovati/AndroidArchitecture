package uk.co.itmms.androidArchitecture.components

import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import uk.co.itmms.androidArchitecture.screens.PreviewAppScreen
import uk.co.itmms.androidArchitecture.ui.theme.AndroidArchitectureTheme

@Composable
fun AppDivider(
    modifier: Modifier = Modifier,
) {
    Divider(
        modifier = modifier,
        color = Color.Gray.copy(alpha = 0.4f),
        thickness = 1.dp,
    )
}

@PreviewAppScreen
@Composable
private fun AppDividerPreview() {
    AndroidArchitectureTheme {
        AppDivider()
    }
}