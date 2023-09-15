package com.bharath.resoluteaiassignment.di

import android.content.Context
import com.bharath.resoluteaiassignment.presentation.login.LoginViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideLoginViewModel(@ApplicationContext context: Context)=LoginViewModel(context)

}