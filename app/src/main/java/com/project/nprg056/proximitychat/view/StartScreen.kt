package com.project.nprg056.proximitychat.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.project.nprg056.proximitychat.view.composables.Buttons
import com.project.nprg056.proximitychat.view.composables.InputTextField
import com.project.nprg056.proximitychat.view.composables.Title
import com.project.nprg056.proximitychat.viewmodel.QueueViewModel

@Composable
fun StartScreenView(
    toQueue: () -> Unit = {},
    queueViewModel: QueueViewModel = viewModel()
)
{
    val userName: String by queueViewModel.userName.observeAsState("")
    val loading: Boolean by queueViewModel.loading.observeAsState(initial = false)

    Surface(color = MaterialTheme.colorScheme.surface) {
        if (loading) {
            CircularProgressIndicator()
        }
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            Title(title = "Proximity Chat")
            InputTextField(
                value = userName,
                onValueChange = { queueViewModel.updateUserName(it) },
                label = "User Name",
                keyboardType = KeyboardType.Text,
                visualTransformation = VisualTransformation.None
            )
            Spacer(modifier = Modifier.height(20.dp))
            Buttons(
                title = "Start chatting",
                onClick = {
                    queueViewModel.registerUser()
                    toQueue()
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}
