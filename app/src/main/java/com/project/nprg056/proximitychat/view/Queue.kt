package com.project.nprg056.proximitychat.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.project.nprg056.proximitychat.view.composables.Appbar
import com.project.nprg056.proximitychat.view.composables.BackPressHandler
import com.project.nprg056.proximitychat.view.composables.Buttons
import com.project.nprg056.proximitychat.view.composables.LoadingDialog
import com.project.nprg056.proximitychat.viewmodel.QueueViewModel

@Composable
fun QueueView(
    toChat: (String, String) -> Unit,
    goBack: () -> Unit = {},
    queueViewModel: QueueViewModel = viewModel()
) {
    val userName: String by queueViewModel.userName.observeAsState("")
    val loading: Boolean by queueViewModel.loading.observeAsState(initial = false)

    BackPressHandler(onBackPressed = {
        queueViewModel.deleteUser()
        goBack()
    })
    Surface(color = MaterialTheme.colorScheme.surface) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            LoadingDialog(isShowingDialog = loading)
            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                Appbar(
                    title = "Proximity Queue",
                    action = {
                        queueViewModel.deleteUser()
                        goBack()
                    }
                )
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize())
                {
                    Text(
                        text = "Welcome $userName. Please wait, we are searching for match.",
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(30.dp)
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                }
            }
        }
    }
}