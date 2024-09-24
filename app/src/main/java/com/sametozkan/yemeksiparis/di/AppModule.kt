package com.sametozkan.yemeksiparis.di

import com.sametozkan.yemeksiparis.data.datasource.YemekSiparisDataSource
import com.sametozkan.yemeksiparis.data.repo.YemekSiparisRepo
import com.sametozkan.yemeksiparis.retrofit.ApiUtils
import com.sametozkan.yemeksiparis.retrofit.YemekSiparisDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideYemekSiparisDao(): YemekSiparisDao = ApiUtils.getKisilerDao()

    @Provides
    @Singleton
    fun provideYemekSiparisDataSource(yemekSiparisDao: YemekSiparisDao)
            : YemekSiparisDataSource = YemekSiparisDataSource(yemekSiparisDao)

    @Provides
    @Singleton
    fun provideYemekSiparisRepo(yemekSiparisDataSource: YemekSiparisDataSource): YemekSiparisRepo =
        YemekSiparisRepo(yemekSiparisDataSource)
}