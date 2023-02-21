package uk.co.itmms.androidArchitecture.widgets

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import uk.co.itmms.androidArchitecture.ui.theme.AndroidArchitectureTheme
import uk.co.itmms.androidArchitecture.ui.theme.Red

@Composable
fun TextTitleScreen(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.h1,
    )
}

@Composable
fun TextBody(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.body1,
    )
}

@Composable
fun TextButton(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.typography.button.color,
) {
    Text(
        modifier = modifier,
        text = text,
        color = color,
        style = MaterialTheme.typography.button,
    )
}

@Composable
fun TextError(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Red,
) {
    Text(
        modifier = modifier,
        text = text,
        color = color,
        style = MaterialTheme.typography.body1.copy(
            fontStyle = FontStyle.Italic,
            textAlign = TextAlign.Center,
        ),
    )
}

@Composable
@Preview(showBackground = true)
fun TextTitleScreenPreview() {
    AndroidArchitectureTheme {
        TextTitleScreen("Login")
    }
}

@Composable
@Preview(showBackground = true)
fun TextBodyPreview() {
    AndroidArchitectureTheme {
        TextBody("This is a multiline text so we should be able to see that there is a standard space between two lines of text")
    }
}

@Composable
@Preview(showBackground = true)
fun TextButtonPreview() {
    AndroidArchitectureTheme {
        TextButton(
            text = "Login",
            color = MaterialTheme.colors.background,
        )
    }
}

@Composable
@Preview(showBackground = true)
fun TextErrorPreview() {
    AndroidArchitectureTheme {
        TextError(
            text = "It seems the device is not connect to Internet. Please verify the connection",
        )
    }
}