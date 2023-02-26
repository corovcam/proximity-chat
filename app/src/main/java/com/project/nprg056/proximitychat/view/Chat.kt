package com.project.nprg056.proximitychat.view

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.project.nprg056.proximitychat.util.Constants
import com.project.nprg056.proximitychat.view.composables.Appbar
import com.project.nprg056.proximitychat.view.composables.SingleMessage
import com.project.nprg056.proximitychat.viewmodel.ChatViewModel

@Composable
fun ChatView(
    roomId: String?,
    goBack: () -> Unit = {},
    chatViewModel: ChatViewModel = viewModel()
) {
    val message: String by chatViewModel.message.observeAsState(initial = "")
    val messages: List<Map<String, Any>> by chatViewModel.messages.observeAsState(
        initial = emptyList<Map<String, Any>>().toMutableList()
    )
    /*Log.w("roomId", roomId!!)*/
    Surface(color = MaterialTheme.colorScheme.surface) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            Appbar(
                title = "Chat",
                action = goBack
            )
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(weight = 0.85f, fill = true),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                reverseLayout = true
            ) {
                items(messages) { message ->
                    val isCurrentUser = message[Constants.IS_CURRENT_USER] as Boolean

                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement =
                        if (isCurrentUser)
                            Arrangement.End
                        else
                            Arrangement.Start
                    ) {
                        SingleMessage(
                            message = message[Constants.MESSAGE].toString(),
                            isCurrentUser = isCurrentUser
                        )
                    }
                }
            }
            OutlinedTextField(
                value = message,
                onValueChange = {
                    chatViewModel.updateMessage(it)
                },
                label = {
                    Text(
                        "Type Your Message"
                    )
                },
                maxLines = 1,
                modifier = Modifier
                    .padding(horizontal = 15.dp, vertical = 1.dp)
                    .fillMaxWidth()
                    .weight(weight = 0.09f, fill = true),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text
                ),
                singleLine = true,
                trailingIcon = {
                    IconButton(
                        onClick = {
                            chatViewModel.addMessage()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Send,
                            contentDescription = "Send Button"
                        )
                    }
                }
            )
        }
    }
}