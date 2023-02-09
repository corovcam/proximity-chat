package com.project.nprg056.proximitychat.controller

import androidx.navigation.NavHostController
import com.project.nprg056.proximitychat.util.Destination

class Navigation(navController: NavHostController) {
    val toLogin: () -> Unit = { navController.navigate(Destination.Login) }
    val toRegister: () -> Unit = { navController.navigate(Destination.Register) }
    val navigateBack: () -> Unit = { navController.popBackStack() }
    val toChat: () -> Unit = {
        navController.navigate(Destination.Chat) {
            popUpTo(Destination.Login) {
                inclusive = true
            }
            popUpTo(Destination.Register) {
                inclusive = true
            }
        }
    }
    val toHome: () -> Unit = {
        navController.navigate(Destination.Authentication) {
            popUpTo(Destination.Chat) {
                inclusive = true
            }
            popUpTo(Destination.SplashScreen) {
                inclusive = true
            }
        }
    }
}