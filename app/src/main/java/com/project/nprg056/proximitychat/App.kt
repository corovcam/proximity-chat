package com.project.nprg056.proximitychat

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.project.nprg056.proximitychat.controller.Navigation
import com.project.nprg056.proximitychat.util.Destination
import com.project.nprg056.proximitychat.ui.theme.ProximityChatTheme
import com.project.nprg056.proximitychat.view.AuthenticationView
import com.project.nprg056.proximitychat.view.ChatView
import com.project.nprg056.proximitychat.view.LoginView
import com.project.nprg056.proximitychat.view.RegisterView

@Composable
fun App() {
    val navController = rememberNavController()
    val actions = remember(navController) { Navigation(navController) }

    ProximityChatTheme {
        NavHost(
            navController = navController,
            startDestination =
            if (FirebaseAuth.getInstance().currentUser != null)
                Destination.Chat
            else
                Destination.Authentication
        ) {
            composable(Destination.Authentication) {
                AuthenticationView(
                    register = actions.toRegister,
                    login = actions.toLogin
                )
            }
            composable(Destination.Register) {
                RegisterView(
                    chat = actions.toChat,
                    back = actions.navigateBack
                )
            }
            composable(Destination.Login) {
                LoginView(
                    chat = actions.toChat,
                    back = actions.navigateBack
                )
            }
            composable(Destination.Chat) {
                ChatView(
                    home = actions.toHome
                )
            }
        }
    }
}