package com.bharath.resoluteaiassignment.presentation.profile

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor():ViewModel() {

    private val _loadingLocation = MutableStateFlow(true)
    val loadingLocation = _loadingLocation.asStateFlow()

    fun setLoadingLocation (value:Boolean){
        _loadingLocation.tryEmit(value)
    }
}