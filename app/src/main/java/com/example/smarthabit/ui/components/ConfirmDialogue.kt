package com.example.smarthabit.ui.components
import androidx.compose.material3.*
import androidx.compose.runtime.Composable

/**
 * Simple reusable alert dialog used to display confirm messages to the user.
 */
@Composable
fun ConfirmDialog(
    title: String,
    message: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {

    // Uses AlertDialog to show a simple message to the user and get feedback
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = { Text(message) },

        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Yes")
            }
        },

        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}