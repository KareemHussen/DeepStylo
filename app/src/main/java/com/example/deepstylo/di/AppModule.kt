package com.example.deepstylo.di

import com.example.deepstylo.R
import com.example.deepstylo.api.ApiInterface
import com.example.deepstylo.repository.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
     fun apiClient(okHttpClient: OkHttpClient) : ApiInterface =  Retrofit.Builder()
        .baseUrl("http://144.217.15.17:8000/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()
        .create(ApiInterface::class.java)


    @Provides
    @Singleton
     fun okHttp() : OkHttpClient = OkHttpClient.Builder()
        .readTimeout(5, TimeUnit.SECONDS)
        .connectTimeout(5 , TimeUnit.SECONDS)
        .callTimeout(5 , TimeUnit.SECONDS)
        .writeTimeout(5 , TimeUnit.SECONDS)
        .build()


    @Provides
    @Singleton
    fun repository(api: ApiInterface) : Repository = Repository(api)




}