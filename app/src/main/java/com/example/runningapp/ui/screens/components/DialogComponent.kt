package com.example.runningapp.ui.screens.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.runningapp.ui.theme.RunningAppTheme

@Composable
fun SimpleDialog(title: String, text: String, confirmText: String, onDismissRequest: () -> Unit) {
    AlertDialog(
        onDismissRequest = { onDismissRequest() },
        title = {
            Text(
                text = title,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        text = { Text(text = text) },
        confirmButton = {
            TextButton(onClick = { onDismissRequest() }) { Text(confirmText) }
        }
    )
}


@Preview(showBackground = true)
@Composable
fun MinimalDialogPreview() {
    RunningAppTheme {
        SimpleDialog(title = "Example Title", text = "Content here!!!", confirmText = "OK") {}
    }
}