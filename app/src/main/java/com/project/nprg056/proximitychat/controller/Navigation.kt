package com.project.nprg056.proximitychat.controller

import androidx.navigation.NavHostController
import com.project.nprg056.proximitychat.util.Destination

class Navigation(navController: NavHostController) {
    val home: () -> Unit = {
        navController.navigate(Destination.Home) {
            popUpTo(Destination.Login) {
                inclusive = true
            }
            popUpTo(Destination.Register) {
                inclusive = true
            }
        }
    }
    val login: () -> Unit = { navController.navigate(Destination.Login) }
    val register: () -> Unit = { navController.navigate(Destination.Register) }
    val navigateBack: () -> Unit = { navController.popBackStack() }
}