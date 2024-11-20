package uk.co.itmms.androidArchitecture.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Switch
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import uk.co.itmms.androidArchitecture.ui.theme.AndroidArchitectureTheme

@Composable
fun AppSwitch(
    text: String,
    checked: Boolean,
    modifier: Modifier = Modifier,
    testTag: String = "Switch",
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        AppText(
            text = text,
            type = AppTextType.Body,
            testTag = text,
        )
        Switch(
            modifier = Modifier
                .testTag(testTag),
            checked = checked,
            onCheckedChange = onCheckedChange,
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun AppSwitchCheckedPreview() {
    AndroidArchitectureTheme {
        AppSwitch(
            text = "This switch is on",
            checked = true,
            onCheckedChange = {},
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun AppSwitchUncheckedPreview() {
    AndroidArchitectureTheme {
        AppSwitch(
            text = "This switch is off",
            checked = false,
            onCheckedChange = {},
        )
    }
}