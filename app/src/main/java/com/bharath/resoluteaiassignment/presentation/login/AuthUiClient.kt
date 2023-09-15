package com.bharath.resoluteaiassignment.presentation.login

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class AuthUiClient(
    private val context: Context
) {
    private lateinit var auth : FirebaseAuth

    suspend fun signIn(email:String,password:String){
        auth=  Firebase.auth
        auth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener{task ->

                if(task.isSuccessful){
                    val user = auth.currentUser
                }
            }
    }

}