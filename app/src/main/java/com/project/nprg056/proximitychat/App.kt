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
                Destination.Home
            else
                Destination.Authentication
        ) {
            composable(Destination.Authentication) {
                AuthenticationView(
                    register = actions.register,
                    login = actions.login
                )
            }
            composable(Destination.Register) {
                RegisterView(
                    home = actions.home,
                    back = actions.navigateBack
                )
            }
            composable(Destination.Login) {
                LoginView(
                    home = actions.home,
                    back = actions.navigateBack
                )
            }
            composable(Destination.Home) {
                ChatView()
            }
        }
    }
}

/*
@Preview(
    showBackground = true
)
@Composable
fun PreviewAuth() {
    AuthenticationView(register = )
}

@Preview(
    showBackground = true
)
@Composable
fun PreviewRegistration() {
    RegisterView(
        home = actions.home,
        back = actions.navigateBack
    )
}

@Preview(
    showBackground = true
)
@Composable
fun PreviewLogin() {
    LoginView(
        home = actions.home,
        back = actions.navigateBack
    )
}*/
