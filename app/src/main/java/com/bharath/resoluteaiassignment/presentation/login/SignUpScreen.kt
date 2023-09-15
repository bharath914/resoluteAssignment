package com.bharath.resoluteaiassignment.presentation.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
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

                        signUp(
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
                Text(text = "Sign Up", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }


        }
    }
}
