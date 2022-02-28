package com.elt.passsystem.extensions

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import com.elt.passsystem.screens.ScreenTestTag

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