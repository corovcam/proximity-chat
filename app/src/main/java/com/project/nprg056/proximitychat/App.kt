package com.project.nprg056.proximitychat

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.android.gms.location.FusedLocationProviderClient
import com.project.nprg056.proximitychat.controller.Navigation
import com.project.nprg056.proximitychat.util.Destination
import com.project.nprg056.proximitychat.ui.theme.ProximityChatTheme
import com.project.nprg056.proximitychat.view.*
import com.project.nprg056.proximitychat.viewmodel.QueueViewModel

@Composable
fun App(
    fusedLocationClient: FusedLocationProviderClient
) {
    val navController = rememberNavController()
    val actions = remember(navController) { Navigation(navController) }
    val queueViewModel: QueueViewModel = viewModel()

    ProximityChatTheme {
        NavHost(
            navController = navController,
            startDestination = Destination.SplashScreen
        ) {
            composable(Destination.SplashScreen) {
                SplashScreenView(
                    home = actions.toStartScreen
                )
            }
            composable(Destination.StartScreen) {
                StartScreenView(
                    toQueue = actions.toQueue,
                    toChat = actions.toChatRoom,
                    goBack = actions.navigateBack,
                    queueViewModel = queueViewModel,
                    fusedLocationClient = fusedLocationClient
                )
            }
            composable(Destination.Queue) {
                QueueView(
                    goBack = actions.navigateBack,
                    queueViewModel = queueViewModel
                )
            }
            composable(
                route = "${Destination.Chat}/{roomId}?userId={userId}",
                arguments = listOf(
                    navArgument("userId") { defaultValue = "" }
                )
            ) { backStackEntry ->
                val roomId = backStackEntry.arguments?.getString("roomId")
                val userId = backStackEntry.arguments?.getString("userId")
                if (roomId == null || userId == null) {
                    Log.e("Chat Navigation",
                        "Invalid route. Either roomId or userId is empty.")
                    return@composable
                }
                ChatView(
                    roomId = roomId,
                    userId = userId
                )
            }
        }
    }
}