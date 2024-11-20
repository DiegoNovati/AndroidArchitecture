package uk.co.itmms.androidArchitecture.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.HideImage
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import uk.co.itmms.androidArchitecture.screens.PreviewAppScreen
import uk.co.itmms.androidArchitecture.ui.theme.AndroidArchitectureTheme

val screenDefaultPadding = 16.dp

@Composable
fun AppScreen(
    toolbarAvailable: Boolean = true,
    toolbarTitle: String,
    toolbarActionList: List<ToolbarAction> = emptyList(),
    defaultPadding: Dp = screenDefaultPadding,
    errorMessage: String?,
    onDismissErrorMessage: () -> Unit,
    content: @Composable ColumnScope.() -> Unit,
) {
    errorMessage?.let {
        AppDialogError(
            errorMessage = it,
            onDismiss = onDismissErrorMessage,
        )
    }

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(defaultPadding),
        ) {
            if (toolbarAvailable) {
                AppToolbar(
                    title = toolbarTitle,
                    actionList = toolbarActionList,
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .padding(defaultPadding)
                    .padding(bottom = defaultPadding),
            ) {
                content()
            }
        }
    }
}

@PreviewAppScreen
@Composable
private fun AppScreenPreview() {
    AndroidArchitectureTheme {
        AppScreen(
            toolbarTitle = "Login",
            errorMessage = null,
            onDismissErrorMessage = {},
        ) {
            Text("Here goes the content")
        }
    }
}

@PreviewAppScreen
@Composable
private fun AppScreenWithBackPreview() {
    AndroidArchitectureTheme {
        AppScreen(
            toolbarTitle = "Login",
            errorMessage = null,
            onDismissErrorMessage = {},
        ) {
            Text("Here goes the content")
        }
    }
}

@PreviewAppScreen
@Composable
private fun AppScreenWithMenuPreview() {
    AndroidArchitectureTheme {
        AppScreen(
            toolbarTitle = "Login",
            errorMessage = null,
            onDismissErrorMessage = {},
        ) {
            Text("Here goes the content")
        }
    }
}

@PreviewAppScreen
@Composable
private fun AppScreenWithBackAndMenuPreview() {
    AndroidArchitectureTheme {
        AppScreen(
            toolbarTitle = "Login",
            errorMessage = null,
            onDismissErrorMessage = {},
        ) {
            Text("Here goes the content")
        }
    }
}

@PreviewAppScreen
@Composable
private fun AppScreenWithBackAndMenuAndActionsPreview() {
    AndroidArchitectureTheme {
        AppScreen(
            toolbarTitle = "Login",
            errorMessage = null,
            toolbarActionList = listOf(
                { AppIconActionOne() },
                { AppIconActionTwo() },
            ),
            onDismissErrorMessage = {},
        ) {
            Text("Here goes the content")
        }
    }
}

@PreviewAppScreen
@Composable
private fun AppScreenNoToolbarPreview() {
    AndroidArchitectureTheme {
        AppScreen(
            toolbarTitle = "Login",
            toolbarAvailable = false,
            errorMessage = null,
            onDismissErrorMessage = {},
        ) {
            Text("Here goes the content")
        }
    }
}

@PreviewAppScreen
@Composable
private fun AppScreenWithErrorMessagePreview() {
    AndroidArchitectureTheme {
        AppScreen(
            toolbarTitle = "Login",
            errorMessage = "Questo è un messaggio di errore",
            onDismissErrorMessage = {},
        ) {
            Text("Here goes the content")
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