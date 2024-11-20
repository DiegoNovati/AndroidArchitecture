package uk.co.itmms.androidArchitecture.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import uk.co.itmms.androidArchitecture.screens.PreviewAppScreen
import uk.co.itmms.androidArchitecture.ui.theme.AndroidArchitectureTheme

enum class AppTextFieldType {
    Email, Password,
}

@Composable
fun AppTextField(
    label: String,
    value: String,
    type: AppTextFieldType,
    testTag: String,
    enabled: Boolean = true,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit,
) {
    OutlinedTextField(
        modifier = Modifier
            .testTag(testTag)
            .then(modifier),
        label = {
            AppText(
                text = label,
                type = AppTextType.Body,
                testTag = label,
            )
        },
        value = value,
        enabled = enabled,
        keyboardOptions = KeyboardOptions(keyboardType = if (type == AppTextFieldType.Password) KeyboardType.Password else KeyboardType.Email),
        visualTransformation = if (type == AppTextFieldType.Password) PasswordVisualTransformation() else VisualTransformation.None,
        onValueChange = onValueChange,
    )
}

@PreviewAppScreen
@Composable
private fun AppTextFieldEmptyPreview() {
    AndroidArchitectureTheme {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            AppTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                label = "Email",
                value = "",
                type = AppTextFieldType.Email,
                testTag = "Email",
                onValueChange = {},
            )
            AppTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                label = "Password",
                value = "",
                type = AppTextFieldType.Password,
                testTag = "Password",
                onValueChange = {},
            )
        }
    }
}

@PreviewAppScreen
@Composable
private fun AppTextFieldWithValuesPreview() {
    AndroidArchitectureTheme {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            AppTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                label = "Email",
                value = "email@gmail.com",
                type = AppTextFieldType.Email,
                testTag = "Email",
                onValueChange = {},
            )
            AppTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                label = "Password",
                value = "This is the password",
                type = AppTextFieldType.Password,
                testTag = "Password",
                onValueChange = {},
            )
        }
    }
}