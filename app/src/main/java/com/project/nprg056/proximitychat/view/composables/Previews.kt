package com.project.nprg056.proximitychat.view.composables

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview

@Preview(showBackground = true, showSystemUi = true, device = Devices.PIXEL_3A)
@Composable
fun PreviewButtons() {
    Buttons(title = "Login", containerColor = MaterialTheme.colorScheme.primary) { }
}

@Preview(showBackground = true, showSystemUi = true, device = Devices.PIXEL_3A)
@Composable
fun PreviewAppBar() {
    Appbar(title = "Login") { }
}