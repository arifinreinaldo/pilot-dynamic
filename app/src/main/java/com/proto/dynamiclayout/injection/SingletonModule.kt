package com.proto.dynamiclayout.injection

import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import androidx.annotation.RequiresApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class SingletonModule {
    @RequiresApi(Build.VERSION_CODES.M)
    @Singleton
    @Provides
    fun connectivity(@ApplicationContext context: Context) =
        context.getSystemService(ConnectivityManager::class.java) as ConnectivityManager
}