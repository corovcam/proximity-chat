package com.project.nprg056.proximitychat.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.project.nprg056.proximitychat.view.composables.Appbar
import com.project.nprg056.proximitychat.view.composables.BackPressHandler
import com.project.nprg056.proximitychat.view.composables.LoadingDialog
import com.project.nprg056.proximitychat.viewmodel.QueueViewModel
import com.project.nprg056.proximitychat.R

@Composable
fun QueueView(
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
                    title = stringResource(R.string.proximity_queue_title),
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
                        text = stringResource(
                            R.string.queue_welcome_user_txt,
                            if (userName.isEmpty()) "" else " $userName"
                        ),
                        style = MaterialTheme.typography.headlineSmall,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(30.dp)
                    )
                    CircularProgressIndicator(
                        strokeWidth = 10.dp,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}