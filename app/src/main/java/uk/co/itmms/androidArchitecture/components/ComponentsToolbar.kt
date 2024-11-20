package uk.co.itmms.androidArchitecture.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.HideImage
import androidx.compose.material.primarySurface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import uk.co.itmms.androidArchitecture.screens.PreviewAppScreen
import uk.co.itmms.androidArchitecture.ui.theme.AndroidArchitectureTheme

typealias ToolbarAction = @Composable () -> Unit

@Composable
fun AppToolbar(
    title: String,
    modifier: Modifier = Modifier,
    actionList: List<ToolbarAction> = emptyList(),
) {
    val modifierLeft = Modifier.padding(start = 16.dp, end = 8.dp)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colors.primarySurface)
            .padding(vertical = 14.dp)
            .then(modifierLeft)
            .then(modifier),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        AppText(
            modifier = Modifier
                .weight(1f),
            text = title,
            type = AppTextType.Title,
        )
        actionList.map { action ->
            action()
        }
    }
}

@PreviewAppScreen
@Composable
private fun AppToolbarPreview() {
    AndroidArchitectureTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Black),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            AppToolbar(
                title = "Login",
            )
            AppToolbar(
                title = "Home",
                actionList = listOf(
                    { AppIconActionOne() },
                    { AppIconActionTwo() },
                ),
            )
        }
    }
}

@Composable
private fun AppIconActionOne(
    modifier: Modifier = Modifier,
    tint: Color = Color.White,
) {
    Icon(
        modifier = modifier,
        imageVector = Icons.Default.HideImage,
        contentDescription = "",
        tint = tint,
    )
}

@Composable
private fun AppIconActionTwo(
    modifier: Modifier = Modifier,
    tint: Color = Color.White,
) {
    Icon(
        modifier = modifier,
        imageVector = Icons.Default.AccountBox,
        contentDescription = "",
        tint = tint,
    )
}