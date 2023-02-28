package com.project.nprg056.proximitychat.controller

import androidx.navigation.NavHostController
import com.project.nprg056.proximitychat.util.Destination

class Navigation(navController: NavHostController) {
    val toQueue: () -> Unit = { navController.navigate(Destination.Queue) }
    val navigateBack: () -> Unit = { navController.popBackStack() }
    val toChatRoom: (String, String, String) -> Unit =  { roomId, userId, distance ->
        navController.navigate("${Destination.Chat}/$roomId?userId=$userId&usersDistance=$distance")
    }
    val toStartScreen: () -> Unit = {
        navController.navigate(Destination.StartScreen) {
            popUpTo(Destination.Chat) {
                inclusive = true
            }
            popUpTo(Destination.SplashScreen) {
                inclusive = true
            }
        }
    }
}