package uk.co.itmms.androidArchitecture.components

import androidx.compose.foundation.background
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import uk.co.itmms.androidArchitecture.screens.PreviewAppScreen
import uk.co.itmms.androidArchitecture.ui.theme.AndroidArchitectureTheme

enum class AppTextType {
    Body, Title, Button, Error,
}

@Composable
fun AppText(
    text: String,
    modifier: Modifier = Modifier,
    type: AppTextType = AppTextType.Body,
    textAlign: TextAlign = TextAlign.Start,
    testTag: String = "",
    color: Color? = null,
) {
    val textColor = color ?: when (type) {
        AppTextType.Body -> Color.Black
        AppTextType.Title -> Color.Black
        AppTextType.Button -> MaterialTheme.typography.button.color
        AppTextType.Error -> Color.Red
    }
    Text(
        text = text,
        modifier = modifier
            .testTag(testTag),
        color = textColor,
        style = when (type) {
            AppTextType.Body -> MaterialTheme.typography.body1
            AppTextType.Title -> MaterialTheme.typography.h5
            AppTextType.Button -> MaterialTheme.typography.button
            AppTextType.Error -> MaterialTheme.typography.body1.copy(
                fontStyle = FontStyle.Italic,
                textAlign = TextAlign.Center,
            )
        },
        textAlign = textAlign,
    )
}

@PreviewAppScreen
@Composable
private fun AppTextBodyPreview() {
    AndroidArchitectureTheme {
        AppText(
            text = "This is a multiline text so we should be able to see that there is a standard space between two lines of text",
            type = AppTextType.Body,
            testTag = "AppTextBodyPreview",
        )
    }
}

@PreviewAppScreen
@Composable
private fun AppTextTitlePreview() {
    AndroidArchitectureTheme {
        AppText(
            modifier = Modifier
                .background(color = Color.Black),
            text = "Login",
            type = AppTextType.Title,
            testTag = "AppTextTitlePreview",
        )
    }
}

@PreviewAppScreen
@Composable
private fun AppTextButtonPreview() {
    AndroidArchitectureTheme {
        AppText(
            text = "Login",
            type = AppTextType.Button,
            testTag = "AppTextButtonPreview",
        )
    }
}

@PreviewAppScreen
@Composable
private fun AppTextErrorPreview() {
    AndroidArchitectureTheme {
        AppText(
            text = "It seems the device is not connect to Internet. Please verify the connection",
            type = AppTextType.Error,
            testTag = "AppTextErrorPreview",
        )
    }
}