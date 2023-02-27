package com.project.nprg056.proximitychat.view.composables

import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DialogCircularProgressIndicator() {
    CircularProgressIndicator(
        modifier = Modifier
            .size(200.dp),
        strokeWidth = 10.dp,
        color = MaterialTheme.colorScheme.primary
    )
}