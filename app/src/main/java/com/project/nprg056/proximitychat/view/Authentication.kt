package com.project.nprg056.proximitychat.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.project.nprg056.proximitychat.view.composables.Buttons
import com.project.nprg056.proximitychat.view.composables.Title

@Composable
fun AuthenticationView(
    register: () -> Unit = {},
    login: () -> Unit = {})
{
    Surface(color = MaterialTheme.colorScheme.background) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            Title(title = "Proximity Chat")
            Buttons(
                title = "Login",
                onClick = login,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
            Buttons(
                title = "Register",
                onClick = register,
                containerColor = MaterialTheme.colorScheme.tertiary,
                contentColor = MaterialTheme.colorScheme.onTertiary
            )
        }
    }
}
