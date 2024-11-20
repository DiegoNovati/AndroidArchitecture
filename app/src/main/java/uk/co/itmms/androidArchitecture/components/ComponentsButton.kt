package uk.co.itmms.androidArchitecture.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import uk.co.itmms.androidArchitecture.R
import uk.co.itmms.androidArchitecture.screens.PreviewAppScreen
import uk.co.itmms.androidArchitecture.ui.theme.AndroidArchitectureTheme

enum class AppButtonType {
    Primary, Secondary,
}

@Composable
fun AppButton(
    @StringRes stringId: Int,
    type: AppButtonType,
    testTag: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    val text = stringResource(id = stringId)
    val colorText = if (enabled) Color.White else MaterialTheme.colors.background

    Button(
        shape = CircleShape,
        modifier = Modifier
            .height(50.dp)
            .testTag(testTag)
            .then(modifier),
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = when (type) {
                AppButtonType.Primary -> MaterialTheme.colors.primary
                AppButtonType.Secondary -> MaterialTheme.colors.secondary
            }
        ),
        onClick = onClick,
        content = {
            AppText(
                text = text,
                type = AppTextType.Button,
                color = colorText,
                testTag = text,
            )
        }
    )
}

@PreviewAppScreen
@Composable
fun AppButtonPrimaryPreview() {
    AndroidArchitectureTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            AppButton(
                modifier = Modifier
                    .fillMaxWidth(),
                stringId = R.string.appName,
                type = AppButtonType.Primary,
                testTag = "AppButtonPrimaryPreviewEnabled",
                onClick = {},
            )
            AppButton(
                modifier = Modifier
                    .fillMaxWidth(),
                stringId = R.string.appName,
                type = AppButtonType.Primary,
                testTag = "AppButtonPrimaryPreviewDisabled",
                enabled = false,
                onClick = {},
            )
        }
    }
}

@PreviewAppScreen
@Composable
fun AppButtonSecondaryPreview() {
    AndroidArchitectureTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            AppButton(
                modifier = Modifier
                    .fillMaxWidth(),
                stringId = R.string.appName,
                type = AppButtonType.Secondary,
                testTag = "AppButtonSecondaryPreviewEnabled",
                onClick = {},
            )
            AppButton(
                modifier = Modifier
                    .fillMaxWidth(),
                stringId = R.string.appName,
                type = AppButtonType.Secondary,
                enabled = false,
                testTag = "AppButtonSecondaryPreviewDisabled",
                onClick = {},
            )
        }
    }
}