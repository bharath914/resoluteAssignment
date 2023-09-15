package com.bharath.resoluteaiassignment.presentation.login

import android.content.Context
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class LoginViewModel  @Inject constructor(
    @ApplicationContext context: Context
) : ViewModel(){


    private val _user = MutableStateFlow(SignInResult())
    val user= _user.asStateFlow()

    fun saveUserDetails(signInResult: SignInResult){
        _user.tryEmit(signInResult)
    }
}