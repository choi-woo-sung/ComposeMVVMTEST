package com.woosung.compose.network.module

import com.woosung.compose.network.remote.MainRemoteSource
import com.woosung.compose.network.remote.MainRemoteSourceImp
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RemoteModule {

    @Singleton
    @Binds
    fun bindsMainRemoteSource(
        loginRemoteSource: MainRemoteSourceImp,
    ): MainRemoteSource
}
