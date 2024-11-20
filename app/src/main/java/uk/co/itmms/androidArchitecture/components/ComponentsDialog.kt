package uk.co.itmms.androidArchitecture.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import uk.co.itmms.androidArchitecture.R
import uk.co.itmms.androidArchitecture.screens.PreviewAppScreen
import uk.co.itmms.androidArchitecture.ui.theme.AndroidArchitectureTheme

@Composable
fun AppDialogProgress(
    modifier: Modifier = Modifier,
    message: String = stringResource(id = R.string.dialog_progress_title),
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = {},
        confirmButton = {},
        text = {
            AppText(
                modifier = Modifier
                    .fillMaxWidth(),
                text = message,
                textAlign = TextAlign.Center,
                //color = Color.Yellow,
            )
        }
    )
}

@Composable
fun AppDialogError(
    errorMessage: String,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
) {
    AppDialogContainer(
        modifier = modifier,
        title = stringResource(id = R.string.dialog_error_title),
        text = errorMessage,
        buttons = listOf(
            DialogButton(
                text = stringResource(id = R.string.dialog_button_ok),
                onClick = onDismiss,
            )
        ),
    )
}

@Composable
fun AppDialogInformation(
    informationMessage: String,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
) {
    AppDialogContainer(
        modifier = modifier,
        title = stringResource(id = R.string.dialog_information_title),
        text = informationMessage,
        buttons = listOf(
            DialogButton(
                text = stringResource(id = R.string.dialog_button_ok),
                onClick = onDismiss,
            )
        ),
    )
}

@Composable
fun AppDialogMessage(
    title: String,
    message: String,
    modifier: Modifier = Modifier,
    buttonText: String = stringResource(id = R.string.dialog_button_ok),
    onDismiss: () -> Unit,
) {
    AppDialogContainer(
        modifier = modifier,
        title = title,
        text = message,
        buttons = listOf(
            DialogButton(
                text = buttonText,
                onClick = onDismiss,
            )
        ),
    )
}

@Composable
fun AppDialogRequestConfirmation(
    message: String,
    modifier: Modifier = Modifier,
    buttonPositive: String = stringResource(id = R.string.dialog_button_yes),
    buttonNegative: String = stringResource(id = R.string.dialog_button_no),
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AppDialogContainer(
        modifier = modifier,
        title = stringResource(id = R.string.dialog_confirmationRequest_title),
        text = message,
        buttons = listOf(
            DialogButton(
                text = buttonPositive,
                onClick = onConfirm,
            ),
            DialogButton(
                text = buttonNegative,
                onClick = onDismiss,
            )
        ),
    )
}

private data class DialogButton(
    val text: String,
    val onClick: () -> Unit,
)

@Composable
private fun AppDialogContainer(
    title: String,
    text: String,
    modifier: Modifier = Modifier,
    buttons: List<DialogButton> = emptyList(),
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = {},
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = true,
            decorFitsSystemWindows = true
        ),
        buttons = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                buttons.forEach { button ->
                    AppButtonDialog(
                        text = button.text,
                        onClick = button.onClick,
                    )
                }
            }
        },
        title = {
            AppText(
                modifier = Modifier
                    .fillMaxWidth(),
                text = title,
                type = AppTextType.Title,
            )
        },
        text = {
            AppText(
                modifier = Modifier
                    .fillMaxWidth(),
                text = text,
                type = AppTextType.Body,
            )
        },
        contentColor = Color.Black,
    )
}

@Composable
private fun RowScope.AppButtonDialog(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Button(
        modifier = Modifier
            .weight(1f)
            .then(modifier),
        onClick = onClick,
    ) {
        AppText(
            text = text,
            type = AppTextType.Button,
        )
    }
}

@PreviewAppScreen
@Composable
private fun AppDialogProgressPreview() {
    AndroidArchitectureTheme {
        AppDialogProgress()
    }
}

@PreviewAppScreen
@Composable
private fun AppDialogProgressWithCustomMessagePreview() {
    AndroidArchitectureTheme {
        AppDialogProgress(
            message = "Printing..."
        )
    }
}

@PreviewAppScreen
@Composable
private fun AppDialogErrorPreview() {
    AndroidArchitectureTheme {
        AppDialogError(
            errorMessage = "This is an error message",
            onDismiss = {},
        )
    }
}

@PreviewAppScreen
@Composable
private fun AppDialogInformationPreview() {
    AndroidArchitectureTheme {
        AppDialogInformation(
            informationMessage = "This is an information message",
            onDismiss = {},
        )
    }
}

@PreviewAppScreen
@Composable
private fun AppDialogMessagePreview() {
    AndroidArchitectureTheme {
        AppDialogMessage(
            title = "Custom title",
            message = "This is a custom message",
            onDismiss = {},
        )
    }
}

@PreviewAppScreen
@Composable
private fun AppDialogMessageWithCustomButtonPreview() {
    AndroidArchitectureTheme {
        AppDialogMessage(
            title = "Custom title",
            message = "This is a custom message",
            buttonText = "Custom button text",
            onDismiss = {},
        )
    }
}

@PreviewAppScreen
@Composable
private fun AppDialogRequestConfirmationPreview() {
    AndroidArchitectureTheme {
        AppDialogRequestConfirmation(
            message = "Are you sure you want to delete the file?",
            onConfirm = {},
            onDismiss = {},
        )
    }
}

@PreviewAppScreen
@Composable
private fun AppDialogRequestConfirmationWithCustomButtonsPreview() {
    AndroidArchitectureTheme {
        AppDialogRequestConfirmation(
            message = "Are you sure you want to delete the file?",
            buttonPositive = "Certo !",
            buttonNegative = "No !!",
            onConfirm = {},
            onDismiss = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AppDialogContainerPreview() {
    AndroidArchitectureTheme {
        AppDialogContainer(
            title = "This is a dialog",
            text = "This is the text",
            buttons = listOf(
                DialogButton(
                    text = stringResource(id = R.string.dialog_button_ok),
                    onClick = {},
                ),
                DialogButton(
                    text = stringResource(id = R.string.dialog_button_cancel),
                    onClick = {},
                )
            ),
        )
    }
}