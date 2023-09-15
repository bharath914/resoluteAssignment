package com.bharath.resoluteaiassignment

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.compose.rememberNavController
import com.bharath.resoluteaiassignment.presentation.login.LoginViewModel
import com.bharath.resoluteaiassignment.presentation.login.SignInResult
import com.bharath.resoluteaiassignment.presentation.navigation.MyNavHost
import com.bharath.resoluteaiassignment.presentation.navigation.NavConst
import com.bharath.resoluteaiassignment.ui.theme.ResoluteAiAssignmentTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var auth: FirebaseAuth

    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ResoluteAiAssignmentTheme {
                // A surface container using the 'background' color from the theme
                Surface(

                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    val permission = rememberPermissionState(
                        permission =
                        Manifest.permission.ACCESS_FINE_LOCATION
                    )
                    val permission2 = rememberPermissionState(permission =
                    Manifest.permission.ACCESS_COARSE_LOCATION)
                    val lifecycleowner = LocalLifecycleOwner.current
                    DisposableEffect(key1 = lifecycleowner) {
                        val obs = LifecycleEventObserver { _, event ->
                            if (event == Lifecycle.Event.ON_RESUME) {
                                permission.launchPermissionRequest()
                                permission2.launchPermissionRequest()
                            }

                        }
                        lifecycleowner.lifecycle.addObserver(obs)
                        onDispose {
lifecycleowner.lifecycle.removeObserver(obs)
                        }
                    }

                    val navHostController = rememberNavController()

                    val loginViewModel: LoginViewModel = hiltViewModel()
                    auth = Firebase.auth
                    val currentUser = auth.currentUser
                    if (currentUser == null) {
                        MyNavHost(
                            navHostController = navHostController,
                            startDestination = NavConst.login
                        )
                    } else {

                        loginViewModel.saveUserDetails(
                            SignInResult(
                                data = currentUser
                            )
                        )
                        MyNavHost(
                            navHostController = navHostController,
                            startDestination = NavConst.profile
                        )


                    }


                }
            }
        }
    }


}
