package com.project.nprg056.proximitychat.view

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.project.nprg056.proximitychat.R
import com.project.nprg056.proximitychat.view.composables.*
import com.project.nprg056.proximitychat.viewmodel.QueueViewModel

@Composable
fun StartScreenView(
    toQueue: () -> Unit = {},
    toChat: (String, String) -> Unit,
    goBack: () -> Unit = {},
    queueViewModel: QueueViewModel = viewModel(),
    fusedLocationClient: FusedLocationProviderClient
)
{
    val userName: String by queueViewModel.userName.observeAsState("")
    val loading: Boolean by queueViewModel.loading.observeAsState(initial = false)

    val context = LocalContext.current
    val showInfoToast: (Int) -> Unit = { resourceId ->
        Toast.makeText(context, resourceId, Toast.LENGTH_LONG).show()
    }
    val obtainCurrentLocation: () -> Unit = {
        queueViewModel.obtainCurrentLocation(
            fusedLocationClient,
            toQueue = toQueue,
            toChat = toChat,
            goBack = goBack,
            showInfoToast = showInfoToast
        )
    }
    val launcherMultiplePermissions = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissionsMap ->
        val areGranted = permissionsMap.values.reduce { acc, next -> acc && next }
        if (areGranted) {
            Log.w("Location Permission", "Location Permission Granted")
            obtainCurrentLocation()
        } else {
            Log.w("Location Permission", "Location Permission Denied")
            Toast.makeText(context, R.string.location_permission_request, Toast.LENGTH_LONG).show()
        }
    }

    Surface(color = MaterialTheme.colorScheme.surface) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            LoadingDialog(isShowingDialog = loading)
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom
            ) {
                Title(title = stringResource(R.string.app_name))
                InputTextField(
                    value = userName,
                    onValueChange = { queueViewModel.updateUserName(it) },
                    label = stringResource(R.string.user_name_label),
                    keyboardType = KeyboardType.Text,
                    visualTransformation = VisualTransformation.None
                )
                Spacer(modifier = Modifier.height(20.dp))
                Buttons(
                    title = stringResource(R.string.start_chatting),
                    onClick = {
                        val permissions = arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        )
                        if (permissions.all {
                                ContextCompat.checkSelfPermission(
                                    context,
                                    it
                                ) == PackageManager.PERMISSION_GRANTED
                            }) {
                            obtainCurrentLocation()
                        } else {
                            launcherMultiplePermissions.launch(permissions)
                        }
                    },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}
