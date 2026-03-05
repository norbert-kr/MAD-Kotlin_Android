package com.example.smarthabit.ui.components
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable


/**
 * Simple reusable alert dialog used to display alert messages to the user.
 */
@Composable
fun AlertDialogMessage(
    title: String,
    message: String,
    onDismiss: () -> Unit
) {

    // Uses AlertDialog to show a simple message to the user
    AlertDialog(
        onDismissRequest = onDismiss,

        title = {
            Text(title)
        },

        text = {
            Text(message)
        },

        confirmButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text("OK")
            }
        }
    )
}