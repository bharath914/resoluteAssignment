package com.bharath.resoluteaiassignment.presentation.profile

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.bharath.resoluteaiassignment.presentation.login.LoginViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Composable

fun ProfileScreen(
    navHostController: NavHostController
) {

    Surface {
        val profileViewModel :ProfileViewModel = hiltViewModel()
        val loginViewModel :LoginViewModel = hiltViewModel()
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val user = loginViewModel.user.collectAsState()

           val name =  user.value?.data?.email

            Text(text = "Email Id is : "+"${name}")

          val locationData =   LocationCurrent(context = LocalContext.current)
            val getReadableLocation = getReadableLocation(locationData.latitude,locationData.longitude, context = LocalContext.current)
            val isLoadingLocation = profileViewModel.loadingLocation.collectAsState()
            Spacer(modifier = Modifier.height(40.dp))
            Row {
                
            Text(text = "current location: ")
                if (isLoadingLocation.value){
                    CircularProgressIndicator()
                }
                else{
                    Text(text = getReadableLocation)
                }
            }

            

//
//            val singapore = LatLng(1.35, 103.87)
//            val cameraPositionState = rememberCameraPositionState {
//                position = CameraPosition.fromLatLngZoom(singapore, 10f)
//            }
//            GoogleMap(
//                modifier = Modifier.fillMaxSize(),
//                cameraPositionState = cameraPositionState
//            ) {
//                Marker(
//                    state = MarkerState(position = singapore),
//                    title = "Singapore",
//                    snippet = "Marker in Singapore"
//                )
//            }


            val context = LocalContext.current
            val currentUser = Firebase.auth.currentUser

            Toast.makeText(context, "${currentUser?.email}", Toast.LENGTH_SHORT).show()

            val isEmailVerified = remember {

            mutableStateOf( currentUser?.isEmailVerified)
            }



            val verifiedText = remember {
                mutableStateOf("Not Verified")
            }
            if (isEmailVerified.value == true){
                verifiedText.value = "Verified"
            }
            val startVerification = remember {
                mutableStateOf(false)
            }
            val buttonClickability = remember {
                mutableStateOf(true)
            }
            Spacer(modifier = Modifier.height(40.dp))
            Text(text = "Your Email is : "+verifiedText.value)
            Spacer(modifier = Modifier.height(40.dp))
            AnimatedVisibility(visible = isEmailVerified.value == false && buttonClickability.value) {

                Button(onClick = {
                    startVerification.value = !startVerification.value
                    buttonClickability.value = !buttonClickability.value

                }) {
                    Text(text = "Verify Email")

                }
            }
            val showText = remember {
                mutableStateOf(false)
            }
            AnimatedVisibility(visible = showText.value) {
                Text(text = "Please Check Your Inbox for Email Verification")
            }
            val newUser = FirebaseAuth.getInstance().currentUser
            Toast.makeText(context, "", Toast.LENGTH_SHORT).show()
             if (!newUser?.isEmailVerified!!){



                if (startVerification.value){

                    Toast.makeText(context, "Verification Started", Toast.LENGTH_SHORT).show()
                newUser.sendEmailVerification()
                    .addOnCompleteListener {
                    if (it.isSuccessful){

                      showText.value= true
                    }

                }
                    newUser.reload()
            }
            }

        }
    }
}