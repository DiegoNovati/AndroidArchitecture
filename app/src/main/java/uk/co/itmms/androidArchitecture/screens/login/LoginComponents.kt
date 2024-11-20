package uk.co.itmms.androidArchitecture.screens.login

import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import uk.co.itmms.androidArchitecture.R
import uk.co.itmms.androidArchitecture.screens.PreviewAppScreen
import uk.co.itmms.androidArchitecture.ui.theme.AndroidArchitectureTheme

@Composable
fun LoginTopBar(
    connected: Boolean,
) {
    val connectedText = if (!connected) stringResource(id = R.string.connectionMissing) else ""
    TopAppBar(
        title = {
            Text(
                modifier = Modifier.testTag(LoginScreenTestTag.TitleText.name),
                text = stringResource(id = R.string.loginTitle, connectedText),
            )
        }
    )
}

@PreviewAppScreen
@Composable
private fun LoginTopBarConnectedPreview() {
    AndroidArchitectureTheme {
        LoginTopBar(
            connected = true,
        )
    }
}

@PreviewAppScreen
@Composable
private fun LoginTopBarNotConnectedPreview() {
    AndroidArchitectureTheme {
        LoginTopBar(
            connected = false,
        )
    }
}