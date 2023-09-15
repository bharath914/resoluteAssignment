package com.bharath.resoluteaiassignment.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.bharath.resoluteaiassignment.presentation.login.LoginUi
import com.bharath.resoluteaiassignment.presentation.login.SignUpScreen
import com.bharath.resoluteaiassignment.presentation.login.TwoFactorAuthScreen
import com.bharath.resoluteaiassignment.presentation.profile.ProfileScreen

@Composable
fun MyNavHost(
    navHostController: NavHostController,
    startDestination :String
) {
    NavHost(navController = navHostController,
        startDestination = startDestination,
        builder = {


            composable(NavConst.login) {
                LoginUi(navHostController = navHostController)
            }
            composable(NavConst.profile) {
                ProfileScreen(navHostController)

            }
            composable(NavConst.signUp){
                SignUpScreen(navHostController)
            }
            composable(NavConst.twoFactorScreen){
                TwoFactorAuthScreen(navHostController)
            }
        })
}