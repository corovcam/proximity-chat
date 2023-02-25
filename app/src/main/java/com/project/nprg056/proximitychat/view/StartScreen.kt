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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import com.project.nprg056.proximitychat.model.LocationDetail
import com.project.nprg056.proximitychat.view.composables.Buttons
import com.project.nprg056.proximitychat.view.composables.InputTextField
import com.project.nprg056.proximitychat.view.composables.LoadingDialog
import com.project.nprg056.proximitychat.view.composables.Title
import com.project.nprg056.proximitychat.viewmodel.QueueViewModel

@Composable
fun StartScreenView(
    toQueue: () -> Unit = {},
    queueViewModel: QueueViewModel = viewModel(),
    fusedLocationClient: FusedLocationProviderClient
)
{
    val userName: String by queueViewModel.userName.observeAsState("")
    val loading: Boolean by queueViewModel.loading.observeAsState(initial = false)

    val context = LocalContext.current
    val permissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    val obtainCurrentLocation = {
        queueViewModel.updateLoadingStatus(true)
        fusedLocationClient
            .getCurrentLocation(
                LocationRequest.PRIORITY_HIGH_ACCURACY,
                object : CancellationToken() {
                    override fun onCanceledRequested(p0: OnTokenCanceledListener) =
                        CancellationTokenSource().token
                    override fun isCancellationRequested() = false
                }
            )
            .addOnSuccessListener { location ->
                if (location != null) {
                    Log.w("Location", "${location.latitude} ${location.longitude}")
                    queueViewModel.registerUser(
                        LocationDetail(location.latitude.toString(), location.longitude.toString()),
                        context = context,
                        toQueue = toQueue
                    )
                } else {
                    Toast.makeText(
                        context, "Check your GPS/Network settings and try again.",
                        Toast.LENGTH_LONG)
                        .show()
                    queueViewModel.updateLoadingStatus(false)
                }
            }
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
            Toast.makeText(context, "Please turn on Location permission.", Toast.LENGTH_LONG)
                .show()
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
