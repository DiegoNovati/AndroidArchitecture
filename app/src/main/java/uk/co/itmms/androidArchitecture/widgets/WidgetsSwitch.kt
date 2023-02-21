package uk.co.itmms.androidArchitecture.widgets

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
fun SwitchRow(
    text: String,
    checked: Boolean,
    modifier: Modifier = Modifier,
    testTag: String = "Switch",
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        TextBody(
            text = text
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
fun SwitchRowCheckedPreview() {
    AndroidArchitectureTheme {
        SwitchRow(
            text = "This switch is on",
            checked = true,
            onCheckedChange = {},
        )
    }
}

@Composable
@Preview(showBackground = true)
fun SwitchRowUncheckedPreview() {
    AndroidArchitectureTheme {
        SwitchRow(
            text = "This switch is off",
            checked = false,
            onCheckedChange = {},
        )
    }
}