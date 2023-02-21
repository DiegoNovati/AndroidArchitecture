package uk.co.itmms.androidArchitecture.widgets

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import uk.co.itmms.androidArchitecture.R
import uk.co.itmms.androidArchitecture.ui.theme.AndroidArchitectureTheme

@Composable
fun ButtonRoundedEdgesPrimary(
    @StringRes stringId: Int,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    ButtonRoundedEdges(
        modifier = modifier
            .height(50.dp),
        enabled = enabled,
        color = MaterialTheme.colors.primary,
        onClick = { onClick() },
    ) {
        TextButton(
            text = stringResource(id = stringId),
            color = MaterialTheme.colors.background
        )
    }
}

@Composable
fun ButtonRoundedEdgesSecondary(
    @StringRes stringId: Int,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    ButtonRoundedEdges(
        modifier = modifier
            .height(50.dp),
        enabled = enabled,
        color = MaterialTheme.colors.secondary,
        onClick = { onClick() },
    ) {
        TextButton(
            text = stringResource(id = stringId),
            color = if (enabled) Color.White else MaterialTheme.colors.background,
        )
    }
}

@Composable
fun ButtonRoundedEdges(
    modifier: Modifier = Modifier,
    color: Color,
    enabled: Boolean = true,
    onClick: () -> Unit,
    content: @Composable RowScope.() -> Unit
) {
    Button(
        shape = CircleShape,
        modifier = modifier
            .height(50.dp),
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(backgroundColor = color),
        onClick = { onClick() },
        content = content
    )
}

@Composable
@Preview(showBackground = true)
fun ButtonRoundedEdgesPrimaryPreview() {
    AndroidArchitectureTheme(darkTheme = false) {
        Surface {
            ButtonRoundedEdgesPrimary(stringId = R.string.appName) {}
        }
    }
}

@Composable
@Preview(showBackground = true)
fun ButtonRoundedEdgesSecondaryPreview() {
    AndroidArchitectureTheme(darkTheme = false) {
        Surface {
            ButtonRoundedEdgesSecondary(stringId = R.string.appName) {}
        }
    }
}

@Composable
@Preview(showBackground = true)
fun ButtonRoundedEdgesEnabledPreview() {
    AndroidArchitectureTheme(darkTheme = false) {
        Surface {
            ButtonRoundedEdges(
                color = MaterialTheme.colors.primary,
                enabled = true,
                onClick = {},
            ) {
                Text(text = "Enabled")
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun ButtonRoundedEdgesDisabledPreview() {
    AndroidArchitectureTheme(darkTheme = false) {
        Surface {
            ButtonRoundedEdges(
                color = MaterialTheme.colors.primary,
                enabled = false,
                onClick = {},
            ) {
                Text(text = "Disabled")
            }
        }
    }
}
