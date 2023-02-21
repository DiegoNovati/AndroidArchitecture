package uk.co.itmms.androidArchitecture.extensions

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import uk.co.itmms.androidArchitecture.screens.login.ScreenTestTag

fun ScreenTestTag.isEnabled(composeContentTestRule: ComposeContentTestRule) =
    composeContentTestRule
        .onNodeWithTag(this.name)
        .assertIsEnabled()

fun ScreenTestTag.isNotEnabled(composeContentTestRule: ComposeContentTestRule) =
    composeContentTestRule
        .onNodeWithTag(this.name)
        .assertIsNotEnabled()

fun ScreenTestTag.isEmpty(composeContentTestRule: ComposeContentTestRule) =
    composeContentTestRule
        .onNodeWithTag(this.name)
        .assertDeepTextContains("")

fun ScreenTestTag.hasText(composeContentTestRule: ComposeContentTestRule, text: String) =
    composeContentTestRule
        .onNodeWithTag(this.name)
        .assertTextEquals(text)

fun ScreenTestTag.isEnabledAndEmpty(composeContentTestRule: ComposeContentTestRule) {
    composeContentTestRule
        .onNodeWithTag(this.name)
        .assertIsEnabled()
        .assertDeepTextEquals("")
}

fun ScreenTestTag.isEnabledAndNotEmpty(
    composeContentTestRule: ComposeContentTestRule,
    text: String,
) {
    composeContentTestRule
        .onNodeWithTag(this.name)
        .assertIsEnabled()
        .assertDeepTextEquals(text)
}

fun ScreenTestTag.isChecked(
    composeContentTestRule: ComposeContentTestRule,
) =
    composeContentTestRule
        .onNodeWithTag(this.name)
        .assertIsOn()

fun ScreenTestTag.isNotChecked(
    composeContentTestRule: ComposeContentTestRule,
) =
    composeContentTestRule
        .onNodeWithTag(this.name)
        .assertIsOff()

fun ScreenTestTag.type(
    composeContentTestRule: ComposeContentTestRule,
    text: String,
) =
    composeContentTestRule
        .onNodeWithTag(this.name)
        .performTextInput(text)

fun ScreenTestTag.tap(composeContentTestRule: ComposeContentTestRule) =
    composeContentTestRule
        .onNodeWithTag(this.name)
        .performClick()