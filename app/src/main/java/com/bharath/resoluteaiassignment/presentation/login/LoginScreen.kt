package com.bharath.resoluteaiassignment.presentation.login

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.bharath.resoluteaiassignment.presentation.navigation.NavConst
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginUi(
    navHostController: NavHostController,
) {
    Surface {

        val loginViewModel: LoginViewModel = hiltViewModel()
        val context = LocalContext.current

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            var userEmail by remember {
                mutableStateOf("")
            }
            var passWord by remember {
                mutableStateOf("")
            }
            var launch by remember {
                mutableStateOf(false)
            }

            OutlinedTextField(value = userEmail, onValueChange = {
                userEmail = it

            }, leadingIcon = {
                Icon(imageVector = Icons.Default.Email, contentDescription = "Email")
            }, placeholder = {
                Text(text = "Enter Your Email Address")
            }, shape = RoundedCornerShape(10)
            )

            Spacer(modifier = Modifier.height(40.dp))
            OutlinedTextField(
                value = passWord,
                onValueChange = {
                    passWord = it

                },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Lock, contentDescription = "Password")
                },
                placeholder = {
                    Text(text = "Enter Your Password")
                },
                shape = RoundedCornerShape(10),
//                visualTransformation = VisualTransformation {
//
//                }
            )
            Spacer(modifier = Modifier.height(40.dp))

            OutlinedButton(
                onClick = {

                    loginViewModel.viewModelScope.launch(Dispatchers.IO) {

                        signIn(
                            userEmail,
                            passWord,
                            loginViewModel,
                            context,
                            navHostController
                        )
                    }


                },
                shape = RoundedCornerShape(10),
            ) {
                Text(text = "Log In", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(40.dp))
            Row (verticalAlignment = Alignment.CenterVertically){
                Text(
                    text = "Doesn't Have An Account?  ",
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(text = "Sign Up", fontWeight = FontWeight.Bold,
                    color = Color(0xF81C86E2),
                    modifier = Modifier.clickable {

                       navHostController.navigate(NavConst.signUp)
                    }
                )
            }

        }
    }
}


suspend fun signIn(
    email: String,
    password: String,
    viewModel: LoginViewModel,
    context: Context,
    navHostController: NavHostController,
) {

    val auth: FirebaseAuth = Firebase.auth





    auth.signInWithEmailAndPassword(email, password)


        .addOnCompleteListener { task ->
            val user = auth.currentUser
            if (task.isSuccessful) {
                viewModel.saveUserDetails(
                    SignInResult(
                        data = user
                    )
                )
                Toast.makeText(context, "SignInSuccessful with ${user?.email}", Toast.LENGTH_SHORT)
                    .show()
                navHostController.navigate(NavConst.profile)

            } else {
                Toast.makeText(context, "Invalid Username / Password", Toast.LENGTH_SHORT).show()
                viewModel.saveUserDetails(
                    SignInResult(
                        data = null
                    )
                )
            }

        }
}

suspend fun signUp(
    email: String,
    password: String,
    viewModel: LoginViewModel,
    context: Context,
    navHostController: NavHostController,
){

    val auth :FirebaseAuth = Firebase.auth
    try {


        auth.createUserWithEmailAndPassword(email, password)

            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(context, "Successfully Created !", Toast.LENGTH_SHORT).show()
                    val user = auth.currentUser
                    viewModel.saveUserDetails(
                        SignInResult(
                            data = user
                        )
                    )
                    navHostController.navigate(NavConst.profile)

                } else if (task.isCanceled) {
                    Toast.makeText(context, "Cancelled", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Authentication Failed", Toast.LENGTH_SHORT).show()
                }
            }
    }catch (e:Exception){
        e.printStackTrace()
    }
}