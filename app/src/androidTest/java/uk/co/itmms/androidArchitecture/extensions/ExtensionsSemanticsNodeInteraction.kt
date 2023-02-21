package uk.co.itmms.androidArchitecture.extensions

import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.assertTextEquals
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue

fun SemanticsNodeInteraction.assertDeepTextEquals(text: String) {
    for ((key, value) in this.fetchSemanticsNode().config) {
        // Checking text for OutlinedTextField
        if (key.name == "EditableText") {
            assertEquals(text, value.toString())
            return
        }
    }
    this.assertTextEquals(text)
}

fun SemanticsNodeInteraction.assertDeepTextContains(
    text: String,
    ignoreCase: Boolean = false,
) {
    for ((key, value) in this.fetchSemanticsNode().config) {
        // Checking text for OutlinedTextField
        if (key.name == "EditableText") {
            assertTrue(value.toString().contains(text, ignoreCase))
            return
        }
    }
    this.assertTextContains(text)
}
