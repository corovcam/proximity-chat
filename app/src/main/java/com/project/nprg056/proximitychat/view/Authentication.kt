package com.project.nprg056.proximitychat.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.project.nprg056.proximitychat.ui.theme.ProximityChatTheme
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
                .fillMaxWidth()
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            Title(title = "Proximity Chat")
            Buttons(title = "Register", onClick = register, backgroundColor = Color.Blue)
            Buttons(title = "Login", onClick = login, backgroundColor = Color.Magenta)
        }
    }
}
