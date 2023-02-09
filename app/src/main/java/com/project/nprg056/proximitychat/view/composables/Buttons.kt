package com.project.nprg056.proximitychat.view.composables

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun Buttons(title: String,
            containerColor: Color = MaterialTheme.colorScheme.primary,
            contentColor: Color = MaterialTheme.colorScheme.onPrimary,
            onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        modifier = Modifier.padding(vertical = 20.dp).requiredHeight(50.dp),
    ) {
        Text(
            text = title
        )
    }
}