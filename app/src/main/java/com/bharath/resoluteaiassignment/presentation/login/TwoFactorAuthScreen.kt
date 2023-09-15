package com.bharath.resoluteaiassignment.presentation.login

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.MultiFactorSession
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import com.google.firebase.auth.PhoneMultiFactorAssertion
import com.google.firebase.auth.PhoneMultiFactorGenerator
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TwoFactorAuthScreen(
    navHostController: NavHostController,
) {
    Surface {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val verificationCode = remember {
                mutableStateOf("")
            }
            val context = LocalContext.current

            OutlinedTextField(value = verificationCode.value, onValueChange = {
                verificationCode.value = it


            })
            Button(onClick = {

                twoAuth(context, verificationCode = verificationCode.value)

            }) {
                Text("Authenticate")
            }
        }
    }
}

fun twoAuth(
    context: Context,
    verificationCode :String
){

    val user = Firebase.auth.currentUser
    var credential :PhoneAuthCredential ?= null

    var verificationId : String = ""
    var forceResendingToken : ForceResendingToken
    val callbacks  = object : OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(p0: PhoneAuthCredential) {
            credential = p0
        }

        override fun onVerificationFailed(p0: FirebaseException) {
            if (p0 is FirebaseAuthInvalidCredentialsException) {
                Toast.makeText(context, "Invalid Request", Toast.LENGTH_SHORT).show()
            } else if (p0 is FirebaseTooManyRequestsException) {
                Toast.makeText(context, "Too Many Requests", Toast.LENGTH_SHORT).show()
            }

        }

        override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
            super.onCodeSent(p0, p1)
            verificationId = p0
            forceResendingToken = p1


        }
    }

        user?.multiFactor?.session?.addOnCompleteListener {task->
        if (task.isSuccessful){
            val multiFactorSession :MultiFactorSession ? = task.result
            val phoneOptions  = PhoneAuthOptions.newBuilder()
                .setPhoneNumber("+917777777777")
                .setTimeout(30L,TimeUnit.SECONDS)

                .requireSmsValidation(true)
                .setCallbacks(callbacks)
                .build()
            PhoneAuthProvider.verifyPhoneNumber(phoneOptions)
        }
    }

    val cred = PhoneAuthProvider
        .getCredential(verificationId,verificationCode)
    val multiFactorAssertion =  PhoneMultiFactorGenerator.getAssertion(cred)
    FirebaseAuth.getInstance()
        .currentUser
        ?.multiFactor
        ?.enroll(multiFactorAssertion,"+917777777777")
        ?.addOnCompleteListener {
            Toast.makeText(context, "Completed", Toast.LENGTH_SHORT).show()
        }






}